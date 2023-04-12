package com.lc.docdeal.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 错误码枚举
 * 约定 code 为 0 表示操作成功,
 * 1 或 2 等正数表示软件错误,
 * -1, -2 等负数表示系统错误.
 * @author: zhangcx
 * @date: 2019/8/12 13:18
 */
public enum ErrorCodeEnum {
    SUCCESS("0", "success"),

    DOC_FILE_NOT_EXISTS("1001", "目标文档不存在"),
    DOC_FILE_EMPTY("1002", "目标文档是目录或空文件"),
    DOC_FILE_UNREADABLE("1003", "目标文档不可读"),
    DOC_FILE_OVERSIZE("1004", "目标文档大小超过限制"),
    DOC_FILE_TYPE_UNSUPPORTED("1005", "目标文档格式不正确"),
    DOC_FILE_MD5_ERROR("1006", "目标文档md5校验失败"),
    DOC_FILE_MIME_ERROR("1007", "目标文档MIME检查失败"),
    DOC_FILE_NO_EXTENSION("1008", "文件路径不包含扩展名"),
    DOC_FILE_EXTENSION_NOT_MATCH("1009", "文件路径和名称后缀不匹配"),
    DOC_FILE_KEY_ERROR("1010", "目标文档key计算失败"),

    DOC_CACHE_ERROR("1101", "文档信息缓存失败"),
    DOC_CACHE_NOT_EXISTS("1102", "从缓存中获取文档信息失败"),

    UNSUPPORTED_REQUEST_METHOD("1201", "不支持的请求类型"),

    SYSTEM_UNKNOWN_ERROR("-1", "系统繁忙，请稍后再试...");

    private String code;
    private String msg;

    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ErrorCodeEnum{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public boolean isSuccessful() {
        return this.code == ErrorCodeEnum.SUCCESS.getCode();
    }

    public boolean isFailed() {
        return !isSuccessful();
    }

    public static void main(String[] args) {
        System.out.println("| 代码  | 描述   |");
        System.out.println("| ---- | ---- |");
        Arrays.stream(ErrorCodeEnum.values()).forEach((ce) -> {
            System.out.println("| " + StringUtils.rightPad(ce.getCode(), 4) + " | " + ce.getMsg() + " |");
        });
    }
}
