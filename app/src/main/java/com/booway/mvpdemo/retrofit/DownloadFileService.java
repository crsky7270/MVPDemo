package com.booway.mvpdemo.retrofit;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

/**
 * Created by wandun on 2018/12/14.
 */

public class DownloadFileService {
    private static int DEFAULT_TIMEOUT = 60;

    public static DownloadFileAPI createDownloadFileService(final String token, DownloadListener listener) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://192.168.2.136:8080");

        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(listener);

        if (!TextUtils.isEmpty(token)) {
            OkHttpClient client =
                    new OkHttpClient().newBuilder()
                            .addInterceptor(downloadInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();
            builder.client(client);
        }
        return builder.build().create(DownloadFileAPI.class);
    }
}
