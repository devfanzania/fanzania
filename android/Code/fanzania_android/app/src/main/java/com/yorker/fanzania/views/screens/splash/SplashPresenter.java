package com.yorker.fanzania.views.screens.splash;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.shared.GetKeyHash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by innofied on 26/3/18.
 */

public class SplashPresenter extends PresenterStub {

    private IMainView activitycallback;

    @Inject
    SharedPrefManager sharedPrefManager;

    @Inject
    GetKeyHash getKeyHash;

    public SplashPresenter(IMainView activitycallback1, Context context) {
        activitycallback = activitycallback1;
        Context mContext = context;

    }

    interface IMainView {
        void getStaticURL(JSONObject jsonObject);
    }

    public void storeToken(String token) {
        //saving the token on shared preferences
        sharedPrefManager.saveDeviceTokenno(token);
    }

    public String getStoreToken() {
        //saving the token on shared preferences
        return sharedPrefManager.getDeviceToken();
    }

    public String GetKeyHash() {
        return getKeyHash.printKeyHash();
    }

    public String getUserID() {
        return sharedPrefManager.getCustomer_Id();
    }

    public String getUserEmail() {
        return sharedPrefManager.getCustomer_Email();
    }

    public void getStaticURL() {
        sharedPrefManager.setUpdateDialog(true);

        sharedPrefManager.claerTournamentId();
        sharedPrefManager.saveDialog(true);
        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getStaticURL(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))

                            activitycallback.getStaticURL(jsonObject);
                        else
                            activitycallback.getStaticURL(jsonObject);

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

    public void saveURL(String value, String tag) {
        sharedPrefManager.saveURL(value, tag);
    }

    public void checkTooltip(){
        if (sharedPrefManager.getPTooltip()==0)
            sharedPrefManager.setPTooltip(1);

        if (sharedPrefManager.getLTooltip()==0)
            sharedPrefManager.setLTooltip(1);

        if (sharedPrefManager.getMcPTooltip()==0)
            sharedPrefManager.setMcpTooltip(1);
    }

}
