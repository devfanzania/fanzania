using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;
using System.Web;
using Newtonsoft.Json;

namespace Fantacy_Model
{
    public class ExternalLoginListViewModel
    {
        public string ReturnUrl { get; set; }
    }
    public class LoginModel
    {       
        public string UserId { get; set; }
        
        [Required(ErrorMessage ="Please enter user name!")]

        public string Email { get; set; }

        
        public string UserName { get; set; }
        public string Name { get; set; }

        [Required(ErrorMessage = "Please enter password!")]
        public string Password { get; set; }
        public string Status { get; set; }
        public string ReturnUrl { get; set; }

        public string LoginProviderAccessToken { get; set; }
        public string LoginProvider { get; set; }
        public string ExternalUserID { get; set; }
        public string Lat { get; set; }
        public string Long { get; set; }


    }

    public class RegistrationModel
    {
       
        //[Required(ErrorMessage = "Please enter User  Name!")]
        public string UserName { get; set; }
        [Required(ErrorMessage = "Please Enter Name.")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Please Enter Password.")]
        public string Password { get; set; }

        //[Required(ErrorMessage = "Please Enter Confirm Password.")]
        public string ConfirmPassword { get; set; }

        public string ReferralCodeUsed { get; set; }

        [Required(ErrorMessage = "Please Enter Email.")]
        [EmailAddress(ErrorMessage = "Invalid Email Address.")]
        public string Email { get; set; }

       
        private int CaptchaResult { get; set; }

        [Required(ErrorMessage = "Please Enter Captcha.")]
        [RegularExpression("([1-9][0-9]*)", ErrorMessage = "Numbers Only.")]
        public string CaptchaCodeUsed { get; set; }
        public string CaptchaCodeSentParam1 { get; set; }
        public string CaptchaCodeSentOperator { get; set; }
        public string CaptchaCodeSentParam2 { get; set; }
        public string CaptchaCodeError { get; set; }
        public string Lat { get; set; }
        public string Long { get; set; }

    }

    public class EmailVerify
    {
        public string UserId { get; set; }

        [Required(ErrorMessage = "Please Enter Verification Code!")]
     
        public string VerificationCode { get; set; }
    }

