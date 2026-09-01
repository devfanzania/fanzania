//
//  VCCreateNewTeam.swift
//  Fanzania
//
//  Created by Tathagata Dey on 25/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class VCCreateNewTeam: UIViewController, DelegateGetPlayerSelectedList, VCPlayerCaptaincyDelegate  {

    
    @IBOutlet weak var matchListCollectionViewCarousalView: UIView!
    @IBOutlet weak var collectionviewMatchlist: UICollectionView!
    //UI Elements
    @IBOutlet weak var labelBudetRemaining: UILabel!
    @IBOutlet weak var labelTransferRemaining: UILabel!
    @IBOutlet weak var labelTeamName: UILabel!
    @IBOutlet weak var labelWinnerPrediction: UILabel!
    @IBOutlet var btnPlayerImage:[UIButton]!
    @IBOutlet var labelPlayerName:[UILabel]!
    @IBOutlet var labelPlayerPrice:[UILabel]!
    @IBOutlet var btnPlayerCaptaincy:[UIButton]!
    @IBOutlet var btnPlayerRole:[UIButton]!
    
    
//    @IBOutlet weak var labelKeeperCount: UILabel!
//    @IBOutlet weak var labelBatsmanCount: UILabel!
//    @IBOutlet weak var labelAllrounderCount: UILabel!
//    @IBOutlet weak var labelBowlerCount: UILabel!
    
    //controls
    @IBOutlet weak var btnSaveTeam: UIButton!
    @IBOutlet weak var painKillerSwitch: UISwitch!
    @IBOutlet weak var nitroBoosterSwitch: UISwitch!
    @IBOutlet weak var autoCaptainSwitch: UISwitch!
    @IBOutlet weak var secondaryButton: UIButton!
    @IBOutlet weak var resetButton: UIButton!
    
    @IBOutlet weak var FieldContainerHolder: UIView!
    @IBOutlet weak var fieldContainer: UIView!
    @IBOutlet weak var fieldContainerWidthConstraints: NSLayoutConstraint!
    
    //pre-loaded variables
    var delegate : VCTournaments?
    var delegateTeamPage : VCMyTeam?
    var batsmanList:[PlayerInfo] = []
    var allrounderList:[PlayerInfo] = []
    var bowlerList:[PlayerInfo] = []
    var wktKeeperList:[PlayerInfo] = []
    var isCreatingTeam:Bool?
    var tournamentId : Int?
    var tournamentStatus : String?
    var transferLeftAfterLastCutoff : Int = 0
    var transferRemaining:Int = PlayerSelectionRule.transferAllowedTotal
    var isLastCutoffTeamExists = true {
        didSet{
            if !isLastCutoffTeamExists {
                labelTransferRemaining.text = "∞"
            }
        }
    }
    
    var TeamCapt:Int?{
        didSet{
            print(TeamCapt)
        }
    }
    var captainPlayerIndex:Int?
    var TeamVCapt:Int?{
        didSet{
            print(TeamVCapt)
        }
    }
    var viceCaptainPlayerIndex:Int?
    var nitroLeft:Int = 0
    var autoPilotLeft:Int = 0
    var painkillerLeft:Int = 0
    var savedNitro : Int = 0
    var savedAutoCaptain : Int = 0
    var savedPainKiller : Int = 0
    
    //local lists and variables
    var cutoffTransferCaller:String = "TRANSFER_INITIAL"
    var lastSavedTeamIDs:[Int]?
    var allowcations:[PlayerInfo?] = []
    var cutOffPlayerList:[PlayerInfo] = []
    var budgetRemaining:Int?
    var totalPlayers: Int = 0
    var titleText : String?
    var teamName : String?
    var teamId : Int?
    var selectedPowerPlay:PowerPlayTypes?
    var painKiller : Bool = false
    var autoCaptain : Bool = false
    var nitroBooster : Bool = false
    let defaultPlayerPlaceHolderImage = UIImage(named: "create-player")
    var playerEditIndex : Int?
    var imageCache:ImageCache?
    var param = [String:Any]()
    var matchList:[MatchModel] = []
