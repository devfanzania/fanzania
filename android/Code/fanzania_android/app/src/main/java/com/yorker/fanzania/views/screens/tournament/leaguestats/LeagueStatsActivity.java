package com.yorker.fanzania.views.screens.tournament.leaguestats;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLeagueStatsNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.tournament.leaguestats.adapter.LeagueStatsListAdapter;
import com.yorker.fanzania.views.screens.tournament.leaguestats.model.LeagueStatsModel;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LeagueStatsActivity extends BaseActivity<LeagueStatsPresenter>
        implements LeagueStatsPresenter.IMainView
{

    private LeagueStatsPresenter presenter;
    private ActivityLeagueStatsNewBinding binding;

    private String tournamentID;
    private String leagueID;
    private int tabPosition = 0;
    private LinkedList<LeagueStatsModel> list= new LinkedList<>();

    @Override
    protected LeagueStatsPresenter onCreatePresenter() {
        presenter=new LeagueStatsPresenter(this,this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LeagueStatsPresenter presenter) {
        LeagueStatsPresenterComponent component1 = DaggerLeagueStatsPresenterComponent.builder()
                .presenterComponent(component)
                .leagueStatsApplicationModule(new LeagueStatsApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_league_stats_new);

        getIntentData();
        initViews();
        initListner();
    }

    private void getIntentData() {
        if (getIntent()!=null)
        {
            tournamentID=getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            leagueID=getIntent().getStringExtra(Constants.TAG_LEAGUEID);

            String leagueName = getIntent().getStringExtra(Constants.TAG_LEAGUENAME);
            binding.tvLeagueName.setText(leagueName);

            String tournamentStatus = getIntent().getStringExtra(Constants.TAG_TOURNAMENTDATE);
            binding.tvTrounamentYear.setText(tournamentStatus);

            String tournamentName = getIntent().getStringExtra(Constants.TAG_TOURNAMENTNAME);
            binding.tvTrounamentName.setText(tournamentName);

            getStatsTopPlayers();
        }
    }

    private void getStatsTopPlayers() {
        if (CheckInternetConnection()){
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getLeagueTopPlayers(leagueID,tournamentID);
        }else
            new NoNetworkDialog(this,this,Constants.APICALL_1);
    }

    private void setAdapter(int position) {
        LeagueStatsListAdapter lAdapter = new LeagueStatsListAdapter(this, list, position);
        binding.inRecyclerview.rvList.setAdapter(lAdapter);
    }

    private void initListner() {
        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        binding.tvHeader3.setText(getString(R.string.text_top3player));
                        binding.tvHeader4.setText(getString(R.string.text_points));
                        binding.tvTabText.setText(getString(R.string.text_teamtopperformer));
                        getStatsTopPlayers();
                        break;

                    case 1:
                        binding.tvHeader3.setText(getString(R.string.text_top3fav));
                        binding.tvHeader4.setText(getString(R.string.text_matchplayed));
                        binding.tvTabText.setText(getString(R.string.text_team_top_preferred_players));
                        getStatsTopTeams();
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

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_leaguestats));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.inRecyclerview.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.inRecyclerview.rvList.addItemDecoration(new ItemDecoration(this));

        setupTabIcons();
    }

    private void setupTabIcons() {
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_performer));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setIcon(R.drawable.ic_5performer));
        binding.tbTabs.getTabAt(0).select();
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getStatsTopPlayers();
                break;

            case Constants.APICALL_2:
                getStatsTopTeams();
                break;
        }
    }

    private void getStatsTopTeams() {
        if (CheckInternetConnection()){
            binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
            binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
            binding.inRecyclerview.rvList.setVisibility(View.GONE);
            presenter.getLeagueTopTeams(leagueID,tournamentID);
        }else
            new NoNetworkDialog(this,this,Constants.APICALL_2);
    }

    @Override
    public void getLeagueStatsResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LeagueStatsModel>>() {
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
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
