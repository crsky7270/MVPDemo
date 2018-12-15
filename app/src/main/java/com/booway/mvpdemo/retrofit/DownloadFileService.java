package com.booway.mvpdemo.retrofit;

import android.text.TextUtils;

import com.booway.mvpdemo.utils.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创建人：万吨
 * 创建时间：20181214
 * 描述：下载请求服务类，包含默认下载监听器
 */

public class DownloadFileService {

    private static int DEFAULT_TIMEOUT = 15;

    public static DownloadFileAPI createDownloadFileService(final String token, DownloadListener listener) {
        if (listener == null)
            listener = DefaultDownloadListener.getDefaultDownloadListener();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        //处理服务端没有返回body的情况
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://192.168.2.136:8080");

        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(listener, token);

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
