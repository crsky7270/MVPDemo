package com.booway.mvpdemo;

import com.booway.mvpdemo.data.DemoRespository;
import com.booway.mvpdemo.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by wandun on 2018/11/23.
 */

public class DemoApplicatoin extends DaggerApplication {
    @Inject
    DemoRespository mDemoRespository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
