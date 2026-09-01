package com.yorker.fanzania.views.screens.tournament.subscription;

import static com.yorker.fanzania.constants.Constants.isPaid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment;
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.helper.sharedpreferences.SharedPrefManager;
import com.yorker.fanzania.restservices.RetrofitAipService;
import com.yorker.fanzania.restservices.RetrofitClient;
import com.yorker.fanzania.restservices.casefree.ApiClient;
import com.yorker.fanzania.restservices.casefree.ApiService;
import com.yorker.fanzania.views.model.casefee.AddMoneyRequest;
import com.yorker.fanzania.views.model.casefee.AddMoneyResponse;
import com.yorker.fanzania.views.model.casefee.ApiResponse;
import com.yorker.fanzania.views.model.casefee.OrderRequest;
import com.yorker.fanzania.views.model.casefee.OrderResponse;
import com.yorker.fanzania.views.model.casefee.PaymentGatewayRequest;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseFreeAPIActivity extends AppCompatActivity implements CFCheckoutResponseCallback {
    String orderID;
    String paymentSessionID;
    CFSession.Environment cfEnvironment = CFSession.Environment.SANDBOX;
    double price = 0.0;
    String currencySymbol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_case_free_apiactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        price = getIntent().getDoubleExtra("amount", 0.0);
        currencySymbol = getIntent().getStringExtra("currencySymbol");

        orderID = generateOrderID();

        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            throw new RuntimeException(e);
        }
        // Initialize Retrofit and API Service
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create the request body
        OrderRequest.CustomerDetails customerDetails = new OrderRequest.CustomerDetails(
                "customer_1736187464007",
                "vdvdvd@gmail.com",
                "2656556565"
        );

        OrderRequest orderRequest = new OrderRequest(
                orderID,
                price,
                currencySymbol,
                customerDetails
        );

        // Make the API call
        Call<ApiResponse> call = apiService.createOrder("application/json", Constants.PGClientId, Constants.PGClientSecret, "2023-08-01", orderRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("  Response", "Order IsD: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    paymentSessionID = response.body().getPaymentSessionId();
                    doDropCheckoutPayment();
                    Log.d("API Response", "Order ID: " + response.body().getCfOrderId());
                } else {
                    Log.e("API Error", "Response Code: " + response.code() + " dfdhfd " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });

    }

    @Override
    public void onPaymentVerify(String orderID) {
        Log.e("onPaymentVerify", "verifyPayment triggered " + orderID);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Replace with the order ID you want to fetch

        Call<OrderResponse> call = apiService.getOrderDetails(Constants.PGClientId, Constants.PGClientSecret, "2023-08-01", orderID);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse order = response.body();
                    Log.d("sdfdgfhgfhgfhfghfhff", "Order ID: " + order.getCfOrderId());
                    Log.d("sdfdgfhgfhgfhfghfhff", "Order Status: " + order.getOrderStatus());
//                    Toast.makeText(CaseFreeAPIActivity.this, order.getOrderStatus(), Toast.LENGTH_SHORT).show();
                    Log.d("sdfdgfhgfhgfhfghfhff", "Customer Email: " + order.getCustomerDetails().getCustomerEmail());
                    Log.d("sdfdgfhgfhgfhfghfhff", "Customer Phone: " + order.getCustomerDetails().getCustomerPhone());
                    RetrofitAipService retrofitAipService = RetrofitClient.getInstance().create(RetrofitAipService.class);
                    String userID = SharedPrefManager.getInstance(CaseFreeAPIActivity.this).getCustomer_Id();
                    int userIds = 0;
                    if (userID != null) {
                        userIds = Integer.parseInt(userID);
                    }
                    AddMoneyRequest request = new AddMoneyRequest(
                            userIds, orderID, SharedPrefManager.getInstance(CaseFreeAPIActivity.this).getCustomerName(), SharedPrefManager.getInstance(CaseFreeAPIActivity.this).getCustomer_Email(),
                            SharedPrefManager.getInstance(CaseFreeAPIActivity.this).getCustomer_Phone(), price, currencySymbol, "CreditCard",
                            "Test Payment", "PAID", paymentSessionID
                    );

                    retrofitAipService.addMoney(Constants.str_HEADER,Constants.RETROFIT_HEADER_TYPE,request).enqueue(new Callback<AddMoneyResponse>() {
                        @Override
                        public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.d("API_RESPONSE", "Status: " + response.body().getStatus());
                                Log.d("API_RESPONSE", "Message: " + response.body().getStatusMessage());
                                isPaid = true;
                                onBackPressed();
                            } else {
                                Log.e("API_ERROR", "Response failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                            Log.e("API_ERROR", "API call failed: " + t.getMessage());
                        }
                    });


                } else {
                    Log.d("sdfdgfhgfhgfhfghfhff", "Failed to fetch details: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
//        Intent intent = new Intent();
//        intent.putExtra("orderID", orderID);
//        intent.putExtra("result", "VerifyPayment");
//        setResult(RESULT_OK, intent);
//        finish();
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        Log.e("onPaymentFailure " + orderID, cfErrorResponse.getMessage());
        Intent intent = new Intent();
        intent.putExtra("orderID", orderID);
        intent.putExtra("result", "PaymentFailure");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void doDropCheckoutPayment() {
        if (orderID.equals("ORDER_ID") || TextUtils.isEmpty(orderID)) {
            Toast.makeText(this, "Please set the orderId (DropCheckoutActivity.class,  line: 21)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (paymentSessionID.equals("PAYMENT_SESSION_ID") || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(this, "Please set the payment_session_id (webCheckoutActivity.class,  line: 22)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
            CFWebCheckoutTheme cfTheme = new CFWebCheckoutTheme.CFWebCheckoutThemeBuilder()
                    .setNavigationBarBackgroundColor("#FFFFFF")
                    .setNavigationBarTextColor("#000000")
                    .build();
            CFWebCheckoutPayment cfWebCheckoutPayment = new CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    .setCFWebCheckoutUITheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(CaseFreeAPIActivity.this, cfWebCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    private String generateOrderID() {
        // Generate a unique order ID (e.g., using UUID)
        return UUID.randomUUID().toString();
    }
}