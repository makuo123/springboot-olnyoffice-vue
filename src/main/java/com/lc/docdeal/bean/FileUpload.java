package com.lc.docdeal.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lc.docdeal.bean.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Accessors(chain = true)
@TableName("file_info")
public class FileUpload  extends BaseEntity {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    @TableField("file_name")
    private String file_name;
    @TableField(value = "file_size")
    private Long file_size;
    @TableField(value = "file_type")
    private String file_type;
    @TableField(value = "file_path")
    private String file_path;
    @TableField(value = "upload_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date upload_date;
}
