package com.booway.mvpdemo.data.source.remote;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.DemoDataSource;
import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.data.entities.InnerJoinTest;
import com.booway.mvpdemo.data.source.local.DemoDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by wandun on 2018/11/29.
 */

public class FakeDemoRemoteDataSource implements DemoDataSource {

    @Inject
    public FakeDemoRemoteDataSource(){}

    @Override
    public Maybe<List<Demo>> getDemos() {
        return null;
    }

    @Override
    public Single<Demo> getDemo(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<Boolean> saveDemoCall(Demo demo) {
        return null;
    }

    @Override
    public void saveDemo(Demo demo) {

    }

    @Override
    public void deleteTask(@NonNull String id) {

    }

    @Override
    public Maybe<List<InnerJoinResult>> getRelationFromDemo() {
        return null;
    }

    @Override
    public Maybe<List<InnerJoinTest.innerResult>> getInnerResult() {
        return null;
    }

    @Override
    public void batchInsertDemoList(List<Demo> demos) {

    }
}
