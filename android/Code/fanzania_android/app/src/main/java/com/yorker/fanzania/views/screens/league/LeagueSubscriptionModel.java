package com.yorker.fanzania.views.screens.league;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueSubscriptionModel {
    public int UserId;
    public int LeagueId;
    public String Name;
    public String UserTeamName,client_id,client_secret;
    public int SubscriptionType;
    public double LivePackageAmount;

    public double getLivePackageAmount_USD() {
        return LivePackageAmount_USD;
    }

    public void setLivePackageAmount_USD(int livePackageAmount_USD) {
        LivePackageAmount_USD = livePackageAmount_USD;
    }

    public double getPrizePackageAmount_USD() {
        return PrizePackageAmount_USD;
    }

    public void setPrizePackageAmount_USD(int prizePackageAmount_USD) {
        PrizePackageAmount_USD = prizePackageAmount_USD;
    }

    public double getFullPackageAmount_USD() {
        return FullPackageAmount_USD;
    }

    public void setFullPackageAmount_USD(int fullPackageAmount_USD) {
        FullPackageAmount_USD = fullPackageAmount_USD;
    }

    public double getLivePackageAmount_GBP() {
        return LivePackageAmount_GBP;
    }

    public void setLivePackageAmount_GBP(int livePackageAmount_GBP) {
        LivePackageAmount_GBP = livePackageAmount_GBP;
    }

    public double getPrizePackageAmount_GBP() {
        return PrizePackageAmount_GBP;
    }

    public void setPrizePackageAmount_GBP(int prizePackageAmount_GBP) {
        PrizePackageAmount_GBP = prizePackageAmount_GBP;
    }

    public double getFullPackageAmount_GBP() {
        return FullPackageAmount_GBP;
    }

    public void setFullPackageAmount_GBP(int fullPackageAmount_GBP) {
        FullPackageAmount_GBP = fullPackageAmount_GBP;
    }

    public double PrizePackageAmount, LivePackageAmount_USD, PrizePackageAmount_USD,
            FullPackageAmount_USD, LivePackageAmount_GBP, PrizePackageAmount_GBP, FullPackageAmount_GBP;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getLeagueId() {
        return LeagueId;
    }

    public void setLeagueId(int leagueId) {
        LeagueId = leagueId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserTeamName() {
        return UserTeamName;
    }

    public void setUserTeamName(String userTeamName) {
        UserTeamName = userTeamName;
    }

    public int getSubscriptionType() {
        return SubscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        SubscriptionType = subscriptionType;
    }

    public double getLivePackageAmount() {
        return LivePackageAmount;
    }

    public void setLivePackageAmount(int livePackageAmount) {
        LivePackageAmount = livePackageAmount;
    }

    public double getPrizePackageAmount() {
        return PrizePackageAmount;
    }

    public void setPrizePackageAmount(int prizePackageAmount) {
        PrizePackageAmount = prizePackageAmount;
    }

    public int getFullPackageAmount() {
        return FullPackageAmount;
    }

    public void setFullPackageAmount(int fullPackageAmount) {
        FullPackageAmount = fullPackageAmount;
    }

    public int getDiscountRate1() {
        return DiscountRate1;
    }

    public void setDiscountRate1(int discountRate1) {
        DiscountRate1 = discountRate1;
    }

    public int getDiscountRate2() {
        return DiscountRate2;
    }

    public void setDiscountRate2(int discountRate2) {
        DiscountRate2 = discountRate2;
    }

    public int getDiscountRate3() {
        return DiscountRate3;
    }

    public void setDiscountRate3(int discountRate3) {
        DiscountRate3 = discountRate3;
    }

    public int FullPackageAmount;
        public int DiscountRate1;
        public int DiscountRate2;
        public int DiscountRate3;
}
