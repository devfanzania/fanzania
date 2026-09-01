//
//  VCTournaments.swift
//  Fanzania
//
//  Created by Tathagata Dey on 15/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCTournaments: UIViewController, DelegateNewTeamNameVarified, UICollectionViewDelegateFlowLayout {
    
    func getTeam(team: String, teamId: Int) {
        newTeamName = team
        newTeamId = teamId
    }
    
    var joinTournament:UpcommingTournamentModel?
    var newTeamName : String?
    var newTeamId : Int?
    var tournamentSelectedIndex:Int = 0
    var isLoadtime = true
    
    @IBOutlet var labelMyTeamName: UILabel!
    @IBOutlet weak var overallPointCircle: UIView!
    @IBOutlet var labelOverallPoints: UILabel!
    @IBOutlet weak var globalRankCircle: UIView!
    @IBOutlet var labelGlobalRank: UILabel!
    @IBOutlet weak var globalPercentileL: UIView!
    @IBOutlet var labelGlobalPercentile: UILabel!
    @IBOutlet weak var TournamentListView: UICollectionView!
    @IBOutlet weak var upcomingTournamentsCollectionView: UICollectionView!
    @IBOutlet weak var tableLeagueData: UITableView!
    @IBOutlet weak var left_arrow: UIButton!
    @IBOutlet weak var right_arrow: UIButton!
    @IBOutlet weak var tournamentUserInfoContainer: UIView!
    @IBOutlet weak var homeLeagueContainer: UIView!
    @IBOutlet weak var upcomingTournamentsContainer: UIView!
    @IBOutlet weak var constraintUpcomingTournamentHeight: NSLayoutConstraint!
    @IBOutlet weak var noTournamentLabel: UILabel!
    @IBOutlet weak var noUpcomingTournamentLabel: UILabel!
    
    var userTeamValues:[(userTeam:String, totalPoints:Int, teamRank:Int, lastMatchPoints:Int)] = []
    var tournamentList:[UserTournamentModel] = []
    var myLeagueList:[MyLeaguesModel] = []
    var myUpcomingTournamentsList:[UpcommingTournamentModel] = []
    var currentTournament:UserTournamentModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        TournamentListView.dataSource = self
        TournamentListView.delegate = self
        tableLeagueData.delegate = self
        tableLeagueData.dataSource = self
        upcomingTournamentsCollectionView.delegate = self
        upcomingTournamentsCollectionView.dataSource = self
        
        setupUI()
        
        tableLeagueData.separatorStyle = .none
        
