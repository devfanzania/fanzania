package com.yorker.fanzania.constants;

public class Constants {

//    public static final String BASE_URL = "http://www.devfanzania.com/FantasyCricketRest/api/fantasycricket/";
//    public static final String BASE_URL_RAZOR_PAY = "https://api.razorpay.com/v1/";
    public static final String BASE_URL = "https://www.fanzania.com/FantasyCricketRest/api/fantasycricket/";
    public static final String BASE_IMAGE_URL = "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/";
//    public static final String BASE_IMAGE_URL = "http://www.devfanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/";

//    public static final String BASE_URL = "http://www.devfanzania.com/FantasyCricketRest/api/fantasycricket/";
//    public static final String BASE_IMAGE_URL = "http://www.devfanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/";

//    public static final String BASE_URL = "http://devfanzania.com/FantasyCricketRest/api/fantasycricket/";
//    public static final String BASE_IMAGE_URL = "http://devfanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/";


//    public static final String BASE_PROFILE_IMAGE_URL = "http://www.devfanzania.com/FantasyCricketRest/ImagePath/image/users/";
    public static final String BASE_PROFILE_IMAGE_URL = "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/users/";
    public static final String str_HEADER = "application/json";
    public static final String TAG_PROFILE_IMAGE = "ProfileImage";

    public static enum PAN_STATUS {
        pending, submitted , saved , approved, rejected
    }
    public static String client_id = null;
    public static Boolean isPaid = false;
    public static String client_secret = null;

    public static final String RETROFIT_HEADER = "Content-Type";
    public static  String PGClientId = "";
    public static  String PGClientSecret = "";
    public static final String RETROFIT_HEADER_DEVICETYPE = "x-api-devicetype";
    public static final String RETROFIT_HEADER1 = "x-api-authtoken";
    public static final String RETROFIT_HEADER2 = "x-api-userid";
    public static final String RETROFIT_HEADER_TOKEN1 = "999304CF-C526-40CA-AB32-D2FAEEC54F53";
    public static final String RETROFIT_HEADER_TYPE = "android";
    public static final String RETROFIT_HEADER_DOC_TYPE = "kyc-doc";

    public static final String STR_SUCCESS = "success";
    public static final String STR_DATA = "data";

    public static final String SOCIAL_MODE_FB = "facebook";
    public static final String SOCIAL_MODE_GOOGLE = "google";

    public static final String SHARED_PREF_NAME = "Device_token";
    public static final String SHARED_DEVICE_TOKEN = "Devicetoken";
    public static final String SHARED_PREF_LOGIN = "LogIn";
    public static final String SHARED_PREF_STATIC_URL = "url";
    public static final String SHARED_MODE = "mode";
    public static final String SHARED_TOKEN = "token";
    public static final String SHARED_TOURNAMENT = "TournamentId";
    public static final String SHARED_LEAGUE = "LeagueId";
    public static final String SHARED_TOOLTIP = "ToolTip";
    public static final String SHARED_PREF_DIALOG = "update_dialog";
    public static final String SHARED_PREF_TOOLTIP_DIALOG = "tooltip_dialog";
    public static final String SHARED_PREF_LTOOLTIP_DIALOG = "Ltooltip_dialog";
    public static final String SHARED_PREF_MCPTOOLTIP_DIALOG = "McPtooltip_dialog";
    public static final String SHARED_PREF_Header = "header_pref";

    public static final String TAG_FAQ = "FAQs";
    public static final String TAG_ABOUTUS = "AboutUs";
    public static final String TAG_HOWTOPLAY = "HowtoPlay";
    public static final String TAG_PRIVACYNOTICE = "PrivacyNotice";
    public static final String TAG_POINTRULES = "PointRules";
    public static final String TAG_TNC = "TnC";
    public static final String TAG_CONTACTUS = "ContactUS";
    public static final String TAG_TEAMCOMPOSITIONRULE = "TeamCompositionRules";
    public static final String TAG_INTENTKEY = "tag";
    public static final String TAG_NOTIFICATIONID = "NotificationId";
    public static final int PICK_IMAGE = 1002;

    public static Boolean CAMERA = true;
    public static final int ACTION_TAKE_CAMERA = 2000;
    public static final int ACTION_TAKE_GALLERY = 1000;

    public static final String TAG_ID = "UserId";
    public static final String TAG_ACTIVATION_TOKEN = "ActivationToken";
    public static final String TAG_NAME = "Name";
    public static final String TAG_DOB = "DOB";
    public static final String TAG_PASS = "password";
    public static final String TAG_PHONENUMBER = "PhoneNumber";
    public static final String TAG_PREFERENCE = "CommPreference";
    public static final String TAG_EMAIL = "Email";
    public static final String TAG_USERDETAILS = "UserDetails";
    public static final String TAG_BACKGROUND_THEME = "BackgroundTheme";
    public static final String TAG_LOGIN_PREFERENCE = "LoginPreference";
    public static final String TAG_BANK_NAME = "BankName";
    public static final String TAG_IFSC = "IFSC";
    public static final String TAG_ACCOUNT_NUMBER = "AccountNumber";

