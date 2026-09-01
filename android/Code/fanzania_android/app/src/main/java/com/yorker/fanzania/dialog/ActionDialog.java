package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.CommonDialogBinding;

import java.util.Objects;

public class ActionDialog extends AlertDialog.Builder {
    public ActionDialog(Context context, String title, String message, IActionDialog iActionDialog, int value,
                        int position, String ID) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        CommonDialogBinding binding = DataBindingUtil.inflate(li, R.layout.common_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.txtTitle.setText(title);
        binding.txtContent.setText(message);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            alertDialog.dismiss();
            iActionDialog.PositiveResponse(true, value,position,ID);
        });

        binding.btnNo.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }

    public interface IActionDialog {
        void PositiveResponse(Boolean value, int Type, int position, String userID);
    }
}
