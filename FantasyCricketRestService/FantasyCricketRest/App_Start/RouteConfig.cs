# region "Using Directives"

using System.Web.Mvc;
using System.Web.Routing;

# endregion "Using Directives"

namespace FantasyCricketAppRest.AppStart
{
    public static class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            if (routes != null)
            {
                routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

                // Since this is a RESTful service, we will only map one specific default controller for the index of this service
                routes.MapRoute(
                    name: "DefaultController",
                    url: "{controller}/{action}",
                    defaults: new { controller = "Default", action = "Index" }
                );
            }
        }
    }
}
