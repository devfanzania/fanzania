package com.yorker.fanzania.views.screens.matchcontest.myallmatches;

import android.content.Context;

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

public class MyAllMatchPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public MyAllMatchPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void dailyMatches(JSONObject obj);
    }

    public void getDailyMatches(String status) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put("MatchStatus", status);
        map.put("FetchAll", "Y");
        map.put("PageIndicator", Constants.TAG_SEEALLINDICATOR);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getDailyMatches(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        activitycallback.dailyMatches(jsonObject);

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
