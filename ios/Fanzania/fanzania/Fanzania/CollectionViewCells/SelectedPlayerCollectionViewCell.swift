//
//  SelectedPlayerCollectionViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 02/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class SelectedPlayerCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var playerRoleIconButton: UIButton!
    @IBOutlet weak var playerNameLabel: UILabel!
    @IBOutlet weak var playerCaptaincyStatusIcon: UIImageView!
    @IBOutlet weak var playerOverseesStatusIcon: UIImageView!
    @IBOutlet weak var playerTeamNameLabel: UILabel!
    @IBOutlet weak var playerRemoveButton: UIButton!
    @IBOutlet weak var playerCaptaincy: UIImageView!
    @IBOutlet weak var viewBackground: UIView!
    var removeSelectedPlayer:((UIButton) -> ())?
    var collectionView:UICollectionView?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.setCurvedCornerBordered()
        self.layer.cornerRadius = 5.0
    }
    
    var dataSourcePlayer:PlayerInfo? {
        didSet {
            playerNameLabel.text = dataSourcePlayer?.shortName
            playerTeamNameLabel.text = dataSourcePlayer?.teamShortName
            playerRoleIconButton.setImage(dataSourcePlayer?.speciality.image, for: .normal)
            playerRemoveButton.addTarget(self, action: #selector(removeTapped(_:)), for: .touchUpInside)
            if (dataSourcePlayer?.isCaptain)!{
                playerCaptaincy.image = Captaincy.Captain.image
            }else if (dataSourcePlayer?.isViceCaptain)!{
                playerCaptaincy.image = Captaincy.ViceCaptain.image
            }else {
                playerCaptaincy.image = nil
            }
            if let isPlaying = dataSourcePlayer?.isPlaying,
               isPlaying {
                self.setCurvedCornerBorderedForLivePlayer()
                self.viewBackground.backgroundColor = UIColor(red: 239/255, green: 245/255, blue: 229/255, alpha: 1.0)
            } else {
                self.setCurvedCornerBordered()
                self.viewBackground.backgroundColor = UIColor.white
            }
        }
    }
    
    override func draw(_ rect: CGRect)
    {
        
        // here you have to reaload your collectionView
        super.draw(rect)
        collectionView!.reloadData()
        
    }
    
    @objc func removeTapped (_ sender:UIButton){
        removeSelectedPlayer!(sender)
    }
    
}
