package com.yorker.fanzania.views.screens.tournament.manageteam.model;

import com.google.gson.annotations.SerializedName;

public class UpcomingMatchModel{

	@SerializedName("Team1Image")
	private String team1Image;

	@SerializedName("Venue")
	private String venue;

	@SerializedName("MatchScheduledDate")
	private String matchScheduledDate;

	public void setTeam1ShortName(String team1ShortName) {
		this.team1ShortName = team1ShortName;
	}

	public void setTeam2ShortName(String team2ShortName) {
		this.team2ShortName = team2ShortName;
	}

	@SerializedName("MatchStage")
	private String matchStage;

	@SerializedName("Winner")
	private String winner;

	@SerializedName("MatchDate")
	private String matchDate;

	@SerializedName("MatchNo")
	private int matchNo;

	@SerializedName("MatchComplete")
	private boolean matchComplete;

	@SerializedName("MatchType")
	private String matchType;

	@SerializedName("MatchScheduledTime")
	private String matchScheduledTime;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("Team1ShortName")
	private String team1ShortName;

	@SerializedName("BattingTeam")
	private String battingTeam;

	public String getBattingTeam() {
		return battingTeam;
	}

	public void setBattingTeam(String battingTeam) {
		this.battingTeam = battingTeam;
	}

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

	@SerializedName("Team2ShortName")
	private String team2ShortName;

	public String getWeather() {
		return Weather;
	}

	private String Weather;

	public String getTeam1Image(){
		return team1Image;
	}

	public String getVenue(){
		return venue;
	}

	public String getMatchScheduledDate(){
		return matchScheduledDate;
	}

	public String getMatchStage(){
		return matchStage;
	}

	public String getWinner(){
		return winner;
	}

	public String getMatchDate(){
		return matchDate;
	}

	public int getMatchNo(){
		return matchNo;
	}

	public boolean isMatchComplete(){
		return matchComplete;
	}

	public String getMatchType(){
		return matchType;
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

	public String getTeam2ShortName(){
		return team2ShortName;
	}
}