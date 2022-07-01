package com.llw.study.api;

import com.llw.study.network.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    /**
     * 忘记密码
     */
    @POST("/test/base64")
    Observable<BaseResponse> base64(@Body RequestBody body);
}
