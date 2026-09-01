//
//  VCLiveComparisonBase.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

class VCLiveComparisonBase: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    @IBOutlet weak var viewContainer: UIView!
    @IBOutlet weak var viewLiveMatch: UIView!
    @IBOutlet weak var lblLiveMatch: UILabel!
    
    var MyTeamId: Int?
    var OtherTeamId: Int?
    var TournamentId: Int?
    var MatchId: Int?
    var currentMatch: LiveMatchModel?
    
    var MyTeamTournamentTotalPts: Int?
    var OtherTeamTournamentTotalPts: Int?
    
    var MyTeamName: String?
    var OtherTeamName: String?
    
    private var currentVC: UIViewController?
    override func viewDidLoad() {
        super.viewDidLoad()
        setupCurrentMatch()
        loadViewController(vc: loadTeamComparisonTableVC())
    }
    
    // MARK: - IBActions
    @IBAction func tappedSegement(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            loadViewController(vc: loadTeamComparisonTableVC())
        } else if sender.selectedSegmentIndex == 1 {
            loadViewController(vc: loadTeamComparisonChartVC())
        }
    }
    
    func setupCurrentMatch() {
        guard let matchNo = currentMatch?.MatchNo,
              let team1 = currentMatch?.team1ShortName,
              let team2 = currentMatch?.team2ShortName
        else {
            viewLiveMatch.isHidden = true
            return
        }
        viewLiveMatch.isHidden = false
        lblLiveMatch.text = "M\(matchNo)|\(team1) vs \(team2)"
    }
    
    private func loadViewController(vc: UIViewController) {
        currentVC?.view.removeFromSuperview()
        currentVC?.removeFromParent()
        addChild(vc)
        vc.view.translatesAutoresizingMaskIntoConstraints = false
        viewContainer.addSubview(vc.view)
        NSLayoutConstraint.activate([
            vc.view.leadingAnchor.constraint(equalTo: viewContainer.leadingAnchor),
            vc.view.trailingAnchor.constraint(equalTo: viewContainer.trailingAnchor),
            vc.view.topAnchor.constraint(equalTo: viewContainer.topAnchor),
            vc.view.bottomAnchor.constraint(equalTo: viewContainer.bottomAnchor)
        ])
        vc.didMove(toParent: self)
        currentVC = vc
    }
    
    private func loadTeamComparisonTableVC() -> VCLiveComparisonTable {
        let storyboard = UIStoryboard(name: "TeamComparison", bundle: .main)
        let vc = storyboard.instantiateViewController(withIdentifier: "VCLiveComparisonTable") as! VCLiveComparisonTable
        vc.MyTeamId = MyTeamId
        vc.MatchId = MatchId
        vc.OtherTeamId = OtherTeamId
        vc.TournamentId = TournamentId
        
        vc.MyTeamTournamentTotalPts = MyTeamTournamentTotalPts
        vc.OtherTeamTournamentTotalPts = OtherTeamTournamentTotalPts
        return vc
    }
    
    private func loadTeamComparisonChartVC() -> VCLiveComparisonChart {
        let storyboard = UIStoryboard(name: "TeamComparison", bundle: .main)
        let vc = storyboard.instantiateViewController(withIdentifier: "VCLiveComparisonChart") as! VCLiveComparisonChart
        vc.MyTeamId = MyTeamId
        vc.OtherTeamId = OtherTeamId
        vc.TournamentId = TournamentId
        
        vc.MyTeamTournamentTotalPts = MyTeamTournamentTotalPts
        vc.OtherTeamTournamentTotalPts = OtherTeamTournamentTotalPts
        
        vc.MyTeamName = MyTeamName
        vc.OtherTeamName = OtherTeamName
        return vc
    }
}
