package com.yorker.fanzania.views.screens.matchcontest.fragments.teams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class McTeamFragmentPresenter extends PresenterStub {
    private IMCMainView activitycallback;
    private Context mContext;

    public McTeamFragmentPresenter(IMCMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMCMainView {
        void dailyMatches(JSONObject obj);
        void playerWithPoints(JSONObject obj);
        void players(JSONObject obj);
    }

    public void getDailyMatches() {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, Integer.parseInt(sharedPrefManager.getCustomer_Id()));
        map.put("MatchStatus", "UPCOMING");
        map.put("FetchAll", "Y");
        map.put("PageIndicator", Constants.TAG_TEAMINDICATOR);

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

    //--------------- Get Players with points -----------//
    public void getPlayersWithPoints(int tournamentID, int matchID, String userTeamId,String matchType) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_MATCHID, matchID);
        map.put(Constants.TAG_USERTEAMID, userTeamId);
        map.put(Constants.TAG_MATCHTYPE, matchType);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getDailyTeamPlayersWithPoints(Constants.str_HEADER,
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
                        activitycallback.playerWithPoints(jsonObject);
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

    //--------------- Get Players  -----------//
    public void getPlayers(int tournamentID,String userTeamId,String matchType) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamId);
        map.put(Constants.TAG_MATCHTYPE, matchType);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getDailyPlayers(Constants.str_HEADER,
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
                        activitycallback.players(jsonObject);
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
    public void setPlayerData(McTeamModel players, ImageView imgPlayer,
                              MontserratMedium tvPlayerName, ImageView tvVC, MontserratMedium tvPlayerPoint,
                              ImageView imgPlayerDel, Boolean isCompletedMatch) {

        tvPlayerName.setText(players.getPlayerShortName());

        if (players.getTeamCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
        } else if (players.getTeamVCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
        } else
            tvVC.setVisibility(View.GONE);

        if (isCompletedMatch)
            tvPlayerPoint.setText(String.valueOf(players.getPlayerPoints()));
        else {
            String text = String.valueOf(players.getPlayerValue()) + "K";
            tvPlayerPoint.setText(text);
        }

        if (players.getTeamImage() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.blank_jersey);
            requestOptions.error(R.drawable.blank_jersey);
            String url = Constants.BASE_IMAGE_URL + players.getTeamImage();
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(imgPlayer);

        } else
            imgPlayer.setImageResource(R.drawable.blank_jersey);

        if (players.getPlayerType().equals("overseas"))
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
        else
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));

        switch (players.getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                imgPlayerDel.setImageResource(R.drawable.ic_new_batsman);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_keeper);
                break;
        }
    }
}
