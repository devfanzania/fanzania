//
//  VCLiveLeagueDetails.swift
//  Fanzania
//
//  Created by Tathagata Dey on 30/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

struct LiveLeagueDetailsModel {
    var owner:String?
    var Team:String?
    var TeamOldStanding:Int
    var TeamNewStanding:Int
    var Points:Int
    let transfers:Int
    let matchPoints:Int
    let powerPlay:PowerPlayTypes?
    let userId:Int?
    let userTeamId:Int?
    let TransferUsed: Int?
}

class VCLiveLeagueDetails: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var currentTournament:UserTournamentModel?
    var currentLeague:MyLeaguesModel?
    var currentMatch:LiveMatchModel?
    
    var tappedLiveUserLeagueDetail:LiveLeagueDetailsModel?
    
    @IBOutlet weak var tableLiveLeagueDetails: UITableView!
    var leagueDetailsList:[LiveLeagueDetailsModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getLiveLeagueDetails()
        if let currentLeague = currentLeague {
            self.navigationItem.title = currentLeague.LeagueName!
        }
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }

//        var view = UIView(frame: CGRect(x: 0, y: 0, width: 50, height: 30))
//        view.backgroundColor = UIColor.colorGreen()
//        var label = UILabel(frame: CGRect(x: 0, y: 0, width: 30, height: 30))
//        label.text = "Live"
//        label.font = label.font.withSize(14.0)
//        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 15, height: 15))
//        imageView.image = UIImage(named: "ic_c")
//        view.addSubview(label)
//        view.addSubview(imageView)
//        let horizontalConstraint2 = NSLayoutConstraint(item: imageView, attribute: NSLayoutConstraint.Attribute.centerX, relatedBy: NSLayoutConstraint.Relation.equal, toItem: view, attribute: NSLayoutConstraint.Attribute.centerX, multiplier: 1, constant: 0)
//        let leading2Constraints = NSLayoutConstraint(item: imageView, attribute: .leading, relatedBy: .equal, toItem: view, attribute: .leading, multiplier: 1, constant: 0)
//        
//        let horizontalConstraint = NSLayoutConstraint(item: label, attribute: NSLayoutConstraint.Attribute.centerX, relatedBy: NSLayoutConstraint.Relation.equal, toItem: view, attribute: NSLayoutConstraint.Attribute.centerX, multiplier: 1, constant: 0)
//        let leading1Constraints = NSLayoutConstraint(item: label, attribute: .leading, relatedBy: .equal, toItem: imageView, attribute: .trailing, multiplier: 1, constant: 0)
//        view.addConstraints([horizontalConstraint,horizontalConstraint2,leading2Constraints,leading1Constraints])
//        var barButtonItem = UIBarButtonItem(customView: view)
//        
//        
//        self.navigationItem.rightBarButtonItem = barButtonItem
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationSetup()
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18.0)], for: UIControl.State.normal)
        
        let rightNavBarItem = UIBarButtonItem(title: "Live", style: UIBarButtonItem.Style.plain, target: self, action: nil)
        navigationItem.rightBarButtonItem = rightNavBarItem
        navigationItem.rightBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 17.0)], for: UIControl.State.normal)
    }
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? LiveUserTeamViewController {
            destination.currentTournament = currentTournament
            destination.currentMatch = currentMatch
            destination.tappedLiveUserLeagueDetail = tappedLiveUserLeagueDetail
            destination.getLiveUserTeam()
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return leagueDetailsList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableLiveLeagueDetails.dequeueReusableCell(withIdentifier: "cellTeamByLeague", for: indexPath) as! LiveScoreLeagueTeamsTableViewCell
        cell.dataSource = leagueDetailsList[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if let userId = leagueDetailsList[indexPath.row].userId, userId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) {
            tappedLiveUserLeagueDetail = leagueDetailsList[indexPath.row]
            performSegue(withIdentifier: "segueUserTeam", sender: self)
        } else {
            let storyboard = UIStoryboard(name: "TeamComparison", bundle: .main)
            let vc = storyboard.instantiateViewController(withIdentifier: "VCLiveComparisonBase") as! VCLiveComparisonBase
            let MyTeam = leagueDetailsList.first(where: { $0.userId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) })
            vc.MyTeamId = MyTeam?.userTeamId
            vc.OtherTeamId = leagueDetailsList[indexPath.row].userTeamId
            vc.TournamentId = currentTournament?.id
            vc.MatchId = currentMatch?.id
            
            vc.MyTeamTournamentTotalPts = MyTeam?.Points
            vc.OtherTeamTournamentTotalPts = leagueDetailsList[indexPath.row].Points
            
            vc.MyTeamName = MyTeam?.Team
            vc.OtherTeamName = leagueDetailsList[indexPath.row].Team
            
            vc.currentMatch = currentMatch
            
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    func getLiveLeagueDetails(){
        
        print("running tournamnet")
        self.leagueDetailsList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Live_LeagueUserPostion,
                          method: .post,
                          parameters: ["TournamentId" : (currentTournament?.id)!,
                                       "LeagueId" : (currentLeague?.LeagueId)!,
                                       "MatchId": (currentMatch?.id)!] as [String : Any],
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
                                    
                                    let UserName = element["UserName"] as? String
                                    let UserTeamName = element["UserTeamName"] as? String
                                    let TeamOldStanding = element["TeamOldStanding"] as! Int
                                    let TeamNewStanding = element["TeamNewStanding"] as! Int
                                    let TotalPoints = element["TotalPoints"] as! Int
                                    let transfers = element["Transfers"] as! Int
                                    let matchPoints = element["CurrentMatchPoints"] as! Int
                                    let userId = element["UserId"] as! Int
                                    let userTeamId = element["UserTeamId"] as! Int
                                    let TransferUsed = element["TransferUsed"] as! Int
                                    let powerPlay:PowerPlayTypes?
                                    if element["PowerPlay"] as! String == "NA" {
                                        powerPlay = nil
                                    }else{
                                        powerPlay = PowerPlayTypes(powerPlay: (element["PowerPlay"] as! String))
                                    }
                                    self.leagueDetailsList.append(LiveLeagueDetailsModel(owner: UserName, Team: UserTeamName, TeamOldStanding: TeamOldStanding, TeamNewStanding: TeamNewStanding, Points: TotalPoints, transfers: transfers, matchPoints: matchPoints, powerPlay: powerPlay, userId:userId, userTeamId:userTeamId, TransferUsed: TransferUsed))
                                }
                            }
                        }
                        self.tableLiveLeagueDetails.reloadData()
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
