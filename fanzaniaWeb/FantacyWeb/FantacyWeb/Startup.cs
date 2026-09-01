using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(FantacyWeb.Startup))]
namespace FantacyWeb
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
