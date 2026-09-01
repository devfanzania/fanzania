package com.yorker.fanzania.views.screens.tournament.kyc.bankdetails;

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

public class BankDetailsFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context mContext;

    public BankDetailsFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void BankDetails(JSONObject jsonObject);
        void OnUpdateDetails(JSONObject jsonObject);
        void OnFailed(Boolean b, String msg);
    }

    public void fetchDetails() {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchBankDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        activitycallback.BankDetails(jsonObject);
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

    public void updateDetails(String bankName, String ifsc, String accountNumber) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_BANK_NAME, bankName);
        map.put(Constants.TAG_IFSC, ifsc);
        map.put(Constants.TAG_ACCOUNT_NUMBER, accountNumber);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.updateBankDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
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
                activitycallback.OnFailed(true, t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }
}
