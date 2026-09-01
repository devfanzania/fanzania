//
//  LiveScoreLeagueTeamsTableViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 01/06/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class LiveScoreLeagueTeamsTableViewCell: UITableViewCell {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var leagueTeamJumpButton: UIButton!
    @IBOutlet weak var leagueTeamRank: UILabel!
    @IBOutlet weak var teamNameLabel: UILabel!
    @IBOutlet weak var teamOwnerLabel: UILabel!
    @IBOutlet weak var teamTransferCountLabel: UILabel!
    @IBOutlet weak var teamTotalPointsLabel: UILabel!
    @IBOutlet weak var teamLastMatchPointsLabel: UILabel!
    @IBOutlet weak var teamDetailsOpenButton: UIButton!
    @IBOutlet weak var powerPlayUsedButton: UIButton!
    
    let whiteDot = UIImage(named: "white-circle")
    let green_arrow = UIImage(named: "up-green-arrow")
    let red_Arrow = UIImage(named: "down-red-arrow")
    
    var dataSource:LiveLeagueDetailsModel? {
        didSet{
            if let dataSource = dataSource {
                
                let string = (dataSource.TeamNewStanding != 0) ?  String(describing: (dataSource.TeamNewStanding)) : "-"
                var rankAdvanced:String = ""
                if dataSource.TeamNewStanding > dataSource.TeamOldStanding && dataSource.TeamOldStanding > 0 {
                    rankAdvanced = "(\(dataSource.TeamOldStanding - dataSource.TeamNewStanding))"
                    leagueTeamJumpButton.setImage(red_Arrow, for: .normal)
                }else if dataSource.TeamNewStanding < dataSource.TeamOldStanding && dataSource.TeamOldStanding > 0{
                    rankAdvanced = "(+\(dataSource.TeamOldStanding - dataSource.TeamNewStanding))"
                    leagueTeamJumpButton.setImage(green_arrow, for: .normal)
                }else {
                    leagueTeamJumpButton.setImage(whiteDot, for: .normal)
                }
                let myMutableString = NSMutableAttributedString(string: string+rankAdvanced, attributes: [:])

                if let userId = dataSource.userId, userId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) {
                    backgroundColor = UIColor.colorAppPrimary()()
                    leagueTeamRank.textColor = UIColor.white
                    teamNameLabel.textColor = UIColor.white
                    teamTotalPointsLabel.textColor = UIColor.white
                    teamLastMatchPointsLabel.textColor = UIColor.white
                    teamTransferCountLabel.textColor = UIColor.white
                    teamOwnerLabel.textColor = UIColor.white
                    //btnLeagueAdmin.setImage(admin, for: .normal)
                    //btnTeamPreview.setImage(right_arrow_white, for: .normal)
                }else{
                    backgroundColor = UIColor.white
                    leagueTeamRank.textColor = UIColor.black
                    teamNameLabel.textColor = UIColor.black
                    teamTotalPointsLabel.textColor = UIColor.black
                    teamLastMatchPointsLabel.textColor = UIColor.black
                    teamTransferCountLabel.textColor = UIColor.black
                    teamOwnerLabel.textColor = UIColor.black
                    //btnLeagueAdmin.setImage(admin_black, for: .normal)
                    //btnTeamPreview.setImage(right_arrow_black, for: .normal)
                }
                
                leagueTeamRank.attributedText = myMutableString
                teamNameLabel.text = (dataSource.Team != nil) ? (dataSource.Team) : "-"
                teamTotalPointsLabel.text = String(describing: dataSource.Points)
                teamTransferCountLabel.text = "\(dataSource.transfers ?? 0)|\(dataSource.TransferUsed ?? 0)"
                teamOwnerLabel.text = dataSource.owner
                teamLastMatchPointsLabel.text = "\(dataSource.matchPoints)"
                switch dataSource.powerPlay {
                case .none:
                    powerPlayUsedButton.setImage(nil, for: .normal)
                    powerPlayUsedButton.setTitle("", for: .normal)
                default :
                    powerPlayUsedButton.setTitle("", for: .normal)
                    powerPlayUsedButton.setImage(dataSource.powerPlay?.image, for: .normal)
                }
            }
        }
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
