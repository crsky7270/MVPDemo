package com.booway.mvpdemo.DemoDetails;

import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by wandun on 2018/12/10.
 */
@Module
public abstract class DemoDetailsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DemoDetailsFragment mDemoDetailsFragment();

    @ActivityScoped
    @Binds
    abstract DemoDetailsContract.Presenter demoDetailsPresenter(DemoDetailsPresenter presenter);
}
