using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model.DailyGame;
using FantacyWeb.Areas.DailyGame.Models;

namespace FantacyWeb.Areas.DailyGame.Controllers
{
    public class LiveScoreDController : Controller
    {
        private DailyRestService DRS = new DailyRestService();
        // GET: DailyGame/LiveScoreD
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    Session["USERSELECTION"] = "Score";
                    Session["USERSELECTIONMODE"] = "D";
                    ViewBag.Page = "LiveD";
                    ViewBag.UserName = Session["UserName"].ToString();
                    return View();
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

        [ActionName("GetDailyMyMatch")]
        [AllowAnonymous]
        public ActionResult GetDailyMyMatch(DailyParamModel Pmodel)
        {
            try
            {
                DailyMatchModel PR = new DailyMatchModel();
                List<DailyMatchDetailsModel> mDetail = new List<DailyMatchDetailsModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Pmodel.FetchAll = "Y";
                Pmodel.MatchStatus = "Live";
                PR = DRS.GetDailyMyMatch(Pmodel);
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

        [ActionName("DailyLiveLeagueUsers")]
        [AllowAnonymous]
        public ActionResult DailyLiveLeagueUsers(DailyParamModel Pmodel)
        {
            try
            {
                DailyLiveLeagueUsersModel PR = new DailyLiveLeagueUsersModel();
                List<DailyLiveLeagueUsersDetailModel> mDetail = new List<DailyLiveLeagueUsersDetailModel>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                
                PR = DRS.DailyLiveLeagueUsers(Pmodel);
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

        [HttpGet]
        [ActionName("ShowMatch_Score")]
        [AllowAnonymous]
        public ActionResult ShowMatch_Score(DailyParamModel pModel)
        {
            try
            {
                DailyMatchScore LR = new DailyMatchScore();
                List<DailyMatchScoreData> ListDetails = new List<DailyMatchScoreData>();
                List<DailyMatchScoreData> SortedList = new List<DailyMatchScoreData>();
                pModel.UserId = Session["UserId"].ToString();
                LR = DRS.LiveMatchScore(pModel);
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
                return Redirect("../Home/Error");
            }
        }

        [HttpGet]
        [ActionName("LiveScore")]
        [AllowAnonymous]
        public ActionResult LiveScore(DailyParamModel pModel)
        {
            try
            {
                DailyLiveScore LR = new DailyLiveScore();
                List<DailyLiveScoreData> ListDetails = new List<DailyLiveScoreData>();
                List<DailyLiveScoreData> SortedList = new List<DailyLiveScoreData>();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = DRS.LiveScore(pModel);
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
                return Redirect("../Home/Error");
            }
        }

        [HttpPost]
        [ActionName("LiveScoreBoard")]
        [AllowAnonymous]
        public ActionResult LiveScoreBoard(DailyParamModel pModel)
        {
            try
            {
                LiveScoreBoard LR = new LiveScoreBoard();
                List<LiveScoreBoardData> ListDetails = new List<LiveScoreBoardData>();
                List<LiveScoreBoardData> SortedList = new List<LiveScoreBoardData>();
               
                if (TempData["LiveScoreBoardData"] != null)
                {
                    ListDetails = (List<LiveScoreBoardData>)TempData["LiveScoreBoardData"];
                    SortedList = ListDetails.Where(x => x.Inning == pModel.Inning).ToList();
                }
                else
                {
                    pModel.authtoken = Session["x-api-authtoken"].ToString();
                    LR = DRS.LiveScoreBoard(pModel);
                    if (LR.status == "success")
                    {
                        ListDetails = LR.data.ToList();
                        TempData["LiveScoreBoardData"] = ListDetails;
                        SortedList = ListDetails.Where(x => x.Inning == pModel.Inning).ToList();

                    }
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        
    }
}