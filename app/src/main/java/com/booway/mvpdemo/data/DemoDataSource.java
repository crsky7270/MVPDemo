package com.booway.mvpdemo.data;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.entities.Demo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by wandun on 2018/11/29.
 */

public interface DemoDataSource {

    Flowable<List<Demo>> getDemos();

    Flowable<Demo> getDemo(@NonNull String id);

    Observable<Boolean> saveDemoCall(Demo demo);

    void saveDemo(Demo demo);

    void deleteTask(@NonNull String id);

}
