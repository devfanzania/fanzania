package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.PlayerDetailDialogBinding;
import com.yorker.fanzania.databinding.WithdrawDialogBinding;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithrawDialog extends AlertDialog.Builder {
    private AlertDialog alertDialog;
    @Inject
    SharedPrefManager sharedPrefManager;
    String BankVerified = null;

    WithdrawalCallback withdrawalCallback;
    public interface WithdrawalCallback{
        public void onWithdrawalSuccess();
    }

    public WithrawDialog(Context context, JSONObject dataBank, int minAmountToWithdraw, int totalOutstandingAmount, WithdrawalCallback withdrawalCallback) {
        super(context);

        this.withdrawalCallback = withdrawalCallback;
        String AccountNumber = null ,IFSC = null , BankName = null;

        LayoutInflater li = LayoutInflater.from(context);
        WithdrawDialogBinding binding = DataBindingUtil.inflate(li, R.layout.withdraw_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());
        
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        
        binding.tvMyMatch.setText("AVAILABLE TO WITHDRAW  | "+totalOutstandingAmount);
//        binding.edtAmount.setText("0");
        binding.amtNotice.setText("min INR "+minAmountToWithdraw+" and max INR 10000 allowed per day");
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateAmount(binding, context, minAmountToWithdraw, totalOutstandingAmount, BankVerified);
            }
        });

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        try {
            if(dataBank != null){
                BankVerified = dataBank.getString("BankVerified");
                AccountNumber = dataBank.getString("AccountNumber");
                IFSC = dataBank.getString("IFSC");
                BankName = dataBank.getString("BankName");

                binding.bankName.setText("Bank Name: "+BankName);
                binding.accountNumber.setText("Account Number: "+AccountNumber);
                binding.bankVerified.setText("Bank Verified: "+BankVerified.toUpperCase());

                if (!BankVerified.equalsIgnoreCase("yes")){
                    binding.tvWithdraw.setClickable(false);
                    binding.tvWithdraw.setBackground(context.getResources().getDrawable(R.drawable.round_corner_grey_live));
                }else{
                    binding.tvWithdraw.setClickable(true);
                    binding.tvWithdraw.setBackground(context.getResources().getDrawable(R.drawable.round_corner_red));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        binding.tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //payout api will goes here
                try {
                    transferFunds(Integer.parseInt(binding.edtAmount.getText().toString()),context, binding, alertDialog);

                }catch (Exception e){
                    Toast.makeText(context, "Enter correct amount to withdraw", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void validateAmount(WithdrawDialogBinding binding, Context context, int minAmountToWithdraw, int totalOutstandingAmount, String bankVerified){
        String text = binding.edtAmount.getText().toString();

        if (text.length() > 0){
            int amount = Integer.parseInt(text);
            if(amount < minAmountToWithdraw){
                Toast.makeText(context, "Enter amount greater or equal to "+minAmountToWithdraw, Toast.LENGTH_SHORT).show();
                //binding.edtAmount.setText(""+minAmountToWithdraw);
                binding.tvWithdraw.setClickable(false);
                binding.tvWithdraw.setBackground(context.getResources().getDrawable(R.drawable.round_corner_grey_live));

            }else if(amount > totalOutstandingAmount){
                Toast.makeText(context, "Enter amount less or equal to "+totalOutstandingAmount, Toast.LENGTH_SHORT).show();
                //binding.edtAmount.setText(""+totalOutstandingAmount);
                binding.tvWithdraw.setClickable(false);
                binding.tvWithdraw.setBackground(context.getResources().getDrawable(R.drawable.round_corner_grey_live));
            }else if(bankVerified.equalsIgnoreCase("yes")){
                binding.tvWithdraw.setClickable(true);
                binding.tvWithdraw.setBackground(context.getResources().getDrawable(R.drawable.round_corner_red));
            }
        }
    }

    public void transferFunds(int amount, Context mContext, WithdrawDialogBinding binding, AlertDialog withrawDialog) {
        sharedPrefManager = SharedPrefManager.getInstance(mContext);

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TAG_ID, sharedPrefManager.getCustomer_Id());
        map.put(Constants.TAG_AMOUNT, amount);

        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);

        Call<JsonObject> call = retrofitAipService.transferFund(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,
                sharedPrefManager.getCustomer_Id(), map);

        binding.pBar.setVisibility(View.VISIBLE);
        binding.tvWithdraw.setClickable(false);
        binding.tvWithdraw.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_grey_live));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                binding.pBar.setVisibility(View.GONE);
                binding.tvWithdraw.setClickable(true);
                binding.tvWithdraw.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_red));

                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                            CustomToast.getInstance(mContext).showSmallCustomToast(jsonObject.getString("statusMessage"));
                            if (withdrawalCallback !=null){
                                withdrawalCallback.onWithdrawalSuccess();
                                withrawDialog.dismiss();
                            }
                        } else
                            CustomToast.getInstance(mContext).showSmallCustomToast(jsonObject.getString("statusMessage"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                binding.pBar.setVisibility(View.GONE);
                binding.tvWithdraw.setClickable(true);
                binding.tvWithdraw.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_red));

                CustomToast.getInstance(mContext).showSmallCustomToast("Something went wrong, please try after sometime.");
                System.out.println("error1 " + call.toString());
            }
        });
    }

}
