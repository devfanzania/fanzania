//
//  TeamFilterCollectionViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 13/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class TeamFilterCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var cellContainerView: UIView!
    @IBOutlet weak var labelTeamName: UILabel!
    var dataSource:TeamModel? {
        didSet{
            labelTeamName.text = dataSource?.teamShortName!
            if (dataSource?.isSelected)!{
                cellContainerView.backgroundColor = UIColor.colorAppPrimary()()
            }else{
                cellContainerView.backgroundColor = UIColor.lightGray
            }
        }
    }
    
    override func awakeFromNib() {
        labelTeamName.textColor = UIColor.white
        cellContainerView.layer.cornerRadius = 18.0
        cellContainerView.backgroundColor = UIColor.colorCrimson()
        cellContainerView.backgroundColor = UIColor.colorGrey()
    }
    
//    override var isSelected: Bool{
//        didSet{
//            if isSelected{
//                cellContainerView.backgroundColor = UIColor.colorAppPrimary()()
//            }else{
//                cellContainerView.backgroundColor = UIColor.colorGrey()
//            }
//        }
//    }
}
