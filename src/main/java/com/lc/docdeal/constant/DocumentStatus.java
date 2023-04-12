package com.lc.docdeal.constant;

/**
 * 文档的状态:
 *
 0 - 找不到带有密钥标识符的文档，
 1 - 正在编辑文档，
 2 - 文件已准备好保存，
 3 - 发生了文档保存错误，
 4 - 文件关闭，没有变化，
 6 - 正在编辑文档，但保存当前文档状态，
 7 - 强制保存文档时发生错误。
 * @author: zhangcx
 * @date: 2019/8/23 14:04
 */
public enum DocumentStatus {
    NOT_FOUND(0, "找不到带有密钥标识符的文档"),
    BEING_EDITED(1, "正在编辑文档"),
    READY_FOR_SAVING(2, "文件已准备好保存"),
    SAVING_ERROR(3, "发生了文档保存错误"),
    CLOSED_WITH_NO_CHANGES(4, "文件关闭，没有变化"),
    BEING_EDITED_STATE_SAVED(6, "正在编辑文档，但保存当前文档状态"),
    FORCE_SAVING_ERROR(7, "强制保存文档时发生错误");

    private int code;
    private String msg;

    DocumentStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "DocumentStatus{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
