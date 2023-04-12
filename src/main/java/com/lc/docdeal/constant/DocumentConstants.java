package com.lc.docdeal.constant;

import java.time.Duration;

/**
 * @author: zhangcx
 * @date: 2019/8/7 17:23
 */
public class DocumentConstants {
    public static final String HTTP_SCHEME = "http";
    /**
     * 支持的文档类型
     */
    public static final String[] FILE_TYPE_SUPPORT_VIEW = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "pdf"};

    /**
     * 不支持编辑的类型
     */
    public static final String[] FILE_TYPE_UNSUPPORT_EDIT = {"pdf"};

    /**
     * 文档文件下载接口地址
     */
    public static final String OFFICE_API_DOC_FILE = "%s/download%s";
    /**
     * 文档信息获取地址
     */
    public static final String OFFICE_API_DOC = "%s/api/doc/%s";
    /**
     * 编辑回调地址
     */
    public static final String OFFICE_API_CALLBACK = "%s/callback";
    /**
     * 预览地址
     */
    public static final String OFFICE_VIEWER = "%s/viewer/%s";
    /**
     * 编辑地址
     */
    public static final String OFFICE_EDITOR = "%s/editor/%s";
    /**
     * 文档redis缓存前缀 格式化
     */
    public static final String DOCUMENT_REDIS_KEY_PREFIX_FORMAT = "onlyoffice:document:%s";
    /**
     * 缓存过期时间: 1天
     */
    public static final Duration CACHE_DURATION = Duration.ofDays(1);

    public static final String HASH_KEY = "lezhixing";
}
