package com.booway.mvpdemo.switchdemo;

import com.booway.mvpdemo.BasePresenter;
import com.booway.mvpdemo.BaseView;

/**
 * Created by wandun on 2018/12/7.
 */

public interface SwitchDemoContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
