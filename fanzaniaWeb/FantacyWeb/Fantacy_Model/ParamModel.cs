using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class ParamModel
    {
        public string GameType { get; set; }
        public string TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string UserId { get; set; }
        public string UserIds { get; set; }
        public string UserTeamId { get; set; }
        public string LeagueId { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSearch { get; set; }
        public string PlayerId { get; set; }
        public string Email { get; set; }
        public string authtoken { get; set; }
        public string UserTeamName { get; set; }
        public string LeagueName { get; set; }
        public string LeagueLeaderId { get; set; }
        public string Type { get; set; }
        public string LeaguePin { get; set; }
        public string UserLeagueId { get; set; }
        public string MatchId { get; set; }
        public string PlayerSelectAs { get; set; }
        public string TournamentStatus { get; set; }
        public string FilterTeams { get; set; }
        public string Team { get; set; }
        public string ParticipationTeamId { get; set; }
        public string Status { get; set; }
        public string PlayerIds { get; set; }
        public string UniqueId { get; set; }
        public string RewardWeek { get; set; }
        public int Inning { get; set; }
        public string MatchStatus { get; set; }
        public string FunMessage { get; set; }
        public string SeriesId { get; set; }
        public string TossWinnerTeam { get; set; }
        public string Weather { get; set; }
        public string BackgroundTheme { get; set; }
        public string Title { get; set; }
        public string Message { get; set; }
        public string OtherTeamId { get; set; }
        public string MyTeamId { get; set; }
        public string TournamentKey { get; set; }
        public string TeamKey { get; set; }
        public string TournamentType { get; set; }
        public string MatchType { get; set; }


    }
    public class UpdatePowerPlayModel
    {
        public string UserTeamId { get; set; }
        public string UserId { get; set; }
        public string TournamentId { get; set; }
        public string NitroUserTeamMatchPointId { get; set; }
        public string PainKillerUserTeamMatchPointId { get; set; }
        public string AutoPilotUserTeamMatchPointId { get; set; }
        public Boolean NitroSelect { get; set; }
        public Boolean PainKillerSelect { get; set; }
        public Boolean AutoPilotSelect { get; set; }
        public string NitroPoints { get; set; }
        public string PainKillerPoints { get; set; }
        public string AutoPilotPoints { get; set; }
    }
    public class UpdatePowerPlayModelResp
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
       
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class UpdatePowerPlayModelRespFeach
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<FetchUserPowerPlay> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class FetchUserPowerPlay
    {
        public Boolean NitroUsed { get; set; }
        public Boolean PainKillerUsed { get; set; }
        public Boolean AutoPilotUsed { get; set; }
        public Boolean NitroEnable { get; set; }
        public Boolean PainKillerEnable { get; set; }
        public Boolean AutoPilotEnable { get; set; }
        public string NitroPoints { get; set; }
        public string PainKillerPoints { get; set; }
        public string AutoPilotPoints { get; set; }
        public string NitroUserTeamMatchPointId { get; set; }
        public string PainKillerUserTeamMatchPointId { get; set; }
        public string AutoPilotUserTeamMatchPointId { get; set; }
        public string UserTeamId { get; set; }
        public string UserId { get; set; }
        public string TournamentId { get; set; }

    }
    public class UpdatePointModel {
        public string UniqueId { get; set; }
        public string APIPId { get; set; }
        public string BattingPoints { get; set; }
        public string BowlingPoints { get; set; }
        public string FieldingPoints { get; set; }
    }

    public class AdminParamModel
    {
        public string GameType { get; set; }
        public string TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string PlayerId { get; set; }
        public string authtoken { get; set; }
        public string MatchId { get; set; }
        public string ParticipationTeamId { get; set; }
        public int Status { get; set; }
        public string PlayerIds { get; set; }
        public string TransferCount { get; set; }

    }
    public class TournamentModel {
        public string TournamentId { get; set; }
    }

    public class FeedbackModel
    {
        public string UserEmail { get; set; }
        public string Name { get; set; }
        public string MessageAbout { get; set; }
        public string Messages { get; set; }
    }
    public class ParamPlayerModel
    {
        public string TournamentId { get; set; }
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

    }

    public class Transfer
    {
        public string Transfer_left { get; set; }
       
    }

    public class MatchResult
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<MatchResultData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class MatchResultData
    {
        public string Name { get; set; }
        public string UserTeamName { get; set; }
        public string MatchDetails { get; set; }
        public string LastMatchPoints { get; set; }
        public string Rank { get; set; }
        public string UserTeamId { get; set; }
    }
    public class GetFunFact
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<GetFunFactData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class GetFunFactData
    {
        public string FunFactId { get; set; }
        public string FunMessage { get; set; }

    }
    public class FetchReferralCode
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<FetchReferralCodeData> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class FetchReferralCodeData
    {
      
        public string ReferralCode { get; set; }

    }
    public class ReferralCodeModel
    {
        public int UserId { get; set; }
    }
    public class FetchKycDetailsAdmin
    {
        public int UserId { get; set; }
        public string KYCStatus { get; set; }
    }
    public class FetchKycDetailsAdminResponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<FetchKycDetailsAdminModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class FetchKycDetailsAdminModel
    {
        public string UserProfileId { get; set; }
        public string UserId { get; set; }
        public string KYCDocName { get; set; }
        public string KYCDocImage { get; set; }
        public string PANName { get; set; }
        public string PANNumber { get; set; }
        public string PANDOB { get; set; }
        public string PANState { get; set; }
        public string KYCStatus { get; set; }
    }
    public class OrderDetails
    {
        public string UserId { get; set; }
        public string cf_order_id { get; set; }
        public string customer_name { get; set; }
        public string customer_email { get; set; }
        public string customer_phone { get; set; }
        public decimal order_amount { get; set; }
        public string order_currency { get; set; }
        public string payment_methods    { get; set; }
        public string order_note { get; set; }
        public string order_status { get; set; }
        public string payment_session_id { get; set; }
        public string order_id { get; set; }
    }

    public class AddMoneyPaymentdetails
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
     
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    
    }
}
