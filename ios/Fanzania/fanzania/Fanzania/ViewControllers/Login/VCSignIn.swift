//
//  VCLoginPage.swift
//  Fanzania
//
//  Created by Tathagata Dey on 27/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire
import FBSDKLoginKit
import FBSDKCoreKit
import GoogleSignIn
import AuthenticationServices
import CoreLocation


class VCSignIn: UIViewController, GIDSignInDelegate, UITextFieldDelegate {
    
    
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var labelError: UILabel!
    @IBOutlet weak var tvUserName: PaddingIconTextField!
    @IBOutlet weak var tvPassword: PaddingIconTextField!
    @IBOutlet weak var btnSignIn: UIButton!
    @IBOutlet weak var btnSignUp: UIButton!
    @IBOutlet weak var facebookButton: UIButton!
    @IBOutlet weak var scroll: UIScrollView!
    @IBOutlet weak var googleButton: UIButton!
    @IBOutlet weak var stackOtherLogin: UIStackView!
    
    var Name:String?
    private var locationManager: CLLocationManager?
    private var lastKnownLocation: CLLocation?
        
    override func viewDidLoad() {
        super.viewDidLoad()
        getUserLocation()
        initialiseUI()
        self.hideKeyboardWhenTappedAround()
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillHideNotification, object: nil)
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        
        
        tvUserName.delegate = self
        tvPassword.delegate = self

