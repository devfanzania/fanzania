package com.yorker.fanzania.views.screens.tournament.fragments.homefragment.model;

import com.google.gson.annotations.SerializedName;

public class TopPlayersResponse{

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("Email")
	private String email;

	@SerializedName("LastMatchPoints")
	private int lastMatchPoints;

	@SerializedName("Rank")
	private int rank;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("MatchDetails")
	private String matchDetails;

	@SerializedName("Name")
	private String name;

	public int getUserTeamId(){
		return userTeamId;
	}

	public String getEmail(){
		return email;
	}

	public int getLastMatchPoints(){
		return lastMatchPoints;
	}

	public int getRank(){
		return rank;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public String getMatchDetails(){
		return matchDetails;
	}

	public String getName(){
		return name;
	}
}