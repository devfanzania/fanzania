# region "Using Directives"
using System;
using System.Text;
using System.Configuration;
using System.Web.Security;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using FantasyCricketAppRest.DBHelpers;
using FantasyCricketAppRest.Models;
using RapidAPI.TeamPlayers.Models;
using RapidAPI.Match.Models;
using RapidAPI.Team.Models;
using Common.ECDC;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Linq;
using System.Threading.Tasks;
using System.IO;
using System.Data;
using System.Xml;
using RestSharp;
using RestSharp.Authenticators;
using FantasyCricketScoreIntegration.RapidMatchPlayers.Models;
using RazorPay.api.Models;
using Common.Logging.Entities;
using FantasyCricketAppRest.CommonUtilities;
using System.Net;
using BankAccount = FantasyCricketAppRest.Models.BankAccount;
using System.Text.RegularExpressions;
//using Google.Protobuf.WellKnownTypes;
#endregion "Using Directives"


namespace FantasyCricketAppRest.Repository
{
    internal static class ProductsRepository
    {
        #region > Private Local Variables <
        //private static string _logServiceURI = null;
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "FantasyCricketRepository.cs";
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        private static bool _IsTestCase;
        //private static bool Initialized = true;
        private static string APIBaseUrl = ConfigurationManager.AppSettings["APIBaseUrl"].ToString();
        private static string apiKey = ConfigurationManager.AppSettings["APIKey"].ToString();
        private static string project_key = ConfigurationManager.AppSettings["project_key"].ToString();
        private static int _Bronze = Int32.Parse(ConfigurationManager.AppSettings["BRONZE"]);
        private static int _Silver = Int32.Parse(ConfigurationManager.AppSettings["SILVER"]);
        private static int _Gold = Int32.Parse(ConfigurationManager.AppSettings["GOLD"]);
        private static int _Platinum = Int32.Parse(ConfigurationManager.AppSettings["PLATINUM"]);
        private static string RapidBaseUrl = ConfigurationManager.AppSettings["RapidBaseUrl"].ToString();
        private static string RapidapiKey = ConfigurationManager.AppSettings["RapidapiKey"].ToString();
        private static string RapidapiHost = ConfigurationManager.AppSettings["RapidapiHost"].ToString();
        private static string RPUrl = ConfigurationManager.AppSettings["RPUrl"].ToString();
        private static string _CurrentTournamentName = ConfigurationManager.AppSettings["CurrentTournamentName"].ToString();
        private static string _EmailTemplatePath = ConfigurationManager.AppSettings["EmailTemplatePath"].ToString();

        //dev razorpay cert
        private static string RPBasicAuthKey_dev = "cnpwX3Rlc3RfUjU5Qm4yQzk2MWpvNkc6U1Z4MW9ReVhES2tXajRscDdiZjUyOWZT"; //test
        private static string RPclient_id_dev = "rzp_test_R59Bn2C961jo6G"; //test
        private static string RPclient_secret_dev = "SVx1oQyXDKkWj4lp7bf529fS"; //test

        //Prod razorpay cert
        private static string RPBasicAuthKey_prod = "cnpwX2xpdmVfbkJCVmg2M1VDbXJCZHM6YXBZMzlRMUkzQlRKNnVobktZSkdlMENM"; //prod
        private static string RPclient_id_prod = "rzp_live_nBBVh63UCmrBds"; //prod
        private static string RPclient_secret_prod = "apY39Q1I3BTJ6uhnKYJGe0CL"; //prod

        //Dev cashless cert
        private static string Cashfree_id_dev = "TEST103359562cde8925c6ceeb53177065953301"; 
        private static string Cashfree_secret_dev = "cfsk_ma_test_6134cee07c5ad73d760cbbd0ec0b05fc_84594b4e"; 



        //private static Task task;
        #endregion

        #region > Properties <
        public static bool IsTestCase
        {
            get { return _IsTestCase; }
            set { _IsTestCase = value; }
        }
        #endregion


        #region Tournament

        internal static dynamic CreateUser(UserModel NewUser, string VerificationCode)
        {
            
            string passPhrase = "fanzania";
            Random generator = new Random();
           
            string encryptedPass = ECDC.Encrypt(NewUser.Password, passPhrase);
            var param = new Dictionary<string, object>() {
                { "@UserName", NewUser.UserName },
                { "@Name", NewUser.Name },
                { "@Password", encryptedPass },
                { "@FirstName", NewUser.FirstName },
                { "@LastName", NewUser.LastName },
                { "@Email", NewUser.Email },
                { "@DOB", NewUser.DOB },
                { "@SessionId", Guid.NewGuid() },
                { "@CountryId", NewUser.CountryId },
                { "@PhoneNumber", NewUser.PhoneNumber },
                { "@ActivationToken", VerificationCode},
                { "@ReferralCodeUsed", NewUser.ReferralCodeUsed}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CreateUser", param);
        }

        internal static string AdminSyncTeamPlayers(AdminMatchDetailsModel series)
        {
            var param = new Dictionary<string, object>() { };
            string TournamentKey = series.TournamentKey;
            string TeamKey = series.TeamKey;
            string TournamentType = series.TournamentType;
            string MatchType = series.MatchType;

            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string rs_token = GenerateAuthToken();
            string url = APIBaseUrl + "/cricket/" + project_key + "/tournament/" + TournamentKey + "/team/" + TeamKey + "/";

            var client = new RestClient(url);
            var request = new RestRequest(Method.GET);
            request.AddHeader("Content-Type", "application/json");
            request.AddHeader("Accept", "application/json");
            request.AddHeader("rs-token", rs_token);
            IRestResponse response = client.Execute(request);

            Dictionary<string, dynamic> apiData = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);

            foreach (var item in apiData["data"]["tournament_team"]["player_keys"])
            {
                try
                {
                    string p_key = item.Value;
                    string p_name = apiData["data"]["tournament_team"]["players"][p_key]["name"].Value.Trim();
                    p_name = p_name.Replace("'","");
                    string[] tokens = p_name.Split(' ');
                    string p_shortName = "";
                    if (tokens.Length > 1)
                        p_shortName = tokens[0].Substring(0, 1) + ' ' + tokens[1];
                    else
                        p_shortName = tokens[0];


                    string p_desc = p_name;
                    string p_type = "local";
                    string p_speciality = apiData["data"]["tournament_team"]["players"][p_key]["seasonal_role"].Value;
                    int p_value = 50;

                    if (p_speciality == "all_rounder")
                        p_speciality = "allrounder";
                    else if (p_speciality == "keeper")
                        p_speciality = "wicketkeeper";

                    param = new Dictionary<string, object>() {
                    { "@PlayerKey", p_key },
                    { "@PlayerName", p_name },
                    { "@PlayerShortName", p_shortName },
                    { "@PlayerDesc", p_desc },
                    { "@PlayerType",p_type },
                    { "@PlayerSpeciality",p_speciality },
                    { "@PlayerValue",p_value},
                    { "@TournamentKey", TournamentKey },
                    { "@TeamKey",TeamKey },
                    { "@TournamentType",TournamentType },
                    { "@MatchType", MatchType}
                    };
                    DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminSyncTeamPlayers", param);
                }
                catch (Exception Excp)
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "AdminSyncTeamPlayers_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "/admin-sync-team-players";
                    error.TransactionId = Guid.NewGuid().ToString();
                    error.TransactionType = "AdminSyncTeamPlayers API";
                    Utilities.LogException(error);
                }
            }

                return "success";
        }

        internal static string AdminSyncTeamid(AdminMatchDetailsModel series)
        {
            var param = new Dictionary<string, object>() { };
            string TournamentKey = series.TournamentKey;
           // string TeamKey = series.TeamKey;
            string TournamentType = series.TournamentType;
            string MatchType = series.MatchType;

            return "";
        }

        internal static string AdminSyncMatchid(AdminMatchDetailsModel series)
        {
            var param = new Dictionary<string, object>() { };

            int MatchId = 0;
            int TournamentId = 0;
            string MatchNo = "";
            string Venue = "";
            string MatchStage = "";
            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string MatchDate = "";
            string MatchScheduledTime = "";
            string UniqueId = "";
            string MatchStatus = "UPCOMING";
            string MatchTitle = "";
            double start_at = 0.0;
            string MatchCity = "";
            string TournamentKey = series.TournamentKey;
            // string TeamKey = series.TeamKey;
            string TournamentType = series.TournamentType;
            string MatchType = series.MatchType;
            string TournamentName = "";

            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string rs_token = GenerateAuthToken();
            
            string next_page_key = "1";
            while (next_page_key != null)
            {
                string url = APIBaseUrl + "/cricket/" + project_key + "/tournament/" + TournamentKey + "/fixtures/" + next_page_key + "/";

                var client = new RestClient(url);
                var request = new RestRequest(Method.GET);
                request.AddHeader("Content-Type", "application/json");
                request.AddHeader("Accept", "application/json");
                request.AddHeader("rs-token", rs_token);
                IRestResponse response = client.Execute(request);

                Dictionary<string, dynamic> apiData = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);
                
                foreach (var item in apiData["data"]["matches"])
                {
                    string mStatus = item["status"].Value;

                    MatchTitle = item["sub_title"].Value;
                    if (mStatus == "not_started")
                    {
                        MatchCity = (item["venue"]["city"] != null) ? item["venue"]["city"].Value : "tbd";
                        UniqueId = (item["key"] != null) ? item["key"].Value : "";
                        MatchNo = "";
                        MatchNo = Regex.Match(MatchTitle, @"\d+").Value;
                        start_at = item["start_at"].Value;

                        DateTime dt = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(start_at);
                        MatchScheduledDate = dt.ToString("yyyy-MM-dd");
                        MatchScheduledTime = dt.ToString("hh:mm:ss.sss");
                        MatchDate = dt.ToString("yyyy-MM-dd hh:mm:ss.sss");

                        if (MatchNo == "")
                            MatchNo = "0";

                        //MatchType = item["format"].Value.ToUpper();
                        Venue = MatchCity;
                        MatchStage = "League";
                        if (MatchTitle.Contains("Qualifier"))
                        {
                            MatchStage = "Qualifier";
                            MatchNo = "0";
                        }
                        else if (MatchTitle.Contains("Eliminator") || MatchTitle.Contains("Knockout"))
                        {
                            MatchStage = "Eliminator";
                            MatchNo = "0";
                        }
                        else if (MatchTitle.Contains("Semi Final"))
                        {
                            MatchStage = "Semifinal";
                            MatchNo = "0";
                        }
                        else if (MatchTitle.Contains("Challenger"))
                        {
                            MatchStage = "Challenger";
                            MatchNo = "0";
                        }
                        else if (MatchTitle.Contains("Final"))
                        {
                            MatchStage = "Final";
                            MatchNo = "0";
                            continue;
                        }

                        Team1 = item["teams"]["a"]["name"].Value.Trim();
                        Team2 = item["teams"]["b"]["name"].Value.Trim();
                        if (Team1 == "TBC")
                            Team1 = "TBD";
                        if (Team2 == "TBC")
                            Team2 = "TBD";

                        //temporary for WC
                        if (Team1 == "TBD" && Team2 == "TBD")
                            break;

                        if (TournamentType == "D")
                        {
                            if (Team1 == "TBD" || Team2 == "TBD")
                                continue; // do not add any match contest for non decided teams.

                            TournamentName = item["tournament"]["short_name"].Value.Split(' ')[0];
                            param = new Dictionary<string, object>()
                        {
                            { "@MatchId", MatchId },
                            { "@MatchNo", 1 },
                            { "@MatchType", MatchType },
                            { "@Venue", Venue },
                            { "@MatchCity", MatchCity },
                            { "@MatchStage", MatchStage },
                            { "@TournamentId", TournamentId },
                            { "@TournamentName", TournamentName },
                            { "@TournamentType", TournamentType },
                            { "@TournamentKey", TournamentKey },
                            { "@Team1", Team1 },
                            { "@Team2", Team2 },
                            { "@MatchStatus", MatchStatus },
                            { "@MatchScheduledDate", MatchScheduledDate },
                            { "@MatchDate", MatchDate },
                            { "@MatchScheduledTime", MatchScheduledTime },
                            { "@UniqueId", UniqueId }
                        };
                            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadDailyMatchDetails", param);
                        }
                        else
                        {
                            param = new Dictionary<string, object>()
                        {
                            { "@MatchId", MatchId },
                            { "@MatchNo", Int32.Parse(MatchNo) },
                            { "@MatchType", MatchType },
                            { "@Venue", Venue },
                            { "@MatchCity", MatchCity },
                            { "@MatchStage", MatchStage },
                            { "@TournamentId", TournamentId },
                            { "@TournamentType", TournamentType },
                            { "@TournamentKey", TournamentKey },
                            { "@Team1", Team1 },
                            { "@Team2", Team2 },
                            { "@MatchStatus", MatchStatus },
                            { "@MatchScheduledDate", MatchScheduledDate },
                            { "@MatchDate", MatchDate },
                            { "@MatchScheduledTime", MatchScheduledTime },
                            { "@UniqueId", UniqueId }
                        };
                            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadMatchDetails", param);
                        }
                    }
                }
                next_page_key = (apiData["data"]["next_page_key"] != null) ? apiData["data"]["next_page_key"].ToString() : "";
                if (next_page_key == null || next_page_key == "")
                    break;
            }

