using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Configuration;
using System.Data.SqlClient;
using System.Dynamic;
using System.Globalization;
using Common.Logging.Entities;
using FantasyCricketAppRest.Models;
using Common.Logger;
using MySql.Data.MySqlClient;
using FantasyCricketAppRest.CommonUtilities;

namespace FantasyCricketAppRest.DBHelpers
{
    internal static class DbConnectionHelper
    {
        # region "Fields"

        internal static readonly string DBConnectionString =
            ConfigurationManager.ConnectionStrings["FANTASYCRICKET"].ConnectionString;
        
        # endregion "Fields"

        # region "Methods"



        public static string GetSqlConnection()
        {
            return DBConnectionString;
        }

        # endregion "Methods"
    }
    public static class DatabaseHelper
    {
       // private static string _logServiceURI = null;
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "DatabaseHelper.cs";
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        private static string ConnectionTimeout = ConfigurationManager.AppSettings["SQLConnectionTimeout"];
        // private static readonly log4net.ILog ErrorLog = log4net.LogManager.GetLogger("ExceptionLogger");

        static DatabaseHelper()
        {
            try
            {

            }
            catch (Exception Excp)
            {
               // Logger.Write(_logServiceURI, enmEventType.Warning, "Error captured in constructor method.", string.Format(CultureInfo.InvariantCulture, "Exception captured in Initialize DatabaseHelper, Logging and normal functions may be affected: {0}", Excp.ToString()), "DatabaseHelper.DatabaseHelper()", _AppName, _Filename, 30);
            }
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
        public static dynamic ExecuteStoredProc(string storedProcName, Dictionary<string, object> parameters = null)
        {
            return ExecuteStoredProc(storedProcName, parameters,null);
        }
        public static dynamic ExecuteStoredProc(string storedProcName, Dictionary<string, object> parameters = null,  string[] sections = null)
        {

            dynamic complexReturns = new ExpandoObject();
            var dataDictionary = (IDictionary<string, object>)complexReturns;
            int iteration = 0;

            List<dynamic> data = new List<dynamic>();

            using (SqlConnection connection = new SqlConnection(DbConnectionHelper.GetSqlConnection()))
            {
                connection.Open();
                using (SqlCommand cmd = new SqlCommand(storedProcName, connection))
                {
                    cmd.CommandTimeout = Int32.Parse(ConnectionTimeout);
                    try
                    {
                        cmd.CommandType = System.Data.CommandType.StoredProcedure;
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }
                        SqlDataReader rs = cmd.ExecuteReader(System.Data.CommandBehavior.CloseConnection);

                        do
                        {
                            data = new List<dynamic>();
                            while (rs.Read())
                            {
                                data.Add(FillObjectWithRecordSet(rs));
                            }
                            if (sections != null && sections.Length < 0 && iteration < sections.Length)
                            {
                                dataDictionary.Add(sections[iteration], data);
                            }
                            else
                            {
                                dataDictionary.Add("result_" + iteration.ToString(), data);
                            }
                            iteration++;
                        }
                        while (rs.NextResult());
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;

                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "DB_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = storedProcName;
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        //error.Payload = parameters.ToString();
                        error.LoginUser = userId;
                        Utilities.LogException(error);

                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            if (iteration == 1)
                return data;
            else
                return complexReturns;


        }

        public static List<VerifyJoinLeague>  VerifyJoinLeague(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<VerifyJoinLeague> responseDictonary = new List<VerifyJoinLeague>();
            string LeagueApproved = "";

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
                                VerifyJoinLeague data = new VerifyJoinLeague();
                                data.UserId = Int32.Parse(rs["UserId"].ToString());
                                data.LeagueId = Int32.Parse(rs["LeagueId"].ToString());
                                LeagueApproved = rs["LeagueApproved"].ToString();
                                
                                if (LeagueApproved.ToLower() == "true")
                                    data.LeagueApproved = true;
                                else
                                    data.LeagueApproved = false;
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "VerifyJoinLeague_SP_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "VerifyJoinLeague";
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        error.LoginUser = userId;
                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<VerifyJoinLeague>().ToList();
        }

        public static JoinLeague JoinLeague(string storedProcName, Dictionary<string, object> parameters = null)
        {
            JoinLeague responseDictonary = new JoinLeague();

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
                            if (rs.Read())
                            {

                                responseDictonary.LeagueLeaderId = Int32.Parse(rs["LeagueLeaderId"].ToString());
                                responseDictonary.UserLeagueId = Int32.Parse(rs["UserLeagueId"].ToString());

                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "JoinLeague_SP_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "JoinLeague";
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        error.Payload = parameters.ToString();
                        error.LoginUser = userId;
                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }

        public static SetVerificationCodeModel SetVerificationCode(string storedProcName, Dictionary<string, object> parameters = null)
        {
            SetVerificationCodeModel responseDictonary = new SetVerificationCodeModel();

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
                            if (rs.Read())
                            {

                                responseDictonary.UserId = Int32.Parse(rs["UserId"].ToString());
                                responseDictonary.ActivationToken = rs["ActivationToken"].ToString();
                                responseDictonary.PhoneNumber = rs["PhoneNumber"].ToString();
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "SetVerificationCode_SP_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "SetVerificationCode";
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        error.Payload = parameters.ToString();
                        error.LoginUser = userId;
                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }

        public static GetSubscriptionDetailsModel GetSubscriptionDetails(string storedProcName, Dictionary<string, object> parameters = null)
        {
            GetSubscriptionDetailsModel responseDictonary = new GetSubscriptionDetailsModel();

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
                            if (rs.Read())
                            {

                                responseDictonary.SubscriptionType = Int32.Parse(rs["SubscriptionType"].ToString());
                                responseDictonary.TournamentName = rs["TournamentName"].ToString();
                                
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;
                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "GetSubscriptionDetails_SP_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "GetSubscriptionDetails";
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        error.Payload = parameters.ToString();
                        error.LoginUser = userId;
                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }

        public static List<SyncMatchApiModel> FetchSyncMatchDetails(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<SyncMatchApiModel> responseDictonary = new List<SyncMatchApiModel>();
            

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
                                SyncMatchApiModel data = new SyncMatchApiModel();
                                data.APIDetailsId = Int32.Parse(rs["APIDetailsId"].ToString());
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.UniqueId = rs["UniqueId"].ToString();
                                data.Team1 = rs["Team1"].ToString();
                                data.Team2 = rs["Team2"].ToString();
                                data.MatchScheduledDate = rs["MatchScheduledDate"].ToString();

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
                        error.ErrorCode = "FetchSyncMatchDetails_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchSyncMatchDetails";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "SP - FetchSyncMatchDetails";
                        
                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<SyncMatchApiModel>().ToList();
        }

        public static List<SyncTeamApiModel> FetchSyncTeamDetails(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<SyncTeamApiModel> responseDictonary = new List<SyncTeamApiModel>();


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
                                SyncTeamApiModel data = new SyncTeamApiModel();
                                data.ParticipationTeamId = Int32.Parse(rs["ParticipationTeamId"].ToString());
                                data.ParticipationTeamName = rs["ParticipationTeamName"].ToString();

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
                        error.ErrorCode = "FetchSyncTeamDetails_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchSyncTeamDetails";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "SP - FetchSyncTeamDetails";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<SyncTeamApiModel>().ToList();
        }

        public static List<StartMatchModel> StartMatch(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<StartMatchModel> responseDictonary = new List<StartMatchModel>();


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
                                StartMatchModel data = new StartMatchModel();
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.MatchNo = Int32.Parse(rs["MatchNo"].ToString());
                                data.MatchDetails = rs["MatchDetails"].ToString();
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
                        error.ErrorCode = "StartMatch_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "StartMatch";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "StartMatch SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<StartMatchModel>().ToList();
        }

        public static List<FetchMatchModel> FetchMatchDetails(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<FetchMatchModel> responseDictonary = new List<FetchMatchModel>();


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
                                FetchMatchModel data = new FetchMatchModel();
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.TournamentType = rs["TournamentType"].ToString();
                                data.MatchType = rs["MatchType"].ToString();

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
                        error.ErrorCode = "FetchMatchModel_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchMatchModel";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "FetchMatchModel SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<FetchMatchModel>().ToList();
        }

        public static List<FetchEmptyRefferalCodeUsers> FetchEmptyRefferalCodeUsers(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<FetchEmptyRefferalCodeUsers> responseDictonary = new List<FetchEmptyRefferalCodeUsers>();


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
                                FetchEmptyRefferalCodeUsers data = new FetchEmptyRefferalCodeUsers();
                                data.UserId = Int32.Parse(rs["UserId"].ToString());
                                data.ReferralCode = rs["ReferralCode"].ToString();
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
                        error.ErrorCode = "FetchEmptyRefferalCodeUsers_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchEmptyRefferalCodeUsers";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "FetchEmptyRefferalCodeUsers SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<FetchEmptyRefferalCodeUsers>().ToList();
        }

        public static List<UserCommunicationModel> UserCommunication(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<UserCommunicationModel> responseDictonary = new List<UserCommunicationModel>();


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
                                UserCommunicationModel data = new UserCommunicationModel();
                                data.UserId = Int32.Parse(rs["UserId"].ToString());
                                data.Email = rs["Email"].ToString();
                                data.Name = rs["Name"].ToString();
                                data.ReferralCode = rs["ReferralCode"].ToString();
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
                        error.ErrorCode = "UserCommunication_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "UserCommunication";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "UserCommunication SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<UserCommunicationModel>().ToList();
        }

        public static List<TeamComparisonModel> TeamPointsComparison(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<TeamComparisonModel> responseDictonary = new List<TeamComparisonModel>();


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
                                TeamComparisonModel data = new TeamComparisonModel();
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.MatchNo = Int32.Parse(rs["MatchNo"].ToString());
                                data.MatchStatus = rs["MatchStatus"].ToString();
                                data.MyMatchTotalPoints = Int32.Parse(rs["MyMatchTotalPoints"].ToString());
                                data.OtherMatchTotalPoints = Int32.Parse(rs["OtherMatchTotalPoints"].ToString());
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
                        error.ErrorCode = "TeamPointsComparison_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "TeamPointsComparison";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "TeamPointsComparison SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<TeamComparisonModel>().ToList();
        }

        public static FetchPlayerStats FetchPlayerStats(string storedProcName, Dictionary<string, object> parameters = null)
        {

            FetchPlayerStats responseDictonary = new FetchPlayerStats();


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
                                FetchPlayerStats data = new FetchPlayerStats();
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.TournamentName = rs["TournamentName"].ToString();
                                data.PlayerName = rs["PlayerName"].ToString();
                                data.PlayerSpeciality = rs["PlayerSpeciality"].ToString();
                                data.TeamShortName = rs["TeamShortName"].ToString();
                                data.PlayerValue = Int32.Parse(rs["PlayerValue"].ToString());
                                data.PlayerImage = rs["PlayerImage"].ToString();
                                data.PlayerStats = rs["PlayerStats"].ToString();
                                data.SelectedBy = Int32.Parse(rs["SelectedBy"].ToString());
                                data.TotalPlayers = Int32.Parse(rs["TotalPlayers"].ToString());
                                data.PlayerValueRank = Int32.Parse(rs["PlayerValueRank"].ToString());
                                data.PlayerRank = Int32.Parse(rs["PlayerRank"].ToString());
                                responseDictonary =data;
                                break;
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "FetchPlayerStats_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchPlayerStats";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "FetchPlayerStats SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }


        public static ValidateTransferModel ValidateTransfer(string storedProcName, Dictionary<string, object> parameters = null)
        {

            ValidateTransferModel responseDictonary = new ValidateTransferModel();


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
                                ValidateTransferModel data = new ValidateTransferModel();
                                data.Status = rs["Status"].ToString();
                                responseDictonary = data;
                                break;
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "ValidateTransfer_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "ValidateTransfer";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "ValidateTransfer SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }


        public static UserContactModel FetchUserContact(string storedProcName, Dictionary<string, object> parameters = null)
        {

            UserContactModel responseDictonary = new UserContactModel();


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
                                UserContactModel data = new UserContactModel();
                                data.UserId = Int32.Parse(rs["UserId"].ToString());
                                data.PhoneNumber = rs["PhoneNumber"].ToString();
                                data.Email = rs["Email"].ToString();
                                data.SubscriptionType = Int32.Parse(rs["SubscriptionType"].ToString());
                                data.PANName = rs["PANName"].ToString();
                                data.KYCStatus = rs["KYCStatus"].ToString();
                                data.RPContactId = rs["RPContactId"].ToString();
                                data.BankVerified = rs["BankVerified"].ToString();
                                data.RPfaId = rs["RPfaId"].ToString();
                                responseDictonary = data;
                                break;
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "FetchUserContact_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "FetchUserContact";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "FetchUserContact SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }

        public static UpdateSubscriptionModel UpdateSubscriptionDetails(string storedProcName, Dictionary<string, object> parameters = null)
        {

            UpdateSubscriptionModel responseDictonary = new UpdateSubscriptionModel();


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
                                UpdateSubscriptionModel data = new UpdateSubscriptionModel();
                                data.SubscriptionType = Int32.Parse(rs["SubscriptionType"].ToString());
                                data.Email = rs["Email"].ToString();
                                responseDictonary = data;
                                break;
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "UpdateSubscriptionDetails_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "UpdateSubscriptionDetails";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "UpdateSubscriptionDetails SP";

                        Utilities.LogException(error);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary;
        }



        public static dynamic ExecuteScalerQuery(dynamic selectQuery, Dictionary<string, object> parameters = null, string[] sections = null)
        {

            dynamic complexReturns = new ExpandoObject();
            var dataDictionary = (IDictionary<string, object>)complexReturns;
            int iteration = 0;

            List<dynamic> data = new List<dynamic>();

            using (SqlConnection connection = new SqlConnection(DbConnectionHelper.GetSqlConnection()))
            {
                connection.Open();
                using (SqlCommand cmd = connection.CreateCommand())
                {
                    cmd.CommandTimeout = Int32.Parse(ConnectionTimeout);
                    try
                    {
                        
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }
                        cmd.CommandText = selectQuery.ToString();
                        cmd.CommandType = System.Data.CommandType.Text;
                        SqlDataReader rs = cmd.ExecuteReader(System.Data.CommandBehavior.CloseConnection);

                        do
                        {
                            data = new List<dynamic>();
                            while (rs.Read())
                            {
                                data.Add(FillObjectWithRecordSet(rs));
                            }
                            if (sections != null && sections.Length < 0 && iteration < sections.Length)
                            {
                                dataDictionary.Add(sections[iteration], data);
                            }
                            else
                            {
                                dataDictionary.Add("result_" + iteration.ToString(), data);
                            }
                            iteration++;
                        }
                        while (rs.NextResult());
                    }
                    catch (Exception Excp)
                    {
                        System.Web.HttpContext httpContext = System.Web.HttpContext.Current;

                        string userId = httpContext.Request.Headers.Get("x-api-userid");
                        if (string.IsNullOrWhiteSpace(userId))
                            userId = "";
                        string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
                        string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
                        if (string.IsNullOrWhiteSpace(authToken))
                            authToken = "";

                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "DB_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "INLINE_SQL";
                        if (authToken == "")
                            error.TransactionId = Guid.NewGuid().ToString();
                        else
                            error.TransactionId = authToken;
                        error.TransactionType = deviceType;
                        error.Payload = selectQuery;
                        error.LoginUser = userId;
                        Utilities.LogException(error);

                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            if (iteration == 1)
            {
                return data;
                
            }
            else
            {
                return complexReturns;
            }

        }


        public static T FillObjectWithRecordSet<T>(T obj, SqlDataReader rs)
        {

            foreach (var property in obj.GetType().GetProperties())
            {
                if (rs[property.Name] != DBNull.Value)
                {
                    switch (property.PropertyType.Name)
                    {
                        default:
                            property.SetValue(obj, rs[property.Name]);
                            break;
                    }
                }
            }


            return obj;
        }

        public static dynamic FillObjectWithRecordSet(SqlDataReader rs)
        {

            dynamic data = new ExpandoObject();
            var dataDictionary = (IDictionary<string, object>)data;

            for (int i = 0; i < rs.FieldCount; i++)
            {
                //add property
                dataDictionary.Add(rs.GetName(i), rs[i]);
            }


            return data;
        }
    }

}