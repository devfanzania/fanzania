package com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityMcLiveTeamScoreBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class McLiveTeamScoreActivity extends BaseActivity<McLiveTeamPresenter>
        implements McLiveTeamPresenter.IMainView {

    private ActivityMcLiveTeamScoreBinding binding;
    private McLiveTeamPresenter presenter;
    private int tournamentID;
    private int matchID;
    private int userTeamID;
    private int userID;

    @Override
    protected McLiveTeamPresenter onCreatePresenter() {
        presenter = new McLiveTeamPresenter(this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, McLiveTeamPresenter presenter) {
        McLiveTeamPresenterComponent component1 = DaggerMcLiveTeamPresenterComponent.builder()
                .presenterComponent(component)
                .mcLiveTeamApplicationModule(new McLiveTeamApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mc_live_team_score);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(this));

        binding.pullToRefresh.setOnRefreshListener(() -> {
            getTeams();
            binding.pullToRefresh.setRefreshing(false);
        });

        if (getIntent() != null) {
            tournamentID = getIntent().getIntExtra(Constants.TAG_TOURNAMENTID,0);
            matchID = getIntent().getIntExtra(Constants.TAG_MATCHID,0);
            userTeamID = getIntent().getIntExtra(Constants.TAG_USERTEAMID,0);
            userID = getIntent().getIntExtra(Constants.TAG_ID,0);

            getTeams();

            binding.inToolbar.toolbarTitle.setText(getIntent().getStringExtra(Constants.TAG_TEAMNAME));
        }
    }

    private void getTeams() {
        binding.inRVList.pBar.setVisibility(View.VISIBLE);

        if (CheckInternetConnection())
            presenter.getUserTeam(tournamentID, matchID, userTeamID, userID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    @Override
    public void getTeamUsers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                List<LivePlayerModel> pList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LivePlayerModel>>() {
                                }.getType())
                );

                if (pList.size() > 0) {

                    McLiveTeamScoreAdapter pAdapter = new McLiveTeamScoreAdapter( pList);
                    binding.inRVList.rvList.setAdapter(pAdapter);

                    binding.inRVList.rvList.setVisibility(View.VISIBLE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                } else {
                    binding.inRVList.rvList.setVisibility(View.GONE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RetryResponse(String type) {
        presenter.getUserTeam(tournamentID, matchID, userTeamID, userID);
    }
}
