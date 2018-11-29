package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.booway.mvpdemo.data.entities.Demo;

/**
 * Created by wandun on 2018/11/29.
 */

@Database(entities = {Demo.class}, version = 1,exportSchema = false)
public abstract class MVPDatabase extends RoomDatabase {

    public abstract DemoDao mDemoDao();

}
