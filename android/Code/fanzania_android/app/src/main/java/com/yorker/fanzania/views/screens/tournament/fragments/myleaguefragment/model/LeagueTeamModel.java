package com.yorker.fanzania.views.screens.tournament.fragments.myleaguefragment.model;

public class LeagueTeamModel {
    private int TeamCurrentStanding;
    private int TeamOldStanding;
    private String LeagueName;
    private String LeagueLeader;
    private String TeamGlobalRank;
    private String UserTeamName;
    private String UserTeamId;
    private String SubsLeft;
    private String NitroLeft;
    private String PainKillerLeft;
    private String AutoPilotLeft;
    private String UserName;
    private String UserId;
    private String Status;
    private String IsLeagueLeader;
    private String FullName;
    private String TotalPoints;
    private String LeagueRank;
    private int UserLeagueId;
    private int LastMatchPoints;
    private int UserTier;
    private String SupportedTeam;

    public int getUserTier() {
        return UserTier;
    }

    public String getSupportedTeam() {
        return SupportedTeam;
    }

    public int getLastMatchPoints() {
        return LastMatchPoints;
    }

    public int getTeamCurrentStanding() {
        return TeamCurrentStanding;
    }

    public int getTeamOldStanding() {
        return TeamOldStanding;
    }

    public String getLeagueName() {
        return LeagueName;
    }

    public String getLeagueLeader() {
        return LeagueLeader;
    }

    public String getTeamGlobalRank() {
        return TeamGlobalRank;
    }

    public String getUserTeamName() {
        return UserTeamName;
    }

    public String getUserTeamId() {
        return UserTeamId;
    }

    public String getSubsLeft() {
        return SubsLeft;
    }

    public String getNitroLeft() {
        return NitroLeft;
    }

    public String getPainKillerLeft() {
        return PainKillerLeft;
    }

    public String getAutoPilotLeft() {
        return AutoPilotLeft;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserId() {
        return UserId;
    }

    public String getStatus() {
        return Status;
    }

    public String getIsLeagueLeader() {
        return IsLeagueLeader;
    }

    public String getFullName() {
        return FullName;
    }

    public String getTotalPoints() {
        return TotalPoints;
    }

    public String getLeagueRank() {
        return LeagueRank;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getUserLeagueId() {
        return UserLeagueId;
    }
}
