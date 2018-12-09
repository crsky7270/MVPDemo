package com.booway.mvpdemo.retrofit;

import android.text.TextUtils;


import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

/**
 * Created by wandun on 2018/11/26.
 */

public class DemoListService {
    private DemoListService() {
    }

    public static DemoListAPI createDemoListService(final String token) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://192.168.31.10:9900");

        if (!TextUtils.isEmpty(token)) {
            OkHttpClient client =
                    new OkHttpClient().newBuilder()
                            .addInterceptor(
                                    chain -> {
                                        Request request = chain.request();
                                        Request newReq = request.newBuilder()
                                                .addHeader("Authorization", format("token %s", token))
                                                .build();
                                        return chain.proceed(newReq);
                                    }).build();
            builder.client(client);
        }
        return builder.build().create(DemoListAPI.class);
    }


    public static DemoListPostAPI createDemoListPostService(final String token) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://192.168.31.10:9900");
        Demo demo = new Demo();
        demo.Id = 10002;
        demo.Name = "张三";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                new Gson().toJson(demo));

        if (!TextUtils.isEmpty(token)) {
            OkHttpClient client =
                    new OkHttpClient().newBuilder()
                            .addInterceptor(
                                    chain -> {
                                        Request request = chain.request();
                                        Request newReq = request.newBuilder()
                                                .addHeader("Authorization", format("token %s", token))
                                                .post(body)
                                                .build();
                                        return chain.proceed(newReq);
                                    }).build();
            builder.client(client);
        }
        return builder.build().create(DemoListPostAPI.class);
    }


}
