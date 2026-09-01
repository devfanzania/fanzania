package com.yorker.fanzania.views.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveTeamScoreComparison {

    @SerializedName("TournamentId")
    @Expose
    private String tournamentId;
    @SerializedName("MatchId")
    @Expose
    private String matchId;
    @SerializedName("MyPlayerId")
    @Expose
    private String myPlayerId;
    @SerializedName("MyPlayerName")
    @Expose
    private String myPlayerName;
    @SerializedName("MyPlayerType")
    @Expose
    private String myPlayerType;
    @SerializedName("MyPlayerSpeciality")
    @Expose
    private String myPlayerSpeciality;
    @SerializedName("MyTotalPoints")
    @Expose
    private int myTotalPoints;
    @SerializedName("MyPlayerSelected")
    @Expose
    private boolean myPlayerSelected;
    @SerializedName("MyCapt")
    @Expose
    private boolean myCapt;
    @SerializedName("MyVCapt")
    @Expose
    private boolean myVCapt;
    @SerializedName("MyTeamName")
    @Expose
    private String myTeamName;
    @SerializedName("MyPowerPlay")
    @Expose
    private String myPowerPlay;
    @SerializedName("OtherPlayerId")
    @Expose
    private String otherPlayerId;
    @SerializedName("OtherPlayerName")
    @Expose
    private String otherPlayerName;

    @SerializedName("MyPrediction")
    @Expose
    private String myPrediction;

    public String getMyPrediction() {
        return myPrediction;
    }

    public void setMyPrediction(String myPrediction) {
        this.myPrediction = myPrediction;
    }

    public String getOtherPrediction() {
        return otherPrediction;
    }

    public void setOtherPrediction(String otherPrediction) {
        this.otherPrediction = otherPrediction;
    }

    @SerializedName("OtherPrediction")
    @Expose
    private String otherPrediction;

    @SerializedName("OtherPlayerType")
    @Expose
    private Object otherPlayerType;
    @SerializedName("OtherPlayerSpeciality")
    @Expose
    private Object otherPlayerSpeciality;
    @SerializedName("OtherTotalPoints")
    @Expose
    private int otherTotalPoints;
    @SerializedName("OtherPlayerSelected")
    @Expose
    private boolean otherPlayerSelected;
    @SerializedName("OtherCapt")
    @Expose
    private boolean otherCapt;
    @SerializedName("OtherVCapt")
    @Expose
    private boolean otherVCapt;
    @SerializedName("OtherTeamName")
    @Expose
    private String otherTeamName;
    @SerializedName("OtherPowerPlay")
    @Expose
    private Object otherPowerPlay;

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMyPlayerId() {
        return myPlayerId;
    }

    public void setMyPlayerId(String myPlayerId) {
        this.myPlayerId = myPlayerId;
    }

    public String getMyPlayerName() {
        return myPlayerName;
    }

    public void setMyPlayerName(String myPlayerName) {
        this.myPlayerName = myPlayerName;
    }

    public String getMyPlayerType() {
        return myPlayerType;
    }

    public void setMyPlayerType(String myPlayerType) {
        this.myPlayerType = myPlayerType;
    }

    public String getMyPlayerSpeciality() {
        return myPlayerSpeciality;
    }

    public void setMyPlayerSpeciality(String myPlayerSpeciality) {
        this.myPlayerSpeciality = myPlayerSpeciality;
    }

    public int getMyTotalPoints() {
        return myTotalPoints;
    }

    public void setMyTotalPoints(int myTotalPoints) {
        this.myTotalPoints = myTotalPoints;
    }

    public boolean getMyPlayerSelected() {
        return myPlayerSelected;
    }

    public void setMyPlayerSelected(boolean myPlayerSelected) {
        this.myPlayerSelected = myPlayerSelected;
    }

    public boolean getMyCapt() {
        return myCapt;
    }

    public void setMyCapt(boolean myCapt) {
        this.myCapt = myCapt;
    }

    public boolean getMyVCapt() {
        return myVCapt;
    }

    public void setMyVCapt(boolean myVCapt) {
        this.myVCapt = myVCapt;
    }

    public String getMyTeamName() {
        return myTeamName;
    }

    public void setMyTeamName(String myTeamName) {
        this.myTeamName = myTeamName;
    }

    public String getMyPowerPlay() {
        return myPowerPlay;
    }

    public void setMyPowerPlay(String myPowerPlay) {
        this.myPowerPlay = myPowerPlay;
    }

    public String getOtherPlayerId() {
        return otherPlayerId;
    }

    public void setOtherPlayerId(String otherPlayerId) {
        this.otherPlayerId = otherPlayerId;
    }

    public String getOtherPlayerName() {
        return otherPlayerName;
    }

    public void setOtherPlayerName(String otherPlayerName) {
        this.otherPlayerName = otherPlayerName;
    }

    public Object getOtherPlayerType() {
        return otherPlayerType;
    }

    public void setOtherPlayerType(Object otherPlayerType) {
        this.otherPlayerType = otherPlayerType;
    }

    public Object getOtherPlayerSpeciality() {
        return otherPlayerSpeciality;
    }

    public void setOtherPlayerSpeciality(Object otherPlayerSpeciality) {
        this.otherPlayerSpeciality = otherPlayerSpeciality;
    }

    public int getOtherTotalPoints() {
        return otherTotalPoints;
    }

    public void setOtherTotalPoints(int otherTotalPoints) {
        this.otherTotalPoints = otherTotalPoints;
    }

    public boolean getOtherPlayerSelected() {
        return otherPlayerSelected;
    }

    public void setOtherPlayerSelected(boolean otherPlayerSelected) {
        this.otherPlayerSelected = otherPlayerSelected;
    }

    public boolean getOtherCapt() {
        return otherCapt;
    }

    public void setOtherCapt(boolean otherCapt) {
        this.otherCapt = otherCapt;
    }

    public boolean getOtherVCapt() {
        return otherVCapt;
    }

    public void setOtherVCapt(boolean otherVCapt) {
        this.otherVCapt = otherVCapt;
    }

    public String getOtherTeamName() {
        return otherTeamName;
    }

    public void setOtherTeamName(String otherTeamName) {
        this.otherTeamName = otherTeamName;
    }

    public Object getOtherPowerPlay() {
        return otherPowerPlay;
    }

    public void setOtherPowerPlay(Object otherPowerPlay) {
        this.otherPowerPlay = otherPowerPlay;
    }

}