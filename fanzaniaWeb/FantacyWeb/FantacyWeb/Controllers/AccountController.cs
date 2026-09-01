using System;
using System.Globalization;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using FantacyWeb.Models;
using Fantacy_Model;
using System.Collections.Generic;
using Facebook;
using System.Web.Security;
using DocumentFormat.OpenXml.Wordprocessing;
using System.Security.Cryptography;

namespace FantacyWeb.Controllers
{
    [Authorize]
    public class AccountController : Controller
    {
        //private ApplicationSignInManager _signInManager;
        //private ApplicationUserManager _userManager;

        private AccountRestService ARS = new AccountRestService();

        // POST: /Account/ExternalLoginConfirmation
        [HttpGet]
        [AllowAnonymous]
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        [AllowAnonymous]
        public ActionResult Index(LoginModel pModel)
        {
                     
            if (ModelState.IsValid)
            {
               // Response LR = new Response();
                Response LR = ARS.ValidateUserLogin(pModel);
                if (LR.status == "success")
                {
                  
                    Session["UserId"] = LR.data.FirstOrDefault().UserId;
                    ReferralCodeModel TR = new ReferralCodeModel();
                    TR.UserId = LR.data.FirstOrDefault().UserId;
                    FetchReferralCode  RC = ARS.FetchReferralCode(TR);
                    if (RC.status == "success")
                    {
                        Session["ReferralCodee"] = RC.data.FirstOrDefault().ReferralCode;
                    }
                    Session["UserName"] = LR.data.FirstOrDefault().Name;
                    Session["Email"] = LR.data.FirstOrDefault().Email;
                    Session["PhoneNumber"] = LR.data.FirstOrDefault().PhoneNumber;
                    Session["x-api-authtoken"] = LR.data.FirstOrDefault().SessionId;
                    Session["USERSELECTIONMODE"] = "T";
                   
                    if (LR.data.FirstOrDefault().LoginPreference.Equals("tournament"))
                    {

                    //    return RedirectToAction("Index", "DailyGame/HomeD");
                     return RedirectToAction("Index", "Dashboard");
                    }
                    else 
                    {
                        return RedirectToAction("Index", "DailyGame/HomeD");
                    }
                }
                else
                {
                    ViewBag.Message = "This email address or password is incorrect.";

                }
            }
            return View();

        }

        [HttpGet]
        [AllowAnonymous]
        public ActionResult Register()
        {

            Captcha lastSentCaptcha = new Captcha();
            string[] dataToSend = lastSentCaptcha.genCaptcha();
            RegistrationModel toModel = new RegistrationModel();

            toModel.CaptchaCodeSentParam1 = "";
            toModel.CaptchaCodeSentOperator = "";
            toModel.CaptchaCodeSentParam2 = "";
            toModel.CaptchaCodeUsed = "";
            toModel.CaptchaCodeError = "";
            if (lastSentCaptcha.captchaSet)
            {
                toModel.CaptchaCodeSentParam1 = dataToSend[0];
                toModel.CaptchaCodeSentOperator = dataToSend[1];
                toModel.CaptchaCodeSentParam2 = dataToSend[2];
            }
            Session["lastSentCaptcha"] = lastSentCaptcha;
            return View(toModel);

            //return View();
        }
       
