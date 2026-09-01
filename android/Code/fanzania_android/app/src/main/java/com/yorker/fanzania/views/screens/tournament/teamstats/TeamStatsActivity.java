package com.yorker.fanzania.views.screens.tournament.teamstats;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityTeamStatsNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.tournament.teamstats.adapter.TeamStatsListAdapter;
import com.yorker.fanzania.views.screens.tournament.teamstats.model.UserStatsModel;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TeamStatsActivity extends BaseActivity<TeamStatsPresenter>
        implements TeamStatsPresenter.IMainView {

    private TeamStatsPresenter presenter;
    private ActivityTeamStatsNewBinding binding;
    private int tabPosition = 0;
    private String tournamentID;
    private String userTeamId;
    private TeamStatsListAdapter lAdapter;
    private LinkedList<UserStatsModel> list = new LinkedList<>();

    @Override
    protected TeamStatsPresenter onCreatePresenter() {
        presenter = new TeamStatsPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, TeamStatsPresenter presenter) {
        TeamStatsPresenterComponent component1 = DaggerTeamStatsPresenterComponent.builder()
                .presenterComponent(component)
                .teamStatsApplicationModule(new TeamStatsApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_team_stats_new);

        getIntentData();
        initViews();
        initListner();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            userTeamId = getIntent().getStringExtra(Constants.TAG_USERTEAMID);
            String teamName = getIntent().getStringExtra(Constants.TAG_TEAMNAME);

            binding.tvTeamName.setText(teamName);
            String tournamentStatus = getIntent().getStringExtra(Constants.TAG_TOURNAMENTDATE);
            binding.tvTrounamentYear.setText(tournamentStatus);

            String tournamentName = getIntent().getStringExtra(Constants.TAG_TOURNAMENTNAME);
            binding.tvTrounamentName.setText(tournamentName);
            getStatsPlayerData();
        }
    }

    private void getStatsPlayerData() {
        if (CheckInternetConnection()) {
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getUserTopPlayers(userTeamId, tournamentID);
        } else {
            new NoNetworkDialog(this, this, Constants.APICALL_1);
        }
    }

    private void getStatsCaptainData() {
        if (CheckInternetConnection()) {
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getUserStatsCaptainPoint(userTeamId, tournamentID);
        } else {
            new NoNetworkDialog(this, this, Constants.APICALL_2);
        }
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_teamstats));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.inRecyclerview.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.inRecyclerview.rvList.addItemDecoration(new ItemDecoration(this));

        setupTabIcons();
    }

    private void setAdapter(int position) {
        lAdapter = new TeamStatsListAdapter(this, list, position);
        binding.inRecyclerview.rvList.setAdapter(lAdapter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getStatsPlayerData();
                break;

            case Constants.APICALL_2:
                getStatsCaptainData();
                break;
        }
    }

    private void initListner() {
        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        binding.tvHeader1.setText(getString(R.string.text_rank));
                        binding.tvHeader2.setText(getString(R.string.text_name));
                        binding.tvHeader3.setText(getString(R.string.text_team));
                        binding.tvHeader4.setText(getString(R.string.text_points));
                        binding.tvTabText.setText(getString(R.string.text_my_top_ten_players));
                        getStatsPlayerData();
                        break;

                    case 1:
                        binding.tvHeader1.setText(getString(R.string.text_match1));
                        binding.tvHeader2.setText(getString(R.string.text_match));
                        binding.tvHeader3.setText(getString(R.string.text_capt));
                        binding.tvHeader4.setText(getString(R.string.text_points));
                        binding.tvTabText.setText(getString(R.string.text_recent_match_captain_usage));
                        getStatsCaptainData();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabIcons() {
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_5performer));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_captain_uses));
        binding.tbTabs.getTabAt(0).select();
    }

    @Override
    public void getUserStatsTopPlayers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                System.out.println("user stats " + jsonObject.toString());
                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserStatsModel>>() {
                                }.getType())
                );

                if (list.size() > 0) {
                    binding.inRecyclerview.pBar.setVisibility(View.GONE);
                    binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
                    binding.inRecyclerview.rvList.setVisibility(View.VISIBLE);

                    setAdapter(tabPosition);

                } else {
                    binding.inRecyclerview.pBar.setVisibility(View.GONE);
                    binding.inRecyclerview.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inRecyclerview.rvList.setVisibility(View.GONE);
                    binding.inRecyclerview.tvNoDataFound.setText(getString(R.string.text_nodata));
                }
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
