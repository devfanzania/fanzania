//
//  VCPlayerProfile.swift
//  Fanzania
//
//  Created by Writayan Das on 21/04/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit
import MarqueeLabel
import Alamofire
import SDWebImage

class VCPlayerProfile: UIViewController {
    // MARK: - IBOutlets
    @IBOutlet weak var lblTournamentName: UILabel!
    @IBOutlet weak var lblPlayerSummary: MarqueeLabel!
    @IBOutlet weak var imgViewPlayer: UIImageView!
    @IBOutlet weak var lblPlayerStatSummary: UILabel!
    @IBOutlet weak var lblSelectedBy: UILabel!
    @IBOutlet weak var lblTotalPoints: UILabel!
    @IBOutlet weak var lblRunsScored: UILabel!
    @IBOutlet weak var lblWicketsTaken: UILabel!
    @IBOutlet weak var lblValueRank: UILabel!
    
    var ParticipationTeamId: Int?
    var PlayerId: Int?
    var playerProfile: PlayerProfileResponseModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        resetProfile()
        getPlayerProfile()
    }
    
    // MARK: - IBActions
    @IBAction func tappedClose(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    func getPlayerProfile() {
        
        guard let ParticipationTeamId = ParticipationTeamId,
              let PlayerId = PlayerId
        else {
            return
        }
        
        let params: [String: Any] = [
            "ParticipationTeamId": ParticipationTeamId,
            "PlayerId": PlayerId,
            "MatchId": 0
        ]
        
        debugPrint("fetching player profile")
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_PlayerProfile,
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("Player profile: \(response.result.value ?? "")")
            }
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
                    
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dictionary = jsonDictionary["data"] as? NSDictionary {
                            debugPrint(dictionary)
                            self.playerProfile = PlayerProfileResponseModel(dictionary: dictionary)
                            DispatchQueue.main.async {
                                self.setupPlayerProfile()
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
    
    func resetProfile() {
        lblTournamentName.text = nil
        lblPlayerSummary.text = nil
        imgViewPlayer.image = UIImage(named: "player-dummy")
        lblPlayerStatSummary.text = nil
        lblSelectedBy.text = nil
        lblTotalPoints.text = nil
        lblRunsScored.text = nil
        lblWicketsTaken.text = nil
        lblValueRank.text = nil
    }
    
    func setupPlayerProfile() {
        
        guard let playerProfile = playerProfile else {
            return
        }
        
        lblTournamentName.text = playerProfile.tournamentName
        
        
        var boldText = "\(playerProfile.playerName ?? "")".uppercased()
        var normalText  = " (\(playerProfile.teamShortName ?? "")) | \(playerProfile.playerSpeciality ?? "") | \(playerProfile.playerValue ?? 0)K".uppercased()
        var attrs = [NSAttributedString.Key.font : UIFont.systemFont(ofSize: 17.0), .foregroundColor: UIColor.white]
        var attributedString = NSMutableAttributedString(string: normalText, attributes: attrs)
        attrs[NSAttributedString.Key.font] = UIFont.boldSystemFont(ofSize: 17.0)
        var boldString = NSMutableAttributedString(string: boldText, attributes: attrs)
        boldString.append(attributedString)
        lblPlayerSummary.attributedText = boldString
        
        imgViewPlayer.sd_setImage(with: URL(string: playerProfile.imageURL ?? ""),
                                  placeholderImage: UIImage(named: "player-dummy"))
        
        normalText  = " Points | "
        attrs = [NSAttributedString.Key.font : UIFont.systemFont(ofSize: 15.0), .foregroundColor: UIColor.black]
        attributedString = NSMutableAttributedString(string: normalText, attributes: attrs)
        boldText = "\(playerProfile.playerTotalPoints ?? 0)"
        attrs[NSAttributedString.Key.font] = UIFont.boldSystemFont(ofSize: 15.0)
        boldString = NSMutableAttributedString(string: boldText, attributes: attrs)
        boldString.append(attributedString)
        
        normalText  = " Overall Rank"
        attrs = [NSAttributedString.Key.font : UIFont.systemFont(ofSize: 15.0), .foregroundColor: UIColor.black]
        attributedString = NSMutableAttributedString(string: normalText, attributes: attrs)
        boldText = "\(playerProfile.playerRank ?? 0)"
        attrs[NSAttributedString.Key.font] = UIFont.boldSystemFont(ofSize: 15.0)
        let boldString2 = NSMutableAttributedString(string: boldText, attributes: attrs)
        boldString.append(boldString2)
        boldString.append(attributedString)
        lblPlayerStatSummary.attributedText = boldString
        
        lblSelectedBy.text = "\(playerProfile.selectedBy ?? 0)% Teams"
        lblTotalPoints.text = "Total Points: \(playerProfile.playerPoints1!), \(playerProfile.playerPoints2!), \(playerProfile.playerPoints3!), \(playerProfile.playerPoints4!), \(playerProfile.playerPoints5!) points"
        lblRunsScored.text = "Runs Scored: \(playerProfile.playerRuns1!), \(playerProfile.playerRuns2!), \(playerProfile.playerRuns3!), \(playerProfile.playerRuns4!), \(playerProfile.playerRuns5!)"
        lblWicketsTaken.text = "Wickets Taken: \(playerProfile.playerWickets1!), \(playerProfile.playerWickets2!), \(playerProfile.playerWickets3!), \(playerProfile.playerWickets4!), \(playerProfile.playerWickets5!)"
        lblValueRank.text = "\(playerProfile.playerValueRank ?? 0) out of \(playerProfile.totalPlayers ?? 0) players"
    }
}