    public static final String TAG_PAN_NAME = "PANName";
    public static final String TAG_PAN_NUMBER = "PANNumber";
    public static final String TAG_PAN_DOB = "PANDOB";
    public static final String TAG_PAN_STATE = "PANState";
    public static final String TAG_KYC_STATUS = "KYCStatus";
    public static final String TAG_SUBSCRIPTOIN_TIER = "SubscriptionTier";
    public static final String TAG_RECEIPT = "receipt";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_CURRENCY = "Currency";

    public static final String TAG_CONNECTIONID = "connectionID";
    public static final String TAG_SESSIONID = "SessionId";

    public static final String TAG_COUNTRYID = "CountryId";

    public static final String TAG_TOURNAMENTID = "TournamentId";
    public static final String TAG_TOURNAMENTNAME = "TournamentName";
    public static final String TAG_TOURNAMENTSTATUS = "TournamentStatus";
    public static final String TAG_TOURNAMENTDATE = "TournamentDate";

    public static final String TAG_USERTEAMNAME = "UserTeamName";

    public static final String TAG_USERTEAMID = "UserTeamId";
    public static final String TAG_MATCHTYPE = "MatchType";
    public static final String TAG_MATCHID = "MatchId";
    public static final String TAG_MATCHDATE = "MatchDate";
    public static final String TAG_HEADER = "Header";
    public static final String TAG_DATA = "data";
    public static final String TAG_INDEX = "index";
    public static final String TAG_PAGE = "page";

    public static final String TAG_LEAGUEID = "LeagueId";
    public static final String TAG_USERLEAGUEID = "UserLeagueId";
    public static final String TAG_LEAGUENAME = "LeagueName";
    public static final String TAG_LEAGUELEADERID = "LeagueLeaderId";
    public static final String TAG_LEAGUEPIN = "LeaguePin";

    public static final int TAG_HOMEINDICATOR = 1;
    public static final int TAG_TEAMINDICATOR = 3;
    public static final int TAG_LEAGUEINDICATOR = 2;
    public static final int TAG_LIVEINDICATOR = 4;
    public static final int TAG_SEEALLINDICATOR = 5;


    public static final String TAG_PLAYERLIST = "PlayerList";
    public static final String TAG_LASTCUTOFF = "LastCutOFF";

    public static final String TAG_SubsLeft = "SubsLeft";
    public static final String TAG_TotalBudget = "TotalBudget";
    public static final String TAG_SubsLeftAtSnapShot = "SubsLeftAtSnapShot";

    public static final String TAG_PLAYER = "Player";
    public static final String TAG_TEAMCAPT = "TeamCapt";
    public static final String TAG_TEAMCAPTVC = "TeamVCapt";
    public static final String TAG_NUMBEROFSUBS = "NumberOfSubs";
    public static final String TAG_NITROUSED = "NitroUsed";
    public static final String TAG_PAINKILLERUSED = "PainKillerUsed";
    public static final String TAG_AUTOPILOTUSED = "AutoPilotUsed";

    public static final String TAG_PLAYERTYPE_BATSMAN = "batsman";
    public static final String TAG_PLAYERTYPE_BLOWER = "bowler";
    public static final String TAG_PLAYERTYPE_ALLROUNDER = "allrounder";
    public static final String TAG_PLAYERTYPE_WICKETKEEPER = "wicketkeeper";
    public static final String TAG_PLAYERTYPE_ALL = "all";

    public static final String TAG_PLAYERID = "PlayerId";
    public static final String TAG_PARTICIPATION_TEAM_ID = "ParticipationTeamId";
    public static final String TAG_APIPId = "APIPId";

    public static final String TAG_COMPLETE = "COMPLETE";
    public static final String TAG_INPROGRESS = "INPROGRESS";
    public static final String TAG_UPCOMING = "UPCOMING";

    public static final String TAG_TEAMNAME = "TeamName";

    public static final String TAG_CLASS = "Registration";

    public static final String TAG_TOTALPOINT = "totalpoint";
    public static final String TAG_CUREENTPOINT = "currentpoint";
    public static final String TAG_POWERPLAY = "powerplay";

    public static final Integer REQ_CODE_PLAYER = 100;

    public static final String str_status = "status";

    public static final String APICALL_1 = "1";
    public static final String APICALL_2 = "2";
    public static final String APICALL_3 = "3";
    public static final String APICALL_4 = "4";
    public static final String APICALL_5 = "5";
    public static final String APICALL_6 = "6";

    public static final String VICECAPTAIN = "VC";
    public static final String CAPTAIN = "C";

}