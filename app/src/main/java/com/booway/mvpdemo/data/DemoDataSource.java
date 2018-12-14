package com.booway.mvpdemo.data;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.data.entities.InnerJoinTest;
import com.booway.mvpdemo.data.source.local.DemoDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by wandun on 2018/11/29.
 */

public interface DemoDataSource {

    Maybe<List<Demo>> getDemos();

    Single<Demo> getDemo(@NonNull String id);

    Observable<Boolean> saveDemoCall(Demo demo);

    void saveDemo(Demo demo);

    void deleteTask(@NonNull String id);

    Maybe<List<InnerJoinResult>> getRelationFromDemo();

    Maybe<List<InnerJoinTest.innerResult>> getInnerResult();

    void batchInsertDemoList(List<Demo> demos);

}
