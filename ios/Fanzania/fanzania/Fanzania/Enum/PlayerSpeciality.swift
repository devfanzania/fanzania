//
//  File.swift
//  Fanzania
//
//  Created by Tathagata Dey on 24/04/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

enum PlayerSpeciality {
    case batsman
    case bowler
    case allrounder
    case wicketKeeper
    
    init(role:String?){
        switch role {
        case "batsman":
            self = .batsman
        case "allrounder":
            self = .allrounder
        case "bowler":
            self = .bowler
        default:
            self = .wicketKeeper
        }
    }
    
    var role:String {
        switch self {
        case .batsman : return "batsman"
        case .allrounder : return "allrounder"
        case .bowler : return "bowler"
        case .wicketKeeper : return "wicketkeeper"
        }
    }
    
    var image:UIImage {
        switch self {
        case .batsman : return UIImage(named: "bat")!
        case .allrounder : return UIImage(named: "bat-ball")!
        case .bowler : return UIImage(named: "ball")!
        case .wicketKeeper : return UIImage(named: "gloves")!
        }
    }
    
    var imgageGrey:UIImage {
        switch self {
        case .batsman : return UIImage(named: "ic_batsman_grey")!
        case .allrounder : return UIImage(named: "ic_allrounder_grey")!
        case .bowler : return UIImage(named: "ic_bowler_grey")!
        case .wicketKeeper : return UIImage(named: "ic_wicketkeeper_grey")!
        }
    }
    
    var index:Int {
        switch self {
        case .batsman : return 1
        case .wicketKeeper : return 2
        case .allrounder : return 3
        case .bowler : return 4
        }
    }
}
