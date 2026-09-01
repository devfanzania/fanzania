package com.yorker.fanzania.views.screens.matchcontest.fragments.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DailyMatchModel implements Serializable {

	@SerializedName("Team1Image")
	private String team1Image;

	@SerializedName("Venue")
	private String venue;

	@SerializedName("TournamentStatus")
	private String tournamentStatus;

	@SerializedName("WeeklyPoints")
	private int weeklyPoints;

	@SerializedName("WeeklyRank")
	private int weeklyRank;

	@SerializedName("MatchScheduledDate")
	private String matchScheduledDate;

	@SerializedName("MatchStage")
	private String matchStage;

	@SerializedName("MatchDate")
	private String matchDate;

	@SerializedName("MatchNo")
	private int matchNo;

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("TournamentType")
	private String tournamentType;

	@SerializedName("MatchType")
	private String matchType;

	@SerializedName("WeeklyActive")
	private boolean weeklyActive;

	@SerializedName("MatchScheduledTime")
	private String matchScheduledTime;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("Team1ShortName")
	private String team1ShortName;

	@SerializedName("TournamentName")
	private String tournamentName;

	@SerializedName("MatchStatus")
	private String matchStatus;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("Team2")
	private String team2;

	@SerializedName("Team2Image")
	private String team2Image;

	@SerializedName("Team1")
	private String team1;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("Team2ShortName")
	private String team2ShortName;

	@SerializedName("TotalPoints")
	private int TotalPoints;

	@SerializedName("Team1Score")
	private String team1Score;

	@SerializedName("Team2Score")
	private String team2Score;

	@SerializedName("Weather")
	private String Weather;

	public String getWeather() {
		return Weather;
	}

	public void setWeather(String weather) {
		Weather = weather;
	}

	@SerializedName("ShowScore")
	private Boolean showScore;

	String BattingTeam;

	public String getBattingTeam() {
		return BattingTeam;
	}

	public void setBattingTeam(String battingTeam) {
		BattingTeam = battingTeam;
	}

	public Boolean getShowScore() {
		return showScore;
	}

	public String getTeam2Score() {
		return team2Score;
	}

	public String getTeam1Score() {
		return team1Score;
	}

	private boolean isSelected=false;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getTeam1Image(){
		return team1Image;
	}

	public String getVenue(){
		return venue;
	}

	public String getTournamentStatus(){
		return tournamentStatus;
	}

	public int getWeeklyPoints(){
		return weeklyPoints;
	}

	public int getWeeklyRank(){
		return weeklyRank;
	}

	public String getMatchScheduledDate(){
		return matchScheduledDate;
	}

	public String getMatchStage(){
		return matchStage;
	}

	public String getMatchDate(){
		return matchDate;
	}

	public int getMatchNo(){
		return matchNo;
	}

	public int getUserTeamId(){
		return userTeamId;
	}

	public String getTournamentType(){
		return tournamentType;
	}

	public String getMatchType(){
		return matchType;
	}

	public boolean isWeeklyActive(){
		return weeklyActive;
	}

	public String getMatchScheduledTime(){
		return matchScheduledTime;
	}

	public int getMatchId(){
		return matchId;
	}

	public String getTeam1ShortName(){
		return team1ShortName;
	}

	public String getTournamentName(){
		return tournamentName;
	}

	public String getMatchStatus(){
		return matchStatus;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public String getTeam2(){
		return team2;
	}

	public String getTeam2Image(){
		return team2Image;
	}

	public String getTeam1(){
		return team1;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public String getTeam2ShortName(){
		return team2ShortName;
	}

	public int getTotalPoints() {
		return TotalPoints;
	}
}