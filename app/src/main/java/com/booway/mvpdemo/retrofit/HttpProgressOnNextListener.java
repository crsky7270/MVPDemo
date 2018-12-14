package com.booway.mvpdemo.retrofit;

/**
 * Created by wandun on 2018/12/14.
 */

public abstract class HttpProgressOnNextListener<T> {

    public abstract void onNext(T t);

    public abstract void onStart();

    public abstract void onComplete();

    public abstract void updateProgress(long readLength,long countLength);

    public void onError(Throwable ex){

    }

    public void onPause(){

    }

    public void onStop(){

    }
}
