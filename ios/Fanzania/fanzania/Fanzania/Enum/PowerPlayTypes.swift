//
//  PowerPlayTypes.swift
//  Fanzania
//
//  Created by Tathagata Dey on 11/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

enum PowerPlayTypes{
    case PainKiller
    case NitroBooster
    case AutoCaptain
    
    init(powerPlay:String){
        switch powerPlay {
        case "NITRO" :
            self = .NitroBooster
        case "PAINKILLER" :
            self = .PainKiller
        default :
            self = .AutoCaptain
        }
    }
    
    var image:UIImage {
        switch self {
        case .PainKiller : return UIImage(named: "Painkiller")!
        case .NitroBooster : return UIImage(named: "Nitro")!
        case .AutoCaptain : return UIImage(named: "Autocaptain")!
        }
    }
    var name: String{
        switch self {
        case .PainKiller : return "PainKiller"
        case .NitroBooster : return "Nitro"
        case .AutoCaptain : return "Ultra Captain"
        }
    }
}
