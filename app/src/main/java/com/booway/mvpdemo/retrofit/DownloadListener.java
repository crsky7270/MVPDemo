package com.booway.mvpdemo.retrofit;

/**
 * Created by wandun on 2018/12/14.
 */

public interface DownloadListener {

    void onStart();

    void onProgress(int progress);

    void onCompleted();

    void onError(String msg);
}