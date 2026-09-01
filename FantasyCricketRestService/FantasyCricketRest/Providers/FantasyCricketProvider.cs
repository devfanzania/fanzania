using FantasyCricketAppRest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using FantasyCricketAppRest.Repository;
using FantasyCricketAppRest.CommonUtilities;
using System.Configuration;
using System.Threading.Tasks;
using RestSharp;
using FantasyCricketScoreIntegration.RapidMatchPlayers.Models;
using System.Net;

namespace FantasyCricketAppRest.Providers
{
    public sealed class FantasyCricketProvider
    {
        
       
        private const string _AppName = "FantasyCricketRestService";
        private const string _Filename = "FantasyCricketProvider.cs";
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        private string _EmailTemplatePath = ConfigurationManager.AppSettings["EmailTemplatePath"];
        private string _PhoneVerificationUrl = ConfigurationManager.AppSettings["PhoneVerificationUrl"];
        private string _CurrentTournamentName = ConfigurationManager.AppSettings["CurrentTournamentName"];
        private bool _IsTestCase;

        public FantasyCricketProvider()
        {
            

        }

        public bool IsTestCase
        {
            get { return _IsTestCase; }
            set { _IsTestCase = value; }
        }

        internal dynamic CreateUser(UserModel NewUser)
        {
            Random generator = new Random();
            string VerificationCode = generator.Next(1, 9999).ToString("D4");
            var data = ProductsRepository.CreateUser(NewUser, VerificationCode);
            bool isMailSent = false;

            string subject = "Welcome to fanzania. Let's get started " + VerificationCode;
            string forgotPassEmailTemplate = _EmailTemplatePath + "registrationpasscodetemplate.html";
            string fileDir = AppDomain.CurrentDomain.BaseDirectory;
            string body = System.IO.File.ReadAllText(fileDir + forgotPassEmailTemplate);
            body = body.Replace("#UserName#", NewUser.Name);
            body = body.Replace("#UserEmail#", NewUser.Email);
            body = body.Replace("#UserPasscode#", VerificationCode);
            
            if(data.Count>0)
                isMailSent = Utilities.SendMail(NewUser.Email, subject, body, "SignUp");

            return data;
        }

        internal dynamic SaveProfile(UserModel user)
        {
            return ProductsRepository.SaveProfile(user);
        }

        internal dynamic FetchKYCDetails(UserModel user)
        {
            return ProductsRepository.FetchKYCDetails(user);
        }
        internal dynamic UpdateKYCStatus(UserModel user)
        {
            if(user.KYCStatus == "pending")
            {
                string emailList = ConfigurationManager.AppSettings["StartMatchEmail"];
                DateTime UTCTime = System.DateTime.UtcNow;
                DateTime IndianTime = UTCTime.AddHours(5.5);
                string currDate = IndianTime.ToString("yyyy'-'MM'-'dd'T'HH':'mm':'ss");

                string subject = "KYC document submitted on " + currDate;
                string body = "KYC document(need to approve/reject from Admin page) submitted by user, userid:  " + user.UserId;
                bool isMailSent = false;

                isMailSent = Utilities.SendMail(emailList, subject, body, "KYCDoc-Notification");
            }
            return ProductsRepository.UpdateKYCStatus(user);
        }
        internal dynamic UpdateKYCDetails(UserModel user)
        {
            return ProductsRepository.UpdateKYCDetails(user);
        }

        internal dynamic SaveLoginPreference(UserModel user)
        {
            return ProductsRepository.SaveLoginPreference(user);
        }

        internal dynamic FetchProfile(int UserId)
        {
            return ProductsRepository.FetchProfile(UserId);
        }

        internal dynamic UploadProfileImage(string UserId, string ext)
        {
            return ProductsRepository.UploadProfileImage(UserId, ext);
        }

        internal dynamic UploadKYCImage(string UserId, string KYCDoc, string ImageName)
        {
            return ProductsRepository.UploadKYCImage(UserId, KYCDoc, ImageName);
        }

        internal dynamic VerifyUser(string UserName)
        {
            return ProductsRepository.VerifyUser(UserName);
        }

        internal dynamic VerifyUser(UserModel user)
        {
            return ProductsRepository.VerifyUser(user);
        }

        internal dynamic VerifyTeamName(UserTeamModel UserTeam)
        {
            return ProductsRepository.VerifyTeamName(UserTeam);
        }

