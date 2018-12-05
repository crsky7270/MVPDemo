package com.booway.mvpdemo.data.source.local;

import android.support.annotation.NonNull;

import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.DemoDataSource;
import com.booway.mvpdemo.data.Local;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
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
public class DemoLocalDataSource implements DemoDataSource {

    private final AppExecutors mAppExecutors;

    private final DemoDao mDemoDao;

    @Inject
    public DemoLocalDataSource(@NonNull AppExecutors executors, @NonNull DemoDao demoDao) {
        this.mAppExecutors = executors;
        this.mDemoDao = demoDao;
    }

    @Override
    public Maybe<List<Demo>> getDemos() {


//        Flowable<List<Demo>> flowable = Flowable.create(emitter -> {
//            List<Demo> demoList = mDemoDao.getDemos();
//            emitter.onNext(demoList);
////            emitter.onComplete();
//        }, BackpressureStrategy.BUFFER);
        return mDemoDao.getDemos();
    }

    @Override
    public Single<Demo> getDemo(@NonNull String id) {
//        Flowable<Demo> flowable = Flowable.create(emitter -> {
//            Demo demo = mDemoDao.getDemo(id);
//            emitter.onNext(demo);
//            emitter.onComplete();
//        }, BackpressureStrategy.BUFFER);
        return mDemoDao.getDemo(id);
    }

    @Override
    public Observable<Boolean> saveDemoCall(Demo demo) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                mDemoDao.insertDemo(demo);
            }
        });
        return observable;
    }

    @Override
    public void saveDemo(Demo demo) {
        checkNotNull(demo);
        Runnable runnable = () -> mDemoDao.insertDemo(demo);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteTask(@NonNull String id) {
        mDemoDao.deleteDemoById(id);
    }

    @Override
    public Maybe<List<InnerJoinResult>> getRelationFromDemo() {


        return mDemoDao.getRelationFromDemo();
    }
}
