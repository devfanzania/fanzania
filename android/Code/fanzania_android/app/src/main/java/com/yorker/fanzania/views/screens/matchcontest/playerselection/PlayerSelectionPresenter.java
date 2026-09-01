package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerSelectionPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public PlayerSelectionPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void getMatchPlayres(JSONObject jsonObject);

        void getTeamRuleDetails(JSONObject jsonObject);
        void saveTeamResponse(JSONObject jsonObject);
        void getPlayerDetails(JSONObject jsonObject);
    }

    public String getUserID(){
        return sharedPrefManager.getCustomer_Id();
    }

    //--------------- Get match details -----------//
    public void matchPlayers(int MatchId, String MatchType) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_MATCHID, MatchId);
        map.put(Constants.TAG_MATCHTYPE, MatchType);
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getMatchPlayers(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        activitycallback.getMatchPlayres(jsonObject);

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


    public int getSameTeamList(List<PlayerListModel> lList) {
        HashMap<String, Integer> mapSameTeam = new HashMap<>();
        for (PlayerListModel data : lList) {
            if (mapSameTeam.size() > 0) {
                if (mapSameTeam.containsKey(data.getParticipationTeamName())) {
                    int val = mapSameTeam.get(data.getParticipationTeamName());
                    val++;
                    mapSameTeam.put(data.getParticipationTeamName(), val);
                } else
                    mapSameTeam.put(data.getParticipationTeamName(), 1);
            } else
                mapSameTeam.put(data.getParticipationTeamName(), 1);
        }

        return (Collections.max(mapSameTeam.values()));
    }

    public int getOverSeasList(List<PlayerListModel> lList) {
        int val = 0;
        HashMap<String, Integer> mapOverSeas = new HashMap<>();
        for (PlayerListModel data : lList) {
            if (mapOverSeas.size() > 0) {
                if (mapOverSeas.containsKey(data.getPlayerType())) {
                    int pos = mapOverSeas.get(data.getPlayerType());
                    pos++;
                    mapOverSeas.put(data.getPlayerType(), pos);
                } else
                    mapOverSeas.put(data.getPlayerType(), 1);
            } else
                mapOverSeas.put(data.getPlayerType(), 1);
        }
        if (mapOverSeas.get("overseas") != null)
            val = mapOverSeas.get("overseas");

        return val;
    }

    //--------------- Get Team Rule -----------//
    public void TeamRule(int MatchId, String MatchType) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_MATCHID, MatchId);
        map.put(Constants.TAG_MATCHTYPE, MatchType);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getMatchTeamRule(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        activitycallback.getTeamRuleDetails(jsonObject);

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

    //--------------- Save Team -----------//
    public void SaveTeamPlayers(Map<String, Object> map) {

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.saveDailyTeam(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.saveTeamResponse(jsonObject);
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

    public int checkToolTip() {
        int val=sharedPrefManager.getMcPTooltip();
        sharedPrefManager.setMcpTooltip(2);
        return val;
    }

    //--------------- Get player detail -----------//
    public void getPlayerDetail(PlayerListModel playerID, int matchId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_PARTICIPATION_TEAM_ID, playerID.getParticipationTeamId());
        map.put(Constants.TAG_PLAYERID, playerID.getPlayerId());
        map.put(Constants.TAG_MATCHID, matchId);
        map.put(Constants.TAG_APIPId, 272450);



        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getPlayerDetail(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
//                Constants.RETROFIT_HEADER_TOKEN1,
//                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.getPlayerDetails(jsonObject);
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
}
