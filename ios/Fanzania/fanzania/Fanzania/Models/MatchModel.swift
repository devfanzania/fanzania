//
//  MatchModel.swift
//  Fanzania
//
//  Created by Tathagata Dey on 19/04/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation

struct LiveMatchModel {
    
    var team1:String?
    var team1ShortName:String?
    var team1ImageName:String?
    var team2:String?
    var team2ShortName:String?
    var team2ImageName:String?
    var matchStatus:String?
    var matchPoints:Int?
    var matchTotal:Int?
    var id:Int?
    var MatchNo: Int?
    
    init(id:Int?, team1:String?, team2:String?, matchStatus:String?, matchPoints:Int?, matchTotal:Int?, team1ShortName:String?, team2ShortName:String?, team1ImageName:String?, team2ImageName:String?, MatchNo: Int?) {
        self.id = id
        self.team1 = team1
        self.team2 = team2
        self.matchPoints = matchPoints
        self.matchStatus = matchStatus
        self.matchTotal = matchTotal
        self.team1ShortName = team1ShortName
        self.team2ShortName = team2ShortName
        self.team1ImageName = team1ImageName
        self.team2ImageName = team2ImageName
        self.MatchNo = MatchNo
    }
    
}
