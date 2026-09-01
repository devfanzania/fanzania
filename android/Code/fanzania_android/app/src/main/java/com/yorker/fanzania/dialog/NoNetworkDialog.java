package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.NetworkDialogBinding;
import com.yorker.fanzania.helper.network.ConnectionDetector;

import java.util.Objects;

public class NoNetworkDialog extends AlertDialog.Builder {
    private ConnectionDetector cd;

    public NoNetworkDialog(Context context, INetworkDialog idialog, String type) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        NetworkDialogBinding networkDialogBinding = DataBindingUtil.inflate(li, R.layout.network_dialog, null, false);

        cd = new ConnectionDetector(context);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(networkDialogBinding.getRoot());
        final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        networkDialogBinding.btnRetry.setOnClickListener(view -> {
            networkDialogBinding.btnRetry.setEnabled(false);
            networkDialogBinding.pBar.setVisibility(View.VISIBLE);
            if (cd.isConnectingToInternet()) {
                alertDialog.dismiss();
                idialog.RetryResponse(type);
            } else {
                networkDialogBinding.btnRetry.startAnimation(animShake);
                networkDialogBinding.btnRetry.setEnabled(true);
                networkDialogBinding.pBar.setVisibility(View.GONE);
                new Handler().postDelayed(networkDialogBinding.btnRetry::clearAnimation, 100);
            }
        });
    }

    public interface INetworkDialog {
        void RetryResponse(String type);
    }
}
