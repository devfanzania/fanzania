//
//  VCMyTeam.swift
//  Fanzania
//
//  Created by Tathagata Dey on 19/12/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

struct PlayerSelectionRule {
    
    static var WktkeeperMIN = 1
    static var WktkeeperMAX = 3
    static var AllrounderMAX = 3
    static var AllrounderMIN = 1
    static var BatsmanMAX = 5
    static var BatsmanMIN = 3
    static var BowlerMAX = 5
    static var BowlerMIN = 3
    static var SameTeamPlayer = 6
    static var teamSize = 11
    static var TotalBudget = 1000
    static var MaxOverseesPlayerCount = 5
    static var TransferAllowedFor_INPROGRESS_NewTeam = 11
    static var transferAllowedTotal = 40
    
    static func maxAllowedPlayerCount(type:PlayerSpeciality?)-> Int{
        switch type {
        case .some(.batsman):
            return BatsmanMAX
        case .some(.allrounder):
            return AllrounderMAX
        case .some(.bowler):
            return BowlerMAX
        case .some(.wicketKeeper):
            return WktkeeperMAX
        default:
            return teamSize
        }
    }
    static func minAllowedPlayerCount(type:PlayerSpeciality?)-> Int{
        switch type {
        case .some(.batsman):
            return BatsmanMIN
        case .some(.allrounder):
            return AllrounderMIN
        case .some(.bowler):
            return BowlerMIN
        case .some(.wicketKeeper):
            return WktkeeperMIN
        default:
            return teamSize
        }
    }

}

class VCMyTeam: UIViewController, UICollectionViewDelegateFlowLayout {

    @IBOutlet weak var collectionviewTournamentList: UICollectionView!
    @IBOutlet weak var collectionviewMatchlist: UICollectionView!
    @IBOutlet weak var labelTotalPoints: UILabel!
    @IBOutlet weak var labelPowerPlay: UILabel!
    @IBOutlet weak var labelTeamName: UILabel!
    @IBOutlet weak var left_arrow: UIButton!
    @IBOutlet weak var right_arrow: UIButton!
    @IBOutlet weak var manageButton: UIButton!
    @IBOutlet weak var powerplayButton: UIButton!
    
    @IBOutlet var playerImages:[UIButton]!
    @IBOutlet var labelPlayerNames:[UILabel]!
    @IBOutlet var playerRoles:[UIButton]!
    @IBOutlet var playerCaptaincy:[UIButton]!
    @IBOutlet var labelPlayerPrice:[UILabel]!
    @IBOutlet weak var labelKeeperCount: UILabel!
    @IBOutlet weak var labelBatsmanCount: UILabel!
    @IBOutlet weak var labelAllrounderCount: UILabel!
    @IBOutlet weak var labelBowlerCount: UILabel!
    @IBOutlet weak var labelWinnerPrediction: UILabel!
    @IBOutlet weak var groundMask: UIView!
    @IBOutlet weak var titleTeamThirdValue: UILabel!
    @IBOutlet weak var titleTeamSecondAttribute: UILabel!
    @IBOutlet weak var groundView: FieldOutlet!
    
    let dateFormat = DateFormatter()
    
    var tournamentList:[UserTournamentModel] = []
    var matchList:[MatchModel] = []
    var playerList:[PlayerInfo] = []
    var lastSavedPlayerList:[PlayerInfo] = []
    
    var batsmanList:[PlayerInfo] = []
    var allrounderList:[PlayerInfo] = []
    var keeperList:[PlayerInfo] = []
    var bowlerList:[PlayerInfo] = []
    
    //local variable
    var currentTournament:UserTournamentModel?
    var currentMatch:MatchModel?
    var tournamentSelectedIndex:Int = 0
    var currentTeamSubstituteLeft : Int = 0
    var cutOffTeamSubstituteLeft : Int = 0
    var captainId : Int?
    var viceCaptainId : Int?
    var currentTeamCapt:Int?
    var currentTeamViceCapt:Int?
    var nitroLeft:Int = 1
    var autoPilotLeft:Int = 1
    var painkillerLeft:Int = 1
    var savedNitro : Int = 0
    var savedAutoCaptain : Int = 0
    var savedPainKiller : Int = 0
    var isLoadtime = true
    var autoSelectMatchIndex = 0
    var WinnerPrediction: String?
    
