package com.booway.mvpdemo.BookList;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.BookRespository;
import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.di.ActivityScoped;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by wandun on 2018/12/4.
 */

@ActivityScoped
final class BookListPresenter implements BookListContract.Presenter {
    @NonNull
    private BookRespository mBookRespository;

    @Inject
    public BookListPresenter(BookRespository respository) {
        this.mBookRespository = respository;
    }

    @Override
    public void takeView(BookListContract.View view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void saveBook(Book book) {
        this.mBookRespository.saveBook(book);
    }
}