//    var team1ShortName: String?
//    var team2ShortName: String?
    var WinnerPrediction: String?
    
    func getCaptaincyIndex(index: Int, isCaptain: Bool) {
        if isCaptain {
            
            if captainPlayerIndex != nil {
            btnPlayerCaptaincy[captainPlayerIndex!].setImage(nil, for: .normal)
            }
            captainPlayerIndex = index
            TeamCapt = allowcations[index]!.id
            if TeamVCapt == TeamCapt{
                TeamVCapt = nil
                viceCaptainPlayerIndex = nil
            }
            btnPlayerCaptaincy[index].setImage(Captaincy.Captain.image, for: .normal)
        }else{
            if viceCaptainPlayerIndex != nil {
                btnPlayerCaptaincy[viceCaptainPlayerIndex!].setImage(nil, for: .normal)
            }
            if TeamVCapt == allowcations[index]!.id{
                TeamVCapt = nil
            }
            viceCaptainPlayerIndex = index
            TeamVCapt = allowcations[index]!.id
            if TeamCapt == TeamVCapt{
                TeamCapt = nil
                captainPlayerIndex = nil
            }
            btnPlayerCaptaincy[index].setImage(Captaincy.ViceCaptain.image, for: .normal)
        }
    }
    
    @objc func infoTapped(){
        performSegue(withIdentifier: "SegueTeamCreationInfo", sender: self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let infoNavButton = UIBarButtonItem(image: UIImage(named: "info"), style: .plain, target: self, action: #selector(infoTapped))
        self.navigationItem.rightBarButtonItem  = infoNavButton
        let backItem = UIBarButtonItem()
        backItem.title = "Back"
        navigationItem.backBarButtonItem = backItem
        self.navigationItem.title = "Manage Team"
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        labelTeamName.text = titleText
        resetButton.layer.cornerRadius = 15.0
        secondaryButton.layer.cornerRadius = 15.0
        secondaryButton.addTarget(self, action: #selector(openPlayerSelectionPage), for: .touchUpInside)
        collectionviewMatchlist.delegate = self
        collectionviewMatchlist.dataSource = self
        //collectionviewMatchlist.register(CollectionViewCellMatch.self, forCellWithReuseIdentifier: "CollectionViewCellMatch")
        let imageView : UIImageView = {
            let iv = UIImageView()
            iv.image = UIImage(named:"team_match_bg")
            iv.contentMode = .scaleAspectFill
            return iv
        }()
        collectionviewMatchlist.backgroundView = imageView
        
        imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        
        painKillerSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        nitroBoosterSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        autoCaptainSwitch.transform = CGAffineTransform(scaleX: 0.75, y: 0.75)
        
        if isCreatingTeam! {
            
            resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
            resetButton.addTarget(self, action: #selector(actionAutoFill), for: .touchUpInside)
            resetButton.setTitle("AutoFill", for: .normal)
        }else{
            resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
            resetButton.setTitle("Reset", for: .normal)
            resetButton.addTarget(self, action: #selector(actionReset), for: .touchUpInside)
        }
        
        let allPlayers = wktKeeperList + batsmanList + bowlerList + allrounderList
        if allPlayers.count == 0 {
            
            resetButton.setTitle("AutoFill", for: .normal)
            resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
            resetButton.addTarget(self, action: #selector(actionAutoFill), for: .touchUpInside)
        }
        
        if !isCreatingTeam! {
            var value = 0
            let temp = (wktKeeperList + batsmanList + bowlerList + allrounderList)
            for i in temp {
                value += i.value!
            }
            
            budgetRemaining = PlayerSelectionRule.TotalBudget - value
            labelBudetRemaining.text = String(budgetRemaining!) + "k"
            
            if savedAutoCaptain == 1 {
                autoCaptainSwitch.setOn(true, animated: true)
                autoCaptain = true
                selectedPowerPlay = .AutoCaptain
            }else{
                autoCaptainSwitch.setOn(false, animated: true)
                autoCaptain = false
                selectedPowerPlay = nil
            }
            if savedNitro == 1 {
                nitroBoosterSwitch.setOn(true, animated: true)
                nitroBooster = true
                selectedPowerPlay = .NitroBooster
            }else{
                nitroBoosterSwitch.setOn(false, animated: true)
                nitroBooster = false
                selectedPowerPlay = nil
            }
            if savedPainKiller == 1 {
                painKillerSwitch.setOn(true, animated: true)
                painKiller = true
                selectedPowerPlay = .PainKiller
            }else{
                painKillerSwitch.setOn(false, animated: true)
                painKiller = false
                selectedPowerPlay = nil
            }
            
            if autoPilotLeft == 0 {
                autoCaptainSwitch.setOn(true, animated: true)
                autoCaptainSwitch.isEnabled = false
            }
            if painkillerLeft == 0 {
                painKillerSwitch.setOn(true, animated: true)
                painKillerSwitch.isEnabled = false
            }
            if nitroLeft == 0 {
                nitroBoosterSwitch.setOn(true, animated: true)
                nitroBoosterSwitch.isEnabled = false
            }
        }else{
            budgetRemaining = PlayerSelectionRule.TotalBudget
            labelBudetRemaining.text = String(budgetRemaining!) + "k"
        }
        
        getAllMatchInfoByTournament()
        allocateFieldPositions()
        getCutOffTeamData()
        
        if tournamentStatus == "INPROGRESS" {
            
            if isCreatingTeam! {
                transferRemaining = PlayerSelectionRule.transferAllowedTotal
                transferLeftAfterLastCutoff = transferRemaining
                labelTransferRemaining.text = "∞"
            }else{
                labelTransferRemaining.text = "\(transferRemaining)/\(PlayerSelectionRule.transferAllowedTotal)"
            }
        }else{
            transferRemaining = PlayerSelectionRule.transferAllowedTotal
            transferLeftAfterLastCutoff = transferRemaining
            labelTransferRemaining.text = "∞"
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

    
    @objc func openPlayerSelectionPage(){
        performSegue(withIdentifier: "seguePlayerSelection", sender: self)
    }
    
    @objc func editPlayerPopup(_ sender:UIButton){
        playerEditIndex = sender.tag
        performSegue(withIdentifier: "seguePlayerEdit", sender: self)
    }
    
//    func removePlayer(index:Int){
//
//
//        totalPlayers -= 1
//        if totalPlayers == 0 {
//            secondaryButton.isEnabled = true
//            secondaryButton.createGradientLayer()
//        }else{
//            secondaryButton.isEnabled = false
//            secondaryButton.createDisableButtonGradientLayer()
//        }
//
//        let player = allowcations[index]!
//        switch player.speciality {
//        case .batsman:
//            self.batsmanList.remove(at: batsmanList.firstIndex(of: player)!)
//            //labelBatsmanCount.text = String(describing: batsmanList.count)
//        case .allrounder:
//            self.allrounderList.remove(at: allrounderList.firstIndex(of: player)!)
//            //labelAllrounderCount.text = String(describing: allrounderList.count)
//        case .bowler:
//            self.bowlerList.remove(at: bowlerList.firstIndex(of: player)!)
//            //labelBowlerCount.text = String(describing: bowlerList.count)
//        default:
//            self.wktKeeper.remove(at: wktKeeper.firstIndex(of: player)!)
//            //labelKeeperCount.text = String(describing: wktKeeper.count)
//        }
//
//
//        if tournamentStatus == "INPROGRESS" {
//            let postTransferTeamList = (wktKeeper + batsmanList + bowlerList + allrounderList).map { $0.id! }
//            let difference = postTransferTeamList.filter{ !lastSavedTeamIDs!.contains($0) }
//            transferRemaining = transferLeftAfterLastCutoff - difference.count
//            labelTransferRemaining.text = "\(transferRemaining)/\(PlayerSelectionRule.transferAllowedTotal)"
//        }else{
//            labelTransferRemaining.text = "∞"
//        }
//
//        budgetRemaining! += player.value!
//        self.labelBudetRemaining.text = String(describing: budgetRemaining!) + "k"
//
//        print(index)
//        print(labelPlayerPrice![index].text)
//        btnPlayerImage![index].setImage(defaultPlayerPlaceHolderImage, for: .normal)
//        btnPlayerImage![index].removeTarget(nil, action: nil, for: .touchUpInside)
//        btnPlayerImage![index].addTarget(self, action: #selector(openPlayerSelectionPage), for: .touchUpInside)
//        labelPlayerName![index].text = ""
//        labelPlayerPrice![index].text = ""
//        btnPlayerCaptaincy![index].setTitle("", for: .normal)
//        btnPlayerRole![index].setImage(nil, for: .normal)
//        allowcations[index] = nil
//
//        if TeamCapt == player.id {
//            TeamCapt = nil
//            captainPlayerIndex = nil
//        }else if TeamVCapt == player.id {
//            TeamVCapt = nil
//            viceCaptainPlayerIndex = nil
//        }
//    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? VCPlayerSelection {
            destination.selectedTournamentId = self.tournamentId
            destination.delegatePlayersSelected = self
            destination.selectedPlayerBatsmanList = batsmanList
            destination.selectedPlayerBowlerList = bowlerList
            destination.selectedPlayerAllrounderList = allrounderList
            destination.selectedPlayerWktKeeperList = wktKeeperList
            destination.transferLeftAfterLastCutoff = transferLeftAfterLastCutoff
            destination.selectedBatsmanCount = batsmanList.count
            destination.selectedAllrounderCount = allrounderList.count
            destination.selectedBowlerCount = bowlerList.count
            destination.selectedWktkeeperCount = wktKeeperList.count
            destination.isCreatingTeam = isCreatingTeam
            destination.lastSavedTeamIDs = lastSavedTeamIDs
            destination.transferRemaining = transferRemaining
            destination.budgetRemaining = budgetRemaining
            destination.tournamentStatus = tournamentStatus
            destination.captainID = TeamCapt
            destination.viceCaptainID = TeamVCapt
            destination.isLastCutoffTeamExists = isLastCutoffTeamExists
        }else if let destination = segue.destination as? VCPlayerEditPopUp {
            destination.groundPlayerIndex = playerEditIndex
            destination.playerName = allowcations[playerEditIndex!]?.name
            if allowcations[playerEditIndex!]?.id == TeamCapt {
                destination.isCaptain = true
            }else{
                destination.isCaptain = false
            }
            destination.delegate = self
            destination.returnDelegate = self
            self.modalPresentationStyle = .popover
        }else if let destination = segue.destination as? VCTeamTransferConfirmationMessage {
            destination.captainName = allowcations[captainPlayerIndex!]?.name
            destination.viceCaptainName = allowcations[viceCaptainPlayerIndex!]?.name
            destination.powerPlay = selectedPowerPlay
            destination.totalTransferSinceLastMatch = param["NumberOfSubs"] as? Int
            destination.tournamentStatus = tournamentStatus
            destination.team1ShortName = self.matchList.first?.Team1
            destination.team2ShortName = self.matchList.first?.Team2
            
            destination.WinnerPrediction = self.WinnerPrediction ?? ""
            self.modalPresentationStyle = .popover
            destination.parentVC = self
        }else if let destination = segue.destination as? VCInfoTeamComp {
            self.modalPresentationStyle = .popover
        }
    }
    
    @IBAction func actionSaveTeam(_ sender: UIButton) {
        if totalPlayers != PlayerSelectionRule.teamSize {
            
            let alert = UIAlertController(title: "Unable to save team", message: "Please Select all the \(PlayerSelectionRule.teamSize ) players", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
            
        }else if budgetRemaining!<0{
            
            let alert = UIAlertController(title: "Unable to save team", message: "Budget exceeded", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
            
        }else if transferRemaining<0 {
            
            let alert = UIAlertController(title: "Unable to save team", message: "No Transfer Remaining", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
            
        } else if TeamCapt == nil {
            
            let alert = UIAlertController(title: "Unable to save team", message: "Please Select a captain", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
        }else if TeamVCapt == nil {
            
            let alert = UIAlertController(title: "Unable to save team", message: "Please Select a Vice captain", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            self.present(alert, animated: true, completion: nil)
        }else{
            
            cutoffTransferCaller = "TRANSFER_SAVE_TEAM"
            getCutOffTeamData()
        }
        
    }
    
    func prepareTeamSaving(){
        
        let allocIds = allowcations.map { $0!.id }
        let difference = allocIds.filter{ !cutOffPlayerList.map{ $0.id }.contains($0) }
        
        param.removeAll()
        for (index,value) in allowcations.enumerated() {
            param["Player\(index+1)"] = value?.id!
        }
        param["UserTeamId"] = teamId
        param["PainKillerUsed"] = (painKiller == true ? 1:0)
        param["AutoPilotUsed"] = (autoCaptain == true ? 1:0)
        param["NitroUsed"] = (nitroBooster == true ? 1:0)
        param["TeamCapt"] = TeamCapt
        param["TeamVCapt"] = TeamVCapt
        param["NumberOfSubs"] =  difference.count
        
        performSegue(withIdentifier: "segueTeamConfirmation", sender: self)
        //saveTeamSelection(param : param)
    }
    
    @objc func actionReset(_ sender: UIButton){
        
        let alert =  UIAlertController(title: "Reset Team", message: "Your team will be set to the one you had fielded in the last match", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { action in
            self.cutoffTransferCaller = "TRANSFER_RESET"
            print("reset pressd")
            self.getCutOffTeamData()
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
        }))
        self.present(alert, animated: true, completion: nil)

    }
    
    @objc func actionAutoFill(_ sender: UIButton){
        
        print(totalPlayers)
        print("evade")
        if totalPlayers == 0 {
            getAutoFillData()
        }else{
            let invalid_login_alert = UIAlertController(title: "Auto-Select will only work on empty field", message: nil, preferredStyle: .alert)
            
            invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
            }))
            self.present(invalid_login_alert, animated: true, completion: nil)
        }
        
    }

    
    @IBAction func painKillerToggle(_ sender: UISwitch) {
        
        if sender.isOn{
            if autoCaptainSwitch.isEnabled {
                autoCaptain = false
                autoCaptainSwitch.setOn(false, animated: true)
            }
            if nitroBoosterSwitch.isEnabled {
                nitroBooster = false
                nitroBoosterSwitch.setOn(false, animated: true)
            }
            painKiller = true
            selectedPowerPlay = .PainKiller
        }else{
            painKiller = false
            selectedPowerPlay = nil
        }
    }
    
    @IBAction func nitroBoosterToggle(_ sender: UISwitch) {
        
        if sender.isOn{
            if painKillerSwitch.isEnabled {
                painKiller = false
                painKillerSwitch.setOn(false, animated: true)
            }
            if autoCaptainSwitch.isEnabled {
                autoCaptain = false
                autoCaptainSwitch.setOn(false, animated: true)
            }
            nitroBooster = true
            selectedPowerPlay = .NitroBooster
        }else{
            nitroBooster = false
            selectedPowerPlay = nil
        }
    }
    @IBAction func autoCaptainToggle(_ sender: UISwitch) {
        
        if sender.isOn{
            if painKillerSwitch.isEnabled {
                painKiller = false
                painKillerSwitch.setOn(false, animated: true)
            }
            if nitroBoosterSwitch.isEnabled {
                nitroBooster = false
                nitroBoosterSwitch.setOn(false, animated: true)
            }
            autoCaptain = true
            selectedPowerPlay = .AutoCaptain
        }else{
            autoCaptain = false
            selectedPowerPlay = nil
        }
    }
    
    func getList(batsman: [PlayerInfo], allrounder: [PlayerInfo], bowler: [PlayerInfo], WktKeeper: [PlayerInfo], budgetRemaining : Int, transferRemaining : Int, captainID:Int?, viceCaptainID:Int?) {
        wktKeeperList = WktKeeper
        batsmanList = batsman
        allrounderList = allrounder
        bowlerList = bowler
        
//        var isCaptainPresent = false
//        var isVCaptainPresent = false
//        for player in (WktKeeper+batsmanList+allrounderList+bowlerList){
//            if player.id == TeamCapt {
//                isCaptainPresent = true
//            }else if player.id == TeamVCapt {
//                isVCaptainPresent = true
//            }
//        }
//        if !isCaptainPresent {
//            TeamCapt = nil
//        }
//        if !isVCaptainPresent {
//            TeamVCapt = nil
//        }
        self.TeamCapt = captainID
        self.TeamVCapt = viceCaptainID
        
        self.budgetRemaining = budgetRemaining
        self.transferRemaining = transferRemaining
        
        labelBudetRemaining.text = String(budgetRemaining) + "k"
        allocateFieldPositions()
        
    }
    
    func allocateFieldPositions(){
        
        if tournamentStatus == "INPROGRESS"{
            labelTransferRemaining.text = "\(transferRemaining)/\(PlayerSelectionRule.transferAllowedTotal)"
        }else{
            labelTransferRemaining.text = "∞"
        }
        
        allowcations = [PlayerInfo]()
        let bowler_allrounder_merge:[PlayerInfo] = bowlerList + allrounderList
        allowcations.append(contentsOf: wktKeeperList)
        allowcations.append(contentsOf: batsmanList)
        allowcations.append(contentsOf: bowler_allrounder_merge)
        allowcations.append(contentsOf: Array(repeating: nil, count: 11-allowcations.count)) // was in prev code, not sure why
        
        totalPlayers = batsmanList.count + allrounderList.count + bowlerList.count + wktKeeperList.count
        
        if totalPlayers == 0 {
            resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
            resetButton.addTarget(self, action: #selector(actionAutoFill), for: .touchUpInside)
            resetButton.setTitle("AutoFil", for: .normal)
        }else{
            resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
            resetButton.setTitle("Reset", for: .normal)
            resetButton.addTarget(self, action: #selector(actionReset), for: .touchUpInside)
        }
        populateField()
        
    }
    
    
    func populateField(){
        
        let imageDummy = UIImage(named: "player-dummy")
        let imageDefault = UIImage(named: "create-player")
        
        var i=0
        while i<11 {
            if allowcations[i] != nil {
                btnPlayerImage[i].setImage(imageDummy, for: .normal)
                labelPlayerName[i].text = allowcations[i]?.shortName
                
                if allowcations[i]?.id == TeamCapt {
                    btnPlayerCaptaincy[i].setImage(Captaincy.Captain.image, for: .normal)
                    captainPlayerIndex = i
                }else if allowcations[i]?.id == TeamVCapt {
                    btnPlayerCaptaincy[i].setImage(Captaincy.ViceCaptain.image, for: .normal)
                    viceCaptainPlayerIndex = i
                } else{
                    btnPlayerCaptaincy[i].setImage(nil, for: .normal)
                }
                
                getPlayerImages(imagename: allowcations[i]?.playerImageName, playerImageButton: (btnPlayerImage?[i])!)
                
                switch allowcations[i]!.speciality {
                case .batsman :
                    btnPlayerRole?[i].setImage(PlayerSpeciality.batsman.image, for: .normal)
                case .bowler :
                    btnPlayerRole?[i].setImage(PlayerSpeciality.bowler.image, for: .normal)
                case .allrounder :
                    btnPlayerRole?[i].setImage(PlayerSpeciality.allrounder.image, for: .normal)
                default :
                    btnPlayerRole?[i].setImage(PlayerSpeciality.wicketKeeper.image, for: .normal)
                }
                labelPlayerPrice[i].text = String(describing: (allowcations[i]?.value)!) + "k"
                btnPlayerImage![i].removeTarget(nil, action: nil, for: .touchUpInside)
                btnPlayerImage[i].tag = i
                btnPlayerImage[i].addTarget(self, action: #selector(editPlayerPopup
                    ), for: .touchUpInside)
            }else{
                btnPlayerImage[i].setImage(imageDefault, for: .normal)
                labelPlayerName[i].text = ""
                btnPlayerCaptaincy[i].setImage(nil, for: .normal)
                btnPlayerRole[i].setImage(nil, for: .normal)
                labelPlayerPrice[i].text = ""
                btnPlayerImage![i].removeTarget(nil, action: nil, for: .touchUpInside)
                btnPlayerImage[i].addTarget(self, action: #selector(openPlayerSelectionPage), for: .touchUpInside)
            }
            i+=1
        }
//        labelKeeperCount.text = "0"+String(wktKeeper.count)
//        labelBatsmanCount.text = "0"+String(batsmanList.count)
//        labelBowlerCount.text = "0"+String(bowlerList.count)
//        labelAllrounderCount.text = "0"+String(allrounderList.count)
        
    }
    
    func saveTeamSelection(WinnerPrediction: String) {
        
        print(param)
        param["WinnerPrediction"] = WinnerPrediction
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_SaveTeamSelection,
                          method: .post,
                          parameters: param,
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
                        
                        self.WinnerPrediction = WinnerPrediction
                        self.labelWinnerPrediction.text = WinnerPrediction
                        
                        let invalid_login_alert = UIAlertController(title: "Team Change", message: "Successful", preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            
                            if self.isCreatingTeam! {
                                self.delegate?.refreshPage()
                            }else{
                                self.delegateTeamPage?.refreshMatchs()
                            }
                            NotificationCenter.default.post(name: NSNotification.CurrentTeamUpdatedNotification, object: nil)
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
    
    func getAutoFillData(){
        
        if totalPlayers != 0 {
            return
        }
        
        batsmanList.removeAll()
        allrounderList.removeAll()
        bowlerList.removeAll()
        wktKeeperList.removeAll()
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_AutoFill,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                       "UserTeamId" : teamId!] as [String : Any],
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
                            
                            var budget = 0
                            for item in dataArray{
                                
                                if let element = item as? [String:Any] {
                                    
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let playerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let PlayerShortName = element["PlayerShortName"] as! String
                                    let teamShortName = element["TeamShortName"] as! String
                                    //let PlayerPoints = element["PlayerPoints"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    let PlayerImageName = element["PlayerImage"] as? String
                                    self.TeamCapt = element["TeamCapt"] as? Int
                                    self.TeamVCapt = element["TeamVCapt"] as? Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    
                                    budget += PlayerValue
                                    let player = PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: playerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: 0, isPlayerSelected: true, shortName: PlayerShortName, playerImageName:PlayerImageName, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction)
                                    
                                        switch player.speciality {
                                        case .batsman:
                                            self.batsmanList.append(player)
                                        case .allrounder:
                                            self.allrounderList.append(player)
                                        case .bowler:
                                            self.bowlerList.append(player)
                                        default:
                                            self.wktKeeperList.append(player)
                                        }
                                }
                            }
                            self.WinnerPrediction = self.wktKeeperList.last?.WinnerPrediction
                            if let WinnerPrediction = self.WinnerPrediction,
                               WinnerPrediction.count > 0 {
                                self.labelWinnerPrediction.text = WinnerPrediction
                            } else {
                                self.labelWinnerPrediction.text = "-"
                            }
                            self.budgetRemaining = PlayerSelectionRule.TotalBudget - budget
                            self.labelBudetRemaining.text = String(self.budgetRemaining!) + "k"
                            self.transferRemaining = PlayerSelectionRule.transferAllowedTotal
                            self.resetButton.removeTarget(nil, action: nil, for: .touchUpInside)
                            self.resetButton.setTitle("Reset", for: .normal)
                            self.resetButton.addTarget(self, action: #selector(self.actionReset), for: .touchUpInside)
                            self.allocateFieldPositions()
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
    
    func getCutOffTeamData(){
        
        cutOffPlayerList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_CutOffLastTeam,
                          method: .post,
                          parameters: ["TournamentId" : tournamentId!,
                                       "UserTeamId" : teamId!] as [String : Any],
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
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            
                            var tempCapt:Int?
                            var teamVCapt:Int?
                            for item in dataArray{
                                
                                if let element = item as? [String:Any] {
                                    
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerId = element["PlayerId"] as! Int
                                    let PlayerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerType = element["PlayerType"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let PlayerShortName = element["PlayerShortName"] as? String
                                    let teamShortName = element["TeamShortName"] as? String
                                    //let PlayerPoints = element["PlayerPoints"] as! Int
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    //let PlayerImageName = element["PlayerImage"] as? String
                                    let TeamImage = element["TeamImage"] as? String
                                    tempCapt = element["TeamCapt"] as? Int
                                    teamVCapt = element["TeamVCapt"] as? Int
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    
                                    self.cutOffPlayerList.append(PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: PlayerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: 0, isPlayerSelected: true, shortName: PlayerShortName, playerImageName: TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction))
                                    
                                    
                                }
                            }
                            
                            if self.cutOffPlayerList.count == 0 {
                                self.isLastCutoffTeamExists = false
                                self.transferRemaining = PlayerSelectionRule.transferAllowedTotal
                            }else{
                                self.isLastCutoffTeamExists = true
                            }
                            
                            if self.cutoffTransferCaller == "TRANSFER_INITIAL" {
                                if self.cutOffPlayerList.count == 0 {
                                    self.lastSavedTeamIDs = []
                                }else{
                                    self.lastSavedTeamIDs = self.cutOffPlayerList.map{ $0.id! }
                                }
                            }else if self.cutoffTransferCaller == "TRANSFER_SAVE_TEAM" {
                                self.prepareTeamSaving()
                            }else{
                                
                                self.batsmanList.removeAll()
                                self.allrounderList.removeAll()
                                self.bowlerList.removeAll()
                                self.wktKeeperList.removeAll()
                                
                                var budget:Int = 0
                                for player in self.cutOffPlayerList {
                                    budget += player.value!
                                    switch player.speciality {
                                    case .batsman :
                                        self.batsmanList.append(player)
                                    case .allrounder:
                                        self.allrounderList.append(player)
                                    case .bowler:
                                        self.bowlerList.append(player)
                                    default:
                                        self.wktKeeperList.append(player)
                                    }
                                }
                                self.TeamCapt = tempCapt
                                self.TeamVCapt = teamVCapt
                                self.budgetRemaining = PlayerSelectionRule.TotalBudget - budget
                                self.labelBudetRemaining.text = String(self.budgetRemaining!) + "k"
                                if self.cutOffPlayerList.count == 0 {
                                    self.isLastCutoffTeamExists = false
                                    self.transferRemaining = PlayerSelectionRule.transferAllowedTotal
                                }else{
                                     self.transferRemaining = self.transferLeftAfterLastCutoff
                                }
                                self.allocateFieldPositions()
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
    
//    func getPlayerImages(imagename:String?, playerImageButton : UIButton){
//
//        if imagename != nil {
//
//            print("cachedImage")
//            print(imagename)
//
//            let cachedImage = imageCache!.image(withIdentifier: imagename!)
//            if cachedImage != nil{
//                
//                playerImageButton.setImage(cachedImage, for: UIControl.State.normal)
//            }else{
//                playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
//            }
//        }
//    }
    
    func getAllMatchInfoByTournament(){
        
        self.matchList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        print(["TournamentId" : tournamentId] as [String : Any])
        Alamofire.request(URL_Future_Matches,
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
                                if self.matchList.count == 0 {
                                    let backgroundLabel:UILabel = UILabel(frame: CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: self.collectionviewMatchlist.frame.width, height: self.collectionviewMatchlist.frame.height)))
                                    backgroundLabel.text = "This tournament has finished"
                                    backgroundLabel.textColor = UIColor.colorAppPrimary()()
                                    backgroundLabel.backgroundColor = UIColor.clear
                                    backgroundLabel.textAlignment = .center
                                    backgroundLabel.font = UIFont.systemFont(ofSize: 12)
                                    self.collectionviewMatchlist.backgroundView?.addSubview(backgroundLabel)
                                }
                                self.collectionviewMatchlist.reloadData()
                            }
                            self.fetchWinnerPrediction()
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
            
            if let cachedImage = imageCache!.image(withIdentifier: imagename!) {
                playerImageButton.setImage(cachedImage, for: UIControl.State.normal)
            }else{
                print("image path \(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!)")
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
    
    @objc func fetchWinnerPrediction() {
        
        guard let teamId = teamId,
              let matchId = matchList.first?.MatchId,
              let tournamentId = tournamentId
        else {
            return
        }
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_CurrentTeamWithPowerPlay,
                          method: .post,
                          parameters: ["UserTeamId" : teamId,
                                       "MatchId": matchId,
                                       "TournamentId" : tournamentId] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString(completionHandler: { [unowned self] (response) in
                debugPrint("URL_CurrentTeamWithPowerPlay: \(response.result.value ?? "")")
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
                        
                        if let dataArray = jsonDictionary["data"] as? [[String: Any]] {
                            if let WinnerPrediction = dataArray.first?["WinnerPrediction"] as? String,
                               WinnerPrediction.count > 0 {
                                self.labelWinnerPrediction.text = WinnerPrediction
                                self.WinnerPrediction = WinnerPrediction
                            } else {
                                self.WinnerPrediction = nil
                                self.labelWinnerPrediction.text = "-"
                            }
                        }
                    } else {
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

extension VCCreateNewTeam : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
       return matchList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         let cell = collectionviewMatchlist.dequeueReusableCell(withReuseIdentifier: "CollectionViewCellMatch", for: indexPath) as! CollectionViewCellMatch
        cell.dataSource = matchList[indexPath.row]
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 140.0, height: collectionviewMatchlist.frame.size.height)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 4.0
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 4.0
    }
    
}
