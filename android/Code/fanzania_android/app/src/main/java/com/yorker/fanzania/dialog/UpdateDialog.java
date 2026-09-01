package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.UpdateDialogBinding;

import java.util.Objects;

public class UpdateDialog extends AlertDialog.Builder {
    public UpdateDialog(Context context, String title, String message, IUpdateDialog iUpdateDialog) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        UpdateDialogBinding binding = DataBindingUtil.inflate(li, R.layout.update_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.txtTitle.setText(title);
        binding.txtContent.setText(message);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(false);

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            alertDialog.dismiss();
            iUpdateDialog.UpdateResponse(true);
        });
    }

    public interface IUpdateDialog {
        void UpdateResponse(Boolean value);
    }
}
