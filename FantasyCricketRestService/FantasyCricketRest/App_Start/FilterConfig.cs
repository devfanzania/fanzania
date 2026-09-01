# region "Using Directives"

using System.Web.Mvc;

# endregion "Using Directives"

namespace FantasyCricketAppRest.AppStart
{
    public static class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            if(filters != null)
                filters.Add(new HandleErrorAttribute());
        }
    }
}
