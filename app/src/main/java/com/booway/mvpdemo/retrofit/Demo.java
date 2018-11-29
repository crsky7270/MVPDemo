package com.booway.mvpdemo.retrofit;

import android.provider.Contacts;

import java.io.Serializable;

/**
 * Created by wandun on 2018/11/26.
 */

public class Demo implements Serializable {
    public int Id;
    public String Name;
    public Demo() {
        super();
    }

    public Demo(int id, String name) {
        this.Id = id;
        this.Name = name;
    }


}
