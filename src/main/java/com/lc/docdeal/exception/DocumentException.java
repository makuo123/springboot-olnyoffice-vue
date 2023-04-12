package com.lc.docdeal.exception;


import com.lc.docdeal.constant.ErrorCodeEnum;

/**
 * 文档异常封装类
 * @author zhangcx
 */
public final class DocumentException extends RuntimeException {
    private ErrorCodeEnum errorCode;

    public DocumentException(ErrorCodeEnum errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public DocumentException(ErrorCodeEnum errorCode, Throwable t) {
        super(errorCode.getMsg(), t);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