        [HttpPost]
        [AllowAnonymous]
        public ActionResult Register(RegistrationModel rModel)
        {
            RegistrationModel toModel = new RegistrationModel();
            toModel.CaptchaCodeSentParam1 = "";
            toModel.CaptchaCodeSentOperator = "";
            toModel.CaptchaCodeSentParam2 = "";
            toModel.CaptchaCodeUsed = "";
            toModel.CaptchaCodeError = "";
            string data = rModel.CaptchaCodeUsed;
            bool flag = false;
            Captcha lastSentCaptcha = new Captcha();
            if (data == "" || data == null)
            {
                toModel.CaptchaCodeError = "Please enter captcha";
            }
            else
            {
                try
                {
                    int res = Int32.Parse(data.Trim());
                    lastSentCaptcha = (Captcha)Session["lastSentCaptcha"];
                    if (lastSentCaptcha.result == res)
                    {
                        toModel.CaptchaCodeError = "OK";
                        flag = true;
                        lastSentCaptcha.captchaSet = false;
                        #region Registration
                        if (ModelState.IsValid)
                        {
                            Response LR = new Response();
                            DefaultResponse DR = new DefaultResponse();
                            DR = ARS.Verify_User(rModel);
                            if (DR.status == "success")
                            {
                                rModel.UserName = rModel.Email;
                                LR = ARS.UserSignup(rModel);
                                if (LR.status == "success")
                                {
                                    Session["UserId"] = LR.data.FirstOrDefault().UserId;
                                    Session["UserName"] = LR.data.FirstOrDefault().Name;
                                    Session["UserEmail"] = LR.data.FirstOrDefault().Email;
                                    Session["x-api-authtoken"] = LR.data.FirstOrDefault().SessionId;
                                    Session["EmailVerifyCode"] = LR.data.FirstOrDefault().ActivationToken;

                                    ModelState.Clear();
                                    return RedirectToAction("EmailVerify", "Account");
                                }
                                else
                                {
                                    ViewBag.Message = LR.statusMessage;
                                    flag = false;
                                }
                            }
                            else
                            {
                                ViewBag.Message = "Sorry !! " + DR.statusMessage;
                                flag = false;
                            }
                        }

                        ModelState.Clear();
                        #endregion
                    }
                    else
                    {
                        toModel.CaptchaCodeError = "Invalid Captcha";
                    }
                }
                catch (Exception err)
                {
                    toModel.CaptchaCodeError = "Captcha Error: " + err.ToString();
                }
            }
            if (false == flag)
            {
                ModelState.Clear();
                string[] dataToSend = lastSentCaptcha.genCaptcha();
                toModel.CaptchaCodeSentParam1 = dataToSend[0];
                toModel.CaptchaCodeSentOperator = dataToSend[1];
                toModel.CaptchaCodeSentParam2 = dataToSend[2];
                Session["lastSentCaptcha"] = lastSentCaptcha;
            }
            return View(toModel);
        }

        [HttpGet]
        [AllowAnonymous]
        public ActionResult EmailVerify()
        {
            //EmailResponse LR = new EmailResponse();
            //ParamModel PM = new ParamModel();
            //PM.UserId = Session["UserId"].ToString();
            //PM.Email = Session["UserEmail"].ToString();
            //PM.authtoken = Session["x-api-authtoken"].ToString();
            //LR = ARS.EmailVerify(PM);
            //if (LR.status == "success")
            //{
            //   Session["EmailVerifyCode"] = LR.data.FirstOrDefault().ActivationToken;
            //}
            ViewBag.Email = Session["UserEmail"].ToString();
            return View();
        }

        [HttpPost]
        [AllowAnonymous]
        public ActionResult EmailVerify(EmailVerify EM)
        {
            VerifiedResponse LR = new VerifiedResponse();
            ParamModel PM = new ParamModel();

            if (Session["EmailVerifyCode"].ToString() == EM.VerificationCode)
            {
                PM.UserId = Session["UserId"].ToString(); ;
                PM.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.EmailVerified(PM);
                if (LR.status == "success")
                {
                    if (LR.data.FirstOrDefault().Active == "True" || LR.data.FirstOrDefault().Active == "1")
                    {
                        Session["USERSELECTIONMODE"] = "T";
                        return RedirectToAction("Index", "Dashboard");
                    }
                    else
                    {
                        ViewBag.Message = "Contact Your fanzania support";
                    }
                }
            }
            else
            {
                ViewBag.Message = "You has enter wrong verification code";
            }

            return View();
        }

        [HttpPost]
        [ActionName("ForgotPassword")]
        [AllowAnonymous]
        public ActionResult ForgotPassword(LoginModel lModel)
        {
            DefaultResponse LR = new DefaultResponse();
           
            LR = ARS.ForgotPassword(lModel);
           
            return Json(LR, JsonRequestBehavior.AllowGet);
        }

