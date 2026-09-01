//
//  VCSignUp.swift
//  Fanzania
//
//  Created by Tathagata Dey on 28/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCSignUp: UIViewController,UITextFieldDelegate{
    
    
    @IBOutlet weak var nameTextField: PaddingIconTextField!
    @IBOutlet weak var emailTextField: PaddingIconTextField!
    @IBOutlet weak var textFieldPassword: PaddingIconTextField!
    @IBOutlet weak var btnSignUp: UIButton!
    
    var isEditingName:Bool = false
    var isEditingMail:Bool = false
    var isEditingPass:Bool = false
    
    @IBOutlet weak var validationErrorText: UILabel!
    @IBOutlet weak var validationErrorView: UIView!
    @IBOutlet weak var scroll: UIScrollView!
    
    var OTPtoPass:String?
    override func viewDidLoad() {
        super.viewDidLoad()
        initialiseViews()
        self.hideKeyboardWhenTappedAround()
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillHideNotification, object: nil)
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        // Do any additional setup after loading the view.
    }
    
    @objc func adjustForKeyboard(notification : Notification){
        let userInfo = notification.userInfo!
        
        let keyboardScreenEndFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        let keyboardViewEndFrame = view.convert(keyboardScreenEndFrame, from: view.window)
        
        if notification.name == UIResponder.keyboardWillHideNotification {
            scroll.contentInset = UIEdgeInsets.zero
        } else {
            scroll.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: keyboardViewEndFrame.height, right: 0)
        }
        scroll.scrollIndicatorInsets = scroll.contentInset
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.darkGray.cgColor
        if !validationErrorView.isHidden {
            UIView.transition(with: validationErrorView, duration: 0.2, options: .transitionCrossDissolve, animations: {
                self.validationErrorView.alpha = 0.0
            })
        }
        if textField == nameTextField {
            isEditingName = true
        }
        if textField == emailTextField {
            isEditingMail = true
        }
        if textField == textFieldPassword {
            isEditingPass = true
        }
        if isEditingName && isEditingMail && isEditingPass {
            btnSignUp.isEnabled = true
            btnSignUp.createGradientLayer()
        }else{
            btnSignUp.isEnabled = true
            btnSignUp.createDisableButtonGradientLayer()
        }
    }

    override func resignFirstResponder() -> Bool {
        if validationErrorView.alpha != 0.0  {
            self.validationErrorView.alpha = 0.0
        }
        return true
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    func initialiseViews(){
        
        self.validationErrorView.alpha = 0.0
        nameTextField.delegate = self
        emailTextField.delegate = self
        textFieldPassword.delegate = self
        nameTextField.attributedPlaceholder = NSAttributedString(string: "Name",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        emailTextField.attributedPlaceholder = NSAttributedString(string: "Email Address",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        textFieldPassword.attributedPlaceholder = NSAttributedString(string: "Password",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        btnSignUp.curvedCornerButtonView()
        btnSignUp.isEnabled = false
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.isTranslucent = true
        self.navigationController?.view.backgroundColor = .clear
    }
    @IBAction func actionShowCountryList(_ sender: Any) {
        self.selectCountry()
    }
    
    @IBAction func actionShowCalendar(_ sender: Any) {
        self.selectDate()
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        if textField == nameTextField {
            guard let firstname = nameTextField.text, firstname != "" else{
                showInputValidationError(textField: nameTextField, error: "Enter Firstname")
                return false
            }
            textField.resignFirstResponder()
            textFieldDidBeginEditing(textField)
            emailTextField.becomeFirstResponder()
        } else if textField == emailTextField {
            guard let email = emailTextField.text, email.validateEmail() else{
                showInputValidationError(textField: emailTextField, error: "Please Enter a valid Email")
                return false
            }
            textField.resignFirstResponder()
            textFieldDidBeginEditing(textField)
            textFieldPassword.becomeFirstResponder()
        }else if textField == textFieldPassword {
            guard let password = textFieldPassword.text, password.validationPassword() else{
                showInputValidationError(textField: textFieldPassword, error: "Enter a minimum 8 characters long password")
                return false
            }
            textField.resignFirstResponder()
        }
        return true
    }
    
    @objc func selectCountry(){
        performSegue(withIdentifier: "SeagueSelectCountry", sender: self)
    }
    
    @objc func selectDate(){
        performSegue(withIdentifier: "SegueSelectDate", sender: self)
    }
    

    @IBAction func actionSignUp(_ sender: Any) {
        validateUserInput()
    }
    
    func validateUserInput(){
        
        guard let firstname = nameTextField.text, firstname != "" else{
            showInputValidationError(textField: nameTextField, error: "Enter Firstname")
            return
        }
        guard let email = emailTextField.text, email.validateEmail() else{
            showInputValidationError(textField: emailTextField, error: "Please Enter a valid Email")
            return
        }
        guard let password = textFieldPassword.text, password.validationPassword() else{
            showInputValidationError(textField: textFieldPassword, error: "Enter a minimum 8 characters long password")
            return
        }
        
        let postParams = ["Name":firstname, "Email":email, "Password":password]
        
        excutionUserExistenceVarification(username:email, param: postParams)
    }
    
    func showInputValidationError(textField: UIView, error: String){
        textField.layer.borderColor = UIColor.red.cgColor
        textField.shakeAnimation()
        validationErrorText.text = error
        self.validationErrorView.alpha = 1.0
        
        let transition = CATransition()
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromBottom
        validationErrorView.layer.add(transition, forKey: nil)
        self.view.addSubview(self.validationErrorView)

    }
    
    func excutionUserExistenceVarification(username: String, param: [String: String]){
        
        let postParams: NSDictionary = ["Email" : username]
        let requestURL = URL_User_Varification
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(requestURL, method: .post, parameters: postParams as? [String : AnyObject], encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
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
                    
                    print("data at user is new varification \(jsonDictionary)")
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                       self.executeSignUp(param: param)
                    }else{
                        let alert = UIAlertController(title: "Email Already Exists", message: "Unable to Register; Please try again using another email", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(alert, animated: true, completion: nil)

                    }
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func executeSignUp(param:[String:String]){
        let requestURL = URL_SignUp
        let loader = UIViewController.displaySpinner(onView: self.view)
        print(param)
        Alamofire.request(requestURL, method: .post, parameters: param, encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
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
                    print("data at Sign Up \(jsonDictionary)")
                    if status == "success" {
                        
                        guard let responseArray = jsonDictionary["data"] as? NSArray else {
                            print("No proper json Data format")
                            return
                        }
                        print("responseArray \(responseArray[0])")
                        
                        if let userData = responseArray[0] as? [String:Any] {
                            
                            
                            UserDefaults.standard.set(userData[UserDefaultData.UserId.rawValue] as! Int, forKey: UserDefaultData.UserId.rawValue)
                            UserDefaults.standard.set(userData[UserDefaultData.UserName.rawValue] as! String , forKey: UserDefaultData.UserName.rawValue)
                            UserDefaults.standard.set(userData[UserDefaultData.Email.rawValue] as! String , forKey: UserDefaultData.Email.rawValue)
                            UserDefaults.standard.set(userData[UserDefaultData.Name.rawValue] as! String , forKey: UserDefaultData.Name.rawValue)
                            UserDefaults.standard.set(userData[UserDefaultData.SessionId.rawValue] as! String, forKey: UserDefaultData.SessionId.rawValue)
                            
                            
                            self.OTPtoPass = userData["ActivationToken"] as? String
                            self.performSegue(withIdentifier: "SegueEmailVarification", sender: self)
                        }else{
                            return
                        }
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if let destination = segue.destination as? VCOTPMailVarification {
            destination.OTPSent = OTPtoPass
            //destination.delegate = self
        }
    }
    @IBAction func actionContactUs(_ sender: UIButton) {
        let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
        let staticPage = storyBoard.instantiateViewController(withIdentifier: "VCStaticPages") as! VCStaticPages
        staticPage.staticPageLink = URLContactUS
        navigationController?.pushViewController(staticPage, animated: true)
    }
    @IBAction func actionTC(_ sender: UIButton) {
        let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
        let staticPage = storyBoard.instantiateViewController(withIdentifier: "VCStaticPages") as! VCStaticPages
        staticPage.staticPageLink = URLTC
        navigationController?.pushViewController(staticPage, animated: true)
    }
    
}
