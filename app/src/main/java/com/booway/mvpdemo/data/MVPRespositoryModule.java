package com.booway.mvpdemo.data;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.source.local.BookDao;
import com.booway.mvpdemo.data.source.local.BookLocalDataSource;
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
    @Binds
    @Local
    abstract BookDataSource provideBookLocalDataSource(BookLocalDataSource dataSource);

    @Singleton
    @Provides
    static MVPDatabase provideDb(Application context) {
        String dbpath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/download/mvp.db";
        return Room.databaseBuilder(context.getApplicationContext(),
                MVPDatabase.class, dbpath)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                .addMigrations(MIGRATION_4_5, MIGRATION_5_6)
                .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
                .addMigrations(MIGRATION_6_7, MIGRATION_7_8)
                .build();

    }

    //修改字段
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Demo ADD COLUMN age INTEGER NOT NULL DEFAULT 18");
//            database.execSQL("UPDATE Demo SET age1=18");
        }
    };

    //创建索引
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE INDEX index_id ON Demo (id)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Relation(" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "demo_id TEXT," +
                    "name TEXT);");

        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE UNIQUE INDEX index_demoId on Relation(demo_id)");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Test(" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "demo_id TEXT," +
                    "name TEXT);");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Book(" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "demo_id TEXT," +
                    "name TEXT," +
                    "FOREIGN KEY(demo_id)" +
                    "REFERENCES Demo(id));");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE UNIQUE INDEX index_book_demoId on Book(demo_id)");
        }
    };


    @Singleton
    @Provides
    static DemoDao provideDemoDao(MVPDatabase db) {
        return db.mDemoDao();
    }

    @Singleton
    @Provides
    static BookDao provideBookDao(MVPDatabase db) {
        return db.mBookDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
