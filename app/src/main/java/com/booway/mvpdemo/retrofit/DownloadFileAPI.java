package com.booway.mvpdemo.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * Created by wandun on 2018/12/14.
 */
public interface DownloadFileAPI {
    @Streaming
    @POST("ZNXJ/ZNXJ_Main/rest/task/v1/taskList")
    Observable<ResponseBody> download(@Body DownInfo downInfo);
}
