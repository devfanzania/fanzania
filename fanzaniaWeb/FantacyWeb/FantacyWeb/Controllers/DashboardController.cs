using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using Fantacy_Model.DailyGame;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class DashboardController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        // GET: Dashboard
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                  //  ViewBag.ReferralCode = Session["ReferralCodee"].ToString();
                    if (Session["USERSELECTIONMODE"].ToString() == "T")
                    {
                        Session["USERSELECTION"] = "Home";
                        ViewBag.UserName = Session["UserName"].ToString();
                        ViewBag.UserID = Session["UserId"].ToString();
                       // ViewBag.ReferralCode = Session["ReferralCodee"].ToString();
                        ViewBag.Page = "Dashboard";
                        return View();
                    }
                    else if (Session["USERSELECTION"] != null && Session["USERSELECTION"].ToString() != "Home")
                    {

                        if (Session["USERSELECTION"].ToString() == "League")
                        {
                            return Redirect("/League");
                        }
                        else if (Session["USERSELECTION"].ToString() == "Team")
                        {
                            return Redirect("/Team");
                        }
                        else if (Session["USERSELECTION"].ToString() == "Wallet")
                        {
                            return Redirect("/Profile/wallet");
                        }
                        else
                        {
                            return Redirect("/LiveScore");
                        }
                    }
                    else
                    {
                        if (Session["UserName"] != null)
                        {
                            Session["USERSELECTION"] = "Home";
                            ViewBag.UserName = Session["UserName"].ToString();
                            ViewBag.Page = "Dashboard";
                            return View();
                        }
                        else
                        {
                            return RedirectToAction("Index", "Account");
                        }
                    }
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
        [ActionName("LoadUserActiveTournament")]
        [AllowAnonymous]
        public ActionResult LoadUserActiveTournament()
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
                    //store TournamentId
                    if (LR.data.ToList().Count != 0)
                    {
                        Session["TournamentIdd"] = LR.data.FirstOrDefault().TournamentId.ToString();
                    }
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
        [ActionName("TeamInfo")]
        [AllowAnonymous]
        public ActionResult TeamInfo(ParamModel Pmodel)
        {
            try
            {
                TeamResponse LR = new TeamResponse();
                List<TeaMDataResponse> TDetail = new List<TeaMDataResponse>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UserTeam(Pmodel);
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

        [HttpGet]
        [ActionName("LoadUserActiveLeagueInfo")]

        public ActionResult LoadUserActiveLeagueInfo(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                var records = new GridModel().UserActiveLeagueinfo(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("UserUpcomingTtournament")]
        [AllowAnonymous]
        public ActionResult UserUpcomingTtournament()
        {
            try
            {
                TournamentResponse LR = new TournamentResponse();
                List<TournamentDataResponse> TDetail = new List<TournamentDataResponse>();
                ParamModel lModel = new ParamModel();
                lModel.UserId = Session["UserId"].ToString();
                LR = ARS.UserUpcomingTtournament(lModel);
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
        [ActionName("CheckAvailable")]
        [AllowAnonymous]
        public ActionResult CheckAvailable(ParamModel PM)
        {
            try
            {
                ResponseModel RM = new ResponseModel();
                RM = ARS.CheckAvailable(PM);
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("CreateTeam")]
        [AllowAnonymous]
        public ActionResult CreateTeam(ParamModel PM)
        {
            try
            {
                CreateTeamResponse LR = new CreateTeamResponse();
                List<CreateTeamDataResponse> Detail = new List<CreateTeamDataResponse>();
                PM.UserId = Session["UserId"].ToString();
                LR = ARS.CreateTeam(PM);
                var redirectUrl = "";
                if (LR.status == "success")
                {
                    Session["TeamName"] = LR.data.FirstOrDefault().UserTeamName.ToString();
                    Session["TeamId"] = LR.data.FirstOrDefault().UserTeamId.ToString();
                    Session["SelectTournamentId"] = LR.data.FirstOrDefault().TournamentId.ToString();
                    Session["TournamentName"] = PM.TournamentName;
                    Session["TournamentStatus"] = PM.TournamentStatus;
                    Session["Subs"] = LR.data.FirstOrDefault().SubsLeft.ToString();
                    Session["SubsLeftAtSnapShot"] = LR.data.FirstOrDefault().SubsLeft.ToString();
                    Session["NitroLeft"] = LR.data.FirstOrDefault().NitroLeft.ToString();
                    Session["AutoPilotLeft"] = LR.data.FirstOrDefault().AutoPilotLeft.ToString();
                    Session["PainKillerLeft"] = LR.data.FirstOrDefault().PainKillerLeft.ToString();
                    Session["NitroUsed"] = "False";
                    Session["AutoPilotUsed"] = "False";
                    Session["PainKillerUsed"] = "False";
                    Detail = LR.data.ToList();

                    var qstr = "utid=" + LR.data.FirstOrDefault().UserTeamId.ToString() + "&tid=" + PM.TournamentId + "&tname=" + PM.TournamentName + "&tstat=" + PM.TournamentStatus;
                    var passPhraseReg = "amaf7LLSWhN@#r5!*";
                    var enc = ECDC.Encrypt(qstr.ToString(), passPhraseReg);
                    redirectUrl = "/Team/ManageTeam?enc=" + enc;
                }
                //return Json(Detail, JsonRequestBehavior.AllowGet);
                return Json(new { Url = redirectUrl }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }


        // Tournament Stats ..........................................................................

        [AllowAnonymous]
        public ActionResult TournamentStat()
        {
            if (Session["UserName"] != null)
            {
                ViewBag.UserName = Session["UserName"].ToString();
                Session["Tid_Stat"] = Request.QueryString["TId"].ToString();
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Account");
            }
        }

        [HttpGet]
        [ActionName("GlobalTopPlayers")]
        public ActionResult GlobalTopPlayers(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().GlobalTopPlayers(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        [ActionName("GlobalTopLeagues")]
        public ActionResult GlobalTopLeagues(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().GlobalTopLeagues(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        [ActionName("GlobalTopTeams")]
        public ActionResult GlobalTopTeams(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().GlobalTopTeams(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("TeamPlayerInfoCompleteMatch")]
        [AllowAnonymous]
        public ActionResult TeamPlayerInfoCompleteMatch(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = ARS.TeamPlayerDetails_CompleteMatch(Pmodel);
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

        [ActionName("GetDashboardTopFact")]
        [AllowAnonymous]
        public ActionResult GetDashboardTopFact()
        {
            try
            {
                GetFunFact fact = new GetFunFact();
                List<GetFunFactData> mDetail = new List<GetFunFactData>();
                String data = "";
                fact = ARS.GetFunFact();
                if (fact.status == "success")
                {
                    mDetail = fact.data.ToList();
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        [ActionName("TopTenUsers")]
        public ActionResult TopTenUsers(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().TopTenUsers(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("MakeActive")]
        [AllowAnonymous]
        public ActionResult MakeActive()
        {
            try
            {
                return Json("Active", JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("SaveloginPreference")]
        [AllowAnonymous]
        public ActionResult SaveloginPreference(SaveLoginPreferenceData Pmodel)
        {

            try
            {
                DefaultResponseLoginP LR = new DefaultResponseLoginP();
               // Pmodel.UserId = Session["UserId"].ToString();
                //Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.SaveLoginPreference(Pmodel);

                if (LR.status == "success")
                {
                    return Json(LR.data.FirstOrDefault(), JsonRequestBehavior.AllowGet);
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
        //private String getCompleteAddressString(double LATITUDE, double LONGITUDE)
        //{
        //    String strAdd = "";
        //    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //    try
        //    {
        //        List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
        //        if (addresses != null)
        //        {
        //            Address returnedAddress = addresses.get(0);
        //            StringBuilder strReturnedAddress = new StringBuilder("");

        //            for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++)
        //            {
        //                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
        //            }
        //            strAdd = strReturnedAddress.toString();
        //            Log.w("My Current loction address", strReturnedAddress.toString());
        //        }
        //        else
        //        {
        //            Log.w("My Current loction address", "No Address returned!");
        //        }
        //    }
        //    catch (Exception e)
        //    {
        //        e.printStackTrace();
        //        Log.w("My Current loction address", "Canont get Address!");
        //    }
        //    return strAdd;
        //}

    }
}