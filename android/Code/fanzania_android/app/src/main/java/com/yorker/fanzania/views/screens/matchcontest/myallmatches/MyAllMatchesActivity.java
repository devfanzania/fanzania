package com.yorker.fanzania.views.screens.matchcontest.myallmatches;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ActivityMyAllMatchesBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerSelectionActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAllMatchesActivity extends BaseActivity<MyAllMatchPresenter>
        implements MyAllMatchPresenter.IMainView, MyMatchListAdapter.itemCallBack {

    private ActivityMyAllMatchesBinding binding;
    private MyAllMatchPresenter presenter;
    private List<DailyMatchModel> list;
    private String mStatus="all";

    @Override
    protected MyAllMatchPresenter onCreatePresenter() {
        presenter = new MyAllMatchPresenter(this, MyAllMatchesActivity.this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, MyAllMatchPresenter presenter) {
        MyAllMatchComponent loginPresenterComponent = DaggerMyAllMatchComponent.builder()
                .presenterComponent(component)
                .myAllMatchApplicationModule(new MyAllMatchApplicationModule(MyAllMatchesActivity.this))
                .build();
        loginPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_all_matches);
        initViews();

        binding.tbSort.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mStatus="all";
                        callDailyMatchs(mStatus);
                        break;
                    case 1:
                        mStatus="UPCOMING";
                        callDailyMatchs(mStatus);
                        break;
                    case 2:
                        mStatus="LIVE";
                        callDailyMatchs(mStatus);
                        break;
                    case 3:
                        mStatus="COMPLETE";
                        callDailyMatchs(mStatus);
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

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_home));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        callDailyMatchs(mStatus);
    }

    private void callDailyMatchs(String status) {
        if (CheckInternetConnection()){
            binding.rvMatches.setVisibility(View.GONE);
            binding.tvNoMatch.setVisibility(View.GONE);
            binding.pBar.setVisibility(View.VISIBLE);
            presenter.getDailyMatches(status);
        }
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initList() {
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        MyMatchListAdapter lAdapter = new MyMatchListAdapter(list,this);
        binding.rvMatches.setAdapter(lAdapter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                callDailyMatchs(mStatus);
                break;
//            case Constants.APICALL_2:
//                callDailyMatch();
//                break;
//
//            case Constants.APICALL_3:
//                presenter.getUpcomingTournament();
//                break;
        }
    }

    @Override
    public void dailyMatches(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                list = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<DailyMatchModel>>() {
                                }.getType())
                );

                if (list.size() > 0) {
                    binding.rvMatches.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setVisibility(View.GONE);
                    binding.pBar.setVisibility(View.GONE);
                    initList();
                } else {
                    binding.rvMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.pBar.setVisibility(View.GONE);
                }
            } else{
                binding.rvMatches.setVisibility(View.GONE);
                binding.tvNoMatch.setVisibility(View.VISIBLE);
                binding.pBar.setVisibility(View.GONE);
                binding.tvNoMatch.setText(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(DailyMatchModel obj) {

        switch (obj.getMatchStatus()){
            case "UPCOMING":
                String txt=obj.getTeam1ShortName()+" v "+obj.getTeam2ShortName();
                startActivity(new Intent(this, PlayerSelectionActivity.class)
                        .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                        .putExtra(Constants.TAG_MATCHTYPE, obj.getMatchType())
                        .putExtra(Constants.TAG_TOURNAMENTID, obj.getTournamentId())
                        .putExtra(Constants.TAG_MATCHDATE, obj.getMatchDate())
                        .putExtra(Constants.TAG_HEADER, txt)
                        .putExtra(Constants.TAG_PAGE, true)
                        .putExtra("Team1ShortName", obj.getTeam1ShortName())
                        .putExtra("Team2ShortName", obj.getTeam2ShortName())
                );
                break;
            case "COMPLETE":
                startActivity(new Intent(this, HomeActivity.class)
                        .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                        .putExtra(Constants.TAG_INDEX, 1)
                );
                finish();
                break;
            case "Live":
                startActivity(new Intent(this, HomeActivity.class)
                        .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                        .putExtra(Constants.TAG_INDEX, 2)
                );
                finish();
                break;
        }
    }
}
