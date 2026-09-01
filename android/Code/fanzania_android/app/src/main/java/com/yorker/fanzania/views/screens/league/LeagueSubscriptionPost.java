package com.yorker.fanzania.views.screens.league;

public class LeagueSubscriptionPost {
    String LeagueId, LoggedInUserId, receipt, Currency;

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getLeagueId() {
        return LeagueId;
    }

    public void setLeagueId(String leagueId) {
        LeagueId = leagueId;
    }

    public String getLoggedInUserId() {
        return LoggedInUserId;
    }

    public void setLoggedInUserId(String loggedInUserId) {
        LoggedInUserId = loggedInUserId;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getSubscriptionType() {
        return SubscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        SubscriptionType = subscriptionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscountTotal() {
        return DiscountTotal;
    }

    public void setDiscountTotal(double discountTotal) {
        DiscountTotal = discountTotal;
    }

    int UserId, SubscriptionType;
    double amount, DiscountTotal;
}
