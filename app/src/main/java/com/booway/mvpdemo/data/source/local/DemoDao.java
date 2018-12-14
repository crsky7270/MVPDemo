package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.data.entities.InnerJoinTest;
import com.booway.mvpdemo.utils.LogUtils;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by wandun on 2018/11/29.
 */

@Dao
public abstract class DemoDao {
    public static final String TAG = "DATA";

    @Query("SELECT * FROM Demo")
    public abstract Maybe<List<Demo>> getDemos();

    @Query("SELECT id FROM Demo")
    public abstract List<String> getDemoIds();

    @Query("SELECT * FROM Demo WHERE id =:did")
    public abstract Single<Demo> getDemo(String did);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDemo(Demo demo);

    @Update
    public abstract void updateDemo(Demo demo);

    @Query("DELETE FROM DEMO WHERE id =:did")
    public abstract void deleteDemoById(String did);

    @Query("SELECT demo_Id,Book.name,demo.age from demo INNER JOIN Book ON Demo.id = Book.demo_id")
    public abstract Maybe<List<InnerJoinResult>> getRelationFromDemo();

    @Query("SELECT Demo.name as DemoName,Book.name as BookName FROM Demo,Book WHERE Demo.id = Book.demo_id")
    public abstract Maybe<List<InnerJoinTest.innerResult>> getInnerResult();

    @Transaction
    public void batchInsertDemos(List<Demo> demoList) {
        LogUtils.d(TAG, "-- Start insert demo!");
        for (Demo demo : demoList) {

            insertDemo(demo);
        }
        LogUtils.d(TAG, "-- Insert demo Completed!");
    }

//    @Query("SELECT Demo.* FROM Demo Left Join Book ON Book.demo_id=Demo.id WHERE Book.demo_id='002'")
//    Maybe<List<Demo>> getInnerResult();


}


