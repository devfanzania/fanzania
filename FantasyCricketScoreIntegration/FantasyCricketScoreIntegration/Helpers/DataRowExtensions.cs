# region "Using Directives"

using System;
using System.Data;
using System.Globalization;

# endregion "Using Directives"

namespace FantasyCricketAppRest.Extensions
{
    public static class DataRowExtensions
    {
        /// <summary>
        /// Gets the value.
        /// </summary>
        /// <param name="instance">The instance.</param>
        /// <param name="columnName">Name of the column.</param>
        /// <param name="defaultValue"></param>
        /// <returns></returns>
        public static T GetValue<T>(this DataRow instance, string columnName, T defaultValue = default(T))
        {
            var o = instance[columnName];

            try
            {
                Type tType = typeof(T);
                Type conversionType = Nullable.GetUnderlyingType(tType) ?? typeof(T);

                return (o == DBNull.Value) ? defaultValue : (T)Convert.ChangeType(o, conversionType, CultureInfo.InvariantCulture);
            }
            catch
            {
                return defaultValue;
            }
        }
    }
}