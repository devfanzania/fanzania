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
    public class AdminKycVerificationController : Controller
    {
        private AdminAccountRestService ARS = new AdminAccountRestService();
       // private AccountRestService ARS = new AccountRestService();
        // GET: AdminKycVerification
        public ActionResult Index()
        {
            FetchKycDetailsAdminModel PR = new FetchKycDetailsAdminModel();
            FetchKycDetailsAdmin pModel = new FetchKycDetailsAdmin();
          //  KycUploadResponse PR = new KycUploadResponse();
           // KycUploadModel rModel = new KycUploadModel();
       
            //pModel.authtoken = Session["x-api-authtoken"].ToString();
          //  PR = ARS.FetchKycDetails(pModel);

            return View();
        }
        [HttpGet]
        [ActionName("FetchKycDetails")]
        [AllowAnonymous]
        public ActionResult FetchKycDetails(FetchKycDetailsAdmin Pmodel)
        {
            try
            {
                FetchKycDetailsAdminResponce LR = new FetchKycDetailsAdminResponce();
                // FetchKycDetailsAdmin pModel = new FetchKycDetailsAdmin();
                //  Pmodel.UserId = Session["UserId"].ToString();
                List<FetchKycDetailsAdminModel> WDetail = new List<FetchKycDetailsAdminModel>();
                LR = ARS.FetchKycDetails(Pmodel);
                if (LR.status == "success")
                {
                    WDetail = LR.data.ToList();
                }
                return Json(WDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("UpdateKycStatus")]
        [AllowAnonymous]
        public ActionResult ManualScoreUpdateEachPlayer(FetchKycDetailsAdmin pModel)
        {
            UpdateKycStatusResponce LR = new UpdateKycStatusResponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();

            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.UpdateKycStatus(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }


    }
}