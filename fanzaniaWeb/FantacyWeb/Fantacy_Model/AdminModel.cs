using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;


namespace Fantacy_Model
{

    public class AdminModel
    {
        public int UserId { get; set; }

        [Required(ErrorMessage = "Enter username!")]
        public string UserName { get; set; }

        [DataType(DataType.Password)]
        [Required(ErrorMessage = "Enter password!")]
        public string Password { get; set; }

        public string NewPassword { get; set; }
        public string UserType { get; set; }
        public string Active { get; set; }
        public string CreatedBy { get; set; }
        public string Email { get; set; }
        public string SubscriptionType { get; set; }
        public string PromotionType { get; set; }
        public string LeaguePin { get; set; }
    }
    public class UpdateUserPoints {

        public string MatchId { get; set; }
        public string PlayerId { get; set; }
        public string BattingPoints { get; set; }
        public string BowlingPoints { get; set; }
        public string FieldingPoints { get; set; }
    }
    public class DetailsUpdateUserPoints
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
      
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class ResetSub
    {
        public string status { get; set; }
        public string statusMessage { get; set; }

        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }


    public class AdminListModel
    {
        public string SelectedTournament { get; set; }
        public SelectList TournamentList { get; set; }

    }

    public class AdminLoginModel
    {
        [Required(ErrorMessage = "Please enter user name!")]
        public string UserName { get; set; }

        [Required(ErrorMessage = "Please enter Password!")]
        public string Password { get; set; }
    }

    public class AdminTournamentPointModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TournamentPointModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class TournamentPointModel
    {
        public int PointRulesId { get; set; }
        public string TournamentId { get; set; }
        public string TournamentStage { get; set; }
        public string RunScored { get; set; }
        public string FourBonus { get; set; }
        public string SixBonus { get; set; }
        public string HalfCenturyBonus { get; set; }
        public string CenturyBonus { get; set; }
        public string DismissalDuck { get; set; }
        public string MinBall4SR { get; set; }
        public string StrikeRateBelow50 { get; set; }
        public string StrikeRate50To60 { get; set; }
        public string StrikeRate60To70 { get; set; }
        public string StrikeRate110To150 { get; set; }
        public string StrikeRateUp150 { get; set; }
        public string WicketTaken { get; set; }
        public string Wicket3UpBonus { get; set; }
        public string Wicket5UpBonus { get; set; }
        public string MaidenOver { get; set; }
        public string Hattrick { get; set; }
        public string MinOver4ER { get; set; }
        public string EconomyBelow4 { get; set; }
        public string Economy4To5 { get; set; }
        public string Economy5To6 { get; set; }
        public string Economy9To11 { get; set; }
        public string EconomyUp11 { get; set; }
        public string Captain { get; set; }
        public string ViceCaptain { get; set; }
        public string CatchTaken { get; set; }
        public string Stumping { get; set; }
        public string RunOutDirect { get; set; }
        public string RunOutThrower { get; set; }
        public string RunOutCatcher { get; set; }
        public string Nitro { get; set; }
        public string MoM { get; set; }

        public string xmlData { get; set; }

    }

    public class AdminTournamentRuleResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TournamentRuleModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class TournamentRuleModel
    {
        public string TournamentId { get; set; }
        public string WicketKeeper { get; set; }
        public string MaxBatsman { get; set; }
        public string MinBatsman { get; set; }
        public string MaxBowler { get; set; }
        public string MinBowler { get; set; }
        public string MaxAllrounder { get; set; }
        public string MinAllrounder { get; set; }
        public string MaxOverseasPlayer { get; set; }

        public string TotalPlayers { get; set;}
        public string MaxSameTeamPlayer { get; set; }
        public string TotalBudget { get; set; }
        public string SubCount { get; set; }
        public string NitroCount { get; set; }
        public string PainKillerCount { get; set; }
        public string AutoPilotCount { get; set; }

    }

    public class TournamentMainModel
    {
        public int TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentStage { get; set; }
        public string TournamentStartDate { get; set; }
        public string TournamentEndDate { get; set; }
        public string TournamentType { get; set; }
        public string TournamentKey { get; set; }
    }
    public class UpdateSubadmin
    {
        public string Email { get; set; }
        public string SubscriptionTier { get; set; }
        public string receipt { get; set; }
        public string amount { get; set; }
      

    }
    public class SubResponceadmin
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class AdminResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<AdminDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class AdminDataResponse
    {
        public int TournamentId { get; set; }
        public string MatchId { get; set; }
        public string PlayerId { get; set; }
       
    }

