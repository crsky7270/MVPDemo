package com.booway.mvpdemo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Created by wandun on 2018/11/29.
 */
@Entity(tableName = "Demo")
public final class Demo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "name")
    private final String mName;

    @Ignore
    public Demo(@Nullable String name) {
        this(UUID.randomUUID().toString(), name);
    }

    /**
     * @param id
     * @param name
     */

    public Demo(@NonNull String id, @Nullable String name) {
        this.mId = id;
        this.mName = name;
    }

    @Nullable
    public String getName() {
        return mName;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Demo demo = (Demo) obj;
        return Objects.equal(mId, demo.mId) &&
                Objects.equal(mName, demo.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mName);
    }

    @Override
    public String toString() {
        return "Demo,id:" + mId + ",name:" + mName;
    }
}
