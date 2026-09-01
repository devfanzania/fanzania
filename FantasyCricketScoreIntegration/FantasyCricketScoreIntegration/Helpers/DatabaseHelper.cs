using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Configuration;
//using LPL.Common.Logging;
using System.Data;
using System.Data.SqlClient;
using System.Dynamic;
using System.Globalization;
using FantasyCricketScoreIntegration.Models;

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
        private static string _logServiceURI = null;
        private static string _AppName = null;
        private static string _Filename = null;

        static DatabaseHelper()
        {
            try
            {
                _logServiceURI = "";
                _Filename = "DatabaseHelper.cs";
                _AppName = "FantasyCricketAppRestService";
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
                        //Logger.Write(_logServiceURI, enmEventType.Error, "Error captured in ExecuteStoredProc method.", string.Format(CultureInfo.InvariantCulture, "Error in ExecuteStoredProc for SPName:{0} ", storedProcName,  Excp.ToString()), "DatabaseHelper.ExecuteStoredProc()", _AppName, _Filename, 52);
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

        public static List<FantasySummaryAPIDetails> GetMatchData(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<FantasySummaryAPIDetails> responseDictonary = new List<FantasySummaryAPIDetails>();


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
                                FantasySummaryAPIDetails data = new FantasySummaryAPIDetails();
                                data.TournamentId = Int32.Parse(rs["TournamentId"].ToString());
                                data.APIDetailsId = Int32.Parse(rs["APIDetailsId"].ToString());
                                data.MatchId = Int32.Parse(rs["MatchId"].ToString());
                                data.MatchNo = Int32.Parse(rs["MatchNo"].ToString());
                                data.Team1 = rs["Team1"].ToString();
                                data.Team2 = rs["Team2"].ToString();
                                data.MatchType = rs["MatchType"].ToString();
                                data.MatchStage = rs["MatchStage"].ToString();
                                data.APIName = rs["APIName"].ToString();
                                data.APIKey = rs["APIKey"].ToString();
                                data.UniqueId = rs["UniqueId"].ToString();
                                data.Inning1BattingTeam = rs["Inning1BattingTeam"].ToString();
                                data.Inning2BattingTeam = rs["Inning2BattingTeam"].ToString();
                                data.MatchStatus = rs["MatchStatus"].ToString();
                                data.TossWinner = rs["TossWinner"].ToString();
                                data.MatchCity = rs["MatchCity"].ToString();
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {
                        
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<FantasySummaryAPIDetails>().ToList();
        }

        public static List<GetPlayerIdModel> GetPlayerId(string storedProcName, Dictionary<string, object> parameters = null)
        {

            List<GetPlayerIdModel> responseDictonary = new List<GetPlayerIdModel>();


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
                                GetPlayerIdModel data = new GetPlayerIdModel();
                                data.APIPId = Int32.Parse(rs["APIPId"].ToString());
                                data.RapidPlayerId = Int32.Parse(rs["RapidPlayerId"].ToString());
                                data.PlayerName = rs["PlayerName"].ToString();
                                responseDictonary.Add(data);
                            }
                        }
                    }
                    catch (Exception Excp)
                    {

                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }

            return responseDictonary.Cast<GetPlayerIdModel>().ToList();
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
                        //Logger.Write(_logServiceURI, enmEventType.Error, "Error captured in ExecuteStoredProc method.", string.Format(CultureInfo.InvariantCulture, "Error in ExecuteStoredProc for SPName:{0} ", storedProcName,  Excp.ToString()), "DatabaseHelper.ExecuteStoredProc()", _AppName, _Filename, 52);
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