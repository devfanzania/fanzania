package com.yorker.fanzania.views.screens.tournament.leaguestats.model;

import com.google.gson.annotations.SerializedName;

public class LeagueStatsModel {

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("Player3Points")
	private int player3Points;

	@SerializedName("Player2Points")
	private int player2Points;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("Player1Points")
	private int player1Points;

	@SerializedName("Player2")
	private String player2;

	@SerializedName("TeamRank")
	private int teamRank;

	@SerializedName("UserTeamName")
	private String userTeamName;

	@SerializedName("Player1")
	private String player1;

	@SerializedName("TeamOwner")
	private String teamOwner;

	@SerializedName("Player3")
	private String player3;

	@SerializedName("Player1Match")
	private int player1Match;

	@SerializedName("Player2Match")
	private int player2Match;

	@SerializedName("Player3Match")
	private int player3Match;

	public int getUserTeamId(){
		return userTeamId;
	}

	public int getPlayer3Points(){
		return player3Points;
	}

	public int getPlayer2Points(){
		return player2Points;
	}

	public int getUserId(){
		return userId;
	}

	public int getPlayer1Points(){
		return player1Points;
	}

	public String getPlayer2(){
		return player2;
	}

	public int getTeamRank(){
		return teamRank;
	}

	public String getUserTeamName(){
		return userTeamName;
	}

	public String getPlayer1(){
		return player1;
	}

	public String getTeamOwner(){
		return teamOwner;
	}

	public String getPlayer3(){
		return player3;
	}

	public int getPlayer1Match() {
		return player1Match;
	}

	public int getPlayer2Match() {
		return player2Match;
	}

	public int getPlayer3Match() {
		return player3Match;
	}
}