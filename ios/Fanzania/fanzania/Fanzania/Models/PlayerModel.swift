//
//  PlayerModel.swift
//  Fanzania
//
//  Created by Tathagata Dey on 26/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

struct PlayerInfo:Hashable, Equatable {
    var rank:Int?
    var name:String?
    var shortName:String?
    var id:Int?
    var type:String?
    var speciality:PlayerSpeciality
    var value:Int?
    var participationTeamId:Int?
    var participationTeamName:String?
    var totalPoints: Int?
    var teamDescription:String?
    var isPlayerSelected:Bool = false
    var playerImageName:String?
    var teamShortName:String?
    var isCaptain:Bool = false
    var isViceCaptain:Bool = false
    var isPlaying: Bool?
    var WinnerPrediction: String?
    
    var isPlayingIndicator: UIImage? {
        guard let isPlaying = isPlaying,
              isPlaying
        else {
            return nil
        }
        guard let indicatorAsset = UIImage(named: "ic_green_dot") else {
            if #available(iOS 13.0, *) {
                 return UIImage().withTintColor(.green, renderingMode: .alwaysTemplate)
            } else {
                return UIImage()
            }
        }
        return indicatorAsset
    }
    
    static func == (lhs:PlayerInfo, rhs:PlayerInfo) -> Bool {
        return lhs.id == rhs.id
    }
    
    init(name:String, id:Int, type:String, speciality:String, value:Int, participationTeamName:String, participationTeamId:Int, totalPoints: Int, isPlayerSelected:Bool, shortName:String?, playerImageName:String?, teamShortName:String?, isPlaying: Bool?, WinnerPrediction: String?) {
        self.name = name
        self.id = id
        self.speciality = PlayerSpeciality(role: speciality)
        self.value = value
        self.participationTeamName = participationTeamName
        self.totalPoints = totalPoints
        self.isPlayerSelected = isPlayerSelected
        self.participationTeamId = participationTeamId
        self.type = type
        self.shortName = shortName
        self.playerImageName = playerImageName
        self.teamShortName = teamShortName
        self.isPlaying = isPlaying
        self.WinnerPrediction = WinnerPrediction
    }
    init(name:String, id:Int, type:String, speciality:String, participationTeamName:String, participationTeamId:Int, isPlayerSelected:Bool) {
        self.name = name
        self.id = id
        self.speciality = PlayerSpeciality(role: speciality)
        self.participationTeamName = participationTeamName
        self.isPlayerSelected = isPlayerSelected
        self.participationTeamId = participationTeamId
        self.type = type
    }
    init(rank:Int?, name:String, id:Int, type:String, speciality:String, participationTeamName:String, totalPoints: Int) {
        self.rank = rank
        self.name = name
        self.id = id
        self.speciality = PlayerSpeciality(role: speciality)
        self.participationTeamName = participationTeamName
        self.totalPoints = totalPoints
        self.type = type
    }
}
