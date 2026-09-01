package com.yorker.fanzania.dialog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.AlertDialogBinding;

public class McAlertDialog extends android.app.AlertDialog.Builder {
    public McAlertDialog(Context context, String title, String catpName, String viceCatpName, ICommonDialog iCommonDialog, String Team1ShortName, String Team2ShortName, String predictTeamNew
    ) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);

        AlertDialogBinding binding = DataBindingUtil.inflate(li, R.layout.alert_dialog, null, false);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.txtTitle.setText(title);

//        binding.tvInfo.setText(context.getString(R.string.text_mcconfirminfo));

        binding.txtCaptain.setText(catpName);
        binding.txtViceCaptain.setText(viceCatpName);

        binding.tvPowerplay.setVisibility(View.GONE);

        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        final String[] predictTeam = { null };

        binding.btnYes.setOnClickListener(view -> {
            alertDialog.dismiss();
            iCommonDialog.ConfirmResponse(true, predictTeam[0]);
        });

        if (predictTeamNew !=null){
            if (predictTeamNew.equalsIgnoreCase(Team1ShortName)){
                binding.firstTeam.setChecked(true);
                predictTeam[0] = Team1ShortName;
            }
            else if (predictTeamNew.equalsIgnoreCase(Team2ShortName)){
                binding.secondTeam.setChecked(true);
                predictTeam[0] = Team2ShortName;
            }
        }

        if (Team1ShortName != null){
            binding.firstTeam.setTag(Team1ShortName);
            binding.secondTeam.setTag(Team2ShortName);

            binding.firstTeam.setText(Team1ShortName+ " vs "+Team2ShortName);
        }

        binding.firstTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    predictTeam[0] = compoundButton.getTag().toString();
                }
            }
        });

        binding.secondTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    predictTeam[0] = compoundButton.getTag().toString();
                }
            }
        });

        binding.btnNo.setOnClickListener(view ->alertDialog.dismiss());
    }

    public interface ICommonDialog {
        void ConfirmResponse(Boolean value, String predictTeam);
    }
}
