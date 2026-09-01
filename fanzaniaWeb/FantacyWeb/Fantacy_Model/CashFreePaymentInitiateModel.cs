using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class CashFreePaymentInitiateModel
    {
        public string username { get; set; }
        public string email { get; set; }          // User's email as a string
        public string phonenumber { get; set; }    // Phone number as a string (to allow leading zeros)
        public decimal amount { get; set; }        // Amount as a decimal for precision with currency
        public string currency { get; set; }       // Currency code as a string (e.g., "INR", "USD")
        public string description { get; set; }    // Payment description as a string

    }
}
