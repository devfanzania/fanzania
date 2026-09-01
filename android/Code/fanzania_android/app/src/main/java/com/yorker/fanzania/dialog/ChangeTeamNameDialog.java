package com.yorker.fanzania.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ChangeTeamNameDialogBinding;
import com.yorker.fanzania.databinding.WithdrawDialogBinding;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTeamNameDialog extends AlertDialog.Builder {
    private AlertDialog alertDialog;
    @Inject
    SharedPrefManager sharedPrefManager;
    String BankVerified = null;

    UpdateTeamNameCallback updateTeamNameCallback;
    public interface UpdateTeamNameCallback{
        public void onConfirmClick(String teamName);
    }

    public ChangeTeamNameDialog(Context context, String teamName, UpdateTeamNameCallback updateTeamNameCallback) {
        super(context);

        this.updateTeamNameCallback = updateTeamNameCallback;
        String AccountNumber = null ,IFSC = null , BankName = null;

        LayoutInflater li = LayoutInflater.from(context);
        ChangeTeamNameDialogBinding binding = DataBindingUtil.inflate(li, R.layout.change_team_name_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);

        binding.edtAmount.setText(teamName);
        binding.amtNotice.setText("");
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edtAmount.getText().toString() == null){
                    binding.amtNotice.setText("Your team name should have at least 3 characters.");
                }else{
                    if (binding.edtAmount.getText().toString().length() < 3 ){
                        binding.amtNotice.setText("Your team name should have at least 3 characters.");
                    }else{
                        binding.amtNotice.setText("");
                    }
                }
            }
        });

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (!act.isFinishing() && !act.isDestroyed()) {
                alertDialog.show();
            }
        }

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        binding.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtAmount.getText().toString() == null){
                    binding.amtNotice.setText("Your team name should have at least 3 characters.");
                }else{
                    if (binding.edtAmount.getText().toString().length() < 3){
                        binding.amtNotice.setText("Your team name should have at least 3 characters.");
                    }else{
                        alertDialog.dismiss();
                        binding.amtNotice.setText("");
                        updateTeamNameCallback.onConfirmClick(binding.edtAmount.getText().toString());
                    }
                }
            }
        });

    }

}
