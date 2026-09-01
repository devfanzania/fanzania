package com.yorker.fanzania.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.yorker.fanzania.R;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.AlertDialogBinding;
import com.yorker.fanzania.views.screens.tournament.manageteam.model.UpcomingMatchModel;

public class CustomeAlertDialog {

    public static void show(
            Context context,
            String title,
            String catpName,
            String viceCatpName,
            Integer transferUsed,
            ICommonDialog iCommonDialog,
            boolean isTournamentInProgress,
            int isAutoPilotUsed,
            int isNitroUsed,
            int isPainKillerUsed,
            UpcomingMatchModel upcomingMatchModel,
            String predictTeamNew
    ) {

        String na = context.getClass().getName();
        // ✅ Safety checks
        if (!(context instanceof Activity)) return;

        Activity activity = (Activity) context;
        if (activity.isFinishing() || activity.isDestroyed()) return;

        LayoutInflater li = LayoutInflater.from(context);
        AlertDialogBinding binding = DataBindingUtil.inflate(
                li,
                R.layout.alert_dialog,
                null,
                false
        );

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // ---------------- UI SETUP ----------------

        binding.txtTitle.setText(title);
        binding.txtCaptain.setText(catpName);
        binding.txtViceCaptain.setText(viceCatpName);

        String text = null;
        final String[] predictTeam = {null};

        // Powerplay logic
        if (isAutoPilotUsed == 0 && isNitroUsed == 0 && isPainKillerUsed == 0) {
            binding.tvPowerplay.setVisibility(View.GONE);
        } else if (isAutoPilotUsed > 0) {
            text = context.getString(R.string.text_autocaptain) + " " +
                    context.getString(R.string.text_powerplayenabled);
            binding.tvPowerplay.setVisibility(View.VISIBLE);
            binding.tvPowerplay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_new_autocaptain, 0, 0, 0);
        } else if (isNitroUsed > 0) {
            text = context.getString(R.string.text_nitro) + " " +
                    context.getString(R.string.text_powerplayenabled);
            binding.tvPowerplay.setVisibility(View.VISIBLE);
            binding.tvPowerplay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_new_nitro, 0, 0, 0);
        } else if (isPainKillerUsed > 0) {
            text = context.getString(R.string.text_painkiller) + " " +
                    context.getString(R.string.text_powerplayenabled);
            binding.tvPowerplay.setVisibility(View.VISIBLE);
            binding.tvPowerplay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_new_painkiller, 0, 0, 0);
        }

        binding.tvPowerplay.setText(text);

        // ---------------- Match prediction ----------------

        if (upcomingMatchModel != null) {
            binding.firstTeam.setTag(upcomingMatchModel.getTeam1ShortName());
            binding.secondTeam.setTag(upcomingMatchModel.getTeam2ShortName());

            if (predictTeamNew != null) {
                if (predictTeamNew.equalsIgnoreCase(upcomingMatchModel.getTeam1ShortName())) {
                    binding.firstTeam.setChecked(true);
                    predictTeam[0] = upcomingMatchModel.getTeam1ShortName();
                } else if (predictTeamNew.equalsIgnoreCase(upcomingMatchModel.getTeam2ShortName())) {
                    binding.secondTeam.setChecked(true);
                    predictTeam[0] = upcomingMatchModel.getTeam2ShortName();
                }
            }

            binding.firstTeam.setText(
                    upcomingMatchModel.getTeam1ShortName() + " vs " +
                            upcomingMatchModel.getTeam2ShortName()
            );
        }

        binding.firstTeam.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked && compoundButton.getTag() != null) {
                predictTeam[0] = compoundButton.getTag().toString();
            }
        });

        binding.secondTeam.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked && compoundButton.getTag() != null) {
                predictTeam[0] = compoundButton.getTag().toString();
            }
        });

        // ---------------- Transfer logic ----------------

        if (transferUsed == -1) {
            setUnlimitedTransfer(binding.txtContent, context);
        } else {
            if (isTournamentInProgress) {
                setLimitedTransfer(binding.txtContent, context, transferUsed);
            } else {
                setUnlimitedTransfer(binding.txtContent, context);
            }
        }

        // ---------------- Show dialog safely ----------------

        dialog.show();

        // ---------------- Button actions ----------------

        binding.btnYes.setOnClickListener(view -> {
            dialog.dismiss();
            iCommonDialog.ConfirmResponse(true, predictTeam[0]);
        });

        binding.btnNo.setOnClickListener(view -> dialog.dismiss());
    }

    // ---------------- Helper methods ----------------

    private static void setLimitedTransfer(MontserratRegular txtContent, Context context, Integer transferUsed) {
        txtContent.setText(Html.fromHtml(
                "<b>" + context.getString(R.string.text_transferused) + " " + transferUsed + "</b>"
        ));
    }

    private static void setUnlimitedTransfer(MontserratRegular txtContent, Context context) {
        txtContent.setText(Html.fromHtml(
                "<b>" + context.getString(R.string.text_transferunlimited) + "</b>"
        ));
    }

    // ---------------- Interface ----------------

    public interface ICommonDialog {
        void ConfirmResponse(Boolean value, String predictTeam);
    }
}
