package com.booway.mvpdemo.DemoList;

import com.booway.mvpdemo.BasePresenter;
import com.booway.mvpdemo.BaseView;

/**
 * Created by wandun on 2018/11/23.
 */

public interface DemoListContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }

}
