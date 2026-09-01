//
//  TeamRuleValidation.swift
//  Fanzania
//
//  Created by Tathagata Dey on 05/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

struct PlayerSelectionValidationRule {

//    case batsmanCount
//    case bowlerCount
//    case allrounderCount
//    case keeperCount
//    case playerCount
    
    static var Wktkeeper = 1
    static var AllrounderMAX = 3
    static var AllrounderMIN = 1
    static var BatsmanMAX = 5
    static var BatsmanMIN = 3
    static var BowlerMAX = 5
    static var BowlerMIN = 3
    static var SameTeamPlayer = 6
    static var teamSize = 11
    static var TotalBudget = 1000
    static var MaxOverseesPlayerCount = 5
    static var TransferAllowedFor_INPROGRESS_NewTeam = 11
    static var transferAllowedTotal = 40

}
