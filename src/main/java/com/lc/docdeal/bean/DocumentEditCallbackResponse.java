package com.lc.docdeal.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档响应实体类
 * @author: zhangcx
 * @date: 2019/8/12 13:29
 */
@Data
@AllArgsConstructor
public class DocumentEditCallbackResponse implements Serializable {
    private int error;

    public static DocumentEditCallbackResponse success(){
        return new DocumentEditCallbackResponse(0);
    }

    public static DocumentEditCallbackResponse failue(){
        return new DocumentEditCallbackResponse(1);
    }
}
