//
//  VCLiveScore.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 29/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCLiveScore: UIViewController {

    @IBOutlet weak var collectionviewTournamentList: UICollectionView!
    @IBOutlet weak var collectionviewMatchList: UICollectionView!
    @IBOutlet weak var segmentedControlSwitchTab: UISegmentedControl!
    @IBOutlet weak var left_arrow: UIButton!
    @IBOutlet weak var right_arrow: UIButton!
    @IBOutlet weak var previousMatchButton: UIButton!
    @IBOutlet weak var nextMatchButton: UIButton!
    @IBOutlet weak var containerView1: UIView!
    @IBOutlet weak var containerView2: UIView!
    @IBOutlet weak var labelNoMatchMessage: UILabel!
    @IBOutlet weak var viewMatchDetailsData: UIView!
    @IBOutlet weak var matchPointsLabel: UILabel!
    @IBOutlet weak var totalPointsLabel: UILabel!
    
    
    var liveTournamentList:[UserTournamentModel] = []
    var liveMatchList:[LiveMatchModel] = []
    var currentTournament:UserTournamentModel?
    var currentMatch:LiveMatchModel?
    var tournamentSelectedIndex:Int = 0
    var isLoadtime = true
    var scoreContainerVC:VCLiveScores?
    var leagueTrackContainerVC:VCTrackLeague?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        setUPCollectionViewUI()
        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        navigationSetup()
        getLiveTournaments()
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17.0)], for: UIControl.State.normal)
        
        let rightNavBarItem = UIBarButtonItem(title: "Live", style: UIBarButtonItem.Style.plain, target: self, action: nil)
        navigationItem.rightBarButtonItem = rightNavBarItem
        navigationItem.rightBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 17.0)], for: UIControl.State.normal)
        
    }
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }
    
    func setUPCollectionViewUI(){
        collectionviewTournamentList.delegate = self
        collectionviewTournamentList.dataSource = self
        collectionviewTournamentList.allowsMultipleSelection = false
        collectionviewMatchList.delegate = self
        collectionviewMatchList.dataSource = self
        collectionviewMatchList.allowsMultipleSelection = false
        segmentedControlSwitchTab.selectedSegmentIndex = 0
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "embeddedLiveScoresSegue") {
            let childViewController = segue.destination as! VCLiveScores
 //           scoreContainerVC?.currentTournament = self.currentTournament
            scoreContainerVC = childViewController
            // Now you have a pointer to the child view controller.
            // You can save the reference to it, or pass data to it.
        }else if (segue.identifier == "embeddedLiveTrackLeague") {
            let childViewController = segue.destination as! VCTrackLeague
            leagueTrackContainerVC = childViewController
//            scoreContainerVC?.currentTournament = self.currentTournament
            
            // Now you have a pointer to the child view controller.
            // You can save the reference to it, or pass data to it.
        }
    }
    
    @IBAction func showComponent(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0 {
            UIView.animate(withDuration: 0.5, animations: {
                
                if self.currentMatch != nil && self.currentTournament != nil {
                    self.scoreContainerVC?.currentMatch = self.currentMatch
                    self.scoreContainerVC?.currentTournament = self.currentTournament
                    self.scoreContainerVC?.getLiveScore()
                }
                self.containerView1.alpha = 1
                self.containerView2.alpha = 0
            })
        } else {
            UIView.animate(withDuration: 0.5, animations: {
                
                if self.currentTournament != nil {
                    self.leagueTrackContainerVC?.currentMatch = self.currentMatch
                    self.leagueTrackContainerVC?.currentTournament = self.currentTournament
                    self.leagueTrackContainerVC?.getLeagues(tournamentId: (self.currentTournament?.id)!)
                }
                self.containerView1.alpha = 0
                self.containerView2.alpha = 1
            })
        }
    }
    
    @IBAction func leftTournamentProgress(_ sender: UIButton) {
        let collectionBounds = self.collectionviewTournamentList.bounds
        let contentOffset = CGFloat(floor(self.collectionviewTournamentList.contentOffset.x - collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    @IBAction func rightTournamentProgress(_ sender: UIButton) {
        let collectionBounds = self.collectionviewTournamentList.bounds
        let contentOffset = CGFloat(floor(self.collectionviewTournamentList.contentOffset.x + collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    
    @IBAction func actionMoveToPreviousMatch(_ sender: UIButton) {
        let collectionBounds = self.collectionviewMatchList.bounds
        let contentOffset = CGFloat(floor(self.collectionviewMatchList.contentOffset.x - collectionBounds.size.width))
        self.moveCollectionMatchToFrame(contentOffset: contentOffset)
    }
    @IBAction func actionMoveToNextMatch(_ sender: UIButton) {
        let collectionBounds = self.collectionviewMatchList.bounds
        let contentOffset = CGFloat(floor(self.collectionviewMatchList.contentOffset.x + collectionBounds.size.width))
        self.moveCollectionMatchToFrame(contentOffset: contentOffset)
    }
    
    func moveCollectionToFrame(contentOffset : CGFloat) {
        
        let frame: CGRect = CGRect(x : contentOffset ,y : self.collectionviewTournamentList.contentOffset.y ,width : self.collectionviewTournamentList.frame.width,height : self.collectionviewTournamentList.frame.height)
        self.collectionviewTournamentList.scrollRectToVisible(frame, animated: true)
    }
    
    func moveCollectionMatchToFrame(contentOffset : CGFloat){
        let frame: CGRect = CGRect(x : contentOffset ,y : self.collectionviewMatchList.contentOffset.y ,width : self.collectionviewMatchList.frame.width,height : self.collectionviewMatchList.frame.height)
        self.collectionviewMatchList.scrollRectToVisible(frame, animated: true)
    }
    
    func matchSelection(index: Int){
        if index >= liveMatchList.count { return }
        currentMatch = liveMatchList[index]
        scoreContainerVC?.currentMatch = currentMatch
        if segmentedControlSwitchTab.selectedSegmentIndex == 0 {
            scoreContainerVC?.getLiveScore()
        }
        leagueTrackContainerVC?.currentMatch = currentMatch
        if currentMatch?.matchStatus != "Live" {
            self.navigationItem.leftBarButtonItem?.isEnabled = false
            self.navigationItem.leftBarButtonItem?.tintColor = UIColor.clear
        } else {
            self.navigationItem.leftBarButtonItem?.isEnabled = true
            self.navigationItem.leftBarButtonItem?.tintColor = UIColor.white
        }
        getLiveScore()
    }
    
    func tournamentSelection(index : Int) {
        let tournament = liveTournamentList[index]
        tournamentSelectedIndex = index
        //(self.tabBarController as? VCTabBar)?.CurrentTournamentSelectedIndex = index
        print(index)
        currentTournament = tournament
        scoreContainerVC?.currentTournament = currentTournament
        leagueTrackContainerVC?.currentTournament = currentTournament
        if segmentedControlSwitchTab.selectedSegmentIndex == 1{
            leagueTrackContainerVC?.getLeagues(tournamentId: (currentTournament?.id)!)
        }
        //labelTotalPoints.text = String(describing: tournament.TotalPoints!)
        
//        if tournament.status == "COMPLETE" {
//            navigationItem.rightBarButtonItem?.isEnabled = false
//            navigationItem.rightBarButtonItem?.tintColor = UIColor.clear
//        }else{
//            navigationItem.rightBarButtonItem?.isEnabled = true
//            navigationItem.rightBarButtonItem?.tintColor = UIColor.white
//        }
        
        getLiveMatches(tournamentId: tournament.id!)
    }
    
    func getLiveTournaments(){
        
        print("running tournamnet")
        self.liveTournamentList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Live_Tournaments,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let TournamentStatus = element["TournamentStatus"] as! String
                                    if TournamentStatus == "INPROGRESS" {
                                        let TournamentId = element["TournamentId"] as! Int
                                        let TournamentName = element["TournamentName"] as! String
                                        let TournamentStage = element["TournamentStage"] as! String
                                        let TournamentStartDate = element["TournamentStartDate"] as! String
                                        let TournamentEndDate = element["TournamentEndDate"] as! String
                                        let UserId = element["UserId"] as! Int
                                        let UserTeamId = element["UserTeamId"] as! Int
                                        let UserTeamName = element["UserTeamName"] as! String
                                        
                                        
                                        self.liveTournamentList.append(UserTournamentModel(name: TournamentName, id: TournamentId, stage: TournamentStage, status: TournamentStatus, UserId: UserId, UserTeamId: UserTeamId, UserTeamName: UserTeamName, TeamCompositionId: 0, TotalPoints: 0, TeamRank: 0, StartDate: TournamentStartDate, EndDate:TournamentEndDate))
                                        
                                    }
                                }
                            }
                            DispatchQueue.main.async {
                                
                                self.collectionviewTournamentList.reloadData()
                                if self.isLoadtime {
                                    //self.selectTournament(index: (self.tabBarController as! VCTabBar).CurrentTournamentSelectedIndex)
                                    self.isLoadtime = false
                                }
                            }
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func getLiveScore(){
        
        print("running tournamnet")
        // let loader = UIViewController.displaySpinner(onView: self.view)
        let param = ["TournamentId" : (currentTournament?.id)!,
                     "MatchId" : (currentMatch?.id)!,
                     "UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any]
        print(param)
        Alamofire.request(URL_Live_MatcheScore,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                //                DispatchQueue.main.async {
                //                    UIViewController.removeSpinner(spinner: loader)
                //                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let CurrentMatchPoints = element["CurrentMatchPoints"] as! Int
                                    let TotalPoints = element["AllTotalPoints"] as! Int
                                    self.matchPointsLabel.text = "\(CurrentMatchPoints)"
                                    self.totalPointsLabel.text = "\(TotalPoints)"
                                    
                                }
                            }
                        }
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func selectTournament(index:Int){
        
        print("selected index \(index)")
        if tournamentSelectedIndex != index {
            if self.liveTournamentList.count != 0 && self.liveTournamentList.count > index {
                self.collectionviewTournamentList.selectItem(at: IndexPath(row: index, section: 0), animated: false, scrollPosition: .centeredHorizontally)
                self.collectionviewTournamentList.reloadData()
            }
        }
    }
    
    func getLiveMatches(tournamentId:Int){
        
        self.liveMatchList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        print(["TournamentId" : tournamentId] as [String : Any])
        Alamofire.request(URL_Live_Matches,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    let MatchStatus = element["MatchStatus"] as! String
                                    if MatchStatus != "UPCOMING" && MatchStatus != "COMPLETE" {
                                        let MatchId = element["MatchId"] as! Int
                                        let Team1 = element["Team1"] as! String
                                         let Team1ShortName = element["Team1ShortName"] as! String
                                        let Team1Image = element["Team1Image"] as! String
                                        let Team2 = element["Team2"] as! String
                                         let Team2ShortName = element["Team2ShortName"] as! String
                                        let Team2Image = element["Team2Image"] as! String
                                        let MatchNo = element["MatchNo"] as? Int
                                        self.liveMatchList.append(LiveMatchModel(id:MatchId, team1: Team1, team2: Team2, matchStatus: MatchStatus, matchPoints: 0, matchTotal: 0, team1ShortName:Team1ShortName, team2ShortName:Team2ShortName, team1ImageName:Team1Image, team2ImageName:Team2Image, MatchNo: MatchNo))
                                    }
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.collectionviewMatchList.reloadData()
                            }
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
}

extension VCLiveScore : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == collectionviewTournamentList {
            return CGSize(width: collectionviewTournamentList.frame.width, height: collectionviewTournamentList.frame.height)
        }else{
            return CGSize(width: collectionviewMatchList.frame.width, height: collectionviewMatchList.frame.height)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == collectionviewTournamentList {
            return liveTournamentList.count
        }else{
            return liveMatchList.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == collectionviewTournamentList{
            let cell = collectionviewTournamentList.dequeueReusableCell(withReuseIdentifier: "myTeamTournamentCell", for: indexPath) as! TournamentsNameCollectionViewCell
            cell.labelTournamentName.text = liveTournamentList[indexPath.row].name
            cell.labelTournamentDates.text = "\((liveTournamentList[indexPath.row].StartDate)!) to \((liveTournamentList[indexPath.row].EndDate)!)"
            return cell
        }else{
            let cell = collectionviewMatchList.dequeueReusableCell(withReuseIdentifier: "myTeamMatchCell", for: indexPath) as! LiveScoreMatchCollectionViewCell
            cell.dataSource = liveMatchList[indexPath.row]
            return cell
        }
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        var numOfSections: Int = 0
        
        if collectionView == collectionviewMatchList {
            if liveMatchList.count != 0
            {
                numOfSections            = 1
                collectionviewMatchList.backgroundView = nil
                viewMatchDetailsData.isHidden = false
                labelNoMatchMessage.isHidden = true
            }
            else
            {
                let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: collectionviewMatchList.bounds.size.width, height: collectionviewMatchList.bounds.size.height))
                //noDataLabel.text          = "No Match Available"
                noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
                noDataLabel.textColor     = UIColor.black
                noDataLabel.textAlignment = .center
                collectionviewMatchList.backgroundView  = noDataLabel
                viewMatchDetailsData.isHidden = true
                labelNoMatchMessage.isHidden = false
            }
        }else{
            if liveTournamentList.count != 0
            {
                numOfSections            = 1
                collectionviewTournamentList.backgroundView = nil
                viewMatchDetailsData.isHidden = false
                labelNoMatchMessage.isHidden = true
                self.navigationItem.rightBarButtonItem?.isEnabled = true
                self.navigationItem.rightBarButtonItem?.tintColor = UIColor.white
            }
            else
            {
                let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: collectionviewTournamentList.bounds.size.width, height: collectionviewTournamentList.bounds.size.height))
                noDataLabel.text          = "No live matches currently"
                noDataLabel.font = UIFont.boldSystemFont(ofSize: 12.0)
                noDataLabel.textColor     = UIColor.white
                noDataLabel.textAlignment = .center
                collectionviewTournamentList.backgroundView  = noDataLabel
                viewMatchDetailsData.isHidden = true
                labelNoMatchMessage.isHidden = false
                self.navigationItem.rightBarButtonItem?.isEnabled = false
                self.navigationItem.rightBarButtonItem?.tintColor = UIColor.clear
                
            }
        }
        
        return numOfSections
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if collectionView == collectionviewTournamentList {
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            
            tournamentSelection(index: indexPath.row)
            
            left_arrow.isEnabled = ( indexPath.row == 0 ) ? false : true
            right_arrow.isEnabled = ( indexPath.row == liveTournamentList.count-1 ) ? false : true
        }else{
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            
            matchSelection(index: indexPath.row)
            
            previousMatchButton.isEnabled = ( indexPath.row == 0 ) ? false : true
            nextMatchButton.isEnabled = ( indexPath.row == liveMatchList.count-1 ) ? false : true
        }
    }
    
}
