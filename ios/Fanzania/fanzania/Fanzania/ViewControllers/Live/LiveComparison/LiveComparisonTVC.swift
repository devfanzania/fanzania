//
//  LiveComparisonTVC.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

class LiveComparisonTVC: UITableViewCell {
    
    // MARK: - IBOutlets
    @IBOutlet weak var viewMyPlayer: UIView!
    @IBOutlet weak var viewOtherPlayer: UIView!
    @IBOutlet weak var lblMyPlayer: UILabel!
    @IBOutlet weak var lblOtherPlayer: UILabel!
    @IBOutlet weak var iconMyVC: UIImageView!
    @IBOutlet weak var iconOtherVC: UIImageView!
    @IBOutlet weak var iconMyCapt: UIImageView!
    @IBOutlet weak var iconOtherCapt: UIImageView!
    @IBOutlet weak var lblMyPlayerPt: UILabel!
    @IBOutlet weak var lblOtherPlayerPt: UILabel!
    @IBOutlet weak var lblComparisonPt: UILabel!
    
    var dataSource: TeamComparisonTableItemModel? {
        didSet {
            setupData()
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setupData() {
        
        guard let dataSource = dataSource else {
            return
        }
        
        viewMyPlayer.backgroundColor = dataSource.myPlayerSelected ? .lightGray : .white
        viewOtherPlayer.backgroundColor = dataSource.otherPlayerSelected ? .lightGray : .white
        
        lblMyPlayer.text = dataSource.myPlayerName
        lblOtherPlayer.text = dataSource.otherPlayerName
        
        iconMyCapt.isHidden = !dataSource.myCapt
        iconOtherCapt.isHidden = !dataSource.otherCapt
        
        iconMyVC.isHidden = !dataSource.myVCapt
        iconOtherVC.isHidden = !dataSource.otherVCapt
        
        lblMyPlayerPt.text = dataSource.myPlayerSelected ? "\(dataSource.myTotalPoints)" : nil
        lblOtherPlayerPt.text = dataSource.otherPlayerSelected ? "\(dataSource.otherTotalPoints)" : nil
        
        let ptsDifference = dataSource.myTotalPoints - dataSource.otherTotalPoints
        
        if !dataSource.myPlayerSelected && !dataSource.otherPlayerSelected {
            lblComparisonPt.text = "-"
            lblComparisonPt.textColor = .black
        } else {
            if ptsDifference > 0 {
                lblComparisonPt.text = "+\(ptsDifference)"
                lblComparisonPt.textColor = .green
            } else if ptsDifference < 0 {
                lblComparisonPt.text = "\(ptsDifference)"
                lblComparisonPt.textColor = .red
            } else {
                lblComparisonPt.text = "\(ptsDifference)"
                lblComparisonPt.textColor = .black
            }
        }
    }
}