    //flags
    var isForEditingTeam = false
    
    @IBAction @objc func actionStats(_ sender: UIBarButtonItem) {
        self.performSegue(withIdentifier: "segueTeamStats", sender: self)
    }
    
    @IBAction @objc func actionManage(_ sender: UIBarButtonItem) {
        self.isForEditingTeam = true
        self.getCurrentSavedTeamInfo()
    }
    
    @IBAction @objc func actionPowerplay(_ sender: UIBarButtonItem) {
        let vc = UIStoryboard(name: "PowerplayLifeline", bundle: .main).instantiateInitialViewController() as! VCPowerplay
        vc.UserId = UserDefaults.standard.integer(forKey: "UserId")
        vc.TournamentId = currentTournament?.id
        vc.UserTeamId = currentTournament!.UserTeamId
        navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        
        dateFormat.dateFormat = "dd-MMM-yyyy"
        dateFormat.timeZone = NSTimeZone(name: "UTC") as TimeZone?
        
        collectionviewTournamentList.delegate = self
        collectionviewTournamentList.dataSource = self
        collectionviewMatchlist.delegate = self
        collectionviewMatchlist.dataSource = self
        collectionviewMatchlist.allowsMultipleSelection = false
        // Do any additional setup after loading the view.
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.getCurrentSavedTeamInfo), name: NSNotification.CurrentTeamUpdatedNotification, object: nil)
        manageButton.layer.cornerRadius = 20
        manageButton.backgroundColor = UIColor.black
        manageButton.addTarget(self, action: #selector(actionManage), for: .touchUpInside)
        manageButton.layer.borderWidth = 3
        manageButton.layer.borderColor = UIColor.white.cgColor
        
        powerplayButton.layer.cornerRadius = 20
        powerplayButton.backgroundColor = UIColor.black
        powerplayButton.addTarget(self, action: #selector(actionPowerplay), for: .touchUpInside)
        powerplayButton.layer.borderWidth = 3
        powerplayButton.layer.borderColor = UIColor.white.cgColor
    }
    
    override func viewDidAppear(_ animated: Bool) {
        navigationSetup()
        getTournaments()
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
        if let destination = segue.destination as? VCCreateNewTeam {
            destination.titleText = (currentTournament?.UserTeamName)!
            destination.delegateTeamPage = self
            destination.tournamentId = currentTournament?.id
            destination.isCreatingTeam = false
            destination.batsmanList = batsmanList
            destination.allrounderList = allrounderList
            destination.bowlerList = bowlerList
            destination.wktKeeperList = keeperList
            destination.nitroLeft = nitroLeft
            destination.autoPilotLeft = autoPilotLeft
            destination.savedNitro = savedNitro
            destination.painkillerLeft = painkillerLeft
            destination.savedAutoCaptain = savedAutoCaptain
            destination.savedPainKiller = savedPainKiller
            destination.budgetRemaining = 0
            destination.transferRemaining = currentTeamSubstituteLeft
            destination.transferLeftAfterLastCutoff = cutOffTeamSubstituteLeft
            destination.tournamentStatus = currentTournament?.status
            destination.teamId = currentTournament!.UserTeamId!
            destination.TeamCapt = currentTeamCapt
            destination.TeamVCapt = currentTeamViceCapt
        } else if let destination = segue.destination as? VCTeamStats {
            destination.tournamentId = currentTournament?.id
            destination.userTeamId = currentTournament?.UserTeamId
            destination.currentTournament = currentTournament
            destination.teamName = currentTournament?.UserTeamName
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
    
    @IBAction func tappedEditTeamName(_ sender: Any) {
        showInputDialog({ [unowned self] (newName) in
            guard let newName = newName,
                  newName.count >= 3
            else {
                let invalidNameAlert = UIAlertController(title: "Invalid name",
                                                         message: "Team name must have at least 3 characters",
                                                         preferredStyle: UIAlertController.Style.alert)
                
                invalidNameAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                }))
                self.present(invalidNameAlert, animated: true, completion: nil)
                return
            }
            self.saveTeamName(teamName: newName)
        })
        
    }
    
    func matchSelection(index: Int){
        if index >= matchList.count { return }
        currentMatch = matchList[index]
        if currentMatch!.MatchStatus != "COMPLETE"{
            isForEditingTeam = false
            getCurrentSavedTeamInfo()
        }else{
            getMatchSpecificTeamsInfo(matchId: currentMatch!.MatchId!)
        }
    }
    
    func tournamentSelection(index : Int){
        if index >= tournamentList.count { return }
        let tournament = tournamentList[index]
        tournamentSelectedIndex = index
        (self.tabBarController as? VCTabBar)?.CurrentTournamentSelectedIndex = index
        print(index)
        currentTournament = tournament
        //labelTotalPoints.text = String(describing: tournament.TotalPoints!)
        labelTeamName.text = tournament.UserTeamName
        if tournament.status == "COMPLETE" {
            manageButton.isEnabled = false
            manageButton.isHidden = true
            
            powerplayButton.isEnabled = false
            powerplayButton.isHidden = true
        }else{
            manageButton.isEnabled = true
            manageButton.isHidden = false
            
            powerplayButton.isEnabled = true
            powerplayButton.isHidden = false
        }
        getTournamentRules()
        getAllMatchInfoByTournament(tournamentId: tournament.id!)
    }
    
    
    
    func selectTournament(index:Int){
        
        print("selected index \(index)")
        if tournamentSelectedIndex != index {
            if self.tournamentList.count != 0 && self.tournamentList.count > index {
                self.collectionviewTournamentList.delegate?.collectionView!(self.collectionviewTournamentList, didSelectItemAt: IndexPath(row: index, section: 0))
                self.collectionviewTournamentList.selectItem(at: IndexPath(row: index, section: 0), animated: false, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
                self.collectionviewTournamentList.reloadData()
            }
        }
    }
    
    func refreshMatchs(){
        getAllMatchInfoByTournament(tournamentId: (currentTournament?.id)!)
    }
    
    func populateGround(){
        
        let ball = UIImage(named: "ball")
        let bat = UIImage(named: "bat")
        let allrounder = UIImage(named: "bat-ball")
        let wktkeeper = UIImage(named: "gloves")
        var batsmanCount = 0
        var bowlerCount = 0
        var allrounderCount = 0
        
        if playerList.count == 0 {
            groundMask.isHidden = false
        }else{
            groundMask.isHidden = true
        }
        
//        for (index,player) in playerList.enumerated() {
//            labelPlayerNames[index].text = player.shortName!
//
//            if currentMatch?.MatchStatus != "COMPLETE" {
//                labelPlayerPrice[index].text = String(describing: player.value!) + "K"
//            }else{
//                labelPlayerPrice[index].text = String(describing: player.totalPoints!)
//            }
//
//            getPlayerImages(imagename: playerList[index].playerImageName, playerImageButton: playerImages[index])
//
//            switch player.speciality {
//            case .batsman :
//                playerRoles[index].setImage(PlayerSpeciality.batsman.image, for: .normal)
//                batsmanCount+=1
//            case .bowler :
//                playerRoles[index].setImage(PlayerSpeciality.bowler.image, for: .normal)
//                bowlerCount+=1
//            case .allrounder :
//                playerRoles[index].setImage(PlayerSpeciality.allrounder.image, for: .normal)
//                allrounderCount+=1
//            default :
//                playerRoles[index].setImage(PlayerSpeciality.wicketKeeper.image, for: .normal)
//            }
//            if player.id == captainId {
//                playerCaptaincy[index].setImage(Captaincy.Captain.image, for: .normal)
//            }else if player.id == viceCaptainId {
//                playerCaptaincy[index].setImage(Captaincy.ViceCaptain.image, for: .normal)
//            }else{
//                playerCaptaincy[index].setImage(nil, for: .normal)
//            }
//
//        }
//        labelKeeperCount.text = String(describing: 1)
//        labelBatsmanCount.text = String(describing: batsmanCount)
//        labelBowlerCount.text = String(describing: bowlerCount)
//        labelAllrounderCount.text = String(describing: allrounderCount)
        
        if playerList.count != 0 {
            groundView.dataSource = (list:playerList, teamCaptId:self.captainId!, teamVCaptId:self.viceCaptainId!)
        }
    }
    
    
    
}

