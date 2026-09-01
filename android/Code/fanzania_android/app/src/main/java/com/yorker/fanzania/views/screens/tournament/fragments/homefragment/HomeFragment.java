package com.yorker.fanzania.views.screens.tournament.fragments.homefragment;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.FragmentTournamentNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.TeamNameDialog;
import com.yorker.fanzania.dialog.TournamentStatsTeamViewDialog;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.adapter.CustomPagerAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.adapter.LeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.adapter.UpcomingTournamentListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.TopPlayersResponse;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UpcomingTournamentModel;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserTournamentModel;
import com.yorker.fanzania.views.screens.tournament.createteam.CreateTeamActivity;
import com.yorker.fanzania.views.screens.tournament.tournamentstats.TournamentStatsActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter>
        implements HomeFragmentPresenter.IMainView, LeagueListAdapter.ILeague, UpcomingTournamentListAdapter.ITournament,
        TeamNameDialog.ITeamNameDialog {

    private HomeFragmentPresenter presenter;
    //    private FragmentHomeNewBinding binding;
    private FragmentTournamentNewBinding binding;

    public static String tournamentID = null;
    private String userTeamId;
    private String tournamentName;
    private String upcomingTournamentName;
    private String tournamentDate;
    private int tStatus = 0;
    private String upcomingTournamentStatus;

    private LeagueListAdapter lAdapter;
    private int tabLength = 0;
    private Boolean isUpcomingTournament = false;
    private Boolean isDataParsed = false;


    private LinkedList<UserTournamentModel> list;
    private LinkedList<TopPlayersResponse> leagueList;
    private LinkedList<UpcomingTournamentModel> tList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_tournament_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserTournamentList();
        getFunfact();
    }

    private void getUpcomingTournamentList() {
        if (CheckInternetConnection())
            presenter.getUpcomingTournament();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    private void initViews() {

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
                setTournamentDetails(list.get(position));
            }
        });

        binding.imgClose.setOnClickListener(v -> {
            binding.rlFunFact.setVisibility(View.GONE);
        });

        binding.inLeagueList.rvList.setHasFixedSize(true);
        leagueList = new LinkedList<>();
        binding.inLeagueList.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.inLeagueList.rvList.addItemDecoration(new ItemDecoration(getActivity()));
        lAdapter = new LeagueListAdapter(getActivity(), leagueList, this);
        binding.inLeagueList.rvList.setAdapter(lAdapter);

        binding.llSub.setOnClickListener(v->{
            upcomingTournamentName = tList.get(0).getTournamentName();
            upcomingTournamentStatus = tList.get(0).getTournamentStatus();
            new TeamNameDialog(getActivity(), this, tList.get(0), presenter.getCustomerId());
        });

    }

    private void getUserTournamentList() {
        if (CheckInternetConnection())
            presenter.getUserTournamentList();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    @Override
    protected HomeFragmentPresenter onCreatePresenter() {
        presenter = new HomeFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, HomeFragmentPresenter presenter) {
        HomeFragmentPresenterComponent homeFragmentPresenterComponent = DaggerHomeFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .homeFragmentApplicationModule(new HomeFragmentApplicationModule(getActivity()))
                .build();
        homeFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_home_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);

        if (isDataParsed) {
            MontserratRegular tvStats = menu.findItem(R.id.action_item_one).getActionView().findViewById(R.id.tvActionApply);
            tvStats.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_star_new_white, 0, 0);
            tvStats.setText(getString(R.string.text_stats));

            tvStats.setOnClickListener(view -> startActivity(new Intent(getActivity(), TournamentStatsActivity.class)
                    .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                    .putExtra(Constants.TAG_TOURNAMENTSTATUS, tStatus)
                    .putExtra(Constants.TAG_TOURNAMENTNAME, tournamentName)
                    .putExtra(Constants.TAG_USERTEAMID, userTeamId)
                    .putExtra(Constants.TAG_TOURNAMENTDATE, tournamentDate)
            ));

            if (isUpcomingTournament)
                tvStats.setVisibility(View.GONE);
            else
                tvStats.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void getTournamentList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                isDataParsed = true;

                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserTournamentModel>>() {
                                }.getType())
                );
                if (list.size() > 0) {

                    tabLength = list.size();
                    CustomPagerAdapter mAdapter = new CustomPagerAdapter(getActivity(), list);
                    binding.viewpager.setAdapter(mAdapter);

                    for (UserTournamentModel obj : list) {
                        if (obj.getTournamentStatus().equals(Constants.TAG_INPROGRESS)) {
                            binding.viewpager.setCurrentItem(list.indexOf(obj));
                            break;
                        } else if (obj.getTournamentStatus().equals(Constants.TAG_UPCOMING)) {
                            binding.viewpager.setCurrentItem(list.indexOf(obj));
                            break;
                        }
                    }

                    if (presenter.getTournamentID() != null) {
                        String id = presenter.getTournamentID();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTournamentId().equals(id)) {
                                binding.viewpager.setCurrentItem(i);
                                setTournamentDetails(list.get(i));
                                if (i == 0) {
                                    if (tabLength > 0)
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
                                } else {
                                    if (tabLength == (i + 1))
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new_disable);
                                    else if (tabLength > i)
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);

                                    binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);
                                }
                            }
                        }
                    } else {
                        binding.viewpager.setCurrentItem(0);
                        setTournamentDetails(list.get(0));
                    }

                    binding.clTournament.setVisibility(View.VISIBLE);
                    binding.clJoin.setVisibility(View.GONE);
                    binding.pBar.setVisibility(View.GONE);

                } else {
                    getUpcomingTournamentList();
                    binding.clTournament.setVisibility(View.GONE);
                    binding.clJoin.setVisibility(View.GONE);
                    isUpcomingTournament = true;
                    getActivity().invalidateOptionsMenu();
                }
            } else {
                getUpcomingTournamentList();
                binding.clTournament.setVisibility(View.GONE);
                binding.clJoin.setVisibility(View.GONE);
                isUpcomingTournament = true;
                getActivity().invalidateOptionsMenu();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTopPlayerList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                leagueList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<TopPlayersResponse>>() {
                                }.getType())
                );

                if (leagueList.size() > 0) {
                    binding.tvMyMatch2.setText(new StringBuilder()
                            .append(leagueList.get(0).getMatchDetails())
                            .append(" ")
                            .append(getString(R.string.text_top10team))
                    );

                    binding.inLeagueList.rvList.setVisibility(View.VISIBLE);
                    binding.inLeagueList.pBar.setVisibility(View.GONE);
                    binding.inLeagueList.tvNoDataFound.setVisibility(View.GONE);
                    binding.tvMyMatch2.setVisibility(View.VISIBLE);
                    lAdapter.AddData(leagueList);
                } else {
                    binding.inLeagueList.rvList.setVisibility(View.GONE);
                    binding.inLeagueList.pBar.setVisibility(View.GONE);
                    binding.tvMyMatch2.setVisibility(View.GONE);
                    binding.inLeagueList.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inLeagueList.tvNoDataFound.setText("");
                }
            } else {
                binding.inLeagueList.rvList.setVisibility(View.GONE);
                binding.inLeagueList.pBar.setVisibility(View.GONE);
                binding.tvMyMatch2.setVisibility(View.GONE);

                binding.inLeagueList.tvNoDataFound.setVisibility(View.VISIBLE);
                binding.inLeagueList.tvNoDataFound.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUpcomingTournamentList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                tList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UpcomingTournamentModel>>() {
                                }.getType())
                );

                if (tList.size() > 0) {
                    binding.clJoin.setVisibility(View.VISIBLE);
                    binding.pBar.setVisibility(View.GONE);
                    binding.tvTrounamentName.setText(tList.get(0).getTournamentName());
                    String txt=tList.get(0).getTournamentStartDate()+" - "+tList.get(0).getTournamentEndDate();
                    binding.tvTrounamentYear.setText(txt);
                } else{
                    binding.pBar.setVisibility(View.GONE);
                    binding.clJoin.setVisibility(View.GONE);
                    binding.clNodata.setVisibility(View.VISIBLE);
                }
            } else {
                binding.pBar.setVisibility(View.GONE);
                binding.clJoin.setVisibility(View.GONE);
                binding.clNodata.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMatchDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                binding.tvTotalPoint.setText(jsonObject1.getString("TotalPoints"));
                String gRank = String.valueOf(jsonObject1.getInt("TeamGlobalRank"));
                binding.tvGlobalRank.setText(gRank.equals("0") ? "-" : gRank);

                binding.tvMyMatch.setText(new StringBuilder().append(getString(R.string.text_myteam))
                        .append(" | ")
                        .append(jsonObject1.getString("UserTeamName")));

                binding.tvLastMatch.setText(jsonObject1.getString("LastMatchPoints"));
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTournamentDetails(UserTournamentModel data) {

        isUpcomingTournament = data.getTournamentStatus().equals("UPCOMING");
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        presenter.saveTournamentID(data.getTournamentId());

        tournamentID = data.getTournamentId();
        userTeamId = data.getUserTeamId();
        checkStatus(data.getTournamentStatus());

        tournamentName = data.getTournamentName();

        tournamentDate = data.getTournamentStartDate() + " - " + data.getTournamentEndDate();

        binding.inLeagueList.rvList.setVisibility(View.GONE);
        binding.inLeagueList.pBar.setVisibility(View.VISIBLE);
        binding.inLeagueList.tvNoDataFound.setVisibility(View.GONE);

        if (CheckInternetConnection()) {
            presenter.getTopPlayerList(tournamentID);
            presenter.MatchDetails(tournamentID);
        } else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    private void checkStatus(String strStatus) {

        switch (strStatus) {
            case Constants.TAG_COMPLETE:
                tStatus = 0;
                break;

            case Constants.TAG_INPROGRESS:
                tStatus = 1;
                break;

            case Constants.TAG_UPCOMING:
                tStatus = 2;
                break;
        }
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getUserTournamentList();
                break;

            case Constants.APICALL_2:
                presenter.getTopPlayerList(tournamentID);
                presenter.MatchDetails(tournamentID);
                break;

            case Constants.APICALL_3:
                presenter.getUpcomingTournament();
                break;

            case Constants.APICALL_4:
                getFunfact();
                break;
        }
    }

    @Override
    public void onClickLeagueList(TopPlayersResponse data) {
        TournamentStatsTeamViewDialog dialog = new TournamentStatsTeamViewDialog(getActivity(), tournamentID, data.getUserTeamId(),
                presenter.getCustomerId(), data.getUserTeamName(), tStatus);
        dialog.show(getFragmentManager(), "ratereview");
    }

    @Override
    public void onClickTournament(UpcomingTournamentModel data) {
        upcomingTournamentName = data.getTournamentName();
        upcomingTournamentStatus = data.getTournamentStatus();
        new TeamNameDialog(getActivity(), this, data, presenter.getCustomerId());
    }

    @Override
    public void CTPositiveResponse(int userTeamID, String tournamentId) {

        startActivity(new Intent(getActivity(), CreateTeamActivity.class)
                .putExtra(Constants.TAG_USERTEAMID, userTeamID)
                .putExtra(Constants.TAG_TOURNAMENTID, tournamentId)
                .putExtra(Constants.TAG_TOURNAMENTNAME, upcomingTournamentName)
                .putExtra(Constants.TAG_TOURNAMENTSTATUS, upcomingTournamentStatus)
        );
    }

    @Override
    public void getFunFact(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                binding.tvFunFact.setText(jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0).getString("FunMessage"));
                binding.rlFunFact.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFunfact() {
        if (CheckInternetConnection())
            presenter.getFunFact();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_4);
    }
}
