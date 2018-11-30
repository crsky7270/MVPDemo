package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.booway.mvpdemo.data.entities.Demo;

import java.util.List;

/**
 * Created by wandun on 2018/11/29.
 */

@Dao
public interface DemoDao {

    @Query("SELECT * FROM Demo")
    List<Demo> getDemos();

    @Query("SELECT * FROM Demo WHERE id =:did")
    Demo getDemo(String did);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDemo(Demo demo);

    @Update
    void updateDemo(Demo demo);

    @Query("DELETE FROM DEMO WHERE id =:did")
    void deleteDemoById(String did);
}