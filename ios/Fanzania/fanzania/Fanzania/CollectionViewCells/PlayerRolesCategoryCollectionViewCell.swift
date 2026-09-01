//
//  PlayerRolesCategoryCollectionViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 04/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class PlayerRolesCategoryCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var PlayerRoleButton: UIButton!
    @IBOutlet weak var playerCountInRole: UILabel!
    @IBOutlet weak var dataContainerView: UIView!
    var didSelectPlayerRole:(()->Void)?
    
    var assignedRole:PlayerSpeciality? {
        didSet{
            if assignedRole == nil {
                PlayerRoleButton.setTitle("ALL", for: .normal)
                PlayerRoleButton.setImage(nil, for: .normal)
            }else{
                PlayerRoleButton.setTitle("", for: .normal)
                PlayerRoleButton.setImage(assignedRole?.imgageGrey, for: .normal)
            }
            PlayerRoleButton.isUserInteractionEnabled = false
            dataContainerView.layer.cornerRadius = 13.0
            dataContainerView.clipsToBounds = true
            dataContainerView.frame = bounds;
            //dataContainerView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            dataContainerView.layer.backgroundColor = colorCellDeselected.cgColor
        }
    }
    var playerCount:Int = 0 {
        didSet{
            playerCountInRole.text = String(describing: playerCount)
            playerCountInRole.setCircularLayer()
            
            if playerCount >= PlayerSelectionRule.minAllowedPlayerCount(type: assignedRole) && playerCount <= PlayerSelectionRule.maxAllowedPlayerCount(type: assignedRole) {
                playerCountInRole.backgroundColor = colorDarkGreen
            }else{
                playerCountInRole.backgroundColor = UIColor.red
            }
        }
    }
    override var isSelected: Bool {
        didSet {
            if isSelected {
                self.transform = CGAffineTransform(scaleX: 1.1, y: 1.1)
                dataContainerView.layer.backgroundColor = colorAppPrimary.cgColor
            } else {
                dataContainerView.layer.backgroundColor = colorCellDeselected.cgColor
                self.transform = CGAffineTransform.identity
            }
        }
    }
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        //playerCountInRole.makeCircular()
    }
}
