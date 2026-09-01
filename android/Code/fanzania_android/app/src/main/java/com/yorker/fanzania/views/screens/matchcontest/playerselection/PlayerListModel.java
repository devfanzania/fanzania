package com.yorker.fanzania.views.screens.matchcontest.playerselection;

import com.google.gson.annotations.SerializedName;

public class PlayerListModel{

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("ParticipationTeamId")
	private String participationTeamId;

	public String getParticipationTeamId() {
		return participationTeamId;
	}

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("TeamImage")
	private String teamImage;

	@SerializedName("PlayerDesc")
	private String playerDesc;

	@SerializedName("APIPId")
	private int aPIPId;

	@SerializedName("PlayingInd")
	private boolean playingInd;

	@SerializedName("PlayerType")
	private String playerType;

	@SerializedName("TeamShortName")
	private String teamShortName;

	@SerializedName("PlayerValue")
	private int playerValue;

	@SerializedName("PlayerStatus")
	private boolean playerStatus;

	@SerializedName("TotalPoints")
	private int totalPoints;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("PlayerSpeciality")
	private String playerSpeciality;

	@SerializedName("PlayerShortName")
	private String playerShortName;

	@SerializedName("PlayerSelected")
	private boolean playerSelected;

	@SerializedName("TeamCapt")
	private boolean teamCapt;

	@SerializedName("TeamVCapt")
	private boolean teamVCapt;

	@SerializedName("WinnerPrediction")
	private String winnerPrediction;

	public String getWinnerPrediction() {
		return winnerPrediction;
	}

	public void setWinnerPrediction(String winnerPrediction) {
		this.winnerPrediction = winnerPrediction;
	}

	public boolean isPlayingInd() {
		return playingInd;
	}

	public boolean isPlayerSelected() {
		return playerSelected;
	}

	public boolean isTeamCapt() {
		return teamCapt;
	}

	public boolean isTeamVCapt() {
		return teamVCapt;
	}

	public void setPlayerSelected(boolean playerSelected) {
		this.playerSelected = playerSelected;
	}

	public void setTeamCapt(boolean teamCapt) {
		this.teamCapt = teamCapt;
	}

	public void setTeamVCapt(boolean teamVCapt) {
		this.teamVCapt = teamVCapt;
	}

	public void setParticipationTeamName(String participationTeamName){
		this.participationTeamName = participationTeamName;
	}

	public String getParticipationTeamName(){
		return participationTeamName;
	}

	public void setPlayerId(int playerId){
		this.playerId = playerId;
	}

	public int getPlayerId(){
		return playerId;
	}

	public void setTeamImage(String teamImage){
		this.teamImage = teamImage;
	}

	public String getTeamImage(){
		return teamImage;
	}

	public void setPlayerDesc(String playerDesc){
		this.playerDesc = playerDesc;
	}

	public String getPlayerDesc(){
		return playerDesc;
	}

	public void setAPIPId(int aPIPId){
		this.aPIPId = aPIPId;
	}

	public int getAPIPId(){
		return aPIPId;
	}

	public void setPlayerType(String playerType){
		this.playerType = playerType;
	}

	public String getPlayerType(){
		return playerType;
	}

	public void setTeamShortName(String teamShortName){
		this.teamShortName = teamShortName;
	}

	public String getTeamShortName(){
		return teamShortName;
	}

	public void setPlayerValue(int playerValue){
		this.playerValue = playerValue;
	}

	public int getPlayerValue(){
		return playerValue;
	}

	public void setPlayerStatus(boolean playerStatus){
		this.playerStatus = playerStatus;
	}

	public boolean isPlayerStatus(){
		return playerStatus;
	}

	public void setTotalPoints(int totalPoints){
		this.totalPoints = totalPoints;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	public String getPlayerName(){
		return playerName;
	}

	public void setPlayerSpeciality(String playerSpeciality){
		this.playerSpeciality = playerSpeciality;
	}

	public String getPlayerSpeciality(){
		return playerSpeciality;
	}

	public void setPlayerShortName(String playerShortName){
		this.playerShortName = playerShortName;
	}

	public String getPlayerShortName(){
		return playerShortName;
	}
}