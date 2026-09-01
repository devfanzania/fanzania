using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class HomeController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        public ActionResult Index()
        {
            ViewBag.Page = "Home";
            //MatchResult LR = new MatchResult();
            //LR = TopTenUser();
            return View();
        }

        //public MatchResult TopTenUser()
        //{
        //    MatchResult LR = new MatchResult();
        //    MatchResultData listData = new MatchResultData();
            
        //    LR = ARS.TopTenUser();
        //    return LR;
        //}

        public ActionResult About()
        {
            ViewBag.Page = "OurStory";
            ViewBag.Message = "Your application description page.";

            return View();
        }
        public ActionResult Howtoplay()
        {
            ViewBag.Page = "HowtoPlay";
            ViewBag.Message = "Your application description page.";

            return View();
        }
        public ActionResult Subscription()
        {
            ViewBag.Page = "Subscription";
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Page = "GetInTouch";
            ViewBag.Message = "Your contact page.";

            return View();
        }

        [HttpPost]
        [ActionName("PostFeedback")]
        [AllowAnonymous]
        public ActionResult PostFeedback(FeedbackModel tModel)
        {
            DefaultResponse LR = new DefaultResponse();
            LR = ARS.PostFeedback(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        public ActionResult FAQ()
        {
            ViewBag.Message = "Your FAQ page.";

            return View();
        }

        public ActionResult PrivacyPolicy()
        {
            
            return View();
        }
        public ActionResult TermsCondition()
        {
            

            return View();
        }
        public ActionResult PointScoring()
        {
           return View();
        }
        public ActionResult TeamComposition()
        {
           return View();
        }

        // For Mobile View
        public ActionResult FAQ_Online()
        {
           
            return View();
        }
        public ActionResult About_Online()
        {

            return View();
        }
        public ActionResult HowToPlay_Online()
        {

            return View();
        }
        public ActionResult Subscription_Online()
        {

            return View();
        }
        public ActionResult PointScoring_Online()
        {
            return View();
        }
        public ActionResult TeamComposition_Online()
        {
            return View();
        }

        public ActionResult Error()
        {
            return View();
        }

        // --------------------------------------------  Notification ---------------------------------------

        [HttpPost]
        [ActionName("GetNotificationCount")]
        [AllowAnonymous]
        public ActionResult GetNotificationCount()
        {
            try
            {
                ParamModel Pmodel = new ParamModel();
                NotificationResponse LR = new NotificationResponse();
                List<NotificationData> NDetail = new List<NotificationData>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.GetNotificationCount(Pmodel);
                if (LR.status == "success")
                {
                    NDetail = LR.data.ToList();
                }
                return Json(NDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("GetNotificatons")]
        [AllowAnonymous]
        public ActionResult GetNotificatons()
        {
            try
            {
                ParamModel Pmodel = new ParamModel();
                NotificationResponse LR = new NotificationResponse();
                List<NotificationData> NDetail = new List<NotificationData>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.GetNotificatons(Pmodel);
                if (LR.status == "success")
                {
                    NDetail = LR.data.ToList();
                }
                return Json(NDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
     

        [HttpPost]
        [ActionName("AckNotificaton")]
        [AllowAnonymous]
        public ActionResult AckNotificaton(NotificationData Pmodel)
        {
            try
            {
                DefaultResponse LR = new DefaultResponse();
                Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.AckNotificaton(Pmodel);
                
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

    }
}