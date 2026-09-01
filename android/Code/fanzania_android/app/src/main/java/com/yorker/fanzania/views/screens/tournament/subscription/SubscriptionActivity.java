package com.yorker.fanzania.views.screens.tournament.subscription;

import static com.yorker.fanzania.constants.Constants.PGClientId;
import static com.yorker.fanzania.constants.Constants.PGClientSecret;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.customviews.CustomToast;
import com.yorker.fanzania.databinding.ActivitySubscriptionBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.dialog.PaymentDialog;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.restservices.RetrofitRazorPayApiService;
import com.yorker.fanzania.restservices.ServiceGenerator;
import com.yorker.fanzania.views.model.casefee.CashfreeResponse;
import com.yorker.fanzania.views.model.casefee.PaymentGateway;
import com.yorker.fanzania.views.model.casefee.PaymentGatewayRequest;
import com.yorker.fanzania.views.screens.auth.registration.adapter.CustomSpinnerAdapter;
import com.yorker.fanzania.views.screens.auth.registration.model.CountryListModel;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletResponse;
import com.yorker.fanzania.views.screens.tournament.wallet.WalletRewardsResponse;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionActivity extends BaseActivity<SubscriptionPresenter> implements SubscriptionPresenter.IMainView, PaymentResultListener {

    private static final String TAG = "SubscriptionActivity";
    private SubscriptionPresenter presenter;
    private ActivitySubscriptionBinding binding;
    private WalletResponse obj;
    private List<WalletRewardsResponse> rList;
    public static String orderId = null;
    public static int selectedTier = 0;
    double amount = 0.0;
    String Details = null, Desc = null, receiptId = null;
    private LinkedList<CountryListModel> countrylist;
    private String strCountry;
    private String currencySymbol = "INR";
    JSONObject jsonObject1;

    @Override
    protected SubscriptionPresenter onCreatePresenter() {
        presenter = new SubscriptionPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, SubscriptionPresenter presenter) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);
        initViews();
        getData();
        getCountrySpinner();
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

                if (!countrylist.get(position).getCountryId().equals("0") && jsonObject1 != null) {
                    strCountry = countrylist.get(position).getCountryId();
                    binding.edtCountry.setText(countrylist.get(position).getCountry());
                    binding.btnPay49.setClickable(true);
                    binding.btnPay49.setEnabled(true);
                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_red));

                    binding.btnPay149.setClickable(true);
                    binding.btnPay149.setEnabled(true);
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_red));

                    String SubTier1 = null;
                    String SubTier2 = null;
                    String SubTier3 = null;

                    if (countrylist.get(position).getCountryId().equals("1")){
                        currencySymbol = "INR";
                        try {
                            SubTier1 = jsonObject1.getString("PrizePackageAmount");
                            SubTier2 = jsonObject1.getString("LivePackageAmount");
                            SubTier3 = jsonObject1.getString("FullPackageAmount");

                            binding.t1.setText(currencySymbol+" "+SubTier1);
                            binding.t2.setText(currencySymbol+" "+SubTier2);
                            binding.t3.setText(currencySymbol+" "+SubTier3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (countrylist.get(position).getCountryId().equals("2")){
                        currencySymbol = "GBP";
                        binding.btnPay49.setClickable(false);
                        binding.btnPay49.setEnabled(false);
                        binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));

                        binding.btnPay149.setClickable(false);
                        binding.btnPay149.setEnabled(false);
                        binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                        try {
                            SubTier1 = "NA";
//                            SubTier1 = jsonObject1.getString("PrizePackageAmount_GBP");
                            SubTier2 = jsonObject1.getString("LivePackageAmount_GBP");
                            SubTier3 = "NA";
//                            SubTier3 = jsonObject1.getString("FullPackageAmount_GBP");

                            binding.t1.setText(SubTier1);
                            binding.t2.setText(currencySymbol+" "+SubTier2);
                            binding.t3.setText(SubTier3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (countrylist.get(position).getCountryId().equals("3")){
                        currencySymbol = "USD";
                        binding.btnPay49.setClickable(false);
                        binding.btnPay49.setEnabled(false);
                        binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));

                        binding.btnPay149.setClickable(false);
                        binding.btnPay149.setEnabled(false);
                        binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                        try {
                            SubTier1 = "NA";
//                            SubTier1 = jsonObject1.getString("PrizePackageAmount_USD");
                            SubTier2 = jsonObject1.getString("LivePackageAmount_USD");
                            SubTier3 = jsonObject1.getString("FullPackageAmount_USD");
                            SubTier3 = "NA";
//                            SubTier3 = jsonObject1.getString("FullPackageAmount_USD");

                            binding.t1.setText(SubTier1);
                            binding.t2.setText(currencySymbol+" "+SubTier2);
                            binding.t3.setText(SubTier3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    binding.btnPay49.setTag(SubTier1 + " SubTier1");
                    binding.btnPay99.setTag(SubTier2 + " SubTier2");
                    binding.btnPay149.setTag(SubTier3 + " SubTier3");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getData() {
        if (CheckInternetConnection()){
            binding.pBar.setVisibility(View.VISIBLE);
            presenter.fetchDetails();
        }
        else
            new NoNetworkDialog(this, this, Constants.APICALL_1);
    }

    private void updateData() {
        if (CheckInternetConnection()){
            binding.pBar.setVisibility(View.VISIBLE);
            presenter.updateDetails(selectedTier,receiptId, amount, currencySymbol);
        }
        else
            new NoNetworkDialog(this, this, Constants.APICALL_2);
    }

    private void initViews() {
        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_titlesubscription));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.tag.setText(getSpannedText(getString(R.string.text_sub_tag)));
//        getWalletData();
//        getTotalClaims();
//        getTotalRewards();
//
//        binding.tvBreakdown.setOnClickListener(v->{
//            new WalletRewardDialog(this,rList);
//        });

//        disablePayButtons();

        binding.btnPay49.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    String tag = binding.btnPay49.getTag().toString();
//                    int price = Integer.parseInt(tag);
                    double aa = Double.parseDouble(tag.split(" ")[0]);
                    Desc = "PRIZE ELIGIBILITY Package | "+currencySymbol+" "+aa+"\n" +
                            "Gives you eligibility to win prizes in both Tournament Mode and Match Contests. Applicable for "+Details+" only.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "Excludes Live Scoring.";

                                initiateOrder(aa, "PRIZE ELIGIBILITY");

//razorpay



                    selectedTier = Integer.parseInt(tag.split(" ")[1].substring(tag.split(" ")[1].length()-1));
                    amount = aa;
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });

        binding.btnPay99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String tag = binding.btnPay99.getTag().toString();
//                    int price = Integer.parseInt(tag);
                    double aa = Double.parseDouble(tag.split(" ")[0]);
                    Desc = "LIVE SCORE Package | "+currencySymbol+" "+aa+"\n" +
                            "Gives you access to Live Score feature in both Tournament Mode and Match Contests. Applicable for "+Details+" only. Prize Eligibility Excluded.";


                                initiateOrder(aa, "LIVE SCORE");

                    selectedTier = Integer.parseInt(tag.split(" ")[1].substring(tag.split(" ")[1].length()-1));
                    amount = aa;
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });

        binding.btnPay149.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String tag = binding.btnPay149.getTag().toString();
                    double aa = Double.parseDouble(tag.split(" ")[0]);
                    Desc = "PRIZE ELIGIBILITY and LIVE SCORE Package | "+currencySymbol+" "+aa+"\n" +
                            "Gives you access to live score fetaure and eligibility to win prizes in both Tournament Mode and Match Contests. Applicable for "+Details+" only.";


                                initiateOrder(aa, "PRIZE ELIGIBILITY & LIVE SCORE");

                    selectedTier = Integer.parseInt(tag.split(" ")[1].substring(tag.split(" ")[1].length()-1));
                    amount = aa;
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });

    }

    private Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    @Override
    public void RetryResponse(String type) {
        switch (type) {
            case Constants.APICALL_1:
                presenter.fetchDetails();
                break;

            case Constants.APICALL_2:
                updateData();
                break;

        }
    }

    //    Razorpay
    public void initiateOrder(double amount, String packageName){

        receiptId = "order_rcptid_"+getID();
        Map<String, Object> map = new HashMap<>();
        map.put("amount", amount * 100);
        map.put("currency", currencySymbol);
        map.put("receipt", receiptId);
        Log.d("rtyrtytfghgh","initiateOrder1 ::: Constants.client_id :: "+Constants.client_id);
        RetrofitRazorPayApiService retrofitRazorPayApiService =
                ServiceGenerator.createService(RetrofitRazorPayApiService.class, Constants.client_id, Constants.client_secret);
        Call<JsonObject> call = retrofitRazorPayApiService.initiateOrder(Constants.str_HEADER,map);
//        RetrofitAipService retrofitAipService = RetrofitClientRazorpay.getInstance().create(RetrofitAipService.class);
//        Call<JsonObject> call = retrofitAipService.initiateOrder(Constants.str_HEADER,"rzp_test_lTgA607AThK4CZ : 018mdJ7azhGvj7rBimxWmYx8",map);
//        disablePayButtons();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        orderId = jsonObject.getString("id");
                        startPayment(orderId,jsonObject.getInt("amount_due"), packageName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        orderId = null;
                        //enablePayButtons();
                    }
                }else{
                    //enablePayButtons();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error1 " + call.toString());
                orderId = null;
//                enablePayButtons();
            }
        });
    }

    public void startPayment(String id, int amount, String packageName) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        SharedPrefManager sharedPrefManager = new SharedPrefManager(activity);

        final Checkout co = new Checkout();
        Log.d("rtyrtytfghgh","startPayment ::: Constants.client_id :: "+Constants.client_id);
        co.setKeyID(Constants.client_id);
