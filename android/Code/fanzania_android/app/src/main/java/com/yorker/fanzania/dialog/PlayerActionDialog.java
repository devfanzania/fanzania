package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.PlayerActionDialogBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.PlayerDataType;

import java.util.Objects;

public class PlayerActionDialog extends AlertDialog.Builder {
    private int val = 0;

    public PlayerActionDialog(Context context, IPlayerActionDialog iPlayerActionDialog, PlayerDataType data,
                              ImageView layoutID, RelativeLayout layout) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        PlayerActionDialogBinding binding = DataBindingUtil.inflate(li, R.layout.player_action_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.txtTitle.setText(data.getPlayerName());

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.RG.clearCheck();

        if (data.getTeamCapt() == data.getPlayerId())
            binding.rbCaptain.setChecked(true);
        else if (data.getTeamVCapt() == data.getPlayerId())
            binding.rbVSCaptain.setChecked(true);
        else
           binding.RG.clearCheck();

        binding.RG.setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.rbCaptain == checkedId)
                val = 1;

            else if (R.id.rbVSCaptain == checkedId)
                val = 2;

        });

        binding.btnYes.setOnClickListener(view -> {
            if (val==1){
                data.setTeamCapt(data.getPlayerId());
                getJsonData(data,layout);
                alertDialog.dismiss();
                iPlayerActionDialog.PositiveResponse(val, data.getPlayerId(),data.getPlayerShortName(),layoutID);
            }else if (val==2){
                data.setTeamVCapt(data.getPlayerId());
                getJsonData(data,layout);
                alertDialog.dismiss();
                iPlayerActionDialog.PositiveResponse(val, data.getPlayerId(),data.getPlayerShortName(),layoutID);
            }
        });

        binding.btnNo.setOnClickListener(view -> {
            alertDialog.dismiss();
            binding.RG.clearCheck();
        });
    }

    public interface IPlayerActionDialog {
        void PositiveResponse(int val, int playerId, String Name, ImageView layoutID);
    }

    private void getJsonData(PlayerDataType playerDataType, RelativeLayout rrPlayer) {
        Gson gson = new Gson();
        String obj = gson.toJson(playerDataType, PlayerDataType.class);
        rrPlayer.setTag(obj);
    }
}
