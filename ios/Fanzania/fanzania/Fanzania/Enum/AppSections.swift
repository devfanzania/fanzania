//
//  AppSections.swift
//  Fanzania
//
//  Created by Tathagata Dey on 23/06/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation

enum AppSections {
    case myTournament
    case myTeam
    case myLeague
    case liveScore
    case more
    
    var name:String {
        switch self {
        case .myTournament : return "Home"
        case .myTeam : return "Team"
        case .myLeague : return "League"
        case .liveScore : return "Live Score"
        case .more : return "More"
        }
    }

    var index:Int {
        
        switch self {
        case .myTournament : return 0
        case .myTeam : return 1
        case .myLeague : return 2
        case .liveScore : return 3
        case .more : return 4
        }
    }
}
