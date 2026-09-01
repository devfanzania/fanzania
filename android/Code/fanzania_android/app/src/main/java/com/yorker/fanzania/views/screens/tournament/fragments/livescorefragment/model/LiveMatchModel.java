package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model;

import com.google.gson.annotations.SerializedName;

public class LiveMatchModel {

	@SerializedName("MatchDate")
	private String matchDate;

	@SerializedName("MatchNo")
	private int matchNo;

	@SerializedName("MatchType")
	private String matchType;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("MatchStatus")
	private String matchStatus;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("Team2")
	private String team2;

	@SerializedName("Team1")
	private String team1;

	@SerializedName("Team1ShortName")
	private String team1ShortName;

	@SerializedName("Team2ShortName")
	private String team2ShortName;

	@SerializedName("Team1Image")
	private String team1Image;

	@SerializedName("Team2Image")
	private String team2Image;

	@SerializedName("MatchComplete")
	private boolean matchComplete;

	@SerializedName("ShowScore")
	private boolean showScore;

	@SerializedName("Team1Score")
	private String team1Score;

	@SerializedName("Team2Score")
	private String team2Score;

	public boolean isShowScore() {
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

	public String getTeam1ShortName() {
		return team1ShortName;
	}

	public String getTeam2ShortName() {
		return team2ShortName;
	}

	public String getMatchDate(){
		return matchDate;
	}

	public int getMatchNo(){
		return matchNo;
	}

	public String getMatchType(){
		return matchType;
	}

	public int getMatchId(){
		return matchId;
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

	public String getTeam1(){
		return team1;
	}

	public String getTeam1Image() {
		return team1Image;
	}

	public String getTeam2Image() {
		return team2Image;
	}

	public boolean isMatchComplete() {
		return matchComplete;
	}
}