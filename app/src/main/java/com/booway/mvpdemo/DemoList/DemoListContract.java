package com.booway.mvpdemo.DemoList;

import com.booway.mvpdemo.BasePresenter;
import com.booway.mvpdemo.BaseView;
import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.data.entities.Demo;

import java.util.List;

/**
 * Created by wandun on 2018/11/23.
 */

public interface DemoListContract {

    interface View extends BaseView<Presenter> {

        void showDemoList(List<Demo> demos);

        void showDemo(Demo demo);

        void showResult(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        void getDemoList(boolean forceUpdate);

        void getDemo(String id);

        void saveDemo(Demo demo);

        void saveBook(Book book);

        void getUnionList();

    }

}
