package com.yorker.fanzania.views.screens.tournament.fragments.myteamfragment.model;

public class Matches {

    private String MatchId;
    private String MatchNo;
    private String MatchType;
    private String Venue;
    private String TournamentId;
    private String MatchStage;
    private String Team1ShortName;
    private String Team2ShortName;
    private String Team1;
    private String Team2;
    private String Winner;

    public String getBattingTeam() {
        return BattingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        BattingTeam = battingTeam;
    }

    private String TossWinner;
    private String MatchStatus;
    private String MatchDate;
    private String MatchScheduledDate;
    private String Weather, BattingTeam;
    private Boolean isSelected=false, MatchComplete = false;

    public Boolean getMatchComplete() {
        return MatchComplete;
    }

    public void setMatchComplete(Boolean matchComplete) {
        MatchComplete = matchComplete;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public String getMatchId() {
        return MatchId;
    }

    public String getMatchNo() {
        return MatchNo;
    }

    public String getMatchType() {
        return MatchType;
    }

    public String getVenue() {
        return Venue;
    }

    public String getTournamentId() {
        return TournamentId;
    }

    public String getMatchStage() {
        return MatchStage;
    }

    public String getTeam1() {
        return Team1;
    }

    public String getTeam2() {
        return Team2;
    }

    public String getWinner() {
        return Winner;
    }

    public String getTossWinner() {
        return TossWinner;
    }

    public String getMatchStatus() {
        return MatchStatus;
    }

    public String getMatchDate() {
        return MatchDate;
    }

    public String getMatchScheduledDate() {
        return MatchScheduledDate;
    }

    public String getTeam1ShortName() {
        return Team1ShortName;
    }

    public String getTeam2ShortName() {
        return Team2ShortName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
