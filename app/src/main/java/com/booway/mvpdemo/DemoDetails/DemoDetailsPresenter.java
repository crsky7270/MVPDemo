package com.booway.mvpdemo.DemoDetails;

import android.support.annotation.Nullable;

import com.booway.mvpdemo.di.ActivityScoped;

import javax.inject.Inject;

/**
 * Created by wandun on 2018/12/10.
 */
@ActivityScoped
public class DemoDetailsPresenter implements DemoDetailsContract.Presenter {


    @Nullable
    private String mId;

    @Inject
    public DemoDetailsPresenter(@Nullable String id) {
        this.mId = id;
    }

    @Override
    public void takeView(DemoDetailsContract.View view) {

    }

    @Override
    public void dropView() {

    }
}
