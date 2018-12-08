package com.booway.mvpdemo.switchdemo;

import com.booway.mvpdemo.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by wandun on 2018/12/7.
 */
@Module
public abstract class SwitchDemoModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SwitchDemoFragment mSwitchDemoFragment();

}
