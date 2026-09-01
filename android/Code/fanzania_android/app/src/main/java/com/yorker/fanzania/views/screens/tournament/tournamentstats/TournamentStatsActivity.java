package com.yorker.fanzania.views.screens.tournament.tournamentstats;

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
import com.yorker.fanzania.databinding.ActivityTournamentStatsBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NewTeamViewDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.TournamentStatsTeamViewDialog;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model.LeagueTeamModel;
import com.yorker.fanzania.views.screens.tournament.tournamentstats.adapter.TournamentStatsListAdapter;
import com.yorker.fanzania.views.screens.tournament.tournamentstats.model.TournamentStatsModel;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TournamentStatsActivity extends BaseActivity<TournamentStatsPresenter>
        implements TournamentStatsPresenter.IMainView, TournamentStatsListAdapter.ICallback{

    private TournamentStatsPresenter presenter;
    private ActivityTournamentStatsBinding binding;
    private int tabPosition = 0;
    private String tournamentID;
    private String userTeamId;
    private LinkedList<TournamentStatsModel> list = new LinkedList<>();
    private TournamentStatsListAdapter tAdapter;
    private int tStatus = 0;

    @Override
    protected TournamentStatsPresenter onCreatePresenter() {
        presenter=new TournamentStatsPresenter(this,this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, TournamentStatsPresenter presenter) {
        TournamentStatsPresenterComponent component1 = DaggerTournamentStatsPresenterComponent.builder()
                .presenterComponent(component)
                .tournamentStatsApplicationModule(new TournamentStatsApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_tournament_stats);

        getIntentData();
        initViews();
        initListner();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            userTeamId = getIntent().getStringExtra(Constants.TAG_USERTEAMID);
            String teamName = getIntent().getStringExtra(Constants.TAG_TEAMNAME);

            tStatus = getIntent().getIntExtra(Constants.TAG_TOURNAMENTSTATUS,0);

            binding.tvTeamName.setText(teamName);
            String tournamentStatus = getIntent().getStringExtra(Constants.TAG_TOURNAMENTDATE);
            binding.tvTrounamentYear.setText(tournamentStatus);

            String tournamentName = getIntent().getStringExtra(Constants.TAG_TOURNAMENTNAME);
            binding.tvTrounamentName.setText(tournamentName);
            getTournamentTopPlayers();
        }
    }

    private void getTournamentTopPlayers() {
        if (CheckInternetConnection()){
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getTournamentStatsTopPlayers(userTeamId,tournamentID);
        }else
        {
            new NoNetworkDialog(this,this,Constants.APICALL_1);
        }
    }

    private void getTournamentTopTeams() {
        if (CheckInternetConnection()){
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getTournamentStatsTopTeams(tournamentID);
        }else
        {
            new NoNetworkDialog(this,this,Constants.APICALL_2);
        }
    }

    private void getTournamentTopLeagues() {
        if (CheckInternetConnection()){
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getTournamentStatsTopLeagues(tournamentID);
        }else
        {
            new NoNetworkDialog(this,this,Constants.APICALL_3);
        }
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_tournamentstats));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.inRecyclerview.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.inRecyclerview.rvList.addItemDecoration(new ItemDecoration(this));

        setupTabIcons();
    }

    private void setupTabIcons() {
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_5performer));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_top_team));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_top_league));
        binding.tbTabs.getTabAt(0).select();
    }

    private void initListner() {
        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        binding.tvHeader2.setText(getString(R.string.text_name));
                        binding.tvHeader3.setText(getString(R.string.text_team));
                        binding.tvTabText.setText(getString(R.string.text_global_top_ten_players));
                        getTournamentTopPlayers();
                        break;

                    case 1:
                        binding.tvHeader2.setText(getString(R.string.text_team));
                        binding.tvHeader3.setText(getString(R.string.text_owner));
                        binding.tvTabText.setText(getString(R.string.text_global_top_ten_teams));
                        getTournamentTopTeams();
                        break;

                    case 2:
                        binding.tvHeader2.setText(getString(R.string.text_league));
                        binding.tvHeader3.setText(getString(R.string.text_owner));
                        binding.tvTabText.setText(getString(R.string.text_global_top_ten_leagues));
                        getTournamentTopLeagues();
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

    private void setAdapter(int position) {
        tAdapter = new TournamentStatsListAdapter(this, list, position,this);
        binding.inRecyclerview.rvList.setAdapter(tAdapter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getTournamentTopPlayers();
                break;

            case Constants.APICALL_2:
                getTournamentTopTeams();
                break;

            case Constants.APICALL_3:
                getTournamentTopLeagues();
                break;
        }
    }

    @Override
    public void getUserStatsTopPlayers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                System.out.println("user stats " + jsonObject.toString());
                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<TournamentStatsModel>>() {
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

    @Override
    public void onClickMatch(TournamentStatsModel data) {
        TournamentStatsTeamViewDialog dialog = new TournamentStatsTeamViewDialog(this, tournamentID, data.getUserTeamId(),
                presenter.getCustomerId(),  data.getUserTeamName(), tStatus);
        dialog.show(this.getSupportFragmentManager(), "ratereview");
    }
}