    public class DefaultResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
       
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        
    }
    public class Response
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<DataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string Registered { get; set; }
    }


    public class CountryResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<CourntryList> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
      
    }
    public class ListOfTeamResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<ListOfTeamm> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class CourntryList
    {
        public int CountryId { get; set; }
        public string Country { get; set; }
        public string Active { get; set; }
    }
    public class ListOfTeamm
    {
        public int TournamentId { get; set; }
        public int ParticipationTeamId { get; set; }
        public string ParticipationTeamName { get; set; }
        public string TeamShortName { get; set; }
        public string TeamImage { get; set; }
    }

    public class DataResponse
    {
        public int UserId { get; set; }
        public string UserName { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string CountryId { get; set; }
        public string DOB { get; set; }
        public string PhoneNumber { get; set; }
        public string ActivationToken { get; set; }
        public string Active { get; set; }
        public string UserRoleId { get; set; }
        public string SignUpDate { get; set; }
        public string LastSignIn { get; set; }
        public string LastPasswordFailureDate { get; set; }
        public string PasswordFailuresSinceLastSuccess { get; set; }
        public string PasswordChangedDate { get; set; }
        public string SessionId { get; set; }
        public string LoginLocation { get; set; }
       
        
        public string SessionCreationDate { get; set; }
        public string SessionActive { get; set; }
        public string BackgroundTheme { get; set; }
        
        public string WalletPoint { get; set; }

        public string DeviceMacAddress { get; set; }
        public string LoginPreference { get; set; }
    }

    public class EmailResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<EmailDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class EmailDataResponse
    {
        public string UserId { get; set; }
        public string ActivationToken { get; set; }
        
    }

    public class VerifiedResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<VerifiedDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class VerifiedDataResponse
    {
        public string UserId { get; set; }
        public string Active { get; set; }

    }
    public class ProfileResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<ProfileModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class ProfileModel
    {
        public string UserId { get; set; }

        [Required(ErrorMessage = "Please Enter Name!")]
        public string Name { get; set; }

        public string Email { get; set; }

        //[Required(ErrorMessage = "Please Enter DOB!")]
        public string DOB { get; set; }

        //[Required(ErrorMessage = "Please Enter Mobile No!")]
        [MaxLength(12)]
        [RegularExpression("^[0-9]{1,12}$", ErrorMessage = "Mobile must be numeric")]
        public string PhoneNumber { get; set; }

        //[Required(ErrorMessage = "Please Select Country!")]
        public string CountryId { get; set; }
        public string ParticipationTeamId { get; set; }
        public string Country { get; set; }

        public List<CourntryList> Countrylist { get; set; }
        public List<ListOfTeamm> ListOfTeam { get; set; }
        public string UserRoleId { get; set; }
        public string SessionId { get; set; }
        public string SessionActive { get; set; }
        public string ProfileImage { get; set; }
        public bool CommPreference  { get; set; }
        public string BackgroundTheme { get; set; }

        [DataType(DataType.Upload)]
        public HttpPostedFileBase UploadedImage { get; set; }
        [DataType(DataType.Upload)]
        public HttpPostedFileBase KycUploadedImage { get; set; }
        public string ReferralCode { get; set; }
        public string ReferralCount { get; set; }

    }
    public class KycUpload {
        public string UserId { get; set; }
      
        [DataType(DataType.Upload)]
        public HttpPostedFileBase KYCDocImage { get; set; }
        [Required(ErrorMessage = "Please Enter PANName!")]
        public string PANName { get; set; }

      
        [DataType(DataType.Upload)]
        public HttpPostedFileBase UploadedImage { get; set; }

        [Display(Name = "PanNumber")]
        [Required]
        [RegularExpression(@"[A-Z]{5}\d{4}[A-Z]{1}", ErrorMessage = "* Invalid PAN Number")]
        // [StringLength(10, MinimumLength = 10)]
        public string PANNumber { get; set; }

       
        [Required(ErrorMessage = "Please Enter DateOfBirth!")]
        public string PANDOB { get; set; }
        [Required(ErrorMessage = "Please Enter State!")]
        public string PANState { get; set; }
        public string KYCStatus { get; set; }
        public string MobileVerified { get; set; }

    }
    public class KycUploadResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<KycUploadModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class KycUploadModel
    {
        public string UserId { get; set; }
        [DataType(DataType.Upload)]
        [Required]
        public HttpPostedFileBase UploadedImage { get; set; }
        //  [DataType(DataType.Upload)]
        //public HttpPostedFileBase KYCDocImage { get; set; }
        [Required(ErrorMessage = "Please Enter PANName!")]
        public string PANName { get; set; }

        [Display(Name = "PanNumber")]
        [Required]
        [RegularExpression(@"[A-Z]{5}\d{4}[A-Z]{1}", ErrorMessage = "* Invalid PAN Number")]
        //  [StringLength(10, MinimumLength = 10)]
        public string PANNumber { get; set; }


        [Required(ErrorMessage = "Please Enter DateOfBirth!")]
        public string PANDOB { get; set; }
        [Required(ErrorMessage = "Please Enter State!")]
        public string PANState { get; set; }
        public string KYCStatus { get; set; }
        public string KYCDocName { get; set; }
        public string UserProfileId { get; set; }
        public string KYCDocImage { get; set; }
          public string MobileVerified { get; set; }
        
    }

    public class updatekycstatus
    {
        public string UserId { get; set; }
        public string KYCStatus { get; set; }
    }

    public class updatebankdetails
    {
        public string UserId { get; set; }
        public string BankName { get; set; }
        public string IFSC { get; set; }
        public string AccountNumber { get; set; }

    }
    public class transferfundsdetails
    {
        public string UserId { get; set; }
        public string amount { get; set; }
      

    }
    public class updatebankdetailsresponce 
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
       // public bank data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class featchbankdetailsresponce
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public bank data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class bank
    {
        public string UserId { get; set; }
        public string BankName { get; set; }
        public string IFSC { get; set; }
        public string AccountNumber { get; set; }
        public string BankVerified { get; set; }
        public string amount { get; set; }

    }

    public class verifyotpmodel
    {
        public string UserId { get; set; }
        public string ActivationToken { get; set; }
    }

    public class ProfileUploadResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<ProfileUploadModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class ProfileUploadModel
    {
        public string UserId { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string DOB { get; set; }
        public string PhoneNumber { get; set; }

        public string CountryId { get; set; }
        public string Country { get; set; }
        public string UserRoleId { get; set; }
        public string SessionId { get; set; }
        public string SessionActive { get; set; }
        public string ProfileImage { get; set; }
        public string BackgroundTheme { get; set; }
        public bool CommPreference { get; set; }
        public string KycProfileImage { get; set; }
    }

    public class ChangePassResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<ChangePassDataModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class ChangePassDataModel
    {
        public string UserId { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public string PasswordChangedDate { get; set; }
        public string authtoken { get; set; }
       
    }
    public class SaveLoginPreferenceData
    {

        public string UserId { get; set; }
        public string LoginPreference { get; set; }
    }
    public class SubscriptionDetails
    {
        public string UserId { get; set; }
        public string Email { get; set; }

    }
    public class UpdateSub
    {
        public string UserId { get; set; }
        public string SubscriptionTier { get; set; }
        public string receipt { get; set; }
        public string amount { get; set; }
        public string Currency { get; set; }

    }
    public class LeagueSub
    {
        public string UserId { get; set; }
        public string SubscriptionType { get; set; }
        public string receipt { get; set; }
        public string amount { get; set; }
        public string DiscountTotal { get; set; }
        public string LoggedInUserId { get; set; }
        public string LeagueId { get; set; }
        public string Currency { get; set; }

    }
    public class SubResponce
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
      //  public data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class DefaultResponseLoginP
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LoginPDataModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }

    public class DefaultResponseSubscriptionDetails
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
        public Subscriptiondata data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }
    public class Subscriptiondata
    {
        public string Details { get; set; }
        public string SubTierFree { get; set; }
        public string SubTier1 { get; set; }
        public string SubTier2 { get; set; }
        public string SubTier3 { get; set; }
        public string CurrentSubscriptionTier { get; set; }
        public string SubTier1PayEnable { get; set; }
        public string SubTier2PayEnable { get; set; }
        public string SubTier3PayEnable { get; set; }
        public string client_id { get; set; }
        public string client_secret { get; set; }

        public string LivePackageAmount_USD { get; set; }
        public string PrizePackageAmount_USD { get; set; }
        public string FullPackageAmount_USD { get; set; }

        public string LivePackageAmount_GBP { get; set; }
        public string PrizePackageAmount_GBP { get; set; }
        public string FullPackageAmount_GBP { get; set; }


        public string LivePackageAmount { get; set; }
        public string PrizePackageAmount { get; set; }
        public string FullPackageAmount { get; set; }
      
    }
    public class LoginPDataModel
    {
        public string LoginPreference { get; set; }
    }
    public class PaymentGetwayDetails
    {
        public string PaymentGatewayName { get; set; }
       

    }
    public class DefaultResponseGetWayDetails
    {

        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<subgetwaydata> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
    }


    public class subgetwaydata
    {
        public string Environment { get; set; }
        public string PaymentGatewayName { get; set; }

        public string PGAuthKey { get; set; }

        public string URL { get; set; }
        public string PGClientId { get; set; }
        public string PGClientSecret { get; set; }
    }


    }
