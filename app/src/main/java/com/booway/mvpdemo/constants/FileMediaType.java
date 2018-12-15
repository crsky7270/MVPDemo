package com.booway.mvpdemo.constants;

/**
 * 创建人：wandun
 * 创建时间：2018/12/15
 * 描述：文件媒体类型，用于请求时指定媒体类型
 */

public enum FileMediaType {
    Zip("application/zip", 1),
    Json("application/json", 2);

    private String name;

    private int index;

    FileMediaType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
