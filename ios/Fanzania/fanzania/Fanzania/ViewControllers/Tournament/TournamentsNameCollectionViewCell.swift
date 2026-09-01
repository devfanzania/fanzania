//
//  TournamentsNameCollectionViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 18/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit

class TournamentsNameCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var labelTournamentName: UILabel!
    @IBOutlet weak var labelTournamentDates: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        setNeedsLayout()
        layoutIfNeeded()
        let size = contentView.systemLayoutSizeFitting(layoutAttributes.size)
        var frame = layoutAttributes.frame
        frame.size.width = ceil(size.width)
        layoutAttributes.frame = frame
        return layoutAttributes
    }

}

class UpcomingTournamentsCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var imageViewUpcomingTournaments: UIImageView!
    @IBOutlet weak var labelTournamentName: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.roundedCornerCollectionViewCell()
    }
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        setNeedsLayout()
        layoutIfNeeded()
        let size = contentView.systemLayoutSizeFitting(layoutAttributes.size)
        var frame = layoutAttributes.frame
        frame.size.width = ceil(size.width)
        frame.size.height = ceil(size.height)
        layoutAttributes.frame = frame
        return layoutAttributes
    }
}

class LeagueNamesCell : UICollectionViewCell {
    
    @IBOutlet weak var labelLeagueName: UILabel!
    @IBOutlet weak var labelLeagueRank: UILabel!
    
    override var isSelected: Bool {
        didSet{
            if isSelected {
                self.backgroundColor = UIColor.colorAppPrimary()()
                labelLeagueName.textColor = UIColor.white
                labelLeagueRank.textColor = .white
            }else{
                self.backgroundColor = UIColor.white
                labelLeagueName.textColor = UIColor.colorAppPrimary()()
                labelLeagueRank.textColor = UIColor.colorAppPrimary()()
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.layer.cornerRadius = 10.0
        self.layer.borderColor = UIColor.colorAppPrimary()().cgColor
        self.layer.borderWidth = 1.0
        labelLeagueName.textColor = UIColor.colorAppPrimary()()
        labelLeagueRank.textColor = UIColor.colorAppPrimary()()
    }
}

class MatchNamesCell : UICollectionViewCell {
    
    @IBOutlet weak var lblMatchNumber: UILabel!
    @IBOutlet weak var lblTeamsInfo: UILabel!
    @IBOutlet weak var matchOngoingStatus: UILabel!
    @IBOutlet weak var comepletedSign: UIImageView!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var imgViewWeather: UIImageView!
    
    private lazy var gradient:CAGradientLayer = {
        let gradientLayer = CAGradientLayer()
        gradientLayer.cornerRadius = 10.0
        gradientLayer.frame = self.bounds
        gradientLayer.colors = [UIColor.colorCrimson().cgColor, UIColor.colorOrange().cgColor]
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.5)
        gradientLayer.endPoint = CGPoint(x: 1.0, y: 0.5)
        return gradientLayer
    }()
    
    var dataSource:MatchModel? {
        didSet{
            let liveImage = UIImage(named: "ic_green_dot")
            let tikImage  = UIImage(named: "Tik")
            
            if let dataSource = dataSource {
                lblMatchNumber.text = "#\(dataSource.MatchNo ?? 0)"
                lblTeamsInfo.text =  "\(dataSource.Team1 ?? "") v \(dataSource.Team2 ?? "")"
                imgViewWeather.image = dataSource.weatherIcon
                if dataSource.MatchStatus == "COMPLETE" {
                    matchOngoingStatus.text = dataSource.MatchDate
                    comepletedSign.isHidden = false
                    comepletedSign.image = tikImage
                } else if dataSource.MatchStatus == "Live" {
                    matchOngoingStatus.text = "Live"
                    comepletedSign.isHidden = false
                    comepletedSign.image = liveImage
                } else {
                    matchOngoingStatus.text = dataSource.MatchDate
                    comepletedSign.isHidden = true
                }
                if let BattingTeam = dataSource.BattingTeam, BattingTeam.count > 0 {
                    matchOngoingStatus.text = "\(BattingTeam) to bat"
                    comepletedSign.isHidden = false
                    comepletedSign.image = liveImage
                } else {
                    ()
                }
                containerView.backgroundColor = UIColor.white
                self.lblTeamsInfo.textColor = UIColor.black
                self.lblMatchNumber.textColor = UIColor.black
                self.matchOngoingStatus.textColor = UIColor.black
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.layer.cornerRadius = 10.0
        containerView.layer.borderWidth = 1.0
        containerView.layer.borderColor = UIColor.colorOrange().cgColor
    }
    
    override var isSelected: Bool {
        didSet {
            if isSelected {
                containerView.backgroundColor = UIColor.orange
                //containerView.setAppGradientLayer()
                self.lblTeamsInfo.textColor = UIColor.white
                self.lblMatchNumber.textColor = UIColor.white
                self.matchOngoingStatus.textColor = UIColor.white
            } else {
                containerView.backgroundColor = UIColor.white
                self.lblTeamsInfo.textColor = UIColor.black
                self.lblMatchNumber.textColor = UIColor.black
                self.matchOngoingStatus.textColor = UIColor.black
            }
            layoutIfNeeded()
        }
    }
}
