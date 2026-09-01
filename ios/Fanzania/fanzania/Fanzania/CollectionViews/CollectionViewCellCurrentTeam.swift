//
//  CollectionViewCellCurrentTeam.swift
//  Fanzania
//
//  Created by Tathagata Dey on 12/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class CollectionViewCellCurrentTeam: UICollectionViewCell {
    
    var transferLeft:Int?
    var team1:String?
    var team2:String?
    var usedPowerPlay:PowerPlayTypes?
    var allocations:[PlayerInfo] = []
    var viceCaptainId:Int?
    var captainId:Int?
    var SubsLeft:Int?
    var userTeam:MyTeamsModel?
    @IBOutlet weak var containerView: UIView!
    
    var currentTournament:UserTournamentModel? {
        didSet{
            if let currentTournament = currentTournament {
                self.getCurrentStelthTeamInfo(currentTournament: currentTournament) //hard coded for last match details
            }
        }
    }
    
    var savedAutoCaptain = 0 {
        didSet{
            if savedAutoCaptain == 1 {
                autoCaptianSwitch.setOn(true, animated: true)
            }else{
                autoCaptianSwitch.setOn(false, animated: true)
            }
        }
    }
    
    var savedNitro = 0 {
        didSet{
            if savedNitro == 1 {
                nitroSwitch.setOn(true, animated: true)
            }else{
                nitroSwitch.setOn(false, animated: true)
            }
        }
    }
    
    var savedPainKiller = 0 {
        didSet{
            if savedPainKiller == 1 {
                painkillerSwitch.setOn(true, animated: true)
            }else{
                painkillerSwitch.setOn(false, animated: true)
            }
        }
    }
    
    var autoPilotLeft = 1 {
        didSet{
            if autoPilotLeft == 0 {
                autoCaptianSwitch.setOn(true, animated: true)
                autoCaptianSwitch.isEnabled = false
            }
        }
    }
    
    var painkillerLeft = 1 {
        didSet{
            if painkillerLeft == 0 {
                painkillerSwitch.setOn(true, animated: true)
                painkillerSwitch.isEnabled = false
            }
        }
    }
    
    var nitroLeft = 1 {
        didSet{
            if nitroLeft == 0 {
                nitroSwitch.setOn(true, animated: true)
                nitroSwitch.isEnabled = false
            }
        }
    }
    
    
    
    
    var delegate:VCMyLeagueDetails?
    
    @IBOutlet weak var transferLeftLabel: UILabel!
    @IBOutlet weak var painkillerSwitch: UISwitch!
    @IBOutlet weak var nitroSwitch: UISwitch!
    @IBOutlet weak var autoCaptianSwitch: UISwitch!
    @IBOutlet weak var Ground: FieldOutlet!
    
    
    override func awakeFromNib() {
        nitroSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        painkillerSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        autoCaptianSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        
        transferLeftLabel.backgroundColor = UIColor.colorCrimson()
        transferLeftLabel.layer.cornerRadius = 5.0
        transferLeftLabel.layer.masksToBounds = true
        
        if GlobalVars.shared.isIpad() {
//            groundWidthConstranint.constant = 500
            layoutIfNeeded()
        }
    }
    
    func getCurrentStelthTeamInfo(currentTournament:UserTournamentModel){
        
        guard let userTeam = userTeam else {return}
        //let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_StelthMode,
                          method: .post,
                          parameters: ["UserTeamId" : userTeam.UserTeamId!,
                                       "TournamentId" : currentTournament.id!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
       //             UIViewController.removeSpinner(spinner: loader)
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
                        
                        var totalBudget = 0
                        var usedNitro = 0
                        var usedAutoCaptain = 0
                        var usedPainKiller = 0
                        
                        var nitroLeft = 0
                        var autoPilotLeft = 0
                        var painKillerLeft = 0
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                
                                if let element = item as? [String:Any] {
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let playerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    totalBudget += PlayerValue
                                    let PlayerShortName = element["PlayerShortName"] as? String
                                    //                                    let PlayerPoints = element["PlayerPoints"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    let TeamImage = element["TeamImage"] as? String
                                    //let PlayerImageName = element["PlayerImage"] as? String
                                    let teamShortName = element["TeamShortName"] as! String
//                                    self.currentTeamSubstituteLeft = element["SubsLeft"] as! Int
//                                    self.cutOffTeamSubstituteLeft = element["SubsLeftAtSnapShot"] as! Int
                                    self.captainId = element["TeamCapt"] as? Int
                                    self.viceCaptainId = element["TeamVCapt"] as? Int
                                    self.SubsLeft = element["SubsLeft"] as? Int
                                    usedNitro = element["NitroUsed"] as! Int
                                    usedAutoCaptain = element["AutoPilotUsed"] as! Int
                                    usedPainKiller = element["PainKillerUsed"] as! Int
                                    nitroLeft = element["NitroLeft"] as! Int
                                    autoPilotLeft = element["AutoPilotLeft"] as! Int
                                    painKillerLeft = element["PainKillerLeft"] as! Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    
                                    let player = PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: playerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: 0, isPlayerSelected: true, shortName:PlayerShortName, playerImageName:TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction)
                                    
                                    self.allocations.append(player)
                                }
                            }
                            
                            if self.allocations.count == 0 {
                                self.containerView.isHidden = true
                                return
                            }
                            
                            self.populateField()
                            self.savedNitro = usedNitro
                            self.savedAutoCaptain = usedAutoCaptain
                            self.savedPainKiller = usedPainKiller
                            self.painkillerLeft = painKillerLeft
                            self.autoPilotLeft = autoPilotLeft
                            self.nitroLeft = nitroLeft
                            if let tournament = self.currentTournament {
                                if tournament.status != "INPROGRESS" {
                                    self.transferLeftLabel.text = "∞"
                                } else {
                                    self.transferLeftLabel.text = "\(self.SubsLeft!)"
                                }
                            }
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)

                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.delegate?.dismiss(animated: true, completion: nil)
                        }))
                        self.delegate?.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func populateField(){
        Ground.isShowingAccountableNumber = .none
        Ground.dataSource = (list:allocations, teamCaptId:self.captainId!, teamVCaptId:self.viceCaptainId!)
    }

}
