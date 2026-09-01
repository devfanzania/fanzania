//
//  VCMyLeague.swift
//  Fanzania
//
//  Created by Tathagata Dey on 13/12/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCMyLeague: UIViewController, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var collectionViewTeamNames: UICollectionView!
    @IBOutlet weak var CollectionViewTournamentNames: UICollectionView!
    @IBOutlet weak var tableTeambyLeague: UITableView!
    @IBOutlet weak var left_arrow: UIButton!
    @IBOutlet weak var right_arrow: UIButton!
    @IBOutlet weak var btnShare: UIButton!
    @IBOutlet weak var stackviewTableHeader: UIStackView!
    @IBOutlet weak var btnleagueControl: UIButton!
    @IBOutlet weak var labelLeagueControl: UILabel!
    @IBOutlet weak var labelShareText: UILabel!
    @IBOutlet weak var btnLeagueStat: UIButton!
    @IBOutlet weak var lblRankInformation: UILabel!
    @IBOutlet weak var stackRankInformation: UIStackView!
    @IBOutlet weak var viewRankInformation: UIView!
    
    
    var right_arrow_white:UIImage?
    var right_arrow_black:UIImage?
    var whiteDot:UIImage?
    var green_arrow:UIImage?
    var red_Arrow:UIImage?
    var isLoadtime = true
    var tournamentList:[UserTournamentModel] = []
    var leagueList:[MyLeaguesModel] = []
    var teamByLeague:[MyTeamsModel] = []

    var currentTournament:UserTournamentModel?
    var currentLeague:MyLeaguesModel?
    var userTeam:MyTeamsModel?
    var tournamentSelectedIndex:Int = 0
    var preSelectedLeagueIndex:Int?
    
    //data passing
    var selectedTeamId:Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        right_arrow_black = UIImage(named: "right-arrow")
        right_arrow_white = UIImage(named: "right-arrow-white")
        whiteDot = UIImage(named: "white-circle")
        green_arrow = UIImage(named: "up-green-arrow")
        red_Arrow = UIImage(named: "down-red-arrow")
        
        CollectionViewTournamentNames.delegate = self
        CollectionViewTournamentNames.dataSource = self
        
        collectionViewTeamNames.delegate = self
        collectionViewTeamNames.dataSource = self
        collectionViewTeamNames.allowsMultipleSelection = false
        tableTeambyLeague.delegate = self
        tableTeambyLeague.dataSource = self
        tableTeambyLeague.tableFooterView = UIView()
    }
    
    func buildNavigationBar(){
        
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        let stackForCreate:UIStackView = {
            let stack = UIStackView(frame: CGRect(x: 0, y: 0, width: 25, height: 25))
            stack.axis = .vertical
            let createLeagueButton: UIButton = UIButton(type: .custom)
            createLeagueButton.setImage(UIImage(named: "league_create"), for: .normal)
            createLeagueButton.addTarget(self, action: #selector(self.createLeague), for: .touchUpInside)
            stack.addArrangedSubview(createLeagueButton)
            let label = UILabel()
            label.text = "Create"
            label.font = UIFont.systemFont(ofSize: 10.0)
            label.textColor = UIColor.white
            stack.addArrangedSubview(label)
            return stack
        }()
        let createBarButton = UIBarButtonItem(customView: stackForCreate)
        
        let stackForJoin:UIStackView = {
            let stack = UIStackView(frame: CGRect(x: 0, y: 0, width: 25, height: 25))
            stack.axis = .vertical
            let button: UIButton = UIButton(type: .custom)
            button.setImage(UIImage(named: "join_league"), for: .normal)
            button.addTarget(self, action: #selector(self.joinLeague), for: .touchUpInside)
            stack.addArrangedSubview(button)
            let label = UILabel()
            label.text = "Join"
            label.font = UIFont.systemFont(ofSize: 10.0)
            label.textColor = UIColor.white
            stack.addArrangedSubview(label)
            return stack
        }()
        let joinBarButton = UIBarButtonItem(customView: stackForJoin)
        //self.navigationItem.rightBarButtonItem = joinBarButton
        
        let notificationButton: UIBarButtonItem = {
            let button = BadgeButton()
            button.frame = CGRect(x: view.frame.width/2 - 22, y: view.frame.height/2 - 22, width: 44, height: 44)
            button.setImage(UIImage(named: "notification-bell")?.withRenderingMode(.alwaysOriginal), for: .normal)
            button.badgeEdgeInsets = UIEdgeInsets(top: 20, left: 0, bottom: 0, right: 15)
            button.addTarget(self, action: #selector(tappedNotifications), for: .touchUpInside)
            button.badge = nil
            return UIBarButtonItem(customView: button)
        }()
        
        self.navigationItem.leftBarButtonItem = notificationButton
        self.navigationItem.rightBarButtonItems = [createBarButton, joinBarButton]
    }
    
    override func viewDidAppear(_ animated: Bool) {
        navigationSetup()
        getTournaments()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        preSelectedLeagueIndex = nil
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17.0)], for: UIControl.State.normal)
        buildNavigationBar()
    }
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }
    
    @objc func tappedNotifications() {
        
        guard let notificationsVC = UIStoryboard(name: StoryboardNames.PlayerProfile.rawValue,
                                                 bundle: Bundle.main)
                .instantiateViewController(withIdentifier: "VCNotifications") as? VCNotifications
        else {
            return
        }
        navigationController?.pushViewController(notificationsVC, animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? VCCreateLeaguePopUp {
            destination.delegate = self
            destination.tournamentId = currentTournament?.id
            self.modalPresentationStyle = .popover
        }else if let destination = segue.destination as? VCJoinLeaguePopUp {
            destination.delegate = self
            destination.tournamentId = currentTournament?.id
            self.modalPresentationStyle = .popover
        }else if let destination = segue.destination as? VCLeaguesMatesTeamView {
            destination.leagueTeamId = selectedTeamId
            destination.tournamentId = currentTournament?.id
            self.modalPresentationStyle = .popover
        }else if let destination = segue.destination as? VCLeagueStats {
            destination.tournamentId = currentTournament?.id
            destination.leagueId = currentLeague?.LeagueId
            destination.currentTournament = currentTournament
            destination.teamName = currentTournament?.UserTeamName
        }else if let destination = segue.destination as? VCMyLeagueDetails {
            destination.currentTournament = currentTournament
            destination.userTeam = userTeam
        }
    }
    
    func refreshLeagues(){
        getLeagues(tournamentId: currentTournament!.id!)
    }
    @IBAction func actionOpenStats(_ sender: UIButton) {
        performSegue(withIdentifier: "segueLeagueStat", sender: self)
    }
    @IBAction func joinLeague(_ sender: UIBarButtonItem) {
        self.performSegue(withIdentifier: "segueJoinLeague", sender: self)
    }
    @IBAction @objc func createLeague(_ sender: UIBarButtonItem) {
        self.performSegue(withIdentifier: "segueCreateLeague", sender: self)
    }
    @IBAction func leftTournamentProgress(_ sender: UIButton) {
        let collectionBounds = self.CollectionViewTournamentNames.bounds
        let contentOffset = CGFloat(floor(self.CollectionViewTournamentNames.contentOffset.x - collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    @IBAction func rightTournamentProgress(_ sender: UIButton) {
        let collectionBounds = self.CollectionViewTournamentNames.bounds
        let contentOffset = CGFloat(floor(self.CollectionViewTournamentNames.contentOffset.x + collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    
    @IBAction func btnShare(_ sender: UIButton) {
        // text to share
        //let text = StringConstants.ShareCodeTextFirstPart + (currentLeague?.LeagueName!)! +  " by " + UserDefaults.standard.string(forKey: UserDefaultData.Name.rawValue)! + StringConstants.ShareCodeTextSecondPart + currentLeague!.LeaguePin!
        
        let text = String(format: StringConstants.ShareLeagueCode, (currentLeague?.LeagueName)!, UserDefaults.standard.string(forKey: UserDefaultData.Name.rawValue)!, (currentLeague?.LeaguePin)!)
        // set up activity view controller
        let textToShare = [ text ]
        let activityViewController = UIActivityViewController(activityItems: textToShare, applicationActivities: nil)
        activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
        
        // exclude some activity types from the list (optional)
        activityViewController.excludedActivityTypes = [UIActivity.ActivityType.airDrop]
        
        // present the view controller
        self.present(activityViewController, animated: true, completion: nil)
    }
    
    func moveCollectionToFrame(contentOffset : CGFloat) {
        
        let frame: CGRect = CGRect(x : contentOffset ,y : self.CollectionViewTournamentNames.contentOffset.y ,width : self.CollectionViewTournamentNames.frame.width, height : self.CollectionViewTournamentNames.frame.height)
        self.CollectionViewTournamentNames.scrollRectToVisible(frame, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == CollectionViewTournamentNames {
            return CGSize(width: CollectionViewTournamentNames.frame.width, height: CollectionViewTournamentNames.frame.height)
        }else{
//            var size:CGSize = leagueList[indexPath.row].LeagueName!.size(withAttributes: nil)
//            size.height = collectionView.bounds.height - 8
//            let Font =  UIFont.systemFont(ofSize: 14.0)
//            let fontAttribute = [NSAttributedString.Key.font: Font]
//            size.width = leagueList[indexPath.row].LeagueName!.size(withAttributes: fontAttribute).width + 64
            return CGSize(width: 175.0, height: collectionView.frame.height-8)
        }
    }
    
    func leagueSelection(indexPath : IndexPath){
        
        if indexPath.row >= leagueList.count { return }
        let leagueSeleted = leagueList[indexPath.row]
        currentLeague = leagueSeleted
        
        //labelShareText.text = leagueSeleted.LeaguePin
        
        let settings = UIImage(named: "gear")
        let exit = UIImage(named: "ic_exit_league")
        
        if leagueSeleted.LeagueLeaderId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue){
            btnleagueControl.setImage(settings, for: .normal)
            labelLeagueControl.text = "Change Code"
            btnleagueControl.removeTarget(self, action: nil, for: .touchUpInside)
            btnleagueControl.addTarget(self, action: #selector(ResetLeaguePin), for: .touchUpInside)
            viewRankInformation.isHidden = true
        }else{
            btnleagueControl.setImage(exit, for: .normal)
            labelLeagueControl.text = "Exit League"
            btnleagueControl.removeTarget(self, action: nil, for: .touchUpInside)
            btnleagueControl.addTarget(self, action: #selector(ExitLeague), for: .touchUpInside)
            viewRankInformation.isHidden = false
        }
        
        // TODO: - Get league owner
        getTeamsByLeague(tournamentId: leagueList[indexPath.row].TournamentId!, leagueId: leagueList[indexPath.row].LeagueId!)
    }
    
    func tournamentSelection(rowItem:Int){
        if rowItem >= tournamentList.count { return }
        let selectedTournament = tournamentList[rowItem]
        tournamentSelectedIndex = rowItem
        (self.tabBarController as? VCTabBar)?.CurrentTournamentSelectedIndex = rowItem
        print(rowItem)
        currentTournament = selectedTournament
        if currentTournament?.status == "COMPLETE" {
            btnShare.isEnabled = false
        }else{
            btnShare.isEnabled = true
        }
        
        if currentTournament?.status == "COMPLETE" {
            navigationItem.rightBarButtonItems?[0].isEnabled = false
            navigationItem.rightBarButtonItems?[0].tintColor = UIColor.clear
            navigationItem.rightBarButtonItems?[1].isEnabled = false
            navigationItem.rightBarButtonItems?[1].tintColor = UIColor.clear
        }else{
            navigationItem.rightBarButtonItems?[0].isEnabled = true
            navigationItem.rightBarButtonItems?[0].tintColor = UIColor.white
            navigationItem.rightBarButtonItems?[1].isEnabled = true
            navigationItem.rightBarButtonItems?[1].tintColor = UIColor.white
        }
        getLeagues(tournamentId: selectedTournament.id!)
    }
    
    @objc func showTeamPreview(_ sender: UIButton){
        selectedTeamId = sender.tag
        performSegue(withIdentifier: "segueTeamView", sender: self)
    }
    
    @objc func approveUser(_ sender: UIButton){
        let team = teamByLeague[sender.tag]
        ApproveUser(UserId: team.UserId!, leagueId: (currentLeague?.LeagueId)!)
    }
    @objc func removeUser(_ sender: UIButton){
        let team = teamByLeague[sender.tag]
        if team.UserLeagueId != nil {
            UnapproveUser(userLeagueId: team.UserLeagueId!, leagueId: (currentLeague?.LeagueId)!)
        }
    }

    func getTournaments(){
        
        print("running tournamnet")
        self.tournamentList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_User_TournamentDetails,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : ""])
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
                    print(responseJSON)
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
                            print(dataArray)
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    if let TournamentStatus = element["TournamentStatus"] as? String, TournamentStatus != "TBD" {
                                        let TournamentId = element["TournamentId"] as! Int
                                        let TournamentName = element["TournamentName"] as! String
                                        let TournamentStage = element["TournamentStage"] as! String
                                        let TournamentStartDate = element["TournamentStartDate"] as! String
                                        let TournamentEndDate = element["TournamentEndDate"] as! String
                                        let TeamRank = element["TeamRank"] as! Int
                                        let UserId = element["UserId"] as! Int
                                        let UserTeamId = element["UserTeamId"] as! Int
                                        let UserTeamName = element["UserTeamName"] as! String
                                        let TeamCompositionId = element["TeamCompositionId"] as? Int
                                        let TotalPoints = element["TotalPoints"] as! Int
                                        
                                        self.tournamentList.append(UserTournamentModel(name: TournamentName, id: TournamentId, stage: TournamentStage, status: TournamentStatus, UserId: UserId, UserTeamId: UserTeamId, UserTeamName: UserTeamName, TeamCompositionId: TeamCompositionId, TotalPoints: TotalPoints, TeamRank: TeamRank, StartDate: TournamentStartDate, EndDate:TournamentEndDate))
                                        
                                    }
                                }
                            }
                            DispatchQueue.main.async {
                                self.CollectionViewTournamentNames.reloadData()
                                if self.isLoadtime {
                                    self.selectTournament(index: (self.tabBarController as! VCTabBar).CurrentTournamentSelectedIndex)
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
    
    func selectTournament(index:Int){
        
        print("selected index \(index)")
        if tournamentSelectedIndex != index {
            if self.tournamentList.count != 0 && self.tournamentList.count > index {
                self.CollectionViewTournamentNames.delegate?.collectionView!(self.CollectionViewTournamentNames, didSelectItemAt: IndexPath(row: index, section: 0))
                self.CollectionViewTournamentNames.selectItem(at: IndexPath(row: index, section: 0), animated: false, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
                self.CollectionViewTournamentNames.reloadData()
            }
        }
    }

    
    func getLeagues(tournamentId:Int){
        
        self.leagueList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        var param:[String:Int] = [:]
        param = [UserDefaultData.UserId.rawValue : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                 "TournamentId" : tournamentId]
        Alamofire.request(URL_Leagues_ByTournament,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : ""])
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
                        print("json format mismatch")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    
                                    let LeagueName = element["LeagueName"] as! String
                                    let TeamStanding = element["TeamStanding"] as! Int
                                    let LeagueRank = element["LeagueRank"] as? Int
                                    let LeagueId = element["LeagueId"] as! Int
                                    let LeaguePoints = element["LeaguePoints"] as! Int
                                    let TournamentId = element["TournamentId"] as! Int
                                    let LeaguePin = element["LeaguePin"] as! String
                                    let LeagueLeaderId = element["LeagueLeaderId"] as! Int
                                    
                                    /*
                                     let LeagueLeaderId = element["LeagueLeaderId"] as! Int
                                     let LeaguePin = element["LeaguePin"] as! Int
                                     
                                     let Status = element["Status"] as! Int
                                     let TeamPoints = element["TeamPoints"] as! Int
                                     
                                     
                                     let UserId = element["UserId"] as! Int
                                     let UserTeamName = element["UserTeamName"] as! Int
                                     */
                                    self.leagueList.append(MyLeaguesModel(LeagueId: LeagueId, LeagueName: LeagueName, LeaguePoints: LeaguePoints, TeamStanding: TeamStanding, LeagueRank: LeagueRank, TournamentId: TournamentId, leaguePin: LeaguePin, LeagueLeaderId:LeagueLeaderId))
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.collectionViewTeamNames.reloadData()
                                
                                if self.leagueList.count != 0 {
                                    var indexPath = IndexPath(item: 0, section: 0)
                                    if let preSelectedLeagueIndex = self.preSelectedLeagueIndex {
                                        indexPath = IndexPath(item: preSelectedLeagueIndex, section: 0)
                                    }
                                    self.leagueSelection(indexPath: indexPath)
                                    self.collectionViewTeamNames.selectItem(at: indexPath, animated: false, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
                                    self.btnLeagueStat.isEnabled = true
                                    self.btnShare.isEnabled = true
                                    self.btnleagueControl.isEnabled = true
                
                                }else{
                                    self.teamByLeague.removeAll()
                                    self.tableTeambyLeague.reloadData()
                                    self.btnLeagueStat.isEnabled = false
                                    self.btnShare.isEnabled = false
                                    self.btnleagueControl.isEnabled = false
                                    self.labelShareText.text = "Share Code"
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
    
    func getTeamsByLeague(tournamentId:Int, leagueId:Int){
        
        self.teamByLeague.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        var param:[String:Int] = [:]
        param = [UserDefaultData.UserId.rawValue : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                 "TournamentId" : tournamentId,
                 "LeagueId" : leagueId]
        Alamofire.request(URL_Leagues_AllTeams,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : ""])
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
                        print("json format mismatch")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    
                                    let FullName = element["FullName"] as! String
                                    var IsLeagueLeader:Bool = false
                                    if (element["IsLeagueLeader"] as! String) == "Y" {
                                        IsLeagueLeader = true
                                    }else{
                                        IsLeagueLeader = false
                                    }
                                    
                                    let LeagueLeader = element["LeagueLeader"] as? String
                                    let status = element["Status"] as! String
                                    let SubsLeft = element["SubsLeft"] as? Int
                                    let LastMatchPoints = element["LastMatchPoints"] as! Int
                                    let TeamCurrentStanding = element["TeamCurrentStanding"] as? Int
                                    let TeamGlobalRank = element["TeamGlobalRank"] as? Int
                                    let TeamOldStanding = element["TeamOldStanding"] as? Int
                                    let TotalPoints = element["TotalPoints"] as? Int
                                    let UserTeamId = element["UserTeamId"] as? Int
                                    let UserTeamName = element["UserTeamName"] as? String
                                    let UserLeagueId = element["UserLeagueId"] as? Int
                                    let userId = element["UserId"] as? Int
                                    let SupportedTeam: String? = element["SupportedTeam"] as? String
                                    let LeagueRank: Int? = element["LeagueRank"] as? Int
                                    
                                    self.teamByLeague.append(MyTeamsModel(FullName: FullName, IsLeagueLeader: IsLeagueLeader, LeagueLeader: LeagueLeader, status: status, SubsLeft: SubsLeft, TeamCurrentStanding: TeamCurrentStanding, TeamGlobalRank: TeamGlobalRank, TeamOldStanding: TeamOldStanding, TotalPoints: TotalPoints, UserTeamId: UserTeamId, UserTeamName: UserTeamName, UserId: userId, UserLeagueId: UserLeagueId, LastMatchPoints: LastMatchPoints, SupportedTeam: SupportedTeam, LeagueRank: LeagueRank))
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.tableTeambyLeague.reloadData()
                                
                                
                                
                                if let leagueLeaderPoints = self.teamByLeague.first(where: {$0.IsLeagueLeader})?.TotalPoints,
                                   let myRank = self.teamByLeague.first(where: ({ $0.UserId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue)}))?.LeagueRank,
                                   let myPoints = self.teamByLeague.first(where: ({ $0.UserId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue)}))?.TotalPoints
                                   {
                                    self.lblRankInformation.text = "Your rank is \(myRank) out of \(self.teamByLeague.count) members, (\(leagueLeaderPoints-myPoints) behind the topper."
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
    
    func UnapproveUser(userLeagueId:Int, leagueId:Int){
        
        print("calling remove user + \(userLeagueId) + \(leagueId)")
        
        let alert = UIAlertController(title: "Remove User", message: StringConstants.teamKickoutText, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Remove", style: .default, handler: { action in
            
            let loader = UIViewController.displaySpinner(onView: self.view)
            let param = ["UserLeagueId" : userLeagueId,
                         "LeagueId" : leagueId]
            
            Alamofire.request(URL_Leagues_UnapproveUser,
                              method: .post,
                              parameters: param,
                              encoding: JSONEncoding.default,
                              headers: [
                                "x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                                "x-api-userid" : String(describing: UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue))])
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
                            print("json format mismatch")
                            return
                        }
                        print(jsonDictionary)
                        guard let status = jsonDictionary["status"] as? String else{
                            print("json format mismatch")
                            return
                        }
                        if status == "success" {
                            
                            let invalid_login_alert = UIAlertController(title: "User Removed", message: "User is removed from the League", preferredStyle: .alert)
                            
                            invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                                self.getTeamsByLeague(tournamentId: (self.currentTournament?.id)!, leagueId: (self.currentLeague?.LeagueId)!)
                            }))
                            self.present(invalid_login_alert, animated: true, completion: nil)
                            
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
            
            self.dismiss(animated: true, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
        }))
        self.present(alert, animated: true, completion: nil)
        
    }
    
    func ApproveUser(UserId:Int, leagueId:Int){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param = ["UserId" : UserId,
                 "LeagueId" : leagueId]
        Alamofire.request(URL_Leagues_ApproveUser,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: [
                            "x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                            "x-api-userid" : String(describing: UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue))])
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
                        print("json format mismatch")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        let invalid_login_alert = UIAlertController(title: "User Approved", message: "This user is now added to league " + (self.currentLeague?.LeagueName)!, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.getTeamsByLeague(tournamentId: (self.currentTournament?.id)!, leagueId: (self.currentLeague?.LeagueId)!)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                       
                        
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
    
    @objc func ExitLeague(){
        
        let alert = UIAlertController(title: "Exit League", message: "Sure want to Exit League \((currentLeague?.LeagueName)!)?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Exit", style: .default, handler: { action in
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Leagues_ExitLeague,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                                       "LeagueId" : self.currentLeague?.LeagueId] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.UserId.rawValue)!])
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
                        print("json format mismatch")
                        return
                    }
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        
                        let invalid_login_alert = UIAlertController(title: "League Exited", message: self.currentLeague?.LeagueName as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.refreshLeagues()
                            self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                       
                        
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
        self.dismiss(animated: true, completion: nil)
    }))
    alert.addAction(UIAlertAction(title: "No", style: .default, handler: { action in
    self.dismiss(animated: true, completion: nil)
    }))
    
    self.present(alert, animated: true, completion: nil)
    }
    
    @objc func ResetLeaguePin(){
        
        
            let loader = UIViewController.displaySpinner(onView: self.view)
            Alamofire.request(URL_League_ResetLeaguePin,
                              method: .post,
                              parameters: ["TournamentId" : self.currentTournament?.id,
                                           "LeagueId" : self.currentLeague?.LeagueId] as [String : Any],
                              encoding: JSONEncoding.default,
                              headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.UserId.rawValue)!, "x-api-devicetype":"ios"])
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
                            print("json format mismatch")
                            return
                        }
                        guard let status = jsonDictionary["status"] as? String else{
                            print("json format mismatch")
                            return
                        }
                        if status == "success" {
                            
                            let invalid_login_alert = UIAlertController(title: "League Code Changed", message: "Your league code has been changed. Time to share it with friends?", preferredStyle: .alert)
                            
                            invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                                if let dataArray = jsonDictionary["data"] as? NSArray{
                                    print(dataArray)
                                    let element = dataArray[0] as! [String:Any]
                                    let LeaguePin = element["LeaguePin"] as! String
                                    //self.labelShareText.text = LeaguePin
                                    self.refreshLeagues()
                                }
                                self.dismiss(animated: true, completion: nil)
                            }))
                            self.present(invalid_login_alert, animated: true, completion: nil)
                            
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
    
    func actionShowTeam(teamTableIndex:Int){
        self.userTeam = self.teamByLeague[teamTableIndex]
        self.performSegue(withIdentifier: "LeagueDetailsStoryboardID", sender: self)
    }
}

extension VCMyLeague:UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return teamByLeague.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableTeambyLeague.dequeueReusableCell(withIdentifier: "cellTeamByLeague", for: indexPath)  as! LeagueMembersListTableViewCell
        cell.currentLeague = currentLeague
        cell.currentTournament = currentTournament
        cell.dataSource = teamByLeague[indexPath.row]
        cell.showTeamPreview = { userID in
            self.actionShowTeam(teamTableIndex: indexPath.row)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.actionShowTeam(teamTableIndex: indexPath.row)
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        
        let dataSource = teamByLeague[indexPath.row]
        let reject = UITableViewRowAction(style: .destructive, title: "Reject") { (action, indexPath) in
            self.UnapproveUser(userLeagueId: (dataSource.UserId)!, leagueId: (self.currentLeague?.LeagueId)!)
        }
        
        let accept = UITableViewRowAction(style: .normal, title: "Accept") { (action, indexPath) in
            self.ApproveUser(UserId: (dataSource.UserId)!, leagueId: (self.currentLeague?.LeagueId)!)
        }
        accept.backgroundColor = UIColor.colorGreen()
        
        let show = UITableViewRowAction(style: .normal, title: "View") { (action, indexPath) in
            self.actionShowTeam(teamTableIndex: indexPath.row)
        }
        show.backgroundColor = UIColor.colorAppPrimary()()
        
        
        var actions = [show]
        if currentLeague?.LeagueLeaderId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) && currentTournament?.status != "COMPLETE" {
            
            if dataSource.UserId == UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue) {
                return actions
            }else{
                actions.append(reject)
                if dataSource.status == "Approved" {
                    return actions
                }else{
                    actions.append(accept)
                    return actions
                }
            }
        }else{
            return actions
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        var numOfSections = 0
        if teamByLeague.count != 0
        {
            numOfSections = 1
            tableTeambyLeague.backgroundView = nil
        }
        else
        {
            let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: tableTeambyLeague.bounds.size.width, height: tableTeambyLeague.bounds.size.height))
            noDataLabel.text = "No Team Available"
            noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
            noDataLabel.textColor = UIColor.black
            noDataLabel.textAlignment = .center
            tableTeambyLeague.backgroundView  = noDataLabel
        }
        return numOfSections
    }
}

