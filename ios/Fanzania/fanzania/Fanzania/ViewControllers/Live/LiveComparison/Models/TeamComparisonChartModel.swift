//
//  TeamComparisonChartModel.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import Foundation
 
/* For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

public class TeamComparisonChartModel {
    public var matchId: Int
    public var matchNo: Int
    public var matchStatus: String
    public var myMatchTotalPoints: Int
    public var otherMatchTotalPoints: Int


/**
    Constructs the object based on the given dictionary.
    
    Sample usage:
    let json4Swift_Base = Json4Swift_Base(someDictionaryFromJSON)

    - parameter dictionary:  NSDictionary from JSON.

    - returns: Json4Swift_Base Instance.
*/
    required public init(dictionary: [String: Any]) {

        matchId = dictionary["MatchId"] as! Int
        matchNo = dictionary["MatchNo"] as! Int
        matchStatus = dictionary["MatchStatus"] as! String
        myMatchTotalPoints = dictionary["MyMatchTotalPoints"] as! Int
        otherMatchTotalPoints = dictionary["OtherMatchTotalPoints"] as! Int
    }
}