typealias MatchAndTournamentList = VCMyTeam
extension MatchAndTournamentList : UICollectionViewDelegate, UICollectionViewDataSource {
    
    func moveCollectionToFrame(contentOffset : CGFloat) {
        
        let frame: CGRect = CGRect(x : contentOffset ,y : self.collectionviewTournamentList.contentOffset.y ,width : self.collectionviewTournamentList.frame.width,height : self.collectionviewTournamentList.frame.height)
        self.collectionviewTournamentList.scrollRectToVisible(frame, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == collectionviewTournamentList {
            return CGSize(width: collectionviewTournamentList.frame.width, height: collectionviewTournamentList.frame.height)
        }else{
            
//            let Font =  UIFont.systemFont(ofSize: 14.0)
//            let fontAttribute = [NSAttributedString.Key.font: Font]
//            let width1 = matchList[indexPath.row].Team1!.size(withAttributes: fontAttribute).width
//            let width2 = matchList[indexPath.row].Team2!.size(withAttributes: fontAttribute).width
//            let widthVS = "VS".size(withAttributes: fontAttribute).width
            let size = CGSize(width: (collectionView.frame.width - 16*2)/3, height: collectionView.bounds.height - 8)
            return size
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == collectionviewTournamentList {
            return tournamentList.count
        }else{
            return matchList.count
        }
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        var numOfSections: Int = 0
        
        if collectionView == collectionviewMatchlist {
            if matchList.count != 0
            {
                numOfSections            = 1
                collectionviewMatchlist.backgroundView = nil
            }
            else
            {
                let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: collectionviewMatchlist.bounds.size.width, height: collectionviewMatchlist.bounds.size.height))
                noDataLabel.text          = "No Match Available"
                noDataLabel.font = UIFont.systemFont(ofSize: 12.0)
                noDataLabel.textColor     = UIColor.black
                noDataLabel.textAlignment = .center
                collectionviewMatchlist.backgroundView  = noDataLabel
            }
        }else{
            if tournamentList.count != 0
            {
                numOfSections            = 1
                collectionviewTournamentList.backgroundView = nil
                
                if let navButtons = self.navigationItem.rightBarButtonItems {
                    for i in navButtons {
                        i.isEnabled = true
                        i.tintColor = UIColor.white
                    }
                }
                manageButton.isEnabled = true
                manageButton.isHidden = false
                
                powerplayButton.isEnabled = true
                powerplayButton.isHidden = false
            }
            else
            {
                if let navButtons = self.navigationItem.rightBarButtonItems {
                    for i in navButtons {
                        i.isEnabled = false
                        i.tintColor = UIColor.clear
                    }
                }
                manageButton.isEnabled = false
                manageButton.isHidden = true
                
                powerplayButton.isEnabled = false
                powerplayButton.isHidden = true
                
                let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: collectionviewTournamentList.bounds.size.width, height: collectionviewTournamentList.bounds.size.height))
                noDataLabel.text          = "No Tournaments Available"
                noDataLabel.font = UIFont.boldSystemFont(ofSize: 12.0)
                noDataLabel.textColor     = UIColor.white
                noDataLabel.textAlignment = .center
                collectionviewTournamentList.backgroundView  = noDataLabel
            }
        }
        
        return numOfSections
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == collectionviewTournamentList{
            let cell = collectionviewTournamentList.dequeueReusableCell(withReuseIdentifier: "myTeamTournamentCell", for: indexPath) as! TournamentsNameCollectionViewCell
            cell.labelTournamentName.text = tournamentList[indexPath.item].name
            cell.labelTournamentDates.text = "\((tournamentList[indexPath.item].StartDate)!) to \((tournamentList[indexPath.item].EndDate)!)"
            return cell
        }else{
            let cell = collectionviewMatchlist.dequeueReusableCell(withReuseIdentifier: "myTeamMatchCell", for: indexPath) as! MatchNamesCell
            if matchList.count > indexPath.item {
                let match = matchList[indexPath.item]
                cell.dataSource = match
            }
            if autoSelectMatchIndex == indexPath.item {
                cell.isSelected = true
            }else{
                cell.isSelected = false
            }
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if collectionView == collectionviewTournamentList {
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            
            tournamentSelection(index: indexPath.item)
            
            left_arrow.isEnabled = ( indexPath.row == 0 ) ? false : true
            right_arrow.isEnabled = ( indexPath.row == tournamentList.count-1 ) ? false : true
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == collectionviewMatchlist {
            self.autoSelectMatchIndex = indexPath.item
            matchSelection(index: indexPath.item)
        }
    }
}

