//
//  VCOTPMailVarification.swift
//  Fanzania
//
//  Created by Tathagata Dey on 13/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCOTPMailVarification: UIViewController{

    @IBOutlet weak var tf1: UITextField!
    @IBOutlet weak var tf2: UITextField!
    @IBOutlet weak var tf3: UITextField!
    @IBOutlet weak var tf4: UITextField!
    
    @IBOutlet weak var btnResend: UIButton!

    var OTPSent:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        print(OTPSent)
        
        btnResend.layer.cornerRadius = 15.0
        
        tf1.addTarget(self, action: #selector(self.textFieldDidChange(textField:)), for: .editingChanged)
        tf2.addTarget(self, action: #selector(self.textFieldDidChange(textField:)), for: .editingChanged)
        tf3.addTarget(self, action: #selector(self.textFieldDidChange(textField:)), for: .editingChanged)
        tf4.addTarget(self, action: #selector(self.textFieldDidChange(textField:)), for: .editingChanged)
//        tf1.delegate = self
//        tf2.delegate = self
//        tf3.delegate = self
//        tf4.delegate = self
//        tf5.delegate = self
//        tf6.delegate = self
        tf1.becomeFirstResponder()
        // Do any additional setup after loading the view.
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.isTranslucent = true
        self.navigationController?.view.backgroundColor = .clear
        tf1.layer.borderColor = UIColor.white.cgColor
        tf2.layer.borderColor = UIColor.white.cgColor
        tf3.layer.borderColor = UIColor.white.cgColor
        tf4.layer.borderColor = UIColor.white.cgColor
    }
    
    @IBAction func ActionResend(_ sender: UIButton) {
        executeResendActivationCode()
    }
    
    
    @objc func textFieldDidChange(textField : UITextField){
        
        let text = textField.text
        
        if text?.count == 2 {
            
            let t = String(text![text!.index(text!.startIndex, offsetBy: 1)])
            textField.text = String(text![text!.index(text!.startIndex, offsetBy: 0)])
            switch textField {
                
            case tf1:
                tf2.becomeFirstResponder()
                tf2.text = t
            case tf2:
                tf3.becomeFirstResponder()
                tf3.text = t
            case tf3:
                tf4.becomeFirstResponder()
                tf4.text = t
                OTPEntered()
            default:
                break
                
            }
        }else if text?.count == 0{
            switch textField {
                
            case tf1:
                break
            case tf2:
                tf1.becomeFirstResponder()
            case tf3:
                tf2.becomeFirstResponder()
            case tf4:
                tf3.becomeFirstResponder()
            default:
                break
                
        }
    }
    }
    
    
    func OTPEntered(){
        
        print("OTP Entered Called")
        let otpEntered = tf1.text!+tf2.text!+tf3.text!+tf4.text!
        let otpInt = otpEntered
        print(otpInt)
        if otpInt == OTPSent {
            executeMailVarified()
        }else{
            let alert = UIAlertController(title: "One Time Passsword Mistmatch", message: "Please enter correct OTP sent to your mail", preferredStyle: UIAlertController.Style.alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
        }
        
    }
    
    func executeMailVarified(){
        let postParams: NSDictionary = ["UserId" : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue)]
        let requestURL = URL_EmailVarifiedOnSignUp
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(requestURL, method: .post, parameters: postParams as? [String : Any], encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
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
                    print("data at mail varify \(jsonDictionary)")
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        print("User Mail Varification Complete")
                        UserDefaults.standard.set(true, forKey: UserDefaultData.StatusLogin.rawValue)
                        let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
                        appDelegateTemp?.window?.rootViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateInitialViewController()
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Login Unsuccessful", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }

    func executeResendActivationCode(){
        
        print("calling")
        let postParams: NSDictionary = ["UserId" : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                                        "Email" : UserDefaults.standard.integer(forKey: UserDefaultData.Email.rawValue)]
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_User_GetActivationCodeforResend, method: .post, parameters: postParams as? [String : Any], encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
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
                    print("data at mail varify \(jsonDictionary)")
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        guard let responseArray = jsonDictionary["data"] as? NSArray else {
                            print("No proper json Data format")
                            return
                        }
                        if let userData = responseArray[0] as? [String:Any] {
                            
                            self.OTPSent = userData["ActivationToken"] as? String
                            
                        }else{
                            return
                        }
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "OTP Varification Unsuccessful", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
}
