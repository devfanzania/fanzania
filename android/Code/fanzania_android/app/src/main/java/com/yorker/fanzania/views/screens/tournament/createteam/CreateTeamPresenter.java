package com.yorker.fanzania.views.screens.tournament.createteam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.presenter.PresenterStub;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTeamPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    @Inject
    SharedPrefManager sharedPrefManager;

    public CreateTeamPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    public interface IMainView {
        void getTournamentMatchList(JSONObject jsonObject);

        void getTeamRuleDetails(JSONObject jsonObject);

        void setcaptdetails(int val, int playerId, String name,ImageView layoutID);

        void getMatchDetails(JSONObject jsonObject);

        void getAutoTeamPLayers(JSONObject jsonObject);

        void saveTeamResponse(JSONObject jsonObject);
    }

    //--------------- Get Team Rule -----------//
    public void TeamRule(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getTeamSelectionRule(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                System.out.println("Response body tr "+response.toString());
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

    @SuppressLint("CheckResult")
    public void setPlayerData(PlayerDataType players, ImageView imgPlayerC,
                              MontserratMedium tvPlayerName, ImageView tvVC, MontserratMedium tvPlayerPoint, ImageView imgPlayer)
    {
        if (players.getTeamCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
            activitycallback.setcaptdetails(1, players.getPlayerId(), players.getPlayerShortName(),tvVC);
        } else if (players.getTeamVCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
            activitycallback.setcaptdetails(2, players.getPlayerId(), players.getPlayerShortName(),tvVC);
        } else
            tvVC.setVisibility(View.GONE);

        imgPlayerC.setVisibility(View.VISIBLE);

        if (players.getTeamImage() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.blank_jersey);
            requestOptions.error(R.drawable.blank_jersey);
            String url = Constants.BASE_IMAGE_URL + players.getTeamImage();
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(imgPlayer);
        } else
            imgPlayer.setImageResource(R.drawable.blank_jersey);

        tvPlayerPoint.setText(String.valueOf(players.getPlayerValue() + "K"));

        if (players.getPlayerType().equals("overseas"))
            tvPlayerName.setTextColor(context.getResources().getColor(R.color.colorBlue));
        else
            tvPlayerName.setTextColor(context.getResources().getColor(R.color.colorWhite));

        switch (players.getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                imgPlayerC.setImageResource(R.drawable.ic_new_batsman);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                imgPlayerC.setImageResource(R.drawable.ic_new_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                imgPlayerC.setImageResource(R.drawable.ic_new_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                imgPlayerC.setImageResource(R.drawable.ic_new_keeper);
                break;
        }

        String fullName = players.getPlayerName();
        String[] names = fullName.split(" ");
        String name = names[0].substring(0, 1) + "." + names[names.length - 1];
        tvPlayerName.setText(name);
    }

    public void getJsonData(PlayerDataType playerDataType, RelativeLayout rrPlayer) {
        Gson gson = new Gson();
        String obj = gson.toJson(playerDataType, PlayerDataType.class);
        rrPlayer.setTag(obj);
    }

    //--------------- Get Match details -----------//
    public void MatchDetails(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getUserTeamInfo(Constants.str_HEADER,
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
                        activitycallback.getMatchDetails(jsonObject);
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

    //--------------- Get Match details -----------//
    public void AutoFillTeam(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.AutoFillTeam(Constants.str_HEADER,
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
                        activitycallback.getAutoTeamPLayers(jsonObject);
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

        Call<JsonObject> call = retrofitAipService.saveMangeTeam(Constants.str_HEADER,
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

    //--------------- Get all tournament specific match listing -----------//
    public void getTournamentMatchList(String tournamentID) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        Log.d("kdhjfkdsgjfgdshfgdsh","ss :: "+sharedPrefManager.getCustomer_Id() + " ddfd "+map);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getUpcomingMatches(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                System.out.println("Response body "+response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        Log.d("kdhjfkdsgjfgdshfgdsh","ljfjdfhdsjhfjdsh "+jsonObject);
                        activitycallback.getTournamentMatchList(jsonObject);
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
