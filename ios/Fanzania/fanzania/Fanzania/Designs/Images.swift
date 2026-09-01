//
//  Images.swift
//  Fanzania
//
//  Created by Tathagata Dey on 24/04/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

extension UIImage {
    
    func increasingSort()->UIImage{
        return UIImage(named: "sort-up") ?? uparrow()
    }
    func decreasingSort()->UIImage {
        return UIImage(named: "sort-down") ?? uparrow()
    }
    func uparrow()->UIImage{
        return UIImage(named: "up-arrow")!
    }
    func downarrow()->UIImage {
        return UIImage(named: "down-arrow") ?? uparrow()
    }
    func iconFilter()->UIImage {
        return UIImage(named: "filter") ?? uparrow()
    }
    func iconFilterGreen()->UIImage {
        return UIImage(named: "Filter-Green") ?? uparrow()
    }
    func playerImagePlaceHolder()->UIImage {
        return UIImage(named: "player-dummy") ?? uparrow()
    }
        
}
let playerImagePlaceHolder = UIImage(named: "player-dummy")
let userProfilePlaceholder = UIImage(named: "ic_profile_placeholder")
