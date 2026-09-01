package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.yorker.fanzania.views.model.PowerPlayLifelinePost;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTeamFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context mContext;

    public MyTeamFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.mContext = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void updatePowerPlayLifeLine(JSONObject jsonObject);
        void getPowerPlayLifeLine(JSONObject jsonObject);
        void getTournamentList(JSONObject jsonObject);

        void updateTeamName(JSONObject jsonObject);

        void getTeamInfo(JSONObject jsonObject);

        void getTournamentMatchList(JSONObject jsonObject);

        void getMatchDetails(JSONObject jsonObject);
    }

    public void UpdateUserPowerPlay(PowerPlayLifelinePost powerPlayLifelinePost) {

        powerPlayLifelinePost.setUserId(Integer.parseInt(sharedPrefManager.getCustomer_Id()));
        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UpdateUserPowerPlay(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                powerPlayLifelinePost);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.updatePowerPlayLifeLine(jsonObject);
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

    public void updateTeamName(String tournamentID, String userTeamID, String userTeamName) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamID);
        map.put(Constants.TAG_USERTEAMNAME, userTeamName);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UpdateUserTeamName(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        Log.d("khfjshfsgfsdh","sds ::x "+response.body());
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.updateTeamName(jsonObject);
                    } catch (JSONException e) {
                        Log.d("khfjshfsgfsdh","sds :: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("khfjshfsgfsdh","sds :: "+t.getMessage() + " xcdsfdsf :: "+call.toString());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void getPowerplayLifeline(String tournamentID, String userTeamID) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamID);
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.GetPowerplayLine(Constants.str_HEADER,
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
                        Log.e("response", jsonObject.toString());
                        activitycallback.getPowerPlayLifeLine(jsonObject);
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

    //Get all tournament details -----------//
    public void getUserTournamentList() {
        Log.d("khfjshfsgfsdh","getUserTournamentList");
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UserTournaments(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id(),
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.d("khfjshfsgfsdh","response.body() :: "+response.body());
                if (response.body() != null) {
                    try {
                        Log.d("khfjshfsgfsdh","response.body()try:: "+response.body());
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.getTournamentList(jsonObject);

                    } catch (JSONException e) {
                        Log.d("khfjshfsgfsdh","response.body() e:: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("khfjshfsgfsdh","t:: "+t.getMessage() +" kh :: "+call.toString());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void getUserTeamInfo(String tournamentID, String userTeamID, String matchId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamID);
        map.put(Constants.TAG_MATCHID, matchId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UserTeamPlayersWithPowerPlay(Constants.str_HEADER,
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
                        activitycallback.getTeamInfo(jsonObject);
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

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.getTournamentMatches(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  getTournamentMatchList: " );
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        activitycallback.getTournamentMatchList(jsonObject);
                    } catch (JSONException e) {
                        Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  e: " +e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  t: " +t.getMessage());
                System.out.println("error1 " + call.toString());
            }
        });
    }

    //--------------- Get match details -----------//
    public void MatchDetails(String tournamentID, String matchID, String userTeamId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_MATCHID, matchID);
        map.put(Constants.TAG_USERTEAMID, userTeamId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UserMatchDetailsWithPlayers(Constants.str_HEADER,
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

    @SuppressLint("CheckResult")
    public void setPlayerData(PlayerDataType players, ImageView imgPlayer,
                              MontserratMedium tvPlayerName, ImageView tvVC, MontserratMedium tvPlayerPoint,
                              ImageView imgPlayerDel, Boolean isCompletedMatch) {

        if (players.isPlayingInd()){
            imgPlayer.setBackgroundResource(R.drawable.whitecircle);
        }else {
            imgPlayer.setBackgroundResource(0);
        }
        tvPlayerName.setText(players.getPlayerShortName());
//        if (players.isPlayingInd())
//            tvPlayerName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_green,0,0,0);
//        else
//            tvPlayerName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

        if (players.getTeamCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
        } else if (players.getTeamVCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
        } else {
            tvVC.setVisibility(View.GONE);
        }

        if (isCompletedMatch) {
            tvPlayerPoint.setText(String.valueOf(players.getPlayerPoints()));
        }else {
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

        } else {
            imgPlayer.setImageResource(R.drawable.blank_jersey);
        }
        if (players.getPlayerType().equals("overseas")) {
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
        }else {
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
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

    public void saveTournamentID(String tournamentID) {
        sharedPrefManager.saveTournamentId(tournamentID);
    }

    public String getTournamentID() {
        return sharedPrefManager.getTournamentId();
    }

    public String getName() {
        return sharedPrefManager.getCustomerName();
    }

}
