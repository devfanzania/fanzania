//
//  CollectionViewCellLastMatchPoints.swift
//  Fanzania
//
//  Created by Tathagata Dey on 12/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class CollectionViewCellLastMatchPoints: UICollectionViewCell {
    
    var lastMatchPoints:Int?
    var team1:String?
    var team2:String?
    var usedPowerPlay:PowerPlayTypes?
    var userTeam:MyTeamsModel?
    var delegate:VCMyLeagueDetails?
    var allocations:[PlayerInfo] = []
    var captainId:Int?
    var viceCaptainId:Int?
    
    var currentTournament:UserTournamentModel? {
        didSet{
            getMatchSpecificTeamsInfo(matchId: 0)      //hard coded for last match details
        }
    }
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var matchTeamsLabel: UILabel!
    @IBOutlet weak var matchPoints: UILabel!
    @IBOutlet weak var powerPlayNameLabel: UILabel!
    @IBOutlet weak var powerPlayIcon: UIImageView!
    @IBOutlet weak var Ground: FieldOutlet!
    
    override func awakeFromNib() {
        
        matchPoints.backgroundColor = UIColor.colorCrimson()
        matchPoints.layer.cornerRadius = 5.0
        matchPoints.layer.masksToBounds = true
        matchTeamsLabel.text = "Match Points"
    }
    
    func getMatchSpecificTeamsInfo(matchId : Int){
        
        print("running match")
        guard let userTeam = userTeam, let currentTournament = currentTournament else {return}
        //let loader = UIViewController.displaySpinner(onView: self.)
        allocations.removeAll()
        Alamofire.request(URL_Match_PostMatchDetails,
                          method: .post,
                          parameters: ["UserTeamId" : userTeam.UserTeamId!,
                                       "MatchId" : matchId,
                                       "TournamentId" : currentTournament.id!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
         //           UIViewController.removeSpinner(spinner: loader)
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
                        
                        var MatchTotalPoints:Int = 0
                        var tempNitroUsed:Int = 0
                        var tempAutoPilot:Int = 0
                        var tempPainKiller:Int = 0
                        var LastMatchTeams:String = ""
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                
                                if let element = item as? [String:Any] {
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerShortName = element["PlayerShortName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let PlayerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let PlayerPoints = element["PlayerPoints"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    LastMatchTeams = element["LastMatchTeams"] as! String
                                    let teamShortName = element["TeamShortName"] as? String
                                    let TeamImage = element["TeamImage"] as? String
                                    //let PlayerImageName = element["PlayerImage"] as? String
                                    self.captainId = element["TeamCapt"] as? Int
                                    MatchTotalPoints = element["MatchTotalPoints"] as! Int
                                    self.viceCaptainId = element["TeamVCapt"] as? Int
                                    tempNitroUsed = element["NitroMultiplier"] as! Int
                                    tempAutoPilot = element["AutoPilotUsed"] as! Int
                                    tempPainKiller = element["PainKillerUsed"] as! Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    self.allocations.append(PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: PlayerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: PlayerPoints, isPlayerSelected: true, shortName:PlayerShortName, playerImageName:TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction))
                                }
                            }
                            
                            if self.allocations.count == 0 {
                                self.containerView.isHidden = true
                                return
                            }
                            
                            self.matchPoints.text = String(describing: MatchTotalPoints)
                            self.matchTeamsLabel.text = LastMatchTeams
                            
                            
                            if tempAutoPilot == 1 {
                                self.usedPowerPlay = .AutoCaptain
                            }else if tempNitroUsed == 1 {
                                self.usedPowerPlay = .NitroBooster
                            }else if tempPainKiller == 1 {
                                self.usedPowerPlay = .PainKiller
                            }else{
                                self.usedPowerPlay = nil
                            }
                            
                            switch self.usedPowerPlay {
                            case .some(self.usedPowerPlay):
                                self.powerPlayNameLabel.text = self.usedPowerPlay?.name
                                self.powerPlayIcon.image = self.usedPowerPlay?.image
                            default:
                                self.powerPlayNameLabel.text = "-"
                                self.powerPlayIcon.image = nil
                            }
                            
                            self.populateField()
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.delegate?.dismiss(animated: true, completion: nil)
                        }))
                        self.delegate?.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func populateField(){
    
        Ground.dataSource = (list:allocations, teamCaptId:self.captainId, teamVCaptId:self.viceCaptainId)
    }
}
