package com.yorker.fanzania.views.screens.tournament.liveleagueview.model;

import com.google.gson.annotations.SerializedName;

public class LeagueUserModel {

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("UserName")
	private String userName;

	@SerializedName("LeagueName")
	private String leagueName;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("TeamNewStanding")
	private int teamNewStanding;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("LeagueId")
	private int leagueId;

	@SerializedName("TeamOldStanding")
	private int teamOldStanding;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("TotalPoints")
	private int totalPoints;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("Transfers")
	private int transfers;

	@SerializedName("TransferUsed")
	private int transfersUsed;

	public int getTransfersUsed() {
		return transfersUsed;
	}

	@SerializedName("CurrentMatchPoints")
	private int currentMatchPoints;

	@SerializedName("PowerPlay")
	private String powerPlay;

	public int getUserTeamId(){
		return userTeamId;
	}

	public String getUserName(){
		return userName;
	}

	public String getLeagueName(){
		return leagueName;
	}

	public int getMatchId(){
		return matchId;
	}

	public int getTeamNewStanding(){
		return teamNewStanding;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public int getLeagueId(){
		return leagueId;
	}

	public int getTeamOldStanding(){
		return teamOldStanding;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public int getUserId() {
		return userId;
	}

	public int getTransfers() {
		return transfers;
	}

	public int getCurrentMatchPoints() {
		return currentMatchPoints;
	}

	public String getPowerPlay() {
		return powerPlay;
	}
}