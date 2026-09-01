using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model.DailyGame;
using FantacyWeb.Areas.DailyGame.Models;

namespace FantacyWeb.Areas.DailyGame.Controllers
{
    public class HomeDController : Controller
    {
        private DailyRestService DRS = new DailyRestService();
        // GET: DailyGame/HomeD
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    if (Session["USERSELECTIONMODE"].ToString() == "D")
                    {
                        Session["USERSELECTION"] = "Home";
                        ViewBag.Page = "HomeD";
                        ViewBag.UserName = Session["UserName"].ToString();
                        return View();
                    }
                    else if (Session["USERSELECTION"] != null && Session["USERSELECTION"].ToString() != "Home" && Session["USERSELECTION"].ToString() != "Team")
                    {

                        if (Session["USERSELECTION"].ToString() == "League")
                        { return Redirect("/DailyGame/LeagueD"); }
                        else if (Session["USERSELECTION"].ToString() == "Wallet")
                        { return Redirect("/Profile/wallet"); }
                        else
                        { return Redirect("/DailyGame/LiveScoreD"); }
                    }
                    else
                    {
                        if (Session["UserName"] != null)
                        {
                            Session["USERSELECTION"] = "Home";
                            ViewBag.Page = "HomeD";
                            ViewBag.UserName = Session["UserName"].ToString();
                            return View();
                        }
                        else
                        {
                            return Redirect("../Account/Index");
                        }
                    }
                }
                else
                {
                    return Redirect("../Account/Index");
                }
            }
            catch (Exception ee)
            {
                //return Redirect("../Home/Error");
                return Redirect("../Account/Index");
            }
        }


        [HttpPost]
        [ActionName("GetDailyMyMatch")]
        [AllowAnonymous]
        public ActionResult GetDailyMyMatch(DailyParamModel Pmodel)
        {
            try
            {
                DailyMatchModel PR = new DailyMatchModel();
                List<DailyMatchDetailsModel> mDetail = new List<DailyMatchDetailsModel>();
                List<DailyMatchDetailsModel> SortedList = new List<DailyMatchDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Pmodel.FetchAll = "Y";
                Pmodel.MatchStatus = "all";

                PR = DRS.GetDailyMyMatch(Pmodel);
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();
                    if (Pmodel.FilterType == "All")
                    {
                        var Restlist = mDetail.Where(x => x.MatchStatus != "COMPLETE").ToList();
                        var Finishlist = mDetail.Where(x => x.MatchStatus == "COMPLETE").ToList();
                        SortedList = Restlist.Concat(Finishlist).ToList();
                    }
                    else
                    {
                        SortedList = mDetail.Where(x => x.MatchStatus.ToLower() == Pmodel.FilterType.ToLower()).ToList();
                    }
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("GetAllDailyMatch")]
        [AllowAnonymous]
        public ActionResult GetAllDailyMatch(DailyParamModel Pmodel)
        {
            try
            {
                DailyMatchModel PR = new DailyMatchModel();
                List<DailyMatchDetailsModel> mDetail = new List<DailyMatchDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = DRS.GetAllDailyMatch(Pmodel);
                if (PR.status == "success")
                {
                    if (Pmodel.TournamentFilter == "ALL")
                    {
                        mDetail = PR.data.ToList();
                    }
                    else
                    {
                        var tlst = Pmodel.TournamentFilter.Split(',');
                        foreach (string i in tlst)
                        {
                            if (!string.IsNullOrEmpty(i))
                            {
                                List<DailyMatchDetailsModel> tlList = new List<DailyMatchDetailsModel>();
                                tlList = PR.data.Where(c => c.TournamentName.Contains(i)).ToList();

                                mDetail.AddRange(tlList);
                            }
                        }
                    }
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("DailyLeagueTeams")]
        [AllowAnonymous]
        public ActionResult DailyLeagueTeams(DailyParamModel Pmodel)
        {
            try
            {
                DailyLeagueTeamModel PR = new DailyLeagueTeamModel();
                List<DailyLeagueTeamDetailsModel> mDetail = new List<DailyLeagueTeamDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = DRS.DailyLeagueTeams(Pmodel);
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("UserDailyTeamPlayers")]
        [AllowAnonymous]
        public ActionResult UserDailyTeamPlayers(DailyParamModel Pmodel)
        {
            try
            {
                DailyUserPlayerModel PR = new DailyUserPlayerModel();
                List<DailyUserPlayerDetailsModel> mDetail = new List<DailyUserPlayerDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = DRS.UserDailyTeamPlayers(Pmodel);
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();
                    TempData["DSelectPlayerlist"] = mDetail;
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("UserDailyTeamPlayersWithPoints")]
        [AllowAnonymous]
        public ActionResult UserDailyTeamPlayersWithPoints(DailyParamModel Pmodel)
        {
            try
            {
                DailyUserPlayerModel PR = new DailyUserPlayerModel();
                List<DailyUserPlayerDetailsModel> mDetail = new List<DailyUserPlayerDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = DRS.UserDailyTeamPlayersWithPoints(Pmodel);
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("DailyTournamentList")]
        [AllowAnonymous]
        public ActionResult DailyTournamentList(DailyParamModel Pmodel)
        {
            try
            {
                DailyTournament PR = new DailyTournament();
                List<DailyTournamentData> mDetail = new List<DailyTournamentData>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.MatchStatus = "UPCOMING";
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = DRS.DailyTournamentList(Pmodel);
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();

                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [ActionName("GetGunFact")]
        [AllowAnonymous]
        public ActionResult GetGunFact()
        {
            try
            {
                GetGunFact PR = new GetGunFact();
                List<GetGunFactData> mDetail = new List<GetGunFactData>();

                PR = DRS.GetGunFact();
                if (PR.status == "success")
                {
                    mDetail = PR.data.ToList();
                }
                return Json(mDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
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
                return Redirect("../Home/Error");
            }
        }
    }
}