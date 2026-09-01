package com.yorker.fanzania.views.screens.league;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivityLeagueSubscriptionBinding;
import com.yorker.fanzania.databinding.ItemLeagueSubscriptionTeamBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.restservices.RetrofitRazorPayApiService;
import com.yorker.fanzania.restservices.ServiceGenerator;
import com.yorker.fanzania.views.screens.auth.registration.adapter.CustomSpinnerAdapter;
import com.yorker.fanzania.views.screens.auth.registration.model.CountryListModel;
import com.yorker.fanzania.views.screens.league.adapter.MyLeagueSubscriptionListAdapter;
import com.yorker.fanzania.views.shared.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueSubscriptionActivity extends BaseActivity<LeagueSubscriptionPresenter>
        implements LeagueSubscriptionPresenter.IMainView, MyLeagueSubscriptionListAdapter.IPoints, PaymentResultListener {

    private LeagueSubscriptionPresenter presenter;
    private ActivityLeagueSubscriptionBinding binding;
//    private Boolean isOldPassValidate = false;
    private Boolean isNewPassValidate = false;
    private Boolean isConfrmPassValidate = false;
    private LinkedList<LeagueSubscriptionModel> list;
    private MyLeagueSubscriptionListAdapter pAdapter;
    List<LeagueSubscriptionModel> plan1Selected, plan2Selected, plan3Selected;
    public static String orderId = null;
    public double amount = 0;
    String Details = null, Desc = null, receiptId = null, tid, lid;
    SharedPrefManager sharedPrefManager;
    boolean selectAll1 = false, selectAll2 = false, selectAll3 = false;
    private LinkedList<CountryListModel> countrylist;
    private String strCountry;
    private String currencySymbol = "INR";
//    MatchId
    @Override
    protected LeagueSubscriptionPresenter onCreatePresenter() {
        presenter = new LeagueSubscriptionPresenter(this, LeagueSubscriptionActivity.this);
        return presenter;
    }

    private void getCountrySpinner() {
        countrylist = new LinkedList<>();
        CountryListModel obj = new CountryListModel("0", "Select Currency", false, false);
        CountryListModel obj1 = new CountryListModel("1", "INR", false, false);
        CountryListModel obj2 = new CountryListModel("2", "GBP", false, false);
        CountryListModel obj3 = new CountryListModel("3", "USD", false, false);
        countrylist.add(0, obj);
        countrylist.add(1, obj1);
        countrylist.add(2, obj2);
        countrylist.add(3, obj3);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, countrylist);
        binding.spinner.setAdapter(customSpinnerAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!countrylist.get(position).getCountryId().equals("0")) {
                    strCountry = countrylist.get(position).getCountryId();
                    binding.edtCountry.setText(countrylist.get(position).getCountry());

                    if (countrylist.get(position).getCountryId().equals("1")){
                        currencySymbol = "INR";
                        binding.plan1Select.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                        binding.plan3Select.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                        binding.plan1Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),1));
                        binding.plan2Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),2));
                        binding.plan3Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),3));
                    }else if (countrylist.get(position).getCountryId().equals("2")){
                        currencySymbol = "GBP";
                        binding.plan1Select.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                        binding.plan3Select.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));

                        binding.plan1Price.setText("NA");
                        binding.plan2Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),2));
                        binding.plan3Price.setText("NA");

                    }else if (countrylist.get(position).getCountryId().equals("3")){
                        currencySymbol = "USD";
                        binding.plan1Select.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                        binding.plan3Select.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));

                        binding.plan1Price.setText("NA");
                        binding.plan2Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),2));
                        binding.plan3Price.setText("NA");
                    }

                    binding.totalcurrency.setText(currencySymbol);

                    removeAllSubscriptionPlan(1);
                    removeAllSubscriptionPlan(2);
                    removeAllSubscriptionPlan(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void injectPresenter(PresenterComponent component, LeagueSubscriptionPresenter presenter) {
        LeagueSubscriptionPresenterComponent component1 = DaggerLeagueSubscriptionPresenterComponent.builder()
                .presenterComponent(component)
                .leagueSubscriptionApplicationModule(new LeagueSubscriptionApplicationModule(LeagueSubscriptionActivity.this))
                .build();
        component1.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_league_subscription);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        tid = getIntent().getStringExtra("tid");
        lid = getIntent().getStringExtra("lid");
        initViews();
        initListners();
        getCountrySpinner();
        presenter.LeagueSubscriptions(lid,tid);
    }

    private void initListners() {
        binding.plan1Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllSubscriptionPlan(1);
            }
        });
        binding.plan2Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllSubscriptionPlan(2);
            }
        });
        binding.plan3Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllSubscriptionPlan(3);
            }
        });

        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicked",""+amount);
                if (amount > 0){
                    initiateOrder(amount);
                    binding.btnPay.setEnabled(false);
                    binding.btnPay.setClickable(false);
                }

            }
        });
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(("League Subscription"));
        plan1Selected = new ArrayList<>();
        plan2Selected = new ArrayList<>();
        plan3Selected = new ArrayList<>();
        setSupportActionBar(binding.inToolbar.toolbar);

        binding.inRVList.rvList.setLayoutManager(new LinearLayoutManager(this));
        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    @Override
    public void RetryResponse(String type) {
        presenter.LeagueSubscriptions(lid, tid);
    }

    @Override
    public void LeagueSubscriptions(JSONObject jsonObject) {
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_passwordsuccess));
                list = new LinkedList<>(
                        new Gson().fromJson(
                                jsonObject.getJSONArray(Constants.STR_DATA).toString()
                                , new TypeToken<List<LeagueSubscriptionModel>>() {
                                }.getType())
                );
                Log.e("size",""+list.size());
                if (list.size() > 0) {
                    Constants.client_id = list.get(0).getClient_id();
                    Log.d("rtyrtytfghgh","LeagueSubscriptions ::: Constants.client_id :: "+Constants.client_id);
                    Constants.client_secret = list.get(0).getClient_secret();
                    binding.plan1Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),1));
                    binding.plan2Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),2));
                    binding.plan3Price.setText(currencySymbol+" "+ getSelectedPlanAmount(list.get(0),3));

                    binding.inRVList.rvList.setVisibility(View.VISIBLE);
                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.GONE);
                    pAdapter = new MyLeagueSubscriptionListAdapter(LeagueSubscriptionActivity.this, list, this);
                    binding.inRVList.rvList.setAdapter(pAdapter);
                }else{

                    binding.inRVList.pBar.setVisibility(View.GONE);
                    binding.inRVList.rvList.setVisibility(View.GONE);
                    binding.inRVList.tvNoDataFound.setVisibility(View.VISIBLE);
                    binding.inRVList.tvNoDataFound.setText(getString(R.string.text_noteamavailable));
                }
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void LeagueSubscriptionsUpdated(JSONObject jsonObject) {
        binding.inRVList.pBar.setVisibility(View.GONE);
        enablePayButtons();
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                CustomToast.getInstance(this).showSmallCustomToast("Subscription details updated");
                presenter.LeagueSubscriptions(lid,tid);
            } else {
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removeAllSubscriptionPlan(int column){
        int i = 0;
        for (LeagueSubscriptionModel leagueSubscriptionModel : list){
            MyLeagueSubscriptionListAdapter.ViewHolder holder = ((MyLeagueSubscriptionListAdapter)binding.inRVList.rvList.getAdapter()).getViewByPosition(i);
            View view = holder.itemView;
            TextView tvTotal = view.findViewById(R.id.tvTotal);
            TextView tvPlan1 = view.findViewById(R.id.tvPlan1);
            TextView tvPlan2 = view.findViewById(R.id.tvPlan2);
            TextView tvPlan3 = view.findViewById(R.id.tvPlan3);

            ImageView imgPlan1 = view.findViewById(R.id.imgPlan1);
            ImageView imgPlan2 = view.findViewById(R.id.imgPlan2);
            ImageView imgPlan3 = view.findViewById(R.id.imgPlan3);

            if (column ==1){
                if (isPlanAlreadySelected(1,leagueSubscriptionModel.getUserId())){
                    view.findViewById(R.id.imgPlan1).setVisibility(View.GONE);
                    removeSelected(1, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan1.setVisibility(View.VISIBLE);
                }
            }else if (column == 2){
                if (isPlanAlreadySelected(2,leagueSubscriptionModel.getUserId())){
                    view.findViewById(R.id.imgPlan2).setVisibility(View.GONE);
                    removeSelected(2, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan2.setVisibility(View.VISIBLE);
                }
            }else if (column == 3){
                if (isPlanAlreadySelected(3,leagueSubscriptionModel.getUserId())){
                    view.findViewById(R.id.imgPlan3).setVisibility(View.GONE);
                    removeSelected(3, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan3.setVisibility(View.VISIBLE);
                }
            }
            i++;
        }
        updatePlanSelectedTotalAMount();
        Log.e("sizes","plan1->"+plan1Selected.size()+", plan2->"+plan2Selected.size()+", plan3->"+plan3Selected.size());
    }

    private void selectAllSubscriptionPlan(int column){
        int i = 0;
        for (LeagueSubscriptionModel leagueSubscriptionModel : list){
            MyLeagueSubscriptionListAdapter.ViewHolder holder = ((MyLeagueSubscriptionListAdapter)binding.inRVList.rvList.getAdapter()).getViewByPosition(i);
            View view = holder.itemView;
            TextView tvTotal = view.findViewById(R.id.tvTotal);
            TextView tvPlan1 = view.findViewById(R.id.tvPlan1);
            TextView tvPlan2 = view.findViewById(R.id.tvPlan2);
            TextView tvPlan3 = view.findViewById(R.id.tvPlan3);

            ImageView imgPlan1 = view.findViewById(R.id.imgPlan1);
            ImageView imgPlan2 = view.findViewById(R.id.imgPlan2);
            ImageView imgPlan3 = view.findViewById(R.id.imgPlan3);

            if (column ==1 && currencySymbol.equalsIgnoreCase("INR")){
                if (isPlanAlreadySelected(1,leagueSubscriptionModel.getUserId()) && selectAll1){
                    view.findViewById(R.id.imgPlan1).setVisibility(View.GONE);
                    removeSelected(1, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan1.setVisibility(View.VISIBLE);
                }else{
                    if (leagueSubscriptionModel.getSubscriptionType() != 1 && leagueSubscriptionModel.getSubscriptionType() != 3){
                        view.findViewById(R.id.imgPlan1).setVisibility(View.VISIBLE);
                        tvPlan1.setVisibility(View.GONE);
                        view.findViewById(R.id.imgPlan1).setBackgroundResource(R.drawable.tick_green);
                        addSelected(1,leagueSubscriptionModel.getUserId(), leagueSubscriptionModel);
                        updateTotalAMount(leagueSubscriptionModel, tvTotal);
                        if (isPlanAlreadySelected(3, leagueSubscriptionModel.getUserId())){
                            tvPlan3.setVisibility(View.VISIBLE);
                            imgPlan3.setVisibility(View.GONE);
                            removeSelected(3, leagueSubscriptionModel.getUserId());
                            updateTotalAMount(leagueSubscriptionModel, tvTotal);
                        }else if (isPlanAlreadySelected(2, leagueSubscriptionModel.getUserId())){
                            if (leagueSubscriptionModel.getSubscriptionType() != 3){
                                tvPlan1.setVisibility(View.VISIBLE);
                                tvPlan2.setVisibility(View.VISIBLE);
                                tvPlan3.setVisibility(View.GONE);

                                imgPlan1.setVisibility(View.GONE);
                                imgPlan2.setVisibility(View.GONE);
                                imgPlan3.setVisibility(View.VISIBLE);
                                removeSelected(1, leagueSubscriptionModel.getUserId());
                                removeSelected(2, leagueSubscriptionModel.getUserId());
                                addSelected(3, leagueSubscriptionModel.getUserId(), leagueSubscriptionModel);
                                updateTotalAMount(leagueSubscriptionModel, tvTotal);
                            }else{
                                tvPlan2.setVisibility(View.VISIBLE);
                                imgPlan2.setVisibility(View.GONE);
                                removeSelected(2, leagueSubscriptionModel.getUserId());
                                updateTotalAMount(leagueSubscriptionModel, tvTotal);
                            }

                        }

                    }
                }
            }else if (column == 2){
                if (isPlanAlreadySelected(2,leagueSubscriptionModel.getUserId()) && selectAll2){
                    view.findViewById(R.id.imgPlan2).setVisibility(View.GONE);
                    removeSelected(2, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan2.setVisibility(View.VISIBLE);
                }else{
                    if (leagueSubscriptionModel.getSubscriptionType() != 2 && leagueSubscriptionModel.getSubscriptionType() != 3){
                        view.findViewById(R.id.imgPlan2).setVisibility(View.VISIBLE);
                        tvPlan2.setVisibility(View.GONE);
                        view.findViewById(R.id.imgPlan2).setBackgroundResource(R.drawable.tick_green);
                        addSelected(2,leagueSubscriptionModel.getUserId(), leagueSubscriptionModel);
                        updateTotalAMount(leagueSubscriptionModel, tvTotal);
                        if (isPlanAlreadySelected(3, leagueSubscriptionModel.getUserId())){
                            tvPlan3.setVisibility(View.VISIBLE);
                            imgPlan3.setVisibility(View.GONE);
                            removeSelected(3, leagueSubscriptionModel.getUserId());
                            updateTotalAMount(leagueSubscriptionModel, tvTotal);
                        }else if (isPlanAlreadySelected(1, leagueSubscriptionModel.getUserId())){
                            if (leagueSubscriptionModel.getSubscriptionType() != 3){
                                tvPlan1.setVisibility(View.VISIBLE);
                                tvPlan2.setVisibility(View.VISIBLE);
                                tvPlan3.setVisibility(View.GONE);

                                imgPlan1.setVisibility(View.GONE);
                                imgPlan2.setVisibility(View.GONE);
                                imgPlan3.setVisibility(View.VISIBLE);
                                removeSelected(1, leagueSubscriptionModel.getUserId());
                                removeSelected(2, leagueSubscriptionModel.getUserId());
                                addSelected(3, leagueSubscriptionModel.getUserId(), leagueSubscriptionModel);
                                updateTotalAMount(leagueSubscriptionModel, tvTotal);
                            }else{
                                tvPlan1.setVisibility(View.VISIBLE);
                                imgPlan1.setVisibility(View.GONE);
                                removeSelected(1, leagueSubscriptionModel.getUserId());
                                updateTotalAMount(leagueSubscriptionModel, tvTotal);
                            }
                        }
                    }
                }
            }else if (column == 3 && currencySymbol.equalsIgnoreCase("INR")){
                if (isPlanAlreadySelected(3,leagueSubscriptionModel.getUserId()) && selectAll3){
                    view.findViewById(R.id.imgPlan3).setVisibility(View.GONE);
                    removeSelected(3, leagueSubscriptionModel.getUserId());
                    updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    tvPlan3.setVisibility(View.VISIBLE);
                }else{
                    if (leagueSubscriptionModel.getSubscriptionType() != 3){
                        view.findViewById(R.id.imgPlan3).setVisibility(View.VISIBLE);
                        tvPlan3.setVisibility(View.GONE);
                        view.findViewById(R.id.imgPlan3).setBackgroundResource(R.drawable.tick_green);
                        addSelected(3,leagueSubscriptionModel.getUserId(), leagueSubscriptionModel);
                        updateTotalAMount(leagueSubscriptionModel, tvTotal);

                        if (leagueSubscriptionModel.getSubscriptionType() != 1){
                            tvPlan1.setVisibility(View.VISIBLE);
                            imgPlan1.setVisibility(View.GONE);
                        }
                        if (leagueSubscriptionModel.getSubscriptionType() != 2){
                            tvPlan2.setVisibility(View.VISIBLE);
                            imgPlan2.setVisibility(View.GONE);
                        }

                        removeSelected(1, leagueSubscriptionModel.getUserId());
                        removeSelected(2, leagueSubscriptionModel.getUserId());
                        updateTotalAMount(leagueSubscriptionModel, tvTotal);
                    }
                }
            }
            i++;
        }
        if (column == 1){
            selectAll1 = !selectAll1;
        }else if (column == 2){
            selectAll2 = !selectAll2;
        }else if (column == 3){
            selectAll3 = !selectAll3;
        }
        updatePlanSelectedTotalAMount();
        Log.e("sizes","plan1->"+plan1Selected.size()+", plan2->"+plan2Selected.size()+", plan3->"+plan3Selected.size());
    }

    private void updateTotalAMount(LeagueSubscriptionModel leagueSubscriptionModel, TextView textView) {
        double amt = 0;
        if (isPlanAlreadySelected(1, leagueSubscriptionModel.getUserId())){

            amt = amt+ getSelectedPlanAmount(leagueSubscriptionModel, 1);
        }
        if (isPlanAlreadySelected(2, leagueSubscriptionModel.getUserId())){
            amt = amt+ getSelectedPlanAmount(leagueSubscriptionModel, 2);
        }
        if (isPlanAlreadySelected(3, leagueSubscriptionModel.getUserId())){
            amt = amt+ getSelectedPlanAmount(leagueSubscriptionModel, 3);
        }
        textView.setText(currencySymbol+" "+amt);
    }

    private double getSelectedPlanAmount(LeagueSubscriptionModel leagueSubscriptionModel, int plan){
        double selectPlanAmount = 0;
        if (plan == 1){
            if (currencySymbol.equalsIgnoreCase("USD")){
                selectPlanAmount = leagueSubscriptionModel.getPrizePackageAmount_USD();
            }else if (currencySymbol.equalsIgnoreCase("GBP")){
                selectPlanAmount = leagueSubscriptionModel.getPrizePackageAmount_GBP();
            }else if (currencySymbol.equalsIgnoreCase("INR")){
                selectPlanAmount = leagueSubscriptionModel.getPrizePackageAmount();
            }
        }else if (plan == 2){
            if (currencySymbol.equalsIgnoreCase("USD")){
                selectPlanAmount = leagueSubscriptionModel.getLivePackageAmount_USD();
            }else if (currencySymbol.equalsIgnoreCase("GBP")){
                selectPlanAmount = leagueSubscriptionModel.getLivePackageAmount_GBP();
            }else if (currencySymbol.equalsIgnoreCase("INR")){
                selectPlanAmount = leagueSubscriptionModel.getLivePackageAmount();
            }
        }else if (plan == 3){
            if (currencySymbol.equalsIgnoreCase("USD")){
                selectPlanAmount = leagueSubscriptionModel.getFullPackageAmount_USD();
            }else if (currencySymbol.equalsIgnoreCase("GBP")){
                selectPlanAmount = leagueSubscriptionModel.getFullPackageAmount_GBP();
            }else if (currencySymbol.equalsIgnoreCase("INR")){
                selectPlanAmount = leagueSubscriptionModel.getFullPackageAmount();
            }
        }

        return selectPlanAmount;
    }

    private void updatePlanSelectedTotalAMount() {
        int i = 0;
        NumberFormat formatter = new DecimalFormat("#0.00");
        double amtTotal = 0;
        double amt1Total = 0;
        double amt2Total = 0;
        double amt3Total = 0;

        double count1Total = 0;
        double count2Total = 0;
        double count3Total = 0;

        for (LeagueSubscriptionModel leagueSubscriptionModel : list){
            if (isPlanAlreadySelected(1, leagueSubscriptionModel.getUserId())){
                amtTotal = amtTotal + getSelectedPlanAmount(leagueSubscriptionModel, 1);
                amt1Total = amt1Total + getSelectedPlanAmount(leagueSubscriptionModel, 1);
                amt1Total = Double.parseDouble(formatter.format(amt1Total));
                count1Total++;
            }
            if (isPlanAlreadySelected(2, leagueSubscriptionModel.getUserId())){
                amtTotal = amtTotal + getSelectedPlanAmount(leagueSubscriptionModel, 2);
                amt2Total = amt2Total + getSelectedPlanAmount(leagueSubscriptionModel, 2);
                amt2Total = Double.parseDouble(formatter.format(amt2Total));
                count2Total++;
            }
            if (isPlanAlreadySelected(3, leagueSubscriptionModel.getUserId())){
                amtTotal = amtTotal + getSelectedPlanAmount(leagueSubscriptionModel, 3);
                amt3Total = amt3Total + getSelectedPlanAmount(leagueSubscriptionModel, 3);
                amt3Total = Double.parseDouble(formatter.format(amt3Total));
                count3Total++;
            }
        }

        double amt1ADTotal = amt1Total;
        double amt2ADTotal = amt2Total;
        double amt3ADTotal = amt3Total;

        double tot = count1Total + count2Total + count3Total;
        binding.planSelectedTotalAmount.setText(""+tot);
        binding.plan1SelectedAmount.setText(""+count1Total);
        binding.plan2SelectedAmount.setText(""+count2Total);
        binding.plan3SelectedAmount.setText(""+count3Total);

        //Discounted amount update
        amtTotal = Double.parseDouble(formatter.format(amtTotal));
        binding.planSelectedTotalAmountBD.setText(currencySymbol+" "+amtTotal);
        binding.plan1SelectedAmountBD.setText(currencySymbol+" "+amt1Total);
        binding.plan2SelectedAmountBD.setText(currencySymbol+" "+amt2Total);
        binding.plan3SelectedAmountBD.setText(currencySymbol+" "+amt3Total);

        int selectedPlansSize = plan1Selected.size() + plan3Selected.size() + plan2Selected.size();
        if(selectedPlansSize < 5){
            amount = amtTotal;
        }else if (selectedPlansSize >= 5 && selectedPlansSize < 10){
            double per = 100 * 0.05;
            double perAmount = per/100 * amtTotal;
            per = amtTotal - perAmount;
            amount = per;

            double per1 = 100 * 0.05;
            double perAmount1 = per1/100 * amt1Total;
            amt1ADTotal = amt1ADTotal - perAmount1;
            amt1ADTotal = Double.parseDouble(formatter.format(amt1ADTotal));

            double perAmount2 = per1/100 * amt2Total;
            amt2ADTotal = amt2ADTotal - perAmount2;
            amt2ADTotal = Double.parseDouble(formatter.format(amt2ADTotal));

            double perAmount3 = per1/100 * amt3Total;
            amt3ADTotal = amt3ADTotal - perAmount3;
            amt3ADTotal = Double.parseDouble(formatter.format(amt3ADTotal));


        }else if (selectedPlansSize >= 10 && selectedPlansSize < 15){
            double per = 100 * 0.10;
            double perAmount = per/100 * amtTotal;
            per = amtTotal - perAmount;
            amount = per;

            double per1 = 100 * 0.10;
            double perAmount1 = per1/100 * amt1Total;
            amt1ADTotal = amt1ADTotal - perAmount1;
            amt1ADTotal = Double.parseDouble(formatter.format(amt1ADTotal));

            double perAmount2 = per1/100 * amt2Total;
            amt2ADTotal = amt2ADTotal - perAmount2;
            amt2ADTotal = Double.parseDouble(formatter.format(amt2ADTotal));

            double perAmount3 = per1/100 * amt3Total;
            amt3ADTotal = amt3ADTotal - perAmount3;
            amt3ADTotal = Double.parseDouble(formatter.format(amt3ADTotal));

        }else if (selectedPlansSize >= 15){
            double per = 100 * 0.15;
            double perAmount = per/100 * amtTotal;
            per = amtTotal - perAmount;
            amount = per;

            double per1 = 100 * 0.10;
            double perAmount1 = per1/100 * amt1Total;
            amt1ADTotal = amt1ADTotal - perAmount1;
            amt1ADTotal = Double.parseDouble(formatter.format(amt1ADTotal));

            double perAmount2 = per1/100 * amt2Total;
            amt2ADTotal = amt2ADTotal - perAmount2;
            amt2ADTotal = Double.parseDouble(formatter.format(amt2ADTotal));

            double perAmount3 = per1/100 * amt3Total;
            amt3ADTotal = amt3ADTotal - perAmount3;
            amt3ADTotal = Double.parseDouble(formatter.format(amt3ADTotal));
        }

        binding.plan1SelectedAmountAD.setText(currencySymbol+" "+amt1ADTotal);
        binding.plan2SelectedAmountAD.setText(currencySymbol+" "+amt2ADTotal);
        binding.plan3SelectedAmountAD.setText(currencySymbol+" "+amt3ADTotal);

        amount = Double.parseDouble(formatter.format(amount));
        binding.planSelectedTotalAmountAD.setText(currencySymbol+" "+amount);
    }

    private boolean isPlanAlreadySelected(int plan, int userId){
        boolean result = false;
        if (plan == 1){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan1Selected){
                if (leagueSubscriptionModel.getUserId() == userId){
                    result = true;
                }
            }
        }else if (plan == 2){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan2Selected){
                if (leagueSubscriptionModel.getUserId() == userId){
                    result = true;
                }
            }
        }else if (plan == 3){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan3Selected){
                if (leagueSubscriptionModel.getUserId() == userId){
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean removeSelected(int plan, int userId){
        boolean result = false;
        List<LeagueSubscriptionModel> temp = new ArrayList<>();
        if (plan == 1){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan1Selected){
                if (leagueSubscriptionModel.getUserId() != userId){
                    temp.add(leagueSubscriptionModel);
                }
            }
            plan1Selected.clear();
            plan1Selected.addAll(temp);
        }else if (plan == 2){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan2Selected){
                if (leagueSubscriptionModel.getUserId() != userId){
                    temp.add(leagueSubscriptionModel);
                }
            }
            plan2Selected.clear();
            plan2Selected.addAll(temp);
        }else if (plan == 3){
            for (LeagueSubscriptionModel leagueSubscriptionModel : plan3Selected){
                if (leagueSubscriptionModel.getUserId() != userId){
                    temp.add(leagueSubscriptionModel);
                }
            }
            plan3Selected.clear();
            plan3Selected.addAll(temp);
        }
        return result;
    }

    private boolean addSelected(int plan, int userId, LeagueSubscriptionModel planToAdd){
        boolean result = false;
        List<LeagueSubscriptionModel> temp = new ArrayList<>();
        if (plan == 1 && !isPlanAlreadySelected(plan, userId)){
            plan1Selected.add(planToAdd);
        }else if (plan == 2 && !isPlanAlreadySelected(plan, userId)){
            plan2Selected.add(planToAdd);
        }else if (plan == 3 && !isPlanAlreadySelected(plan, userId)){
            plan3Selected.add(planToAdd);
        }
        return result;
    }

    private List<LeagueSubscriptionPost> getSelectedSubscriptions(){

        List<LeagueSubscriptionPost> jsonArray = new ArrayList<>();
        for (LeagueSubscriptionModel leagueSubscriptionModel : list){
            if (isPlanAlreadySelected(1, leagueSubscriptionModel.getUserId())){
                LeagueSubscriptionPost jsonObject = new LeagueSubscriptionPost();
                jsonObject.setLeagueId( lid);
                jsonObject.setUserId(leagueSubscriptionModel.getUserId());
                jsonObject.setLoggedInUserId( sharedPrefManager.getCustomer_Id());
                jsonObject.setSubscriptionType(1);
                jsonObject.setAmount(getSelectedPlanAmount(leagueSubscriptionModel, 2));
                jsonObject.setDiscountTotal(amount);
                jsonObject.setReceipt(receiptId);
                jsonObject.setCurrency(currencySymbol);

                jsonArray.add(jsonObject);

            }
            if (isPlanAlreadySelected(2, leagueSubscriptionModel.getUserId())){
                LeagueSubscriptionPost jsonObject = new LeagueSubscriptionPost();
                jsonObject.setLeagueId(lid);
                jsonObject.setUserId(leagueSubscriptionModel.getUserId());
                jsonObject.setLoggedInUserId( sharedPrefManager.getCustomer_Id());
                jsonObject.setSubscriptionType(2);
                jsonObject.setAmount(getSelectedPlanAmount(leagueSubscriptionModel, 1));
                jsonObject.setDiscountTotal(amount);
                jsonObject.setReceipt(receiptId);
                jsonObject.setCurrency(currencySymbol);

                jsonArray.add(jsonObject);
            }
            if (isPlanAlreadySelected(3, leagueSubscriptionModel.getUserId())){
                LeagueSubscriptionPost jsonObject = new LeagueSubscriptionPost();

                jsonObject.setLeagueId(lid);
                jsonObject.setUserId(leagueSubscriptionModel.getUserId());
                jsonObject.setLoggedInUserId(sharedPrefManager.getCustomer_Id());
                jsonObject.setSubscriptionType(3);
                jsonObject.setAmount(getSelectedPlanAmount(leagueSubscriptionModel, 2));
                jsonObject.setDiscountTotal(amount);
                jsonObject.setReceipt(receiptId);
                jsonObject.setCurrency(currencySymbol);

                jsonArray.add(jsonObject);
            }
        }

        return jsonArray;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void OnClicked(LeagueSubscriptionModel leagueTeamModel, int plan, TextView tvTotal, ImageView imageView, TextView tvPlan, ItemLeagueSubscriptionTeamBinding binding) {
        if (leagueTeamModel.getSubscriptionType() != plan && leagueTeamModel.getSubscriptionType() != 3){
            if(isPlanAlreadySelected(plan, leagueTeamModel.getUserId())){
                removeSelected(plan, leagueTeamModel.getUserId());
                imageView.setVisibility(View.GONE);
                tvPlan.setVisibility(View.VISIBLE);
            }else {

                if (plan == 3 || plan == 1){
                    if (currencySymbol.equalsIgnoreCase("INR")){
                        addSelected(plan,leagueTeamModel.getUserId(), leagueTeamModel);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setBackgroundResource(R.drawable.tick_green);
                        tvPlan.setVisibility(View.GONE);
                    }
                }else{
                    addSelected(plan,leagueTeamModel.getUserId(), leagueTeamModel);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setBackgroundResource(R.drawable.tick_green);
                    tvPlan.setVisibility(View.GONE);
                }


                if (plan == 1){
                    if (isPlanAlreadySelected(3, leagueTeamModel.getUserId())){
                        binding.tvPlan3.setVisibility(View.VISIBLE);
                        binding.imgPlan3.setVisibility(View.GONE);
                        removeSelected(3, leagueTeamModel.getUserId());
                        updateTotalAMount(leagueTeamModel, tvTotal);
                    }else if (isPlanAlreadySelected(2, leagueTeamModel.getUserId())){
                        if (leagueTeamModel.getSubscriptionType() != 3){
                            removeSelected(1, leagueTeamModel.getUserId());
                            removeSelected(2, leagueTeamModel.getUserId());

                            binding.tvPlan1.setVisibility(View.VISIBLE);
                            binding.tvPlan2.setVisibility(View.VISIBLE);
                            binding.imgPlan1.setVisibility(View.GONE);
                            binding.imgPlan2.setVisibility(View.GONE);

                            binding.imgPlan3.setVisibility(View.VISIBLE);
                            binding.imgPlan3.setBackgroundResource(R.drawable.tick_green);
                            binding.tvPlan3.setVisibility(View.GONE);
                            addSelected(3,leagueTeamModel.getUserId(), leagueTeamModel);
                        }else {
                            removeSelected(2, leagueTeamModel.getUserId());
                            binding.tvPlan2.setVisibility(View.VISIBLE);
                            binding.imgPlan2.setVisibility(View.GONE);
                        }
                    }
                }

                if (plan == 2){
                    if (isPlanAlreadySelected(3, leagueTeamModel.getUserId())){
                        binding.tvPlan3.setVisibility(View.VISIBLE);
                        binding.imgPlan3.setVisibility(View.GONE);
                        removeSelected(3, leagueTeamModel.getUserId());
                        updateTotalAMount(leagueTeamModel, tvTotal);
                    }else if (isPlanAlreadySelected(1, leagueTeamModel.getUserId())){
                        if (leagueTeamModel.getSubscriptionType() != 3){
                            removeSelected(1, leagueTeamModel.getUserId());
                            removeSelected(2, leagueTeamModel.getUserId());

                            binding.tvPlan1.setVisibility(View.VISIBLE);
                            binding.tvPlan2.setVisibility(View.VISIBLE);
                            binding.imgPlan1.setVisibility(View.GONE);
                            binding.imgPlan2.setVisibility(View.GONE);

                            binding.imgPlan3.setVisibility(View.VISIBLE);
                            binding.imgPlan3.setBackgroundResource(R.drawable.tick_green);
                            binding.tvPlan3.setVisibility(View.GONE);
                            addSelected(3,leagueTeamModel.getUserId(), leagueTeamModel);
                        }else{
                            removeSelected(1, leagueTeamModel.getUserId());
                            binding.tvPlan1.setVisibility(View.VISIBLE);
                            binding.imgPlan1.setVisibility(View.GONE);
                        }
                    }
                }

                if (plan == 3 && currencySymbol.equalsIgnoreCase("INR")){
                    if (leagueTeamModel.getSubscriptionType() != 1){
                        binding.tvPlan1.setVisibility(View.VISIBLE);
//                        binding.tvPlan1.setText("-");
                        binding.imgPlan1.setVisibility(View.GONE);
                    }
                    if (leagueTeamModel.getSubscriptionType() != 2){
                        binding.tvPlan2.setVisibility(View.VISIBLE);
//                        binding.tvPlan2.setText("-");
                        binding.imgPlan2.setVisibility(View.GONE);
                    }
                    removeSelected(1, leagueTeamModel.getUserId());
                    removeSelected(2, leagueTeamModel.getUserId());
                }
            }
            updatePlanSelectedTotalAMount();
            updateTotalAMount(leagueTeamModel, tvTotal);
        }
    }

    //    Razorpay
    public void initiateOrder(double amount){

        receiptId = "order_rcptid_"+getID();
        Map<String, Object> map = new HashMap<>();
        map.put("amount", amount * 100);
        map.put("currency", currencySymbol);
        map.put("receipt", receiptId);
        Log.d("rtyrtytfghgh","initiateOrder ::: Constants.client_id :: "+Constants.client_id);
        RetrofitRazorPayApiService retrofitRazorPayApiService =
                ServiceGenerator.createService(RetrofitRazorPayApiService.class, Constants.client_id, Constants.client_secret);
        Call<JsonObject> call = retrofitRazorPayApiService.initiateOrder(Constants.str_HEADER,map);
//        RetrofitAipService retrofitAipService = RetrofitClientRazorpay.getInstance().create(RetrofitAipService.class);
//        Call<JsonObject> call = retrofitAipService.initiateOrder(Constants.str_HEADER,"rzp_test_lTgA607AThK4CZ : 018mdJ7azhGvj7rBimxWmYx8",map);
        disablePayButtons();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        orderId = jsonObject.getString("id");
                        startPayment(orderId,jsonObject.getInt("amount_due"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                        orderId = null;
                        enablePayButtons();
                    }
                }else{
                    enablePayButtons();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
                Log.e("error", call.toString());
                orderId = null;
                enablePayButtons();
            }
        });
    }

    private void disablePayButtons() {
        binding.btnPay.setEnabled(false);
        binding.btnPay.setClickable(false);
    }

    private void enablePayButtons() {
        binding.btnPay.setEnabled(true);
        binding.btnPay.setClickable(true);
    }

    public void startPayment(String id, int amount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        SharedPrefManager sharedPrefManager = new SharedPrefManager(activity);

        final Checkout co = new Checkout();
        Log.d("rtyrtytfghgh","startPayment1 ::: Constants.client_id :: "+Constants.client_id);
        co.setKeyID(Constants.client_id);
//        int image = R.drawable.fanzania_razorpay_logo; // Can be any drawable
//        co.setImage(image);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Fanzania");
            options.put("description", Desc);
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", currencySymbol);
            options.put("amount", amount); // 1 Rs*100
            options.put("order_id", id);//from response of step 3.

            JSONObject preFill = new JSONObject();
            preFill.put("email", sharedPrefManager.getCustomer_Email());
            preFill.put("contact", sharedPrefManager.getCustomer_Phone());

            options.put("prefill", preFill);

            co.open(activity, options);
            disablePayButtons();
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
            enablePayButtons();
        }
    }

    public int getID() {
        // create a string of all characters
        String alphabet = "0123456789";

        // create random string builder
        String sb = "";

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 5;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb += randomChar;
        }

        int randomString = Integer.parseInt(sb);
        return randomString;
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        enablePayButtons();
        try {

            Toast.makeText(this, "Payment Successful " + razorpayPaymentID + orderId, Toast.LENGTH_SHORT).show();
            ////send this status to the server -- Success
            orderId = null;
            if (getSelectedSubscriptions().size() > 0){
                disablePayButtons();
                binding.inRVList.pBar.setVisibility(View.VISIBLE);
                presenter.updateSubscriptions(getSelectedSubscriptions());
            }

        } catch (Exception e) {
            Log.e("Payment", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        enablePayButtons();
        //presenter.LeagueSubscriptions(lid,tid);
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            ////send this status to the server -- Failed
            orderId = null;

        } catch (Exception e) {
            Log.e("Payment", "Exception in onPaymentError", e);
        }
    }
}
