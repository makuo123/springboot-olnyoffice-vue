package com.lc.docdeal.service.impl;

import com.alibaba.fastjson.JSON;
import com.lc.docdeal.bean.Document;
import com.lc.docdeal.bean.DocumentEditCallback;
import com.lc.docdeal.bean.DocumentEditCallbackResponse;
import com.lc.docdeal.bean.DocumentEditParam;
import com.lc.docdeal.constant.DocumentConstants;
import com.lc.docdeal.constant.DocumentStatus;
import com.lc.docdeal.constant.ErrorCodeEnum;
import com.lc.docdeal.exception.DocumentException;
import com.lc.docdeal.service.DocumentService;

import com.lc.docdeal.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文档相关业务方法
 *
 * @author: zhangcx
 * @date: @date: 2019/8/7 16:30
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    Environment environment;
    @Value("${document.server.host}")
    private String serverHost ;
    /**
     * 大小限制，默认10M
     */
    @Value("${document.file-size.limit:10485760}")
    private Long docFileSizeLimit;
    @Value("${files.docservice.url.site}")
    private String documentServerHost;
    @Value("${files.docservice.url.api}")
    private String documentServerApiJs;
   // @Autowired
    //private DocumentCacheService cacheService;

    @Override
    public String buildDocument(String filePath, String fileName) {
        if (StringUtils.isBlank(filePath)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NOT_EXISTS);
        }
        filePath = FilenameUtils.normalize(filePath);
        String fileType = StringUtils.lowerCase(FilenameUtils.getExtension(filePath));
        if (StringUtils.isBlank(fileType)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NO_EXTENSION);
        }
        // 如果指定了文件名，则需要校验和实体文件格式是否一致
        if (StringUtils.isNotBlank(fileName) && !fileType.equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_EXTENSION_NOT_MATCH);
        }
        File docFile = new File(filePath);
        // 校验文件实体
        preFileCheck(docFile);
        fileName = StringUtils.isNotBlank(fileName) ? fileName : docFile.getName();
        String fileKey = this.fileKey(docFile, fileName);
        Document document = Document.builder()
                                .fileType(fileType)
                                .title(fileName)
                                .storage(filePath)
                                .build();
        boolean cached = false;
