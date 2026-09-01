using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;
using System.IO;
using System.Globalization;
using System.Configuration;

namespace FantacyWeb.Controllers
{
    public class ProfileController : Controller
    {
        private AccountRestService ARS = new AccountRestService();

        public string _ProfileImagePath = ConfigurationManager.AppSettings["ProfilePicUrl"];
        // GET: Profile
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    ViewBag.UserName = Session["UserName"].ToString();
                    
                    ProfileModel rModel = new ProfileModel();
                    List<CourntryList> CList = new List<CourntryList>();
                   
                    
                    ParamModel pModel = new ParamModel();
                    ProfileResponse PR = new ProfileResponse();
                    pModel.UserId = Session["UserId"].ToString();
                           //  var tid= Session["TournamentId"].ToString(); 
                    pModel.authtoken = Session["x-api-authtoken"].ToString();
                 
                    PR = ARS.FetchProfile(pModel);
                    if (PR.status == "success")
                    {
                        
                        rModel.Name = PR.data.FirstOrDefault().Name.ToString();
                        rModel.Email = PR.data.FirstOrDefault().Email.ToString();
                        // rModel.BackgroundTheme = PR.data.FirstOrDefault().BackgroundTheme.ToString();
                        //string x = DateTime.Now.ToString("dd-MMM-yyyy", CultureInfo.InvariantCulture);
                        //DateTime dt = Convert.ToDateTime(PR.data.FirstOrDefault().DOB);
                         // rModel.DOB = (PR.data.FirstOrDefault().DOB == null) ? "" : PR.data.FirstOrDefault().DOB.ToString();
                        rModel.DOB = (PR.data.FirstOrDefault().DOB == null) ? "" : Convert.ToDateTime(PR.data.FirstOrDefault().DOB).ToString("dd-MMM-yyyy", CultureInfo.InvariantCulture);
                        rModel.PhoneNumber = (PR.data.FirstOrDefault().PhoneNumber == null) ? "" : PR.data.FirstOrDefault().PhoneNumber.ToString();
                        //rModel.ProfileImage = (PR.data.FirstOrDefault().ProfileImage == null) ? "blank-profile.png" : PR.data.FirstOrDefault().ProfileImage.ToString();
                       // rModel.ProfileImage = _ProfileImagePath + rModel.ProfileImage;
                        rModel.CountryId = (PR.data.FirstOrDefault().CountryId == null) ? "0" : PR.data.FirstOrDefault().CountryId.ToString();
                        rModel.BackgroundTheme = (PR.data.FirstOrDefault().BackgroundTheme == null) ? "0" : PR.data.FirstOrDefault().BackgroundTheme.ToString();
                        if (String.IsNullOrEmpty(PR.data.FirstOrDefault().ProfileImage))
                        {
                            rModel.ProfileImage = "/images/blank-profile.png";
                        }
                        else
                        {
                            rModel.ProfileImage = _ProfileImagePath +  PR.data.FirstOrDefault().ProfileImage.ToString();
                        }
                        rModel.CommPreference = PR.data.FirstOrDefault().CommPreference;
                        rModel.ReferralCode = "Referral Code " + PR.data.FirstOrDefault().ReferralCode;
                        rModel.ReferralCount = "Referral Count " + PR.data.FirstOrDefault().ReferralCount;
                    }
                    CList = LoadCountry();
                    rModel.Countrylist = CList.ToList();
                    //pass here tournament id
                    if (Session["TournamentIdd"] != null)
                    {
                        List<ListOfTeamm> TList = LoadListOfTeam(Session["TournamentIdd"].ToString());
                        rModel.ListOfTeam = TList.ToList();
                    }
                    else {
                        rModel.ListOfTeam = null;
                    }
                    //rModel.ParticipationTeamId = (TList.FirstOrDefault() == null) ? "0" : TList.FirstOrDefault().ParticipationTeamId.ToString();



