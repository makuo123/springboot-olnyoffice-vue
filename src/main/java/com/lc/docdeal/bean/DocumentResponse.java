package com.lc.docdeal.bean;


import com.lc.docdeal.constant.ErrorCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文档响应实体类
 * @author: zhangcx
 * @date: 2019/8/12 13:29
 */
@Data
@ApiModel("文档响应实体")
public class DocumentResponse<T> implements Serializable {
    @ApiModelProperty(value = "错误代码（参考：ErrorCodeEnum）", example="\"0\"")
    private String code;
    @ApiModelProperty("错误信息")
    private String msg;
    @ApiModelProperty("返回的对象")
    private T data;
    @ApiModelProperty("返回的列表")
    private List<T> dataList;

    private DocumentResponse<T> code(String code){
        this.code = code;
        return this;
    }

    private DocumentResponse<T> msg(String msg){
        this.msg = msg;
        return this;
    }

    private DocumentResponse<T> data(T data){
        this.data = data;
        return this;
    }

    private DocumentResponse<T> dataList(List<T> dataList){
        this.dataList = dataList;
        return this;
    }

    public static <T>DocumentResponse<T> success(T data){
        DocumentResponse<T> dr = new DocumentResponse<>();
        return dr.code(ErrorCodeEnum.SUCCESS.getCode()).msg(ErrorCodeEnum.SUCCESS.getMsg()).data(data);
    }

    public static <T>DocumentResponse<T> success(List<T> dataList){
        DocumentResponse<T> dr = new DocumentResponse<>();
        return dr.code(ErrorCodeEnum.SUCCESS.getCode()).msg(ErrorCodeEnum.SUCCESS.getMsg()).dataList(dataList);
    }

    public static DocumentResponse failue(ErrorCodeEnum errorCodeEnum) {
        DocumentResponse dr = new DocumentResponse<>();
        return dr.code(errorCodeEnum.getCode()).msg(errorCodeEnum.getMsg());
    }
}
