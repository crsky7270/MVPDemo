package com.booway.mvpdemo.retrofit;

import com.booway.mvpdemo.utils.LogUtils;

/**
 * 创建人：wandun
 * 创建时间：2018/12/15
 * 描述：默认下载监听器
 */

public class DefaultDownloadListener implements DownloadListener {
    private final String TAG = DefaultDownloadListener.class.getName().toUpperCase();

    private static DefaultDownloadListener mDefaultDownloadListener;

    static DefaultDownloadListener getDefaultDownloadListener() {
        if (mDefaultDownloadListener == null)
            synchronized (DefaultDownloadListener.class) {
                if (mDefaultDownloadListener == null)
                    mDefaultDownloadListener = new DefaultDownloadListener();
            }
        return mDefaultDownloadListener;
    }

    @Override
    public void onStart() {
        LogUtils.d(TAG, "Start to Download file");
    }

    @Override
    public void onProgress(int progress) {
        LogUtils.d(TAG, "Current progress:" + progress);
    }

    @Override
    public void onCompleted() {
        LogUtils.d(TAG, "Download file completed!");
    }

    @Override
    public void onError(String msg) {
        LogUtils.d(TAG, "Error:" + msg);
    }
}