        [AllowAnonymous]
        public ActionResult Logout()
        {
            Response LR = new Response();
            ParamModel pmodel = new ParamModel();
            //pmodel.UserId = Session["UserId"].ToString();
            LR = ARS.LogOut(pmodel);
            if (LR.status == "success")
            {
                Session["UserId"] = null;
                Session["UserName"] = null;
                Session.Abandon();
                Session.RemoveAll();
            }

            return RedirectPermanent("/");
        }

        

        #region Google Login
        [HttpPost]
        [ActionName("ExternalLoginValidateGoogle")]
        [AllowAnonymous]
        public ActionResult ExternalLoginValidateGoogle(LoginModel lModel)
        {
            var res = ExterLoginValidate(lModel.Email, "Google", lModel.ExternalUserID, lModel.ExternalUserID, lModel.Name);
            if (res == "success")
            {
                Session["USERSELECTIONMODE"] = "T";
            }

            return Json(res, JsonRequestBehavior.AllowGet);
        }
        public string ExterLoginValidate(string Email, string ExternalLoginProvider, string ExternalAccessToken, string ExternalUserId, string Name)
        {
            //if (ModelState.IsValid)
            //{

            LoginModel lModel = new LoginModel();
            Response LR = new Response();
            lModel.ExternalUserID = ExternalUserId;
            lModel.LoginProvider = ExternalLoginProvider;
            lModel.LoginProviderAccessToken = ExternalAccessToken;
            lModel.Email = Email;
            lModel.UserName = Name;
            lModel.Name = Name;
            LR = ARS.ExternalLogin(lModel);
            if (LR.status == "success")
            {
                Session["UserId"] = LR.data.FirstOrDefault().UserId;
                Session["UserName"] = LR.data.FirstOrDefault().UserName;
                Session["x-api-authtoken"] = LR.data.FirstOrDefault().SessionId;
                return LR.status;
            }
            else
            {
                return LR.statusMessage;
            }
            //}
            //else
            //{
            //    return "Login Failed";
            //}

        }

        #endregion

        #region Facebook Login
        [AllowAnonymous]
        public ActionResult Facebook()
        {
            var fb = new FacebookClient();
            var loginUrl = fb.GetLoginUrl(new
            {
                //client_id = "759080021158709",
                //client_secret = "618683a03dac6687d4d36c1e25dede23",
                client_id = "347301449756008",
                client_secret = "3f2713cfe0121b95b514ce4f0b44a1bc",
                //redirect_uri = RediredtUri.AbsoluteUri,
                redirect_uri = "https://www.fanzania.com/Account/FacebookCallback",
                response_type = "code",
                scope = "email"

            });
            return Redirect(loginUrl.AbsoluteUri);
        }

        [AllowAnonymous]
        public ActionResult FacebookCallback(string code)
        {
            try
            {
                //code = "AQCDgyrVkKQfSDSzpNj-DQpvRRMQb556of3dyIIFvUxQ-T4yNb_-d00ubU2NCsvBL13kG3KPwzPrHVN12ps-pFMuXPvEqX8Kgss9VU0NpyULm92UbqkZNu4-EKKzbx0ZBJnXCDoRyn5BJAQWBgBNSwAyNoAamMl122Ld3Hmd-Vu0OaSX6Gy9v-JR7llBEyn1Nk-a2rdtc2d25-XYGlKkxWfFwLbIrAkC4Ws1_mmt1di79ZCU1owozkYyyU6InWvrYVYGfMQS-rF_-yt5LnEVGl7Gkti-DjWCO4tlmRNzzT72WfRk0NrShrSgKq6jIBIXnFgrqaconHCzi3uZRvZNg6AX";
                //ViewBag.Code = code;
                //ViewBag.Emsg = RediredtUri.AbsoluteUri;
                var fb = new FacebookClient();
                dynamic result = fb.Post("oauth/access_token", new
                {
                    client_id = "347301449756008",
                    client_secret = "3f2713cfe0121b95b514ce4f0b44a1bc",

                    //client_id = "759080021158709",
                    //client_secret = "618683a03dac6687d4d36c1e25dede23",

                    redirect_uri = "https://www.fanzania.com/Account/FacebookCallback",
                    code = code

                });
                var accessToken = result.access_token;
                ViewBag.accessToken = accessToken;
                //Session["AccessToken"] = accessToken;
                fb.AccessToken = accessToken;
                dynamic me = fb.Get("me?fields=link,first_name,currency,last_name,email,gender,locale,timezone,verified,picture,age_range,id");
                string email = me.email;
                string id = me.id;
                string msg = ExterLoginValidate(me.email, "Facebook", me.id, me.id, me.first_name + " " + me.last_name);

                if (msg == "success")
                {
                    FormsAuthentication.SetAuthCookie(email, false);
                    Session["USERSELECTIONMODE"] = "T";
                    return RedirectToAction("Index", "Dashboard");
                }
                else
                {
                    //TempData["msg"] = "<script>alert('" + msg + "');</script>";
                    TempData["msg"] = "<script>toastr_warning('" + msg + "');</script>";
                    return RedirectToAction("Index", "Account");
                }
            }
            catch (Exception ee)
            {
                ViewBag.ErrorMsg = ee.Message;
                return View("Error");
            }

        }

