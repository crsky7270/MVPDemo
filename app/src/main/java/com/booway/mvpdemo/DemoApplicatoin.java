package com.booway.mvpdemo;

import android.content.Context;
import android.os.Handler;

import com.booway.mvpdemo.data.DemoRespository;
import com.booway.mvpdemo.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by wandun on 2018/11/23.
 */

public class DemoApplicatoin extends DaggerApplication {
    public static Context context;

    @Inject
    DemoRespository mDemoRespository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