            return "";
        }

        //this fundtion will generate auth token from api.sports.roanuz.com REST end point
        internal static string GenerateAuthToken()
        {

            string rs_token = "";
            string url = APIBaseUrl + "/core/" + project_key + "/auth/";
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            var client = new RestClient(url);
            var request = new RestRequest(Method.POST);
            request.AddHeader("Content-Type", "application/json");
            request.AddHeader("Accept", "application/json");
            request.AddParameter("application/json", "{\"api_key\": \"" + apiKey + "\" }", ParameterType.RequestBody);
            IRestResponse response = client.Execute(request);

            Dictionary<string, dynamic> response_value = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);

            rs_token = (response_value["data"]["token"] != null) ? response_value["data"]["token"].Value : "";

            return rs_token;
        }

        internal static dynamic SaveProfile(UserModel user)
        {
            bool CommPreference = true;
            if (user.CommPreference == false)
                CommPreference = user.CommPreference;

            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@Name", user.Name },
               // { "@DOB", user.DOB },
                { "@CountryId", user.CountryId },
                { "@PhoneNumber", user.PhoneNumber },
                { "@CommPreference", CommPreference },
                { "@BackgroundTheme", user.BackgroundTheme },
                { "@ReqType", "profile" }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveProfile", param);
        }

        internal static dynamic FetchKYCDetails(UserModel user)
        {
            string KYCStatus = user.KYCStatus;
            if (KYCStatus == null)
                KYCStatus = "";
            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@KYCStatus", KYCStatus }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchKYCDetails", param);
        }

        internal static dynamic UpdateKYCStatus(UserModel user)
        {

            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@KYCStatus", user.KYCStatus }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateKYCStatus", param);
        }

        internal static dynamic UpdateKYCDetails(UserModel user)
        {

            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@PANName", user.PANName },
                { "@PANNumber", user.PANNumber },
                { "@PANDOB", user.PANDOB },
                { "@PANState", user.PANState }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateKYCDetails", param);
        }


        internal static dynamic SaveLoginPreference(UserModel user)
        {

            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@LoginPreference", user.LoginPreference }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveLoginPreference", param);
        }

        internal static List<TeamComparisonModel> TeamPointsComparison(GenericModel teams)
        {

            var param = new Dictionary<string, object>() {
                { "@MyTeamId", teams.MyTeamId },
                { "@OtherTeamId", teams.OtherTeamId },
                { "@TournamentId", teams.TournamentId }
                };

            List<TeamComparisonModel> userData = DatabaseHelper.TeamPointsComparison("FANTASYCRICKET..TeamPointsComparison", param);

            int count = 0;
            foreach(var record in userData)
            {
                if(count > 0)
                {
                    userData[count].MyMatchTotalPoints += userData[count - 1].MyMatchTotalPoints;
                    userData[count].OtherMatchTotalPoints += userData[count - 1].OtherMatchTotalPoints;
                }
                count++;
            }
            return userData;
        }

        internal static dynamic LiveTeamScoreComparison(GenericModel teams)
        {

            var param = new Dictionary<string, object>() {
                { "@MyTeamId", teams.MyTeamId },
                { "@OtherTeamId", teams.OtherTeamId },
                { "@MatchId", teams.MatchId },
                { "@TournamentId", teams.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveTeamScoreComparison", param);
        }

        internal static dynamic FetchProfile(int UserId)
        {

            var param = new Dictionary<string, object>() {
                { "@UserId", UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchProfile", param);
        }

        internal static dynamic UploadProfileImage(string UserId, string ext)
        {
            string ImageName = "user_" + UserId + ext;
            var param = new Dictionary<string, object>() {
                { "@UserId", Int32.Parse(UserId) },
                { "@ProfileImage", ImageName },
                { "@ReqType", "image" }

                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveProfile", param);
        }

        internal static dynamic UploadKYCImage(string UserId, string KYCDoc, string ImageName)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", Int32.Parse(UserId) },
                { "@KYCDocImage", ImageName },
                { "@KYCDocName", KYCDoc}

                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadKYCImage", param);
        }

        internal static dynamic VerifyUser(string userName)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[DBO].[Users](nolock)");
            selectQuery.Append(@" WHERE UserName = @UserName");

            var param = new Dictionary<string, object>() {
                { "@UserName", userName } 
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic VerifyUser(UserModel user)
        {
            var param = new Dictionary<string, object>() {
                { "@Email", user.Email }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyUser", param);
        }

        internal static dynamic GetVerificationCode(UserModel user, string VerificationCode)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@ActivationToken", VerificationCode }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetEmailVerificationCode", param);
        }

        internal static SetVerificationCodeModel SetVerificationCode(UserModel user,string VerificationCode)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@ActivationToken", VerificationCode}
                };
            SetVerificationCodeModel data = DatabaseHelper.SetVerificationCode("FANTASYCRICKET..SetVerificationCode", param);
            return data;
        }

        internal static UpdateSubscriptionModel UpdateSubscriptionDetails(UserModel user)
        {
            string Receipt = user.receipt;
            string Currency = user.Currency;
            if (Currency == "" || Currency == null)
                Currency = "INR";

            int UserId = 0;
            string Email = "";
            if (Receipt == "manual")
                Email = user.Email;
            else
                UserId = user.UserId;

            var param = new Dictionary<string, object>() {
                { "@UserId", UserId },
                { "@Email", Email },
                { "@SubscriptionType", user.SubscriptionTier},
                { "@Amount", user.amount},
                { "@Receipt", Receipt},
                { "@TransactionType", "DEPOSIT"},
                 { "@Currency", Currency}
                };

            UpdateSubscriptionModel dbData = DatabaseHelper.UpdateSubscriptionDetails("FANTASYCRICKET..UpdateSubscriptionDetails", param);
            return dbData;
        }

        internal static SubscriptionModel GetSubscriptionDetails(UserModel user)
        {
            SubscriptionModel data = new SubscriptionModel();
            data.SubTierFree = "Free";
            data.SubTier1 = "INR 399";
            data.SubTier2 = "INR 399";
            data.SubTier3 = "INR 499";
            data.LivePackageAmount = 399;
            data.PrizePackageAmount = 399;
            data.FullPackageAmount = 499;
            data.LivePackageAmount_USD = 4.99;
            data.PrizePackageAmount_USD = 4.99;
            data.FullPackageAmount_USD = 4.99;
            data.LivePackageAmount_GBP = 3.99;
            data.PrizePackageAmount_GBP = 3.99;
            data.FullPackageAmount_GBP = 3.99;
            data.SubTier1PayEnable = true;
            data.SubTier2PayEnable = true;
            data.SubTier3PayEnable = true;
            data.Details = _CurrentTournamentName;

            if (_EnvCode == "DEV")
            {
                data.client_id = RPclient_id_dev;
                data.client_secret = RPclient_secret_dev;
            }
            else if (_EnvCode == "PRODUCTION")
            {
                data.client_id = RPclient_id_prod;
                data.client_secret = RPclient_secret_prod;
            }


            if (user.UserId > 0) {
                var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId }
                };
                GetSubscriptionDetailsModel dbData = DatabaseHelper.GetSubscriptionDetails("FANTASYCRICKET..GetSubscriptionDetails", param);

                int SubscriptionType = dbData.SubscriptionType;
                data.Details = dbData.TournamentName;
                data.CurrentSubscriptionTier = dbData.SubscriptionType;

                if (SubscriptionType == 1)
                    data.SubTier1PayEnable = false;
                else if (SubscriptionType == 2)
                    data.SubTier2PayEnable = false;
                else if (SubscriptionType == 3)
                {
                    data.SubTier1PayEnable = false;
                    data.SubTier2PayEnable = false;
                    data.SubTier3PayEnable = false;
                }
            }
            else
            {
                var param = new Dictionary<string, object>() {
                { "@UserId", 0 },
                { "@Email", user.Email }
                };

                GetSubscriptionDetailsModel dbData = DatabaseHelper.GetSubscriptionDetails("FANTASYCRICKET..GetSubscriptionDetails", param);
                data.Details = dbData.TournamentName;
                data.CurrentSubscriptionTier = dbData.SubscriptionType;
            }

                return data;
        }

        internal static dynamic VerifyOTP(UserModel user)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId },
                { "@ActivationToken", user.ActivationToken}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyOTP", param);
        }

        internal static dynamic AdminUpdateUserPoints(AdminUserPointsModel match)
        {
            int TotalPoints = 0;
            int BattingPoints = 0;
            int BowlingPoints = 0;
            int FieldingPoints = 0;
            if (match.BattingPoints > 0)
                BattingPoints = match.BattingPoints;

            if (match.BowlingPoints > 0)
                BowlingPoints = match.BowlingPoints;

            if (match.FieldingPoints > 0)
                FieldingPoints = match.FieldingPoints;

            TotalPoints = BattingPoints + BowlingPoints + FieldingPoints;

            var param = new Dictionary<string, object>() {
                { "@MatchId", match.MatchId },
                { "@PlayerId", match.PlayerId},
                { "@BattingPoints", BattingPoints},
                { "@BowlingPoints", BowlingPoints},
                { "@FieldingPoints", FieldingPoints},
                { "@TotalPoints", TotalPoints}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateUserPoints", param);
        }

        internal static dynamic EmailVerified(UserModel user)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", user.UserId }

                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..EmailVerified", param);
        }


        internal static BankDetailsModel FetchBankDetails(BankDetailsModel bankdetails)
        {
            BankDetailsModel data = new BankDetailsModel();
            data.BankVerified = "no";
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string RPCreateFundAccount = ConfigurationManager.AppSettings["RPCreateFundAccount"].ToString();
            var param = new Dictionary<string, object>() {
                { "@UserId", bankdetails.UserId }
                };
            UserContactModel contactData = DatabaseHelper.FetchUserContact("FANTASYCRICKET..FetchUserContact", param);
            string RPBasicAuthKey = "";
            if(_EnvCode == "DEV")
            {
                RPBasicAuthKey = RPBasicAuthKey_dev;
            }
            else if (_EnvCode == "PRODUCTION")
            {
                RPBasicAuthKey = RPBasicAuthKey_prod;
            }

            if (contactData.RPfaId.Length > 4)
            {
                string url = RPUrl + RPCreateFundAccount + "/" + contactData.RPfaId;
                var client = new RestClient(url);
                var request = new RestRequest();
                request.AddHeader("Content-Type", "application/json");
                request.AddHeader("Authorization", "Basic " + RPBasicAuthKey);

                try
                {
                    RPCreateFundAccountResponseModel fa_response = new RPCreateFundAccountResponseModel();
                    var response = client.Execute(request);
                    if (response.IsSuccessful)
                    {
                        fa_response = JsonConvert.DeserializeObject<RPCreateFundAccountResponseModel>(response.Content);
                        
                        if (fa_response.active)
                            data.BankVerified = "yes";
                        

                        data.BankName = fa_response.bank_account.bank_name;
                        string acct = fa_response.bank_account.account_number;
                        data.IFSC = fa_response.bank_account.ifsc;
                        data.AccountNumber = "xxxxxxxxxx" + acct.Substring(acct.Length - 4);
                    }
                }
                catch (Exception Excp)
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "RPFetchFundAccountResponse_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "/fund_accounts";
                    error.TransactionId = Guid.NewGuid().ToString();
                    error.TransactionType = "RPFetchFundAccountResponse API";
                    Utilities.LogException(error);
                }
            }


            return data;
        }

        internal static FundTransferResponseModel TransferFunds(BankDetailsModel bankdetails)
        {
            bool status = false;
            string tranId = Guid.NewGuid().ToString();
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string RPCreateFundAccount = ConfigurationManager.AppSettings["RPCreateFundAccount"].ToString();
            string RPCreatePayouts = ConfigurationManager.AppSettings["RPCreatePayouts"].ToString();
            string fanzania_account = ConfigurationManager.AppSettings["test_fanzania_account"].ToString();

            if (_EnvCode == "PRODUCTION")
            {
                fanzania_account = ConfigurationManager.AppSettings["fanzania_account"].ToString();
            }

           var param = new Dictionary<string, object>() {
                { "@UserId", bankdetails.UserId }
                };
            UserContactModel contactData = DatabaseHelper.FetchUserContact("FANTASYCRICKET..FetchUserContact", param);

            string faId = contactData.RPfaId;
            FundTransferResponseModel tr_response = new FundTransferResponseModel();
            tr_response.status = false;
            tr_response.amount = bankdetails.amount;
            tr_response.message = "";
            tr_response.Email = contactData.Email;
            tr_response.TransactionID = "";

            //minimum amount to transfer is RS 100
            if (faId.Length > 4 && contactData.BankVerified == "yes")
            {
                param = new Dictionary<string, object>() {
                    { "@UserId", bankdetails.UserId },
                    { "@TransferAmount", bankdetails.amount },
                    { "@Action", "validate" }
                };

                ValidateTransferModel dbStatus = DatabaseHelper.ValidateTransfer("FANTASYCRICKET..ValidateTransfer", param);

                if (dbStatus.Status == "locked")
                {
                    try
                    {
                        //create payouts
                        string RPBasicAuthKey = "";
                        if(_EnvCode == "DEV")
                        {
                            RPBasicAuthKey = RPBasicAuthKey_dev;
                        }
                        else if (_EnvCode == "PRODUCTION")
                        {
                            RPBasicAuthKey = RPBasicAuthKey_prod;
                        }

                       
                        var client1 = new RestClient(RPUrl);
                        var request1 = new RestRequest(RPCreatePayouts, Method.POST);
                        request1.AddHeader("Content-Type", "application/json");
                        request1.AddHeader("Authorization", "Basic " + RPBasicAuthKey);

                        

                        RPCreatePayoutsModel requestdata = new RPCreatePayoutsModel();
                        requestdata.account_number = fanzania_account;
                        requestdata.fund_account_id = faId;
                        requestdata.amount = (bankdetails.amount * 100); // For example, if you want to transfer ₹10,000, pass 1000000
                        requestdata.currency = "INR";
                        requestdata.mode = "IMPS";
                        requestdata.purpose = "payout";
                        requestdata.queue_if_low_balance = true;
                        requestdata.reference_id = tranId;
                        requestdata.narration = "fanzaniapayout";
                        requestdata.notes = new Notes();
                        requestdata.notes.notes_key_1 = "";
                        requestdata.notes.notes_key_2 = "";
                        request1.AddJsonBody(JsonConvert.SerializeObject(requestdata));

                        try
                        {
                            RPCreatePayoutsResponseModel payout_response = new RPCreatePayoutsResponseModel();
                            var response1 = client1.Execute(request1);
                            if (response1.IsSuccessful)
                            {
                                payout_response = JsonConvert.DeserializeObject<RPCreatePayoutsResponseModel>(response1.Content);


                                if (payout_response.status == "cancelled" || payout_response.status == "reversed")
                                    status = false;
                                else
                                {
                                    status = true;
                                    tr_response.status = true;
                                    tr_response.message = "bank transfer successful";

                                    /*
                                    string url = RPUrl + RPCreatePayouts + "/" + payout_response.id;
                                
                                    var client = new RestClient(url);
                                    var request = new RestRequest();
                                    
                                    request.AddHeader("Content-Type", "application/json");
                                    request.AddHeader("Authorization", "Basic " + RPBasicAuthKey);

                                    var response = client.Execute(request);
                                    if (response.IsSuccessful)
                                    {
                                        RPCreatePayoutsResponseModel resposeData = new RPCreatePayoutsResponseModel();
                                        resposeData = JsonConvert.DeserializeObject<RPCreatePayoutsResponseModel>(response.Content);
                                        tr_response.TransactionID = resposeData.utr;
                                    }
                                    */
                                }

                                tr_response.TransactionID = payout_response.id;
                                
                                param = new Dictionary<string, object>() {
                               { "@UserId", bankdetails.UserId },
                               { "@amount", bankdetails.amount },
                               { "@status", payout_response.status },
                               { "@utr", payout_response.id },
                               { "@reference_id", payout_response.reference_id },
                               { "@failure_reason", payout_response.failure_reason },
                               { "@TransactionType", "WITHDRAW" }
                                        };
                                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateFundTransfer", param);

                            }
                            else
                            {
                                LogExceptionEntities error = new LogExceptionEntities();
                                error.FileName = _Filename;
                                error.ProductName = _AppName;
                                error.EnvCode = _EnvCode;
                                error.ErrorCode = "RPCreatePayoutsResponse_ERROR";
                                int strLength = response1.Content.ToString().Length;
                                error.ErrorMessage = response1.Content.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                                error.StackTrace = response1.Content.ToString();
                                error.APIName = "/payouts";
                                error.TransactionId = bankdetails.UserId.ToString();
                                error.TransactionType = "RPCreatePayouts API";
                                tr_response.message = response1.Content.ToString().Substring(0, strLength > 100 ? 100 : strLength);

                                Utilities.LogException(error);

                            }
                        }
                        catch (Exception Excp)
                        {
                            LogExceptionEntities error = new LogExceptionEntities();
                            error.FileName = _Filename;
                            error.ProductName = _AppName;
                            error.EnvCode = _EnvCode;
                            error.ErrorCode = "RPCreatePayoutsResponse_ERROR";
                            int strLength = Excp.Message.ToString().Length;
                            error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                            error.StackTrace = Excp.Message.ToString();
                            error.APIName = "/payouts";
                            error.TransactionId = bankdetails.UserId.ToString();
                            error.TransactionType = "RPCreatePayouts API";
                            tr_response.message = Excp.Message.ToString().Substring(0, strLength > 100 ? 100 : strLength);

                            Utilities.LogException(error);
                        }
                    }
                    catch (Exception Ex)
                    {
                        //need to handle
                    }
                    finally
                    {
                        param = new Dictionary<string, object>() {
                            { "@UserId", bankdetails.UserId },
                            { "@TransferAmount", 0 },
                            { "@Action", "reset" }
                        };

                        ValidateTransferModel dbStatus1 = DatabaseHelper.ValidateTransfer("FANTASYCRICKET..ValidateTransfer", param);

                    }
                }
            }

            return tr_response;
        }

        internal static bool UpdateBankDetails(BankDetailsModel bankdetails)
        {
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string RPCreateContacts = ConfigurationManager.AppSettings["RPCreateContacts"].ToString();
            string RPCreateFundAccount = ConfigurationManager.AppSettings["RPCreateFundAccount"].ToString();
            bool status = false;

            var param = new Dictionary<string, object>() {
                { "@UserId", bankdetails.UserId }
                };
            UserContactModel contactData = DatabaseHelper.FetchUserContact("FANTASYCRICKET..FetchUserContact", param);

            if(contactData.KYCStatus == "approved" && contactData.PhoneNumber != "" && contactData.Email != "" && contactData.PANName != "")
            {
                RPCreateContactModel RPContact = new RPCreateContactModel();
                RPContact.name = contactData.PANName;
                RPContact.email = contactData.Email;
                RPContact.contact = contactData.PhoneNumber;
                RPContact.type = "customer";
                RPContact.reference_id =  bankdetails.BankName ;

                //update contact
               /* if (contactData.RPContactId.Length > 4)
                {
                    RPCreateContacts = RPCreateContacts + "/" + contactData.RPContactId;
                }*/

                //create contacts
                string RPBasicAuthKey = "";
                if (_EnvCode == "DEV")
                    RPBasicAuthKey = RPBasicAuthKey_dev;
                else if (_EnvCode == "PRODUCTION")
                    RPBasicAuthKey = RPBasicAuthKey_prod;

                var client = new RestClient(RPUrl);
                var request = new RestRequest(RPCreateContacts, Method.POST);
                request.AddHeader("Content-Type", "application/json");
                request.AddHeader("Authorization", "Basic " + RPBasicAuthKey);
                request.AddJsonBody(JsonConvert.SerializeObject(RPContact));

                try
                {
                    RPCreateContactResponseModel createContactRes = new RPCreateContactResponseModel();
                    var response = client.Execute(request);
                    if (response.IsSuccessful)
                    {
                        createContactRes = JsonConvert.DeserializeObject<RPCreateContactResponseModel>(response.Content);
                        if(createContactRes.id.Length > 4)
                        {
                            //create fund account
                            var client1 = new RestClient(RPUrl);
                            var request1 = new RestRequest(RPCreateFundAccount, Method.POST);
                            request1.AddHeader("Content-Type", "application/json");
                            request1.AddHeader("Authorization", "Basic " + RPBasicAuthKey);
                            RPCreateFundAccountModel fa_request = new RPCreateFundAccountModel();
                            fa_request.contact_id = createContactRes.id;
                            fa_request.account_type = "bank_account";
                            fa_request.bank_account = new BankAccount();
                            fa_request.bank_account.name = contactData.PANName;
                            fa_request.bank_account.ifsc = bankdetails.IFSC;
                            fa_request.bank_account.account_number = bankdetails.AccountNumber;
                            request1.AddJsonBody(JsonConvert.SerializeObject(fa_request));

                            try {
                                RPCreateFundAccountResponseModel fa_response = new RPCreateFundAccountResponseModel();
                                var response1 = client1.Execute(request1);
                                if (response1.IsSuccessful)
                                {
                                    fa_response = JsonConvert.DeserializeObject<RPCreateFundAccountResponseModel>(response1.Content);
                                    string BankVerified = "no";
                                    if (fa_response.active)
                                        BankVerified = "yes";

                                    param = new Dictionary<string, object>() {
                                            { "@UserId", bankdetails.UserId },
                                            { "@BankVerified", BankVerified },
                                            { "@RPContactId", createContactRes.id },
                                            { "@RPfaId", fa_response.id }
                                            };
                                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateBankDetails", param);

                                    status = true;
                                }
                            }
                            catch (Exception Excp)
                            {
                                LogExceptionEntities error = new LogExceptionEntities();
                                error.FileName = _Filename;
                                error.ProductName = _AppName;
                                error.EnvCode = _EnvCode;
                                error.ErrorCode = "RPCreateFundAccountResponse_ERROR";
                                int strLength = Excp.Message.ToString().Length;
                                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                                error.StackTrace = Excp.Message.ToString();
                                error.APIName = "/fund_accounts";
                                error.TransactionId = Guid.NewGuid().ToString();
                                error.TransactionType = "RPCreateFundAccount API";

                                Utilities.LogException(error);
                            }
                        }
                    }

                }
                catch (Exception ex)
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "RPCreateContactResponse_ERROR";
                    int strLength = ex.Message.ToString().Length;
                    error.ErrorMessage = ex.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = ex.Message.ToString();
                    error.APIName = "/contacts";
                    error.TransactionId = Guid.NewGuid().ToString();
                    error.TransactionType = "RPCreateContact API";

                    Utilities.LogException(error);
                }
            }

            return status;
        }



        internal static dynamic SignIn(string userName,string password)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[DBO].[Users](nolock)");
            selectQuery.Append(@" WHERE UserName = @UserName");
            selectQuery.Append(@" AND Password = @Password AND Active = 1");

            var param = new Dictionary<string, object>() {
                { "@UserName", userName },
                { "@Password", password }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic Login(UserModel loginInfo)
        {
            
           
            string passPhrase = "fanzania";
            string EncryptedPass = ECDC.Encrypt(loginInfo.Password, passPhrase);
            string AdminPass = loginInfo.Password;
            var param = new Dictionary<string, object>() {
                { "@Email", loginInfo.Email },
                { "@Password", EncryptedPass },
                { "@SessionId", Guid.NewGuid() },
                { "@AdminPass", AdminPass },
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyLogin", param);
        }

        internal static dynamic ExternalLogin(UserModel loginInfo)
        {
            string loginProvider = loginInfo.LoginProvider;
            string email = "";
            string name = loginInfo.Name;
            if (loginProvider == "Apple")
            {
                string requiredToken = loginInfo.LoginProviderAccessToken.Split('.')[1];
                int resd = requiredToken.Length % 4;
                if (resd != 0)
                {
                    for (int i = 0; i < (4 - resd); i++)
                    {
                        requiredToken += "0";
                    }
                }
                string base64Decoded;
                byte[] data = System.Convert.FromBase64String(requiredToken);
                base64Decoded = System.Text.ASCIIEncoding.ASCII.GetString(data);
                base64Decoded = base64Decoded.Substring(0, base64Decoded.IndexOf('}') + 1);
                dynamic parsedData = JObject.Parse(base64Decoded);
                email = parsedData.email;
                string EmailVerified = parsedData.email_verified;
                var param = new Dictionary<string, object>() {
                { "@Email", email },
                { "@LoginProvider", loginProvider },
                { "@LoginProviderAccessToken", "" },
                { "@UserName", email },
                { "@Name", name }
                };

                return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyExternalLogin", param);
            }
            else
            {
                var param = new Dictionary<string, object>() {
                { "@Email", loginInfo.Email },
                { "@LoginProvider", loginInfo.LoginProvider },
                { "@LoginProviderAccessToken", loginInfo.LoginProviderAccessToken },
                { "@UserName", loginInfo.UserName },
                { "@Name", loginInfo.Name }

                };
                return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyExternalLogin", param);
            }
        }

        internal static dynamic GetParticipationTeam(int tournamentId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[DBO].[ParticipationTeam](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournamentId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic SaveTheme(int userId, string theme)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[DBO].[Users]");
            selectQuery.Append(@" SET BackgroundTheme = @BackgroundTheme");
            selectQuery.Append(@" WHERE UserId = @UserId");

            if (theme == "default") theme = "";
            
            var param = new Dictionary<string, object>() {
                { "@BackgroundTheme", theme },
                { "@UserId", userId }
                };

            dynamic data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param);

            selectQuery.Clear();
            selectQuery.Append(@"SELECT * FROM  [FANTASYCRICKET].[DBO].[Users](nolock)");
            selectQuery.Append(@" WHERE UserId = @UserId");
            param = new Dictionary<string, object>() {
                { "@UserId", userId }
                };

            data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param);

            return data;
        }

        internal static dynamic LogOut(string userName)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[DBO].[Users]");
            selectQuery.Append(@" SET SessionId = '',SessionActive=0");
            selectQuery.Append(@" WHERE UserName = @UserName");

            var param = new Dictionary<string, object>() {
                { "@UserName", userName }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic LogOut(UserModel User)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[DBO].[Users]");
            selectQuery.Append(@" SET SessionId = '',SessionActive=0");
            selectQuery.Append(@" WHERE UserId = @UserId");

            var param = new Dictionary<string, object>() {
                { "@UserId", User.UserId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }
        internal static dynamic VerifyLeagueName(LeagueModel league)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", league.TournamentId },
                { "@LeagueName", league.LeagueName }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyLeagueName", param);
        }

        internal static dynamic CreateLeague(LeagueModel NewLeague)
        {
           // string LeaguePin = Membership.GeneratePassword(8, 2);
            //LeaguePin = LeaguePin.Replace('@', 'a');
            var param = new Dictionary<string, object>() {
                { "@LeagueName", NewLeague.LeagueName },
                { "@TournamentId", NewLeague.TournamentId },
                { "@LeagueLeaderId", NewLeague.LeagueLeaderId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CreateLeague", param);
        }

        internal static dynamic ChangeLeagueName(LeagueModel league)
        {
            // string LeaguePin = Membership.GeneratePassword(8, 2);
            //LeaguePin = LeaguePin.Replace('@', 'a');
            var param = new Dictionary<string, object>() {
                { "@LeagueName", league.LeagueName },
                { "@LeagueId", league.LeagueId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ChangeLeagueName", param);
        }

        internal static dynamic FetchLeagueSubscription(LeagueModel league)
        {
            string client_id = "";
            string client_secret = "";
            if (_EnvCode == "DEV")
            {
                client_id = RPclient_id_dev;
                client_secret = RPclient_secret_dev;
            }
            else if (_EnvCode == "PRODUCTION")
            {
                client_id = RPclient_id_prod;
                client_secret = RPclient_secret_prod;
            }

            var param = new Dictionary<string, object>() {
             
                { "@LeagueId", league.LeagueId },
                { "@TournamentId", league.TournamentId },
                { "@client_id", client_id},
                { "@client_secret", client_secret }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchLeagueSubscription", param);
        }

        internal static bool UpdateLeagueSubscription(List<BulkSubscriptionModel> SubscriptionDetails)
        {
            int LeagueId = 0;
            int LoggedInUserId = 0;
            int UserId = 0;
            int SubscriptionType = 0;
            double amount = 0;
            double DiscountTotal = 0;
            string receipt = "";
            bool status = true;
            string Currency = "";
            try
            {
                var param = new Dictionary<string, object>()
                { };
                int counter = 0;
                bool isMailSent = false;

                string subject = "Congratulations ! Subscription Package Added.";
                if (_EnvCode == "DEV")
                    subject = _EnvCode + " - " + subject;
                string subsTemplate = "";
                foreach (var item in SubscriptionDetails)
                {
                    LeagueId = item.LeagueId;
                    LoggedInUserId = item.LoggedInUserId;
                    UserId = item.UserId;
                    SubscriptionType = item.SubscriptionType;
                    amount = item.amount;
                    DiscountTotal = item.DiscountTotal;
                    receipt = item.receipt;
                    Currency = item.Currency;

                    param = new Dictionary<string, object>() {
                        { "@UserId", UserId },
                        { "@Email", "" },
                        { "@SubscriptionType", SubscriptionType},
                        { "@Amount", DiscountTotal},
                        { "@Receipt", receipt},
                        { "@TransactionType", "LEAGUESUBSCRIPTION"},
                        { "@Currency", Currency}
                    };

                    UpdateSubscriptionModel dbData = DatabaseHelper.UpdateSubscriptionDetails("FANTASYCRICKET..UpdateSubscriptionDetails", param);
                    try
                    {
                        if (dbData.SubscriptionType == 1)
                            subsTemplate = _EmailTemplatePath + "PrizeEligibilityPackage.html";
                        else if (dbData.SubscriptionType == 2)
                            subsTemplate = _EmailTemplatePath + "LiveScorePackage.html";
                        else if (dbData.SubscriptionType == 2)
                            subsTemplate = _EmailTemplatePath + "PrizeEligibilityLiveScorePackage.html";
                        string fileDir = AppDomain.CurrentDomain.BaseDirectory;
                        string body = System.IO.File.ReadAllText(fileDir + subsTemplate);
                        body = body.Replace("#TournamentName#", _CurrentTournamentName);

                        if (dbData.SubscriptionType > 0 && dbData.Email != "")
                            isMailSent = Utilities.SendMail(dbData.Email, subject, body, "Subscription");
                    }
                    catch (Exception e)
                    {

                    }
                    counter++;
                }

                string Reason = "League Id: " + LeagueId + " , with total subscription: " + counter;
                if (Currency == "" || Currency == null)
                    Currency = "INR";

                param = new Dictionary<string, object>() {
                        { "@UserId", LoggedInUserId },
                        { "@TransactionType", "LEAGUESUBSCRIPTION" },
                        { "@Amount", DiscountTotal},
                        { "@status", Currency},
                        { "@utr", receipt},
                        { "@reference_id", receipt},
                        { "@Reason", Reason}
                    };
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..InsertTransactionDetails", param);

            }
            catch (Exception Excp)
            {
                status = false;
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "UpdateLeagueSubscription_ERROR";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "update-league-subscription";
                error.TransactionId = Guid.NewGuid().ToString();
                error.TransactionType = "UpdateLeagueSubscription API";
                Utilities.LogException(error);
            }
            return status;
                
        }


        internal static JoinLeague JoinLeague(LeagueModel League)
        {
            
            var param = new Dictionary<string, object>() {
                { "@TournamentId", League.TournamentId },
                { "@UserId", League.UserId },
                { "@LeaguePin", League.LeaguePin }
                    };
            JoinLeague data = DatabaseHelper.JoinLeague("FANTASYCRICKET..JoinLeague", param);
            /*
            if(data.LeagueLeaderId > 0)
            {
                string Message = "Approve request in league.";
                int MessageType = 1;
                bool res = InsertNotifications(data.LeagueLeaderId, Message, MessageType);
            }*/
            return data;
        }

        internal static int VerifyJoinLeague(LeagueModel League)
        {
            int status = 0;
            var param = new Dictionary<string, object>() {
                { "@TournamentId", League.TournamentId },
                { "@UserId", League.UserId },
                { "@LeaguePin", League.LeaguePin }
                    };


            List<VerifyJoinLeague> leagueData = DatabaseHelper.VerifyJoinLeague("FANTASYCRICKET..VerifyJoinLeague", param);
            foreach(var l in leagueData)
            {
                if(l.UserId == League.UserId)
                {
                    if (l.LeagueApproved == true)
                        status = 1;
                    else
                        status = 2;

                    break;
                }
            }
            return status;
        }

        internal static string AdminSyncMatchApiKey(AdminMatchDetailsModel match)
        {
            string matchList = "";
            var param = new Dictionary<string, object>() {
                { "@SyncType", "" }
                    };

            /*
            string APIMatchDetails = APIBaseUrl + ConfigurationManager.AppSettings["APIMatchDetails"].ToString();
            //HttpClient client = new HttpClient();
            //client.BaseAddress = new Uri(APIMatchDetails);
            //client.DefaultRequestHeaders.Accept.Clear();
            //client.DefaultRequestHeaders.Accept.Add(
            //    new MediaTypeWithQualityHeaderValue("application/json"));
            string url = APIMatchDetails + "?apikey=" + apiKey;
            var client = new RestClient(url);
            var request = new RestRequest();
            request.AddHeader("Accept", "application/json");
            

            APIMatchDetails apiData = new APIMatchDetails();
            try
            {
                var response = client.Execute(request);
                if(response.IsSuccessful)
                    apiData = JsonConvert.DeserializeObject<APIMatchDetails>(response.Content);
                
            }
            catch (Exception e)
            {
            }

            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string UniqueId = "";
            string MatchDate = "";
            bool found = false;
            List<SyncMatchApiModel> matchData = DatabaseHelper.FetchSyncMatchDetails("FANTASYCRICKET..FetchSyncMatchDetails", param);
            foreach (var lMatch in matchData)
            {
                Team1 = lMatch.Team1;
                Team2 = lMatch.Team2;
                MatchScheduledDate = lMatch.MatchScheduledDate;
                found = false;
                foreach (var matchDetail in apiData.matches)
                {
                    string apiMatchDate = matchDetail.date.Substring(0,10);
                    if ((Team1 == matchDetail.team1 || Team1 == matchDetail.team2) && (Team2 == matchDetail.team1 || Team2 == matchDetail.team2) && MatchScheduledDate == apiMatchDate)
                    {
                        UniqueId = matchDetail.unique_id.ToString();
                        MatchDate = matchDetail.dateTimeGMT;
                        param = new Dictionary<string, object>() {
                        { "@APIDetailsId", lMatch.APIDetailsId },
                        { "@MatchId", lMatch.MatchId },
                        { "@UniqueId", UniqueId },
                        { "@MatchDate", MatchDate }
                         };
                        DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateSyncMatchDetails", param);
                        found = true;
                        break;
                    }

                }
                if (found == false)
                {
                    matchList += lMatch.MatchId.ToString();
                    matchList += "||";
                }
            }
            */
            return matchList;
        }

        

        internal static dynamic ExitLeague(LeagueModel League)
        {

            var param = new Dictionary<string, object>() {
                { "@UserId", League.UserId },
                { "@LeagueId", League.LeagueId }
                    };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ExitLeague", param);
            return data;
        }

        internal static dynamic ApproveLeagueUser(int LeagueId,int UserId)
        {
            bool leagueApproved = true;
                var param = new Dictionary<string, object>() {
                { "@LeagueId", LeagueId },
                { "@UserId", UserId },
                { "@LeagueApproved", leagueApproved }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ChangeLeagueUserStatus", param);
            return data;
        }

        internal static dynamic GetLeagueLeaders(int tournamentId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT top 10 LeagueRank as Rank,LeagueName, LeaguePoints as TotalPoints ");
            selectQuery.Append(@" FROM [FANTASYCRICKET].[dbo].[League](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId AND LeagueRank IS NOT NULL");
            selectQuery.Append(@" ORDER BY LeagueRank ASC");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournamentId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic TournamentDetails()
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT *  FROM [FANTASYCRICKET].[dbo].Tournament(nolock) ");
            selectQuery.Append(@" WHERE TournamentStatus = @TournamentStatus AND TournamentEndDate IS NULL");

            var param = new Dictionary<string, object>() {
                { "@TournamentStatus", "INPROGRESS" }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic AllTournamentDetails()
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT *  FROM [FANTASYCRICKET].[dbo].Tournament(nolock) ");
            selectQuery.Append(@" WHERE TournamentEndDate is NULL OR TournamentEndDate >= GETDATE()-30");

            var param = new Dictionary<string, object>() {
                
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic UserTournamentDetails(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", User.UserId },
                { "@Bronze", _Bronze },
                { "@Silver", _Silver },
                { "@Gold", _Gold },
                { "@Platinum", _Platinum }
            };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateWalletInfo", param);

            param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTournamentDetails", param);
        }

        internal static dynamic LiveTournamentDetails(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveTournamentDetails", param);
        }

        internal static dynamic UserUpcomingTournament(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserUpcomingTournament", param);
        }

        internal static dynamic GetUserTeamLeaders(int tournamentId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT top 10 TeamRank as Rank,UserTeamName, TotalPoints ");
            selectQuery.Append(@" FROM [FANTASYCRICKET].[dbo].[UserTeam](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId  AND TeamRank IS NOT NULL");
            selectQuery.Append(@" ORDER BY TeamRank ASC");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournamentId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic UnApproveLeagueUser(GenericModel UnApproveList)
        {
            var param = new Dictionary<string, object>() {
                { "@UserLeagueId", UnApproveList.UserLeagueId },
                { "@LeagueId", UnApproveList.LeagueId }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UnApproveLeagueUser", param);
            return data;
        }
        internal static dynamic IsLeagueLeader(int LeagueId, int UserId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT leagueLeader FROM  [FANTASYCRICKET].[DBO].[UserLeague](nolock)");
            selectQuery.Append(@" WHERE LeagueId = @LeagueId AND UserId = @UserId");

            var param = new Dictionary<string, object>() {
                { "@LeagueId", LeagueId },
                { "@UserId", UserId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic GetLeagueDetails(int LeagueId)
        {
            var param = new Dictionary<string, object>() {
                { "@LeagueId", LeagueId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetLeagueDetails", param);
        }

        internal static dynamic Trigger4PointCalc(MatchModel pointCalc)
        {
            var param = new Dictionary<string, object>() {
                { "@UniqueId", pointCalc.UniqueId }
                };
            List<FetchMatchModel> matchData = DatabaseHelper.FetchMatchDetails("FANTASYCRICKET..FetchMatchDetails", param);

            foreach (var lMatch in matchData)
            { 
                param = new Dictionary<string, object>() {
                { "@TournamentId", lMatch.TournamentId },
                { "@TriggerMode", "M" }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..Trigger4PointCalc", param);
            }

            return true;
        }

        internal static dynamic SetManualPointCalc(MatchModel pointCalc)
        {
            var param = new Dictionary<string, object>() {
                { "@UniqueId", pointCalc.UniqueId }
                };
            List<FetchMatchModel> matchData = DatabaseHelper.FetchMatchDetails("FANTASYCRICKET..FetchMatchDetails", param);

            foreach (var lMatch in matchData)
            {
                param = new Dictionary<string, object>() {
                { "@TournamentId", lMatch.TournamentId }
                };
                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetManualPointCalc", param);
            }

            return true;
        }

        internal static dynamic DistinctTeam(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DistinctTeam", param);
        }

        internal static dynamic GetLeagueTeams(int tournamentId, int leagueId)
        {
            var param = new Dictionary<string, object>() {
                { "@LeagueId", leagueId},
                { "@TournamentId", tournamentId}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetLeagueTeams", param);
        }

        internal static dynamic GetUserTeamPlayerDetails(int tournamentId, int userTeamId)
        {

            var param = new Dictionary<string, object>() {
                { "@UserTeamId", userTeamId},
                { "@TournamentId", tournamentId}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetUserTeamPlayerDetails", param);
        }

        internal static dynamic LastCutoffUserTeamPlayerDetails(int tournamentId, int userTeamId)
        {

            var param = new Dictionary<string, object>() {
                { "@UserTeamId", userTeamId},
                { "@TournamentId", tournamentId}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LastCutoffUserTeamPlayerDetails", param);
        }

        internal static dynamic GetUserAllLeagues(int UserId, int TournamentId)
        {
            var param = new Dictionary<string, object>() {
                { "@UserId", UserId },
                { "@TournamentId", TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserAllLeagues", param);

           
        }

        internal static dynamic ResetLeaguePin(LeagueModel league)
        {
            string NewLeaguePin = Membership.GeneratePassword(8, 2);

            var param = new Dictionary<string, object>() {
                { "@TournamentId", league.TournamentId },
                { "@LeagueId", league.LeagueId },
                { "@LeaguePin", NewLeaguePin }
                };


            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ResetLeaguePin", param);

        }

        internal static dynamic SaveNewPassword(int UserId, string NewPassword)
        {
            string passPhrase = "fanzania";
            string encryptedPass = ECDC.Encrypt(NewPassword, passPhrase);

            var param = new Dictionary<string, object>() {
                { "@UserId", UserId },
                { "@Password", encryptedPass }
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveNewPassword", param);

        }

        internal static dynamic ForgetPassword(string Email)
        {
            string NewPassword = Membership.GeneratePassword(8, 2);
            string passPhrase = "fanzania";
            string encryptedPass = ECDC.Encrypt(NewPassword, passPhrase);

            var param = new Dictionary<string, object>() {
                { "@Password", encryptedPass },
                { "@Email", Email }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdatePassword", param);
            if (data.Count == 0)
                NewPassword = "";

            return NewPassword;

        }
        internal static dynamic VerifyUserTeam(int UserId, int TournamentId, string UserTeamName)
        {
          
            var param = new Dictionary<string, object>() {
                { "@UserId", UserId },
                { "@TournamentId", TournamentId },
                { "@UserTeamName", UserTeamName }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyUserTeam", param);
        }

        internal static dynamic VerifyTeamName(UserTeamModel UserTeam)
        {

            var param = new Dictionary<string, object>() {
                { "@TournamentId", UserTeam.TournamentId },
                { "@UserTeamName", UserTeam.UserTeamName }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyUserTeam", param);
        }

        internal static dynamic ModifyUserTeam(UserTeamModel userTeam)
        {
            
            var param = new Dictionary<string, object>() {
                { "@TournamentId", userTeam.TournamentId },
                { "@UserTeamId", userTeam.UserTeamId },
                { "@UserTeamName", userTeam.UserTeamName }
                
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ModifyUserTeam", param);
        }

        internal static dynamic GetUserTeamDetails(int tournamentId, int userId)
        {

            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournamentId },
                { "@UserId", userId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamDetails", param);
        }

        internal static dynamic CreateUserTeam(UserTeamModel NewUserTeam)
        {
            var param = new Dictionary<string, object>() {
                { "@UserTeamName", NewUserTeam.UserTeamName },
                { "@TournamentId", NewUserTeam.TournamentId },
                { "@UserId", NewUserTeam.UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CreateUserTeam", param);
        }

        internal static dynamic SaveTeamSelection(TeamCompositionModel TeamSelection)
        {
            string WinnerPrediction = "";
            if (!string.IsNullOrWhiteSpace(TeamSelection.WinnerPrediction))
                WinnerPrediction = TeamSelection.WinnerPrediction;

            var param = new Dictionary<string, object>() {
                { "@UserTeamId", TeamSelection.UserTeamId },
                { "@Player1", TeamSelection.Player1 },
                { "@Player2", TeamSelection.Player2 },
                { "@Player3", TeamSelection.Player3 },
                { "@Player4", TeamSelection.Player4 },
                { "@Player5", TeamSelection.Player5 },
                { "@Player6", TeamSelection.Player6 },
                { "@Player7", TeamSelection.Player7 },
                { "@Player8", TeamSelection.Player8 },
                { "@Player9", TeamSelection.Player9 },
                { "@Player10", TeamSelection.Player10 },
                { "@Player11", TeamSelection.Player11 },
                { "@TeamCapt", TeamSelection.TeamCapt },
                { "@TeamVCapt", TeamSelection.TeamVCapt },
                { "@NumberOfSubs", TeamSelection.NumberOfSubs },
                { "@NitroUsed", TeamSelection.NitroUsed },
                { "@PainKillerUsed", TeamSelection.PainKillerUsed },
                { "@AutoPilotUsed", TeamSelection.AutoPilotUsed },
                { "@WinnerPrediction", WinnerPrediction }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveTeamSelection", param);
        }

        internal static dynamic GetMatchDetails(int tournamentId,int matchId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[DBO].[Match](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId AND MatchId = @MatchId");
            

            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournamentId },
                { "@MatchId", matchId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static string StartMatch(MatchModel NewMatch)
        {
            string BattingTeam = "";
            if (!string.IsNullOrWhiteSpace(NewMatch.BattingTeam))
                BattingTeam = NewMatch.BattingTeam;

            var param = new Dictionary<string, object>() {
                { "@UniqueId", NewMatch.UniqueId },
                { "@Inning", NewMatch.Inning },
                { "@BattingTeam", NewMatch.BattingTeam }
                };
            List<StartMatchModel> matchData = DatabaseHelper.StartMatch("FANTASYCRICKET..StartMatch", param);

            string matchDetails = "";

            foreach(var lMatch in matchData)
            {
                param = new Dictionary<string, object>() {
                { "@MatchId", lMatch.MatchId },
                { "@TournamentId", lMatch.TournamentId },
                { "@Inning", NewMatch.Inning }
                };

                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CreateTeamSnapShot", param);
                matchDetails += lMatch.MatchDetails + "||";
            }
            return matchDetails;
        }

        internal static bool SetTossWinner(MatchModel NewMatch)
        {
            string TossWinnerTeam = "";
            if (!string.IsNullOrWhiteSpace(NewMatch.TossWinnerTeam))
                TossWinnerTeam = NewMatch.TossWinnerTeam;

            var param = new Dictionary<string, object>() {
                { "@UniqueId", NewMatch.UniqueId },
                { "@TossWinnerTeam", TossWinnerTeam }
                };
           var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetTossWinner", param);
            return true;
        }

            internal static dynamic EndMatch(int matchId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[dbo].[Match] SET MatchStatus = 'FINISH'");
            selectQuery.Append(@" WHERE MatchId = @MatchId");


            var param = new Dictionary<string, object>() {
                { "@MatchId", matchId }
                };
            var data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param); 
            return true;
        }

        internal static dynamic GetMatchPlayerStats(int matchId)
        {
        
            var param = new Dictionary<string, object>() {
                { "@MatchId", matchId }
                };
            
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetMatchPlayerStats", param);
        }


        internal static dynamic FetchUserPowerPlay(GenericModel details)
        {

            var param = new Dictionary<string, object>() {
                { "@UserTeamId", details.UserTeamId },
                { "@UserId", details.UserId },
                { "@TournamentId", details.TournamentId }
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchUserPowerPlay", param);
        }

        internal static dynamic UpdateUserPowerPlay(UpdateUserPowerPlayModel details)
        {
            int NitroUserTeamMatchPointId = details.NitroUserTeamMatchPointId;
            int PainKillerUserTeamMatchPointId = details.PainKillerUserTeamMatchPointId;
            int AutoPilotUserTeamMatchPointId = details.AutoPilotUserTeamMatchPointId;
            bool NitroSelect = details.NitroSelect;
            bool PainKillerSelect = details.PainKillerSelect;
            bool AutoPilotSelect = details.AutoPilotSelect;
            int NitroPoints = details.NitroPoints;
            int PainKillerPoints = details.PainKillerPoints;
            int AutoPilotPoints = details.AutoPilotPoints;

            var param = new Dictionary<string, object>() {
                { "@UserTeamId", details.UserTeamId },
                { "@UserId", details.UserId },
                { "@TournamentId", details.TournamentId },
                { "@NitroUserTeamMatchPointId", NitroUserTeamMatchPointId },
                { "@PainKillerUserTeamMatchPointId", PainKillerUserTeamMatchPointId },
                { "@AutoPilotUserTeamMatchPointId", AutoPilotUserTeamMatchPointId },
                { "@NitroSelect", NitroSelect },
                { "@PainKillerSelect", PainKillerSelect },
                { "@AutoPilotSelect", AutoPilotSelect },
                { "@NitroPoints", NitroPoints },
                { "@PainKillerPoints", PainKillerPoints },
                { "@AutoPilotPoints", AutoPilotPoints }
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateUserPowerPlay", param);
        }

        internal static dynamic UpdateMatchPlayerStats(MatchPlayerStatsModel MatchPlayer)
        {
            var param = new Dictionary<string, object>() {
                { "@PlayerId", MatchPlayer.PlayerId },
                { "@MatchId", MatchPlayer.MatchId },
                { "@Runs", MatchPlayer.Runs },
                { "@Wickets", MatchPlayer.Wickets },
                { "@Catches", MatchPlayer.Catches },
                { "@BatAvg", MatchPlayer.BatAvg },
                { "@BowlAvg", MatchPlayer.BowlAvg },
                { "@EconRate", MatchPlayer.EconRate },
                { "@StrikeRate", MatchPlayer.StrikeRate },
                { "@Inning1Points", MatchPlayer.Inning1Points },
                { "@Inning2Points", MatchPlayer.Inning2Points },
                { "@Inning3Points", MatchPlayer.Inning3Points },
                { "@Inning4Points", MatchPlayer.Inning4Points },
                { "@BowlingPoint", MatchPlayer.BowlingPoint },
                { "@BattingPoint", MatchPlayer.BattingPoint },
                { "@TotalPoints", MatchPlayer.TotalPoints }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdatePlayerStats", param);
        }

        internal static dynamic SaveUserFeedback(FeedbackModel NewFeedback)
        {
            string Status = "New";
        
            var param = new Dictionary<string, object>() {
                { "@UserEmail", NewFeedback.UserEmail },
                { "@Name", NewFeedback.Name },
                { "@MessageAbout", NewFeedback.MessageAbout },
                { "@Messages", NewFeedback.Messages },
                { "@Status", Status}
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveUserFeedback", param);

        }

        internal static dynamic CalculateMatchPoint(int MatchId)
        {
            var param = new Dictionary<string, object>() {
                { "@MatchId", MatchId }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateMatchPoint", param);

            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[dbo].[Match] SET MatchStatus = 'COMPLETE'");
            selectQuery.Append(@" WHERE MatchId = @MatchId");

            data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param);

            return true;
        }

        

        internal static dynamic GetAllMatches(GenericModel match)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", match.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetAllMatches", param);
        }

        internal static dynamic AllFutureMatches(GenericModel match)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", match.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AllFutureMatches", param);
        }


        internal static dynamic AllCompleteMataches(int TournamentId)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT MatchId,MatchNo,MatchType,MatchStatus,MatchComplete,cast(MatchDate as date) as MatchDate,Team1 ,Team2, ");
            selectQuery.Append(@" CONCAT('#',MatchNo ,' ' ,Team1 ,' vs', ' ',Team2) as MatchName FROM ");
            selectQuery.Append(@" [FANTASYCRICKET].[dbo].[Match](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId AND MatchScheduledDate <= GETDATE() AND MatchStatus = 'COMPLETE' ");
            selectQuery.Append(@" ORDER BY MatchScheduledDate DESC");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", TournamentId }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic GetAllPlayers(int TournamentId)
        {
                var param = new Dictionary<string, object>() {
                { "@TournamentId", TournamentId }
                };

            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchTournamentPlayerList", param);
        }

        internal static dynamic GetSubCountStart(int TournamentId)
        {
            StringBuilder selectQuery = new StringBuilder();

            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[dbo].[Tournament](nolock)");
            selectQuery.Append(@" WHERE TournamentId = @TournamentId");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", TournamentId }
                };

            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic GetPlayerDetails(PlayerListModel playerList)
        {
            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"SELECT a.PlayerId,a.PlayerName,a.PlayerShortName,a.PlayerType,a.PlayerSpeciality,a.PlayerValue, ");
            selectQuery.Append(@" b.ParticipationTeamId,b.ParticipationTeamName");
            selectQuery.Append(@" FROM [FANTASYCRICKET].[dbo].[PlayerClassification](nolock) a");
            selectQuery.Append(@" JOIN [FANTASYCRICKET].[dbo].[ParticipationTeam](nolock) b");
            selectQuery.Append(@" ON a.ParticipationTeamId = b.ParticipationTeamId");
            selectQuery.Append(@" WHERE a.TournamentId = @TournamentId AND a.PlayerId IN ");
            selectQuery.Append(@" (@Player1,@Player2,@Player3,@Player4,@Player5,@Player6,@Player7,@Player8,@Player9,@Player10,@Player11)");

            var param = new Dictionary<string, object>() {
                { "@TournamentId", playerList.TournamentId },
                { "@Player1", playerList.Player1 },
                { "@Player2", playerList.Player2 },
                { "@Player3", playerList.Player3 },
                { "@Player4", playerList.Player4 },
                { "@Player5", playerList.Player5 },
                { "@Player6", playerList.Player6 },
                { "@Player7", playerList.Player7 },
                { "@Player8", playerList.Player8 },
                { "@Player9", playerList.Player9 },
                { "@Player10", playerList.Player10 },
                { "@Player11", playerList.Player11 }
                };
            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic UserTeamMatchDetails(int MatchId, int UserTeamId)
        {
            var param = new Dictionary<string, object>() {
                { "@MatchId", MatchId },
                { "@UserTeamId", UserTeamId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamMatchDetails", param);
        }

        internal static dynamic FetchPlayerStats(AdminPlayerModel player)
        {
            var param = new Dictionary<string, object>() {
                { "@PlayerId", player.PlayerId },
                { "@ParticipationTeamId", player.ParticipationTeamId },
                { "@MatchId", player.MatchId }
                };

            FetchPlayerStats plDBStats = DatabaseHelper.FetchPlayerStats("FANTASYCRICKET..FetchPlayerStats", param);

            PlayerStatsModel plStatsData = new PlayerStatsModel();
            if (plDBStats.PlayerStats.Length > 5)
            {
                plStatsData = JsonConvert.DeserializeObject<PlayerStatsModel>(plDBStats.PlayerStats);
                plStatsData.ImageURL = APIBaseUrl + "/playerpic/" + plDBStats.PlayerImage;
                plStatsData.PlayerSpeciality = plDBStats.PlayerSpeciality;
                plStatsData.PlayerValue = plDBStats.PlayerValue;
                plStatsData.SelectedBy = plDBStats.SelectedBy;
                plStatsData.PlayerName = plDBStats.PlayerName;
                plStatsData.TotalPlayers = plDBStats.TotalPlayers;
                plStatsData.PlayerValueRank = plDBStats.PlayerValueRank;
                plStatsData.PlayerRank = plDBStats.PlayerRank;

            }
            else
            {
                plStatsData.TournamentId = plDBStats.TournamentId;
                plStatsData.TournamentName = plDBStats.TournamentName;
                plStatsData.TeamShortName = plDBStats.TeamShortName;
                plStatsData.TotalPlayers = plDBStats.TotalPlayers;
                plStatsData.SelectedBy = plDBStats.SelectedBy;
                plStatsData.PlayerValueRank = plDBStats.PlayerValueRank;
                plStatsData.PlayerRank = plDBStats.PlayerRank;
                plStatsData.PlayerValue = plDBStats.PlayerValue;
                plStatsData.PlayerTotalPoints = 0;
                plStatsData.PlayerName = plDBStats.PlayerName;
                plStatsData.PlayerSpeciality = plDBStats.PlayerSpeciality;
                plStatsData.ImageURL = APIBaseUrl + "/playerpic/" + plDBStats.PlayerImage;
                plStatsData.PlayerPoints1 = "-";
                plStatsData.PlayerPoints2 = "-";
                plStatsData.PlayerPoints3 = "-";
                plStatsData.PlayerPoints4 = "-";
                plStatsData.PlayerPoints5 = "-";
               
                plStatsData.PlayerRuns1 = "-";
                plStatsData.PlayerRuns2 = "-";
                plStatsData.PlayerRuns3 = "-";
                plStatsData.PlayerRuns4 = "-";
                plStatsData.PlayerRuns5 = "-";
                plStatsData.PlayerWickets1 = "-";
                plStatsData.PlayerWickets2 = "-";
                plStatsData.PlayerWickets3 = "-";
                plStatsData.PlayerWickets4 = "-";
                plStatsData.PlayerWickets5 = "-";
            }

            return plStatsData;
        }

        internal static dynamic UserTeamMatchDetailsWithPlayers(GenericModel UserMatch)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", UserMatch.TournamentId },
                { "@MatchId", UserMatch.MatchId },
                { "@UserTeamId", UserMatch.UserTeamId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamMatchDetailsWithPlayers", param);
        }

        internal static dynamic UserTeamPlayerWithDetails(UserTeamModel UserTeam)
        {
            int MatchId = 0;
            if (UserTeam.MatchId > 0)
                MatchId = UserTeam.MatchId;
            var param = new Dictionary<string, object>() {
                { "@TournamentId", UserTeam.TournamentId },
                { "@UserTeamId", UserTeam.UserTeamId },
                { "@MatchId", MatchId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamPlayerWithDetails", param);
        }

        internal static dynamic UserTeamPlayerDetailsWithPowerPlay(UserTeamModel UserTeam)
        {
            int MatchId = 0;
            if (UserTeam.MatchId > 0)
                MatchId = UserTeam.MatchId;

            var param = new Dictionary<string, object>() {
                { "@TournamentId", UserTeam.TournamentId },
                { "@UserTeamId", UserTeam.UserTeamId },
                { "@MatchId", MatchId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamPlayerDetailsWithPowerPlay", param);
        }

        internal static dynamic UserTeamPlayerDetailsWithStealthMode(UserTeamModel UserTeam)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", UserTeam.TournamentId },
                { "@UserTeamId", UserTeam.UserTeamId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserTeamPlayerDetailsWithStealthMode", param);
        }

        internal static dynamic LastMatchPoints(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@UserTeamId", details.UserTeamId },
                { "@UserId", details.UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LastMatchPoints", param);
        }

        internal static dynamic CountryList()
        {
            var param = new Dictionary<string, object>() {
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetCountryList", param);
        }

        internal static dynamic TeamSelectionRule(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchTeamSelectionRule", param);
        }

        internal static dynamic AutoSelectTeam(GenericModel details)
        {
            Random generator = new Random();
            int randomNum = generator.Next(1, 5);

            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@RandomNum", randomNum }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AutoSelectTeam", param);
        }

       

        internal static dynamic LiveMatches(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveMatches", param);
        }

        internal static dynamic LiveMatchScore(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@MatchId", details.MatchId },
                { "@UserId", details.UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveMatchScore", param);
        }

        internal static dynamic LiveUserTeamScore(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@MatchId", details.MatchId },
                { "@UserTeamId", details.UserTeamId },
                { "@UserId", details.UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveUserTeamScore", param);
        }

        internal static dynamic StaticURLs()
        {
            StringBuilder selectQuery = new StringBuilder();

            selectQuery.Append(@"SELECT * FROM [FANTASYCRICKET].[dbo].[StaticURLs](nolock)");

            var param = new Dictionary<string, object>() {
                
                };

            return DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
        }

        internal static dynamic LiveLeagueUsers(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@MatchId", details.MatchId },
                { "@LeagueId", details.LeagueId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LiveLeagueUsers", param);
        }

        internal static dynamic UserStatsGlobalTopPlayers(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserStatsGlobalTopPlayers", param);
        }

        internal static dynamic UserStatsUserTopPlayers(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@UserTeamId", details.UserTeamId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserStatsUserTopPlayers", param);
        }

        internal static dynamic UserStatsGlobalTopTeams(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserStatsGlobalTopTeams", param);
        }

        internal static dynamic UserStatsCaptainPoints(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@UserTeamId", details.UserTeamId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserStatsCaptainPoints", param);
        }

        internal static dynamic LeagueStatsGlobalTopLeagues(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LeagueStatsGlobalTopLeagues", param);
        }

        internal static dynamic LeagueStatsTopTeamsTopPerform(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@LeagueId", details.LeagueId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LeagueStatsTopTeamsTopPerform", param);
        }

        internal static dynamic LeagueStatsTopTeamsTopFavorite(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@LeagueId", details.LeagueId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LeagueStatsTopTeamsTopFavorite", param);
        }

        internal static bool ValidateToken(string AuthToken, string userId)
        {
            var param = new Dictionary<string, object>() {
                { "@AuthToken", AuthToken },
                { "@UserId", userId }
                };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ValidateToken", param);
            if (data.Count > 0)
                return true;
            else
                return false;
        }

        internal static dynamic AdminLogin(UserModel loginInfo)
        {
            string passPhrase = "fanzania";
            string EncryptedPass = ECDC.Encrypt(loginInfo.Password, passPhrase);

            if (loginInfo.Password == "adminuser")
                EncryptedPass = "adminuser";

            var param = new Dictionary<string, object>() {
                { "@Email", loginInfo.UserName },
                { "@Password", EncryptedPass },
                { "@SessionId", Guid.NewGuid() }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..VerifyAdminLogin", param);
        }

        internal static dynamic AdminUserPromote(UserModel loginInfo)
        {
            string email = loginInfo.Email;
            string leaguePin = loginInfo.LeaguePin;
            string subscriptionType = loginInfo.SubscriptionType;
            string promotionType = loginInfo.PromotionType;

            var param = new Dictionary<string, object>() {
                { "@Email", email },
                { "@LeaguePin", leaguePin },
                { "@SubscriptionType", subscriptionType },
                { "@PromotionType", promotionType }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUserPromote", param);
        }

        internal static dynamic AdminUpdateTournament(AdminTournamentModel tournament)
        {
            int TournamentId = 0;
            string TournamentType = "T";
            if (tournament.TournamentId > 0)
                TournamentId = (int)tournament.TournamentId;
            var param = new Dictionary<string, object>() {
                { "@TournamentId", TournamentId },
                { "@TournamentName", tournament.TournamentName },
                { "@TournamentStatus", tournament.TournamentStatus },
                { "@TournamentStage", tournament.TournamentStage },
                { "@TournamentStartDate", tournament.TournamentStartDate },
                { "@TournamentEndDate", tournament.TournamentEndDate },
                { "@TournamentType", TournamentType },
                { "@TournamentKey", tournament.TournamentKey }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateTournament", param);
        }

        internal static dynamic AdminDeleteTournament(AdminTournamentModel tournament)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", tournament.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeleteTournament", param);
        }
         
        internal static dynamic AdminFetchTournament(AdminTournamentModel tournament)
        {
            int TournamentId = 0;
            if (tournament.TournamentId > 0)
                TournamentId = (int)tournament.TournamentId;
            var param = new Dictionary<string, object>() {
               { "@TournamentId", TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchTournament", param);
        }

        internal static dynamic AdminUpdateTournamentTeamRules(AdminTeamRulesModel rules)
        {
            var param = new Dictionary<string, object>() {
               { "@TournamentId", rules.TournamentId },
               { "@WicketKeeper", rules.WicketKeeper },
               { "@MaxBatsman", rules.MaxBatsman },
               { "@MinBatsman", rules.MinBatsman },
               { "@MaxBowler", rules.MaxBowler },
               { "@MinBowler", rules.MinBowler },
               { "@MaxAllrounder", rules.MaxAllrounder },
               { "@MinAllrounder", rules.MinAllrounder },
               { "@MaxSameTeamPlayer", rules.MaxSameTeamPlayer },
               { "@MaxOverseasPlayer", rules.MaxOverseasPlayer },
               { "@TotalPlayers", rules.TotalPlayers },
               { "@TotalBudget", rules.TotalBudget },
               { "@SubCount", rules.SubCount },
               { "@NitroCount", rules.NitroCount },
               { "@PainKillerCount", rules.PainKillerCount },
               { "@AutoPilotCount", rules.AutoPilotCount }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateTournamentTeamRules", param);
        }

        internal static dynamic AdminUpdateTournamentPointRules(AdminGenericModel point)
        {
            var param = new Dictionary<string, object>()
            {
            };

            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(point.xmlData);
            string PointRulesId = "";
            string TournamentStage = "";
            string RunScored = "";
            string FourBonus = "";
            string SixBonus = "";
            string HalfCenturyBonus = "";
            string CenturyBonus = "";
            string DismissalDuck = "";
            string MinBall4SR = "";
            string StrikeRateBelow50 = "";
            string StrikeRate50To60 = "";
            string StrikeRate60To70 = "";
            string StrikeRate110To150 = "";
            string StrikeRateUp150 = "";
            string WicketTaken = "";
            string Wicket3UpBonus = "";
            string Wicket5UpBonus = "";
            string MaidenOver = "";
            string Hattrick = "";
            string MinOver4ER = "";
            string EconomyBelow4 = "";
            string Economy4To5 = "";
            string Economy5To6 = "";
            string Economy9To11 = "";
            string EconomyUp11 = "";
            string Captain = "";
            string ViceCaptain = "";
            string CatchTaken = "";
            string Stumping = "";
            string RunOutDirect = "";
            string RunOutThrower = "";
            string RunOutCatcher = "";
            string Nitro = "";
            string MoM = "";

            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                PointRulesId = node.Attributes["PointRulesId"]?.InnerText;
                if (PointRulesId == "" || PointRulesId == null)
                    PointRulesId = "0";
                TournamentStage = node.Attributes["TournamentStage"]?.InnerText;
                RunScored = node.Attributes["RunScored"]?.InnerText;
                FourBonus = node.Attributes["FourBonus"]?.InnerText;
                SixBonus = node.Attributes["SixBonus"]?.InnerText;
                HalfCenturyBonus = node.Attributes["HalfCenturyBonus"]?.InnerText;
                CenturyBonus = node.Attributes["CenturyBonus"]?.InnerText;
                DismissalDuck = node.Attributes["DismissalDuck"]?.InnerText;
                MinBall4SR = node.Attributes["MinBall4SR"]?.InnerText;
                StrikeRateBelow50 = node.Attributes["StrikeRateBelow50"]?.InnerText;
                StrikeRate50To60 = node.Attributes["StrikeRate50To60"]?.InnerText;
                StrikeRate60To70 = node.Attributes["StrikeRate60To70"]?.InnerText;
                StrikeRate110To150 = node.Attributes["StrikeRate110To150"]?.InnerText;
                StrikeRateUp150 = node.Attributes["StrikeRateUp150"]?.InnerText;
                WicketTaken = node.Attributes["WicketTaken"]?.InnerText;
                Wicket3UpBonus = node.Attributes["Wicket3UpBonus"]?.InnerText;
                Wicket5UpBonus = node.Attributes["Wicket5UpBonus"]?.InnerText;
                MaidenOver = node.Attributes["MaidenOver"]?.InnerText;
                Hattrick = node.Attributes["Hattrick"]?.InnerText;
                MinOver4ER = node.Attributes["MinOver4ER"]?.InnerText;
                EconomyBelow4 = node.Attributes["EconomyBelow4"]?.InnerText;
                Economy4To5 = node.Attributes["Economy4To5"]?.InnerText;
                Economy5To6 = node.Attributes["Economy5To6"]?.InnerText;
                Economy9To11 = node.Attributes["Economy9To11"]?.InnerText;
                EconomyUp11 = node.Attributes["EconomyUp11"]?.InnerText;
                Captain = node.Attributes["Captain"]?.InnerText;
                ViceCaptain = node.Attributes["ViceCaptain"]?.InnerText;
                CatchTaken = node.Attributes["CatchTaken"]?.InnerText;
                Stumping = node.Attributes["Stumping"]?.InnerText;
                RunOutDirect = node.Attributes["RunOutDirect"]?.InnerText;
                RunOutThrower = node.Attributes["RunOutThrower"]?.InnerText;
                RunOutCatcher = node.Attributes["RunOutCatcher"]?.InnerText;
                Nitro = node.Attributes["Nitro"]?.InnerText;
                MoM = node.Attributes["MoM"]?.InnerText;

                param = new Dictionary<string, object>() {
                    { "@TournamentId", point.TournamentId },
                    {"@PointRulesId",PointRulesId },
                    {"@TournamentStage",TournamentStage },
                    {"@RunScored",RunScored },
                    {"@FourBonus",FourBonus },
                    {"@SixBonus",SixBonus },
                    {"@HalfCenturyBonus",HalfCenturyBonus },
                    {"@CenturyBonus",CenturyBonus },
                    {"@DismissalDuck",DismissalDuck },
                    {"@MinBall4SR",MinBall4SR },
                    {"@StrikeRateBelow50",StrikeRateBelow50 },
                    {"@StrikeRate50To60",StrikeRate50To60 },
                    {"@StrikeRate60To70",StrikeRate60To70 },
                    {"@StrikeRate110To150",StrikeRate110To150 },
                    {"@StrikeRateUp150",StrikeRateUp150 },
                    {"@WicketTaken",WicketTaken },
                    {"@Wicket3UpBonus",Wicket3UpBonus },
                    {"@Wicket5UpBonus",Wicket5UpBonus },
                    {"@MaidenOver",MaidenOver },
                    {"@Hattrick",Hattrick },
                    {"@MinOver4ER",MinOver4ER },
                    {"@EconomyBelow4",EconomyBelow4 },
                    {"@Economy4To5",Economy4To5 },
                    {"@Economy5To6",Economy5To6 },
                    {"@Economy9To11",Economy9To11 },
                    {"@EconomyUp11",EconomyUp11 },
                    {"@Captain",Captain },
                    {"@ViceCaptain",ViceCaptain },
                    {"@CatchTaken",CatchTaken },
                    {"@Stumping",Stumping },
                    {"@RunOutDirect",RunOutDirect },
                    {"@RunOutThrower",RunOutThrower },
                    {"@RunOutCatcher",RunOutCatcher },
                    {"@Nitro",Nitro },
                    {"@MoM",MoM }
                };

                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateTournamentPointRules", param);
            }
            param = new Dictionary<string, object>() {
               { "@TournamentId", point.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchTournamentPointRules", param);
        }



        internal static dynamic AdminFetchTournamentPointRules(AdminTournamentModel tournament)
        {
            var param = new Dictionary<string, object>() {
               { "@TournamentId", tournament.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchTournamentPointRules", param);
        }

        internal static dynamic AdminUploadParticipationTeam(AdminGenericModel team)
        {
            var param = new Dictionary<string, object>() {
                };
            
            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(team.xmlData);
            string ParticipationTeamName = "";
            string TeamShortName = "";
            string TeamDescription = "";
            string TeamImage = "";
            string TeamKey = "";
            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                ParticipationTeamName = node.Attributes["ParticipationTeamName"]?.InnerText;
                TeamShortName = node.Attributes["TeamShortName"]?.InnerText;
                TeamDescription = node.Attributes["TeamDescription"]?.InnerText;
                TeamImage = node.Attributes["TeamImage"]?.InnerText;
                TeamKey = TeamShortName.ToLower();
                if (TeamImage == null || TeamImage == "")
                    TeamImage = TeamShortName + ".png";
                param = new Dictionary<string, object>() {
                   { "@ParticipationTeamName", ParticipationTeamName },
                   { "@TeamShortName", TeamShortName },
                   { "@TeamDescription", TeamDescription },
                   { "@TournamentId", team.TournamentId },
                   { "@TeamImage",TeamImage },
                   { "@TeamKey",TeamKey }
                };
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateParticipationTeam", param);
            }


            return true;
        }

        internal static  void AdminUploadPlayerDetails(AdminGenericModel player)
        {
            var param = new Dictionary<string, object>()
            {
            };
            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(player.xmlData);
            string PlayerName = "";
            string playerfName = "";
            string playerlName = "";
            string PlayerShortName = "";
            string PlayerDesc = "";
            string PlayerType = "";
            string PlayerSpeciality = "";
            string PlayerValue = "";
            string ParticipationTeam = "";
            string playerImage = "";
            int APIPId = 0;
            bool PlayerStatus = true;
            string APIPlayerFinder = APIBaseUrl + ConfigurationManager.AppSettings["APIPlayerFinder"].ToString();


            string url = "";
            int counter = 0;

            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                PlayerName = node.Attributes["PlayerName"]?.InnerText;
                string[] tokens = PlayerName.Split(' ');
                playerfName = tokens[0];
                if(tokens.Length>1)
                    playerlName = tokens[1];
                PlayerShortName = node.Attributes["PlayerShortName"]?.InnerText;
                PlayerDesc = node.Attributes["PlayerDesc"]?.InnerText;
                PlayerType = node.Attributes["PlayerType"]?.InnerText;
                PlayerSpeciality = node.Attributes["PlayerSpeciality"]?.InnerText;
                PlayerValue = node.Attributes["PlayerValue"]?.InnerText;
                ParticipationTeam = node.Attributes["ParticipationTeam"]?.InnerText;
                url = APIPlayerFinder + "?name=" + PlayerName + "&apikey=" + apiKey;
                var client = new RestClient(url);
                var request = new RestRequest();
                request.AddHeader("Accept", "application/json");

                bool apiResponse = false;
                APIPlayerData pData = new APIPlayerData();
                try
                {
                    //HttpResponseMessage response =  await client.GetAsync(url);
                    var response = client.Execute(request);
                    apiResponse = response.IsSuccessful;
                    if (apiResponse)
                        pData = JsonConvert.DeserializeObject<APIPlayerData>(response.Content);
                    
                   
                }
                catch(Exception e)
                { }
                
                
                counter = 0;
                APIPId = 0;
                if (apiResponse)
                {
                    foreach (var playerData in pData.data)
                    {
                        if (playerData.name.Contains(playerfName) && (playerlName == "" || playerData.name.Contains(playerlName)))
                        {
                            APIPId = playerData.pid;
                            playerImage = APIPId.ToString() + ".jpg";
                            counter++;
                        }
                    }
                    if (counter > 1)//for same name player, we need to manually verify pid from cricapi.com, hence assiging 0 initially
                        APIPId = 0;
                }

                param = new Dictionary<string, object>() {
                    { "@TournamentId", player.TournamentId },
                    { "@APIPId", APIPId },
                    { "@playerImage", playerImage },
                    { "@PlayerName", PlayerName },
                    { "@PlayerShortName", PlayerShortName },
                    { "@PlayerDesc",PlayerDesc },
                    { "@PlayerType", PlayerType },
                    { "@PlayerSpeciality", PlayerSpeciality },
                    { "@PlayerStatus", PlayerStatus },
                    { "@PlayerValue", PlayerValue },
                    { "@ParticipationTeam", ParticipationTeam }
                };
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdatePlayerDetails", param);
            }
           
        }

        internal static dynamic AdminUpdatePlayerDetails(AdminPlayerModel player)
        {
            int APIPId = 0;
            string playerImage = "";
            string PlayerKey = "";

            if (!string.IsNullOrWhiteSpace(player.PlayerKey))
                PlayerKey = player.PlayerKey;

                if (player.APIPId > 0)
            {
                APIPId = player.APIPId;
                playerImage = APIPId.ToString() + ".jpg";
            }
            var param = new Dictionary<string, object>()
            {
                { "@PlayerId", player.PlayerId },
                { "@TournamentId", player.TournamentId },
                { "@APIPId", APIPId },
                { "@playerImage", playerImage },
                { "@PlayerName", player.PlayerName },
                { "@PlayerShortName", player.PlayerShortName },
                { "@PlayerDesc", player.PlayerDesc },
                { "@PlayerType", player.PlayerType },
                { "@PlayerSpeciality", player.PlayerSpeciality },
                { "@PlayerStatus", player.PlayerStatus },
                { "@PlayerValue", player.PlayerValue },
                { "@ParticipationTeam", player.ParticipationTeam },
                { "@PlayerKey", PlayerKey }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdatePlayerDetails", param);
        }

        internal static dynamic AdminDeletePlayerDetails(AdminPlayerModel player)
        {
            var param = new Dictionary<string, object>()
            {
                { "@PlayerId", player.PlayerId },
                { "@TournamentId", player.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeletePlayerDetails", param);
        }

        internal static dynamic AdminFetchPlayerDetails(AdminPlayerModel player)
        {
            int ParticipationTeamId = 0;
            if (player.ParticipationTeamId > 0)
                ParticipationTeamId = player.ParticipationTeamId;

            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", player.TournamentId },
                { "@ParticipationTeamId", ParticipationTeamId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchPlayerDetails", param);
        }

        internal static dynamic AdminUpdateParticipationTeam(AdminParticipationTeamModel team)
        {
            int ParticipationTeamId = 0;
            string TeamImage = "";
            if (team.TeamImage != null && team.TeamImage != "")
                TeamImage = team.TeamImage;
            else
                TeamImage = team.TeamShortName + ".png";
            if (team.ParticipationTeamId>0)
                ParticipationTeamId = (int)team.ParticipationTeamId;
            var param = new Dictionary<string, object>() {
               { "@ParticipationTeamId", ParticipationTeamId },
               { "@ParticipationTeamName", team.ParticipationTeamName },
               { "@TeamShortName", team.TeamShortName },
               { "@TeamDescription", team.TeamDescription },
               { "@TournamentId", team.TournamentId },
               { "@TeamImage", TeamImage },
               { "@TeamKey", team.TeamKey }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateParticipationTeam", param);
        }

        internal static dynamic AdminDeleteParticipationTeam(AdminParticipationTeamModel team)
        {

            var param = new Dictionary<string, object>() {
               { "@ParticipationTeamId", team.ParticipationTeamId },
               { "@TournamentId", team.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeleteParticipationTeam", param);
        }

        internal static dynamic AdminFetchParticipationTeam(AdminParticipationTeamModel team)
        {
            int ParticipationTeamId = 0;
            if (team.ParticipationTeamId > 0)
                ParticipationTeamId = (int)team.ParticipationTeamId;
            var param = new Dictionary<string, object>() {
               { "@ParticipationTeamId", ParticipationTeamId },
               { "@TournamentId", team.TournamentId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchParticipationTeam", param);
        }

        internal static void AdminInsertMatchDetails(AdminGenericModel match)
        {
            var param = new Dictionary<string, object>()
            {

            };
            int MatchId = 0;
            string APIName = "FantasySummary";
            string MatchNo = "";
            string MatchType = "";
            string Venue = "";
            string MatchStage = "";
            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string MatchDate = "";
            string MatchScheduledTime = "";
            string UniqueId = "";
            string MatchStatus = "UPCOMING";
            string APIMatchDetails = APIBaseUrl + ConfigurationManager.AppSettings["APIMatchDetails"].ToString();
            string apiMatchDate = "", MatchCity = "";
            

            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(match.xmlData);
            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                MatchDate = "";
                MatchCity = "";
                MatchNo = node.Attributes["MatchNo"]?.InnerText;
                MatchType = node.Attributes["MatchType"]?.InnerText;
                Venue = node.Attributes["Venue"]?.InnerText;
                MatchStage = node.Attributes["MatchStage"]?.InnerText;
                Team1 = node.Attributes["Team1"]?.InnerText;
                Team2 = node.Attributes["Team2"]?.InnerText;
                MatchScheduledDate = node.Attributes["MatchScheduledDate"]?.InnerText;
                MatchScheduledTime = node.Attributes["MatchScheduledTime"]?.InnerText;
                UniqueId = node.Attributes["UniqueId"]?.InnerText;

                if (Venue.Contains('|'))
                {
                    MatchCity = Venue.Split('|')[1];
                    Venue = Venue.Split('|')[0];
                }
                
                    MatchDate = MatchScheduledDate;
                

                param = new Dictionary<string, object>()
                {
                    { "@MatchId", MatchId },
                    { "@MatchNo", MatchNo },
                    { "@MatchType", MatchType },
                    { "@Venue", Venue },
                    { "@MatchStage", MatchStage },
                    { "@TournamentId", match.TournamentId },
                    { "@Team1", Team1 },
                    { "@Team2", Team2 },
                    { "@MatchStatus", MatchStatus },
                    { "@MatchScheduledDate", MatchScheduledDate },
                    { "@MatchDate", MatchDate },
                    { "@MatchScheduledTime", MatchScheduledTime },
                    { "@UniqueId", UniqueId },
                    { "@MatchCity", MatchCity }
                };
                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateMatchDetails", param);
            }
            // return true;
        }

        internal static  void UploadMatchDetails(object match)
        { 
            var param = new Dictionary<string, object>() {
               
                };
            int MatchId = 0;
            int TournamentId = 0;
            string MatchNo = "";
            string MatchType = "";
            string Venue = "";
            string MatchStage = "";
            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string MatchDate = "";
            string MatchScheduledTime = "";
            string UniqueId = "";
            string MatchStatus = "UPCOMING";
            string TournamentKey = "";
            string MatchTitle = "";
            double start_at = 0.0;
            string TournamentType = "T";

            string  MatchCity="";
            Dictionary<string, dynamic> matchDetails = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(match.ToString());

            foreach (var item in matchDetails["data"]["matches"])
            {
                string mStatus = item["status"].Value;
                TournamentKey = item["tournament"]["key"].Value;
                MatchTitle = item["sub_title"].Value;
                if (mStatus == "not_started")
                {
                    MatchCity = (item["venue"]["city"] != null) ? item["venue"]["city"].Value : "tbd";
                    UniqueId = (item["key"] != null) ? item["key"].Value : "";
                    MatchNo = "";
                    MatchNo = Regex.Match(MatchTitle, @"\d+").Value;
                    start_at = item["start_at"].Value;
                    
                    DateTime dt = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(start_at);
                    MatchScheduledDate = dt.ToString("yyyy-MM-dd");
                    MatchScheduledTime = dt.ToString("hh:mm:ss.sss");
                    MatchDate = dt.ToString("yyyy-MM-dd hh:mm:ss.sss");

                    if (MatchNo == "")
                        MatchNo = "0";

                    MatchType = item["format"].Value.ToUpper();
                    Venue = MatchCity;
                    MatchStage = "League";
                    if (MatchTitle.Contains("Qualifier"))
                    {
                        MatchStage = "Qualifier";
                        MatchNo = "0";
                    }
                    else if (MatchTitle.Contains("Eliminator"))
                    {
                        MatchStage = "Eliminator";
                        MatchNo = "0";
                    }
                    else if (MatchTitle.Contains("Final"))
                    {
                        MatchStage = "Final";
                        MatchNo = "0";
                    }

                    Team1 = item["teams"]["a"]["name"].Value;
                    Team2 = item["teams"]["b"]["name"].Value;
                    if (Team1 == "TBC")
                        Team1 = "TBD";
                    if (Team2 == "TBC")
                        Team2 = "TBD";


                    param = new Dictionary<string, object>()
                    {
                        { "@MatchId", MatchId },
                        { "@MatchNo", Int32.Parse(MatchNo) },
                        { "@MatchType", MatchType },
                        { "@Venue", Venue },
                        { "@MatchCity", MatchCity },
                        { "@MatchStage", MatchStage },
                        { "@TournamentId", TournamentId },
                        { "@TournamentType", TournamentType },
                        { "@TournamentKey", TournamentKey },
                        { "@Team1", Team1 },
                        { "@Team2", Team2 },
                        { "@MatchStatus", MatchStatus },
                        { "@MatchScheduledDate", MatchScheduledDate },
                        { "@MatchDate", MatchDate },
                        { "@MatchScheduledTime", MatchScheduledTime },
                        { "@UniqueId", UniqueId }    
                    };
                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadMatchDetails", param);
                }
            }

        }

        internal static void UploadDailyMatchDetails(object match)
        {
            var param = new Dictionary<string, object>()
            {

            };
            int MatchId = 0;
            int TournamentId = 0;
            int MatchNo = 1;
            string MatchType = "";
            string Venue = "";
            string MatchStage = "D";
            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string MatchDate = "";
            string MatchScheduledTime = "";
            string UniqueId = "";
            string MatchStatus = "UPCOMING";
            string TournamentKey = "";
            string MatchTitle = "";
            double start_at = 0.0;
            string TournamentType = "D";
            string TournamentName = "";

            string MatchCity = "";
            Dictionary<string, dynamic> matchDetails = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(match.ToString());

            foreach (var item in matchDetails["data"]["matches"])
            {
                string mStatus = item["status"].Value;
                TournamentKey = item["tournament"]["key"].Value;
                MatchTitle = item["sub_title"].Value;
                if (mStatus == "not_started")
                {
                    Team1 = item["teams"]["a"]["name"].Value;
                    Team2 = item["teams"]["b"]["name"].Value;
                    if (Team1 == "TBC" || Team2 == "TBC")
                        continue;

                    MatchCity = (item["venue"]["city"] != null) ? item["venue"]["city"].Value : "tbd";
                    UniqueId = (item["key"] != null) ? item["key"].Value : "";
                    TournamentName = item["tournament"]["short_name"].Value.Split(' ')[0];

                    start_at = item["start_at"].Value;

                    DateTime dt = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(start_at);
                    MatchScheduledDate = dt.ToString("yyyy-MM-dd");
                    MatchScheduledTime = dt.ToString("hh:mm:ss.sss");
                    MatchDate = dt.ToString("yyyy-MM-dd hh:mm:ss.sss");

                    MatchType = item["format"].Value.ToUpper();
                    Venue = MatchCity;
                    
                    param = new Dictionary<string, object>()
                    {
                        { "@MatchId", MatchId },
                        { "@MatchNo", MatchNo },
                        { "@MatchType", MatchType },
                        { "@Venue", Venue },
                        { "@MatchCity", MatchCity },
                        { "@MatchStage", MatchStage },
                        { "@TournamentId", TournamentId },
                        { "@TournamentName", TournamentName },
                        { "@TournamentType", TournamentType },
                        { "@TournamentKey", TournamentKey },
                        { "@Team1", Team1 },
                        { "@Team2", Team2 },
                        { "@MatchStatus", MatchStatus },
                        { "@MatchScheduledDate", MatchScheduledDate },
                        { "@MatchDate", MatchDate },
                        { "@MatchScheduledTime", MatchScheduledTime },
                        { "@UniqueId", UniqueId }
                    };
                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadDailyMatchDetails", param);
                }
            }

        }

        internal static dynamic AdminUpdateMatchDetails(AdminMatchDetailsModel match)
        {
            int MatchId = 0;
            string MatchCity = match.MatchCity;
            string MatchDate = match.MatchDate;
            if (match.MatchId > 0)
                MatchId = match.MatchId;
            if (MatchDate == "" || MatchDate == null)
                MatchDate = match.MatchScheduledDate;

            if (MatchCity == "" || MatchCity == null)
                MatchCity = match.Venue;

            var param = new Dictionary<string, object>()
            {
                { "@MatchId", MatchId },
                { "@MatchNo", match.MatchNo },
                { "@MatchType", match.MatchType },
                { "@Venue", match.Venue },
                { "@MatchStage", match.MatchStage },
                { "@TournamentId", match.TournamentId },
                { "@Team1", match.Team1 },
                { "@Team2", match.Team2 },
                { "@MatchStatus", match.MatchStatus },
                { "@MatchScheduledDate", match.MatchScheduledDate },
                { "@MatchDate", MatchDate },
                { "@MatchScheduledTime", match.MatchScheduledTime },
                { "@UniqueId", match.UniqueId },
                { "@MatchCity", MatchCity }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateMatchDetails", param);
        }

        internal static dynamic AdminDeleteMatchDetails(AdminMatchDetailsModel match)
        {
            var param = new Dictionary<string, object>()
            {
                { "@MatchId", match.MatchId },
                { "@TournamentId", match.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeleteMatchDetails", param);
        }

        internal static dynamic AdminFetchMatchDetails(AdminMatchDetailsModel match)
        {
            int MatchId = 0;
            if (match.MatchId > 0)
                MatchId = (int)match.MatchId;
            var param = new Dictionary<string, object>()
            {
                { "@MatchId", MatchId },
                { "@TournamentId", match.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchMatchDetails", param);
        }

        internal static dynamic AdminFetchMatchOnStart(AdminMatchDetailsModel match)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", 0 },
                { "@MatchStatus", match.MatchStatus }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchMatchOnStart", param);
        }

        internal static dynamic AdminSwitchBattingTeam(AdminMatchDetailsModel match)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UniqueId", match.UniqueId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminSwitchBattingTeam", param);
        }

        internal static dynamic AdminFetchAutoTeam(AdminAutoTeamModel team)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", team.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchAutoTeam", param);
        }

        internal static dynamic AdminDeleteAutoTeam(AdminAutoTeamModel team)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", team.TournamentId },
                { "@AutoSelectionTeamId", team.AutoSelectionTeamId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeleteAutoTeam", param);
        }

        internal static dynamic AdminUpdateAutoTeam(AdminAutoTeamModel team)
        {
            int AutoSelectionTeamId = 0;
            if (team.AutoSelectionTeamId > 0)
                AutoSelectionTeamId = (int)team.AutoSelectionTeamId;
            var param = new Dictionary<string, object>()
            {
                { "@AutoSelectionTeamId", AutoSelectionTeamId },
                { "@TournamentId", team.TournamentId },
                { "@Player1", team.Player1 },
                { "@Player2", team.Player2 },
                { "@Player3", team.Player3 },
                { "@Player4", team.Player4 },
                { "@Player5", team.Player5 },
                { "@Player6", team.Player6 },
                { "@Player7", team.Player7 },
                { "@Player8", team.Player8 },
                { "@Player9", team.Player9 },
                { "@Player10", team.Player10 },
                { "@Player11", team.Player11 },
                { "@TeamCapt", team.TeamCapt },
                { "@TeamVCapt", team.TeamVCapt }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateAutoTeam", param);
        }

        internal static bool InsertNotifications(int UserId, string Message,int MessageType)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", UserId },
                { "@Message", Message },
                { "@MessageType", MessageType }
            };
             DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..InsertNotifications", param);

            return true;
        }

        internal static bool SendNotificationMessage(NotificationModel Notification)
        {
            var param = new Dictionary<string, object>()
            {
                { "@Title", Notification.Title },
                { "@Message", Notification.Message }
            };
            DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SendNotificationMessage", param);

            return true;
        }

        internal static dynamic FetchNotificationMessage(int UserId)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", UserId }
            };
           return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchNotificationMessage", param);  
        }

        internal static dynamic AcknowledgeNotificationMessage(NotificationModel Notification)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", Notification.UserId },
                { "@NotificationId", Notification.NotificationId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AcknowledgeNotificationMessage", param);
        }

        internal static dynamic FetchNotificationCount(int UserId)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchNotificationCount", param);
        }

        internal static dynamic ResetSubscription()
        {
            var param = new Dictionary<string, object>()
            {
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ResetSubscription", param);
        }

        internal static int GetNotificationCount(string Email = null, int UserId = 0)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", UserId },
                { "@Email", Email }
            };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchNotificationMessage", param);
            return data.Count;
        }

        internal static dynamic AdminResetTransfer(ResetTransferModel value)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", value.TournamentId },
                { "@TransferCount", value.TransferCount }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminResetTransfer", param);
        }

        internal static dynamic LastMatchTopPerformer(UserTeamModel users)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", users.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..LastMatchTopPerformer", param);
        }

        internal static dynamic UserCount()
        {
            var param = new Dictionary<string, object>()
            {
                { "@ReportType", "UserCount" }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserCount", param);
        }

        internal static dynamic MatchRewards()
        {
            var param = new Dictionary<string, object>()
            {
                { "@ReportType", "MatchRewards" }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..MatchRewards", param);
        }

        internal static dynamic TestCall(object pl)
        {
            
            Dictionary<string, dynamic> values = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(pl.ToString());

            int APIPId = 0;
            string KName = "";
            string FName = "";
            string LName = "";
            double start_at = 1633615200.0;
            DateTime dt = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(start_at);
            string dateFormated = dt.ToString("yyyy-MM-dd");
            string timeFormated = dt.ToString("hh:mm:ss.sssZ");
            string acFormated = dt.ToString("yyyy-MM-dd hh:mm:ss.sss");
            string MatchTitle = "01Final  67Match";
            string MatchNo = Regex.Match(MatchTitle, @"\d+").Value;
            /*
            foreach (var item in values["data"]["tournament_team"]["player_keys"])
            {
                string p_key = item.Value;
                KName = p_key.Split('_')[1];
                if (KName == "")
                    KName = "NONAME";

                var p_name = values["data"]["tournament_team"]["players"][p_key]["name"].Value;
                FName = p_name.Split(' ')[0];
                LName = p_name.Split(' ')[1];

                if (FName == "")
                    FName = "NONAME";

                if (LName == "")
                    LName = "NONAME";

                var param = new Dictionary<string, object>()
                {
                    { "@APIPId", APIPId },
                    { "@PlayerKey", p_key },
                    { "@KName", KName },
                    { "@FName", FName },
                    { "@LName", LName }
                };
                var data =  DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdatePlayerKey", param);
            }*/
            return 1;
        }

        internal static bool ManualScoreUpdate(AdminGenericModel mData)
        {
            var param = new Dictionary<string, object>()
            {
            };
            string UniqueId = mData.UniqueId;
            int BattingPoints = 0;
            int BowlingPoints = 0;
            int FieldingPoints = 0;
            int TotalPoints = 0;
            int APIPId = 0;

            try
            {
                XmlDocument xDoc = new XmlDocument();
                xDoc.LoadXml(mData.xmlData);
                foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
                {
                    APIPId = 0;
                    BattingPoints = 0;
                    BowlingPoints = 0;
                    FieldingPoints = 0;

                    APIPId = Int32.Parse(node.Attributes["APIPId"]?.InnerText);
                    BattingPoints = Int32.Parse(node.Attributes["BattingPoints"]?.InnerText);
                    BowlingPoints = Int32.Parse(node.Attributes["BowlingPoints"]?.InnerText);
                    FieldingPoints = Int32.Parse(node.Attributes["FieldingPoints"]?.InnerText);
                    TotalPoints = BattingPoints + BowlingPoints + FieldingPoints;

                    param = new Dictionary<string, object>()
                    {
                        { "@UniqueId", UniqueId },
                        { "@APIPId", APIPId },
                        { "@BattingPoints", BattingPoints },
                        { "@BowlingPoints", BowlingPoints },
                        { "@FieldingPoints", FieldingPoints },
                        { "@TotalPoints", TotalPoints }
                    };

                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ManualScoreUpdate", param);
                }
            }
            catch (Exception Excp)
            {
                //need to handle
            }
            return true;
        }

        internal static bool ManualScoreUpdateEachPlayer(ManualScoreUpdate mData)
        {
            int BattingPoints = mData.BattingPoints ?? 0;
            int BowlingPoints = mData.BowlingPoints ?? 0;
            int FieldingPoints = mData.FieldingPoints ?? 0;
            int TotalPoints = BattingPoints + BowlingPoints + FieldingPoints;

            var param = new Dictionary<string, object>()
                    {
                        { "@UniqueId", mData.UniqueId },
                        { "@APIPId", mData.APIPId },
                        { "@BattingPoints", BattingPoints },
                        { "@BowlingPoints", BowlingPoints },
                        { "@FieldingPoints", FieldingPoints },
                        { "@TotalPoints", TotalPoints }
                    };

            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ManualScoreUpdate", param);

            return true;
        }

        internal static bool SetManualMoM(ManualScoreUpdate mData)
        {

            var param = new Dictionary<string, object>()
                    {
                        { "@UniqueId", mData.UniqueId },
                        { "@APIPId", mData.APIPId }
                    };

            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetManualMoM", param);

            return true;
        }

        internal static dynamic FetchManualScore(ManualScoreUpdate mData)
        {

            var param = new Dictionary<string, object>()
                    {
                        { "@UniqueId", mData.UniqueId }
                    };

            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchManualScore", param);

            return data;
        }

        internal static dynamic FetchPaymentGatewayDetails()
        {

            var param = new Dictionary<string, object>()
                    {
                        { "@Environment", _EnvCode }
                    };

            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchPaymentGatewayDetails", param);

            return data;
        }

        internal static dynamic AddMoney2Wallet(PaymentCF paymentDetails)
        {

            var param = new Dictionary<string, object>()
                    {
                        { "@UserId", paymentDetails.UserId },
                        { "@WalletAmount", paymentDetails.order_amount },
                        { "@Comments", paymentDetails.order_note },
                        { "@CFOrderId", paymentDetails.cf_order_id },
                        { "@Currency", paymentDetails.order_currency },
                        { "@PaymentMethod", paymentDetails.payment_methods },
                        { "@PaymentStatus", paymentDetails.order_status },
                        { "@PaymentSessionId", paymentDetails.payment_session_id }
                    };

            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AddMoney2Wallet", param);

            return data;
        }

        #endregion


        #region Daily Matches

        internal static dynamic UserUpcomingDailyMatches(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserUpcomingDailyMatches", param);
        }

        internal static dynamic UserDailyMatches(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", User.UserId },
                { "@Bronze", _Bronze },
                { "@Silver", _Silver },
                { "@Gold", _Gold },
                { "@Platinum", _Platinum }
            };
            var data =  DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateWalletInfo", param);

            param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId },
                 { "@MatchStatus", User.MatchStatus },
                 { "@FetchAll", User.FetchAll }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserDailyMatches", param);
        }

        internal static dynamic DailyLeagueTeams(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@UserId", User.UserId },
                 { "@TournamentId", User.TournamentId },
                 { "@MatchId", User.MatchId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyLeagueTeams", param);
        }

        internal static dynamic DailyTeamSelectionRules(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@MatchId", User.MatchId },
                 { "@MatchType", User.MatchType }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyTeamSelectionRules", param);
        }

        internal static dynamic DailyMatchesPlayerList(GenericModel User)
        {
            int UserId = 0;
            if (User.UserId > 0)
                UserId = User.UserId;
            var param = new Dictionary<string, object>()
            {
                 { "@MatchId", User.MatchId },
                 { "@MatchType", User.MatchType },
                 { "@UserId", UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyMatchesPlayerList", param);
        }

        internal static dynamic UserDailyTeamPlayers(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@TournamentId", User.TournamentId },
                 { "@UserTeamId", User.UserTeamId },
                 { "@MatchType", User.MatchType }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserDailyTeamPlayers", param);
        }

        internal static dynamic UserDailyTeamPlayersWithPoints(GenericModel User)
        {
            var param = new Dictionary<string, object>()
            {
                 { "@TournamentId", User.TournamentId },
                 { "@UserTeamId", User.UserTeamId },
                 { "@MatchId", User.MatchId },
                 { "@MatchType", User.MatchType }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UserDailyTeamPlayersWithPoints", param);
        }


        internal static dynamic SaveDailyTeamSelection(TeamCompositionModel TeamSelection)
        {
            string WinnerPrediction = "";
            if (!string.IsNullOrWhiteSpace(TeamSelection.WinnerPrediction))
                WinnerPrediction = TeamSelection.WinnerPrediction;

            var param = new Dictionary<string, object>() {
                { "@UserId", TeamSelection.UserId },
                { "@TournamentId", TeamSelection.TournamentId },
                { "@MatchId", TeamSelection.MatchId },
                { "@Player1", TeamSelection.Player1 },
                { "@Player2", TeamSelection.Player2 },
                { "@Player3", TeamSelection.Player3 },
                { "@Player4", TeamSelection.Player4 },
                { "@Player5", TeamSelection.Player5 },
                { "@Player6", TeamSelection.Player6 },
                { "@Player7", TeamSelection.Player7 },
                { "@Player8", TeamSelection.Player8 },
                { "@Player9", TeamSelection.Player9 },
                { "@Player10", TeamSelection.Player10 },
                { "@Player11", TeamSelection.Player11 },
                { "@TeamCapt", TeamSelection.TeamCapt },
                { "@TeamVCapt", TeamSelection.TeamVCapt },
                { "@WinnerPrediction", WinnerPrediction }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveDailyTeamSelection", param);
        }

        internal static void AdminInsertDailyMatchDetails(AdminGenericModel match)
        {
            var param = new Dictionary<string, object>()
            {

            };
            string TournamentName = "";
            int MatchId = 0;
            string MatchType = "";
            string Venue = "";
            string MatchStage = "";
            string Team1 = "";
            string Team2 = "";
            string MatchScheduledDate = "";
            string MatchScheduledTime = "";
            string UniqueId = "";
            string MatchStatus = "UPCOMING";
            
            string MatchDate = "", MatchCity="";

            
            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(match.xmlData);
            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                MatchDate = "";
                MatchCity = "";
                TournamentName = node.Attributes["TournamentName"]?.InnerText;
                MatchType = node.Attributes["MatchType"]?.InnerText;
                Venue = node.Attributes["Venue"]?.InnerText;
                MatchStage = node.Attributes["MatchStage"]?.InnerText;
                Team1 = node.Attributes["Team1"]?.InnerText;
                Team2 = node.Attributes["Team2"]?.InnerText;
                MatchScheduledDate = node.Attributes["MatchScheduledDate"]?.InnerText;
                MatchScheduledTime = node.Attributes["MatchScheduledTime"]?.InnerText;
                UniqueId = node.Attributes["UniqueId"]?.InnerText;
               
               if(Venue.Contains('|'))
                {
                    MatchCity = Venue.Split('|')[1];
                    Venue = Venue.Split('|')[0];
                }

               
                if (MatchDate == "" || MatchDate == null)
                {
                    param = new Dictionary<string, object>()
                    {
                    { "@MatchId", MatchId },
                    { "@MatchNo", 1 },
                    { "@TournamentName", TournamentName },
                    { "@MatchType", MatchType },
                    { "@Venue", Venue },
                    { "@MatchStage", MatchStage },
                    { "@TournamentId",0 },
                    { "@Team1", Team1 },
                    { "@Team2", Team2 },
                    { "@MatchStatus", MatchStatus },
                    { "@MatchScheduledDate", MatchScheduledDate },
                    { "@MatchScheduledTime", MatchScheduledTime },
                    { "@UniqueId", UniqueId },
                    { "@MatchCity", MatchCity }
                    };
                }
                else
                {
                    param = new Dictionary<string, object>()
                    {
                    { "@MatchId", MatchId },
                    { "@MatchNo", 1 },
                    { "@TournamentName", TournamentName },
                    { "@MatchType", MatchType },
                    { "@Venue", Venue },
                    { "@MatchStage", MatchStage },
                    { "@TournamentId",0 },
                    { "@Team1", Team1 },
                    { "@Team2", Team2 },
                    { "@MatchStatus", MatchStatus },
                    { "@MatchScheduledDate", MatchScheduledDate },
                    { "@MatchDate", MatchDate },
                    { "@MatchScheduledTime", MatchScheduledTime },
                    { "@UniqueId", UniqueId },
                    { "@MatchCity", MatchCity }
                    };
                }
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateDailyMatchDetails", param);
            }

        }

        internal static dynamic AdminUpdateDailyMatchDetails(AdminMatchDetailsModel match)
        {
            int MatchId = 0;
            
            string MatchDate = match.MatchDate;
            string MatchCity = match.MatchCity;

            if (MatchCity == "" || MatchCity == null)
                MatchCity = match.Venue;
            if (match.MatchId > 0)
                MatchId = match.MatchId;

            if (string.IsNullOrWhiteSpace(match.MatchDate))
                MatchDate = match.MatchScheduledDate;

            var param = new Dictionary<string, object>()
            {
                { "@MatchId", MatchId },
                { "@MatchNo", match.MatchNo },
                { "@TournamentName", match.TournamentName },
                { "@MatchType", match.MatchType },
                { "@Venue", match.Venue },
                { "@MatchStage", match.MatchStage },
                { "@TournamentId", match.TournamentId },
                { "@Team1", match.Team1 },
                { "@Team2", match.Team2 },
                { "@MatchStatus", match.MatchStatus },
                { "@MatchScheduledDate", match.MatchScheduledDate },
                { "@MatchDate", MatchDate },
                { "@MatchScheduledTime", match.MatchScheduledTime },
                { "@UniqueId", match.UniqueId },
                { "@MatchCity", MatchCity }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminUpdateDailyMatchDetails", param);
        }

        internal static dynamic AdminDeleteDailyMatchDetails(AdminMatchDetailsModel match)
        {
            var param = new Dictionary<string, object>()
            {
                { "@MatchId", match.MatchId },
                { "@TournamentId", match.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminDeleteDailyMatchDetails", param);
        }

        internal static dynamic AdminFetchDailyMatchDetails(AdminMatchDetailsModel match)
        {
            int MatchId = 0;
            if (match.MatchId > 0)
                MatchId = (int)match.MatchId;
            var param = new Dictionary<string, object>()
            {
                { "@MatchId", MatchId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchDailyMatchDetails", param);
        }


        internal static dynamic AdminFetchDailyPlayers(AdminPlayerModel players)
        {

            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", players.TournamentId },
                { "@ParticipationTeamId", players.ParticipationTeamId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminFetchDailyPlayers", param);
        }

        internal static dynamic SetPlayersStatus(AdminGenericModel players)
        {

            var param = new Dictionary<string, object>()
            {
                { "@PlayerIds", players.PlayerIds },
                { "@Status", players.Status }
            };
             DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetPlayersStatus", param);
            return true;
        }

        internal static dynamic SetMacthesWeeklyStatus(AdminGenericModel players)
        {

            var param = new Dictionary<string, object>()
            {
                { "@MatchIds", players.MatchIds },
                { "@Status", players.Status }
            };
            DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetMacthesWeeklyStatus", param);

            return true;
        }

        internal static dynamic ResetDailyTeamPlayerPoints(AdminPlayerModel players)
        {

            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", players.TournamentId },
                { "@ParticipationTeamId", players.ParticipationTeamId }
            };
            DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..ResetDailyTeamPlayerPoints", param);

            return true;
        }

        internal static dynamic DailyTournamentList(GenericModel User)
        {

            var param = new Dictionary<string, object>()
            {
                { "@UserId", User.UserId },
                { "@MatchStatus", User.MatchStatus }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyTournamentList", param);

        }

        internal static dynamic FetchMatchScore(MatchModel match)
        {

            var param = new Dictionary<string, object>()
            {
                { "@MatchId", match.MatchId}
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchMatchScore", param);

        }

        internal static dynamic DailyUniqueTeamList(AdminGenericModel team)
        {

            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", team.TournamentId}
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyUniqueTeamList", param);

        }

        internal static dynamic DailyLiveLeagueUsers(GenericModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@TournamentId", details.TournamentId },
                { "@MatchId", details.MatchId },
                { "@UserId", details.UserId }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DailyLiveLeagueUsers", param);
        }

        internal static dynamic CalcWeeklyReward(Reward details)
        {
            var param = new Dictionary<string, object>() {
                { "@RewardType", "weekly" },
                { "@RewardWeek", details.RewardWeek }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalcWeeklyReward", param);
        }

        internal static dynamic FetchRewardWeek(Reward details)
        {
            var param = new Dictionary<string, object>() {
                { "@RewardType", "weekly" }
                };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchRewardWeek", param);
        }

        internal static dynamic DeleteRewardWeek(Reward details)
        {
            var param = new Dictionary<string, object>()
            {
                { "@RewardWeek", details.RewardWeek }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DeleteRewardWeek", param);
        }

        internal static dynamic FetchRewardWeekDetails(Reward details)
        {
            var param = new Dictionary<string, object>()
            {
                { "@RewardWeek", details.RewardWeek }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchRewardWeekDetails", param);
        }

        internal static dynamic WeeklyRewardProcessed(Reward details)
        {
            var param = new Dictionary<string, object>()
            {
                { "@RewardIds", details.RewardId },
                { "@Comments", details.Comments }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..WeeklyRewardProcessed", param);
        }

        internal static bool UpdateMatchPlayerIds(MatchModel details)
        {
            var param = new Dictionary<string, object>() {
                { "@UniqueId", details.UniqueId }
                };
            List<FetchMatchModel> matchData = DatabaseHelper.FetchMatchDetails("FANTASYCRICKET..FetchMatchDetails", param);
            int Tournament = 0;
            foreach (var lMatch in matchData)
            {
                Tournament = lMatch.TournamentId;
                if(lMatch.TournamentType == "D")
                {
                    if (lMatch.MatchType == "T20")
                        Tournament = -99;
                    else if (lMatch.MatchType == "ODI")
                        Tournament = -100;
                }

                param = new Dictionary<string, object>() {
                { "@MatchId", lMatch.MatchId },
                { "@TournamentId", Tournament }
                };

                var data1 = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateMatchPlayerIds", param);
            }
            return true;
        }

        internal static dynamic FetchWalletInfo(UserModel user)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", user.UserId },
                { "@Bronze", _Bronze },
                { "@Silver", _Silver },
                { "@Gold", _Gold },
                { "@Platinum", _Platinum }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchWalletInfo", param);
        }

        internal static dynamic SyncReferralCode(UserModel user)
        {
            var param = new Dictionary<string, object>()
            {
               { "@Status", "all" }
            };
            List<FetchEmptyRefferalCodeUsers> userData = DatabaseHelper.FetchEmptyRefferalCodeUsers("FANTASYCRICKET..FetchEmptyRefferalCodeUsers", param);
            foreach (var luser in userData)
            {
                param = new Dictionary<string, object>()
                 {
                    { "@UserId", luser.UserId}
                };
                
                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SyncReferralCode", param);
            }
            return true;
        }

        internal static List<UserCommunicationModel> UserCommunication(UserCommModel comm)
        {
            var param = new Dictionary<string, object>() { };
            if (comm.StartUserId > 0)
            {
                param = new Dictionary<string, object>()
                {
                   { "@userIds", "" },
                   { "@StartUserId",comm.StartUserId },
                   { "@EndUserId",comm.EndUserId }
                };
            }
            else
            {
                param = new Dictionary<string, object>()
                {
                   { "@userIds", comm.userIds }
                };
            }
            List<UserCommunicationModel> userData = DatabaseHelper.UserCommunication("FANTASYCRICKET..UserCommunication", param);
            
            return userData;
        }

        internal static dynamic GetFunFact(FunFactModel fun)
        {
            var param = new Dictionary<string, object>()
            {
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..GetFunFact", param);
        }

        internal static dynamic AddFunFact(FunFactModel fun)
        {
            var param = new Dictionary<string, object>()
            {
                { "@FunMessage", fun.FunMessage }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AddFunFact", param);
        }

        internal static dynamic FetchTotalRewards(UserModel user)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", user.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchTotalRewards", param);
        }

        internal static dynamic FetchTotalClaims(UserModel user)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", user.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchTotalClaims", param);
        }

        internal static dynamic CalculateReferralReward()
        {
            var param = new Dictionary<string, object>()
            {
                { "@Status", "all" }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateReferralReward", param);
        }

        internal static dynamic CalculateTournamentReward(UserTeamModel user)
        {
            var param = new Dictionary<string, object>()
            {
                { "@TournamentId", user.TournamentId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateTournamentReward", param);
        }

        internal static dynamic DownloadRewardDetails()
        {
            var param = new Dictionary<string, object>()
            {
                { "@Status", "download" }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DownloadRewardDetails", param);
        }

        internal static dynamic ViewRewardDetails()
        {
            var param = new Dictionary<string, object>()
            {
                { "@Status", "view" }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..DownloadRewardDetails", param);
        }

        internal static bool UploadClaimDetails(AdminGenericModel claims)
        {
            var param = new Dictionary<string, object>()
            {
            };

            XmlDocument xDoc = new XmlDocument();
            xDoc.LoadXml(claims.xmlData);
            int UserId = 0;
            int ClaimAmount = 0;
            string Bundle = "";
            string Vouchar = "";
            string Comments = "";
            foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
            {
                UserId = Int32.Parse(node.Attributes["UserId"]?.InnerText);
                ClaimAmount = Int32.Parse(node.Attributes["ClaimAmount"]?.InnerText);
                Bundle = node.Attributes["Bundle"]?.InnerText;
                Vouchar = node.Attributes["Vouchar"]?.InnerText;
                Comments = node.Attributes["Comments"]?.InnerText;

                param = new Dictionary<string, object>()
                {
                    { "@UserId", UserId },
                    { "@ClaimAmount", ClaimAmount },
                    { "@Bundle", Bundle },
                    { "@Vouchar", Vouchar },
                    { "@Comments", Comments }
                };

                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UploadClaimDetails", param);
            }
            return true;
        }

        internal static dynamic FetchReferralCode(UserTeamModel user)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UserId", user.UserId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FetchReferralCode", param);
        }

        internal static dynamic AdminHideScoreCard(MatchModel match)
        {
            var param = new Dictionary<string, object>()
            {
                { "@UniqueId", match.UniqueId }
            };
            return DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..AdminHideScoreCard", param);
        }

        #endregion
    }
    internal static class RapidAPIRepository
    {
        #region > Private Local Variables <
        //private static string _logServiceURI = null;
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "FantasyCricketRepository.cs";
        private static bool _IsTestCase;
        private static string RapidBaseUrl = ConfigurationManager.AppSettings["RapidBaseUrl"].ToString();
        private static string RapidapiKey = ConfigurationManager.AppSettings["RapidapiKey"].ToString();
        private static string RapidapiHost = ConfigurationManager.AppSettings["RapidapiHost"].ToString();
        //private static Task task;
        #endregion

        #region > Properties <
        public static bool IsTestCase
        {
            get { return _IsTestCase; }
            set { _IsTestCase = value; }
        }
        #endregion

        
    
        
        internal static bool SyncRapidMatchPlayers(RapidMatchPlayers match)
        {
            int RapidPlayerId = 0;
            string fullName = "";
            var param = new Dictionary<string, object>() { };

            foreach (var player in match.playersInMatch.homeTeam.players)
            {
                RapidPlayerId = player.playerId;
                fullName = player.fullName.Replace("(c)", "");
                fullName = fullName.Replace("(wk)", "");
                fullName = fullName.Trim();

                 param = new Dictionary<string, object>() {
                        { "@PlayerName", fullName },
                        { "@RapidPlayerId", RapidPlayerId }
                         };
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateRapidPlayerId", param);

            }

            foreach (var player in match.playersInMatch.awayTeam.players)
            {
                RapidPlayerId = player.playerId;
                fullName = player.fullName;

                param = new Dictionary<string, object>() {
                        { "@PlayerName", fullName },
                        { "@RapidPlayerId", RapidPlayerId }
                         };
                DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateRapidPlayerId", param);

            }

            return true;
        }
    }


}