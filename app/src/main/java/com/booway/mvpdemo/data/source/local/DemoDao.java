package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.data.entities.InnerJoinTest;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by wandun on 2018/11/29.
 */

@Dao
public interface DemoDao {

    @Query("SELECT * FROM Demo")
    Maybe<List<Demo>> getDemos();

    @Query("SELECT id FROM Demo")
    List<String> getDemoIds();

    @Query("SELECT * FROM Demo WHERE id =:did")
    Single<Demo> getDemo(String did);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDemo(Demo demo);

    @Update
    void updateDemo(Demo demo);

    @Query("DELETE FROM DEMO WHERE id =:did")
    void deleteDemoById(String did);

    @Query("SELECT demo_Id,Book.name,demo.age from demo INNER JOIN Book ON Demo.id = Book.demo_id")
    Maybe<List<InnerJoinResult>> getRelationFromDemo();

    @Query("SELECT Demo.name as DemoName,Book.name as BookName FROM Demo,Book WHERE Demo.id = Book.demo_id")
    Maybe<List<InnerJoinTest.innerResult>> getInnerResult();

//    @Query("SELECT Demo.* FROM Demo Left Join Book ON Book.demo_id=Demo.id WHERE Book.demo_id='002'")
//    Maybe<List<Demo>> getInnerResult();



}


