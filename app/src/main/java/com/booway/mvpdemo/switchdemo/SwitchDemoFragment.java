package com.booway.mvpdemo.switchdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.DemoList.DemoListFragment;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;
import com.booway.mvpdemo.utils.LogUtils;
import com.booway.mvpdemo.utils.StringUtils;
import com.google.common.eventbus.Subscribe;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.observable.ObservableCreate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wandun on 2018/12/7.
 */
@ActivityScoped
public class SwitchDemoFragment extends DaggerFragment implements SwitchDemoContract.View {

    private Unbinder mUnbinder;

    public final String TAG = "※※※※※※※RXJAVA2※※※※※※※";

    private CompositeDisposable mCompositeDisposable;

    @OnClick(R.id.gotoDemo)
    public void gotoDemo() {
        Intent intent = new Intent(getContext(), DemoListActivity.class);
        intent.putExtra(DemoListFragment.ARGUMENT_EDIT_DEMO_ID, "001");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Inject
    public SwitchDemoFragment() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        asyncRxjava();
//        multObserver();
//        Rxjava_Map();
//        Rxjava_floatMap();
        Rxjava_Zip();
    }


    private void syncRxjava() {
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            LogUtils.d(TAG, "Start to emmiter");
            for (int i = 1; i < 8; i++) {
                e.onNext(i);
                LogUtils.d(TAG, "i:" + String.valueOf(i));

            }
            e.onComplete();
            LogUtils.d(TAG, "onComplete!!!");
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                LogUtils.d(TAG, "observer start on Subscribe");
            }

            @Override
            public void onNext(Integer i) {
                LogUtils.d(TAG, "OnNext:" + i.toString());
                if (i == 5) {
                    mDisposable.dispose();
                    Log.d(TAG, "dispose!!!");
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(TAG, "OnError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d(TAG, "OnComplete!");
            }
        });
    }

    private void asyncRxjava() {
        mCompositeDisposable.add(
                Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    LogUtils.d(TAG, "Current Thread is :" + Thread.currentThread().getName());
                    e.onNext(1);
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            LogUtils.d(TAG, "Observer Thread is:" + Thread.currentThread().getName());
                            LogUtils.d(TAG, "onNext:" + integer + "");
                        }));

    }

    private void multObserver() {
        mCompositeDisposable.add(
                Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);
                    e.onNext(4);
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(integer -> LogUtils.d(TAG, integer + ",first," +
                                Thread.currentThread().getName()))
                        .observeOn(Schedulers.newThread())
                        .doOnNext(integer -> LogUtils.d(TAG, integer + ",second," +
                                Thread.currentThread().getName()))
                        .subscribe());//integer -> LogUtils.d(TAG, "accept:" + integer));

    }

    private void Rxjava_Map() {
        mCompositeDisposable.add(
                Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    e.onNext(1);
                    e.onNext(2);
                }).map(integer -> {
                    if (integer == 1) {
                        return "one";
                    } else {
                        return "two";
                    }
                }).subscribe(s -> {
                    LogUtils.d(TAG, s);
                }));
    }

    private void Rxjava_floatMap() {
        mCompositeDisposable.add(
                Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    e.onNext(1);
                }).flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer i) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                e.onNext("aa");
                                LogUtils.d(TAG, 1 + "" + Thread.currentThread().getName());
                            }
                        });
                    }
                }).flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                e.onNext(1);
                                LogUtils.d(TAG, 2 + "" + Thread.currentThread().getName());
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                LogUtils.d(TAG, 3 + "");
                            }
                        })
        );
    }

    private void Rxjava_Zip() {
        Observable<Integer> oi = Observable.create(e -> {
            e.onNext(1);
            LogUtils.d(TAG, "one select ");
            Thread.sleep(1000);
            e.onComplete();
        });
        Observable<String> os = Observable.create(e -> {
            e.onNext("a");
            Thread.sleep(1000);
            LogUtils.d(TAG, "two select");
            e.onComplete();
        });
        Observable.zip(oi, os, (integer, s) -> {
            LogUtils.d(TAG, "cal expression");
            return s + integer;
        }).delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d(TAG, "subscrib!");
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.d(TAG, "on next:");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d(TAG, "complete!");
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_switch_demo, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        return root;
    }
}
