package com.booway.mvpdemo.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.net.URLDecoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;

import static java.lang.String.format;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：${DESC}
 */

public class LoginService {
    public LoginService() {

    }

    public static LoginAPI createLoginService(final String token) {
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
                                    })
                            .addInterceptor(chain -> {
                                Response response = chain.proceed(chain.request());
                                String msg = response.header("message");
                                String msgDecode = URLDecoder.decode(msg,"UTF-8");
                                String state = response.header("status");
                                return response.newBuilder().build();
                            })
                            .build();
            builder.client(client);
        }
        return builder.build().create(LoginAPI.class);
    }
}
