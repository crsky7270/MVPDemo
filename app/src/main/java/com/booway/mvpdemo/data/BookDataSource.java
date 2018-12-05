package com.booway.mvpdemo.data;

import com.booway.mvpdemo.data.entities.Book;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by wandun on 2018/12/4.
 */

public interface BookDataSource {

    Maybe<List<Book>> getBooks();

    void saveBook(Book book);
}
