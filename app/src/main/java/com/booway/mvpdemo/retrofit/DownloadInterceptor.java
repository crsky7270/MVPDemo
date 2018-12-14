package com.booway.mvpdemo.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by wandun on 2018/12/14.
 */

public class DownloadInterceptor implements Interceptor {

    private DownloadListener mProgressListener;

    public DownloadInterceptor(DownloadListener listener) {
        this.mProgressListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), mProgressListener))
                .build();
    }
}
