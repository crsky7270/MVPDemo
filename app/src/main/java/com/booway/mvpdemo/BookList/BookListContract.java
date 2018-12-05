package com.booway.mvpdemo.BookList;

import com.booway.mvpdemo.BasePresenter;
import com.booway.mvpdemo.BaseView;
import com.booway.mvpdemo.data.entities.Book;

/**
 * Created by wandun on 2018/12/4.
 */

public interface BookListContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {
        void saveBook(Book book);
    }
}
