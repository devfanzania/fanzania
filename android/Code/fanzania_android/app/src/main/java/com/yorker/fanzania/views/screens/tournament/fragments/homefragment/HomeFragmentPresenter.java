package com.yorker.fanzania.views.screens.tournament.fragments.homefragment;

import android.content.Context;

import androidx.annotation.NonNull;

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
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentPresenter extends PresenterStub {
    private IMainView activitycallback;

    public HomeFragmentPresenter(IMainView activitycallback, Context context) {
        this.activitycallback = activitycallback;
        Context context1 = context;
    }

    @Inject
    SharedPrefManager sharedPrefManager;

    public String getName() {
        return sharedPrefManager.getCustomerName();
    }

    public interface IMainView {

        void getTournamentList(JSONObject jsonObject);

        void getTopPlayerList(JSONObject jsonObject);

        void getUpcomingTournamentList(JSONObject jsonObject);

        void getMatchDetails(JSONObject jsonObject);

        void getFunFact(JSONObject jsonObject);
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

//    public void getUserLeagueList(String tournamentID) {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
//        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
//
//        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
//
//        Call<JsonObject> call = retrofitAipService.UserAllLeague(Constants.str_HEADER,
//                Constants.RETROFIT_HEADER_TYPE,
//                Constants.RETROFIT_HEADER_TOKEN1,
//                sharedPrefManager.getCustomer_Id(),
//                map);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        activitycallback.getLeagueList(jsonObject);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                System.out.println("error1 " + call.toString());
//            }
//        });
//    }

    public void getTopPlayerList(String tournamentID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchLastMatchTopPerformerUser(Constants.str_HEADER,
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
                        activitycallback.getTopPlayerList(jsonObject);

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

    public void getUpcomingTournament() {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.UpcommingAllTournament(Constants.str_HEADER,
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
                        activitycallback.getUpcomingTournamentList(jsonObject);

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

    public String getCustomerId() {
        return sharedPrefManager.getCustomer_Id();
    }

    public void saveTournamentID(String tournamentID) {
        sharedPrefManager.saveTournamentId(tournamentID);
    }

    public void saveLeagueID(String leagueID) {
        sharedPrefManager.saveLeagueId(leagueID);
    }

    public String getTournamentID() {
        return sharedPrefManager.getTournamentId();
    }

    public void getFunFact() {

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.fetchFunFact(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                sharedPrefManager.getCustomer_Id());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        activitycallback.getFunFact(jsonObject);

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
