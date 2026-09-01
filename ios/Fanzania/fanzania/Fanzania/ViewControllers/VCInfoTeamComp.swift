//
//  VCInfoTeamComp.swift
//  Fanzania
//
//  Created by Tathagata Dey on 13/03/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCInfoTeamComp: UIViewController, UIGestureRecognizerDelegate {
    
    var loader:UIView?
    var staticPageLink:String? = URLTeamCompositionRules
    
    @IBOutlet weak var okayButton: UIButton!
    @IBOutlet weak var popUpContainerView: UIView!
    @IBOutlet weak var lblInfo: UILabel!
    var tap: UITapGestureRecognizer?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        popUpContainerView.layer.cornerRadius = 15.0
        okayButton.backgroundColor = UIColor.colorAppPrimary()()
        okayButton.layer.cornerRadius = 15.0
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
        
        lblInfo.text = "You can select \(PlayerSelectionRule.BatsmanMIN)-\(PlayerSelectionRule.BatsmanMAX) batsman, \(PlayerSelectionRule.BowlerMIN)-\(PlayerSelectionRule.BowlerMAX) bowler, \(PlayerSelectionRule.AllrounderMIN)-\(PlayerSelectionRule.AllrounderMAX) all-rounder, \(PlayerSelectionRule.WktkeeperMIN)-\(PlayerSelectionRule.WktkeeperMAX) wicket keeper in your team with maximum \(PlayerSelectionRule.SameTeamPlayer) players from same team and maximum \(PlayerSelectionRule.MaxOverseesPlayerCount) overseas players."
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !popUpContainerView.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        DispatchQueue.main.async {
            UIViewController.removeSpinner(spinner: self.loader!)
        }
    }
    @IBAction func actionOkay(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
}
