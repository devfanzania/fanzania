using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Globalization;
using System.Net.Mail;
using System.Net;
using Common.Logging.Entities;
using Common.Logger;
using System.Configuration;


namespace FantasyCricketAppRest.CommonUtilities
{

    public static class Utilities
    {
        // private static string _logServiceURI = null;
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "CommonUtilities.cs";
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        
        private static string _smtpServer = ConfigurationManager.AppSettings["SMTPSERVER"];
        private static string _smtpPort = ConfigurationManager.AppSettings["SMTPPORT"];
        private static string _EmailUser = ConfigurationManager.AppSettings["EmailUser"];
        private static string _EmailPassword = ConfigurationManager.AppSettings["EmailUserPassword"];
        private static string _FromEmail = ConfigurationManager.AppSettings["FromEmail"];
        private static string _server = Environment.MachineName;
        //private static readonly log4net.ILog ErrorLog = log4net.LogManager.GetLogger("ExceptionLogger");


        static Utilities()
        {
            
        }
        public static bool SendMail(String to, String subject, String body, string APIType)

        {
            bool retVal = false;
            string SMTPSERVER = _smtpServer;
            int SMTPPORT = Int32.Parse(_smtpPort);
            string USERNAME = _EmailUser;
            string PASSWORD = _EmailPassword;
            string FROMEMAIL = _FromEmail;

            MailMessage msg = new MailMessage();

            msg.From = new MailAddress(FROMEMAIL);
            msg.To.Add(to);
            msg.Subject = subject;
            msg.IsBodyHtml = true;
            msg.Body = body;
            msg.Priority = MailPriority.High;

            try
            {
                SmtpClient mailClient = new SmtpClient();
                mailClient.UseDefaultCredentials = false;
                mailClient.Credentials = new NetworkCredential(USERNAME, PASSWORD);
                mailClient.Host = SMTPSERVER;
                mailClient.Port = SMTPPORT;
                //mailClient.DeliveryFormat = SmtpDeliveryFormat.International;
                mailClient.EnableSsl = true;
                mailClient.DeliveryMethod = SmtpDeliveryMethod.Network;
                System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
                mailClient.Send(msg);
                retVal = true;
            }
            catch (Exception Excp)
            {

                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = "ERROR_EMAIL_SENT";
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = APIType;
                error.TransactionId = Guid.NewGuid().ToString();
                error.TransactionType = "REST";
                //error.Payload = sInputContents;
                error.LoginUser = to;
                LogException(error);

                retVal = false;
            }

            return retVal;
        }
        public static void LogException(LogExceptionEntities error)
        {
            log4net.ILog ErrorLog = log4net.LogManager.GetLogger("ExceptionLogger");
            log4net.GlobalContext.Properties.Clear();
            log4net.GlobalContext.Properties["envCode"] = error.EnvCode;
            log4net.GlobalContext.Properties["productName"] = error.ProductName;
            log4net.GlobalContext.Properties["apiName"] = error.APIName;
            log4net.GlobalContext.Properties["fileName"] = error.FileName;
            log4net.GlobalContext.Properties["loginUser"] = error.LoginUser;
            log4net.GlobalContext.Properties["hostName"] = _server;
            log4net.GlobalContext.Properties["transactionId"] = error.TransactionId;
            log4net.GlobalContext.Properties["transactionType"] = error.TransactionType;
            log4net.GlobalContext.Properties["errorCode"] = error.ErrorCode;
            log4net.GlobalContext.Properties["errorMessage"] = error.ErrorMessage;
            log4net.GlobalContext.Properties["stackTrace"] = error.StackTrace;
            log4net.GlobalContext.Properties["payload"] = error.Payload;
            ErrorLog.Error("log4net");
        }

        public static void LogAudit(LogAuditEntities audit)
        {
            log4net.GlobalContext.Properties.Clear();
            log4net.GlobalContext.Properties["envCode"] = audit.EnvCode;
            log4net.GlobalContext.Properties["productName"] = audit.ProductName;
            log4net.GlobalContext.Properties["apiName"] = audit.APIName;
            log4net.GlobalContext.Properties["fileName"] = audit.FileName;
            log4net.GlobalContext.Properties["loginUser"] = audit.LoginUser;
            log4net.GlobalContext.Properties["hostName"] = _server;
            log4net.GlobalContext.Properties["transactionId"] = audit.TransactionId;
            log4net.GlobalContext.Properties["transactionType"] = audit.TransactionType;
            log4net.GlobalContext.Properties["status"] = audit.Status;
            log4net.GlobalContext.Properties["message"] = audit.Message;
            log4net.GlobalContext.Properties["payload"] = audit.Payload;
            CommonLogger.Info(audit);
        }
    }
}