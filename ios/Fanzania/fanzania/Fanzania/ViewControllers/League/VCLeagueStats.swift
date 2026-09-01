//
//  VCLeagueStats.swift
//  Fanzania
//
//  Created by Tathagata Dey on 28/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class LeagueStatsModel {
    var rank:Int?
    var teamName : String?
    var playerName1 : String?
    var playerName2 : String?
    var playerName3 : String?
    var player1Points : Int?
    var player2Points : Int?
    var player3Points : Int?
    
    init(rank:Int?, teamName : String?, playerName1 : String?, playerName2 : String?, playerName3 : String?, player1Points : Int?, player2Points : Int?, player3Points : Int?) {
        self.rank  = rank
        self.teamName = teamName
        self.playerName1 = playerName1
        self.playerName2 = playerName2
        self.playerName3 = playerName3
        self.player1Points = player1Points
        self.player2Points = player2Points
        self.player3Points = player3Points
    }
}

class VCLeagueStats: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tournamentView: UIView!
    @IBOutlet weak var tableLeagueStats: UITableView!
    @IBOutlet weak var labelTournamentName: UILabel!
    @IBOutlet weak var labelTournamentStatus: UILabel!
    @IBOutlet weak var labelTeamName: UILabel!
    @IBOutlet weak var segmentedControlLeague: UISegmentedControl!
    
    @IBOutlet weak var thirdHeader: UILabel!
    @IBOutlet weak var fourthHeader: UILabel!
    var statList : [LeagueStatsModel] = []
    var tournamentId:Int?
    var leagueId:Int?
    var currentTournament:UserTournamentModel?
    var teamName:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        segmentedControlLeague.selectedSegmentIndex = 0
        getTopPerformer()
        if let currentTournament = currentTournament {
            labelTournamentName.text = currentTournament.name!
            labelTournamentStatus.text = "\((currentTournament.StartDate)!) to \((currentTournament.EndDate)!)"
        }
        labelTeamName.text = teamName!
        // Do any additional setup after loading the view.
    }
    
    @IBAction func showComponent(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0 {
            thirdHeader.text = "Top 3 Players"
            fourthHeader.text = "Points"
            getTopPerformer()
        } else {
            thirdHeader.text = "Top 3 Favourite"
            fourthHeader.text = "Match Played"
            getTopFavourite()
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return statList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableLeagueStats.dequeueReusableCell(withIdentifier: "cell", for: indexPath)  as! LeagueStatsTableViewCell
        
        let entry = statList[indexPath.row]
        
        cell.labelRank.text = String(describing: entry.rank!)
        cell.labelLeagueTeam.text = entry.teamName!
        cell.Player1.text = entry.playerName1!
        cell.Player2.text = entry.playerName2!
        cell.Player3.text = entry.playerName3!
        cell.labelPlayer1Points.text = String(describing: entry.player1Points!)
        cell.labelPlayer2Points.text = String(describing: entry.player2Points!)
        cell.labelPlayer3Points.text = String(describing: entry.player3Points!)
        
        return cell
    }
    
    func getTopPerformer(){
        
        print("running tournamnet")
        self.statList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_League_TopPerformer,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                       "LeagueId" : leagueId!] as [String : Any],
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
                                    let TeamRank = element["TeamRank"] as? Int
                                    let UserTeamName = element["UserTeamName"] as? String
                                    let Player1 = element["Player1"] as? String
                                    let Player2 = element["Player2"] as? String
                                    let Player3 = element["Player3"] as? String
                                    let Player1Points = element["Player1Points"] as? Int
                                    let Player2Points = element["Player2Points"] as? Int
                                    let Player3Points = element["Player3Points"] as? Int
                                    
                                    self.statList.append(LeagueStatsModel(rank: TeamRank, teamName: UserTeamName, playerName1: Player1, playerName2: Player2, playerName3: Player3, player1Points: Player1Points, player2Points: Player2Points, player3Points: Player3Points))
                                }
                            }
                        }
                        
                        print(self.statList)
                        self.tableLeagueStats.reloadData()
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
    
    func getTopFavourite(){
        
        print("running tournamnet")
        self.statList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_League_TopFav,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                       "LeagueId" : leagueId!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : ""])
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
                                    let Player1 = element["Player1"] as? String
                                    let Player2 = element["Player2"] as? String
                                    let Player3 = element["Player3"] as? String
                                    let Player1Points = element["Player1Match"] as? Int
                                    let Player2Points = element["Player2Match"] as? Int
                                    let Player3Points = element["Player3Match"] as? Int
                                    
                                    self.statList.append(LeagueStatsModel(rank: TeamRank, teamName: UserTeamName, playerName1: Player1, playerName2: Player2, playerName3: Player3, player1Points: Player1Points, player2Points: Player2Points, player3Points: Player3Points))
                                }
                            }
                        }
                        print(self.statList)
                        self.tableLeagueStats.reloadData()
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
