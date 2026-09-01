//
//  VCPlayerEditPopUp.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 16/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

protocol VCPlayerCaptaincyDelegate {
    func getCaptaincyIndex(index:Int, isCaptain:Bool)
}

class VCPlayerEditPopUp: UIViewController, UIGestureRecognizerDelegate {

    
    @IBOutlet var popupView: UIView!
    @IBOutlet var btnSave: UIButton!
    @IBOutlet weak var playerNameLabel: UILabel!
    @IBOutlet var btnCancel: UIButton!
    @IBOutlet weak var scCaptaincy: UISegmentedControl!
    var tap: UITapGestureRecognizer?
    
    var isCaptain : Bool = true
    var playerName:String?
    var groundPlayerIndex:Int?
    
    var delegate:VCCreateNewTeam?
    var returnDelegate:VCPlayerCaptaincyDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        popupView.setCurvedCornerBordered()
        btnCancel.layer.cornerRadius = 15.0
        btnCancel.backgroundColor = UIColor.red
        btnSave.layer.cornerRadius = 15.0
        btnSave.backgroundColor = UIColor.red
        btnSave.addTarget(self, action: #selector(actionSave), for: .touchUpInside)
        btnCancel.addTarget(self, action: #selector(actionDismiss), for: .touchUpInside)
        playerNameLabel.text = playerName!
        if isCaptain {
            scCaptaincy.selectedSegmentIndex = 0
        }else{
            scCaptaincy.selectedSegmentIndex = 1
        }

        tap?.delegate = self
        tap?.cancelsTouchesInView = false
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !popupView.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    @objc func actionSave(){
        returnDelegate?.getCaptaincyIndex(index: groundPlayerIndex!, isCaptain: isCaptain)
        self.dismiss(animated: true, completion: nil)
    }
    @objc func actionDismiss(){
        self.dismiss(animated: true, completion: nil)
    }
//    @objc func actionChangePlayer(){
//        delegate?.removePlayer(index: groundPlayerIndex!)
//        self.dismiss(animated: true, completion: nil)
//    }
    @IBAction func actionValueChange(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0 {
            isCaptain = true
        }else if sender.selectedSegmentIndex == 1 {
            isCaptain = false
        }
        print(isCaptain)
    }
    
}
