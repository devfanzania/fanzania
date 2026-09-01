package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CashfreeResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("statusMessage")
    private String statusMessage;

    @SerializedName("httpStatusCode")
    private int httpStatusCode;

    @SerializedName("data")
    private List<PaymentGateway> data;

    public List<PaymentGateway> getData() {
        return data;
    }
}
