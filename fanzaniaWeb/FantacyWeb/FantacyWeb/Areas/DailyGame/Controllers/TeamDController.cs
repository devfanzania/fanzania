using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Fantacy_Model.DailyGame;
using FantacyWeb.Areas.DailyGame.Models;
using FantacyWeb.Models;

namespace FantacyWeb.Areas.DailyGame.Controllers
{
    public class TeamDController : Controller
    {
        private DailyRestService DRS = new DailyRestService();
        // GET: DailyGame/TeamD
        public ActionResult Index()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    Session["USERSELECTION"] = "Team";
                    Session["USERSELECTIONMODE"] = "D";
                    ViewBag.UserName = Session["UserName"].ToString();
                    return View();

                }
                else
                {
                    return Redirect("../Account/Index");
                }
            }
            catch (Exception ee)
            {
                //return Redirect("../Home/Error");
                return Redirect("../Account/Index");
            }
        }

        [HttpPost]
        [ActionName("RedirectToManageTeam")]
        [AllowAnonymous]
        public ActionResult RedirectToManageTeam(DailyParamModel Pmodel)
        {
            try
            {
                var qstr = "tid=" + Pmodel.TournamentId + "&mtype=" + Pmodel.MatchType + "&mid=" + Pmodel.MatchId + "&type=" + Pmodel.FilterType;
                var passPhraseReg = "amaf7LLSWhN@#r5!*";
                var enc = ECDC.Encrypt(qstr.ToString(), passPhraseReg);
                var redirectUrl = "/DailyGame/TeamD/ManageTeam?enc=" + enc;
                return Json(new { Url = redirectUrl });

            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }
        }

        [HttpGet]
        public ActionResult ManageTeam()
        {
            try
            {
                if (Session["UserName"] != null)
                {
                    ViewBag.Page = "Team";
                    ViewBag.UserName = Session["UserName"].ToString();
                    
                    var tstat = ""; var opType = "";
                    if (Request.QueryString["enc"] != null)
                    {
                        var passPhraseReg = "amaf7LLSWhN@#r5!*";
                        string enc = Request.QueryString["enc"].ToString();
                        var dcp = ECDC.Decrypt(enc.Replace(" ", "+"), passPhraseReg);

                        DailyParamModel pm = new DailyParamModel();
                        pm.MatchType = dcp.Split('&')[1].Split('=')[1].ToString();
                        pm.MatchId = dcp.Split('&')[2].Split('=')[1].ToString();

                        Session["Session_DMatchId"] = dcp.Split('&')[2].Split('=')[1].ToString();
                        Session["Session_DMatchType"] = dcp.Split('&')[1].Split('=')[1].ToString();
                        Session["Session_DMatchTId"] = dcp.Split('&')[0].Split('=')[1].ToString();
                        opType = dcp.Split('&')[3].Split('=')[1].ToString();
                    }

                    //if (Request.QueryString["mtype"] != null)
                    //{
                    //    DailyParamModel pm = new DailyParamModel();
                    //    pm.MatchType = Request.QueryString["mtype"].ToString();
                    //    pm.MatchId = Request.QueryString["mid"].ToString();

                    //    Session["Session_DMatchId"] = Request.QueryString["mid"].ToString();
                    //    Session["Session_DMatchType"] = Request.QueryString["mtype"].ToString();
                    //    Session["Session_DMatchTId"] = Request.QueryString["tid"].ToString();
                    //}

                    //if (Request.QueryString["type"].ToString() == "new")
                    if (opType == "new")
                    {
                        TempData.Clear();
                        Session["SelectDPlayerCount"] = "0";
                        Session["DBatsmanCount"] = "0";
                        Session["DWicketkeeperCount"] = "0";
                        Session["DBowlerCount"] = "0";
                        Session["DAllrounderCount"] = "0";
                        Session["SelectPlayerCount"] = "0";
                        //Session["TeamType"] = "NewCreate";

                        return View();
                    }
                    else
                    {
                        List<DailyUserPlayerDetailsModel> Slist = new List<DailyUserPlayerDetailsModel>();
                        if (TempData["DSelectPlayerlist"] != null)
                        {
                            Slist = (List<DailyUserPlayerDetailsModel>)TempData.Peek("DSelectPlayerlist");
                        }

                        Session["SelectDPlayerCount"] = Slist.Count;
                        Session["DBatsmanCount"] = (Slist.Where(x => x.PlayerSpeciality == "batsman").ToList()).Count;
                        Session["DWicketkeeperCount"] = (Slist.Where(x => x.PlayerSpeciality == "wicketkeeper").ToList()).Count;
                        Session["DBowlerCount"] = (Slist.Where(x => x.PlayerSpeciality == "bowler").ToList()).Count;
                        Session["DAllrounderCount"] = (Slist.Where(x => x.PlayerSpeciality == "allrounder").ToList()).Count;
                        Session["SelectPlayerCount"] = "11";
                        TempData["DSelectPlayerlist"] = Slist;
                        //Session["TeamType"] = "EditTeam";

                        return View();
                    }
                    //return View();
                }
                else
                {
                    return Redirect("../Account/Index");
                }
            }
            catch (Exception ee)
            {
                 return Redirect("../Home/Error");
            }
        }


        [HttpPost]
        [ActionName("ShowTeamFilter")]
        [AllowAnonymous]
        public ActionResult ShowTeamFilter()
        {
            try
            {
                
                List<DailyUserPlayerDetailsModel> Plist = (List<DailyUserPlayerDetailsModel>)TempData["Playerlist"];
                TempData["Playerlist"] = Plist;
                var teams = Plist.Select(m => new { m.ParticipationTeamId, m.ParticipationTeamName, m.TeamShortName }).Distinct().ToList();
                return Json(teams, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }

        }

        [HttpPost]
        [ActionName("PlayerSelectionRules")]
        [AllowAnonymous]
        public ActionResult PlayerSelectionRules(DailyParamModel Pmodel)
        {
            try
            {
                DailyTeamRulesResponse LR = new DailyTeamRulesResponse();
                List<DailyTeamRulesDataResponse> TDetail = new List<DailyTeamRulesDataResponse>();
                LR = DRS.PlayerSelectionRules(Pmodel);
                if (LR.status == "success")
                {
                    TDetail = LR.data.ToList();
                }
                return Json(TDetail, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                 return Redirect("../Home/Error");
            }
        }

        [HttpPost]
        [ActionName("FetchPlayerStats")]
        public ActionResult FetchPlayerStats(FeachPlayerModel pModel)
        {
            try
            {
            
                PlayerResponce LR = new PlayerResponce();
         //       List<PlayerListt> NDetail = new List<PlayerListt>();
                LR = DRS.FetchPlayerStats(pModel);
                if (LR.status == "success")
                {
                    return Json(LR.data, JsonRequestBehavior.AllowGet);
                }
                else {
                    return RedirectToAction("Error", "Home");
                }
               
            }
            catch (Exception ee)
            {
                return RedirectToAction("Error", "Home");
            }

        }
        [HttpGet]
        [ActionName("GetPlayerlist")]
        public ActionResult GetPlayerlist(int? page, int? limit, string sortBy, string direction, DailyParamModel pModel)
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
                    List<DailyUserPlayerDetailsModel> filteredData = new List<DailyUserPlayerDetailsModel>();
                    var teamlst = pModel.FilterTeams.Split(',');
                    foreach (string i in teamlst)
                    {
                        if (!string.IsNullOrEmpty(i))
                        {
                            List<DailyUserPlayerDetailsModel> temlList = new List<DailyUserPlayerDetailsModel>();
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
                List<DailyUserPlayerDetailsModel> Slist = (List<DailyUserPlayerDetailsModel>)TempData["DSelectPlayerlist"];
                TempData["DSelectPlayerlist"] = Slist;
                if (Slist != null)
                {
                    foreach (var x in Slist)
                    {
                        var test = records.FirstOrDefault();
                        foreach (var rec in records)
                        {
                            if (rec.PlayerId == x.PlayerId)
                            {
                                rec.PSelected = "S";
                            }
                        }
                    }
                }
                records = (records.Where(c => c.PSelected != "S")).ToList();
                return Json(new { records, total }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                 return Redirect("../Home/Error");
            }
        }

        public List<DailyUserPlayerDetailsModel> GetPlayerList(int? page, int? limit, string sortBy, string direction, DailyParamModel pModel, out int total)
        {

            DailyUserPlayerModel PR = new DailyUserPlayerModel();
            List<DailyUserPlayerDetailsModel> listplayer = new List<DailyUserPlayerDetailsModel>();

            if (TempData["DPreviousMatchId"] == null || TempData["DPreviousMatchId"].ToString() != pModel.MatchId)
            {
                TempData["DPreviousMatchId"] = pModel.MatchId;
                PR = DRS.DailyMatchesPlayerList(pModel);
                if (PR.status == "success")
                {
                    listplayer = PR.data.ToList();
                }
                TempData["DPlayerlistFromApi"] = listplayer;
            }
            else if (TempData["DPreviousMatchId"].ToString() == pModel.MatchId)
            {
                TempData["DPreviousMatchId"] = pModel.MatchId;
                if (TempData["DPlayerlistFromApi"] != null)
                {
                    listplayer = (List<DailyUserPlayerDetailsModel>)TempData["DPlayerlistFromApi"];
                    TempData["DPlayerlistFromApi"] = listplayer;
                }
                else
                {
                    PR = DRS.DailyMatchesPlayerList(pModel);
                    if (PR.status == "success")
                    {
                        listplayer = PR.data.ToList();
                    }
                    TempData["DPlayerlistFromApi"] = listplayer;
                }
            }
            //else if (TempData["DPreviousMatchId"] != pModel.MatchId)
            //{
            //    TempData["DPreviousMatchId"] = pModel.MatchId;
            //    PR = DRS.DailyMatchesPlayerList(pModel);
            //    if (PR.status == "success")
            //    {
            //        listplayer = PR.data.ToList();
            //    }
            //    TempData["DPlayerlistFromApi"] = listplayer;
            //}


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


        [HttpPost]
        [ActionName("SelectOrRemove")]
        public ActionResult SelectOrRemove(DailyPlayerModel pModel)
        {
            DailyResponseModel RM = new DailyResponseModel();
            try
            {
                List<DailyUserPlayerDetailsModel> PlayerList = (List<DailyUserPlayerDetailsModel>)TempData.Peek("Playerlist");
                List<DailyUserPlayerDetailsModel> SelectedPlayerList = new List<DailyUserPlayerDetailsModel>();
                if (TempData["DSelectPlayerlist"] != null)
                {
                    SelectedPlayerList = (List<DailyUserPlayerDetailsModel>)TempData.Peek("DSelectPlayerlist");
                }
                //DailyParamModel PR = new DailyParamModel();
                //PR.UserTeamId = Session["TeamId"].ToString();
              
                
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
                    TempData["DSelectPlayerlist"] = SelectedPlayerList;

                    RM.status = "Success";
                                    
                return Json(new { result = RM }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
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
            List<DailyUserPlayerDetailsModel> SelectedPlayerList = (List<DailyUserPlayerDetailsModel>)TempData["DSelectPlayerlist"];
            TempData["DSelectPlayerlist"] = SelectedPlayerList;
            return Json(SelectedPlayerList, JsonRequestBehavior.AllowGet);
        }



        //[HttpPost]
        //[ActionName("SavePlayer_Info")]
        //[AllowAnonymous]
        //public ActionResult SavePlayer_Info()
        //{
        //    try
        //    {
        //        var TransferCnt = "0";
        //        List<DailyUserPlayerDetailsModel> PlayerList = (List<DailyUserPlayerDetailsModel>)TempData.Peek("DSelectPlayerlist");

        //        DailyResponseModel RM = new DailyResponseModel();
        //        if (LastCutOff_PlayerList.Count() == 0)
        //        {
        //            TransferCnt = "0";
        //        }
        //        else
        //        {
        //            var SelectedPlayerIds = PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
        //            var PreviousSelectedPlayerIds = LastCutOff_PlayerList.Where(s => s.PlayerId == s.PlayerId).Select(s => s.PlayerId).ToList();
        //            var diff = SelectedPlayerIds.Except(PreviousSelectedPlayerIds);
        //            TransferCnt = diff.Count().ToString();
        //        }
        //        return Json(new { TransferCnt }, JsonRequestBehavior.AllowGet);
        //    }
        //    catch (Exception ee)
        //    {
        //        return Redirect("../Home/Error");
        //    }
        //}

        [HttpPost]
        [ActionName("SavePlayer")]
        [AllowAnonymous]
        public ActionResult SavePlayer(DailyUserTeamDataResponse WP)
        {
            try
            {
                DailyResponseModel RM = new DailyResponseModel();
                DailyUserTeamDataResponse Pmodel = new DailyUserTeamDataResponse();
                DailyUserTeamResponse PR = new DailyUserTeamResponse();
                List<DailyUserPlayerDetailsModel> PlayerList = (List<DailyUserPlayerDetailsModel>)TempData.Peek("DSelectPlayerlist");
                Pmodel.UserId = Session["UserId"].ToString();
                Pmodel.authtoken = Session["x-api-authtoken"].ToString();
                Pmodel.MatchId = Session["Session_DMatchId"].ToString();
                Pmodel.TournamentId = Session["Session_DMatchTId"].ToString();
                Pmodel.WinnerPrediction = WP.WinnerPrediction.ToString();
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

                if (PlayerList[0].TeamCapt != null)
                {
                    Pmodel.TeamCapt = PlayerList[0].TeamCapt.ToString();
                }
                if (PlayerList[0].TeamVCapt != null)
                {
                    Pmodel.TeamVCapt = PlayerList[0].TeamVCapt.ToString();
                }

                PR = DRS.Save_Players(Pmodel);
                if (PR.status == "success")
                {
                    RM.status = "Success";

                }
                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }

        [HttpPost]
        [ActionName("SaveCaptainVsCaptain")]
        [AllowAnonymous]
        public ActionResult SaveCaptainVsCaptain(DailyParamModel Pmodel)
        {
            try
            {
                DailyResponseModel RM = new DailyResponseModel();

                List<DailyUserPlayerDetailsModel> PlayerList = (List<DailyUserPlayerDetailsModel>)TempData["DSelectPlayerlist"];

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
                TempData["DSelectPlayerlist"] = PlayerList;
                RM.status = "Success";


                return Json(RM, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ee)
            {
                return Redirect("../Home/Error");
            }
        }
    }
}