package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

    @SerializedName("cart_details")
    private Object cartDetails; // Replace Object with appropriate class if needed

    @SerializedName("cf_order_id")
    private String cfOrderId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("customer_details")
    private CustomerDetails customerDetails;

    @SerializedName("entity")
    private String entity;

    @SerializedName("order_amount")
    private double orderAmount;

    @SerializedName("order_currency")
    private String orderCurrency;

    @SerializedName("order_expiry_time")
    private String orderExpiryTime;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("order_meta")
    private OrderMeta orderMeta;

    @SerializedName("order_note")
    private Object orderNote; // Replace Object with appropriate type if needed

    @SerializedName("order_splits")
    private List<Object> orderSplits;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("order_tags")
    private Object orderTags; // Replace Object with appropriate type if needed

    @SerializedName("payment_session_id")
    private String paymentSessionId;

    @SerializedName("terminal_data")
    private Object terminalData; // Replace Object with appropriate type if needed

    // Getters and setters
    public String getCfOrderId() {
        return cfOrderId;
    }

    public void setCfOrderId(String cfOrderId) {
        this.cfOrderId = cfOrderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public String getOrderExpiryTime() {
        return orderExpiryTime;
    }

    public void setOrderExpiryTime(String orderExpiryTime) {
        this.orderExpiryTime = orderExpiryTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderMeta getOrderMeta() {
        return orderMeta;
    }

    public void setOrderMeta(OrderMeta orderMeta) {
        this.orderMeta = orderMeta;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    public void setPaymentSessionId(String paymentSessionId) {
        this.paymentSessionId = paymentSessionId;
    }

    // Inner Classes for Nested Objects
    public static class CustomerDetails {
        @SerializedName("customer_id")
        private String customerId;

        @SerializedName("customer_name")
        private String customerName;

        @SerializedName("customer_email")
        private String customerEmail;

        @SerializedName("customer_phone")
        private String customerPhone;

        @SerializedName("customer_uid")
        private String customerUid;

        // Getters and setters
        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getCustomerUid() {
            return customerUid;
        }

        public void setCustomerUid(String customerUid) {
            this.customerUid = customerUid;
        }
    }

    public static class OrderMeta {
        @SerializedName("return_url")
        private String returnUrl;

        @SerializedName("notify_url")
        private String notifyUrl;

        @SerializedName("payment_methods")
        private String paymentMethods;

        // Getters and setters
        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(String paymentMethods) {
            this.paymentMethods = paymentMethods;
        }
    }
}
