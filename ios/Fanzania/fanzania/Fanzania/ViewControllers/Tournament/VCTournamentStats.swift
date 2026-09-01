//
//  VCTournamentStats.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCTournamentStats: UIViewController {

    @IBOutlet weak var labelTournamentName: UILabel!
    @IBOutlet weak var labelTournamentStatus: UILabel!
    @IBOutlet weak var container1: UIView!
    @IBOutlet weak var container2: UIView!
    @IBOutlet weak var container3: UIView!
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    var TopPlayersVC:VCTournamentStatsTopPlayers?
    var TopTeamsVC:VCTournamentStatsTopTeams?
    var TopLeaguesVC:VCTournamentStatsTopLeagues?
    var currentTournament:UserTournamentModel?
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        if let currentTournament = currentTournament {
            labelTournamentName.text = currentTournament.name!
            labelTournamentStatus.text = "\((currentTournament.StartDate)!) to \((currentTournament.EndDate)!)"
        }
        segmentedControl.selectedSegmentIndex = 0
        //segmentedControl.makeMultiline()
        self.container1.alpha = 1
        self.container2.alpha = 0
        self.container3.alpha = 0
        
        // Do any additional setup after loading the view.
    }
    
    @IBAction func switchedControl(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            UIView.animate(withDuration: 0.5, animations: {
                self.container1.alpha = 1
                self.container2.alpha = 0
                self.container3.alpha = 0
            })
        }else if sender.selectedSegmentIndex == 1 {
            UIView.animate(withDuration: 0.5, animations: {
                self.container1.alpha = 0
                self.container2.alpha = 1
                self.container3.alpha = 0
            })
        }else {
            UIView.animate(withDuration: 0.5, animations: {
                self.container1.alpha = 0
                self.container2.alpha = 0
                self.container3.alpha = 1
            })
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "embeddedTopPlayerSegue") {
            let childViewController = segue.destination as! VCTournamentStatsTopPlayers
            childViewController.currentTournament = currentTournament
        }else if (segue.identifier == "embeddedTopTeams") {
            let childViewController = segue.destination as! VCTournamentStatsTopTeams
            childViewController.currentTournament = currentTournament
        }else if (segue.identifier == "embeddedTopLeagues") {
            let childViewController = segue.destination as! VCTournamentStatsTopLeagues
            childViewController.currentTournament = currentTournament
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
