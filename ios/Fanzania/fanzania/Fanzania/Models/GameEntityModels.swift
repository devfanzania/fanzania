//
//  TournamentList.swift
//  Fanzania
//
//  Created by Tathagata Dey on 18/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

struct UserTournamentModel:Hashable {
    
    var name:String?
    var id:Int?
    var status:String?
    var stage:String?
    var StartDate:String?
    var EndDate:String?
    var UserTeamId:Int?
    var UserTeamName:String?
    var UserId:Int?
    var SubsLeft:Int?
    var NitroLeft:Int?
    var PainKillerLeft:Int?
    var AutoPilotLeft:Int?
    var TotalPoints:Int?
    var TeamCompositionId:Int?
    var TeamRank:Int?
    var CreatedDate:String?
    
    init(name:String, id:Int, status:String, stage:String) {
        self.name = name
        self.id = id
        self.status = status
        self.stage = stage
    }
    
    init(name:String,status:String) {
        self.name = name
        self.status = status
    }
    
    init(name:String, id:Int, stage:String, status:String, UserId:Int, UserTeamId:Int, UserTeamName:String, TeamCompositionId:Int?, TotalPoints:Int, TeamRank: Int, StartDate: String, EndDate:String){
        
        self.name = name
        self.id = id
        self.stage = stage
        self.status = status
        self.UserId = UserId
        self.UserTeamId = UserTeamId
        self.UserTeamName = UserTeamName
        self.TeamCompositionId = TeamCompositionId
        self.TotalPoints = TotalPoints
        self.TeamRank = TeamRank
        self.StartDate = StartDate
        self.EndDate = EndDate
    }
}

struct UpcommingTournamentModel:Hashable {
    
    var name:String?
    var id:Int?
    var stage:String?
    var status:String?
    var startDate:String?
    var endDate:String?
    var logo:String?
    var logoByte:Int?
    var image:String?
    
    init(name:String?, stage:String, status:String, id:Int, startDate:String, endDate:String, image:String?) {
        self.name = name
        self.id = id
        self.stage = stage
        self.status = status
        self.startDate = startDate
        self.endDate = endDate
        self.image = image
    }
}

struct MyLeaguesModel:Hashable {
    
    var TeamStanding:Int?
    var TeamPoints:Int?
    var TournamentId:Int?
    var UserTeamName:String?
    var UserId:Int?
    var LeagueLeaderId:Int?
    var LeagueLeaderName:String?
    var LeagueName:String?
    var LeagueId:Int?
    var LeaguePoints:Int?
    var LeagueRank:Int?
    var Status:String?
    var LeaguePin:String?
    var selectedStatus:Bool = false
    
    init(LeagueId:Int, LeagueName:String, LeaguePoints:Int, TeamStanding:Int, LeagueRank:Int?, TournamentId:Int, leaguePin: String?, LeagueLeaderId:Int?) {
        self.LeagueId = LeagueId
        self.LeagueName = LeagueName
        self.LeaguePoints = LeaguePoints
        self.LeagueRank = LeagueRank
        self.TeamStanding  = TeamStanding
        self.TournamentId = TournamentId
        self.LeaguePin = leaguePin
        self.LeagueLeaderId = LeagueLeaderId
    }
    init(LeagueRank:Int?, LeagueName:String, LeagueLeaderName:String?, LeaguePoints:Int?) {
        self.LeagueRank = LeagueRank
        self.LeagueName = LeagueName
        self.LeagueLeaderName = LeagueLeaderName
        self.LeaguePoints = LeaguePoints
    }
}

struct MyTeamsModel {
    
    var FullName:String = "-"
    var IsLeagueLeader:Bool
    var LeagueLeader:String?
    var status : String?
    var SubsLeft : Int?
    var TeamCurrentStanding : Int?
    var TeamGlobalRank : Int?
    var TeamOldStanding : Int?
    var TotalPoints: Int?
    var UserTeamId:Int?
    var UserTeamName:String?
    var UserLeagueId:Int?
    var UserId:Int?
    var LastMatchPoints:Int = 0
    var SupportedTeam: String?
    var LeagueRank: Int = 0
    
