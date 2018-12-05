package com.booway.mvpdemo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by wandun on 2018/12/4.
 */

@Entity(tableName = "Book", foreignKeys = @ForeignKey(entity = Demo.class,
        parentColumns = "id", childColumns = "demo_id"),
        indices = @Index(value = {"demo_id"}, unique = true))
public final class Book {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    @Nullable
    @ColumnInfo(name = "demo_id")
    private String mDid;

    @Nullable
    @ColumnInfo(name = "name")
    private String mName;

    public Book(@Nullable String name, @NonNull String did, @NonNull String id) {
        this.mDid = did;
        this.mId = id;
        this.mName = name;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @NonNull
    public String getDid() {
        return mDid;
    }

    public void setDid(@NonNull String did) {
        mDid = did;
    }

    @Nullable
    public String getName() {
        return mName;
    }

    public void setName(@Nullable String name) {
        mName = name;
    }
}
