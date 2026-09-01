package com.yorker.fanzania.views.screens.tournament.teamstats.model;

import com.google.gson.annotations.SerializedName;

public class UserStatsModel {

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("PlayerRank")
	private int playerRank;

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("TeamImage")
	private String teamImage;

	@SerializedName("PlayerType")
	private String playerType;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("PlayerSpeciality")
	private String playerSpeciality;

	@SerializedName("TeamDescription")
	private String teamDescription;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("PlayerShortName")
	private String playerShortName;

	@SerializedName("TotalPoints")
	private int totalPoints;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("MatchNo")
	private int matchNo;

	@SerializedName("Team1")
	private String team1;

	@SerializedName("Team2")
	private String team2;

	@SerializedName("CaptainId")
	private int captainId;

	@SerializedName("Captain")
	private String captain;

	@SerializedName("TeamFrom")
	private String teamFrom;

	public String getParticipationTeamName(){
		return participationTeamName;
	}

	public int getPlayerRank(){
		return playerRank;
	}

	public int getPlayerId(){
		return playerId;
	}

	public String getTeamImage(){
		return teamImage;
	}

	public String getPlayerType(){
		return playerType;
	}

	public String getPlayerName(){
		return playerName;
	}

	public String getPlayerSpeciality(){
		return playerSpeciality;
	}

	public String getTeamDescription(){
		return teamDescription;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public String getPlayerShortName(){
		return playerShortName;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public int getMatchId() {
		return matchId;
	}

	public int getMatchNo() {
		return matchNo;
	}

	public String getTeam1() {
		return team1;
	}

	public String getTeam2() {
		return team2;
	}

	public int getCaptainId() {
		return captainId;
	}

	public String getCaptain() {
		return captain;
	}

	public String getTeamFrom() {
		return teamFrom;
	}
}