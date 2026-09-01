package com.yorker.fanzania.views.screens.matchcontest.fragments.home.model;

import com.google.gson.annotations.SerializedName;

public class UpcomingDailyMatchModel{

	@SerializedName("Team1Image")
	private String team1Image;

	@SerializedName("Venue")
	private String venue;

	@SerializedName("TournamentStatus")
	private String tournamentStatus;

	@SerializedName("MatchScheduledDate")
	private String matchScheduledDate;

	@SerializedName("MatchStage")
	private String matchStage;

	@SerializedName("MatchDate")
	private String matchDate;

	@SerializedName("MatchNo")
	private int matchNo;

	@SerializedName("TournamentType")
	private String tournamentType;

	@SerializedName("MatchType")
	private String matchType;

	@SerializedName("WeeklyActive")
	private boolean weeklyActive;

	@SerializedName("MatchScheduledTime")
	private String matchScheduledTime;

	@SerializedName("MatchId")
	private int matchId;

	@SerializedName("Team1ShortName")
	private String team1ShortName;

	@SerializedName("TournamentName")
	private String tournamentName;

	@SerializedName("MatchStatus")
	private String matchStatus;

	@SerializedName("TournamentId")
	private int tournamentId;

	@SerializedName("Team2")
	private String team2;

	@SerializedName("Team2Image")
	private String team2Image;

	@SerializedName("Team1")
	private String team1;

	@SerializedName("Team2ShortName")
	private String team2ShortName;

	public String getTeam1Image(){
		return team1Image;
	}

	public String getVenue(){
		return venue;
	}

	public String getTournamentStatus(){
		return tournamentStatus;
	}

	public String getMatchScheduledDate(){
		return matchScheduledDate;
	}

	public String getMatchStage(){
		return matchStage;
	}

	public String getMatchDate(){
		return matchDate;
	}

	public int getMatchNo(){
		return matchNo;
	}

	public String getTournamentType(){
		return tournamentType;
	}

	public String getMatchType(){
		return matchType;
	}

	public boolean isWeeklyActive(){
		return weeklyActive;
	}

	public String getMatchScheduledTime(){
		return matchScheduledTime;
	}

	public int getMatchId(){
		return matchId;
	}

	public String getTeam1ShortName(){
		return team1ShortName;
	}

	public String getTournamentName(){
		return tournamentName;
	}

	public String getMatchStatus(){
		return matchStatus;
	}

	public int getTournamentId(){
		return tournamentId;
	}

	public String getTeam2(){
		return team2;
	}

	public String getTeam2Image(){
		return team2Image;
	}

	public String getTeam1(){
		return team1;
	}

	public String getTeam2ShortName(){
		return team2ShortName;
	}
}