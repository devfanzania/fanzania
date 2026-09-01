package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonObject;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.TeamNameDialogBinding;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model.UpcomingTournamentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamNameDialog extends AlertDialog.Builder {
    private GradientDrawable bgShape;
    private Context context;
    private TeamNameDialogBinding binding;
    private ITeamNameDialog iTeamNameDialog;
    private AlertDialog alertDialog;

    public TeamNameDialog(Context context, ITeamNameDialog iJoinLeaguDialog, UpcomingTournamentModel data, String customerId) {
        super(context);
        this.context = context;
        this.iTeamNameDialog = iJoinLeaguDialog;

        LayoutInflater li = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(li, R.layout.team_name_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        bgShape = (GradientDrawable) binding.edtContent.getBackground();

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.edtContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                binding.txtError.setText("");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    bgShape.setStroke(1, context.getResources().getColor(R.color.colorBorder, context.getTheme()));
                else
                    bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorBorder));
            }
        });

        binding.btnYes.setOnClickListener(view -> {
            if (binding.edtContent.getText().toString().length() > 0) {
                if (binding.edtContent.getText().toString().length()>2){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorBorder, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorBorder));

                    showLoader();
                    CheckTeamName(String.valueOf(data.getTournamentId()), binding.edtContent.getText().toString(), customerId);
                }else
                {
                    binding.edtContent.setError(context.getString(R.string.text_minteamname));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
                    else
                        bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
                }
            } else {
                showError(context.getString(R.string.text_errorplaceisblank));
            }
        });

        binding.btnNo.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }

    private void showError(String text) {
        binding.txtError.setText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            bgShape.setStroke(1, context.getResources().getColor(R.color.colorRed, context.getTheme()));
        else
            bgShape.setStroke(1, ContextCompat.getColor(context, R.color.colorRed));
    }

    public interface ITeamNameDialog {

        void CTPositiveResponse(int userTeamID, String tournamentId);
    }

    private void CheckTeamName(String tournamentID, String teamName, String customerId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID, Integer.parseInt(tournamentID));
        map.put(Constants.TAG_USERTEAMNAME, teamName);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.VerifyTeamName(Constants.str_HEADER,
                Constants.RETROFIT_HEADER_TYPE,
                Constants.RETROFIT_HEADER_TOKEN1,
                map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                            CreateTeamName(tournamentID, teamName, customerId);
                        } else {
                            showError(context.getString(R.string.text_teamnameexists));
                            hideLoader();
                        }

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

    private void showLoader() {
        binding.btnNo.setEnabled(false);
        binding.btnYes.setEnabled(false);
        binding.edtContent.setEnabled(false);
        binding.pBar.setVisibility(View.VISIBLE);
    }

    private void CreateTeamName(String tournamentID, String teamName, String customerId) {

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_TOURNAMENTID,  Integer.parseInt(tournamentID));
        map.put(Constants.TAG_USERTEAMNAME, teamName);
        map.put(Constants.TAG_ID,  Integer.parseInt(customerId));

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.CreateUserTeamName(Constants.str_HEADER,
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
                            JSONObject obj = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);

                            int userTeamID=obj.getInt("UserTeamId");
                            String TournamentId=obj.getString("TournamentId");

                            iTeamNameDialog.CTPositiveResponse(userTeamID,TournamentId);
                            alertDialog.dismiss();

                        } else {
                            CustomToast.getInstance(context).showSmallCustomToast(jsonObject.getString("statusMessage"));
                            hideLoader();
                        }

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
}