 // Do any additional setup after loading the view.
//        GIDSignIn.sharedInstance().uiDelegate = self
//        if FBSDKAccessToken.current() != nil{
//            fetchFBProfile()
//        }
        
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
        textField.layer.borderColor = UIColor.white.cgColor
        if self.errorView.alpha != 0.0 {
            
            UIView.transition(with: errorView, duration: 0.1, options: .transitionCrossDissolve, animations: {
                self.errorView.alpha = 0.0
                
            })
        }
    }

    override func resignFirstResponder() -> Bool {
        if self.errorView.alpha != 0.0{
            self.errorView.alpha = 0.0
        }
        return true
    }
    
    @IBAction func GoogleLoginTapped(_ sender: Any) {
        GIDSignIn.sharedInstance().delegate=self
        GIDSignIn.sharedInstance()?.presentingViewController = self
        GIDSignIn.sharedInstance().signIn()
    }
    
    @IBAction func FacebookLoginTapped(_ sender: Any) {
        
        if AccessToken.current != nil{
            fetchFBProfile()
        }else {
            let loginManager:LoginManager = LoginManager()
            
            loginManager.logIn(permissions: ["email","public_profile"], from: self, handler: { (loginResults: LoginManagerLoginResult?, error: Error?) -> Void in
                if !(loginResults?.isCancelled)! {
                    self.fetchFBProfile()
                } else {
                    // Sign in request cancelled
                }
            })
        }
    }
    
    @IBAction func fbLogout(_ sender: Any) {
        
        Alamofire.request(URL_Logout,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue)],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
            .responseJSON { response in
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
                        
                        print("logout successful")
                    }
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
  
    func loginButton(_ loginButton: FBLoginButton!, didCompleteWith result: LoginManagerLoginResult!, error: Error!) {
        if error != nil {
            print(error)
        } else {
            fetchFBProfile()
        }
    }
    
    func getUserLocation() {
        locationManager = CLLocationManager()
        locationManager?.requestWhenInUseAuthorization()
        locationManager?.startUpdatingLocation()
        locationManager?.delegate = self
    }

    func fetchFBProfile() {
        
        let parameters = ["fields": "email,picture.type(large),name,first_name,last_name,gender,age_range,cover,timezone,verified,updated_time,education,religion,friends"]
        GraphRequest(graphPath: "me", parameters: parameters).start{ (connection,result,error)-> Void in
            
            if error != nil {   // Error occured while logging in
                // handle error
                print(error!)
                return
            }
            // Details received successfully
            let dictionary = result as! [String: AnyObject]
            print(dictionary)
            // pass this dictionary object into your model class initialiser
            let userName = dictionary["name"] as! String
            let accessToken = AccessToken.current?.tokenString
            self.Name = userName

            var parameter:[String: Any] = [
                "UserName" : userName,
                "LoginProviderAccessToken" : accessToken!,
                "LoginProvider" : LoginProviderMarchent.Facebook.rawValue,
                "Email" : dictionary["email"] as! String
            ]
            self.geocode { [unowned self] (placeMark) in
                if let placeMark = placeMark {
                    parameter["LoginLocation"] = placeMark.jsonValue()
                }
                self.executeSocialSignIn(parameter: parameter)
            }
        }
    }
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if let error = error {
            print("\(error.localizedDescription)")
        } else {
            // Perform any operations on signed in user here.
            
            
            let userName = user.profile.name as String    // For client-side use only!
            print(userName)
            let idToken = user.authentication.idToken as String // Safe to send to the server
            let email = user.profile.email as String
            self.Name = userName
            
            
            
            var parameter:[String: Any] = [
                "UserName" : userName,
                "LoginProviderAccessToken" : idToken,
                "LoginProvider" : LoginProviderMarchent.Google.rawValue,
                "Email" : email
            ]
            self.geocode { [unowned self] (placeMark) in
                if let placeMark = placeMark {
                    parameter["LoginLocation"] = placeMark.jsonValue()
                }
                self.executeSocialSignIn(parameter: parameter)
            }
        }
    }
    
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
        // Perform any operations when the user disconnects from app here.
        print("Google Disconnect User")
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    func executeSocialSignIn(parameter: [String: Any]) {
        
        print("external login param \(parameter)")
        let requestURL = URL_ExternalLoginCheck
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(requestURL,
                          method: .post,
                          parameters: parameter,
                          encoding: JSONEncoding.default,
                          headers: [
                            "x-api-authtoken":"",
                            "x-api-devicetype":"ios"
            ]
        )
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
                
                print("data at external login \(jsonDictionary)")
                guard let status = jsonDictionary["status"] as? String else{
                    print("json format mismatch")
                    return
                }
                
                if status == "success" {
                    
                    guard let responseArray = jsonDictionary["data"] as? NSArray else {
                        print("No proper json Data format")
                        return
                    }
                    print("responseArray \(responseArray[0])")

                    if let userData = responseArray[0] as? [String:Any] {
                        
                        guard let _ = userData[UserDefaultData.Email.rawValue] as? String else {
                            let invalid_login_alert = UIAlertController(title: "Could not Login",
                                                                        message: jsonDictionary["statusMessage"] as? String,
                                                                        preferredStyle: UIAlertController.Style.alert)
                            
                            invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                            }))
                            self.present(invalid_login_alert, animated: true, completion: nil)
                            return
                        }
                        
                        
                        UserDefaults.standard.set(true , forKey: UserDefaultData.StatusLogin.rawValue)
                        
                        UserDefaults.standard.set(userData[UserDefaultData.UserId.rawValue] as! Int,
                                                  forKey: UserDefaultData.UserId.rawValue)
                        UserDefaults.standard.set(userData[UserDefaultData.UserName.rawValue] as! String,
                                                  forKey: UserDefaultData.UserName.rawValue)
                        UserDefaults.standard.set(userData[UserDefaultData.Email.rawValue] as! String,
                                                  forKey: UserDefaultData.Email.rawValue)
                        self.Name = (self.Name == nil ? userData[UserDefaultData.UserName.rawValue] as? String : self.Name)
                        UserDefaults.standard.set(self.Name , forKey: UserDefaultData.Name.rawValue)
                        UserDefaults.standard.set(userData[UserDefaultData.SessionId.rawValue] as! String , forKey: UserDefaultData.SessionId.rawValue)
                        UserDefaults.standard.set(userData[UserDefaultData.BackgroundTheme.rawValue] as? String , forKey: UserDefaultData.BackgroundTheme.rawValue)
                        let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
                        appDelegateTemp?.window?.rootViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateInitialViewController()
                        
                    }else{
                        return
                    }
                    
                }else{
                    let invalid_login_alert = UIAlertController(title: "Could not Login",
                                                                message: jsonDictionary["statusMessage"] as? String,
                                                                preferredStyle: UIAlertController.Style.alert)
                    
                    invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                    }))
                    self.present(invalid_login_alert, animated: true, completion: nil)
                }
                
            case .failure(let error):
                print("Request failed with error: \(error)")
            }
        }
    }
    
    func geocode(completion: @escaping (_ placemark: CLPlacemark?) -> Void)  {
        guard let location = locationManager?.location else {
            completion(nil)
            return
        }
        CLGeocoder().reverseGeocodeLocation(location) { (placeMarks, error) in
            guard let place = placeMarks?.first else {
                completion(nil)
                return
            }
            completion(place)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(true)
        navigationController?.setNavigationBarHidden(false, animated: true)
    }
    
    func initialiseUI(){
        
        self.errorView.alpha = 0.0
        tvUserName.attributedPlaceholder = NSAttributedString(string: "Email Address",
                                                               attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        tvPassword.attributedPlaceholder = NSAttributedString(string: "Password",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        facebookButton.curvedCornerBorderButtonView()
        facebookButton.imageView?.contentMode = .scaleAspectFit
        googleButton.curvedCornerBorderButtonView()
        googleButton.imageView?.contentMode = .scaleAspectFit
        btnSignIn.curvedCornerButtonView()
        initializeAppleSignin()
    }
    
    func initializeAppleSignin() {
        
        if #available(iOS 13.0, *) {
            let btnApple = ASAuthorizationAppleIDButton(authorizationButtonType: .signIn,
                                                        authorizationButtonStyle: .whiteOutline)
            stackOtherLogin.addArrangedSubview(btnApple)
            btnApple.addConstraint(NSLayoutConstraint(item: btnApple,
                                                      attribute: .height,
                                                      relatedBy: .equal,
                                                      toItem: nil,
                                                      attribute: .height,
                                                      multiplier: 1.0,
                                                      constant: 44.0))
            btnApple.addTarget(self, action: #selector(tappedBtnApple), for: .touchUpInside)
            
        } else {
            debugPrint("Sign In with Apple Not Available in current iOS version.")
        }
    }
    
    @available(iOS 13.0, *)
    @objc func tappedBtnApple(sender: ASAuthorizationAppleIDButton) {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]
        let controller = ASAuthorizationController(authorizationRequests: [request])
        controller.delegate = self
        controller.presentationContextProvider = self
        controller.performRequests()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        if textField == tvUserName {
            guard let username = tvUserName.text, username.validateEmail() else{
                showInputValidationError(textField: tvUserName, error: "Enter valid Email")
                return false
            }
            textField.resignFirstResponder()
            textFieldDidBeginEditing(textField)
            tvPassword.becomeFirstResponder()
        } else if textField == tvPassword {
            guard let password = tvPassword.text, password.validationPassword() else{
                showInputValidationError(textField: tvPassword, error: "Enter minimum 8 characters long Password")
                return false
            }
            textField.resignFirstResponder()
            textFieldDidBeginEditing(textField)
            actionSignup(self)
        }
        return true
    }
    
    @IBAction func actionSignup(_ sender: Any) {
        
        guard let username = tvUserName.text, username.validateEmail() else{
            showInputValidationError(textField: tvUserName, error: "Please enter a valid name")
            return
        }
        guard let password = tvPassword.text, password != "" else{
            showInputValidationError(textField: tvPassword, error: "Please enter a password")
            return
        }
        print(username)
        print(password)
        
        executeSignIn(username:username, password:password)
        
//        if(username == "123" && password == "123"){
//            UserDefaults.standard.set(true, forKey: UserDefaultData.StatusLogin.rawValue)
//            let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
//            appDelegateTemp?.window?.rootViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateInitialViewController()
//        }
    }
    
    func showInputValidationError(textField: UIView, error: String){
        textField.layer.borderColor = UIColor.red.cgColor
        textField.shakeAnimation()
        labelError.text = error
        self.errorView.alpha = 1.0
        
        let transition = CATransition()
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromBottom
        errorView.layer.add(transition, forKey: nil)
        self.view.addSubview(self.errorView)
        
    }
    
    
    func executeSignIn(username:String, password:String){
        var postParams: [String: Any] = [
            "Email" : username,
            "Password" : password
        ]
        self.geocode { [unowned self] (placeMark) in
            if let placeMark = placeMark {
                postParams["LoginLocation"] = placeMark.jsonValue()
            }
            let requestURL = URL_Login
            
            let loader = UIViewController.displaySpinner(onView: self.view)
            Alamofire.request(requestURL, method: .post, parameters: postParams, encoding: JSONEncoding.default, headers: ["x-api-authtoken":"", "x-api-devicetype":"ios"])
                .responseString { response in
                    debugPrint("Login Response: \(response.result.value ?? "")")
                }
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
                        
                        print("data at normal login \(jsonDictionary)")
                        guard let status = jsonDictionary["status"] as? String else{
                            print("json format mismatch")
                            return
                        }
                        if status == "success" {
                            
                            guard let responseArray = jsonDictionary["data"] as? NSArray else {
                                print("No proper json Data format")
                                return
                            }
                            print("responseArray \(responseArray[0])")
                            
                            UserDefaults.standard.set(true, forKey: UserDefaultData.StatusLogin.rawValue)
                            
                            if let userData = responseArray[0] as? [String:Any] {
                                
                                print(userData[UserDefaultData.Name.rawValue] as! String)
                                
                                UserDefaults.standard.set(true , forKey: UserDefaultData.StatusLogin.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.UserId.rawValue] as! Int , forKey: UserDefaultData.UserId.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.UserName.rawValue] as! String , forKey: UserDefaultData.UserName.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.Email.rawValue] as! String , forKey: UserDefaultData.Email.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.Name.rawValue] as! String , forKey: UserDefaultData.Name.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.SessionId.rawValue] as! String , forKey: UserDefaultData.SessionId.rawValue)
                                UserDefaults.standard.set(userData[UserDefaultData.BackgroundTheme.rawValue] as? String , forKey: UserDefaultData.BackgroundTheme.rawValue)
                            }else{
                                return
                            }
                            let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
                            appDelegateTemp?.window?.rootViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateInitialViewController()
                            
                        }else{
                            let invalid_login_alert = UIAlertController(title: "That's a no-ball. We are unable to login", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                            
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
    @IBAction func actionContactUS(_ sender: UIButton) {
        let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
        let staticPage = storyBoard.instantiateViewController(withIdentifier: "VCStaticPages") as! VCStaticPages
        staticPage.staticPageLink = URLContactUS
        navigationController?.pushViewController(staticPage, animated: true)
    }
}

// MARK: - ASAuthorizationControllerDelegate
extension VCSignIn: ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding {
    
    @available(iOS 13.0, *)
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return ASPresentationAnchor()
    }
    
    
    @available(iOS 13.0, *)
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        debugPrint(error.localizedDescription)
        
        let invalid_login_alert = UIAlertController(title: "Could not Login",
                                                    message: error.localizedDescription,
                                                    preferredStyle: UIAlertController.Style.alert)
        
        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
        }))
        self.present(invalid_login_alert, animated: true, completion: nil)
    }
    
    @available(iOS 13.0, *)
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        
        guard let appleIDCredentials = authorization.credential as? ASAuthorizationAppleIDCredential else {
            return
        }
        guard let appleToken = String(data: appleIDCredentials.identityToken!,
                                      encoding: .utf8) else {
            return
        }
        var parameter: [String: Any] = [
            "LoginProviderAccessToken": appleToken,
            "LoginProvider": LoginProviderMarchent.Apple.rawValue,
        ]
        self.geocode { [unowned self] (placeMark) in
            if let placeMark = placeMark {
                parameter["LoginLocation"] = placeMark.jsonValue()
            }
            self.executeSocialSignIn(parameter: parameter)
        }
    }
}

// MARK: - CLLocationManagerDelegate
extension VCSignIn: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            lastKnownLocation = location
        }
    }
}
