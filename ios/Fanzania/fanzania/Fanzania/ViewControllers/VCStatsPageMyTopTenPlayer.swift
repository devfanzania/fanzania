//
//  VCStatsPageMyTopTenPlayer.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 28/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCStatsPageMyTopTenPlayer: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return playerList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableTopTenPlayer.dequeueReusableCell(withIdentifier: "cell", for: indexPath)  as! TopTenPlayerListTableViewCell
        
        let player = playerList[indexPath.row]
        
        cell.labelRank.text = String(describing: player.rank!)
        cell.labelPlayerName.text = player.name
        cell.labelPlayersTeam.text = player.participationTeamName
        cell.labelPointsScored.text = String(describing: player.totalPoints!)
        return cell
    }
    
    @IBOutlet var tableTopTenPlayer: UITableView!
    
    var playerList:[PlayerInfo] = []
    
    var tournamentId:Int?
    var userTeamId:Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableTopTenPlayer.delegate = self
        tableTopTenPlayer.dataSource = self
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        getTop10Players()
    }
    
    func getTop10Players(){
        
        print("running tournamnet")
        self.playerList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_StatsPageTop10,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                        "UserTeamId" : userTeamId!] as [String : Any],
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
                                    let rank = element["PlayerRank"] as? Int
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let PlayerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerPoints = element["TotalPoints"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    
                                    self.playerList.append(PlayerInfo(rank: rank, name: PlayerName, id: PlayerId, type: PlayerType, speciality: PlayerSpeciality, participationTeamName: ParticipationTeamName, totalPoints: PlayerPoints))
                                }
                            }
                            
                        }
                        
                        self.tableTopTenPlayer.reloadData()
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
