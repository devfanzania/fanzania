//
//  TeamComparisonTableItemModel.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import Foundation
 
/* For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

public class TeamComparisonTableItemModel {
    public var tournamentId : Int
    public var matchId : Int
    public var myPlayerId : Int
    public var myPlayerName : String
    public var myPlayerType : String
    public var myPlayerSpeciality : String
    public var myTotalPoints : Int
    public var myPlayerSelected : Bool
    public var myCapt : Bool
    public var myVCapt : Bool
    public var myTeamName : String
    public var myPowerPlay : String
    public var otherPlayerId : Int
    public var otherPlayerName : String
    public var otherPlayerType : String
    public var otherPlayerSpeciality : String
    public var otherTotalPoints : Int
    public var otherPlayerSelected : Bool
    public var otherCapt : Bool
    public var otherVCapt : Bool
    public var otherTeamName : String
    public var otherPowerPlay : String


/**
    Constructs the object based on the given dictionary.
    
    Sample usage:
    let json4Swift_Base = Json4Swift_Base(someDictionaryFromJSON)

    - parameter dictionary:  NSDictionary from JSON.

    - returns: Json4Swift_Base Instance.
*/
    required public init?(dictionary: [String: Any]) {

        tournamentId = dictionary["TournamentId"] as! Int
        matchId = dictionary["MatchId"] as! Int
        myPlayerId = dictionary["MyPlayerId"] as? Int ?? -1
        myPlayerName = dictionary["MyPlayerName"] as? String ?? ""
        myPlayerType = dictionary["MyPlayerType"] as? String ?? ""
        myPlayerSpeciality = dictionary["MyPlayerSpeciality"] as? String ?? ""
        myTotalPoints = dictionary["MyTotalPoints"] as? Int ?? 0
        myPlayerSelected = dictionary["MyPlayerSelected"] as? Bool ?? false
        myCapt = dictionary["MyCapt"] as? Bool ?? false
        myVCapt = dictionary["MyVCapt"] as? Bool ?? false
        myTeamName = dictionary["MyTeamName"] as? String ?? ""
        myPowerPlay = dictionary["MyPowerPlay"] as? String ?? ""
        otherPlayerId = dictionary["OtherPlayerId"] as? Int ?? -1
        otherPlayerName = dictionary["OtherPlayerName"] as? String ?? ""
        otherPlayerType = dictionary["OtherPlayerType"] as? String ?? ""
        otherPlayerSpeciality = dictionary["OtherPlayerSpeciality"] as? String ?? ""
        otherTotalPoints = dictionary["OtherTotalPoints"] as? Int ?? 0
        otherPlayerSelected = dictionary["OtherPlayerSelected"] as? Bool ?? false
        otherCapt = dictionary["OtherCapt"] as? Bool ?? false
        otherVCapt = dictionary["OtherVCapt"] as? Bool ?? false
        otherTeamName = dictionary["OtherTeamName"] as? String ?? ""
        otherPowerPlay = dictionary["OtherPowerPlay"] as? String ?? ""
    }
}

