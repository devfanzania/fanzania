package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.CommonDialogBinding;

import java.util.Objects;

public class CommonDialog extends AlertDialog.Builder {
    public CommonDialog(Context context, String title, String message, ICommonDialog iCommonDialog) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        CommonDialogBinding binding = DataBindingUtil.inflate(li, R.layout.common_dialog, null, false);

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
            iCommonDialog.PositiveResponse(true);
        });

        binding.btnNo.setOnClickListener(view ->
            alertDialog.dismiss());
    }

    public interface ICommonDialog {
        void PositiveResponse(Boolean value);
    }
}
