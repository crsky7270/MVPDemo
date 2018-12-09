package com.booway.mvpdemo.switchdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;
import com.booway.mvpdemo.utils.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by wandun on 2018/12/7.
 */
@ActivityScoped
public class SwitchDemoFragment extends DaggerFragment implements SwitchDemoContract.View {

    private Unbinder mUnbinder;

    public final String TAG = "RXJAVA2";

    @Inject
    public SwitchDemoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onComplete();
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d(TAG, "OnSubscribe");
            }

            @Override
            public void onNext(Integer i) {
                LogUtils.d(TAG, "OnNext:" + i.toString());
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_switch_demo, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        return root;
    }
}