    init(FullName:String, IsLeagueLeader:Bool, LeagueLeader:String?, status : String?, SubsLeft : Int?, TeamCurrentStanding : Int?, TeamGlobalRank : Int?, TeamOldStanding : Int?, TotalPoints: Int?, UserTeamId:Int?, UserTeamName:String?, UserId:Int?, LeagueRank: Int?) {
        self.FullName = FullName
        self.IsLeagueLeader = IsLeagueLeader
        self.LeagueLeader = LeagueLeader
        self.status = status
        self.SubsLeft = SubsLeft
        self.TeamCurrentStanding = TeamCurrentStanding
        self.TeamGlobalRank = TeamGlobalRank
        self.TeamOldStanding = TeamOldStanding
        self.TotalPoints = TotalPoints
        self.UserTeamId = UserTeamId
        self.UserTeamName = UserTeamName
        self.UserId = UserId
        self.LeagueRank = LeagueRank ?? 0
    }
    
    init(FullName:String, IsLeagueLeader:Bool, LeagueLeader:String?, status : String?, SubsLeft : Int?, TeamCurrentStanding : Int?, TeamGlobalRank : Int?, TeamOldStanding : Int?, TotalPoints: Int?, UserTeamId:Int?, UserTeamName:String?, UserId:Int?, UserLeagueId:Int?, LastMatchPoints: Int, SupportedTeam: String?, LeagueRank: Int?) {
        self.FullName = FullName
        self.IsLeagueLeader = IsLeagueLeader
        self.LeagueLeader = LeagueLeader
        self.status = status
        self.SubsLeft = SubsLeft
        self.TeamCurrentStanding = TeamCurrentStanding
        self.TeamGlobalRank = TeamGlobalRank
        self.TeamOldStanding = TeamOldStanding
        self.TotalPoints = TotalPoints
        self.UserTeamId = UserTeamId
        self.UserTeamName = UserTeamName
        self.UserId = UserId
        self.UserLeagueId = UserLeagueId
        self.LastMatchPoints = LastMatchPoints
        self.SupportedTeam = SupportedTeam
        self.LeagueRank = LeagueRank ?? 0
    }
}

struct MatchModel {
    
    var MatchDate:String?
    var MatchStatus:String?
    var Team1:String?
    var Team2:String?
    var Team1ShortName:String?
    var Team2ShortName:String?
    var TournamentId:Int?
    var MatchId:Int?
    var MatchNo:Int?
    var Winner:String?
    var venue:String?
    var weather: String?
    var BattingTeam: String?
    
    var weatherIcon: UIImage {
        guard let weather = weather,
              let weatherImage = UIImage(named: weather) else {
            return UIImage()
        }
        return weatherImage
    }
    
    init(MatchId:Int, Team1:String, MatchNo: Int, Team2:String, MatchStatus:String, MatchDate:String, venue:String?, weather: String?, BattingTeam: String?){
        self.MatchId = MatchId
        self.MatchNo = MatchNo
        self.MatchStatus = MatchStatus
        self.Team1 = Team1
        self.Team2 = Team2
        self.MatchDate = MatchDate
        self.venue = venue
        self.weather = weather
        self.BattingTeam = BattingTeam
    }
    
    init(MatchId:Int, Team1:String, Team2:String, MatchStatus:String, MatchDate:String, Team1ShortName:String?, Team2ShortName:String?, BattingTeam: String) {
        self.MatchId = MatchId
        self.MatchStatus = MatchStatus
        self.Team1 = Team1
        self.Team2 = Team2
        self.Team1ShortName = Team1ShortName
        self.Team2ShortName = Team2ShortName
        self.MatchDate = MatchDate
        self.BattingTeam = BattingTeam
    }
}

struct MyPointHistoryModel {
    
    var tournameName:String?
    var teamName:String?
    var totalPoints:Int?
    
    init(tournameName:String, teamName:String, totalPoints:Int) {
        self.tournameName = tournameName
        self.teamName = teamName
        self.totalPoints = totalPoints
    }
}
