# region "Using Directives"

using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web.Http;
using System.Net;
using System.Web.Http.Controllers;
using LPL.UI.REST.Core.Helpers;
using LPL.UI.REST.Core.Models;
using LPL.UI.REST.Core.Entities;


# endregion "Using Directives"

namespace FantasyCricketAppRest.Controllers
{
    
    public abstract class FantasyCricketAppRestBaseController : ApiController
    {
        # region "Fields"

        private readonly ContextMap _contextMap = ContextMap.Instance;

        # endregion "Fields"

        # region "Properties"
     

        /// <summary>
        /// HttpHelper instance
        /// </summary>
        protected HttpHelper Http
        {
            get { return HttpHelper.Instance; }
        }

        /// <summary>
        /// CookieHelper instance
        /// </summary>
        protected CookieHelper Cookies
        {
            get { return CookieHelper.Instance; }
        }

        /// <summary>
        /// ContextMap instance
        /// </summary>
        protected ContextMap ContextMap
        {
            get { return ContextMap.Instance; }
        }

        /// <summary>
        /// The domain
        /// </summary>
        protected string Domain
        {
            get
            {
                string host = Http.HttpRequest.Url.Host;
                return host;
            }
        }

        /// <summary>
        /// Can represent any data associated with a given user
        /// </summary>
        protected string UserData
        {
            get { return ContextMap.UserData; }
        }

        
        # endregion "Properties"

        # region "Methods"
        /// <summary>
        /// Executes an HTTP operation Asynchronously
        /// </summary>
        /// <param name="controllerContext"></param>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public override Task<HttpResponseMessage> ExecuteAsync(HttpControllerContext controllerContext,
            CancellationToken cancellationToken)
        {
            
            return base.ExecuteAsync(controllerContext, cancellationToken);
        }

        /// <summary>
        /// Initializes the API controller
        /// </summary>
        /// <param name="controllerContext"></param>
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);

            // Note that the ExecuteAsync Method fires before this one in the call order
        }

        /// <summary>
        /// Returns a JsonResponse
        /// </summary>
        /// <param name="httpStatusCode"></param>
        /// <param name="status"></param>
        /// <param name="statusMessage"></param>
        /// <returns></returns>
    
        protected JsonResponse JsonResponse(HttpStatusCode? httpStatusCode,ResponseStatus status,string statusMessage)
        {
            return JsonResponseFactory.Create(httpStatusCode, status, statusMessage);
        }

        /// <summary>
        /// Returns a JsonResponse
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="data"></param>
        /// <param name="httpStatusCode"></param>
        /// <param name="status"></param>
        /// <param name="statusMessage"></param>
        /// <returns></returns>
        protected JsonResponse JsonResponse<T>(
            T data = null,
            HttpStatusCode? httpStatusCode = null,
            ResponseStatus status = null,
            string statusMessage = null) where T : class
        {
            return JsonResponseFactory.Create<T>(
                data,
                httpStatusCode,
                status,
                statusMessage);
        }

        # endregion "Methods"
    }
}
