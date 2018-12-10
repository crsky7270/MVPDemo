package com.booway.mvpdemo.DemoDetails;

import com.booway.mvpdemo.BasePresenter;
import com.booway.mvpdemo.BaseView;

/**
 * Created by wandun on 2018/12/10.
 */

public interface DemoDetailsContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
