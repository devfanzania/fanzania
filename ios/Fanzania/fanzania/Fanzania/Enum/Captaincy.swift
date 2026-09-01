//
//  Captaincy.swift
//  Fanzania
//
//  Created by Tathagata Dey on 11/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

enum Captaincy {
    case Captain
    case ViceCaptain
    
    var image:UIImage {
        switch self {
        case .Captain:
            return UIImage(named: "ic_c")!
        default:
            return UIImage(named: "ic_vc")!
        }
    }
}
