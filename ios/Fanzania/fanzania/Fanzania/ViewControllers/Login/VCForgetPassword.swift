//
//  VCForgetPasswordStep1.swift
//  Fanzania
//
//  Created by Tathagata Dey on 29/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCForgetPassword: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var tvEmail: UITextField!
    @IBOutlet weak var btnGetOTP: UIButton!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var labelLog: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        btnGetOTP.layer.cornerRadius = 15.0
        self.errorView.alpha = 0.0
        setupUI()
    }
    
    func setupUI(){
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.isTranslucent = true
        self.navigationController?.view.backgroundColor = .clear
        tvEmail.attributedPlaceholder = NSAttributedString(string: "Email Address",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.darkGray.cgColor
        if self.errorView.alpha != 0.0 {
            UIView.transition(with: errorView, duration: 0.2, options: .transitionCrossDissolve, animations: {
                self.errorView.alpha = 0.0
            })
        }
    }
    @IBAction func GetOTPTapped(_ sender: Any) {
        
        //get otp sms
        guard let email = tvEmail.text, email.validateEmail() else{
            showInputValidationError(textField: tvEmail, error: "Give a valid email")
            return
        }
        // get OTP and segue
        forgotPassword(email: email)
    }
    
    func showInputValidationError(textField: UIView, error: String){
        textField.layer.borderColor = UIColor.red.cgColor
        textField.shakeAnimation()
        labelLog.text = error
        self.errorView.alpha = 1.0
        
        let transition = CATransition()
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromBottom
        errorView.layer.add(transition, forKey: nil)
        self.view.addSubview(self.errorView)
        
    }
    
    func forgotPassword(email:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["Email" : email]
        Alamofire.request(URL_ForgotPassword,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : "", "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch")
                        return
                    }
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        let alert = UIAlertController(title: "Almost there. We have emailed your password", message: "Please try logging in with this password", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.navigationController?.popViewController(animated: true)
                        }))
                        self.present(alert, animated: true, completion: nil)
                        
                    }else{
                        let alert = UIAlertController(title: "Password Reset Incomplete", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.navigationController?.popViewController(animated: true)
                        }))
                        self.present(alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
}
