//
//  VCLeaguesMatesTeamView.swift
//  Fanzania
//
//  Created by Tathagata Dey on 27/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCLeaguesMatesTeamView: UIViewController, UIGestureRecognizerDelegate {

    var tap: UITapGestureRecognizer?
    
    @IBOutlet weak var viewPopUp: UIView!
    @IBOutlet weak var labelTeamNames: UILabel!
    @IBOutlet weak var labelTotalPoints: UILabel!
    @IBOutlet weak var labelPowerPlay: UILabel!
    
    @IBOutlet weak var labelWicketKeeperCount: UILabel!
    @IBOutlet weak var labelBatsmanCount: UILabel!
    @IBOutlet weak var labelAllrounderCount: UILabel!
    @IBOutlet weak var labelBowlerCount: UILabel!
    
    @IBOutlet var labelPlayerName:[UILabel]!
    @IBOutlet var labelPlayerPrice:[UILabel]!
    @IBOutlet var btnPlayerRole:[UIButton]!
    @IBOutlet var playerCaptaincy:[UIButton]!
    @IBOutlet var playerImage:[UIButton]!
    
    
    var currentTeamCapt:Int?
    var currentTeamViceCapt:Int?

    var leagueTeamId:Int?
    var tournamentId:Int?
    var teamName:String?
    var teamPoint:Int?
    var powerPlayUsed:String?
    
    var userTeam:[PlayerInfo] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
        viewPopUp.setCurvedCornerBordered()
        getUserTeam()
        // Do any additional setup after loading the view.
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !viewPopUp.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func populateGround(){
        
        //let imageDummy = UIImage(named: "player-dummy")
        var batsmanCount = 0
        var bowlerCount = 0
        var allrounderCount = 0
        var wicketkeeperCount = 0
        
        
        for (index,player) in userTeam.enumerated() {
            labelPlayerName[index].text = player.shortName!
            
            getPlayerImages(imagename: player.playerImageName, playerImageButton: playerImage[index])
            
            switch player.speciality{
            case .batsman :
                btnPlayerRole[index].setImage(PlayerSpeciality.batsman.image, for: .normal)
                batsmanCount+=1
            case .bowler :
                btnPlayerRole[index].setImage(PlayerSpeciality.bowler.image, for: .normal)
                bowlerCount+=1
            case .allrounder :
                btnPlayerRole[index].setImage(PlayerSpeciality.allrounder.image, for: .normal)
                allrounderCount+=1
            default :
                btnPlayerRole[index].setImage(PlayerSpeciality.wicketKeeper.image, for: .normal)
                wicketkeeperCount+=1
            }
            if player.id == currentTeamCapt {
                playerCaptaincy[index].setTitle("C", for: .normal)
            }else if player.id == currentTeamViceCapt {
                playerCaptaincy[index].setTitle("VC", for: .normal)
            }else{
                playerCaptaincy[index].setTitle("", for: .normal)
            }
        }
        labelWicketKeeperCount.text = String(describing: wicketkeeperCount)
        labelBatsmanCount.text = String(describing: batsmanCount)
        labelBowlerCount.text = String(describing: bowlerCount)
        labelAllrounderCount.text = String(describing: allrounderCount)
    }

    func getUserTeam(){
        
        print("running tournamnet")
        self.userTeam.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_LastSaved,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId,
                            "UserTeamId" : leagueTeamId] as [String : Any],
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
                            print(dataArray)
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerShortName = element["PlayerShortName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let playerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    let PlayerImageName = element["PlayerImage"] as? String
                                    let teamShortName = element["TeamShortName"] as! String
                                    self.currentTeamCapt = element["TeamCapt"] as? Int
                                    self.currentTeamViceCapt = element["TeamVCapt"] as? Int
                                    self.teamPoint = element["TotalPoints"] as? Int
                                    self.teamName = element["UserTeamName"] as? String
                                    self.currentTeamCapt = element["TeamCapt"] as? Int
                                    self.currentTeamViceCapt = element["TeamVCapt"] as? Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    
                                    self.userTeam.append(PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: playerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: 0, isPlayerSelected: true, shortName: PlayerShortName, playerImageName:PlayerImageName, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction))
                                    
                                }
                            }
                            self.populateGround()
                            if self.teamPoint == nil {
                                self.labelTotalPoints.text = "0"
                            }else{
                                self.labelTotalPoints.text = String(describing: self.teamPoint!)
                            }
                            self.labelTeamNames.text = self.teamName!
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

    func getPlayerImages(imagename:String?, playerImageButton : UIButton){
        
        let imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        
        if imagename != nil {
            
            let cachedImage = imageCache!.image(withIdentifier: imagename!)
            if cachedImage != nil{
                playerImageButton.setImage(cachedImage, for: UIControl.State.normal)
            }else{
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!+".png").responseImage { response in
                    
                    if let image = response.result.value {
                        print("image downloaded: \(image)")
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerImageButton.setImage(image, for: .normal)
                    }else{
                        playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
                    }
                }
            }
            
        }else{
            playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
        }
    }
}
