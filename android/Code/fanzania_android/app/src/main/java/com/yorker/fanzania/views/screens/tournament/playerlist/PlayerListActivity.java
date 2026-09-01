package com.yorker.fanzania.views.screens.tournament.playerlist;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.NewActivityPlayerListBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.PlayerDetailDialog;
import com.yorker.fanzania.dialog.TeamFilterDialog;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.UpcomingMatchModel;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.PlayerListAdapter;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.SelectedPlayerListAdapter;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.UpcomingMatchListAdapter;
import com.yorker.fanzania.views.screens.tournament.playerlist.model.TeamFilterModel;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import com.yorker.fanzania.widgets.ItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class PlayerListActivity extends BaseActivity<PlayerListPresenter>
        implements PlayerListPresenter.IMainView, PlayerListAdapter.IPoints, SelectedPlayerListAdapter.ISelectedPlayer,
        TeamFilterDialog.ITeamFilterDialog {

    private PlayerListPresenter presenter;
    private NewActivityPlayerListBinding binding;

    private LinkedList<PlayerDataType> pList = new LinkedList<>();

    private LinkedList<Integer> selectedPlayerID = new LinkedList<>();
    private ArrayList<Integer> oldPlayerID = new ArrayList<>();
    private HashMap<Integer, Integer> playerPosition;

    private LinkedList<PlayerDataType> selectedPlayerList = new LinkedList<>();

    private LinkedList<TeamFilterModel> tList = new LinkedList<>();
    private LinkedList<PlayerDataType> listBySpecility;

    private LinkedList<String> teamName = new LinkedList<>();

    private LinkedList<UpcomingMatchModel> matchList = new LinkedList<>();

    private SelectedPlayerListAdapter spAdapter;
    private PlayerListAdapter pAdapter;
    private UpcomingMatchListAdapter uAdapter;

    private String tournamentID;
    private String playerType = Constants.TAG_PLAYERTYPE_ALL;

    private int playerCount = 0;
    private int tabPosition = 0;
    private int remainingBudget = 0;

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

    private int SubsLeftAtSnapShot = 0;
    private int transferLeft = 0;

    private int isPriceSortUsed = 0;
    private int isPointSortUsed = 0;

//    private boolean isTooltipShowed=false;

    private Boolean isTournamentInProgress;

    @Override
    protected PlayerListPresenter onCreatePresenter() {
        presenter = new PlayerListPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, PlayerListPresenter presenter) {
        PlayerListPresenterComponent component1 = DaggerPlayerListPresenterComponent.builder()
                .presenterComponent(component)
                .playerListApplicationModule(new PlayerListApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.new_activity_player_list);

        initViews();

        if (getIntent() != null) {
            if (getIntent().getStringExtra(Constants.TAG_LASTCUTOFF) != null) {
                oldPlayerID = new ArrayList<>(
                        new Gson().fromJson(
                                getIntent().getStringExtra(Constants.TAG_LASTCUTOFF)
                                , new TypeToken<List<Integer>>() {
                                }.getType())
                );
            }

            tournamentID = getIntent().getStringExtra(Constants.TAG_TOURNAMENTID);

            isTournamentInProgress = getIntent().getBooleanExtra(Constants.TAG_TOURNAMENTSTATUS, false);

            setSelectedPlayers();

            getTeamRule();

            getTeamListForFilter();
            getMatches();
//            getPlayerDetail();

            remainingBudget = getIntent().getIntExtra(Constants.TAG_TotalBudget, 0);
            SubsLeftAtSnapShot = getIntent().getIntExtra(Constants.TAG_SubsLeft, 0);

            transferLeft = SubsLeftAtSnapShot;
            setTransferLeft(getIntent().getIntExtra(Constants.TAG_SubsLeftAtSnapShot, 0));

            setBudget();
            setSelectedPLayer();
            getPlayers();
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

        binding.tvPlayerName.setOnClickListener(view -> {
            if (binding.explPlayerSearch.isExpanded()) {
                binding.explPlayerSearch.collapse();
                binding.edtSearch.setText("");
            } else
                binding.explPlayerSearch.expand();
        });

        binding.tvFilter.setOnClickListener(view -> new TeamFilterDialog(this, this, tList, teamName));

        initListners();
    }

    private void setSelectedPlayers() {
        String list = getIntent().getStringExtra(Constants.TAG_PLAYERLIST);

        if (list != null && list.length() > 0) {
            selectedPlayerList = new LinkedList<>(
                    new Gson().fromJson(
                            list
                            , new TypeToken<List<PlayerDataType>>() {
                            }.getType())
            );
            playerPosition = new HashMap<>();

            batsmanCount = 0;
            bolwerCount = 0;
            allrunderCount = 0;
            wicketKeeperCount = 0;

            for (PlayerDataType playerDataType : selectedPlayerList) {
                selectedPlayerID.add(playerDataType.getPlayerId());
                playerPosition.put(playerDataType.getPlayerId(), selectedPlayerList.indexOf(playerDataType));
                switch (playerDataType.getPlayerSpeciality()) {
                    case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                        wicketKeeperCount++;
                        setKeeperCount();
                        break;

                    case Constants.TAG_PLAYERTYPE_BATSMAN:
                        batsmanCount++;
                        setBatsmanCount();
                        break;

                    case Constants.TAG_PLAYERTYPE_BLOWER:
                        bolwerCount++;
                        setBowlerCount();
                        break;

                    case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                        allrunderCount++;
                        setAllrounderCount();
                        break;
                }
            }
            playerCount = selectedPlayerID.size();
            setAllCount();
        } else
            setSelectedText();
    }

    private void getMatches() {
        if (CheckInternetConnection())
            presenter.getTournamentMatchList(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_4);
    }

    private void getPlayerDetail(String tournamentID, PlayerDataType playerID) {
        if (CheckInternetConnection())
            presenter.getPlayerDetail(tournamentID,playerID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_5);
    }

    private void getTeamListForFilter() {
        if (CheckInternetConnection())
            presenter.teamFilter(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    private void getTeamRule() {
        if (CheckInternetConnection())
            presenter.TeamRule(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    private void setBudget() {
        String text = remainingBudget + "k";
        binding.tvBudgetAmt.setText(text);
    }

    private void setSelectedPLayer() {
        spAdapter = new SelectedPlayerListAdapter(this, selectedPlayerList, this);
        binding.rvList.setAdapter(spAdapter);
        setSelectedText();

        if (!binding.explPlayers.isExpanded())
            binding.explPlayers.expand();

    }

    private void getPlayers() {
        binding.inRecyclerview.pBar.setVisibility(View.VISIBLE);

        if (CheckInternetConnection())
            presenter.tournamentPlayers(tournamentID);
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void initListners() {

        binding.tbSort.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                setTabWiseList(tabPosition, pList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.tvSPlayers.setOnClickListener(view -> {
            if (binding.explPlayers.isExpanded()) {
                binding.explPlayers.collapse();
                binding.tvSPlayers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow_filled, 0);
            } else {
                binding.explPlayers.expand();
                binding.tvSPlayers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow_filled, 0);
            }
        });
    }

    private void setTabWiseList(int tabPosition, LinkedList<PlayerDataType> list) {
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

        if (presenter.checkToolTip() == 1) {
//        if (!isTooltipShowed)
            showTooltip();
        }
    }

    private void showTooltip() {
//        isTooltipShowed=true;
        new SimpleTooltip.Builder(this)
                .anchorView(binding.View)
                .text(getString(R.string.text_swipetoadd))
                .gravity(Gravity.TOP)
                .arrowColor(getResources().getColor(R.color.colorWhite))
                .animated(true)
                .transparentOverlay(false)
                .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                .overlayOffset(0)
                .contentView(R.layout.custom_tooltip, R.id.montserratLight)
                .build()
                .show();
    }

    private void setBowlerPlayerList(LinkedList<PlayerDataType> plist) {
        playerType = Constants.TAG_PLAYERTYPE_BLOWER;
        listBySpecility = new LinkedList<>();
        for (PlayerDataType playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_BLOWER)) {
                if (!selectedPlayerID.contains(playerDataType.getPlayerId())) {
                    if (teamName.size() > 0) {
                        if (teamName.contains(playerDataType.getParticipationTeamName()))
                            listBySpecility.add(playerDataType);
                    } else
                        listBySpecility.add(playerDataType);
                }
            }
        }
        refreshAdapter(listBySpecility);
    }

    private void setAllRounderPlayerList(LinkedList<PlayerDataType> plist) {
        playerType = Constants.TAG_PLAYERTYPE_ALLROUNDER;
        listBySpecility = new LinkedList<>();
        for (PlayerDataType playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_ALLROUNDER)) {
                if (!selectedPlayerID.contains(playerDataType.getPlayerId())) {
                    if (teamName.size() > 0) {
                        if (teamName.contains(playerDataType.getParticipationTeamName()))
                            listBySpecility.add(playerDataType);
                    } else
                        listBySpecility.add(playerDataType);
                }
            }
        }
        refreshAdapter(listBySpecility);
    }

    private void setWicketKeeperPlayerList(LinkedList<PlayerDataType> plist) {
        playerType = Constants.TAG_PLAYERTYPE_WICKETKEEPER;
        listBySpecility = new LinkedList<>();
        for (PlayerDataType playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_WICKETKEEPER)) {
                if (!selectedPlayerID.contains(playerDataType.getPlayerId())) {
                    if (teamName.size() > 0) {
                        if (teamName.contains(playerDataType.getParticipationTeamName()))
                            listBySpecility.add(playerDataType);
                    } else
                        listBySpecility.add(playerDataType);
                }
            }
        }
        refreshAdapter(listBySpecility);
    }

    private void setAllPlayerList(LinkedList<PlayerDataType> list) {
        playerType = Constants.TAG_PLAYERTYPE_ALL;

        if (teamName.size() > 0) {
            listBySpecility = new LinkedList<>();
            for (PlayerDataType playerDataType : list) {
                if (teamName.contains(playerDataType.getParticipationTeamName()))
                    if (!selectedPlayerID.contains(playerDataType.getPlayerId()))
                        listBySpecility.add(playerDataType);
            }
        } else
            listBySpecility = list;

        setData(listBySpecility);
    }

    private void setBatsmanPlayerList(LinkedList<PlayerDataType> plist) {
        playerType = Constants.TAG_PLAYERTYPE_BATSMAN;
        listBySpecility = new LinkedList<>();
        for (PlayerDataType playerDataType : plist) {
            if (playerDataType.getPlayerSpeciality().equals(Constants.TAG_PLAYERTYPE_BATSMAN)) {
                if (!selectedPlayerID.contains(playerDataType.getPlayerId())) {
                    if (teamName.size() > 0) {
                        if (teamName.contains(playerDataType.getParticipationTeamName()))
                            listBySpecility.add(playerDataType);
                    } else
                        listBySpecility.add(playerDataType);
                }
            }
        }
        refreshAdapter(listBySpecility);
    }

    private void refreshAdapter(LinkedList<PlayerDataType> list) {
        pAdapter = new PlayerListAdapter(this, list, this, playerCount, remainingBudget,
                MaxWicketKeeper, MaxBowler, MaxBatsman, MaxAllrounder, wicketKeeperCount, bolwerCount, batsmanCount, allrunderCount, new PlayerListAdapter.ItemClickListener() {
            @Override
            public void onPlayerClick(PlayerDataType playerId) {
                getPlayerDetail(tournamentID, playerId);
            }
        });
        binding.inRecyclerview.rvList.setAdapter(pAdapter);
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_playerlist));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.rvMatch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        uAdapter = new UpcomingMatchListAdapter(matchList);
        binding.rvMatch.setAdapter(uAdapter);

        binding.inRecyclerview.rvList.setHasFixedSize(true);

        binding.inRecyclerview.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.inRecyclerview.rvList.addItemDecoration(new ItemDecoration(this));

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.tournamentPlayers(tournamentID);
                break;

            case Constants.APICALL_2:
                presenter.teamFilter(tournamentID);
                break;

            case Constants.APICALL_3:
                presenter.TeamRule(tournamentID);
                break;

            case Constants.APICALL_4:
                getMatches();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_menu, menu);

        RelativeLayout rootView = (RelativeLayout) menu.findItem(R.id.action_apply).getActionView();
        MontserratRegular customTextView = rootView.findViewById(R.id.tvActionApply);
        customTextView.setText(getString(R.string.text_continue));

        customTextView.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            Gson gson = new Gson();
            if (spAdapter.getList().size() > 0) {
                if (spAdapter.getList().size() == 11) {
                    if (wicketKeeperCount >= MinWicketKeeper && wicketKeeperCount <= MaxWicketKeeper) {
                        if (allrunderCount >= MinAllrounder && allrunderCount <= MaxAllrounder) {
                            if (batsmanCount >= MinBatsman && batsmanCount <= MaxBatsman) {
                                if (bolwerCount >= MinBowler && bolwerCount <= MaxBowler) {
                                    int valueST = spAdapter.getSameTeamList();
                                    if (valueST > MaxSameTeamPlayer) {
                                        String text = getString(R.string.text_maxsameplayer) + " " + MaxSameTeamPlayer;
                                        CustomToast.getInstance(this).showSmallCustomToast(text);
                                    } else {
                                        int valueOS = spAdapter.getOverSeasList();
                                        if (valueOS > MaxOverseasPlayer) {
                                            String text = getString(R.string.text_maxoverseasplayer) + " " + MaxOverseasPlayer;
                                            CustomToast.getInstance(this).showSmallCustomToast(text);
                                        } else {
                                            String element = gson.toJson(
                                                    spAdapter.getList(),
                                                    new TypeToken<LinkedList<PlayerDataType>>() {
                                                    }.getType());
                                            try {
                                                JSONArray list = new JSONArray(element);
                                                returnIntent.putExtra(Constants.TAG_PLAYERLIST, list.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            returnIntent.putExtra(Constants.TAG_SubsLeftAtSnapShot, transferLeft);

                                            setResult(Constants.REQ_CODE_PLAYER, returnIntent);
                                            finish();
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
        return true;
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
                if (matchList.size() > 0) {
                    binding.pbMatches.setVisibility(View.GONE);
                    binding.tvNoMatch.setVisibility(View.GONE);
                    uAdapter.AddData(matchList);
                } else {
                    Log.d("sajhshdhajkhjdkhjkas","ss ");
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

    @Override
    public void getPlayerlist(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                pList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<PlayerDataType>>() {
                                }.getType())
                );

                if (pList.size() > 0) {
                    listBySpecility = pList;
                    for (PlayerDataType data : pList) {
                        if (playerPosition != null && playerPosition.size() > 0 && playerPosition.containsKey(data.getPlayerId()))
                            selectedPlayerList.get(playerPosition.get(data.getPlayerId())).setTotalPoints(data.getTotalPoints());
                    }
                    setTabWiseList(tabPosition, listBySpecility);
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

    @Override
    public void getFilterTeamList(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                tList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<TeamFilterModel>>() {
                                }.getType())
                );

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeamRuleDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {

                JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                System.out.println("Team Rules" + jsonObject1.toString());

                MaxWicketKeeper = jsonObject1.getInt("MaxWicketKeeper");
                MinWicketKeeper = jsonObject1.getInt("WicketKeeper");
                MaxBatsman = jsonObject1.getInt("MaxBatsman");
                MinBatsman = jsonObject1.getInt("MinBatsman");
                MaxBowler = jsonObject1.getInt("MaxBowler");
                MinBowler = jsonObject1.getInt("MinBowler");
                MaxAllrounder = jsonObject1.getInt("MaxAllrounder");
                MinAllrounder = jsonObject1.getInt("MinAllrounder");

                String btxt = MinBatsman + "-" + MaxBatsman;
                binding.tvBatsmanCount.setText(btxt);

                String wktxt = MinWicketKeeper + "-" + MaxWicketKeeper;
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

                MaxSameTeamPlayer = jsonObject1.getInt("MaxSameTeamPlayer");
                MaxOverseasPlayer = jsonObject1.getInt("MaxOverseasPlayer");
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPlayerDetails(JSONObject jsonObject) {
        Log.e("success",""+jsonObject);
        PlayerDetailDialog playerDetailDialog = new PlayerDetailDialog(PlayerListActivity.this, jsonObject);
    }

    private void setData(LinkedList<PlayerDataType> list) {
        for (Iterator<PlayerDataType> iterator = list.iterator(); iterator.hasNext(); ) {
            PlayerDataType value = iterator.next();
            if (selectedPlayerID.contains(value.getPlayerId()))
                iterator.remove();
        }

        binding.inRecyclerview.rvList.setVisibility(View.VISIBLE);
        binding.inRecyclerview.pBar.setVisibility(View.GONE);
        binding.inRecyclerview.tvNoDataFound.setVisibility(View.GONE);
        pAdapter = new PlayerListAdapter(this, list, this, playerCount, remainingBudget,
                MaxWicketKeeper, MaxBowler, MaxBatsman, MaxAllrounder, wicketKeeperCount, bolwerCount, batsmanCount, allrunderCount, new PlayerListAdapter.ItemClickListener() {
            @Override
            public void onPlayerClick(PlayerDataType playerId) {
//                presenter.getPlayerDetail(tournamentID, playerId);
                getPlayerDetail(tournamentID,playerId);
            }
        });
        binding.inRecyclerview.rvList.setAdapter(pAdapter);
    }

    @Override
    public void OnRemovePlayer(PlayerDataType data) {
        if (playerCount > 0) {
            pList.add(data);
            setTabWiseList(tabPosition, pList);
            selectedPlayerID.remove(selectedPlayerID.indexOf(data.getPlayerId()));
            playerCount--;
            remainingBudget = remainingBudget + data.getPlayerValue();
            pAdapter.refreshBudget(remainingBudget);
            setBudget();

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
            pAdapter.refreshCounts(batsmanCount, bolwerCount, allrunderCount, wicketKeeperCount);
        }
        setSelectedText();
    }

    private void setTransferLeft(int count) {
        transferLeft = count;
        if (isTournamentInProgress) {
            binding.imgInfinity.setVisibility(View.GONE);
            binding.tvTransferAmt.setVisibility(View.VISIBLE);
            if (count < 0)
                count = 0;

            binding.tvTransferAmt.setText(String.valueOf(count));
        } else {
            binding.imgInfinity.setVisibility(View.VISIBLE);
            binding.tvTransferAmt.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnSelectPlayer(PlayerDataType playerDataType) {

        if (playerCount < 11) {
            selectedPlayerID.add(playerDataType.getPlayerId());

            int transferUsed = 0;

            if (oldPlayerID.size() > 0) {
                for (int value : selectedPlayerID) {
                    if (!oldPlayerID.contains(value))
                        transferUsed++;
                }
            }

            transferLeft = SubsLeftAtSnapShot - transferUsed;

            if (isTournamentInProgress) {
                if (transferLeft >= 0)
                    setPlayerData(playerDataType);
                else {
                    selectedPlayerID.remove(selectedPlayerID.indexOf(playerDataType.getPlayerId()));
                    CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_youdonthavetransfer));
                }
            } else
                setPlayerData(playerDataType);
        }
    }

    private void setPlayerData(PlayerDataType playerDataType) {

        pAdapter.removePlayer(playerDataType);
        pList.remove(playerDataType);
        setTabWiseList(tabPosition, pList);
        remainingBudget = remainingBudget - playerDataType.getPlayerValue();
        setTransferLeft(transferLeft);
        playerCount++;

        pAdapter.refreshBudget(remainingBudget);
        setBudget();

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

        pAdapter.refreshCounts(batsmanCount, bolwerCount, allrunderCount, wicketKeeperCount);

        spAdapter.addNewPlayer(playerDataType);
        setSelectedText();
    }

    private void setSelectedText() {
        binding.tvAllCount.setText(String.valueOf(playerCount));
    }

    @Override
    public void returnteamname(LinkedList<String> teamname) {
        teamName = new LinkedList<>();
        teamName.addAll(teamname);
        if (teamName.size() > 0) {
            setTeamWiseList(teamName);
            binding.tvFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_filter_green, 0);
        } else {
            setTabWiseList(tabPosition, pList);
            binding.tvFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_filter, 0);
        }
    }

    @Override
    public void clearTeamName() {
        teamName = new LinkedList<>();
    }

    private void setTeamWiseList(LinkedList<String> teamname) {
        LinkedList<PlayerDataType> list = new LinkedList<>();
        for (PlayerDataType playerDataType : pList) {
            if (teamname.contains(playerDataType.getParticipationTeamName()))
                if (!selectedPlayerID.contains(playerDataType.getPlayerId()))
                    list.add(playerDataType);
        }
        setTabWiseList(tabPosition, list);
    }

    private void setBatsmanCount() {
        MontserratMedium bc = Objects.requireNonNull(Objects.requireNonNull(binding.tbSort.getTabAt(1)).getCustomView()).findViewById(R.id.tvTabBatsmanCount);
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

//        if (wicketKeeperCount==1)
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
    public void OnSelectPlayerNew(PlayerDataType playerDataType) {

        getPlayerDetail(tournamentID, playerDataType);
    }
}
