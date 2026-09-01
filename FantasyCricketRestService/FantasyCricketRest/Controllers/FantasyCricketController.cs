using System;
using System.Web;
using System.Collections.Generic;
using System.Web.Http;
using System.Net;
using System.Globalization;
using FantasyCricketAppRest.Models;
using FantasyCricketAppRest.Providers;
using LPL.UI.REST.Core.Models;
using LPL.UI.REST.Core.Entities;
using System.Configuration;
using System.IO;
using Common.Logging.Entities;
using FantasyCricketAppRest.CommonUtilities;
using Common.ECDC;
using FantasyCricketScoreIntegration.RapidMatchPlayers.Models;


namespace FantasyCricketAppRest.Controllers
{
     public class FantasyCricketController : FantasyCricketAppRestBaseController
     {
        private string _AppName = null;
        private string _Filename = null;
        private string TokenValidation = ConfigurationManager.AppSettings["TokenValidation"];
        private string _ProfileImageFilePath = ConfigurationManager.AppSettings["ProfileImageFilePath"];
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        private static string _passPhraseReg = ConfigurationManager.AppSettings["RegEncryptionKey"];

        private bool _IsTestCase;
        

        public FantasyCricketController()
        {
           
            _Filename = "FantasyCricketController.cs";
            _AppName = "FantasyCricketAppRestService";
        }

        public bool IsTestCase
        {
            get { return _IsTestCase; }
            set { _IsTestCase = value; }
        }

        
        [Route("api/fantasycricket/health-check"), HttpGet()]
        public JsonResponse HealthCheck()
        {

           return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "APIs are up and running");

        }