typealias TeamAPIs = VCMyTeam
extension TeamAPIs {
    
    func getTournamentRules(){
        
        guard let currentTournament = currentTournament else {return}
        print(currentTournament.id!)
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_TeamSelectionRule,
                          method: .post,
                          parameters: ["TournamentId" : currentTournament.id!] as [String : Any],
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
                        
                        guard let responseArray = jsonDictionary["data"] as? NSArray else {
                            print("No proper json Data format")
                            return
                        }
                        
                        if let userData = responseArray[0] as? [String:Any] {
                            
                            //let WicketKeeper = userData["WicketKeeper"] as! Int
                            let MaxBatsman = userData["MaxBatsman"] as! Int
                            let MinBatsman = userData["MinBatsman"] as! Int
                            let MaxBowler = userData["MaxBowler"] as! Int
                            let MinBowler = userData["MinBowler"] as! Int
                            let MaxAllrounder = userData["MaxAllrounder"] as! Int
                            let MinAllrounder = userData["MinAllrounder"] as! Int
                            let MaxWicketKeeper = userData["MaxWicketKeeper"] as! Int
                            let MinWicketKeeper = userData["WicketKeeper"] as! Int
                            let MaxSameTeamPlayer = userData["MaxSameTeamPlayer"] as! Int
                            let TotalBudget = userData["TotalBudget"] as! Int
                            let MaxOverseasPlayer = userData["MaxOverseasPlayer"] as! Int
                            let SubCount = userData["SubCount"] as! Int
                            let NitroCount = userData["NitroCount"] as! Int
                            let PainKillerCount = userData["PainKillerCount"] as! Int
                            let AutoPilotCount = userData["AutoPilotCount"] as! Int
                            
                            PlayerSelectionRule.AllrounderMAX = MaxAllrounder
                            PlayerSelectionRule.AllrounderMIN = MinAllrounder
                            PlayerSelectionRule.BatsmanMAX = MaxBatsman
                            PlayerSelectionRule.BatsmanMIN = MinBatsman
                            PlayerSelectionRule.BowlerMAX = MaxBowler
                            PlayerSelectionRule.BowlerMIN = MinBowler
                            PlayerSelectionRule.WktkeeperMAX = MaxWicketKeeper
                            PlayerSelectionRule.WktkeeperMIN = MinWicketKeeper
                            PlayerSelectionRule.MaxOverseesPlayerCount = MaxOverseasPlayer
                            PlayerSelectionRule.TotalBudget = TotalBudget
                            PlayerSelectionRule.transferAllowedTotal = SubCount
                        }
                        
                        print("Structure Rules \(PlayerSelectionRule.MaxOverseesPlayerCount)")
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Could not fetch Tournament Rules : Try Later", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
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
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("Response String: \(response.result.value ?? "")")
            }
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
                                
