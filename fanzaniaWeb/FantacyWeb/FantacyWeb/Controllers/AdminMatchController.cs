using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;
using System.Text;
using System.Globalization;
using System.Data;
using System.Data.OleDb;
using System.IO;

namespace FantacyWeb.Controllers
{
    public class AdminMatchController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminMatch
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

        [HttpPost]
        [ActionName("ShowMatch")]
        [AllowAnonymous]
        public JsonResult ShowMatch(HttpPostedFileBase pFile)
        {
            string strResult = "F";
            string strFileName = "";
            string strResMsg = "";

            Session["MatchListData"] = null;
            try
            {
                if (pFile != null && pFile.ContentLength > 0)
                {
                    string currTime = DateTime.Now.ToString("dd/MM/yyyy hh:mm:ss").ToString().Replace("-", string.Empty).Replace("/", string.Empty).Replace(":", string.Empty).Replace(" ", string.Empty);
                    string strPath = Server.MapPath("~/Temp/");
                    strFileName = Path.GetFileNameWithoutExtension(pFile.FileName.ToString().Replace(" ", string.Empty).Replace("-", string.Empty)) + "_" + currTime + Path.GetExtension(pFile.FileName.ToString());
                    pFile.SaveAs(strPath + strFileName);
                    string fileLocation = Server.MapPath("~/Temp/" + strFileName);
                    DataTable dt = new DataTable();
                    dt = ExcelToDatatable(fileLocation);
                    Session["MatchListData"] = dt;
                    strResult = "T";
                }
            }
            catch (Exception ex)
            {
                strResult = "F";
                if (ex.Message == "No value given for one or more required parameters.")
                {
                    strResMsg = "One or more column does't match to selected excel. Please check your templete!";
                }
                else
                {
                    strResMsg = ex.ToString();
                }
            }
            return Json(new { result = strResult, fileName = strFileName, resMsg = strResMsg }, JsonRequestBehavior.AllowGet);
        }

