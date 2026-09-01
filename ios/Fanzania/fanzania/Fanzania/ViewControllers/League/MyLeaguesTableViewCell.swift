//
//  MyLeaguesTableViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 20/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit

class MyLeaguesTableViewCell: UITableViewCell {

    @IBOutlet weak var leagueName: UILabel!
    @IBOutlet weak var leagueStanding: UILabel!
    @IBOutlet weak var leagueGlobalRank: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
    }

}
class MyPointsHistoryTableViewCell: UITableViewCell {
    @IBOutlet weak var tournamentName: UILabel!
    @IBOutlet weak var myTeams: UILabel!
    @IBOutlet weak var myPoints: UILabel!
}

class PlayerSelectionListTableViewCell : UITableViewCell {
    
    @IBOutlet weak var PlayerRoleImg: UIImageView!
    @IBOutlet weak var PlayerImage: UIImageView!
    @IBOutlet weak var labelPlayerName: UILabel!
    @IBOutlet weak var labelPlayerPrice: UILabel!
    @IBOutlet weak var labelPlayerPoints: UILabel!
    @IBOutlet weak var playerCountryFlag: UIImageView!
    @IBOutlet weak var playerCountryName: UILabel!
    @IBOutlet weak var btnCellMenu: UIButton!
    @IBOutlet weak var overseasPlayerIndicatorImageView: UIImageView!
    
    var dataSource:PlayerInfo?
    var tapMenuAction: (()->Void)?
    
    @IBAction func tappedMenu(_ sender: UIButton) {
        tapMenuAction?()
    }
}

class TopTenPlayerListTableViewCell : UITableViewCell {
    
    @IBOutlet var labelRank: UILabel!
    @IBOutlet var labelPlayerName: UILabel!
    @IBOutlet var labelPlayersTeam: UILabel!
    @IBOutlet var labelPointsScored: UILabel!
}

class RecentCaptaincyTableViewCell : UITableViewCell {
    
    @IBOutlet weak var labelMatch: UILabel!
    @IBOutlet weak var labelMatchName: UILabel!
    @IBOutlet weak var labelCaptainName: UILabel!
    @IBOutlet weak var labelPoints: UILabel!
    
}

class LeagueStatsTableViewCell : UITableViewCell {
    
    @IBOutlet weak var labelRank: UILabel!
    @IBOutlet weak var labelLeagueTeam: UILabel!
    @IBOutlet weak var Player1: UILabel!
    @IBOutlet weak var Player2: UILabel!
    @IBOutlet weak var Player3: UILabel!
    @IBOutlet weak var labelPlayer1Points: UILabel!
    @IBOutlet weak var labelPlayer2Points: UILabel!
    @IBOutlet weak var labelPlayer3Points: UILabel!
}

class LiveScoreTableScore : UITableViewCell {
    
    @IBOutlet weak var imgRole: UIImageView!
    @IBOutlet weak var labelName: UILabel!
    @IBOutlet weak var labelBattingPoints: UILabel!
    @IBOutlet weak var labelBowlingPoints: UILabel!
    @IBOutlet weak var labelFieldingPoints: UILabel!
    @IBOutlet weak var labelTotalPoints: UILabel!
    @IBOutlet weak var captaincyTypeImageView: UIImageView!
    
    var dataSource:LiveScoreTableModel? {
        didSet{
            if let dataSource = dataSource {
                labelName.text = dataSource.Name!
                labelBattingPoints.text = String(describing: (dataSource.BatingPoints)!)
                labelBowlingPoints.text = String(describing: (dataSource.BowlPoints)!)
                labelFieldingPoints.text = String(describing: (dataSource.FieldPoints)!)
                labelTotalPoints.text = String(describing: (dataSource.TotalPoints)!)
                imgRole.image = dataSource.Speciality?.image
                captaincyTypeImageView.image = dataSource.captaincyType?.image
                self.isSelected = dataSource.isSelected ? true : false
                layoutIfNeeded()
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
}

class LiveTrackLeague : UITableViewCell {
    @IBOutlet weak var leagueName: UILabel!
    @IBOutlet weak var leagueOwner: UILabel!
}

class TournamentStatsTopPlayerTableViewCell : UITableViewCell  {
    @IBOutlet weak var labelRank: UILabel!
    @IBOutlet weak var labelName: UILabel!
    @IBOutlet weak var labelTeam: UILabel!
    @IBOutlet weak var labelPoints: UILabel!
}
class TournamentStatsTopTeamsTableViewCell : UITableViewCell {
    @IBOutlet weak var labelRank: UILabel!
    @IBOutlet weak var labelTeam: UILabel!
    @IBOutlet weak var labelOwner: UILabel!
    @IBOutlet weak var labelPoints: UILabel!
}
class TournamentStatsTopLeaguesTableViewCell : UITableViewCell  {
    @IBOutlet weak var labelRank: UILabel!
    @IBOutlet weak var labelLeague: UILabel!
    @IBOutlet weak var labelOwner: UILabel!
    @IBOutlet weak var labelPoints: UILabel!
}

class TeamSelectionTableViewCell : UITableViewCell {
    @IBOutlet weak var labelTeamName : UILabel!
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // update UI
        accessoryType = selected ? .checkmark : .none
        self.layer.backgroundColor = UIColor.white.cgColor
    }
}
