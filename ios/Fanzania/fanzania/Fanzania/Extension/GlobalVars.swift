//
//  GlobalVars.swift
//  Fanzania
//
//  Created by Tathagata Dey on 05/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class GlobalVars: NSObject {

    static let shared  = GlobalVars()
    
    // MARK: - Global variables
    var navBarHeight: CGFloat {
        if #available(iOS 11.0, *) {
            let window = UIApplication.shared.keyWindow
            return 64.0 + (UIScreen.main.bounds.height >= 812 ? (window?.safeAreaInsets.top)! - 20.0 : 0.0)
        }
        return 64.0
    }
    var isGreaterThanOrEqualiOS11: Bool {
        if #available(iOS 11.0, *) {
            return true
        }
        return false
    }
    var isIPhoneX: Bool {
        if #available(iOS 11.0, *), !isIpad()  {
            return UIScreen.main.bounds.height >= 812 ? true: false
        }
        return false
    }
    var isIPhonePlus = UIScreen.main.bounds.height == 736
    let isIPhone5 = UIScreen.main.bounds.height == 568
    func isIpad() -> Bool {
        switch UIDevice.current.userInterfaceIdiom {
        case .phone: // It's an iPhone
            return false
        case .pad: // It's an iPad
            return true
        case .unspecified: // undefined
            return false
        default:
            return false
        }
    }
}
