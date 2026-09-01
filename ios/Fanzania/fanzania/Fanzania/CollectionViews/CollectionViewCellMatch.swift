//
//  CollectionViewCellMatch.swift
//  Fanzania
//
//  Created by Tathagata Dey on 16/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class CollectionViewCellMatch: UICollectionViewCell {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var lblMatchNo: UILabel!
    @IBOutlet weak var labelTeam1: UILabel!
    @IBOutlet weak var label2Team: UILabel!
    @IBOutlet weak var labelDate: UILabel!
    @IBOutlet weak var labelVenue: UILabel!
    @IBOutlet weak var imgViewWeather: UIImageView!
    @IBOutlet weak var imgGreenDot: UIImageView!
    
    override func awakeFromNib() {
        containerView.layer.borderColor = UIColor.colorOrange().cgColor
        containerView.layer.cornerRadius = 15.0
        containerView.layer.borderWidth = 1.0
    }
    
    var dataSource:MatchModel?{
        didSet{
            lblMatchNo.text = "#\(dataSource?.MatchNo ?? 0)"
            labelTeam1.text = dataSource?.Team1
            label2Team.text = dataSource?.Team2
            labelDate.text = dataSource?.MatchDate
            labelVenue.text = dataSource?.venue
            imgViewWeather.image = dataSource?.weatherIcon
            
            if let BattingTeam = dataSource?.BattingTeam, BattingTeam.count > 0 {
                labelDate.text = "\(BattingTeam) to bat"
                imgGreenDot.isHidden = false
            } else {
                imgGreenDot.isHidden = true
            }
        }
    }
    
}
