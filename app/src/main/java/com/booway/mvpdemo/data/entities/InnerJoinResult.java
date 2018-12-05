package com.booway.mvpdemo.data.entities;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by wandun on 2018/12/4.
 */

public class InnerJoinResult {
    @ColumnInfo(name = "demo_id")
    private String demoId;

    private String name;

    private int age;

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
