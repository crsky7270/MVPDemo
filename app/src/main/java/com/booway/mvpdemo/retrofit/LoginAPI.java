package com.booway.mvpdemo.retrofit;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：${DESC}
 */

public interface LoginAPI {

    @POST("/ZNXJ/ZNXJ_Main/rest/user/v1/login")
    Observable<JsonObject> login(@Body User user);
}
