package com.yorker.fanzania.views.screens.tournament.kyc.emailmobile;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmailMobileFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context mContext;

    public UpdateEmailMobileFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void KYCDetails(JSONObject jsonObject);
        void KYCUploadStatus(JSONObject jsonObject);
        void OnFailed(Boolean b, String msg);
        void OnVerificationCodeSent(JSONObject jsonObject);
        void OnVerificationCodeVerified(JSONObject jsonObject);
    }

    public void fetchDetails() {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchKYCDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.KYCDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true, t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void sendVerificationCode() {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.sendVerificationCode(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.OnVerificationCodeSent(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true, t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void verifyVerificationCode(String activationToken) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_ACTIVATION_TOKEN, activationToken);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.verifyVerificationCode(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.OnVerificationCodeVerified(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (response.errorBody()!=null){
                    activitycallback.OnFailed(true, "Invalid OTP");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true, t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void updateKYCUploadStatus(Constants.PAN_STATUS status) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_KYC_STATUS, status);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.updateKYCUploadStatus(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.KYCUploadStatus(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true, t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }
}
