package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.databinding.ActivityPlayerSelectionsBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.McAlertDialog;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.PlayerDetailDialog;
import com.yorker.fanzania.dialog.TeamViewDialog;
import com.yorker.fanzania.views.screens.Home.HomeActivity;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.UpcomingMatchModel;
import com.yorker.fanzania.views.screens.tournament.playerlist.PlayerListActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.yorker.fanzania.widgets.DateUtils.printDifference;

public class PlayerSelectionActivity extends BaseActivity<PlayerSelectionPresenter>
        implements PlayerSelectionPresenter.IMainView, McPlayerListAdapter.IPoints, McAlertDialog.ICommonDialog {

    private ActivityPlayerSelectionsBinding binding;
    private PlayerSelectionPresenter presenter;
    private int matchId;
    private int tournamentId;
    private String matchType;
    private LinkedList<PlayerListModel> pList = new LinkedList<>();
    private LinkedList<Integer> selectedPlayerID = new LinkedList<>();

    private HashMap<Integer, Integer> playerPosition;
    private String playerType = Constants.TAG_PLAYERTYPE_ALL;
    private LinkedList<PlayerListModel> listBySpecility;
    private McPlayerListAdapter pAdapter;
    private List<PlayerListModel> selectedPlayerList = new ArrayList<>();

    private int playerCount = 0;
    private int tabPosition = 0;
    private int remainingBudget = -1;

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

    private int batsmanCount = 0;
    private int bolwerCount = 0;
    private int allrunderCount = 0;
    private int wicketKeeperCount = 0;

    private int isPriceSortUsed = 0;
    private int isPointSortUsed = 0;

    private int catpID = 0;
    private String catpName;
    private int vsCaptID = 0;
    private String viceCatpName;
    private boolean isFromMatch = false;

    private SimpleTooltip tooltip;
    String Team1ShortName = null, Team2ShortName = null, predictTeam = null;

    @Override
    protected PlayerSelectionPresenter onCreatePresenter() {
        presenter = new PlayerSelectionPresenter(this, PlayerSelectionActivity.this);
        return presenter;
    }

    @Override
    public void onPause() {
        if (tooltip != null)
            tooltip.dismiss();

        super.onPause();
    }


    @Override
    protected void injectPresenter(PresenterComponent component, PlayerSelectionPresenter presenter) {
        PlayerSelectionComponent loginPresenterComponent = DaggerPlayerSelectionComponent.builder()
                .presenterComponent(component)
                .playerSelectionApplicationModule(new PlayerSelectionApplicationModule(PlayerSelectionActivity.this))
                .build();
        loginPresenterComponent.inject(presenter);
    }

    private void initViews() {

        Log.e("playerSelection","true");
        matchId = getIntent().getIntExtra(Constants.TAG_MATCHID, 0);
        matchType = getIntent().getStringExtra(Constants.TAG_MATCHTYPE);
        Team1ShortName = getIntent().getStringExtra("Team1ShortName");
        Team2ShortName = getIntent().getStringExtra("Team2ShortName");
        predictTeam = getIntent().getStringExtra("predictTeam");

        String matchDate = getIntent().getStringExtra(Constants.TAG_MATCHDATE);
        tournamentId = getIntent().getIntExtra(Constants.TAG_TOURNAMENTID, 0);
        isFromMatch = getIntent().getBooleanExtra(Constants.TAG_PAGE, false);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getIntent().getStringExtra(Constants.TAG_HEADER));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.inRecyclerview.rvList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        binding.inRecyclerview.rvList.setLayoutManager(linearLayoutManager);
        binding.inRecyclerview.rvList.addItemDecoration(new ItemDecoration(this));

        try {
            SimpleDateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            normalDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date2 = normalDateFormat.parse(matchDate);
            Date date1 = normalDateFormat.parse(normalDateFormat.format(Calendar.getInstance().getTime()));
            printDifference(date1, date2, binding.tvTimeRemaining);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.tvPriceSort.setOnClickListener(view -> {
            switch (isPriceSortUsed) {
                case 0:
                    Collections.sort(listBySpecility, (obj1, obj2) -> {
                        return Integer.compare(obj1.getPlayerValue(), obj2.getPlayerValue()); // To compare string values
                    });
                    isPriceSortUsed = 1;
                    break;

                case 1:
                    Collections.sort(listBySpecility, (obj1, obj2) -> {
                        return Integer.compare(obj2.getPlayerValue(), obj1.getPlayerValue()); // To compare string values
                    });
                    isPriceSortUsed = 0;
                    break;
            }
            pAdapter.sortData(listBySpecility);
        });

        binding.tvPointsSort.setOnClickListener(view -> {
            switch (isPointSortUsed) {
                case 0:
                    Collections.sort(listBySpecility, (obj1, obj2) -> {
                        return Integer.compare(obj1.getTotalPoints(), obj2.getTotalPoints()); // To compare string values
                    });
                    isPointSortUsed = 1;
                    break;

                case 1:
                    Collections.sort(listBySpecility, (obj1, obj2) -> {
                        return Integer.compare(obj2.getTotalPoints(), obj1.getTotalPoints()); // To compare string values
                    });
                    isPointSortUsed = 0;
                    break;
            }
            pAdapter.sortData(listBySpecility);
        });

        binding.tbSort.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);
                binding.inRecyclerview.rvList.setVisibility(View.GONE);
                setTabWiseList(tabPosition, pList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.tvPlayerName.setOnClickListener(view -> {
            if (binding.explPlayerSearch.isExpanded()) {
                binding.explPlayerSearch.collapse();
                binding.edtSearch.setText("");
            } else
                binding.explPlayerSearch.expand();
        });

        binding.tvSPlayers.setOnClickListener(view -> {
            TeamViewDialog dialog = new TeamViewDialog(this, selectedPlayerList);
            dialog.show(getSupportFragmentManager(), "teamview");
        });

        binding.btnSave.setOnClickListener(view -> {

            if (selectedPlayerList.size() > 0) {
                if (selectedPlayerList.size() == 11) {
                    if (wicketKeeperCount >= MinWicketKeeper && wicketKeeperCount <= MaxWicketKeeper) {
                        if (allrunderCount >= MinAllrounder && allrunderCount <= MaxAllrounder) {
                            if (batsmanCount >= MinBatsman && batsmanCount <= MaxBatsman) {
                                if (bolwerCount >= MinBowler && bolwerCount <= MaxBowler) {
                                    int valueST = presenter.getSameTeamList(selectedPlayerList);
                                    if (valueST > MaxSameTeamPlayer) {
                                        String text = getString(R.string.text_maxsameplayer) + " " + MaxSameTeamPlayer;
                                        CustomToast.getInstance(this).showSmallCustomToast(text);
                                    } else {
                                        int valueOS = presenter.getOverSeasList(selectedPlayerList);
                                        if (valueOS > MaxOverseasPlayer) {
                                            String text = getString(R.string.text_maxoverseasplayer) + " " + MaxOverseasPlayer;
                                            CustomToast.getInstance(this).showSmallCustomToast(text);
                                        } else {
                                            if (catpID == 0)
                                                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectcapt));
                                            else {
                                                if (vsCaptID == 0)
                                                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectvcapt));
                                                else
                                                    showComfirmDialog();
                                            }
                                        }
                                    }
                                } else {
                                    String text = getString(R.string.text_infotext2) + " " + MinBowler + "-" + MaxBowler + " " + getString(R.string.text_bowler);
                                    CustomToast.getInstance(this).showSmallCustomToast(text);
                                }
                            } else {
                                String text = getString(R.string.text_infotext2) + " " + MinBatsman + "-" + MaxBatsman + " " + getString(R.string.text_batsman);
                                CustomToast.getInstance(this).showSmallCustomToast(text);
                            }
                        } else {
                            String text = getString(R.string.text_infotext2) + " " + MinAllrounder + "-" + MaxAllrounder + " " + getString(R.string.text_allrounder);
                            CustomToast.getInstance(this).showSmallCustomToast(text);
                        }
                    } else {
                        String text = getString(R.string.text_infotext2) + " " + MinWicketKeeper + "-" + MaxWicketKeeper + " " + getString(R.string.text_wicketkeeperrequired);
//                        String text = getString(R.string.text_minimum) + " " + WicketKeeper + " " + getString(R.string.text_wicketkeeperrequired);
                        CustomToast.getInstance(this).showSmallCustomToast(text);
                    }
                } else
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_pleaseselectallplayers));
            }
        });

        binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);

        getTeamRules();
        getPlayers();
    }

    private void showComfirmDialog() {
        if (selectedPlayerID.contains(catpID))
            new McAlertDialog(this, getString(R.string.text_teamconfirmation), catpName, viceCatpName, this, Team1ShortName, Team2ShortName, predictTeam);
        else
            CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_selectcaptain));
    }

    private void getPlayers() {
        if (CheckInternetConnection())
            presenter.matchPlayers(matchId, matchType);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void getTeamRules() {
        if (CheckInternetConnection())
            presenter.TeamRule(matchId, matchType);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_selections);
        initViews();
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                getPlayers();
                break;

            case Constants.APICALL_2:
                getTeamRules();
                break;

            case Constants.APICALL_3:
                saveTeam();
                break;
        }
    }

    @Override
    public void getMatchPlayres(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                binding.inRecyclerview.pBar.setVisibility(View.GONE);
                pList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerListModel>>() {
                                }.getType())
                );

                if (pList.size() > 0) {
                    predictTeam = pList.get(0).getWinnerPrediction();
                    listBySpecility = pList;
                    selectedPlayerID = new LinkedList<>();
                    selectedPlayerList = new ArrayList<>();
                    for (PlayerListModel data : pList) {
                        if (playerPosition != null && playerPosition.size() > 0 && playerPosition.containsKey(data.getPlayerId()))
                            selectedPlayerList.get(playerPosition.get(data.getPlayerId())).setTotalPoints(data.getTotalPoints());

                        if (data.isPlayerSelected()) {
                            setPlayerCount(data);
                        }
                    }
                    setTabWiseList(tabPosition, listBySpecility);
                    if (presenter.checkToolTip() == 1) {
                        showTooltip();
                    }
                } else {
                    binding.inRecyclerview.rvList.setVisibility(View.GONE);
                    binding.inRecyclerview.pBar.setVisibility(View.GONE);
                    binding.inRecyclerview.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inRecyclerview.tvNoDataFound.setText(jsonObject.getString("statusMessage"));
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTabWiseList(int tabPosition, LinkedList<PlayerListModel> list) {
        switch (tabPosition) {
            case 0:
                setAllPlayerList(list);
                break;

            case 1:
                setBatsmanPlayerList(list);
                break;

            case 2:
                setWicketKeeperPlayerList(list);
                break;

            case 3:
                setAllRounderPlayerList(list);
                break;

            case 4:
                setBowlerPlayerList(list);
                break;
        }
        binding.inRecyclerview.rvList.setVisibility(View.VISIBLE);
        binding.inRecyclerview.pBar.setVisibility(View.GONE);

    }

    private void setBowlerPlayerList(LinkedList<PlayerListModel> plist) {
        playerType = Constants.TAG_PLAYERTYPE_BLOWER;
        listBySpecility = new LinkedList<>();
        for (PlayerListModel playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_BLOWER)) {
                if (playerDataType.getPlayerId() == catpID) {
                    playerDataType.setTeamCapt(true);
                    playerDataType.setTeamVCapt(false);
                } else
                    playerDataType.setTeamCapt(false);

                if (playerDataType.getPlayerId() == vsCaptID) {
                    playerDataType.setTeamVCapt(true);
                    playerDataType.setTeamCapt(false);
                } else
                    playerDataType.setTeamVCapt(false);

                listBySpecility.add(playerDataType);
            }
        }
        refreshAdapter(listBySpecility);
    }

    private void setAllRounderPlayerList(LinkedList<PlayerListModel> plist) {
        playerType = Constants.TAG_PLAYERTYPE_ALLROUNDER;
        listBySpecility = new LinkedList<>();
        for (PlayerListModel playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_ALLROUNDER)) {
                if (playerDataType.getPlayerId() == catpID) {
                    playerDataType.setTeamCapt(true);
                    playerDataType.setTeamVCapt(false);
                } else
                    playerDataType.setTeamCapt(false);

                if (playerDataType.getPlayerId() == vsCaptID) {
                    playerDataType.setTeamVCapt(true);
                    playerDataType.setTeamCapt(false);
                } else
                    playerDataType.setTeamVCapt(false);

                listBySpecility.add(playerDataType);
            }

        }
        refreshAdapter(listBySpecility);
    }

    private void setWicketKeeperPlayerList(LinkedList<PlayerListModel> plist) {
        playerType = Constants.TAG_PLAYERTYPE_WICKETKEEPER;
        listBySpecility = new LinkedList<>();
        for (PlayerListModel playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_WICKETKEEPER)) {
                if (playerDataType.getPlayerId() == catpID) {
                    playerDataType.setTeamCapt(true);
                    playerDataType.setTeamVCapt(false);
                } else
                    playerDataType.setTeamCapt(false);

                if (playerDataType.getPlayerId() == vsCaptID) {
                    playerDataType.setTeamVCapt(true);
                    playerDataType.setTeamCapt(false);
                } else
                    playerDataType.setTeamVCapt(false);

                listBySpecility.add(playerDataType);
            }

        }
        refreshAdapter(listBySpecility);
    }

    private void setAllPlayerList(LinkedList<PlayerListModel> list) {
        playerType = Constants.TAG_PLAYERTYPE_ALL;
//        listBySpecility = list;

        LinkedList<PlayerListModel> thingsToBeAdd = new LinkedList<>();

        for (Iterator<PlayerListModel> it = list.iterator(); it.hasNext(); ) {
            PlayerListModel playerDataType = it.next();
            if (playerDataType.getPlayerId() == catpID) {
                playerDataType.setTeamCapt(true);
                playerDataType.setTeamVCapt(false);
            } else
                playerDataType.setTeamCapt(false);

            if (playerDataType.getPlayerId() == vsCaptID) {
                playerDataType.setTeamVCapt(true);
                playerDataType.setTeamCapt(false);
            } else
                playerDataType.setTeamVCapt(false);

            thingsToBeAdd.add(playerDataType);
        }
        listBySpecility = thingsToBeAdd;

//        for (PlayerListModel playerDataType : list) {
//            if (playerDataType.getPlayerId() == catpID) {
//                playerDataType.setTeamCapt(true);
//                playerDataType.setTeamVCapt(false);
//            }
//
//            if (playerDataType.getPlayerId() == vsCaptID) {
//                playerDataType.setTeamVCapt(true);
//                playerDataType.setTeamCapt(false);
//            }
//
//            listBySpecility.add(playerDataType);
//        }

        setData(listBySpecility);
    }

    private void setData(LinkedList<PlayerListModel> list) {
        binding.inRecyclerview.rvList.setVisibility(View.VISIBLE);
        binding.inRecyclerview.pBar.setVisibility(View.GONE);
        binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
        pAdapter = new McPlayerListAdapter(this, list, this, playerCount, remainingBudget,
                MaxWicketKeeper, MaxBowler, MaxBatsman, MaxAllrounder, wicketKeeperCount, bolwerCount, batsmanCount, allrunderCount,
                MaxSameTeamPlayer, MaxOverseasPlayer, new McPlayerListAdapter.ItemClickListener() {
            @Override
            public void onPlayerClick(PlayerListModel playerId) {
                presenter.getPlayerDetail(playerId, matchId);
            }
        });

        binding.inRecyclerview.rvList.setAdapter(pAdapter);
    }

    private void setBatsmanPlayerList(LinkedList<PlayerListModel> plist) {
        playerType = Constants.TAG_PLAYERTYPE_BATSMAN;
        listBySpecility = new LinkedList<>();
        for (PlayerListModel playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_BATSMAN)) {
                if (playerDataType.getPlayerId() == catpID) {
                    playerDataType.setTeamCapt(true);
                    playerDataType.setTeamVCapt(false);
                } else
                    playerDataType.setTeamCapt(false);

                if (playerDataType.getPlayerId() == vsCaptID) {
                    playerDataType.setTeamVCapt(true);
                    playerDataType.setTeamCapt(false);
                } else
                    playerDataType.setTeamVCapt(false);

                listBySpecility.add(playerDataType);
            }

        }
        refreshAdapter(listBySpecility);
    }

    private void refreshAdapter(LinkedList<PlayerListModel> list) {
        pAdapter = new McPlayerListAdapter(this, list, this, playerCount, remainingBudget,
                MaxWicketKeeper, MaxBowler, MaxBatsman, MaxAllrounder, wicketKeeperCount, bolwerCount, batsmanCount,
                allrunderCount, MaxSameTeamPlayer, MaxOverseasPlayer, new McPlayerListAdapter.ItemClickListener() {
            @Override
            public void onPlayerClick(PlayerListModel playerId) {
                presenter.getPlayerDetail(playerId, matchId);
            }
        });
        binding.inRecyclerview.rvList.setAdapter(pAdapter);
    }

    @Override
    public void OnSelectPlayer(PlayerListModel playerDataType) {
        if (playerCount < 11) {
            selectedPlayerID.add(playerDataType.getPlayerId());
            selectedPlayerList.add(playerDataType);
            pAdapter.updateSelectedPlayer(selectedPlayerList);

            remainingBudget = remainingBudget - playerDataType.getPlayerValue();
            setBudget();
            pAdapter.refreshBudget(remainingBudget);

            setPlayerData(playerDataType);
        }
    }

    public void setPlayerCount(PlayerListModel playerDataType) {
        remainingBudget = remainingBudget - playerDataType.getPlayerValue();
        setBudget();

        selectedPlayerList.add(playerDataType);
        selectedPlayerID.add(playerDataType.getPlayerId());
        playerCount = playerCount + 1;
        System.out.println("player count activity " + playerCount);

        if (playerDataType.isTeamCapt())
            captainSelect(playerDataType);

        if (playerDataType.isTeamVCapt())
            viceCaptainSelect(playerDataType);

        switch (playerDataType.getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                if (batsmanCount < MaxBatsman) {
                    batsmanCount = batsmanCount + 1;
                    setBatsmanCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                if (allrunderCount < MaxAllrounder) {
                    allrunderCount = allrunderCount + 1;
                    setAllrounderCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_BLOWER:
                if (bolwerCount < MaxBowler) {
                    bolwerCount = bolwerCount + 1;
                    setBowlerCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                if (wicketKeeperCount < MaxWicketKeeper) {
                    wicketKeeperCount = wicketKeeperCount + 1;
                    setKeeperCount();
                    setAllCount();
                }
                break;
        }
        setSelectedText();
    }

    public void setPlayerData(PlayerListModel playerDataType) {
        playerCount = playerCount + 1;
        System.out.println("player count activity " + playerCount);

        switch (playerDataType.getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                if (batsmanCount < MaxBatsman) {
                    batsmanCount = batsmanCount + 1;
                    setBatsmanCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                if (allrunderCount < MaxAllrounder) {
                    allrunderCount = allrunderCount + 1;
                    setAllrounderCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_BLOWER:
                if (bolwerCount < MaxBowler) {
                    bolwerCount = bolwerCount + 1;
                    setBowlerCount();
                    setAllCount();
                }
                break;

            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                if (wicketKeeperCount < MaxWicketKeeper) {
                    wicketKeeperCount = wicketKeeperCount + 1;
                    setKeeperCount();
                    setAllCount();
                }
                break;
        }

        pAdapter.refreshCounts(batsmanCount, bolwerCount, allrunderCount, wicketKeeperCount, playerCount);

        setSelectedText();
    }

    private void setSelectedText() {
        binding.tvAllCount.setText(String.valueOf(playerCount));
    }

    private void setBudget() {
        String text = remainingBudget + "k";
        binding.tvBudgetAmt.setText(text);
    }

    @Override
    public void getTeamRuleDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                System.out.println("Team rules " + jsonObject1.toString());
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

                if (remainingBudget == -1) {
                    remainingBudget = jsonObject1.getInt("TotalBudget");
                    String txt = remainingBudget + "K";
                    binding.tvBudgetAmt.setText(txt);
                    if (pAdapter != null) {
                        pAdapter.updateBudget(remainingBudget);
                        pAdapter.updateLimit(MaxWicketKeeper, MaxBatsman, MaxBowler, MaxAllrounder, MaxSameTeamPlayer, MaxOverseasPlayer);
                    }
                }

                String btxt = MinBatsman + "-" + MaxBatsman;
                binding.tvBatsmanCount.setText(btxt);

                String wktxt = MinWicketKeeper+"-" + MaxWicketKeeper;
                binding.tvKeeperCount.setText(wktxt);

                String artxt = MinAllrounder + "-" + MaxAllrounder;
                binding.tvAllRounderCount.setText(artxt);

                String bwtxt = MinBowler + "-" + MaxBowler;
                binding.tvBowlerCount.setText(bwtxt);

                setKeeperCount();
                setAllrounderCount();
                setBowlerCount();
                setBatsmanCount();
                setAllCount();
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
                if (isFromMatch) {
                    startActivity(new Intent(this, HomeActivity.class)
                            .putExtra(Constants.TAG_MATCHID, matchId)
                            .putExtra(Constants.TAG_INDEX, 3)
                    );
                    finish();
                } else
                    finish();
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.btnSave.setEnabled(true);
        binding.pBar.setVisibility(View.GONE);
    }

    @Override
    public void getPlayerDetails(JSONObject jsonObject) {
        Log.e("success",""+jsonObject);
        PlayerDetailDialog playerDetailDialog = new PlayerDetailDialog(PlayerSelectionActivity.this, jsonObject);
    }

    private void setBatsmanCount() {
        MontserratMedium bc = binding.tbSort.getTabAt(1).getCustomView().findViewById(R.id.tvTabBatsmanCount);
        bc.setText(String.valueOf(batsmanCount));

        if (batsmanCount >= MinBatsman && batsmanCount <= MaxBatsman)
            bc.setTextColor(getResources().getColor(R.color.colorGreen));
        else
            bc.setTextColor(getResources().getColor(R.color.colorRedNew));
    }

    private void setBowlerCount() {
        MontserratMedium bc = Objects.requireNonNull(Objects.requireNonNull(binding.tbSort.getTabAt(4)).getCustomView()).findViewById(R.id.tvTabBowlerCount);
        bc.setText(String.valueOf(bolwerCount));

        if (bolwerCount >= MinBowler && bolwerCount <= MaxBowler)
            bc.setTextColor(getResources().getColor(R.color.colorGreen));
        else
            bc.setTextColor(getResources().getColor(R.color.colorRedNew));
    }

    private void setAllrounderCount() {
        MontserratMedium bc = Objects.requireNonNull(Objects.requireNonNull(binding.tbSort.getTabAt(3)).getCustomView()).findViewById(R.id.tvTabAllrounderCount);
        bc.setText(String.valueOf(allrunderCount));

        if (allrunderCount >= MinAllrounder && allrunderCount <= MaxAllrounder)
            bc.setTextColor(getResources().getColor(R.color.colorGreen));
        else
            bc.setTextColor(getResources().getColor(R.color.colorRedNew));
    }

    private void setKeeperCount() {
        MontserratMedium bc = Objects.requireNonNull(Objects.requireNonNull(binding.tbSort.getTabAt(2)).getCustomView()).findViewById(R.id.tvTabKeeperCount);
        bc.setText(String.valueOf(wicketKeeperCount));

        if (wicketKeeperCount >= MinWicketKeeper && wicketKeeperCount <= MaxWicketKeeper)
            bc.setTextColor(getResources().getColor(R.color.colorGreen));
        else
            bc.setTextColor(getResources().getColor(R.color.colorRedNew));
    }

    private void setAllCount() {
        MontserratMedium bc = Objects.requireNonNull(Objects.requireNonNull(binding.tbSort.getTabAt(0)).getCustomView()).findViewById(R.id.tvTabAllCount);
        bc.setText(String.valueOf(playerCount));

        if (playerCount == 11)
            bc.setTextColor(getResources().getColor(R.color.colorGreen));
        else
            bc.setTextColor(getResources().getColor(R.color.colorRedNew));
    }

    @Override
    public void OnRemovePlayer(PlayerListModel data) {
        if (playerCount > 0) {
            selectedPlayerID.remove(selectedPlayerID.indexOf(data.getPlayerId()));
            selectedPlayerList.remove(data);
            pAdapter.updateSelectedPlayer(selectedPlayerList);
            playerCount--;
            remainingBudget = remainingBudget + data.getPlayerValue();
            pAdapter.refreshBudget(remainingBudget);
            setBudget();

            if (data.isTeamCapt()) {
                catpID = 0;
                catpName = "";
            } else if (data.isTeamVCapt()) {
                vsCaptID = 0;
                viceCatpName = "";
            }

            if (!playerType.equals(Constants.TAG_PLAYERTYPE_ALL)) {
                if (data.getPlayerSpeciality().equals(playerType))
                    pAdapter.removeSelectedPlayer(data, playerCount);
            } else
                pAdapter.removeSelectedPlayer(data, playerCount);

            switch (data.getPlayerSpeciality()) {
                case Constants.TAG_PLAYERTYPE_BATSMAN:
                    if (batsmanCount <= MaxBatsman) {
                        batsmanCount = batsmanCount - 1;
                        setBatsmanCount();
                        setAllCount();
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                    if (allrunderCount <= MaxAllrounder) {
                        allrunderCount = allrunderCount - 1;
                        setAllrounderCount();
                        setAllCount();
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_BLOWER:
                    if (bolwerCount <= MaxBowler) {
                        bolwerCount = bolwerCount - 1;
                        setBowlerCount();
                        setAllCount();
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                    if (wicketKeeperCount <= MaxWicketKeeper) {
                        wicketKeeperCount = wicketKeeperCount - 1;
                        setKeeperCount();
                        setAllCount();
                    }
                    break;
            }
            pAdapter.refreshCounts(batsmanCount, bolwerCount, allrunderCount, wicketKeeperCount, playerCount);
        }
        setSelectedText();
    }

    @Override
    public void captainSelect(PlayerListModel obj) {
        if (obj.getPlayerId() == vsCaptID) {
            vsCaptID = 0;
            viceCatpName = "";
        }

        catpID = obj.getPlayerId();
        catpName = obj.getPlayerName();
    }

    @Override
    public void viceCaptainSelect(PlayerListModel obj) {
        if (obj.getPlayerId() == catpID) {
            catpID = 0;
            catpName = "";
        }

        vsCaptID = obj.getPlayerId();
        viceCatpName = obj.getPlayerName();
    }

    @Override
    public void updateOverSeasCount(int val) {
    }

    @Override
    public void updateSameTeamCount(int val) {
    }

    private void saveTeam() {
        if (CheckInternetConnection()) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.TAG_TEAMCAPT, catpID);
            map.put(Constants.TAG_TEAMCAPTVC, vsCaptID);
            map.put(Constants.TAG_ID, Integer.parseInt(presenter.getUserID()));
            map.put(Constants.TAG_MATCHID, matchId);
            map.put(Constants.TAG_TOURNAMENTID, tournamentId);
            if (predictTeam !=null){
                map.put("WinnerPrediction", predictTeam);
            }
            for (int i = 0; i < selectedPlayerID.size(); i++) {
                map.put(Constants.TAG_PLAYER + (i + 1), selectedPlayerID.get(i));
            }
            System.out.println("save team map " + map.toString());
            presenter.SaveTeamPlayers(map);
            binding.btnSave.setEnabled(false);
            binding.pBar.setVisibility(View.VISIBLE);
        } else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    private void showTooltip() {
        SimpleTooltip.Builder builder = new SimpleTooltip.Builder(this)
                .anchorView(binding.inRecyclerview.vwItem)
                .text(getString(R.string.text_swipetoeview))
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
    public void ConfirmResponse(Boolean value, String predictTeam) {
        this.predictTeam = predictTeam;
        saveTeam();
    }
}
