package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

public class AddMoneyResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("statusMessage")
    private String statusMessage;

    @SerializedName("httpStatusCode")
    private int httpStatusCode;

    public String getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
