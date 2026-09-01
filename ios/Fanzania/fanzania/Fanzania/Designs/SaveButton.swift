//
//  SaveButton.swift
//  Fanzania
//
//  Created by Writayan Das on 04/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

class SaveButton: UIButton {
    
    private var gradientLayer: CAGradientLayer?
    
    override open var isEnabled: Bool {
        didSet {
            if self.isEnabled {
                addGrdientLayer()
            }
            else {
                removeGradientLayer()
            }
            super.isEnabled = isEnabled
            self.layoutIfNeeded()
        }
    }
    
    override func layerWillDraw(_ layer: CALayer) {
        gradientLayer?.frame = self.bounds
        super.layerWillDraw(layer)
    }
    
    func addGrdientLayer() {
        self.backgroundColor = .clear
        gradientLayer = CAGradientLayer()
        gradientLayer?.frame = self.bounds
        gradientLayer?.colors = [UIColor.systemYellow.cgColor, UIColor.systemPink.cgColor]
        gradientLayer?.startPoint = CGPoint(x: 0, y: 0.5)
        gradientLayer?.endPoint = CGPoint(x: 1, y: 0.5)
        layer.insertSublayer(gradientLayer!, at: 0)
    }
    
    func removeGradientLayer() {
        gradientLayer?.removeFromSuperlayer()
        gradientLayer = nil
        self.backgroundColor = .lightGray
    }
}