                                self.collectionviewTournamentList.reloadData()
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
    
    func getAllMatchInfoByTournament(tournamentId:Int){
        
        self.matchList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        print(["TournamentId" : tournamentId] as [String : Any])
        Alamofire.request(URL_TOURNAMENT_AllMatches,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("Response String -  Tournament Info: \(response.result.value ?? "")")
            }
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
                                    let MatchId = element["MatchId"] as! Int
                                    let MatchDate = element["MatchDate"] as! String
                                    let MatchStatus = element["MatchStatus"] as! String
                                    let Team1 = element["Team1ShortName"] as! String
                                    let Team2 = element["Team2ShortName"] as! String
                                    let Venue = element["Venue"] as! String
                                    let MatchNo = element["MatchNo"] as! Int
                                    let weather = element["Weather"] as? String
                                    var BattingTeam = element["BattingTeam"] as? String
                                    let team1FullName = element["Team1"] as? String
                                    let team2FullName = element["Team2"] as? String
                                    
                                    if BattingTeam == team1FullName {
                                        BattingTeam = Team1
                                    } else if BattingTeam == team2FullName {
                                        BattingTeam = Team2
                                    }
                                    self.matchList.append(MatchModel(MatchId: MatchId, Team1: Team1, MatchNo: MatchNo, Team2: Team2, MatchStatus: MatchStatus, MatchDate: MatchDate, venue: Venue, weather: weather, BattingTeam: BattingTeam))
                                }
                            }
                            DispatchQueue.main.async {
                                if self.matchList.count != 0 {
                                    
                                    var flag:Bool = false
                                    for (index, match) in self.matchList.enumerated() {
                                        if match.MatchStatus == "UPCOMING" {
                                            self.autoSelectMatchIndex = (index)
                                            //self.collectionviewMatchlist.selectItem(at: IndexPath(row: , section: 0), animated: true, scrollPosition: UICollectionView.ScrollPosition.right)
                                            //self.collectionviewMatchlist.delegate?.collectionView!(self.collectionviewMatchlist, didSelectItemAt: IndexPath(row: (self.matchList.count - index - 1), section: 0))
                                            flag = true
                                            break
                                        }
                                    }
                                    if !flag {
                                        
                                        self.autoSelectMatchIndex = 0
                                        //self.collectionviewMatchlist.delegate?.collectionView!(self.collectionviewMatchlist, didSelectItemAt: IndexPath(row: 0, section: 0))
                                        //self.collectionviewMatchlist.selectItem(at: IndexPath(row: 0, section: 0), animated: false, scrollPosition: UICollectionView.ScrollPosition.left)
                                    }
                                }
                                self.collectionviewMatchlist.reloadData()
                            }
                            
                            DispatchQueue.main.async {
                                
                                self.collectionviewMatchlist.selectItem(at: IndexPath(item: self.autoSelectMatchIndex, section: 0), animated: false, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
                                self.matchSelection(index: self.autoSelectMatchIndex)
                                self.collectionviewMatchlist.layoutIfNeeded()
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
    
    func getMatchSpecificTeamsInfo(matchId: Int) {
        
        print("running match")
        self.playerList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Match_PostMatchDetails,
                          method: .post,
                          parameters: ["UserTeamId" : currentTournament!.UserTeamId!,
                                       "MatchId" : matchId,
                                       "TournamentId" : currentTournament!.id!] as [String : Any],
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
                        
                        var matchTotalPoints:Int = 0
                        var averagePoints:Int = 0
                        var tempNitroUsed:Int = 0
                        var tempAutoPilot:Int = 0
                        var tempPainKiller:Int = 0
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            for item in dataArray{
                                
                                if let element = item as? [String:Any] {
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerShortName = element["PlayerShortName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let PlayerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let PlayerPoints = element["PlayerPoints"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    let teamShortName = element["TeamShortName"] as? String
                                    let TeamImage = element["TeamImage"] as? String
                                    //let PlayerImageName = element["PlayerImage"] as? String
                                    self.captainId = element["TeamCapt"] as! Int
                                    matchTotalPoints = element["MatchTotalPoints"] as! Int
                                    averagePoints = element["AveragePoints"] as! Int
                                    self.viceCaptainId = element["TeamVCapt"] as! Int
                                    tempNitroUsed = element["NitroMultiplier"] as! Int
                                    tempAutoPilot = element["AutoPilotUsed"] as! Int
                                    tempPainKiller = element["PainKillerUsed"] as! Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    self.playerList.append(PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: PlayerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: PlayerPoints, isPlayerSelected: true, shortName:PlayerShortName, playerImageName:TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction))
                                }
                            }
                            
                            if let WinnerPrediction = self.playerList.last?.WinnerPrediction,
                               WinnerPrediction.count > 0 {
                                self.labelWinnerPrediction.text = WinnerPrediction
                                self.WinnerPrediction = WinnerPrediction
                            } else {
                                self.labelWinnerPrediction.text = "-"
                            }
                            self.labelTotalPoints.text = String(describing: matchTotalPoints) + "|" + String(describing: averagePoints)
                            self.titleTeamThirdValue.text = "Pts|Avg. Pts."
                            
                            if tempAutoPilot == 1 {
                                self.labelPowerPlay.text = "Ultra-Captain"
                            }else if tempNitroUsed == 1 {
                                self.labelPowerPlay.text = "Nitro"
                            }else if tempPainKiller == 1 {
                                self.labelPowerPlay.text = "Pain-Killer"
                            }else{
                                self.labelPowerPlay.text = "-"
                            }
                            self.titleTeamSecondAttribute.text = "Power Play"
                            self.groundView.isShowingAccountableNumber = .points
                            self.populateGround()
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
    
    @objc func getCurrentSavedTeamInfo() {
        
        batsmanList.removeAll()
        allrounderList.removeAll()
        bowlerList.removeAll()
        keeperList.removeAll()
        lastSavedPlayerList.removeAll()
        
        if currentTournament == nil {
            return
        }
        var matchID: Int?
        if self.isForEditingTeam {
            matchID = matchList.first(where: { $0.MatchStatus == "UPCOMING" } )?.MatchId
        } else {
            matchID = currentMatch!.MatchId!
        }
        
        guard let matchId = matchID else {
            return
        }
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_CurrentTeamWithPowerPlay,
                          method: .post,
                          parameters: ["UserTeamId" : currentTournament!.UserTeamId!,
                                       "MatchId": matchId,
                                       "TournamentId" : currentTournament!.id!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString(completionHandler: { [unowned self] (response) in
                debugPrint("URL_CurrentTeamWithPowerPlay: \(response.result.value ?? "")")
                debugPrint("MatchID: \(self.currentMatch!.MatchId!)")
            })
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
                        
                        var totalBudget = 0
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            //new created team with no player yet
                            if dataArray.count == 0 {
                                
                                self.cutOffTeamSubstituteLeft = 0
                                self.currentTeamSubstituteLeft = PlayerSelectionRule.transferAllowedTotal
                                self.nitroLeft = 1
                                self.autoPilotLeft = 1
                                self.painkillerLeft = 1
                                self.savedNitro = 0
                                self.savedAutoCaptain = 0
                                self.savedPainKiller = 0
                                self.captainId = nil
                                self.viceCaptainId = nil
                                self.currentTeamCapt = nil
                                self.currentTeamViceCapt = nil
                            }else{
                                for item in dataArray{
                                    
                                    if let element = item as? [String:Any] {
                                        let PlayerName = element["PlayerName"] as! String
                                        let PlayerId = element["PlayerId"] as! Int
                                        let playerSpeciality = element["PlayerSpeciality"] as! String
                                        let PlayerType = element["PlayerType"] as! String
                                        let PlayerValue = element["PlayerValue"] as! Int
                                        totalBudget += PlayerValue
                                        let PlayerShortName = element["PlayerShortName"] as? String
                                        let PlayerPoints = element["PlayerPoints"] as! Int
                                        let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                        let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                        let TeamImage = element["TeamImage"] as? String
                                        //let PlayerImageName = element["PlayerImage"] as? String
                                        let teamShortName = element["TeamShortName"] as! String
                                        self.currentTeamSubstituteLeft = element["SubsLeft"] as! Int
                                        self.cutOffTeamSubstituteLeft = element["SubsLeftAtSnapShot"] as! Int
                                        self.currentTeamCapt = element["TeamCapt"] as? Int
                                        self.currentTeamViceCapt = element["TeamVCapt"] as? Int
                                        self.captainId = self.currentTeamCapt
                                        self.viceCaptainId = self.currentTeamViceCapt
                                        
                                        self.nitroLeft = element["NitroLeft"] as! Int
                                        self.autoPilotLeft = element["AutoPilotLeft"] as! Int
                                        self.painkillerLeft = element["PainKillerLeft"] as! Int
                                        self.savedNitro = element["NitroUsed"] as! Int
                                        self.savedAutoCaptain = element["AutoPilotUsed"] as! Int
                                        self.savedPainKiller = element["PainKillerUsed"] as! Int
                                        let isPlaying = element["PlayingInd"] as? Bool
                                        let WinnerPrediction = element["WinnerPrediction"] as? String
                                        
                                        let player = PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: playerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: PlayerPoints, isPlayerSelected: true, shortName:PlayerShortName, playerImageName:TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction)
                                        switch player.speciality {
                                        case .batsman :
                                            self.batsmanList.append(player)
                                        case .allrounder:
                                            self.allrounderList.append(player)
                                        case .bowler:
                                            self.bowlerList.append(player)
                                        default:
                                            self.keeperList.append(player)
                                        }
                                    }
                                }
                                if let WinnerPrediction = self.bowlerList.last?.WinnerPrediction,
                                   WinnerPrediction.count > 0 {
                                    self.labelWinnerPrediction.text = WinnerPrediction
                                    self.WinnerPrediction = WinnerPrediction
                                } else {
                                    self.labelWinnerPrediction.text = "-"
                                }
                            }
                            
                            if self.currentTournament?.status == "INPROGRESS" {
                                self.labelTotalPoints.text = String(describing: self.currentTeamSubstituteLeft)
                            }else{
                                self.labelTotalPoints.text = "∞"
                            }
                            self.titleTeamThirdValue.text = StringConstants.transfersRemainingText
                            
                            if self.savedAutoCaptain == 1 {
                                self.labelPowerPlay.text = "Ultra-Captain"
                            }else if self.savedNitro == 1 {
                                self.labelPowerPlay.text = "Nitro"
                            }else if self.savedPainKiller == 1 {
                                self.labelPowerPlay.text = "Pain-Killer"
                            }else{
                                self.labelPowerPlay.text = "-"
                            }
                            self.titleTeamSecondAttribute.text = "Power Play"
                            
                            self.lastSavedPlayerList = self.keeperList + self.batsmanList + self.bowlerList + self.allrounderList
                            if self.isForEditingTeam {
                                self.isForEditingTeam = false
                                self.performSegue(withIdentifier: "segueManageTeam", sender: self)
                            }else{
                                self.playerList.removeAll()
                                self.playerList = self.lastSavedPlayerList
                                self.groundView.isShowingAccountableNumber = .cost
                                self.populateGround()
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
    
    func getPlayerImages(imagename:String?, playerImageButton : UIButton){
        
        let imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        
        if imagename != nil {
            
            let cachedImage = imageCache!.image(withIdentifier: imagename!)
            if cachedImage != nil{
                playerImageButton.setImage(cachedImage, for: UIControl.State.normal)
            }else{
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!).responseImage { response in
                    
                    if let image = response.result.value {
                        print("image downloaded: \(image)")
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerImageButton.setImage(image, for: .normal)
                    }else{
                        playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
                    }
                }
            }
            
        }else{
            playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
        }
    }
    
    func saveTeamName(teamName: String) {
        
        let params: [String: Any] = [
            "TournamentId": currentTournament?.id ?? "",
            "UserTeamId" : currentTournament?.UserTeamId ?? "",
            "UserTeamName": teamName
        ]
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_ModifyTeamName,
                          method: .post,
                          parameters: params,
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
                        
                        let invalid_login_alert = UIAlertController(title: "Team Named Changed", message: "Successful", preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            
                            self.labelTeamName.text = teamName
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
}

extension VCMyTeam: ShowsNotificationCount {
    func updateNotificationCount(_ notificationCount: Int?) {
        (self.navigationItem.leftBarButtonItem?.customView as? BadgeButton)?.badge = notificationCount == nil ? nil : "\(notificationCount!)"
    }
}

extension UIViewController {
    func showInputDialog(title:String? = "Edit Team Name",
                         subtitle:String? = "Team name minimum 3 characters",
                         actionTitle:String? = "Confirm",
                         cancelTitle:String? = "Close",
                         inputPlaceholder:String? = "Enter New Name",
                         inputKeyboardType:UIKeyboardType = UIKeyboardType.default,
                         cancelHandler: ((UIAlertAction) -> Swift.Void)? = nil,
                         _ actionHandler: ((_ text: String?) -> Void)? = nil) {
        
        let alert = UIAlertController(title: title, message: subtitle, preferredStyle: .alert)
        alert.addTextField { (textField:UITextField) in
            textField.placeholder = inputPlaceholder
            textField.keyboardType = inputKeyboardType
        }
        alert.addAction(UIAlertAction(title: actionTitle, style: .default, handler: { (action:UIAlertAction) in
            guard let textField =  alert.textFields?.first else {
                actionHandler?(nil)
                return
            }
            actionHandler?(textField.text)
        }))
        alert.addAction(UIAlertAction(title: cancelTitle, style: .cancel, handler: cancelHandler))
        
        self.present(alert, animated: true, completion: nil)
    }
}
