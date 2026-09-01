package com.yorker.fanzania.restservices.casefree;

import static com.yorker.fanzania.constants.Constants.PGClientId;

import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.views.model.casefee.ApiResponse;
import com.yorker.fanzania.views.model.casefee.OrderRequest;
import com.yorker.fanzania.views.model.casefee.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("pg/orders")
    Call<ApiResponse> createOrder(@Header(Constants.RETROFIT_HEADER) String content_type, @Header("x-client-id") String content_type1, @Header("x-client-secret") String content_type2, @Header("x-api-version") String content_type3, @Body OrderRequest body);


    @GET("pg/orders/{orderId}")
    Call<OrderResponse> getOrderDetails(@Header("x-client-id") String content_type1, @Header("x-client-secret") String content_type2, @Header("x-api-version") String content_type3, @Path("orderId") String orderId);
}