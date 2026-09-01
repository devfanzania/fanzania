//
//  Shapes.swift
//  Fanzania
//
//  Created by Tathagata Dey on 28/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

public extension UITextField {
    
    public func whiteBorderRoundedCornerFrame(){
        self.layer.cornerRadius = 10.0
        self.layer.borderWidth = 2.0
        self.layer.borderColor = UIColor.white.cgColor
    }
    
    //----------------------------------------------
    
    public func roundedCorner(withBGColor bgColor: UIColor = .white){
        self.layer.cornerRadius = 15.0
        self.layer.backgroundColor = bgColor.cgColor
        self.layer.borderWidth = 1.0
        self.layer.borderColor = UIColor.darkGray.cgColor
    }
    
    func setLeftPaddingPoints(_ amount:CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
    func setRightPaddingPoints(_ amount:CGFloat) {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.rightView = paddingView
        self.rightViewMode = .always
    }
}

public extension UIButton {
    
    func createGradientLayer() {
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = self.bounds
        gradientLayer.cornerRadius = 15.0
        gradientLayer.colors = [colorGradientTop.cgColor, colorGradientMid.cgColor, colorGradientBottom.cgColor]
        self.layer.insertSublayer(gradientLayer, below: self.imageView?.layer)
    }
    func createDisableButtonGradientLayer() {
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = self.bounds
        gradientLayer.cornerRadius = 15.0
        gradientLayer.colors = [colorAppButtonDisableGrey.cgColor, UIColor.gray.cgColor]
        self.layer.insertSublayer(gradientLayer, below: self.imageView?.layer)
    }}

public extension UIView {
    
    func curvedCornerButtonView() {
        layer.cornerRadius = 20.0
    }
    
    func curvedCornerBorderButtonView(){
        curvedCornerButtonView()
        self.layer.borderWidth = 2.0
        self.layer.borderColor = UIColor.white.cgColor
    }
    
    //---------------------------------
    
    func setAppGradientLayer() {
        let gradientLayer = CAGradientLayer()
        gradientLayer.cornerRadius = 10.0
        gradientLayer.frame = self.bounds
        gradientLayer.colors = [UIColor.colorCrimson().cgColor, UIColor.colorOrange().cgColor]
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.5)
        gradientLayer.endPoint = CGPoint(x: 1.0, y: 0.5)
        self.layer.insertSublayer(gradientLayer, at: 0)
    }
    
    func setCircularLayer() {
        self.layer.cornerRadius = min(self.frame.size.height, self.frame.size.width) / 2.0
        self.clipsToBounds = true
    }
    
    func circleViewCaptainFieldIcon(){
        setCircularLayer()
        self.layer.backgroundColor = UIColor.red.cgColor
    }
    
    func makeCircleWithBorder(){
        setCircularLayer()
        self.layer.borderWidth = 1.0
        self.layer.borderColor = UIColor.darkGray.cgColor
    }
    
    public func setCurvedCornerBordered(borderColor: UIColor = .black){
        self.layer.cornerRadius = 15.0
        self.layer.backgroundColor = UIColor.white.cgColor
        self.layer.borderWidth = 1.0
        self.layer.borderColor = borderColor.cgColor
    }
    
    public func setCurvedCornerBorderedForLivePlayer() {
        self.layer.cornerRadius = 15.0
        self.layer.backgroundColor = UIColor.white.cgColor
        self.layer.borderWidth = 2.0
        self.layer.borderColor = UIColor(red: 51/255, green: 84/255, blue: 25/255, alpha: 1.0).cgColor
    }
    
    
    public func headerListCurvedCornerView(){
        self.layer.cornerRadius = 5.0
        self.layer.backgroundColor = UIColor.white.cgColor
        self.layer.borderWidth = 1.0
        self.layer.borderColor = UIColor.darkGray.cgColor
    }
    
    public func roundedCornerCollectionViewCell(){
        self.layer.cornerRadius = 10.0
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 2.0, height: 3.0)
        self.layer.masksToBounds = false
        self.clipsToBounds = false
        self.layer.shadowRadius = 2.0
        self.layer.shadowOpacity = 0.5
    }}
