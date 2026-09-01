package com.yorker.fanzania.views.screens.matchcontest.fragments.home.model;

import com.google.gson.annotations.SerializedName;

public class TournamentModel{

	@SerializedName("TournamentName")
	private String tournamentName;

	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public void setTournamentName(String tournamentName){
		this.tournamentName = tournamentName;
	}

	public String getTournamentName() {
		return tournamentName;
	}
}