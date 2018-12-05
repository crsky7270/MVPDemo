package com.booway.data.source.local;

import android.arch.persistence.room.Room;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.source.local.MVPDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by wandun on 2018/12/4.
 */
@RunWith(AndroidJUnit4.class)
public class DemoDaoTest {
    private static final Demo demo = new Demo("004", "lan", 18);

    private MVPDatabase mDatabase;

    @Before
    public void initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                MVPDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }


    @Test
    public void insertDemoAndGetById() {
        mDatabase.mDemoDao().insertDemo(demo);

        mDatabase.mDemoDao().getDemo(demo.getId())
                .subscribe(demo -> {

                });

    }


}
