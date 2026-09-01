package com.yorker.fanzania.views.screens.tournament.createteam;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.ActivityCreateTeamNewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateTeamActivity extends BaseActivity<CreateTeamPresenter>
        implements CreateTeamPresenter.IMainView, PlayerActionDialog.IPlayerActionDialog, CustomeAlertDialog.ICommonDialog {

    private CreateTeamPresenter presenter;
    private ActivityCreateTeamNewBinding binding;

    private String tournamentID;
    private int userTeamId;

    private Boolean isTournamentInProgress = false;

    private int SubsLeftAtSnapShot = 0;
    private int transferLeft = 0;
    private int TotalBudget = 0;
    private int RemainingBudget = 0;

    private int nitroLeft = 0;
    private int painKillerLeft = 0;
    private int autoPilotLeft = 0;
    private int isNitroUsed = 0;
    private int isPainKillerUsed = 0;
    private int isAutoPilotUsed = 0;

    private int catpID = 0;
    private String catpName;
    private int vsCaptID = 0;
    private String viceCatpName;

    private Gson gson = new Gson();

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

    private ArrayList<Integer> fieldList;
    private ArrayList<Integer> selectedPlayerID = new ArrayList<>();
    private LinkedList<PlayerDataType> playerListing = new LinkedList<>();
    private LinkedList<UpcomingMatchModel> matchList = new LinkedList<>();

    private UpcomingMatchListAdapter uAdapter;

    private int oldCaptLayoutID;
    private int oldViceCaptLayoutID;
    UpcomingMatchModel upcomingMatchModelOne = null;
    String predictTeam = null;

    @Override
    protected CreateTeamPresenter onCreatePresenter() {
        presenter = new CreateTeamPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, CreateTeamPresenter presenter) {
        CreateTeamPresenterComponent component1 = DaggerCreateTeamPresenterComponent.builder()
                .presenterComponent(component)
                .createTeamApplicationModule(new CreateTeamApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_team_new);

        if (getIntent() != null) {
            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);
            userTeamId = getIntent().getIntExtra(Constants.TAG_USERTEAMID, 0);

            binding.tvTeamName.setText(getIntent().getStringExtra(Constants.TAG_TEAMNAME));

            isTournamentInProgress = getIntent().getStringExtra(Constants.TAG_TOURNAMENTSTATUS).equals("INPROGRESS");

            getTeamRuleData();
            getMatches();
        }
        init();
    }

    private void init() {
        initView();
        initListner();
        initSwitches();
    }

    private void getMatchData() {
        if (CheckInternetConnection())
            presenter.MatchDetails(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getTournamentMatchList(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_5);
    }

    private void getTeamRuleData() {
        if (CheckInternetConnection())
            presenter.TeamRule(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initListner() {
        //---------- Player 1 Click----------//

        binding.rrPlayer1.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer1Captain, binding.rrPlayer1);
            }
        });

        //---------- Player 2 Click----------//
        binding.rrPlayer2.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer2Captain, binding.rrPlayer2);
            }
        });

        //---------- Player 3 Click----------//
        binding.rrPlayer3.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer3Captain, binding.rrPlayer3);
            }
        });

        //---------- Player 4 Click----------//
        binding.rrPlayer4.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer4Captain, binding.rrPlayer4);
            }
        });

        //---------- Player 5 Click----------//
        binding.rrPlayer5.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer5Captain, binding.rrPlayer5);
            }
        });

        //---------- Player 6 Click----------//
        binding.rrPlayer6.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer6Captain, binding.rrPlayer6);
            }
        });

        //---------- Player 7 Click----------//
        binding.rrPlayer7.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer7Captain, binding.rrPlayer7);
            }
        });

        //---------- Player 8 Click----------//
        binding.rrPlayer8.setOnClickListener(view -> {
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer8Captain, binding.rrPlayer8);
            }
        });

        //---------- Player 9 Click----------//
        binding.rrPlayer9.setOnClickListener(view -> {
            if (view.getTag() != null) {
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
            if (view.getTag() != null) {
                PlayerDataType data = gson.fromJson((String) view.getTag(), PlayerDataType.class);
                new PlayerActionDialog(this, this, data, binding.tvPlayer11Captain, binding.rrPlayer11);
            }
        });

        //---------- Reset Button-----------//
        binding.btnAutofill.setOnClickListener(view -> {
//            if (transferLeft > 11) {
            if (selectedPlayerID.size() == 0)
                getAutoPlayers();
//            } else
//                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_donthaveenoughtransfer));
        });

        //----------Save ------//
        binding.btnSaveTeam.setOnClickListener(view -> {
            if (selectedPlayerID.size() == 11) {
                if (catpID == 0)
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectcapt));
                else {
                    if (vsCaptID == 0)
                        CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectvcapt));
                    else
                        showComfirmDialog();
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectallplayers));
        });

        binding.btnReplace.setOnClickListener(view -> {
            if (isTournamentInProgress) {
                if (transferLeft >= 1)
                    ReplacePlayer();
                else
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_youdonthavetransfer));
            } else
                ReplacePlayer();
        });
    }

    public void ReplacePlayer() {
        Gson gson = new Gson();
        String PlayerLIst = gson.toJson(playerListing);

        startActivityForResult(new Intent(this, PlayerListActivity.class)
                        .putExtra(Constants.TAG_TOURNAMENTID, tournamentID)
                        .putExtra(Constants.TAG_TOURNAMENTSTATUS, false)
                        .putExtra(Constants.TAG_PLAYERLIST, PlayerLIst)
                        .putExtra(Constants.TAG_TotalBudget, RemainingBudget)
                        .putExtra(Constants.TAG_SubsLeftAtSnapShot, transferLeft)
                        .putExtra(Constants.TAG_SubsLeft, SubsLeftAtSnapShot)
                , Constants.REQ_CODE_PLAYER);
    }

    private void showComfirmDialog() {
        if (selectedPlayerID.contains(catpID)) {
            CustomeAlertDialog.show(this, getString(R.string.text_teamconfirmation), catpName, viceCatpName,
                    -1, this, isTournamentInProgress,
                    isAutoPilotUsed, isNitroUsed, isPainKillerUsed, upcomingMatchModelOne, predictTeam);
        } else {
            CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_selectcaptain));
        }
    }

    private void saveData() {
        if (CheckInternetConnection()) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.TAG_USERTEAMID, userTeamId);
            map.put(Constants.TAG_TEAMCAPT, catpID);
            map.put(Constants.TAG_TEAMCAPTVC, vsCaptID);
            map.put(Constants.TAG_NUMBEROFSUBS, 0);
            map.put(Constants.TAG_NITROUSED, isNitroUsed);
            map.put(Constants.TAG_PAINKILLERUSED, isPainKillerUsed);
            map.put(Constants.TAG_AUTOPILOTUSED, isAutoPilotUsed);
            if (predictTeam != null) {
                map.put("WinnerPrediction", predictTeam);
            }
            for (int i = 0; i < selectedPlayerID.size(); i++) {
                map.put(Constants.TAG_PLAYER + (i + 1), selectedPlayerID.get(i));
            }
            presenter.SaveTeamPlayers(map);

            binding.btnSaveTeam.setEnabled(false);
            binding.pBar.setVisibility(View.VISIBLE);
        } else
            new NoNetworkDialog(this, this, Constants.APICALL_4);
    }

    private void initSwitches() {
        binding.scNitro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (nitroLeft > 0) {
                if (isChecked) {
                    binding.scNitro.setTextColor(getResources().getColor(R.color.colorRed));
                    isNitroUsed = 1;
                    isAutoPilotUsed = 0;
                    isPainKillerUsed = 0;
                    binding.scPainKiller.setChecked(false);
                    binding.scAutoCaptain.setChecked(false);
                } else {
                    binding.scNitro.setTextColor(getResources().getColor(R.color.colorText));
                    isNitroUsed = 0;
                }

            }
        });

        binding.scPainKiller.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (painKillerLeft > 0) {
                if (isChecked) {
                    binding.scPainKiller.setTextColor(getResources().getColor(R.color.colorRed));
                    isPainKillerUsed = 1;
                    isAutoPilotUsed = 0;
                    isNitroUsed = 0;
                    binding.scNitro.setChecked(false);
                    binding.scAutoCaptain.setChecked(false);
                } else {
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
                    isPainKillerUsed = 0;
                    isNitroUsed = 0;
                    binding.scPainKiller.setChecked(false);
                    binding.scNitro.setChecked(false);
                } else {
                    binding.scAutoCaptain.setTextColor(getResources().getColor(R.color.colorText));
                    isAutoPilotUsed = 0;
                }

            }
        });
    }

    private void getAutoPlayers() {
        if (CheckInternetConnection()) {
            binding.clTeamDetails.setVisibility(View.GONE);
            binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
            presenter.AutoFillTeam(tournamentID);
        } else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    private void initView() {
        fieldList = new ArrayList<>();
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_createtam));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.rvMatch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        uAdapter = new UpcomingMatchListAdapter(matchList);
        binding.rvMatch.setAdapter(uAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);

        MontserratRegular tvStats = menu.findItem(R.id.action_item_one).getActionView().findViewById(R.id.tvActionApply);
        tvStats.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_info, 0, 0);
        tvStats.setText(getString(R.string.text_info));

        tvStats.setOnClickListener(view ->
                new TeamInfoDialog(this, MaxWicketKeeper, MinWicketKeeper, MaxAllrounder, MinAllrounder, MaxBatsman, MinBatsman, MaxBowler, MinBowler,
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
                presenter.MatchDetails(tournamentID);
                break;

            case Constants.APICALL_3:
                presenter.AutoFillTeam(tournamentID);
                break;

            case Constants.APICALL_4:
                saveData();
                break;

            case Constants.APICALL_5:
                getMatches();
                break;
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
                viceCatpName = name;

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
        switch (text) {
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

    @Override
    public void getTeamRuleDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                TotalBudget = jsonObject1.getInt("TotalBudget");
                RemainingBudget = TotalBudget;

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

                setBudget();

                getMatchData();

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
                viceCatpName = name;
                this.oldViceCaptLayoutID = layoutID.getId();
                break;
        }
    }

    @Override
    public void getMatchDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                SubsLeftAtSnapShot = jsonObject1.getInt("SubsLeftAtSnapShot");
                transferLeft = SubsLeftAtSnapShot;
                nitroLeft = jsonObject1.getInt("NitroLeft");
                painKillerLeft = jsonObject1.getInt("PainKillerLeft");
                autoPilotLeft = jsonObject1.getInt("AutoPilotLeft");
                predictTeam = jsonObject1.getString("WinnerPrediction");
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAutoTeamPLayers(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                binding.clTeamDetailsBlank.setVisibility(View.GONE);
                binding.clTeamDetails.setVisibility(View.VISIBLE);
                playerListing = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (playerListing.size() > 0) {
                    setPlayerData();
                } else {
                    binding.clTeamDetails.setVisibility(View.VISIBLE);
                    binding.clTeamDetailsBlank.setVisibility(View.GONE);
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTeamResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_savechanged));
                finish();
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.btnSaveTeam.setEnabled(true);
        binding.pBar.setVisibility(View.GONE);
    }

    private void setPlayerData() {
        int usedBudget = 0;

        selectedPlayerID = new ArrayList<>();

        for (PlayerDataType players : playerListing) {

            selectedPlayerID.add(players.getPlayerId());

            if (players.getTeamCapt() == players.getPlayerId()) {
                catpName = players.getPlayerName();
                catpID = players.getPlayerId();
            }

            if (players.getTeamVCapt() == players.getPlayerId()) {
                viceCatpName = players.getPlayerName();
                vsCaptID = players.getPlayerId();
            }

            usedBudget = usedBudget + players.getPlayerValue();
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

        binding.clTeamDetails.setVisibility(View.VISIBLE);
        binding.clTeamDetailsBlank.setVisibility(View.GONE);
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
        } else if (!fieldList.contains(binding.tvPlayer4Name.getId())) {
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
        } else if (!fieldList.contains(binding.tvPlayer9Name.getId())) {
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

    private void setBudget() {
        String text = String.valueOf(RemainingBudget) + "K";
        binding.tvBudgetAmt.setText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Constants.REQ_CODE_PLAYER) {
            if (data != null) {

                binding = DataBindingUtil.setContentView(this, R.layout.activity_create_team_new);
                init();

                vsCaptID = 0;
                catpID = 0;

                binding.tvTeamName.setText(getIntent().getStringExtra(Constants.TAG_TEAMNAME));

                selectedPlayerID = new ArrayList<>();

                binding.clTeamDetailsBlank.setVisibility(View.GONE);
                binding.clTeamDetails.setVisibility(View.VISIBLE);

                if (data.getStringExtra(Constants.TAG_PLAYERLIST) != null)
                    new Handler().postDelayed(() -> setNewPLayerLIst(data.getStringExtra(Constants.TAG_PLAYERLIST)), 150);
            }
        }
    }

    private void setNewPLayerLIst(String stringExtra) {
        if (stringExtra.length() > 0) {
            playerListing = new LinkedList<>(
                    new Gson().fromJson(
                            stringExtra
                            , new TypeToken<List<PlayerDataType>>() {
                            }.getType())
            );

            setPlayerData();
        }
    }

    @Override
    public void getTournamentMatchList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                matchList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<UpcomingMatchModel>>() {
                                }.getType())
                );
                Log.d("kdhjfkdsgjfgdshfgdsh","fdsfdsfd "+matchList.size());
                if (matchList.size() > 0) {
                    binding.pbMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.GONE);
                    uAdapter.AddData(matchList);
                    upcomingMatchModelOne = matchList.get(0);
                } else {
                    Log.d("sajhshdhajkhjdkhjkas","dsdsdsds ");
                    binding.pbMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.VISIBLE);
                    binding.tvNoMatch.setText(getString(R.string.text_thistournamenthasfinished));
                    upcomingMatchModelOne = null;
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error " + e.toString());
        }
    }

    @Override
    public void ConfirmResponse(Boolean value, String predictTeam) {
        this.predictTeam = predictTeam;
        saveData();
    }
}
