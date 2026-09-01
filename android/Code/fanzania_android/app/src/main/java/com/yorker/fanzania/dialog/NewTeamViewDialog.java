package com.yorker.fanzania.dialog;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.databinding.NewTeamviewDialogBinding;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class NewTeamViewDialog extends BottomSheetDialogFragment {

    private NewTeamviewDialogBinding binding;
    private ArrayList<Integer> fieldList;
    private Context mContext;
    private int tStatus;
    private String tournamentID;
    private String customerId;
    private String userTeamId;
    private String userTeamName;
    private int tabPosition=0;

    @SuppressLint("ValidFragment")
    public NewTeamViewDialog(Context context, String tournamentID, String userTeamId, String customerId, int tStatus, String userTeamName) {
        this.mContext = context;
        this.tStatus = tStatus;
        this.tournamentID = tournamentID;
        this.customerId = customerId;
        this.userTeamId = userTeamId;
        this.userTeamName = userTeamName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.new_teamview_dialog, container, false);

        binding.tvTeamName.setText(userTeamName);

        binding.switchLayout.setVisibility(View.VISIBLE);
        setupTabIcons();
        getUserTeamInfo(tournamentID, userTeamId, customerId);
        // get the views and attach the listener

        binding.imgClose.setOnClickListener(view -> dismiss());

        binding.tbTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.imgTransfer.setVisibility(View.VISIBLE);
                    binding.switchLayout.setVisibility(View.VISIBLE);
                    getUserTeamInfo(tournamentID, userTeamId, customerId);
                    binding.tvLeftBottomTxt.setText(getString(R.string.text_transfer));
                } else {
                    binding.imgTransfer.setVisibility(View.GONE);
                    binding.switchLayout.setVisibility(View.INVISIBLE);
                    binding.imgInfinity.setVisibility(View.GONE);
                    binding.tvMyRank.setVisibility(View.VISIBLE);
                    LastMatchDetails(tournamentID, userTeamId, customerId);
                }
                tabPosition=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return binding.getRoot();

    }

    private void setupTabIcons() {

        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_currentteam)));
        binding.tbTabs.addTab(binding.tbTabs.newTab().setText(getString(R.string.text_lastmatchpoint)));
        binding.tbTabs.getTabAt(0).select();
    }

    public void getUserTeamInfo(String tournamentID, String userTeamID, String userID) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_USERTEAMID, userTeamID);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.UserTeamPlayersWithStealthMode(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                userID,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        LinkedList<PlayerDataType> playerList = new LinkedList<>(
                                new Gson().fromJson(
                                        jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                        , new TypeToken<List<PlayerDataType>>() {
                                        }.getType())
                        );
                        if (playerList.size() > 0) {
                            PlayerDataType obj = playerList.get(0);

                            String TotalPoints = String.valueOf(obj.getSubsLeft());
                            switch (tStatus) {
                                case 0:

                                case 2:
                                    binding.tvMyRank.setVisibility(View.GONE);
                                    binding.imgInfinity.setVisibility(View.VISIBLE);
//                                    binding.tvMyRank.setText(TotalPoints.length() > 0 ? TotalPoints : "-");
                                    break;

                                case 1:
                                    binding.tvMyRank.setVisibility(View.VISIBLE);
                                    binding.imgInfinity.setVisibility(View.GONE);
                                    binding.tvMyRank.setText(TotalPoints.length() > 0 ? TotalPoints : "-");
                                    break;
                            }

                            if (obj.isAutoPilotUsed()) {
                                binding.tvNitro.setText(mContext.getString(R.string.text_autocaptain));
                                binding.imgPowerPlay.setImageResource(R.drawable.ic_new_autocaptain);
                            } else if (obj.isPainKillerUsed()) {
                                binding.tvNitro.setText(mContext.getString(R.string.text_painkiller));
                                binding.imgPowerPlay.setImageResource(R.drawable.ic_new_painkiller);
                            } else if (obj.isNitroUsed()) {
                                binding.tvNitro.setText(mContext.getString(R.string.text_nitros));
                                binding.imgPowerPlay.setImageResource(R.drawable.ic_new_nitro);
                            } else {
                                binding.tvNitro.setText("-");
                                binding.imgPowerPlay.setImageResource(android.R.color.transparent);
                            }

                            if (obj.getWinnerPrediction().length() != 0){
                                binding.preditOne.setText(obj.getWinnerPrediction());
                            }else{
                                binding.preditOne.setText("-");
                            }

//                            if (playerList.get(0).getWinnerPrediction().length() > 0){
//                                if (playerList.get(0).isWinnerPredictionStatus()){
//                                    binding.imgTick.setVisibility(View.VISIBLE);
//                                    binding.imgTick.setBackgroundResource(R.drawable.ic_tick);
//                                }else{
//                                    binding.imgTick.setVisibility(View.VISIBLE);
//                                    binding.imgTick.setBackgroundResource(R.drawable.ic_clear_red);
//                                }
//                            }else
//                                binding.imgTick.setVisibility(View.GONE);

                            if (obj.getNitroLeft() < 1) {
                                binding.scNitro.setChecked(true);
                                binding.scNitro.setCheckMarkDrawable(R.drawable.ic_check_grey);
                            }else  if (obj.isNitroUsed()) {
                                binding.scNitro.setChecked(true);
                                binding.scNitro.setCheckMarkDrawable(R.drawable.ic_check_black);
                            } else
                                binding.scNitro.setCheckMarkDrawable(null);

                            if (obj.getPainKillerLeft() < 1) {
                                binding.scPainKiller.setChecked(true);
                                binding.scPainKiller.setCheckMarkDrawable(R.drawable.ic_check_grey);
                            }else  if (obj.isPainKillerUsed()) {
                                binding.scPainKiller.setChecked(true);
                                binding.scPainKiller.setCheckMarkDrawable(R.drawable.ic_check_black);
                            }
                            else
                                binding.scPainKiller.setCheckMarkDrawable(null);

                            if (obj.getAutoPilotLeft() < 1) {
                                binding.scAutoCaptain.setChecked(true);
                                binding.scAutoCaptain.setCheckMarkDrawable(R.drawable.ic_check_grey);
                            }else  if (obj.isAutoPilotUsed()) {
                                binding.scAutoCaptain.setChecked(true);
                                binding.scAutoCaptain.setCheckMarkDrawable(R.drawable.ic_check_black);
                            }
                            else
                                binding.scAutoCaptain.setCheckMarkDrawable(null);

                            setPlayerData(playerList);
                        } else {
                            binding.pbField.setVisibility(View.GONE);
                            binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                            binding.clTeamDetails.setVisibility(View.GONE);
                            binding.llLayout2.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    //--------------- Get match details -----------//
    public void LastMatchDetails(String tournamentID, String userTeamId, String userID) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_MATCHID, "0");
        map.put(Constants.TAG_USERTEAMID, userTeamId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        Call<JsonObject> call = retrofitAipService.UserMatchDetailsWithPlayers(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                userID,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                            LinkedList<PlayerDataType> playerList = new LinkedList<>(
                                    new Gson().fromJson(
                                            jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                            , new TypeToken<List<PlayerDataType>>() {
                                            }.getType())
                            );

                            if (playerList.size() > 0) {

                                PlayerDataType obj = playerList.get(0);

                                binding.tvLeftBottomTxt.setText(obj.getLastMatchTeams());
                                binding.tvMyRank.setText(String.valueOf(obj.getMatchTotalPoints()));
                                String TotalPoints = String.valueOf(obj.getMatchTotalPoints());
                                setPoints(TotalPoints);

                                if (obj.isAutoPilotUsed()) {
                                    binding.tvNitro.setText(mContext.getString(R.string.text_autocaptain));
                                    binding.imgPowerPlay.setImageResource(R.drawable.ic_new_autocaptain);
                                } else if (obj.isPainKillerUsed()) {
                                    binding.tvNitro.setText(mContext.getString(R.string.text_painkiller));
                                    binding.imgPowerPlay.setImageResource(R.drawable.ic_new_painkiller);
                                } else if (obj.isNitroUsed()) {
                                    binding.tvNitro.setText(mContext.getString(R.string.text_nitros));
                                    binding.imgPowerPlay.setImageResource(R.drawable.ic_new_nitro);
                                } else {
                                    binding.tvNitro.setText("-");
                                    binding.imgPowerPlay.setImageResource(android.R.color.transparent);
                                }

                                binding.rrMain.removeView(binding.clTeamDetails);
                                binding.rrMain.addView(binding.clTeamDetails);
                                setPlayerData(playerList);
                            } else {
                                binding.pbField.setVisibility(View.GONE);
                                binding.clTeamDetailsBlank.setVisibility(View.VISIBLE);
                                binding.clTeamDetails.setVisibility(View.GONE);
                                binding.llLayout2.setVisibility(View.GONE);
                            }

                        } else
                            CustomToast.getInstance(getActivity()).showSmallCustomToast(jsonObject.getString("statusMessage"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("error1 " + call.toString());
            }
        });
    }

    private void setPoints(String totalPoints) {

        binding.tvMyRank.setText(totalPoints);
    }

    private void setPlayerData(LinkedList<PlayerDataType> playerList) {
//        Collections.sort(playerList, (lhs, rhs) -> lhs.getPlayerSpeciality().compareTo(rhs.getPlayerSpeciality()));

        fieldList = new ArrayList<>();

        for (PlayerDataType players : playerList) {
            switch (players.getPlayerSpeciality()) {
                case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                    if (!fieldList.contains(binding.tvPlayer11Name.getId())) {
                        setPlayerData(players, binding.imgPlayer11, binding.tvPlayer11Name,
                                binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11Delete);
                        fieldList.add(binding.tvPlayer11Name.getId());

                        setDelay();
                    } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
                        setPlayerData(players, binding.imgPlayer10, binding.tvPlayer10Name,
                                binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10Delete);
                        fieldList.add(binding.tvPlayer10Name.getId());

                        setDelay();
                    } else {
                        setPlayerData(players, binding.imgPlayer9, binding.tvPlayer9Name,
                                binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9Delete);
                        fieldList.add(binding.tvPlayer9Name.getId());

                        setDelay();
                    }
                    break;

                case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                    if (!fieldList.contains(binding.tvPlayer1Name.getId())) {
                        setPlayerData(players, binding.imgPlayer1, binding.tvPlayer1Name,
                                binding.tvPlayer1Captain, binding.tvPlayer1Point, binding.imgPlayer1Delete);
                        fieldList.add(binding.tvPlayer1Name.getId());
                        setDelay();
                    } else
                        addPlayerData(players);

//                    else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
//                        setPlayerData(players, binding.imgPlayer2, binding.tvPlayer2Name,
//                                binding.tvPlayer2Captain, binding.tvPlayer2Point, binding.imgPlayer2Delete);
//                        fieldList.add(binding.tvPlayer2Name.getId());
//                    } else {
//                        setPlayerData(players, binding.imgPlayer4, binding.tvPlayer4Name,
//                                binding.tvPlayer4Captain, binding.tvPlayer4Point, binding.imgPlayer4Delete);
//                        fieldList.add(binding.tvPlayer4Name.getId());
//                    }
//                    setDelay();
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
    }

    private void addPlayerData(PlayerDataType players) {
        if (!fieldList.contains(binding.tvPlayer8Name.getId())) {
            setPlayerData(players, binding.imgPlayer8, binding.tvPlayer8Name,
                    binding.tvPlayer8Captain, binding.tvPlayer8Point, binding.imgPlayer8Delete);
            fieldList.add(binding.tvPlayer8Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer7Name.getId())) {
            setPlayerData(players, binding.imgPlayer7, binding.tvPlayer7Name,
                    binding.tvPlayer7Captain, binding.tvPlayer7Point, binding.imgPlayer7Delete);
            fieldList.add(binding.tvPlayer7Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer6Name.getId())) {
            setPlayerData(players, binding.imgPlayer6, binding.tvPlayer6Name,
                    binding.tvPlayer6Captain, binding.tvPlayer6Point, binding.imgPlayer6Delete);
            fieldList.add(binding.tvPlayer6Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer5Name.getId())) {
            setPlayerData(players, binding.imgPlayer5, binding.tvPlayer5Name,
                    binding.tvPlayer5Captain, binding.tvPlayer5Point, binding.imgPlayer5Delete);
            fieldList.add(binding.tvPlayer5Name.getId());
            setDelay();
        }else if (!fieldList.contains(binding.tvPlayer4Name.getId())) {
            setPlayerData(players, binding.imgPlayer4, binding.tvPlayer4Name,
                    binding.tvPlayer4Captain, binding.tvPlayer4Point, binding.imgPlayer4Delete);
            fieldList.add(binding.tvPlayer4Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer3Name.getId())) {
            setPlayerData(players, binding.imgPlayer3, binding.tvPlayer3Name,
                    binding.tvPlayer3Captain, binding.tvPlayer3Point, binding.imgPlayer3Delete);
            fieldList.add(binding.tvPlayer3Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer2Name.getId())) {
            setPlayerData(players, binding.imgPlayer2, binding.tvPlayer2Name,
                    binding.tvPlayer2Captain, binding.tvPlayer2Point, binding.imgPlayer2Delete);
            fieldList.add(binding.tvPlayer2Name.getId());
            setDelay();
        } else if (!fieldList.contains(binding.tvPlayer10Name.getId())) {
            setPlayerData(players, binding.imgPlayer10, binding.tvPlayer10Name,
                    binding.tvPlayer10Captain, binding.tvPlayer10Point, binding.imgPlayer10Delete);
            fieldList.add(binding.tvPlayer10Name.getId());
            setDelay();
        }else if (!fieldList.contains(binding.tvPlayer9Name.getId()))  {
            setPlayerData(players, binding.imgPlayer9, binding.tvPlayer9Name,
                    binding.tvPlayer9Captain, binding.tvPlayer9Point, binding.imgPlayer9Delete);
            fieldList.add(binding.tvPlayer9Name.getId());
            setDelay();
        }else   {
            setPlayerData(players, binding.imgPlayer11, binding.tvPlayer11Name,
                    binding.tvPlayer11Captain, binding.tvPlayer11Point, binding.imgPlayer11Delete);
            fieldList.add(binding.tvPlayer11Name.getId());
            setDelay();
        }

    }

    private void setDelay() {
        new Handler().postDelayed(() -> {
        }, 150);
    }

    @SuppressLint("CheckResult")
    private void setPlayerData(PlayerDataType players, ImageView imgPlayer,
                               MontserratMedium tvPlayerName, ImageView tvVC, MontserratMedium tvPlayerPoint,
                               ImageView imgPlayerDel) {

        tvPlayerName.setText(players.getPlayerShortName());

        if (players.getTeamCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
        } else if (players.getTeamVCapt() == players.getPlayerId()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
        } else
            tvVC.setVisibility(View.GONE);

        switch (tabPosition){
            case 0:
                tvPlayerPoint.setText("");
                break;

            case 1:
                String txt = String.valueOf(players.getPlayerPoints());
                tvPlayerPoint.setText(txt);
                break;
        }

        if (players.getTeamImage() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.blank_jersey);
            requestOptions.error(R.drawable.blank_jersey);
            String url = Constants.BASE_IMAGE_URL + players.getTeamImage();
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(url).into(imgPlayer);
        } else
            imgPlayer.setImageResource(R.drawable.blank_jersey);

        if (players.getPlayerType().equals("overseas "))
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
        else
            tvPlayerName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));

        switch (players.getPlayerSpeciality()) {
            case Constants.TAG_PLAYERTYPE_BATSMAN:
                imgPlayerDel.setImageResource(R.drawable.ic_new_batsman);
                break;
            case Constants.TAG_PLAYERTYPE_ALLROUNDER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_allrounder);
                break;
            case Constants.TAG_PLAYERTYPE_BLOWER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_bowler);
                break;
            case Constants.TAG_PLAYERTYPE_WICKETKEEPER:
                imgPlayerDel.setImageResource(R.drawable.ic_new_keeper);
                break;
        }
    }
}
