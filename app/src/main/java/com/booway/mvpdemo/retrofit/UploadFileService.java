package com.booway.mvpdemo.retrofit;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

/**
 * Created by wandun on 2018/12/13.
 */

public class UploadFileService {

    public static UploadFileAPI createUploadFileService(final String token) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://192.168.2.136:8080");

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
        return builder.build().create(UploadFileAPI.class);
    }
}
