package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.yorker.fanzania.databinding.FragmentMyLeagueNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.ActionDialog;
import com.yorker.fanzania.dialog.CreateLeagueDialog;
import com.yorker.fanzania.dialog.ExitLeagueDialog;
import com.yorker.fanzania.dialog.JoinLeagueDialog;
import com.yorker.fanzania.dialog.NewTeamViewDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.RenameLeagueDialog;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.screens.league.LeagueSubscriptionActivity;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserLeagueModel;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UserTournamentModel;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.CustomPagerAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.LeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.adapter.MyLeagueListAdapter;
import com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model.LeagueTeamModel;
import com.yorker.fanzania.views.screens.tournament.leaguestats.LeagueStatsActivity;
import com.yorker.fanzania.views.shared.fragment.BaseFragment;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MyLeagueFragment extends BaseFragment<MyLeagueFragmentPresenter>
        implements MyLeagueFragmentPresenter.IMainView,
        MyLeagueListAdapter.IPoints,
        JoinLeagueDialog.IJoinLeaguDialog,
        CreateLeagueDialog.ICreateLeaguDialog,
        ExitLeagueDialog.IExitDialog,
        ActionDialog.IActionDialog,
        LeagueListAdapter.ILeague, RenameLeagueDialog.IRenameLeaguDialog {

    private MyLeagueFragmentPresenter presenter;
    private FragmentMyLeagueNewBinding binding;

    private MyLeagueListAdapter pAdapter;
    private LeagueListAdapter lAdapter;

    private LinkedList<UserTournamentModel> list;

    private String tournamentID;
    private String leagueID;
    private String leaguePin;
    private String leagueName;
    private String tournamentStatus;
    private String tournamentDate;
    private String tournamentName;

    private int tabLength = 0;

    private Boolean isActiveTournament = false;
    private Boolean isUpcomingTournament = false;

    private int tStatus = 0;

    private Boolean isUserAdmin = false;

    private LinkedList<UserLeagueModel> leagueList = new LinkedList<>();
    private LinkedList<LeagueTeamModel> ltList;

    private SimpleTooltip tooltip;
    private String leagueRank, standingTeam;
    private String leagueLeaderName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_league_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListners();
        getuserTournamentList();
    }

    @Override
    public void onPause() {
        if (tooltip != null)
            tooltip.dismiss();

        super.onPause();
    }

    private void initListners() {
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

        binding.tvLeagueSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeagueSubscriptionActivity.class);
                intent.putExtra("tid", tournamentID);
                intent.putExtra("lid", leagueID);
                getActivity().startActivity(intent);
            }
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
                checkStatus(list.get(position).getTournamentStatus());
                setTournamentDetails(list.get(position));
            }
        });

        binding.llManage.setOnClickListener(view -> {
            if (isUserAdmin) {
                if (CheckInternetConnection())
                    presenter.ChangeLeaguePin(tournamentID, leagueID);
                else
                    new NoNetworkDialog(getActivity(), this, Constants.APICALL_4);
            } else
                new ExitLeagueDialog(getActivity(), tournamentID, leagueID, presenter.getCustomerId(), this);
        });

        binding.llShare.setOnClickListener(view -> presenter.shareApp(leaguePin, leagueName));

        binding.llStats.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), LeagueStatsActivity.class)
                        .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                        .putExtra(Constants.TAG_TOURNAMENTNAME, tournamentName)
                        .putExtra(Constants.TAG_TOURNAMENTDATE, tournamentDate)
                        .putExtra(Constants.TAG_LEAGUENAME, leagueName)
                        .putExtra(Constants.TAG_LEAGUEID, leagueID)
                ));
    }

    private void getuserTournamentList() {
        binding.llListLayout.setVisibility(View.VISIBLE);
        binding.inRVList.pBar.setVisibility(View.VISIBLE);
        binding.inRVList.rvList.setVisibility(View.GONE);

        binding.inRVList.tvNoDataFound.setVisibility(View.GONE);

        if (CheckInternetConnection())
            presenter.getUserTournamentList();
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_1);
    }

    private void initView() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ll.setAutoMeasureEnabled(false);
        binding.tbLeagues.setLayoutManager(ll);

        lAdapter = new LeagueListAdapter(getActivity(), leagueList, isActiveTournament, this, presenter.getLeagueID());
        binding.tbLeagues.setAdapter(lAdapter);

        binding.inRVList.rvList.setHasFixedSize(true);
        ltList = new LinkedList<>();
        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.inRVList.rvList.addItemDecoration(new ItemDecoration(getActivity()));
        pAdapter = new MyLeagueListAdapter(getActivity(), ltList, this, isUserAdmin, tournamentStatus, presenter.getCustomerId());
        binding.inRVList.rvList.setAdapter(pAdapter);
    }

    private void checkLeagueAdmin(String leagueLeaderId) {
        if (isActiveTournament || isUpcomingTournament) {
            if (leagueLeaderId.equals(presenter.getCustomerId())) {
                isUserAdmin = true;
                setUserAdmin();
            } else {
                isUserAdmin = false;
                setUserNotAdmin();
            }
        }
    }

    private void setUserAdmin() {
        binding.imgManage.setEnabled(true);
        binding.imgManage.setImageResource(R.drawable.ic_settings);
        binding.tvManageLeagueTxt.setText(getString(R.string.text_changepin));
        binding.tvManageLeagueTxt.setTextColor(getResources().getColor(R.color.colorText));
        binding.imgStats.setEnabled(true);
        binding.imgShare.setEnabled(true);
        binding.imgShare.setImageResource(R.drawable.ic_new_share_active);
        binding.tvShareLeagueTxt.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void setUserNotAdmin() {
        binding.imgManage.setEnabled(true);
        binding.imgManage.setImageResource(R.drawable.ic_power_off);
        binding.tvManageLeagueTxt.setText(getString(R.string.text_exitleague1));
        binding.tvManageLeagueTxt.setTextColor(getResources().getColor(R.color.colorText));
        binding.imgStats.setEnabled(true);
        binding.imgShare.setEnabled(true);
        binding.imgShare.setImageResource(R.drawable.ic_new_share_active);
        binding.tvShareLeagueTxt.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void getLeagueTeams() {

        pAdapter.ClearALL();

        if (CheckInternetConnection())
            presenter.getLeagueTeamList(tournamentID, leagueID);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_3);
    }

    @Override
    protected MyLeagueFragmentPresenter onCreatePresenter() {
        presenter = new MyLeagueFragmentPresenter(this, getActivity());
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, MyLeagueFragmentPresenter presenter) {
        MyLeagueFragmentPresenterComponent myLeagueFragmentPresenterComponent = DaggerMyLeagueFragmentPresenterComponent.builder()
                .presenterComponent(component)
                .myLeagueFragmentApplicationModule(new MyLeagueFragmentApplicationModule(getActivity()))
                .build();
        myLeagueFragmentPresenterComponent.inject(presenter);
    }

    private void setTournamentDetails(UserTournamentModel data) {

        presenter.saveTournamentID(data.getTournamentId());

        tournamentStatus = data.getTournamentStatus();
        String txt=data.getTournamentStartDate()+" - "+data.getTournamentEndDate();
        tournamentDate=txt;
        tournamentID = data.getTournamentId();
        tournamentName = data.getTournamentName();
        binding.inRVList.pBar.setVisibility(View.VISIBLE);
        binding.inRVList.rvList.setVisibility(View.GONE);
        binding.inRVList.tvNoDataFound.setVisibility(View.GONE);
        binding.llListLayout.setVisibility(View.VISIBLE);

        if (CheckInternetConnection())
            presenter.getUserLeagueList(tournamentID);
        else
            new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.leaguemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem1 = menu.findItem(R.id.itCreateLeague);
        MenuItem menuItem2 = menu.findItem(R.id.itJoinLeague);
        MenuItem menuItem3 = menu.findItem(R.id.itRenameLeague);

        Log.e("leagueleader",leagueLeaderName+", "+SharedPrefManager.getInstance(getActivity()).getCustomerName());
        if (leagueLeaderName != null && leagueLeaderName.equalsIgnoreCase(SharedPrefManager.getInstance(getActivity()).getCustomerName())){
            menuItem3.setVisible(true);
        }else
            menuItem3.setVisible(false);

        if (isActiveTournament || isUpcomingTournament) {
            menuItem1.setVisible(true);
            menuItem2.setVisible(true);
        } else {
            menuItem1.setVisible(false);
            menuItem2.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itCreateLeague:
                new CreateLeagueDialog(getActivity(), this, tournamentID, presenter.getCustomerId());
                return true;

            case R.id.itJoinLeague:
                new JoinLeagueDialog(getActivity(), this, tournamentID, leagueID, presenter.getCustomerId());
                return true;

            case R.id.itRenameLeague:
                new RenameLeagueDialog(getActivity(), this, tournamentID, leagueID, presenter.getCustomerId(), leagueName);
                return true;
        }
        return false;
    }

    @Override
    public void JDPositiveResponse(Boolean value) {
        if (value) {
            if (CheckInternetConnection()) {
                lAdapter.ClearData();
                presenter.getUserLeagueList(tournamentID);
            } else
                new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
        }
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.getUserTournamentList();
                break;

            case Constants.APICALL_2:
                presenter.getUserLeagueList(tournamentID);
                break;

            case Constants.APICALL_3:
                presenter.getLeagueTeamList(tournamentID, leagueID);
                break;

            case Constants.APICALL_4:
                presenter.ChangeLeaguePin(tournamentID, leagueID);
                break;
        }
    }

    @Override
    public void getTournamentList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserTournamentModel>>() {
                                }.getType())
                );
                if (list.size() > 0) {
                    tabLength = list.size();

                    binding.llMain.setVisibility(View.VISIBLE);
                    binding.tvNoTextMain.setVisibility(View.GONE);

                    CustomPagerAdapter mAdapter = new CustomPagerAdapter(getActivity(), list);
                    binding.viewpager.setAdapter(mAdapter);

                    if (presenter.getTournamentID() != null) {
                        String id = presenter.getTournamentID();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTournamentId().equals(id)) {
                                binding.viewpager.setCurrentItem(i);
                                setTournamentDetails(list.get(i));
                                checkStatus(list.get(i).getTournamentStatus());
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
                                break;
                            }
                        }
                    } else {
                        binding.viewpager.setCurrentItem(0);
                        setTournamentDetails(list.get(0));
                        checkStatus(list.get(0).getTournamentStatus());
                    }
                } else {
                    binding.llMain.setVisibility(View.GONE);
                    binding.tvNoTextMain.setVisibility(View.VISIBLE);
                    binding.tvNoTextMain.setText(getString(R.string.text_notournamenttext));
                }
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

                leagueList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UserLeagueModel>>() {
                                }.getType())
                );

                if (leagueList.size() > 0) {
                    binding.llLeague.setVisibility(View.VISIBLE);
                    binding.llListLayout.setVisibility(View.VISIBLE);

                    binding.tvNoTextMain.setVisibility(View.GONE);

                    setLeagueAvailable();

                    lAdapter.ClearData();
                    lAdapter.AddData(leagueList);
                    presenter.clearLeagueID();
                } else {
                    setInActiveTournament();

                    binding.llLeague.setVisibility(View.GONE);
                    binding.llListLayout.setVisibility(View.GONE);
                    binding.tvNoTextMain.setVisibility(View.VISIBLE);

                    switch (tournamentStatus) {
                        case Constants.TAG_UPCOMING:
                            binding.tvNoTextMain.setText(getString(R.string.text_notextleague_upcoming));
                            break;

                        case Constants.TAG_INPROGRESS:
                            binding.tvNoTextMain.setText(getString(R.string.text_notextleague_upcoming));
                            break;

                        case Constants.TAG_COMPLETE:
                            binding.tvNoTextMain.setText(getString(R.string.text_notextleague_complete));
                            break;
                    }
                }
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLeagueAvailable() {
        binding.imgStats.setEnabled(true);
    }

    @Override
    public void getLeagueTeamList(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                ltList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LeagueTeamModel>>() {
                                }.getType())
                );

                if (ltList.size() > 0) {
                    binding.llListLayout.setVisibility(View.VISIBLE);
                    binding.inRVList.rvList.setVisibility(View.VISIBLE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.GONE);

                    pAdapter = new MyLeagueListAdapter(getActivity(), ltList, this, isUserAdmin, tournamentStatus, presenter.getCustomerId());
                    binding.inRVList.rvList.setAdapter(pAdapter);

                    if (presenter.checkToolTip()==1) {
                        showTooltip();
                    }
                    int userPoints = 0;
                    int otherTeamPoints = 0;
                    otherTeamPoints = Integer.parseInt(ltList.get(0).getTotalPoints());

                    leagueLeaderName = ltList.get(0).getLeagueLeader();
                    Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                    standingTeam = String.valueOf(ltList.size());
                    for (LeagueTeamModel leagueTeamModel : ltList){
                        if (SharedPrefManager.getInstance(getActivity()).getCustomer_Id().equalsIgnoreCase(leagueTeamModel.getUserId())){
                            userPoints = userPoints + Integer.parseInt(leagueTeamModel.getTotalPoints());
                            Log.e("rankPoints", leagueTeamModel.getUserTeamName());
                            int comparePoints = otherTeamPoints - userPoints;
                            displayRankMsg(comparePoints,otherTeamPoints, leagueTeamModel.getTeamCurrentStanding());
                        }
                    }


                } else {
                    binding.llListLayout.setVisibility(View.GONE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.rvList.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inRVList.tvNoDataFound.setText(getString(R.string.text_noteamavailable));
                }
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayRankMsg(int comparePoints, int otherTeamPoints, int teamCurrentStanding) {
//        if (otherTeamPoints - userPoints > 0){
//            binding.rankText.setText("Your rank is "+leagueRank+"th out of "+standingTeam+" members, "+comparePoints+" points behind the topper.");
//        }else {
//            binding.rankText.setText("Your rank is "+leagueRank+"th out of "+standingTeam+" members, "+comparePoints+" points ahead the topper");
//        }
//        if (comparePoints == 0){
//            binding.rankText.setText("Your rank is "+leagueRank+"th out of "+standingTeam+" members.");
//        }
        if (comparePoints == 0 && teamCurrentStanding == 0){
            binding.rankText.setText("Your points will be posted after you have played your first match.");
        }else if (comparePoints == 0 && teamCurrentStanding == 1){
            binding.rankText.setText("Your rank is 1st out of "+standingTeam+" members.");
        }else if (teamCurrentStanding == 2){
            binding.rankText.setText("Your rank is 2nd out of "+standingTeam+" members, "+comparePoints+" behind the topper.");
        }else if (teamCurrentStanding == 3){
            binding.rankText.setText("Your rank is 3rd out of "+standingTeam+" members, "+comparePoints+" behind the topper.");
        }else{
            binding.rankText.setText("Your rank is "+teamCurrentStanding+"th out of "+standingTeam+" members, "+comparePoints+" behind the topper.");
        }
    }

    private void showTooltip() {
        SimpleTooltip.Builder builder = new SimpleTooltip.Builder(getActivity())
                .anchorView(binding.View)
                .text(getString(R.string.text_swipetoadddeleteview))
                .gravity(Gravity.TOP)
                .arrowColor(getResources().getColor(R.color.colorWhite))
                .animated(true)
                .transparentOverlay(false)
                .dismissOnOutsideTouch(true)
                .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                .overlayOffset(0)
                .contentView(R.layout.custom_tooltip, R.id.montserratLight);

        tooltip = builder.build();
        tooltip.show();
    }

    @Override
    public void getApproveMemeber(JSONObject jsonObject, int position) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                pAdapter.TeamAdded(position);
            else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRemoveMemeber(JSONObject jsonObject, int position) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS))
                pAdapter.TeamRemoved(position);
            else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getChangePin(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                leaguePin = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0).getString("LeaguePin");
                CustomToast.getInstance(getActivity()).showSmallCustomToast(getString(R.string.text_pingenarated));
            } else
                CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnClickLeaguTeam(LeagueTeamModel data) {
        NewTeamViewDialog dialog = new NewTeamViewDialog(getActivity(), tournamentID, data.getUserTeamId(),
                presenter.getCustomerId(), tStatus, data.getUserTeamName());
        dialog.show(getActivity().getSupportFragmentManager(), "ratereview");
    }

    @Override
    public void OnClickAddTeam(int position, String userId) {
        new ActionDialog(getActivity(), getString(R.string.text_approval), getString(R.string.text_approvaltxt), this, 0, position, userId);
    }

    @Override
    public void OnClickRemoveTeam(int UserLeagueId, int position) {
        new ActionDialog(getActivity(), getString(R.string.text_removal), getString(R.string.text_removaltxt), this, 1,
                position, String.valueOf(UserLeagueId));
    }

    @Override
    public void createLeague(Boolean value) {
        if (value) {
            if (CheckInternetConnection()) {
                lAdapter.ClearData();
                presenter.getUserLeagueList(tournamentID);
            } else
                new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
        }
    }

    private void checkStatus(String strStatus) {

        switch (strStatus) {

            case Constants.TAG_COMPLETE:
                isActiveTournament = false;
                isUpcomingTournament = false;
                tStatus = 0;
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                setInActiveTournament();
                binding.llStats.setEnabled(true);
                setStatEnable();
                break;

            case Constants.TAG_INPROGRESS:
                tStatus = 1;
                setStatEnable();
                isActiveTournament = true;
                isUpcomingTournament = false;
                binding.llStats.setEnabled(true);
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                break;

            case Constants.TAG_UPCOMING:
                tStatus = 2;
                isActiveTournament = false;
                isUpcomingTournament = true;
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                binding.llStats.setEnabled(false);
                setActiveTournament();
                setStatDisable();
                break;
        }
    }

    private void setStatDisable() {
        binding.imgStats.setEnabled(false);
        binding.imgStats.setImageResource(R.drawable.ic_disable_star_new);
        binding.tvLeagueStatsTxt.setTextColor(getResources().getColor(R.color.colorGrey));
    }

    private void setStatEnable() {
        binding.imgStats.setEnabled(true);
        binding.imgStats.setImageResource(R.drawable.ic_star_new);
        binding.tvLeagueStatsTxt.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void setInActiveTournament() {
        binding.imgManage.setEnabled(false);
        binding.imgManage.setImageResource(R.drawable.ic_settings_light);
        binding.tvManageLeagueTxt.setTextColor(getResources().getColor(R.color.colorGrey));

        binding.imgShare.setEnabled(false);
        binding.imgShare.setImageResource(R.drawable.ic_new_share_disable);
        binding.tvShareLeagueTxt.setTextColor(getResources().getColor(R.color.colorGrey));
    }

    private void setActiveTournament() {
        binding.imgShare.setEnabled(true);
        binding.imgShare.setImageResource(R.drawable.ic_new_share_active);
        binding.tvShareLeagueTxt.setTextColor(getResources().getColor(R.color.colorText));
    }

    @Override
    public void ExitLeague(Boolean value) {
        if (value) {
            if (CheckInternetConnection()) {
                lAdapter.ClearData();
                presenter.getUserLeagueList(tournamentID);
            } else
                new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
        }
    }

    @Override
    public void PositiveResponse(Boolean value, int Type, int position, String ID) {
        switch (Type) {
            case 0:
                if (CheckInternetConnection())
                    presenter.ApproveLeagueTeam(leagueID, position, ID);
                break;

            case 1:
                if (CheckInternetConnection())
                    presenter.RemoveLeagueTeam(leagueID, Integer.parseInt(ID), position);
                break;
        }
    }

    @Override
    public void onClickLeagueList(UserLeagueModel data) {
        leagueID = data.getLeagueId();
        leaguePin = data.getLeaguePin();
        leagueName = data.getLeagueName();
        leagueRank = data.getLeagueRank();
        checkLeagueAdmin(data.getLeagueLeaderId());
        getLeagueTeams();
    }

    @Override
    public void renameLeague(Boolean value) {
        if (value) {
            if (CheckInternetConnection()) {
                lAdapter.ClearData();
                presenter.getUserLeagueList(tournamentID);
            } else
                new NoNetworkDialog(getActivity(), this, Constants.APICALL_2);
        }
    }
}
