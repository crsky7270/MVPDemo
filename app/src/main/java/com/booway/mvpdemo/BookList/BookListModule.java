package com.booway.mvpdemo.BookList;

import com.booway.mvpdemo.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by wandun on 2018/12/4.
 */

@Module
public abstract class BookListModule {

    @ActivityScoped
    @Binds
    abstract BookListContract.Presenter bookListPresenter(BookListPresenter presenter);
}
