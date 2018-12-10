package com.booway.mvpdemo.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by wandun on 2018/11/27.
 */

public interface DemoListPostAPI {

    @POST("/api/values")
    Observable<String> postDemos(@Body Demo demo);

}
