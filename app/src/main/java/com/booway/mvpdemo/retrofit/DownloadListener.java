package com.booway.mvpdemo.retrofit;

/**
 * 创建人：万吨
 * 创建时间：20181216
 * 描述：下载监听接口类
 */
public interface DownloadListener {

    void onStart();

    void onProgress(int progress);

    void onCompleted();

    void onError(String msg);
}