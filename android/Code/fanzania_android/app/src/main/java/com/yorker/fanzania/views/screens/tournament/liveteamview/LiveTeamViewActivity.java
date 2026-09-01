package com.yorker.fanzania.views.screens.tournament.liveteamview;

import androidx.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLiveTeamViewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.McTeamViewDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.TeamCompareDialog;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import com.yorker.fanzania.views.screens.tournament.liveteamview.adapter.LiveTeamPlayerListAdapter;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LiveTeamViewActivity extends BaseActivity<LiveTeamViewPresenter>
        implements LiveTeamViewPresenter.IMainView {

    private LiveTeamViewPresenter presenter;
    private ActivityLiveTeamViewBinding binding;
    private String tournamentID;
    private String matchID;
    private String userTeamID;
    private String userID;
    private String myTeamId;
    private Handler handler = new Handler();

    @Override
    protected LiveTeamViewPresenter onCreatePresenter() {
        presenter=new LiveTeamViewPresenter(this,this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LiveTeamViewPresenter presenter) {
        LiveTeamViewPresenterComponent component1 = DaggerLiveTeamViewPresenterComponent.builder()
                .presenterComponent(component)
                .liveTeamViewApplicationModule(new LiveTeamViewApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_live_team_view);

        initView();

        if (getIntent()!=null){
            tournamentID=getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            matchID=getIntent().getStringExtra(Constants.TAG_MATCHID);
            userTeamID=getIntent().getStringExtra(Constants.TAG_USERTEAMID);
            userID=getIntent().getStringExtra(Constants.TAG_ID);
            myTeamId = getIntent().getStringExtra("myTeamId");
            getTeams();

            if (myTeamId !=null && !myTeamId.equalsIgnoreCase(userTeamID) ){
                binding.inToolbar.teamcompareTitle.setVisibility(View.VISIBLE);
            }else{
                binding.inToolbar.teamcompareTitle.setVisibility(View.GONE);
            }
            binding.inToolbar.toolbarTitle.setText(getIntent().getStringExtra(Constants.TAG_TEAMNAME));
            String txt= getString(R.string.text_match)+" "+getIntent().getStringExtra(Constants.TAG_CUREENTPOINT)
                    +" | "+getString(R.string.text_total)+" "+getIntent().getStringExtra(Constants.TAG_TOTALPOINT);
            binding.tvMatchPoints.setText(txt);

            if (getIntent().getStringExtra(Constants.TAG_POWERPLAY)!=null)
            {
                if (getIntent().getStringExtra(Constants.TAG_POWERPLAY).equals("NA")) {
                    binding.imgPowerPlay.setVisibility(View.GONE);
                } else {
                    binding.imgPowerPlay.setVisibility(View.VISIBLE);
                    switch (getIntent().getStringExtra(Constants.TAG_POWERPLAY)) {
                        case "NITRO":
                            binding.imgPowerPlay.setImageResource(R.drawable.ic_new_nitro);
                            break;

                        case "PAINKILLER":
                            binding.imgPowerPlay.setImageResource(R.drawable.ic_new_painkiller);
                            break;

                        case "AUTOCAPTAIN":
                            binding.imgPowerPlay.setImageResource(R.drawable.ic_new_autocaptain);
                            break;
                    }
                }
            }
        }
    }

    private void getTeams() {
        binding.inRVList.pBar.setVisibility(View.VISIBLE);

        if (CheckInternetConnection())
            presenter.getUserTeam(tournamentID, matchID, userTeamID, userID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initView() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");
        binding.inToolbar.teamcompareTitle.setVisibility(View.VISIBLE);
        binding.inToolbar.teamcompareTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamCompareDialog dialog = new TeamCompareDialog(LiveTeamViewActivity.this, tournamentID, userTeamID, matchID, userID, myTeamId);
                dialog.show(getSupportFragmentManager(), "ratereview");
            }
        });

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)binding.inToolbar.teamcompareTitle.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        //binding.inToolbar.teamcompareTitle.setLayoutParams(params);


        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.pullToRefresh.setOnRefreshListener(() -> {
            getTeams();
            binding.pullToRefresh.setRefreshing(false);
        });

        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void RetryResponse(String type) {
        presenter.getUserTeam(tournamentID, matchID, userTeamID, userID);
    }

    @Override
    public void getTeamUsers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                LinkedList<LivePlayerModel> pList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LivePlayerModel>>() {
                                }.getType())
                );

                if (pList.size() > 0) {

                    LiveTeamPlayerListAdapter pAdapter = new LiveTeamPlayerListAdapter(this, pList);
                    binding.inRVList.rvList.setAdapter(pAdapter);

                    binding.inRVList.rvList.setVisibility(View.VISIBLE);
                    binding.inRVList.pBar.setVisibility(View.GONE);

                } else {
                    binding.inRVList.rvList.setVisibility(View.GONE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                }
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Runnable getResponceAfterInterval = new Runnable() {

        public void run() {
            try {
                getTeams();
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
