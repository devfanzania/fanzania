using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class AdminController : Controller
    {

        private AdminAccountRestService ARS = new AdminAccountRestService();
        private AccountRestService AR = new AccountRestService();
        // GET: Admin
        public ActionResult Index()
        {
            return View();
        }
        [HttpPost]
        public ActionResult Index(AdminLoginModel pModel)
        {

            //string adminUser = "das.rajibdas@gmail.com";
            //string adminPassword = "adminuser";
           if (ModelState.IsValid)
           // if (!string.IsNullOrEmpty(adminUser) && !string.IsNullOrEmpty(adminPassword))
            {
                Response LR = new Response();
                LR = ARS.ValidateAdminLogin(pModel);
                if (LR.status == "success")
                {
                    Session["AdminUserName"] = "Admin";
                    //Session["x-api-authtoken"] = LR.data.FirstOrDefault().SessionId;

                    return RedirectToAction("Dashboard", "Admin");
                }
                else
                {
                    ViewBag.Message = "This email address or password is incorrect.";
                }
            }
            return View();
           
        }

        [AllowAnonymous]
        public ActionResult Logout()
        {
                Session["AdminUserName"] = null;
                Session.Abandon();
                Session.RemoveAll();
           
            return RedirectPermanent("/Admin");
        }

        #region -------------------------- Dashboard ---------------------------------------------
        public ActionResult Dashboard()
        {
            if (Session["AdminUserName"] != null)
            {

                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }
        [HttpGet]
        [ActionName("GetAllTournament")]
        [AllowAnonymous]
        public ActionResult GetAllTournament(ParamModel pModel)
        {
            UserTournamentResponse LR = new UserTournamentResponse();
            List<UserTournamentDataResponse> ListDetail = new List<UserTournamentDataResponse>();

            LR = ARS.GetTournament(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
            }
            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("GetGlobalTopTeam")]
        public ActionResult GetGlobalTopTeam(ParamModel pModel)
        {
            GlobalTopTeam LR = new GlobalTopTeam();
            List<GlobalTopTeamData> chartList = new List<GlobalTopTeamData>();
            
            LR = ARS.GlobalTopTeams(pModel);
            if (LR.status == "success")
            {
                chartList = LR.data.ToList();
            }
            var ChartList = (from data in chartList
                             select new
                             {
                                 UserTeamName = data.UserTeamName,
                                 Owner = data.Owner,
                                 TotalPoints = data.TotalPoints,
                                 TeamRank = data.TeamRank
                             }).ToList();

            return Json(ChartList, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("GetGlobalTopLeague")]
        public ActionResult GetGlobalTopLeague(ParamModel pModel)
        {
            GlobalTopLeague LR = new GlobalTopLeague();
            List<GlobalTopLeagueData> chartList = new List<GlobalTopLeagueData>();
            LR = ARS.GlobalTopLeagues(pModel);
            if (LR.status == "success")
            {
                chartList = LR.data.ToList();
            }
            var ChartList = (from data in chartList
                             select new
                             {
                                 LeagueName = data.LeagueName,
                                 LeagueOwner = data.LeagueOwner,
                                 LeaguePoints = data.LeaguePoints,
                                 LeagueRank = data.LeagueRank
                             }).ToList();

            return Json(ChartList, JsonRequestBehavior.AllowGet);
        }


        #endregion
        public ActionResult PromoteUser()
        {
            if (Session["AdminUserName"] != null)
            {
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }

        [HttpPost]
        [ActionName("UpdateUser")]
        [AllowAnonymous]
        public ActionResult UpdateUser(AdminModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.UpdateUser(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("UpdateSubscriptionDetails")]
        [AllowAnonymous]
        public ActionResult UpdateSubscriptionDetails(UpdateSubadmin Pmodel)
        {
                SubResponceadmin LR = new SubResponceadmin();
                LR = ARS.UpdateSubscriptionDetails(Pmodel);
                return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("GetSubscriptionDetails")]
        [AllowAnonymous]
        public ActionResult GetSubscriptionDetails(SubscriptionDetails Pmodel)
        {

            DefaultResponseSubscriptionDetails LR = new DefaultResponseSubscriptionDetails();
            LR = AR.GetSubscriptionDetails(Pmodel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("adminupdateuserpoints")]
        [AllowAnonymous]
        public ActionResult adminupdateuserpoints(UpdateUserPoints tModel)
        {
            DetailsUpdateUserPoints LR = new DetailsUpdateUserPoints();
            LR = ARS.adminupdateuserpoints(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("AddFunFact")]
        [AllowAnonymous]
        public ActionResult AddFunFact(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.AddFunFact(pModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }


        [HttpPost]
        [ActionName("SendNotification")]
        [AllowAnonymous]
        public ActionResult SendNotification(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.SendNotification(pModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("SyncMatch")]
        [AllowAnonymous]
        public ActionResult SyncMatch(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.SyncMatchRapid(pModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("SyncTeam")]
        [AllowAnonymous]
        public ActionResult SyncTeam(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.SyncTeamRapid(pModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("SyncPlayer")]
        [AllowAnonymous]
        public ActionResult SyncPlayer(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.SyncPlayerRapid(pModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
    }
}