package com.booway.mvpdemo.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * 创建人：万吨
 * 创建时间：20181216
 * 描述：Retrofit请求接口类
 */
public interface DownloadFileAPI {
    @Streaming
    @POST("ZNXJ/ZNXJ_Main/rest/task/v1/taskList")
    Observable<ResponseBody> download(@Body DownInfo downInfo);
}
