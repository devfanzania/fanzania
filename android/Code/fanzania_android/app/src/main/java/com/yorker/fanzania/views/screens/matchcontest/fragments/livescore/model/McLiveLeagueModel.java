package com.yorker.fanzania.views.screens.matchcontest.fragments.livescore.model;

import com.google.gson.annotations.SerializedName;

public class McLiveLeagueModel{

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("UserName")
	private String userName;

	@SerializedName("LeagueName")
	private String leagueName;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("TeamNewStanding")
	private int teamNewStanding;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("CurrentMatchPoints")
	private int currentMatchPoints;

	@SerializedName("LeagueId")
	private int leagueId;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("TotalPoints")
	private Object totalPoints;

	public int getUserTeamId(){
		return userTeamId;
	}

	public String getUserName(){
		return userName;
	}

	public String getLeagueName(){
		return leagueName;
	}

	public int getUserId(){
		return userId;
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

	public int getCurrentMatchPoints(){
		return currentMatchPoints;
	}

	public int getLeagueId(){
		return leagueId;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public Object getTotalPoints(){
		return totalPoints;
	}
}