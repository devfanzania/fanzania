using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;
using System.Xml.Linq;
using System.Xml;

namespace FantacyWeb.Controllers
{
    public class AdminUpdatePScoreController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminUpdatePScore
        public ActionResult Index()
        {
            return View();
        }
        [HttpGet]
        [ActionName("FetchManualScore")]
        [AllowAnonymous]
        public ActionResult FetchManualScore(ParamModel pModel)
        {
            FetchManualScoreResponce LR = new FetchManualScoreResponce();
            List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();

            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.FetchManualScore(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();

            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("ManualScoreUpdateEachPlayer")]
        [AllowAnonymous]
        public ActionResult ManualScoreUpdateEachPlayer(UpdatePointModel pModel)
        {
            ManualScoreUpdateEachPlayerResponce LR = new ManualScoreUpdateEachPlayerResponce();
       //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();

            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.ManualScoreUpdateEachPlayer(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("SetManualMom")]
        [AllowAnonymous]
        public ActionResult SetManualMom(UpdatePointModel pModel)
        {
            ManualScoreUpdateEachPlayerResponce LR = new ManualScoreUpdateEachPlayerResponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();

            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.SetManualMom(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("AdminFetchMatchOnStart")]
        [AllowAnonymous]
        public ActionResult AdminFetchMatchOnStart(ParamModel pModel)
        {
            AdminMatchModel LR = new AdminMatchModel();
            List<AdminMatchDetailsModel> ListDetail = new List<AdminMatchDetailsModel>();

            LR = ARS.GetMatchOnStart(pModel);
            if (LR.status == "success")
            {

                ListDetail = LR.data.ToList();
              
            }
            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

    }
}