extension VCMyLeague : UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == CollectionViewTournamentNames {
            return tournamentList.count
        }else{
            return leagueList.count
        }
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        var numOfSections: Int = 0
        
        if collectionView == collectionViewTeamNames {
            if leagueList.count != 0
            {
                numOfSections = 1
                collectionViewTeamNames.backgroundView = nil
            }
            else
            {
                let noDataLabel: UILabel = UILabel(frame: CGRect(x: 0, y: 0, width: collectionViewTeamNames.bounds.size.width, height: collectionViewTeamNames.bounds.size.height))
                noDataLabel.text = "No League Available"
                noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
                noDataLabel.textColor = UIColor.black
                noDataLabel.textAlignment = .center
                collectionViewTeamNames.backgroundView  = noDataLabel
            }
        }else{
            if tournamentList.count != 0
            {
                numOfSections  = 1
                CollectionViewTournamentNames.backgroundView = nil
                
                if let navButtons = self.navigationItem.rightBarButtonItems {
                    for i in navButtons {
                        i.isEnabled = true
                        i.tintColor = UIColor.white
                    }
                }
            }
            else
            {
                if let navButtons = self.navigationItem.rightBarButtonItems {
                    for i in navButtons {
                        i.isEnabled = false
                        i.tintColor = UIColor.clear
                    }
                }
                
                let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: CollectionViewTournamentNames.bounds.size.width, height: CollectionViewTournamentNames.bounds.size.height))
                noDataLabel.text          = "No tournaments Available"
                noDataLabel.textColor     = UIColor.black
                noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
                noDataLabel.textAlignment = .center
                CollectionViewTournamentNames.backgroundView  = noDataLabel
            }
        }
        
        return numOfSections
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if collectionView == CollectionViewTournamentNames {
            let cell = CollectionViewTournamentNames.dequeueReusableCell(withReuseIdentifier: "myleagueTournamentCell", for: indexPath) as! TournamentsNameCollectionViewCell
            cell.labelTournamentName.text = tournamentList[indexPath.row].name
            cell.labelTournamentDates.text = "\((tournamentList[indexPath.row].StartDate)!) to \((tournamentList[indexPath.row].EndDate)!)"
            return cell
        } else {
            let cell = collectionViewTeamNames.dequeueReusableCell(withReuseIdentifier: "myLeagueNameCell", for: indexPath) as! LeagueNamesCell
            cell.labelLeagueName.text = leagueList[indexPath.row].LeagueName
//            if leagueList.count != 0 {
//                if indexPath.row == 0 {
//                    cell.ifSelected = true
//                }else{
//                    cell.ifSelected = false
//                }
//
//            }
            if let leagueRank = leagueList[indexPath.row].LeagueRank {
                cell.labelLeagueRank.text = "Rank: \(leagueRank)"
            } else {
                cell.labelLeagueRank.text = "Rank: -"
            }
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == CollectionViewTournamentNames {
          //  tournamentSelection(rowItem: indexPath.row)          //making leagues appear double
        }else if collectionView == collectionViewTeamNames {
            leagueSelection(indexPath: indexPath)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if collectionView == CollectionViewTournamentNames {
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            (self.tabBarController as? VCTabBar)?.CurrentTournamentSelectedIndex = indexPath.row
            tournamentSelection(rowItem: indexPath.row)
            left_arrow.isEnabled = ( indexPath.row == 0 ) ? false : true
            right_arrow.isEnabled = ( indexPath.row == tournamentList.count-1 ) ? false : true
        }
    }
}

extension VCMyLeague: ShowsNotificationCount {
    func updateNotificationCount(_ notificationCount: Int?) {
        (self.navigationItem.leftBarButtonItem?.customView as? BadgeButton)?.badge = notificationCount == nil ? nil : "\(notificationCount!)"
    }
}
