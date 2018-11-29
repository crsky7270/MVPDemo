package com.booway.mvpdemo.data.source.remote;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.DemoDataSource;
import com.booway.mvpdemo.data.Remote;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by wandun on 2018/11/29.
 */

@Singleton
public class DemoRemoteDataSource implements DemoDataSource {

    public DemoRemoteDataSource() {
    }

    @Override
    public Flowable<List<Demo>> getDemos() {
        return null;
    }

    @Override
    public Flowable<Demo> getDemo(@NonNull String id) {
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
}
