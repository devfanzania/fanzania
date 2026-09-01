package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
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
import com.yorker.fanzania.databinding.FragmentLiveScoreNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.McLivePlayerListAdapter;
import com.yorker.fanzania.views.screens.matchcontest.scorecard.ScoreCardActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter.LiveLeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter.LiveTournamentAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.adapter.NewMatchListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LiveMatchModel;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LivePlayerModel;
import com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model.LiveTournamentModel;
import com.yorker.fanzania.views.screens.tournament.liveleagueview.LiveLeagueViewActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LiveScoreFragment extends BaseFragment<LiveScoreFragmentPresenter>
        implements LiveScoreFragmentPresenter.IMainView, LiveLeagueListAdapter.ILeagues, NewMatchListAdapter.IMatch {

    public static String TEAM2NAME = null;
    public static String TEAM1NAME = null;
    private LiveScoreFragmentPresenter presenter;
    private FragmentLiveScoreNewBinding binding;
    private Handler handler = new Handler();

    private LinkedList<LiveTournamentModel> tList;
    private LinkedList<LiveMatchModel> mList;

    private List<LivePlayerModel> t1List;
    private List<LivePlayerModel> t2List;

    private int tabLength = 0;
    private int tabPosition = 0;

    private String tournamentID;
    private String matchID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_live_score_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        getTournaments();
    }

    private void initViews() {
        binding.rvPointsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.addItemDecoration(new ItemDecoration(getActivity()));

        setupTabIcons();

        binding.pullToRefresh.setOnRefreshListener(() -> {
            refreshData();
            binding.pullToRefresh.setRefreshing(false);
        });

        //-------------------- Tournament view -----------//
        binding.leftNav.setOnClickListener(v -> {
            int tab = binding.viewpager.getCurrentItem();
            if (tab > 0) {
                tab--;
                binding.viewpager.setCurrentItem(tab);
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
            } else if (tab == 0)
                binding.viewpager.setCurrentItem(tab);

            if (tab == 0)
                binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new_disable);
            else
                binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);
        });

        binding.rightNav.setOnClickListener(v -> {
            int tab = binding.viewpager.getCurrentItem();

            tab++;
            binding.viewpager.setCurrentItem(tab);
            binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);

            if (tab < (tabLength - 1))
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
            else
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new_disable);
        });

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setTournamentDetails(tList.get(position));
            }
        });

        binding.rlLeftTeam.setOnClickListener(v -> setTeam1List(t1List));

        binding.rlRightTeam.setOnClickListener(v -> setTeam2List(t2List));

        //-------------------- Match view -----------//

//        binding.imgLeftNav.setOnClickListener(v -> {
//            int tab = binding.tbLeagues.getCurrentItem();
//            if (tab > 0) {
//                tab--;
//                binding.tbLeagues.setCurrentItem(tab);
//                binding.imgRightNav.setImageResource(R.drawable.ic_right_arrow_filled_dark);
//            } else if (tab == 0)
//                binding.tbLeagues.setCurrentItem(tab);
//
//            if (tab == 0)
//                binding.imgLeftNav.setImageResource(R.drawable.ic_left_arrow_filled_light);
//            else
//                binding.imgLeftNav.setImageResource(R.drawable.ic_left_arrow_filled_dark);
//        });

//        binding.imgRightNav.setOnClickListener(v -> {
//            int tab = binding.tbLeagues.getCurrentItem();
//
//            tab++;
//            binding.tbLeagues.setCurrentItem(tab);
//            binding.imgLeftNav.setImageResource(R.drawable.ic_left_arrow_filled_dark);
//
//            if (tab < (tabLength - 1))
//                binding.imgRightNav.setImageResource(R.drawable.ic_right_arrow_filled_dark);
//            else
//                binding.imgRightNav.setImageResource(R.drawable.ic_right_arrow_filled_light);
//        });

