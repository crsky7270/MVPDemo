package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.Relation;
import com.booway.mvpdemo.data.entities.Test;

/**
 * Created by wandun on 2018/11/29.
 */

@Database(entities = {Demo.class, Relation.class, Book.class}, version = 8, exportSchema = false)
public abstract class MVPDatabase extends RoomDatabase {

    public abstract DemoDao mDemoDao();

    public abstract BookDao mBookDao();

}
