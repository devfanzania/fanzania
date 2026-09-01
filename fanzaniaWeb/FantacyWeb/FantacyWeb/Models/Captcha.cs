using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FantacyWeb.Models
{
    public class Captcha
    {
        public bool captchaSet { get; set; }
        private readonly Random _random = new Random();
        public int result { get; set; }
        public char[] operators = { '+', '-', '*', '/' };
        public int RandomNumber(int min = 1, int max = 9)
        {
            return _random.Next(min, max);
        }
        public Captcha()
        {
            result = 0;
            captchaSet = false;
        }
        public string[] genCaptcha()
        {
            int param1 = RandomNumber();
            int param2 = RandomNumber(1, param1);
            int opRand = RandomNumber(0, 4);
            char oper = operators[opRand];
            switch (oper)
            {
                case '+':
                    result = param1 + param2;
                    break;
                case '-':
                    result = param1 - param2;
                    break;
                case '*':
                    result = param1 * param2;
                    break;
                case '/':
                    result = param1 / param2;
                    if (param1 % param2 != 0)
                        param1 = (param1 / param2) * param2;
                    break;
                default:
                    break;
            }
            captchaSet = true;
            string[] dataToSend = new string[3];
            dataToSend[0] = param1.ToString();
            dataToSend[1] = " " + oper.ToString() + " ";
            dataToSend[2] = param2.ToString();
            return dataToSend;
        }
    }

}