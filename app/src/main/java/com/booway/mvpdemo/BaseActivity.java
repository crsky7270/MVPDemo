package com.booway.mvpdemo;

import com.booway.mvpdemo.DemoDetails.DemoDetailsFragment;
import com.booway.mvpdemo.DemoList.DemoListFragment;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by wandun on 2018/12/8.
 */

public class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    protected DemoListFragment mFragment;

    @Inject
    protected DemoDetailsFragment mDemoDetailsFragment;
}
