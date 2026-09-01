package com.yorker.fanzania.views.screens.auth.emailverification;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.GetUserData;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.shared.model.UserDetailsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationPresenter extends PresenterStub {
    private IMainView activitycallback;

    @Inject
    SharedPrefManager sharedPrefManager;

    @Inject
    GetUserData getUserData;

    public EmailVerificationPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        Context context1 = context;
    }

    public interface IMainView {

        void VerificationCode(JSONObject jsonObject);

        void setEmailVerified(JSONObject jsonObject);
    }

    public void getVerificationCode(String userID, String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("UserId", userID);
        map.put("Email", email);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getVerficationCode(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.VerificationCode(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void setEmailVerified(String userID) {
        Map<String, Object> map = new HashMap<>();
        map.put("UserId", userID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.setEmailVerified(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE,Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.setEmailVerified(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void SaveCustomer_id(String Customer_id) {
        sharedPrefManager.saveCustomer_Id(Customer_id);
    }

    public void SaveCustomerName(String name) {
        sharedPrefManager.saveCustomerName(name);
    }

    public void SaveCustomer_Email(String email) {
        sharedPrefManager.saveCustomer_Email(email);
    }

    public void SaveAuthToken(String token) {
        sharedPrefManager.saveAuthToken(token);
    }

    public UserDetailsModel getUserData(String data) {
        return getUserData.getUserData(data);
    }
}
