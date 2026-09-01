package com.yorker.fanzania.views.screens.tournament.manageteam;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

public class ManageTeamPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context mContext;

    @Inject
    SharedPrefManager sharedPrefManager;

    public ManageTeamPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.mContext = context;
    }

    public boolean getDialog() {
        return sharedPrefManager.getDialog();
    }

    public void setDialog() {
        sharedPrefManager.saveDialog(false);
    }

    public interface IMainView {
        void getTournamentMatchList(JSONObject jsonObject);

        void getPlayersDetails(JSONObject jsonObject);

        void LastPlayedTeamPlayersListing(JSONObject jsonObject);

        void CheckLastCuttOff(JSONObject jsonObject);

        void getTeamRuleDetails(JSONObject jsonObject);

        void setcaptdetails(int val, int playerId, String name, ImageView tvVC);

        void saveTeamResponse(JSONObject jsonObject);
    }

    //--------------- Get match details -----------//
    public void PlayerDetails(String tournamentID, String matchID, String userTeamId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        //map.put(Constants.TAG_MATCHID, matchID);
        map.put(Constants.TAG_USERTEAMID, userTeamId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UserTeamPlayersWithDetails(
                Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.getPlayersDetails(jsonObject);
                    }
                    catch (JSONException e) {
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

    //--------------- Get Team Rule -----------//
    public void TeamRule(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getTeamSelectionRule(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.getTeamRuleDetails(jsonObject);
                    }
                    catch (JSONException e) {
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
        if (players.isPlayingInd()){
            imgPlayer.setBackgroundResource(R.drawable.whitecircle);
        }
        if (players.getTeamCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
            activitycallback.setcaptdetails(1, players.getPlayerId(),players.getPlayerShortName(),tvVC);
        } else if (players.getTeamVCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
            activitycallback.setcaptdetails(2, players.getPlayerId(),players.getPlayerShortName(),tvVC);
        } else
            tvVC.setVisibility(View.GONE);

        imgPlayerC.setVisibility(View.VISIBLE);

        if (players.getTeamImage() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.blank_jersey);
            requestOptions.error(R.drawable.blank_jersey);
            String url = Constants.BASE_IMAGE_URL + players.getTeamImage();
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(url)
                    .into(imgPlayer);

        } else
            imgPlayer.setImageResource(R.drawable.blank_jersey);

        tvPlayerPoint.setText(String.valueOf(players.getPlayerValue() + "K"));

        if (players.getPlayerType().equals("overseas"))
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
        else
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));

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
        tvPlayerName.setText(players.getPlayerShortName());

//        if (players.isPlayingInd())
//            tvPlayerName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_green,0,0,0);
//        else
//            tvPlayerName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
    }

    //--------------- Get last playerd team -----------//
    public void getLastPlayedTeamPlayers(String tournamentID, String userTeamId, boolean isFirstTime) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamId);
        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.getTeamLastPlayers(
                Constants.str_HEADER,
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

                        if (isFirstTime)
                            activitycallback.LastPlayedTeamPlayersListing(jsonObject);
                        else
                            activitycallback.CheckLastCuttOff(jsonObject);

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

    //--------------- Get last playerd team -----------//
    public void SaveTeamPlayers(Map<String, Object> map) {

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.saveMangeTeam(
                Constants.str_HEADER,
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

    public void getJsonData(PlayerDataType playerDataType, RelativeLayout rrPlayer) {
        Gson gson = new Gson();
        String obj = gson.toJson(playerDataType, PlayerDataType.class);
        rrPlayer.setTag(obj);
    }

    //--------------- Get all tournament specific match listing -----------//
    public void getTournamentMatchList(String tournamentID) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        Log.d("sajhshdhajkhjdkhjkas","sds ::: "+sharedPrefManager.getCustomer_Id() +" ddf "+map);
        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getUpcomingMatches(Constants.str_HEADER,
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
