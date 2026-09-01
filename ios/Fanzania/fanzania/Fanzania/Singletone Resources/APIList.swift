//
//  APIList.swift
//  Fanzania
//
//  Created by Tathagata Dey on 28/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation

let live_domain = "https://www.fanzania.com/"
let dev_domain = "http://devfanzania.com/"
let selectedDomain = dev_domain
let APIService = selectedDomain + "FantasyCricketRest/api/fantasycricket/"

let URL_Login = APIService + "sign-in"
var URL_SignUp = APIService + "sign-up"
let URL_ForgotPassword = APIService + "forget-password"
let URL_Country_List = APIService + "country-list"
let URL_User_Varification = APIService + "verify-user"
let URL_User_GetActivationCodeforResend = "email-verification-code"
let URL_EmailVarifiedOnSignUp = APIService + "email-verified"
let URL_ExternalLoginCheck = APIService + "external-sign-in"
let URL_Logout = APIService + "log-out"
let URL_User_TournamentDetails = APIService + "user-tournament-details"
let URL_UpcomingTournamentsForUser = APIService + "user-upcoming-tournament"
let URL_Leagues_ByTournament = APIService + "user-all-leagues"
let URL_Leagues_AllTeams = APIService + "league-teams"
let URL_Leagues_CreateLeague = APIService + "create-league"
let URL_Leagues_JoinLeague = APIService + "join-league"
let URL_Leagues_VarifyLeague = APIService + "verify-league"
let URL_Leagues_ApproveUser = APIService + "approve-league-users"
let URL_Leagues_UnapproveUser = APIService + "unapprove-league-users"
let URL_Leagues_ExitLeague = APIService + "exit-league"
let URL_League_ResetLeaguePin = APIService + "reset-league-pin"
let URL_League_TopPerformer = APIService + "league-stats-top-teams-top-perform"
let URL_League_TopFav = APIService + "league-stats-top-teams-top-favorite"
let URL_League_User_Team_StelthMode = APIService + "user-team-players-details-with-stealthmode"
let URL_League_User_Team_Powerplay = APIService + "fetch-user-powerplay"
let URL_League_Update_User_Team_Powerplay = APIService + "update-user-powerplay"

let URL_TournamentTopPlayers = APIService + "user-stats-user-top-players"
let URL_TournamentTopTeams = APIService + "user-stats-global-top-teams"
let URL_GlobalTopPlayers = APIService + "user-stats-global-top-players"
let URL_TournamentTopLeagues = APIService + "league-stats-global-top-leagues"

let URL_TOURNAMENT_ALLPLAYERS = APIService + "get-tournament-players"
let URL_USER_Profile = APIService + "fetch-profile"
let URL_USER_ChangePassword = APIService + "save-new-password"
let URL_USER_Team = APIService + "user-team"
let URL_USER_ProfileSave = APIService + "save-profile"
let URL_TOURNAMENT_AllMatches = APIService + "all-matches"
let URL_Future_Matches = APIService + "all-future-matches"
let URL_Match_PostMatchDetails = APIService + "user-team-match-details-with-players"
let URL_CurrentTeamWithPowerPlay = APIService + "user-team-players-details-with-powerplay"
let URL_DistinctTeam = APIService + "distinct-team-filter"
let URL_PlayerProfile = APIService + "fetch-player-stats"

let URL_Team_VarifyName = APIService + "verify-team-name"
let URL_Team_CreateTeam = APIService + "create-user-team"
let URL_Team_TeamSelectionRule = APIService + "team-selection-rule"
let URL_Team_ModifyTeamName = APIService + "modify-user-team"
let URL_Team_SaveTeamSelection = APIService + "save-team-selection"
let URL_Team_PlayerDetails = APIService + "player-details"
let URL_Team_AutoFill = APIService + "auto-select-team"
let URL_Team_CutOffLastTeam = APIService + "user-team-players-last-cutoff"
let URL_Team_LastSaved = APIService + "user-team-players-with-details"
let URL_Team_StatsPageTop10 = APIService + "user-stats-user-top-players"
let URL_Team_StatsCaptaincyList = APIService + "user-stats-captain-points"
let URL_Team_FilterDistinct = APIService + "distinct-team-filter"
let URL_Team_StelthMode = APIService + "user-team-players-details-with-stealthmode"

let URL_Live_Tournaments = APIService + "live-tournament-details"
let URL_Live_Matches = APIService + "live-matches"
let URL_Live_MatcheScore = APIService + "live-match-score"
let URL_Live_UserTeamScore = APIService + "live-user-team-score"
let URL_Live_LeagueUserPostion = APIService + "live-league-users"
let URL_Live_TeamComparison = APIService + "team-points-comparison"
let URL_Live_TeamLiveComparison = APIService + "live-team-score-comparison"

//file location
let URLServerImageLocation = selectedDomain + "FantasyCricketRest/ImagePath/"
let URL_SERVER_IMAGE_LOCATION_PlayerImage = URLServerImageLocation + "image/cricket/teams/"
let URLServerProfilePicturePath = URLServerImageLocation + "image/users/"
let URL_ImageUpload_Profile = APIService + "upload-profile-image"

//Static Pages
let staticPagesURL = selectedDomain + "Home/"
let URLTermsCondition = staticPagesURL + "TermsCondition"
let URLFAQ = staticPagesURL + "FAQ_Online"
let URLAboutUs = staticPagesURL + "About_Online"
let URLHowToPlay = staticPagesURL + "HowToPlay_Online"
let URLPrivacyNotice = staticPagesURL  + "PrivacyPolicy"
let URLTeamCompositionRules = staticPagesURL + "TeamComposition_Online"
let URLPointRules = staticPagesURL + "PointScoring_Online"
let URLTC = staticPagesURL + "TermsCondition"
let URLContactUS = staticPagesURL + "Contact"
let rateUS = URLAppStorePage
let URLAppStorePage = "itms-apps://itunes.apple.com/app/\(StringConstants.appID)" // (Option 1) Open App Page
//let URLAppStoreReviewPage = "itms-apps://itunes.apple.com/app/viewContentsUserReviews?id=\(appID)" // (Option 2) Open App Review Tab
let fanzaniaAppStoreURL = "https://apps.apple.com/us/app/fanzania/id\(StringConstants.appID)?ls=1"
let fanzaniaWebsite = "https://www.fanzania.com"

// Notifications
let URL_NotificationCount = APIService + "fetch-notification-count"
let URL_FetchNotifications = APIService + "fetch-notification-message"
let URL_AckNotification = APIService + "ack-notification-message"
