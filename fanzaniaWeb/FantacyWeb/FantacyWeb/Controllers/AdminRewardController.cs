using System;
using System.Collections.Generic;
using System.Data;
using System.Data.OleDb;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;
using System.Web.UI.WebControls;
using Fantacy_Model;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class AdminRewardController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
        // GET: AdminRewards
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
        [ActionName("ShowRewardList")]
        [AllowAnonymous]
        public ActionResult ShowRewardList()
        {
            RewardResponseModel LR = new RewardResponseModel();
            List<RewardResponseDetailsModel> ListDetail = new List<RewardResponseDetailsModel>();
            try
            {
                LR = ARS.ShowRewardList();
                if (LR.status == "success")
                {
                    ListDetail = LR.data.ToList();
                }
                return Json(ListDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
        }

        [HttpGet]
        [ActionName("DownloadRewardList")]
        [AllowAnonymous]
        public ActionResult DownloadRewardList()
        {
            RewardResponseModel LR = new RewardResponseModel();
            List<RewardResponseDetailsModel> ListDetail = new List<RewardResponseDetailsModel>();
            try
            {
                LR = ARS.DownloadRewardList();
                if (LR.status == "success")
                {
                    ListDetail = LR.data.ToList();
                }
                //ExportToExcel(ListDetail);
                return Json(ListDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("CalcReferralReward")]
        public JsonResult CalcReferralReward()
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.CalcReferralReward();
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
           
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("CalcTournamentReward")]
        public JsonResult CalcTournamentReward(ParamModel pModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {
                LR = ARS.CalcTournamentReward(pModel);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                LR.status = "error";
                return Json(LR, JsonRequestBehavior.AllowGet);
            }

        }

        [HttpPost]
        [ActionName("ShowClaimList")]
        [AllowAnonymous]
        public JsonResult ShowClaimList(HttpPostedFileBase pFile)
        {
            string strResult = "F";
            string strFileName = "";
            string strResMsg = "";

            Session["ClaimList"] = null;
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
                    Session["ClaimList"] = dt;
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
                    cmd.CommandText = "SELECT UserId, ClaimAmount, Bundle, Voucher, Comments FROM[" + sheets.Rows[0]["TABLE_NAME"].ToString() + "] ";
                    var adapter = new OleDbDataAdapter(cmd);
                    adapter.Fill(ds);
                }
            }
            return ds.Tables[0];
        }


        [HttpGet]
        [ActionName("PreSaveClaimList")]
        public JsonResult PreSaveClaimList(int? page, int? limit, string sortBy, string direction, string FileName)
        {
            DataTable dt = new DataTable();
            dt = (DataTable)Session["ClaimList"];
            int total;
            var records = new GridModel().GetClaimList(page, limit, sortBy, direction, dt, out total);
            List<ClaimUploadDetailsModel> list = new List<ClaimUploadDetailsModel>();

            list = records.ToList();
            Session["ClaimList"] = list;
            return Json(new { records, total }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        [ActionName("UploadClaim")]
        public JsonResult UploadClaim(UploadClaimModel tModel)
        {
            AdminResponse LR = new AdminResponse();
            try
            {

                List<ClaimUploadDetailsModel> list = new List<ClaimUploadDetailsModel>();
                list = (List<ClaimUploadDetailsModel>)Session["ClaimList"];
                if (list.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.Append("<Claims>");
                    foreach (var l in list)
                    {
                        sb.Append("<Claim UserId='" + l.UserId + "' ClaimAmount='" + l.ClaimAmount + "' Bundle='" + l.Bundle + "' Vouchar = '" + l.Voucher + "' Comments = '" + l.Comments + "' />");
                    }
                    sb.Append("</Claims>");

                    string ClaimXML = sb.ToString();
                    tModel.xmlData = ClaimXML;
                    LR = ARS.UploadClaim(tModel);
                }
            }
            catch (Exception ex)
            {

            }
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

    }
}