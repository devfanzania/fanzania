package com.yorker.fanzania.views.screens.league;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.shared.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueSubscriptionPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    Validation validation;

    @Inject
    SharedPrefManager sharedPrefManager;

    public LeagueSubscriptionPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {
        void LeagueSubscriptions(JSONObject value);
        void LeagueSubscriptionsUpdated(JSONObject value);
    }

    public void updateSubscriptions(List<LeagueSubscriptionPost> data) {

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UpdateLeagueSubscriptions(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE, Constants.RETROFIT_HEADER_TOKEN1, data);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.LeagueSubscriptionsUpdated(jsonObject);
                        else
                            activitycallback.LeagueSubscriptionsUpdated(jsonObject);

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

    public void LeagueSubscriptions(String leagueId, String tournamentId) {

        Map<String, Object> map = new HashMap<>();
        map.put("LeagueId", leagueId);
        map.put("TournamentId", tournamentId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.LeagueSubscriptions(Constants.str_HEADER, Constants.RETROFIT_HEADER_TYPE,Constants.RETROFIT_HEADER_TOKEN1, map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                            activitycallback.LeagueSubscriptions(jsonObject);
                        else
                            activitycallback.LeagueSubscriptions(jsonObject);

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
