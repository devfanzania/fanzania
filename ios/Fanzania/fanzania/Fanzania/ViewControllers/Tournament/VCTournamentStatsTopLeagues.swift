//
//  VCTournamentStatsTopLeagues.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCTournamentStatsTopLeagues: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var tableTournamentStatsTopLeagues: UITableView!
    var currentTournament:UserTournamentModel?
    var leagueList:[MyLeaguesModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        getData()
        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return leagueList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableTournamentStatsTopLeagues.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! TournamentStatsTopLeaguesTableViewCell
        
        print(leagueList)
        cell.labelRank.text = String(describing: leagueList[indexPath.row].LeagueRank!)
        cell.labelLeague.text = leagueList[indexPath.row].LeagueName!
        cell.labelOwner.text = leagueList[indexPath.row].LeagueLeaderName!
        cell.labelPoints.text = String(describing: leagueList[indexPath.row].LeaguePoints!)
        return cell
    }
    
    func getData(){
        
        print("running tournamnet")
        self.leagueList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_TournamentTopLeagues,
                          method: .post,
                          parameters: ["TournamentId" : (currentTournament?.id)!
                            ] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.UserId.rawValue)!, "x-api-devicetype":"ios"])
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
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    let LeagueRank = element["LeagueRank"] as? Int
                                    let LeagueName = element["LeagueName"] as? String
                                    let Owner = element["LeagueOwner"] as? String
                                    let LeaguePoints = element["LeaguePoints"] as? Int
                                    self.leagueList.append(MyLeaguesModel(LeagueRank: LeagueRank, LeagueName: LeagueName!, LeagueLeaderName: Owner, LeaguePoints: LeaguePoints))
                                }
                            }                        }
                        self.tableTournamentStatsTopLeagues.reloadData()
                        DispatchQueue.main.async {
                            UIViewController.removeSpinner(spinner: loader)
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
