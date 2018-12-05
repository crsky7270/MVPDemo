package com.booway.mvpdemo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by wandun on 2018/12/4.
 */

@Entity(tableName = "Test")
public final class Test {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    @NonNull
    @ColumnInfo(name = "demo_id")
    private String mDid;

    @Nullable
    @ColumnInfo(name = "name")
    private String mName;

    public Test(@NonNull String id, @Nullable String did, @NonNull String name) {
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
