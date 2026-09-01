using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class TournamentResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TournamentDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
        public string Registered { get; set; }
    }
    public class TournamentDataResponse
    {
        public string TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentStage { get; set; }
        public string TournamentStartDate { get; set; }
        public string TournamentEndDate { get; set; }
    }

    public class UserTournamentResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<UserTournamentDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }
       
    }

    public class UserTournamentDataResponse
    {
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string UserId { get; set; }
        public string SubsLeft { get; set; }
        public string NitroLeft { get; set; }
        public string TotalPoints { get; set; }
        public string TeamCompositionId { get; set; }
        public string TeamRank { get; set; }
        public string CreatedDate { get; set; }
        public string ModifiedDate { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
        public string TournamentId { get; set; }
        public string TournamentName { get; set; }
        public string TournamentStatus { get; set; }
        public string TournamentStage { get; set; }
        public string TournamentStartDate { get; set; }
        public string TournamentEndDate { get; set; }
        public string TournamentLogo { get; set; }
        
    }
    public class LeagueResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LeagueDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class LeagueDataResponse
    {
        public string LeagueLeaderId { get; set; }
        public string LeagueName { get; set; }
        public string LeagueId { get; set; }
        public string Status { get; set; }
        public string LeaguePin { get; set; }
        public string TeamStanding { get; set; }
        public string TeamPoints { get; set; }
        public string UserTeamName { get; set; }
        public string LeaguePoints { get; set; }
        public string LeagueRank { get; set; }
        public string UserId { get; set; }
        public string TournamentId { get; set; }
        public string LeagueLeader { get; set; }
        public string LeagueCreationDate { get; set; }

    }

    public class LeagueTeamDetailResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<LeagueTeamDetailDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class LeagueTeamDetailDataResponse
    {
       
        public string TeamCurrentStanding { get; set; }
        public string TeamOldStanding { get; set; }
        public string LeagueName { get; set; }
        public string LeagueLeader { get; set; }
        public string TeamGlobalRank { get; set; }
        public string UserTeamName { get; set; }
        public string UserTeamId { get; set; }
        public string SubsLeft { get; set; }
        public string NitroLeft { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
        public string UserName { get; set; }
        public string UserId { get; set; }
        public string Status { get; set; }
        public string IsLeagueLeader { get; set; }
        public string FullName { get; set; }
        public string TotalPoints { get; set; }
        public string LeagueRank { get; set; }
        public string UserLeagueId { get; set; }
        public string OwnerTeam { get; set; }
    }
    public class CreateTeamResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<CreateTeamDataResponse> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class CreateTeamDataResponse
    {
        public string UserTeamId { get; set; }
        public string UserTeamName { get; set; }
        public string TournamentId { get; set; }
        public string UserId { get; set; }
        public string SubsLeft { get; set; }
        public string NitroLeft { get; set; }
        public string TotalPoints { get; set; }
        public string TeamCompositionId { get; set; }
        public string TeamRank { get; set; }
        public string CreatedDate { get; set; }
        public string ModifiedDate { get; set; }
        public string PainKillerLeft { get; set; }
        public string AutoPilotLeft { get; set; }
    }
}
