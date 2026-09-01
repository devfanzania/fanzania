package com.yorker.fanzania.views.screens.tournament.wallet;

import static com.yorker.fanzania.constants.Constants.BASE_URL;
import static com.yorker.fanzania.constants.Constants.PGClientId;
import static com.yorker.fanzania.constants.Constants.PGClientSecret;
import static com.yorker.fanzania.constants.Constants.isPaid;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.databinding.ActivityWalletBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.WalletInfoDialog;
import com.yorker.fanzania.dialog.WalletRewardDialog;
import com.yorker.fanzania.dialog.WithrawDialog;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.restservices.casefree.ApiClient;
import com.yorker.fanzania.views.model.casefee.CashfreeResponse;
import com.yorker.fanzania.views.model.casefee.PaymentGateway;
import com.yorker.fanzania.views.model.casefee.PaymentGatewayRequest;
import com.yorker.fanzania.views.screens.tournament.kyc.bankdetails.BankDetailsFragmentPresenter;
import com.yorker.fanzania.views.screens.tournament.subscription.CaseFreeAPIActivity;
import com.yorker.fanzania.views.screens.tournament.subscription.SubscriptionActivity;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends BaseActivity<WalletPresenter>
        implements WalletPresenter.IMainView, BankDetailsFragmentPresenter.IMainView {

    private WalletPresenter presenter;
    private ActivityWalletBinding binding;
    private WalletResponse obj;
    private List<WalletRewardsResponse> rList;
    private int totalClaimAmount=0;
    private int totalRewardAmount=0, totalOutstandingAmount= 0, minAmountToWithDraw = 0;
    private BankDetailsFragmentPresenter presenterBank;
    private JSONObject dataBank = null;
    private int walletAmount = 0,recentTopUp = 0;


    @Override
    protected WalletPresenter onCreatePresenter() {
        presenter = new WalletPresenter(this, this);
        presenterBank = new BankDetailsFragmentPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, WalletPresenter presenter) {
        WalletPresenterComponent component1 = DaggerWalletPresenterComponent.builder()
                .presenterComponent(component)
                .walletApplicationModule(new WalletApplicationModule(this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        initViews();
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.title_mywallet));
        binding.tvAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentDialog(WalletActivity.this);
            }
        });
        binding.tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minAmountToWithDraw != 0 && totalOutstandingAmount !=0 && dataBank != null){
                    new WithrawDialog(WalletActivity.this, dataBank, minAmountToWithDraw, totalOutstandingAmount, new WithrawDialog.WithdrawalCallback() {
                        @Override
                        public void onWithdrawalSuccess() {
                            getWalletData();
                            getTotalClaims();
                            getTotalRewards();
                        }
                    });
                }else{
                    Toast.makeText(WalletActivity.this, "No amount to withdraw", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getWalletData();
        getTotalClaims();
        getTotalRewards();
        getBankData();

        binding.tvBreakdown.setOnClickListener(v->{
            if(rList != null) {
                new WalletRewardDialog(this, rList);
            }
        });
    }

    private void getBankData() {
        if (CheckInternetConnection())
            presenterBank.fetchDetails();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_4);
    }

    private void getWalletData() {
        if (CheckInternetConnection())
            presenter.fetchData();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void getTotalClaims() {
        if (CheckInternetConnection())
            presenter.getTotalClaimsInfo();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_3);
    }

    private void getTotalRewards() {
        if (CheckInternetConnection())
            presenter.getTotalRewardsInfo();
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    @Override
    public void RetryResponse(String type) {
        switch (type){
            case Constants.APICALL_1:
                presenter.fetchData();
                break;

            case Constants.APICALL_2:
                presenter.getTotalRewardsInfo();
                break;

            case Constants.APICALL_3:
                presenter.getTotalClaimsInfo();
                break;

            case Constants.APICALL_4:
                presenterBank.fetchDetails();
                break;
        }
    }

    @Override
    public void fetchDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                obj = new Gson().fromJson(jsonObject.getJSONArray("data").getJSONObject(0).toString(), WalletResponse.class);
                setData();
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchTotalRewardsInfo(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                rList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<WalletRewardsResponse>>() {
                                }.getType())
                );

                if (rList.size()>0){
                    totalRewardAmount=rList.get(0).getTotalRewardAmount();
                    binding.tvTRWxt.setText(new StringBuilder().append("INR ")
                            .append(totalRewardAmount));
                    binding.tvBreakdown.setEnabled(true);
                }else {
                    binding.tvTRWxt.setText(new StringBuilder().append("INR ")
                            .append(totalRewardAmount));
                    binding.tvTRWxt1.setText(new StringBuilder().append("INR ")
                            .append(totalRewardAmount));
                    binding.tvBreakdown.setEnabled(false);
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<WalletClaimResponse> getWalletClaims(List<WalletClaimResponse> cList){
        List<WalletClaimResponse> cList_ = new ArrayList<>();
        for (WalletClaimResponse walletClaimResponse : cList){
            if (walletClaimResponse.getClaimId() != 0){
                cList_.add(walletClaimResponse);
            }
        }
        return cList_;
    }

    @Override
    public void fetcTotalClaimsInfo(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                List<WalletClaimResponse> cList = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<WalletClaimResponse>>() {
                                }.getType())
                );
                Log.d("jjkhjkhjkdgfgdshfgdshfghgfs","jhj ::: "+cList);

                if (cList.size()>0){
                    totalClaimAmount=cList.get(0).getTotalClaimAmount();
                    walletAmount= cList.get(0).getWalletAmount();
                    recentTopUp= cList.get(0).getRecentTopUpAmount();
                    binding.tvTRWxt1.setText(new StringBuilder().append("INR ")
                            .append(walletAmount));
                    binding.tvRecentTopUp.setText(new StringBuilder().append("INR ")
                            .append(recentTopUp).append(" ON ").append(cList.get(0).getRecentTopUpDate()));
                    StringBuilder str=new StringBuilder().append("INR ")
                            .append(totalClaimAmount);

                    StringBuilder strOut =new StringBuilder();

                    if (cList.get(0).getTotalOutstandingAmount()>0){
                        totalOutstandingAmount = cList.get(0).getTotalOutstandingAmount();
                        minAmountToWithDraw = cList.get(0).getMinAmountToWithDraw();

                        str.append(" ");
                        str.append("( ").append(" INR ").append(cList.get(0).getTotalOutstandingAmount())
                                .append(" ").append(getString(R.string.text_outstanding)).append(" )");


                    }

                    strOut.append(" ").append(" INR ").append(cList.get(0).getTotalCash());

                    binding.tvRACtxt.setText(strOut);
                    binding.llPaymentDetails.setVisibility(View.VISIBLE);

                    binding.rcvWCL.setVisibility(View.VISIBLE);
                    binding.rcvWCL.setLayoutManager(new LinearLayoutManager(this));
                    binding.rcvWCL.setAdapter(new ClaimListAdapter(getWalletClaims(cList)));
                    binding.tvVOUCtxt.setText(str);
                    if (totalOutstandingAmount <= 0 ){
                        binding.tvWithdraw.setClickable(false);
                        binding.tvWithdraw.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    }

                }else {
                    binding.rcvWCL.setVisibility(View.GONE);
                    binding.tvRACtxt.setText(new StringBuilder().append("INR ")
                            .append(totalClaimAmount));
                    binding.llPaymentDetails.setVisibility(View.GONE);
                    binding.tvTRWxt1.setText(new StringBuilder().append("INR ")
                            .append(walletAmount));
                    binding.tvRecentTopUp.setText(new StringBuilder().append("INR ")
                            .append(recentTopUp));
                    totalOutstandingAmount = 0;
                    minAmountToWithDraw = 0;

                    StringBuilder str=new StringBuilder().append("INR ")
                            .append(0);
                    str.append(" ");
                    str.append("( ").append(" INR ").append(0)
                            .append(" ").append(getString(R.string.text_outstanding)).append(" )");
                    binding.tvVOUCtxt.setText(str);

                    binding.tvWithdraw.setClickable(false);
                    binding.tvWithdraw.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                }
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
        } catch (JSONException e) {
            Log.d("jjkhjkhjkdgfgdshfgdshfghgfs","jhj e::: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void setData() {
        binding.pBar.setVisibility(View.GONE);
        binding.clMain.setVisibility(View.VISIBLE);

        String text = obj.getUserTier() + " " + getString(R.string.text_member);
        binding.tvTitle.setText(text);

        String tpts = obj.getWalletPoints() + " " + getString(R.string.text_tierpoints);
        binding.tvTierPts.setText(tpts);

        String mcPts = obj.getMatchContestTotal() + " PTS";
        String tcPts = obj.getTournamentTotal() + " PTS";

        binding.matchContestPts.setText(mcPts);
        binding.tournamentPts.setText(tcPts);

        //--------- Retain Tier ------//
        if (obj.getRetainTier() != null && obj.getRetainTier().length() > 0) {
            binding.llRetainTier.setVisibility(View.VISIBLE);
            String htxt = getString(R.string.text_retain) + " " + obj.getRetainTier();
            binding.tvRT.setText(htxt);
            String txt = getString(R.string.text_get) + " " + obj.getRetainTierByPoints() + " " + getString(R.string.text_ptsmoreby) + " " + obj.getRetainTierByDate();
            binding.tvRTTxt.setText(txt);
        } else
            binding.llRetainTier.setVisibility(View.GONE);

        //--------- Attain Tier ------//
        if (obj.getAttainTier() != null && obj.getAttainTier().length()>0) {
            binding.llAttainTier.setVisibility(View.VISIBLE);
            String htxt = getString(R.string.text_atain) + " " + obj.getAttainTier();
            binding.tvAT.setText(htxt);
            String txt = getString(R.string.text_get) + " " + obj.getAttainTierByPoints() + " " + getString(R.string.text_ptsmoreby) + " " + obj.getAttainTierByDate();
            binding.tvATTxt.setText(txt);
        } else
            binding.llAttainTier.setVisibility(View.GONE);

        String txtexp = getString(R.string.text_expires) + " " + obj.getTierExpiryDate();
        switch (obj.getUserTier()) {
            case "BRONZE":
                binding.tvBronze.setTextColor(getResources().getColor(R.color.colorBlack));
                binding.tvBronzeTxt.setText(obj.getTierExpiryDate());
                binding.tvBronzeTxt.setTypeface(Typeface.DEFAULT_BOLD);
                break;

            case "SILVER":
                binding.tvSilver.setTextColor(getResources().getColor(R.color.colorBlack));
                binding.tvSilverTxt.setText(txtexp);
                break;

            case "GOLD":
                binding.tvGold.setTextColor(getResources().getColor(R.color.colorBlack));
                binding.tvGoldTxt.setText(txtexp);
                break;

            case "PLATINUM":
                binding.tvPlatinum.setTextColor(getResources().getColor(R.color.colorBlack));
                binding.tvPlatinumTxt.setText(txtexp);

                if (binding.llRetainTier.getVisibility()==View.GONE
                        && binding.llAttainTier.getVisibility()==View.GONE ) {
                    binding.tvHighestTxt.setVisibility(View.VISIBLE);
                }
                break;
        }

        String txt = getString(R.string.text_youareamembersince) + " " + obj.getSignUpDate();
        binding.tvMemberSince.setText(txt);

//        String txt1 = getString(R.string.text_yourtierpoints) + " " + obj.getTierStartDate() + " " + getString(R.string.text_peryear);
        String txt1 = getString(R.string.text_yourtierpoints) + " " + obj.getTierStartDate();
        binding.tvMemberSinceTxt.setText(txt1);
    }
    public void caseFree(double aa){
        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
        PaymentGatewayRequest request = new PaymentGatewayRequest("cashfree");
        Call<CashfreeResponse> call = retrofitAipService.fetchPaymentGatewayDetails(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,request);
        call.enqueue(new Callback<CashfreeResponse>() {
            @Override
            public void onResponse(Call<CashfreeResponse> call, Response<CashfreeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PaymentGateway pg : response.body().getData()) {
                        PGClientId = pg.getPGClientId();
                        PGClientSecret = pg.getPGClientSecret();
                        ApiClient.BASE_URL = pg.getUrl();
                    }
                    startActivity(new Intent(WalletActivity.this, CaseFreeAPIActivity.class).putExtra("amount",aa).putExtra("currencySymbol","INR"));
                } else {
                    Log.e("API_ERROR", "Response failed");
                }
            }

            @Override
            public void onFailure(Call<CashfreeResponse> call, Throwable t) {
                Log.e("API_ERROR", "API call failed: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
        MontserratRegular tvStats = menu.findItem(R.id.action_item_one).getActionView().findViewById(R.id.tvActionApply);
        tvStats.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_info, 0, 0);
        tvStats.setText(getString(R.string.text_info));
        tvStats.setOnClickListener(view -> new WalletInfoDialog(this));
        return true;
    }

    @Override
    public void BankDetails(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                dataBank = jsonObject.getJSONObject(Constants.STR_DATA);

                if(dataBank.getString("BankVerified").equalsIgnoreCase("yes") && totalOutstandingAmount > 0){
                    binding.tvWithdraw.setClickable(true);
                    binding.tvWithdraw.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                }else{
                    binding.tvWithdraw.setClickable(false);
                    binding.tvWithdraw.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                }

            }
//            else
//                CustomToast.getInstance(WalletActivity.this).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
            binding.tvWithdraw.setClickable(false);
            binding.tvWithdraw.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
        }
    }

    @Override
    public void OnUpdateDetails(JSONObject jsonObject) {

    }

    @Override
    public void OnFailed(Boolean b, String msg) {

    }


    private void showPaymentDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_payment, null);
        builder.setView(dialogView);

        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount);
        Button buttonAddPayment = dialogView.findViewById(R.id.buttonAddPayment);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editTextAmount.getText().toString().trim();
                if (!amount.isEmpty()) {
                    caseFree(Double.parseDouble(amount));
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaid){
            isPaid = false;
            getTotalClaims();
        }
    }
}
