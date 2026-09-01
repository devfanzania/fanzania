//
//  VCTrackLeague.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 29/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCTrackLeague: UIViewController, UITableViewDataSource, UITableViewDelegate {
    

    @IBOutlet weak var tableLiveLeagueTrack: UITableView!
    var currentTournament:UserTournamentModel?
    var currentMatch:LiveMatchModel?
    var currentLeague:MyLeaguesModel?
    var leagueId:Int?
    var leagueList:[MyLeaguesModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        // Do any additional setup after loading the view.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? VCLiveLeagueDetails {
            destination.currentTournament = currentTournament
            destination.currentLeague = currentLeague
            destination.currentMatch = currentMatch
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return leagueList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableLiveLeagueTrack.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! LiveTrackLeague
        cell.leagueName.text = leagueList[indexPath.row].LeagueName!
        if let leagueLeader = leagueList[indexPath.row].LeagueLeaderName {
            cell.leagueOwner.text = leagueLeader
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        currentLeague = leagueList[indexPath.row]
        self.performSegue(withIdentifier: "segueLiveLeagueDetails", sender: self)
    }
    
    func getLeagues(tournamentId:Int){
        guard let currentTournament = currentTournament else { return }
        
        print("print")
        self.leagueList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        var param:[String:Int] = [:]
        param = [UserDefaultData.UserId.rawValue : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                 "TournamentId" : tournamentId]
        Alamofire.request(URL_Leagues_ByTournament,
                          method: .post,
                          parameters: param,
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
                        print("json format mismatch")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    
                                    let LeagueName = element["LeagueName"] as! String
                                    let TeamStanding = element["TeamStanding"] as! Int
                                    let LeagueRank = element["LeagueRank"] as? Int
                                    let LeagueId = element["LeagueId"] as! Int
                                    let LeagueLeaderName = element["LeagueLeader"] as? String
                                    let LeaguePoints = element["LeaguePoints"] as! Int
                                    let TournamentId = element["TournamentId"] as! Int
                                    let LeaguePin = element["LeaguePin"] as! String
                                    let LeagueLeaderId = element["LeagueLeaderId"] as! Int
                                    
                                    /*
                                     let LeagueLeaderId = element["LeagueLeaderId"] as! Int
                                     let LeaguePin = element["LeaguePin"] as! Int
                                     
                                     let Status = element["Status"] as! Int
                                     let TeamPoints = element["TeamPoints"] as! Int
                                     
                                     
                                     let UserId = element["UserId"] as! Int
                                     let UserTeamName = element["UserTeamName"] as! Int
                                     */
                                    var entry = MyLeaguesModel(LeagueId: LeagueId, LeagueName: LeagueName, LeaguePoints: LeaguePoints, TeamStanding: TeamStanding, LeagueRank: LeagueRank, TournamentId: TournamentId, leaguePin: LeaguePin, LeagueLeaderId:LeagueLeaderId)
                                    entry.LeagueLeaderName = LeagueLeaderName
                                    self.leagueList.append(entry)
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.tableLiveLeagueTrack.reloadData()
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
