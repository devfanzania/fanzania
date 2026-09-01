package com.yorker.fanzania.views.screens.tournament.liveleagueview;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLiveLeagueViewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.screens.tournament.liveleagueview.adapter.LeagueUserAdapter;
import com.yorker.fanzania.views.screens.tournament.liveleagueview.model.LeagueUserModel;
import com.yorker.fanzania.views.screens.tournament.liveteamview.LiveTeamViewActivity;
import com.yorker.fanzania.views.screens.tournament.liveteamview.LiveTeamViewCompareActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class LiveLeagueViewActivity extends BaseActivity<LiveLeagueViewPresenter>
        implements LiveLeagueViewPresenter.IMainView, LeagueUserAdapter.IPoints {

    private ActivityLiveLeagueViewBinding binding;
    private LiveLeagueViewPresenter presenter;
    private Handler handler = new Handler();
    private String tournamentID;
    private String matchID;
    private String leagueID;

    @Inject
    SharedPrefManager sharedPrefManager;

    @Override
    protected LiveLeagueViewPresenter onCreatePresenter() {
        presenter = new LiveLeagueViewPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LiveLeagueViewPresenter presenter) {
        LiveLeagueViewPresenterComponent component1 = DaggerLiveLeagueViewPresenterComponent.builder()
                .presenterComponent(component)
                .liveLeagueViewApplicationModule(new LiveLeagueViewApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_league_view);

        initView();

        if (getIntent() != null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            matchID = getIntent().getStringExtra(Constants.TAG_MATCHID);
            leagueID = getIntent().getStringExtra(Constants.TAG_LEAGUEID);

            binding.inToolbar.toolbarTitle.setText(getIntent().getStringExtra(Constants.TAG_LEAGUENAME));
            getLUsers();
            handler.post(getResponceAfterInterval);
        }
    }

    private void getLUsers() {
        if (CheckInternetConnection())
            presenter.getUsers(leagueID, tournamentID, matchID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initView() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.pullToRefresh.setOnRefreshListener(() -> {
            getLUsers(); // your code
            binding.pullToRefresh.setRefreshing(false);
        });

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void getLeagueUsers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                LinkedList<LeagueUserModel> lList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LeagueUserModel>>() {
                                }.getType())
                );

                if (lList.size() > 0) {
                    binding.inRVList.rvList.setVisibility(View.VISIBLE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.GONE);
                    LeagueUserAdapter lAdapter = new LeagueUserAdapter(lList, this,presenter.getCustomerId());
                    binding.inRVList.rvList.setAdapter(lAdapter);
                } else {
                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.rvList.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inRVList.tvNoDataFound.setText(getString(R.string.text_noteamavailable));
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RetryResponse(String type) {
        getLUsers();
    }

    @Override
    public void OnClickLeaguTeam(LeagueUserModel data) {

        Intent intent;
        if (LeagueUserAdapter.myTeamID !=null && !LeagueUserAdapter.myTeamID.equalsIgnoreCase(String.valueOf(data.getUserTeamId())) ){
            intent = new Intent(this, LiveTeamViewCompareActivity.class);
        }else{
            intent = new Intent(this, LiveTeamViewActivity.class);
        }
        intent.putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                .putExtra(Constants.TAG_USERTEAMID, String.valueOf(data.getUserTeamId()))
                .putExtra(Constants.TAG_MATCHID, matchID)
                .putExtra(Constants.TAG_TEAMNAME, data.getUserTeamName())
                .putExtra(Constants.TAG_ID, String.valueOf(data.getUserId()))
                .putExtra(Constants.TAG_TOTALPOINT, String.valueOf(data.getTotalPoints()))
                .putExtra(Constants.TAG_CUREENTPOINT, String.valueOf(data.getCurrentMatchPoints()))
                .putExtra(Constants.TAG_POWERPLAY, data.getPowerPlay());

        intent.putExtra("myTeamId", LeagueUserAdapter.myTeamID);
        startActivity(intent);
    }

    private Runnable getResponceAfterInterval = new Runnable() {

        public void run() {
            try {
                getLUsers();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 300000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(getResponceAfterInterval);
    }
}
