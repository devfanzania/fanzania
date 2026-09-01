package com.yorker.fanzania.views.screens.tournament.manageteam.model;

import com.google.gson.annotations.SerializedName;

public class PlayerDataType {
    private String TournamentId;
    private String UserTeamName, WinnerPrediction;
    private int UserTeamId;
    private int UserId;
    private int MatchId;
    private int TeamCapt;
    private boolean WinnerPredictionStatus;

    public String getWinnerPrediction() {
        return WinnerPrediction;
    }

    public boolean isWinnerPredictionStatus() {
        return WinnerPredictionStatus;
    }

    public void setWinnerPredictionStatus(boolean winnerPredictionStatus) {
        WinnerPredictionStatus = winnerPredictionStatus;
    }

    public void setWinnerPrediction(String winnerPrediction) {
        WinnerPrediction = winnerPrediction;
    }

    private int Captain;
    private int TeamVCapt;
    private int ViceCaptain;
    private int SubsLeft;
    private int SubsLeftAtSnapShot;
    private int NitroLeft;
    private int NitroMultiplier;
    private int PainKillerLeft;
    private int AutoPilotLeft;
    private int TotalPoints;
    private int TeamRank;
    private int TeamCompositionId;
    private boolean NitroUsed;
    private boolean PainKillerUsed;
    private boolean AutoPilotUsed;

    @SerializedName("PlayingInd")
    private boolean isPlayingInd;

    private int PlayerId;
    private String PlayerName;
    private String PlayerShortName;
    private String PlayerType;
    private String PlayerSpeciality;
    private int PlayerValue;
    private int PlayerPoints;
    private int AveragePoints;
    private String PlayerDesc;
    private String PlayerImage;
    private int ParticipationTeamId;
    private int PainKillerPlayerPoint;
    private int MatchTotalPoints;
    private String ParticipationTeamName;
    private Boolean isSelected = false;

    private String TeamImage;
    private String TeamDescription;
    private String TeamShortName;
    private String LastMatchTeams;
    private int PlayerTotalPoints;

    public boolean isPlayingInd() {
        return isPlayingInd;
    }

    public String getLastMatchTeams() {
        return LastMatchTeams;
    }

    public String getTournamentId() {
        return TournamentId;
    }

    public String getUserTeamName() {
        return UserTeamName;
    }

    public int getUserTeamId() {
        return UserTeamId;
    }

    public int getUserId() {
        return UserId;
    }

    public int getTeamCapt() {
        return TeamCapt;
    }

    public int getTeamVCapt() {
        return TeamVCapt;
    }



    public int getSubsLeft() {
        return SubsLeft;
    }

    public int getSubsLeftAtSnapShot() {
        return SubsLeftAtSnapShot;
    }

    public int getNitroLeft() {
        return NitroLeft;
    }

    public int getPainKillerLeft() {
        return PainKillerLeft;
    }

    public int getAutoPilotLeft() {
        return AutoPilotLeft;
    }

    public int getTotalPoints() {
        return TotalPoints;
    }

    public int getTeamRank() {
        return TeamRank;
    }

    public int getTeamCompositionId() {
        return TeamCompositionId;
    }

    public boolean isNitroUsed() {
        return NitroUsed;
    }

    public boolean isPainKillerUsed() {
        return PainKillerUsed;
    }

    public boolean isAutoPilotUsed() {
        return AutoPilotUsed;
    }

    public int getPlayerId() {
        return PlayerId;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getPlayerShortName() {
        return PlayerShortName;
    }

    public String getPlayerType() {
        return PlayerType;
    }

    public String getPlayerSpeciality() {
        return PlayerSpeciality;
    }

    public int getPlayerValue() {
        return PlayerValue;
    }

    public String getPlayerDesc() {
        return PlayerDesc;
    }

    public String getPlayerImage() {
        return PlayerImage;
    }

    public int getParticipationTeamId() {
        return ParticipationTeamId;
    }

    public String getParticipationTeamName() {
        return ParticipationTeamName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getMatchId() {
        return MatchId;
    }

    public int getCaptain() {
        return Captain;
    }

    public int getViceCaptain() {
        return ViceCaptain;
    }

    public int getNitroMultiplier() {
        return NitroMultiplier;
    }

    public int getPlayerPoints() {
        return PlayerPoints;
    }

    public int getPainKillerPlayerPoint() {
        return PainKillerPlayerPoint;
    }

    public int getMatchTotalPoints() {
        return MatchTotalPoints;
    }

    public void setTeamCapt(int teamCapt) {
        TeamCapt = teamCapt;
    }

    public void setCaptain(int captain) {
        Captain = captain;
    }

    public void setTeamVCapt(int teamVCapt) {
        TeamVCapt = teamVCapt;
    }

    public void setViceCaptain(int viceCaptain) {
        ViceCaptain = viceCaptain;
    }

    public String getTeamImage() {
        return TeamImage;
    }

    public String getTeamDescription() {
        return TeamDescription;
    }

    public String getTeamShortName() {
        return TeamShortName;
    }

    public int getAveragePoints() {
        return AveragePoints;
    }

    public int getPlayerTotalPoints() {
        return PlayerTotalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        TotalPoints = totalPoints;
    }
}
