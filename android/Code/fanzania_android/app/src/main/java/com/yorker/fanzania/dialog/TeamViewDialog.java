package com.yorker.fanzania.dialog;

import android.annotation.SuppressLint;
import android.content.Context;

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
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratMedium;
import com.yorker.fanzania.databinding.TeamviewDialogBinding;
import com.yorker.fanzania.views.screens.matchcontest.playerselection.PlayerListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("ValidFragment")
public class TeamViewDialog extends BottomSheetDialogFragment {

    private TeamviewDialogBinding binding;
    private ArrayList<Integer> fieldList;
    private List<PlayerListModel> playersList;
    private Context mContext;

    public TeamViewDialog(Context context, List<PlayerListModel> selectedPlayerList) {
        this.mContext=context;
        this.playersList=selectedPlayerList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.teamview_dialog, container, false);
        setPlayerList(playersList);
        binding.imgClose.setOnClickListener(view -> dismiss());
        return binding.getRoot();
    }

    private void setPlayerList(List<PlayerListModel> playerList) {
        Collections.sort(playerList, (lhs, rhs) -> lhs.getPlayerSpeciality().compareTo(rhs.getPlayerSpeciality()));

        fieldList = new ArrayList<>();

        for (PlayerListModel players : playerList) {
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
    }

    private void addPlayerData(PlayerListModel players) {
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
    private void setPlayerData(PlayerListModel players, ImageView imgPlayer,
                               MontserratMedium tvPlayerName, ImageView tvVC, MontserratMedium tvPlayerPoint,
                               ImageView imgPlayerDel) {

        tvPlayerName.setText(players.getPlayerShortName());

        if (players.isTeamCapt()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_c);
        } else if (players.isTeamVCapt()) {
            tvVC.setVisibility(View.VISIBLE);
            tvVC.setImageResource(R.drawable.ic_vc);
        } else
            tvVC.setVisibility(View.GONE);

        tvPlayerPoint.setText(new StringBuilder().append(players.getPlayerValue())
                .append("K"));

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
