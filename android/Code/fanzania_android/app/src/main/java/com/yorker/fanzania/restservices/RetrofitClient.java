package com.yorker.fanzania.restservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yorker.fanzania.applications.FanzaniaApplication;
import com.yorker.fanzania.constants.Constants;

import org.riversun.okhttp3.OkHttp3CookieHelper;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = Constants.BASE_URL;
    private static Retrofit mInstance = null;
    private static SiCookieStore2 siCookieStore;

    public static Retrofit getInstance() {
        if (mInstance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // Set your desired log level. Use Level.BODY for debugging errors.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieHandler cookieHandler = new CookieManager();
            siCookieStore = new SiCookieStore2(FanzaniaApplication.getInstance());

            CookieManager cookieManager = new CookieManager((CookieStore) siCookieStore, CookiePolicy.ACCEPT_ALL);
            cookieHandler.setDefault(cookieManager);


            OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.HOURS)
                    .connectTimeout(120, TimeUnit.HOURS)
                    .cookieJar(cookieHelper.cookieJar())
                    .addNetworkInterceptor(logging)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            // Adding Rx so the calls can be Observable, and adding a Gson converter with
            // leniency to make parsing the results simple.
            mInstance = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build();
        }
        return mInstance;
    }

}

