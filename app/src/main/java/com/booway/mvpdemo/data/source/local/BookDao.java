package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.data.entities.Demo;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by wandun on 2018/12/4.
 */

@Dao
public interface BookDao {

    @Query("SELECT * FROM Book")
    Maybe<List<Book>> getBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book book);
}
