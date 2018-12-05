package com.booway.mvpdemo.di;

import com.booway.mvpdemo.BookList.BookListModule;
import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.DemoList.DemoListModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by wandun on 2018/11/23.
 */

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {DemoListModule.class, BookListModule.class})
    abstract DemoListActivity demoListActivity();
}
