package com.lc.docdeal.controller;


import com.alibaba.fastjson.JSON;
import com.lc.docdeal.bean.Document;
import com.lc.docdeal.bean.DocumentEditCallback;
import com.lc.docdeal.bean.DocumentEditCallbackResponse;
import com.lc.docdeal.bean.FileUpload;
import com.lc.docdeal.constant.DocumentConstants;
import com.lc.docdeal.constant.DocumentStatus;
import com.lc.docdeal.constant.ErrorCodeEnum;
import com.lc.docdeal.exception.DocumentException;
import com.lc.docdeal.service.DocumentService;
import com.lc.docdeal.service.FileUploadService;
import com.lc.docdeal.utils.FileUtil;
import com.lc.docdeal.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
@Slf4j
@Controller
public class FileController {
    @Value("${files.savePath}")
    private String filePath;
    @Value("${files.docservice.url.site}")
    private String officeUrl;
    @Value("${files.docservice.url.command}")
    private String officeCommand;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FileUploadService uploadService;
    @Autowired
    RestTemplate restTemplate;
    @ResponseBody
    @PostMapping(value = "upload")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        if (file.isEmpty()) {
            throw new Exception("上传文件不能为空");
        }
        FileUpload upload = new FileUpload();
        String fileName = file.getOriginalFilename();
//        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
//            throw new Exception("请上传Excel文件");
//        }
        //更新保存文件信息到数据库
        FileUtil.saveFile(file.getInputStream(), filePath + file.getOriginalFilename());
//        System.out.println(fileName);
        upload.setUpload_date(new Date());
        System.out.println(".".indexOf(fileName));
        System.out.println(fileName.length());
        upload.setFile_type(fileName.substring(fileName.indexOf(".")));
        upload.setFile_path(filePath);
        upload.setFile_name(file.getOriginalFilename());
        upload.setFile_size(file.getSize());
        uploadService.save(upload);
        //操作人
//        String operator=request.getAttribute(StrUtil.USER_WORKNUMBER).toString();
//        xxxService.saveUploadCkdExecl(file,operator);

        return new ResponseEntity<Object>("上传成功", HttpStatus.OK);

    }

    /**
     * \
     * 查询所有上传文档信息接口
     *
     * @return
     */
    @GetMapping("/filelist")
    public ResponseEntity<Object> listFile() {

        return new ResponseEntity<Object>(uploadService.list(), HttpStatus.OK);
    }

//    public ResponseEntity<Object>  rview(){
//
//    }

    /**
     * 下载文档接口
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileUtil.downLoadFile(name,filePath,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/edit")
    public String editDocFile(@RequestParam String name,String userName,String userId,Model model) {
        String path = filePath+name;
//        String name = "cc.docx";
        Document document = documentService.getDocument(documentService.buildDocument(path, name));
        model.addAttribute("document", document);
        // 如果该格式不支持编辑，则返回预览页面
        if (!documentService.canEdit(document)) {
            return "/demo";
        }
        model.addAttribute("documentEditParam", documentService.buildDocumentEditParam(userId, userName,name));
        return "/editor";
    }

    /**
     * 编辑文档时回调接口
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/callback")
    public void saveDocumentFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //处理编辑回调逻辑
        callBack(request, response);

    }

    /**
     *
     * @return
     */
    @GetMapping("/editStatus")
    public  ResponseEntity<Object> getDoucmentEditStatus(String name) throws ParseException {
        String url = officeUrl+officeCommand;
        Map<String,String>  map = new HashMap<String,String>();
        map.put("c", "forcesave");
        String docFileMd5 = Md5Utils.getFileMd5(new File(filePath+name));
        if (StringUtils.isBlank(docFileMd5)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_MD5_ERROR);
        }
        String pathShortMd5 = Md5Utils.md5(filePath + name);
        String nameShortMd5 = Md5Utils.md5(name);
        Hashids hashids = new Hashids(DocumentConstants.HASH_KEY);
        // (将路径字符串短md5值 + 名称字符串短md5值) ==> 再转成短id形式 ==> 作为文档的key（暂且认为是不会重复的）
        String key = hashids.encodeHex(String.format("%s%s%s", docFileMd5,pathShortMd5, nameShortMd5));
        map.put("key", key);
        map.put("userdata", "sample userdata");
        JSONObject obj = (JSONObject) new JSONParser().parse(FileUtil.editStatus(url, JSON.toJSONString(map)));
        return new ResponseEntity<Object>(obj, HttpStatus.OK);

    }
    /**
     * 处理在线编辑文档的逻辑
     * @param request
     * @param response
     * @throws IOException
     */
    private void callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = null;
        JSONObject jsonObj = null;
        System.out.println("===saveeditedfile------------");

        try {
            writer = response.getWriter();
            Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            jsonObj = (JSONObject) new JSONParser().parse(body);
            System.out.println(jsonObj);
            System.out.println("===saveeditedfile:" + jsonObj.get("status"));
	            /*
	                0 - no document with the key identifier could be found,
	                1 - document is being edited,
	                2 - document is ready for saving,
	                3 - document saving error has occurred,
	                4 - document is closed with no changes,
	                6 - document is being edited, but the current document state is saved,
	                7 - error has occurred while force saving the document.
	             * */
            if ((long) jsonObj.get("status") == 2) {
                FileUtil.callBackSaveDocument(jsonObj,filePath,request, response);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*组件
         * status = 1，我们给onlyoffice的服务返回{"error":"0"}的信息，这样onlyoffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
         * 在线编辑还没有关闭，前端有人下载文档时，强制保存最新内容  当status 是6时说明有人在编辑时下载文档
         * */
        System.out.println(jsonObj.get("status"));
        if ((long) jsonObj.get("status") == 6) {
            //处理当文档正在编辑为关闭时，下载文档
            if (((String)jsonObj.get("userdata")).equals("sample userdata")){
                FileUtil.callBackSaveDocument(jsonObj,filePath,request, response);
            }

            System.out.println("====保存失败:");
            writer.write("{\"error\":1}");
        } else {
            //执行删除编辑时下载保存的文件:
            FileUtil.deleteTempFile(filePath,request.getParameter("fileName"));
            writer.write("{\"error\":0}");
        }
    }

}
