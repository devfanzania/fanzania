using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.IO;
using System.Data;
using System.Data.OleDb;
using Fantacy_Model;
using FantacyWeb.Models;
using System.Text;
using System.Globalization;

namespace FantacyWeb.Controllers
{
    public class AdminTourController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminTour
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
            
            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.GetTournament(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();

            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("GetTournament")]
        [AllowAnonymous]
        public ActionResult GetTournament(ParamModel pModel)
        {
            UserTournamentResponse LR = new UserTournamentResponse();
            List<UserTournamentDataResponse> ListDetail = new List<UserTournamentDataResponse>();

            LR = ARS.GetTournament(pModel);
            if (LR.status == "success")
            {
                LR.data.FirstOrDefault().TournamentStartDate = Convert.ToDateTime(LR.data.FirstOrDefault().TournamentStartDate).ToString("yyyy/MM/dd", CultureInfo.InvariantCulture);
                LR.data.FirstOrDefault().TournamentEndDate = Convert.ToDateTime(LR.data.FirstOrDefault().TournamentEndDate).ToString("yyyy/MM/dd", CultureInfo.InvariantCulture);
                ListDetail = LR.data.ToList();
            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        public ActionResult TournamentAddEdit()
        {
            if (Session["AdminUserName"] != null)
            {
                if (Request.QueryString["type"] != null)
                {
                    if (Request.QueryString["type"].ToString() == "add")
                    {
                        Session["AdminTournamentId"] = "0";
                        Session["Mode"] = "add";
                    }
                    else if (Request.QueryString["type"].ToString() == "edit")
                    {
                        Session["AdminTournamentId"] = Request.QueryString["tid"].ToString();
                        Session["Mode"] = "edit";
                    }

                }
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }

        [HttpPost]
        [ActionName("SaveTournament")]
        [AllowAnonymous]
        public ActionResult SaveTournament(TournamentMainModel tModel )
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.SaveTournament(tModel);
            LR.ResponseId = LR.data.FirstOrDefault().TournamentId.ToString();
            //Session["AdminTournamentId"] = LR.data.FirstOrDefault().TournamentId.ToString();
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("SaveTournamentRule")]
        [AllowAnonymous]
        public ActionResult SaveTournamentRule(TournamentRuleModel tModel)
        {
            //tModel.TournamentId = Session["AdminTournamentId"].ToString();
            //tModel.TournamentId = "11";
            
            AdminResponse LR = new AdminResponse();
            LR = ARS.SaveTournamentRule(tModel);
           
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult SaveExcel(HttpPostedFileBase pFile)
        {
            string strResult = "F";
            string strFileName = "";
            string strResMsg = "";
            
            Session["ExcelPointListData"] = null;
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
                    Session["ExcelPointListData"] = dt;
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
                    cmd.CommandText = "SELECT TournamentStage, RunScored, FourBonus, SixBonus,HalfCenturyBonus,CenturyBonus,DismissalDuck,MinBall4SR,StrikeRateBelow50,StrikeRate50To60,StrikeRate60To70,StrikeRate110To150,StrikeRateUp150,WicketTaken,Wicket3UpBonus,Wicket5UpBonus,MaidenOver,Hattrick,MinOver4ER,EconomyBelow4,Economy4To5,Economy5To6,Economy9To11,EconomyUp11,Captain,ViceCaptain,CatchTaken,Stumping,RunOutDirect,RunOutThrower,RunOutCatcher,Nitro,MoM FROM[" + sheets.Rows[0]["TABLE_NAME"].ToString() + "] ";
                    var adapter = new OleDbDataAdapter(cmd);
                    //var ds = new DataSet();
                    adapter.Fill(ds);
                }
            }
            return ds.Tables[0];
        }

        [HttpGet]
        [ActionName("GetTournamentPointList")]
        public JsonResult GetTournamentPointList(int? page, int? limit, string sortBy, string direction, string FileName)
        {
            //AdminLoginModel LM = new AdminLoginModel();
            //LM = (AdminLoginModel)Session["LoginSession"];
            DataTable dt = new DataTable();
            dt = (DataTable)Session["ExcelPointListData"];
            int total;
            var records = new GridModel().GetTournamentPointList(page, limit, sortBy, direction, dt, out total);
            List<TournamentPointModel> list = new List<TournamentPointModel>();
            
            list = records.ToList();
            Session["ExcelPointListData"] = list;
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SaveTournametPoint")]
        public JsonResult SaveTournametPoint(TournamentPointModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                
                List<TournamentPointModel> list = new List<TournamentPointModel>();
                list = (List<TournamentPointModel>)Session["ExcelPointListData"];
                if (list.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.Append("<Tournament>");
                    foreach (var l in list)
                    {
                        sb.Append("<PointDetails TournamentStage='" + l.TournamentStage + "' RunScored='" + l.RunScored + "' FourBonus='" + l.FourBonus + "' SixBonus='" + l.SixBonus + "' HalfCenturyBonus='" + l.HalfCenturyBonus + "'"); 
                         sb.Append(" CenturyBonus='" + l.CenturyBonus + "' DismissalDuck='" + l.DismissalDuck + "' MinBall4SR='" + l.MinBall4SR + "' StrikeRateBelow50='" + l.StrikeRateBelow50 + "' StrikeRate50To60='" + l.StrikeRate50To60 + "' StrikeRate60To70='" + l.StrikeRate60To70 + "'");
                         sb.Append(" StrikeRate110To150='" + l.StrikeRate110To150 + "' StrikeRateUp150='" + l.StrikeRateUp150 + "' WicketTaken='" + l.WicketTaken + "' Wicket3UpBonus='" + l.Wicket3UpBonus + "' Wicket5UpBonus='" + l.Wicket5UpBonus + "' MaidenOver='" + l.MaidenOver + "'");
                         sb.Append(" Hattrick='" + l.Hattrick + "' MinOver4ER ='" + l.MinOver4ER + "' EconomyBelow4 ='" + l.EconomyBelow4 + "' Economy4To5 ='" + l.Economy4To5 + "' Economy5To6 ='" + l.Economy5To6 + "' Economy9To11 ='" + l.Economy9To11 + "' EconomyUp11 ='" + l.EconomyUp11 + "'");
                         sb.Append(" Captain='" + l.Captain + "' ViceCaptain ='" + l.ViceCaptain + "' CatchTaken ='" + l.CatchTaken + "' Stumping ='" + l.Stumping + "' RunOutDirect ='" + l.RunOutDirect + "' RunOutThrower ='" + l.RunOutThrower + "' RunOutCatcher ='" + l.RunOutCatcher + "' Nitro ='" + l.Nitro + "' MoM='" + l.MoM + "' />");
                    }
                    sb.Append("</Tournament>");
                                        
                    string PointXML = sb.ToString();
                    tModel.xmlData = PointXML;
                    LR = ARS.SaveTournamentPoint(tModel);
                }
               
            }
            catch (Exception ex)
            {
               
            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("GetTournamentRule")]
        [AllowAnonymous]
        public ActionResult GetTournamentRule(ParamModel pModel)
        {
            AdminTournamentRuleResponse LR = new AdminTournamentRuleResponse();
            List<TournamentRuleModel> ListDetail = new List<TournamentRuleModel>();

            LR = ARS.GetTournamentRule(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public JsonResult ExportPointList(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            int total;
            var records = new GridModel().GetPointList(page, limit, sortBy, direction, pModel, out total);
            List<TournamentPointModel> ListDetail = new List<TournamentPointModel>();
            ListDetail = records.ToList();
            return Json( ListDetail, JsonRequestBehavior.AllowGet);
        }

        public ActionResult CalculatePoint()
        {
            if (Session["AdminUserName"] != null)
            {
                AdminListModel model = new AdminListModel();
                ParamModel Pmodel = new ParamModel();
                UserTournamentResponse LR = new UserTournamentResponse();
                List<UserTournamentDataResponse> ListDetail = new List<UserTournamentDataResponse>();
                Pmodel.TournamentId = "0";
                LR = ARS.GetTournament(Pmodel);
                if (LR.status == "success")
                {
                    ListDetail = LR.data.ToList();
                }
                
                model.TournamentList = new SelectList(ListDetail, "TournamentId", "TournamentName", model.SelectedTournament);
                return View(model);
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("DeleteTournament")]
        public JsonResult DeleteTournament(ParamModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.DeleteTournament(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("Calculate")]
        [AllowAnonymous]
        public ActionResult Calculate(ParamModel Pmodel)
        {
                AdminResponse Amodel = new AdminResponse();
                Amodel = ARS.CalculatePoint(Pmodel);
                
                return Json(Amodel, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("SetTransferCount")]
        [AllowAnonymous]
        public ActionResult SetTransferCount(AdminParamModel Pmodel)
        {
            AdminResponse Amodel = new AdminResponse();
            Amodel = ARS.SetTransferCount(Pmodel);

            return Json(Amodel, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("ResetSubscription")]
        [AllowAnonymous]
        public ActionResult ResetSubscription(AdminParamModel Pmodel)
        {
            ResetSub Amodel = new ResetSub();
            Amodel = ARS.ResetSubscription(Pmodel);

            return Json(Amodel, JsonRequestBehavior.AllowGet);
        }
    }
}