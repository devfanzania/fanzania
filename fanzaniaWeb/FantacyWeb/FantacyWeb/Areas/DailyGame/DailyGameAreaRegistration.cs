using System.Web.Mvc;

namespace FantacyWeb.Areas.DailyGame
{
    public class DailyGameAreaRegistration : AreaRegistration 
    {
        public override string AreaName 
        {
            get 
            {
                return "DailyGame";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context) 
        {
            context.MapRoute(
                "DailyGame_default",
                "DailyGame/{controller}/{action}/{id}",
                new { action = "Index", id = UrlParameter.Optional }
            );
        }
    }
}