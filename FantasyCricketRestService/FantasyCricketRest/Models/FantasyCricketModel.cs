using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Newtonsoft.Json;

namespace FantasyCricketAppRest.Models
{
    class TestMember
    {
        [JsonExtensionData]
        public Dictionary<string, object> DataItems  { get; set; }
    }

public class HttpResponseStatus
    {
        private HttpResponseStatus() { }
        public const string Success = "success";
        public const string Fail = "fail";
    }
    public class UserModel
    {

        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("userName")]
        public string UserName { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }

        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("firstName")]
        public string FirstName { get; set; }

        [JsonProperty("lastName")]
        public string LastName { get; set; }

        [JsonProperty("countryId")]
        public int CountryId { get; set; }

        [JsonProperty("dob")]
        public string DOB { get; set; }

        [JsonProperty("phoneNumber")]
        public string PhoneNumber { get; set; }

        [JsonProperty("activationToken")]
        public string ActivationToken { get; set; }

        [JsonProperty("active")]
        public int? Active { get; set; }

        [JsonProperty("userRoleId")]
        public int? UserRoleId { get; set; }

        [JsonProperty("signUpDate")]
        public DateTime? SignUpDate { get; set; }

        [JsonProperty("lastSignIn")]
        public DateTime? LastSignIn { get; set; }

        [JsonProperty("lastPasswordFailureDate")]
        public DateTime? LastPasswordFailureDate { get; set; }

        [JsonProperty("passwordFailuresSinceLastSuccess")]
        public string PasswordFailuresSinceLastSuccess { get; set; }

        [JsonProperty("passwordChangedDate")]
        public DateTime? PasswordChangedDate { get; set; }

        //no need to expose as JSON
        public string PasswordSalt { get; set; }

        [JsonProperty("passwordVerificationToken")]
        public string PasswordVerificationToken { get; set; }

        [JsonProperty("passwordVerificationTokenExpirationDate")]
        public DateTime? PasswordVerificationTokenExpirationDate { get; set; }

        [JsonProperty("sessionId")]
        public string SessionId { get; set; }

        [JsonProperty("sessionCreationDate")]
        public string SessionCreationDate { get; set; }

        [JsonProperty("sessionActive")]
        public int? SessionActive { get; set; }

        [JsonProperty("leagueId")]
        public int? LeagueId { get; set; }

        [JsonProperty("leagueApproved")]
        public int? LeagueApproved { get; set; }

        [JsonProperty("leaguePin")]
        public string LeaguePin { get; set; }
        
        [JsonProperty("promotionType")]
        public string PromotionType { get; set; }

        [JsonProperty("subscriptionType")]
        public string SubscriptionType { get; set; }

        [JsonProperty("externalUserID")]
        public string ExternalUserID { get; set; }

        [JsonProperty("loginProviderAccessToken")]
        public string LoginProviderAccessToken { get; set; }

        [JsonProperty("loginProvider")]
        public string LoginProvider { get; set; }

        [JsonProperty("commPreference")]
        public bool CommPreference { get; set; }

        [JsonProperty("referralCode")]
        public string ReferralCode { get; set; }

        [JsonProperty("referralCodeUsed")]
        public string ReferralCodeUsed { get; set; }
        [JsonProperty("backgroundTheme")]
        public string BackgroundTheme { get; set; }

        [JsonProperty("loginPreference")]
        public string LoginPreference { get; set; }
        [JsonProperty("KYCDocName")]
        public string KYCDocName { get; set; }
        [JsonProperty("KYCDocImage")]
        public string KYCDocImage { get; set; }
        [JsonProperty("PANName")]
        public string PANName { get; set; }
        [JsonProperty("PANNumber")]
        public string PANNumber { get; set; }
        [JsonProperty("PANDOB")]
        public string PANDOB { get; set; }
        [JsonProperty("PANState")]
        public string PANState { get; set; }
        [JsonProperty("KYCStatus")]
        public string KYCStatus { get; set; }
        public string Status { get; set; }
        public int SubscriptionTier { get; set; }
        public string receipt { get; set; }
        public double amount { get; set; }
        public string Currency { get; set; }

    }

    public class PaymentCF
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }
        public string cf_order_id { get; set; }
        public string customer_name { get; set; }
        public string customer_email { get; set;}
        public string customer_phone { get; set;}
        public double order_amount { get; set; }
        public string order_currency { get; set; }
        public string payment_methods { get; set; }
        public string order_note { get; set; }
        public string order_status { get; set; }
        public string payment_session_id { get; set; }
    }

    public class BulkSubscriptionModel
    {
        public int LeagueId { get; set; }
        public int LoggedInUserId { get; set; }
        public int UserId { get; set; }
        public int SubscriptionType { get; set; }
        public double amount { get; set; }
        public double DiscountTotal { get; set; }
        public string receipt { get; set; }
        public string Currency { get; set; }
    }

    public class UpdateUserPowerPlayModel
    {
        public int UserTeamId { get; set; }
        public int UserId { get; set; }
        public int TournamentId { get; set; }
        public int NitroPoints { get; set; }
        public int PainKillerPoints { get; set; }
        public int AutoPilotPoints { get; set; }
        public int NitroUserTeamMatchPointId { get; set; }
        public int PainKillerUserTeamMatchPointId { get; set; }
        public int AutoPilotUserTeamMatchPointId { get; set; }
        public bool NitroSelect { get; set; }
        public bool PainKillerSelect { get; set; }
        public bool AutoPilotSelect { get; set; }

    }

    public class LeagueModel
    {
        [JsonProperty("leagueId")]
        public int LeagueId { get; set; }

        [JsonProperty("leagueName")]
        public string LeagueName { get; set; }

        [JsonProperty("leaguePin")]
        public string LeaguePin { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("leagueLeader")]
        public string LeagueLeader { get; set; }

        [JsonProperty("leagueLeaderId")]
        public int LeagueLeaderId { get; set; }

        [JsonProperty("leagueUser")]
        public string LeagueUser { get; set; }

        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("leaguePoints")]
        public int? LeaguePoints { get; set; }
    }

    public class PlayerListModel
    {
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("player1")]
        public int Player1 { get; set; }

        [JsonProperty("player2")]
        public int Player2 { get; set; }

        [JsonProperty("player3")]
        public int Player3 { get; set; }

        [JsonProperty("player4")]
        public int Player4 { get; set; }

        [JsonProperty("player5")]
        public int Player5 { get; set; }

        [JsonProperty("player6")]
        public int Player6 { get; set; }

        [JsonProperty("player7")]
        public int Player7 { get; set; }

        [JsonProperty("player8")]
        public int Player8 { get; set; }

        [JsonProperty("player9")]
        public int Player9 { get; set; }

        [JsonProperty("player10")]
        public int Player10 { get; set; }

        [JsonProperty("player11")]
        public int Player11 { get; set; }


    }

    public class UserTeamModel
    {
        [JsonProperty("userTeamId")]
        public int UserTeamId { get; set; }

        [JsonProperty("userTeamName")]
        public string UserTeamName { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("userId")]
        public int UserId { get; set; }
        public int MatchId { get; set; }
    }

    public class UpdateSubscriptionModel
    {
        public int SubscriptionType { get; set; }
        public string Email { get; set; }
    }

    public class UserCommunicationModel
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("referralCode")]
        public string ReferralCode { get; set; }
    }

    public class AdminUserPointsModel
    {
        public int MatchId { get; set; }
        public int PlayerId { get; set; }
        public int BattingPoints { get; set; }
        public int BowlingPoints { get; set; }
        public int FieldingPoints { get; set; }
    }

    public class TeamCompositionModel
    {
        [JsonProperty("userTeamId")]
        public int UserTeamId { get; set; }
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("matchId")]
        public int MatchId { get; set; }
        [JsonProperty("userId")]
        public int UserId { get; set; }
        [JsonProperty("player1")]
        public int? Player1 { get; set; }
        [JsonProperty("player2")]
        public int? Player2 { get; set; }
        [JsonProperty("player3")]
        public int? Player3 { get; set; }
        [JsonProperty("player4")]
        public int? Player4 { get; set; }
        [JsonProperty("player5")]
        public int? Player5 { get; set; }
        [JsonProperty("player6")]
        public int? Player6 { get; set; }
        [JsonProperty("player7")]
        public int? Player7 { get; set; }
        [JsonProperty("player8")]
        public int? Player8 { get; set; }
        [JsonProperty("player9")]
        public int? Player9 { get; set; }
        [JsonProperty("player10")]
        public int? Player10 { get; set; }
        [JsonProperty("player11")]
        public int? Player11 { get; set; }
        [JsonProperty("teamCapt")]
        public int? TeamCapt { get; set; }
        [JsonProperty("teamVCapt")]
        public int? TeamVCapt { get; set; }
        [JsonProperty("numberOfSubs")]
        public int? NumberOfSubs { get; set; }
        [JsonProperty("nitroUsed")]
        public bool NitroUsed { get; set; }
        [JsonProperty("painKillerUsed")]
        public bool PainKillerUsed { get; set; }
        [JsonProperty("autoPilotUsed")]
        public bool AutoPilotUsed { get; set; }
        public string WinnerPrediction { get; set; }
    }

    public class MatchModel
    {
        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("matchNo")]
        public int MatchNo { get; set; }

        [JsonProperty("uniqueId")]
        public string UniqueId { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("team1")]
        public string Team1 { get; set; }

        [JsonProperty("team2")]
        public string Team2 { get; set; }

        [JsonProperty("inning")]
        public int Inning { get; set; }

        [JsonProperty("battingTeam")]
        public string BattingTeam { get; set; }

        [JsonProperty("tossWinnerTeam")]
        public string TossWinnerTeam { get; set; }
    }

    public class StartMatchModel
    {
        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("matchNo")]
        public int MatchNo { get; set; }

        [JsonProperty("matchDetails")]
        public string MatchDetails { get; set; }

    }

    public class SubscriptionModel
    {
        public String Details { get; set; }
        public String SubTierFree { get; set; }
        public String SubTier1 { get; set; }
        public String SubTier2 { get; set; }
        public String SubTier3 { get; set; }
        public int LivePackageAmount { get; set; }
        public int PrizePackageAmount { get; set; }
        public int FullPackageAmount { get; set; }
        public double LivePackageAmount_USD { get; set; }
        public double PrizePackageAmount_USD { get; set; }
        public double FullPackageAmount_USD { get; set; }
        public double LivePackageAmount_GBP { get; set; }
        public double PrizePackageAmount_GBP { get; set; }
        public double FullPackageAmount_GBP { get; set; }
        public int CurrentSubscriptionTier { get; set; }
        public bool SubTier1PayEnable { get; set; }
        public bool SubTier2PayEnable { get; set; }
        public bool SubTier3PayEnable { get; set; }
        public String client_id { get; set; }
        public String client_secret { get; set; }
    }

    public class GetSubscriptionDetailsModel
    {
        public int SubscriptionType { get; set; }
        public string TournamentName { get; set; }
    }

    public class FetchMatchModel
    {
        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("tournamentType")]
        public string TournamentType { get; set; }

        [JsonProperty("matchType")]
        public string MatchType { get; set; }

    }

    public class TeamComparisonModel
    {
        public int MatchId { get; set; }
        public int MatchNo { get; set; }
        public string MatchStatus { get; set; }
        public int MyMatchTotalPoints { get; set; }
        public int OtherMatchTotalPoints { get; set; }
    }

    public class GenericModel
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("leagueId")]
        public int LeagueId { get; set; }

        [JsonProperty("userTeamId")]
        public int UserTeamId { get; set; }

        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("userLeagueId")]
        public int UserLeagueId { get; set; }

        [JsonProperty("matchStatus")]
        public string MatchStatus { get; set; }

        [JsonProperty("matchType")]
        public string MatchType { get; set; }

        [JsonProperty("fetchAll")]
        public string FetchAll { get; set; }

        [JsonProperty("pageIndicator")]
        public int PageIndicator { get; set; }
        public int MyTeamId { get; set; }
        public int OtherTeamId { get; set; }

    }

    
    public class FeedbackModel
    {
        [JsonProperty("userEmail")]
        public string UserEmail { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("messages")]
        public string Messages { get; set; }

        [JsonProperty("messageAbout")]
        public string MessageAbout { get; set; }

        [JsonProperty("status")]
        public string Status { get; set; }

    }

    public class MatchPlayerStatsModel
    {
        [JsonProperty("playerId")]
        public int PlayerId { get; set; }

        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("runs")]
        public int? Runs { get; set; }

        [JsonProperty("wickets")]
        public int? Wickets { get; set; }

        [JsonProperty("catches")]
        public int? Catches { get; set; }

        [JsonProperty("batAvg")]
        public decimal? BatAvg { get; set; }

        [JsonProperty("bowlAvg")]
        public decimal? BowlAvg { get; set; }

        [JsonProperty("econRate")]
        public decimal? EconRate { get; set; }

        [JsonProperty("strikeRate")]
        public decimal? StrikeRate { get; set; }

        [JsonProperty("inning1Points")]
        public int? Inning1Points { get; set; }

        [JsonProperty("inning2Points")]
        public int? Inning2Points { get; set; }

        [JsonProperty("Inning3Points")]
        public int? Inning3Points { get; set; }

        [JsonProperty("inning4Points")]
        public int? Inning4Points { get; set; }

        [JsonProperty("bowlingPoint")]
        public int? BowlingPoint { get; set; }

        [JsonProperty("battingPoint")]
        public int? BattingPoint { get; set; }

        [JsonProperty("totalPoints")]
        public int? TotalPoints { get; set; }
    }

    public class UploadImage
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("profileImage")]
        public string ProfileImage { get; set; }

        [JsonProperty("ext")]
        public string Ext { get; set; }

    }
    public class UploadCustomerImageModel
    {
        public int UserId { get; set; }
        public string ImageData { get; set; }
    }

    public class AdminTournamentModel
    {
        [JsonProperty("tournamentId")]
        public int? TournamentId { get; set; }

        [JsonProperty("tournamentName")]
        public string TournamentName { get; set; }

        [JsonProperty("tournamentStatus")]
        public string TournamentStatus { get; set; }

        [JsonProperty("tournamentStage")]
        public string TournamentStage { get; set; }

        [JsonProperty("tournamentStartDate")]
        public string TournamentStartDate { get; set; }

        [JsonProperty("tournamentEndDate")]
        public string TournamentEndDate { get; set; }

        [JsonProperty("tournamentType")]
        public string TournamentType { get; set; }
        public string TournamentKey { get; set; }
    }

    public class AdminParticipationTeamModel
    {
        [JsonProperty("participationTeamId")]
        public int? ParticipationTeamId { get; set; }

        [JsonProperty("participationTeamName")]
        public string ParticipationTeamName { get; set; }

        [JsonProperty("teamShortName")]
        public string TeamShortName { get; set; }

        [JsonProperty("teamDescription")]
        public string TeamDescription { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("teamImage")]
        public string TeamImage { get; set; }
        public string TeamKey { get; set; }

    }

    public class AdminMatchDetailsModel
    {
        [JsonProperty("matchId")]
        public int MatchId { get; set; }

        [JsonProperty("matchNo")]
        public int MatchNo { get; set; }

        [JsonProperty("matchType")]
        public string MatchType { get; set; }

        [JsonProperty("venue")]
        public string Venue { get; set; }

        [JsonProperty("matchStage")]
        public string MatchStage { get; set; }

        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }

        [JsonProperty("tournamentName")]
        public string TournamentName { get; set; }

        [JsonProperty("team1")]
        public string Team1 { get; set; }

        [JsonProperty("team2")]
        public string Team2 { get; set; }

        [JsonProperty("matchStatus")]
        public string MatchStatus { get; set; }

        [JsonProperty("matchScheduledDate")]
        public string MatchScheduledDate { get; set; }

        [JsonProperty("matchDate")]
        public string MatchDate { get; set; }

        [JsonProperty("matchScheduledTime")]
        public string MatchScheduledTime { get; set; }

        [JsonProperty("uniqueId")]
        public string UniqueId { get; set; }

        [JsonProperty("seriesId")]
        public int SeriesId { get; set; }

        [JsonProperty("rapidMatchId")]
        public int RapidMatchId { get; set; }
        [JsonProperty("matchCity")]
        public string MatchCity { get; set; }
        public string TournamentKey { get; set; }
        public string TeamKey { get; set; }
        public string TournamentType { get; set; }

    }

    public class AdminAutoTeamModel
    {
        [JsonProperty("AutoSelectionTeamId")]
        public int? AutoSelectionTeamId { get; set; }
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("player1")]
        public int Player1 { get; set; }
        [JsonProperty("player2")]
        public int Player2 { get; set; }
        [JsonProperty("player3")]
        public int Player3 { get; set; }
        [JsonProperty("player4")]
        public int Player4 { get; set; }
        [JsonProperty("player5")]
        public int Player5 { get; set; }
        [JsonProperty("player6")]
        public int Player6 { get; set; }
        [JsonProperty("player7")]
        public int Player7 { get; set; }
        [JsonProperty("player8")]
        public int Player8 { get; set; }
        [JsonProperty("player9")]
        public int Player9 { get; set; }
        [JsonProperty("player10")]
        public int Player10 { get; set; }
        [JsonProperty("player11")]
        public int Player11 { get; set; }
        [JsonProperty("teamCapt")]
        public int TeamCapt { get; set; }
        [JsonProperty("teamVCapt")]
        public int TeamVCapt { get; set; }

    }

    public class AdminTeamRulesModel
    {
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("wicketKeeper")]
        public int WicketKeeper { get; set; }
        [JsonProperty("maxBatsman")]
        public int MaxBatsman { get; set; }
        [JsonProperty("minBatsman")]
        public int MinBatsman { get; set; }
        [JsonProperty("maxBowler")]
        public int MaxBowler { get; set; }
        [JsonProperty("minBowler")]
        public int MinBowler { get; set; }
        [JsonProperty("maxAllrounder")]
        public int MaxAllrounder { get; set; }
        [JsonProperty("minAllrounder")]
        public int MinAllrounder { get; set; }
        [JsonProperty("maxSameTeamPlayer")]
        public int MaxSameTeamPlayer { get; set; }
        [JsonProperty("maxOverseasPlayer")]
        public int MaxOverseasPlayer { get; set; }
        [JsonProperty("totalPlayers")]
        public int TotalPlayers { get; set; }
        [JsonProperty("totalBudget")]
        public int TotalBudget { get; set; }
        [JsonProperty("subCount")]
        public int SubCount { get; set; }
        [JsonProperty("nitroCount")]
        public int NitroCount { get; set; }
        [JsonProperty("painKillerCount")]
        public int PainKillerCount { get; set; }
        [JsonProperty("autoPilotCount")]
        public int AutoPilotCount { get; set; }
    }

    public class AdminPlayerModel
    {
        [JsonProperty("playerId")]
        public int PlayerId { get; set; }
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("APIPId")]
        public int APIPId { get; set; }
        [JsonProperty("playerName")]
        public string PlayerName { get; set; }
        [JsonProperty("playerShortName")]
        public string PlayerShortName { get; set; }
        [JsonProperty("playerDesc")]
        public string PlayerDesc { get; set; }
        [JsonProperty("playerType")]
        public string PlayerType { get; set; }
        [JsonProperty("playerSpeciality")]
        public string PlayerSpeciality { get; set; }
        [JsonProperty("playerStatus")]
        public bool PlayerStatus { get; set; }
        [JsonProperty("playerValue")]
        public int PlayerValue { get; set; }
        [JsonProperty("participationTeamId")]
        public int ParticipationTeamId { get; set; }
        [JsonProperty("participationTeam")]
        public string ParticipationTeam { get; set; }
        [JsonProperty("rapidPlayerId")]
        public int RapidPlayerId { get; set; }
        public int MatchId { get; set; }
        public string PlayerKey { get; set; }
    }

    public class PlayerStatsModel
    {
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("tournamentName")]
        public string TournamentName { get; set; }
        [JsonProperty("playerName")]
        public string PlayerName { get; set; }
        [JsonProperty("teamShortName")]
        public string TeamShortName { get; set; }
        [JsonProperty("PlayerSpeciality")]
        public string PlayerSpeciality { get; set; }
        [JsonProperty("playerValue")]
        public int PlayerValue { get; set; }
        [JsonProperty("playerTotalPoints")]
        public int PlayerTotalPoints { get; set; }
        [JsonProperty("playerRank")]
        public int PlayerRank { get; set; }
        [JsonProperty("selectedBy")]
        public int SelectedBy { get; set; }
        [JsonProperty("playerPoints1")]
        public string PlayerPoints1 { get; set; }
        [JsonProperty("playerPoints2")]
        public string PlayerPoints2 { get; set; }
        [JsonProperty("playerPoints3")]
        public string PlayerPoints3 { get; set; }
        [JsonProperty("playerPoints4")]
        public string PlayerPoints4 { get; set; }
        [JsonProperty("playerPoints5")]
        public string PlayerPoints5 { get; set; }
        [JsonProperty("playerRuns1")]
        public string PlayerRuns1 { get; set; }
        [JsonProperty("playerRuns2")]
        public string PlayerRuns2 { get; set; }
        [JsonProperty("playerRuns3")]
        public string PlayerRuns3 { get; set; }
        [JsonProperty("playerRuns4")]
        public string PlayerRuns4 { get; set; }
        [JsonProperty("playerRuns5")]
        public string PlayerRuns5 { get; set; }
        [JsonProperty("playerWickets1")]
        public string PlayerWickets1 { get; set; }
        [JsonProperty("playerWickets2")]
        public string PlayerWickets2 { get; set; }
        [JsonProperty("playerWickets3")]
        public string PlayerWickets3 { get; set; }
        [JsonProperty("playerWickets4")]
        public string PlayerWickets4 { get; set; }
        [JsonProperty("playerWickets5")]
        public string PlayerWickets5 { get; set; }
        [JsonProperty("playerValueRank")]
        public int PlayerValueRank { get; set; }
        [JsonProperty("totalPlayers")]
        public int TotalPlayers { get; set; }
        [JsonProperty("imageURL")]
        public string ImageURL { get; set; }
        public int MatchCounter { get; set; }
        public int LastMatchId { get; set; }
    }

    public class FetchPlayerStats
    {
        public int TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string PlayerName { get; set; }
        public string PlayerSpeciality { get; set; }
        public string TeamShortName { get; set; }
        public int PlayerValue { get; set; }
        public string PlayerImage { get; set; }
        public string PlayerStats { get; set; }
        public int SelectedBy { get; set; }
        public int TotalPlayers { get; set; }
        public int PlayerValueRank { get; set; }
        public int PlayerRank { get; set; }
    }

    public class BankDetailsModel
    {
        public int UserId { get; set; }
        public string BankName { get; set; }
        public string IFSC { get; set; }
        public string AccountNumber { get; set; }
        public string BankVerified { get; set; }
        public int amount { get; set; }
    }

    public class FundTransferResponseModel
    {
        public string message { get; set; }
        public bool status { get; set; }
        public string TransactionID { get; set; }
        public int amount { get; set; }
        public string Email { get; set; }
    }

    public class UserContactModel
    {
        public int UserId { get; set; }
        public string PANName { get; set; }
        public string PhoneNumber { get; set; }
        public string Email { get; set; }
        public int SubscriptionType { get; set; }
        public string RPContactId { get; set; }
        public string KYCStatus { get; set; }
        public string BankVerified { get; set; }
        public string RPfaId { get; set; }
    }

    public class RPCreateContactModel
    {
        public string name { get; set; }
        public string email { get; set; }
        public string contact { get; set; }
        public string type { get; set; }
        public string reference_id { get; set; }
    }

    public class BankAccount
    {
        public string name { get; set; }
        public string ifsc { get; set; }
        public string account_number { get; set; }
    }

    public class RPCreateFundAccountModel
    {
        public string contact_id { get; set; }
        public string account_type { get; set; }
        public BankAccount bank_account { get; set; }
    }

    public class Notes
    {
        public string notes_key_1 { get; set; }
        public string notes_key_2 { get; set; }
    }

    public class RPCreatePayoutsModel
    {
        public string account_number { get; set; }
        public string fund_account_id { get; set; }
        public int amount { get; set; }
        public string currency { get; set; }
        public string mode { get; set; }
        public string purpose { get; set; }
        public bool queue_if_low_balance { get; set; }
        public string reference_id { get; set; }
        public string narration { get; set; }
        public Notes notes { get; set; }
    }

    public class RPCreatePayoutsResponseModel
    {
        public string id { get; set; }
        public string entity { get; set; }
        public string fund_account_id { get; set; }
        public int amount { get; set; }
        public string currency { get; set; }
        public Notes notes { get; set; }
        public decimal fees { get; set; }
        public decimal tax { get; set; }
        public string status { get; set; }
        public string utr { get; set; }
        public string mode { get; set; }
        public string purpose { get; set; }
        public string reference_id { get; set; }
        public string narration { get; set; }
        public string batch_id { get; set; }
        public string failure_reason { get; set; }
        public int created_at { get; set; }
    }

    public class RPCreateContactResponseModel
    {
        public string id { get; set; }
        public string entity { get; set; }
        public string name { get; set; }
        public string contact { get; set; }
        public string email { get; set; }
        public string type { get; set; }
        public string reference_id { get; set; }
        public string batch_id { get; set; }
        public bool active { get; set; }
        public Object notes { get; set; }
        public int created_at { get; set; }
    }


    public class AdminGenericModel
    {
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("uniqueId")]
        public string UniqueId { get; set; }
        [JsonProperty("xmlData")]
        public string xmlData { get; set; }
        [JsonProperty("playerIds")]
        public string PlayerIds { get; set; }
        [JsonProperty("matchIds")]
        public string MatchIds { get; set; }
        [JsonProperty("status")]
        public bool Status { get; set; }
    }

    public class APIPlayerData
    {
        public DateTime? dateTimeGMT { get; set; }
        public List<playerData> data { get; set; }
        public int ttl { get; set; }
        public bool cache3 { get; set; }
        public int creditsLeft { get; set; }
        public string v { get; set; }
        
        public Provider provider { get; set; }

    }
    public class Provider
    {
        public string source { get; set; }
        public string url { get; set; }
        public DateTime? pubDate { get; set; }
    }


    public class playerData
    {
        public int pid { get; set; }
        public string fullName { get; set; }
        public string name { get; set; }
    }

    public class Reward
    {
        public string RewardId { get; set; }
        public string RewardType { get; set; }
        public DateTime RewardWeek { get; set; }
        public string Comments { get; set; }
    }

    public class Match
    {
        public int unique_id { get; set; }
        public string date { get; set; }
        public string dateTimeGMT { get; set; }
        [JsonProperty("team-1")]
        public string team1 { get; set; }
        [JsonProperty("team-2")]
        public string team2 { get; set; }
        public string type { get; set; }
        public bool squad { get; set; }
        public string toss_winner_team { get; set; }
        public bool matchStarted { get; set; }
        public string winner_team { get; set; }
       }
    public class APIMatchDetails
    {
        public List<Match> matches { get; set; }
        public string v { get; set; }
        public int ttl { get; set; }
        public Provider provider { get; set; }
        public int creditsLeft { get; set; }
    }

    public class VerifyJoinLeague
    {
        public int UserId { get; set; }
        public int LeagueId { get; set; }
        public bool LeagueApproved { get; set; }
        
    }

    public class JoinLeague
    {
        public int LeagueLeaderId { get; set; }
        public int UserLeagueId { get; set; }

    }

    public class SetVerificationCodeModel
    {
        public int UserId { get; set; }
        public string ActivationToken { get; set; }
        public string PhoneNumber { get; set; }
    }

    public class FetchEmptyRefferalCodeUsers
    {
        public int UserId { get; set; }
        public string ReferralCode { get; set; }

    }

    public class SyncMatchApiModel
    {
        public int APIDetailsId { get; set; }
        public int MatchId { get; set; }
        public string UniqueId { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string MatchScheduledDate { get; set; }

    }

    public class SyncTeamApiModel
    {
        public int ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }

    }

    public class NotificationModel
    {
        public int UserId { get; set; }
        public int NotificationId { get; set; }
        public string Title { get; set; }
        public string Message { get; set; }
    }

    public class ResetTransferModel
    {
        public int TournamentId { get; set; }
        public int TransferCount { get; set; }

    }

    public class UserCommModel
    {
        public string emailIds { get; set; }
        public string SubjectLine { get; set; }
        public int commType { get; set; }
        public string userIds { get; set; }
        public int StartUserId { get; set; }
        public int EndUserId { get; set; }

    }

    public class ManualScoreUpdate
    {
        public string UniqueId { get; set; }
        public int? APIPId { get; set; }
        public int? BattingPoints { get; set; }
        public int? BowlingPoints { get; set; }
        public int? FieldingPoints { get; set; }
    }

    public class ScoreObject
    {
        public int TournamentId { get; set; }
        public int MatchId { get; set; }
        public int? MoM { get; set; }
        public string MatchStatus { get; set; }
        public List<ManualScoreUpdate> data { get; set; }
    }

    public class FunFactModel
    {
        public int FunFactId { get; set; }
        public string FunMessage { get; set; }

    }

    public class ValidateTransferModel
    {
        public string Status { get; set; }

    }
}
namespace RapidAPI.Match.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Meta
    {
        public int upcomingMatchCount { get; set; }
        public int inProgressMatchCount { get; set; }
        public int completedMatchCount { get; set; }
    }

    public class Series
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
    }

    public class Venue
    {
        public string name { get; set; }
        public string shortName { get; set; }
    }

    public class HomeTeam
    {
        public bool isBatting { get; set; }
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
    }

    public class AwayTeam
    {
        public bool isBatting { get; set; }
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
    }

    public class Match
    {
        public int id { get; set; }
        public int matchTypeId { get; set; }
        public Series series { get; set; }
        public string name { get; set; }
        public string status { get; set; }
        public Venue venue { get; set; }
        public HomeTeam homeTeam { get; set; }
        public AwayTeam awayTeam { get; set; }
        public string currentMatchState { get; set; }
        public bool isMultiDay { get; set; }
        public string matchSummaryText { get; set; }
        public List<object> liveStreams { get; set; }
        public bool isLive { get; set; }
        public int currentInningId { get; set; }
        public bool isMatchDrawn { get; set; }
        public bool isMatchAbandoned { get; set; }
        public string startDateTime { get; set; }
        public string endDateTime { get; set; }
        public bool isWomensMatch { get; set; }
        public bool isGamedayEnabled { get; set; }
        public bool removeMatch { get; set; }
    }

    public class MatchList
    {
        public List<Match> matches { get; set; }
    }

    public class RapidAPISeriesMatch
    {
        public Meta meta { get; set; }
        public MatchList matchList { get; set; }
        public int status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace RapidAPI.Team.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Meta
    {
        public int seriesId { get; set; }
        public string seriesName { get; set; }
    }

    public class Team
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class SeriesTeams
    {
        public List<Team> teams { get; set; }
    }

    public class RapidAPISeriesTeam
    {
        public Meta meta { get; set; }
        public SeriesTeams seriesTeams { get; set; }
        public int status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace RazorPay.api.Models
{
    public class BankAccount
    {
        public string ifsc { get; set; }
        public string bank_name { get; set; }
        public string name { get; set; }
        public string account_number { get; set; }
        public List<object> notes { get; set; }
    }

    public class RPCreateFundAccountResponseModel
    {
        public string id { get; set; }
        public string entity { get; set; }
        public string contact_id { get; set; }
        public string account_type { get; set; }
        public BankAccount bank_account { get; set; }
        public bool active { get; set; }
        public string batch_id { get; set; }
        public int created_at { get; set; }
    }



}

namespace RapidAPI.TeamPlayers.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Player
    {
        public int playerId { get; set; }
        public string fullName { get; set; }
        [JsonProperty("class")]
        public string class1 { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string battingStyle { get; set; }
        public string bowlingStyle { get; set; }
        public string imageURL { get; set; }
        public string playerType { get; set; }
        public DateTime? dob { get; set; }
        public DateTime? testDebutDate { get; set; }
        public DateTime? odiDebutDate { get; set; }
        public DateTime? t20DebutDate { get; set; }
        public string bio { get; set; }
        public string didYouKnow { get; set; }
        public string height { get; set; }
    }

    public class TeamPlayers
    {
        public List<Player> players { get; set; }
    }

    public class RapidAPITeamPlayers
    {
        public TeamPlayers teamPlayers { get; set; }
        public int status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace FantasyCricketScoreIntegration.RapidMatchPlayers.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Team
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class Player
    {
        public int playerId { get; set; }
        public string fullName { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string imageURL { get; set; }
        public string battingStyle { get; set; }
        public string bowlingStyle { get; set; }
        public string playerType { get; set; }
        public string dob { get; set; }
        public string testDebutDate { get; set; }
        public string odiDebutDate { get; set; }
        public string t20DebutDate { get; set; }
        public string bio { get; set; }
        public string didYouKnow { get; set; }
        public string height { get; set; }
    }

    public class HomeTeam
    {
        public string teamName { get; set; }
        public string teamShortName { get; set; }
        public Team team { get; set; }
        public List<Player> players { get; set; }
    }

    public class Team2
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class Player2
    {
        public int playerId { get; set; }
        public string fullName { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string imageURL { get; set; }
        public string battingStyle { get; set; }
        public string bowlingStyle { get; set; }
        public string playerType { get; set; }
        public DateTime? dob { get; set; }
        public DateTime? testDebutDate { get; set; }
        public DateTime? odiDebutDate { get; set; }
        public DateTime? t20DebutDate { get; set; }
        public string bio { get; set; }
        public string didYouKnow { get; set; }
        public string height { get; set; }
    }

    public class AwayTeam
    {
        public string teamName { get; set; }
        public string teamShortName { get; set; }
        public Team2 team { get; set; }
        public List<Player2> players { get; set; }
    }

    public class PlayersInMatch
    {
        public HomeTeam homeTeam { get; set; }
        public AwayTeam awayTeam { get; set; }
    }

    public class RapidMatchPlayers
    {
        public PlayersInMatch playersInMatch { get; set; }
        public int? status { get; set; }
        public string poweredBy { get; set; }
    }


}