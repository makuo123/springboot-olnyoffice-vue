package com.lc.docdeal.bean;
import lombok.Builder;
import lombok.Data;

/**
 * 文档编辑时参数 实体
 * @author: zhangcx
 * @date: 2019/8/26 14:50
 */
@Data
@Builder
public class DocumentEditParam {
    /** 当前打开编辑页面的用户信息 */
    private UserBean user;
    /** onlyoffice在编辑时请求的回调地址,必选项 */
    private String callbackUrl;

    @Data
    @Builder
    public static class UserBean {
        /** 用户id */
        private String id;
        /** 用户姓名 */
        private String name;
    }
}
