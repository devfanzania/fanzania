package com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model;

import com.google.gson.annotations.SerializedName;

public class UpcomingTournamentModel {

	@SerializedName("TournamentLogoBytes")
	private Object tournamentLogoBytes;

	@SerializedName("TournamentEndDate")
	private String tournamentEndDate;

	@SerializedName("TournamentStatus")
	private String tournamentStatus;

	@SerializedName("TournamentStage")
	private String tournamentStage;

	@SerializedName("TournamentComplete")
	private boolean tournamentComplete;

	@SerializedName("TournamentName")
	private String tournamentName;

	@SerializedName("TournamentLogo")
	private String tournamentLogo;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("TournamentStartDate")
	private String tournamentStartDate;

	public Object getTournamentLogoBytes(){
		return tournamentLogoBytes;
	}

	public String getTournamentEndDate(){
		return tournamentEndDate;
	}

	public String getTournamentStatus(){
		return tournamentStatus;
	}

	public String getTournamentStage(){
		return tournamentStage;
	}

	public boolean isTournamentComplete(){
		return tournamentComplete;
	}

	public String getTournamentName(){
		return tournamentName;
	}

	public String getTournamentLogo(){
		return tournamentLogo;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public String getTournamentStartDate(){
		return tournamentStartDate;
	}
}