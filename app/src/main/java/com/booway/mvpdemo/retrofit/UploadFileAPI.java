package com.booway.mvpdemo.retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by wandun on 2018/12/13.
 */

public interface UploadFileAPI {

//    @Multipart
//    @POST("/ZNXJ/ZNXJ_Main/rest/task/v1/submitTask")
//    Observable<String> uploadFile(@Part MultipartBody.Part file);

    @POST("/ZNXJ/ZNXJ_Main/rest/task/v1/submitTask")
    Observable<String> uploadFile(@Body RequestBody file);
}
