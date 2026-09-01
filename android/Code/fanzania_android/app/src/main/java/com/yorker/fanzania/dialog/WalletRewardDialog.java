package com.yorker.fanzania.dialog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.WalletInfoDialogBinding;
import com.yorker.fanzania.databinding.WalletRewardsDialogBinding;
import com.yorker.fanzania.views.screens.tournament.wallet.RewardsListAdapter;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletRewardsResponse;

import java.util.List;

public class WalletRewardDialog extends android.app.AlertDialog.Builder {
    public WalletRewardDialog(Context context, List<WalletRewardsResponse> rList) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);

        WalletRewardsDialogBinding binding = DataBindingUtil.inflate(li, R.layout.wallet_rewards_dialog, null, false);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.rcv.setLayoutManager(new LinearLayoutManager(context));
        binding.rcv.setAdapter(new RewardsListAdapter(rList));

        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
}
