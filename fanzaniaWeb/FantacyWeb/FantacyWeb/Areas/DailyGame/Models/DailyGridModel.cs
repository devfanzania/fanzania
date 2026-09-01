using Fantacy_Model.DailyGame;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using FantacyWeb.Models;

namespace FantacyWeb.Areas.DailyGame.Models
{
    public class DailyGridModel
    {
        private DailyRestService DRS = new DailyRestService();

       

        public List<DailyLeagueTeamDetailsModel> GetLeagueTeamList(int? page, int? limit, string sortBy, string direction, DailyParamModel pModel, out int total)
        {

            DailyLeagueTeamModel PR = new DailyLeagueTeamModel();
            List<DailyLeagueTeamDetailsModel> listplayer = new List<DailyLeagueTeamDetailsModel>();

            PR = DRS.DailyLeagueTeams(pModel);
            if (PR.status == "success")
            {
                listplayer = PR.data.ToList();
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
    }
}