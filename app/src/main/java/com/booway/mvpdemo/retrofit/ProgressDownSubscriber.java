package com.booway.mvpdemo.retrofit;

import com.google.common.eventbus.Subscribe;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;

/**
 * Created by wandun on 2018/12/14.
 */

public class ProgressDownSubscriber{

//        <T> extends Subscriber<T> implements DownloadResponseBody.DownloadProgressListener {
//
//    private WeakReference<HttpProgressOnNextListener> mSubscriberOnNextListener;
//
//    private DownInfo downInfo;
//
//    public ProgressDownSubscriber(DownInfo downInfo) {
//        this.mSubscriberOnNextListener = new WeakReference<>(downInfo.getListener());
//        this.downInfo = downInfo;
//    }
//
//
//
//    @Override
//    public void update(long read, long count, boolean done) {
//
//    }
//
//    @Override
//    public void onSubscribe(Subscription s) {
//
//    }
//
//    @Override
//    public void onNext(T t) {
//
//    }
//
//    @Override
//    public void onError(Throwable t) {
//
//    }
//
//    @Override
//    public void onComplete() {
//
//    }
}
