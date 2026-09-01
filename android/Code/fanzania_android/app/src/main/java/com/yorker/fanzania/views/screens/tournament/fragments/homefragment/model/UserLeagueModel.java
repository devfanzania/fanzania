package com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model;

public class UserLeagueModel {

    private String TeamStanding;
    private String TeamPoints;
    private String UserId;
    private String TournamentId;
    private String UserTeamName;
    private String LeagueLeaderId;
    private String LeagueName;
    private String LeagueLeader;
    private String LeagueId;

    public String getTeamGlobalRank() {
        return TeamGlobalRank;
    }

    public void setTeamGlobalRank(String teamGlobalRank) {
        TeamGlobalRank = teamGlobalRank;
    }

    private String LeaguePoints;
    private String LeagueRank, TeamGlobalRank;
    private String Status;
    private String LeaguePin;

    private Boolean isSelected=false;

    public String getTeamStanding() {
        return TeamStanding;
    }

    public String getTeamPoints() {
        return TeamPoints;
    }

    public String getUserId() {
        return UserId;
    }

    public String getTournamentId() {
        return TournamentId;
    }

    public String getUserTeamName() {
        return UserTeamName;
    }

    public String getLeagueLeaderId() {
        return LeagueLeaderId;
    }

    public String getLeagueName() {
        return LeagueName;
    }

    public String getLeagueId() {
        return LeagueId;
    }

    public String getLeaguePoints() {
        return LeaguePoints;
    }

    public String getLeagueRank() {
        return LeagueRank;
    }

    public String getStatus() {
        return Status;
    }

    public String getLeaguePin() {
        return LeaguePin;
    }

    public String getLeagueLeader() {
        return LeagueLeader;
    }


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
