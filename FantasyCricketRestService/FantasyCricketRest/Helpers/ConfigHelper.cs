using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AccountProductService.Net.Providers.Helpers
{
    public static class ConfigHelper
    {
        public static string GetKey(string keyName)
        {
            return ConfigurationManager.AppSettings[keyName];

        }
    }
}
