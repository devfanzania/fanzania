//
//  VCTournamentStatsTopPlayers.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire


class VCTournamentStatsTopPlayers: UIViewController, UITableViewDelegate, UITableViewDataSource {

    
    @IBOutlet weak var tableTournamentStatsTopPlayers: UITableView!
    var currentTournament:UserTournamentModel?
    var playerList:[PlayerInfo] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        if let currentTournament = currentTournament {
            getData()
        }
        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return playerList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableTournamentStatsTopPlayers.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! TournamentStatsTopPlayerTableViewCell
        
        cell.labelRank.text = String(describing: playerList[indexPath.row].rank!)
        cell.labelName.text = playerList[indexPath.row].name!
        cell.labelTeam.text = playerList[indexPath.row].participationTeamName!
        cell.labelPoints.text = String(describing: playerList[indexPath.row].totalPoints!)
        return cell
    }
    
    func getData(){
        
        print("running tournamnet")
        self.playerList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_GlobalTopPlayers,
                          method: .post,
                          parameters: ["TournamentId" : (currentTournament?.id)!,
                                       "UserTeamId" : (currentTournament?.UserTeamId)!] as [String : Any],
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
                                    
                                    let PlayerRank = element["PlayerRank"] as? Int
                                    let PlayerName = element["PlayerName"] as? String
                                    let PlayerId = element["PlayerId"] as? Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as? String
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    self.playerList.append(PlayerInfo(rank: PlayerRank, name: PlayerName!, id: PlayerId!, type: "", speciality: "", participationTeamName: ParticipationTeamName!, totalPoints: TotalPoints!))
                                }
                            }
                        }
                        self.tableTournamentStatsTopPlayers.reloadData()
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
