//
//  paddingTextField.swift
//  HelpDesk
//
//  Created by ICA-IT-IOS-01 on 30/12/17.
//  Copyright © 2017 ICA-IT-IOS-01. All rights reserved.
//

import UIKit
class PaddingIconTextField: UITextField {

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    @IBInspectable var leftImage: UIImage?{
        didSet {
            updateView()
        }
        
    }
    @IBInspectable var leftPadding: CGFloat = 0 {
        didSet {
            updateView()
        }
    }
    @IBInspectable var rightPadding: CGFloat = 0 {
        didSet {
            updateView()
        }
    }
    @IBInspectable var imagewidth: CGFloat = 0 {
        didSet {
            updateView()
        }
    }
    @IBInspectable var imageheight: CGFloat = 0 {
        didSet {
            updateView()
        }
    }
    @IBInspectable var borderwidth: CGFloat = 0 {
        didSet {
            updateView()
        }
    }
    @IBInspectable var borderColor: CGColor = UIColor.white.cgColor {
        didSet {
            updateView()
        }
    }

    func updateView(){
        if let tempImage = leftImage {
            leftViewMode = .always
            
            let customeImageView = UIImageView(frame:CGRect(x:leftPadding,y:0,width:imagewidth,height:imageheight))
            customeImageView.image = tempImage
            
            let width = leftPadding + imagewidth + rightPadding
            let height = imageheight
            let view = UIView(frame:CGRect(x:10,y:0,width:width,height:height))
            view.addSubview(customeImageView)
            leftView = view
            
            layer.cornerRadius = 20.0
            layer.borderWidth = borderwidth
            layer.borderColor = borderColor
            
        }else{
            leftViewMode = .never
        }
    }

}
