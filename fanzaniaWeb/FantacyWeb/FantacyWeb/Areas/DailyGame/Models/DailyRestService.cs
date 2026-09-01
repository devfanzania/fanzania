using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Fantacy_Model;
using RestSharp;
using System.Configuration;
using Fantacy_Model.DailyGame;

namespace FantacyWeb.Areas.DailyGame.Models
{
    public class DailyRestService
    {
        private readonly RestClient _client;
        private readonly Uri _url = new Uri(ConfigurationManager.AppSettings["FantacyApiBaseUrl"]);
        public DailyRestService()
        {
            _client = new RestClient { BaseUrl = _url };
        }
        public DailyMatchModel GetAllDailyMatch(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/user-upcoming-daily-matches", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyMatchModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyMatchModel GetDailyMyMatch(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/user-daily-matches", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyMatchModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyLeagueTeamModel DailyLeagueTeams(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-league-teams", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyLeagueTeamModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyUserPlayerModel UserDailyTeamPlayers(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/user-daily-team-players", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyUserPlayerModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyUserPlayerModel UserDailyTeamPlayersWithPoints(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/user-daily-team-players-with-points", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyUserPlayerModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyTournament DailyTournamentList(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-tournament-list", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyTournament>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public DailyTeamRulesResponse PlayerSelectionRules(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-matches-team-selection-rules", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyTeamRulesResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyUserPlayerModel DailyMatchesPlayerList(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-matches-player-list", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyUserPlayerModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public PlayerResponce FetchPlayerStats(FeachPlayerModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-player-stats", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
           // request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<PlayerResponce>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
      

        public DailyUserTeamResponse Save_Players(DailyUserTeamDataResponse pModel)
        {
            var request = new RestRequest("api/fantasycricket/save-daily-team-selection", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", pModel.authtoken);
            request.AddHeader("x-api-userid", pModel.UserId);
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyUserTeamResponse>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyLiveLeagueUsersModel DailyLiveLeagueUsers(DailyParamModel pmodel)
        {
            var request = new RestRequest("api/fantasycricket/daily-live-league-users", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyLiveLeagueUsersModel>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyMatchScore LiveMatchScore(DailyParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-match-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyMatchScore>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public DailyLiveScore LiveScore(DailyParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/live-user-team-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<DailyLiveScore>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
        public LiveScoreBoard LiveScoreBoard(DailyParamModel pModel)
        {
            var request = new RestRequest("api/fantasycricket/fetch-match-score", Method.POST) { RequestFormat = DataFormat.Json };
            request.AddBody(pModel);
            request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<LiveScoreBoard>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }

        public GetGunFact GetGunFact()
        {
            var request = new RestRequest("api/fantasycricket/get-fun-fact", Method.POST) { RequestFormat = DataFormat.Json };
            //request.AddBody(pmodel);
            //request.AddHeader("x-api-authtoken", "B087BD51-3425-4BB2-B1F0-119EDB987346");
            request.AddHeader("x-api-devicetype", "web");
            var response = _client.Execute<GetGunFact>(request);
            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new Exception(response.ErrorMessage);
            return response.Data;
        }
    }
}