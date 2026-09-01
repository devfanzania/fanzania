package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore;

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

public class McLivePresenter extends PresenterStub {
    private IMCMainView activitycallback;
    private Context mContext;

    public McLivePresenter(IMCMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMCMainView {
        void dailyMatches(JSONObject obj);
        void getMatchTeamList(JSONObject jsonObject);

        void getLeagueList(JSONObject jsonObject);

    }

    public void getDailyMatches() {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));
        map.put("MatchStatus", "Live");
        map.put("FetchAll", "Y");
        map.put("PageIndicator", Constants.TAG_LIVEINDICATOR);

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

    public int getCustomerID(){
        return Integer.parseInt(sharedPrefManager.getCustomer_Id());
    }

    public void getLiveMatchScore(String tournamentID,String matchID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, Integer.parseInt(tournamentID));
        map.put(Constants.TAG_MATCHID, Integer.parseInt(matchID));
        map.put(Constants.TAG_ID,getCustomerID());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getLiveMatchScore(Constants.str_HEADER,
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
                        activitycallback.getMatchTeamList(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void getUserLeagueList(String tournamentID,String matchID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_MATCHID, matchID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.dailyLiveLeagueUsers(Constants.str_HEADER,
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
                        activitycallback.getLeagueList(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

}
