package com.yorker.fanzania.views.screens.matchcontest.fragments.teams;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.FragmentMcTeamBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.McMatchListAdapter;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerSelectionActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.yorker.fanzania.widgets.DateUtils.printDifference;

public class McTeam extends BaseFragment<McTeamFragmentPresenter>
        implements McTeamFragmentPresenter.IMCMainView, McMatchListAdapter.IMatch {

    private McTeamFragmentPresenter presenter;
    private FragmentMcTeamBinding binding;
    private ArrayList<DailyMatchModel> mList;
    private int tournamentId;
    private int matchId;
    private String userTeamId;
    private String matchType;
    private String header;
    private String matchDate;
    private int cMatchId=0;
    private ArrayList<Integer> fieldList;
    private Boolean isCompletedMatch = false;
    String Team1ShortName, Team2ShortName, predictTeam = null;
    McMatchListAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_mc_team, container, false);
        return binding.getRoot();
    }

    @Override
    protected McTeamFragmentPresenter onCreatePresenter() {
        presenter = new McTeamFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, McTeamFragmentPresenter presenter) {
        McTeamFragmentPresenterComponent component1 = DaggerMcTeamFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .mcTeamFragmentApplicationModule(new McTeamFragmentApplicationModule(getActivity()))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getMatches();
                break;

            case Constants.APICALL_2:
                getPlayersPoints();
                break;

            case Constants.APICALL_3:
                getTeamPlayers();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        cancelTimer();
        getMatches();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cMatchId= bundle.getInt(Constants.TAG_MATCHID, 0);
            System.out.println("match id "+cMatchId);
        }

        binding.tvManage.setOnClickListener(v -> startActivity(new Intent(getActivity(), PlayerSelectionActivity.class)
                .putExtra(Constants.TAG_MATCHID, matchId)
                .putExtra(Constants.TAG_MATCHTYPE, matchType)
                .putExtra(Constants.TAG_TOURNAMENTID, tournamentId)
                .putExtra(Constants.TAG_MATCHDATE, matchDate)
                .putExtra(Constants.TAG_HEADER, header)
                .putExtra("Team1ShortName", Team1ShortName)
                .putExtra("Team2ShortName", Team2ShortName)
                .putExtra("predictTeam", predictTeam)
        ));
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getDailyMatches();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    @Override
    public void onClickMatchList(DailyMatchModel obj) {
        cancelTimer();
        try {
            SimpleDateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            normalDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date2 = normalDateFormat.parse(obj.getMatchDate());
            Date date1 = normalDateFormat.parse(normalDateFormat.format(Calendar.getInstance().getTime()));
            printDifference(date1, date2,binding.tvMyPoints);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        header=obj.getTeam1ShortName()+" v "+obj.getTeam2ShortName();
        tournamentId =obj.getTournamentId();
        matchId = obj.getMatchId();
        Team1ShortName = obj.getTeam1ShortName();
        Team2ShortName = obj.getTeam2ShortName();
        userTeamId = String.valueOf(obj.getUserTeamId());
        matchType = String.valueOf(obj.getMatchType());
        matchDate=obj.getMatchDate();

        if (obj.getMatchStatus().equals("UPCOMING"))
            binding.tvManage.setVisibility(View.VISIBLE);
        else
            binding.tvManage.setVisibility(View.GONE);

        if (obj.getMatchStatus().equals("COMPLETE")) {
            isCompletedMatch = true;
            getPlayersPoints();
        } else {
            isCompletedMatch = false;
            getTeamPlayers();
        }
    }

    private void getTeamPlayers() {
        if (CheckInternetConnection())
            presenter.getPlayers(tournamentId, userTeamId, matchType);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    private void getPlayersPoints() {
        if (CheckInternetConnection())
            presenter.getPlayersWithPoints(tournamentId, matchId, userTeamId, matchType);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
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
                    if (cMatchId==0)
                        mList.get(0).setSelected(true);

                    binding.clMain.setVisibility(View.VISIBLE);
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.pBar.setVisibility(View.GONE);
                    binding.mBar.setVisibility(View.GONE);
                    initList();
                } else {
                    binding.clMain.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.pBar.setVisibility(View.GONE);
                    binding.tvNoData.setText(jsonObject.getString("statusMessage"));
                }
            } else {
                binding.clMain.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.pBar.setVisibility(View.GONE);
                binding.tvNoData.setText(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playerWithPoints(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {


            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void players(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                LinkedList<McTeamModel> playerList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<McTeamModel>>() {
                                }.getType())
                );

                if (playerList.size() > 0){
                    predictTeam = playerList.get(0).getWinnerPrediction();
                    setPlayerData(playerList);
                    binding.tvPrediction.setText(predictTeam+"\nPrediction");

                }
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerData(LinkedList<McTeamModel> playerList) {
//        Collections.sort(playerList, (lhs, rhs) -> lhs.getPlayerSpeciality().compareTo(rhs.getPlayerSpeciality()));

        fieldList = new ArrayList<>();

        binding.tvTotalTeam.setText(playerList.get(0).getTotalTeams());

        for (McTeamModel players : playerList) {
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
                    } else
                        addPlayerData(players);


                    break;

                case Constants.TAG_PLAYERTYPE_BATSMAN:
                    addPlayerData(players);
                    break;

                case Constants.TAG_PLAYERTYPE_BLOWER:
                    addPlayerData(players);
                    break;
            }
        }
    }

    public void addPlayerData(McTeamModel players) {
        Log.e("battingTeam",players.getBattingTeam());
        if (mAdapter != null){
            mAdapter.setBattingTeam(players.getBattingTeam());
            mAdapter.notifyDataSetChanged();
        }

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
        }
        else if (!fieldList.contains(binding.tvPlayer4Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer4, binding.tvPlayer4Name,
                    binding.tvPlayer4Captain, binding.tvPlayer4Point, binding.imgPlayer4Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer4Name.getId());
        }else if (!fieldList.contains(binding.tvPlayer3Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer3, binding.tvPlayer3Name,
                    binding.tvPlayer3Captain, binding.tvPlayer3Point, binding.imgPlayer3Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer3Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer2, binding.tvPlayer2Name,
                    binding.tvPlayer2Captain, binding.tvPlayer2Point, binding.imgPlayer2Delete, isCompletedMatch);
            fieldList.add(binding.tvPlayer2Name.getId());
        }else if (!fieldList.contains(binding.tvPlayer9Name.getId())) {
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

    private void initList() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMatch.setLayoutManager(ll);
        mAdapter = new McMatchListAdapter(mList, this,cMatchId);
        binding.rvMatch.setAdapter(mAdapter);
    }
}
