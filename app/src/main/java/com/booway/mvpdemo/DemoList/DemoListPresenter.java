package com.booway.mvpdemo.DemoList;

import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by wandun on 2018/11/23.
 */

final class DemoListPresenter implements DemoListContract.Presenter {

    @Nullable
    private DemoListContract.View mView;

    @Inject
    public DemoListPresenter() {
    }

    @Override
    public void takeView(DemoListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
