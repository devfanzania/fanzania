package com.yorker.fanzania.dialog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.text.Html;
import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.WalletInfoDialogBinding;

public class WalletInfoDialog extends android.app.AlertDialog.Builder {
    public WalletInfoDialog(Context context) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);

        WalletInfoDialogBinding binding = DataBindingUtil.inflate(li, R.layout.wallet_info_dialog, null, false);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.tvInfoText.setText(Html.fromHtml(getContext().getString(R.string.text_wallet_info)));
    }
}