//        int image = R.drawable.fanzania_razorpay_logo; // Can be any drawable
//        co.setImage(image);
        String title = Details+": "+packageName;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Fanzania");
            options.put("description", Desc);
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount); // 1 Rs*100
            options.put("order_id", id);//from response of step 3.

            JSONObject preFill = new JSONObject();
            preFill.put("email", sharedPrefManager.getCustomer_Email());
            preFill.put("contact", sharedPrefManager.getCustomer_Phone());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
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
        //enablePayButtons();
        try {

            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID + orderId, Toast.LENGTH_SHORT).show();
            ////send this status to the server -- Success
            orderId = null;
            if (selectedTier != 0){
                binding.pBar.setVisibility(View.VISIBLE);
                presenter.updateDetails(selectedTier, receiptId, amount, currencySymbol);
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
//        enablePayButtons();
        getData();
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            ////send this status to the server -- Failed
            orderId = null;
            selectedTier = 0;

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            selectedTier = 0;
        }
    }

    @Override
    public void OnSubscriptionDetails(JSONObject jsonObject) {
        Log.e("details",""+jsonObject);
        binding.pBar.setVisibility(View.GONE);
//        enablePayButtons();
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                jsonObject1 = jsonObject.getJSONObject(Constants.STR_DATA);
                Details = jsonObject1.getString("Details");
                String SubTierFree = jsonObject1.getString("SubTierFree");
                ///------------

                String SubTier1 = null;
                String SubTier2 = null;
                String SubTier3 = null;

                if (currencySymbol.equalsIgnoreCase("INR")){
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount");
                        SubTier2 = jsonObject1.getString("LivePackageAmount");
                        SubTier3 = jsonObject1.getString("FullPackageAmount");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (currencySymbol.equalsIgnoreCase("GBP")){
                    binding.btnPay49.setClickable(false);
                    binding.btnPay49.setEnabled(false);
                    binding.btnPay149.setClickable(false);
                    binding.btnPay149.setEnabled(false);

                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount_GBP");
                        SubTier2 = jsonObject1.getString("LivePackageAmount_GBP");
                        SubTier3 = jsonObject1.getString("FullPackageAmount_GBP");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (currencySymbol.equalsIgnoreCase("USD")){
                    binding.btnPay49.setClickable(false);
                    binding.btnPay49.setEnabled(false);
                    binding.btnPay149.setClickable(false);
                    binding.btnPay149.setEnabled(false);

                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount_USD");
                        SubTier2 = jsonObject1.getString("LivePackageAmount_USD");
                        SubTier3 = jsonObject1.getString("FullPackageAmount_USD");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ////--------
                boolean SubTierPay1 = jsonObject1.getBoolean("SubTier1PayEnable");
                boolean SubTierPay2 = jsonObject1.getBoolean("SubTier2PayEnable");
                boolean SubTierPay3 = jsonObject1.getBoolean("SubTier3PayEnable");

                Constants.client_secret = jsonObject1.getString("client_secret");
                Constants.client_id = jsonObject1.getString("client_id");
                Log.d("rtyrtytfghgh","jsonObject1 ::: Constants.client_id :: "+Constants.client_id);


                binding.freeT.setText(SubTierFree);
                binding.t1.setText(currencySymbol+" "+SubTier1);
                binding.t2.setText(currencySymbol+" "+SubTier2);
                binding.t3.setText(currencySymbol+" "+SubTier3);

                binding.btnPay49.setTag(SubTier1 + " SubTier1");
                binding.btnPay99.setTag(SubTier2 + " SubTier2");
                binding.btnPay149.setTag(SubTier3 + " SubTier3");

                binding.coverageText.setText("All packages relate to "+Details);
//                binding.coverageText.setText("All packages relate to "+Details+getString(R.string.text_coverage_detail));
//                binding.firstDesc.setText("For "+Details+getString(R.string.text_sub_title));
                binding.firstDesc.setText(getString(R.string.text_sub_title));

                int subscribedTier = jsonObject1.getInt("CurrentSubscriptionTier");
                switch (subscribedTier) {
                     case 0:
                         binding.subFree.setVisibility(View.VISIBLE);
                         binding.sub1.setVisibility(View.INVISIBLE);
                         binding.sub2.setVisibility(View.INVISIBLE);
                         binding.sub3.setVisibility(View.INVISIBLE);
                     break;

                    case 1:
                        binding.sub1.setVisibility(View.VISIBLE);
                        binding.subFree.setVisibility(View.INVISIBLE);
                        binding.sub2.setVisibility(View.INVISIBLE);
                        binding.sub3.setVisibility(View.INVISIBLE);
                        break;

                    case 2:
                        binding.sub2.setVisibility(View.VISIBLE);
                        binding.subFree.setVisibility(View.INVISIBLE);
                        binding.sub1.setVisibility(View.INVISIBLE);
                        binding.sub3.setVisibility(View.INVISIBLE);
                        break;

                    case 3:
                        binding.sub3.setVisibility(View.VISIBLE);
                        binding.subFree.setVisibility(View.INVISIBLE);
                        binding.sub1.setVisibility(View.INVISIBLE);
                        binding.sub2.setVisibility(View.INVISIBLE);
                        break;
                }

                if (SubTierPay1){
                    binding.btnPay49.setClickable(true);
                    binding.btnPay49.setEnabled(true);
                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_red));

                }else{
                    binding.btnPay49.setClickable(false);
                    binding.btnPay49.setEnabled(false);
                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                }

                if (SubTierPay2 ){
                    binding.btnPay99.setClickable(true);
                    binding.btnPay99.setEnabled(true);
                    binding.btnPay99.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                }else{
                    binding.btnPay99.setClickable(false);
                    binding.btnPay99.setEnabled(false);
                    binding.btnPay99.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                }

                if (SubTierPay3 && currencySymbol.equalsIgnoreCase("INR")){
                    binding.btnPay149.setClickable(true);
                    binding.btnPay149.setEnabled(true);
                }else{
                    binding.btnPay149.setClickable(false);
                    binding.btnPay149.setEnabled(false);
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                }

                if (currencySymbol.equalsIgnoreCase("INR")){
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount");
                        SubTier2 = jsonObject1.getString("LivePackageAmount");
                        SubTier3 = jsonObject1.getString("FullPackageAmount");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (currencySymbol.equalsIgnoreCase("GBP")){
                    binding.btnPay49.setClickable(false);
                    binding.btnPay49.setEnabled(false);
                    binding.btnPay149.setClickable(false);
                    binding.btnPay149.setEnabled(false);

                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount_GBP");
                        SubTier2 = jsonObject1.getString("LivePackageAmount_GBP");
                        SubTier3 = jsonObject1.getString("FullPackageAmount_GBP");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (currencySymbol.equalsIgnoreCase("USD")){
                    binding.btnPay49.setClickable(false);
                    binding.btnPay49.setEnabled(false);
                    binding.btnPay149.setClickable(false);
                    binding.btnPay149.setEnabled(false);

                    binding.btnPay49.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    binding.btnPay149.setBackground(getResources().getDrawable(R.drawable.round_corner_grey_live));
                    try {
                        SubTier1 = jsonObject1.getString("PrizePackageAmount_USD");
                        SubTier2 = jsonObject1.getString("LivePackageAmount_USD");
                        SubTier3 = jsonObject1.getString("FullPackageAmount_USD");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnUpdateDetails(JSONObject jsonObject) {
        binding.pBar.setVisibility(View.GONE);
        try {
            if (jsonObject.getString(Constants.str_status).equals(Constants.STR_SUCCESS)) {
                //JSONObject jsonObject1 = jsonObject.getJSONArray(Constants.STR_DATA).getJSONObject(0);
                CustomToast.getInstance(this).showSmallCustomToast(getString(R.string.text_subscriptiondetailsupdated));
                getData();
            } else
                CustomToast.getInstance(this).showSmallCustomToast(jsonObject.getString("statusMessage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("update",""+jsonObject);
    }

//    public void caseFree(double aa){
//        RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
//        PaymentGatewayRequest request = new PaymentGatewayRequest("cashfree");
//        Call<CashfreeResponse> call = retrofitAipService.fetchPaymentGatewayDetails(request);
//        call.enqueue(new Callback<CashfreeResponse>() {
//            @Override
//            public void onResponse(Call<CashfreeResponse> call, Response<CashfreeResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    for (PaymentGateway pg : response.body().getData()) {
//                        PGClientId = pg.getPGClientId();
//                        PGClientSecret = pg.getPGClientSecret();
//                    }
//                    startActivity(new Intent(SubscriptionActivity.this, CaseFreeAPIActivity.class).putExtra("amount",aa).putExtra("currencySymbol",currencySymbol));
//                } else {
//                    Log.e("API_ERROR", "Response failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CashfreeResponse> call, Throwable t) {
//                Log.e("API_ERROR", "API call failed: " + t.getMessage());
//            }
//        });
//    }

    @Override
    public void OnFailed(Boolean b) {
        binding.pBar.setVisibility(View.GONE);
    }
}
