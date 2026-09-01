package com.yorker.fanzania.views.screens.matchcontest.scorecard;

import com.google.gson.annotations.SerializedName;

public class ScoreCardResponse{

	@SerializedName("Team2Extras")
	private String team2Extras;

	@SerializedName("BTrunScored")
	private int bTrunScored;

	@SerializedName("BTstrikerate")
	private double bTstrikerate;

	@SerializedName("TeamSortName")
	private String teamSortName;

	@SerializedName("BLrun")
	private int bLrun;

	@SerializedName("BLmaiden")
	private int bLmaiden;

	@SerializedName("MatchSummary")
	private String matchSummary;

	@SerializedName("Team1Score")
	private String team1Score;

	@SerializedName("BL4s")
	private int bL4s;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("BTrun4s")
	private int bTrun4s;

	@SerializedName("BL6s")
	private int bL6s;

	@SerializedName("BTrun6s")
	private int bTrun6s;

	@SerializedName("Team")
	private String team;

	@SerializedName("BLover")
	private double bLover;

	@SerializedName("BLwicket")
	private int bLwicket;

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("PlayerIndicator")
	private String  playerIndicator;

	@SerializedName("BTdismissalinfo")
	private String bTdismissalinfo;

	@SerializedName("InningDesc")
	private String inningDesc;

	@SerializedName("Team1Extras")
	private String team1Extras;

	@SerializedName("Inning")
	private int inning;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("BLecon")
	private double bLecon;

	@SerializedName("Team2Score")
	private String team2Score;

	@SerializedName("Team1RR")
	private double team1RR;

	@SerializedName("Team2RR")
	private double team2RR;

	@SerializedName("BTballfaced")
	private int bTballfaced;

	@SerializedName("Team2Image")
	private String team2Image;

	@SerializedName("Team1Image")
	private String team1Image;

	public String getTeam2Extras() {
		return team2Extras;
	}

	public int getbTrunScored() {
		return bTrunScored;
	}

	public double getbTstrikerate() {
		return bTstrikerate;
	}

	public String getTeamSortName() {
		return teamSortName;
	}

	public int getbLrun() {
		return bLrun;
	}

	public int getbLmaiden() {
		return bLmaiden;
	}

	public String getMatchSummary() {
		return matchSummary;
	}

	public String getTeam1Score() {
		return team1Score;
	}

	public int getbL4s() {
		return bL4s;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getbTrun4s() {
		return bTrun4s;
	}

	public int getbL6s() {
		return bL6s;
	}

	public int getbTrun6s() {
		return bTrun6s;
	}

	public String getTeam() {
		return team;
	}

	public double getbLover() {
		return bLover;
	}

	public int getbLwicket() {
		return bLwicket;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getPlayerIndicator() {
		return playerIndicator;
	}

	public String getbTdismissalinfo() {
		return bTdismissalinfo;
	}

	public String getInningDesc() {
		return inningDesc;
	}

	public String getTeam1Extras() {
		return team1Extras;
	}

	public int getInning() {
		return inning;
	}

	public int getMatchId() {
		return matchId;
	}

	public double getbLecon() {
		return bLecon;
	}

	public String getTeam2Score() {
		return team2Score;
	}

	public double getTeam1RR() {
		return team1RR;
	}

	public double getTeam2RR() {
		return team2RR;
	}

	public int getbTballfaced() {
		return bTballfaced;
	}

	public String getTeam2Image() {
		return team2Image;
	}

	public String getTeam1Image() {
		return team1Image;
	}
}