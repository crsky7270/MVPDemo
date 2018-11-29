package com.booway.mvpdemo.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wandun on 2018/11/26.
 */

public interface DemoListAPI {
    @GET("/api/values")
    Observable<List<String>> getDemoList();
}
