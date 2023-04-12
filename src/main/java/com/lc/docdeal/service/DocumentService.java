package com.lc.docdeal.service;


import com.lc.docdeal.bean.Document;
import com.lc.docdeal.bean.DocumentEditParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文档业务 接口
 * @author: zhangcx
 * @date: 2019/8/7 16:30
 */
public interface DocumentService {
    /**
     * 构建文档对象
     * @param filePath
     * @param fileName
     * @return documentKey 文档key
     */
    String buildDocument(String filePath, String fileName);

    /**
     * 从缓从中获取文档信息
     * @param documentKey
     * @return
     */
    Document getDocument(String documentKey);

    /**
     * 下载文档实体文件
     * @param documentKey
     * @param request
     * @param response
     * @throws IOException
     */
    void downloadDocumentFile(String documentKey, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 构建文档编辑参数 对象
     * @param userId
     * @param userName
     * @return
     */
    DocumentEditParam buildDocumentEditParam(String userId, String userName,String fileName);

    /**
     * 编辑后保存文档实体文件
     * @param documentKey
     * @param downloadUrl
     * @throws IOException
     */
    boolean saveDocumentFile(String documentKey, String downloadUrl) throws IOException;

    /**
     * 获取服务暴露的host(包含端口)
     * @return
     */
    Object getServerHost();

    /**
     * 文档是否支持编辑
     * @param document
     * @return
     */
    boolean canEdit(Document document);
}
