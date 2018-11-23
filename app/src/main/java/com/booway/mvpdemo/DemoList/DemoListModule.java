package com.booway.mvpdemo.DemoList;

import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by wandun on 2018/11/23.
 */

@Module
public abstract class DemoListModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DemoListFragment mDemoListFragment();

    @ActivityScoped
    @Binds
    abstract DemoListContract.Presenter demoListPresenter(DemoListPresenter presenter);
}
