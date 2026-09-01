using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Fantacy_Model;
using System.Configuration;
using RestSharp;
using System.IO;

namespace FantacyWeb.Models
{
    public class AdminAccountRestService
    {
        private readonly RestClient _client;
        private readonly Uri _url = new Uri(ConfigurationManager.AppSettings["FantacyApiBaseUrl"]);

        public AdminAccountRestService()
        {
            _client = new RestClient { BaseUrl = _url };
        }
        public Response ValidateAdminLogin(AdminLoginModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/admin-sign-in", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<Response>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public UserTournamentResponse GetTournament(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-tournament", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<UserTournamentResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public FetchManualScoreResponce FetchManualScore(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-manual-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<FetchManualScoreResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public ManualScoreUpdateEachPlayerResponce ManualScoreUpdateEachPlayer(UpdatePointModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/manual-score-update-each-player", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<ManualScoreUpdateEachPlayerResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public UpdateKycStatusResponce UpdateKycStatus(FetchKycDetailsAdmin pmodel)
        {
            var request = new RestRequest("api/fantasycricket/update-kyc-status", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<UpdateKycStatusResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public ManualScoreUpdateEachPlayerResponce SetManualMom(UpdatePointModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/set-manual-mom", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<ManualScoreUpdateEachPlayerResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SaveTournament(TournamentMainModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-tournament", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SaveTournamentRule(TournamentRuleModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-tournament-team-rules", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SaveTournamentPoint(TournamentPointModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-tournament-point-rules", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminTournamentRuleResponse GetTournamentRule(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/team-selection-rule", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTournamentRuleResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminTournamentPointModel GetTournamentPoint(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-tournament-point-rules", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTournamentPointModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse DeleteTournament(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-tournament", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse CalculatePoint(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/trigger-point-calculation", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse UpdateUser(AdminModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-user-promote", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public SubResponceadmin UpdateSubscriptionDetails(UpdateSubadmin pmodel)
        {
            var request = new RestRequest("api/fantasycricket/update-subscription-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<SubResponceadmin>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public DetailsUpdateUserPoints adminupdateuserpoints(UpdateUserPoints pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-user-points", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<DetailsUpdateUserPoints>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse AddFunFact(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/add-fun-fact", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SendNotification(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/send-notification-message", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SaveMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-insert-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SaveDailyMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-insert-daily-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse UpdateMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse UpdateDailyMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-daily-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminMatchModel GetSaveMatch(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminMatchModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminMatchModel GetSaveDailyMatch(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-daily-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminMatchModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminMatchModel GetMatchOnStart(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-match-onstart", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminMatchModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminTournamentRuleResponse MatchStart(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/start-match", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTournamentRuleResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminTournamentRuleResponse SwitchBattingTeam(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-switch-batting-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTournamentRuleResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse DeleteMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SyncMatch(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-sync-match-apikey", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse DeleteDailyMatch(AdminMatchDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-daily-match-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminPlayerModel GetSavePlayer(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-player-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminPlayerModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminPlayerModel GetDailySavePlayer(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-daily-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminPlayerModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SavePlayer(AdminPlayerDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-upload-player-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse ActiveInactive(AdminParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/set-players-status", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse UpdatePlayer(AdminPlayerDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-player-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse DeletePlayer(AdminPlayerDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-player-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminTeamModel GetSaveTeam(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-participation-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTeamModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SaveTeam(AdminTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-upload-participation-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse UpdateTeam(AdminTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-participation-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse DeleteTeam(AdminTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-participation-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse ResetPlayerPoints(AdminTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/reset-daily-team-player-points", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminAutoTeamModel GetAutoTeam(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-fetch-auto-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminAutoTeamModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SaveAutoTeam(AdminAutoTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-update-auto-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.Timeout = 600000;
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse DeleteAutoTeam(AdminAutoTeamDetailsModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-delete-auto-team", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SetTransferCount(AdminParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/admin-reset-transfer", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public ResetSub ResetSubscription(AdminParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/reset-subscription", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<ResetSub>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminTeamModel GetTeam_DailyMatch(ParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-unique-team-list", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminTeamModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public GlobalTopTeam GlobalTopTeams(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/user-stats-global-top-teams", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<GlobalTopTeam>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public GlobalTopLeague GlobalTopLeagues(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/league-stats-global-top-leagues", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<GlobalTopLeague>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse CalcReferralReward()
        {
            var request = new RestRequest("api/fantasycricket/calculate-referral-reward", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse CalcTournamentReward(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/calculate-tournament-reward", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public RewardResponseModel ShowRewardList()
        {
            var request = new RestRequest("api/fantasycricket/view-reward-details", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<RewardResponseModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public RewardResponseModel DownloadRewardList()
        {
            var request = new RestRequest("api/fantasycricket/download-reward-details", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<RewardResponseModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse UploadClaim(UploadClaimModel uModel)
        {
            var request = new RestRequest("api/fantasycricket/upload-claim-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(uModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }


        public AdminResponse ManualPointCalculation(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/set-manual-point-calc", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse TossWinner(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/set-toss-winner", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse HideScore(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/admin-hide-score-card", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", pModel.authtoken);
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public AdminResponse SyncMatchRapid(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/admin-sync-matchid", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SyncTeamRapid(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/admin-sync-teamid", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public AdminResponse SyncPlayerRapid(ParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/admin-sync-team-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            var response = _client.Execute<AdminResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public FetchKycDetailsAdminResponce FetchKycDetails(FetchKycDetailsAdmin pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-kyc-details", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<FetchKycDetailsAdminResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK && response.StatusCode != System.Net.HttpStatusCode.SeeOther)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

    }
}