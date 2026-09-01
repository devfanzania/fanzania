//
//  VCLiveScores.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 29/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

struct LiveScoreTableModel {
    var Speciality:PlayerSpeciality?
    var Name:String?
    var BatingPoints : Int?
    var BowlPoints:Int?
    var FieldPoints:Int?
    var TotalPoints:Int?
    var ParticipationTeamName:String?
    var captaincyType:Captaincy?
    var isSelected:Bool = false
}

class VCLiveScores: UIViewController, UITabBarDelegate, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet var tableViewLiveScore: UITableView!
    
    let refreshControl = UIRefreshControl()
    
    var currentTournament:UserTournamentModel?
    var currentMatch:LiveMatchModel? {
        didSet{
            if let currentMatch = currentMatch {
                team1Button.setTitle(currentMatch.team1ShortName, for: .normal)
                team2Button.setTitle(currentMatch.team2ShortName, for: .normal)
            }
        }
    }
    
    var liveScoreListTeam1:[LiveScoreTableModel] = []
    var liveScoreListTeam2:[LiveScoreTableModel] = []

    @IBOutlet weak var viewTeam1Header: UIView!
    @IBOutlet weak var team1Button: UIButton!
    @IBOutlet weak var team2Button: UIButton!
    
    var team1Open:Bool = true {
        didSet{
            if team1Open {
                team1Button.backgroundColor = UIColor.black
                team2Button.backgroundColor = UIColor.gray
            }else{
                team1Button.backgroundColor = UIColor.gray
                team2Button.backgroundColor = UIColor.black
            }
            getLiveScore()
        }
    }
    
    let add_img = UIImage(named: "add-white")
    let minus_img = UIImage(named: "minus-white")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableViewLiveScore.delegate = self
        tableViewLiveScore.dataSource = self
        setupUI()
        
        refreshControl.addTarget(self, action: #selector(refreshData), for: UIControl.Event.valueChanged)
        tableViewLiveScore.refreshControl = refreshControl
        getLiveScore()

        //viewTeam1Header.setCurvedCornerBordered()
        //viewTeam2Header.setCurvedCornerBordered()
    }
    
    func setupUI(){
        team1Button.layer.cornerRadius = 15.0
        team2Button.layer.cornerRadius = 15.0
        team1Button.backgroundColor = UIColor.black
        team2Button.backgroundColor = UIColor.gray
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if team1Open {
            return liveScoreListTeam1.count
        }else{
            return liveScoreListTeam2.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableViewLiveScore.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! LiveScoreTableScore
        if team1Open {
            cell.dataSource = liveScoreListTeam1[indexPath.row]
        }else{
            cell.dataSource = liveScoreListTeam2[indexPath.row]
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 35.0
    }
    
    @IBAction func actionShowTeam1(_ sender: UIButton) {
        team1Open = true
    }
    
    @IBAction func actionShowTeam2(_ sender: UIButton) {
        team1Open = false
    }
    
    @objc func refreshData(){
        getLiveScore()
    }
    
    func getLiveScore(){
        
        print("running tournamnet")
        guard let currentTournament = currentTournament, let currentMatch = currentMatch else { return }
       // let loader = UIViewController.displaySpinner(onView: self.view)
        let param = ["TournamentId" : currentTournament.id!,
                     "MatchId" : currentMatch.id!,
                     "UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any]
        print(param)
        Alamofire.request(URL_Live_MatcheScore,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
//                DispatchQueue.main.async {
//                    UIViewController.removeSpinner(spinner: loader)
//                }
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
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray {
                            self.liveScoreListTeam1.removeAll()
                            self.liveScoreListTeam2.removeAll()
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let TournamentId = element["TournamentId"] as? Int
                                    let PlayerName = element["PlayerName"] as? String
                                    let PlayerRole:PlayerSpeciality = PlayerSpeciality(role: element["PlayerSpeciality"] as? String)
                                    let BattingPoints = element["BattingPoints"] as? Int
                                    let BowlingPoints = element["BowlingPoints"] as? Int
                                    let FieldingPoints = element["FieldingPoints"] as? Int
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as? String
                                    let Vcapt = element["VCapt"] as? Int
                                    let Capt = element["Capt"] as? Int
                                    
                                    var captaincy : Captaincy?
                                    if let Vcapt = Vcapt,  Vcapt == 1 {
                                        captaincy = Captaincy.ViceCaptain
                                    } else if let Capt = Capt, Capt == 1 {
                                        captaincy = Captaincy.Captain
                                    } else {
                                        captaincy = nil
                                    }
                                    
                                    let selected = element["PlayerSelected"] as! Int
                                    var isSelected = false
                                    if selected == 1 {
                                        isSelected = true
                                    } else {
                                        isSelected = false
                                    }
                                    
                                    if ParticipationTeamName == self.currentMatch?.team1 {
                                        self.liveScoreListTeam1.append(LiveScoreTableModel(Speciality: PlayerRole, Name: PlayerName, BatingPoints: BattingPoints, BowlPoints: BowlingPoints, FieldPoints: FieldingPoints, TotalPoints: TotalPoints, ParticipationTeamName: ParticipationTeamName, captaincyType:captaincy, isSelected: isSelected))
                                    } else{
                                        self.liveScoreListTeam2.append(LiveScoreTableModel(Speciality: PlayerRole, Name: PlayerName, BatingPoints: BattingPoints, BowlPoints: BowlingPoints, FieldPoints: FieldingPoints, TotalPoints: TotalPoints, ParticipationTeamName: ParticipationTeamName, captaincyType:captaincy, isSelected: isSelected))
                                    }
                                }
                            }
                            DispatchQueue.main.async {
                                self.tableViewLiveScore.reloadData()
                                self.refreshControl.endRefreshing()
                            }
                        }
                    } else{
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
