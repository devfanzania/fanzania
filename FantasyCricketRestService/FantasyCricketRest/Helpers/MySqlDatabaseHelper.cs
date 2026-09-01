using System;
using System.Collections.Generic;
using System.Linq;
using System.Configuration;
using System.Dynamic;
using System.Globalization;
using Common.Logging.Entities;
using Common.Logger;
using MySql.Data.MySqlClient;

namespace FantasyCricketAppRest.DBHelpers
{
    
    public  class MySqlDBConnect
    {
        private MySqlConnection connection;
        internal static readonly string MySqlConnectionString =
             ConfigurationManager.ConnectionStrings["FANTASYCRICKETMYSQL"].ConnectionString;
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "DatabaseHelper.cs";
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];

        public MySqlDBConnect()
        {
            Initialize();
        }

        private void Initialize()
        {
            connection = new MySqlConnection(MySqlConnectionString);
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
        public static dynamic MySqlExecuteStoredProc(string storedProcName, Dictionary<string, object> parameters = null)
        {
            return MySqlExecuteStoredProc(storedProcName, parameters,null);
        }
        public static dynamic MySqlExecuteStoredProc(string storedProcName, Dictionary<string, object> parameters = null,  string[] sections = null)
        {

            dynamic complexReturns = new ExpandoObject();
            var dataDictionary = (IDictionary<string, object>)complexReturns;
            int iteration = 0;

            List<dynamic> data = new List<dynamic>();

            using (MySqlConnection conn = new MySqlConnection(MySqlConnectionString))
            {
                conn.Open();
                using (MySqlCommand cmd = new MySqlCommand(storedProcName, conn))
                {
                    try
                    {
                        cmd.CommandType = System.Data.CommandType.StoredProcedure;
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }
                        MySqlDataReader rs = cmd.ExecuteReader(System.Data.CommandBehavior.CloseConnection);

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
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "DB_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = storedProcName;
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "REST";
                        //error.Payload = sInputContents;
                        // error.LoginUser = ;
                        CommonLogger.Error(error);
                        
                    }
                    finally
                    {
                        conn.Close();
                    }
                }
            }

            if (iteration == 1)
                return data;
            else
                return complexReturns;


        }

        public static dynamic MySqlExecuteScalerQuery(dynamic selectQuery, Dictionary<string, object> parameters = null, string[] sections = null)
        {

            dynamic complexReturns = new ExpandoObject();
            var dataDictionary = (IDictionary<string, object>)complexReturns;
            int iteration = 0;

            List<dynamic> data = new List<dynamic>();

            using (MySqlConnection conn = new MySqlConnection(MySqlConnectionString))
            {
                conn.Open();
                using (MySqlCommand cmd = conn.CreateCommand())
                {
                    try
                    {
                        
                        foreach (var item in parameters)
                        {
                            cmd.Parameters.AddWithValue(item.Key, IfNull(item.Value));
                        }
                        cmd.CommandText = selectQuery.ToString();
                        cmd.CommandType = System.Data.CommandType.Text;
                        MySqlDataReader rs = cmd.ExecuteReader(System.Data.CommandBehavior.CloseConnection);

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
                        LogExceptionEntities error = new LogExceptionEntities();
                        error.FileName = _Filename;
                        error.ProductName = _AppName;
                        error.EnvCode = _EnvCode;
                        error.ErrorCode = "DB_ERROR";
                        int strLength = Excp.Message.ToString().Length;
                        error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                        error.StackTrace = Excp.Message.ToString();
                        error.APIName = "INLINE_SQL";
                        error.TransactionId = Guid.NewGuid().ToString();
                        error.TransactionType = "REST";
                        error.Payload = selectQuery;
                        // error.LoginUser = ;
                        CommonLogger.Error(error);
                       
                    }
                    finally
                    {
                        conn.Close();
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


        public static T FillObjectWithRecordSet<T>(T obj, MySqlDataReader rs)
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

        public static dynamic FillObjectWithRecordSet(MySqlDataReader rs)
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