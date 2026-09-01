package com.yorker.fanzania.views.screens.tournament.manageteam;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomButton;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.ActivityManageTeamNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.CommonDialog;
import com.yorker.fanzania.dialog.CustomeAlertDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.PlayerActionDialog;
import com.yorker.fanzania.dialog.TeamInfoDialog;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.UpcomingMatchModel;
import com.yorker.fanzania.views.screens.tournament.playerlist.PlayerListActivity;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.UpcomingMatchListAdapter;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.tooltip.Tooltip;

public class ManageTeamActivity extends BaseActivity<ManageTeamPresenter>
        implements ManageTeamPresenter.IMainView,
        PlayerActionDialog.IPlayerActionDialog,
        CustomeAlertDialog.ICommonDialog,
        CommonDialog.ICommonDialog {

    private ManageTeamPresenter presenter;
    private ActivityManageTeamNewBinding binding;

    private String tournamentID;
    private String matchId;
    private String userTeamId;
    private String catpName;
    private String viceCatpName;

    private ArrayList<PlayerDataType> oldPlayerList;
    private ArrayList<PlayerDataType> CurrentPlayerList;
    private ArrayList<Integer> fieldList;
    private ArrayList<Integer> selectedPlayerID = new ArrayList<>();
    private ArrayList<Integer> oldPlayerID = new ArrayList<>();
    private LinkedList<UpcomingMatchModel> matchList = new LinkedList<>();

    private UpcomingMatchListAdapter uAdapter;

    private Map<String, Object> mapCurrentData;
    private Map<String, Object> mapSelectedData;

    private int transferUsed = 0;
    private int SubsLeftAtSnapShot = 0;
    private int oldSubsLeft = 0;
    private int firstSubsLeft = 0;
    private int SubsCountTotal = 0;
    private int transferLeft = 0;
    private int TotalBudget = 0;
    private int RemainingBudget = 0;

    private int oldCaptLayoutID;
    private int oldViceCaptLayoutID;

    private int nitroLeft = 0;
    private int painKillerLeft = 0;
    private int autoPilotLeft = 0;
    private int isNitroUsed = 0;
    private int isPainKillerUsed = 0;
    private int isAutoPilotUsed = 0;

    private int catpID = 0;
    private int vsCaptID = 0;

    private int MaxWicketKeeper = 0;
    private int MinWicketKeeper = 0;
    private int MaxBatsman = 0;
    private int MinBatsman = 0;
    private int MaxBowler = 0;
    private int MinBowler = 0;
    private int MaxAllrounder = 0;
    private int MinAllrounder = 0;
    private int MaxSameTeamPlayer = 0;
    private int MaxOverseasPlayer = 0;

    private Boolean AutoPilotUsed = false;
    private Boolean NitroUsed = false;
    private Boolean PainKillerUsed = false;
    private Boolean isTournamentInProgress = false;
    private Boolean isNewTeam = false;
    private Boolean isFirstTransferCounted = false;
    String Team1ShortName = null, Team2ShortName = null, predictTeam = null;

    private Gson gson = new Gson();

    @Override
    protected ManageTeamPresenter onCreatePresenter() {
        presenter = new ManageTeamPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, ManageTeamPresenter presenter) {
        ManageTeamPresenterComponent component1 = DaggerManageTeamPresenterComponent.builder()
                .presenterComponent(component)
                .manageTeamApplicationModule(new ManageTeamApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_team_new);
        Log.d("sajhshdhajkhjdkhjkas","dsdsd ");
        if (getIntent() != null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            matchId = getIntent().getStringExtra(Constants.TAG_MATCHID);
            userTeamId = getIntent().getStringExtra(Constants.TAG_USERTEAMID);
            Team1ShortName = getIntent().getStringExtra("Team1ShortName");
            Team2ShortName = getIntent().getStringExtra("Team2ShortName");
            getTeamRuleData();
            setTournamentData();
            getMatches();
        }

        init();
    }

    private void init() {
        initViews();
        initListner();
        initSwitches();
    }

    private void setTournamentData() {
        binding.tvTeamName.setText(getIntent().getStringExtra(Constants.TAG_TEAMNAME));

        if (getIntent().getStringExtra(Constants.TAG_TOURNAMENTSTATUS).equals("INPROGRESS"))
            isTournamentInProgress = true;
        else
            isTournamentInProgress = false;
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getTournamentMatchList(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_6);
    }

    private void getTeamRuleData() {
        if (CheckInternetConnection())
            presenter.TeamRule(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initSwitches() {
        binding.scNitro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (nitroLeft > 0) {
                if (isChecked) {
                    binding.scNitro.setTextColor(getResources().getColor(R.color.colorRed));
                    isNitroUsed = 1;

                    if (painKillerLeft > 0) {
                        binding.scPainKiller.setChecked(false);
                        isPainKillerUsed = 0;
                    }

                    if (autoPilotLeft > 0) {
                        binding.scAutoCaptain.setChecked(false);
                        isAutoPilotUsed = 0;
                    }
                } else{
                    isNitroUsed = 0;
                    binding.scNitro.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });

        binding.scPainKiller.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (painKillerLeft > 0) {
                if (isChecked) {
                    binding.scPainKiller.setTextColor(getResources().getColor(R.color.colorRed));
                    isPainKillerUsed = 1;

                    if (nitroLeft > 0) {
                        binding.scNitro.setChecked(false);
                        isNitroUsed = 0;
                    }

                    if (autoPilotLeft > 0) {
                        binding.scAutoCaptain.setChecked(false);
                        isAutoPilotUsed = 0;
                    }
                } else{
                    binding.scPainKiller.setTextColor(getResources().getColor(R.color.colorText));
                    isPainKillerUsed = 0;
                }
            }
        });

        binding.scAutoCaptain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (autoPilotLeft > 0) {
                if (isChecked) {
                    binding.scAutoCaptain.setTextColor(getResources().getColor(R.color.colorRed));
                    isAutoPilotUsed = 1;

                    if (painKillerLeft > 0) {
                        binding.scPainKiller.setChecked(false);
                        isPainKillerUsed = 0;
                    }

                    if (nitroLeft > 0) {
                        binding.scNitro.setChecked(false);
                        isNitroUsed = 0;
                    }
                } else{
                    binding.scAutoCaptain.setTextColor(getResources().getColor(R.color.colorText));
                    isAutoPilotUsed = 0;
                }
            }
        });
    }

    private void getData() {
        if (CheckInternetConnection())
            presenter.PlayerDetails(tournamentID, matchId, userTeamId);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    private void initViews() {
        fieldList = new ArrayList<>();
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_manageteam));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.rvMatch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        uAdapter = new UpcomingMatchListAdapter(matchList);
        binding.rvMatch.setAdapter(uAdapter);
    }

    private void initListner() {
        //---------- Player 1 Click----------//

        binding.rrPlayer1.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer1Captain, binding.rrPlayer1);
            }
        });

        //---------- Player 2 Click----------//
        binding.rrPlayer2.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer2Captain, binding.rrPlayer2);
            }
        });

        //---------- Player 3 Click----------//
        binding.rrPlayer3.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer3Captain, binding.rrPlayer3);
            }
        });

        //---------- Player 4 Click----------//
        binding.rrPlayer4.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer4Captain, binding.rrPlayer4);
            }
        });

        //---------- Player 5 Click----------//
        binding.rrPlayer5.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer5Captain, binding.rrPlayer5);
            }
        });

        //---------- Player 6 Click----------//
        binding.rrPlayer6.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer6Captain, binding.rrPlayer6);
            }
        });

        //---------- Player 7 Click----------//
        binding.rrPlayer7.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer7Captain, binding.rrPlayer7);
            }
        });

        //---------- Player 8 Click----------//
        binding.rrPlayer8.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer8Captain, binding.rrPlayer8);
            }
        });

        //---------- Player 9 Click----------//
        binding.rrPlayer9.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer9Captain, binding.rrPlayer9);
            }
        });

        //---------- Player 10 Click----------//
        binding.rrPlayer10.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer10Captain, binding.rrPlayer10);
            }
        });

        //---------- Player 11 Click----------//
        binding.rrPlayer11.setOnClickListener(view -> {
            if (view.getTag() != null){
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer11Captain, binding.rrPlayer11);
            }
        });

        //---------- Reset Button-----------//
        binding.btnReset.setOnClickListener(view -> {
            if (oldPlayerList.size() > 0)
                new CommonDialog(this, "", getString(R.string.text_resettext), this);
            else
                showResetTooltip(getString(R.string.text_resettooltip), binding.btnReset);
        });

        //----------Save ------//
        binding.btnSaveTeam.setOnClickListener(view -> {
            if (selectedPlayerID.size() == 11) {
                if (catpID == 0)
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectcapt_tournament));
                else {
                    if (vsCaptID == 0)
                        CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectvcapt_tournament));
                    else
                        saveData();
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectallplayers));
        });

        binding.btnSaveTeam1.setOnClickListener(view -> {
            if (selectedPlayerID.size() == 11) {
                if (catpID == 0)
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectcapt_tournament));
                else {
                    if (vsCaptID == 0)
                        CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectvcapt_tournament));
                    else
                        saveData();
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectallplayers));
        });

        binding.btnReplace.setOnClickListener(view -> {
            if (isTournamentInProgress) {
                if (transferLeft >= 1) {
                    ReplacePlayer();
                } else
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_youdonthavetransfer));
            } else
                ReplacePlayer();
        });
    }

    public void ReplacePlayer() {
        Gson gson = new Gson();
        String PlayerLIst = gson.toJson(CurrentPlayerList);

        String playerID = gson.toJson(oldPlayerID);

        startActivityForResult(new Intent(this, PlayerListActivity.class)
                        .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                        .putExtra(Constants.TAG_TOURNAMENTSTATUS, isTournamentInProgress)
                        .putExtra(Constants.TAG_PLAYERLIST, PlayerLIst)
                        .putExtra(Constants.TAG_LASTCUTOFF, playerID)
                        .putExtra(Constants.TAG_TotalBudget, RemainingBudget)
                        .putExtra(Constants.TAG_SubsLeftAtSnapShot, transferLeft)
                        .putExtra(Constants.TAG_SubsLeft, SubsLeftAtSnapShot)
                , Constants.REQ_CODE_PLAYER);
    }

    private void showComfirmDialog() {
        if (oldPlayerList.size() > 0) {
            if (!isTournamentInProgress)
                transferUsed = 0;
        } else
            transferUsed = 0;

        UpcomingMatchModel upcomingMatchModel = null;
        if (Team1ShortName != null && Team2ShortName != null) {
            upcomingMatchModel = new UpcomingMatchModel();
            upcomingMatchModel.setTeam1ShortName(Team1ShortName);
            upcomingMatchModel.setTeam2ShortName(Team2ShortName);
        }
        if (selectedPlayerID.contains(catpID))
            CustomeAlertDialog.show(this, getString(R.string.text_teamconfirmation), catpName, viceCatpName,
                    transferUsed, this, isTournamentInProgress,
                    isAutoPilotUsed,isNitroUsed,isPainKillerUsed, upcomingMatchModel, predictTeam);
        else
            CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_selectcaptain));
    }

    private void saveData() {
//        if (mapCurrentData.equals(mapSelectedData))
//            CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_sameteam));
//        else {
            if (CheckInternetConnection())
                presenter.getLastPlayedTeamPlayers(tournamentID, userTeamId, false);
            else
                new NoNetworkDialog(this, this, Constants.APICALL_4);
//        }
    }

    private void SaveTeam() {
        if (CheckInternetConnection()) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.TAG_USERTEAMID, userTeamId);
            map.put(Constants.TAG_TEAMCAPT, catpID);
            map.put(Constants.TAG_TEAMCAPTVC, vsCaptID);
            map.put(Constants.TAG_NUMBEROFSUBS, transferUsed);
            map.put(Constants.TAG_NITROUSED, isNitroUsed);
            map.put(Constants.TAG_PAINKILLERUSED, isPainKillerUsed);
            map.put(Constants.TAG_AUTOPILOTUSED, isAutoPilotUsed);
            if (predictTeam !=null){
                map.put("WinnerPrediction", predictTeam);
            }
            for (int i = 0; i < selectedPlayerID.size(); i++) {
                map.put(Constants.TAG_PLAYER + (i + 1), selectedPlayerID.get(i));
            }

            presenter.SaveTeamPlayers(map);

            binding.btnSaveTeam.setEnabled(false);
            binding.btnSaveTeam1.setEnabled(false);
            binding.pBar.setVisibility(View.VISIBLE);
        } else
            new NoNetworkDialog(this, this, Constants.APICALL_5);
    }

    private void setCurrentData() {
        mapCurrentData = new HashMap<>();
        mapCurrentData.put(Constants.TAG_USERTEAMID, userTeamId);
        mapCurrentData.put(Constants.TAG_TEAMCAPT, catpID);
        mapCurrentData.put(Constants.TAG_TEAMCAPTVC, vsCaptID);
        mapCurrentData.put(Constants.TAG_NITROUSED, isNitroUsed);
        mapCurrentData.put(Constants.TAG_PAINKILLERUSED, isPainKillerUsed);
        mapCurrentData.put(Constants.TAG_AUTOPILOTUSED, isAutoPilotUsed);

        for (int i = 0; i < selectedPlayerID.size(); i++) {
            mapCurrentData.put(Constants.TAG_PLAYER + (i + 1), selectedPlayerID.get(i));
        }
    }

    private void setSelectedData() {
        mapSelectedData = new HashMap<>();
        mapSelectedData.put(Constants.TAG_USERTEAMID, userTeamId);
        mapSelectedData.put(Constants.TAG_TEAMCAPT, catpID);
        mapSelectedData.put(Constants.TAG_TEAMCAPTVC, vsCaptID);
        mapSelectedData.put(Constants.TAG_NITROUSED, isNitroUsed);
        mapSelectedData.put(Constants.TAG_PAINKILLERUSED, isPainKillerUsed);
        mapSelectedData.put(Constants.TAG_AUTOPILOTUSED, isAutoPilotUsed);

        for (int i = 0; i < selectedPlayerID.size(); i++) {
            mapSelectedData.put(Constants.TAG_PLAYER + (i + 1), selectedPlayerID.get(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Constants.REQ_CODE_PLAYER) {
            if (data != null) {
                binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_team_new);
                init();
                setTournamentData();
                Log.d("sajhshdhajkhjdkhjkas","dsdsds55");
                binding.pbMatches.setVisibility(View.GONE);
                binding.clTeamDetailsBlank.setVisibility(View.GONE);
                binding.clTeamDetails.setVisibility(View.VISIBLE);
                binding.clTeamDetailsBlank.setVisibility(View.GONE);
                binding.clTeamDetails.setVisibility(View.VISIBLE);
                setTransferLeft(data.getIntExtra(Constants.TAG_SubsLeftAtSnapShot, 0));
                int val = data.getIntExtra(Constants.TAG_SubsLeftAtSnapShot, 0);
                transferUsed = 0;

                if (oldSubsLeft > val)
                    transferUsed = firstSubsLeft + (oldSubsLeft - val);

                if (data.getStringExtra(Constants.TAG_PLAYERLIST) != null) {
                    isNewTeam = true;
                    vsCaptID = 0;
                    catpID = 0;
                    setNewPLayerLIst(data.getStringExtra(Constants.TAG_PLAYERLIST));
                } else {
                    RemainingBudget = TotalBudget;
                    setBudget();
                }

                setHeaderData();
            }
        }
    }

    private void setNewPLayerLIst(String stringExtra) {

        ArrayList<PlayerDataType> playerList = new ArrayList<>(
                new Gson().fromJson(
                        stringExtra
                        , new TypeToken<List<PlayerDataType>>() {
                        }.getType())
        );

        setPlayerData(playerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);

        MontserratRegular tvStats = menu.findItem(R.id.action_item_one).getActionView().findViewById(R.id.tvActionApply);
        tvStats.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_info, 0, 0);
        tvStats.setText(getString(R.string.text_info));

        tvStats.setOnClickListener(view ->
                new TeamInfoDialog(this, MaxWicketKeeper,MinWicketKeeper, MaxAllrounder, MinAllrounder, MaxBatsman, MinBatsman, MaxBowler, MinBowler,
                        MaxSameTeamPlayer, MaxOverseasPlayer)
        );

        return true;
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.TeamRule(tournamentID);
                break;

            case Constants.APICALL_2:
                presenter.getLastPlayedTeamPlayers(tournamentID, userTeamId, true);
                break;

            case Constants.APICALL_3:
                presenter.PlayerDetails(tournamentID, matchId, userTeamId);
                break;

            case Constants.APICALL_4:
                presenter.getLastPlayedTeamPlayers(tournamentID, userTeamId, false);
                break;

            case Constants.APICALL_5:
                SaveTeam();
                break;

            case Constants.APICALL_6:
                getMatches();
                break;
        }
    }

    private void getLastTeamDetails() {
        if (CheckInternetConnection())
            presenter.getLastPlayedTeamPlayers(tournamentID, userTeamId, true);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    @Override
    public void getPlayersDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                ArrayList<PlayerDataType> playerList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (playerList.size() > 0) {
                    SubsLeftAtSnapShot = playerList.get(0).getSubsLeftAtSnapShot();
                    int subsLeft = playerList.get(0).getSubsLeft();
                    transferLeft = subsLeft;

                    nitroLeft = playerList.get(0).getNitroLeft();
                    painKillerLeft = playerList.get(0).getPainKillerLeft();
                    autoPilotLeft = playerList.get(0).getAutoPilotLeft();

                    AutoPilotUsed = playerList.get(0).isAutoPilotUsed();
                    PainKillerUsed = playerList.get(0).isPainKillerUsed();
                    NitroUsed = playerList.get(0).isNitroUsed();
                    predictTeam = playerList.get(0).getWinnerPrediction();

                    setHeaderData();
                    oldSubsLeft = subsLeft;
                    setTransferLeft(subsLeft);

                    setPlayerData(playerList);
                } else {
                    binding.pbField.setVisibility(View.GONE);
                    binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                    binding.clTeamDetails.setVisibility(View.GONE);
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHeaderData() {
        if (AutoPilotUsed) {
            binding.scAutoCaptain.setChecked(true);
            binding.scPainKiller.setChecked(false);
            binding.scNitro.setChecked(false);
        } else if (PainKillerUsed) {
            binding.scPainKiller.setChecked(true);
            binding.scNitro.setChecked(false);
            binding.scAutoCaptain.setChecked(false);
        } else if (NitroUsed) {
            binding.scNitro.setChecked(true);
            binding.scPainKiller.setChecked(false);
            binding.scAutoCaptain.setChecked(false);
        }

        if (nitroLeft > 0)
            binding.scNitro.setEnabled(true);
        else {
            binding.scNitro.setChecked(true);
            binding.scNitro.setEnabled(false);
        }

        if (painKillerLeft > 0)
            binding.scPainKiller.setEnabled(true);
        else {
            binding.scPainKiller.setEnabled(false);
            binding.scPainKiller.setChecked(true);
        }

        if (autoPilotLeft > 0)
            binding.scAutoCaptain.setEnabled(true);
        else {
            binding.scAutoCaptain.setChecked(true);
            binding.scAutoCaptain.setEnabled(false);
        }
    }

    private void setTransferLeft(int count) {
        transferLeft = count;
        if (count < 0)
            count = 0;

        if (isTournamentInProgress) {
            binding.imgInfinity.setVisibility(View.GONE);
            binding.tvTransferAmt.setVisibility(View.VISIBLE);
            String text = String.valueOf(count) + "/" + SubsCountTotal;
            binding.tvTransferAmt.setText(text);
        } else {
            binding.imgInfinity.setVisibility(View.VISIBLE);
            binding.tvTransferAmt.setVisibility(View.GONE);
        }
    }

    @Override
    public void getTeamRuleDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                TotalBudget = jsonObject1.getInt("TotalBudget");

                MaxWicketKeeper = jsonObject1.getInt("MaxWicketKeeper");
                MinWicketKeeper = jsonObject1.getInt("WicketKeeper");
                MaxBatsman = jsonObject1.getInt("MaxBatsman");
                MinBatsman = jsonObject1.getInt("MinBatsman");
                MaxBowler = jsonObject1.getInt("MaxBowler");
                MinBowler = jsonObject1.getInt("MinBowler");
                MaxAllrounder = jsonObject1.getInt("MaxAllrounder");
                MinAllrounder = jsonObject1.getInt("MinAllrounder");

                MaxSameTeamPlayer = jsonObject1.getInt("MaxSameTeamPlayer");
                MaxOverseasPlayer = jsonObject1.getInt("MaxOverseasPlayer");

                SubsCountTotal = jsonObject1.getInt("SubCount");

                getLastTeamDetails();
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setcaptdetails(int val, int playerId, String name, ImageView layoutID) {
        switch (val) {
            case 1:
                catpID = playerId;
                catpName = name;
                this.oldCaptLayoutID = layoutID.getId();
                break;

            case 2:
                vsCaptID = playerId;
                viceCatpName=name;
                this.oldViceCaptLayoutID = layoutID.getId();
                break;
        }
    }

    @Override
    public void saveTeamResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_savechanged));
                finish();
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.btnSaveTeam.setEnabled(true);
        binding.btnSaveTeam1.setEnabled(true);
        binding.pBar.setVisibility(View.GONE);
    }

    private void setPlayerData(ArrayList<PlayerDataType> playerList) {
        int usedBudget = 0;

        Collections.sort(playerList, (lhs, rhs) -> lhs.getPlayerSpeciality().compareTo(rhs.getPlayerSpeciality()));

        selectedPlayerID = new ArrayList<>();

        CurrentPlayerList = new ArrayList<>();

        binding.tvPrediction.setText(playerList.get(0).getWinnerPrediction()+"\nPrediction");
        for (PlayerDataType players : playerList) {

            CurrentPlayerList.add(players);

            usedBudget = usedBudget + players.getPlayerValue();

            if (players.getTeamCapt() == players.getPlayerId()) {
                catpName = players.getPlayerShortName();
                catpID = players.getPlayerId();
            }

            selectedPlayerID.add(players.getPlayerId());

            if (players.getTeamVCapt() == players.getPlayerId()){
                viceCatpName=players.getPlayerShortName();
                vsCaptID = players.getPlayerId();
            }


            switch (players.getPlayerSpeciality()) {
                case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                    if (!fieldList.contains(binding.tvPlayer11Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer11Delete, binding.tvPlayer11Name,
                                binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11);
                        fieldList.add(binding.tvPlayer11Name.getId());
                        presenter.getJsonData(players, binding.rrPlayer11);
                    } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer10Delete, binding.tvPlayer10Name,
                                binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10);

                        presenter.getJsonData(players, binding.rrPlayer10);
                        fieldList.add(binding.tvPlayer10Name.getId());
                    } else {
                        presenter.setPlayerData(players, binding.imgPlayer9Delete, binding.tvPlayer9Name,
                                binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9);
                        fieldList.add(binding.tvPlayer9Name.getId());
                        presenter.getJsonData(players, binding.rrPlayer9);
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                    if (!fieldList.contains(binding.tvPlayer1Name.getId())) {
                        presenter.setPlayerData(players, binding.imgPlayer1Delete, binding.tvPlayer1Name,
                                binding.tvPlayer1Captain, binding.tvPlayer1Point, binding.imgPlayer1);
                        fieldList.add(binding.tvPlayer1Name.getId());
                        presenter.getJsonData(players, binding.rrPlayer1);
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

        RemainingBudget = TotalBudget - usedBudget;

        setBudget();

        if (!isFirstTransferCounted) {
            firstSubsLeft = 0;
            isFirstTransferCounted = true;
            for (PlayerDataType playerDataType : playerList) {
                if (!oldPlayerID.contains(playerDataType.getPlayerId()))
                    firstSubsLeft++;
            }
            transferUsed = firstSubsLeft;
        }

        binding.clTeamDetails.setVisibility(View.VISIBLE);
        binding.clTeamDetailsBlank.setVisibility(View.GONE);

        if (isNewTeam)
            setSelectedData();
        else
            setCurrentData();
    }

    private void setBudget() {
        String text = RemainingBudget + "K";
        binding.tvBudgetAmt.setText(text);
    }

    public void addPlayerData(PlayerDataType players) {
        if (!fieldList.contains(binding.tvPlayer8Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer8Delete, binding.tvPlayer8Name, binding.tvPlayer8Captain,
                    binding.tvPlayer8Point, binding.imgPlayer8);
            presenter.getJsonData(players, binding.rrPlayer8);
            fieldList.add(binding.tvPlayer8Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer7Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer7Delete, binding.tvPlayer7Name, binding.tvPlayer7Captain,
                    binding.tvPlayer7Point, binding.imgPlayer7);
            presenter.getJsonData(players, binding.rrPlayer7);
            fieldList.add(binding.tvPlayer7Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer6Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer6Delete, binding.tvPlayer6Name, binding.tvPlayer6Captain,
                    binding.tvPlayer6Point, binding.imgPlayer6);
            presenter.getJsonData(players, binding.rrPlayer6);
            fieldList.add(binding.tvPlayer6Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer5Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer5Delete, binding.tvPlayer5Name, binding.tvPlayer5Captain,
                    binding.tvPlayer5Point, binding.imgPlayer5);
            presenter.getJsonData(players, binding.rrPlayer5);
            fieldList.add(binding.tvPlayer5Name.getId());
        }else if (!fieldList.contains(binding.tvPlayer4Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer4Delete, binding.tvPlayer4Name, binding.tvPlayer4Captain,
                    binding.tvPlayer4Point, binding.imgPlayer4);
            presenter.getJsonData(players, binding.rrPlayer4);
            fieldList.add(binding.tvPlayer4Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer3Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer3Delete, binding.tvPlayer3Name, binding.tvPlayer3Captain,
                    binding.tvPlayer3Point, binding.imgPlayer3);
            presenter.getJsonData(players, binding.rrPlayer3);
            fieldList.add(binding.tvPlayer3Name.getId());
        } else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer2Delete, binding.tvPlayer2Name, binding.tvPlayer2Captain,
                    binding.tvPlayer2Point, binding.imgPlayer2);
            presenter.getJsonData(players, binding.rrPlayer2);
            fieldList.add(binding.tvPlayer2Name.getId());
        }else if (!fieldList.contains(binding.tvPlayer9Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer9Delete, binding.tvPlayer9Name,
                    binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9);
            fieldList.add(binding.tvPlayer9Name.getId());
            presenter.getJsonData(players, binding.rrPlayer9);
        } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer10Delete, binding.tvPlayer10Name,
                    binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10);
            fieldList.add(binding.tvPlayer10Name.getId());
            presenter.getJsonData(players, binding.rrPlayer10);
        } else if (!fieldList.contains(binding.tvPlayer11Name.getId())) {
            presenter.setPlayerData(players, binding.imgPlayer11Delete, binding.tvPlayer11Name,
                    binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11);
            fieldList.add(binding.tvPlayer11Name.getId());
            presenter.getJsonData(players, binding.rrPlayer11);
        }
    }

    @Override
    public void LastPlayedTeamPlayersListing(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                oldPlayerList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (oldPlayerList.size() > 0) {
                    for (PlayerDataType playerDataType : oldPlayerList) {
                        oldPlayerID.add(playerDataType.getPlayerId());
                    }

                    selectedPlayerID = new ArrayList<>();

                    if (oldPlayerID.size() > 0)
                        selectedPlayerID.addAll(oldPlayerID);

                    if (oldPlayerList.size() > 0)
                        binding.btnReset.setEnabled(true);
                    else
                        binding.btnReset.setEnabled(false);
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getData();
    }

    @Override
    public void CheckLastCuttOff(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                oldPlayerList = new ArrayList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (oldPlayerList.size() > 0) {
                    for (PlayerDataType playerDataType : oldPlayerList) {
                        oldPlayerID.add(playerDataType.getPlayerId());
                    }

                    transferUsed = 0;

                    if (oldPlayerID.size() > 0) {
                        for (int value : selectedPlayerID) {
                            if (!oldPlayerID.contains(value))
                                transferUsed++;
                        }
                    }

                    runOnUiThread(() -> {
                        getWindow().getDecorView().post(() -> {
                            showComfirmDialog();
                        });
                    });
                } else
                    runOnUiThread(() -> {
                        getWindow().getDecorView().post(() -> {
                            showComfirmDialog();
                        });
                    });
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PositiveResponse(int val, int playerId, String name, ImageView layoutID) {
        switch (val) {
            case 1:
                catpID = playerId;
                catpName = name;

                if (vsCaptID == playerId) {
                    vsCaptID = 0;
                    RemoveCaptain(oldViceCaptLayoutID);
                    oldViceCaptLayoutID = 0;
                }

                RemoveCaptain(oldCaptLayoutID);
                SetCaptain(layoutID.getId(), getString(R.string.captaintext));
                oldCaptLayoutID = layoutID.getId();
                break;

            case 2:
                vsCaptID = playerId;
                viceCatpName=name;

                if (catpID == playerId) {
                    catpID = 0;
                    RemoveCaptain(oldCaptLayoutID);
                    oldCaptLayoutID = 0;
                }

                RemoveCaptain(oldViceCaptLayoutID);
                SetCaptain(layoutID.getId(), getString(R.string.vicecaptaintext));
                oldViceCaptLayoutID = layoutID.getId();
                break;
        }
    }

    private void SetCatainDetails(ImageView filed, String text) {
        filed.setVisibility(View.VISIBLE);

        switch (text)
        {
            case Constants.VICECAPTAIN:
                filed.setImageResource(R.drawable.ic_vc);
                break;

            case Constants.CAPTAIN:
                filed.setImageResource(R.drawable.ic_c);
                break;
        }
    }

    private void RemoveCatainDetails(ImageView filed, RelativeLayout rrPlayer) {
        PlayerDataType data = gson.fromJson((String) rrPlayer.getTag(), PlayerDataType.class);
        data.setTeamCapt(catpID);
        data.setTeamVCapt(vsCaptID);
        presenter.getJsonData(data, rrPlayer);
        filed.setVisibility(View.GONE);
    }

    public void SetCaptain(int id, String text) {
        if (binding.tvPlayer1Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer1Captain, text);
        else if (binding.tvPlayer2Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer2Captain, text);
        else if (binding.tvPlayer3Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer3Captain, text);
        else if (binding.tvPlayer4Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer4Captain, text);
        else if (binding.tvPlayer5Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer5Captain, text);
        else if (binding.tvPlayer6Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer6Captain, text);
        else if (binding.tvPlayer7Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer7Captain, text);
        else if (binding.tvPlayer8Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer8Captain, text);
        else if (binding.tvPlayer9Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer9Captain, text);
        else if (binding.tvPlayer10Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer10Captain, text);
        else if (binding.tvPlayer11Captain.getId() == id)
            SetCatainDetails(binding.tvPlayer11Captain, text);
    }

    public void RemoveCaptain(int id) {
        if (binding.tvPlayer1Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer1Captain, binding.rrPlayer1);
        else if (binding.tvPlayer2Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer2Captain, binding.rrPlayer2);
        else if (binding.tvPlayer3Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer3Captain, binding.rrPlayer3);
        else if (binding.tvPlayer4Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer4Captain, binding.rrPlayer4);
        else if (binding.tvPlayer5Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer5Captain, binding.rrPlayer5);
        else if (binding.tvPlayer6Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer6Captain, binding.rrPlayer6);
        else if (binding.tvPlayer7Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer7Captain, binding.rrPlayer7);
        else if (binding.tvPlayer8Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer8Captain, binding.rrPlayer8);
        else if (binding.tvPlayer9Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer9Captain, binding.rrPlayer9);
        else if (binding.tvPlayer10Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer10Captain, binding.rrPlayer10);
        else if (binding.tvPlayer11Captain.getId() == id)
            RemoveCatainDetails(binding.tvPlayer11Captain, binding.rrPlayer11);
    }

    private void showResetTooltip(String text, CustomButton rrPlayer) {
        Tooltip.make(this,
                new Tooltip.Builder(101)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .anchor(rrPlayer, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 3000)
                        .activateDelay(700)
                        .showDelay(200)
                        .text(text)
                        .maxWidth(450)
                        .withArrow(true)
                        .withOverlay(true).build()
        ).show();
    }

    @Override
    public void ConfirmResponse(Boolean value, String predictTeam) {
        this.predictTeam = predictTeam;
        setSelectedData();
        SaveTeam();
    }

    @Override
    public void PositiveResponse(Boolean value) {
        transferUsed = 0;
        isNewTeam = true;

        AutoPilotUsed = oldPlayerList.get(0).isAutoPilotUsed();
        PainKillerUsed = oldPlayerList.get(0).isPainKillerUsed();
        NitroUsed = oldPlayerList.get(0).isNitroUsed();

        setHeaderData();

        fieldList = new ArrayList<>();

        oldSubsLeft = SubsLeftAtSnapShot;
        setTransferLeft(SubsLeftAtSnapShot);
        setPlayerData(oldPlayerList);
    }

    @Override
    public void getTournamentMatchList(JSONObject jsonObject) {
        Log.d("sajhshdhajkhjdkhjkas","dsds :: "+jsonObject);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                matchList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UpcomingMatchModel>>() {
                                }.getType())
                );
                if (matchList.size() > 0) {
                    Log.d("sajhshdhajkhjdkhjkas","dsdsds11");
                    binding.pbMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.GONE);
                    binding.rvMatch.setVisibility(View.VISIBLE);

                    Team1ShortName = matchList.get(0).getTeam1ShortName();
                    Team2ShortName = matchList.get(0).getTeam2ShortName();
                    uAdapter.AddData(matchList);

                } else {
                    Log.d("sajhshdhajkhjdkhjkas","dsdsds22");
                    binding.pbMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setText(getString(R.string.text_thistournamenthasfinished));
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