//        binding.tbLeagues.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrollStateChanged(int state) {
//            }
//
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            public void onPageSelected(int position) {
//                matchID = String.valueOf(mList.get(position).getMatchId());
//                if (tabPosition == 1) {
//                    getMatchTeams();
//                } else {
//                    getLeagues();
//                }
//                handler.post(getResponceAfterInterval);
//            }
//        });

        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        binding.rrLivePoints.setVisibility(View.VISIBLE);
                        binding.clTab2.setVisibility(View.VISIBLE);
                        binding.clMatchTab.setVisibility(View.VISIBLE);

                        binding.rrLiveLeagues.setVisibility(View.GONE);
                        getMatchTeams();
                        break;

                    case 1:
                        binding.rrLivePoints.setVisibility(View.GONE);
                        binding.clTab2.setVisibility(View.GONE);
                        binding.clMatchTab.setVisibility(View.GONE);

                        binding.rrLiveLeagues.setVisibility(View.VISIBLE);
                        binding.pBar.setVisibility(View.VISIBLE);
                        getLeagues();
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

        binding.relativeLayout.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ScoreCardActivity.class)
                    .putExtra(Constants.TAG_MATCHID,Integer.parseInt(matchID)));
        });
    }

    private void setTeam1List(List<LivePlayerModel> t1List) {

        binding.tvMyRank.setText(String.valueOf(t1List.get(0).getAllTotalPoints()));
        binding.tvPoints.setText(String.valueOf(t1List.get(0).getCurrentMatchPoints()));

        binding.ptsBar.setVisibility(View.GONE);
        binding.rlLeftTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_black_live, getActivity().getTheme()));
        binding.rlRightTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live, getActivity().getTheme()));
        binding.rvPointsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        McLivePlayerListAdapter pAdapter = new McLivePlayerListAdapter(t1List);
        binding.rvPointsList.setAdapter(pAdapter);
    }

    private void setTeam2List(List<LivePlayerModel> t2List) {
        binding.rlLeftTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live, getActivity().getTheme()));
        binding.rlRightTeam.setBackground(getResources().getDrawable(R.drawable.round_corner_black_live, getActivity().getTheme()));
        binding.rvPointsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        McLivePlayerListAdapter pAdapter = new McLivePlayerListAdapter(t2List);
        binding.rvPointsList.setAdapter(pAdapter);
    }

    private void refreshData() {
        if (tabPosition == 0)
            getMatchTeams();
        else
            getLeagues();
    }

    private void getLeagues() {
        if (CheckInternetConnection())
            presenter.getUserLeagueList(tournamentID);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_4);
    }

    private void getMatchTeams() {
        if (CheckInternetConnection())
            presenter.getLiveMatchScore(tournamentID, matchID);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    private void getTournaments() {
        if (CheckInternetConnection())
            presenter.getLiveTournamentList();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    private void getMatchs() {
        if (CheckInternetConnection())
            presenter.getLiveMatchList(tournamentID);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    private void setTournamentDetails(LiveTournamentModel data) {
        tournamentID = String.valueOf(data.getTournamentId());
        binding.rvMatch.setVisibility(View.GONE);
        binding.mBar.setVisibility(View.GONE);
        binding.tvNoLeague.setVisibility(View.GONE);

        getMatchs();
    }

    private void setupTabIcons() {

        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_livescore)));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_trackleague)));
        binding.tbTabs.getTabAt(0).select();
    }

    @Override
    protected LiveScoreFragmentPresenter onCreatePresenter() {
        presenter = new LiveScoreFragmentPresenter(this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LiveScoreFragmentPresenter presenter) {
        LiveScoreFragmentPresenterComponent liveScoreFragmentPresenterComponent = DaggerLiveScoreFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .liveScoreFragmentApplicationModule(new LiveScoreFragmentApplicationModule(getActivity()))
                .build();
        liveScoreFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getTournaments();
                break;

            case Constants.APICALL_2:
                getMatchs();
                break;

            case Constants.APICALL_3:
                getMatchTeams();
                break;

            case Constants.APICALL_4:
                getLeagues();
                break;
        }
    }

    @Override
    public void getTournamentList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                tList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LiveTournamentModel>>() {
                                }.getType())
                );

                if (tList.size() > 0) {
                    tabLength = tList.size();

                    binding.pullToRefresh.setVisibility(View.VISIBLE);
                    binding.tvNoDataMain.setVisibility(View.GONE);

                    LiveTournamentAdapter mAdapter = new LiveTournamentAdapter(getActivity(), tList);
                    binding.viewpager.setAdapter(mAdapter);
                    setTournamentDetails(tList.get(0));
                } else {
                    binding.pullToRefresh.setVisibility(View.GONE);
                    binding.tvNoDataMain.setVisibility(View.VISIBLE);
                    binding.tvNoDataMain.setText(getString(R.string.text_notournamenttextlive1));
                }
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMatchList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                mList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LiveMatchModel>>() {
                                }.getType())
                );

                if (mList.size() > 0) {
                    binding.rrLayout2.setVisibility(View.VISIBLE);

                    binding.rvMatch.setVisibility(View.VISIBLE);
                    binding.mBar.setVisibility(View.GONE);
                    binding.tbTabs.setVisibility(View.VISIBLE);
                    binding.clScore.setVisibility(View.VISIBLE);

                    binding.tvNoLeague.setVisibility(View.GONE);

                    if (tabPosition == 0) {
                        binding.rrLivePoints.setVisibility(View.VISIBLE);
                        binding.clTab2.setVisibility(View.VISIBLE);
                        binding.clMatchTab.setVisibility(View.VISIBLE);
                    } else
                        binding.rrLiveLeagues.setVisibility(View.VISIBLE);


                    initList();
                    matchID = String.valueOf(mList.get(0).getMatchId());
                    getMatchTeams();

                } else {
                    if (tabPosition == 0) {
                        binding.rrLivePoints.setVisibility(View.VISIBLE);
                        binding.clTab2.setVisibility(View.VISIBLE);
                        binding.clMatchTab.setVisibility(View.VISIBLE);
                    } else
                        binding.rrLiveLeagues.setVisibility(View.GONE);

                    binding.rvMatch.setVisibility(View.GONE);
                    binding.clScore.setVisibility(View.GONE);
                    binding.mBar.setVisibility(View.GONE);
                    binding.tvNoLeague.setVisibility(View.VISIBLE);
                    binding.tvNoLeague.setText(getString(R.string.text_nomatchavailable));
                }
            } else {
                binding.rrLayout2.setVisibility(View.GONE);
                binding.tbTabs.setVisibility(View.GONE);
                binding.clScore.setVisibility(View.GONE);

                if (tabPosition == 0) {
                    binding.rrLivePoints.setVisibility(View.VISIBLE);
                    binding.clTab2.setVisibility(View.VISIBLE);
                    binding.clMatchTab.setVisibility(View.VISIBLE);
                } else
                    binding.rrLiveLeagues.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMatchTeamList(JSONObject jsonObject) {

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
                    String txt;

                    txt = pList.get(0).getParticipationTeamName();

                    for (LivePlayerModel data : pList) {
                        if (txt.equals(data.getParticipationTeamName()))
                            t1List.add(data);
                        else
                            t2List.add(data);
                    }

                    binding.tvLeftTeamName.setText(t1List.get(0).getTeamShortName());
                    binding.tvRightTeamName.setText(t2List.get(0).getTeamShortName());

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.blank_jersey);
                    requestOptions.error(R.drawable.blank_jersey);

                    if (t1List.get(0).getTeamImage() != null) {
                        String url = Constants.BASE_IMAGE_URL + t1List.get(0).getTeamImage();
                        Glide.with(getActivity())
                                .setDefaultRequestOptions(requestOptions)
                                .load(url).into(binding.imgLeftTeamLogo);
                    } else
                        binding.imgLeftTeamLogo.setImageResource(R.drawable.blank_jersey);

                    if (t2List.get(0).getTeamImage() != null) {
                        String url = Constants.BASE_IMAGE_URL + t2List.get(0).getTeamImage();
                        Glide.with(getActivity())
                                .setDefaultRequestOptions(requestOptions)
                                .load(url).into(binding.imgRightTeamLogo);
                    } else
                        binding.imgRightTeamLogo.setImageResource(R.drawable.blank_jersey);

                    setTeam1List(t1List);

                } else
                    binding.rrLivePoints.setVisibility(View.GONE);
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getLeagueList(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                LinkedList<UserLeagueModel> leagueList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserLeagueModel>>() {
                                }.getType())
                );

                if (leagueList.size() > 0) {
                    binding.pBar.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.tvListNoData.setVisibility(View.GONE);

                    LiveLeagueListAdapter lAdapter = new LiveLeagueListAdapter(getActivity(), this, leagueList);
                    binding.rvList.setAdapter(lAdapter);
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

    @Override
    public void OnSelectLeague(UserLeagueModel data) {

        String leagueID = data.getLeagueId();

        startActivity(new Intent(getActivity(), LiveLeagueViewActivity.class)
                .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                .putExtra(Constants.TAG_MATCHID, matchID)
                .putExtra(Constants.TAG_LEAGUEID, leagueID)
                .putExtra(Constants.TAG_LEAGUENAME, data.getLeagueName())
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(getResponceAfterInterval);
    }

    private Runnable getResponceAfterInterval = new Runnable() {
        public void run() {
            try {
                if (tabPosition == 0)
                    getMatchTeams();
                else
                    getLeagues();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 300000);
        }
    };

    private void initList() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMatch.setLayoutManager(ll);
        mList.get(0).setSelected(true);
        NewMatchListAdapter mAdapter = new NewMatchListAdapter(mList, this,0);
        binding.rvMatch.setAdapter(mAdapter);

    }

    @Override
    public void onClickMatchList(LiveMatchModel matches) {

        TEAM1NAME = matches.getTeam1ShortName();
        TEAM2NAME = matches.getTeam2ShortName();
        if (matches.isShowScore())
            binding.relativeLayout.setVisibility(View.VISIBLE);
        else
            binding.relativeLayout.setVisibility(View.INVISIBLE);

        binding.tvTeam1.setText(matches.getTeam1Score().replace("~"," "));
        binding.tvTeam2.setText(matches.getTeam2Score().replace("~"," "));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.blank_jersey);
        requestOptions.error(R.drawable.blank_jersey);

        if (matches.getTeam1Image() != null) {
            String url = Constants.BASE_IMAGE_URL + matches.getTeam1Image();
            Glide.with(getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgTeam1);
        } else
            binding.imgTeam1.setImageResource(R.drawable.blank_jersey);

        if (matches.getTeam2Image() != null) {
            String url = Constants.BASE_IMAGE_URL + matches.getTeam2Image();
            Glide.with(getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(binding.imgTeam2);
        } else
            binding.imgTeam2.setImageResource(R.drawable.blank_jersey);


        matchID = String.valueOf(matches.getMatchId());
        if (tabPosition == 1) {
            getMatchTeams();
        } else {
            getLeagues();
        }
        handler.post(getResponceAfterInterval);
    }
}