//        try {
//            cached = cacheService.put(fileKey, document);
//        } catch (Exception e) {
//            log.error("$$$ 缓存失败~~", e);
//        }
//        if (!cached) {
//            throw new DocumentException(ErrorCodeEnum.DOC_CACHE_ERROR);
//        }
        document.setKey(fileKey);
        return JSON.toJSONString(document);
    }

    @Override
    public Document getDocument(String documentKey) {
        Document doc = null;

        try {
            doc = JSON.parseObject(documentKey,Document.class);
        } catch (Exception e) {
            log.error("$$$ 获取缓存失败~~", e);
        }
        if (doc == null) {
            throw new DocumentException(ErrorCodeEnum.DOC_CACHE_NOT_EXISTS);
        }
        // 从缓存中取出后，再绑定非必需缓存字段（节省缓存大小）
//        doc.setKey(documentKey);
        doc.setUrl(fileUrl(doc.getTitle()));
        if (log.isDebugEnabled()) {
            log.info(doc.toString());
        }
        return doc;
    }

    /**
     * 计算文件key值: 文件md5值+路径的短md5值+名称的短md5值
     * @param docFile
     * @param name 生成协作时文档的docuemnt.key的值
     * @return
     */
    public String fileKey(File docFile, String name) {
        String docFileMd5 = Md5Utils.getFileMd5(docFile);
        if (StringUtils.isBlank(docFileMd5)) {
            log.error("$$$ 构建文件信息失败！计算文件 md5 失败！");
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_MD5_ERROR);
        }
        String pathShortMd5 = Md5Utils.md5(docFile.getAbsolutePath());
        String nameShortMd5 = Md5Utils.md5(name);
        Hashids hashids = new Hashids(DocumentConstants.HASH_KEY);
        // (将路径字符串短md5值 + 名称字符串短md5值) ==> 再转成短id形式 ==> 作为文档的key（暂且认为是不会重复的）
        String key = hashids.encodeHex(String.format("%s%s%s", docFileMd5,pathShortMd5, nameShortMd5));
        if (StringUtils.isBlank(key)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_KEY_ERROR);
        }
        return key;
    }

    /**
     * 文件key值
     * @param fileType
     * @param docCrc32
     * @return
     */
    private String fileKey(String fileType, String docCrc32) {
        return String.format("%s_%s", fileType, docCrc32);
    }

    /**
     * 文件url地址
     * @param
     * @return
     */
    private String fileUrl(String filename) {
        return String.format(DocumentConstants.OFFICE_API_DOC_FILE, getServerHost(), "?name="+filename);
//        return  "http://192.168.0.58:20053/download?name="+filename;
    }

    /**
     * 根据文档信息下载文档文件
     * @param documentKey
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void downloadDocumentFile(String documentKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Document doc = this.getDocument(documentKey);
        File file = new File(doc.getStorage());
        try (InputStream reader = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[(int) FileUtils.ONE_KB];
            int len = 0;
            //response.setContentType(mimeType(file));
            while ((len = reader.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("下载失败！读取文件[" + doc.getStorage() + "]报错~~", e);
        }
    }

    @Override
    public DocumentEditParam buildDocumentEditParam(String userId, String userName,String fileName) {
        return DocumentEditParam.builder()
                .callbackUrl(callbackUrl(fileName))
                .user(DocumentEditParam.UserBean.builder()
                        .id(userId)
                        .name(userName)
                        .build())
                .build();
    }

    private String callbackUrl(String fileName) {
        String format = String.format(DocumentConstants.OFFICE_API_CALLBACK, getServerHost());
        format=format+"?fileName="+fileName;
        return format;
    }

    /**
     * 上传文档实体文件
     * @param documentKey
     * @param downloadUrl
     * @throws IOException
     */
    @Override
    public boolean saveDocumentFile(String documentKey, String downloadUrl) {
        if (log.isInfoEnabled()) {
            log.info(downloadUrl);
        }
        // TODO 默认覆盖源文件，如果调用者指定，则存到临时目录？
        boolean isCover = true;
        Document doc = this.getDocument(documentKey);
        String saveFilePath = doc.getStorage();
//        if (!isCover) {
//            String baseDir = environment.getProperty("java.io.tmpdir");
//            saveFilePath = String.format("%s/office-api/%s/%s.%s", baseDir, documentKey, System.currentTimeMillis(), doc.getFileType());
//        }
        File saveFile = new File(saveFilePath);
        boolean success = false;
        try {
            FileUtils.copyURLToFile(new URL(downloadUrl), saveFile);
            if (saveFile.exists() && saveFile.length() > 0) {
                success = true;
            }
        } catch (IOException e) {
            log.error("$$$ 保存文档失败！", e);
        }
        return success;
        //TODO 编辑成功后，应该删除之前的编辑状态缓存
    }

    @Override
    public Object getServerHost() {
        if (StringUtils.startsWith(serverHost, DocumentConstants.HTTP_SCHEME)) {
            return serverHost;
        }
        return String.format("http://%s", serverHost);
    }

    @Override
    public boolean canEdit(Document document) {
        if (ArrayUtils.contains(DocumentConstants.FILE_TYPE_UNSUPPORT_EDIT, document.getFileType())) {
            return false;
        }
        return true;
    }

    /**
     * 获取文档信息api地址
     * @param docId
     * @return
     */
    private String docInfoUrl(String docId) {
        return String.format(DocumentConstants.OFFICE_API_DOC, getServerHost(), docId);
    }

    /**
     * 获取文件的 mimetype
     * @param file
     * @deprecated
     * @return
     */
    @Deprecated
    private String mimeType(File file) {
        try {
            return Files.probeContentType(Paths.get(file.toURI()));
        } catch (IOException e) {
            log.error("$$$ 获取文件mimeType错误！", e);
        }
        return null;
    }

    /**
     * 先校验文档文件
     * @param docFile
     * @return
     */
    private void preFileCheck(File docFile) {
        if (log.isDebugEnabled()) {
            log.debug("### 开始校验文档：[{}]", docFile.getAbsolutePath());
        }
        if (docFile == null || !docFile.exists()) {
            log.error("$$$ 目标文档不存在，无法打开！");
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NOT_EXISTS);
        }
        if (docFile.isDirectory() || docFile.length() <= 0) {
            log.error("$$$ 目标文档[{}]是目录或空文件，无法打开！", docFile.getAbsolutePath());
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_EMPTY);
        }
        if (!docFile.canRead()) {
            log.error("$$$ 目标文档[{}]不可读，无法打开！", docFile.getAbsolutePath());
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_UNREADABLE);
        }
        if (docFile.length() > docFileSizeLimit) {
            log.error("$$$ 目标文档大小超过限制({}B > {}B)，无法打开！", docFile.length(), docFileSizeLimit);
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_OVERSIZE);
        }
        String ext = StringUtils.lowerCase(FilenameUtils.getExtension(docFile.getName()));
        if (!ArrayUtils.contains(DocumentConstants.FILE_TYPE_SUPPORT_VIEW, ext)) {
            log.error("$$$ 目标文档格式[{}]不正确，无法打开！（只支持：{}）",
                    ext, StringUtils.join(DocumentConstants.FILE_TYPE_SUPPORT_VIEW, ","));
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_TYPE_UNSUPPORTED);
        }
    }


}
