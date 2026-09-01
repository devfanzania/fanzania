using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class LeagueStat
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LeagueStatdata> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class LeagueStatdata
    {
        public string TeamRank { get; set; }
        public string UserId { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string TeamOwner { get; set; }
        public string Player1 { get; set; }
        public string Player2 { get; set; }
        public string Player3 { get; set; }
        public string Player1Points { get; set; }
        public string Player2Points { get; set; }
        public string Player3Points { get; set; }
        public string Player1Match { get; set; }
        public string Player2Match { get; set; }
        public string Player3Match { get; set; }

    }
    public class TeamStat
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TeamStatdata> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class TeamStatdata
    {
        public string PlayerRank { get; set; }
        public string PlayerId { get; set; }
        public string TournamentId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerShortName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public string TotalPoints { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamShortName { get; set; }
        public string TeamImage { get; set; }
        public string MatchId { get; set; }
        public string MatchNo { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string CaptainId { get; set; }
        public string Captain { get; set; }
        public string TeamFrom { get; set; }

    }

    public class GlobalTopPlayer
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<GlobalTopPlayerData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class GlobalTopPlayerData
    {
        public string PlayerRank { get; set; }
        public string PlayerId { get; set; }
        public string TournamentId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerShortName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public string TotalPoints { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamDescription { get; set; }
        public string TeamImage { get; set; }

    }

    public class GlobalTopLeague
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<GlobalTopLeagueData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    
    public class GlobalTopLeagueData
    {
        public string LeagueId { get; set; }
        public string LeagueName { get; set; }
        public string LeagueRank { get; set; }
        public string LeaguePoints { get; set; }
        public string LeagueOwner { get; set; }
        public string LeagueOwnerId { get; set; }
       

    }

    public class GlobalTopTeam
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<GlobalTopTeamData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
   
    public class GlobalTopTeamData
    {
        public string TeamRank { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string TotalPoints { get; set; }
        public string Owner { get; set; }
       
    }

    public class MatchScore
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<MatchScoreData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class MatchScoreData
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

    public class liveleagueUser
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<liveleagueUserData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    
    public class liveleagueUserData
    {
        public string TournamentId { get; set; }
        public string MatchId { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string LeagueId { get; set; }
        public string LeagueName { get; set; }
        public string UserId { get; set; }
        public string UserName { get; set; }
        public string TeamOldStanding { get; set; }
        public string TeamNewStanding { get; set; }
        public string TotalPoints { get; set; }
        public string Transfers { get; set; }
        public string PowerPlay { get; set; }
        public string CurrentMatchPoints { get; set; }
        public string TransferUsed { get; set; }
    }


    public class LiveScore
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LiveScoreData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    
    public class LiveScoreData
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

    public class LiveScoreBoardT
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LiveScoreBoardDataT> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class LiveScoreBoardDataT
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
}
