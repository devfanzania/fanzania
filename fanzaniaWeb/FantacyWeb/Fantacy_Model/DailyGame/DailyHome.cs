using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;


namespace Fantacy_Model.DailyGame
{
    public class DailyHome
    {
    }
    public class FeachPlayerModel
    {
        public string UserId { get; set; }
        public string MatchStatus { get; set; }
        public string FetchAll { get; set; }
        public string statusMessage { get; set; }
        public string authtoken { get; set; }
        public int TournamentId { get; set; }
        public string MatchId { get; set; }
        public string UserTeamId { get; set; }
        public string MatchType { get; set; }
        public string PlayerSearch { get; set; }
        public string FilterTeams { get; set; }
        public string PlayerType { get; set; }
        public string PlayerId { get; set; }
        public string PlayerSelectAs { get; set; }
        public string Team { get; set; }
        public int Inning { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentFilter { get; set; }
        public string FilterType { get; set; }
        public string ParticipationTeamId { get; set; }
      
        public string APIPId { get; set; }
      
    }
    public class PlayerResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public Player data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class Player
    {
        public string tournamentId { get; set; }
        public string tournamentName { get; set; }
        public string playerName { get; set; }
        public string teamShortName { get; set; }
        public string PlayerSpeciality { get; set; }
        public string playerValue { get; set; }
        public string playerTotalPoints { get; set; }
        public string playerRank { get; set; }
        public string selectedBy { get; set; }
        public string playerPoints1 { get; set; }
        public string playerPoints2 { get; set; }
        public string playerPoints3 { get; set; }
        public string playerPoints4 { get; set; }
        public string playerPoints5 { get; set; }
        public string playerRuns1 { get; set; }
        public string playerRuns2 { get; set; }
        public string playerRuns3 { get; set; }
        public string playerRuns4 { get; set; }
        public string playerRuns5 { get; set; }
        public string playerWickets1 { get; set; }
        public string playerWickets2 { get; set; }
        public string playerWickets3 { get; set; }
        public string playerWickets4 { get; set; }
        public string playerWickets5 { get; set; }
        public string playerValueRank { get; set; }
        public string totalPlayers { get; set; }
        public string imageURL { get; set; }
        public string WinnerPrediction { get; set; }

    }

