//
//  VCMorePage.swift
//  Fanzania
//
//  Created by Tathagata Dey on 24/02/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCMorePage: UIViewController {


    override func viewDidLoad() {
        super.viewDidLoad()

        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        // Do any additional setup after loading the view.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
            if let destination = segue.destination as? VCStaticPages {
                if segue.identifier == "segueAboutUS" {
                    destination.staticPageLink = URLAboutUs
                }else if segue.identifier == "segueT&C" {
                    destination.staticPageLink = URLTermsCondition
                }else if segue.identifier == "segueRules" {
                    destination.staticPageLink = URLHowToPlay
                }else if segue.identifier == "segueFAQ" {
                    destination.staticPageLink = URLFAQ
                }else if segue.identifier == "segueContactUs" {
                    destination.staticPageLink = URLContactUS
                }
        }
    }
    
    @IBAction func actionLogout(_ sender: Any) {
        
        let alert = UIAlertController(title: "Logout", message: "Do you want to logout from Fanzania?", preferredStyle: UIAlertController.Style.alert)
        
        alert.addAction(UIAlertAction(title: "Logout", style: .default, handler: { action in
            self.dismiss(animated: true, completion: nil)
            self.actionLogout()
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { action in
            self.dismiss(animated: true, completion: nil)
        }))
        self.present(alert, animated: true, completion: nil)
        
    }
    
    func actionLogout(){
        Alamofire.request(URL_Logout,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: "UserId")],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
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
                    print("data at mail varify \(jsonDictionary)")
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        UserDefaults.standard.set(false, forKey: UserDefaultData.StatusLogin.rawValue)
                        let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
                        
                        let loginController = UIStoryboard(name: "Login", bundle: Bundle.main).instantiateViewController(withIdentifier: "idLoginNavigation")
                        appDelegateTemp?.window?.rootViewController = loginController
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Cant Logout", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    @IBAction func actionShareApp(_ sender: Any) {
        let text = StringConstants.ShareAppText
        // set up activity view controller
        let textToShare = [ text ]
        let activityViewController = UIActivityViewController(activityItems: textToShare, applicationActivities: nil)
        activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
        
        // exclude some activity types from the list (optional)
        activityViewController.excludedActivityTypes = [UIActivity.ActivityType.airDrop]
        
        // present the view controller
        self.present(activityViewController, animated: true, completion: nil)
    }
    
    @IBAction func actionRateUs(_ sender: Any) {
        if let url = URL(string: rateUS), UIApplication.shared.canOpenURL(url) {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
}
