//
//  Animation.swift
//  Fanzania
//
//  Created by Tathagata Dey on 10/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

extension UIView {
    func shakeAnimation() {
        let animation = CABasicAnimation(keyPath: "position")
        animation.duration = 0.05
        animation.repeatCount = 5
        animation.autoreverses = true
        animation.fromValue = CGPoint(x: self.center.x - 4.0, y: self.center.y)
        animation.toValue = CGPoint(x: self.center.x + 4.0, y: self.center.y)
        layer.add(animation, forKey: "position")
    }
}

extension UIViewController {
    func slideDownAppearAnimation(view: UIView) {
        
    }
}
