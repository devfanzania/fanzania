package com.yorker.fanzania.views.screens.matchcontest.fragments.leagues.model;

import com.google.gson.annotations.SerializedName;

public class McLeagueModel{

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("TeamNewLeagueRank")
	private int teamNewLeagueRank;

	@SerializedName("LeagueName")
	private String leagueName;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("TotalTeams")
	private int totalTeams;

	@SerializedName("LastMatchPoints")
	private int lastMatchPoints;

	@SerializedName("TournamentName")
	private String tournamentName;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("LeagueId")
	private int leagueId;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("TeamRank")
	private int teamRank;

	@SerializedName("Name")
	private String name;

	public int getUserTeamId(){
		return userTeamId;
	}

	public int getTeamNewLeagueRank(){
		return teamNewLeagueRank;
	}

	public String getLeagueName(){
		return leagueName;
	}

	public int getUserId(){
		return userId;
	}

	public int getTotalTeams(){
		return totalTeams;
	}

	public int getLastMatchPoints(){
		return lastMatchPoints;
	}

	public String getTournamentName(){
		return tournamentName;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public int getLeagueId(){
		return leagueId;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public int getTeamRank(){
		return teamRank;
	}

	public String getName(){
		return name;
	}
}