        internal dynamic GetVerificationCode(UserModel user)
        {
            Random generator = new Random();
            string VerificationCode = generator.Next(1, 9999).ToString("D4");

            string subject = "Here's your account activation code " + VerificationCode;
            string forgotPassEmailTemplate = _EmailTemplatePath + "registrationpasscodetemplate.html";
            string fileDir = AppDomain.CurrentDomain.BaseDirectory;
            string body = System.IO.File.ReadAllText(fileDir + forgotPassEmailTemplate);
            body = body.Replace("#UserName#", user.Name);
            body = body.Replace("#UserEmail#", user.Email);
            body = body.Replace("#UserPasscode#", VerificationCode);
            
            var isMailSent = Utilities.SendMail(user.Email, subject, body, "GetVerificationCode");
            if (!isMailSent)
                return "fail";

            var data = ProductsRepository.GetVerificationCode(user, VerificationCode);
            return data;
        }

        internal dynamic SendVerificationCode(UserModel user)
        {
            Random generator = new Random();
            string VerificationCode = generator.Next(1, 99999).ToString("D5");
            string sendotp = ConfigurationManager.AppSettings["SendOTP"];

        //send OTP here
        //https://2factor.in/API/V1/{api_key}/SMS/{phone_number}/{otp}


        SetVerificationCodeModel data = ProductsRepository.SetVerificationCode(user, VerificationCode);

            if (sendotp != "yes")
                return data;

            if (!string.IsNullOrWhiteSpace(data.PhoneNumber))
            {
                string url = _PhoneVerificationUrl + "/SMS/" + data.PhoneNumber + "/" + VerificationCode;

                var client = new RestClient(url);
                var request = new RestRequest(Method.GET);
                request.AddHeader("content-type", "application/x-www-form-urlencoded");
                bool apiResponse = false;
                try
                {
                    IRestResponse response = client.Execute(request);
                    apiResponse = response.IsSuccessful;
                    if (!apiResponse)
                        return "fail";
                    
                }
                catch (Exception e)
                { }

                
            }
            else
                return "fail";


            return data;
        }

        internal dynamic VerifyOTP(UserModel user)
        {
            return ProductsRepository.VerifyOTP(user);
        }

        internal dynamic UpdateSubscriptionDetails(UserModel user)
        {
            UpdateSubscriptionModel dbData = ProductsRepository.UpdateSubscriptionDetails(user);

            bool isMailSent = false;

            string subject = "Congratulations ! Subscription Package Added.";
            if (_EnvCode == "DEV")
                subject = _EnvCode + " - " + subject;
            string subsTemplate = "";
            if(dbData.SubscriptionType == 1)
                subsTemplate =  _EmailTemplatePath + "PrizeEligibilityPackage.html";
            else if(dbData.SubscriptionType == 2)
                subsTemplate = _EmailTemplatePath + "LiveScorePackage.html";
            else if (dbData.SubscriptionType == 3)
                subsTemplate = _EmailTemplatePath + "PrizeEligibilityLiveScorePackage.html";
            string fileDir = AppDomain.CurrentDomain.BaseDirectory;
            string body = System.IO.File.ReadAllText(fileDir + subsTemplate);
            body = body.Replace("#TournamentName#", _CurrentTournamentName);

            if(dbData.SubscriptionType > 0 && dbData.Email != "")
            isMailSent = Utilities.SendMail(dbData.Email, subject, body, "Subscription");

            return dbData;
        }

        internal dynamic GetSubscriptionDetails(UserModel user)
        {
            return ProductsRepository.GetSubscriptionDetails(user);
        }

        internal dynamic AdminUpdateUserPoints(AdminUserPointsModel match)
        {
            return ProductsRepository.AdminUpdateUserPoints(match);
        }

        internal bool UserCommunication(UserCommModel comm)
        {

            string subject = "";
            string emailTemplate = "2026IPL_1.html";

           
            string fileDir = AppDomain.CurrentDomain.BaseDirectory;
            string UserCommTemplate =  _EmailTemplatePath + emailTemplate;
            string bodyText = System.IO.File.ReadAllText(fileDir + UserCommTemplate);



            var isMailSent = false;
          
            List<UserCommunicationModel> userData = ProductsRepository.UserCommunication(comm);
            foreach (var item in userData)
            {
                if (item.Email == "" || item.Email == null)
                    continue;

                string body = bodyText;
               // body = body.Replace("#ReferralCode#", item.ReferralCode);
                subject = "Indian T20 League | LAUNCHED!";
               
                isMailSent = Utilities.SendMail(item.Email, subject, body, emailTemplate);
            }
            return isMailSent;

        }

        internal dynamic EmailVerified(UserModel user)
        {
            return ProductsRepository.EmailVerified(user);
        }

        internal bool UpdateBankDetails(BankDetailsModel bankdetails)

        {
            return ProductsRepository.UpdateBankDetails(bankdetails);
        }

