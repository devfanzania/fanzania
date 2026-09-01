package com.yorker.fanzania.views.screens.matchcontest.fragments.leagues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.FragmentMcLeagueBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;

import com.yorker.fanzania.dialog.McTeamViewDialog;
import com.yorker.fanzania.dialog.NewTeamViewDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.model.McLeagueModel;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class McLeagueFragment extends BaseFragment<MCLeagueFragmentPresenter>
        implements MCLeagueFragmentPresenter.IMCMainView, McMatchListAdapter.IMatch,McLeagueListAdapter.ILeague {

    private MCLeagueFragmentPresenter presenter;
    private FragmentMcLeagueBinding binding;
    private List<McLeagueModel> list;
    private List<DailyMatchModel> mList;
    private int tournamentId;
    private int matchId;
    private int cMatchId=0;
    private String matchType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_mc_league, container, false);
        return binding.getRoot();
    }

    @Override
    protected MCLeagueFragmentPresenter onCreatePresenter() {
        presenter = new MCLeagueFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, MCLeagueFragmentPresenter presenter) {
        MCLeagueFragmentPresenterComponent component1 = DaggerMCLeagueFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .mCLeagueFragmentApplicationModule(new MCLeagueFragmentApplicationModule(getActivity()))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cMatchId= bundle.getInt(Constants.TAG_MATCHID, 0);
            System.out.println("match id "+cMatchId);
        }

        getMatches();
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getDailyMatches();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    private void getLeagues() {
        if (CheckInternetConnection())
            presenter.getDailyLeagues(tournamentId, matchId);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getMatches();
                break;
            case Constants.APICALL_2:
                getLeagues();
                break;
        }
    }

    @Override
    public void onClickMatchList(DailyMatchModel obj) {
        setUserData(obj);
        tournamentId = obj.getTournamentId();
        matchId = obj.getMatchId();
        matchType = obj.getMatchType();
        getLeagues();
    }

    private void setUserData(DailyMatchModel obj) {


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

                    binding.rvMatches.setVisibility(View.VISIBLE);
                    binding.mBar.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.clMain.setVisibility(View.VISIBLE);
                    initList();
                } else {
                    binding.rvMatches.setVisibility(View.GONE);
                    binding.mBar.setVisibility(View.GONE);
                    binding.clMain.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.tvNoData.setText(jsonObject.getString("statusMessage"));
                }
            } else {
                binding.rvMatches.setVisibility(View.GONE);
                binding.clMain.setVisibility(View.GONE);
                binding.mBar.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.tvNoData.setText(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dailyLeagues(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                list = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<McLeagueModel>>() {
                                }.getType())
                );

                if (list.size() > 0) {

                    if (list.get(0).getTotalTeams() > 0)
                        binding.tvTotalTeams.setText(String.valueOf(list.get(0).getTotalTeams()));
                    else
                        binding.tvTotalTeams.setText("-");

                    binding.rvMatch.setVisibility(View.VISIBLE);
                    binding.pBar.setVisibility(View.GONE);
                    initLeagueList();
                } else {
                    binding.rvMatch.setVisibility(View.GONE);
                    binding.pBar.setVisibility(View.GONE);
                }
            } else {
                binding.rvMatch.setVisibility(View.GONE);
                binding.pBar.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initLeagueList() {
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        McLeagueListAdapter lAdapter = new McLeagueListAdapter(list,presenter.getCustomerID(),this);
        binding.rvMatches.addItemDecoration(new ItemDecoration(getActivity()));
        binding.rvMatches.setAdapter(lAdapter);
    }

    private void initList() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMatch.setLayoutManager(ll);
        McMatchListAdapter mAdapter = new McMatchListAdapter(mList, this,cMatchId);
        binding.rvMatch.setAdapter(mAdapter);
    }

    @Override
    public void OnClickLeagueTeam(McLeagueModel obj) {
        McTeamViewDialog dialog = new McTeamViewDialog(getActivity(), obj.getTournamentId(), obj.getUserTeamId(),
                obj.getUserId(), obj.getName(),matchId,matchType);
        dialog.show(getActivity().getSupportFragmentManager(), "ratereview");
    }

    @Override
    public void OnSetData(McLeagueModel obj) {
        if (obj.getLastMatchPoints() > 0)
            binding.tvMyPoints.setText(String.valueOf(obj.getLastMatchPoints()));
        else
            binding.tvMyPoints.setText("-");

        if (obj.getTeamRank() > 0)
            binding.tvMyRank.setText(String.valueOf(obj.getTeamRank()));
        else
            binding.tvMyRank.setText("-");
    }
}