        #endregion

        // #region External Login
        // public AccountController()
        // {
        // }

        // public AccountController(ApplicationUserManager userManager, ApplicationSignInManager signInManager )
        // {
        //     UserManager = userManager;
        //     SignInManager = signInManager;
        // }

        // public ApplicationSignInManager SignInManager
        // {
        //     get
        //     {
        //         return _signInManager ?? HttpContext.GetOwinContext().Get<ApplicationSignInManager>();
        //     }
        //     private set 
        //     { 
        //         _signInManager = value; 
        //     }
        // }

        // public ApplicationUserManager UserManager
        // {
        //     get
        //     {
        //         return _userManager ?? HttpContext.GetOwinContext().GetUserManager<ApplicationUserManager>();
        //     }
        //     private set
        //     {
        //         _userManager = value;
        //     }
        // }

        // //

        // // POST: /Account/ExternalLogin
        // [HttpPost]
        // [AllowAnonymous]
        // [ValidateAntiForgeryToken]
        // public ActionResult ExternalLogin(string provider, string returnUrl)
        // {
        //     //returnUrl = "https://www.fanzania.com/signin-google";
        //     // Request a redirect to the external login provider
        //     return new ChallengeResult(provider, Url.Action("ExternalLoginCallback", "Account", new { ReturnUrl = returnUrl }));
        //     //return new ChallengeResult(provider, "ExternalLoginCallback");
        // }

        // //
        // // GET: /Account/SendCode
        // [AllowAnonymous]
        // public async Task<ActionResult> SendCode(string returnUrl, bool rememberMe)
        // {
        //     var userId = await SignInManager.GetVerifiedUserIdAsync();
        //     if (userId == null)
        //     {
        //         return View("Error");
        //     }
        //     var userFactors = await UserManager.GetValidTwoFactorProvidersAsync(userId);
        //     var factorOptions = userFactors.Select(purpose => new SelectListItem { Text = purpose, Value = purpose }).ToList();
        //     return View(new SendCodeViewModel { Providers = factorOptions, ReturnUrl = returnUrl, RememberMe = rememberMe });
        // }

        // //
        // // POST: /Account/SendCode
        // [HttpPost]
        // [AllowAnonymous]
        // [ValidateAntiForgeryToken]
        // public async Task<ActionResult> SendCode(SendCodeViewModel model)
        // {
        //     if (!ModelState.IsValid)
        //     {
        //         return View();
        //     }

        //     // Generate the token and send it
        //     if (!await SignInManager.SendTwoFactorCodeAsync(model.SelectedProvider))
        //     {
        //         return View("Error");
        //     }
        //     return RedirectToAction("VerifyCode", new { Provider = model.SelectedProvider, ReturnUrl = model.ReturnUrl, RememberMe = model.RememberMe });
        // }

