using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model;
using FantacyWeb.Models;

namespace FantacyWeb.Controllers
{
    public class TeamController : Controller
    {
        private AccountRestService ARS = new AccountRestService();
        // GET: Team
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    TempData.Clear();
                    Session["USERSELECTION"] = "Team";
                    Session["USERSELECTIONMODE"] = "T";
                    ViewBag.Page = "Team";
                    ViewBag.UserName = Session["UserName"].ToString();
                  //  ViewBag.WinnerPrediction = Session["WinnerPrediction"].ToString();
                   
                    return View();
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
        [ActionName("TournamentList")]
        [AllowAnonymous]
        public ActionResult TournamentList()
        {
            try
            {
                UserTournamentResponse LR = new UserTournamentResponse();
                List<UserTournamentDataResponse> TDetail = new List<UserTournamentDataResponse>();
                List<UserTournamentDataResponse> SortedList = new List<UserTournamentDataResponse>();
                ParamModel lModel = new ParamModel();
                lModel.UserId = Session["UserId"].ToString();
                lModel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.GetUserTournament(lModel);
                if (LR.status == "success")
                {

                    TDetail = LR.data.ToList();
                    var Inprogresslist = TDetail.Where(x => x.TournamentStatus == "INPROGRESS" || x.TournamentStatus == "BREAK").ToList();
                    var Upcomminglist = TDetail.Where(x => x.TournamentStatus == "UPCOMING").ToList();
                    var Finishlist = TDetail.Where(x => x.TournamentStatus == "COMPLETE").ToList();
                    SortedList = Inprogresslist.Concat(Upcomminglist).ToList();
                    SortedList = SortedList.Concat(Finishlist).ToList();
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        //[HttpPost]
        //[ActionName("TeamInfo")]
        //[AllowAnonymous]
        public void TeamInfo(ParamModel Pmodel)
        {
            try
            {
                TeamResponse LR = new TeamResponse();
                List<TeaMDataResponse> TDetail = new List<TeaMDataResponse>();

                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                LR = ARS.UserTeam(Pmodel);
                if (LR.status == "success")
                {
                    Session["TeamId"] = LR.data.FirstOrDefault().UserTeamId.ToString();
                    Session["TeamName"] = LR.data.FirstOrDefault().UserTeamName.ToString();
                    Session["SelectTournamentId"] = LR.data.FirstOrDefault().TournamentId.ToString();
                    Session["Subs"] = LR.data.FirstOrDefault().SubsLeft.ToString();
                    Session["SubsLeftAtSnapShot"] = LR.data.FirstOrDefault().SubsLeftAtSnapShot.ToString();
                    Session["NitroLeft"] = LR.data.FirstOrDefault().NitroLeft.ToString();
                    Session["AutoPilotLeft"] = LR.data.FirstOrDefault().AutoPilotLeft.ToString();
                    Session["PainKillerLeft"] = LR.data.FirstOrDefault().PainKillerLeft.ToString();
                    Session["NitroUsed"] = (LR.data.FirstOrDefault().NitroUsed == null) ? "False" : LR.data.FirstOrDefault().NitroUsed.ToString();
                    Session["AutoPilotUsed"] = (LR.data.FirstOrDefault().AutoPilotUsed == null) ? "False" : LR.data.FirstOrDefault().AutoPilotUsed.ToString();
                    Session["PainKillerUsed"] = (LR.data.FirstOrDefault().PainKillerUsed == null) ? "False" : LR.data.FirstOrDefault().PainKillerUsed.ToString();

                }
            }
            catch (Exception ee)
            {
            }
        }

        [HttpPost]
        [ActionName("TeamPlayerInfo")]
        [AllowAnonymous]
        public ActionResult TeamPlayerInfo(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Session["TournamentStatus"] = Pmodel.TournamentStatus;
                PR = ARS.TeamPlayerDetails(Pmodel);
                if (PR.status == "success")
                {
                    if (PR.data.Count > 0)
                    {
                        Session["TeamId"] = PR.data.FirstOrDefault().UserTeamId.ToString();
                        Session["TeamName"] = PR.data.FirstOrDefault().UserTeamName.ToString();
                        Session["SelectTournamentId"] = PR.data.FirstOrDefault().TournamentId.ToString();
                        Session["Subs"] = PR.data.FirstOrDefault().SubsLeft.ToString();
                        Session["SubsLeftAtSnapShot"] = PR.data.FirstOrDefault().SubsLeftAtSnapShot.ToString();
                        Session["NitroLeft"] = PR.data.FirstOrDefault().NitroLeft.ToString();
                        Session["AutoPilotLeft"] = PR.data.FirstOrDefault().AutoPilotLeft.ToString();
                        Session["PainKillerLeft"] = PR.data.FirstOrDefault().PainKillerLeft.ToString();
                        Session["NitroUsed"] = (PR.data.FirstOrDefault().NitroUsed == null) ? "False" : PR.data.FirstOrDefault().NitroUsed.ToString();
                        Session["AutoPilotUsed"] = (PR.data.FirstOrDefault().AutoPilotUsed == null) ? "False" : PR.data.FirstOrDefault().AutoPilotUsed.ToString();
                        Session["PainKillerUsed"] = (PR.data.FirstOrDefault().PainKillerUsed == null) ? "False" : PR.data.FirstOrDefault().PainKillerUsed.ToString();
                        Session["WinnerPrediction"] = PR.data.FirstOrDefault().WinnerPrediction.ToString();
                    }
                    else
                    {
                        TeamInfo(Pmodel);
                    }
                    PlayerList = PR.data.ToList();

                    TempData["SelectPlayerlist"] = PR.data.ToList();
                    TempData["LastSavedPlayerlist"] = PR.data.ToList();
                }
                return Json(PlayerList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }

        [HttpPost]
        [ActionName("TeamPlayerInfoCompleteMatch")]
        [AllowAnonymous]
        public ActionResult TeamPlayerInfoCompleteMatch(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();

                PR = ARS.TeamPlayerDetails_CompleteMatch(Pmodel);
                //PR = ARS.CurrentTeamPlayerDetails_(Pmodel);
                if (PR.status == "success")
                {
                    PlayerList = PR.data.ToList();

                    TempData["SelectPlayerlist"] = PR.data.ToList();
                }
                return Json(PlayerList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("RedirectToManageTeam")]
        [AllowAnonymous]
        public ActionResult RedirectToManageTeam(ParamModel Pmodel)
        {
            try
            {
                var qstr = "utid=" + Pmodel.UserTeamId + "&tid=" + Pmodel.TournamentId + "&tname=" + Pmodel.TournamentName + "&tstat=" + Pmodel.TournamentStatus;
                var passPhraseReg = "amaf7LLSWhN@#r5!*";
                var enc = ECDC.Encrypt(qstr.ToString(), passPhraseReg);
                var redirectUrl = "/Team/ManageTeam?enc=" + enc;
                return Json(new { Url = redirectUrl });
                
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        // Manage Team..................................

        [HttpGet]
        public ActionResult ManageTeam()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    ViewBag.Page = "Team";
                    ViewBag.UserName = Session["UserName"].ToString();

                    var tstat = "";

                    if (Request.QueryString["enc"] != null)
                    {
                        var passPhraseReg = "amaf7LLSWhN@#r5!*";
                        string enc = Request.QueryString["enc"].ToString();
                        var dcp = ECDC.Decrypt(enc.Replace(" ","+"), passPhraseReg);

                        ParamModel pm = new ParamModel();
                        pm.UserTeamId = dcp.Split('&')[0].Split('=')[1].ToString();
                        pm.TournamentId = dcp.Split('&')[1].Split('=')[1].ToString();
                        TeamPlayerInfo(pm);
                        ViewBag.TournamentName = dcp.Split('&')[2].Split('=')[1].ToString();
                        Session["TournamentStatus"] = dcp.Split('&')[3].Split('=')[1].ToString();
                        if (Session["WinnerPrediction"] == null) {
                            Session["WinnerPrediction"] = '-';
                        }
                        tstat = dcp.Split('&')[3].Split('=')[1].ToString();
                    }
                    //else
                    //{
                    //    tstat = Session["TournamentStatus"].ToString();
                    //}

                    //if (Request.QueryString["utid"] != null)
                    //{
                    //    ParamModel pm = new ParamModel();
                    //    pm.UserTeamId = Request.QueryString["utid"].ToString();
                    //    pm.TournamentId = Request.QueryString["tid"].ToString();
                    //    TeamPlayerInfo(pm);
                    //    ViewBag.TournamentName = Request.QueryString["tname"].ToString();
                    //    Session["TournamentStatus"] = Request.QueryString["tstat"].ToString();
                    //    tstat = Request.QueryString["tstat"].ToString();
                    //}


                    ViewBag.TeamName = Session["TeamName"].ToString();
                    Transfer tran = new Transfer();
                    if (tstat == "INPROGRESS")
                    {
                        tran.Transfer_left = Session["Subs"].ToString();
                        if (TempData["Trans_Left"] != null)
                        { 
                            var temp = TempData["Trans_Left"];
                        }
                    }
                    else
                    {
                        tran.Transfer_left = "<img src='/Assets/Icon/infinity.png' alt='' height='20px' width='20px' />";
                    }
                    List<Playerlist> Slist = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                    if (Slist == null)
                    {
                        Session["SelectPlayerCount"] = "0";
                        Session["BatsmanCount"] = "0";
                        Session["WicketkeeperCount"] = "0";
                        Session["BowlerCount"] = "0";
                        Session["AllrounderCount"] = "0";
                        return View(tran);
                    }
                    else
                    {
                        Session["SelectPlayerCount"] = Slist.Count;
                        Session["BatsmanCount"] = (Slist.Where(x => x.PlayerSpeciality == "batsman").ToList()).Count;
                        Session["WicketkeeperCount"] = (Slist.Where(x => x.PlayerSpeciality == "wicketkeeper").ToList()).Count;
                        Session["BowlerCount"] = (Slist.Where(x => x.PlayerSpeciality == "bowler").ToList()).Count;
                        Session["AllrounderCount"] = (Slist.Where(x => x.PlayerSpeciality == "allrounder").ToList()).Count;
                        ViewBag.Playerlist = Slist;

                        return View(tran);
                    }
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
        [ActionName("ShowMatchDetails")]
        [AllowAnonymous]
        public ActionResult ShowMatchDetails(ParamModel Pmodel)
        {
            try
            {
                MatchResponse LR = new MatchResponse();
                List<MatchDataResponse> TDetail = new List<MatchDataResponse>();
                List<MatchDataResponse> SortedList = new List<MatchDataResponse>();
                LR = ARS.MatchDetails(Pmodel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();

                    var CompleteList = TDetail.Where(x => x.MatchStatus == "COMPLETE").ToList();
                    var Runninglist = TDetail.Where(x => x.MatchStatus != "UPCOMING" && x.MatchStatus != "COMPLETE").ToList();
                    var Inprogresslist = TDetail.Where(x => x.MatchStatus == "UPCOMING").ToList();

                    SortedList = CompleteList.Concat(Runninglist).ToList();
                    SortedList = SortedList.Concat(Inprogresslist).ToList();
                }
                return Json(SortedList, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("GetFutureMatchs")]
        [AllowAnonymous]
        public ActionResult GetFutureMatchs(ParamModel Pmodel)
        {
            try
            {
                MatchResponse LR = new MatchResponse();
                List<MatchDataResponse> TDetail = new List<MatchDataResponse>();
                List<MatchDataResponse> SortedList = new List<MatchDataResponse>();
                LR = ARS.AllFutureMatches(Pmodel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();
                }
                return Json(TDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("ShowTeamFilter")]
        [AllowAnonymous]
        public ActionResult ShowTeamFilter(ParamModel Pmodel)
        {
            try
            {
                TeamFilterResponse LR = new TeamFilterResponse();
                List<TeamFilterDataResponse> Detail = new List<TeamFilterDataResponse>();
                Pmodel.TournamentId = Session["SelectTournamentId"].ToString();
                LR = ARS.FilterTeam(Pmodel);
                if (LR.status == "success")
                {
                    Detail = LR.data.ToList();
                }

                return Json(Detail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }


        [HttpPost]
        [ActionName("MovefromList")]
        [AllowAnonymous]
        public ActionResult MovefromList(ParamModel Pmodel)
        {
            try
            {
                List<Playerlist> Slist = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                Slist.RemoveAll(x => x.PlayerId == Pmodel.PlayerId);
                TempData["SelectPlayerlist"] = Slist;
                ViewBag.Playerlist = Slist;
                return Json(Slist, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }

        [HttpPost]
        [ActionName("PlayerSelectionRules")]
        [AllowAnonymous]
        public ActionResult PlayerSelectionRules(ParamModel Pmodel)
        {
            try
            {
                TeamRulesResponse LR = new TeamRulesResponse();
                List<TeamRulesDataResponse> TDetail = new List<TeamRulesDataResponse>();
                LR = ARS.PlayerSelectionRules(Pmodel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();
                }
                return Json(TDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }


        [AllowAnonymous]
        public ActionResult PlayerList()
        {
            return View();
        }

        [HttpGet]
        [ActionName("GetPlayerlist")]
        public ActionResult GetPlayerlist(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
               
                pModel.UserId = Session["UserId"].ToString();
                var records = GetPlayerList(page, limit, sortBy, direction, pModel, out total);

                TempData["Playerlist"] = records.ToList();
                if (pModel.PlayerSearch != null || pModel.PlayerSearch == "")
                {
                    records = (records.Where(c => c.PlayerShortName.ToLower().StartsWith(pModel.PlayerSearch.ToLower()) || c.PlayerShortName.ToLower().EndsWith(pModel.PlayerSearch.ToLower()) || c.PlayerName.ToLower().Contains(pModel.PlayerSearch.ToLower()))).ToList();
                }
                if (pModel.FilterTeams != null)
                {
                    List<Playerlist> filteredData = new List<Playerlist>();
                    var teamlst = pModel.FilterTeams.Split(',');
                    foreach (string i in teamlst)
                    {
                        if (!string.IsNullOrEmpty(i))
                        {
                            List<Playerlist> temlList = new List<Playerlist>();
                            temlList = records.Where(c => c.ParticipationTeamId.Contains(i)).ToList();

                            filteredData.AddRange(temlList);
                        }
                    }
                    if (direction == null || direction == "")
                    {
                        records = filteredData.OrderBy(s => s.PlayerName).ToList();
                    }
                    else
                    {
                        if (direction == "asc")
                        {
                            if (sortBy == "TotalPoints")
                            {
                                records = filteredData.OrderBy(s => s.TotalPoints).ToList();
                            }
                            else
                            {
                                records = filteredData.OrderBy(s => s.PlayerValue).ToList();
                            }
                        }
                        else
                        {
                            if (sortBy == "TotalPoints")
                            {
                                records = filteredData.OrderByDescending(s => s.TotalPoints).ToList();
                            }
                            else
                            {
                                records = filteredData.OrderByDescending(s => s.PlayerValue).ToList();
                            }
                        }
                    }
                }
                if (pModel.PlayerType == null || pModel.PlayerType == "All")
                {
                    records = (records.Where(c => c.PlayerSpeciality == c.PlayerSpeciality)).ToList();
                }
                else
                {
                    records = (records.Where(c => c.PlayerSpeciality == pModel.PlayerType)).ToList();
                }
                List<Playerlist> Slist = (List<Playerlist>)TempData["SelectPlayerlist"];
                TempData["SelectPlayerlist"] = Slist;

                if (Slist != null)
                {
                    // Make All player as Not-Selected
                    foreach (var rec in records)
                    {
                        rec.PSelected = null;
                    }
                    //Mark only selected players
                    foreach (var x in Slist)
                    {
                        
                        foreach (var rec in records)
                        {
                            if (rec.PlayerId == x.PlayerId)
                            {
                                rec.PSelected = "S";
                            }
                        }
                    }
                    if (Slist.Count > 0)
                    {
                        records = (records.Where(c => c.PSelected != "S")).ToList();
                    }
                }
                
               
                //records = (records.Where(c => c.PSelected != "S")).ToList();
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        public List<Playerlist> GetPlayerList(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            PlayerResponse PR = new PlayerResponse();
            List<Playerlist> listplayer = new List<Playerlist>();

            if (TempData["PlayerlistFromApi"] != null)
            {
                listplayer = (List<Playerlist>)TempData["PlayerlistFromApi"];
                TempData["PlayerlistFromApi"] = listplayer;
            }
            else
            {
                PR = ARS.TournamentWisePlayerList(pModel);
                if (PR.status == "success")
                {
                    listplayer = PR.data.ToList();
                }
                TempData["PlayerlistFromApi"] = listplayer;
            }
                        
            var records = listplayer.AsQueryable();

            if (!string.IsNullOrEmpty(sortBy) && !string.IsNullOrEmpty(direction))
            {
                if (direction.Trim().ToLower() == "asc")
                {
                    records = SortHelper.OrderBy(records, sortBy);
                }
                else
                {
                    records = SortHelper.OrderByDescending(records, sortBy);
                }
            }

            total = records.Count();
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }

            return records.ToList();
        }


        [HttpGet]
        [ActionName("SelectRejectPlayer")]
        public ActionResult SelectRejectPlayer(int? page, int? limit, string sortBy, string direction, PlayerModel pModel)
        {
            try
            {
                List<Playerlist> lst = (List<Playerlist>)TempData.Peek("Playerlist");
                List<Playerlist> selectplayers = new List<Playerlist>();
                if (pModel.PId == "0")
                {
                    selectplayers = (lst.Where(c => c.PSelected == "S")).ToList();
                }
                else
                {
                    selectplayers = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                }

                var PlayerExists = selectplayers.Where(item => item.PlayerId == pModel.PId).ToList();

                if (PlayerExists.Count > 0)
                {
                }
                else
                {
                    var selected = lst.Where(item => item.PlayerId == pModel.PId).ToList();
                    selectplayers.AddRange(selected);
                }
                TempData["Playerlist"] = lst;

                int total;
                var records = new GridModel().GetSelectPlayerList(page, limit, sortBy, direction, pModel, selectplayers, out total);
                TempData["SelectPlayerlist"] = records.ToList();
                records = (records.Where(c => c.PSelected == "S")).ToList();
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }

        [HttpPost]
        [ActionName("SelectOrRemove")]
        public ActionResult SelectOrRemove(PlayerModel pModel)
        {
            ResponseModel RM = new ResponseModel();
            try
            {
                List<Playerlist> PlayerList = (List<Playerlist>)TempData.Peek("Playerlist");
                List<Playerlist> SelectedPlayerList = new List<Playerlist>();
                if (TempData["SelectPlayerlist"] != null)
                {
                    SelectedPlayerList = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                }
                List<Playerlist> LastCutOff_PlayerList = new List<Playerlist>();
                ParamModel PR = new ParamModel();
                PR.TournamentId = Session["SelectTournamentId"].ToString();
                PR.UserTeamId = Session["TeamId"].ToString();
                Transfer tran = new Transfer();
                var trans_Left = (TempData["Trans_Left"] == null) ? Session["Subs"].ToString() : TempData.Peek("Trans_Left");//
                //var trans_Left = (TempData["Trans_Left"] == null) ? Session["SubsLeftAtSnapShot"].ToString() : TempData.Peek("Trans_Left");//
                if (Convert.ToInt32(trans_Left) == 0 && pModel.PSelect == "A")
                {
                    LastCutOff_PlayerList = Get_LastCutoffTeam(PR);
                    var playerExist = LastCutOff_PlayerList.Where(item => item.PlayerId == pModel.PId).ToList();
                    if (playerExist.Count > 0)
                    {
                        var selected = PlayerList.Where(item => item.PlayerId == pModel.PId).ToList();
                        SelectedPlayerList.AddRange(selected);
                        RM.status = "Success";
                    }
                    else
                    {
                        if (Session["TournamentStatus"].ToString() == "INPROGRESS")
                        {
                            RM.status = "LimitCross";
                            RM.statusMessage = "Transfer Limit Crossed";
                        }
                        else
                        {
                            var selected = PlayerList.Where(item => item.PlayerId == pModel.PId).ToList();
                            SelectedPlayerList.AddRange(selected);
                            RM.status = "Success";
                        }
                    }
                    tran.Transfer_left = trans_Left.ToString();
                    TempData["Trans_Left"] = trans_Left.ToString();
                    TempData["Playerlist"] = PlayerList;
                    TempData["SelectPlayerlist"] = SelectedPlayerList;
                }
                else
                {
                    if (pModel.PSelect == "A")
                    {
                        var selected = PlayerList.Where(item => item.PlayerId == pModel.PId).ToList();
                        SelectedPlayerList.AddRange(selected);

                        var itemToChange = PlayerList.FirstOrDefault(d => d.PlayerId == pModel.PId);
                        if (itemToChange != null)
                            itemToChange.PSelected = "S";
                        var SelectPlayerCount = Convert.ToInt32(Session["SelectPlayerCount"].ToString());
                        SelectPlayerCount = SelectPlayerCount + 1;
                        Session["SelectPlayerCount"] = SelectPlayerCount;
                        // Subs Calculation 

                    }
                    else
                    if (pModel.PSelect == "R")
                    {
                        var Vcap = SelectedPlayerList.FirstOrDefault(x => x.TeamVCapt == pModel.PId);
                        if (Vcap != null)
                        {
                            foreach (var ply in SelectedPlayerList)
                            {
                                 ply.TeamVCapt = null;
                            }
                        }

                        var player = SelectedPlayerList.SingleOrDefault(x => x.PlayerId == pModel.PId);
                        if (player != null)
                            SelectedPlayerList.Remove(player);

                        var itemToChange = PlayerList.FirstOrDefault(d => d.PlayerId == pModel.PId);
                        if (itemToChange != null)
                            itemToChange.PSelected = "U";
                        var SelectPlayerCount = Convert.ToInt32(Session["SelectPlayerCount"].ToString());
                        SelectPlayerCount = SelectPlayerCount - 1;
                        Session["SelectPlayerCount"] = SelectPlayerCount;
                    }

                    TempData["Playerlist"] = PlayerList;
                    TempData["SelectPlayerlist"] = SelectedPlayerList;

                    RM.status = "Success";
                    LastCutOff_PlayerList = Get_LastCutoffTeam(PR);
                    // Transfer Calculation ------------------------------------------------
                    if (Session["TournamentStatus"].ToString() == "INPROGRESS")
                    {
                        if (LastCutOff_PlayerList.Count > 0)
                        {
                            // if no lastcutoff available 
                            var trans_Left_Actual = "";
                            //trans_Left_Actual = Session["SubsLeftAtSnapShot"].ToString();
                            //trans_Left_Actual = Session["Subs"].ToString();
                            //---------------------
                            if (LastCutOff_PlayerList.Count == 0)
                            {
                                trans_Left_Actual = Session["SubsLeftAtSnapShot"].ToString();
                                var SelectedPlayerIds = SelectedPlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                                trans_Left_Actual = (Convert.ToInt32(trans_Left_Actual) - Convert.ToInt32(SelectedPlayerIds.Count())).ToString();
                                tran.Transfer_left = trans_Left_Actual.ToString();
                                TempData["Trans_Left"] = trans_Left_Actual.ToString();
                            }
                            else
                            {
                                if (pModel.PSelect == "A")
                                {
                                    trans_Left_Actual = Session["SubsLeftAtSnapShot"].ToString();
                                    var SelectedPlayerIds = SelectedPlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                                    var PreviousSelectedPlayerIds = LastCutOff_PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                                    var diff = SelectedPlayerIds.Except(PreviousSelectedPlayerIds);
                                    var Diff_cnt = diff.Count();
                                    trans_Left_Actual = (Convert.ToInt32(trans_Left_Actual) - Convert.ToInt32(Diff_cnt)).ToString();
                                    tran.Transfer_left = trans_Left_Actual.ToString();
                                    TempData["Trans_Left"] = trans_Left_Actual.ToString();
                                }
                                else
                                {
                                    trans_Left_Actual = Session["Subs"].ToString();
                                    if (TempData["Trans_Left"] == null)
                                    {
                                        tran.Transfer_left = trans_Left_Actual.ToString();
                                        TempData["Trans_Left"] = trans_Left_Actual.ToString();
                                    }
                                    else
                                    {
                                        tran.Transfer_left = TempData.Peek("Trans_Left").ToString();
                                        if (Convert.ToInt32(tran.Transfer_left) == 0)
                                        {
                                            var playerExist = LastCutOff_PlayerList.Where(item => item.PlayerId == pModel.PId).ToList();
                                            if (playerExist.Count == 0)
                                            {
                                                tran.Transfer_left = (Convert.ToInt32(tran.Transfer_left) + 1).ToString();
                                            }
                                        }
                                        TempData["Trans_Left"] = tran.Transfer_left;
                                    }
                                }
                            }
                        }
                        else
                        {
                            tran.Transfer_left = Session["SubsLeftAtSnapShot"].ToString();
                        }
                    }
                    else
                    {
                        tran.Transfer_left = "<img src='/Assets/Icon/infinity.png' alt='' height='20px' width='20px' />";
                    }
                }
                return Json(new { result = RM, cnt = tran }, JsonRequestBehavior.AllowGet);
            }
            catch ( Exception ee)
            {
                RM.status = "Unsuccess";
                RM.statusMessage = ee.Message;
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
        }

        [HttpPost]
        [ActionName("LoadSelectedPlayers")]
        public ActionResult LoadSelectedPlayers()
        {
            
            try
            {
                List<Playerlist> SelectedPlayerList = (List<Playerlist>)TempData["SelectPlayerlist"];
                TempData["SelectPlayerlist"] = SelectedPlayerList;
                if (SelectedPlayerList == null)
                {
                    List<Playerlist> NewSelectedPlayerList = new List<Playerlist>();
                    return Json(NewSelectedPlayerList, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    return Json(SelectedPlayerList, JsonRequestBehavior.AllowGet);
                }
            }
            catch (Exception ee)
            {
                List<Playerlist> SelectedPlayerList = new List<Playerlist>();
                return Json(SelectedPlayerList, JsonRequestBehavior.AllowGet);
            }
            
        }

        [HttpPost]
        [ActionName("TeamPlayer_LastCutoff")]
        [AllowAnonymous]
        public ActionResult TeamPlayer_LastCutoff(ParamModel Pmodel)
        {
            PlayerResponse PR = new PlayerResponse();
            List<Playerlist> PlayerList = new List<Playerlist>();
            PlayerList = Get_LastCutoffTeam(Pmodel);
            Transfer tran = new Transfer();
            if(Pmodel.Type == "Reset")
            {
                if (TempData["LCO_PlayerList"] != null)
                    TempData.Remove("LCO_PlayerList");
                tran.Transfer_left = Session["SubsLeftAtSnapShot"].ToString();
                TempData["Trans_Left"] = tran.Transfer_left;
                TempData["SelectPlayerlist"] = PlayerList;
            }
           
            return Json(new { result = PlayerList, cnt = tran }, JsonRequestBehavior.AllowGet);
        }

        public List<Playerlist> Get_LastCutoffTeam(ParamModel Pmodel)
        {
            PlayerResponse PR = new PlayerResponse();
            List<Playerlist> PlayerList = new List<Playerlist>();
            Pmodel.UserId = Session["UserId"].ToString();
            Pmodel.authtoken = Session["x-api-authtoken"].ToString();
            Pmodel.TournamentId = Session["SelectTournamentId"].ToString();
            Pmodel.UserTeamId = Session["TeamId"].ToString();

            if (TempData["LCO_PlayerList"] != null)
            {
                PlayerList = (List<Playerlist>)TempData["LCO_PlayerList"];
                TempData["LCO_PlayerList"] = PlayerList;
            }
            else
            {
                PR = ARS.UserTeamPlayer_LastCutOff(Pmodel);
                if (PR.status == "success")
                {
                    PlayerList = PR.data.ToList();
                    TempData["LCO_PlayerList"] = PlayerList;
                }
            }
            return PlayerList;
        }

        [HttpPost]
        [ActionName("AutoSelectTeam")]
        [AllowAnonymous]
        public ActionResult AutoSelectTeam(ParamModel Pmodel)
        {
            try
            {
                PlayerResponse PR = new PlayerResponse();
                List<Playerlist> PlayerList = new List<Playerlist>();
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Pmodel.TournamentId = Session["SelectTournamentId"].ToString();
                Transfer tran = new Transfer();
                PR = ARS.AutoSelection_Team(Pmodel);
                if (PR.status == "success")
                {
                    PlayerList = PR.data.ToList();

                    if (Session["TournamentStatus"].ToString() == "INPROGRESS")
                    {
                        var trans_Left_Actual = Session["SubsLeftAtSnapShot"].ToString();
                        tran.Transfer_left = trans_Left_Actual.ToString();
                        TempData["Trans_Left"] = trans_Left_Actual.ToString();
                    }
                    else
                    {
                        tran.Transfer_left = "<img src='/Assets/Icon/infinity.png' alt='' height='20px' width='20px' />";
                    }

                    TempData["SelectPlayerlist"] = PR.data.ToList();
                }
                return Json(new { result = PlayerList, cnt = tran }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("SavePlayer_Info")]
        [AllowAnonymous]
        public ActionResult SavePlayer_Info(UserTeamDataResponse Pmodel)
        {
            try
            {
                var TransferCnt = "0";
                List<Playerlist> PlayerList = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                List<Playerlist> LastCutOff_PlayerList = new List<Playerlist>();
                ParamModel PM = new ParamModel();
                PM.TournamentId = Session["SelectTournamentId"].ToString();
                PM.UserTeamId = Session["TeamId"].ToString();
                LastCutOff_PlayerList = Get_LastCutoffTeam(PM);
                if (LastCutOff_PlayerList.Count() == 0)
                {
                    TransferCnt = "0";
                }
                else
                {
                    var SelectedPlayerIds = PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                    var PreviousSelectedPlayerIds = LastCutOff_PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                    var diff = SelectedPlayerIds.Except(PreviousSelectedPlayerIds);
                    TransferCnt = diff.Count().ToString();
                }
                return Json(new { TransferCnt }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("SavePlayer")]
        [AllowAnonymous]
        public ActionResult SavePlayer(UserTeamDataResponse Pmodel)
        {
            try
            {
                ResponseModel RM = new ResponseModel();
                UserTeamResponse PR = new UserTeamResponse();
                List<UserTeamDataResponse> Response = new List<UserTeamDataResponse>();
                List<Playerlist> PlayerList = (List<Playerlist>)TempData.Peek("SelectPlayerlist");
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Pmodel.UserTeamId = Session["TeamId"].ToString();

                Pmodel.Player1 = PlayerList[0].PlayerId.ToString();
                Pmodel.Player2 = PlayerList[1].PlayerId.ToString();
                Pmodel.Player3 = PlayerList[2].PlayerId.ToString();
                Pmodel.Player4 = PlayerList[3].PlayerId.ToString();
                Pmodel.Player5 = PlayerList[4].PlayerId.ToString();
                Pmodel.Player6 = PlayerList[5].PlayerId.ToString();
                Pmodel.Player7 = PlayerList[6].PlayerId.ToString();
                Pmodel.Player8 = PlayerList[7].PlayerId.ToString();
                Pmodel.Player9 = PlayerList[8].PlayerId.ToString();
                Pmodel.Player10 = PlayerList[9].PlayerId.ToString();
                Pmodel.Player11 = PlayerList[10].PlayerId.ToString();

                var NumberOfSubs = Session["Subs"].ToString();

                ParamModel PM = new ParamModel();
                PM.TournamentId = Session["SelectTournamentId"].ToString();
                PM.UserTeamId = Session["TeamId"].ToString();
                List<Playerlist> LastCutOff_PlayerList = new List<Playerlist>();
                LastCutOff_PlayerList = Get_LastCutoffTeam(PM);
                if (LastCutOff_PlayerList.Count() == 0)
                {
                    Pmodel.NumberOfSubs = "0";
                }
                else
                {
                    var SelectedPlayerIds = PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                    var PreviousSelectedPlayerIds = LastCutOff_PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
                    var diff = SelectedPlayerIds.Except(PreviousSelectedPlayerIds);
                    var Diff_cnt = diff.Count();
                    Pmodel.NumberOfSubs = Diff_cnt.ToString();
                }
                if (PlayerList[0].TeamCapt != null)
                {
                    Pmodel.TeamCapt = PlayerList[0].TeamCapt.ToString();
                }
                if (PlayerList[0].TeamVCapt != null)
                {
                    Pmodel.TeamVCapt = PlayerList[0].TeamVCapt.ToString();
                }

                PR = ARS.Save_Players(Pmodel);
                if (PR.status == "success")
                {
                    RM.status = "Success";

                }
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpPost]
        [ActionName("SaveCaptainVsCaptain")]
        [AllowAnonymous]
        public ActionResult SaveCaptainVsCaptain(ParamModel Pmodel)
        {
            try
            {
                ResponseModel RM = new ResponseModel();

                List<Playerlist> PlayerList = (List<Playerlist>)TempData["SelectPlayerlist"];

                if (Pmodel.PlayerSelectAs == "Captain")
                {
                    foreach (var ply in PlayerList)
                    {
                        if (ply.TeamVCapt == Pmodel.PlayerId)
                        {
                            ply.TeamVCapt = null;
                        }
                        ply.TeamCapt = Pmodel.PlayerId;
                    }
                }
                if (Pmodel.PlayerSelectAs == "ViceCaptain")
                {
                    foreach (var ply in PlayerList)
                    {
                        if (ply.TeamCapt == Pmodel.PlayerId)
                        {
                            ply.TeamCapt = null;
                        }
                        ply.TeamVCapt = Pmodel.PlayerId;
                    }
                }
                TempData["SelectPlayerlist"] = PlayerList;
                RM.status = "Success";


                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [ActionName("CheckAvailable")]
        [AllowAnonymous]
        public ActionResult CheckAvailable(ParamModel PM)
        {
            try
            {
                ResponseModel RM = new ResponseModel();
                RM = ARS.CheckAvailable(PM);
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("ChangeTeamName")]
        [AllowAnonymous]
        public ActionResult ChangeTeamName(ParamModel PM)
        {
            try
            {
                CreateTeamResponse LR = new CreateTeamResponse();
                List<CreateTeamDataResponse> Detail = new List<CreateTeamDataResponse>();
                PM.UserId = Session["UserId"].ToString();
                LR = ARS.ChangeTeamName(PM);
                if (LR.status == "success")
                {
                    Detail = LR.data.ToList();
                }
                return Json(Detail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }


        // --------------------- Team Stat --------------------------------------

        [AllowAnonymous]
        public ActionResult TeamStat()
        {
            if (Session["UserName"] != null)
            {
                ViewBag.UserName = Session["UserName"].ToString();
                Session["Utid_Stat"] = Request.QueryString["utid"].ToString();
                Session["Tid_Stat"] = Request.QueryString["tid"].ToString();
                return View();
            }
            else
            {
                return RedirectToAction("Index", "Account");
            }
        }

        [HttpGet]
        [ActionName("Top_Ten_Player")]
        public ActionResult Top_Ten_Player(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().Get_Top_Ten_Player(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpGet]
        [ActionName("Recent_Match_Captain_Usage")]
        public ActionResult Recent_Match_Captain_Usage(int? page, int? limit, string sortBy, string direction, ParamModel pModel)
        {
            try
            {
                int total;
                var records = new GridModel().Get_Recent_Match_Captain_Usage(page, limit, sortBy, direction, pModel, out total);
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("UpdateUserPowerplay")]
        [AllowAnonymous]
        public ActionResult UpdateUserPowerplay(UpdatePowerPlayModel PM)
        {
            try
            {
                UpdatePowerPlayModelResp RM = new UpdatePowerPlayModelResp();
                RM = ARS.UpdateUserPowerplay(PM);
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
        [HttpPost]
        [ActionName("FetchUserPowerPlay")]
        [AllowAnonymous]
        public ActionResult FetchUserPowerPlay(FetchUserPowerPlay PM)
        {
            try
            {
                UpdatePowerPlayModelRespFeach RM = new UpdatePowerPlayModelRespFeach();
                RM = ARS.FetchUserPowerPlay(PM);
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }
    }
}