        internal FundTransferResponseModel TransferFunds(BankDetailsModel bankdetails)
        {
            BankDetailsModel bankdetail = new BankDetailsModel();
            FundTransferResponseModel tr_response = new FundTransferResponseModel();
            bankdetail = ProductsRepository.FetchBankDetails(bankdetails);
            tr_response = ProductsRepository.TransferFunds(bankdetails);

            if (tr_response.status)
            {
                bool isMailSent = false;
                DateTime UTCTime = System.DateTime.UtcNow;
                DateTime IndianTime = UTCTime.AddHours(5.5);
                string currDate = IndianTime.ToString("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
               
                string subject = "CONGRATULATIONS! Withdrawal initiated successfully";
                string WithdrawalTemplate = _EmailTemplatePath + "WithdrawalSuccessful.html";
                string fileDir = AppDomain.CurrentDomain.BaseDirectory;
                string body = System.IO.File.ReadAllText(fileDir + WithdrawalTemplate);
                body = body.Replace("#[WithdrawalAmount]", tr_response.amount.ToString());
                body = body.Replace("#[TransactionID]", tr_response.TransactionID);
                body = body.Replace("#[CurrentDate]", currDate);
                body = body.Replace("#[BankName]", bankdetail.BankName);
                body = body.Replace("#[AccountNumber]", bankdetail.AccountNumber);
                body = body.Replace("#[IFSCCode]", bankdetail.IFSC);
                
                isMailSent = Utilities.SendMail(tr_response.Email, subject, body, "Withdrawal");

                string emailList = ConfigurationManager.AppSettings["StartMatchEmail"];
                subject = "Money Withdrawal notification- " + currDate;
                body = "Money Withdrawn by User: " + tr_response.Email;
                body += " ; INR  " + bankdetails.amount;
                isMailSent = Utilities.SendMail(emailList, subject, body, "Withdrawal");
            }

            return tr_response;
        }

        internal BankDetailsModel FetchBankDetails(BankDetailsModel bankdetails)
        {
            return ProductsRepository.FetchBankDetails(bankdetails);
        }

        internal dynamic SignIn(string UserName, string Password)
        {
            return ProductsRepository.SignIn(UserName, Password);
        }

        internal dynamic Login(UserModel loginInfo)
        {
            return ProductsRepository.Login(loginInfo);
        }

        internal dynamic ExternalLogin(UserModel loginInfo)
        {
            return ProductsRepository.ExternalLogin(loginInfo);
        }

        internal dynamic GetLeagueLeaders(int tournamentId)
        {
            return ProductsRepository.GetLeagueLeaders(tournamentId);
        }

        internal dynamic GetUserTeamLeaders(int tournamentId)
        {
            return ProductsRepository.GetUserTeamLeaders(tournamentId);
        }

        internal dynamic TournamentDetails()
        {
            return ProductsRepository.TournamentDetails();
        }

        internal dynamic AllTournamentDetails()
        {
            return ProductsRepository.AllTournamentDetails();
        }

        internal dynamic UserTournamentDetails(GenericModel User)
        {
            return ProductsRepository.UserTournamentDetails(User);
        }

        internal dynamic UserUpcomingTournament(GenericModel User)
        {
            return ProductsRepository.UserUpcomingTournament(User);
        }

       

        internal dynamic AllCompleteMataches(int TournamentId)
        {
            return ProductsRepository.AllCompleteMataches(TournamentId);
        }

        internal dynamic LogOut(string UserName)
        {
            return ProductsRepository.LogOut(UserName);
        }

        internal dynamic LogOut(UserModel User)
        {
            return ProductsRepository.LogOut(User);
        }
        internal dynamic CreateLeague(LeagueModel NewLeague)
        {
            return ProductsRepository.CreateLeague(NewLeague);
        }

        internal dynamic VerifyLeagueName(LeagueModel league)
        {
            return ProductsRepository.VerifyLeagueName(league);
        }
        internal dynamic ChangeLeagueName(LeagueModel league)
        {
            return ProductsRepository.ChangeLeagueName(league);
        }

        internal dynamic FetchLeagueSubscription(LeagueModel league)
        {
            return ProductsRepository.FetchLeagueSubscription(league);
        }

        internal bool UpdateLeagueSubscription(List<BulkSubscriptionModel> SubscriptionDetails)
        {
            return ProductsRepository.UpdateLeagueSubscription(SubscriptionDetails);
        }

        internal JoinLeague JoinLeague(LeagueModel League)
        {
            return ProductsRepository.JoinLeague(League);
        }

        internal int VerifyJoinLeague(LeagueModel League)
        {
            return ProductsRepository.VerifyJoinLeague(League);
        }

        internal dynamic ExitLeague(LeagueModel League)
        {
            return ProductsRepository.ExitLeague(League);
        }

        internal string AdminSyncMatchApiKey(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminSyncMatchApiKey(match);
        }

        internal dynamic ResetSubscription()
        {
            return ProductsRepository.ResetSubscription();
        }

        internal string AdminSyncMatchid(AdminMatchDetailsModel series)
        {
            return ProductsRepository.AdminSyncMatchid(series);
        }

        internal string AdminSyncTeamPlayers(AdminMatchDetailsModel series)
        {
            return ProductsRepository.AdminSyncTeamPlayers(series);
        }

        internal string AdminSyncTeamid(AdminMatchDetailsModel series)
        {
            return ProductsRepository.AdminSyncTeamid(series);
        }

        internal dynamic ApproveLeagueUser(int LeagueId, int UserId)
        {
            return ProductsRepository.ApproveLeagueUser(LeagueId, UserId);
        }
        internal dynamic UnApproveLeagueUser(GenericModel UnApproveList)
        {
            return ProductsRepository.UnApproveLeagueUser(UnApproveList);
        }

        internal dynamic UserTeamMatchDetailsWithPlayers(GenericModel UserMatch)
        {
            return ProductsRepository.UserTeamMatchDetailsWithPlayers(UserMatch);
        }

        internal dynamic UserTeamPlayerWithDetails(UserTeamModel UserTeam)
        {
            return ProductsRepository.UserTeamPlayerWithDetails(UserTeam);
        }

        internal dynamic UserTeamPlayerDetailsWithPowerPlay(UserTeamModel UserTeam)
        {
            return ProductsRepository.UserTeamPlayerDetailsWithPowerPlay(UserTeam);
        }

        internal dynamic LastMatchTopPerformer(UserTeamModel users)
        {
            return ProductsRepository.LastMatchTopPerformer(users);
        }

        internal dynamic UserCount()
        {
            return ProductsRepository.UserCount();
        }

        internal dynamic FetchUserPowerPlay(GenericModel details)
        {
            return ProductsRepository.FetchUserPowerPlay(details);
        }

        internal dynamic UpdateUserPowerPlay(UpdateUserPowerPlayModel details)
        {
            return ProductsRepository.UpdateUserPowerPlay(details);
        }

        internal dynamic MatchRewards()
        {
            return ProductsRepository.MatchRewards();
        }

        internal dynamic UserTeamPlayerDetailsWithStealthMode(UserTeamModel UserTeam)
        {
            return ProductsRepository.UserTeamPlayerDetailsWithStealthMode(UserTeam);
        }

        internal dynamic IsLeagueLeader(int LeagueId, int UserId)
        {
            return ProductsRepository.IsLeagueLeader(LeagueId, UserId);
        }
        internal dynamic GetLeagueDetails(int LeagueId)
        {
            return ProductsRepository.GetLeagueDetails(LeagueId);
        }
        internal dynamic GetUserAllLeagues(int UserId,int TournamentId)
        {
            return ProductsRepository.GetUserAllLeagues(UserId, TournamentId);
        }
        internal dynamic ResetLeaguePin(LeagueModel league)
        {
            return ProductsRepository.ResetLeaguePin(league);
        }
        internal dynamic SaveNewPassword(int UserId, string NewPassword)
        {
            return ProductsRepository.SaveNewPassword(UserId, NewPassword);
        }
        internal dynamic ForgetPassword(UserModel user)
        { 
            var NewPassword = ProductsRepository.ForgetPassword(user.Email);
            if (NewPassword == "")
                return false;
            string subject = "Here's your new fanzania password.";
            string forgotPassEmailTemplate = _EmailTemplatePath + "forgotpasswordtemplate.html";
            string fileDir = AppDomain.CurrentDomain.BaseDirectory;
            string body = System.IO.File.ReadAllText(fileDir + forgotPassEmailTemplate);
            
            body = body.Replace("#UserName#", user.Name); 
            body = body.Replace("#GeneratedNewPassword#", NewPassword);
            var isMailSent = Utilities.SendMail(user.Email, subject,body, "ForgetPassword");
            return isMailSent;
        }
        
        internal dynamic VerifyUserTeam(int UserId, int TournamentId, string UserTeamName)
        {
            return ProductsRepository.VerifyUserTeam(UserId, TournamentId, UserTeamName);
        }
        internal dynamic CreateUserTeam(UserTeamModel NewUserTeam)
        {
            return ProductsRepository.CreateUserTeam(NewUserTeam);
        }

        internal dynamic ModifyUserTeam(UserTeamModel NewUserTeam)
        {
            return ProductsRepository.ModifyUserTeam(NewUserTeam);
        }

        internal dynamic GetUserTeamDetails(int tournamentId, int userId)
        {
            return ProductsRepository.GetUserTeamDetails(tournamentId, userId);
        }

        internal dynamic GetLeagueTeams(int tournamentId, int leagueId)
        {
            return ProductsRepository.GetLeagueTeams(tournamentId, leagueId);
        }

        internal dynamic GetUserTeamPlayerDetails(int tournamentId, int userTeamId)
        {
            return ProductsRepository.GetUserTeamPlayerDetails(tournamentId, userTeamId);
        }

        internal dynamic LastCutoffUserTeamPlayerDetails(int tournamentId, int userTeamId)
        {
            return ProductsRepository.LastCutoffUserTeamPlayerDetails(tournamentId, userTeamId);
        }

        internal dynamic SaveTeamSelection(TeamCompositionModel TeamSelection)
        {
            return ProductsRepository.SaveTeamSelection(TeamSelection);
        }
        internal dynamic GetMatchDetails(int tournamentId,int matchId)
        {
            return ProductsRepository.GetMatchDetails(tournamentId, matchId);
        }
        internal dynamic GetAllMatches(GenericModel match)
        {
            return ProductsRepository.GetAllMatches(match);
        }
        internal dynamic AllFutureMatches(GenericModel match)
        {
            return ProductsRepository.AllFutureMatches(match);
        }


        internal dynamic EndMatch(int matchId)
        {
            return ProductsRepository.EndMatch(matchId);
        }

        internal dynamic GetMatchPlayerStats(int matchId)
        {
            return ProductsRepository.GetMatchPlayerStats(matchId);
        }

        internal dynamic GetAllPlayers(int tournamentId)
        {
            return ProductsRepository.GetAllPlayers(tournamentId);
        }

        internal dynamic Trigger4PointCalc(MatchModel pointCalc)
        {
            return ProductsRepository.Trigger4PointCalc(pointCalc);
        }

        internal dynamic SetManualPointCalc(MatchModel pointCalc)
        {
            return ProductsRepository.SetManualPointCalc(pointCalc);
        }

        internal dynamic GetSubCountStart(int tournamentId)
        {
            return ProductsRepository.GetSubCountStart(tournamentId);
        }

        internal bool StartMatch(MatchModel NewMatch)
        {
            string data = ProductsRepository.StartMatch(NewMatch);
            string emailList = ConfigurationManager.AppSettings["StartMatchEmail"];
            DateTime UTCTime = System.DateTime.UtcNow;
            DateTime IndianTime = UTCTime.AddHours(5.5);
            string currDate = IndianTime.ToString("yyyy'-'MM'-'dd'T'HH':'mm':'ss");

            string subject = "Match Started on " + currDate;
            string body = "Match Started between - " + data;
            bool isMailSent = false;

            if (data != "")
              isMailSent = Utilities.SendMail(emailList, subject, body, "StartMatch-Notification");
            
            return isMailSent;
        }

        internal bool SetTossWinner(MatchModel NewMatch)
        {
            var data = ProductsRepository.SetTossWinner(NewMatch);
            
            return true;
        }

        internal dynamic UpdateMatchPlayerStats(MatchPlayerStatsModel MatchPlayer)
        {
            return ProductsRepository.UpdateMatchPlayerStats(MatchPlayer);
        }

        internal dynamic SaveUserFeedback(FeedbackModel NewFeedback)
        {
            string ToEmail = ConfigurationManager.AppSettings["FeedBackEmail"]; ;
            string subject = "User Feedback - "+ NewFeedback.MessageAbout;
            string body = "<html><body>User Name - "+ NewFeedback.Name;
            body += "<br> User Email - " + NewFeedback.UserEmail;
            body += "<br> Message - " + NewFeedback.Messages + "</body></html>";
            var isMailSent = Utilities.SendMail(ToEmail, subject, body, "FeedbackEmail");

            return ProductsRepository.SaveUserFeedback(NewFeedback);
        }

        internal dynamic CalculateMatchPoint(int matchId)
        {
            return ProductsRepository.CalculateMatchPoint(matchId);
        }

        internal dynamic GetParticipationTeam(int tournamentId)
        {
            return ProductsRepository.GetParticipationTeam(tournamentId);
        }

        internal dynamic SaveTheme(int userId, string theme)
        {
            return ProductsRepository.SaveTheme(userId, theme);
        }

        internal dynamic UserTeamMatchDetails(int matchId, int userTeamId)
        {
            return ProductsRepository.UserTeamMatchDetails(matchId, userTeamId);
        }

        internal dynamic GetPlayerDetails(PlayerListModel playerList)
        {
            return ProductsRepository.GetPlayerDetails(playerList);
        }

        internal dynamic FetchPlayerStats(AdminPlayerModel player)
        {
            return ProductsRepository.FetchPlayerStats(player);
        }

        internal dynamic LastMatchPoints(GenericModel details)
        {
            return ProductsRepository.LastMatchPoints(details);
        }

        internal dynamic CountryList()
        {
            return ProductsRepository.CountryList();
        }

        internal dynamic TeamSelectionRule(GenericModel details)
        {
            return ProductsRepository.TeamSelectionRule(details);
        }

        internal dynamic AutoSelectTeam(GenericModel details)
        {
            return ProductsRepository.AutoSelectTeam(details);
        }

        

        internal dynamic LiveMatches(GenericModel details)
        {
            return ProductsRepository.LiveMatches(details);
        }

        internal dynamic LiveMatchScore(GenericModel details)
        {
            return ProductsRepository.LiveMatchScore(details);
        }

        internal dynamic LiveUserTeamScore(GenericModel details)
        {
            return ProductsRepository.LiveUserTeamScore(details);
        }

        internal dynamic UserStatsGlobalTopPlayers(GenericModel details)
        {
            return ProductsRepository.UserStatsGlobalTopPlayers(details);
        }

        internal dynamic UserStatsUserTopPlayers(GenericModel details)
        {
            return ProductsRepository.UserStatsUserTopPlayers(details);
        }

        internal dynamic UserStatsGlobalTopTeams(GenericModel details)
        {
            return ProductsRepository.UserStatsGlobalTopTeams(details);
        }

        internal dynamic UserStatsCaptainPoints(GenericModel details)
        {
            return ProductsRepository.UserStatsCaptainPoints(details);
        }

        internal dynamic LeagueStatsGlobalTopLeagues(GenericModel details)
        {
            return ProductsRepository.LeagueStatsGlobalTopLeagues(details);
        }

        internal dynamic LeagueStatsTopTeamsTopPerform(GenericModel details)
        {
            return ProductsRepository.LeagueStatsTopTeamsTopPerform(details);
        }

        internal dynamic LeagueStatsTopTeamsTopFavorite(GenericModel details)
        {
            return ProductsRepository.LeagueStatsTopTeamsTopFavorite(details);
        }

        internal dynamic DistinctTeam(GenericModel details)
        {
            return ProductsRepository.DistinctTeam(details);
        }

        internal dynamic LiveTournamentDetails(GenericModel details)
        {
            return ProductsRepository.LiveTournamentDetails(details);
        }

        internal dynamic StaticURLs()
        {
            return ProductsRepository.StaticURLs();
        }

        internal dynamic LiveLeagueUsers(GenericModel details)
        {
            return ProductsRepository.LiveLeagueUsers(details);
        }

        internal bool ValidateToken(string AuthToken,string userId)
        {
            return ProductsRepository.ValidateToken(AuthToken, userId);
        }

        internal dynamic AdminLogin(UserModel loginInfo)
        {
            return ProductsRepository.AdminLogin(loginInfo);
        }

        internal dynamic AdminUserPromote(UserModel loginInfo)
        {
            return ProductsRepository.AdminUserPromote(loginInfo);
        }

        internal dynamic AdminUpdateTournament(AdminTournamentModel tournament)
        {
            return ProductsRepository.AdminUpdateTournament(tournament);
        }

        internal dynamic AdminDeleteTournament(AdminTournamentModel tournament)
        {
            return ProductsRepository.AdminDeleteTournament(tournament);
        }

        internal dynamic AdminFetchTournament(AdminTournamentModel tournament)
        {
            return ProductsRepository.AdminFetchTournament(tournament);
        }

        internal dynamic AdminUpdateTournamentTeamRules(AdminTeamRulesModel rules)
        {
            return ProductsRepository.AdminUpdateTournamentTeamRules(rules);
        }

        internal dynamic AdminUpdateTournamentPointRules(AdminGenericModel point)
        {
            return ProductsRepository.AdminUpdateTournamentPointRules(point);
        }

        internal dynamic AdminFetchTournamentPointRules(AdminTournamentModel tournament)
        {
            return ProductsRepository.AdminFetchTournamentPointRules(tournament);
        }

        internal dynamic AdminUploadParticipationTeam(AdminGenericModel team)
        {
            return ProductsRepository.AdminUploadParticipationTeam(team);
        }

        internal dynamic AdminUploadPlayerDetails(AdminGenericModel player)
        {
            ProductsRepository.AdminUploadPlayerDetails(player);
            return true;
        }

        internal dynamic AdminUpdatePlayerDetails(AdminPlayerModel player)
        {
            return ProductsRepository.AdminUpdatePlayerDetails(player);
        }

        internal dynamic AdminDeletePlayerDetails(AdminPlayerModel player)
        {
            return ProductsRepository.AdminDeletePlayerDetails(player);
        }

        internal dynamic FetchPaymentGatewayDetails()
        {
            return ProductsRepository.FetchPaymentGatewayDetails();
        }
        internal dynamic AddMoney2Wallet(PaymentCF paymentDetails)
        {
            return ProductsRepository.AddMoney2Wallet(paymentDetails);
        }

        internal dynamic AdminFetchPlayerDetails(AdminPlayerModel player)
        {
            return ProductsRepository.AdminFetchPlayerDetails(player);
        }

        internal dynamic AdminUpdateParticipationTeam(AdminParticipationTeamModel team)
        {
            return ProductsRepository.AdminUpdateParticipationTeam(team);
        }

        internal dynamic AdminDeleteParticipationTeam(AdminParticipationTeamModel team)
        {
            return ProductsRepository.AdminDeleteParticipationTeam(team);
        }

        internal dynamic AdminFetchParticipationTeam(AdminParticipationTeamModel team)
        {
            return ProductsRepository.AdminFetchParticipationTeam(team);
        }

        internal dynamic AdminInsertMatchDetails(AdminGenericModel match)
        {
            ProductsRepository.AdminInsertMatchDetails(match);
            return true;
        }

        internal dynamic UploadMatchDetails(object match)
        {
            ProductsRepository.UploadMatchDetails(match);
            return true;
        }

        internal dynamic UploadDailyMatchDetails(object match)
        {
            ProductsRepository.UploadDailyMatchDetails(match);
            return true;
        }

        internal dynamic AdminUpdateMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminUpdateMatchDetails(match);
        }

