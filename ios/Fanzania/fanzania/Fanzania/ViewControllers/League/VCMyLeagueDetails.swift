//
//  VCMyLeagueDetails.swift
//  Fanzania
//
//  Created by Tathagata Dey on 11/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCMyLeagueDetails: UIViewController {

    @IBOutlet weak var lastMatchPointsPageViewButton: UIButton!
    @IBOutlet weak var currentTeamPageViewButton: UIButton!
    @IBOutlet weak var collectionViewLeagueDetails: UICollectionView!
    @IBOutlet weak var stackViewTabStack: UIStackView!
    
    var currentTournament:UserTournamentModel?
    var currentVisiblePage = 0
    
    var userTeam:MyTeamsModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if let title = userTeam?.UserTeamName {
            self.setTitle(title)
        }
    self.navigationController?.navigationBar.topItem?.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.mySystemFont(ofSize: 15.0)], for: .normal)
        
        collectionViewLeagueDetails.delegate = self
        collectionViewLeagueDetails.dataSource = self
        
        self.navigationController?.navigationBar.tintColor = UIColor.white
        
        currentTeamPageViewButton.layer.borderColor = UIColor.colorOrange().cgColor
        currentTeamPageViewButton.layer.borderWidth = 1.0
        currentTeamPageViewButton.layer.cornerRadius = 10.0
        //currentTeamPageViewButton.setAppGradientLayer()
        currentTeamPageViewButton.backgroundColor = UIColor.colorOrange()
        currentTeamPageViewButton.setTitleColor(UIColor.white, for: .normal)
        
        lastMatchPointsPageViewButton.layer.borderColor = UIColor.colorOrange().cgColor
        lastMatchPointsPageViewButton.layer.borderWidth = 1.0
        lastMatchPointsPageViewButton.layer.cornerRadius = 10.0
        lastMatchPointsPageViewButton.setTitleColor(UIColor.black, for: .normal)
        
        if let currentTournament = currentTournament {
            if let userTeam = userTeam {
                self.navigationItem.title = userTeam.UserTeamName
            }
            
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationSetup()
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17.0)], for: UIControl.State.normal)
    }
    
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }

    
    @IBAction func actionLastMatchPoints(_ sender: UIButton) {
        if currentVisiblePage != 1 {
            currentVisiblePage = 1
            //lastMatchPointsPageViewButton.setAppGradientLayer()
            lastMatchPointsPageViewButton.setTitleColor(UIColor.white, for: .normal)
            lastMatchPointsPageViewButton.backgroundColor = UIColor.colorOrange()
            //currentTeamPageViewButton.layer.sublayers?.removeFirst()
            currentTeamPageViewButton.backgroundColor = UIColor.white
            currentTeamPageViewButton.setTitleColor(UIColor.black, for: .normal)
            collectionViewLeagueDetails.scrollToItem(at: IndexPath(item: 1, section: 0), at: .centeredHorizontally, animated: true)
        }
    }
    @IBAction func actionCurrentTeam(_ sender: UIButton) {
        if currentVisiblePage != 0 {
            currentVisiblePage = 0
            //currentTeamPageViewButton.setAppGradientLayer()
            currentTeamPageViewButton.backgroundColor = UIColor.colorOrange()
            currentTeamPageViewButton.setTitleColor(UIColor.white, for: .normal)
            //lastMatchPointsPageViewButton.layer.sublayers?.removeFirst()
            lastMatchPointsPageViewButton.backgroundColor = UIColor.white
            lastMatchPointsPageViewButton.setTitleColor(UIColor.black, for: .normal)
            collectionViewLeagueDetails.scrollToItem(at: IndexPath(item: 0, section: 0), at: .centeredHorizontally, animated: true)
        }
    }
}

extension VCMyLeagueDetails:UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 2
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if indexPath.row == 0 {
            let cellCurrentTeam = collectionViewLeagueDetails.dequeueReusableCell(withReuseIdentifier: "cellCurrentTeam", for: indexPath) as! CollectionViewCellCurrentTeam
            cellCurrentTeam.delegate = self
            cellCurrentTeam.userTeam = userTeam
            cellCurrentTeam.currentTournament = currentTournament
            return cellCurrentTeam
        }else{
            let cellLastMatch = collectionViewLeagueDetails.dequeueReusableCell(withReuseIdentifier: "cellLastMatchPoints", for: indexPath) as! CollectionViewCellLastMatchPoints
            cellLastMatch.delegate = self
            cellLastMatch.userTeam = userTeam
            cellLastMatch.currentTournament = currentTournament
            return cellLastMatch
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionViewLeagueDetails.frame.width, height: collectionViewLeagueDetails.frame.height)
    }
    
}
