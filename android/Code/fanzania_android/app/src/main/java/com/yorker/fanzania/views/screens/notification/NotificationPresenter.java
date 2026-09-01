package com.yorker.fanzania.views.screens.notification;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresenter extends PresenterStub {

    private IMainView activitycallback;

    @Inject
    SharedPrefManager sharedPrefManager;

    public NotificationPresenter(IMainView activitycallback1) {
        activitycallback = activitycallback1;
    }

    interface IMainView {
        void getNotificationsList(JSONObject jsonObject);
        void onSuccessDeleteNotification(JSONObject jsonObject);
    }

    public String getUserID() {
        return sharedPrefManager.getCustomer_Id();
    }

    public void getNotifications() {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchNotifications(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.getNotificationsList(jsonObject);

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

    public void deleteNotification(int n_id) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));
        map.put(Constants.TAG_NOTIFICATIONID, n_id);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.deleteNotification(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.onSuccessDeleteNotification(jsonObject);

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

}
