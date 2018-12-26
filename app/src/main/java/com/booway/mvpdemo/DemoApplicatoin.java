package com.booway.mvpdemo;

import android.content.Context;
import android.os.Handler;

import com.booway.mvpdemo.data.DemoRespository;
import com.booway.mvpdemo.di.DaggerAppComponent;
import com.secneo.sdk.Helper;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by wandun on 2018/11/23.
 */

public class DemoApplicatoin extends DaggerApplication {
    public static Context context;

    private TmpApplication tmpApplication;

    @Inject
    DemoRespository mDemoRespository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        tmpApplication.onCreate();
        context = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Helper.install(DemoApplicatoin.this);
        if (tmpApplication == null) {
            tmpApplication = new TmpApplication();
            tmpApplication.setContext(this);
        }
    }
}
