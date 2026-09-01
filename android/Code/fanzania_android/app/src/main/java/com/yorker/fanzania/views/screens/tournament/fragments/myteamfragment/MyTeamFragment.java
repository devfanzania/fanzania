package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.FragmentMyTeamNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.ChangeTeamNameDialog;
import com.yorker.fanzania.dialog.LifelineDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.model.PowerPlayLifeline;
import com.yorker.fanzania.views.model.PowerPlayLifelinePost;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserTournamentModel;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.CustomPagerAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.adapter.MatchListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.model.Matches;
import com.yorker.fanzania.views.screens.tournament.createteam.CreateTeamActivity;
import com.yorker.fanzania.views.screens.tournament.manageteam.ManageTeamActivity;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;
import com.yorker.fanzania.views.screens.tournament.teamstats.TeamStatsActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyTeamFragment extends BaseFragment<MyTeamFragmentPresenter>
        implements MyTeamFragmentPresenter.IMainView, MatchListAdapter.IMatch {

    private MyTeamFragmentPresenter presenter;
    private FragmentMyTeamNewBinding binding;

    private LinkedList<UserTournamentModel> list;
    private LinkedList<PowerPlayLifeline> powerPlayLifelineLinkedList;
    private LinkedList<Matches> matchList = new LinkedList<>();
    private ArrayList<Integer> fieldList;

    private String tournamentID;
    private String matchId;
    private String userTeamId;
    private String tournamentName;
    private String teamName, newTeamName;
    private String tournamentStatus;
    private String tournamentDate;

    private Boolean isActiveTournament = false;
    private Boolean isUpcomingTournament = false;
    private Boolean isCompletedMatch = false;
    private Boolean isFirstTime = false;
    private Boolean isUpComingMatchSelected = false;
    private Boolean isDataParsed = false;

    private int tabLength = 0;
    private int avgPoints = 0;

    private MenuItem menuItem1;
    private boolean isTeamCreated = false;

    private MatchListAdapter mAdapter;
    String Team1ShortName, Team2ShortName;

    private boolean isDialogShown = false;

    @Override
    protected MyTeamFragmentPresenter onCreatePresenter() {
        Log.d("jkjghjghjeygygeyghjefboooifher","onCreatePresenter");
        presenter = new MyTeamFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, MyTeamFragmentPresenter presenter) {
        Log.d("jkjghjghjeygygeyghjefboooifher","onCreatePresenter");
        MyTeamFragmentPresenterComponent myTeamFragmentPresenterComponent = DaggerMyTeamFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .myTeamFragmentApplicationModule(new MyTeamFragmentApplicationModule(getActivity()))
                .build();
        myTeamFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("jkjghjghjeygygeyghjefboooifher","onCreateView");
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_team_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("jkjghjghjeygygeyghjefboooifher","onActivityCreated");
        initView();
        initListners();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("jkjghjghjeygygeyghjefboooifher","onResume");
        Log.d("khfjshfsgfsdh", "dsfdsfdsfsdfsdf");

        isUpComingMatchSelected = false;
        getuserTournamentList();
    }

    private void disableCLick() {
        Log.d("jkjghjghjeygygeyghjefboooifher","disableCLick");
        if (isDataParsed) {
            menuItem1.setEnabled(false);
        }
        requireActivity().invalidateOptionsMenu();
        binding.leftNav.setEnabled(false);
        binding.rightNav.setEnabled(false);
    }

    private void enableCLick() {
        Log.d("jkjghjghjeygygeyghjefboooifher","enableCLick");
        if (!isFirstTime) {
            isFirstTime = true;
        }

        if (isDataParsed) {
            menuItem1.setEnabled(true);
        }
        requireActivity().invalidateOptionsMenu();
        binding.leftNav.setEnabled(true);
        binding.rightNav.setEnabled(true);
    }

    private void initListners() {
        Log.d("jkjghjghjeygygeyghjefboooifher","initListners");

        binding.teamNameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ChangeTeamNameDialog(requireActivity(), teamName, new ChangeTeamNameDialog.UpdateTeamNameCallback() {
                    @Override
                    public void onConfirmClick(String teamName_) {
                        newTeamName = teamName_;
                        updateTeamName();
                    }
                });
            }
        });

        binding.btnLifeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LifelineDialog(getContext(), powerPlayLifelineLinkedList.get(0), new LifelineDialog.UpdateLifeLineCallback() {
                    @Override
                    public void onConfirmClick(PowerPlayLifelinePost powerPlayLifelinePost) {
                        powerPlayLifelinePost.setTournamentId(Integer.parseInt(tournamentID));
                        powerPlayLifelinePost.setUserTeamId(Integer.parseInt(userTeamId));

                        presenter.UpdateUserPowerPlay(powerPlayLifelinePost);
                    }
                });
            }
        });

        binding.tvTeamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeTeamNameDialog(getContext(), teamName, new ChangeTeamNameDialog.UpdateTeamNameCallback() {
                    @Override
                    public void onConfirmClick(String teamName_) {
                        newTeamName = teamName_;
                        updateTeamName();
                    }
                });
            }
        });

        binding.tbTournament.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                disableCLick();
                binding.clTeamDetails.setVisibility(View.GONE);
                binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                binding.llLayout2.setVisibility(View.GONE);
                isUpComingMatchSelected = false;
                setTournamentDetails(list.get(position));
            }
        });

        binding.leftNav.setOnClickListener(v -> {
            int tab = binding.tbTournament.getCurrentItem();
            if (tab > 0) {
                tab--;
                binding.tbTournament.setCurrentItem(tab);
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
            } else if (tab == 0) {
                binding.tbTournament.setCurrentItem(tab);
            }
            if (tab == 0) {
                binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new_disable);
            } else {
                binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);
            }
        });

        binding.rightNav.setOnClickListener(v -> {
            int tab = binding.tbTournament.getCurrentItem();
            tab++;
            binding.tbTournament.setCurrentItem(tab);
            binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);

            if (tab < (tabLength - 1)) {
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
            } else {
                binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new_disable);
            }
        });

        binding.btnManage.setOnClickListener(view -> gotoEditPage());
    }

    private void getCurrentMatchPlayers() {
        Log.d("jkjghjghjeygygeyghjefboooifher","getCurrentMatchPlayers");
        if (CheckInternetConnection()) {
            presenter.getUserTeamInfo(tournamentID, userTeamId, matchId);
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_4);
        }
    }

    private void getMatchPlayers() {
        Log.d("jkjghjghjeygygeyghjefboooifher","getMatchPlayers");
        if (CheckInternetConnection()) {
            presenter.MatchDetails(tournamentID, matchId, userTeamId);
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
        }
    }

    private void getuserTournamentList() {
        Log.d("jkjghjghjeygygeyghjefboooifher","getuserTournamentList");
        if (CheckInternetConnection()) {
            presenter.getUserTournamentList();
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
        }
    }

    private void getPowerPlayLifeLine() {
        Log.d("jkjghjghjeygygeyghjefboooifher","getPowerPlayLifeLine");
        if (CheckInternetConnection()) {
            presenter.getPowerplayLifeline(tournamentID, userTeamId);
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
        }
    }

    private void updateTeamName() {
        Log.d("jkjghjghjeygygeyghjefboooifher","updateTeamName");
        Log.d("khfjshfsgfsdh", "qqq ");
        if (CheckInternetConnection()) {
            binding.pbField.setVisibility(View.VISIBLE);
            presenter.updateTeamName(tournamentID, userTeamId, newTeamName);
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_5);
        }
    }

    private void initView() {
        Log.d("jkjghjghjeygygeyghjefboooifher","initView");
//        ((HomeActivity) getActivity()).binding.inAppBar.inToolbar.toolbarTitle.setText(getString(R.string.title_myteam));
        fieldList = new ArrayList<>();
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ll.setAutoMeasureEnabled(false);
        binding.rvMatch.setLayoutManager(ll);

        mAdapter = new MatchListAdapter(getActivity(), matchList, isActiveTournament, this);
        binding.rvMatch.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.teammenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("jkjghjghjeygygeyghjefboooifher","onCreateOptionsMenu");
        MenuItem menuItem = menu.findItem(R.id.itManageTeam);
        menuItem.setVisible(false);
        menuItem1 = menu.findItem(R.id.itTeamStats);

        if (isDataParsed) {

            MontserratRegular tvstats = menuItem1.getActionView().findViewById(R.id.tvActionApply);
            tvstats.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_star_new_white, 0, 0);
            tvstats.setText(getString(R.string.text_stats));
            tvstats.setOnClickListener(view -> gotoStatsPage());

            if (!isFirstTime) {
                menuItem1.setEnabled(false);
            }

            if (isUpcomingTournament) {
                menuItem1.setVisible(false);
            } else {
                menuItem1.setVisible(true);
            }
        } else {
            menuItem1.setVisible(false);
        }

    }

    private void gotoEditPage() {
        Log.d("jkjghjghjeygygeyghjefboooifher","gotoEditPage");
        if (!isTeamCreated) {
            try {
                startActivity(new Intent(getActivity(), CreateTeamActivity.class)
                        .putExtra(Constants.TAG_USERTEAMID, Integer.parseInt(userTeamId))
                        .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                        .putExtra(Constants.TAG_TOURNAMENTNAME, tournamentName)
                        .putExtra(Constants.TAG_TEAMNAME, teamName)
                        .putExtra(Constants.TAG_TOURNAMENTSTATUS, tournamentStatus)
                        .putExtra("Team1ShortName", Team1ShortName)
                        .putExtra("Team2ShortName", Team2ShortName)
                );
            }catch (Exception ignored){

            }

        } else {
            startActivity(new Intent(getActivity(), ManageTeamActivity.class)
                    .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                    .putExtra(Constants.TAG_MATCHID, matchId)
                    .putExtra(Constants.TAG_USERTEAMID, userTeamId)
                    .putExtra(Constants.TAG_TEAMNAME, teamName)
                    .putExtra(Constants.TAG_TOURNAMENTNAME, tournamentName)
                    .putExtra(Constants.TAG_TOURNAMENTSTATUS, tournamentStatus)
                    .putExtra("Team1ShortName", Team1ShortName)
                    .putExtra("Team2ShortName", Team2ShortName)
            );
        }
    }

    private void gotoStatsPage() {
        Log.d("jkjghjghjeygygeyghjefboooifher","gotoStatsPage");
        startActivity(new Intent(getActivity(), TeamStatsActivity.class)
                .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                .putExtra(Constants.TAG_TOURNAMENTNAME, tournamentName)
                .putExtra(Constants.TAG_USERTEAMID, userTeamId)
                .putExtra(Constants.TAG_TEAMNAME, teamName)
                .putExtra(Constants.TAG_TOURNAMENTDATE, tournamentDate)
        );
    }

    @Override
    public void RetryResponse(String type) {
        Log.d("jkjghjghjeygygeyghjefboooifher","RetryResponse");
        switch (type) {
            case Constants.APICALL_1:
                presenter.getUserTournamentList();
                break;

            case Constants.APICALL_2:
                presenter.getTournamentMatchList(tournamentID);
                break;

            case Constants.APICALL_3:
                presenter.MatchDetails(tournamentID, matchId, userTeamId);
                break;

            case Constants.APICALL_4:
                presenter.getUserTeamInfo(tournamentID, userTeamId, matchId);
                break;

            case Constants.APICALL_5:
                presenter.updateTeamName(tournamentID, userTeamId, teamName);
                break;
        }
    }

    @Override
    public void updatePowerPlayLifeLine(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","updatePowerPlayLifeLine");
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                getPowerPlayLifeLine();
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            } else {
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPowerPlayLifeLine(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","getPowerPlayLifeLine");
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                powerPlayLifelineLinkedList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PowerPlayLifeline>>() {
                                }.getType())
                );
                binding.btnLifeline.setVisibility(View.VISIBLE);
                binding.imgLifeline.setVisibility(View.VISIBLE);

            } else {
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTournamentList(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","getTournamentList");
        disableCLick();
        Log.d("khfjshfsgfsdh", "getTournamentList:: " + jsonObject.toString());
        try {
            Log.d("khfjshfsgfsdh", "getTournamentList12 try:: " + jsonObject.toString());
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                Log.d("khfjshfsgfsdh", "if:: ");

                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserTournamentModel>>() {
                                }.getType())
                );
                Log.d("khfjshfsgfsdh", "list.size():: " + list.size());
                if (list.size() > 0) {

                    tabLength = list.size();

                    binding.llMain.setVisibility(View.VISIBLE);
                    binding.tvNoDataFound.setVisibility(View.GONE);

                    CustomPagerAdapter mAdapter = new CustomPagerAdapter(getActivity(), list);
                    binding.tbTournament.setAdapter(mAdapter);
                    Log.d("khfjshfsgfsdh", "presenter.getTournamentID():: " + presenter.getTournamentID());
                    if (presenter.getTournamentID() != null) {
                        String id = presenter.getTournamentID();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTournamentId().equals(id)) {
                                Log.d("khfjshfsgfsdh", "pfiiihdjfjdg: ");
                                binding.tbTournament.setCurrentItem(i);
                                setTournamentDetails(list.get(i));
                                if (i == 0) {
                                    if (tabLength > 0) {
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);
                                    }
                                } else {
                                    if (tabLength == (i + 1)) {
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new_disable);
                                    } else if (tabLength > i) {
                                        binding.rightNav.setImageResource(R.drawable.ic_right_arrow_new);

                                    }
                                    binding.leftNav.setImageResource(R.drawable.ic_left_arrow_new);
                                }
                            }
                        }
                    } else {
                        Log.d("khfjshfsgfsdh", "presenter.else():: " + presenter.getTournamentID());
                        binding.tbTournament.setCurrentItem(0);
                        setTournamentDetails(list.get(0));
                    }
                } else {
                    Log.d("khfjshfsgfsdh", "presenter.elsess():: " + presenter.getTournamentID());
                    binding.llMain.setVisibility(View.GONE);
                    binding.tvNoDataFound.setVisibility(View.VISIBLE);
                    String text = getString(R.string.text_hello) + " " + presenter.getName() + " " + getString(R.string.text_restnodatatourname);
                    binding.tvNoDataFound.setText(text);
                    isUpcomingTournament = true;
                    getActivity().invalidateOptionsMenu();
                }

                getPowerPlayLifeLine();
            } else {
                Log.d("khfjshfsgfsdh", "zcczc.elsess():: " + presenter.getTournamentID());
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            Log.d("khfjshfsgfsdh", "getTournamentList12 e:: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateTeamName(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","updateTeamName");
        Log.d("khfjshfsgfsdh", "qqwwq ");
        binding.pbField.setVisibility(View.GONE);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                binding.tvTeamName.setText(newTeamName);
            } else {
                binding.tvTeamName.setText(teamName);
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeamInfo(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","getTeamInfo");
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                isDataParsed = true;
                getActivity().invalidateOptionsMenu();

                System.out.println("team players " + jsonObject.toString());
                LinkedList<PlayerDataType> playerList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (playerList.size() > 0) {
                    if (isCompletedMatch) {
                        binding.tvMyRank.setVisibility(View.VISIBLE);
                        avgPoints = playerList.get(0).getAveragePoints();
                        String text = playerList.get(0).getTotalPoints() + " | " + avgPoints;
                        setPoints(text);
                    } else {
                        if (isUpcomingTournament) {
                            binding.tvMyRank.setVisibility(View.GONE);
                            binding.imgInfinity.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvMyRank.setVisibility(View.VISIBLE);
                            binding.imgInfinity.setVisibility(View.GONE);
                            binding.tvMyRank.setText(String.valueOf(playerList.get(0).getSubsLeft()));
                        }
                        binding.tvPointTxt.setText(getString(R.string.text_transfer));
                    }

                    if (playerList.get(0).isAutoPilotUsed()) {
                        binding.tvNitro.setText(getString(R.string.text_autocaptain));
                    } else if (playerList.get(0).isPainKillerUsed()) {
                        binding.tvNitro.setText(getString(R.string.text_painkiller));
                    } else if (playerList.get(0).isNitroUsed()) {
                        binding.tvNitro.setText(getString(R.string.text_nitros));
                    } else {
                        binding.tvNitro.setText("-");
                    }
                    isTeamCreated = true;

                    if (isActiveTournament) {
                        binding.btnManage.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnManage.setVisibility(View.GONE);
                    }
                    binding.rrMain.removeView(binding.clTeamDetails);
                    binding.rrMain.addView(binding.clTeamDetails);
                    setPlayerData(playerList);
                } else {
                    enableCLick();
                    Log.d("khfjshfsgfsdh", "qqq sc");
                    binding.btnManage.setVisibility(View.VISIBLE);
                    binding.pbField.setVisibility(View.GONE);
                    binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                    binding.clTeamDetails.setVisibility(View.GONE);
                    binding.llLayout2.setVisibility(View.GONE);
                }
            } else{
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerData(LinkedList<PlayerDataType> playerList) {
        Log.d("jkjghjghjeygygeyghjefboooifher","setPlayerData");
//        Collections.sort(playerList, (lhs, rhs) -> lhs.getPlayerSpeciality().compareTo(rhs.getPlayerSpeciality()));

        fieldList = new ArrayList<>();
        binding.tvPrediction.setText(playerList.get(0).getWinnerPrediction());
        if (playerList.get(0).getWinnerPrediction().length() > 0 && isCompletedMatch) {
            if (playerList.get(0).isWinnerPredictionStatus()) {
                binding.imgTick.setVisibility(View.VISIBLE);
                binding.imgTick.setBackgroundResource(R.drawable.ic_tick);
            } else {
                binding.imgTick.setVisibility(View.VISIBLE);
                binding.imgTick.setBackgroundResource(R.drawable.ic_clear_red);
            }
        } else {
            binding.imgTick.setVisibility(View.GONE);
        }
        for (PlayerDataType players : playerList) {
            switch (players.getPlayerSpeciality()) {
                case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                    if (!fieldList.contains(binding.tvPlayer11Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer11, binding.tvPlayer11Name,
                                binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11Delete, isCompletedMatch);
                        fieldList.add(binding.tvPlayer11Name.getId());
                    } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer10, binding.tvPlayer10Name,
                                binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10Delete, isCompletedMatch);
                        fieldList.add(binding.tvPlayer10Name.getId());
                    } else {
                        presenter.setPlayerData(players, binding.imgPlayer9, binding.tvPlayer9Name,
                                binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9Delete, isCompletedMatch);
                        fieldList.add(binding.tvPlayer9Name.getId());
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_WICKETKEEPER:

                    if (!fieldList.contains(binding.tvPlayer1Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer1, binding.tvPlayer1Name,
                                binding.tvPlayer1Captain, binding.tvPlayer1Point, binding.imgPlayer1Delete, isCompletedMatch);
                        fieldList.add(binding.tvPlayer1Name.getId());
                    } else {
                        addPlayerData(players);
                    }
//                    else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
//                        presenter.setPlayerData(players, binding.imgPlayer2, binding.tvPlayer2Name,
//                                binding.tvPlayer2Captain, binding.tvPlayer2Point, binding.imgPlayer2Delete, isCompletedMatch);
//                        fieldList.add(binding.tvPlayer2Name.getId());
//                    } else {
//                        presenter.setPlayerData(players, binding.imgPlayer4, binding.tvPlayer4Name,
//                                binding.tvPlayer4Captain, binding.tvPlayer4Point, binding.imgPlayer4Delete, isCompletedMatch);
//                        fieldList.add(binding.tvPlayer4Name.getId());
//                    }

                    break;

                case Constants.TAG_PLAYERTYPE_BATSMAN:
                    addPlayerData(players);
                    break;

                case Constants.TAG_PLAYERTYPE_BLOWER:
                    addPlayerData(players);
                    break;
            }
        }

        binding.clTeamDetails.setVisibility(View.VISIBLE);
        binding.clTeamDetailsBlank.setVisibility(View.GONE);
        binding.llLayout2.setVisibility(View.VISIBLE);
        enableCLick();
    }

    public void addPlayerData(PlayerDataType players) {
        Log.d("jkjghjghjeygygeyghjefboooifher","addPlayerData");
        if (!fieldList.contains(binding.tvPlayer8Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer8, binding.tvPlayer8Name,
                    binding.tvPlayer8Captain, binding.tvPlayer8Point, binding.imgPlayer8Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer8Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer7Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer7, binding.tvPlayer7Name,
                    binding.tvPlayer7Captain, binding.tvPlayer7Point, binding.imgPlayer7Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer7Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer6Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer6, binding.tvPlayer6Name,
                    binding.tvPlayer6Captain, binding.tvPlayer6Point, binding.imgPlayer6Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer6Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer5Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer5, binding.tvPlayer5Name,
                    binding.tvPlayer5Captain, binding.tvPlayer5Point, binding.imgPlayer5Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer5Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer4Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer4, binding.tvPlayer4Name,
                    binding.tvPlayer4Captain, binding.tvPlayer4Point, binding.imgPlayer4Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer4Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer3Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer3, binding.tvPlayer3Name,
                    binding.tvPlayer3Captain, binding.tvPlayer3Point, binding.imgPlayer3Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer3Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer2, binding.tvPlayer2Name,
                    binding.tvPlayer2Captain, binding.tvPlayer2Point, binding.imgPlayer2Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer2Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer9Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer9, binding.tvPlayer9Name,
                    binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer9Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer10, binding.tvPlayer10Name,
                    binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer10Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer11Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer11, binding.tvPlayer11Name,
                    binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer11Name.getId());
        }
    }

    @Override
    public void getTournamentMatchList(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","getTournamentMatchList");
        Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  getTournamentMatchList1233: ");
        try {
            Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  tgdfd: "+jsonObject);
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                matchList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<Matches>>() {
                                }.getType())
                );
                Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  matchList: " + matchList.size());

                if (matchList.size() > 0) {
                    binding.tvNoMatch.setVisibility(View.GONE);
                    LinkedList<Matches> matchesLinkedList = new LinkedList<>();
                    for (Matches data : matchList) {
                        if (!data.getMatchComplete()) {
                            matchesLinkedList.add(data);
                        }

                        if (isActiveTournament) {
                            if (data.getMatchStatus().equals("Live")) {
                                if (!isUpComingMatchSelected) {
                                    isUpComingMatchSelected = true;
                                    binding.rvMatch.scrollToPosition(matchList.indexOf(data));
                                }
                            } else if (data.getMatchStatus().equals("UPCOMING")) {
                                if (!isUpComingMatchSelected) {
                                    isUpComingMatchSelected = true;
                                    binding.rvMatch.scrollToPosition(matchList.indexOf(data));
                                }
                            }
                        }
                    }
                    mAdapter.AddData(matchList, isActiveTournament);
                } else {
                    Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  dfsf: ");
                    enableCLick();
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setText(getString(R.string.text_noleagueavailable));
                }
            } else {
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            Log.d("khfjshfsgfsdh", "pfiiihdjfjdg  ewew: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void getMatchDetails(JSONObject jsonObject) {
        Log.d("jkjghjghjeygygeyghjefboooifher","getMatchDetails");
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                isDataParsed = true;
                getActivity().invalidateOptionsMenu();
                LinkedList<PlayerDataType> playerList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (playerList.size() > 0) {
                    avgPoints = playerList.get(0).getAveragePoints();
                    String TotalPoints = playerList.get(0).getMatchTotalPoints() + " | " + avgPoints;
                    setPoints(TotalPoints);

                    if (playerList.get(0).isAutoPilotUsed()) {
                        binding.tvNitro.setText(getString(R.string.text_autocaptain));
                    }else if (playerList.get(0).isPainKillerUsed()) {
                        binding.tvNitro.setText(getString(R.string.text_painkiller));
                    }else if (playerList.get(0).getNitroMultiplier() > 0) {
                        binding.tvNitro.setText(getString(R.string.text_nitros));
                    }else {
                        binding.tvNitro.setText("-");
                    }
                    binding.rrMain.removeView(binding.clTeamDetails);
                    binding.rrMain.addView(binding.clTeamDetails);
                    setPlayerData(playerList);
                } else {
                    enableCLick();
                    Log.d("khfjshfsgfsdh", "qqqscc ");
                    binding.pbField.setVisibility(View.GONE);
                    binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                    binding.clTeamDetails.setVisibility(View.GONE);
                    binding.llLayout2.setVisibility(View.GONE);
                }
            } else {
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }} catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPoints(String totalPoints) {
        Log.d("jkjghjghjeygygeyghjefboooifher","setPoints");
        binding.tvMyRank.setText(totalPoints);
        binding.tvPointTxt.setText(getString(R.string.text_pointavrpoint));
    }

    private void setTournamentDetails(UserTournamentModel data) {
        Log.d("jkjghjghjeygygeyghjefboooifher","setTournamentDetails "+ data.getTournamentId());
        presenter.saveTournamentID(data.getTournamentId());

        isActiveTournament = !data.getTournamentStatus().equals("COMPLETE");

        isUpcomingTournament = data.getTournamentStatus().equals("UPCOMING");

        requireActivity().invalidateOptionsMenu();

        tournamentID = data.getTournamentId();
        userTeamId = data.getUserTeamId();
        tournamentName = data.getTournamentName();
        tournamentStatus = data.getTournamentStatus();

        tournamentDate = data.getTournamentStartDate() + " - " + data.getTournamentEndDate();

        teamName = data.getUserTeamName();
        binding.tvTeamName.setText(teamName);

        if (CheckInternetConnection()) {
            presenter.getTournamentMatchList(tournamentID);
        } else {
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
        }
    }

    @Override
    public void onClickMatchList(Matches matches) {
        Log.d("jkjghjghjeygygeyghjefboooifher","onClickMatchList");
        matchId = matches.getMatchId();
        Team1ShortName = matches.getTeam1ShortName();
        Team2ShortName = matches.getTeam2ShortName();

        if (matches.getMatchStatus().equals("COMPLETE")) {
            isCompletedMatch = true;
            disableCLick();
            getMatchPlayers();
            fieldList = new ArrayList<>();
            binding.clTeamDetails.setVisibility(View.GONE);
            binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
            binding.llLayout2.setVisibility(View.GONE);
        } else {
            isCompletedMatch = false;
            disableCLick();
            fieldList = new ArrayList<>();
            getCurrentMatchPlayers();
        }
    }
}
