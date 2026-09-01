//
//  VCTournamentStatsTopTeams.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCTournamentStatsTopTeams: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableTournamentStatsTopTeams: UITableView!
    var currentTournament:UserTournamentModel?
    var teamList:[TeamModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        getData()
        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return teamList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableTournamentStatsTopTeams.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! TournamentStatsTopTeamsTableViewCell
        
        cell.labelRank.text = String(describing: teamList[indexPath.row].rank!)
        cell.labelTeam.text = teamList[indexPath.row].teamName!
        cell.labelOwner.text = teamList[indexPath.row].owner!
        cell.labelPoints.text = String(describing: teamList[indexPath.row].points!)
        return cell
    }
    
    func getData(){
        
        print("running tournamnet")
        self.teamList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_TournamentTopTeams,
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
                                    
                                    let TeamRank = element["TeamRank"] as? Int
                                    let UserTeamName = element["UserTeamName"] as? String
                                    let Owner = element["Owner"] as? String
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    self.teamList.append(TeamModel(rank: TeamRank!, teamName: UserTeamName!, owner: Owner, points: TotalPoints))
                                }
                            }
                        }
                        self.tableTournamentStatsTopTeams.reloadData()
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
