package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.BookDataSource;
import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wandun on 2018/12/4.
 */

@Singleton
public class BookLocalDataSource implements BookDataSource {

    private final AppExecutors mAppExecutors;

    private final BookDao mBookDao;

    @Inject
    public BookLocalDataSource(@NonNull AppExecutors executors, @NonNull BookDao bookDao) {
        mAppExecutors = executors;
        mBookDao = bookDao;
    }

    @Override
    public Maybe<List<Book>> getBooks() {
        return mBookDao.getBooks();
    }

    @Override
    public void saveBook(Book book) {
        checkNotNull(book);
        Runnable runnable = () -> mBookDao.insertBook(book);
        mAppExecutors.diskIO().execute(runnable);
    }
}
