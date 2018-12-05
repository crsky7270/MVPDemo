package com.booway.mvpdemo.DemoList;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuPresenter;

import com.booway.mvpdemo.data.BookRespository;
import com.booway.mvpdemo.data.DemoRespository;
import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.data.entities.InnerJoinResult;
import com.booway.mvpdemo.data.source.remote.DemoRemoteDataSource;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.utils.AppExecutors;
import com.booway.mvpdemo.utils.DiskIOThreadExecutor;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wandun on 2018/11/23.
 */
@ActivityScoped
final class DemoListPresenter implements DemoListContract.Presenter {

    @Nullable
    private DemoListContract.View mView;

    private DemoRespository mDemoRespository;

    private BookRespository mBookRespository;

//    @Nullable
//    private String mId;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public DemoListPresenter(DemoRespository respository, BookRespository bookRespository) {
        this.mDemoRespository = respository;
        mCompositeDisposable = new CompositeDisposable();
        mBookRespository = bookRespository;
    }

    @Override
    public void takeView(DemoListContract.View view) {
        this.mView = view;
//        getDemoList(false);
    }

    @Override
    public void dropView() {
        mView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void getDemoList(boolean forceUpdate) {
        mCompositeDisposable.add(
                mDemoRespository.getDemos()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                lst -> {
                                    mView.showDemoList(lst);
                                },
                                throwable -> {
                                    mView.showResult("get list faield,ex:" + throwable.getMessage());
                                })

        );
    }

    @Override
    public void getDemo(String id) {
        mCompositeDisposable.add(mDemoRespository
                .getDemo(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        demo -> {
                            mView.showDemo(demo);
                        },
                        //onError
                        throwable -> {
                            mView.showResult(throwable.getMessage());
                        }));
    }

    @Override
    public void saveDemo(Demo demo) {
        try {
            mDemoRespository.saveDemo(demo);
            mView.showResult("save success!!!");
        } catch (Exception ex) {
            mView.showResult(ex.getMessage());
        }
    }

    @Override
    public void saveBook(Book book) {
        try {
            mBookRespository.saveBook(book);
        } catch (Exception ex) {
            mView.showResult("save book failed:" + ex.getMessage());
        }

    }

    @Override
    public void getUnionList() {
        try {
            mDemoRespository.getRelationFromDemo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {

                    });
        } catch (Exception ex) {
            mView.showResult(ex.getMessage());
        }
    }
}
