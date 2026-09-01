package com.yorker.fanzania.views.screens.tournament.playerlist.model;

import com.google.gson.annotations.SerializedName;

public class TeamFilterModel {

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("TeamImage")
	private String teamImage;

	@SerializedName("TeamShortName")
	private String teamShortName;

	@SerializedName("ParticipationTeamId")
	private int participationTeamId;

	@SerializedName("TournamentId")
	private int tournamentId;

	private Boolean isChecked=false;


	public Boolean getChecked() {
		return isChecked;
	}

	public void setChecked(Boolean checked) {
		isChecked = checked;
	}

	public String getParticipationTeamName(){
		return participationTeamName;
	}

	public void setTeamShortName(String teamShortName) {
		this.teamShortName = teamShortName;
	}

	public void setTournamentId(int tournamentId) {
		this.tournamentId = tournamentId;
	}

	public String getTeamImage(){
		return teamImage;
	}

	public String getTeamShortName(){
		return teamShortName;
	}

	public int getParticipationTeamId(){
		return participationTeamId;
	}

	public int getTournamentId(){
		return tournamentId;
	}
}