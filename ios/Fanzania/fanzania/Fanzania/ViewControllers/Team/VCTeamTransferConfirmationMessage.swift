//
//  VCTeamTransferConfirmationMessage.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 08/02/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCTeamTransferConfirmationMessage: UIViewController {

    var parentVC:VCCreateNewTeam?
    @IBOutlet var dialogView: UIView!
    @IBOutlet var currentCaptainName: UILabel!
    @IBOutlet weak var currentViceCaptainName: UILabel!
    @IBOutlet var labelTransferSinceLastMatchCount: UILabel!
    @IBOutlet weak var powerPlayView: UIView!
    @IBOutlet weak var powerPlayImageView: UIImageView!
    @IBOutlet weak var powerPlayEnabledLabel: UILabel!
    @IBOutlet var btnConfirm: UIButton!
    @IBOutlet var btnCancel: UIButton!
    @IBOutlet weak var btnPredictLeft: UIButton!
    @IBOutlet weak var btnPredictRight: UIButton!
    
    var totalTransferSinceLastMatch:Int?
    var captainName:String?
    var viceCaptainName:String?
    var powerPlay:PowerPlayTypes?
    var tournamentStatus:String?
    var team1ShortName: String?
    var team2ShortName: String?
    var WinnerPrediction: String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        dialogView.setCurvedCornerBordered()
        
        labelTransferSinceLastMatchCount.text = ( tournamentStatus == "INPROGRESS" ) ? "Transfers made since last match = " + String(describing: totalTransferSinceLastMatch!) : "You have unlimited transfers during this stage of the tournament."
        btnCancel.layer.cornerRadius = 15.0
        btnCancel.backgroundColor = UIColor.red
        btnConfirm.layer.cornerRadius = 15.0
        btnConfirm.backgroundColor = UIColor.red
        
        currentCaptainName.text = captainName!
        currentViceCaptainName.text = viceCaptainName
        switch powerPlay {
        case .none: powerPlayView.isHidden = true
        case .some:
            self.powerPlayView.isHidden = false
            self.powerPlayEnabledLabel.text = "\(String(describing: (self.powerPlay?.name)!)) Power Play Enabled"
            self.powerPlayImageView.image = self.powerPlay?.image
        }
        btnPredictLeft.setTitle(team1ShortName, for: .normal)
        btnPredictRight.setTitle(team2ShortName, for: .normal)
        
        if WinnerPrediction == team1ShortName {
            btnPredictLeft.isSelected = true
            btnPredictRight.isSelected = false
        } else if WinnerPrediction == team2ShortName {
            btnPredictLeft.isSelected = false
            btnPredictRight.isSelected = true
        } else {
            btnPredictLeft.isSelected = false
            btnPredictRight.isSelected = false
        }
    }
    
    @IBAction func actionConfirm(_ sender: UIButton) {
        parentVC?.saveTeamSelection(WinnerPrediction: WinnerPrediction)
        self.dismiss(animated: true, completion: nil)
    }
    @IBAction func actionCancel(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func tappedBtnPredictLeft(_ sender: UIButton) {
        sender.isSelected = true
        btnPredictRight.isSelected = false
        WinnerPrediction = team1ShortName ?? ""
    }
    
    @IBAction func tappedBtnPredictRight(_ sender: UIButton) {
        sender.isSelected = true
        btnPredictLeft.isSelected = false
        WinnerPrediction = team2ShortName ?? ""
    }
}
