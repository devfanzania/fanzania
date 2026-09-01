//
//  LeagueMembersListTableViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 19/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import SDWebImage

class LeagueMembersListTableViewCell: UITableViewCell {

    @IBOutlet weak var labelTeamRank: UILabel!
    @IBOutlet weak var labelTeamName: UILabel!
    @IBOutlet weak var labelTeamPoints: UILabel!
    @IBOutlet weak var imgViewJersey: UIImageView!
    @IBOutlet weak var labelTeamTransfer: UILabel!
    @IBOutlet weak var btnTeamPreview: UIButton!
    @IBOutlet weak var labelTeamOwner: UILabel!
    @IBOutlet weak var btnLeagueIndicator: UIButton!
    @IBOutlet weak var btnRankAdvancement: UIButton!
    @IBOutlet weak var labelLastMatchPoints: UILabel!
    
    let whiteDot = UIImage(named: "white-circle")
    let green_arrow = UIImage(named: "up-green-arrow")
    let red_Arrow = UIImage(named: "down-red-arrow")
    let ic_exclamation = UIImage(named: "ic_exclamation")
    var showTeamPreview:((Int)->Void)!
    var currentLeague:MyLeaguesModel?
    var currentTournament:UserTournamentModel?
    
    var dataSource:MyTeamsModel?{
        didSet{
            if let dataSource = dataSource {
                
                if let supportedTeam = dataSource.SupportedTeam,
                   let imageURL = URL(string: "\(URL_SERVER_IMAGE_LOCATION_PlayerImage)\(supportedTeam)") {
                    imgViewJersey.sd_setImage(with: imageURL, placeholderImage: nil)
                } else {
                    imgViewJersey.image = nil
                }
                
                labelTeamName.text = (dataSource.UserTeamName != nil) ? (dataSource.UserTeamName) : "-"
                labelTeamPoints.text = (dataSource.TotalPoints != nil) ? String(describing: (dataSource.TotalPoints)!) : "-"
                if let currentTournament = currentTournament {
                    if currentTournament.status != "INPROGRESS" {
                        labelTeamTransfer.text = "∞"
                    } else {
                        labelTeamTransfer.text = (dataSource.SubsLeft != nil) ? String(describing:(dataSource.SubsLeft)!) : "-"
                    }
                }
                labelTeamOwner.text = dataSource.FullName
                labelLastMatchPoints.text = "\((dataSource.LastMatchPoints))"
                btnTeamPreview.tag = (dataSource.UserTeamId)!
                btnTeamPreview.addTarget(self, action: #selector(actionPreview), for: .touchUpInside)
                
                if let currentRank = dataSource.TeamCurrentStanding, let oldRank = dataSource.TeamOldStanding {
                    
                    let string = (currentRank > 0) ?  String(describing: currentRank ) : "-"
                    var rankAdvanced:String = ""
                    
                    if currentRank > oldRank && oldRank > 0 && currentRank > 0 {
                        rankAdvanced = "(\(oldRank - currentRank))"
                        btnRankAdvancement.setImage(red_Arrow, for: .normal)
                    }else if currentRank < oldRank && oldRank > 0  && currentRank > 0 {
                        rankAdvanced = "(+\(oldRank - currentRank))"
                        btnRankAdvancement.setImage(green_arrow, for: .normal)
                    }else {
                        btnRankAdvancement.setImage(whiteDot, for: .normal)
                    }
                    if currentRank > 0 {
                        let myMutableString = NSMutableAttributedString(string: string+rankAdvanced, attributes: [:])
                        labelTeamRank.attributedText = myMutableString
                    }else{
                        labelTeamRank.text = string
                    }
                }
                
                //            myMutableString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.colorGreen, range: NSRange(location:1,length:3))
                
                
                let admin = UIImage(named: "admin")
                
                if dataSource.UserId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) {
                    backgroundColor = UIColor.colorDeselected()()
                    labelTeamRank.textColor = UIColor.black
                    labelTeamName.textColor = UIColor.black
                    labelTeamPoints.textColor = UIColor.black
                    labelLastMatchPoints.textColor = UIColor.black
                    labelTeamTransfer.textColor = UIColor.black
                    labelTeamOwner.textColor = UIColor.black
                    //btnLeagueAdmin.setImage(admin, for: .normal)
                    //btnTeamPreview.setImage(right_arrow_white, for: .normal)
                }else{
                    backgroundColor = UIColor.white
                    labelTeamRank.textColor = UIColor.colorAppPrimary()()
                    labelTeamName.textColor = UIColor.colorAppPrimary()()
                    labelTeamPoints.textColor = UIColor.colorAppPrimary()()
                    labelLastMatchPoints.textColor = UIColor.colorAppPrimary()()
                    labelTeamTransfer.textColor = UIColor.colorAppPrimary()()
                    labelTeamOwner.textColor = UIColor.colorAppPrimary()()
                    //btnLeagueAdmin.setImage(admin_black, for: .normal)
                    //btnTeamPreview.setImage(right_arrow_black, for: .normal)
                }
                
                if currentLeague?.LeagueLeaderId == dataSource.UserId {
                    btnLeagueIndicator.isHidden = false
                    btnLeagueIndicator.setImage(admin, for: .normal)
                }else{
                    btnLeagueIndicator.isHidden = true
                    btnLeagueIndicator.setImage(nil, for: .normal)
                }
                
                if currentLeague?.LeagueLeaderId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) && currentTournament?.status != "COMPLETE" {
                    
                    if dataSource.UserId != UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) {
                        
                        if dataSource.status != "Approved" {
                            btnLeagueIndicator.isHidden = false
                            btnLeagueIndicator.setImage(ic_exclamation, for: .normal)
                        }
                    }
                }
            }
        }
    }
    
    @objc func actionPreview(){
        self.showTeamPreview((dataSource?.UserTeamId)!)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}
