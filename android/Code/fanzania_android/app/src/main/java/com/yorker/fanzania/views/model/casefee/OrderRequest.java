package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("order_amount")
    private double orderAmount;

    @SerializedName("order_currency")
    private String orderCurrency;

    @SerializedName("customer_details")
    private CustomerDetails customerDetails;

    // Constructor
    public OrderRequest(String orderId, double orderAmount, String orderCurrency, CustomerDetails customerDetails) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.orderCurrency = orderCurrency;
        this.customerDetails = customerDetails;
    }

    // Inner class for customer details
    public static class CustomerDetails {
        @SerializedName("customer_id")
        private String customerId;

        @SerializedName("customer_email")
        private String customerEmail;

        @SerializedName("customer_phone")
        private String customerPhone;

        // Constructor
        public CustomerDetails(String customerId, String customerEmail, String customerPhone) {
            this.customerId = customerId;
            this.customerEmail = customerEmail;
            this.customerPhone = customerPhone;
        }
    }
}