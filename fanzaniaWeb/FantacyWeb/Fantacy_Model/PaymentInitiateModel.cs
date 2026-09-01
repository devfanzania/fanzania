using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
   public  class PaymentInitiateModel
    {
        public string name { get; set; }
        public string email { get; set; }
        public string contactnumber { get; set; }
        public string address { get; set; }
        public float  amount { get; set; }
        public string currency { get; set; }

    }
}
