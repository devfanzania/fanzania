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
    public class AdminAutoTeamController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminAutoTeam
        public ActionResult Index()
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

        [HttpGet]
        [ActionName("GetAutoTeam")]
        public JsonResult GetAutoTeam(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            int total;
            var records = new GridModel().GetAutoTeam(page, limit, sortBy, direction, pModel, out total);
            List<AdminAutoTeamDetailsModel> list = new List<AdminAutoTeamDetailsModel>();

            list = records.ToList();
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        public ActionResult AutoTeamAddEdit()
        {
            if (Session["AdminUserName"] != null)
            {
                if (Request.QueryString["tid"] != null)
                {
                    Session["AutoTeamTourId"] = Request.QueryString["tid"].ToString(); 
                }
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }


        [HttpPost]
        [ActionName("GetAllPlayers")]
        [AllowAnonymous]
        public ActionResult GetAllPlayers(ParamModel pModel)
        {
            AdminPlayerModel LR = new AdminPlayerModel();
            List<AdminPlayerDetailsModel> ListDetail = new List<AdminPlayerDetailsModel>();

            LR = ARS.GetSavePlayer(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
            }
            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }
        
        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SaveAutoTeam")]
        public JsonResult SaveAutoTeam(AdminAutoTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                tModel.TournamentId =  Convert.ToInt32(Session["AutoTeamTourId"].ToString());
                XmlDocument xDoc = new XmlDocument();
                xDoc.LoadXml(tModel.playerXML);

                foreach (XmlNode node in xDoc.DocumentElement.ChildNodes)
                {
                    tModel.Player1 = node.Attributes["Player1"]?.InnerText;
                    tModel.Player2 = node.Attributes["Player2"]?.InnerText;
                    tModel.Player3 = node.Attributes["Player3"]?.InnerText;
                    tModel.Player4 = node.Attributes["Player4"]?.InnerText;
                    tModel.Player5 = node.Attributes["Player5"]?.InnerText;
                    tModel.Player6 = node.Attributes["Player6"]?.InnerText;
                    tModel.Player7 = node.Attributes["Player7"]?.InnerText;
                    tModel.Player8 = node.Attributes["Player8"]?.InnerText;
                    tModel.Player9 = node.Attributes["Player9"]?.InnerText;
                    tModel.Player10 = node.Attributes["Player10"]?.InnerText;
                    tModel.Player11 = node.Attributes["Player11"]?.InnerText;
                }
                LR = ARS.SaveAutoTeam(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("DeleteAutoTeam")]
        public JsonResult DeleteAutoTeam(AdminAutoTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.DeleteAutoTeam(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
    }
}