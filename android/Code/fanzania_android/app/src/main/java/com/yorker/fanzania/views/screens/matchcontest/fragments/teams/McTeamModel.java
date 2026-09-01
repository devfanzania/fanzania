package com.yorker.fanzania.views.screens.matchcontest.fragments.teams;

import com.google.gson.annotations.SerializedName;

public class McTeamModel{


	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("MatchTotalPoints")
	private int matchTotalPoints;

	@SerializedName("PlayerPoints")
	private int playerPoints;

	@SerializedName("AveragePoints")
	private int averagePoints;

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("WinnerPrediction")
	private String winnerPrediction;

	public String getWinnerPrediction() {
		return winnerPrediction;
	}

	public void setWinnerPrediction(String winnerPrediction) {
		this.winnerPrediction = winnerPrediction;
	}

	@SerializedName("TeamCapt")
	private int teamCapt;

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("TeamImage")
	private String teamImage;

	@SerializedName("PlayerDesc")
	private String playerDesc;

	@SerializedName("PlayerType")
	private String playerType;

	@SerializedName("TeamShortName")
	private String teamShortName;

	@SerializedName("PlayerValue")
	private int playerValue;

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("TeamCompositionId")
	private int teamCompositionId;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("PlayerSpeciality")
	private String playerSpeciality;

	@SerializedName("TeamVCapt")
	private int teamVCapt;

	@SerializedName("ParticipationTeamId")
	private int participationTeamId;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("PlayerShortName")
	private String playerShortName;

	@SerializedName("TotalTeams")
	private String totalTeams;

	String BattingTeam;

	public String getBattingTeam() {
		return BattingTeam;
	}

	public void setBattingTeam(String battingTeam) {
		BattingTeam = battingTeam;
	}

	public String getTotalTeams() {
		return totalTeams;
	}

	public void setParticipationTeamName(String participationTeamName){
		this.participationTeamName = participationTeamName;
	}

	public int getMatchId() {
		return matchId;
	}

	public int getMatchTotalPoints() {
		return matchTotalPoints;
	}

	public int getPlayerPoints() {
		return playerPoints;
	}

	public int getAveragePoints() {
		return averagePoints;
	}

	public String getParticipationTeamName() {
		return participationTeamName;
	}

	public int getTeamCapt() {
		return teamCapt;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getTeamImage() {
		return teamImage;
	}

	public String getPlayerDesc() {
		return playerDesc;
	}

	public String getPlayerType() {
		return playerType;
	}

	public String getTeamShortName() {
		return teamShortName;
	}

	public int getPlayerValue() {
		return playerValue;
	}

	public int getUserTeamId() {
		return userTeamId;
	}

	public int getTeamCompositionId() {
		return teamCompositionId;
	}

	public int getUserId() {
		return userId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getPlayerSpeciality() {
		return playerSpeciality;
	}

	public int getTeamVCapt() {
		return teamVCapt;
	}

	public int getParticipationTeamId() {
		return participationTeamId;
	}

	public int getTournamentId() {
		return tournamentId;
	}

	public String getUserTeamName() {
		return userTeamName;
	}

	public String getPlayerShortName() {
		return playerShortName;
	}
}