        public DataTable ExcelToDatatable(string fileLocation)
        {
            DataSet ds = new DataSet();
            string fileExtension = System.IO.Path.GetExtension(fileLocation);
            var connectionString = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + fileLocation + ";Extended Properties=\"Excel 12.0;IMEX=1;HDR=Yes;TypeGuessRows=0;ImportMixedTypes=Text\""; ;

            using (var conn = new OleDbConnection(connectionString))
            {
                conn.Open();
                var sheets = conn.GetOleDbSchemaTable(System.Data.OleDb.OleDbSchemaGuid.Tables, new object[] { null, null, null, "TABLE" });
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "SELECT MatchNo, MatchType, Venue, MatchStage,Team1,Team2,MatchScheduledDate, MatchScheduledTime, UniqueId, TournamentName FROM[" + sheets.Rows[0]["TABLE_NAME"].ToString() + "] ";
                    var adapter = new OleDbDataAdapter(cmd);
                    //var ds = new DataSet();
                    adapter.Fill(ds);
                }
            }
            return ds.Tables[0];
        }

        [HttpGet]
        [ActionName("PreSaveMatchList")]
        public JsonResult PreSaveMatchList(int? page, int? limit, string sortBy, string direction, string FileName)
        {
            //AdminLoginModel LM = new AdminLoginModel();
            //LM = (AdminLoginModel)Session["LoginSession"];
            DataTable dt = new DataTable();
            dt = (DataTable)Session["MatchListData"];
            int total;
            var records = new GridModel().GetMatchList(page, limit, sortBy, direction, dt, out total);
            List<AdminMatchDetailsModel> list = new List<AdminMatchDetailsModel>();

            list = records.ToList();
            Session["MatchListData"] = list;
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SaveMatch")]
        public JsonResult SaveMatch(AdminMatchDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {

                List<AdminMatchDetailsModel> list = new List<AdminMatchDetailsModel>();
                list = (List<AdminMatchDetailsModel>)Session["MatchListData"];
                if (list.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    if (Session["GameType"].ToString() == "D")
                    {
                        sb.Append("<Tournament>");
                        foreach (var l in list)
                        {
                            sb.Append("<MatchDetails MatchNo='" + l.MatchNo + "' MatchType='" + l.MatchType + "' Venue='" + l.Venue + "' MatchStage='" + l.MatchStage + "' Team1='" + l.Team1 + "'");
                            sb.Append(" Team2='" + l.Team2 + "' MatchScheduledDate='" + l.MatchScheduledDate + "' MatchScheduledTime='" + l.MatchScheduledTime + "' UniqueId='" + l.UniqueId + "' TournamentName ='" + l.TournamentName + "'/>");
                        }
                        sb.Append("</Tournament>");
                        string MatchXML = sb.ToString();
                        tModel.xmlData = MatchXML;
                        LR = ARS.SaveDailyMatch(tModel);
                    }
                    else
                    {
                        sb.Append("<Tournament>");
                        foreach (var l in list)
                        {
                            sb.Append("<MatchDetails MatchNo='" + l.MatchNo + "' MatchType='" + l.MatchType + "' Venue='" + l.Venue + "' MatchStage='" + l.MatchStage + "' Team1='" + l.Team1 + "'");
                            sb.Append(" Team2='" + l.Team2 + "' MatchScheduledDate='" + l.MatchScheduledDate + "' MatchScheduledTime='" + l.MatchScheduledTime + "' UniqueId='" + l.UniqueId + "' />");
                        }
                        sb.Append("</Tournament>");
                        string MatchXML = sb.ToString();
                        tModel.xmlData = MatchXML;
                        LR = ARS.SaveMatch(tModel);
                    }

                    //string MatchXML = sb.ToString();
                    //tModel.xmlData = MatchXML;
                    //LR = ARS.SaveMatch(tModel);
                }

            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

       
        [HttpGet]
        [ActionName("GetSaveMatch")]
        public JsonResult GetSaveMatch(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            //AdminLoginModel LM = new AdminLoginModel();
            //LM = (AdminLoginModel)Session["LoginSession"];
            Session["GameType"] = pModel.GameType;
            int total;
            var records = new GridModel().GetSaveMatch(page, limit, sortBy, direction, pModel, out total);
            List<AdminMatchDetailsModel> list = new List<AdminMatchDetailsModel>();
            for(int i =0;i< records.Count; i++)
            {
                records[i].MatchDate = records[i].MatchDate.Replace('T', ' ');
            }
            list = records.ToList();
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }
       

        public ActionResult MatchEdit()
        {
            if (Session["AdminUserName"] != null)
            {
                if (Request.QueryString["mid"] != null)
                {
                    
                    Session["M_mid"] = Request.QueryString["mid"].ToString();
                    Session["M_tid"] = Request.QueryString["tid"].ToString();

                }
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }

        [HttpPost]
        [ActionName("GetMatchForUpdte")]
        [AllowAnonymous]
        public ActionResult GetMatchForUpdte(ParamModel pModel)
        {
            AdminMatchModel LR = new AdminMatchModel();
            List<AdminMatchDetailsModel> ListDetail = new List<AdminMatchDetailsModel>();

            //LR = ARS.GetSaveMatch(pModel);
            if (Session["GameType"].ToString() == "D")
            {
                LR = ARS.GetSaveDailyMatch(pModel);
            }
            else
            {
                LR = ARS.GetSaveMatch(pModel);
            }
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }


        [HttpPost]
        [ActionName("UpdateMatch")]
        [AllowAnonymous]
        public ActionResult UpdateMatch(AdminMatchDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            if (Session["GameType"].ToString() == "D")
            {
                LR = ARS.UpdateDailyMatch(tModel);
            }
            else
            {
                LR = ARS.UpdateMatch(tModel);
            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        public ActionResult MatchControl()
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
        [ActionName("GetMatch")]
        [AllowAnonymous]
        public ActionResult GetMatch(ParamModel pModel)
        {
            AdminMatchModel LR = new AdminMatchModel();
            List<AdminMatchDetailsModel> ListDetail = new List<AdminMatchDetailsModel>();

            LR = ARS.GetMatchOnStart(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
                if (TempData["MatchDataOnStart"] == null)
                {
                    TempData["MatchDataOnStart"] = ListDetail;
                }
                else
                {
                    var UpcomMatchList = (List<AdminMatchDetailsModel>)TempData.Peek("MatchDataOnStart");
                    List<AdminMatchDetailsModel> LiveMatchList = new List<AdminMatchDetailsModel>();
                    LiveMatchList = ListDetail;
                    LiveMatchList = LiveMatchList.Concat(UpcomMatchList).ToList();
                    TempData["MatchDataOnStart"] = LiveMatchList;
                }
            }
            return Json(ListDetail, JsonRequestBehavior.AllowGet);
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
                if (TempData["MatchDataOnStart"] == null)
                {
                    TempData["MatchDataOnStart"] = ListDetail;
                }
                else
                {
                    var UpcomMatchList = (List<AdminMatchDetailsModel>)TempData.Peek("MatchDataOnStart");
                    List<AdminMatchDetailsModel> LiveMatchList = new List<AdminMatchDetailsModel>();
                    LiveMatchList = ListDetail;
                    LiveMatchList = LiveMatchList.Concat(UpcomMatchList).ToList();
                    TempData["MatchDataOnStart"] = LiveMatchList;
                }
            }
            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("GetMatchDeatils")]
        [AllowAnonymous]
        public ActionResult GetMatchDeatils(ParamModel pModel)
        {
            List<AdminMatchDetailsModel> Slist = (List<AdminMatchDetailsModel>)TempData.Peek("MatchDataOnStart");
            var outList = Slist.Where(x => x.UniqueId == pModel.UniqueId).ToList();
            return Json(outList, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("MatchStart")]
        [AllowAnonymous]
        public ActionResult MatchStart(AdminMatchDetailsModel pModel)
        {
            pModel.Inning1BattingTeam = pModel.BattingTeam;
            AdminTournamentRuleResponse LR = new AdminTournamentRuleResponse();
           
            LR = ARS.MatchStart(pModel);
           
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("SwitchBattingTeam")]
        [AllowAnonymous]
        public ActionResult SwitchBattingTeam(AdminMatchDetailsModel pModel)
        {
            AdminTournamentRuleResponse LR = new AdminTournamentRuleResponse();

            LR = ARS.SwitchBattingTeam(pModel);

            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("DeleteMatch")]
        public JsonResult DeleteMatch(AdminMatchDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                if (Session["GameType"].ToString() == "D")
                {
                    LR = ARS.DeleteDailyMatch(tModel);
                }
                else
                {
                    LR = ARS.DeleteMatch(tModel);
                }
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SyncMatch")]
        public JsonResult SyncMatch(ParamModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
               LR = ARS.SyncMatch(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
              

        [HttpPost]
        [ActionName("TossWinner")]
        [AllowAnonymous]
        public ActionResult TossWinner(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.TossWinner(pModel);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            
        }

        [HttpPost]
        [ActionName("HideScore")]
        [AllowAnonymous]
        public ActionResult HideScore(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.HideScore(pModel);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }

        }

        [HttpPost]
        [ActionName("CalculatePoints")]
        [AllowAnonymous]
        public ActionResult CalculatePoints(ParamModel Pmodel)
        {
            AdminResponse Amodel = new AdminResponse();
            Amodel = ARS.CalculatePoint(Pmodel);

            return Json(Amodel, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("ManualPointCalculation")]
        [AllowAnonymous]
        public ActionResult ManualPointCalculation(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.ManualPointCalculation(pModel);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }

        }

    }
}