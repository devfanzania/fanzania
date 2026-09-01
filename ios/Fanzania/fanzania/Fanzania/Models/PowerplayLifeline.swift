//
//  PowerplayLifeline.swift
//  Fanzania
//
//  Created by Writayan Das on 04/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import Foundation
 
/* For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

public class PowerplayLifelineModel {
    public var nitroUsed : Bool
    public var painKillerUsed : Bool
    public var autoPilotUsed : Bool
    public var nitroEnable : Bool
    public var painKillerEnable : Bool
    public var autoPilotEnable : Bool
    public var nitroPoints : Int
    public var painKillerPoints : Int
    public var autoPilotPoints : Int
    public var nitroUserTeamMatchPointId : Int
    public var painKillerUserTeamMatchPointId : Int
    public var autoPilotUserTeamMatchPointId : Int

    required public init?(dictionary: [String: Any]) {

        nitroUsed = dictionary["NitroUsed"] as! Bool
        painKillerUsed = dictionary["PainKillerUsed"] as! Bool
        autoPilotUsed = dictionary["AutoPilotUsed"] as! Bool
        nitroEnable = dictionary["NitroEnable"] as! Bool
        painKillerEnable = dictionary["PainKillerEnable"] as! Bool
        autoPilotEnable = dictionary["AutoPilotEnable"] as! Bool
        nitroPoints = dictionary["NitroPoints"] as! Int
        painKillerPoints = dictionary["PainKillerPoints"] as! Int
        autoPilotPoints = dictionary["AutoPilotPoints"] as! Int
        nitroUserTeamMatchPointId = dictionary["NitroUserTeamMatchPointId"] as! Int
        painKillerUserTeamMatchPointId = dictionary["PainKillerUserTeamMatchPointId"] as! Int
        autoPilotUserTeamMatchPointId = dictionary["AutoPilotUserTeamMatchPointId"] as! Int
    }
}
