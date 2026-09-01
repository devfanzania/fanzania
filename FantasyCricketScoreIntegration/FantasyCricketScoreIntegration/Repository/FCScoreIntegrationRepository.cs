# region "Using Directives"
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Configuration;
using FantasyCricketAppRest.DBHelpers;
using FantasyCricketScoreIntegration.Models;

using Newtonsoft.Json;
using System.Data;
using System.Data.SqlClient;
using System.Dynamic;
using System.Linq;
using System.Web;
using System.Globalization;
using Common.Logging.Entities;
using Common.Logger;
using FantasyCricketAppRest.CommonUtilities;
using RestSharp;
using System.Net;
using log4net.Repository.Hierarchy;
using Newtonsoft.Json.Linq;

# endregion "Using Directives"

namespace FantasyCricket.Score.Repository
{
    public sealed class IntegrationScoreRepository
    {
        #region > Private Local Variables <
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"].ToString();
        private static string _AppName = ConfigurationManager.AppSettings["AppName"].ToString();
        private static string _Filename = "FCScoreIntegrationRepository.cs";
        private static string APIBaseUrl = ConfigurationManager.AppSettings["APIBaseUrl"].ToString();
        private static string APIKey = ConfigurationManager.AppSettings["APIKey"].ToString();
        private static string project_key = ConfigurationManager.AppSettings["project_key"].ToString();
        private static bool _IsTestCase;

        #endregion

        #region > Properties <
        public static bool IsTestCase
        {
            get { return _IsTestCase; }
            set { _IsTestCase = value; }
        }


        #endregion

        #region > Internal Methods <



        internal List<FantasySummaryAPIDetails> GetMatchData(string tranId)
        {
           
            var parameters = new Dictionary<string, object>() {
                { "@TriggerVal", "data" }
                };

            List<FantasySummaryAPIDetails> responseDictonary = new List<FantasySummaryAPIDetails>();
            responseDictonary = DatabaseHelper.GetMatchData("FANTASYCRICKET..GetMatchData", parameters);
            
            return responseDictonary.Cast<FantasySummaryAPIDetails>().ToList();
        }

        //this fundtion will generate auth token from api.sports.roanuz.com REST end point
        internal string GenerateAuthToken()
        {
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;

            string rs_token = "";
            string url = APIBaseUrl + "/core/" + project_key + "/auth/";
            
            var client = new RestClient(url);
            var request = new RestRequest(Method.POST);
            request.AddHeader("Content-Type", "application/json");
            request.AddHeader("Accept", "application/json");
            request.AddParameter("application/json", "{\"api_key\": \"" + APIKey + "\" }", ParameterType.RequestBody);
            IRestResponse response = client.Execute(request);

            Dictionary<string, dynamic> response_value = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);

            rs_token = (response_value["data"]["token"] != null) ? response_value["data"]["token"].Value : "";

            return rs_token;
        }



