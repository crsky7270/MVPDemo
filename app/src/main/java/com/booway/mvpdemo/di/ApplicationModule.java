package com.booway.mvpdemo.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by wandun on 2018/11/23.
 */

@Module
public abstract class ApplicationModule {

    @Binds
    abstract Context bindContext(Application application);
}
