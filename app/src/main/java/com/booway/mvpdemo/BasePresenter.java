package com.booway.mvpdemo;

/**
 * Created by wandun on 2018/11/23.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();
}
