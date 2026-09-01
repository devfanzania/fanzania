package com.yorker.fanzania.views.screens.matchcontest.fragments.home;

import android.annotation.SuppressLint;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.FragmentHomeMatchContestBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.TournamentFilterDialog;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.DailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.TournamentModel;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.UpcomingDailyMatchModel;
import com.yorker.fanzania.views.screens.matchcontest.myallmatches.MyAllMatchesActivity;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerSelectionActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.yorker.fanzania.widgets.DateUtils.getFullDateFromISO;
import static com.yorker.fanzania.widgets.DateUtils.printDifference;

public class HomeMatchContest extends BaseFragment<MCHomeFragmentPresenter>
        implements MCHomeFragmentPresenter.IMCMainView, TournamentFilterDialog.ITeamFilterDialog, HomeMatchListListAdapter.itemCallBack {

    private MCHomeFragmentPresenter presenter;
    private FragmentHomeMatchContestBinding binding;
    private List<UpcomingDailyMatchModel> list;
    private List<UpcomingDailyMatchModel> fList;
    private List<TournamentModel> tList = new ArrayList<>();
    private List<String> tournamentName = new ArrayList<>();
    private DailyMatchModel obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_match_contest, container, false);
        return binding.getRoot();
    }

    @Override
    protected MCHomeFragmentPresenter onCreatePresenter() {
        presenter = new MCHomeFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, MCHomeFragmentPresenter presenter) {
        MCHomeFragmentPresenterComponent homeFragmentPresenterComponent = DaggerMCHomeFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .mCHomeFragmentApplicationModule(new MCHomeFragmentApplicationModule(getActivity()))
                .build();
        homeFragmentPresenterComponent.inject(presenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvSeeAllMyMatches.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyAllMatchesActivity.class)));

        binding.tvFilter.setOnClickListener(v -> new TournamentFilterDialog(getActivity(), this, tList, tournamentName));

        binding.linearLayout.setOnClickListener(v->{
            if (obj!=null){

                switch (obj.getMatchStatus()) {
                    case "UPCOMING":
                        String txt = obj.getTeam1ShortName() + " v " + obj.getTeam2ShortName();
                        startActivity(new Intent(getActivity(), PlayerSelectionActivity.class)
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
                        startActivity(new Intent(getActivity(), HomeActivity.class)
                                .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                                .putExtra(Constants.TAG_INDEX, 1)
                        );
                        break;
                    case "Live":
                        startActivity(new Intent(getActivity(), HomeActivity.class)
                                .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                                .putExtra(Constants.TAG_INDEX, 2)
                        );
                        break;
                }
            }
        });

        binding.imgClose.setOnClickListener(v->{
            binding.rlFunFact.setVisibility(View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        cancelTimer();

        getFilterData();
        callDailyMatch();
        fetFunfact();
        callUpcomingDailyMatches();
    }

    private void fetFunfact() {
        if (CheckInternetConnection())
            presenter.getFunFact();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_4);
    }

    private void getFilterData() {
        if (CheckInternetConnection())
            presenter.teamFilter();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    private void callDailyMatch() {
        if (CheckInternetConnection())
            presenter.getDailyMatches();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    private void callUpcomingDailyMatches() {
        if (CheckInternetConnection())
            presenter.getUpcomingDailyMatches();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    private void initList(List<UpcomingDailyMatchModel> list) {
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        HomeMatchListListAdapter lAdapter = new HomeMatchListListAdapter(list,this);
        binding.rvMatches.setAdapter(lAdapter);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                callUpcomingDailyMatches();
                break;

            case Constants.APICALL_2:
                callDailyMatch();
                break;

            case Constants.APICALL_3:
                getFilterData();
                break;

            case Constants.APICALL_4:
                fetFunfact();
                break;
        }
    }

    @Override
    public void upcomingDailyMatches(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                list = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UpcomingDailyMatchModel>>() {
                                }.getType())
                );

                if (list.size() > 0) {

                    this.fList=list;
                    binding.tvUpcomingMatches.setVisibility(View.VISIBLE);
                    binding.tvFilter.setVisibility(View.VISIBLE);
                    binding.rvMatches.setVisibility(View.VISIBLE);
                    binding.pBarUDM.setVisibility(View.GONE);
                    initList(list);
                } else {
                    binding.tvUpcomingMatches.setVisibility(View.GONE);
                    binding.tvFilter.setVisibility(View.GONE);
                    binding.rvMatches.setVisibility(View.GONE);
                    binding.pBarUDM.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setText(jsonObject.getString("statusMessage"));
                }
            } else{
                binding.tvUpcomingMatches.setVisibility(View.GONE);
                binding.tvFilter.setVisibility(View.GONE);
                binding.rvMatches.setVisibility(View.GONE);
                binding.tvNoMatch.setVisibility(View.VISIBLE);
                binding.tvNoMatch.setText(jsonObject.getString("statusMessage"));
                binding.pBarUDM.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void dailyMatches(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                obj = new Gson().fromJson(jsonObject.getJSONArray("data").getJSONObject(0).toString()
                        , DailyMatchModel.class);
                if (obj != null) {
                    binding.tvTrournamentName.setText(obj.getTournamentName());
                    binding.tvTournamentStatus.setText(obj.getMatchStatus());
                    binding.tvLeftTeamName.setText(obj.getTeam1ShortName());
                    binding.tvRightTeamName.setText(obj.getTeam2ShortName());
                    binding.tvTrournamentName.setText(obj.getTournamentName());

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.blank_jersey);
                    requestOptions.error(R.drawable.blank_jersey);

                    if (obj.getTeam1Image() != null) {
                        String url = Constants.BASE_IMAGE_URL + obj.getTeam1Image();
                        Glide.with(getActivity())
                                .setDefaultRequestOptions(requestOptions)
                                .load(url).into(binding.imgLeft);
                    } else
                        binding.imgLeft.setImageResource(R.drawable.blank_jersey);

                    if (obj.getTeam2Image() != null) {
                        String url = Constants.BASE_IMAGE_URL + obj.getTeam2Image();
                        Glide.with(getActivity())
                                .setDefaultRequestOptions(requestOptions)
                                .load(url).into(binding.imgRight);
                    } else
                        binding.imgRight.setImageResource(R.drawable.blank_jersey);

                    if (obj.getMatchStatus().equals("UPCOMING"))
                    {
                        try {
                            SimpleDateFormat normalDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            normalDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date2 = normalDateFormat.parse(obj.getMatchDate());
                            Date date1 = normalDateFormat.parse(normalDateFormat.format(Calendar.getInstance().getTime()));
                            printDifference(date1, date2,binding.tvTournamentTxt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else
                        getFullDateFromISO(obj.getMatchDate(),binding.tvTournamentTxt);

                    binding.tvWeeklyRank.setText(String.valueOf(obj.getWeeklyRank()));
                    binding.tvWeeklyPoint.setText(String.valueOf(obj.getWeeklyPoints()));

                    if (obj.getMatchStatus().equals("Live"))
                        binding.tvTournamentStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_round, 0, 0, 0);
                    else
                        binding.tvTournamentStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    if (obj.getTotalPoints() != 0)
                        binding.tvTournamentTxt.setText(new StringBuilder().append(getString(R.string.text_youhave))
                                .append(" ").append(obj.getTotalPoints())
                                .append(" ").append(getString(R.string.text_pts)));

                    binding.tvMyMatch.setVisibility(View.VISIBLE);
                    binding.tvSeeAllMyMatches.setVisibility(View.VISIBLE);
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.clHeader.setVisibility(View.VISIBLE);
                } else {
                    binding.clHeader.setVisibility(View.GONE);
                    binding.tvMyMatch.setVisibility(View.GONE);
                    binding.tvSeeAllMyMatches.setVisibility(View.GONE);
                    binding.linearLayout.setVisibility(View.GONE);
                }
            } else{
//                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
                binding.clHeader.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFilterTeamList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                tList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<TournamentModel>>() {
                                }.getType())
                );
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void returnTName(List<String> tName) {
        tournamentName = new ArrayList();
        tournamentName.addAll(tName);
        if (tournamentName.size() > 0)
            setTournamentWiseList(tName);
        else
            initList(list);
    }

    private void setTournamentWiseList(List<String> tName) {
         fList = new ArrayList<>();
        for (UpcomingDailyMatchModel obj : list) {
            if (tName.contains(obj.getTournamentName()))
                fList.add(obj);
        }
        initList(fList);
    }

    @Override
    public void clearTeamName() {
        tournamentName = new LinkedList<>();
        initList(list);
    }

    @Override
    public void onItemClick(UpcomingDailyMatchModel obj) {
        String txt=obj.getTeam1ShortName()+" v "+obj.getTeam2ShortName();
        startActivity(new Intent(getActivity(), PlayerSelectionActivity.class)
                .putExtra(Constants.TAG_MATCHID, obj.getMatchId())
                .putExtra(Constants.TAG_MATCHTYPE, obj.getMatchType())
                .putExtra(Constants.TAG_TOURNAMENTID, obj.getTournamentId())
                .putExtra(Constants.TAG_MATCHDATE, obj.getMatchDate())
                .putExtra(Constants.TAG_HEADER, txt)
                .putExtra("Team1ShortName", obj.getTeam1ShortName())
                .putExtra("Team2ShortName", obj.getTeam2ShortName())
        );
    }
}