        // //
        // // GET: /Account/ExternalLoginCallback
        //// [HttpGet]
        //// [HttpPost]
        // [AllowAnonymous]
        // //[ActionName("ExternalLoginCallback")]
        // public async Task<ActionResult> ExternalLoginCallback(string returnUrl)
        // {
        //     try
        //     {
        //         //return RedirectToAction("About", "Home");
        //         var loginInfo = await AuthenticationManager.GetExternalLoginInfoAsync();
        //         if (loginInfo == null)
        //         {
        //             return RedirectToAction("Index");
        //         }
        //         //ViewBag.Code = loginInfo.Email.ToString();
        //         //ViewBag.accessToken = loginInfo.ExternalIdentity.ToString();
        //         //ViewBag.Emsg = loginInfo.Login.ToString();
        //         // Sign in the user with this external login provider if the user already has a login
        //         var result = await SignInManager.ExternalSignInAsync(loginInfo, isPersistent: false);

        //         switch (result)
        //         {
        //             case SignInStatus.Success:
        //                 var loginProvider = loginInfo.Login.LoginProvider;
        //                 var providerKey = loginInfo.Login.ProviderKey;
        //                 var userName = loginInfo.ExternalIdentity.Name;
        //                 var emailId = loginInfo.Email;
        //                 ViewBag.accessToken = providerKey;
        //                 var res = ExterLoginValidate(emailId, loginProvider, providerKey, providerKey, userName);
        //                 if (res == "success")
        //                 {
        //                     Session["USERSELECTIONMODE"] = "T";
        //                     return RedirectToAction("Index", "Dashboard");
        //                 }
        //                 else
        //                 {
        //                     TempData["msg"] = "<script>toastr_warning('" + res + "');</script>";
        //                     return RedirectToAction("Index", "Account");
        //                 }
        //             /// return View("Error");
        //             //return RedirectToLocal(returnUrl);
        //             case SignInStatus.LockedOut:
        //                 return View("Lockout");
        //             case SignInStatus.RequiresVerification:
        //                 return RedirectToAction("SendCode", new { ReturnUrl = returnUrl, RememberMe = false });
        //             case SignInStatus.Failure:
        //             default:
        //                 // If the user does not have an account, then prompt the user to create an account
        //                 ViewBag.ReturnUrl = returnUrl;
        //                 ViewBag.LoginProvider = loginInfo.Login.LoginProvider;
        //                 return View("ExternalLoginConfirmation", new ExternalLoginConfirmationViewModel { Email = loginInfo.Email });
        //         }
        //     }
        //     catch(Exception ee)
        //     {
        //         ViewBag.ErrorMsg = ee.Message ;
        //         return View("Error");
        //     }
        // }



        // //
        // // POST: /Account/ExternalLoginConfirmation
        // [HttpPost]
        // [AllowAnonymous]
        // [ValidateAntiForgeryToken]
        // public async Task<ActionResult> ExternalLoginConfirmation(ExternalLoginConfirmationViewModel model, string returnUrl)
        // {
        //     if (User.Identity.IsAuthenticated)
        //     {
        //         return RedirectToAction("Index", "Manage");
        //     }

        //     if (ModelState.IsValid)
        //     {
        //         // Get the information about the user from the external login provider
        //         var info = await AuthenticationManager.GetExternalLoginInfoAsync();
        //         if (info == null)
        //         {
        //             return View("ExternalLoginFailure");
        //         }
        //         var user = new ApplicationUser { UserName = model.Email, Email = model.Email };
        //         var result = await UserManager.CreateAsync(user);
        //         if (result.Succeeded)
        //         {
        //             result = await UserManager.AddLoginAsync(user.Id, info.Login);
        //             if (result.Succeeded)
        //             {
        //                 await SignInManager.SignInAsync(user, isPersistent: false, rememberBrowser: false);
        //                 return RedirectToLocal(returnUrl);
        //             }
        //         }
        //         AddErrors(result);
        //     }

        //     ViewBag.ReturnUrl = returnUrl;
        //     return View(model);
        // }

        // //

        // // GET: /Account/ExternalLoginFailure
        // [AllowAnonymous]
        // public ActionResult ExternalLoginFailure()
        // {
        //     return View();
        // }