        internal List<MatchDetails4PointCalculation> GetMatchDetails(string tranId)
        {

            var parameters = new Dictionary<string, object>() {
                { "@TriggerValue", "CALCULATEPOINT" }
            };
            // var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..MatchDetails4PointCalculation", parameters);
            string storedProcName = "FANTASYCRICKET..MatchDetails4PointCalculation";
            List<MatchDetails4PointCalculation> responseDictonary = new List<MatchDetails4PointCalculation>();

            using (SqlConnection connection = new SqlConnection(DbConnectionHelper.GetSqlConnection()))
            {
                connection.Open();
                using (SqlCommand cmd = new SqlCommand(storedProcName, connection))
                {
                    try
                    {
                        cmd.CommandType = System.Data.CommandType.StoredProcedure;
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }

                        using (SqlDataReader rs = cmd.ExecuteReader())
                        {
                            while (rs.Read())
                            {
                                MatchDetails4PointCalculation data = new MatchDetails4PointCalculation();
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.MatchStage = rs["MatchStage"].ToString();
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "GetMatchDetails_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "MatchDetails4PointCalculation";
                        error.TransactionId = tranId;
                        error.TransactionType = "BATCHJOB - MatchDetails4PointCalculation";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<MatchDetails4PointCalculation>().ToList();
        }

        internal bool UpdatePlayerStats(MatchDetails4PlayerStatsModel match, string tranId)
        {
            List<FetchPlayerDetails4StatsModel> playerDetails = new IntegrationScoreRepository().FetchPlayerDetails4Stats(match,tranId);

            foreach (var player in playerDetails)
            {
                PlayerStatsModel plStatsData = new PlayerStatsModel();
                
                if (player.PlayerStats.Length > 5)
                {
                    plStatsData = JsonConvert.DeserializeObject<PlayerStatsModel>(player.PlayerStats);
                    plStatsData.TournamentId = match.TournamentId;
                    plStatsData.TournamentName = match.TournamentName;
                    plStatsData.TeamShortName = player.TeamShortName;
                    plStatsData.PlayerValue = player.PlayerValue;
                    plStatsData.PlayerTotalPoints = player.TotalPoints;
                    plStatsData.PlayerRank = player.PlayerRank;
                    plStatsData.LastMatchId = match.MatchId;
                    plStatsData.PlayerValueRank = player.PlayerValueRank;
                    if(plStatsData.MatchCounter == 1)
                    {
                        plStatsData.MatchCounter += 1;
                        plStatsData.PlayerPoints2 = player.PlayerPoints.ToString();
                        plStatsData.PlayerRuns2 = player.PlayerRuns.ToString();
                        plStatsData.PlayerWickets2 = player.PlayerWickets.ToString();
                        
                    }
                    else if(plStatsData.MatchCounter == 2)
                    {
                        plStatsData.MatchCounter += 1;
                        plStatsData.PlayerPoints3 = player.PlayerPoints.ToString();
                        plStatsData.PlayerRuns3 = player.PlayerRuns.ToString();
                        plStatsData.PlayerWickets3 = player.PlayerWickets.ToString();
                        
                    }
                    else if (plStatsData.MatchCounter == 3)
                    {
                        plStatsData.MatchCounter += 1;
                        plStatsData.PlayerPoints4 = player.PlayerPoints.ToString();
                        plStatsData.PlayerRuns4 = player.PlayerRuns.ToString();
                        plStatsData.PlayerWickets4 = player.PlayerWickets.ToString();
                        
                    }
                    else if (plStatsData.MatchCounter == 4)
                    {
                        plStatsData.MatchCounter += 1;
                        plStatsData.PlayerPoints5 = player.PlayerPoints.ToString();
                        plStatsData.PlayerRuns5 = player.PlayerRuns.ToString();
                        plStatsData.PlayerWickets5 = player.PlayerWickets.ToString();
                    }
                    else
                    {
                        //left shift data
                        plStatsData.MatchCounter += 1;
                        plStatsData.PlayerPoints1 = plStatsData.PlayerPoints2;
                        plStatsData.PlayerPoints2 = plStatsData.PlayerPoints3;
                        plStatsData.PlayerPoints3 = plStatsData.PlayerPoints4;
                        plStatsData.PlayerPoints4 = plStatsData.PlayerPoints5;
                        plStatsData.PlayerPoints5 = player.PlayerPoints.ToString();

                        plStatsData.PlayerRuns1 = plStatsData.PlayerRuns2;
                        plStatsData.PlayerRuns2 = plStatsData.PlayerRuns3;
                        plStatsData.PlayerRuns3 = plStatsData.PlayerRuns4;
                        plStatsData.PlayerRuns4 = plStatsData.PlayerRuns5;
                        plStatsData.PlayerRuns5 = player.PlayerRuns.ToString();

                        plStatsData.PlayerWickets1 = plStatsData.PlayerWickets2;
                        plStatsData.PlayerWickets2 = plStatsData.PlayerWickets3;
                        plStatsData.PlayerWickets3 = plStatsData.PlayerWickets4;
                        plStatsData.PlayerWickets4 = plStatsData.PlayerWickets5;
                        plStatsData.PlayerWickets5 = player.PlayerWickets.ToString();
                    }
                    plStatsData.PlayerName = "";
                    plStatsData.PlayerSpeciality = "";
                    plStatsData.SelectedBy = 0;
                    plStatsData.TotalPlayers = 0;
                    plStatsData.ImageURL = "";
                }
                else
                {
                    plStatsData.TournamentId = match.TournamentId;
                    plStatsData.TournamentName = match.TournamentName;
                    plStatsData.PlayerName = "";
                    plStatsData.TeamShortName = player.TeamShortName;
                    plStatsData.PlayerSpeciality = "";
                    plStatsData.PlayerValue = player.PlayerValue;
                    plStatsData.PlayerTotalPoints = player.TotalPoints;
                    plStatsData.PlayerRank = player.PlayerRank;
                    plStatsData.SelectedBy = 0;
                    plStatsData.PlayerPoints1 = player.PlayerPoints.ToString();
                    plStatsData.PlayerPoints2 = "-";
                    plStatsData.PlayerPoints3 = "-";
                    plStatsData.PlayerPoints4 = "-";
                    plStatsData.PlayerPoints5 = "-";
                    plStatsData.PlayerRuns1 = player.PlayerRuns.ToString();
                    plStatsData.PlayerRuns2 = "-";
                    plStatsData.PlayerRuns3 = "-";
                    plStatsData.PlayerRuns4 = "-";
                    plStatsData.PlayerRuns5 = "-";
                    plStatsData.PlayerWickets1 = player.PlayerWickets.ToString();
                    plStatsData.PlayerWickets2 = "-";
                    plStatsData.PlayerWickets3 = "-";
                    plStatsData.PlayerWickets4 = "-";
                    plStatsData.PlayerWickets5 = "-";
                    plStatsData.PlayerValueRank = player.PlayerValueRank;
                    plStatsData.TotalPlayers = 0;
                    plStatsData.ImageURL = "";
                    plStatsData.MatchCounter = 1;
                    plStatsData.LastMatchId = match.MatchId;
                }

                string jsonStr = JsonConvert.SerializeObject(plStatsData);

                var param1 = new Dictionary<string, object>() {
                { "@APIPId", player.APIPId },
                { "@PlayerStats", jsonStr }
                };
                var data1 = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdatePlayerDetailsStats", param1);
            }

            var param = new Dictionary<string, object>() {
                { "@MatchId", match.MatchId }
            };
            var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdateMatchComplete", param);

            return true;
        }

        internal List<FetchPlayerDetails4StatsModel> FetchPlayerDetails4Stats(MatchDetails4PlayerStatsModel match,string tranId)
        {

            var parameters = new Dictionary<string, object>() {
                { "@MatchId", match.MatchId },
                { "@TournamentId", match.TournamentId }
            };
            
            string storedProcName = "FANTASYCRICKET..FetchPlayerDetails4Stats";
            List<FetchPlayerDetails4StatsModel> responseDictonary = new List<FetchPlayerDetails4StatsModel>();

            using (SqlConnection connection = new SqlConnection(DbConnectionHelper.GetSqlConnection()))
            {
                connection.Open();
                using (SqlCommand cmd = new SqlCommand(storedProcName, connection))
                {
                    try
                    {
                        cmd.CommandType = System.Data.CommandType.StoredProcedure;
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }

                        using (SqlDataReader rs = cmd.ExecuteReader())
                        {
                            while (rs.Read())
                            {
                                FetchPlayerDetails4StatsModel data = new FetchPlayerDetails4StatsModel();
                                data.APIPId = Int32.Parse(rs["APIPId"].ToString());
                                data.PlayerRank = Int32.Parse(rs["PlayerRank"].ToString());
                                data.PlayerPoints = Int32.Parse(rs["PlayerPoints"].ToString());
                                data.PlayerRuns = Int32.Parse(rs["PlayerRuns"].ToString());
                                data.PlayerWickets = Int32.Parse(rs["PlayerWickets"].ToString());
                                data.TotalPoints = Int32.Parse(rs["TotalPoints"].ToString());
                                data.PlayerValue = Int32.Parse(rs["PlayerValue"].ToString());
                                data.PlayerValueRank = Int32.Parse(rs["PlayerValueRank"].ToString());
                                data.PlayerId = Int32.Parse(rs["PlayerId"].ToString());
                                data.TeamShortName = rs["TeamShortName"].ToString();
                                data.PlayerStats = rs["PlayerStats"].ToString();
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "FetchPlayerDetails4Stats_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchPlayerDetails4Stats";
                        error.TransactionId = tranId;
                        error.TransactionType = "BATCHJOB - FetchPlayerDetails4Stats";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<FetchPlayerDetails4StatsModel>().ToList();
        }

        internal List<MatchDetails4PlayerStatsModel> GetMatchDetails4PlayerStats(string tranId)
        {

            var parameters = new Dictionary<string, object>() {
                { "@Action", "" }
            };
            // var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..MatchDetails4PointCalculation", parameters);
            string storedProcName = "FANTASYCRICKET..GetMatchDetails4PlayerStats";
            List<MatchDetails4PlayerStatsModel> responseDictonary = new List<MatchDetails4PlayerStatsModel>();

            using (SqlConnection connection = new SqlConnection(DbConnectionHelper.GetSqlConnection()))
            {
                connection.Open();
                using (SqlCommand cmd = new SqlCommand(storedProcName, connection))
                {
                    try
                    {
                        cmd.CommandType = System.Data.CommandType.StoredProcedure;
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }

                        using (SqlDataReader rs = cmd.ExecuteReader())
                        {
                            while (rs.Read())
                            {
                                MatchDetails4PlayerStatsModel data = new MatchDetails4PlayerStatsModel();
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.TournamentName = rs["TournamentName"].ToString();
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "GetMatchDetails4PlayerStats_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "GetMatchDetails4PlayerStats";
                        error.TransactionId = tranId;
                        error.TransactionType = "BATCHJOB - GetMatchDetails4PlayerStats";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<MatchDetails4PlayerStatsModel>().ToList();
        }

        internal bool InitiateMatchPlayerStats(FantasySummaryAPIDetails apiDetail)
        {
            StringBuilder selectQuery = new StringBuilder();
            int MatchId = apiDetail.MatchId;
            int TournamentId = apiDetail.TournamentId;
            string playerName = "";
            string playerRole = "";

            //string test = apiDetail.Inning1BattingTeam;
            //bool test1 = string.IsNullOrWhiteSpace(test);

            string playerShortName = "";
            selectQuery.Append(@"SELECT PlayerId FROM [FANTASYCRICKET].[DBO].[MatchPlayerStats](nolock) WHERE MatchId= @MatchId AND PlayerId IS NOT NULL");
            var param = new Dictionary<string, object>() {
                { "@MatchId", MatchId }
             };

            if (apiDetail.MatchStage == "D")
            {
                if (apiDetail.MatchType == "T20")
                    TournamentId = -99;
                else
                    TournamentId = -100;
            }
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            var data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param);
            if (data.Count < 30)
            {
                
                string rs_token = GenerateAuthToken();

                if (!string.IsNullOrWhiteSpace(rs_token))
                {
                    try
                    {
                        string url = APIBaseUrl + "/cricket/" + project_key + "/match/" + apiDetail.UniqueId + "/";

                        var client = new RestClient(url);
                        var request = new RestRequest(Method.GET);
                        request.AddHeader("Content-Type", "application/json");
                        request.AddHeader("Accept", "application/json");
                        request.AddHeader("rs-token", rs_token);
                        IRestResponse response = client.Execute(request);

                        Dictionary<string, dynamic> apiData = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);

                        string Inning1BattingTeam = apiDetail.Inning1BattingTeam;
                        string Team1 = apiDetail.Team1.Trim();
                        string Team2 = apiDetail.Team2.Trim();
                        string team_a = (apiData["data"]["teams"]["a"]["name"] != null) ? apiData["data"]["teams"]["a"]["name"].Value.Trim() : "";
                        string team_b = (apiData["data"]["teams"]["b"]["name"] != null) ? apiData["data"]["teams"]["b"]["name"].Value.Trim() : "";
                        string first_batting = "";
                        if (apiData["data"]["play"] != null)
                        {
                            first_batting = (apiData["data"]["play"]["first_batting"] != null) ? apiData["data"]["play"]["first_batting"].Value : "";
                            first_batting = first_batting.Trim();
                        }

                        //decide 1st batting team in case it is not set in DB
                        //if (string.IsNullOrWhiteSpace(Inning1BattingTeam) && first_batting != "")
                        if (string.IsNullOrWhiteSpace(Inning1BattingTeam))
                        {

                            string tWinner = "";
                            string TossWinner = "";
                            string tLooser = "a";
                            string tElected = "";
                            if (apiData["data"]["toss"] != null)
                            {
                                tWinner = (apiData["data"]["toss"]["winner"] != null) ? apiData["data"]["toss"]["winner"].Value.Trim() : "";
                                
                            }
                            if (tWinner != "")
                            {
                                if (tWinner == "a")
                                {
                                    TossWinner = team_a;
                                    tLooser = "b";
                                }
                                else
                                    TossWinner = team_b;
                            }

                            if(first_batting == "")
                            {
                                tElected = (apiData["data"]["toss"]["elected"] != null) ? apiData["data"]["toss"]["elected"].Value.Trim() : "";
                                if (tElected == "bat")
                                    first_batting = tWinner;
                                else
                                    first_batting = tLooser;

                            }

                            Inning1BattingTeam = first_batting;

                            if (Inning1BattingTeam != "")
                            {
                                if (Inning1BattingTeam == "a")
                                {
                                    Inning1BattingTeam = team_a;
                                    Team1 = team_a;
                                    Team2 = team_b;
                                }
                                else
                                {

                                    Inning1BattingTeam = team_b;
                                    Team1 = team_b;
                                    Team2 = team_a;
                                }
                            }

                            param = new Dictionary<string, object>() {
                            { "@Inning1BattingTeam", Inning1BattingTeam },
                            { "@Team1", Team1 },
                            { "@Team2", Team2 },
                            { "@tWinner", TossWinner },
                            { "@MatchId", MatchId }
                             };
                            data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetBattingTeam", param);

                        }

                        foreach (var player in apiData["data"]["squad"]["a"]["playing_xi"])
                        {
                                
                            string p_key = player.Value;
                            playerName = (apiData["data"]["players"][p_key]["player"]["name"] != null) ? apiData["data"]["players"][p_key]["player"]["name"].Value : "";
                            playerName = playerName.Replace("'", "");
                            playerRole = (apiData["data"]["players"][p_key]["player"]["seasonal_role"] != null) ? apiData["data"]["players"][p_key]["player"]["seasonal_role"].Value : "batsman";
                            playerShortName = "";
                            if (playerRole == "all_rounder")
                                playerRole = "allrounder";
                            else if (playerRole == "keeper")
                                playerRole = "wicketkeeper";

                            string[] pName_array = playerName.Split(' ');
                            if (pName_array.Length > 1)
                                playerShortName = pName_array[0][0] + pName_array[1]; 
                            else
                                playerShortName = pName_array[0];

                                param = new Dictionary<string, object>() {
                                    { "@PlayerKey", p_key },
                                    { "@PlayerName", playerName },
                                    { "@MatchId", MatchId },
                                    { "@TournamentId", TournamentId },
                                    { "@PlayerSpeciality", playerRole },
                                    { "@TeamName", team_a },
                                    { "@PlayerShortName", playerShortName }
                                 };
                                data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..InitiateMatchPlayerStats", param);
                        }

                        foreach (var player in apiData["data"]["squad"]["b"]["playing_xi"])
                        {

                            string p_key = player.Value;
                            playerName = (apiData["data"]["players"][p_key]["player"]["name"] != null) ? apiData["data"]["players"][p_key]["player"]["name"].Value : "";
                            playerName = playerName.Replace("'", "");
                            playerRole = (apiData["data"]["players"][p_key]["player"]["seasonal_role"] != null) ? apiData["data"]["players"][p_key]["player"]["seasonal_role"].Value : "batsman";
                            playerShortName = "";
                            if (playerRole == "all_rounder")
                                playerRole = "allrounder";
                            else if (playerRole == "keeper")
                                playerRole = "wicketkeeper";

                            string[] pName_array = playerName.Split(' ');
                            if (pName_array.Length > 1)
                                playerShortName = pName_array[0][0] + pName_array[1];
                            else
                                playerShortName = pName_array[0];

                            param = new Dictionary<string, object>() {
                                    { "@PlayerKey", p_key },
                                    { "@PlayerName", playerName },
                                    { "@MatchId", MatchId },
                                    { "@TournamentId", TournamentId },
                                    { "@PlayerSpeciality", playerRole },
                                    { "@TeamName", team_b },
                                    { "@PlayerShortName", playerShortName }
                                 };
                            data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..InitiateMatchPlayerStats", param);
                        }
                    }
                    catch (Exception e)
                    { }
                }
            }

            return true;
        }
        internal bool SaveFantasyScoreData(FantasySummaryAPIDetails apiDetail,  string tranId)
        {
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;

            string rs_token = GenerateAuthToken();
            string url = APIBaseUrl + "/cricket/" + project_key + "/match/" + apiDetail.UniqueId + "/";

            var client = new RestClient(url);
            var request = new RestRequest(Method.GET);
            request.AddHeader("Content-Type", "application/json");
            request.AddHeader("Accept", "application/json");
            request.AddHeader("rs-token", rs_token);
            IRestResponse response = client.Execute(request);

            Dictionary<string, dynamic> apiData = JsonConvert.DeserializeObject<Dictionary<string, dynamic>>(response.Content);

            StringBuilder selectQuery = new StringBuilder();
            selectQuery.Append(@"UPDATE [FANTASYCRICKET].[DBO].[APIDetails] SET ResponsePayload = @ResponsePayload, ModifyDateTime=GETDATE()");
            selectQuery.Append(@" WHERE APIDetailsId = @APIDetailsId ");
            string jsonStr = response.Content;

            var param = new Dictionary<string, object>() {
                { "@ResponsePayload", jsonStr },
                { "@APIDetailsId", apiDetail.APIDetailsId }
             };
            var data = DatabaseHelper.ExecuteScalerQuery(selectQuery, param);

            int TournamentId = apiDetail.TournamentId;
            int MatchId = apiDetail.MatchId;
            int MatchNo = apiDetail.MatchNo;
            string MatchType = apiDetail.MatchType;
            string Inning1BattingTeam = apiDetail.Inning1BattingTeam;
            string Inning2BattingTeam = apiDetail.Inning2BattingTeam;
            string Team1 = apiDetail.Team1;
            string Team2 = apiDetail.Team2;


            if (apiDetail.MatchStage == "D")
            {
                if (apiDetail.MatchType == "T20")
                    TournamentId = -99;
                else
                    TournamentId = -100;
            }

            string team_a = (apiData["data"]["teams"]["a"]["name"] != null) ? apiData["data"]["teams"]["a"]["name"].Value.Trim() : "";
            string team_b = (apiData["data"]["teams"]["b"]["name"] != null) ? apiData["data"]["teams"]["b"]["name"].Value.Trim() : "";
            string short_team_a = (apiData["data"]["teams"]["a"]["code"] != null) ? apiData["data"]["teams"]["a"]["code"].Value.Trim() : "";
            string short_team_b = (apiData["data"]["teams"]["b"]["code"] != null) ? apiData["data"]["teams"]["b"]["code"].Value.Trim() : "";
            string Team1Score =  "";
            string Team2Score = "";
            string Team1Extras = "";
            string Team2Extras = "";
            string Team1runrate = "0";
            string Team2runrate = "0";
            string first_batting = "";
            string MatchSummary = "";
            if (apiData["data"]["play"] != null)
            {
                Team1Score = (apiData["data"]["play"]["innings"]["a_1"]["score_str"] != null) ? apiData["data"]["play"]["innings"]["a_1"]["score_str"].Value : "";
                Team2Score = (apiData["data"]["play"]["innings"]["b_1"]["score_str"] != null) ? apiData["data"]["play"]["innings"]["b_1"]["score_str"].Value : "";
                Team1Extras = (apiData["data"]["play"]["innings"]["a_1"]["extra_runs"]["extra"] != null) ? apiData["data"]["play"]["innings"]["a_1"]["extra_runs"]["extra"].Value.ToString() : "";
                Team2Extras = (apiData["data"]["play"]["innings"]["b_1"]["extra_runs"]["extra"] != null) ? apiData["data"]["play"]["innings"]["b_1"]["extra_runs"]["extra"].Value.ToString() : "";
                Team1runrate = (apiData["data"]["play"]["innings"]["a_1"]["score"]["run_rate"] != null) ? apiData["data"]["play"]["innings"]["a_1"]["score"]["run_rate"].Value.ToString() : "0";
                Team2runrate = (apiData["data"]["play"]["innings"]["b_1"]["score"]["run_rate"] != null) ? apiData["data"]["play"]["innings"]["b_1"]["score"]["run_rate"].Value.ToString() : "0";
                first_batting = (apiData["data"]["play"]["first_batting"] != null) ? apiData["data"]["play"]["first_batting"].Value.Trim() : "";
                if(apiData["data"]["play"]["result"] != null)
                    MatchSummary = (apiData["data"]["play"]["result"]["msg"] != null) ? apiData["data"]["play"]["result"]["msg"].Value : "";
            }

            string TeamScore = Team1Score + Team2Score;

            if (Team1 == team_a)
            {
                if (Team2 != team_b)
                    return false;
            }
            else if (Team1 == team_b)
            {
                if (Team2 != team_a)
                    return false;
            }
            else
                return false;

            InitiateMatchPlayerStats(apiDetail);

            try
            {
                if (Team1 == team_b)
                {
                    string temp = short_team_a;
                    short_team_a = short_team_b;
                    short_team_b = temp;

                    temp = Team1Score;
                    Team1Score = Team2Score;
                    Team2Score = temp;

                    temp = Team1Extras;
                    Team1Extras = Team2Extras;
                    Team2Extras = temp;

                    temp = Team1runrate;
                    Team1runrate = Team2runrate;
                    Team2runrate = temp;
                }

                decimal Team1RR = 0;
                decimal Team2RR = 0;
                if (TeamScore != "")
                {
                    Team1Score = Team1Score.Replace("in", "~ov");
                    Team2Score = Team2Score.Replace("in", "~ov");

                    Team1Score = short_team_a + "~ " + Team1Score;
                    Team2Score = short_team_b + "~ " + Team2Score;

                    if (Team1runrate != "")
                        Team1RR = decimal.Parse(Team1runrate);

                    if (Team2runrate != "")
                        Team2RR = decimal.Parse(Team2runrate);

                
                        param = new Dictionary<string, object>() {
                            { "@TournamentId", TournamentId},
                            { "@MatchId", MatchId},
                            { "@Team1Score", Team1Score},
                            { "@Team2Score", Team2Score},
                            { "@Team1RR", Team1RR},
                            { "@Team2RR", Team2RR},
                            { "@MatchSummary", MatchSummary},
                            { "@Team1Extras", Team1Extras},
                            { "@Team2Extras", Team2Extras}

                        };
                    data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveScoreSummary", param);
                }
            }
            catch (Exception Excp)
            {
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "SaveScoreSummary_ERROR";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "SaveScoreSummary(TournamentId**MatchId) - " + TournamentId.ToString() + "**" + MatchId.ToString();
                error.TransactionId = tranId;
                error.TransactionType = "BATCHJOB - SaveScoreSummary";

                Utilities.LogException(error);
            }


            bool res = false;
  
            
            if (apiData["data"]["squad"]["a"]["playing_xi"].Count > 0)
                res =  SavePlayersScore(first_batting, team_a, team_b, TournamentId, MatchId, "a", apiData, tranId);
            if (apiData["data"]["squad"]["b"]["playing_xi"].Count > 0)
                res = SavePlayersScore(first_batting, team_a, team_b, TournamentId, MatchId, "b", apiData, tranId);

            bool matchFinished = CheckMatchFinish(team_a, team_b, MatchId, apiData, tranId);

            try
            {
                //update Player Points Stats for that match
                param = new Dictionary<string, object>() {
                        { "@TournamentId", TournamentId},
                        { "@MatchId", MatchId}
                    };
                data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..UpdatePlayerStatsOnPointRules", param);

                //trigger point calc automatically
                
                if(matchFinished == true)
                {
                    param = new Dictionary<string, object>() {
                        { "@TournamentId", apiDetail.TournamentId},
                        { "@TriggerMode", "A"}
                    };
                    data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..Trigger4PointCalc", param);
                }

            }
            catch (Exception Excp)
            {
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "SaveFantasyScoreData_ERROR";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "UpdatePlayerStatsOnPointRules(TournamentId**MatchId) - " + TournamentId.ToString() + "**" + MatchId.ToString();
                error.TransactionId = tranId;
                error.TransactionType = "BATCHJOB - UpdatePlayerStatsOnPointRules";
                Utilities.LogException(error);
            }

            return true;
        }

        internal bool CheckMatchFinish(string team_a, string team_b, int MatchId,  Dictionary<string, dynamic> apiData, string tranId)
        {
            bool matchFinished = false;
            try
            {
                string playStatus = (apiData["data"]["play_status"] != null) ? apiData["data"]["play_status"].Value : "";
                string matchStatus = (apiData["data"]["status"] != null) ? apiData["data"]["status"].Value : "";
                string matchWinner = (apiData["data"]["winner"] != null) ? apiData["data"]["winner"].Value.Trim() : "";
                string mom_PlayerKey = "";
                string playStarted = "yes";

                

                if (apiData["data"]["play"] != null)
                {
                    if(playStatus == "result")
                        mom_PlayerKey = (apiData["data"]["play"]["result"] != null) ? (apiData["data"]["play"]["result"]["pom"] != null ? apiData["data"]["play"]["result"]["pom"][0] : "") : "";
                }
                else
                    playStarted = "no";

                if (matchStatus == "completed" && mom_PlayerKey != "" && playStatus == "result")
                {
                    int draw = 0;
                    string Loser = "";
                    if (matchWinner == "a")
                    {
                        matchWinner = team_a;
                        Loser = team_b;
                    }
                    else if (matchWinner == "b")
                    {
                        matchWinner = team_b;
                        Loser = team_a;
                    }
                    else
                        draw = 1;

                    var param = new Dictionary<string, object>() {
                       { "@MatchId", MatchId},
                       { "@mom_PlayerKey", mom_PlayerKey},
                       {"@Winner", matchWinner},
                       {"@Loser", Loser},
                       {"@Draw", draw}
                            };
                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..FinishMatch", param);
                    matchFinished = true;
                }

                if (matchStatus == "completed" && playStatus == "abandoned")
                {
                    var param = new Dictionary<string, object>() {
                       { "@MatchId", MatchId},
                       { "@PlayStarted", playStarted}
                            };
                    var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SetAbandonedMatch", param);
                    if(playStarted == "yes")
                        matchFinished = true;
                    
                }
            }
            catch (Exception Excp)
            {
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "CheckMatchFinish_ERROR";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "CheckMatchFinish(MatchId) - " +  "**" + MatchId.ToString();
                error.TransactionId = tranId;
                //error.Payload = (apiData["data"]["play"]["result"]["pom"].Length == 0) ? "empty": "not";
                error.TransactionType = "BATCHJOB - CheckMatchFinish";

                //Utilities.LogException(error);
            }
            return matchFinished;
        }


        internal bool SavePlayersScore(string first_batting, string team_a, string team_b, int TournamentId, int MatchId,string team_symbol, Dictionary<string, dynamic> apiData, string tranId)
        {
           var param = new Dictionary<string, object>()
            { };


                int Inning = 0;
                string p_key = "";
                string _dismissal = "";
                int _sixes = 0;
                int _fours = 0;
                double _econ = 0;
                int _ballfaced = 0;
                int _run = 0;
                string _dismissal_info = "";
                string PlayerIndicator = "";
                int _wicket = 0;
                int _maiden = 0;
                int _zeros = 0;
                double _over = 0;
                int _runout = 0;
                int _stumped = 0;
                int _bowled = 0;
                int _lbw = 0;
                int _catch = 0;

                string captain = (apiData["data"]["squad"][team_symbol]["captain"] != null) ? apiData["data"]["squad"][team_symbol]["captain"].Value : "";
                string keeper = (apiData["data"]["squad"][team_symbol]["keeper"] != null) ? apiData["data"]["squad"][team_symbol]["keeper"].Value : "";

                    foreach (var item in apiData["data"]["squad"][team_symbol]["player_keys"])
                    {
                        p_key = item.Value;
                        _run = 0;
                        _fours = 0;
                        _sixes = 0;
                        _econ = 0;
                        _ballfaced = 0;
                        _wicket = 0;
                        _maiden = 0;
                        _zeros = 0;
                        _over = 0;
                        _runout = 0;
                        _stumped = 0;
                        _bowled = 0;
                        _lbw = 0;
                        _catch = 0;
                        Inning = 0;
                        if (team_symbol == "a")
                        {
                            if (first_batting == "a")
                                Inning = 1;
                            else if (first_batting == "b")
                                Inning = 2;
                        }
                        else
                        {
                            if (first_batting == "a")
                                Inning = 2;
                            else if (first_batting == "b")
                                Inning = 1;
                        }
                try
                {
                    if (apiData["data"]["players"][p_key]["score"] != null)
                    {
                        //process batting score
                        if (apiData["data"]["players"][p_key]["score"]["1"]["batting"] != null)
                        {
                            PlayerIndicator = "";

                            try
                            {

                                _run = (int)((apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["runs"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["runs"].Value : 0);
                                _fours = (int)((apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["fours"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["fours"].Value : 0);
                                _sixes = (int)((apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["sixes"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["sixes"].Value : 0);
                                _econ = (apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["strike_rate"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["strike_rate"].Value : 0;
                                _ballfaced = (int)((apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["balls"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["score"]["balls"].Value : 0);
                                _dismissal_info = (apiData["data"]["players"][p_key]["score"]["1"]["batting"]["dismissal"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["batting"]["dismissal"]["msg"].Value : "not out";
                                _dismissal = _dismissal_info;
                                if (p_key == captain)
                                    PlayerIndicator = "c";

                                if (p_key == keeper)
                                    PlayerIndicator = "w";

                                param = new Dictionary<string, object>() {
                            { "@TournamentId", TournamentId},
                            { "@MatchId", MatchId},
                            { "@Inning", Inning},
                            { "@PlayerKey", p_key},
                            { "@Dismissal", _dismissal},
                            { "@SR", _econ},
                            { "@sixes", _sixes},
                            { "@fours", _fours},
                            { "@BallFaced", _ballfaced},
                            { "@Runs", _run},
                            { "@DismissalInfo", _dismissal_info},
                            { "@PlayerIndicator", PlayerIndicator}
                                          };
                                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveMatchPlayerBattingData", param);
                            }
                            catch (Exception Excp)
                            {
                                LogExceptionEntities error = new LogExceptionEntities();
                                error.FileName = _Filename;
                                error.ProductName = _AppName;
                                error.EnvCode = _EnvCode;
                                error.ErrorCode = "SavePlayersScore_ERROR";
                                int strLength = Excp.Message.ToString().Length;
                                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                                error.StackTrace = Excp.Message.ToString();
                                error.APIName = "Batting Data";
                                error.TransactionId = tranId;
                                error.TransactionType = "BATCHJOB - PointCalculationIntegration";
                                //error.Payload = sInputContents;
                                //error.LoginUser = this.Request.Headers.GetValues("loginUser").First();;
                                Utilities.LogException(error);
                            }
                        }

                        //process bowling score
                        if (apiData["data"]["players"][p_key]["score"]["1"]["bowling"] != null)
                        {

                            try
                            {

                                _sixes = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["sixes"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["sixes"].Value : 0);
                                _fours = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["fours"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["fours"].Value : 0);
                                _zeros = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["dot_balls"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["balls_breakup"]["dot_balls"].Value : 0);
                                _econ = (apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["economy"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["economy"].Value : 0;
                                _wicket = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["wickets"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["wickets"].Value : 0);
                                _run = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["runs"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["runs"].Value : 0);
                                _maiden = (int)((apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["maiden_overs"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["maiden_overs"].Value : 0);
                                string over_O = (apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["overs"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["overs"][0].Value.ToString() : "0";
                                string over_B = (apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["overs"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["bowling"]["score"]["overs"][1].Value.ToString() : "0";
                                _over = Convert.ToDouble(over_O + "." + over_B);

                                param = new Dictionary<string, object>() {
                        { "@TournamentId", TournamentId},
                        { "@MatchId", MatchId},
                        { "@Inning", Inning},
                        { "@PlayerKey", p_key},
                        { "@sixes", _sixes},
                        { "@fours", _fours},
                        { "@zeros", _zeros},
                        { "@econ", _econ},
                        { "@W", _wicket},
                        { "@R", _run},
                        { "@M", _maiden},
                        { "@O", _over}
                        };

                                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveMatchPlayerBowlingData", param);
                            }
                            catch (Exception Excp)
                            {
                                //need to handle
                                LogExceptionEntities error = new LogExceptionEntities();
                                error.FileName = _Filename;
                                error.ProductName = _AppName;
                                error.EnvCode = _EnvCode;
                                error.ErrorCode = "SavePlayersScore_ERROR";
                                int strLength = Excp.Message.ToString().Length;
                                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                                error.StackTrace = Excp.Message.ToString();
                                error.APIName = "Bowling Data";
                                error.TransactionId = tranId;
                                error.TransactionType = "BATCHJOB - PointCalculationIntegration";
                                //error.Payload = sInputContents;
                                //error.LoginUser = this.Request.Headers.GetValues("loginUser").First();;
                                Utilities.LogException(error);
                            }
                        }

                        //process fielding score
                        if (apiData["data"]["players"][p_key]["score"]["1"]["fielding"] != null)
                        {

                            try
                            {
                                _runout = (int)((apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["runouts"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["runouts"].Value : 0);
                                _stumped = (int)((apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["stumpings"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["stumpings"].Value : 0);
                                _catch = (int)((apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["catches"] != null) ? apiData["data"]["players"][p_key]["score"]["1"]["fielding"]["catches"].Value : 0);

                                param = new Dictionary<string, object>() {
                        { "@TournamentId", TournamentId},
                        { "@MatchId", MatchId},
                        { "@Inning", Inning},
                        { "@PlayerKey", p_key},
                        { "@runout", _runout},
                        { "@stumped", _stumped},
                        { "@bowled", _bowled},
                        { "@lbw", _lbw},
                        { "@catch", _catch}
                            };
                                var data = DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveMatchPlayerFieldingData", param);
                            }
                            catch(Exception Excp)
                            {
                                LogExceptionEntities error = new LogExceptionEntities();
                                error.FileName = _Filename;
                                error.ProductName = _AppName;
                                error.EnvCode = _EnvCode;
                                error.ErrorCode = "SavePlayersScore_ERROR";
                                int strLength = Excp.Message.ToString().Length;
                                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                                error.StackTrace = Excp.Message.ToString();
                                error.APIName = "Fielding Data";
                                error.TransactionId = tranId;
                                error.TransactionType = "BATCHJOB - PointCalculationIntegration";
                                //error.Payload = sInputContents;
                                //error.LoginUser = this.Request.Headers.GetValues("loginUser").First();;
                                Utilities.LogException(error);
                            }
                        }
                    }
                }
                catch (Exception Excp)
                {
                    //need to handle it
                    /*
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "SavePlayersScore_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "SavePlayersScore";
                    error.TransactionId = tranId;
                    error.TransactionType = "BATCHJOB - PointCalculationIntegration";
                    //error.Payload = sInputContents;
                    //error.LoginUser = this.Request.Headers.GetValues("loginUser").First();;
                    Utilities.LogException(error);
                    */
                }
                }
           

            return true;
        }

       
       

        internal bool CalculateMatchPoints(MatchDetails4PointCalculation matchDetail, string tranId)
        {
            try
            {
                
                //calculate match points for all users
                var param = new Dictionary<string, object>() {
                    { "@MatchId", matchDetail.MatchId }
                    };

                if(matchDetail.MatchStage != "D")
                    DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateMatchPoint", param);
                else
                    DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..CalculateDailyMatchPoint", param);

            }
            catch (Exception Excp)
            {
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "CalculateMatchPoints_ERROR";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = "CalculateMatchPoints(TournamentId**MatchId) - " + matchDetail.TournamentId.ToString() + "**" + matchDetail.MatchId.ToString();
                error.TransactionId = tranId;
                error.TransactionType = "BATCHJOB - CalculateMatchPoints";

                Utilities.LogException(error);
            }

            return true;
        }

        internal bool SaveWeatherData(int MatchId, String WeatherCondition)
        {
            if (WeatherCondition.Contains("Cloud") || WeatherCondition.Contains("Mist") || WeatherCondition.Contains("Haze") || WeatherCondition.Contains("Smoke") || WeatherCondition.Contains("Dust"))
                WeatherCondition = "cloudy";
            else if(WeatherCondition.Contains("Clear"))
                WeatherCondition = "sunny";
            else if (WeatherCondition.Contains("Rain"))
                WeatherCondition = "rain";
            else if (WeatherCondition.Contains("Thunderstorm"))
                WeatherCondition = "thunderstorms";

            var param = new Dictionary<string, object>() {
                 { "@MatchId", MatchId },
                 { "@Weather", WeatherCondition }
             };

            DatabaseHelper.ExecuteStoredProc("FANTASYCRICKET..SaveWeatherData", param);
            return true;
        }

        public static object IfNull(object value)
        {
            if (value == null)
            {
                return DBNull.Value;
            }

            if (value.GetType() == typeof(bool)
                || value.GetType() == typeof(char))
            {
                return value;
            }

            if ((value.GetType() == typeof(string) && string.IsNullOrEmpty(value.ToString()))
                || (value.GetType() == typeof(DateTime) && DateTime.Parse(value.ToString(), CultureInfo.InvariantCulture) == DateTime.MinValue)
                )
            {
                return DBNull.Value;
            }

            return value;
        }

        #endregion
    }
}