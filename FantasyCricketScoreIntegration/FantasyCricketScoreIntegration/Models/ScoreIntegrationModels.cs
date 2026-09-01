using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace FantasyCricketScoreIntegration.Models
{
    public class FantasySummaryAPIDetails
    {
        public int TournamentId { get; set; }
        public int MatchId { get; set; }
        public int MatchNo { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string MatchType { get; set; }
        public string MatchStage { get; set; }
        public int APIDetailsId { get; set; }
        public string APIName { get; set; }
        public string APIKey { get; set; }
        public string UniqueId { get; set; }
        public string Inning1BattingTeam { get; set; }
        public string Inning2BattingTeam { get; set; }
        public string MatchStatus { get; set; }
        public string TossWinner { get; set; }
        public string MatchCity { get; set; }
    }

    public class GetPlayerIdModel
    {
        public int APIPId { get; set; }
        public int RapidPlayerId { get; set; }
        public string PlayerName { get; set; }
    }

    public class MatchDetails4PlayerStatsModel
    {
        public int TournamentId { get; set; }
        public int MatchId { get; set; }
        public string TournamentName { get; set; }
        
    }

    public class FetchPlayerDetails4StatsModel
    {
        public int APIPId { get; set; }
        public int PlayerRank { get; set; }
        public int PlayerPoints { get; set; }
        public int PlayerRuns { get; set; }
        public int PlayerWickets { get; set; }
        public int TotalPoints { get; set; }
        public int PlayerValue { get; set; }
        public int PlayerValueRank { get; set; }
        public int PlayerId { get; set; }
        public string TeamShortName { get; set; }
        public string PlayerStats { get; set; }

    }

    public class RapidFieldingModel
    {
        public int APIPId { get; set; }
        public int RapidPlayerId { get; set; }
        public int InningId { get; set; }
        public int Runout { get; set; }
        public int Stamped { get; set; }
        public int Catch { get; set; }
    }

    public class MatchDetails4PointCalculation
    {
        public int TournamentId { get; set; }
        public int MatchId { get; set; }
        public string MatchStage { get; set; }
    }

    public class FantasySummary
    {
        public DateTime? dateTimeGMT { get; set; }
        public Data data { get; set; }
        public string type { get; set; }
        public bool cache3 { get; set; }
        public int creditsLeft { get; set; }
        public string v { get; set; }
        public int ttl { get; set; }
        public Provider provider { get; set; }
    }

    public class Data
    {
        public Fielding[] fielding { get; set; }
        public Bowling[] bowling { get; set; }
        public Batting[] batting { get; set; }
        public Team[] team { get; set; }

        [JsonProperty("man-of-the-match")]
        public ManOfTheMatch manofthematch { get; set; }
        public string toss_winner_team { get; set; }
        public string winner_team { get; set; }
        public bool? matchStarted { get; set; }
    }

    public class ManOfTheMatch
    {
        public string name { get; set; }
        public string pid { get; set; }
    }

    public class Fielding
    {
        public string title { get; set; }
        public ScoreF[] scores { get; set; }
    }

    public class ScoreF
    {
        public string name { get; set; }
        public int? runout { get; set; }
        public int? stumped { get; set; }
        public int? bowled { get; set; }
        public int? lbw { get; set; }

        [JsonProperty("catch")]
        public int? _catch { get; set; }
        public string pid { get; set; }
    }

    public class Bowling
    {
        public ScoreBL[] scores { get; set; }
        public string title { get; set; }
    }

    public class ScoreBL
    {
        [JsonProperty("6s")]
        public int? _6s { get; set; }

        [JsonProperty("4s")]
        public int? _4s { get; set; }

        [JsonProperty("0s")]
        public int? _0s { get; set; }
        public string Econ { get; set; }
        public string W { get; set; }
        public string R { get; set; }
        public string M { get; set; }
        public string O { get; set; }
        public string bowler { get; set; }
        public string pid { get; set; }
    }

    public class Batting
    {
        public ScoreBT[] scores { get; set; }
        public string title { get; set; }
    }

    public class ScoreBT
    {
        [JsonProperty("dismissal-by")]
        public object dismissalby { get; set; }
        public string dismissal { get; set; }
        public double? SR { get; set; }

        [JsonProperty("6s")]
        public int? _6s { get; set; }

        [JsonProperty("4s")]
        public int? _4s { get; set; }
        public int? B { get; set; }
        public int? R { get; set; }

        [JsonProperty("dismissal-info")]
        public string dismissalinfo { get; set; }
        public string batsman { get; set; }
        public string pid { get; set; }
        public string detail { get; set; }
    }

    public class Dismissalby
    {
        public string name { get; set; }
        public string pid { get; set; }
    }

    public class Team
    {
        public Player[] players { get; set; }
        public string name { get; set; }
    }

    public class Player
    {
        public string name { get; set; }
        public string pid { get; set; }
    }

    public class Provider
    {
        public string source { get; set; }
        public string url { get; set; }
        public DateTime? pubDate { get; set; }
    }

    public class ScoreObject
    {
        public string stat { get; set; }
        public string score { get; set; }
        public string description { get; set; }
        public bool matchStarted { get; set; }
        [JsonProperty("team-1")]
        public string team1 { get; set; }
        [JsonProperty("team-2")]
        public string team2 { get; set; }
        public string v { get; set; }
        public int ttl { get; set; }
        public Provider provider { get; set; }
        public int creditsLeft { get; set; }
    }

    public class PlayerStatsModel
    {
        [JsonProperty("tournamentId")]
        public int TournamentId { get; set; }
        [JsonProperty("tournamentName")]
        public string TournamentName { get; set; }
        [JsonProperty("playerName")]
        public string PlayerName { get; set; }
        [JsonProperty("teamShortName")]
        public string TeamShortName { get; set; }
        [JsonProperty("PlayerSpeciality")]
        public string PlayerSpeciality { get; set; }
        [JsonProperty("playerValue")]
        public int PlayerValue { get; set; }
        [JsonProperty("playerTotalPoints")]
        public int PlayerTotalPoints { get; set; }
        [JsonProperty("playerRank")]
        public int PlayerRank { get; set; }
        [JsonProperty("selectedBy")]
        public int SelectedBy { get; set; }
        [JsonProperty("playerPoints1")]
        public string PlayerPoints1 { get; set; }
        [JsonProperty("playerPoints2")]
        public string PlayerPoints2 { get; set; }
        [JsonProperty("playerPoints3")]
        public string PlayerPoints3 { get; set; }
        [JsonProperty("playerPoints4")]
        public string PlayerPoints4 { get; set; }
        [JsonProperty("playerPoints5")]
        public string PlayerPoints5 { get; set; }
        [JsonProperty("playerRuns1")]
        public string PlayerRuns1 { get; set; }
        [JsonProperty("playerRuns2")]
        public string PlayerRuns2 { get; set; }
        [JsonProperty("playerRuns3")]
        public string PlayerRuns3 { get; set; }
        [JsonProperty("playerRuns4")]
        public string PlayerRuns4 { get; set; }
        [JsonProperty("playerRuns5")]
        public string PlayerRuns5 { get; set; }
        [JsonProperty("playerWickets1")]
        public string PlayerWickets1 { get; set; }
        [JsonProperty("playerWickets2")]
        public string PlayerWickets2 { get; set; }
        [JsonProperty("playerWickets3")]
        public string PlayerWickets3 { get; set; }
        [JsonProperty("playerWickets4")]
        public string PlayerWickets4 { get; set; }
        [JsonProperty("playerWickets5")]
        public string PlayerWickets5 { get; set; }
        [JsonProperty("playerValueRank")]
        public int PlayerValueRank { get; set; }
        [JsonProperty("totalPlayers")]
        public int TotalPlayers { get; set; }
        [JsonProperty("imageURL")]
        public string ImageURL { get; set; }
        public int MatchCounter { get; set; }
        public int LastMatchId { get; set; }
    }

}

namespace FantasyCricketScoreIntegration.RapidMatchSummary.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Series
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string shieldImageUrl { get; set; }
    }

    public class Venue
    {
        public string name { get; set; }
        public string location { get; set; }
        public string latitude { get; set; }
        public string longitude { get; set; }
        public string antisocialPhoneNumber { get; set; }
    }

    public class HomeTeam
    {
        public bool? isBatting { get; set; }
        public int? id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class AwayTeam
    {
        public bool? isBatting { get; set; }
        public int? id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class Scores
    {
        public string homeScore { get; set; }
        public string homeOvers { get; set; }
        public string awayScore { get; set; }
        public string awayOvers { get; set; }
    }

    public class Match
    {
        public int? id { get; set; }
        public int? matchTypeId { get; set; }
        public string statisticsProvider { get; set; }
        public Series series { get; set; }
        public string name { get; set; }
        public string status { get; set; }
        public Venue venue { get; set; }
        public HomeTeam homeTeam { get; set; }
        public AwayTeam awayTeam { get; set; }
        public string currentMatchState { get; set; }
        public bool? isMultiDay { get; set; }
        public string matchSummaryText { get; set; }
        public Scores scores { get; set; }
        public List<object> liveStreams { get; set; }
        public bool? isLive { get; set; }
        public int? currentInningId { get; set; }
        public bool isMatchDrawn { get; set; }
        public bool? isMatchAbandoned { get; set; }
        public int? winningTeamId { get; set; }
        public string startDateTime { get; set; }
        public string endDateTime { get; set; }
        public string localStartDate { get; set; }
        public string localStartTime { get; set; }
        public bool isWomensMatch { get; set; }
        public string cmsMatchType { get; set; }
        public string cmsMatchAssociatedType { get; set; }
        public string cmsMatchVenueStartDateTime { get; set; }
        public string cmsMatchVenueEndDateTime { get; set; }
        public string cmsMatchStartDate { get; set; }
        public string cmsMatchEndDate { get; set; }
        public string gamedayStatus { get; set; }
        public bool? isGamedayEnabled { get; set; }
        public bool? removeMatch { get; set; }
    }

    public class RapidMatchSummary
    {
        public Match match { get; set; }
        public int? status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace FantasyCricketScoreIntegration.RapidScoreDetails.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Series
    {
        public int id { get; set; }
        public string name { get; set; }
    }

    public class Meta
    {
        public int? matchTypeId { get; set; }
        public Series series { get; set; }
    }

    public class Team
    {
        public int id { get; set; }
        public string shortName { get; set; }
    }

    public class Batsman
    {
        public int id { get; set; }
        public string name { get; set; }
        public string runs { get; set; }
        public string balls { get; set; }
        public string strikeRate { get; set; }
        public string fours { get; set; }
        public string sixes { get; set; }
        public string howOut { get; set; }
        public string fallOfWicket { get; set; }
        public string fallOfWicketOver { get; set; }
        public int? fowOrder { get; set; }
        public string imageURL { get; set; }
    }

    public class Bowler
    {
        public int id { get; set; }
        public string name { get; set; }
        public string imageURL { get; set; }
        public string runsConceded { get; set; }
        public string maidens { get; set; }
        public string wickets { get; set; }
        public string overs { get; set; }
        public string wides { get; set; }
        public string noBalls { get; set; }
        public string economy { get; set; }
    }

    public class Innings
    {
        public int id { get; set; }
        public bool? isDeclared { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public Team team { get; set; }
        public List<Batsman> batsmen { get; set; }
        public List<Bowler> bowlers { get; set; }
        public string wicket { get; set; }
        public string run { get; set; }
        public string over { get; set; }
        public string extra { get; set; }
        public string bye { get; set; }
        public string legBye { get; set; }
        public string wide { get; set; }
        public string noBall { get; set; }
        public string runRate { get; set; }
        public string requiredRunRate { get; set; }
    }

    public class FullScorecard
    {
        public List<Innings> innings { get; set; }
    }

    public class MostRunsAward
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runs { get; set; }
        public string balls { get; set; }
        public string strikeRate { get; set; }
        public int? fowOrder { get; set; }
    }

    public class MostWicketsAward
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runsConceded { get; set; }
        public string wickets { get; set; }
        public string overs { get; set; }
        public string economy { get; set; }
    }

    public class ManOfMatchBattingResult
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runs { get; set; }
        public string balls { get; set; }
        public string strikeRate { get; set; }
        public string fours { get; set; }
        public string sixes { get; set; }
        public string howOut { get; set; }
        public string fallOfWicket { get; set; }
        public string fallOfWicketOver { get; set; }
        public int? fowOrder { get; set; }
    }

    public class ManOfMatchBowlngResult
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runsConceded { get; set; }
        public string maidens { get; set; }
        public string wickets { get; set; }
        public string overs { get; set; }
        public string wides { get; set; }
        public string noBalls { get; set; }
        public string economy { get; set; }
    }

    public class MostRunsAwardPlayerResult
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runs { get; set; }
        public string balls { get; set; }
        public string strikeRate { get; set; }
        public string fours { get; set; }
        public string sixes { get; set; }
        public string howOut { get; set; }
        public string fallOfWicket { get; set; }
        public string fallOfWicketOver { get; set; }
        public int? fowOrder { get; set; }
    }

    public class MostWicketsAwardPlayerResult
    {
        public int? id { get; set; }
        public string name { get; set; }
        public string runsConceded { get; set; }
        public string maidens { get; set; }
        public string wickets { get; set; }
        public string overs { get; set; }
        public string wides { get; set; }
        public string noBalls { get; set; }
        public string economy { get; set; }
    }

    public class FullScorecardAwards
    {
        public MostRunsAward mostRunsAward { get; set; }
        public MostWicketsAward mostWicketsAward { get; set; }
        public int? manOfTheMatchId { get; set; }
        public string manOfTheMatchName { get; set; }
        public List<ManOfMatchBattingResult> manOfMatchBattingResults { get; set; }
        public List<ManOfMatchBowlngResult> manOfMatchBowlngResults { get; set; }
        public List<MostRunsAwardPlayerResult> mostRunsAwardPlayerResults { get; set; }
        public List<MostWicketsAwardPlayerResult> mostWicketsAwardPlayerResults { get; set; }
    }

    public class RapidScoreDetails
    {
        public Meta meta { get; set; }
        public FullScorecard fullScorecard { get; set; }
        public FullScorecardAwards fullScorecardAwards { get; set; }
        public int? status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace FantasyCricketScoreIntegration.RapidMatchPlayers.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Team
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class Player
    {
        public int playerId { get; set; }
        public string fullName { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string imageURL { get; set; }
        public string battingStyle { get; set; }
        public string bowlingStyle { get; set; }
        public string playerType { get; set; }
        public string dob { get; set; }
        public string testDebutDate { get; set; }
        public string odiDebutDate { get; set; }
        public string t20DebutDate { get; set; }
        public string bio { get; set; }
        public string didYouKnow { get; set; }
        public string height { get; set; }
    }

    public class HomeTeam
    {
        public string teamName { get; set; }
        public string teamShortName { get; set; }
        public Team team { get; set; }
        public List<Player> players { get; set; }
    }

    public class Team2
    {
        public int id { get; set; }
        public string name { get; set; }
        public string shortName { get; set; }
        public string logoUrl { get; set; }
    }

    public class Player2
    {
        public int playerId { get; set; }
        public string fullName { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string imageURL { get; set; }
        public string battingStyle { get; set; }
        public string bowlingStyle { get; set; }
        public string playerType { get; set; }
        public DateTime? dob { get; set; }
        public DateTime? testDebutDate { get; set; }
        public DateTime? odiDebutDate { get; set; }
        public DateTime? t20DebutDate { get; set; }
        public string bio { get; set; }
        public string didYouKnow { get; set; }
        public string height { get; set; }
    }

    public class AwayTeam
    {
        public string teamName { get; set; }
        public string teamShortName { get; set; }
        public Team2 team { get; set; }
        public List<Player2> players { get; set; }
    }

    public class PlayersInMatch
    {
        public HomeTeam homeTeam { get; set; }
        public AwayTeam awayTeam { get; set; }
    }

    public class RapidMatchPlayers
    {
        public PlayersInMatch playersInMatch { get; set; }
        public int? status { get; set; }
        public string poweredBy { get; set; }
    }


}

namespace FantasyCricketScoreIntegration.WeatherData.Models
{
    // Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse); 
    public class Coord
    {
        public double lon { get; set; }
        public double lat { get; set; }
    }

    public class Weather
    {
        public int id { get; set; }
        public string main { get; set; }
        public string description { get; set; }
        public string icon { get; set; }
    }

    public class Main
    {
        public double temp { get; set; }
        public double feels_like { get; set; }
        public double temp_min { get; set; }
        public double temp_max { get; set; }
        public int pressure { get; set; }
        public int humidity { get; set; }
    }

    public class Wind
    {
        public double speed { get; set; }
        public int deg { get; set; }
    }

    public class Clouds
    {
        public int all { get; set; }
    }

    public class Sys
    {
        public int type { get; set; }
        public int id { get; set; }
        public double message { get; set; }
        public string country { get; set; }
        public int sunrise { get; set; }
        public int sunset { get; set; }
    }

    public class WeatherData
    {
        public Coord coord { get; set; }
        public List<Weather> weather { get; set; }
        public string @base { get; set; }
        public Main main { get; set; }
        public int visibility { get; set; }
        public Wind wind { get; set; }
        public Clouds clouds { get; set; }
        public int dt { get; set; }
        public Sys sys { get; set; }
        public int timezone { get; set; }
        public int id { get; set; }
        public string name { get; set; }
        public int cod { get; set; }
    }


}