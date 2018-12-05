package com.booway.mvpdemo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.booway.mvpdemo.data.entities.Relation;

import java.util.List;

/**
 * Created by wandun on 2018/12/4.
 */
@Dao
public interface RelationDao {

    @Query("SELECT * FROM Relation")
    List<Relation> getRelationList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRelation();

}
