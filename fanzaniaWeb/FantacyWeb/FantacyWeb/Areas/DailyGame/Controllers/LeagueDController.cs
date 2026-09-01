using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model.DailyGame;
using FantacyWeb.Areas.DailyGame.Models;

namespace FantacyWeb.Areas.DailyGame.Controllers
{
    public class LeagueDController : Controller
    {
        private DailyRestService DRS = new DailyRestService();
        // GET: DailyGame/LeagueD
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    Session["USERSELECTION"] = "League";
                    Session["USERSELECTIONMODE"] = "D";
                    ViewBag.Page = "LeagueD";
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
                Pmodel.FetchAll = "Y";
                Pmodel.MatchStatus = "COMPLETE";
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

        [ActionName("DailyLeagueTeams")]
        [AllowAnonymous]
        public ActionResult DailyLeagueTeams(int? page, int? limit, string sortBy, string direction, DailyParamModel pModel)
        {
            try
            {
                int total;
                pModel.UserId = Session["UserId"].ToString();
                pModel.authtoken = Session["x-api-authtoken"].ToString();
                var records = new DailyGridModel().GetLeagueTeamList(page, limit, sortBy, direction, pModel, out total);
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
                    records[i].MatchId = pModel.MatchId;
                }
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
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
    }
}