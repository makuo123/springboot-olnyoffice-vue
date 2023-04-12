package com.lc.docdeal.bean;

import com.lc.docdeal.bean.base.BaseEntity;

public class FileHistoryLog extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String historyFileId;
    private String fileId;
    private String lastSave; //最后保存事件
    private String notmodified; //最后不修改
    private String changesUrl;//修改记录zip包路径
    private String serverVersion; //服务器版本
    private String changes; //用户修改记录json数组
    private String actions;//共同编辑用户json数组
    private String key; //当前版本号
    private String url; //当前最新记录
    private String users; //用户数组字符串
    private String stauts;
    private String realUrl;  //最新版本文件下载路径
    private String isDel;
    private String previousKey;  //之前版本的key
    private String previousUrl; //之前版本的url

}
