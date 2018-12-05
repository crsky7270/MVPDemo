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

@Entity(tableName = "Relation",
indices = {@Index(value = "demo_id",unique = true)})
public class Relation {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "demo_id")
    private final String demoId;

    @Nullable
    public String getId() {
        return mId;
    }

    @Nullable
    @ColumnInfo(name = "name")
    public final String mName;

    public Relation(@Nullable String name, @NonNull String demoId, @NonNull String id) {
        this.mId = id;
        this.mName = name;
        this.demoId = demoId;
    }

    @NonNull
    public String getDemoId() {
        return demoId;
    }
}
