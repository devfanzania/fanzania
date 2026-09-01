using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Fantacy_Model;
using System.Web.Mvc;
using System.Net;
using Fantacy_Model;
using Fantacy_Model.DailyGame;
using FantacyWeb.Models;
using System.Net.Http;
using DocumentFormat.OpenXml.EMMA;
using System.Threading.Tasks;

namespace FantacyWeb.Controllers
{
    public class CashFreePaymentController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        // GET: Payment
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet]
        [ActionName("fetch-order-details")]
        [AllowAnonymous]
        public async Task<JsonResult> FetchOrderStatus(string orderId)
        {
            //   string cashfreeUrl = $"https://sandbox.cashfree.com/pg/orders/{orderId}";

            string baseUrl = Session["CashfreeURL"]?.ToString();
            if (string.IsNullOrEmpty(baseUrl))
            {
                return Json(new { error = "Cashfree URL not found in session" }, JsonRequestBehavior.AllowGet);
            }

            // Append session value before the existing URL
            string cashfreeUrl = $"{baseUrl}pg/orders/{orderId}";

            string clientId = Session["PGClientId"].ToString();
            string clientSecret = Session["PGClientSecret"].ToString();

            using (HttpClient client = new HttpClient())
            {
                // Ensure TLS 1.2 is used (default in most systems but can be explicitly set if necessary)
                client.DefaultRequestHeaders.Add("x-client-id", clientId);
                client.DefaultRequestHeaders.Add("x-client-secret", clientSecret);
                client.DefaultRequestHeaders.Add("x-api-version", "2025-01-01");

                try
                {
                    HttpResponseMessage response = await client.GetAsync(cashfreeUrl);
                    if (response.IsSuccessStatusCode)
                    {
                        string result = await response.Content.ReadAsStringAsync();
                        var jsonResponse = Newtonsoft.Json.JsonConvert.DeserializeObject<dynamic>(result);

                        OrderDetails Pmodel = new OrderDetails
                        {
                            UserId = jsonResponse.customer_details.customer_id,
                            cf_order_id = jsonResponse.cf_order_id,
                            customer_name = jsonResponse.customer_details.customer_name,
                            customer_email = jsonResponse.customer_details.customer_email,
                            customer_phone = jsonResponse.customer_details.customer_phone,
                            order_amount = jsonResponse.order_amount,
                            order_currency = jsonResponse.order_currency,
                            payment_methods = jsonResponse.payment_methods,
                            order_note = jsonResponse.order_note,
                            order_status = jsonResponse.order_status,
                            payment_session_id = jsonResponse.payment_session_id,
                            order_id = orderId
                        };

                        AddMoneyPaymentdetails LR = ARS.AddMoneyPaymentdetails(Pmodel);

                       // Console.WriteLine(result);
                        return await Task.FromResult(Json(Pmodel, JsonRequestBehavior.AllowGet));
                 //       return Json(jsonResponse, JsonRequestBehavior.AllowGet);// Or process the response as needed
                    }
                    else
                    {
                        return Json(new { error = $"Error: {response.StatusCode}" }, JsonRequestBehavior.AllowGet);
                        Console.WriteLine($"Error: {response.StatusCode}");
                    }
                }
                catch (HttpRequestException ex)
                {
                    return Json(new { error = $"Request failed: {ex.Message}" }, JsonRequestBehavior.AllowGet);
                    //   Console.WriteLine($"Request failed: {ex.Message}");
                }
            }
        }

        [HttpPost]
        [ActionName("CreateOrder")]
        [AllowAnonymous]
        public ActionResult CreateOrder(CashFreePaymentInitiateModel _requestdata)
        {
            if (_requestdata == null)
            {
                return Json(new { success = false, message = "Request data is null or invalid." });
            }

            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;

             string clientId = Session["PGClientId"].ToString();
             string clientSecret = Session["PGClientSecret"].ToString();
            //   string clientId = "TEST10437118b618ae3596dfeab45d9781173401";
            //  string clientSecret = "cfsk_ma_test_addcd1a2680ec6c391e2c43b78d54004_65601cdb";
            //  string endpoint = "https://sandbox.cashfree.com/pg/orders";

            string baseUrl = Session["CashfreeURL"]?.ToString();
            string endpoint = $"{baseUrl}pg/orders";
            string transactionId = "ORDER" + new Random().Next(10000000, 99999999);
            string userId = Session["UserId"].ToString();
            string jsonData = $@"
            {{
                ""order_id"": ""{transactionId}"",
                ""order_amount"": {_requestdata.amount},
                ""order_currency"": ""{_requestdata.currency}"",
                ""customer_details"": {{
                 ""customer_id"": ""{userId}"",
                    ""customer_name"": ""{_requestdata.username}"",
                    ""customer_email"": ""{_requestdata.email}"",
                    ""customer_phone"": ""{_requestdata.phonenumber}""
                }},
                ""order_note"": ""Test Payment"",
                ""notify_url"": ""https://yourdomain.com/payment/webhook""
            }}";

            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(endpoint);
                request.Method = "POST";
                request.ContentType = "application/json";
                request.Headers["x-client-id"] = clientId;
                request.Headers["x-client-secret"] = clientSecret;
                request.Headers["x-api-version"] = "2025-01-01";
                byte[] payloadBytes = System.Text.Encoding.UTF8.GetBytes(jsonData);
                using (var requestStream = request.GetRequestStream())
                {
                    requestStream.Write(payloadBytes, 0, payloadBytes.Length);
                }
                using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
                {
                    using (var reader = new System.IO.StreamReader(response.GetResponseStream()))
                    {
                        string responseBody = reader.ReadToEnd();

                        // Log response for debugging
                        System.Diagnostics.Debug.WriteLine($"Cashfree Response: {responseBody}");

                        try
                        {
                            // Deserialize JSON to check contents
                            var jsonResponse = Newtonsoft.Json.JsonConvert.DeserializeObject<dynamic>(responseBody);

                            if (jsonResponse != null && jsonResponse.order_status == "ACTIVE")
                            {
                                string orderId = jsonResponse.order_id;
                                string paymentLink = jsonResponse.payment_link;

                                //AddMoneyPaymentdetails LR = new AddMoneyPaymentdetails();
                                 string UserId_ = Session["UserId"].ToString();
                                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                                //  AddMoneyPaymentdetails LR = new AddMoneyPaymentdetails();


                                // LR = ARS.AddMoneyPaymentdetails(Pmodel);

                                OrderDetails Pmodel = new OrderDetails
                                {
                                    UserId = UserId_, // UserId from session
                                    cf_order_id = jsonResponse.cf_order_id,
                                    customer_name = jsonResponse.customer_details.customer_name,
                                    customer_email = jsonResponse.customer_details.customer_email,
                                    customer_phone = jsonResponse.customer_details.customer_phone,
                                    order_amount = jsonResponse.order_amount,
                                    order_currency = jsonResponse.order_currency,
                                    payment_methods = jsonResponse.payment_methods,
                                    order_note = jsonResponse.order_note,
                                    order_status = jsonResponse.order_status,
                                    payment_session_id = jsonResponse.payment_session_id,
                                    order_id = orderId
                                };

                                //// Call the AddMoneyPaymentdetails method to process the order
                                //AddMoneyPaymentdetails LR = ARS.AddMoneyPaymentdetails(Pmodel);
                                return Json(Pmodel, JsonRequestBehavior.AllowGet);

                                //if (LR.status == "success")
                                //   {
                                //       return Json(LR, JsonRequestBehavior.AllowGet);
                                //   }
                                //   else
                                //   {
                                //       return RedirectToAction("Error", "Home");
                                //   }


                                //return Json(new
                                //{
                                //    success = true,
                                //    orderId = orderId,
                                //    paymentLink = paymentLink,
                                //    clientId = clientId
                                //}, JsonRequestBehavior.AllowGet);
                            }
                            else
                            {
                                // Check for specific error messages
                                string errorMessage = jsonResponse?.message ?? "Unexpected response from Cashfree.";
                                throw new Exception($"Cashfree error: {errorMessage}");
                            }
                        }
                        catch (Exception ex)
                        {
                            // Fallback for unexpected errors
                            throw new Exception($"Failed to process response: {responseBody}", ex);
                        }
                    }
                }
               
            }
            catch (WebException ex)
            {
                using (var response = (HttpWebResponse)ex.Response)
                {
                    using (var reader = new System.IO.StreamReader(response.GetResponseStream()))
                    {
                        string responseBody = reader.ReadToEnd();
                        return Json(new { success = false, message = $"HTTP Error: {responseBody}" });
                    }
                }
            }
        }
        private string ExtractField(string json, string fieldName)
        {
            int startIndex = json.IndexOf($"\"{fieldName}\":\"");
            if (startIndex == -1) return null;

            startIndex += fieldName.Length + 3;
            int endIndex = json.IndexOf("\"", startIndex);
            return endIndex == -1 ? null : json.Substring(startIndex, endIndex - startIndex);
        }
        public class OrderModel {
            public string orderId { get; set; }
            public string razorpayKey { get; set; }
            public float amount { get; set; }
            public string currency { get; set; }
            public string name { get; set; }
            public string email { get; set; }
            public string contactNumber { get; set; }
            public string address { get; set; }
            public string description { get; set; }
            public string receipt { get; set; }
            
        }

        [HttpPost]
        public ActionResult Complete()
        { // Payment data comes in url so we have to get it from url

            // This id is razorpay unique payment id which can be use to get the payment details from razorpay server
            string paymentId = Request.Params["rzp_paymentid"];

            // This is orderId
            string orderId = Request.Params["rzp_orderid"];
            Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient(Session["client_id"].ToString(), Session["client_secret"].ToString());

      //      Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient("rzp_test_lTgA607AThK4CZ", "018mdJ7azhGvj7rBimxWmYx8");

            Razorpay.Api.Payment payment = client.Payment.Fetch(paymentId);

            // This code is for capture the payment 
            Dictionary<string, object> options = new Dictionary<string, object>();
            options.Add("amount", payment.Attributes["amount"]);
            Razorpay.Api.Payment paymentCaptured = payment.Capture(options);
            string amt = paymentCaptured.Attributes["amount"];

            //// Check payment made successfully

            if (paymentCaptured.Attributes["status"] == "captured")
            { 
                //payment success
                // Create these action method
                return RedirectToAction("Success");
            }
            else
            {
                return RedirectToAction("Failed");
            }


        }
        public ActionResult Success()
        {
            return View();
        }

        public ActionResult Failed()
        {
            return View();
        }
        public ActionResult Subscription() {
            if (Session["UserName"] != null)
            {
                ViewBag.UserName = Session["UserName"].ToString();
                ViewBag.Email = Session["Email"].ToString();
                //if (!string.IsNullOrEmpty(Session["PhoneNumber"].ToString())) { 
                //  ViewBag.PhoneNumber = Session["PhoneNumber"].ToString();
                //}
                //    ViewBag.PhoneNumber = Session["PhoneNumber"].ToString();
                //ViewBag.PhoneNumber = '';
                ViewBag.Page = "Subscription";
            }
                return View();
        }
        [HttpPost]
        [ActionName("FetchPaymentGatewayDetails")]
        [AllowAnonymous]
        public ActionResult FetchPaymentGatewayDetails(PaymentGetwayDetails Pmodel)
        {

            try
            {
                DefaultResponseGetWayDetails LR = new DefaultResponseGetWayDetails();
                // Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.FetchPaymentGatewayDetails(Pmodel);

                if (LR.status == "success")
                {
                    var cashfreeDetails = LR.data.FirstOrDefault(pg => pg.PaymentGatewayName == "cashfree");
                    // Session["client_id"] = LR.data;
                    //Session["client_secret"] = LR.data;
                    Session["PGClientId"] = cashfreeDetails.PGClientId;
                    Session["PGClientSecret"] = cashfreeDetails.PGClientSecret;
                    Session["CashfreeEnv"] = cashfreeDetails.Environment.ToLower();
                    Session["CashfreeURL"] = cashfreeDetails.URL;
                    return Json(LR.data, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    return RedirectToAction("Error", "Home");
                }

            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("UpdateSubscriptionDetails")]
        [AllowAnonymous]
        public ActionResult UpdateSubscriptionDetails(UpdateSub Pmodel)
        {

            try
            {
                SubResponce LR = new SubResponce();
                // Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UpdateSubscriptionDetails(Pmodel);

                if (LR.status == "success")
                {
                    return Json(LR, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    return RedirectToAction("Error", "Home");
                }

            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

    }
    
}