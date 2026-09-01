//
//  LiveUserTeamViewController.swift
//  Fanzania
//
//  Created by Tathagata Dey on 02/06/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class LiveUserTeamViewController: UIViewController {

    @IBOutlet weak var matchPointsLabel: UILabel!
    @IBOutlet weak var userTeamTableView: UITableView!
    @IBOutlet weak var totalPointsLabel: UILabel!
    @IBOutlet weak var powerPlayUsedImageView: UIImageView!
    
    var tappedLiveUserLeagueDetail:LiveLeagueDetailsModel?

    var userTeam : [LiveScoreTableModel] = []
    
    var currentTournament:UserTournamentModel?
    var currentMatch:LiveMatchModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        userTeamTableView.delegate = self
        userTeamTableView.dataSource = self
        powerPlayUsedImageView.image = tappedLiveUserLeagueDetail?.powerPlay?.image
        
//        let label = UILabel(frame: CGRect(x:0, y:0, width:400, height:50))
//        label.backgroundColor = .clear
//        label.numberOfLines = 2
//        label.font = UIFont.boldSystemFont(ofSize: 14.0)
//        label.textAlignment = .center
//        label.textColor = .white
//        if let liveUserLeagueDetail = tappedLiveUserLeagueDetail {
//            label.text = "\(liveUserLeagueDetail.Team!)\n\(liveUserLeagueDetail.owner!)"
//            matchPointsLabel.text = "\(liveUserLeagueDetail.matchPoints)"
//            totalPointsLabel.text = "\(liveUserLeagueDetail.Points)"
//        }
//        self.navigationItem.titleView = label
        
        if let liveUserLeagueDetail = tappedLiveUserLeagueDetail {
            self.setTitle(liveUserLeagueDetail.Team!)
            matchPointsLabel.text = "\(liveUserLeagueDetail.matchPoints)"
            totalPointsLabel.text = "\(liveUserLeagueDetail.Points)"
        }
        //getLiveScore()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationSetup()
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17.0)], for: UIControl.State.normal)
        
        let rightNavBarItem = UIBarButtonItem(title: "Live", style: UIBarButtonItem.Style.plain, target: self, action: nil)
        navigationItem.rightBarButtonItem = rightNavBarItem
        navigationItem.rightBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 17.0)], for: UIControl.State.normal)
    }
    
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }
    
    func getLiveUserTeam(){
        
        guard let liveUserLeagueDetail = tappedLiveUserLeagueDetail else { return }
        print("running tournamnet")
        self.userTeam.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param = ["TournamentId" : (currentTournament?.id)!,
                     "MatchId" : (currentMatch?.id)!,
                     "UserTeamId" : liveUserLeagueDetail.userTeamId!,
                     "UserId" : liveUserLeagueDetail.userId!] as [String : Any]
        print(param)
        Alamofire.request(URL_Live_UserTeamScore,
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
                                    let TournamentId = element["TournamentId"] as? Int
                                    let PlayerName = element["PlayerName"] as? String
                                    let PlayerRole:PlayerSpeciality = PlayerSpeciality(role: element["PlayerSpeciality"] as? String)
                                    let BattingPoints = element["BattingPoints"] as? Int
                                    let BowlingPoints = element["BowlingPoints"] as? Int
                                    let FieldingPoints = element["FieldingPoints"] as? Int
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as? String
                                    let Vcapt = element["VCapt"] as! Int
                                    let Capt = element["Capt"] as! Int
                                    
                                    var captaincy : Captaincy?
                                    if Vcapt == 1 {
                                        captaincy = Captaincy.ViceCaptain
                                    } else if Capt == 1 {
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
                                    
                                    self.userTeam.append(LiveScoreTableModel(Speciality: PlayerRole, Name: PlayerName, BatingPoints: BattingPoints, BowlPoints: BowlingPoints, FieldPoints: FieldingPoints, TotalPoints: TotalPoints, ParticipationTeamName: ParticipationTeamName, captaincyType:captaincy, isSelected: isSelected))
                                    
                                }
                            }
                        }
                        
                        self.userTeamTableView.reloadData()
                        
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

    func getLiveScore(){
        
        print("running tournamnet")
        // let loader = UIViewController.displaySpinner(onView: self.view)
        let param = ["TournamentId" : (currentTournament?.id)!,
                     "MatchId" : (currentMatch?.id)!,
                     "UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any]
        print(param)
        Alamofire.request(URL_Live_MatcheScore,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : ""])
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
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let CurrentMatchPoints = element["CurrentMatchPoints"] as! Int
                                    let TotalPoints = element["AllTotalPoints"] as! Int
                                    self.matchPointsLabel.text = "\(CurrentMatchPoints)"
                                    self.totalPointsLabel.text = "\(TotalPoints)"
                                    
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

extension LiveUserTeamViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return userTeam.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = userTeamTableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! LiveScoreTableScore
        cell.dataSource = userTeam[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 35.0
    }
    
}
