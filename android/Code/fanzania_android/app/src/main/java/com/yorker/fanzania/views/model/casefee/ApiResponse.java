package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("cart_details")
    private Object cartDetails;

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
    private Object orderNote;

    @SerializedName("order_splits")
    private List<Object> orderSplits;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("order_tags")
    private Object orderTags;

    @SerializedName("payment_session_id")
    private String paymentSessionId;

    @SerializedName("terminal_data")
    private Object terminalData;

    // Getters and Setters
    public Object getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(Object cartDetails) {
        this.cartDetails = cartDetails;
    }

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

    public Object getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(Object orderNote) {
        this.orderNote = orderNote;
    }

    public List<Object> getOrderSplits() {
        return orderSplits;
    }

    public void setOrderSplits(List<Object> orderSplits) {
        this.orderSplits = orderSplits;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Object getOrderTags() {
        return orderTags;
    }

    public void setOrderTags(Object orderTags) {
        this.orderTags = orderTags;
    }

    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    public void setPaymentSessionId(String paymentSessionId) {
        this.paymentSessionId = paymentSessionId;
    }

    public Object getTerminalData() {
        return terminalData;
    }

    public void setTerminalData(Object terminalData) {
        this.terminalData = terminalData;
    }

    // Nested classes
    public static class CustomerDetails {
        @SerializedName("customer_id")
        private String customerId;

        @SerializedName("customer_name")
        private Object customerName;

        @SerializedName("customer_email")
        private String customerEmail;

        @SerializedName("customer_phone")
        private String customerPhone;

        @SerializedName("customer_uid")
        private Object customerUid;

        // Getters and Setters
        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Object getCustomerName() {
            return customerName;
        }

        public void setCustomerName(Object customerName) {
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

        public Object getCustomerUid() {
            return customerUid;
        }

        public void setCustomerUid(Object customerUid) {
            this.customerUid = customerUid;
        }
    }

    public static class OrderMeta {
        @SerializedName("return_url")
        private Object returnUrl;

        @SerializedName("notify_url")
        private Object notifyUrl;

        @SerializedName("payment_methods")
        private Object paymentMethods;

        // Getters and Setters
        public Object getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(Object returnUrl) {
            this.returnUrl = returnUrl;
        }

        public Object getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(Object notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public Object getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(Object paymentMethods) {
            this.paymentMethods = paymentMethods;
        }
    }
}
