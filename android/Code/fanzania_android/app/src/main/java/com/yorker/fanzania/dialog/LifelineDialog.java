package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.ChangeTeamNameDialogBinding;
import com.yorker.fanzania.databinding.LifelineDialogBinding;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.views.model.PowerPlayLifeline;
import com.yorker.fanzania.views.model.PowerPlayLifelinePost;

import java.util.Objects;

import javax.inject.Inject;

public class LifelineDialog extends AlertDialog.Builder {
    private AlertDialog alertDialog;
    @Inject
    SharedPrefManager sharedPrefManager;
    String BankVerified = null;

    UpdateLifeLineCallback updateLifeLineCallback;
    public interface UpdateLifeLineCallback{
        public void onConfirmClick(PowerPlayLifelinePost powerPlayLifelinePost);
    }

    public LifelineDialog(Context context, PowerPlayLifeline powerPlayLifeline, UpdateLifeLineCallback updateLifeLineCallback) {
        super(context);

        PowerPlayLifelinePost powerPlayLifelinePost = new PowerPlayLifelinePost();
        powerPlayLifelinePost.setAutoPilotPoints(powerPlayLifeline.getAutoPilotPoints());
        powerPlayLifelinePost.setNitroPoints(powerPlayLifeline.getNitroPoints());
        powerPlayLifelinePost.setPainKillerPoints(powerPlayLifeline.getPainKillerPoints());

        powerPlayLifelinePost.setNitroUserTeamMatchPointId(powerPlayLifeline.getNitroUserTeamMatchPointId());
        powerPlayLifelinePost.setPainKillerUserTeamMatchPointId(powerPlayLifeline.getPainKillerUserTeamMatchPointId());
        powerPlayLifelinePost.setAutoPilotUserTeamMatchPointId(powerPlayLifeline.getAutoPilotUserTeamMatchPointId());

        this.updateLifeLineCallback = updateLifeLineCallback;
        LayoutInflater li = LayoutInflater.from(context);
        LifelineDialogBinding binding = DataBindingUtil.inflate(li, R.layout.lifeline_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());
        
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        final int[] selectedCount = {0};
        if (powerPlayLifeline.isNitroEnable()){
            if (powerPlayLifeline.isNitroUsed()){
                binding.nitroBtn.setText("Used");
                binding.nitroBtn.setEnabled(false);
                binding.nitroBtn.setClickable(false);
            }else{
                binding.nitroBtn.setText("Select");
                binding.nitroBtn.setEnabled(true);
                binding.nitroBtn.setClickable(true);
                binding.nitroText.setText("Lifeline available. You scored "+powerPlayLifeline.getNitroPoints()+" bonus points from your first use.");

                binding.nitroBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (binding.nitroBtn.getText().toString().equalsIgnoreCase("selected")){
                            binding.nitroBtn.setText("Select");
                            binding.nitroBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));

                                                        powerPlayLifelinePost.setNitroSelect(false);
                            selectedCount[0] = selectedCount[0] -1;
                            binding.nitroText.setText("Lifeline available. You scored "+powerPlayLifeline.getNitroPoints()+" bonus points from your first use.");
                            if (selectedCount[0] == 0){
                                binding.llImportant.setVisibility(View.GONE);
                                binding.notSelectedText.setVisibility(View.VISIBLE);

                                binding.saveBtn.setClickable(false);
                                binding.saveBtn.setEnabled(false);
                                binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));

                            }

                        }else{
                            binding.saveBtn.setClickable(true);
                            binding.saveBtn.setEnabled(true);
                            binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));
                            selectedCount[0] = selectedCount[0] + 1;
                            binding.nitroBtn.setText("Selected");
                            binding.nitroBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_green_new));
                            powerPlayLifelinePost.setNitroSelect(true);

                            binding.llImportant.setVisibility(View.VISIBLE);
                            binding.notSelectedText.setVisibility(View.GONE);
                        }

                    }
                });
            }
        }else{
            if (powerPlayLifeline.isNitroUsed()){
                binding.nitroText.setText(getContext().getResources().getString(R.string.lifeline_option_already_used));
                binding.nitroBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));
                binding.nitroBtn.setText("Used");
                binding.nitroBtn.setEnabled(false);
                binding.nitroBtn.setClickable(false);
            }else{
                binding.nitroText.setText(getContext().getResources().getString(R.string.lifeline_not_available));
                binding.nitroBtn.setText("Unavailable");
                binding.nitroBtn.setEnabled(false);
                binding.nitroBtn.setClickable(false);
            }
        }

        if (powerPlayLifeline.isPainKillerEnable()){
            if (powerPlayLifeline.isPainKillerUsed()){
                binding.painKillerBtn.setText("Used");
                binding.painKillerBtn.setEnabled(false);
                binding.painKillerBtn.setClickable(false);
            }else{
                binding.painKillerBtn.setText("Select");
                binding.painKillerBtn.setEnabled(true);
                binding.painKillerBtn.setClickable(true);
                binding.painKillerText.setText("Lifeline available. You scored "+powerPlayLifeline.getPainKillerPoints()+" bonus points from your first use.");
                binding.painKillerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.painKillerBtn.getText().toString().equalsIgnoreCase("selected")){
                            binding.painKillerBtn.setText("Select");
                            binding.painKillerBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));

                            powerPlayLifelinePost.setPainKillerSelect(false);
                            selectedCount[0] = selectedCount[0] - 1;
                            binding.painKillerText.setText("Lifeline available. You scored "+powerPlayLifeline.getPainKillerPoints()+" bonus points from your first use.");
                            if (selectedCount[0] == 0){
                                binding.llImportant.setVisibility(View.GONE);
                                binding.notSelectedText.setVisibility(View.VISIBLE);
                                binding.saveBtn.setClickable(false);
                                binding.saveBtn.setEnabled(false);
                                binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));
                            }

                        }else{
                            selectedCount[0] = selectedCount[0] + 1;
                            binding.saveBtn.setClickable(true);
                            binding.saveBtn.setEnabled(true);
                            binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));

                            binding.painKillerBtn.setText("Selected");
                            binding.painKillerBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_green_new));
                            powerPlayLifelinePost.setPainKillerSelect(true);

                            binding.llImportant.setVisibility(View.VISIBLE);
                            binding.notSelectedText.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }else{
            if (powerPlayLifeline.isPainKillerUsed()){
                binding.painKillerText.setText(getContext().getResources().getString(R.string.lifeline_option_already_used));
                binding.painKillerBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));
                binding.painKillerBtn.setText("Used");
                binding.painKillerBtn.setEnabled(false);
                binding.painKillerBtn.setClickable(false);
            }else{
                binding.painKillerText.setText(getContext().getResources().getString(R.string.lifeline_not_available));
                binding.painKillerBtn.setText("Unavailable");
                binding.painKillerText.setText(getContext().getResources().getString(R.string.lifeline_not_available));
                binding.painKillerBtn.setEnabled(false);
                binding.painKillerBtn.setClickable(false);
            }
        }

        if (powerPlayLifeline.isAutoPilotEnable()){
            if (powerPlayLifeline.isAutoPilotUsed()){
                binding.autoPilotBtn.setText("Used");
                binding.autoPilotBtn.setEnabled(false);
                binding.autoPilotBtn.setClickable(false);
            }else{
                binding.autoPilotText.setText("Lifeline available. You scored "+powerPlayLifeline.getAutoPilotPoints()+" bonus points from your first use.");
                binding.autoPilotBtn.setText("Select");
                binding.autoPilotBtn.setEnabled(true);
                binding.autoPilotBtn.setClickable(true);
                binding.autoPilotBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.autoPilotBtn.getText().toString().equalsIgnoreCase("selected")){
                            binding.autoPilotBtn.setText("Select");
                            binding.autoPilotBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));

                            binding.autoPilotText.setText("Lifeline available. You scored "+powerPlayLifeline.getAutoPilotPoints()+" bonus points from your first use.");
                            powerPlayLifelinePost.setAutoPilotSelect(false);
                            selectedCount[0] = selectedCount[0] - 1;

                            if (selectedCount[0] == 0){
                                binding.llImportant.setVisibility(View.GONE);
                                binding.notSelectedText.setVisibility(View.VISIBLE);
                                binding.saveBtn.setClickable(false);
                                binding.saveBtn.setEnabled(false);
                                binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));

                            }

                        }else{
                            selectedCount[0] = selectedCount[0] + 1;
                            binding.saveBtn.setClickable(true);
                            binding.saveBtn.setEnabled(true);
                            binding.saveBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_gradient));

                            binding.autoPilotBtn.setText("Selected");
                            binding.autoPilotBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_green_new));
                            powerPlayLifelinePost.setAutoPilotSelect(true);

                            binding.llImportant.setVisibility(View.VISIBLE);
                            binding.notSelectedText.setVisibility(View.GONE);

                        }
                    }
                });
            }
        }else{
            if (powerPlayLifeline.isAutoPilotUsed()){
                binding.autoPilotText.setText(getContext().getResources().getString(R.string.lifeline_option_already_used));
                binding.autoPilotBtn.setText("Used");
                binding.autoPilotBtn.setEnabled(false);
                binding.autoPilotBtn.setClickable(false);
                binding.autoPilotBtn.setBackground(getContext().getDrawable(R.drawable.round_corner_grey_live));
            }else{
                binding.autoPilotBtn.setText("Unavailable");
                binding.autoPilotText.setText(getContext().getResources().getString(R.string.lifeline_not_available));
                binding.autoPilotBtn.setEnabled(false);
                binding.autoPilotBtn.setClickable(false);
            }
        }

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLifeLineCallback.onConfirmClick(powerPlayLifelinePost);
                alertDialog.dismiss();
            }
        });
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