    public class AdminMatchModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<AdminMatchDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class AdminMatchDetailsModel
    {
        public int TournamentId { get; set; }
        public string TournamentName { get; set; }
        public int MatchId { get; set; }
        public string MatchNo { get; set; }
        public string MatchType { get; set; }
        public string Venue { get; set; }
        public string MatchStage { get; set; }
        public string MatchStatus { get; set; }
        public string MatchScheduledDate { get; set; }
        public string MatchScheduledTime { get; set; }
        public string MatchDate { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string UniqueId { get; set; }
        public string xmlData { get; set; }
        public string BattingTeam { get; set; }
        public string Inning { get; set; }
        public string Inning1BattingTeam { get; set; }
        public int RapidMatchId { get; set; }
        public string MatchCity { get; set; }

    }

    public class AdminPlayerModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<AdminPlayerDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class AdminPlayerDetailsModel
    {
        public int TournamentId { get; set; }
        public int PlayerId { get; set; }
        public string PlayerName { get; set; }
        public string PlayerShortName { get; set; }
        public string PlayerDesc { get; set; }
        public string PlayerType { get; set; }
        public string PlayerSpeciality { get; set; }
        public string PlayerValue { get; set; }
        public string ParticipationTeam { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamShortName { get; set; }
        public string ParticipationTeamId { get; set; }
        public string PlayerStatus { get; set; }
        public string APIPId { get; set; }
        public int RapidPlayerId { get; set; }
        public string xmlData { get; set; }
        public string PlayerKey { get; set; }
    }

public class AdminTeamModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<AdminTeamDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class AdminTeamDetailsModel
    {
        public int TournamentId { get; set; }
        public string ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string MatchPlayed { get; set; }
        public string MatchWon { get; set; }
        public string MatchLost { get; set; }
        public string MatchDraw { get; set; }
        public string TeamPoints { get; set; }
        public string TeamDescription { get; set; }
        public string TeamImage { get; set; }
        public string TeamShortName { get; set; }
        public string xmlData { get; set; }
        public int RapidTeamId { get; set; }
        public string TeamKey { get; set; }
    }

    public class AdminAutoTeamModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<AdminAutoTeamDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class AdminAutoTeamDetailsModel
    {
        public int TournamentId { get; set; }
        public string AutoSelectionTeamId { get; set; }
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
        public string Player1Name { get; set; }
        public string Player2Name { get; set; }
        public string Player3Name { get; set; }
        public string Player4Name { get; set; }
        public string Player5Name { get; set; }
        public string Player6Name { get; set; }
        public string Player7Name { get; set; }
        public string Player8Name { get; set; }
        public string Player9Name { get; set; }
        public string Player10Name { get; set; }
        public string Player11Name { get; set; }
        public string TeamCapt { get; set; }
        public string TeamVCapt { get; set; }
        public string TeamCaptName { get; set; }
        public string TeamVCaptName { get; set; }
        public string playerXML { get; set; }

    }


    public class RewardResponseModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<RewardResponseDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class RewardResponseDetailsModel
    {
        public int RewardId { get; set; }
        public string RewardDate { get; set; }
        public string RewardType { get; set; }
        public int UserId { get; set; }
        public int RewardAmount { get; set; }
        public string Details { get; set; }

    }

    public class ClaimUploadDetailsModel
    {
        public string Bundle { get; set; }
        public string Voucher { get; set; }
        public int UserId { get; set; }
        public int ClaimAmount { get; set; }
        public string Comments { get; set; }

    }

    public class RewardDetailResponseModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<RewardDetailResponseDetailsModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
    public class RewardDetailResponseDetailsModel
    {
        public string RewardId { get; set; }
        public string RewardWeek { get; set; }
        public string Comments { get; set; }
        public bool Active { get; set; }
        public string Email { get; set; }
        public string Details { get; set; }
        public string Rank { get; set; }
        public string Name { get; set; }
        public string WeeklyPoints { get; set; }
    }

    public class UploadClaimModel
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public string xmlData { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string ResponseId { get; set; }
    }
}
