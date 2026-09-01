package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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

public class RenameLeagueDialog extends AlertDialog.Builder {
    private Context context;
    private IRenameLeaguDialog iCreateLeaguDialog;
    private JoinLeagueDialogBinding binding;
    private GradientDrawable bgShape;
    private AlertDialog alertDialog;

    public RenameLeagueDialog(Context context, IRenameLeaguDialog iCreateLeaguDialog, String tournamentID, String leagueId, String customerId, String leagueName) {
        super(context);
        this.context = context;
        this.iCreateLeaguDialog = iCreateLeaguDialog;

        LayoutInflater li = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(li, R.layout.join_league_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        bgShape = (GradientDrawable) binding.edtContent.getBackground();


        binding.txtTitle.setText("Change League Name (for admins only)");
        binding.btnYes.setText("Rename");
        binding.edtContent.setText(leagueName);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            if (!binding.edtContent.getText().toString().trim().isEmpty()) {
                if (binding.edtContent.getText().toString().length()>2){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorBorder, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorBorder));

                    CreateLeague(tournamentID, binding.edtContent.getText().toString(), customerId, leagueId);

                    showLoader();
                }else
                {
                    binding.edtContent.setError(context.getString(R.string.text_minleaguename));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
                }
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

    public interface IRenameLeaguDialog {
        void renameLeague(Boolean value);
    }

    private void CreateLeague(String tournamentID, String leagueName, String customerId, String leagueId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_LEAGUENAME, leagueName);
        map.put(Constants.TAG_LEAGUEID, leagueId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.RenameLeague(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                customerId,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                            alertDialog.dismiss();
                            iCreateLeaguDialog.renameLeague(true);
                            CustomToast.getInstance(context).showSmallCustomToast(jsonObject.getString("statusMessage"));
                        } else {
                            CustomToast.getInstance(context).showSmallCustomToast(jsonObject.getString("statusMessage"));
                        }
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