        [Route("api/fantasycricket/match-rewards"), HttpGet()]
        public JsonResponse MatchRewards()
        {
            var data = new FantasyCricketProvider().MatchRewards();
            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "successful");

        }

        [Route("api/fantasycricket/signup"), HttpPost()]
        public JsonResponse CreateUser(UserModel user)
        {
            JsonResponse ReturnValue = null;

            if (user != null)
            {

                    // Validate Minimum Fields provided...
                    if (!string.IsNullOrWhiteSpace(user.UserName) && !string.IsNullOrWhiteSpace(user.Password) && !string.IsNullOrWhiteSpace(user.FirstName) &&
                         !string.IsNullOrWhiteSpace(user.LastName))
                    {
                        var verifyUser = new FantasyCricketProvider().VerifyUser(user.UserName);
                        if (verifyUser.Count == 1)
                        {
                            return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail,"UserName already exists.");
                        }
                        // Create User
                        var data = new FantasyCricketProvider().CreateUser(user);
                        return JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Success);
                    }
                    else // Minimum Fields not provided, reject it...
                    {
                        string Description = string.Empty;

                        if (string.IsNullOrWhiteSpace(user.UserName))
                        {
                            Description = "UserName; ";
                        }
                        if (string.IsNullOrWhiteSpace(user.Password))
                        {
                            Description += "Password; ";
                        }
                        if (string.IsNullOrWhiteSpace(user.FirstName))
                        {
                            Description += "FirstName; ";
                        }
                        if (string.IsNullOrWhiteSpace(user.LastName))
                        {
                            Description += "LastName; ";
                        }


                        Description = string.Concat(CultureInfo.InvariantCulture, "Minimum Required Fields were not provided, Request Rejected: ", Description);

                        
                        ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                    }
               
            }
            else
            {
                
                ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "Failed: supplied User data could not be de-serialized.");
            }

            return ReturnValue;
        }

        [Route("api/fantasycricket/sign-up"), HttpPost()]
        public JsonResponse SignUp(UserModel user)
        {
            JsonResponse ReturnValue = null;
            bool validRegistration = false;
            string invalidMessage = "Invalid registration Request.";
            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
            string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
            try
            {
                if (deviceType == "ios" || deviceType == "android")
                    validRegistration = true;
                else if (deviceType == "web")
                {

                    string authToken = httpContext.Request.Headers.Get("x-api-authorization");
                    authToken = ECDC.Decrypt(authToken, _passPhraseReg);
                    var CurrentEpoch = (int)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalSeconds;
                    var RequestedEpoc = Int32.Parse(authToken.Split('=')[1]);

                    //request is within 5 mins then do registration
                    if ((CurrentEpoch - RequestedEpoc) <= 300)
                        validRegistration = true;

                }
                else
                    validRegistration = false;

            }
            catch (Exception Excp)
            {
                invalidMessage += Excp.Message.ToString();
            }

            if (user != null && validRegistration)
            {
                
                    // Validate Minimum Fields provided...
                    if (!string.IsNullOrWhiteSpace(user.Email) && !string.IsNullOrWhiteSpace(user.Password))
                    {
                        var verifyUser = new FantasyCricketProvider().VerifyUser(user);
                        if (verifyUser.Count == 1)
                        {
                            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "UserName already exists.");
                        }
                        // Create User
                        var data = new FantasyCricketProvider().CreateUser(user);
                        return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success);
                    }
                    else // Minimum Fields not provided, reject it...
                    {
                        string Description = string.Empty;

                        if (string.IsNullOrWhiteSpace(user.Email))
                        {
                            Description = "Email; ";
                        }
                        if (string.IsNullOrWhiteSpace(user.Password))
                        {
                            Description += "Password; ";
                        }
                       
                        Description = string.Concat(CultureInfo.InvariantCulture, "Minimum Required Fields were not provided, Request Rejected: ", Description);
                    
                        ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                    }
                
            }
            else
            {
                
                ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, invalidMessage);
            }

            return ReturnValue;
        }

        [Route("api/fantasycricket/save-login-preference"), HttpPost()]
        public JsonResponse SaveLoginPreference(UserModel user)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().SaveLoginPreference(user);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "Failed saving Login preference.");
        }

        [Route("api/fantasycricket/save-profile"), HttpPost()]
        public JsonResponse SaveProfile(UserModel user)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().SaveProfile(user);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success,"Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "Failed saving profile.");
        }

        [Route("api/fantasycricket/fetch-profile"), HttpPost()]
        public JsonResponse FetchProfile(UserModel user)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().FetchProfile(user.UserId);
            if (data.Count > 0)
            {
                
                string statusMessage = "request successful.";
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);
            }
            else
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "there is no profile for this user id.");

        }

        [Route("api/fantasycricket/country-list"), HttpPost()]
        public JsonResponse CountryList()
        {
            var data = new FantasyCricketProvider().CountryList();
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/verify-user"), HttpPost()]
        public JsonResponse VerifyUser(UserModel user)
        {
            var data = new FantasyCricketProvider().VerifyUser(user);
            if (data.Count == 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(data,HttpStatusCode.OK, ResponseStatus.Fail, "user already exists");

        }

        [Route("api/fantasycricket/email-verification-code"), HttpPost()]
        public JsonResponse GetVerificationCode(UserModel user)
        {
            var data = new FantasyCricketProvider().GetVerificationCode(user);
            if(data is string)
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "error sending email");

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "email has been sent successfully");

        }

        [Route("api/fantasycricket/send-verification-code"), HttpPost()]
        public JsonResponse SendVerificationCode(UserModel user)
        {
            var data = new FantasyCricketProvider().SendVerificationCode(user);
            if (data is string)
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "error sending OTP");

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "OTP has been sent successfully");

        }

        [Route("api/fantasycricket/verify-otp"), HttpPost()]
        public JsonResponse VerifyOTP(UserModel user)
        {
            var data = new FantasyCricketProvider().VerifyOTP(user);

            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "Verification failed, incorrect OTP.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "OTP verification successful.");

        }

        [Route("api/fantasycricket/email-verified"), HttpPost()]
        public JsonResponse EmailVerified(UserModel user)
        {
            var data = new FantasyCricketProvider().EmailVerified(user);
            
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-update-user-points"), HttpPost()]
        public JsonResponse AdminUpdateUserPoints(AdminUserPointsModel match)
        {
            var data = new FantasyCricketProvider().AdminUpdateUserPoints(match);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "points updated successfully.");

        }


        [Route("api/fantasycricket/user-team"), HttpPost()]
        public JsonResponse GetUserTeamDetails(UserTeamModel UserTeam)
        {
            if(!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().GetUserTeamDetails(UserTeam.TournamentId, UserTeam.UserId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/tournament-details"), HttpGet()]
        public JsonResponse TournamentDetails()
        {
            var data = new FantasyCricketProvider().TournamentDetails();
            if(data.Count == 0)
                return JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "There is no active tournament at present.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/all-tournament-details"), HttpPost()]
        public JsonResponse AllTournamentDetails()
        {
            var data = new FantasyCricketProvider().AllTournamentDetails();
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "There is no active tournament at present.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-tournament-details"), HttpPost()]
        public JsonResponse UserTournamentDetails(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserTournamentDetails(User);
            
            string statusMessage = "";
            if (data.Count == 0)
                statusMessage = "There is no active tournament for this user.";
            else statusMessage = "request successful";


            if (data.Count == 0)
            {
                 return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "There is no active tournament for this user.");
            }
            else
            {
                 return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful");
            }

        }

        [Route("api/fantasycricket/user-upcoming-tournament"), HttpPost()]
        public JsonResponse UserUpcomingTournament(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserUpcomingTournament(User);
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "There is no upcoming tournament for this user.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }


        [Route("api/fantasycricket/sign-in"), HttpPost()]
        public JsonResponse Login(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;

            var data = new FantasyCricketProvider().Login(loginInfo);
            if (data.Count == 1)
            {
                string statusMessage = "login successful.";
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);
            }
            else
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "Invalid UserId or Password.");

            return ReturnValue;

        }

        [Route("api/fantasycricket/fetch-kyc-details"), HttpPost()]
        public JsonResponse FetchKYCDetails(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;
            var data = new FantasyCricketProvider().FetchKYCDetails(loginInfo);
            ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "fetch request successful.");

            return ReturnValue;
        }

        [Route("api/fantasycricket/update-kyc-status"), HttpPost()]
        public JsonResponse UpdateKYCStatus(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;
            var data = new FantasyCricketProvider().UpdateKYCStatus(loginInfo);
            ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful.");

            return ReturnValue;
        }

        [Route("api/fantasycricket/update-kyc-details"), HttpPost()]
        public JsonResponse UpdateKYCDetails(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;
            var data = new FantasyCricketProvider().UpdateKYCDetails(loginInfo);

            if (data.Count == 1)
            {
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "update successful.");
            }
            else
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "update failure.");
  
            return ReturnValue;
        }


        [Route("api/fantasycricket/update-bank-details"), HttpPost()]
        public JsonResponse UpdateBankDetails(BankDetailsModel bankdetails)
        {

            JsonResponse ReturnValue = null;
            
            bool data = new FantasyCricketProvider().UpdateBankDetails(bankdetails);

            if (data)
            {
                ReturnValue = JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "update successful.");
            }
            else
                ReturnValue = JsonResponse( HttpStatusCode.OK, ResponseStatus.Fail, "update failure.");

           
            return ReturnValue;
        }

        [Route("api/fantasycricket/fetch-bank-details"), HttpPost()]
        public JsonResponse FetchBankDetails(BankDetailsModel bankdetails)
        {

            JsonResponse ReturnValue = null;

            BankDetailsModel data = new FantasyCricketProvider().FetchBankDetails(bankdetails);
          
            if (data.BankVerified == "yes")
            {
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "bank details fetch successful.");
            }
            else
            {
                data.BankName = "";
                data.AccountNumber = "";
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "no data available");
                
            }

            return ReturnValue;
        }

        [Route("api/fantasycricket/transfer-funds"), HttpPost()]
        public JsonResponse TransferFunds(BankDetailsModel bankdetails)
        {

            JsonResponse ReturnValue = null;

            if (bankdetails.amount >= 100 && bankdetails.amount <= 10000)
            {
                FundTransferResponseModel response = new FantasyCricketProvider().TransferFunds(bankdetails);

              //  response.status = false;
                if (response.status)
                {
                    ReturnValue = JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Transfer Initiated Successfully. It may take few hours to complete.");
                }
                else
                {
                    if(bankdetails.BankName == "test")
                        ReturnValue = JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "Transfer Failed - " + response.message);
                    else
                        ReturnValue = JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "Transfer Failed. Please contact fanzania support.");
                }
            }
            else
                ReturnValue = JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "transfer failed, invalid amount.");

            return ReturnValue;
        }


        [Route("api/fantasycricket/external-sign-in"), HttpPost()]
        public JsonResponse ExternalLogin(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;
            string loginProvider = loginInfo.LoginProvider;
            var data = new FantasyCricketProvider().ExternalLogin(loginInfo);
            if (data.Count == 1)
            {
                string statusMessage = "login successful.";
                ReturnValue = JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);
            }
            else
            {
                if(loginProvider == "Apple")
                    ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "email may not exist with this apple device.");
                else
                    ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "login already exists with different provider.");
            }
            return ReturnValue;

        }

        

        [Route("api/fantasycricket/log-out"), HttpPost()]
        public JsonResponse LogOut(UserModel User)
        {
            JsonResponse ReturnValue = null;
             var data = new FantasyCricketProvider().LogOut(User);
             ReturnValue = JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "logout successful.");

            return ReturnValue;
        }

        [Route("api/fantasycricket/create-league"), HttpPost()]
        public JsonResponse CreateLeague(LeagueModel league)
        {
            JsonResponse ReturnValue = null;
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            if (league != null)
            {

                    // Validate Minimum Fields provided...
                    if (!string.IsNullOrWhiteSpace(league.LeagueName)  && league.TournamentId > 0 && league.LeagueLeaderId > 0)
                    {
                       
                       /* var verifyLeagueName = new FantasyCricketProvider().VerifyLeagueName(league);
                        if (verifyLeagueName.Count == 1)
                        {
                            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "League Name already exists.");
                        }*/
                        // Create League
                        var data = new FantasyCricketProvider().CreateLeague(league);
                        if (data.Count > 0)
                            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "League Creation Successful.");
                        else
                            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "League Creation failed.");
                        
                    }
                    else // Minimum Fields not provided, reject it...
                    {
                        string Description = "TournamentId,LeagueName, LeagueLeaderId ";

                        Description = string.Concat(CultureInfo.InvariantCulture, "Invalid Fields, Request Rejected: ", Description);

                        
                        ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                    }
                
            }
            else
            {
                ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "Failed: supplied League data could not be de-serialized.");
            }

            return ReturnValue;
        }

        [Route("api/fantasycricket/verify-league"), HttpPost()]
        public JsonResponse VerifyLeagueName(LeagueModel league)
        {
            var data = new FantasyCricketProvider().VerifyLeagueName(league);
            if(data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "league name already exist.");

        }

        [Route("api/fantasycricket/change-league-name"), HttpPost()]
        public JsonResponse ChangeLeagueName(LeagueModel league)
        {
            var data = new FantasyCricketProvider().ChangeLeagueName(league);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "league name changed.");

        }

        [Route("api/fantasycricket/join-league"), HttpPost()]
        public JsonResponse JoinLeague(LeagueModel league)
        {
            JsonResponse ReturnValue = null;
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            if (league != null)
            {

                    // Validate Minimum Fields provided...
                    if ( !string.IsNullOrWhiteSpace(league.LeaguePin) && league.TournamentId > 0 && league.UserId > 0)
                    {
                        int status = new FantasyCricketProvider().VerifyJoinLeague(league);

                    if (status == 0) {
                        JoinLeague response = new FantasyCricketProvider().JoinLeague(league);

                        if (response.LeagueLeaderId > 0)
                            return JsonResponse( HttpStatusCode.OK, ResponseStatus.Success, "Success, we have let the league admin know about your membership request. You will be notified once approved. Good Luck!");
                        else
                            return JsonResponse(HttpStatusCode.OK, ResponseStatus.Fail, "League pin or tournament invalid.");

                    }
                    else if(status == 1)
                        return JsonResponse(HttpStatusCode.OK, ResponseStatus.Success, "Good news. You are already part of this league.");
                    else
                        return JsonResponse(HttpStatusCode.OK, ResponseStatus.Success, "Your membership request is already with the league admin. Don’t worry, we will send the admin another reminder on your behalf!");
                }
                    else // Minimum Fields not provided, reject it...
                    {
                        string Description = "Mandatory fields - TournamentId, UserId, LeaguePin ";
                        ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                    }
               
            }
            else
            {
                ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "Failed: supplied Join League data could not be de-serialized.");
            }

            return ReturnValue;
        }

        [Route("api/fantasycricket/exit-league"), HttpPost()]
        public JsonResponse ExitLeague(LeagueModel league)
        {

            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var response = new FantasyCricketProvider().ExitLeague(league);

            if (response.Count == 0)
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Success, "exit league successful.");
            else
                return JsonResponse( HttpStatusCode.OK, ResponseStatus.Fail, "failure while exiting league.");

        }


        [Route("api/fantasycricket/approve-league-users"), HttpPost()]
        public JsonResponse ApproveLeagueUser(GenericModel ApproveList)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().ApproveLeagueUser(ApproveList.LeagueId, ApproveList.UserId);
            if (data.Count > 0)
                return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "User is approved successfully.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error approving user.");
        }


        [Route("api/fantasycricket/all-matches"), HttpPost()]
        public JsonResponse GetAllMatches(GenericModel match)
        {
            var data = new FantasyCricketProvider().GetAllMatches(match);
                string statusMessage = "Request successful.";
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);
           

        }

        [Route("api/fantasycricket/all-future-matches"), HttpPost()]
        public JsonResponse AllFutureMatches(GenericModel match)
        {
            var data = new FantasyCricketProvider().AllFutureMatches(match);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request successful");
          
        }
        
        [Route("api/fantasycricket/all-complete-mataches"), HttpGet()]
        public JsonResponse AllCompleteMataches(int TournamentId)
        {
            var data = new FantasyCricketProvider().AllCompleteMataches(TournamentId);

            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error retrieving all matches.");

        }

       
        [Route("api/fantasycricket/unapprove-league-users"), HttpPost()]
        public JsonResponse UnApproveLeagueUser(GenericModel UnApproveList)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UnApproveLeagueUser(UnApproveList);
            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "UnApproved.");

        }

       

        [Route("api/fantasycricket/league-details"), HttpPost()]
        public JsonResponse LeagueDetails(GenericModel League)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().GetLeagueDetails(League.LeagueId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/league-teams"), HttpPost()]
        public JsonResponse LeagueTeams(GenericModel League)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().GetLeagueTeams(League.TournamentId, League.LeagueId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-team-players"), HttpPost()]
        public JsonResponse GetUserTeamPlayerDetails(UserTeamModel UserTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().GetUserTeamPlayerDetails(UserTeam.TournamentId, UserTeam.UserTeamId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-team-players-with-details"), HttpPost()]
        public JsonResponse UserTeamPlayerWithDetails(UserTeamModel UserTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserTeamPlayerWithDetails(UserTeam);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-team-players-details-with-powerplay"), HttpPost()]
        public JsonResponse UserTeamPlayerDetailsWithPowerPlay(UserTeamModel UserTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserTeamPlayerDetailsWithPowerPlay(UserTeam);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-team-players-details-with-stealthmode"), HttpPost()]
        public JsonResponse UserTeamPlayerDetailsWithStealthMode(UserTeamModel UserTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserTeamPlayerDetailsWithStealthMode(UserTeam);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/trigger-point-calculation"), HttpPost()]
        public JsonResponse Trigger4PointCalc(MatchModel pointCalc)
        {
           
            var data = new FantasyCricketProvider().Trigger4PointCalc(pointCalc);

            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "point calculation trigger set successfully.");
            
            //    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "failure, no match(FINISH) avaialble to set this trigger yet");
        }

        [Route("api/fantasycricket/set-manual-point-calc"), HttpPost()]
        public JsonResponse SetManualPointCalc(MatchModel pointCalc)
        {

            var data = new FantasyCricketProvider().SetManualPointCalc(pointCalc);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Now point calculation trigger should be manual.");
        }

        [Route("api/fantasycricket/user-team-players-last-cutoff"), HttpPost()]
        public JsonResponse LastCutoffUserTeamPlayerDetails(UserTeamModel UserTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().LastCutoffUserTeamPlayerDetails(UserTeam.TournamentId, UserTeam.UserTeamId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

       

        [Route("api/fantasycricket/user-all-leagues"), HttpPost()]
        public JsonResponse UserAllLeagues(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().GetUserAllLeagues(User.UserId, User.TournamentId);
            string statusMessage = "request successful.";
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);

        }

        [Route("api/fantasycricket/reset-league-pin"), HttpPost()]
        public JsonResponse ResetLeaguePin(LeagueModel league)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().ResetLeaguePin(league);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/save-new-password"), HttpPost()]
        public JsonResponse SaveNewPassword(UserModel user)
        {
            var data = new FantasyCricketProvider().SaveNewPassword(user.UserId, user.Password);
            if(data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "password has been saved successfully.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "password save failure");
        }
        [Route("api/fantasycricket/forget-password"), HttpPost()]
        public JsonResponse ForgetPassword(UserModel user)
        {
            var retVal = new FantasyCricketProvider().ForgetPassword(user);
            if(retVal == true)
                return JsonResponse(HttpStatusCode.OK, ResponseStatus.Success, "New Password has been sent to email address. Please check your email including spam or junk folder."); 
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Password Reset request failed. Please verify Email address.");
        }

        [Route("api/fantasycricket/verify-user-team-name"), HttpPost()]
        public JsonResponse VerifyUserTeam(UserTeamModel UserTeam)
        {
            var data = new FantasyCricketProvider().VerifyUserTeam(UserTeam.UserId, UserTeam.TournamentId, UserTeam.UserTeamName);
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "User Team Name may not exist.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/verify-team-name"), HttpPost()]
        public JsonResponse VerifyTeamName(UserTeamModel UserTeam)
        {
            var data = new FantasyCricketProvider().VerifyTeamName(UserTeam);
            if (data.Count == 1)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "user team Name already exist.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/create-user-team"), HttpPost()]
        public JsonResponse CreateUserTeam(UserTeamModel userTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            JsonResponse ReturnValue = null;


                    // Validate Minimum Fields provided...
                    if (!string.IsNullOrWhiteSpace(userTeam.UserTeamName) && userTeam.UserId >0 && userTeam.TournamentId > 0)
                    {
                        var verifyTeamName = new FantasyCricketProvider().VerifyUserTeam(userTeam.UserId, userTeam.TournamentId, userTeam.UserTeamName);
                        if (verifyTeamName.Count == 1)
                        {
                            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Team Name already exists.");
                        }
                        // Create League
                        var data = new FantasyCricketProvider().CreateUserTeam(userTeam);
                        if (data.Count == 1)
                            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success,"Request Successful.");
                        else
                            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "user team creation failure");
                    }
                    else // Minimum Fields not provided, reject it...
                    {
                        string Description = string.Empty;

                        if (string.IsNullOrWhiteSpace(userTeam.UserTeamName))
                        {
                            Description = "UserTeamName; ";
                        }
                        if (userTeam.UserId <= 0)
                        {
                            Description += "UserId; ";
                        }
                        if (userTeam.TournamentId <= 0)
                        {
                            Description += "TournamentId; ";
                        }
                    Description = string.Concat(CultureInfo.InvariantCulture, "Invalid Fields, Request Rejected: ", Description);
                        // Logger.Write(_logServiceURI, enmEventType.Error, "Client Record could not be created.", Description, "ClientController.CreateClient", _AppName, _Filename, 63);
                        ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                    }
               
            return ReturnValue;
        }

        [Route("api/fantasycricket/modify-user-team"), HttpPost()]
        public JsonResponse ModifyUserTeam(UserTeamModel userTeam)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            JsonResponse ReturnValue = null;

                // Validate Minimum Fields provided...
                if (!string.IsNullOrWhiteSpace(userTeam.UserTeamName) && userTeam.UserTeamId > 0 && userTeam.TournamentId > 0)
                {
                /*
                    var verifyTeamName = new FantasyCricketProvider().VerifyUserTeam(userTeam.UserId, userTeam.TournamentId, userTeam.UserTeamName);
                    if (verifyTeamName.Count == 1)
                    {
                        return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Team Name already exists.");
                    }
                  */  
                var data = new FantasyCricketProvider().ModifyUserTeam(userTeam);
                if (data.Count == 1)
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "user team modification failure");
            }
                else // Minimum Fields not provided, reject it...
                {
                    string Description = string.Empty;

                    if (string.IsNullOrWhiteSpace(userTeam.UserTeamName))
                    {
                        Description = "UserTeamName; ";
                    }
                    if (userTeam.UserTeamId <= 0)
                    {
                        Description += "UserTeamId; ";
                    }
                    if (userTeam.TournamentId <= 0)
                    {
                        Description += "TournamentId; ";
                    }
                    Description = string.Concat(CultureInfo.InvariantCulture, "Invalid Fields, Request Rejected: ", Description);
                    // Logger.Write(_logServiceURI, enmEventType.Error, "Client Record could not be created.", Description, "ClientController.CreateClient", _AppName, _Filename, 63);
                    ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, Description);
                }
          


            return ReturnValue;
        }

        [Route("api/fantasycricket/save-team-selection"), HttpPost()]
        public JsonResponse SaveTeamSelection(TeamCompositionModel TeamSelection)
        {
            JsonResponse ReturnValue = null;
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
            string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");

           // if(deviceType == "web")
             //   return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "web under maintenance, please use android/ios to manage team.");

            // Validate Minimum Fields provided...
            if (TeamSelection.UserTeamId>0 && TeamSelection.Player1>0 && TeamSelection.Player2 > 0 && TeamSelection.Player3 > 0 && TeamSelection.Player4 > 0
                    && TeamSelection.Player5 > 0 && TeamSelection.Player6 > 0 && TeamSelection.Player7 > 0 && TeamSelection.Player8 > 0 && TeamSelection.Player9 > 0
                    && TeamSelection.Player10 > 0 && TeamSelection.Player11 > 0 && TeamSelection.TeamCapt > 0 && TeamSelection.TeamVCapt > 0 && TeamSelection.TeamCapt != TeamSelection.TeamVCapt)
                {
                if (TeamSelection.TeamCapt == TeamSelection.Player1 || TeamSelection.TeamCapt == TeamSelection.Player2
                    || TeamSelection.TeamCapt == TeamSelection.Player3 || TeamSelection.TeamCapt == TeamSelection.Player4
                    || TeamSelection.TeamCapt == TeamSelection.Player5 || TeamSelection.TeamCapt == TeamSelection.Player6
                    || TeamSelection.TeamCapt == TeamSelection.Player7 || TeamSelection.TeamCapt == TeamSelection.Player8
                    || TeamSelection.TeamCapt == TeamSelection.Player9 || TeamSelection.TeamCapt == TeamSelection.Player10
                    || TeamSelection.TeamCapt == TeamSelection.Player11)
                {
                    if ((TeamSelection.TeamVCapt > 0) && (TeamSelection.TeamVCapt != TeamSelection.Player1 && TeamSelection.TeamVCapt != TeamSelection.Player2
                    && TeamSelection.TeamVCapt != TeamSelection.Player3 && TeamSelection.TeamVCapt != TeamSelection.Player4
                    && TeamSelection.TeamVCapt != TeamSelection.Player5 && TeamSelection.TeamVCapt != TeamSelection.Player6
                    && TeamSelection.TeamVCapt != TeamSelection.Player7 && TeamSelection.TeamVCapt != TeamSelection.Player8
                    && TeamSelection.TeamVCapt != TeamSelection.Player9 && TeamSelection.TeamVCapt != TeamSelection.Player10
                    && TeamSelection.TeamVCapt != TeamSelection.Player11)) {
                        return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "TeamVCapt is invalid, team will not be saved.");
                    }
                    var data = new FantasyCricketProvider().SaveTeamSelection(TeamSelection);
                    if (data.Count == 1)
                        return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "team selection saved successful.");
                    else
                        return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "team selection did not save.");
                }
                else
                {
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "TeamCapt is invalid, team will not be saved.");
                }
                    
                }
                else // Minimum Fields not provided, reject it...
                {
                   
                    ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "All eleven players(and captain, vice captain) were not selected or userteam missing.");
                }
            

            return ReturnValue;
        }


        [Route("api/fantasycricket/end-match"), HttpGet()]
        public JsonResponse EndMatch(int matchId)
        {
            if (matchId > 0)
            {
                var data = new FantasyCricketProvider().EndMatch(matchId);
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            }
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: Match cant end.");
        }

        [Route("api/fantasycricket/start-match"), HttpPost()]
        public JsonResponse StartMatch(MatchModel NewMatch)
        {

            var data = new FantasyCricketProvider().StartMatch(NewMatch);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Match started successfully");
             
        }

        [Route("api/fantasycricket/set-toss-winner"), HttpPost()]
        public JsonResponse SetTossWinner(MatchModel NewMatch)
        {

            var data = new FantasyCricketProvider().SetTossWinner(NewMatch);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Toss Winner set successfully");

        }

        [Route("api/fantasycricket/user-feedback"), HttpPost()]
        public JsonResponse SaveUserFeedback(FeedbackModel NewFeedback)
        {
          var data = new FantasyCricketProvider().SaveUserFeedback(NewFeedback);
          return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Thank you for your message. We will get back to you for any concern.");
        }

        [Route("api/fantasycricket/updatematchplayerstats"), HttpPost()]
        public JsonResponse UpdateMatchPlayerStats(MatchPlayerStatsModel MatchPlayer)
        {
           
                if (MatchPlayer.PlayerId > 0 && MatchPlayer.MatchId > 0)
                {
                    var data = new FantasyCricketProvider().UpdateMatchPlayerStats(MatchPlayer);
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: Player Id or Match Id missing.");
           
        }

        [Route("api/fantasycricket/calculatematchpoint"), HttpGet()]
        public JsonResponse CalculateMatchPoint(int matchId)
        {
            
                if (matchId > 0)
                {
                    var data = new FantasyCricketProvider().CalculateMatchPoint(matchId);
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: Match Id missing.");
       
        }

        [Route("api/fantasycricket/user-team-match-details"), HttpPost()]
        public JsonResponse UserTeamMatchDetails(GenericModel UserMatch)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            if (UserMatch.MatchId > 0 && UserMatch.UserTeamId > 0)
                {
                    var data = new FantasyCricketProvider().UserTeamMatchDetails(UserMatch.MatchId, UserMatch.UserTeamId);
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "failure, MatchId or UserTeamId invalid");
         
        }

        [Route("api/fantasycricket/user-team-match-details-with-players"), HttpPost()]
        public JsonResponse UserTeamMatchDetailsWithPlayers(GenericModel UserMatch)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            if (UserMatch.UserTeamId > 0 && UserMatch.TournamentId > 0)
            {
                var data = new FantasyCricketProvider().UserTeamMatchDetailsWithPlayers(UserMatch);
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            }
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "failure, MatchId or UserTeamId invalid");

        }

        [Route("api/fantasycricket/match-player-stats"), HttpGet()]
        public JsonResponse GetMatchPlayerStats(int matchId)
        {
      
                if (matchId > 0)
                {
                    var data = new FantasyCricketProvider().GetMatchPlayerStats(matchId);
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: Match Id missing.");
  
        }

        [Route("api/fantasycricket/reset-subscription"), HttpPost()]
        public JsonResponse ResetSubscription()
        {
                var data = new FantasyCricketProvider().ResetSubscription();
            if(data.Count > 0)
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "reset subscription successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "reset subscription failed as there is active tournament.");

        }

        [Route("api/fantasycricket/player-details"), HttpPost()]
        public JsonResponse GetPlayerDetails(PlayerListModel playerList)
        {

                if (playerList.TournamentId > 0)
                {
                    var data = new FantasyCricketProvider().GetPlayerDetails(playerList);
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: Input data invalid");
            
        }

        

        [Route("api/fantasycricket/get-tournament-players"), HttpPost()]
        public JsonResponse GetTournamentPlayers(PlayerListModel list)
        {

            if (list.TournamentId > 0)
            {
                var data = new FantasyCricketProvider().GetAllPlayers(list.TournamentId);
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            }
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "failure to fetch tournament players");
            
        }

        

        [Route("api/fantasycricket/save-theme"), HttpGet()]
        public JsonResponse SaveTheme(int userId, string theme)
        {
  
                if (userId > 0)
                {
                    var data = new FantasyCricketProvider().SaveTheme(userId, theme);
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
                }
                else
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failed: saving theme");
           
        }

        [Route("api/fantasycricket/participation-team"), HttpGet()]
        public JsonResponse GetParticipationTeam(int tournamentId)
        {
            var data = new FantasyCricketProvider().GetParticipationTeam(tournamentId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
               
        }

        [Route("api/fantasycricket/last-match-points"), HttpPost()]
        public JsonResponse LastMatchPoints(GenericModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().LastMatchPoints(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }


        [Route("api/fantasycricket/fetch-user-powerplay"), HttpPost()]
        public JsonResponse FetchUserPowerPlay(GenericModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().FetchUserPowerPlay(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/update-user-powerplay"), HttpPost()]
        public JsonResponse UpdateUserPowerPlay(UpdateUserPowerPlayModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UpdateUserPowerPlay(details);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "update Successful.");

        }

        [Route("api/fantasycricket/team-selection-rule"), HttpPost()]
        public JsonResponse TeamSelectionRule(GenericModel details)
        {
            var data = new FantasyCricketProvider().TeamSelectionRule(details);           
            string statusMessage = "request successful.";
           return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, statusMessage);

        }

        [Route("api/fantasycricket/auto-select-team"), HttpPost()]
        public JsonResponse AutoSelectTeam(GenericModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().AutoSelectTeam(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }



        [Route("api/fantasycricket/live-matches"), HttpPost()]
        public JsonResponse LiveMatches(GenericModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().LiveMatches(details);
                if(data.Count>0)
                    return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request is successful");
                else
                    return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "request failure");
            
        }

        [Route("api/fantasycricket/live-match-score"), HttpPost()]
        public JsonResponse LiveMatchScore(GenericModel details)
        {
            var data = new FantasyCricketProvider().LiveMatchScore(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/live-user-team-score"), HttpPost()]
        public JsonResponse LiveUserTeamScore(GenericModel details)
        {
            var data = new FantasyCricketProvider().LiveUserTeamScore(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/live-league-users"), HttpPost()]
        public JsonResponse LiveLeagueUsers(GenericModel details)
        {
            var data = new FantasyCricketProvider().LiveLeagueUsers(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/fetch-league-subscription"), HttpPost()]
        public JsonResponse FetchLeagueSubscription(LeagueModel league)
        {
            var data = new FantasyCricketProvider().FetchLeagueSubscription(league);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/update-league-subscription"), HttpPost()]
        public JsonResponse UpdateLeagueSubscription(List<BulkSubscriptionModel> SubscriptionDetails)
        {
            bool status = new FantasyCricketProvider().UpdateLeagueSubscription(SubscriptionDetails);
            if(status == true)
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Request failure.");

        }

        [Route("api/fantasycricket/live-tournament-details"), HttpPost()]
        public JsonResponse LiveTournamentDetails(GenericModel details)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().LiveTournamentDetails(details);
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Come back when match is live or check your subscription package.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/fetch-static-urls"), HttpPost()]
        public JsonResponse StaticURLs()
        {
            var data = new FantasyCricketProvider().StaticURLs();
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-stats-global-top-players"), HttpPost()]
        public JsonResponse UserStatsGlobalTopPlayers(GenericModel details)
        {
            var data = new FantasyCricketProvider().UserStatsGlobalTopPlayers(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-stats-user-top-players"), HttpPost()]
        public JsonResponse UserStatsUserTopPlayers(GenericModel details)
        {
            var data = new FantasyCricketProvider().UserStatsUserTopPlayers(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-stats-global-top-teams"), HttpPost()]
        public JsonResponse UserStatsGlobalTopTeams(GenericModel details)
        {
            var data = new FantasyCricketProvider().UserStatsGlobalTopTeams(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-stats-captain-points"), HttpPost()]
        public JsonResponse UserStatsCaptainPoints(GenericModel details)
        {
            var data = new FantasyCricketProvider().UserStatsCaptainPoints(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/league-stats-global-top-leagues"), HttpPost()]
        public JsonResponse LeagueStatsGlobalTopLeagues(GenericModel details)
        {
            var data = new FantasyCricketProvider().LeagueStatsGlobalTopLeagues(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/league-stats-top-teams-top-perform"), HttpPost()]
        public JsonResponse LeagueStatsTopTeamsTopPerform(GenericModel details)
        {
            var data = new FantasyCricketProvider().LeagueStatsTopTeamsTopPerform(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/league-stats-top-teams-top-favorite"), HttpPost()]
        public JsonResponse LeagueStatsTopTeamsTopFavorite(GenericModel details)
        {
            var data = new FantasyCricketProvider().LeagueStatsTopTeamsTopFavorite(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/distinct-team-filter"), HttpPost()]
        public JsonResponse DistinctTeam(GenericModel details)
        {
            var data = new FantasyCricketProvider().DistinctTeam(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-sign-in"), HttpPost()]
        public JsonResponse AdminLogin(UserModel loginInfo)
        {

            JsonResponse ReturnValue = null;
            string AdminPass = ConfigurationManager.AppSettings["AdminPass"];
            if (loginInfo.UserName == "fanzania.admin" && loginInfo.Password == AdminPass)
            {
                ReturnValue = JsonResponse(HttpStatusCode.OK, ResponseStatus.Success, "Login successful.");
            }
            else
            {
                var data = new FantasyCricketProvider().AdminLogin(loginInfo);
                if (data.Count == 1)
                    ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Success, "Login successful.");
                else
                    ReturnValue = JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Fail, "Invalid UserName or Password.");
            }
            return ReturnValue;

        }

        [Route("api/fantasycricket/admin-user-promote"), HttpPost()]
        public JsonResponse AdminUserPromote(UserModel loginInfo)
        {
            var data = new FantasyCricketProvider().AdminUserPromote(loginInfo);
            return JsonResponse(data, HttpStatusCode.OK, ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-update-tournament"), HttpPost()]
        public JsonResponse AdminUpdateTournament(AdminTournamentModel tournament)
        {
            var data = new FantasyCricketProvider().AdminUpdateTournament(tournament);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-delete-tournament"), HttpPost()]
        public JsonResponse AdminDeleteTournament(AdminTournamentModel tournament)
        {
            var data = new FantasyCricketProvider().AdminDeleteTournament(tournament);
            if(data.Count == 0)
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Tournament deleted successfully.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error deleting tournament");

        }

        [Route("api/fantasycricket/admin-fetch-tournament"), HttpPost()]
        public JsonResponse AdminFetchTournament(AdminTournamentModel tournament)
        {
            var data = new FantasyCricketProvider().AdminFetchTournament(tournament);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-update-tournament-team-rules"), HttpPost()]
        public JsonResponse AdminUpdateTournamentTeamRules(AdminTeamRulesModel rules)
        {
            var data = new FantasyCricketProvider().AdminUpdateTournamentTeamRules(rules);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-update-tournament-point-rules"), HttpPost()]
        public JsonResponse AdminUpdateTournamentPointRules(AdminGenericModel point)
        {
            var data = new FantasyCricketProvider().AdminUpdateTournamentPointRules(point);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-fetch-tournament-point-rules"), HttpPost()]
        public JsonResponse AdminFetchTournamentPointRules(AdminTournamentModel tournament)
        {
            var data = new FantasyCricketProvider().AdminFetchTournamentPointRules(tournament);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-upload-participation-team"), HttpPost()]
        public JsonResponse AdminUploadParticipationTeam(AdminGenericModel team)
        {
            var data = new FantasyCricketProvider().AdminUploadParticipationTeam(team);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-update-participation-team"), HttpPost()]
        public JsonResponse AdminUpdateParticipationTeam(AdminParticipationTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminUpdateParticipationTeam(team);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-delete-participation-team"), HttpPost()]
        public JsonResponse AdminDeleteParticipationTeam(AdminParticipationTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminDeleteParticipationTeam(team);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-upload-player-details"), HttpPost()]
        public JsonResponse AdminUploadPlayerDetails(AdminGenericModel player)
        {
            var data = new FantasyCricketProvider().AdminUploadPlayerDetails(player);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "player upload successful");

        }

        [Route("api/fantasycricket/last-match-top-performer-user"), HttpGet(), HttpPost()]
        public JsonResponse LastMatchTopPerformer(UserTeamModel users)
        {
            var data = new FantasyCricketProvider().LastMatchTopPerformer(users);
            if(data.Count>0)
                return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request is successful");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "There is no data.");

        }

        [Route("api/fantasycricket/user-count"), HttpGet(), HttpPost()]
        public JsonResponse UserCount()
        {
            var data = new FantasyCricketProvider().UserCount();
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request is successful");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "There is no data.");

        }

        [Route("api/fantasycricket/admin-update-player-details"), HttpPost()]
        public JsonResponse AdminUpdatePlayerDetails(AdminPlayerModel player)
        {
            var data = new FantasyCricketProvider().AdminUpdatePlayerDetails(player);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-delete-player-details"), HttpPost()]
        public JsonResponse AdminDeletePlayerDetails(AdminPlayerModel player)
        {
            var data = new FantasyCricketProvider().AdminDeletePlayerDetails(player);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-fetch-player-details"), HttpPost()]
        public JsonResponse AdminFetchPlayerDetails(AdminPlayerModel player)
        {
            var data = new FantasyCricketProvider().AdminFetchPlayerDetails(player);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-fetch-participation-team"), HttpPost()]
        public JsonResponse AdminFetchParticipationTeam(AdminParticipationTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminFetchParticipationTeam(team);
            if(data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failure fetching team");

        }

        [Route("api/fantasycricket/admin-insert-match-details"), HttpPost()]
        public JsonResponse AdminInsertMatchDetails(AdminGenericModel match)
        {
            var data = new FantasyCricketProvider().AdminInsertMatchDetails(match);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Insert match details successfully");

        }

        [Route("api/fantasycricket/upload-match-details"), HttpPost()]
        public JsonResponse UploadMatchDetails(object match)
        {
            var data = new FantasyCricketProvider().UploadMatchDetails(match);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Insert match details successfully");

        }

        [Route("api/fantasycricket/upload-daily-match-details"), HttpPost()]
        public JsonResponse UploadDailyMatchDetails(object match)
        {
            var data = new FantasyCricketProvider().UploadDailyMatchDetails(match);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Insert daily match details successfully");

        }

        [Route("api/fantasycricket/admin-update-match-details"), HttpPost()]
        public JsonResponse AdminUpdateMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminUpdateMatchDetails(match);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-delete-match-details"), HttpPost()]
        public JsonResponse AdminDeleteMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminDeleteMatchDetails(match);
            if(data.Count == 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Match delete successfully");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error on delete");
        }

        [Route("api/fantasycricket/admin-fetch-match-details"), HttpPost()]
        public JsonResponse AdminFetchMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminFetchMatchDetails(match);
            if(data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failure while fetching");
        }

        [Route("api/fantasycricket/admin-fetch-match-onstart"), HttpPost()]
        public JsonResponse AdminFetchMatchOnStart(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminFetchMatchOnStart(match);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failure while fetching");
        }

        [Route("api/fantasycricket/admin-switch-batting-team"), HttpPost()]
        public JsonResponse AdminSwitchBattingTeam(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminSwitchBattingTeam(match);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Switch was not successful.");
        }

        [Route("api/fantasycricket/admin-sync-match-apikey"), HttpPost()]
        public JsonResponse AdminSyncMatchApiKey(AdminMatchDetailsModel match)
        {
            var list = new FantasyCricketProvider().AdminSyncMatchApiKey(match);
            string response = "Success, this matchId list is not yet synced: " + list.ToString();
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, response);
        }

        [Route("api/fantasycricket/admin-sync-matchid"), HttpPost()]
        public JsonResponse AdminSyncMatchid(AdminMatchDetailsModel series)
        {
            var list = new FantasyCricketProvider().AdminSyncMatchid(series);
            
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "match sync is complete.");
        }

        [Route("api/fantasycricket/admin-sync-teamid"), HttpPost()]
        public JsonResponse AdminSyncTeamid(AdminMatchDetailsModel series)
        {
            var list = new FantasyCricketProvider().AdminSyncTeamid(series);
            
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "team sync is complete.");
        }

        [Route("api/fantasycricket/admin-sync-team-players"), HttpPost()]
        public JsonResponse AdminSyncTeamPlayers(AdminMatchDetailsModel series)
        {
            var list = new FantasyCricketProvider().AdminSyncTeamPlayers(series);
            
            if(list == "success")
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request success");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, list);
        }

        [Route("api/fantasycricket/admin-update-auto-team"), HttpPost()]
        public JsonResponse AdminUpdateAutoTeam(AdminAutoTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminUpdateAutoTeam(team);
            
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            
        }

        [Route("api/fantasycricket/admin-delete-auto-team"), HttpPost()]
        public JsonResponse AdminDeleteAutoTeam(AdminAutoTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminDeleteAutoTeam(team);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-fetch-auto-team"), HttpPost()]
        public JsonResponse AdminFetchAutoTeam(AdminAutoTeamModel team)
        {
            var data = new FantasyCricketProvider().AdminFetchAutoTeam(team);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
           
        }

        [Route("api/fantasycricket/admin-reset-transfer"), HttpPost()]
        public JsonResponse AdminResetTransfer(ResetTransferModel value)
        {
            var data = new FantasyCricketProvider().AdminResetTransfer(value);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/upload-profile-image"), HttpPost()]
        public JsonResponse UploadProfileImage()
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
            string UserId = httpContext.Request.Headers.Get("x-api-userid");
            string KYCDoc = httpContext.Request.Headers.Get("kyc-doc");
            string ext = "";
            bool uploaded = false;
            string ImageName = null;
            string errMessage = "";
            string postedFileName = "";
            string fileSavePath = "";

            try
            {
                var httpRequest = HttpContext.Current.Request;
                var postedFile = httpRequest.Files["UploadedImage"];
               // postedFileName = postedFile.FileName;

                if (postedFile != null && UserId != null)
                {
                    ext = Path.GetExtension(postedFile.FileName);
                    if(KYCDoc != null && KYCDoc == "PAN")
                        ImageName = KYCDoc+"_" + UserId + ext;
                    else
                        ImageName = "user_" + UserId + ext;

                    string fileDir = AppDomain.CurrentDomain.BaseDirectory;
                    string mappedPath = fileDir + _ProfileImageFilePath; //System.Web.Hosting.HostingEnvironment.MapPath(_ProfileImageFilePath);
                    if (File.Exists(mappedPath + ImageName))
                    {
                        File.Delete(mappedPath + ImageName);
                    }

                    fileSavePath = mappedPath + ImageName;

                    // Save the uploaded file to server folder
                    postedFile.SaveAs(fileSavePath);
                    uploaded = true;
                }

                if (UserId == null)
                    errMessage += "x-api-userid Header missing.";

                if (postedFile == null)
                    errMessage += "File name with path missing.";
            }
            catch (Exception Excp)
            {

                if (string.IsNullOrWhiteSpace(UserId))
                    UserId = "";
                string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                if (string.IsNullOrWhiteSpace(authToken))
                    authToken = "";

                errMessage = Excp.Message.ToString();
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = ImageName;
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "/api/fantasycricket/upload-profile-image";
                if (authToken == "")
                    error.TransactionId = Guid.NewGuid().ToString();
                else
                    error.TransactionId = authToken;
                error.TransactionType = deviceType;
               // error.Payload = selectQuery;
                error.LoginUser = UserId;
                Utilities.LogException(error);

            }
             
            if (uploaded == true)
            {

                if (KYCDoc != null && KYCDoc == "PAN")
                {
                    var data = new FantasyCricketProvider().UploadKYCImage(UserId, KYCDoc, ImageName);
                    
                }
                else
                {
                    var data = new FantasyCricketProvider().UploadProfileImage(UserId, ext);
                }
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "upload Successful.");
            }
            else
                return JsonResponse( HttpStatusCode.OK, ResponseStatus.Fail, "failure:"+ errMessage + " PostedFileName: "+ postedFileName);
        }

        public bool ValidateToken()
        {
            if (TokenValidation == "false")//no token validation if false
                return true;

            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;

            string userId = httpContext.Request.Headers.Get("x-api-userid");
            string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
           
            if (string.IsNullOrWhiteSpace(userId) || string.IsNullOrWhiteSpace(authToken))
                return false;
            else
            {
                var data = new FantasyCricketProvider().ValidateToken(authToken, userId);
                return data;
            }

        }

        [Route("api/fantasycricket/fetch-notification-message"), HttpPost()]
        public JsonResponse FetchNotificationMessage(UserModel user)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
            string UserId = httpContext.Request.Headers.Get("x-api-userid");

            var data = new FantasyCricketProvider().FetchNotificationMessage(user.UserId);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "There is no pending notification for you at this moment.");

        }

        [Route("api/fantasycricket/ack-notification-message"), HttpPost()]
        public JsonResponse AcknowledgeNotificationMessage(NotificationModel Notification)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().AcknowledgeNotificationMessage(Notification);
  
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
      

        }

        [Route("api/fantasycricket/send-notification-message"), HttpPost()]
        public JsonResponse SendNotificationMessage(NotificationModel Notification)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().SendNotificationMessage(Notification);

            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "notifications sent successful.");


        }

        [Route("api/fantasycricket/fetch-notification-count"), HttpPost()]
        public JsonResponse FetchNotificationCount(UserModel user)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
            string UserId = httpContext.Request.Headers.Get("x-api-userid");

            var data = new FantasyCricketProvider().FetchNotificationCount(user.UserId);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/test-call"), HttpPost()]
        public JsonResponse TestCall(object pl)
        {

           var data = new FantasyCricketProvider().TestCall(pl);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-communication"), HttpPost()]
        public JsonResponse UserCommunication(UserCommModel comm)
        {

            bool response = new FantasyCricketProvider().UserCommunication(comm);
            if(response)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Communication sent successfully.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error sending emails");

        }

        [Route("api/fantasycricket/manual-score-update"), HttpPost()]
        public JsonResponse ManualScoreUpdate(AdminGenericModel mData)
        {

            bool data = new FantasyCricketProvider().ManualScoreUpdate(mData);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Score updated successfully.");

        }

        [Route("api/fantasycricket/manual-score-update-each-player"), HttpPost()]
        public JsonResponse ManualScoreUpdateEachPlayer(ManualScoreUpdate mData)
        {

            bool data = new FantasyCricketProvider().ManualScoreUpdateEachPlayer(mData);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Player Score updated successfully.");

        }

        [Route("api/fantasycricket/set-manual-mom"), HttpPost()]
        public JsonResponse SetManualMoM(ManualScoreUpdate mData)
        {

            bool data = new FantasyCricketProvider().SetManualMoM(mData);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "MoM updated successfully.");

        }

        [Route("api/fantasycricket/fetch-manual-score"), HttpPost()]
        public JsonResponse FetchManualScore(ManualScoreUpdate mData)
        {

            var data = new FantasyCricketProvider().FetchManualScore(mData);
            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Data retrieved successfully.");

        }

        [Route("api/fantasycricket/fetch-player-stats"), HttpPost()]
        public JsonResponse FetchPlayerStats(AdminPlayerModel player)
        {

            var data = new FantasyCricketProvider().FetchPlayerStats(player);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Data retrieved successfully.");

        }

        [Route("api/fantasycricket/get-subscription-details"), HttpPost()]
        public JsonResponse GetSubscriptionDetails(UserModel user)
        {
            var data = new FantasyCricketProvider().GetSubscriptionDetails(user);
           
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "subscription retrieved successfully.");

        }

        [Route("api/fantasycricket/update-subscription-details"), HttpPost()]
        public JsonResponse UpdateSubscriptionDetails(UserModel user)
        {
            var data = new FantasyCricketProvider().UpdateSubscriptionDetails(user);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "subscription updated successfully.");

        }

        [Route("api/fantasycricket/add-money"), HttpPost()]
        public JsonResponse AddMoney2Wallet(PaymentCF paymentDetails)
        {
            var data = new FantasyCricketProvider().AddMoney2Wallet(paymentDetails);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "add money to wallet successfull.");

        }

        [Route("api/fantasycricket/fetch-payment-gateway-details"), HttpPost()]
        public JsonResponse FetchPaymentGatewayDetails()
        {
            var data = new FantasyCricketProvider().FetchPaymentGatewayDetails();
            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Data retrieved successfully.");

        }


        #region Daily Matches

        [Route("api/fantasycricket/user-upcoming-daily-matches"), HttpPost()]
        public JsonResponse UserUpcomingDailyMatches(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserUpcomingDailyMatches(User);
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Stay tuned. We will be back very soon with exciting matches for you.");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-daily-matches"), HttpPost()]
        public JsonResponse UserDailyMatches(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            string failureMessage="";

            if (User.PageIndicator == 1)//1 home page
                failureMessage = "You can join macthes in 'Join Match' section for any available matches.";
            else if (User.PageIndicator == 2)//league page
                failureMessage = "You can see your final league standings here after match completion.";
            else if (User.PageIndicator == 3)//team page
                failureMessage = "You don't have any upcoming matches. Please select a match in the Home screen to make your team.";
            else if (User.PageIndicator == 4)//live
                failureMessage = "You can see a match here when it is live or check your subscription package.";
            else if (User.PageIndicator == 5)//see all
                failureMessage = "There is no match for your in this status yet.";
            else
                failureMessage = "There is no daily matches for this user or check your subscription package.";


            var data = new FantasyCricketProvider().UserDailyMatches(User);
            if (data.Count == 0)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, failureMessage);
            else
                return JsonResponse( data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-league-teams"), HttpPost()]
        public JsonResponse DailyLeagueTeams(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().DailyLeagueTeams(User);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-matches-team-selection-rules"), HttpPost()]
        public JsonResponse DailyTeamSelectionRules(GenericModel User)
        {
            var data = new FantasyCricketProvider().DailyTeamSelectionRules(User);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-matches-player-list"), HttpPost()]
        public JsonResponse DailyMatchesPlayerList(GenericModel User)
        {
            var data = new FantasyCricketProvider().DailyMatchesPlayerList(User);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-daily-team-players"), HttpPost()]
        public JsonResponse UserDailyTeamPlayers(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserDailyTeamPlayers(User);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/user-daily-team-players-with-points"), HttpPost()]
        public JsonResponse UserDailyTeamPlayersWithPoints(GenericModel User)
        {
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            var data = new FantasyCricketProvider().UserDailyTeamPlayersWithPoints(User);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/save-daily-team-selection"), HttpPost()]
        public JsonResponse SaveDailyTeamSelection(TeamCompositionModel TeamSelection)
        {
            JsonResponse ReturnValue = null;
            if (!ValidateToken())
                return JsonResponse(HttpStatusCode.Unauthorized, ResponseStatus.Fail, "auth token not valid");

            // Validate Minimum Fields provided...
            if (TeamSelection.UserId>0 && TeamSelection.TournamentId > 0 && TeamSelection.Player1 > 0 && TeamSelection.Player2 > 0 && TeamSelection.Player3 > 0 && TeamSelection.Player4 > 0
                    && TeamSelection.Player5 > 0 && TeamSelection.Player6 > 0 && TeamSelection.Player7 > 0 && TeamSelection.Player8 > 0 && TeamSelection.Player9 > 0
                    && TeamSelection.Player10 > 0 && TeamSelection.Player11 > 0 && TeamSelection.TeamCapt > 0 && TeamSelection.TeamVCapt>0 && TeamSelection.TeamCapt != TeamSelection.TeamVCapt)
            {
                if (TeamSelection.TeamCapt == TeamSelection.Player1 || TeamSelection.TeamCapt == TeamSelection.Player2
                    || TeamSelection.TeamCapt == TeamSelection.Player3 || TeamSelection.TeamCapt == TeamSelection.Player4
                    || TeamSelection.TeamCapt == TeamSelection.Player5 || TeamSelection.TeamCapt == TeamSelection.Player6
                    || TeamSelection.TeamCapt == TeamSelection.Player7 || TeamSelection.TeamCapt == TeamSelection.Player8
                    || TeamSelection.TeamCapt == TeamSelection.Player9 || TeamSelection.TeamCapt == TeamSelection.Player10
                    || TeamSelection.TeamCapt == TeamSelection.Player11)
                {
                    if ((TeamSelection.TeamVCapt > 0) && (TeamSelection.TeamVCapt != TeamSelection.Player1 && TeamSelection.TeamVCapt != TeamSelection.Player2
                    && TeamSelection.TeamVCapt != TeamSelection.Player3 && TeamSelection.TeamVCapt != TeamSelection.Player4
                    && TeamSelection.TeamVCapt != TeamSelection.Player5 && TeamSelection.TeamVCapt != TeamSelection.Player6
                    && TeamSelection.TeamVCapt != TeamSelection.Player7 && TeamSelection.TeamVCapt != TeamSelection.Player8
                    && TeamSelection.TeamVCapt != TeamSelection.Player9 && TeamSelection.TeamVCapt != TeamSelection.Player10
                    && TeamSelection.TeamVCapt != TeamSelection.Player11))
                    {
                        return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "TeamVCapt is invalid, team will not be saved.");
                    }
                    var data = new FantasyCricketProvider().SaveDailyTeamSelection(TeamSelection);
                    if (data.Count == 1)
                        return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "team selection saved successful.");
                    else
                        return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "team selection did not save.");
                }
                else
                {
                    return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "TeamCapt is invalid, team will not be saved.");
                }

            }
            else // Minimum Fields not provided, reject it...
            {

                ReturnValue = JsonResponse(HttpStatusCode.InternalServerError, ResponseStatus.Fail, "All eleven players(and captain, vice captain) were not selected.");
            }


            return ReturnValue;
        }

        [Route("api/fantasycricket/admin-insert-daily-match-details"), HttpPost()]
        public JsonResponse AdminInsertDailyMatchDetails(AdminGenericModel match)
        {
            var data = new FantasyCricketProvider().AdminInsertDailyMatchDetails(match);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Insert match details successfully");

        }

        [Route("api/fantasycricket/admin-update-daily-match-details"), HttpPost()]
        public JsonResponse AdminUpdateDailyMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminUpdateDailyMatchDetails(match);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/admin-delete-daily-match-details"), HttpPost()]
        public JsonResponse AdminDeleteDailyMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminDeleteDailyMatchDetails(match);
            if (data.Count == 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Match delete successfully");
            else
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error on delete");
        }

        [Route("api/fantasycricket/admin-fetch-daily-match-details"), HttpPost()]
        public JsonResponse AdminFetchDailyMatchDetails(AdminMatchDetailsModel match)
        {
            var data = new FantasyCricketProvider().AdminFetchDailyMatchDetails(match);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failure while fetching");
        }

        [Route("api/fantasycricket/admin-fetch-daily-players"), HttpPost()]
        public JsonResponse AdminFetchDailyPlayers(AdminPlayerModel players)
        {
            var data = new FantasyCricketProvider().AdminFetchDailyPlayers(players);
            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Failure while fetching");
        }

        [Route("api/fantasycricket/set-players-status"), HttpPost()]
        public JsonResponse SetPlayersStatus(AdminGenericModel players)
        {
            var data = new FantasyCricketProvider().SetPlayersStatus(players);

            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");
            
        }

        [Route("api/fantasycricket/set-matches-weekly-status"), HttpPost()]
        public JsonResponse SetMacthesWeeklyStatus(AdminGenericModel players)
        {
            var data = new FantasyCricketProvider().SetMacthesWeeklyStatus(players);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/reset-daily-team-player-points"), HttpPost()]
        public JsonResponse ResetDailyTeamPlayerPoints(AdminPlayerModel players)
        {
            if(players.TournamentId != -99 && players.TournamentId != 100)
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "failure - reset player points is only for daily games.");

            var data = new FantasyCricketProvider().ResetDailyTeamPlayerPoints(players);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-tournament-list"), HttpPost()]
        public JsonResponse DailyTournamentList(GenericModel User)
        {
            var data = new FantasyCricketProvider().DailyTournamentList(User);

            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-unique-team-list"), HttpPost()]
        public JsonResponse DailyUniqueTeamList(AdminGenericModel team)
        {
            var data = new FantasyCricketProvider().DailyUniqueTeamList(team);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/fetch-match-score"), HttpPost()]
        public JsonResponse FetchMatchScore(MatchModel Match)
        {
            var data = new FantasyCricketProvider().FetchMatchScore(Match);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/daily-live-league-users"), HttpPost()]
        public JsonResponse DailyLiveLeagueUsers(GenericModel details)
        {
            var data = new FantasyCricketProvider().DailyLiveLeagueUsers(details);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request Successful.");

        }

        [Route("api/fantasycricket/calc-weekly-reward"), HttpPost()]
        public JsonResponse CalcWeeklyReward(Reward details)
        {
            var data = new FantasyCricketProvider().CalcWeeklyReward(details);
            if (data.Count > 0)
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Reward calculation successful.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "Error on Reward calculation.");

        }

        [Route("api/fantasycricket/fetch-reward-week"), HttpPost()]
        public JsonResponse FetchRewardWeek(Reward details)
        {
            var data = new FantasyCricketProvider().FetchRewardWeek(details);

            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful.");

        }

        [Route("api/fantasycricket/delete-reward-week"), HttpPost()]
        public JsonResponse DeleteRewardWeek(Reward details)
        {
            var data = new FantasyCricketProvider().DeleteRewardWeek(details);

            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "deleted successful.");

        }

        [Route("api/fantasycricket/fetch-reward-week-details"), HttpPost()]
        public JsonResponse FetchRewardWeekDetails(Reward details)
        {
            var data = new FantasyCricketProvider().FetchRewardWeekDetails(details);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful.");

        }

        [Route("api/fantasycricket/weekly-reward-processed"), HttpPost()]
        public JsonResponse WeeklyRewardProcessed(Reward details)
        {
            var data = new FantasyCricketProvider().WeeklyRewardProcessed(details);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Reward processed successful.");

        }

        [Route("api/fantasycricket/update-match-player-ids"), HttpPost()]
        public JsonResponse UpdateMatchPlayerIds(MatchModel details)
        {
            var data = new FantasyCricketProvider().UpdateMatchPlayerIds(details);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "match player id update successful.");

        }

        [Route("api/fantasycricket/fetch-wallet-info"), HttpPost()]
        public JsonResponse FetchWalletInfo(UserModel user)
        {
            var data = new FantasyCricketProvider().FetchWalletInfo(user);

            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "wallet request successful.");

        }

        [Route("api/fantasycricket/sync-referral-code"), HttpPost()]
        public JsonResponse SyncReferralCode(UserModel user)
        {
            var data = new FantasyCricketProvider().SyncReferralCode(user);

            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful.");

        }

        [Route("api/fantasycricket/get-fun-fact"), HttpPost()]
        public JsonResponse GetFunFact(FunFactModel fun)
        {
            var data = new FantasyCricketProvider().GetFunFact(fun);

            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successful.");

        }

        [Route("api/fantasycricket/add-fun-fact"), HttpPost()]
        public JsonResponse AddFunFact(FunFactModel fun)
        {
            var data = new FantasyCricketProvider().AddFunFact(fun);

            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "fun added successfully.");

        }

        [Route("api/fantasycricket/fetch-total-rewards"), HttpPost()]
        public JsonResponse FetchTotalRewards(UserModel user)
        {
            var data = new FantasyCricketProvider().FetchTotalRewards(user);

            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "total rewards fetch successfull.");

        }

        [Route("api/fantasycricket/fetch-total-claims"), HttpPost()]
        public JsonResponse FetchTotalClaims(UserModel user)
        {
            var data = new FantasyCricketProvider().FetchTotalClaims(user);

            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "total rewards fetch successfull.");

        }

        [Route("api/fantasycricket/calculate-referral-reward"), HttpPost()]
        public JsonResponse CalculateReferralReward()
        {
            var data = new FantasyCricketProvider().CalculateReferralReward();

            if(data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Referral award allocation is successfull.");
            else
                return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "There is no referral award to calculate.");

        }

        [Route("api/fantasycricket/calculate-tournament-reward"), HttpPost()]
        public JsonResponse CalculateTournamentReward(UserTeamModel user)
        {
            var data = new FantasyCricketProvider().CalculateTournamentReward(user);

            if (data.Count > 0)
                return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Tournament award calculation is successfull.");
            else
                return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Fail, "There is no tournament to calculate.");

        }

        [Route("api/fantasycricket/download-reward-details"), HttpPost()]
        public JsonResponse DownloadRewardDetails()
        {
            var data = new FantasyCricketProvider().DownloadRewardDetails(); 
           return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Reward details downloaded successfully.");
        }

        [Route("api/fantasycricket/view-reward-details"), HttpPost()]
        public JsonResponse ViewRewardDetails()
        {
            var data = new FantasyCricketProvider().ViewRewardDetails();
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Fetch Reward details successfully.");
        }

        [Route("api/fantasycricket/upload-claim-details"), HttpPost()]
        public JsonResponse UploadClaimDetails(AdminGenericModel claims)
        {
            var data = new FantasyCricketProvider().UploadClaimDetails(claims);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Claim details uploaded successfully.");
        }

        [Route("api/fantasycricket/fetch-referral-code"), HttpPost()]
        public JsonResponse FetchReferralCode(UserTeamModel user)
        {
            var data = new FantasyCricketProvider().FetchReferralCode(user);
            return JsonResponse(data,HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Request successfully.");
        }

        [Route("api/fantasycricket/admin-hide-score-card"), HttpPost()]
        public JsonResponse AdminHideScoreCard(MatchModel match)
        {
            var data = new FantasyCricketProvider().AdminHideScoreCard(match);
            return JsonResponse( HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "Match Score hidden successfully.");
        }

        [Route("api/fantasycricket/sync-rapid-match-players"), HttpPost()]
        public JsonResponse SyncRapidMatchPlayers(RapidMatchPlayers match)
        {
            var data = new FantasyCricketProvider().SyncRapidMatchPlayers(match);
            return JsonResponse(HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "successfull.");
        }

        [Route("api/fantasycricket/team-points-comparison"), HttpPost()]
        public JsonResponse TeamPointsComparison(GenericModel teams)
        {
            var data = new FantasyCricketProvider().TeamPointsComparison(teams);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successfull.");
        }

        [Route("api/fantasycricket/live-team-score-comparison"), HttpPost()]
        public JsonResponse LiveTeamScoreComparison(GenericModel teams)
        {
            var data = new FantasyCricketProvider().LiveTeamScoreComparison(teams);
            return JsonResponse(data, HttpStatusCode.OK, LPL.UI.REST.Core.Entities.ResponseStatus.Success, "request successfull.");
        }
        #endregion
    }
}