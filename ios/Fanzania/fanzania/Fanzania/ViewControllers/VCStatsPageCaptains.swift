//
//  VCStatsPageCaptains.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 28/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class CaptaincyModel {
    var matchNo:Int?
    var match:String?
    var CaptainName:String?
    var points:Int?
    
    init(matchNo:Int?, match:String?, CaptainName:String?, points:Int?) {
        self.matchNo = matchNo
        self.match = match
        self.CaptainName = CaptainName
        self.points = points
    }
}

class VCStatsPageCaptains: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return captainList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableCaptainList.dequeueReusableCell(withIdentifier: "cell", for: indexPath)  as! RecentCaptaincyTableViewCell
        let entry = captainList[indexPath.row]
        
        cell.labelMatch.text = String(describing: entry.matchNo!)
        cell.labelMatchName.text = entry.match!
        cell.labelCaptainName.text = entry.CaptainName
        cell.labelPoints.text = String(describing: entry.points!)
        return cell
    }
    
    var tournamentId:Int?
    var userTeamId:Int?
    var captainList: [CaptaincyModel] = []
    
    @IBOutlet weak var tableCaptainList: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableCaptainList.delegate = self
        tableCaptainList.dataSource = self
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        getRecentCaptaincyList()
    }
    
    func getRecentCaptaincyList(){
        
        print("called")
        
        self.captainList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_StatsCaptaincyList,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                       "UserTeamId" : userTeamId!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
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
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let MatchNo = element["MatchNo"] as? Int
                                    let Team1 = element["Team1"] as! String
                                    let Team2 = element["Team2"] as! String
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    let Captain = element["Captain"] as? String
                                    
                                    self.captainList.append(CaptaincyModel(matchNo: MatchNo, match: Team1+" vs "+Team2, CaptainName: Captain, points: TotalPoints))
                                }
                            }
                            
                        }
                        
                        self.tableCaptainList.reloadData()
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
