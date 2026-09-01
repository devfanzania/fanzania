//
//  VCTabBar.swift
//  Fanzania
//
//  Created by Tathagata Dey on 17/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

protocol ShowsNotificationCount: UIViewController {
    func updateNotificationCount(_ notificationCount: Int?)
}

class VCTabBar: UITabBarController, UITabBarControllerDelegate {

    var CurrentTournamentSelectedIndex = 0
    var CurrentTournamentID: Int?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
        fetchNotificationCount()
    }
    
//    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
//
//
//        let arrVC = viewControllers
//
//        print("switched index \(index)")
//        switch index {
//        case 0:
//            let vc = arrVC![index!] as! VCTournaments
//            vc.selectTournament(index: CurrentTournamentSelectedIndex)
//        default:
//            break
//        }
//    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        
        let arrVc = viewControllers
        switch viewController {
        case arrVc![0]:
            let vc = (viewController as! UINavigationController)
            (vc.viewControllers.first as! VCTournaments).selectTournament(index: CurrentTournamentSelectedIndex)
        case arrVc![1]:
            let vc = (viewController as! UINavigationController)
            (vc.viewControllers.first as! VCMyTeam).selectTournament(index: CurrentTournamentSelectedIndex)
        case arrVc![2]:
            let vc = (viewController as! UINavigationController)
            (vc.viewControllers.first as! VCMyLeague).selectTournament(index: CurrentTournamentSelectedIndex)
        default:
            break
        }
        fetchNotificationCount()
    }
    
    func fetchNotificationCount() {
        
        guard let authToken = UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue) else {
            UserDefaults.standard.set(false, forKey: UserDefaultData.StatusLogin.rawValue)
            let appDelegateTemp = UIApplication.shared.delegate as? AppDelegate
            
            let loginController = UIStoryboard(name: "Login", bundle: Bundle.main).instantiateViewController(withIdentifier: "idLoginNavigation")
            appDelegateTemp?.window?.rootViewController = loginController
            return
        }
        
        debugPrint("fetching notifications")
        let params: [String: Any] = [
            "UserId": UserDefaults.standard.integer(forKey: "UserId"),
        ]
        Alamofire.request(URL_NotificationCount,
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken": authToken,
                                    "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("Notifications Count: \(response.result.value ?? "")")
            }
            .responseJSON { response in
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        if let array = jsonDictionary["data"] as? [[String: Any]] {
                            debugPrint(array)
                            let NotificationCount = array.first?["NotificationCount"] as? Int
                            DispatchQueue.main.async {
                                self.viewControllers?.forEach {
                                    (($0 as? UINavigationController)?.viewControllers.first as? ShowsNotificationCount)?.updateNotificationCount(NotificationCount == 0 ? nil : NotificationCount)
                                }
                            }
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
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
