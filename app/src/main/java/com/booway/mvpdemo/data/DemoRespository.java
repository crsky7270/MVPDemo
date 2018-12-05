package com.booway.mvpdemo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wandun on 2018/11/29.
 */

@Singleton
public class DemoRespository implements DemoDataSource {

    @NonNull
    private final DemoDataSource mDemoLocalDataSource;

    @NonNull
    private final DemoDataSource mDemoRemoteDataSource;

    @VisibleForTesting
    @Nullable
    Map<String, Demo> mCachedDemos;

    @VisibleForTesting
    boolean mCacheIsDirty = false;

    @Inject
    public DemoRespository(@Local DemoDataSource demoLocalDataSource,
                           @Remote DemoDataSource demoRemoteDataSource) {
        this.mDemoLocalDataSource = demoLocalDataSource;
        this.mDemoRemoteDataSource = demoRemoteDataSource;
    }

    @Override
    public Maybe<List<Demo>> getDemos() {
//        if (mCachedDemos != null && !mCacheIsDirty) {
//            return Flowable.fromIterable(mCachedDemos.values()).toList().toFlowable();
//        }

//        Flowable<List<Demo>> remoteDemos=getAndCacheRemoteDemos();
//        if (mCacheIsDirty) {
//            return remoteDemos;
//        }else{
//
//        }

//        Flowable<List<Demo>> localDemos = getAndCacheLocalDemos();
        return mDemoLocalDataSource.getDemos();

    }

    private Maybe<List<Demo>> getAndCacheLocalDemos() {
        return null;
//        return mDemoLocalDataSource.getDemos()
//                .flatMap(demos -> Flowable.fromIterable(demos)
//                        .doOnNext(demo -> mCachedDemos.put(demo.getId(), demo))
//                        .toList()
//                        .toFlowable());
    }

    private Flowable<List<Demo>> getAndCacheRemoteDemos() {
        return null;
//        return mDemoRemoteDataSource.getDemos()
//                .flatMap(demos -> Flowable.fromIterable(demos).doOnNext(demo -> {
//                    mDemoLocalDataSource.saveDemo(demo);
//                    mCachedDemos.put(demo.getId(), demo);
//                }).toList().toFlowable())
//                .doOnComplete(() -> mCacheIsDirty = false);
    }


    @Override
    public Single<Demo> getDemo(@NonNull String id) {
        return mDemoLocalDataSource.getDemo(id);
    }

    @Override
    public Observable<Boolean> saveDemoCall(Demo demo) {
        return mDemoLocalDataSource.saveDemoCall(demo);
    }

    @Override
    public void saveDemo(Demo demo) {
        checkNotNull(demo);
        mDemoLocalDataSource.saveDemo(demo);
    }

    @Override
    public void deleteTask(@NonNull String id) {

    }

    @Override
    public Maybe<List<InnerJoinResult>> getRelationFromDemo() {
        return mDemoLocalDataSource.getRelationFromDemo();
    }
}
