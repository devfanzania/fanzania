using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Fantacy_Model;
using System.Configuration;
using RestSharp;
using System.IO;
using System.Net;


namespace FantacyWeb.Models
{
    public class AccountRestService
    {
        private readonly RestClient _client;
        private readonly Uri _url = new Uri(ConfigurationManager.AppSettings["FantacyApiBaseUrl"]);

        public AccountRestService()
        {
            _client = new RestClient { BaseUrl = _url };
        }
        public Response ValidateUserLogin(LoginModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/sign-in", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<Response>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DefaultResponse ForgotPassword(LoginModel lModel)
        {
            var request = new RestRequest("api/fantasycricket/forget-password", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(lModel);
            request.AddHeader("x-api-authtoken", "");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public CountryResponse CountryList()
        {
            var request = new RestRequest("api/fantasycricket/country-list", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<CountryResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public ListOfTeamResponse ListOfTeam(TournamentModel model)
        {
            var request = new RestRequest("api/fantasycricket/distinct-team-filter", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(model);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<ListOfTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public DefaultResponse PostFeedback(FeedbackModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/user-feedback", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public ProfileResponse FetchProfile(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-profile", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<ProfileResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        
        public ProfileUploadResponse UploadProfilePic(ProfileModel pModel)
        {
            MemoryStream target = new MemoryStream();
            pModel.UploadedImage.InputStream.CopyTo(target);
            byte[] fdata = target.ToArray();
            //var request = new RestRequest("api/fantasycricket/upload-profile-image?UserId=" + pModel.UserId, Method.POST);
            var request = new RestRequest("api/fantasycricket/upload-profile-image", Method.POST);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
          // request.AddHeader("Content-Type", "multipart/form-data");
            request.AddFile("UploadedImage", fdata,pModel.UploadedImage.FileName);


            var response = _client.Execute<ProfileUploadResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public KycUploadResponse FetchKycDetails(KycUpload pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-kyc-details", Method.POST) { RequestFormat = DataFormat.Json };
             request.AddBody(pModel);
             request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
             request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<KycUploadResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
             throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public UpdateKycStatusResponce UpdateKycStatus(updatekycstatus pmodel)
        {
            var request = new RestRequest("api/fantasycricket/update-kyc-status", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<UpdateKycStatusResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public UpdateKycStatusResponce sendverificationcode(updatekycstatus pmodel)
        {
            var request = new RestRequest("api/fantasycricket/send-verification-code", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<UpdateKycStatusResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        

        public featchbankdetailsresponce fetchbankdetails(SubscriptionDetails pmodel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-bank-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<featchbankdetailsresponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public updatebankdetailsresponce updatebankdetails(updatebankdetails pmodel)
        {
            var request = new RestRequest("api/fantasycricket/update-bank-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<updatebankdetailsresponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public updatebankdetailsresponce transferfunds(transferfundsdetails pmodel)
        {
            var request = new RestRequest("api/fantasycricket/transfer-funds", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<updatebankdetailsresponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public verifyotpResponce verifyotp(verifyotpmodel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/verify-otp", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
             request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-userid", pmodel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<verifyotpResponce>(request);
            //   if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
            //throw new Exception(response.ErrorMessage);
      //      if (response.StatusCode != System.Net.HttpStatusCode.OK)
          //      throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public KycUploadResponse KycUploadProfilePic(KycUpload pModel)
        {
            MemoryStream target = new MemoryStream();
            pModel.UploadedImage.InputStream.CopyTo(target);
            byte[] fdata = target.ToArray();
            //var request = new RestRequest("api/fantasycricket/upload-profile-image?UserId=" + pModel.UserId, Method.POST);
            var request = new RestRequest("api/fantasycricket/upload-profile-image", Method.POST);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("kyc-doc", "PAN");
            request.AddHeader("x-api-devicetype", "web");
           
            request.AddFile("UploadedImage", fdata, pModel.UploadedImage.FileName);


            var response = _client.Execute<KycUploadResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public ProfileUploadResponse UploadProfile(ProfileModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/save-profile", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<ProfileUploadResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public KycUploadResponse UpdateKycDetails(KycUploadModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/update-kyc-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<KycUploadResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public ChangePassResponse UpdatePassword(ChangePassDataModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/save-new-password", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<ChangePassResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public Response UserSignup(RegistrationModel pModel)
        {
            var passPhraseReg = "amaf7LLSWhN@#r5!*";
            var CurrentEpoch = (int)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalSeconds;
            var authToken = ECDC.Encrypt("Auth=" + CurrentEpoch.ToString(), passPhraseReg);

            var request = new RestRequest("api/fantasycricket/sign-up", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-devicetype", "web");
            request.AddHeader("x-api-authorization", authToken);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<Response>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public DefaultResponse Verify_User(RegistrationModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/verify-user", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "999304CF-C526-40CA-AB32-D2FAEEC54F53");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public EmailResponse EmailVerify(ParamModel eModel)
        {
            var request = new RestRequest("api/fantasycricket/email-verification-code", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(eModel);
            request.AddHeader("x-api-authtoken", eModel.authtoken);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<EmailResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public VerifiedResponse EmailVerified(ParamModel eModel)
        {
            var request = new RestRequest("api/fantasycricket/email-verified", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(eModel);
            request.AddHeader("x-api-authtoken", eModel.authtoken);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<VerifiedResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public Response ExternalLogin(LoginModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/external-sign-in", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<Response>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public Response LogOut(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/log-out", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<Response>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TournamentResponse UserUpcomingTtournament(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/user-upcoming-tournament", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", uModel.authtoken);
            request.AddHeader("x-api-userid", uModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TournamentResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public UserTournamentResponse GetUserTournament(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/user-tournament-details", Method.POST) { RequestFormat = DataFormat.Json };
            
            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", uModel.authtoken);
            request.AddHeader("x-api-userid", uModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UserTournamentResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse UserActiveLeagueInfo(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/user-all-leagues", Method.POST) { RequestFormat = DataFormat.Json };

            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", uModel.authtoken);
            request.AddHeader("x-api-userid", uModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueTeamDetailResponse UserActiveLeagueDetails(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/league-teams", Method.POST) { RequestFormat = DataFormat.Json };

            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", uModel.authtoken);
            request.AddHeader("x-api-userid", uModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueTeamDetailResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse TournamentWisePlayerList(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/get-tournament-players", Method.POST) { RequestFormat = DataFormat.Json };

            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public MatchResponse MatchDetails(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/all-matches", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<MatchResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public MatchResponse AllFutureMatches(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/all-future-matches", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<MatchResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public TeamFilterResponse FilterTeam(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/distinct-team-filter", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamFilterResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public TeamResponse UserTeam(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public UserTeamResponse UserTeamPlayer(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UserTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse UserTeamPlayer_LastCutOff(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-players-last-cutoff", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse TeamPlayerDetails_(ParamPlayerModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/player-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse TeamPlayerDetails(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-players-with-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public PlayerResponse TeamPlayerDetails_CompleteMatch(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-match-details-with-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public PlayerResponse TeamPlayerDetails_CompleteMatch_New(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-players-details-with-powerplay", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse CurrentTeamPlayerDetails(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-team-players-details-with-stealthmode", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public PlayerResponse LastPlayedTeamPlayerDetails(ParamModel pModel)
        {
            
            var request = new RestRequest("api/fantasycricket/user-team-match-details-with-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueSubResponse fetchleaguesubscription(ParamModel pModel)
        {

            var request = new RestRequest("api/fantasycricket/fetch-league-subscription", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueSubResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }



        public UserTeamResponse Save_Players(UserTeamDataResponse pModel)
        {
            var request = new RestRequest("api/fantasycricket/save-team-selection", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UserTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public PlayerResponse AutoSelection_Team(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/auto-select-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TeamRulesResponse PlayerSelectionRules(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/team-selection-rule", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamRulesResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public ResponseModel CheckAvailable(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/verify-team-name", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<ResponseModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public CreateTeamResponse CreateTeam(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/create-user-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<CreateTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public CreateTeamResponse ChangeTeamName(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/modify-user-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<CreateTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public UpdatePowerPlayModelResp UpdateUserPowerplay(UpdatePowerPlayModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/update-user-powerplay", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
          //  request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UpdatePowerPlayModelResp>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public UpdatePowerPlayModelRespFeach FetchUserPowerPlay(FetchUserPowerPlay pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-user-powerplay", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //  request.AddHeader("x-api-authtoken", pModel.authtoken);
           // request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UpdatePowerPlayModelRespFeach>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse ResetShareCode(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/reset-league-pin", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse VerifyLeagueName(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/verify-league", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse CreateLague(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/create-league", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DefaultResponse Unapprove_league_users(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/unapprove-league-users", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        
        public DefaultResponse Approve_league_users(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/approve-league-users", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse Exit_League(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/exit-league", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public LeagueResponse Join_League(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/join-league", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LeagueResponse ChangeLeagueName(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/change-league-name", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }


        // Ststs api------------------------------------------------------------------------------------
        public LeagueStat Team_Top_Performer(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/league-stats-top-teams-top-perform", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueStat>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public LeagueStat Team_Top_Preferred_Players(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/league-stats-top-teams-top-favorite", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LeagueStat>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public TeamStat Top_Ten_Player(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-stats-user-top-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamStat>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TeamStat Recent_Match_Captain_Usage(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-stats-captain-points", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamStat>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public GlobalTopPlayer GlobalTopPlayers(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-stats-global-top-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<GlobalTopPlayer>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public GlobalTopLeague GlobalTopLeagues(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/league-stats-global-top-leagues", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<GlobalTopLeague>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public GlobalTopTeam GlobalTopTeams(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-stats-global-top-teams", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<GlobalTopTeam>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        // -------------------------------- Live Score -------------------------------------------------------

        public UserTournamentResponse InprogressTournament(ParamModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/live-tournament-details", Method.POST) { RequestFormat = DataFormat.Json };

            request.AddBody(uModel);
            request.AddHeader("x-api-authtoken", uModel.authtoken);
            request.AddHeader("x-api-userid", uModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<UserTournamentResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public MatchResponse LiveMatchDetails(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-matches", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<MatchResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TeamPointsC TeamPointsComparison(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/team-points-comparison", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TeamPointsC>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LiveTeamScore LiveTeamScoreComparison(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-team-score-comparison", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LiveTeamScore>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public MatchScore LiveMatchScore(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-match-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<MatchScore>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public liveleagueUser LiveLeagueUser(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-league-users", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<liveleagueUser>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public LiveScore LiveScore(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-user-team-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LiveScore>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LiveScoreBoardT LiveScoreBoard(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-match-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LiveScoreBoardT>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public MatchResult TopTenUser(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/last-match-top-performer-user", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<MatchResult>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        // ------------------------ Wallet ------------------------------------------

        public WalletModelResponse FetchWalletInfo(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-wallet-info", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<WalletModelResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TotalRewardModelResponse FetchTotalRewards(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-total-rewards", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TotalRewardModelResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public TotalClaimModelResponse FetchTotalClaims(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-total-claims", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<TotalClaimModelResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public NotificationResponse GetNotificationCount(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-notification-count", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<NotificationResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public NotificationResponse GetNotificatons(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-notification-message", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<NotificationResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
     
        public DefaultResponse AckNotificaton(NotificationData pModel)
        {
            var request = new RestRequest("api/fantasycricket/ack-notification-message", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DefaultResponseLoginP SaveLoginPreference(SaveLoginPreferenceData pModel)
        {
            var request = new RestRequest("api/fantasycricket/save-login-preference", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponseLoginP>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public DefaultResponseSubscriptionDetails GetSubscriptionDetails(SubscriptionDetails pModel)
        {
            var request = new RestRequest("api/fantasycricket/get-subscription-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponseSubscriptionDetails>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public SubResponce UpdateSubscriptionDetails(UpdateSub pModel)
        {
            var request = new RestRequest("api/fantasycricket/update-subscription-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<SubResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public SubResponce UpdateLeagueSubscription(List<LeagueSub> pModel)
        {
            var request = new RestRequest("api/fantasycricket/update-league-subscription", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<SubResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        // ------------------------ DashBoard Top Fact ------------------------------------------

        public GetFunFact GetFunFact()
        {
            var request = new RestRequest("api/fantasycricket/get-fun-fact", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<GetFunFact>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public FetchReferralCode FetchReferralCode(ReferralCodeModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-referral-code", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<FetchReferralCode>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DefaultResponseGetWayDetails FetchPaymentGatewayDetails(PaymentGetwayDetails pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-payment-gateway-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DefaultResponseGetWayDetails>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AddMoneyPaymentdetails AddMoneyPaymentdetails(OrderDetails pModel)
        {
            var request = new RestRequest("api/fantasycricket/add-money", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            //request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<AddMoneyPaymentdetails>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

    }
}