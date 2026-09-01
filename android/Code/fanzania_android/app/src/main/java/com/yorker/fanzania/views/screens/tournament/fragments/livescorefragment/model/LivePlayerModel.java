package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model;

import com.google.gson.annotations.SerializedName;

public class LivePlayerModel {

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("FieldingPoints")
	private int fieldingPoints;

	@SerializedName("BattingPoints")
	private int battingPoints;

	@SerializedName("PlayerType")
	private String playerType;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("PlayerSpeciality")
	private String playerSpeciality;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("ParticipationTeamId")
	private int participationTeamId;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("BowlingPoints")
	private int bowlingPoints;

	@SerializedName("TotalPoints")
	private int totalPoints;

	@SerializedName("PlayerSelected")
	private boolean playerSelected;

	@SerializedName("Capt")
	private boolean capt;

	@SerializedName("VCapt")
	private boolean vCapt;

	@SerializedName("CurrentMatchPoints")
	private int currentMatchPoints;

	@SerializedName("AllTotalPoints")
	private int allTotalPoints;

	@SerializedName("TeamShortName")
	private String teamShortName;

	@SerializedName("TeamImage")
	private String teamImage;

	public String getTeamImage() {
		return teamImage;
	}

	public String getTeamShortName() {
		return teamShortName;
	}

	public String getParticipationTeamName(){
		return participationTeamName;
	}

	public int getPlayerId(){
		return playerId;
	}

	public int getFieldingPoints(){
		return fieldingPoints;
	}

	public int getBattingPoints(){
		return battingPoints;
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

	public int getMatchId(){
		return matchId;
	}

	public int getParticipationTeamId(){
		return participationTeamId;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public int getBowlingPoints(){
		return bowlingPoints;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public boolean isPlayerSelected() {
		return playerSelected;
	}

	public boolean isCapt() {
		return capt;
	}

	public boolean isvCapt() {
		return vCapt;
	}

	public int getCurrentMatchPoints() {
		return currentMatchPoints;
	}

	public int getAllTotalPoints() {
		return allTotalPoints;
	}
}