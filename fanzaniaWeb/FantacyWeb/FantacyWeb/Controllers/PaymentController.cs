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


namespace FantacyWeb.Controllers
{
    public class PaymentController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        // GET: Payment
        public ActionResult Index()
        {
            return View();
        }
        [HttpPost]
        [ActionName("CreateOrder")]
        [AllowAnonymous]
        public ActionResult CreateOrder(PaymentInitiateModel _requestdata)
        {
            Random randomObj = new Random();
            string transactionId = randomObj.Next(10000000, 100000000).ToString();

            //Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient("rzp_test_lTgA607AThK4CZ", "018mdJ7azhGvj7rBimxWmYx8");
            Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient(Session["client_id"].ToString(), Session["client_secret"].ToString());
            Dictionary<string, object> options = new Dictionary<string, object>();
            options.Add("amount", _requestdata.amount*100);  // Amount will in paise
            options.Add("receipt", transactionId);
            options.Add("currency", _requestdata.currency);
             options.Add("payment_capture", "0"); // 1 - automatic  , 2 - manual
            //options.Add("notes", "-- You can put any notes here --");
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            Razorpay.Api.Order orderResponse = client.Order.Create(options);
            //order create api 
            string orderId = orderResponse["id"].ToString();
          //  try
        //    {
          //  Razorpay.Api.Order orderResponse = client.Order.Create(options);
         //   string orderId = orderResponse["id"].ToString();
            //    Razorpay.Api.Order order = client.Order.Create(options);
               // string   orderId = order["id"].ToString();
        //    }
           // catch (Exception ex)
         //   {
              //  Console.WriteLine(ToString());
         //   }
            // Create order model for return on view
            OrderModel orderModel = new OrderModel
            {
                orderId = orderResponse.Attributes["id"],
                razorpayKey = Session["client_id"].ToString(),
                amount = _requestdata.amount * 100,
                currency = _requestdata.currency,
                name = _requestdata.name,
                email = _requestdata.email,
                contactNumber = _requestdata.contactnumber,
                address = _requestdata.address,
                description = "Testing description",
                receipt = transactionId
            };

            // Return on PaymentPage with Order data
            return Json(orderModel, JsonRequestBehavior.AllowGet);
        //    return View( orderModel);
           // return View();
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
        [ActionName("GetSubscriptionDetails")]
        [AllowAnonymous]
        public ActionResult GetSubscriptionDetails(SubscriptionDetails Pmodel)
        {

            try
            {
                DefaultResponseSubscriptionDetails LR = new DefaultResponseSubscriptionDetails();
                // Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.GetSubscriptionDetails(Pmodel);

                if (LR.status == "success")
                {
                    Session["client_id"] = LR.data.client_id;
                    Session["client_secret"] = LR.data.client_secret;
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