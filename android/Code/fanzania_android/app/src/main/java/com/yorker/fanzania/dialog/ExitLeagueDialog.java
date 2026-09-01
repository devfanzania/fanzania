package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.CommonDialogBinding;
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

public class ExitLeagueDialog extends AlertDialog.Builder {
    private Context context;
    private IExitDialog iExitDialog;
    private CommonDialogBinding binding;
    private GradientDrawable bgShape;
    private AlertDialog alertDialog;

    public ExitLeagueDialog(Context context, String tournamentID, String leagueID, String customerId, IExitDialog iExitDialog) {
        super(context);

        this.context = context;
        this.iExitDialog = iExitDialog;

        LayoutInflater li = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(li, R.layout.common_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        binding.txtTitle.setText(context.getString(R.string.text_exitleague));
        binding.txtContent.setText(context.getString(R.string.text_exitleaguetext));

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            showLoader();
            ExitLeague(tournamentID, leagueID, customerId);
        });

        binding.btnNo.setOnClickListener(view ->
            alertDialog.dismiss());
    }

    public interface IExitDialog {
        void ExitLeague(Boolean value);
    }

    private void ExitLeague(String tournamentID, String leagueID, String customerId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, tournamentID);
        map.put(Constants.TAG_LEAGUEID, leagueID);
        map.put(Constants.TAG_ID, customerId);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.ExitLeague(Constants.str_HEADER,
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
                            iExitDialog.ExitLeague(true);
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
        binding.pBar.setVisibility(View.GONE);
    }

    private void showLoader() {
        binding.btnNo.setEnabled(false);
        binding.btnYes.setEnabled(false);
        binding.pBar.setVisibility(View.VISIBLE);
    }
}
