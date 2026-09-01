package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
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

public class MyLeagueFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;
    private Context context;

    public MyLeagueFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        this.context = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public interface IMainView {
        void getTournamentList(JSONObject jsonObject);

        void getLeagueList(JSONObject jsonObject);

        void getLeagueTeamList(JSONObject jsonObject);

        void getApproveMemeber(JSONObject jsonObject, int data);

        void getRemoveMemeber(JSONObject jsonObject, int position);

        void getChangePin(JSONObject jsonObject);
    }

    public void shareApp(String leaguePin, String leagueName) {
        final String appPackageName = context.getPackageName();
        String str = context.getString(R.string.text_shareleaguetxt1) + " "
                + leagueName + " " + context.getString(R.string.text_shareleaguetxt2)
                + context.getString(R.string.text_shareleaguetxt3)
                + " " + leaguePin + " " + context.getString(R.string.text_shareleaguetxt4)
                + "https://play.google.com/store/apps/details?id="
                + appPackageName;

        System.out.println("share text " + str);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public void getUserTournamentList() {

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
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        activitycallback.getTournamentList(jsonObject);

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

    public void getUserLeagueList(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.UserAllLeague(Constants.str_HEADER,
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
                System.out.println("error1 " + call.toString());
            }
        });
    }

    public void getLeagueTeamList(String tournamentID, String leagueID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_LEAGUEID, leagueID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.LeagueTeams(Constants.str_HEADER,
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

                        activitycallback.getLeagueTeamList(jsonObject);

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

    public void ApproveLeagueTeam(String LeagueId, int position, String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_LEAGUEID, LeagueId);
        map.put(Constants.TAG_ID, userId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.ApproveLeagueUser(Constants.str_HEADER,
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

                        activitycallback.getApproveMemeber(jsonObject,position);

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

    public void RemoveLeagueTeam(String LeagueId, int UserLeagueId, int position) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_LEAGUEID, LeagueId);
        map.put(Constants.TAG_USERLEAGUEID, UserLeagueId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.RemoveLeagueUser(Constants.str_HEADER,
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

                        activitycallback.getRemoveMemeber(jsonObject,position);

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

    public void ChangeLeaguePin(String tournamentID, String LeagueId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_LEAGUEID, LeagueId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.CreateLeaguePin(Constants.str_HEADER,
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

                        activitycallback.getChangePin(jsonObject);

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

    public String getCustomerId() {
        return sharedPrefManager.getCustomer_Id();
    }

    public void saveTournamentID(String tournamentID) {
        sharedPrefManager.saveTournamentId(tournamentID);
    }

    public String getTournamentID() {
        return sharedPrefManager.getTournamentId();
    }

    public String getLeagueID() {
        return sharedPrefManager.getLeagueId();
    }

    public void clearLeagueID() {
        sharedPrefManager.claerLeagueId();
    }

    public String getName() {
        return sharedPrefManager.getCustomerName();
    }

    public int checkToolTip() {
        int val=sharedPrefManager.getLTooltip();
        sharedPrefManager.setLTooltip(2);
        return val;
    }
}
