package com.yorker.fanzania.views.screens.tournament.tournamentstats.model;

import com.google.gson.annotations.SerializedName;

public class TournamentStatsModel {

	@SerializedName("ParticipationTeamName")
	private String participationTeamName;

	@SerializedName("PlayerRank")
	private int playerRank;

	@SerializedName("PlayerId")
	private int playerId;

	@SerializedName("TeamImage")
	private String teamImage;

	@SerializedName("PlayerType")
	private String playerType;

	@SerializedName("PlayerName")
	private String playerName;

	@SerializedName("PlayerSpeciality")
	private String playerSpeciality;

	@SerializedName("TeamDescription")
	private String teamDescription;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("PlayerShortName")
	private String playerShortName;

	@SerializedName("TotalPoints")
	private int totalPoints;

	@SerializedName("LeagueRank")
	private int leagueRank;

	@SerializedName("LeagueName")
	private String leagueName;

	@SerializedName("LeaguePoints")
	private int leaguePoints;

	@SerializedName("LeagueOwnerId")
	private int leagueOwnerId;

	@SerializedName("LeagueId")
	private int leagueId;

	@SerializedName("LeagueOwner")
	private String leagueOwner;

	@SerializedName("UserTeamId")
	private int userTeamId;

	@SerializedName("Owner")
	private String owner;

	@SerializedName("TeamRank")
	private int teamRank;

	@SerializedName("UserTeamName")
	private String userTeamName;

	public String getParticipationTeamName(){
		return participationTeamName;
	}

	public int getPlayerRank(){
		return playerRank;
	}

	public int getPlayerId(){
		return playerId;
	}

	public String getTeamImage(){
		return teamImage;
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

	public String getTeamDescription(){
		return teamDescription;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public String getPlayerShortName(){
		return playerShortName;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public int getLeagueRank(){
		return leagueRank;
	}

	public String getLeagueName(){
		return leagueName;
	}

	public int getLeaguePoints(){
		return leaguePoints;
	}

	public int getLeagueOwnerId(){
		return leagueOwnerId;
	}

	public int getLeagueId(){
		return leagueId;
	}

	public String getLeagueOwner(){
		return leagueOwner;
	}

	public int getUserTeamId(){
		return userTeamId;
	}

	public String getOwner(){
		return owner;
	}

	public int getTeamRank(){
		return teamRank;
	}

	public String getUserTeamName(){
		return userTeamName;
	}



}