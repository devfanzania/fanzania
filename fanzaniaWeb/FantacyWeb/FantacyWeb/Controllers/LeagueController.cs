using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;
using System.Net;
namespace FantacyWeb.Controllers
{
    public class LeagueController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
       
        [AllowAnonymous]
        public ActionResult Index(string LId,string TId, string type)
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    Session["USERSELECTION"] = "League";
                    Session["USERSELECTIONMODE"] = "T";
                    ViewBag.Page = "League";
                    ViewBag.UserName = Session["UserName"].ToString();
                    if ((LId == "" || LId == null) && (TId == "" || TId == null) && (type == "" || type == null))
                    {
                        Session["LeagueId"] = "0";
                        Session["TournamentId"] = "0";
                        ViewBag.hdnFlag = "";
                    }
                    else
                    {
                        Session["LeagueId"] = LId;
                        Session["TournamentId"] = TId;
                        if (Session["UserId"].ToString() == type)
                        {
                            ViewBag.hdnFlag = "owner";
                        }
                        else
                        {
                            ViewBag.hdnFlag = "other";
                        }

                    }
                    return View();
                }
                else
                {
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("GetOwner")]
        public ActionResult GetOwner(string type)
        {
            try
            {
                if (Session["UserId"].ToString() == type)
                {
                    Session["LeagueOwner"] = "owner";
                    ViewBag.hdnFlag = "owner";
                }
                else
                {
                    Session["LeagueOwner"] = "other";
                    ViewBag.hdnFlag = "other";
                }
                return View();
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        [ActionName("GetLeagueTeams")]

        public ActionResult GetLeagueTeams(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                var records = new GridModel().GetLeagueTeams(page, limit, sortBy, direction, pModel, out total);
                //var records = list.Where(s => s.Status == "Approved");
                for (int i = 0; i < records.Count; i++)
                {
                    if (records[i].UserId == Session["UserId"].ToString())
                    {
                        records[i].OwnerTeam = "True";
                    }
                    else
                    {
                        records[i].OwnerTeam = "False";
                    }
                }
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpGet]
        [ActionName("GetLeagueTeamsForManage")]

        public ActionResult GetLeagueTeamsForManage(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                var records = new GridModel().GetLeagueTeams(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("UserActiveTournament")]
        [AllowAnonymous]
        public ActionResult UserActiveTournament()
        {
            try
            {
                UserTournamentResponse LR = new UserTournamentResponse();
                List<UserTournamentDataResponse> TDetail = new List<UserTournamentDataResponse>();
                List<UserTournamentDataResponse> SortedList = new List<UserTournamentDataResponse>();
                ParamModel lModel = new ParamModel();
                lModel.UserId = Session["UserId"].ToString();
                lModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.GetUserTournament(lModel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();
                   
                    var Inprogresslist = TDetail.Where(x => x.TournamentStatus == "INPROGRESS" || x.TournamentStatus == "BREAK").ToList();
                    var Upcomminglist = TDetail.Where(x => x.TournamentStatus == "UPCOMING").ToList();
                    var Finishlist = TDetail.Where(x => x.TournamentStatus == "COMPLETE").ToList();
                    SortedList = Inprogresslist.Concat(Upcomminglist).ToList();
                    SortedList = SortedList.Concat(Finishlist).ToList();
                }

                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("LoadUserActiveLeagueInfo")]

        public ActionResult LoadUserActiveLeagueInfo(ParamModel pModel)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                List<LeagueDataResponse> TDetail = new List<LeagueDataResponse>();
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UserActiveLeagueInfo(pModel);
                if (LR.status == "success")
                {
                    //Status
                    TDetail = LR.data.ToList();
                    TDetail = TDetail.Where(a => a.Status == "Approved").ToList();
                }
                return Json(TDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("CheckLeagueName")]
        [AllowAnonymous]
        public ActionResult CheckLeagueName(ParamModel PM)
        {
            try
            {
                LeagueResponse RM = new LeagueResponse();
                RM = ARS.VerifyLeagueName(PM);
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("CreateLeague")]
        [AllowAnonymous]
        public ActionResult CreateLeague(ParamModel PM)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                PM.LeagueLeaderId = Session["UserId"].ToString();
                LR = ARS.CreateLague(PM);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("ExitLeague")]
        [AllowAnonymous]
        public ActionResult ExitLeague(ParamModel PM)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                PM.UserId = Session["UserId"].ToString();
                LR = ARS.Exit_League(PM);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("JoinLeague")]
        [AllowAnonymous]
        public ActionResult JoinLeague(ParamModel PM)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                PM.UserId = Session["UserId"].ToString();
                LR = ARS.Join_League(PM);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("ChangeLeagueName")]
        [AllowAnonymous]
        public ActionResult ChangeLeagueName(ParamModel PM)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                PM.UserId = Session["UserId"].ToString();
                LR = ARS.ChangeLeagueName(PM);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        //Manage League -------------------------------------------------------

        [AllowAnonymous]
        public ActionResult ManageLeague(string LId, string TId, string Tst)
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    if (LId != null && TId != null)
                    {
                        Session["ManageLeagueId"] = LId;
                        Session["TournamentId"] = TId;
                        Session["TournamentStatus"] = Tst;
                    }
                    return View();
                }
                else
                {
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("ResetShareCode")]

        public ActionResult ResetShareCode(ParamModel pModel)
        {
            try
            {
                LeagueResponse LR = new LeagueResponse();
                List<LeagueDataResponse> TDetail = new List<LeagueDataResponse>();
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.ResetShareCode(pModel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();
                }
                return Json(TDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("ActionAdmin")]

        public ActionResult ActionAdmin(ParamModel pModel)
        {
            try
            {
                DefaultResponse LR = new DefaultResponse();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                if (pModel.Type == "A")
                {
                    LR = ARS.Approve_league_users(pModel);
                }
                else
                {
                    LR = ARS.Unapprove_league_users(pModel);
                }
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("ShowCurrentTeam")]
        [AllowAnonymous]
        public ActionResult ShowCurrentTeam(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = ARS.CurrentTeamPlayerDetails(Pmodel);
                if (PR.status == "success")
                {
                    PlayerList = PR.data.ToList();

                }
                return Json(PlayerList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }
        [HttpPost]
        [ActionName("ShowLastPlayedTeam")]
        [AllowAnonymous]
        public ActionResult ShowLastPlayedTeam(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                PR = ARS.LastPlayedTeamPlayerDetails(Pmodel);
                if (PR.status == "success")
                {
                    PlayerList = PR.data.ToList();
                }
                return Json(PlayerList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        // League Stats ..........................................................................

        [AllowAnonymous]
        public ActionResult LeagueStat()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    ViewBag.UserName = Session["UserName"].ToString();
                    Session["Lid_Stat"] = Request.QueryString["LId"].ToString();
                    Session["Tid_Stat"] = Request.QueryString["TId"].ToString();
                    return View();
                }
                else
                {
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpGet]
        [ActionName("Team_Top_Performer")]

        public ActionResult Team_Top_Performer(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().Get_Team_Top_Performer(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpGet]
        [ActionName("Team_Top_Preferred_Players")]

        public ActionResult Team_Top_Preferred_Players(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().Team_Top_Preferred_Players(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }



        public ActionResult LegueSubscription()
        {
          var  LID = Request.QueryString["LId"].ToString();
          var  TID = Request.QueryString["TId"].ToString();
        //   return Content("Details of " + LID);
            if (Session["UserName"] != null)
            {
                //  ParamModel Pmodel
                ParamModel Pmodel = new ParamModel();
                Pmodel.LeagueId = LID;
                Pmodel.TournamentId = TID;
                
                LeagueSubResponse PR = new LeagueSubResponse();
                List<LeagueSubList> LeagueSubList = new List<LeagueSubList>();
                PR = ARS.fetchleaguesubscription(Pmodel);
                if (PR.status == "success")
                {
                    LeagueSubList = PR.data.ToList();

                }
                Session["client_id"] = PR.data[0].client_id;
                Session["client_secret"] = PR.data[0].client_secret;
                ViewBag.UserName = Session["UserName"].ToString();
                ViewBag.Email = Session["Email"].ToString();
           
                ViewBag.TID = TID;
                ViewBag.LID = LID;
                if (Session["PhoneNumber"] !=null)
                {
                    ViewBag.PhoneNumber = Session["PhoneNumber"].ToString();
                }
                else
                {
                    ViewBag.PhoneNumber = null;
                }
                ViewBag.Page = "LegueSubscription";
                ViewBag.subscriptionlist = LeagueSubList;
                return View();
            }
            return View();
            // return View();
        }
        [HttpPost]
        [ActionName("CreateOrder")]
        [AllowAnonymous]
        public ActionResult CreateOrder(PaymentInitiateModel _requestdata)
        {
            Random randomObj = new Random();
            string transactionId = randomObj.Next(10000000, 100000000).ToString();

          //  Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient("rzp_test_lTgA607AThK4CZ", "018mdJ7azhGvj7rBimxWmYx8");
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
                //razorpayKey = "rzp_test_lTgA607AThK4CZ",
                amount = (int)_requestdata.amount * 100,
                currency = "INR",
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
        public class OrderModel
        {
            public string orderId { get; set; }
            public string razorpayKey { get; set; }
            public int amount { get; set; }
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

                 // Razorpay.Api.RazorpayClient client = new Razorpay.Api.RazorpayClient("rzp_test_lTgA607AThK4CZ", "018mdJ7azhGvj7rBimxWmYx8");

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
        [HttpPost]
        [ActionName("UpdateLeagueSubscription")]
        [AllowAnonymous]
        public ActionResult UpdateLeagueSubscription(List<LeagueSub> Pmodel)
        {

            try
            {
                SubResponce LR = new SubResponce();
                // Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UpdateLeagueSubscription(Pmodel);

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
        public ActionResult Success()
        {
            return View();
        }

        public ActionResult Failed()
        {
            return View();
        }
    }
 

}