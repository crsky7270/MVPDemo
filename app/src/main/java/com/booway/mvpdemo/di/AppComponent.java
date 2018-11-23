package com.booway.mvpdemo.di;

import android.app.Application;

import com.booway.mvpdemo.DemoApplicatoin;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by wandun on 2018/11/23.
 */

@Singleton
@Component(modules = {ActivityBindingModule.class,
        ApplicationModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DemoApplicatoin> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
