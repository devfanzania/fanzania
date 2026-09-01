using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class PlayerModel
    {
        public string PId { get; set; }
        public string PSelect { get; set; }
    }

    public class ResponseModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
    }
    public class PlayerResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<Playerlist> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class Playerlist
    {
        public string MatchId { get; set; }
        public string PainKillerPlayerPoint { get; set; }
        public string TournamentId { get; set; }
        public string UserTeamName { get; set; }
        public string UserTeamId { get; set; }
        public string UserId { get; set; }
        public string TeamCapt { get; set; }
        public string TeamVCapt { get; set; }
        public string SubsLeft { get; set; }
        public string SubsLeftAtSnapShot { get; set; }
        public string NitroLeft { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
        public int TotalPoints { get; set; }
        public string TeamRank { get; set; }
        public string TeamCompositionId { get; set; }
        public string NitroUsed { get; set; }
        public string PainKillerUsed { get; set; }
        public string AutoPilotUsed { get; set; }
        public string PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerShortName { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public int PlayerValue { get; set; }
        public string PlayerDesc { get; set; }
        public string PlayerImage { get; set; }
        public string ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string PSelected { get; set; }
        public string MatchTotalPoints { get; set; }
        public string NitroMultiplier { get; set; }
        public string PlayerPoints { get; set; }
        public string TeamShortName { get; set; }
        public string AveragePoints { get; set; }
        public string TeamImage { get; set; }
        public string LastMatchTeams { get; set; }
        //public string ParticipationTeamName { get; set; }
        public string TeamDescription { get; set; }
        public bool PlayingInd { get; set; }
        public string WinnerPrediction { get; set; }
        public bool WinnerPredictionStatus { get; set; }

    }
    public class LeagueSubResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LeagueSubList> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class LeagueSubList
    {
        public int UserId { get; set; }
        public int LeagueId { get; set; }
        public string Name { get; set; }
        public string UserTeamName { get; set; }
        public int SubscriptionType { get; set; }
        public string LivePackageAmount { get; set; }
        public string PrizePackageAmount { get; set; }
        public string FullPackageAmount { get; set; }
        public int DiscountRate1 { get; set; }
        public int DiscountRate2 { get; set; }
        public int DiscountRate3 { get; set; }
        public string client_id { get; set; }
        public string client_secret { get; set; }

        public float LivePackageAmount_USD { get; set; }
        public float PrizePackageAmount_USD { get; set; }
        public float FullPackageAmount_USD { get; set; }
        public float LivePackageAmount_GBP { get; set; }
        public float PrizePackageAmount_GBP { get; set; }
        public float FullPackageAmount_GBP { get; set; }
    }
    public class FetchManualScoreResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<FetchManualScoreResponceData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class ManualScoreUpdateEachPlayerResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }

        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class UpdateKycStatusResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<KycRes> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class KycRes {

        public string KYCStatus { get; set; }

    }
    public class verifyotpResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }

        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class FetchManualScoreResponceData {

        public string APIPId { get; set; }
        public string PlayerName { get; set; }
        public string BattingPoints { get; set; }
        public string BowlingPoints { get; set; }
        public string FieldingPoints { get; set; }
        public string TotalPoints { get; set; }
        public string MoM { get; set; }
    }

    public class TeamResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TeaMDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TeaMDataResponse
    {
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string TournamentId { get; set; }
        public string UserId { get; set; }
        public string SubsLeft { get; set; }
        public string TeamGlobalRank { get; set; }
        public string TotalPoints { get; set; }
        public string TeamCompositionId { get; set; }
        public string TeamRank { get; set; }
        public string CreatedDate { get; set; }
        public string ModifiedDate { get; set; }
        public string NitroLeft { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
        public string TeamPercentile { get; set; }
        public string NitroUsed { get; set; }
        public string PainKillerUsed { get; set; }
        public string AutoPilotUsed { get; set; }
        public string SubsLeftAtSnapShot { get; set; }

    }

    public class TeamFilterResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TeamFilterDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TeamFilterDataResponse
    {
        public string TournamentId { get; set; }
        public string ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamShortName { get; set; }
        public string TeamImage { get; set; }

    }


    public class MatchResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<MatchDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }

    public class MatchDataResponse
    {
        public string MatchId { get; set; }
        public string MatchNo { get; set; }
        public string MatchType { get; set; }
        public string Venue { get; set; }
        public string MatchStage { get; set; }
        public string TournamentId { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string Team1ShortName { get; set; }
        public string Team2ShortName { get; set; }
        public string Winner { get; set; }
        public string Loser { get; set; }
        public string Draw { get; set; }
        public string TotalPoints { get; set; }
        public string MatchScheduledDate { get; set; }
        public string MatchScheduledTime { get; set; }
        public string MatchStatus { get; set; }
        public string MatchDate { get; set; }
        public string Inning1BattingTeam { get; set; }
        public string Inning2BattingTeam { get; set; }
        public string TossWinner { get; set; }
        public string Team1Image { get; set; }
        public string Team2Image { get; set; }
        public string Team1Score { get; set; }
        public string Team2Score { get; set; }
        public bool ShowScore { get; set; }
        public string Weather { get; set; }
        public string BattingTeam { get; set; }
    }
    public class TeamPointsC
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TeamPointsComparisonResponce> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TeamPointsComparisonResponce
    {
        public string MatchId { get; set; }
        public string MatchNo { get; set; }
        public string MatchStatus { get; set; }
        public string MyMatchTotalPoints { get; set; }
        public string OtherMatchTotalPoints { get; set; }

    }
    public class LiveTeamScore
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LiveTeamScoreResponce> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class LiveTeamScoreResponce
    {
        public string TournamentId { get; set; }
        public string MatchId { get; set; }
        public string MyPlayerId { get; set; }
        public string MyPlayerName { get; set; }
        public string MyPlayerType { get; set; }
        public string MyPlayerSpeciality { get; set; }
        public string MyTotalPoints { get; set; }
        public string MyPlayerSelected { get; set; }
        public string MyCapt { get; set; }
        public string MyVCapt { get; set; }
        public string MyTeamName { get; set; }
        public string MyPowerPlay { get; set; }
        public string OtherPlayerId { get; set; }
        public string OtherPlayerName { get; set; }
        public string OtherPlayerType { get; set; }
        public string OtherPlayerSpeciality { get; set; }
        public string OtherTotalPoints { get; set; }
        public string OtherPlayerSelected { get; set; }
        public string OtherCapt { get; set; }
        public string OtherVCapt { get; set; }
        public string OtherTeamName { get; set; }
        public string OtherPowerPlay { get; set; }
        public string MyPrediction { get; set; }
        public string OtherPrediction { get; set; }
    }
  

        public class UserTeamResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<UserTeamDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class UserTeamDataResponse
    {
        
        public string WinnerPrediction { get; set; }
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string UserId { get; set; }
        public string SubsLeft { get; set; }
        public string NumberOfSubs { get; set; }
        public string NitroLeft { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
        public string TotalPoints { get; set; }
        public string TeamCompositionId { get; set; }
        public string TeamRank { get; set; }
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
        public string CreatedByUserId { get; set; }
        public string CreatedDateTime { get; set; }
        public string UpdatedDateTime { get; set; }
        public string NitroUsed { get; set; }
        public string TeamVCapt { get; set; }
        public string PainKillerUsed { get; set; }
        public string AutoPilotUsed { get; set; }
        public string authtoken { get; set; }

    }

    public class TeamRulesResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TeamRulesDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TeamRulesDataResponse
    {
        public string TeamSelectionRuleId { get; set; }
        public string TournamentId { get; set; }
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
        public string SubCount { get; set; }
        public string NitroCount { get; set; }
        public string PainKillerCount { get; set; }
        public string AutoPilotCount { get; set; }


    }

    public class NotificationResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<NotificationData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class NotificationData
    {
        public string NotificationId { get; set; }
        public string UserId { get; set; }
        public string Message { get; set; }
        public string MessageType { get; set; }
        public string Active { get; set; }
        public string UpdateDate { get; set; }
        public string InsertDate { get; set; }
        public string Title { get; set; }
        public string NotificationCount { get; set; }
       
    }
    
}