//        let gradientLayer = CAGradientLayer()
//        gradientLayer.frame = labelMyTeamName.bounds
//        gradientLayer.colors = [UIColor.colorCrimson().cgColor, UIColor.colorOrange().cgColor]
//        gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.0)
//        gradientLayer.endPoint = CGPoint(x: 1.0, y: 0.0)
//        labelMyTeamName.layer.insertSublayer(gradientLayer, at: 0)

    }
    
    func setupUI(){
        
        noTournamentLabel.text = {
            return String(format: StringConstants.NoTournamentText,
                          UserDefaults.standard.string(forKey: UserDefaultData.Name.rawValue)!)}()
        noUpcomingTournamentLabel.text = {
            return StringConstants.NoUpcomingTournamentText }()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        navigationSetup()
        refreshPage()
    }
    
    func navigationSetup(){
        let backButton = UIBarButtonItem(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(actionBack))
        navigationItem.backBarButtonItem = backButton
        navigationItem.backBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17.0)], for: UIControl.State.normal)
        
        let stackForStats:UIStackView = {
            let stack = UIStackView(frame: CGRect(x: 0, y: 0, width: 25, height: 25))
            stack.axis = .vertical
            let button: UIButton = UIButton(type: .custom)
            button.setImage(UIImage(named: "graph-small"), for: .normal)
            button.addTarget(self, action: #selector(self.actionStats), for: .touchUpInside)
            stack.addArrangedSubview(button)
            let label = UILabel()
            label.text = "Stats"
            label.font = UIFont.systemFont(ofSize: 10.0)
            label.textColor = UIColor.white
            stack.addArrangedSubview(label)
            return stack
        }()
        
        let notificationButton: UIBarButtonItem = {
            let button = BadgeButton()
            button.frame = CGRect(x: view.frame.width/2 - 22, y: view.frame.height/2 - 22, width: 44, height: 44)
            button.setImage(UIImage(named: "notification-bell")?.withRenderingMode(.alwaysOriginal), for: .normal)
            button.badgeEdgeInsets = UIEdgeInsets(top: 20, left: 0, bottom: 0, right: 15)
            button.addTarget(self, action: #selector(tappedNotifications), for: .touchUpInside)
            button.badge = nil
            return UIBarButtonItem(customView: button)
        }()
        
        let statsBarButton = UIBarButtonItem(customView: stackForStats)
        self.navigationItem.leftBarButtonItem = notificationButton
        self.navigationItem.rightBarButtonItems = [statsBarButton]
    }
    
    @objc func actionStats(){
        performSegue(withIdentifier: "segueTournamentStats", sender: self)
    }
    
    @objc func actionBack(){
        self.navigationController?.popViewController(animated: true)
    }
    
    func refreshPage(){
        getTournaments()
        getUpcomingTournaments()
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
    
    @IBAction func tournamentLeftProgress(_ sender: UIButton) {
        let collectionBounds = self.TournamentListView.bounds
        let contentOffset = CGFloat(floor(self.TournamentListView.contentOffset.x - collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    
    @IBAction func tournamentRightProgress(_ sender: UIButton) {
        let collectionBounds = self.TournamentListView.bounds
        let contentOffset = CGFloat(floor(self.TournamentListView.contentOffset.x + collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    func moveCollectionToFrame(contentOffset : CGFloat) {
        
        let frame: CGRect = CGRect(x : contentOffset ,y : self.TournamentListView.contentOffset.y ,width : self.TournamentListView.frame.width,height : self.TournamentListView.frame.height)
        self.TournamentListView.scrollRectToVisible(frame, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == TournamentListView {
            return CGSize(width: TournamentListView.frame.width, height: TournamentListView.frame.height)
        }else{
            return CGSize(width: 200 , height: upcomingTournamentsCollectionView.frame.height*3/4)
        }
    }
    
    func tournamentSelection(rowItem:Int){
        if rowItem >= tournamentList.count {
            return
        }
        let selectedTournament = tournamentList[rowItem]
        let userValue = userTeamValues[rowItem]
        self.labelOverallPoints.text = ( userValue.totalPoints == 0 ) ? "-" : String(describing : userValue.totalPoints)
        self.labelGlobalRank.text = ( userValue.teamRank == 0 ) ? "-" : String(describing: userValue.teamRank)
        self.labelGlobalPercentile.text = ( userValue.lastMatchPoints == 0 ) ? "-" : String(describing : userValue.lastMatchPoints)
        self.labelMyTeamName.text = userValue.userTeam
        currentTournament = selectedTournament
        tournamentSelectedIndex = rowItem
        (self.tabBarController as? VCTabBar)?.CurrentTournamentSelectedIndex = rowItem
        (self.tabBarController as? VCTabBar)?.CurrentTournamentID = tournamentList[rowItem].id
        getLeagues(tournamentId: selectedTournament.id!)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        var numOfSections = 0
        if myLeagueList.count != 0
        {
            numOfSections = 1
            tableLeagueData.backgroundView = nil
        }
        else
        {
            let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: tableLeagueData.bounds.size.width, height: tableLeagueData.bounds.size.height))
            noDataLabel.text = "No Leagues Available"
            noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
            noDataLabel.textColor = UIColor.black
            noDataLabel.textAlignment = .center
            tableLeagueData.backgroundView  = noDataLabel
        }
        return numOfSections
    }
    
    func firstInProgressTournamentIndex()->Int {
        for (index,tournament) in tournamentList.enumerated() {
            if tournament.status == "INPROGRESS" {
                return index
            }
        }
        return 0
    }

    
    func selectTournament(index:Int){
        
        if tournamentSelectedIndex != index {
        if self.tournamentList.count != 0 && self.tournamentList.count > index {
            self.TournamentListView.delegate?.collectionView!(self.TournamentListView, didSelectItemAt: IndexPath(row: index, section: 0))
            self.TournamentListView.selectItem(at: IndexPath(row: index, section: 0), animated: false, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
            self.TournamentListView.reloadData()
        }
        }
    }
    
    @IBAction func upcomingTournamentsTapped(_ sender: Any) {
        performSegue(withIdentifier: "segueVarifyTeam", sender: self)
    }
    
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? VCVarifyTeamName {
            self.modalPresentationStyle = .popover
            destination.tournamentId = joinTournament?.id
            destination.delegate = self
            destination.delegateTeamNameSend = self
        }else if let destination = segue.destination as? VCCreateNewTeam {
            destination.teamName = newTeamName
            destination.teamId = newTeamId
            destination.titleText = "\(newTeamName!)"
            destination.tournamentId = joinTournament?.id
            destination.tournamentStatus = joinTournament?.status
            destination.isCreatingTeam = true
            destination.delegate = self
        }else if let destination = segue.destination as? VCTournamentStats {
            destination.currentTournament = currentTournament
        }
    }
}

extension VCTournaments : UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if collectionView == TournamentListView {
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            
            tournamentSelection(rowItem: indexPath.row)
            left_arrow.isEnabled = ( indexPath.row == 0 ) ? false : true
            right_arrow.isEnabled = ( indexPath.row == tournamentList.count-1 ) ? false : true
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == TournamentListView {
            return tournamentList.count
        }else{
            return myUpcomingTournamentsList.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if collectionView == TournamentListView {
            let cell = TournamentListView.dequeueReusableCell(withReuseIdentifier: "TournamentsNameCollectionViewCell", for: indexPath) as! TournamentsNameCollectionViewCell
            cell.labelTournamentName.text = tournamentList[indexPath.row].name
            cell.labelTournamentDates.text = "\((tournamentList[indexPath.row].StartDate)!) to \((tournamentList[indexPath.row].EndDate)!)"
            
            return cell
        }else {
            let cell = upcomingTournamentsCollectionView.dequeueReusableCell(withReuseIdentifier: "UpcomingTournaments", for: indexPath) as! UpcomingTournamentsCollectionViewCell
            cell.labelTournamentName.text = myUpcomingTournamentsList[indexPath.row].name!
            getPlayerImages(imagename: myUpcomingTournamentsList[indexPath.row].image, playerImageView: cell.imageViewUpcomingTournaments)
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == TournamentListView {
            //tournamentSelection(rowItem: indexPath.row)    //making leagues appear double
        }else{
            joinTournament = myUpcomingTournamentsList[indexPath.row]
            performSegue(withIdentifier: "segueVarifyTeam", sender: self)
        }
    }
}

extension VCTournaments : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return myLeagueList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableLeagueData.dequeueReusableCell(withIdentifier: "cellLeagueData", for: indexPath)  as! MyLeaguesTableViewCell
        
        /*
         if (indexPath.row % 2) != 0{
         cell.leagueName.backgroundColor = colorListViewLightGrey
         cell.leagueStanding.backgroundColor = colorListViewLightGrey
         cell.leagueGlobalRank.backgroundColor = colorListViewLightGrey
         }else{
         cell.leagueName.backgroundColor = colorListViewLightYellow
         cell.leagueStanding.backgroundColor = colorListViewLightYellow
         cell.leagueGlobalRank.backgroundColor = colorListViewLightYellow
         }
         */
        cell.leagueName.text = myLeagueList[indexPath.row].LeagueName
        
        let myRank:Int? = myLeagueList[indexPath.row].TeamStanding
        cell.leagueStanding.text = ( myRank == nil || myRank == 0 ) ? "-" : String(describing: myRank!)
        let rank = myLeagueList[indexPath.row].LeagueRank
        cell.leagueGlobalRank.text = ( rank == nil || rank == 0 ) ? "-" : String(describing: rank!)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let nav = self.tabBarController?.viewControllers![AppSections.myLeague.index] as! UINavigationController
        let leagueController = nav.viewControllers.reversed().first(where: { $0 is VCMyLeague }) as? VCMyLeague
        leagueController?.preSelectedLeagueIndex = indexPath.row
        self.tabBarController?.selectedIndex = AppSections.myLeague.index
    }
}

extension VCTournaments {
    
    func getTournaments(){
        
        self.tournamentList.removeAll()
        self.userTeamValues.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_User_TournamentDetails,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.UserId.rawValue)!, "x-api-devicetype" : "ios"])
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
                                        let lastMatchPoints = element["LastMatchPoints"] as! Int
                                        
                                        self.userTeamValues.append((userTeam:UserTeamName, totalPoints: TotalPoints, teamRank: TeamRank, lastMatchPoints: lastMatchPoints))
                                        
                                        self.tournamentList.append(UserTournamentModel(name: TournamentName, id: TournamentId, stage: TournamentStage, status: TournamentStatus, UserId: UserId, UserTeamId: UserTeamId, UserTeamName: UserTeamName, TeamCompositionId: TeamCompositionId, TotalPoints: TotalPoints, TeamRank: TeamRank, StartDate: TournamentStartDate, EndDate:TournamentEndDate))
                                        
                                    }
                                }
                            }
                            DispatchQueue.main.async {
                                if self.tournamentList.count == 0 {
                                    self.tournamentUserInfoContainer.isHidden = true
                                    self.homeLeagueContainer.isHidden = true
                                    if let navButtons = self.navigationItem.rightBarButtonItems {
                                        for i in navButtons {
                                            i.isEnabled = false
                                            i.tintColor = UIColor.clear
                                        }
                                    }
                                }else{
                                    self.tournamentUserInfoContainer.isHidden = false
                                    self.homeLeagueContainer.isHidden = false
                                    if let navButtons = self.navigationItem.rightBarButtonItems {
                                        for i in navButtons {
                                            i.isEnabled = true
                                            i.tintColor = UIColor.white
                                        }
                                    }
                                }
                                self.TournamentListView.reloadData()
                                if self.isLoadtime {
                                    (self.tabBarController as! VCTabBar).CurrentTournamentSelectedIndex = self.firstInProgressTournamentIndex()
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
    
    func getPlayerImages(imagename:String?, playerImageView : UIImageView){
        
        let imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        let placeholder = UIImage(named: "upcoming-tournament-placeholder")
        
        if imagename != nil {
            
            let cachedImage = imageCache!.image(withIdentifier: imagename!)
            if cachedImage != nil{
                playerImageView.image = cachedImage
            }else{
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!+".png").responseImage { response in
                    
                    if let image = response.result.value {
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerImageView.image = image
                    }else{
                        playerImageView.image = placeholder
                        
                    }
                }
            }
            
        }else{
            playerImageView.image = placeholder
        }
    }
    
    func getUpcomingTournaments(){
        
        self.myUpcomingTournamentsList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_UpcomingTournamentsForUser,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue)] as [String : Any],
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
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            print(dataArray)
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    let TournamentId = element["TournamentId"] as! Int
                                    let TournamentName = element["TournamentName"] as? String
                                    let TournamentStage = element["TournamentStage"] as! String
                                    let TournamentStatus = element["TournamentStatus"] as! String
                                    let TournamentStartDate = element["TournamentStartDate"] as! String
                                    let TournamentEndDate = element["TournamentEndDate"] as! String
                                    let TournamentLogo = element["TournamentLogo"] as? String
                                    
                                    self.myUpcomingTournamentsList.append(UpcommingTournamentModel(name: TournamentName, stage: TournamentStage, status: TournamentStatus, id: TournamentId, startDate: TournamentStartDate, endDate: TournamentEndDate, image:TournamentLogo))
                                    
                                    
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                if self.myUpcomingTournamentsList.count == 0 {
                                    self.constraintUpcomingTournamentHeight.constant = 0
                                    //    self.upcomingTournamentsContainer.isHidden = true
                                }else{
                                    //    self.upcomingTournamentsContainer.isHidden = false
                                    self.constraintUpcomingTournamentHeight.constant = 201
                                }
                                self.upcomingTournamentsCollectionView.reloadData()
                                
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
    
    func getLeagues(tournamentId:Int){
        
        self.myLeagueList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        var param:[String:Int] = [:]
        param = [UserDefaultData.UserId.rawValue : UserDefaults.standard.integer(forKey: UserDefaultData.UserId.rawValue),
                 "TournamentId" : tournamentId]
        Alamofire.request(URL_Leagues_ByTournament,
                          method: .post,
                          parameters: param,
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
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            print(dataArray)
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
                                    self.myLeagueList.append(MyLeaguesModel(LeagueId: LeagueId, LeagueName: LeagueName, LeaguePoints: LeaguePoints, TeamStanding: TeamStanding, LeagueRank: LeagueRank, TournamentId: TournamentId, leaguePin: LeaguePin, LeagueLeaderId:LeagueLeaderId))
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.tableLeagueData.reloadData()
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

extension VCTournaments: ShowsNotificationCount {
    func updateNotificationCount(_ notificationCount: Int?) {
        (self.navigationItem.leftBarButtonItem?.customView as? BadgeButton)?.badge = notificationCount == nil ? nil : "\(notificationCount!)"
    }
}
