package com.booway.mvpdemo.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 创建人：wandun
 * 创建时间：2018/12/14
 * 描述：下载监听器类
 */

public class DownloadInterceptor implements Interceptor {

    private DownloadListener mProgressListener;

    private String mToken;

    DownloadInterceptor(DownloadListener listener, @Nullable String token) {
        this.mProgressListener = listener;
        this.mToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response.newBuilder()
                .addHeader("Authorization", mToken)
                .body(new DownloadResponseBody(response.body(), mProgressListener))
                .build();
    }
}
