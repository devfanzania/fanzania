package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

public class PaymentGateway {
    @SerializedName("Environment")
    private String environment;

    @SerializedName("PaymentGatewayName")
    private String paymentGatewayName;

    @SerializedName("PGAuthKey")
    private String pgAuthKey;

    @SerializedName("PGClientId")
    private String pgClientId;

    @SerializedName("PGClientSecret")
    private String pgClientSecret;
    @SerializedName("URL")
    private String url;

    @SerializedName("cart_details")  // Extra field added as per request
    private String cartDetails;

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public String getPGClientId() {
        return pgClientId;
    }

    public String getPGClientSecret() {
        return pgClientSecret;
    }

    public String getCartDetails() {
        return cartDetails;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
