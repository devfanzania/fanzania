# region "Using Directives"

using System;

# endregion "Using Directives"

namespace FantasyCricketAppRest.Extensions
{
    public static class ObjectExtensions
    {
        public static void ThrowIfNull(this object item, string exceptionMessage)
        {
            if (item == null)
            {
                throw new ArgumentNullException(exceptionMessage);
            }
        }    
    }
}