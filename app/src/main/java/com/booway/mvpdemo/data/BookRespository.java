package com.booway.mvpdemo.data;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.data.source.local.BookLocalDataSource;

import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by wandun on 2018/12/4.
 */
@Singleton
public class BookRespository implements BookDataSource {

    @NonNull
    private final BookDataSource mBookLocalDataSource;

    @Inject
    public BookRespository(@Local BookDataSource bookLocalDataSource) {
        this.mBookLocalDataSource = bookLocalDataSource;
    }

    @Override
    public Maybe<List<Book>> getBooks() {
        return mBookLocalDataSource.getBooks();
    }

    @Override
    public void saveBook(Book book) {
        mBookLocalDataSource.saveBook(book);
    }
}