                    return View(rModel);
                }
                else
                {
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        public List<CourntryList> LoadCountry()
        {
            CountryResponse LR = new CountryResponse();
            List<CourntryList> CList = new List<CourntryList>();
            LR = ARS.CountryList();
            if (LR.status == "success")
            {
                CList = LR.data.ToList();
            }
            return CList;
        }
        public List<ListOfTeamm> LoadListOfTeam(string tournamentId)
        {
            TournamentModel tr = new TournamentModel();
            tr.TournamentId = tournamentId;
            ListOfTeamResponse LR = new ListOfTeamResponse();
            List<ListOfTeamm> TList = new List<ListOfTeamm>();
            LR = ARS.ListOfTeam(tr);
            if (LR.status == "success")
            {
                TList = LR.data.ToList();
            }
            return TList;
        }
        

        [HttpPost]
        public ActionResult Index(ProfileModel PM)
        {
            try
            {
                ProfileUploadResponse PR = new ProfileUploadResponse();
                PM.UserId = Session["UserId"].ToString();
                PM.ReferralCode = "";
                PM.ReferralCount = "";
                PR = ARS.UploadProfile(PM);
                if (PR.status == "success")
                {
                    Session["UserName"] = PR.data.FirstOrDefault().Name.ToString();
                    PM.Name = PR.data.FirstOrDefault().Name.ToString();
                    PM.Email = PR.data.FirstOrDefault().Email.ToString();
                   // PM.DOB = (PR.data.FirstOrDefault().DOB == null) ? "" : Convert.ToDateTime(PR.data.FirstOrDefault().DOB).ToString("dd-MMM-yyyy", CultureInfo.InvariantCulture);

                    PM.DOB = (PR.data.FirstOrDefault().DOB == null) ? "" : PR.data.FirstOrDefault().DOB.ToString();
                    PM.PhoneNumber = (PR.data.FirstOrDefault().PhoneNumber == null) ? "" : PR.data.FirstOrDefault().PhoneNumber.ToString();
                    PM.ProfileImage = (PR.data.FirstOrDefault().ProfileImage == null) ? "/images/blank-profile.png" : PR.data.FirstOrDefault().ProfileImage.ToString();
                    PM.ProfileImage = _ProfileImagePath + PM.ProfileImage;
                    PM.CountryId = (PR.data.FirstOrDefault().CountryId == null) ? "0" : PR.data.FirstOrDefault().CountryId.ToString();
                    PM.BackgroundTheme = (PR.data.FirstOrDefault().BackgroundTheme == null) ? "0" : PR.data.FirstOrDefault().BackgroundTheme.ToString();
                    PM.CommPreference = PR.data.FirstOrDefault().CommPreference;
                    Session["PhoneNumber"] = PR.data.FirstOrDefault().PhoneNumber;
                    ViewBag.Message = "Update Successfully";
                }
                else
                {
                    ViewBag.Message = "Not Update";
                }
                List<CourntryList> CList = new List<CourntryList>();
                CList = LoadCountry();
                PM.Countrylist = CList.ToList();

                if (Session["TournamentIdd"] != null)
                {
                    List<ListOfTeamm> TList = LoadListOfTeam(Session["TournamentIdd"].ToString());
                    PM.ListOfTeam = TList.ToList();
                }
                else
                {
                    PM.ListOfTeam = null;
                }
               
                ViewBag.UserName = Session["UserName"].ToString();
                return View(PM);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }


        [HttpPost]
        [ActionName("ImageUpload")]
        [AllowAnonymous]
        public ActionResult ImageUpload(ProfileModel PM)
        {
            try
            {
                ProfileUploadResponse PR = new ProfileUploadResponse();
                if (PM.UploadedImage != null && PM.UploadedImage.ContentLength > 0)
                {
                    PM.UserId = Session["UserId"].ToString();
                    PR = ARS.UploadProfilePic(PM);
                }
                return Json(PR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("KycImageUpload")]
        [AllowAnonymous]
        public ActionResult KycImageUpload(KycUpload PM)
        {
            try
            {
                KycUploadResponse PR = new KycUploadResponse();
                if (PM.UploadedImage != null && PM.UploadedImage.ContentLength > 0)
                {
                    PM.UserId = Session["UserId"].ToString();
                    PR = ARS.KycUploadProfilePic(PM);
                }
                return Json(PR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        public ActionResult ChangePassword()
        {
            ViewBag.UserName = Session["UserName"].ToString();
            return View();
        }

        [HttpPost]
        [ActionName("UpdatePassword")]
        [AllowAnonymous]
        public ActionResult UpdatePassword(ChangePassDataModel CP)
        {
            try
            {
                ChangePassResponse LR = new ChangePassResponse();
                List<ChangePassDataModel> TDetail = new List<ChangePassDataModel>();
                CP.UserId = Session["UserId"].ToString();
                CP.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UpdatePassword(CP);
                return Json(LR, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        //--------------------------------------------------- Wallet Page ------------------------------------------------

        public ActionResult wallet()
        {
            if (Session["UserName"] != null)
            {
                Session["USERSELECTION"] = "Wallet";
                ViewBag.Page = "Wallet";

                ViewBag.UserName = Session["UserName"].ToString();
                ViewBag.Email = Session["Email"].ToString();
                
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Account");
            }
        }

        [HttpPost]
        [ActionName("FetchWalletInfo")]
        [AllowAnonymous]
        public ActionResult FetchWalletInfo(ParamModel Pmodel)
        {
            try
            {
                WalletModelResponse LR = new WalletModelResponse();
                List<WalletModel> WDetail = new List<WalletModel>();
                Pmodel.UserId = Session["UserId"].ToString();
                
                LR = ARS.FetchWalletInfo(Pmodel);
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
        [ActionName("FetchTotalClaims")]
        [AllowAnonymous]
        public ActionResult FetchTotalClaims(ParamModel Pmodel)
        {
            try
            {
                TotalClaimModelResponse LR = new TotalClaimModelResponse();
                List<TotalClaimModel> WDetail = new List<TotalClaimModel>();
                Pmodel.UserId = Session["UserId"].ToString();

                LR = ARS.FetchTotalClaims(Pmodel);
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
        [ActionName("FetchTotalRewards")]
        [AllowAnonymous]
        public ActionResult FetchTotalRewards(ParamModel Pmodel)
        {
            try
            {
                TotalRewardModelResponse LR = new TotalRewardModelResponse();
                List<TotalRewardModel> WDetail = new List<TotalRewardModel>();
                Pmodel.UserId = Session["UserId"].ToString();

                LR = ARS.FetchTotalRewards(Pmodel);
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
        public ActionResult KycUpload() {
          
            try
            {
                if (Session["UserName"] != null)
                {
                    Session["USERSELECTION"] = "KycUpload";
                 
                    ViewBag.UserName = Session["UserName"].ToString();
                 //   ViewBag.PhoneNumber = Session["PhoneNumber"].ToString();
                    ViewBag.Email = Session["Email"].ToString();
                    ViewBag.Page = "KycUpload";
                    //KycUpload rModel = new KycUpload();
                    KycUpload pModel = new KycUpload();
                    KycUploadResponse PR = new KycUploadResponse();
                    KycUploadModel rModel = new KycUploadModel();
                    pModel.UserId = Session["UserId"].ToString();
                    //pModel.authtoken = Session["x-api-authtoken"].ToString();
                    PR = ARS.FetchKycDetails(pModel);
                    if (PR.status == "success" && PR.data.Count>0)
                    {
                        if (PR.data.FirstOrDefault().MobileVerified != null)
                        {
                            rModel.MobileVerified = PR.data.FirstOrDefault().MobileVerified.ToString();
                        }
                        if (PR.data.FirstOrDefault().KYCDocName != null)
                        {
                            rModel.KYCDocName = PR.data.FirstOrDefault().MobileVerified.ToString();
                        }

                        if (PR.data.FirstOrDefault().PANName != null)
                        {
                            rModel.PANName = PR.data.FirstOrDefault().PANName.ToString();
                        }
                       if (PR.data.FirstOrDefault().KYCStatus != null) {
                          rModel.KYCStatus = PR.data.FirstOrDefault().KYCStatus.ToString();
                     }
                       
                        if (PR.data.FirstOrDefault().PANDOB != null && PR.data.FirstOrDefault().PANDOB != "") {
                            rModel.PANDOB = (PR.data.FirstOrDefault().PANDOB == null) ? "" : PR.data.FirstOrDefault().PANDOB.ToString();
                            //rModel.PANDOB = (PR.data.FirstOrDefault().PANDOB == null) ? "" : Convert.ToDateTime(PR.data.FirstOrDefault().PANDOB).ToString("dd-MMM-yyyy", CultureInfo.InvariantCulture);
                        }
                        if (PR.data.FirstOrDefault().PANState != null) {
                            rModel.PANState = PR.data.FirstOrDefault().PANState.ToString();
                        }

                        if (PR.data.FirstOrDefault().PANNumber != null)
                        {
                            rModel.PANNumber = (PR.data.FirstOrDefault().PANNumber == null) ? "" : PR.data.FirstOrDefault().PANNumber.ToString();

                        }
                        //rModel.ProfileImage = (PR.data.FirstOrDefault().ProfileImage == null) ? "blank-profile.png" : PR.data.FirstOrDefault().ProfileImage.ToString();
                        // rModel.ProfileImage = _ProfileImagePath + rModel.ProfileImage;
                      //  pModel.PANNumber = (PR.data.FirstOrDefault().PANNumber == null) ? "0" : PR.data.FirstOrDefault().PANNumber.ToString();
                       // pModel.BackgroundTheme = (PR.data.FirstOrDefault().BackgroundTheme == null) ? "0" : PR.data.FirstOrDefault().BackgroundTheme.ToString();
                      //  if (String.IsNullOrEmpty(PR.data.FirstOrDefault().KYCDocImage))
                        //{
                          //  pModel.KYCDocImage = "/images/blank-profile.png";
                      //  }
                       // else
                       // {
                           // rModel.KYCDocImage = _ProfileImagePath + PR.data.FirstOrDefault().KYCDocImage.ToString();
                      //  }
                       
                    }
                 
                    return View(rModel);
                }
                else
                {
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }


        }
        [HttpPost]
        [ActionName("UpdateKycStatus")]
        [AllowAnonymous]
        public ActionResult ManualScoreUpdateEachPlayer(updatekycstatus pModel)
        {
            UpdateKycStatusResponce LR = new UpdateKycStatusResponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();
            pModel.UserId = Session["UserId"].ToString();
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
        [HttpPost]
        [ActionName("updatebankdetails")]
        [AllowAnonymous]
        public ActionResult updatebankdetails(updatebankdetails pModel)
        {
            updatebankdetailsresponce LR = new updatebankdetailsresponce();
            pModel.UserId = Session["UserId"].ToString();
            LR = ARS.updatebankdetails(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("transferfunds")]
        [AllowAnonymous]
        public ActionResult transferfunds(transferfundsdetails pModel)
        {
            updatebankdetailsresponce LR = new updatebankdetailsresponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();
            pModel.UserId = Session["UserId"].ToString();
            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.transferfunds(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        
        [HttpPost]
        [ActionName("fetchbankdetails")]
        [AllowAnonymous]
        public ActionResult fetchbankdetails(SubscriptionDetails pModel)
        {
            featchbankdetailsresponce LR = new featchbankdetailsresponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();
            pModel.UserId = Session["UserId"].ToString();
            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.fetchbankdetails(pModel);
          //  if (LR.status == "success")
            //{
                //ListDetail = LR.data.();

            //}

            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("sendverificationcode")]
        [AllowAnonymous]
        public ActionResult sendverificationcode(updatekycstatus pModel)
        {
            UpdateKycStatusResponce LR = new UpdateKycStatusResponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();
            pModel.UserId = Session["UserId"].ToString();
            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
            //lModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.sendverificationcode(pModel);
            if (LR.status == "success")
            {
                //ListDetail = LR.data.();

            }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        [ActionName("verifyotp")]
        [AllowAnonymous]
        public ActionResult verifyotp(verifyotpmodel pModel)
        {
            verifyotpResponce LR = new verifyotpResponce();
            //     List<FetchManualScoreResponceData> ListDetail = new List<FetchManualScoreResponceData>();
            pModel.UserId = Session["UserId"].ToString();
            //pModel.TournamentId = "0";
            //lModel.UserId = Session["UserId"].ToString();
          //  pModel.authtoken = Session["x-api-authtoken"].ToString();
            LR = ARS.verifyotp(pModel);
      //      if (LR.status == "success")
        //    {
                //ListDetail = LR.data.();

         //   }

            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult KycUpload(KycUploadModel PM) {
          
            try
            {
                Session["USERSELECTION"] = "KycUpload";
                ViewBag.Page = "KycUpload";
                KycUploadResponse PR = new KycUploadResponse();
                PM.UserId = Session["UserId"].ToString();
              
                PR = ARS.UpdateKycDetails(PM);
                if (PR.status == "success" && PR.data.Count > 0)
                {
                    //Session["UserName"] = PR.data.FirstOrDefault().Name.ToString();
                    if (PR.data.FirstOrDefault().PANName != null)
                    {
                        PM.PANName = PR.data.FirstOrDefault().PANName.ToString();
                    }
                  

                    if (PR.data.FirstOrDefault().PANDOB != null)
                    {
                        PM.PANDOB = (PR.data.FirstOrDefault().PANDOB == null) ? "" : Convert.ToDateTime(PR.data.FirstOrDefault().PANDOB).ToString("dd-MMM-yyyy", CultureInfo.InvariantCulture);
                    }
                    if (PR.data.FirstOrDefault().PANState != null)
                    {
                        PM.PANState = PR.data.FirstOrDefault().PANState.ToString();
                    }

                    if (PR.data.FirstOrDefault().MobileVerified != null)
                    {
                        PM.MobileVerified = (PR.data.FirstOrDefault().MobileVerified == null) ? "" : PR.data.FirstOrDefault().MobileVerified.ToString();

                    }
                   

                    if (PR.data.FirstOrDefault().PANNumber != null)
                    {
                        PM.PANNumber = (PR.data.FirstOrDefault().PANNumber == null) ? "" : PR.data.FirstOrDefault().PANNumber.ToString();

                    }

                    //  PM.PhoneNumber = (PR.data.FirstOrDefault().PhoneNumber == null) ? "" : PR.data.FirstOrDefault().PhoneNumber.ToString();
                    // PM.ProfileImage = (PR.data.FirstOrDefault().ProfileImage == null) ? "/images/blank-profile.png" : PR.data.FirstOrDefault().ProfileImage.ToString();
                    //  PM.ProfileImage = _ProfileImagePath + PM.ProfileImage;
                    updatekycstatus tr = new updatekycstatus();
                    UpdateKycStatusResponce LR = new UpdateKycStatusResponce();
                    tr.UserId = Session["UserId"].ToString();
                    tr.KYCStatus = "pending";
                    LR= ARS.UpdateKycStatus(tr);
                    if (LR.data.FirstOrDefault().KYCStatus != null)
                    {
                        PM.KYCStatus = LR.data.FirstOrDefault().KYCStatus.ToString();
                    }
                    ViewBag.Message = "Update Successfully";
                }
                else
                {
                    ViewBag.Message = "Not Update";
                }
               
              

                return View(PM);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
    }
}