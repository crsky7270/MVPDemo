package com.booway.mvpdemo;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by wandun on 2018/11/23.
 */

public class DemoApplicatoin extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return null;
    }
}
