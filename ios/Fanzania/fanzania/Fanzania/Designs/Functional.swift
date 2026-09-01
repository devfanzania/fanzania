//
//  Functional.swift
//  Fanzania
//
//  Created by Tathagata Dey on 10/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    func setTitle(_ title: String ) {
        let titleLbl = UILabel()
        titleLbl.text = title
        titleLbl.textColor = UIColor.white
        titleLbl.font = UIFont.systemFont(ofSize: 15.0, weight: .bold)
        let imageView = UIImageView(image: UIImage(named: "ic_logo_very_small"))
        let titleView = UIStackView(arrangedSubviews: [imageView, titleLbl])
        titleView.axis = .horizontal
        titleView.spacing = 10.0
        navigationItem.titleView = titleView
    }
}

extension UITextField {

    public func addPaddingRight(_ padding: CGFloat) {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: padding, height: frame.height))
        rightView = paddingView
        rightViewMode = .always
    }
    
    public func addPaddingRightIcon(_ button: UIButton, padding: CGFloat) {
        
        button.frame = CGRect(x:0, y:0, width: button.frame.width, height: button.frame.height)
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: padding)
        rightView = button
        rightViewMode = .always
    }

}

extension UIViewController {
    class func displaySpinner(onView : UIView) -> UIView {
        let spinnerView = UIView.init(frame: onView.bounds)
        spinnerView.backgroundColor = UIColor.init(red: 0.8, green: 0.8, blue: 0.8, alpha: 0.5)
        let ai = UIActivityIndicatorView.init(style: .whiteLarge)
        ai.startAnimating()
        ai.center = spinnerView.center
        
        DispatchQueue.main.async {
            spinnerView.addSubview(ai)
            onView.addSubview(spinnerView)
            UIApplication.shared.beginIgnoringInteractionEvents()
        }
        return spinnerView
    }
    
    class func removeSpinner(spinner :UIView) {
        DispatchQueue.main.async {
            spinner.removeFromSuperview()
            UIApplication.shared.endIgnoringInteractionEvents()
        }
    }
}

