package com.yorker.fanzania.views.screens.tournament.fragments.livescorefragment.model;

import com.google.gson.annotations.SerializedName;

public class LiveTournamentModel {

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("TournamentEndDate")
	private String tournamentEndDate;

	@SerializedName("TournamentStatus")
	private String tournamentStatus;

	@SerializedName("TournamentStage")
	private String tournamentStage;

	@SerializedName("UserId")
	private int userId;

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

	@SerializedName("StartIndicator")
	private boolean startIndicator;

	@SerializedName("UserTeamName")
	private String userTeamName;

	public int getUserTeamId(){
		return userTeamId;
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

	public int getUserId(){
		return userId;
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

	public boolean isStartIndicator(){
		return startIndicator;
	}

	public String getUserTeamName(){
		return userTeamName;
	}
}