package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.TeamInfoDialogBinding;

import java.util.Objects;

public class TeamInfoDialog extends AlertDialog.Builder {
    private AlertDialog alertDialog;

    public TeamInfoDialog(Context context, int maxWicketKeeper,int minWicketKeeper, int maxAllrounder, int minAllrounder,
                          int maxBatsman, int minBatsman, int maxBowler, int minBowler, int maxSameTeamPlayer, int maxOverseasPlayer) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        TeamInfoDialogBinding binding = DataBindingUtil.inflate(li, R.layout.team_info_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        String text = context.getString(R.string.text_infotext1) + " " + minBatsman + "-" + maxBatsman + " " + context.getString(R.string.text_batsman)
                + " " + minBowler + "-" + maxBowler + " " + context.getString(R.string.text_bowler)
                + " " + minAllrounder + "-" + maxAllrounder + " " + context.getString(R.string.text_allrounder)
                + " " + minWicketKeeper+"-"+maxWicketKeeper + " " + context.getString(R.string.text_textinfo2)
                + " " + maxSameTeamPlayer + " " + context.getString(R.string.text_info3)
                + " " + maxOverseasPlayer + " " + context.getString(R.string.text_info4);

        binding.tvDetails.setText(text);

        String txt=context.getString(R.string.text_nitro)+" = "+context.getString(R.string.text_doubleyourpoints)+"\n"+
                context.getString(R.string.text_autocaptain)+" = "+context.getString(R.string.text_yourhighestscorer)+"\n"+
                context.getString(R.string.text_painkiller)+" = "+context.getString(R.string.text_yougetmatchtop);

        binding.tvPowerplayDetails.setText(txt);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view ->
            alertDialog.dismiss());
    }
}
