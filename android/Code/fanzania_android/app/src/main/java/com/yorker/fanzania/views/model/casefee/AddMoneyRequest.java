package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

public class AddMoneyRequest {
    @SerializedName("UserId")
    private int userId;

    @SerializedName("cf_order_id")
    private String cfOrderId;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("customer_email")
    private String customerEmail;

    @SerializedName("customer_phone")
    private String customerPhone;

    @SerializedName("order_amount")
    private double orderAmount;

    @SerializedName("order_currency")
    private String orderCurrency;

    @SerializedName("payment_methods")
    private String paymentMethods;

    @SerializedName("order_note")
    private String orderNote;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("payment_session_id")
    private String paymentSessionId;

    public AddMoneyRequest(int userId, String cfOrderId, String customerName, String customerEmail,
                           String customerPhone, double orderAmount, String orderCurrency,
                           String paymentMethods, String orderNote, String orderStatus, String paymentSessionId) {
        this.userId = userId;
        this.cfOrderId = cfOrderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.orderAmount = orderAmount;
        this.orderCurrency = orderCurrency;
        this.paymentMethods = paymentMethods;
        this.orderNote = orderNote;
        this.orderStatus = orderStatus;
        this.paymentSessionId = paymentSessionId;
    }
}
