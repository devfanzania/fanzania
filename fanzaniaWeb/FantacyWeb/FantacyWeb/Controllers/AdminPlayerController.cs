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
    public class AdminPlayerController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminPlayer
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
        [ActionName("GetTeam")]
       
        public ActionResult GetTeam(ParamModel pModel)
        {
            AdminTeamModel LR = new AdminTeamModel();
            List<AdminTeamDetailsModel> ListDetail = new List<AdminTeamDetailsModel>();

            LR = ARS.GetTeam_DailyMatch(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();

            }

            return Json(ListDetail, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("ShowPlayer")]
        [AllowAnonymous]
        public JsonResult ShowPlayer(HttpPostedFileBase pFile)
        {
            string strResult = "F";
            string strFileName = "";
            string strResMsg = "";

            Session["PlayerListData"] = null;
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
                    Session["PlayerListData"] = dt;
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
                    cmd.CommandText = "SELECT PlayerName, PlayerShortName, PlayerDesc, PlayerType,PlayerSpeciality,PlayerValue,ParticipationTeam FROM[" + sheets.Rows[0]["TABLE_NAME"].ToString() + "] ";
                    var adapter = new OleDbDataAdapter(cmd);
                    //var ds = new DataSet();
                    adapter.Fill(ds);
                }
            }
            return ds.Tables[0];
        }

        [HttpGet]
        [ActionName("PreSavePlayerList")]
        public JsonResult PreSavePlayerList(int? page, int? limit, string sortBy, string direction, string FileName)
        {
           
            DataTable dt = new DataTable();
            dt = (DataTable)Session["PlayerListData"];
            int total;
            var records = new GridModel().GetAdminPlayerList(page, limit, sortBy, direction, dt, out total);
            List<AdminPlayerDetailsModel> list = new List<AdminPlayerDetailsModel>();

            list = records.ToList();
            Session["PlayerListData"] = list;
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        [ActionName("GetSavePlayer")]
        public JsonResult GetSavePlayer(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
           
            int total;
            var records = new GridModel().GetSavePlayer(page, limit, sortBy, direction, pModel, out total);
            List<AdminPlayerDetailsModel> list = new List<AdminPlayerDetailsModel>();

            list = records.ToList();
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("SavePlayer")]
        public JsonResult SavePlayer(AdminPlayerDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                List<AdminPlayerDetailsModel> list = new List<AdminPlayerDetailsModel>();
                list = (List<AdminPlayerDetailsModel>)Session["PlayerListData"];
                if (list.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.Append("<Tournament>");
                    foreach (var l in list)
                    {
                        sb.Append("<PlayerDetails  PlayerName='" + l.PlayerName + "' PlayerShortName='" + l.PlayerShortName + "' PlayerDesc='" + l.PlayerDesc + "' PlayerType='" + l.PlayerType + "'");
                        sb.Append(" PlayerSpeciality='" + l.PlayerSpeciality + "' PlayerValue='" + l.PlayerValue + "' ParticipationTeam='" + l.ParticipationTeamName + "'/>");
                    }
                    sb.Append("</Tournament>");

                    string MatchXML = sb.ToString();
                    tModel.xmlData = MatchXML;
                    LR = ARS.SavePlayer(tModel);
                }

            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        public ActionResult PlayerEdit()
        {
            if (Session["AdminUserName"] != null)
            {
                if (Request.QueryString["pid"] != null)
                {
                    Session["P_pid"] = Request.QueryString["pid"].ToString();
                    Session["P_tid"] = Request.QueryString["tid"].ToString();
                }
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Admin");
            }
        }
        [HttpPost]
        [ActionName("GetPlayerForUpdate")]
        [AllowAnonymous]
        public ActionResult GetPlayerForUpdate(ParamModel pModel)
        {
            AdminPlayerModel LR = new AdminPlayerModel();
            List<AdminPlayerDetailsModel> ListDetail = new List<AdminPlayerDetailsModel>();
            List<AdminPlayerDetailsModel> SortedList = new List<AdminPlayerDetailsModel>();
           
            LR = ARS.GetSavePlayer(pModel);
            if (LR.status == "success")
            {
                ListDetail = LR.data.ToList();
                SortedList = ListDetail.Where(c => c.PlayerId.ToString() == pModel.PlayerId).ToList();
                
            }

            return Json(SortedList, JsonRequestBehavior.AllowGet);
        }


        [HttpPost]
        [ActionName("UpdatePlayer")]
        [AllowAnonymous]
        public ActionResult UpdatePlayer(AdminPlayerDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            
            LR = ARS.UpdatePlayer(tModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("DeletePlayer")]
        public JsonResult DeletePlayer(AdminPlayerDetailsModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.DeletePlayer(tModel);
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ActionName("ActiveInactive")]
        [AllowAnonymous]
        public ActionResult ActiveInactive(AdminParamModel PModel)
        {
            AdminResponse LR = new AdminResponse();

            LR = ARS.ActiveInactive(PModel);
            return Json(LR, JsonRequestBehavior.AllowGet);
        }
    }
}