        // // GET: /Account/VerifyCode
        // [AllowAnonymous]
        // public async Task<ActionResult> VerifyCode(string provider, string returnUrl, bool rememberMe)
        // {
        //     // Require that the user has already logged in via username/password or external login
        //     if (!await SignInManager.HasBeenVerifiedAsync())
        //     {
        //         return View("Error");
        //     }
        //     return View(new VerifyCodeViewModel { Provider = provider, ReturnUrl = returnUrl, RememberMe = rememberMe });
        // }

        // //
        // // POST: /Account/VerifyCode
        // [HttpPost]
        // [AllowAnonymous]
        // [ValidateAntiForgeryToken]
        // public async Task<ActionResult> VerifyCode(VerifyCodeViewModel model)
        // {
        //     if (!ModelState.IsValid)
        //     {
        //         return View(model);
        //     }

        //     // The following code protects for brute force attacks against the two factor codes. 
        //     // If a user enters incorrect codes for a specified amount of time then the user account 
        //     // will be locked out for a specified amount of time. 
        //     // You can configure the account lockout settings in IdentityConfig
        //     var result = await SignInManager.TwoFactorSignInAsync(model.Provider, model.Code, isPersistent: model.RememberMe, rememberBrowser: model.RememberBrowser);
        //     switch (result)
        //     {
        //         case SignInStatus.Success:
        //             return RedirectToLocal(model.ReturnUrl);
        //         case SignInStatus.LockedOut:
        //             return View("Lockout");
        //         case SignInStatus.Failure:
        //         default:
        //             ModelState.AddModelError("", "Invalid code.");
        //             return View(model);
        //     }
        // }

        // //

        // protected override void Dispose(bool disposing)
        // {
        //     if (disposing)
        //     {
        //         if (_userManager != null)
        //         {
        //             _userManager.Dispose();
        //             _userManager = null;
        //         }

        //         if (_signInManager != null)
        //         {
        //             _signInManager.Dispose();
        //             _signInManager = null;
        //         }
        //     }

        //     base.Dispose(disposing);
        // }

        // // Facebook Login

        // private Uri RediredtUri
        // {
        //     get
        //     {
        //         var uriBuilder = new UriBuilder(Request.Url);
        //         uriBuilder.Query = null;
        //         uriBuilder.Fragment = null;
        //         uriBuilder.Path = Url.Action("FacebookCallback");
        //         return uriBuilder.Uri;
        //     }
        // }



        // #region Helpers
        // // Used for XSRF protection when adding external logins
        // private const string XsrfKey = "XsrfId";

        // private IAuthenticationManager AuthenticationManager
        // {
        //     get
        //     {
        //         return HttpContext.GetOwinContext().Authentication;
        //     }
        // }

        // private void AddErrors(IdentityResult result)
        // {
        //     foreach (var error in result.Errors)
        //     {
        //         ModelState.AddModelError("", error);
        //     }
        // }

        // private ActionResult RedirectToLocal(string returnUrl)
        // {
        //     if (Url.IsLocalUrl(returnUrl))
        //     {
        //         return Redirect(returnUrl);
        //     }
        //     return RedirectToAction("Index", "Home");
        // }

        // internal class ChallengeResult : HttpUnauthorizedResult
        // {
        //     public ChallengeResult(string provider, string redirectUri)
        //         : this(provider, redirectUri, null)
        //     {
        //     }

        //     public ChallengeResult(string provider, string redirectUri, string userId)
        //     {
        //         LoginProvider = provider;
        //         RedirectUri = redirectUri;
        //         UserId = userId;
        //     }

        //     public string LoginProvider { get; set; }
        //     public string RedirectUri { get; set; }
        //     public string UserId { get; set; }

        //     public override void ExecuteResult(ControllerContext context)
        //     {
        //         var properties = new AuthenticationProperties { RedirectUri = RedirectUri };
        //         if (UserId != null)
        //         {
        //             properties.Dictionary[XsrfKey] = UserId;
        //         }
        //         context.HttpContext.GetOwinContext().Authentication.Challenge(properties, LoginProvider);
        //     }
        // }
        // #endregion
        // #endregion
    }
}