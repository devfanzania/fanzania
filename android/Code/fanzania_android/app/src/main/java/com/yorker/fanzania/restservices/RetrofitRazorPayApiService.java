package com.yorker.fanzania.restservices;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitRazorPayApiService {


    @POST("orders")
    Call<JsonObject> initiateOrder(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Body Map<String, Object> map);
}
