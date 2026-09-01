package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.JoinLeagueDialogBinding;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinLeagueDialog extends AlertDialog.Builder {
    private Context context;
    private IJoinLeaguDialog iJoinLeaguDialog;
    private JoinLeagueDialogBinding binding;
    private GradientDrawable bgShape;
    private AlertDialog alertDialog;

    public JoinLeagueDialog(Context context, IJoinLeaguDialog iJoinLeaguDialog, String tournamentID, String leagueID, String userID) {
        super(context);

        this.context = context;
        this.iJoinLeaguDialog = iJoinLeaguDialog;

        LayoutInflater li = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(li, R.layout.join_league_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        bgShape = (GradientDrawable) binding.edtContent.getBackground();

        binding.txtTitle.setText(context.getString(R.string.text_joinleagueheader));
        binding.btnYes.setText(context.getString(R.string.text_join));

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            if (!binding.edtContent.getText().toString().trim().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    bgShape.setStroke(1, context.getResources().getColor(R.color.colorBorder, context.getTheme()));
                else
                    bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorBorder));

                JoinLeague(tournamentID, leagueID, userID, binding.edtContent.getText().toString());

                showLoader();

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
                else
                    bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));

                binding.edtContent.setError(context.getString(R.string.text_placeisblank));
            }
        });

        binding.btnNo.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }

    private void showLoader() {
        binding.btnNo.setEnabled(false);
        binding.btnYes.setEnabled(false);
        binding.edtContent.setEnabled(false);
        binding.pBar.setVisibility(View.VISIBLE);
    }

    public interface IJoinLeaguDialog {
        void JDPositiveResponse(Boolean value);
    }

    private void JoinLeague(String tournamentID, String leagueID, String userID, String code) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_LEAGUEID, leagueID);
        map.put(Constants.TAG_ID, userID);
        map.put(Constants.TAG_LEAGUEPIN, code);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.JoineLeague(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                userID,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                            alertDialog.dismiss();
                            iJoinLeaguDialog.JDPositiveResponse(true);
                            CustomToast.getInstance(context).showSmallCustomToast(jsonObject.getString("statusMessage"));
                        } else
                            CustomToast.getInstance(context).showSmallCustomToast(jsonObject.getString("statusMessage"));

                        hideLoader();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideLoader();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
                hideLoader();
            }
        });
    }

    private void hideLoader() {
        binding.btnNo.setEnabled(true);
        binding.btnYes.setEnabled(true);
        binding.edtContent.setEnabled(true);
        binding.pBar.setVisibility(View.GONE);
    }

}
