package com.lc.docdeal.constant;

/**
 * 文档打开方式 枚举
 * @author: zhangcx
 * @date: 2019/8/7 16:41
 */
public enum OpenModeEnum {
    /** 拒绝打开(无法打开、不支持类型等) */
    REFUSE(0),
    /** 预览 */
    VIEW(1),
    /** 编辑 */
    EDIT(2);

    private int id;

    OpenModeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