        internal dynamic AdminDeleteMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminDeleteMatchDetails(match);
        }

        internal dynamic AdminFetchMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminFetchMatchDetails(match);
        }

        internal dynamic AdminFetchMatchOnStart(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminFetchMatchOnStart(match);
        }

        internal dynamic AdminSwitchBattingTeam(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminSwitchBattingTeam(match);
        }

        internal dynamic AdminUpdateAutoTeam(AdminAutoTeamModel team)
        {
            return ProductsRepository.AdminUpdateAutoTeam(team);
        }

        internal dynamic AdminDeleteAutoTeam(AdminAutoTeamModel team)
        {
            return ProductsRepository.AdminDeleteAutoTeam(team);
        }

        internal dynamic AdminFetchAutoTeam(AdminAutoTeamModel team)
        {
            return ProductsRepository.AdminFetchAutoTeam(team);
        }
        internal dynamic SendNotificationMessage(NotificationModel Notification)
        {
            return ProductsRepository.SendNotificationMessage(Notification);
        }

        internal bool InsertNotifications(int UserId, string Message,int MessageType)
        {
            return ProductsRepository.InsertNotifications(UserId,  Message,MessageType);
        }
        internal dynamic FetchNotificationMessage(int UserId)
        {
            return ProductsRepository.FetchNotificationMessage(UserId);
        }
        internal dynamic FetchNotificationCount(int UserId)
        {
            return ProductsRepository.FetchNotificationCount(UserId);
        }
        internal dynamic AcknowledgeNotificationMessage(NotificationModel Notification)
        {
            return ProductsRepository.AcknowledgeNotificationMessage(Notification);
        }

        internal int GetNotificationCount(string Email = null,int UserId=0)
        {
            return ProductsRepository.GetNotificationCount(Email, UserId);
        }

        internal dynamic AdminResetTransfer(ResetTransferModel value)
        {
            return ProductsRepository.AdminResetTransfer(value);
        }

        internal dynamic TestCall(object pl)
        {
            return ProductsRepository.TestCall(pl);
        }

        internal bool ManualScoreUpdate(AdminGenericModel mData)
        {
            return ProductsRepository.ManualScoreUpdate(mData);
        }

        internal bool ManualScoreUpdateEachPlayer(ManualScoreUpdate mData)
        {
            return ProductsRepository.ManualScoreUpdateEachPlayer(mData);
        }

        internal bool SetManualMoM(ManualScoreUpdate mData)
        {
            return ProductsRepository.SetManualMoM(mData);
        }

        internal dynamic FetchManualScore(ManualScoreUpdate mData)
        {
            return ProductsRepository.FetchManualScore(mData);
        }

        internal dynamic CalcWeeklyReward(Reward details)
        {
            return ProductsRepository.CalcWeeklyReward(details);
        }

        internal dynamic FetchRewardWeek(Reward details)
        {
            return ProductsRepository.FetchRewardWeek(details);
        }

        internal dynamic DeleteRewardWeek(Reward details)
        {
            return ProductsRepository.DeleteRewardWeek(details);
        }

        internal dynamic FetchRewardWeekDetails(Reward details)
        {
            return ProductsRepository.FetchRewardWeekDetails(details);
        }

        internal dynamic WeeklyRewardProcessed(Reward details)
        {
            return ProductsRepository.WeeklyRewardProcessed(details);
        }

        internal dynamic TeamPointsComparison(GenericModel teams)
        {
            return ProductsRepository.TeamPointsComparison(teams);
        }

        internal dynamic LiveTeamScoreComparison(GenericModel teams)
        {
            return ProductsRepository.LiveTeamScoreComparison(teams);
        }

        internal bool UpdateMatchPlayerIds(MatchModel details)
        {
            return ProductsRepository.UpdateMatchPlayerIds(details);
        }

        #region Daily Matches
        internal dynamic UserUpcomingDailyMatches(GenericModel User)
        {
            return ProductsRepository.UserUpcomingDailyMatches(User);
        }

        internal dynamic UserDailyMatches(GenericModel User)
        {
            return ProductsRepository.UserDailyMatches(User);
        }

        internal dynamic DailyLeagueTeams(GenericModel User)
        {
            return ProductsRepository.DailyLeagueTeams(User);
        }

        internal dynamic DailyTeamSelectionRules(GenericModel User)
        {
            return ProductsRepository.DailyTeamSelectionRules(User);
        }

        internal dynamic DailyMatchesPlayerList(GenericModel User)
        {
            return ProductsRepository.DailyMatchesPlayerList(User);
        }

        internal dynamic UserDailyTeamPlayers(GenericModel User)
        {
            return ProductsRepository.UserDailyTeamPlayers(User);
        }

        internal dynamic UserDailyTeamPlayersWithPoints(GenericModel User)
        {
            return ProductsRepository.UserDailyTeamPlayersWithPoints(User);
        }

        internal dynamic SaveDailyTeamSelection(TeamCompositionModel TeamSelection)
        {
            return ProductsRepository.SaveDailyTeamSelection(TeamSelection);
        }

        internal dynamic AdminInsertDailyMatchDetails(AdminGenericModel match)
        {
            ProductsRepository.AdminInsertDailyMatchDetails(match);
            return true;
        }

        internal dynamic AdminUpdateDailyMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminUpdateDailyMatchDetails(match);
        }

        internal dynamic AdminDeleteDailyMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminDeleteDailyMatchDetails(match);
        }

        internal dynamic AdminFetchDailyMatchDetails(AdminMatchDetailsModel match)
        {
            return ProductsRepository.AdminFetchDailyMatchDetails(match);
        }

        internal dynamic AdminFetchDailyPlayers(AdminPlayerModel players)
        {
            return ProductsRepository.AdminFetchDailyPlayers(players);
        }

        internal dynamic SetPlayersStatus(AdminGenericModel players)
        {
            return ProductsRepository.SetPlayersStatus(players);
        }

        internal dynamic SetMacthesWeeklyStatus(AdminGenericModel players)
        {
            return ProductsRepository.SetMacthesWeeklyStatus(players);
        }

        internal dynamic ResetDailyTeamPlayerPoints(AdminPlayerModel players)
        {
            return ProductsRepository.ResetDailyTeamPlayerPoints(players);
        }

        internal dynamic DailyTournamentList(GenericModel User)
        {
            return ProductsRepository.DailyTournamentList(User);
        }

        internal dynamic FetchMatchScore(MatchModel Match)
        {
            return ProductsRepository.FetchMatchScore(Match);
        }

        internal dynamic DailyUniqueTeamList(AdminGenericModel team)
        {
            return ProductsRepository.DailyUniqueTeamList(team);
        }

        internal dynamic DailyLiveLeagueUsers(GenericModel details)
        {
            return ProductsRepository.DailyLiveLeagueUsers(details);
        }

        internal dynamic FetchWalletInfo(UserModel user)
        {
            return ProductsRepository.FetchWalletInfo(user);
        }

        internal dynamic SyncReferralCode(UserModel user)
        {
            return ProductsRepository.SyncReferralCode(user);
        }

        internal dynamic GetFunFact(FunFactModel fun)
        {
            return ProductsRepository.GetFunFact(fun);
        }

        internal dynamic AddFunFact(FunFactModel fun)
        {
            return ProductsRepository.AddFunFact(fun);
        }
        internal dynamic FetchTotalRewards(UserModel user)
        {
            return ProductsRepository.FetchTotalRewards(user);
        }
        internal dynamic FetchTotalClaims(UserModel user)
        {
            return ProductsRepository.FetchTotalClaims(user);
        }

        internal dynamic CalculateReferralReward()
        {
            return ProductsRepository.CalculateReferralReward();
        }

        internal dynamic CalculateTournamentReward(UserTeamModel user)
        {
            return ProductsRepository.CalculateTournamentReward(user);
        }

        internal dynamic DownloadRewardDetails()
        {
            return ProductsRepository.DownloadRewardDetails();
        }

        internal dynamic ViewRewardDetails()
        {
            return ProductsRepository.ViewRewardDetails();
        }

        internal dynamic UploadClaimDetails(AdminGenericModel claims)
        {
            return ProductsRepository.UploadClaimDetails(claims);
        }

        internal dynamic FetchReferralCode(UserTeamModel user)
        {
            return ProductsRepository.FetchReferralCode(user);
        }

        internal dynamic AdminHideScoreCard(MatchModel match)
        {
            return ProductsRepository.AdminHideScoreCard(match);
        }

        internal bool SyncRapidMatchPlayers(RapidMatchPlayers match)
        {
            return RapidAPIRepository.SyncRapidMatchPlayers(match);
        }

        #endregion
    }
}