package com.yorker.fanzania.views.screens.tournament.subscription;

import android.content.Context;
import android.util.Log;

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

public class SubscriptionPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    SharedPrefManager sharedPrefManager;

    public SubscriptionPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {
        void OnSubscriptionDetails(JSONObject jsonObject);
        void OnUpdateDetails(JSONObject jsonObject);
        void OnFailed(Boolean b);
    }

    public void fetchDetails() {
        sharedPrefManager = SharedPrefManager.getInstance(context);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Log.d("rtyrtytfghgh","sdsd  ::: Constants.str_HEADER :: "+Constants.str_HEADER+",Constants.RETROFIT_HEADER_TYPE ::  "+Constants.RETROFIT_HEADER_TYPE+",sharedPrefManager.getCustomer_Id() :: "+sharedPrefManager.getCustomer_Id()+",map :: "+map.toString());
        Call<JsonObject> call = retrofitAipService.fetchSubscriptionDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.OnSubscriptionDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true);
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void updateDetails(int subscriptionTier, String receipt, double amount, String currency) {
        sharedPrefManager = SharedPrefManager.getInstance(context);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_SUBSCRIPTOIN_TIER, subscriptionTier);
        map.put(Constants.TAG_RECEIPT, receipt);
        map.put(Constants.TAG_AMOUNT, String.valueOf(amount));
        map.put(Constants.TAG_CURRENCY, currency);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.updateSubscriptionDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.OnUpdateDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activitycallback.OnFailed(true);
                System.out.println("error1 " + call.toString());
            }
        });
    }
}
