using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Fantacy_Model;
using FantacyWeb.Models;
using System.Data;
using System.Text;

namespace FantacyWeb.Models
{
    public class GridModel
    {
        private AccountRestService ARS = new AccountRestService();
        private AdminAccountRestService AARS = new AdminAccountRestService();
        public List<LeagueDataResponse> UserActiveLeagueinfo(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            LeagueResponse LR = new LeagueResponse();
            List<LeagueDataResponse> TDetail = new List<LeagueDataResponse>();
            //pModel.UserId = Session["UserId"].ToString();
            LR = ARS.UserActiveLeagueInfo(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
                var records = TDetail.AsQueryable();
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

        public List<LeagueTeamDetailDataResponse> GetLeagueTeams(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            LeagueTeamDetailResponse LR = new LeagueTeamDetailResponse();
            List<LeagueTeamDetailDataResponse> TDetail = new List<LeagueTeamDetailDataResponse>();
           
            LR = ARS.UserActiveLeagueDetails(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        
        public List<Playerlist> GetSelectPlayerList(int? page, int? limit, string sortBy, string direction, PlayerModel pModel, List<Playerlist> listplayer, out int total)
        {

            foreach (var player in listplayer.Where(w => w.PlayerId == pModel.PId))
            {
                if (pModel.PSelect == "R")
                {
                    player.PSelected = "U";
                }
                else
                {
                    player.PSelected = "S";
                }
            }


            var records = listplayer.AsQueryable();
            //records = SortHelper.OrderBy(records, "PSelected");

            if (!string.IsNullOrEmpty(sortBy) && !string.IsNullOrEmpty(direction))
            {
                if (direction.Trim().ToLower() == "asc")
                {
                    records = SortHelper.OrderBy(records, "PSelected").ThenBy(sortBy);
                }
                else
                {
                    records = SortHelper.OrderByDescending(records, "PSelected").ThenBy(sortBy);
                }
            }
            else
            {
                records = SortHelper.OrderBy(records, "PSelected");
            }


            total = records.Count();
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }

            return records.ToList();
        }


        // League Stats-------------------------------------------

        public List<LeagueStatdata> Get_Team_Top_Performer(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            LeagueStat LR = new LeagueStat();
            List<LeagueStatdata> TDetail = new List<LeagueStatdata>();

            LR = ARS.Team_Top_Performer(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        public List<LeagueStatdata> Team_Top_Preferred_Players(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            LeagueStat LR = new LeagueStat();
            List<LeagueStatdata> TDetail = new List<LeagueStatdata>();

            LR = ARS.Team_Top_Preferred_Players(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        // Team Stats-------------------------------------------

        public List<TeamStatdata> Get_Top_Ten_Player(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            TeamStat LR = new TeamStat();
            List<TeamStatdata> TDetail = new List<TeamStatdata>();

            LR = ARS.Top_Ten_Player(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        public List<TeamStatdata> Get_Recent_Match_Captain_Usage(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            TeamStat LR = new TeamStat();
            List<TeamStatdata> TDetail = new List<TeamStatdata>();

            LR = ARS.Recent_Match_Captain_Usage(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        // Tournament Stats-------------------------------------------

        public List<GlobalTopPlayerData> GlobalTopPlayers(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            GlobalTopPlayer LR = new GlobalTopPlayer();
            List<GlobalTopPlayerData> TDetail = new List<GlobalTopPlayerData>();

            LR = ARS.GlobalTopPlayers(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        public List<GlobalTopLeagueData> GlobalTopLeagues(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            GlobalTopLeague LR = new GlobalTopLeague();
            List<GlobalTopLeagueData> TDetail = new List<GlobalTopLeagueData>();

            LR = ARS.GlobalTopLeagues(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        public List<GlobalTopTeamData> GlobalTopTeams(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            GlobalTopTeam LR = new GlobalTopTeam();
            List<GlobalTopTeamData> TDetail = new List<GlobalTopTeamData>();

            LR = ARS.GlobalTopTeams(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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

        public List<MatchResultData> TopTenUsers(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            MatchResult LR = new MatchResult();
            List<MatchResultData> TDetail = new List<MatchResultData>();
            LR = ARS.TopTenUser(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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
        public List<liveleagueUserData> LiveLeagueUser(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            liveleagueUser LR = new liveleagueUser();
            List<liveleagueUserData> TDetail = new List<liveleagueUserData>();

            LR = ARS.LiveLeagueUser(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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


        public List<LiveScoreData> LiveScore(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {

            LiveScore LR = new LiveScore();
            List<LiveScoreData> TDetail = new List<LiveScoreData>();

            LR = ARS.LiveScore(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
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


        // ---------------------------  Admin Grid ------------------------------//

        public List<TournamentPointModel> GetTournamentPointList(int? page, int? limit, string sortBy, string direction, DataTable dt, out int total)
        {
           
                int i = 1;
                List<TournamentPointModel> list = new List<TournamentPointModel>();
                foreach (DataRow dtRow in dt.Rows)
                {
                    if (dtRow["TournamentStage"].ToString() != "")
                    {
                        TournamentPointModel TM = new TournamentPointModel();

                        TM.TournamentStage = dtRow["TournamentStage"].ToString();
                        TM.RunScored = dtRow["RunScored"].ToString();
                        TM.FourBonus = dtRow["FourBonus"].ToString();
                        TM.SixBonus = dtRow["SixBonus"].ToString();
                        TM.HalfCenturyBonus = dtRow["HalfCenturyBonus"].ToString();
                        TM.CenturyBonus = dtRow["CenturyBonus"].ToString();
                        TM.DismissalDuck = dtRow["DismissalDuck"].ToString();
                        TM.MinBall4SR = dtRow["MinBall4SR"].ToString();
                        TM.StrikeRateBelow50 = dtRow["StrikeRateBelow50"].ToString();
                        TM.StrikeRate50To60 = dtRow["StrikeRate50To60"].ToString();
                        TM.StrikeRate60To70 = dtRow["StrikeRate60To70"].ToString();
                        TM.StrikeRate110To150 = dtRow["StrikeRate110To150"].ToString();
                        TM.StrikeRateUp150 = dtRow["StrikeRateUp150"].ToString();
                        TM.WicketTaken = dtRow["WicketTaken"].ToString();
                        TM.Wicket3UpBonus = dtRow["Wicket3UpBonus"].ToString();
                        TM.Wicket5UpBonus = dtRow["Wicket5UpBonus"].ToString();

                        TM.MaidenOver = dtRow["MaidenOver"].ToString();
                        TM.Hattrick = dtRow["Hattrick"].ToString();
                        TM.MinOver4ER = dtRow["MinOver4ER"].ToString();
                        TM.EconomyBelow4 = dtRow["EconomyBelow4"].ToString();
                        TM.Economy4To5 = dtRow["Economy4To5"].ToString();
                        TM.Economy5To6 = dtRow["Economy5To6"].ToString();
                        TM.Economy9To11 = dtRow["Economy9To11"].ToString();
                        TM.EconomyUp11 = dtRow["EconomyUp11"].ToString();
                        TM.Captain = dtRow["Captain"].ToString();
                        TM.ViceCaptain = dtRow["ViceCaptain"].ToString();
                        TM.CatchTaken = dtRow["CatchTaken"].ToString();
                        TM.Stumping = dtRow["Stumping"].ToString();
                        TM.RunOutDirect = dtRow["RunOutDirect"].ToString();
                        TM.RunOutThrower = dtRow["RunOutThrower"].ToString();
                        TM.RunOutCatcher = dtRow["RunOutCatcher"].ToString();
                        TM.Nitro = dtRow["Nitro"].ToString();
                        TM.MoM = dtRow["MoM"].ToString();

                    list.Add(TM);
                        i += 1;
                    }
                }
               
          
                var records = list.AsQueryable();
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

        public List<TournamentPointModel> GetPointList(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            AdminTournamentPointModel LR = new AdminTournamentPointModel();
            List<TournamentPointModel> TDetail = new List<TournamentPointModel>();

            LR = AARS.GetTournamentPoint(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
            total = records.Count();
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
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }
            return records.ToList();
        }

        public List<AdminMatchDetailsModel> GetMatchList(int? page, int? limit, string sortBy, string direction, DataTable dt, out int total)
        {

            int i = 1;
            List<AdminMatchDetailsModel> list = new List<AdminMatchDetailsModel>();
            foreach (DataRow dtRow in dt.Rows)
            {
                if (dtRow["MatchNo"].ToString() != "")
                {
                    AdminMatchDetailsModel TM = new AdminMatchDetailsModel();

                    TM.MatchNo = dtRow["MatchNo"].ToString();
                    TM.MatchType = dtRow["MatchType"].ToString();
                    TM.Venue = dtRow["Venue"].ToString();
                    TM.MatchStage = dtRow["MatchStage"].ToString();
                    TM.Team1 = dtRow["Team1"].ToString();
                    TM.Team2 = dtRow["Team2"].ToString();
                    TM.MatchScheduledDate = Convert.ToDateTime(dtRow["MatchScheduledDate"].ToString()).ToString("yyyy-MM-dd");
                    string MTime = Convert.ToDateTime(dtRow["MatchScheduledTime"].ToString()).ToString("HH:mm:ss");
                    TM.MatchScheduledTime = MTime.Replace('.', ':');
                    TM.TournamentName = dtRow["TournamentName"].ToString();
                    list.Add(TM);
                    i += 1;
                }
            }


            var records = list.AsQueryable();
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

        public List<AdminMatchDetailsModel> GetSaveMatch(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            AdminMatchModel LR = new AdminMatchModel();
            List<AdminMatchDetailsModel> TDetail = new List<AdminMatchDetailsModel>();

           
            if(pModel.GameType == "D")
            {
                LR = AARS.GetSaveDailyMatch(pModel);
            }
            else
            {
                LR = AARS.GetSaveMatch(pModel);
            }
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
            total = records.Count();
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
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }
            return records.ToList();
        }

        public List<AdminTeamDetailsModel> GetTeamList(int? page, int? limit, string sortBy, string direction, DataTable dt, out int total)
        {

            int i = 1;
            List<AdminTeamDetailsModel> list = new List<AdminTeamDetailsModel>();
            foreach (DataRow dtRow in dt.Rows)
            {
                if (dtRow["ParticipationTeamName"].ToString() != "")
                {
                    AdminTeamDetailsModel TM = new AdminTeamDetailsModel();

                    TM.ParticipationTeamName = dtRow["ParticipationTeamName"].ToString();
                    TM.TeamDescription = dtRow["TeamDescription"].ToString();
                    TM.TeamShortName = dtRow["TeamShortName"].ToString();
                    list.Add(TM);
                    i += 1;
                }
            }


            var records = list.AsQueryable();
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
        public List<AdminTeamDetailsModel> GetSaveTeam(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            AdminTeamModel LR = new AdminTeamModel();
            List<AdminTeamDetailsModel> TDetail = new List<AdminTeamDetailsModel>();

            LR = AARS.GetSaveTeam(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
            total = records.Count();
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
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }
            return records.ToList();
        }

        public List<AdminPlayerDetailsModel> GetAdminPlayerList(int? page, int? limit, string sortBy, string direction, DataTable dt, out int total)
        {

            int i = 1;
            List<AdminPlayerDetailsModel> list = new List<AdminPlayerDetailsModel>();
            foreach (DataRow dtRow in dt.Rows)
            {
                if (dtRow["PlayerName"].ToString() != "")
                {
                    AdminPlayerDetailsModel TM = new AdminPlayerDetailsModel();

                    TM.PlayerName = dtRow["PlayerName"].ToString();
                    TM.PlayerShortName = dtRow["PlayerShortName"].ToString();
                    TM.PlayerDesc = dtRow["PlayerDesc"].ToString();
                    TM.PlayerType = dtRow["PlayerType"].ToString();
                    TM.PlayerSpeciality = dtRow["PlayerSpeciality"].ToString();
                    TM.PlayerValue = dtRow["PlayerValue"].ToString();
                    TM.ParticipationTeamName = dtRow["ParticipationTeam"].ToString();

                    list.Add(TM);
                    i += 1;
                }
            }


            var records = list.AsQueryable();
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

        public List<AdminPlayerDetailsModel> GetSavePlayer(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            AdminPlayerModel LR = new AdminPlayerModel();
            List<AdminPlayerDetailsModel> TDetail = new List<AdminPlayerDetailsModel>();
            if (pModel.GameType == "D")
            {
                LR = AARS.GetDailySavePlayer(pModel);
            }
            else
            {
                LR = AARS.GetSavePlayer(pModel);
            }
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
            total = records.Count();
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
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }
            return records.ToList();
        }

        public List<AdminAutoTeamDetailsModel> GetAutoTeam(int? page, int? limit, string sortBy, string direction, ParamModel pModel, out int total)
        {
            AdminAutoTeamModel LR = new AdminAutoTeamModel();
            List<AdminAutoTeamDetailsModel> TDetail = new List<AdminAutoTeamDetailsModel>();

            LR = AARS.GetAutoTeam(pModel);
            if (LR.status == "success")
            {
                TDetail = LR.data.ToList();
            }
            var records = TDetail.AsQueryable();
            total = records.Count();
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
            if (page.HasValue && limit.HasValue)
            {
                int start = (page.Value - 1) * limit.Value;
                records = records.Skip(start).Take(limit.Value);
            }
            return records.ToList();
        }

        public List<ClaimUploadDetailsModel> GetClaimList(int? page, int? limit, string sortBy, string direction, DataTable dt, out int total)
        {

            int i = 1;
            List<ClaimUploadDetailsModel> list = new List<ClaimUploadDetailsModel>();
            foreach (DataRow dtRow in dt.Rows)
            {
                if (dtRow["UserId"].ToString() != "")
                {
                    ClaimUploadDetailsModel TM = new ClaimUploadDetailsModel();

                    TM.Voucher = dtRow["Voucher"].ToString();
                    TM.Bundle = dtRow["Bundle"].ToString();
                    TM.UserId = Convert.ToInt32(dtRow["UserId"]);
                    TM.ClaimAmount = Convert.ToInt32(dtRow["ClaimAmount"]);
                    TM.Comments = dtRow["Comments"].ToString();
                    list.Add(TM);
                    i += 1;
                }
            }


            var records = list.AsQueryable();
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
    }
}