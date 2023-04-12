package com.lc.docdeal.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * onlyoffice定义的文档对象
 * @author: zhangcx
 * @date: 2019/8/7 16:30
 */
@ApiModel("文档实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document implements Serializable {
    /** 【必需】文件唯一标识 */
    @ApiModelProperty(value = "文档 key", example="xYz123")
    private String key;
    /** 【必需】文档名称 */
    @ApiModelProperty(value = "文档标题", example="test.doc")
    private String title;
    /** 【必需】文档后缀 */
    @ApiModelProperty(value = "文档类型", example="doc")
    private String fileType;
    /** mimeType 应该先校验文件是否可以打开(非api必须字段) */
    //private String mimeType;
    /** 文件实体在服务器硬盘存储位置 */
    @ApiModelProperty(value = "文档物理存储位置", example="/temp/test.doc")
    private String storage;
    /** 【必需】文件实体下载地址 */
    @ApiModelProperty(value = "文档获取url", example="http://192.168.0.58:20053/api/file/xYz123")
    private String url;
    /** 打开文件预览/编辑的链接 */
    //private String refrence;

    /** 文档打开方式 {@link OpenModeEnum} */
    //private String mode;

}
