package com.yorker.fanzania.views.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamPointsComparison {

    @SerializedName("MatchNo")
    @Expose
    private String matchNo;

    @SerializedName("MatchStatus")
    @Expose
    private String matchStatus;

    @SerializedName("MyMatchTotalPoints")
    @Expose
    private String myMatchTotalPoints;

    @SerializedName("OtherMatchTotalPoints")
    @Expose
    private String otherMatchTotalPoints;

    public String getMatchNo() {
        return matchNo;
    }

    public void setMatchNo(String matchNo) {
        this.matchNo = matchNo;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMyMatchTotalPoints() {
        return myMatchTotalPoints;
    }

    public void setMyMatchTotalPoints(String myMatchTotalPoints) {
        this.myMatchTotalPoints = myMatchTotalPoints;
    }

    public String getOtherMatchTotalPoints() {
        return otherMatchTotalPoints;
    }

    public void setOtherMatchTotalPoints(String otherMatchTotalPoints) {
        this.otherMatchTotalPoints = otherMatchTotalPoints;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @SerializedName("MatchId")
    @Expose
    private String matchId;


}