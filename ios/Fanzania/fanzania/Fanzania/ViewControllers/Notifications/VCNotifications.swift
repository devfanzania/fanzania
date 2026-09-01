//
//  VCNotifications.swift
//  Fanzania
//
//  Created by Writayan Das on 21/04/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

struct NotificationResponseElement {
    
    let NotificationId, UserId, MessageType: Int
    let Title, Message, UpdateDate, InsertDate: String
    let Active, ReadActive: Bool
}

class VCNotifications: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lblNotificationCount: UILabel!
    
    var notifications = [NotificationResponseElement]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setTitle("My Notifications")
        self.lblNotificationCount.text = nil
        fetchNotifications()
    }
    
    // MARK: - IBActions
    @IBAction func tappedBtnClearAll(_ sender: UIButton) {
        removeNotification(withId: -1, index: -1)
    }
}

// MARK: - UITableViewDataSource
extension VCNotifications: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return notifications.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TVCNotification", for: indexPath) as! TVCNotification
        cell.lblHeading.text = notifications[indexPath.row].Title
        cell.lblDescription.text = notifications[indexPath.row].Message
        
        let dateFormater = DateFormatter()
        dateFormater.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SS"
        dateFormater.timeZone = TimeZone(identifier: "UTC")
        
        if let date = dateFormater.date(from: notifications[indexPath.row].UpdateDate) {
            dateFormater.dateFormat = "yyyy-MM-dd"
            dateFormater.timeZone = .current
            cell.lblDate.text = dateFormater.string(from: date)
        } else {
            cell.lblDate.text = nil
        }
        
        cell.deleteAction = { [unowned self] in
            removeNotification(withId: notifications[indexPath.row].NotificationId, index: indexPath.row)
        }
        return cell
    }
}

// MARK: - UITableViewDelegate
extension VCNotifications: UITableViewDelegate {
    
}

// MARK: - API Methods
extension VCNotifications {
    
    func fetchNotifications() {
        debugPrint("fetching notifications")
        let params: [String: Any] = [
            "UserId": UserDefaults.standard.integer(forKey: "UserId")
        ]
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_FetchNotifications,
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("All Tournament Players: \(response.result.value ?? "")")
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
                        print("json format mismatch first")
                        return
                    }
                    
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        self.notifications.removeAll()
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            print(dataArray)
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    let NotificationId = element["NotificationId"] as! Int
                                    let UserId = element["UserId"] as! Int
                                    let MessageType = element["MessageType"] as! Int
                                    let Title = element["Title"] as! String
                                    let Message = element["Message"] as! String
                                    let UpdateDate = element["UpdateDate"] as! String
                                    let InsertDate = element["InsertDate"] as! String
                                    let Active = element["Active"] as! Bool
                                    let ReadActive = element["ReadActive"] as! Bool

                
                                    self.notifications.append(NotificationResponseElement(NotificationId: NotificationId,
                                                                                          UserId: UserId,
                                                                                          MessageType: MessageType,
                                                                                          Title: Title,
                                                                                          Message: Message,
                                                                                          UpdateDate: UpdateDate,
                                                                                          InsertDate: InsertDate,
                                                                                          Active: Active,
                                                                                          ReadActive: ReadActive))
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                if self.notifications.count == 0 {
                                    self.lblNotificationCount.text = "You have no notifications."
                                } else {
                                    self.lblNotificationCount.text = "Top \(self.notifications.count) notifications:"
                                }
                                self.tableView.reloadData()
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
    
    func removeNotification(withId notificationId: Int, index: Int) {
        debugPrint("fetching notifications")
        let params: [String: Any] = [
            "UserId": UserDefaults.standard.integer(forKey: "UserId"),
            "NotificationId": notificationId
        ]
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_AckNotification,
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("Notifications: \(response.result.value ?? "")")
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
                            let NotificationCount = array.first?["NotificationCount"]
                            DispatchQueue.main.async {
                                
                                if let NotificationCount = NotificationCount as? Int {
                                    if NotificationCount == 0 {
                                        self.lblNotificationCount.text = "You have no notifications."
                                    } else {
                                        self.lblNotificationCount.text = "Top \(NotificationCount) notifications:"
                                    }
                                } else {
                                    self.lblNotificationCount.text = "You have no notifications."
                                }
                                
                                if notificationId == -1 {
                                    self.notifications.removeAll()
                                } else {
                                    self.tableView.beginUpdates()
                                    self.notifications.remove(at: index)
                                    self.tableView.deleteRows(at: [[0,index]], with: .automatic)
                                    self.tableView.endUpdates()
                                }
                                self.tableView.reloadData()
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
