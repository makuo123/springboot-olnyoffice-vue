package com.lc.docdeal.exception;


import com.lc.docdeal.bean.DocumentResponse;
import com.lc.docdeal.constant.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class WebExceptionHandler {
    @ExceptionHandler(DocumentException.class)
    public DocumentResponse documentException(DocumentException e) {
        log.error("$$$ 文档异常~~", e);
        return DocumentResponse.failue(e.getErrorCode());
   }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public DocumentResponse requestException(HttpRequestMethodNotSupportedException e) {
        log.error("$$$ 不支持的请求类型~~", e);
        return DocumentResponse.failue(ErrorCodeEnum.UNSUPPORTED_REQUEST_METHOD);
    }

    @ExceptionHandler(Exception.class)
    public DocumentResponse unknownException(Exception e) {
        log.error("$$$ 未知异常~~", e);
        return DocumentResponse.failue(ErrorCodeEnum.SYSTEM_UNKNOWN_ERROR);
    }
}
