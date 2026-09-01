using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AccountProductService.Net.Providers.Helpers
{
    public static class Extensions
    {
        public static bool In(this String item, String[] values)
        {
            try
            {
                return values.Any(o => o == item);
            }
            catch
            {
                return false;
            }
        }

        public static bool In(this String item, int[] values)
        {
            try
            {
                return values.Any(o => o.ToString() == item);
            }
            catch
            {
                return false;
            }
        }
    }
}
