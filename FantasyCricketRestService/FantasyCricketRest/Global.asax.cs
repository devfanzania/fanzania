using System;
using System.IO;
using System.Text;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Routing;
using FantasyCricketAppRest.AppStart;
using LPL.UI.REST.Core.Helpers;
using Common.Logging.Entities;
using Common.Logger;
using System.Configuration;
using FantasyCricketAppRest.CommonUtilities;
using System.Linq;

namespace FantasyCricketAppRest
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        # region "Fields"
        private static readonly bool DeveloperMode = LplAppSettings.Overrides.DeveloperMode;
        private static readonly bool CorsEnabled = LplAppSettings.Cors.Enabled;
        private static readonly string CorsAllowOrgins = LplAppSettings.Cors.AllowOrigins;
        
        private static string _EnvCode = ConfigurationManager.AppSettings["EnvCode"];
        private static string _AppName = "FantasyCricketAppRestService";
        private static string _Filename = "Global.asax.cs";
        
        #endregion "Fields"

        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            GlobalConfiguration.Configure(WebAPIConfig.Register);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            // Initialize log4net.
            log4net.Config.XmlConfigurator.Configure();

        }

        protected void Application_PostAuthorizeRequest()
        {
        }

        protected void Application_BeginRequest()
        {

            string sInputContents = string.Empty;
            string authToken = "";
            string userId = "";
            string deviceType = "";
            if ((this.Request.Headers["x-api-authtoken"] ?? "").Trim().Length > 0)
                authToken = this.Request.Headers["x-api-authtoken"].ToString().Trim();
            if ((this.Request.Headers["x-api-userId"] ?? "").Trim().Length > 0)
                userId = this.Request.Headers["x-api-userid"].ToString().Trim();
            if ((this.Request.Headers["x-api-devicetype"] ?? "").Trim().Length > 0)
                deviceType = this.Request.Headers["x-api-devicetype"].ToString().Trim();

            LogAuditEntities audit = new LogAuditEntities();
            audit.FileName = _Filename;
            audit.ProductName = _AppName;
            audit.EnvCode = _EnvCode;
            audit.Status = "START";
            audit.Message = this.Request.HttpMethod;
            audit.APIName = this.Request.Path;
            if (authToken == "")
                audit.TransactionId = Guid.NewGuid().ToString();
            else
                audit.TransactionId = authToken;
            audit.TransactionType = deviceType;
            audit.LoginUser = userId;


            //  Let's take a look at the HTTPRequest object...
            //       try to head some errors off in advance of processing...
            //       Like "Application/XML" instead of "Application/JSON"
            try
            {
                
                    // if it's a post and JSON, then grab the body and log it...
                    if (this.Request.HttpMethod.ToUpper() == "POST" && this.Request.ContentType.ToLower() == "application/json")
                    {
                        
                        StreamReader SR1 = null;
                        HttpHelper Helper = null;
                        Helper = HttpHelper.Instance;
                        SR1 = new StreamReader(Helper.HttpRequest.InputStream);
                        sInputContents = SR1.ReadToEnd();
                        Helper.HttpRequest.InputStream.Position = 0;
                        audit.Payload = sInputContents;

                       
                    }
                    else if (this.Request.HttpMethod.ToUpper() == "OPTIONS")
                    {
                       
                        
                    }
                    else  // log that a "GET" or Non-JSON request was received...
                    {
                        
                    }

                Utilities.LogAudit(audit);
                //CommonLogger.Info(audit);

            }
            catch (Exception Excp)
            {
                try
                {
                    LogExceptionEntities error = new LogExceptionEntities();
                    error.FileName = _Filename;
                    error.ProductName = _AppName;
                    error.EnvCode = _EnvCode;
                    error.ErrorCode = "TECHNICAL_ERROR";
                    int strLength = Excp.Message.ToString().Length;
                    error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                    error.StackTrace = Excp.Message.ToString();
                    error.APIName = this.Request.Path;
                    if (authToken == "")
                        error.TransactionId = Guid.NewGuid().ToString();
                    else
                        error.TransactionId = authToken;
                    error.TransactionType = deviceType;
                    error.Payload = sInputContents;
                    error.LoginUser = userId;
                    
                    Utilities.LogException(error);


                }
                catch { }  // do nothing if we get an error here...
            }
        }

        protected void Application_EndRequest()
        {
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            System.Web.HttpContext httpContext = System.Web.HttpContext.Current;

            string userId = httpContext.Request.Headers.Get("x-api-userid");
            if (string.IsNullOrWhiteSpace(userId))
                userId = "";
            string authToken = httpContext.Request.Headers.Get("x-api-authtoken");
            string deviceType = httpContext.Request.Headers.Get("x-api-devicetype");
            if (string.IsNullOrWhiteSpace(authToken))
                authToken = "";
            // Global Exception Handler for unhandled exceptions
            Exception Excp = Server.GetLastError();
            try
            {
                LogExceptionEntities error = new LogExceptionEntities();
                error.FileName = _Filename;
                error.ProductName = _AppName;
                error.EnvCode = _EnvCode;
                error.ErrorCode = this.Request.HttpMethod;
                int strLength = Excp.Message.ToString().Length;
                error.ErrorMessage = Excp.Message.ToString().Substring(0, strLength > 490 ? 490 : strLength);
                error.StackTrace = Excp.Message.ToString();
                error.APIName = this.Request.Path;
                if (authToken == "")
                    error.TransactionId = Guid.NewGuid().ToString();
                else
                    error.TransactionId = authToken;
                error.TransactionType = deviceType;
                error.Payload = this.Request.InputStream.ToString();
                error.LoginUser = userId;
                Utilities.LogException(error);


            }
            catch { }
            Response.Clear();
            Response.ContentType = "application/json";
            Response.ContentEncoding = Encoding.UTF8;
            Response.Write(Excp.Message);
            Response.End();
        }
    }
}