    public class DailyParamModel
    {
        public string UserId { get; set; }
        public string MatchStatus { get; set; }
        public string FetchAll { get; set; }
        public string statusMessage { get; set; }
        public string authtoken { get; set; }
        public int TournamentId { get; set; }
        public string MatchId { get; set; }
        public string UserTeamId { get; set; }
        public string MatchType { get; set; }
        public string PlayerSearch { get; set; }
        public string FilterTeams { get; set; }
        public string PlayerType { get; set; }
        public string PlayerId { get; set; }
        public string PlayerSelectAs { get; set; }
        public string Team { get; set; }
        public int Inning { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentFilter { get; set; }
        public string FilterType { get; set; }
        public string WinnerPrediction { get; set; }
        
    }
    public class DailyMatchModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyMatchDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class DailyMatchDetailsModel
    {
        public int TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentType { get; set; }
        public string MatchId { get; set; }
        public string MatchNo { get; set; }
        public string MatchType { get; set; }
        public string Venue { get; set; }
        public string MatchStage { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string MatchScheduledDate { get; set; }
        public string MatchStatus { get; set; }
        public string MatchDate { get; set; }
        public string MatchScheduledTime { get; set; }
        public string Team1ShortName { get; set; }
        public string Team2ShortName { get; set; }
        public string Team1Image { get; set; }
        public string Team2Image { get; set; }
        public bool WeeklyActive { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string WeeklyPoints { get; set; }
        public string WeeklyRank { get; set; }
        public string TotalPoints { get; set; }
        public string Team1Score { get; set; }
        public string Team2Score { get; set; }
        public string TeamRank { get; set; }
        public bool ShowScore { get; set; }

    }
    public class DailyLeagueTeamModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyLeagueTeamDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class DailyLeagueTeamDetailsModel
    {
        public string TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string LeagueId { get; set; }
        public string LeagueName { get; set; }
        public string TeamNewLeagueRank { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string TeamRank { get; set; }
        public string UserId { get; set; }
        public string Name { get; set; }
        public string LastMatchPoints { get; set; }
        public string TotalTeams { get; set; }
        public string OwnerTeam { get; set; }
        public string MatchId { get; set; }

    }

    public class DailyLiveLeagueUsersModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyLiveLeagueUsersDetailModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class DailyLiveLeagueUsersDetailModel
    {
        public string TournamentId { get; set; }
        public string MatchId { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string LeagueId { get; set; }
        public string LeagueName { get; set; }
        public string UserId { get; set; }
        public string UserName { get; set; }
        public string TeamNewStanding { get; set; }
        public string TotalPoints { get; set; }
        public string CurrentMatchPoints { get; set; }
       
    }

    public class DailyUserPlayerModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyUserPlayerDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }

    public class DailyUserPlayerDetailsModel
    {
       
        public string TournamentId { get; set; }
        public string UserTeamName { get; set; }
        public string UserTeamId { get; set; }
        public string MatchId { get; set; }
        public string UserId { get; set; }
        public string TeamCapt { get; set; }
        public string TeamVCapt { get; set; }
        public string TeamCompositionId { get; set; }
        public string PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerShortName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public int PlayerValue { get; set; }
        public string PlayerDesc { get; set; }
        public string PlayerPoints { get; set; }
        public string AveragePoints { get; set; }
        public string ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamImage { get; set; }
        public string TeamShortName { get; set; }
        public string MatchTotalPoints { get; set; }
        public string TotalTeams { get; set; }
        public int TotalPoints { get; set; }
        public string PlayerStatus { get; set; }
        public string PSelected { get; set; }
        public bool PlayingInd { get; set; }
        public string WinnerPrediction { get; set; }
        public string BattingTeam { get; set; }

    }
    public class DailyUserTeamResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyUserTeamDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class DailyUserTeamDataResponse
    {
        public string WinnerPrediction { get; set; }
        public string TournamentId { get; set; }
        public string MatchId {get; set;}
        public string UserId { get; set; }
        public string Player1 { get; set; }
        public string Player2 { get; set; }
        public string Player3 { get; set; }
        public string Player4 { get; set; }
        public string Player5 { get; set; }
        public string Player6 { get; set; }
        public string Player7 { get; set; }
        public string Player8 { get; set; }
        public string Player9 { get; set; }
        public string Player10 { get; set; }
        public string Player11 { get; set; }
        public string TeamCapt { get; set; }
        public string TeamVCapt { get; set; }
        public string authtoken { get; set; }

    }

    public class DailyTeamRulesResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyTeamRulesDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class DailyTeamRulesDataResponse
    {
        public string WicketKeeper { get; set; }
        public string MaxWicketKeeper { get; set; }
        public string MaxBatsman { get; set; }
        public string MinBatsman { get; set; }
        public string MaxBowler { get; set; }
        public string MinBowler { get; set; }
        public string MaxAllrounder { get; set; }
        public string MinAllrounder { get; set; }
        public string MaxSameTeamPlayer { get; set; }
        public string TotalPlayers { get; set; }
        public string TotalBudget { get; set; }
        public string MaxOverseasPlayer { get; set; }
     }

    public class DailyPlayerModel
    {
        public string PId { get; set; }
        public string PSelect { get; set; }
    }
    public class DailyResponseModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
    }

    public class DailyMatchScore
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyMatchScoreData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class DailyMatchScoreData
    {
        public string TournamentId { get; set; }
        public string PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public string MatchId { get; set; }
        public string BattingPoints { get; set; }
        public string BowlingPoints { get; set; }
        public string FieldingPoints { get; set; }
        public string TotalPoints { get; set; }
        public string ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string CurrentMatchPoints { get; set; }
        public string AllTotalPoints { get; set; }
        public string Capt { get; set; }
        public string VCapt { get; set; }
        public string PlayerSelected { get; set; }
        public string UserTeamName { get; set; }
        public string PowerPlay { get; set; }

    }

    public class DailyLiveScore
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyLiveScoreData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class DailyLiveScoreData
    {
        public string TournamentId { get; set; }
        public string MatchId { get; set; }
        public string PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public string BattingPoints { get; set; }
        public string BowlingPoints { get; set; }
        public string FieldingPoints { get; set; }
        public string TotalPoints { get; set; }
        public string Capt { get; set; }
        public string VCapt { get; set; }
        public string PlayerSelected { get; set; }

    }
    public class LiveScoreBoard
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LiveScoreBoardData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class LiveScoreBoardData
    {
        public string MatchId { get; set; }
        public int Inning { get; set; }
        public string InningDesc { get; set; }
        public string PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerIndicator { get; set; }
        public string TeamSortName { get; set; }
        public string Team { get; set; }
        public string BTrunScored { get; set; }
        public string BTballfaced { get; set; }
        public string BTrun6s { get; set; }
        public string BTrun4s { get; set; }
        public string BTdismissalinfo { get; set; }
        public string BTstrikerate { get; set; }
        public string BLover { get; set; }
        public string BLmaiden { get; set; }
        public string BLrun { get; set; }
        public string BLwicket { get; set; }
        public string BLecon { get; set; }
        public string BL6s { get; set; }
        public string BL4s { get; set; }
        public string Team1Score { get; set; }
        public string Team2Score { get; set; }
        public string MatchSummary { get; set; }
        public string Team1Extras { get; set; }
        public string Team2Extras { get; set; }
        public string Team1RR { get; set; }
        public string Team2RR { get; set; }

    }

    public class DailyTournament
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DailyTournamentData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class DailyTournamentData
    {
        public string TournamentName { get; set; }
       
    }

    public class GetGunFact
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<GetGunFactData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class GetGunFactData
    {
        public string FunFactId { get; set; }
        public string FunMessage { get; set; }

    }
}
