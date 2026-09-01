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
    public class AdminTeamController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminTeam
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
        [ActionName("ShowTeam")]
        [AllowAnonymous]
        public JsonResult ShowTeam(HttpPostedFileBase pFile)
        {
            string strResult = "F";
            string strFileName = "";
            string strResMsg = "";

            Session["TeamListData"] = null;
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
                    Session["TeamListData"] = dt;
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
                    cmd.CommandText = "SELECT ParticipationTeamName, TeamShortName, TeamDescription FROM[" + sheets.Rows[0]["TABLE_NAME"].ToString() + "] ";
                    var adapter = new OleDbDataAdapter(cmd);
                    //var ds = new DataSet();
                    adapter.Fill(ds);
                }
            }
            return ds.Tables[0];
        }

        [HttpGet]
        [ActionName("PreSaveTeamList")]
        public JsonResult PreSaveTeamList(int? page, int? limit, string sortBy, string direction, string FileName)
        {
            //AdminLoginModel LM = new AdminLoginModel();
            //LM = (AdminLoginModel)Session["LoginSession"];
            DataTable dt = new DataTable();
            dt = (DataTable)Session["TeamListData"];
            int total;
            var records = new GridModel().GetTeamList(page, limit, sortBy, direction, dt, out total);
            List<AdminTeamDetailsModel> list = new List<AdminTeamDetailsModel>();

            list = records.ToList();
            Session["TeamListData"] = list;
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }



        [HttpGet]
        [ActionName("GetSaveTeam")]
        public JsonResult GetSaveTeam(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            //AdminLoginModel LM = new AdminLoginModel();
            //LM = (AdminLoginModel)Session["LoginSession"];
            int total;
            var records = new GridModel().GetSaveTeam(page, limit, sortBy, direction, pModel, out total);
            List<AdminTeamDetailsModel> list = new List<AdminTeamDetailsModel>();

            list = records.ToList();
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SaveTeam")]
        public JsonResult SaveTeam(AdminTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {

                List<AdminTeamDetailsModel> list = new List<AdminTeamDetailsModel>();
                list = (List<AdminTeamDetailsModel>)Session["TeamListData"];
                if (list.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.Append("<Tournament>");
                    foreach (var l in list)
                    {
                        sb.Append("<TeamDetails ParticipationTeamName='" + l.ParticipationTeamName + "' TeamShortName='" + l.TeamShortName + "' TeamDescription='" + l.TeamDescription + "' />");
                    }
                    sb.Append("</Tournament>");

                    string MatchXML = sb.ToString();
                    tModel.xmlData = MatchXML;
                    LR = ARS.SaveTeam(tModel);
                }

            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        public ActionResult TeamEdit()
        {
            if (Session["AdminUserName"] != null)
            {
                if (Request.QueryString["ptid"] != null)
                {
                    Session["T_ptid"] = Request.QueryString["ptid"].ToString();
                    Session["T_tid"] = Request.QueryString["tid"].ToString();
                }
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }

        [HttpPost]
        [ActionName("GetTeamForUpdate")]
        [AllowAnonymous]
        public ActionResult GetTeamForUpdate(ParamModel pModel)
        {
            AdminTeamModel LR = new AdminTeamModel();
            List<AdminTeamDetailsModel> ListDetail = new List<AdminTeamDetailsModel>();

            LR = ARS.GetSaveTeam(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("UpdateTeam")]
        [AllowAnonymous]
        public ActionResult UpdateTeam(AdminTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.UpdateTeam(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("ResetPlayerPoints")]
        [AllowAnonymous]
        public ActionResult ResetPlayerPoints(AdminTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            LR = ARS.ResetPlayerPoints(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("DeleteTeam")]
        public JsonResult DeleteTeam(AdminTeamDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.DeleteTeam(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
    }
}