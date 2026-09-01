using System;
using System.Net;
using System.Timers;
using System.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Collections.Generic;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using FantasyCricketScoreIntegration.Models;
using FantasyCricketScoreIntegration.RapidScoreDetails.Models;
using FantasyCricketScoreIntegration.RapidMatchSummary.Models;
using FantasyCricketScoreIntegration.WeatherData.Models;
using FantasyCricket.Score.Repository;
using Common.Logging.Entities;
using Common.Logger;
using FantasyCricketAppRest.CommonUtilities;
using RestSharp;

namespace FantasyCricket.Score.Integration
{
   
    public class ScoreIntegartion
    {
       
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"].ToString();
        private static string _AppName = ConfigurationManager.AppSettings["AppName"].ToString();
        private static string _Filename = "FantasyCricketScoreIntegration.cs";
        private static string _ScoreTimerInterval = ConfigurationManager.AppSettings["ScoreTimerInterval"].ToString();
        private static string _PointCalcTimerInterval = ConfigurationManager.AppSettings["PointCalcTimerInterval"].ToString();
        private static string _UpdatePlyaerStatTimerInterval = ConfigurationManager.AppSettings["UpdatePlyaerStatTimerInterval"].ToString();
        private static string WeatherAPIUrl = ConfigurationManager.AppSettings["WeatherAPIUrl"].ToString();
        private static string WeatherAPIKey = ConfigurationManager.AppSettings["WeatherAPIKey"].ToString();
        //private static Task task;
        readonly Timer _ScoreTimer;
        readonly Timer _PointCalcTimer;
        readonly Timer _UpdatePlyaerStatTimer;

        public ScoreIntegartion()
        {
            //FetchFantasySummary().GetAwaiter().GetResult();

            //score calculation
            _ScoreTimer = new Timer(Int32.Parse(_ScoreTimerInterval)) { AutoReset = true };
            Console.WriteLine("Score Start Time: " + DateTime.Now);
            _ScoreTimer.Elapsed += (sender, eventArgs) => FantasySummaryIntegration();
            Console.WriteLine("Score End Time: " + DateTime.Now);

            
            //point calculation after match
            _PointCalcTimer = new Timer(Int32.Parse(_PointCalcTimerInterval)) { AutoReset = true };
            Console.WriteLine("Point Calc Start Time: " + DateTime.Now);
            _PointCalcTimer.Elapsed += (sender, eventArgs) => CalculatePointSummary();
            Console.WriteLine("Point Calc End Time: " + DateTime.Now);

            //player stat update after match
            _UpdatePlyaerStatTimer = new Timer(Int32.Parse(_UpdatePlyaerStatTimerInterval)) { AutoReset = true };
            Console.WriteLine("Player Stat Start Time: " + DateTime.Now);
            _UpdatePlyaerStatTimer.Elapsed += (sender, eventArgs) => UpdatePlayerStatSummary();
            Console.WriteLine("Player Stat End Time: " + DateTime.Now);
            
        }
         public void Start() { _ScoreTimer.Start(); _PointCalcTimer.Start(); _UpdatePlyaerStatTimer.Start(); }
         public void Stop() { _ScoreTimer.Stop(); _PointCalcTimer.Stop(); _UpdatePlyaerStatTimer.Stop(); }
        //public void Start() { _ScoreTimer.Start();  }
        //public void Stop() { _ScoreTimer.Stop();  }

        static  bool FantasySummaryIntegration()
        {
            Console.WriteLine("Score Begins Time: " + DateTime.Now);
            try
            {
                
                bool ret = false;
                ret = UpdateFantasyScoreSummary();
 
            }
            catch(Exception e)
            { }

            return true;
        }

        static bool CalculatePointSummary()
        {
            
            string tranId = Guid.NewGuid().ToString();
            bool res = false;

            LogAuditEntities audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "BEGIN";
            audit.Message = "Point Calc Batch Job - BEGIN";
            audit.APIName = "PointCalculation";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - PointCalculationIntegration";
            Utilities.LogAudit(audit);

            try
            {
                List<MatchDetails4PointCalculation> matchDetails = new IntegrationScoreRepository().GetMatchDetails(tranId);
                foreach (var matchDetail in matchDetails)
                {
                    res = new IntegrationScoreRepository().CalculateMatchPoints(matchDetail, tranId);

                }
            }
            catch (Exception Excp)
            {
                //Console.WriteLine(Excp.Message);
                try
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "CalculatePointSummary_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "PointCalculationIntegration";
                    error.TransactionId = tranId;
                    error.TransactionType = "BATCHJOB - PointCalculationIntegration";
                    //error.Payload = sInputContents;
                    //error.LoginUser = this.Request.Headers.GetValues("loginUser").First();;
                    Utilities.LogException(error);

                }
                catch { }  // do nothing if we get an error here...
            }

            audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "END";
            audit.Message = "Point Calc Batch Job - END";
            audit.APIName = "PointCalculation";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - PointCalculationIntegration";
            Utilities.LogAudit(audit);

            return true;
        }

        static bool UpdatePlayerStatSummary()
        {
            

            string tranId = Guid.NewGuid().ToString();
            bool res = false;

            LogAuditEntities audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "BEGIN";
            audit.Message = "Update Player stat Batch Job - BEGIN";
            audit.APIName = "UpdatePlayerStat";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - UpdatePlayerStat";
            Utilities.LogAudit(audit);

            try
            {
                List<MatchDetails4PlayerStatsModel> matchDetails = new IntegrationScoreRepository().GetMatchDetails4PlayerStats(tranId);
                foreach (var match in matchDetails)
                {
                    res = new IntegrationScoreRepository().UpdatePlayerStats(match, tranId);

                }
            }
            catch (Exception Excp)
            {
                
                try
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "UpdatePlayerStat_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "UpdatePlayerStat";
                    error.TransactionId = tranId;
                    error.TransactionType = "BATCHJOB - UpdatePlayerStat";
                    Utilities.LogException(error);

                }
                catch { }  // do nothing if we get an error here...
            }

            audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "END";
            audit.Message = "Update Player stat Batch Job - END";
            audit.APIName = "UpdatePlayerStat";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - UpdatePlayerStat";
            Utilities.LogAudit(audit);

            return true;
        }

        static bool UpdateFantasyScoreSummary()
        {
            
            string tranId = Guid.NewGuid().ToString();
            LogAuditEntities audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "BEGIN";
            audit.Message = "Score Integration Batch Job - BEGIN";
            audit.APIName = "ScoreIntegration";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - ScoreIntegration";
            Utilities.LogAudit(audit);

            //calculate score and summary
            try
            {
                List<FantasySummaryAPIDetails> dbItems = new IntegrationScoreRepository().GetMatchData(tranId); 
                foreach (var item in dbItems)
                {
                    string unique_id = item.UniqueId;
                    if (unique_id == "" || unique_id == null)
                        continue;

                    
                    //setting playing 11 indicator on players
                    if (item.MatchStatus == "UPCOMING" && (item.TossWinner == item.Team1 || item.TossWinner == item.Team2))
                    {
                        
                        bool res = new IntegrationScoreRepository().InitiateMatchPlayerStats(item);
                        
                        if (item.MatchCity != "")
                            res = FetchWeatherData(item);
                    }
                    if (item.MatchStatus != "UPCOMING")
                    {
                        bool response = new IntegrationScoreRepository().SaveFantasyScoreData(item, tranId); 
                    }
                }
            }
            catch (Exception Excp)
            {
                //Console.WriteLine(Excp.Message);
                try
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "CalculatePointSummary_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "ScoreIntegration";
                    error.TransactionId = tranId;
                    error.TransactionType = "BATCHJOB - ScoreIntegration";
                    Utilities.LogException(error);

                }
                catch { }  // do nothing if we get an error here...
            }
            

            audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "END";
            audit.Message = "Score Integration Batch Job - END";
            audit.APIName = "ScoreIntegration";
            audit.TransactionId = tranId;
            audit.TransactionType = "BATCHJOB - ScoreIntegration";
            Utilities.LogAudit(audit);

            return true;
        }


        static bool FetchWeatherData(FantasySummaryAPIDetails matchDetails)
        {
            System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            string url = WeatherAPIUrl + matchDetails.MatchCity + "&appid=" + WeatherAPIKey;

            try
            {
                WeatherData data = new WeatherData();
                var client = new RestClient(url);
                var request = new RestRequest();
                request.AddHeader("Accept", "application/json");
                var response = client.Execute(request);

                if (response.IsSuccessful)
                {
                    data = JsonConvert.DeserializeObject<WeatherData>(response.Content);
                    string WeatherCondition = data.weather[0].main;
                    bool res = new IntegrationScoreRepository().SaveWeatherData(matchDetails.MatchId, WeatherCondition);
                }
            }
            catch (Exception Excp)
            {
                //Console.WriteLine(Excp.Message);
                try
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "FetchWeatherData_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = "FetchWeatherData";
                    error.TransactionId = Guid.NewGuid().ToString();
                    error.TransactionType = "BATCHJOB - ScoreIntegration";
                    Utilities.LogException(error);

                }
                catch { }  // do nothing if we get an error here...
            }

            return true;
        }
        
        
    }
}