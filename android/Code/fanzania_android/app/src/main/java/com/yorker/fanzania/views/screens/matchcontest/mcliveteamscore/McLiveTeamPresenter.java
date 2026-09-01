package com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore;


import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class McLiveTeamPresenter extends PresenterStub {
    private IMainView activitycallback;

    public McLiveTeamPresenter(IMainView activitycallback) {
        this.activitycallback = activitycallback;
    }

    public interface IMainView {
        void getTeamUsers(JSONObject jsonObject);
    }

    //Get User stats captain point -----------//
    public void getUserTeam(int TournamentId, int matchID, int userTeamID, int userID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, TournamentId);
        map.put(Constants.TAG_MATCHID, matchID);
        map.put(Constants.TAG_USERTEAMID, userTeamID);
        map.put(Constants.TAG_ID, userID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getLiveTeamUsers(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                String.valueOf(userID),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        activitycallback.getTeamUsers(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

//    public int getCustomerID(){
//        return Integer.parseInt(sharedPrefManager.getCustomer_Id());
//    }
}
