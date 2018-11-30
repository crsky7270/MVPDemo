package com.booway.mvpdemo.data;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.source.local.DemoDao;
import com.booway.mvpdemo.data.source.local.DemoLocalDataSource;
import com.booway.mvpdemo.data.source.local.MVPDatabase;
import com.booway.mvpdemo.data.source.remote.DemoRemoteDataSource;
import com.booway.mvpdemo.data.source.remote.FakeDemoRemoteDataSource;
import com.booway.mvpdemo.utils.AppExecutors;
import com.booway.mvpdemo.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by wandun on 2018/11/29.
 */

@Module
abstract public class MVPRespositoryModule {

    private static final int THREAD_COUNT = 3;

    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/mvp.db";

    @Singleton
    @Binds
    @Local
    abstract DemoDataSource provideDemoLocalDataSource(DemoLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract DemoDataSource provideDemoRemoteDataSource(FakeDemoRemoteDataSource dataSource);

    @Singleton
    @Provides
    static MVPDatabase provideDb(Application context) {
        String dbpath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/download/mvp.db";
        return Room.databaseBuilder(context.getApplicationContext(),
                MVPDatabase.class, dbpath)
                .addMigrations(MIGRATION_1_2)
                .build();

    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Demo ADD COLUMN age1 INTEGER");
            database.execSQL("UPDATE Demo SET age1=18");
        }
    };

    @Singleton
    @Provides
    static DemoDao provideDemoDao(MVPDatabase db) {
        return db.mDemoDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
