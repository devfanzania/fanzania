using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class LiveScoreController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        // GET: LiveScore
        [AllowAnonymous]
        public ActionResult Index()
        {
            if (Session["UserName"] != null)
            {
                Session["USERSELECTION"] = "Score";
                Session["USERSELECTIONMODE"] = "T";
                ViewBag.Page = "LiveScore";
                ViewBag.UserName = Session["UserName"].ToString();
                
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Account");
            }
        }

        [HttpPost]
        [ActionName("InprogressTournament")]
        [AllowAnonymous]
        public ActionResult InprogressTournament()
        {
            try
            {
                UserTournamentResponse LR = new UserTournamentResponse();
                List<UserTournamentDataResponse> ListDetails = new List<UserTournamentDataResponse>();
                ParamModel lModel = new ParamModel();
                lModel.UserId = Session["UserId"].ToString();
                lModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.InprogressTournament(lModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.ToList();
                }
                return Json(ListDetails, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }

        [HttpPost]
        [ActionName("ShowMatch")]
        [AllowAnonymous]
        public ActionResult ShowMatch(ParamModel pModel)
        {
            try
            {
                MatchResponse LR = new MatchResponse();
                List<MatchDataResponse> ListDetails = new List<MatchDataResponse>();
                LR = ARS.LiveMatchDetails(pModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.ToList();
                }
                return Json(ListDetails, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        [ActionName("ShowMatch_Score")]
        [AllowAnonymous]
        public ActionResult ShowMatch_Score(ParamModel pModel)
        {
            try
            {
                MatchScore LR = new MatchScore();
                List<MatchScoreData> ListDetails = new List<MatchScoreData>();
                List<MatchScoreData> SortedList = new List<MatchScoreData>();
                pModel.UserId = Session["UserId"].ToString();
                LR = ARS.LiveMatchScore(pModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.Where(x => x.ParticipationTeamName == pModel.Team).ToList();

                    var WKList = ListDetails.Where(x => x.PlayerSpeciality == "wicketkeeper").ToList();
                    var BATlist = ListDetails.Where(x => x.PlayerSpeciality == "batsman").ToList();
                    var ALLlist = ListDetails.Where(x => x.PlayerSpeciality == "allrounder").ToList();
                    var BWLlist = ListDetails.Where(x => x.PlayerSpeciality == "bowler").ToList();

                    SortedList = BATlist.Concat(WKList).ToList();
                    SortedList = SortedList.Concat(ALLlist).ToList();
                    SortedList = SortedList.Concat(BWLlist).ToList();
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("LiveLeague")]
        [AllowAnonymous]
        public ActionResult LiveLeague(ParamModel pModel)
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
        [HttpGet]
        [ActionName("LiveLeagueUser")]
        [AllowAnonymous]
        public ActionResult LiveLeagueUser(ParamModel pModel)
        {
            try
            {
                liveleagueUser LR = new liveleagueUser();
                List<liveleagueUserData> TDetail = new List<liveleagueUserData>();
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.LiveLeagueUser(pModel);
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
        [ActionName("LiveScore")]
        [AllowAnonymous]
        public ActionResult LiveScore(ParamModel pModel)
        {
            try
            {
                LiveScore LR = new LiveScore();
                List<LiveScoreData> ListDetails = new List<LiveScoreData>();
                List<LiveScoreData> SortedList = new List<LiveScoreData>();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.LiveScore(pModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.ToList();
                    var WKList = ListDetails.Where(x => x.PlayerSpeciality == "wicketkeeper").ToList();
                    var BATlist = ListDetails.Where(x => x.PlayerSpeciality == "batsman").ToList();
                    var ALLlist = ListDetails.Where(x => x.PlayerSpeciality == "allrounder").ToList();
                    var BWLlist = ListDetails.Where(x => x.PlayerSpeciality == "bowler").ToList();

                    SortedList = BATlist.Concat(WKList).ToList();
                    SortedList = SortedList.Concat(ALLlist).ToList();
                    SortedList = SortedList.Concat(BWLlist).ToList();
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("LiveScoreBoard")]
        [AllowAnonymous]
        public ActionResult LiveScoreBoard(ParamModel pModel)
        {
            try
            {
                LiveScoreBoardT LR = new LiveScoreBoardT();
                List<LiveScoreBoardDataT> ListDetails = new List<LiveScoreBoardDataT>();
                List<LiveScoreBoardDataT> SortedList = new List<LiveScoreBoardDataT>();
                //if (pModel.Inning == 1)
                {
                    if (TempData["LiveScoreBoardData"] != null)
                    {
                        ListDetails = (List<LiveScoreBoardDataT>)TempData["LiveScoreBoardData"];
                        SortedList = ListDetails.Where(x => x.Inning == pModel.Inning).ToList();
                    }
                    else
                    {
                        pModel.authtoken = Session["x-api-authtoken"].ToString();
                        LR = ARS.LiveScoreBoard(pModel);
                        if (LR.status == "success")
                        {
                            ListDetails = LR.data.ToList();
                            TempData["LiveScoreBoardData"] = ListDetails;
                            SortedList = ListDetails.Where(x => x.Inning == pModel.Inning).ToList();

                        }
                    }
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../../Home/Error");
            }
        }

        public ActionResult ScoreBoard()
        {
            return View();
        }

        [HttpPost]
        [ActionName("TeamPointsComparison")]
        [AllowAnonymous]
        public ActionResult TeamPointsComparison(ParamModel pModel)
        {
            try
            {
                TeamPointsC LR = new TeamPointsC();
                List<TeamPointsComparisonResponce> ListDetails = new List<TeamPointsComparisonResponce>();
                LR = ARS.TeamPointsComparison(pModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.ToList();
                }
                return Json(ListDetails, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpGet]
        [ActionName("LiveTeamScoreComparison")]
        [AllowAnonymous]
        public ActionResult LiveTeamScoreComparison(ParamModel pModel)
        {
            try
            {
                LiveTeamScore LR = new LiveTeamScore();
                List<LiveTeamScoreResponce> ListDetails = new List<LiveTeamScoreResponce>();
                LR = ARS.LiveTeamScoreComparison(pModel);
                if (LR.status == "success")
                {
                    ListDetails = LR.data.ToList();
                }
                return Json(ListDetails, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
    }
}