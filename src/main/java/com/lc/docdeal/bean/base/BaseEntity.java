package com.lc.docdeal.bean.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类：用于自动生成数据库表实体的公共字段
 *
 * @author wgb
 * @date 2020/3/26 11:47
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEntity implements Serializable {


    /**
//     * 创建时间，插入数据时自动填充
//     */


//    /**
//     * 修改时间，插入、更新数据时自动填充
//     */
//    @TableField(value = "modified_time", fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime modifiedTime;
//    /**
//     * 删除状态：插入数据时自动填充
//     */
//    @TableField(value = "delete_status", fill = FieldFill.INSERT)
//    @TableLogic
//    private boolean deleteStatus;

}
