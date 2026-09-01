//
//  VCTeamStats.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 28/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCTeamStats: UIViewController {

    @IBOutlet var tournamentView: UIView!
    @IBOutlet var labelTournamentName: UILabel!
    @IBOutlet var labelTournamentStatus: UILabel!
    @IBOutlet var labelUserTeamName: UILabel!
    @IBOutlet var segmentedControlSwitchTab: UISegmentedControl!
    @IBOutlet var containerView1: UIView!
    @IBOutlet var containerView2: UIView!
    
    var tournamentId:Int?
    var userTeamId:Int?
    var currentTournament:UserTournamentModel?
    var teamName:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        segmentedControlSwitchTab.selectedSegmentIndex = 0
        self.containerView1.alpha = 1
        self.containerView2.alpha = 0
        if let currentTournament = currentTournament {
            labelTournamentName.text = currentTournament.name!
            labelTournamentStatus.text = "\((currentTournament.StartDate)!) to \((currentTournament.EndDate)!)"
        }
        labelUserTeamName.text = teamName!
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "embeddedSegue1") {
            let childViewController = segue.destination as! VCStatsPageMyTopTenPlayer
            childViewController.tournamentId = tournamentId
            childViewController.userTeamId = userTeamId
            // Now you have a pointer to the child view controller.
            // You can save the reference to it, or pass data to it.
        }else if (segue.identifier == "embeddedSegue2") {
            let childViewController = segue.destination as! VCStatsPageCaptains
            childViewController.tournamentId = tournamentId
            childViewController.userTeamId = userTeamId
            // Now you have a pointer to the child view controller.
            // You can save the reference to it, or pass data to it.
        }
        
    }
    
    @IBAction func showComponent(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0 {
            UIView.animate(withDuration: 0.5, animations: {
                self.containerView1.alpha = 1
                self.containerView2.alpha = 0
            })
        } else {
            UIView.animate(withDuration: 0.5, animations: {
                self.containerView1.alpha = 0
                self.containerView2.alpha = 1
            })
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
