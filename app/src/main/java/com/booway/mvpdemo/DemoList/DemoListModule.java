package com.booway.mvpdemo.DemoList;

import android.support.annotation.Nullable;

import com.booway.mvpdemo.BookList.BookListContract;
import com.booway.mvpdemo.DemoDetails.DemoDetailsContract;
import com.booway.mvpdemo.DemoDetails.DemoDetailsFragment;
import com.booway.mvpdemo.DemoDetails.DemoDetailsPresenter;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
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

    @Provides
    @ActivityScoped
    @Nullable
    static String provideDemoId(DemoListActivity activity) {
        return activity.getIntent().getStringExtra(DemoListFragment.ARGUMENT_EDIT_DEMO_ID);
    }
}
