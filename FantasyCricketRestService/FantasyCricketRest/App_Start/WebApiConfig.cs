# region "Using Directives"

using System.Linq;
using System.Web.Http;
using System.Net.Http.Formatting;
using System.Web.Http.Cors;


# endregion "Using Directives"

namespace FantasyCricketAppRest.AppStart
{
    public static class WebAPIConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Web API configuration and services
            if (config != null)
            {
                var cors = new EnableCorsAttribute("*", "*", "*");
                cors.SupportsCredentials = true;
                config.EnableCors(cors);

                // Web API routes
                config.MapHttpAttributeRoutes();

                // This must be here in order for browsers like Chrome to accept the response as JSON.
                var appXmlType = config.Formatters.XmlFormatter.SupportedMediaTypes.FirstOrDefault(t => t.MediaType == "application/xml");
                config.Formatters.XmlFormatter.SupportedMediaTypes.Remove(appXmlType);

                config.Formatters.JsonFormatter.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
                config.Formatters.JsonFormatter.SerializerSettings.NullValueHandling = Newtonsoft.Json.NullValueHandling.Include;

                config.Formatters.JsonFormatter.AddQueryStringMapping("$format", "json", "application/json");
                config.Formatters.XmlFormatter.AddQueryStringMapping("$format", "xml", "text/xml");
            }
        }
    }
}
