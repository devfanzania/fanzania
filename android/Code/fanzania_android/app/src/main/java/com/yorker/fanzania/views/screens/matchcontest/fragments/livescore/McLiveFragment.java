package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.FragmentMcLiveBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;

import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.McMatchListAdapter;
import com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.model.McLiveLeagueModel;
import com.yorker.fanzania.views.screens.matchcontest.mcliveteamscore.McLiveTeamScoreActivity;
import com.yorker.fanzania.views.screens.matchcontest.scorecard.ScoreCardActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class McLiveFragment extends BaseFragment<McLivePresenter>
        implements McLivePresenter.IMCMainView, McMatchListAdapter.IMatch, McLiveLeagueListAdapter.ILeagues {

    private FragmentMcLiveBinding binding;
    private McLivePresenter presenter;
    private List<DailyMatchModel> mList;
    private String tournamentId;
    private int matchId;
    private int tabPOsition = 0;
    private List<LivePlayerModel> t1List;
    private List<LivePlayerModel> t2List;
    private int cMatchId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_mc_live, container, false);
        return binding.getRoot();
    }

    @Override
    protected McLivePresenter onCreatePresenter() {
        presenter = new McLivePresenter(this, getActivity());
        return presenter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTabIcons();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cMatchId = bundle.getInt(Constants.TAG_MATCHID, 0);
            System.out.println("match id " + cMatchId);
        }

        binding.pullToRefresh.setOnRefreshListener(() -> {
            if (tabPOsition == 0) {
                binding.rlLeftTeam.setVisibility(View.GONE);
                binding.rlRightTeam.setVisibility(View.GONE);
                binding.clTab1.setVisibility(View.VISIBLE);
                binding.clTab2.setVisibility(View.GONE);
                binding.rrLiveLeagues.setVisibility(View.VISIBLE);
                binding.rrLivePoints.setVisibility(View.GONE);
                getLeagueStanding();
            } else {
                binding.rlLeftTeam.setVisibility(View.VISIBLE);
                binding.rlRightTeam.setVisibility(View.VISIBLE);
                binding.clTab1.setVisibility(View.GONE);
                binding.clTab2.setVisibility(View.VISIBLE);
                binding.rrLiveLeagues.setVisibility(View.GONE);
                binding.rrLivePoints.setVisibility(View.VISIBLE);
                getMyPoints();
            }
            binding.pullToRefresh.setRefreshing(false);
        });

        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPOsition = tab.getPosition();
                if (tab.getPosition() == 0) {
                    binding.rlLeftTeam.setVisibility(View.GONE);
                    binding.rlRightTeam.setVisibility(View.GONE);
                    binding.clTab1.setVisibility(View.VISIBLE);
                    binding.clTab2.setVisibility(View.GONE);
                    binding.rrLiveLeagues.setVisibility(View.VISIBLE);
                    binding.rrLivePoints.setVisibility(View.GONE);
                    getLeagueStanding();
                } else {
                    binding.rlLeftTeam.setVisibility(View.VISIBLE);
                    binding.rlRightTeam.setVisibility(View.VISIBLE);
                    binding.clTab1.setVisibility(View.GONE);
                    binding.clTab2.setVisibility(View.VISIBLE);
                    binding.rrLiveLeagues.setVisibility(View.GONE);
                    binding.rrLivePoints.setVisibility(View.VISIBLE);
                    getMyPoints();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.relativeLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ScoreCardActivity.class)
                    .putExtra(Constants.TAG_MATCHID, matchId));
        });

        binding.rlLeftTeam.setOnClickListener(v -> setTeam1List(t1List));

        binding.rlRightTeam.setOnClickListener(v -> setTeam2List(t2List));
    }

    private void getMyPoints() {
        if (CheckInternetConnection())
            presenter.getLiveMatchScore(tournamentId, String.valueOf(matchId));
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    @Override
    protected void injectPresenter(PresenterComponent component, McLivePresenter presenter) {
        McLivePresenterComponent component1 = DaggerMcLivePresenterComponent.builder()
                .presenterComponent(component)
                .mcLiveApplicationModule(new McLiveApplicationModule(getActivity()))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMatches();
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getMatches();
                break;

            case Constants.APICALL_2:
                getLeagueStanding();
                break;

            case Constants.APICALL_3:
                getMyPoints();
                break;
        }
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getDailyMatches();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    @Override
    public void dailyMatches(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                mList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<DailyMatchModel>>() {
                                }.getType())
                );

                if (mList.size() > 0) {
                    binding.clMAin.setVisibility(View.VISIBLE);
                    if (cMatchId == 0)
                        mList.get(0).setSelected(true);

                    binding.rvMatch.setVisibility(View.VISIBLE);
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.clMAin.setVisibility(View.VISIBLE);
                    binding.mBar.setVisibility(View.GONE);
                    initList();
                } else {
                    binding.clMAin.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.tvNoData.setText(getString(R.string.text_nolivemc));
                    binding.clMAin.setVisibility(View.GONE);
                    binding.rvMatch.setVisibility(View.GONE);
                    binding.mBar.setVisibility(View.GONE);
                }
            } else {
                binding.clMAin.setVisibility(View.GONE);
                binding.rvMatch.setVisibility(View.GONE);
                binding.mBar.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.tvNoData.setText(getString(R.string.text_nolivemc));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMatchTeamList(JSONObject jsonObject) {
        System.out.println("match point data " + jsonObject.toString());
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                List<LivePlayerModel> pList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LivePlayerModel>>() {
                                }.getType())
                );

                if (pList.size() > 0) {
                    t1List = new ArrayList<>();
                    t2List = new ArrayList<>();

                    String txt = pList.get(0).getParticipationTeamName();

                    for (LivePlayerModel data : pList) {
                        if (txt.equals(data.getParticipationTeamName()))
                            t1List.add(data);
                        else
                            t2List.add(data);
                    }

                    binding.rrLivePoints.setVisibility(View.VISIBLE);
                    binding.ptsBar.setVisibility(View.VISIBLE);
                    setTeam1List(t1List);

                    binding.rvPointsList.setVisibility(View.VISIBLE);

                } else
                    binding.rvPointsList.setVisibility(View.GONE);
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTeam1List(List<LivePlayerModel> t1List) {
        binding.ptsBar.setVisibility(View.GONE);
        binding.rlLeftTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_black_live, getActivity().getTheme()));
        binding.rlRightTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live, getActivity().getTheme()));
        binding.rvPointsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPointsList.addItemDecoration(new ItemDecoration(getActivity()));
        McLivePlayerListAdapter pAdapter = new McLivePlayerListAdapter(t1List);
        binding.rvPointsList.setAdapter(pAdapter);
    }

    private void setTeam2List(List<LivePlayerModel> t2List) {
        binding.rlLeftTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live, getActivity().getTheme()));
        binding.rlRightTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_black_live, getActivity().getTheme()));
        binding.rvPointsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPointsList.addItemDecoration(new ItemDecoration(getActivity()));
        McLivePlayerListAdapter pAdapter = new McLivePlayerListAdapter(t2List);
        binding.rvPointsList.setAdapter(pAdapter);
    }

    @Override
    public void getLeagueList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                List<McLiveLeagueModel> leagueList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<McLiveLeagueModel>>() {
                                }.getType())
                );

                if (leagueList.size() > 0) {
                    binding.pBar.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.tvListNoData.setVisibility(View.GONE);
                    initLeagueList(leagueList);

                } else {
                    binding.pBar.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.GONE);
                    binding.tvListNoData.setVisibility(View.VISIBLE);
                    binding.tvListNoData.setText(getString(R.string.text_noleagueavailable));
                }
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initList() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMatch.setLayoutManager(ll);
        McMatchListAdapter mAdapter = new McMatchListAdapter(mList, this, cMatchId);
        binding.rvMatch.setAdapter(mAdapter);
    }

    private void initLeagueList(List<McLiveLeagueModel> leagueList) {
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.addItemDecoration(new ItemDecoration(getActivity()));
        McLiveLeagueListAdapter lAdapter = new McLiveLeagueListAdapter(this, leagueList, presenter.getCustomerID());
        binding.rvList.setAdapter(lAdapter);
    }

    @Override
    public void onClickMatchList(DailyMatchModel obj) {
        setUserData(obj);

        tournamentId = String.valueOf(obj.getTournamentId());
        matchId = obj.getMatchId();

        binding.tvLeftTeamName.setText(obj.getTeam1ShortName());
        binding.tvRightTeamName.setText(obj.getTeam2ShortName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        if (obj.getTeam1Image() != null) {
            String url = Constants.BASE_IMAGE_URL + obj.getTeam1Image();
            Glide.with(getActivity())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgLeftTeamLogo);
        } else
            binding.imgLeftTeamLogo.setImageResource(R.drawable.blank_jersey);

        if (obj.getTeam2Image() != null) {
            String url = Constants.BASE_IMAGE_URL + obj.getTeam2Image();
            Glide.with(getActivity())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgRightTeamLogo);
        } else
            binding.imgRightTeamLogo.setImageResource(R.drawable.blank_jersey);

        if (tabPOsition == 0)
            getLeagueStanding();
    }

    private void getLeagueStanding() {
        if (CheckInternetConnection()) {
            binding.rrLiveLeagues.setVisibility(View.VISIBLE);
            presenter.getUserLeagueList(tournamentId, String.valueOf(matchId));
        } else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    private void setUserData(DailyMatchModel obj) {


        if (obj.getShowScore())
            binding.relativeLayout.setVisibility(View.VISIBLE);
        else
            binding.relativeLayout.setVisibility(View.INVISIBLE);

        if (obj.getTeam1Score() != null)
            binding.tvTeam1.setText(obj.getTeam1Score().replace("~", " "));

        if (obj.getTeam2Score() != null)
            binding.tvTeam2.setText(obj.getTeam2Score().replace("~", " "));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        if (obj.getTeam1Image() != null) {
            String url = Constants.BASE_IMAGE_URL + obj.getTeam1Image();
            Glide.with(getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgTeam1);
        } else
            binding.imgTeam1.setImageResource(R.drawable.blank_jersey);

        if (obj.getTeam2Image() != null) {
            String url = Constants.BASE_IMAGE_URL + obj.getTeam2Image();
            Glide.with(getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgTeam2);
        } else
            binding.imgTeam2.setImageResource(R.drawable.blank_jersey);
    }

    private void setupTabIcons() {
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_mystanding)));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_mypoints)));
        binding.tbTabs.getTabAt(0).select();
    }

    @Override
    public void OnSelectLeague(McLiveLeagueModel data) {
        startActivity(new Intent(getActivity(), McLiveTeamScoreActivity.class)
                .putExtra(Constants.TAG_TOURNAMENTID, data.getTournamentId())
                .putExtra(Constants.TAG_USERTEAMID, data.getUserTeamId())
                .putExtra(Constants.TAG_MATCHID, data.getMatchId())
                .putExtra(Constants.TAG_ID, data.getUserId())
                .putExtra(Constants.TAG_TEAMNAME, data.getUserName())
        );
    }

    @Override
    public void OnSetData(McLiveLeagueModel obj) {
        binding.tvPoints.setText(String.valueOf(obj.getCurrentMatchPoints()));
        binding.tvMyRank.setText(String.valueOf(obj.getTeamNewStanding()));
    }
}
