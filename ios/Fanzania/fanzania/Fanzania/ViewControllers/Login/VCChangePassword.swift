//
//  VCChangePassword.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 31/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCChangePassword: UIViewController, UITextFieldDelegate {

    @IBOutlet var labelErrorLog: UILabel!
    @IBOutlet var errorView: UIView!
    @IBOutlet var labelNewPassword: UITextField!
    @IBOutlet var labelConfirmPassword: UITextField!
    @IBOutlet var btnSubmit: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        initialise()
        // Do any additional setup after loading the view.
    }
    
    func initialise() {
        self.errorView.alpha = 0.0
        labelNewPassword.roundedCorner()
        labelNewPassword.setLeftPaddingPoints(10.0)
        labelConfirmPassword.roundedCorner()
        labelConfirmPassword.setLeftPaddingPoints(10.0)
        btnSubmit.layer.cornerRadius = 15.0
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.darkGray.cgColor
        if !errorView.isHidden {
            UIView.transition(with: errorView, duration: 0.2, options: .transitionCrossDissolve, animations: {
                self.errorView.alpha = 0.0
            })
        }
    }
    override func resignFirstResponder() -> Bool {
        if errorView.alpha != 0.0  {
            self.errorView.alpha = 0.0
        }
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        if textField == labelNewPassword {
            guard let password = labelNewPassword.text, password != "" else{
                showInputValidationError(textField: labelNewPassword, error: "Please enter a valid 8 Character long password")
                return false
            }
            textField.resignFirstResponder()
            textFieldDidBeginEditing(textField)
            labelConfirmPassword.becomeFirstResponder()
        } else if textField == labelConfirmPassword {
            guard let confirmPass = labelConfirmPassword.text, confirmPass != labelNewPassword.text else{
                showInputValidationError(textField: labelConfirmPassword, error: "Please re-enter new password")
                return false
            }
            textField.resignFirstResponder()
        }
        return true
    }
    
    func validateUserInput(){
        
        guard let password = labelNewPassword.text, password.validationPassword() else{
            showInputValidationError(textField: labelNewPassword, error: "Enter a minimum 8 characters long password")
            return
        }
        
        guard let confirmPassword = labelConfirmPassword.text, confirmPassword == password else{
            showInputValidationError(textField: labelConfirmPassword, error: "Enter a minimum 8 characters long password")
            return
        }
        
        print(password+" "+confirmPassword)
        
        let postParams = ["Password":password, UserDefaultData.UserId.rawValue : UserDefaults.standard.string(forKey: UserDefaultData.UserId.rawValue)!] as [String:Any]
        
        actionSubmit(params: postParams)
    }
    
    func showInputValidationError(textField: UIView, error: String){
        textField.layer.borderColor = UIColor.red.cgColor
        textField.shakeAnimation()
        labelErrorLog.text = error
        self.errorView.alpha = 1.0
        
        let transition = CATransition()
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromBottom
        errorView.layer.add(transition, forKey: nil)
        self.view.addSubview(self.errorView)
        
    }
    
    @IBAction func actionSubmit(_ sender: UIButton) {
        
        validateUserInput()
    }
    
    func actionSubmit(params : [String:Any]){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_USER_ChangePassword,
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
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
                    guard let jsonDictionary = (responseJSON as? [String: Any]) else{
                        print("json format mismatch")
                        return
                    }
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    
                    if status == "success" {
                        
                        let invalid_login_alert = UIAlertController(title: "Password", message: "Password Changed Successfully", preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.navigationController?.popViewController(animated: true)
                            self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Registration Incomplete", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
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
