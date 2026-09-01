//
//  VCPlayerSelection.swift
//  Fanzania
//
//  Created by Tathagata Dey on 26/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

protocol DelegateGetPlayerSelectedList {
    func getList(batsman : [PlayerInfo], allrounder : [PlayerInfo], bowler : [PlayerInfo], WktKeeper : [PlayerInfo], budgetRemaining:Int, transferRemaining:Int, captainID:Int?, viceCaptainID:Int?)
}

class VCPlayerSelection: UIViewController, UISearchBarDelegate, UISearchResultsUpdating, DelegateTeamSelection{
    
    @IBOutlet weak var labelBudgetRemaining: UILabel!
    @IBOutlet weak var labelTransferRemaining: UILabel!
    @IBOutlet weak var collectionViewRoleSelect: UICollectionView!
    @IBOutlet weak var collectionViewMatchlist: UICollectionView!
    @IBOutlet weak var tablePlayerDetails: UITableView!
    @IBOutlet weak var btnToggleSortingByPrice: UIButton!
    @IBOutlet weak var btnToggleSortingByPoints: UIButton!
    @IBOutlet weak var playerNamesTagCollectionView: UICollectionView!
    @IBOutlet var playerNamesTagHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var searchBarHeightConstraint: NSLayoutConstraint!
    
    //@IBOutlet weak var labelSelectedPlayersCount: UILabel!
    @IBOutlet weak var btnViewCollapse: UIButton!
    @IBOutlet weak var searchPlayer: UISearchBar!
    @IBOutlet weak var btnFilterByTeam: UIButton!
    @IBOutlet weak var TeamPreviewButtonContainer: UIView!
    
    // Player Selection Rule
    @IBOutlet weak var lblCountAll: UILabel!
    @IBOutlet weak var lblCountBatsman: UILabel!
    @IBOutlet weak var lblCountKeeper: UILabel!
    @IBOutlet weak var lblCountAllrounder: UILabel!
    @IBOutlet weak var lblCountBowler: UILabel!
    

    var playerSelectedItemDimension: CGSize!
    let minimumSpacing:CGFloat = {
        if GlobalVars.shared.isIpad(){
            return 6.0
        }else{
            return 2.0
        }
    }()
    let noOfCols:Int = {
        if GlobalVars.shared.isIpad(){
            return 3
        }else{
            return 2
        }
    }()
    
    var tournamentPlayerList:[PlayerInfo] = []
    var filteredByTeamData:[PlayerInfo] = []
    var tournamentPlayerListFilteredByRole:[PlayerInfo] = []
    var selectedTeams:[Int] = []
    var playerRoleCategoryCell:[PlayerSpeciality?] = [nil, .batsman, .wicketKeeper, .allrounder, .bowler]
    var isSearchActive = false;
    var searchedActiveWord = ""
    
    //Pre-Loaded variables
    var transferLeftAfterLastCutoff:Int?
    var currentOverSeesPlayerCount:Int?
    var selectedTournamentId:Int?
    var totalSelectedPlayersCount:Int = 0 {
        didSet{
            if let collectionview = collectionViewRoleSelect{
                if let indexPaths = collectionview.indexPathsForSelectedItems, indexPaths.count != 0 {
                    print("for common \(indexPaths[0].row)")
                    collectionview.reloadItems(at: [IndexPath(row: 0, section: 0)])
                    collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    //collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    //collectionview.reloadItems(at: [IndexPath(row: 0, section: 0)])
//                        if indexPaths[0].row == 0 {
//                             print("for selection")
//                            collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
//                        }
                    
                }
            }
        }
    }
    
    var selectedWktkeeperCount = 0 {
        didSet{
            if let collectionview = collectionViewRoleSelect{
                if let indexPaths = collectionview.indexPathsForSelectedItems, indexPaths.count != 0 {
                    print("for keep \(indexPaths[0].row)")
                    collectionview.reloadItems(at: [IndexPath(row: 2, section: 0)])
                    if indexPaths[0].row == 2 {
                        print("for selection")
                        collectionview.selectItem(at: IndexPath(row: 2, section: 0), animated: false, scrollPosition: .centeredVertically)
                    }else{
                        collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    }
                    
                }
            }
        }
    }
    var selectedBatsmanCount = 0 {
        didSet{
            if let collectionview = collectionViewRoleSelect{
                if let indexPaths = collectionview.indexPathsForSelectedItems, indexPaths.count != 0 {
                    print("for bat \(indexPaths[0].row)")
                    collectionview.reloadItems(at: [IndexPath(row: 1, section: 0)])
                    if indexPaths[0].row == 1 {
                        collectionview.selectItem(at: IndexPath(row: 1, section: 0), animated: false, scrollPosition: .centeredHorizontally)
                    }else{
                        collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    }
                    
                }
            }
        }
    }
    var selectedAllrounderCount = 0 {
        didSet{
            if let collectionview = collectionViewRoleSelect{
                let indexPaths = collectionview.indexPathsForSelectedItems
                if let indexPaths = collectionview.indexPathsForSelectedItems, indexPaths.count != 0 {
                    print("for allr \(indexPaths[0].row)")
                    collectionview.reloadItems(at: [IndexPath(row: 3, section: 0)])
                    if indexPaths[0].row == 3 {
                        collectionview.selectItem(at: IndexPath(row: 3, section: 0), animated: false, scrollPosition: .centeredHorizontally)
                    }else{
                        collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    }
                    
                }
            }
        }
    }
    var selectedBowlerCount = 0 {
        didSet{
            if let collectionview = collectionViewRoleSelect{
                if let indexPaths = collectionview.indexPathsForSelectedItems, indexPaths.count != 0 {
                    print("for bowler \(indexPaths[0].row)")
                    collectionview.reloadItems(at: [IndexPath(row: 4, section: 0)])
                    if indexPaths[0].row == 4 {
                        collectionview.selectItem(at: IndexPath(row: 4, section: 0), animated: false, scrollPosition: .centeredHorizontally)
                    }else{
                        collectionview.selectItem(at: indexPaths[0], animated: false, scrollPosition: .centeredHorizontally)
                    }
                }
            }
        }
    }
    var tournamentStatus:String?
    var budgetRemaining:Int?
    var transferRemaining:Int?
    
    // true for edit false for new team create
    
    //selection lists
    var selectedPlayerBatsmanList:[PlayerInfo] = []
    var selectedPlayerAllrounderList:[PlayerInfo] = []
    var selectedPlayerBowlerList:[PlayerInfo] = []
    var selectedPlayerWktKeeperList:[PlayerInfo] = []
    var allSelectedPlayers:[PlayerInfo] = [] {
        didSet{
            
            for (index, player) in allSelectedPlayers.enumerated() {
                if player.id == captainID {
                    allSelectedPlayers[index].isCaptain = true
                }else{
                    allSelectedPlayers[index].isCaptain = false
                }
                if player.id == viceCaptainID {
                    allSelectedPlayers[index].isViceCaptain = true
                }else{
                    allSelectedPlayers[index].isViceCaptain = false
                }
            }
        }
    }
    var searchedData = [PlayerInfo]()
    var captainID:Int?
    var viceCaptainID:Int?
    
    //delegates
    var delegatePlayersSelected:DelegateGetPlayerSelectedList?
    
    //local variables
    
    var playersByCountry = [Int:Int?]()
    
    var isPriceOrderIncreasing = true {
        didSet{
            if isPriceOrderIncreasing {
                tournamentPlayerListFilteredByRole = tournamentPlayerListFilteredByRole.sorted(by: { $0.value! < $1.value! })
                btnToggleSortingByPrice.setImage(increasingSort, for: .normal)
            }else{
                tournamentPlayerListFilteredByRole = tournamentPlayerListFilteredByRole.sorted(by: { $0.value! > $1.value! })
                btnToggleSortingByPrice.setImage(decreasingSort, for: .normal)
            }
            self.tablePlayerDetails.reloadData()
        }
    }
    
    var isPointsOrderIncreasing = true {
        didSet{
            if isPointsOrderIncreasing {
                tournamentPlayerListFilteredByRole = tournamentPlayerListFilteredByRole.sorted(by: { $0.totalPoints! < $1.totalPoints! })
                btnToggleSortingByPoints.setImage(increasingSort, for: .normal)
            }else{
                tournamentPlayerListFilteredByRole = tournamentPlayerListFilteredByRole.sorted(by: { $0.totalPoints! > $1.totalPoints! })
                btnToggleSortingByPoints.setImage(decreasingSort, for: .normal)
            }
            self.tablePlayerDetails.reloadData()
        }
    }
    
    var isSelectedPlayersTagViewCollapsed = false {
        didSet{
            if !isSelectedPlayersTagViewCollapsed {
                playerNamesTagHeightConstraint.constant = heightOfTagList!
                btnViewCollapse.setImage(uparrow, for: .normal)
            }else{
                playerNamesTagHeightConstraint.constant = 0
                btnViewCollapse.setImage(downarrow, for: .normal)
            }
        }
    }
    let increasingSort = UIImage(named: "sort-up")
    let downarrow = UIImage(named: "down-arrow")
    let uparrow = UIImage(named: "up-arrow")
    let decreasingSort = UIImage(named: "sort-down")
    let iconFilter = UIImage(named: "filter")
    let iconFilterGreen = UIImage(named: "Filter-Green")
    var heightOfTagList:CGFloat?
    var currentSelectedRole:PlayerSpeciality?
    var isCreatingTeam:Bool?
    var lastSavedTeamIDs:[Int]?
    var postTransferTeamList:[Int] = []
    var matchList:[MatchModel] = []
    var overSeesPlayerCounter = 0
    var isLastCutoffTeamExists = false
    
    var searchController: UISearchController = {
        let searchController = UISearchController(searchResultsController: nil)
        searchController.dimsBackgroundDuringPresentation = false
        searchController.searchBar.sizeToFit()
        return searchController
    }()
    
    @IBAction func removeSearchBar(_ sender: UIButton) {
        UIView.animate(withDuration: 0.5) {
            self.isSearchActive = false
            self.searchPlayer.text = ""
            self.tablePlayerDetails.reloadData()
            self.searchBarHeightConstraint.constant = 0
            self.view.layoutIfNeeded()
        }
    }
    @IBAction func openSearchBar(_ sender: UIButton) {
        UIView.animate(withDuration: 0.5) {
            self.searchBarHeightConstraint.constant = 40.0
            self.view.layoutIfNeeded()
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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        self.hideKeyboardWhenTappedAround()
        collectionViewRoleSelect.delegate = self
        collectionViewRoleSelect.dataSource = self
        playerNamesTagCollectionView.delegate = self
        playerNamesTagCollectionView.dataSource = self
        collectionViewMatchlist.delegate = self
        collectionViewMatchlist.dataSource = self
        //collectionViewMatchlist.register(CollectionViewCellMatch.self, forCellWithReuseIdentifier: "CollectionViewCellMatch")
        tablePlayerDetails.delegate = self
        tablePlayerDetails.dataSource = self
        searchPlayer.delegate = self
        let imageView : UIImageView = {
            let iv = UIImageView()
            iv.image = UIImage(named:"team_match_bg")
            iv.contentMode = .scaleAspectFill
            return iv
        }()
        collectionViewMatchlist.backgroundView = imageView
        
        searchController.searchResultsUpdater = self
        definesPresentationContext = true
        
        labelBudgetRemaining.text = String(budgetRemaining!) + "k"
        if selectedPlayerWktKeeperList.count != 0 {
            allSelectedPlayers.append(contentsOf : selectedPlayerWktKeeperList)
        }
        if selectedPlayerBatsmanList.count != 0 {
            allSelectedPlayers.append(contentsOf : selectedPlayerBatsmanList)
        }
        if selectedPlayerBowlerList.count != 0 {
            allSelectedPlayers.append(contentsOf : selectedPlayerBowlerList)
        }
        if selectedPlayerAllrounderList.count != 0 {
            allSelectedPlayers.append(contentsOf : selectedPlayerAllrounderList)
        }
        
        for player in allSelectedPlayers {
            if player.type != "local" {
                overSeesPlayerCounter += 1
                print("oversees \(player.shortName)")
            }
        }
        
        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(actionDone))
        doneButton.tintColor = UIColor.white
        navigationItem.rightBarButtonItem = doneButton
        
        totalSelectedPlayersCount = allSelectedPlayers.count
        getAllMatchInfoByTournament()

        getAllPlayersInfo()
        for player in (selectedPlayerWktKeeperList + selectedPlayerBatsmanList + selectedPlayerBowlerList + selectedPlayerAllrounderList) {
            populateHomeTeamDependancy(player: player)
        }
        
        if tournamentStatus == "INPROGRESS" {
            postTransferTeamList = (selectedPlayerWktKeeperList + selectedPlayerBatsmanList + selectedPlayerBowlerList + selectedPlayerAllrounderList).map { $0.id! }
            if isLastCutoffTeamExists{
                let difference = postTransferTeamList.filter{ !lastSavedTeamIDs!.contains($0) }
                transferRemaining! = transferLeftAfterLastCutoff! - difference.count
                labelTransferRemaining.text = "\(transferRemaining!)/\(PlayerSelectionRule.transferAllowedTotal)"
            }else{
                labelTransferRemaining.text = "∞"
            }
        }else{
            labelTransferRemaining.text = "∞"
        }
        setupUI()
        collectionViewUI()
        lblCountBatsman.text = "\(PlayerSelectionRule.BatsmanMIN)-\(PlayerSelectionRule.BatsmanMAX)"
        lblCountKeeper.text = "\(PlayerSelectionRule.WktkeeperMIN)-\(PlayerSelectionRule.WktkeeperMAX)"
        lblCountAllrounder.text = "\(PlayerSelectionRule.AllrounderMIN)-\(PlayerSelectionRule.AllrounderMAX)"
        lblCountBowler.text = "\(PlayerSelectionRule.BowlerMIN)-\(PlayerSelectionRule.BowlerMAX)"
    }
    
    func setupUI(){
        
        TeamPreviewButtonContainer.setCurvedCornerBordered()
    }
    
    func collectionViewUI(){
        let playerSelectedCellWidth = (UIScreen.main.bounds.width - (CGFloat(noOfCols) * 2 * minimumSpacing))/CGFloat(noOfCols)
        
        let playerSelectedCellHeight:CGFloat = {
            if GlobalVars.shared.isIpad() {
                return 35.0
            }else{
                return 25.0
            }
        }()
        playerSelectedItemDimension = CGSize(width: playerSelectedCellWidth, height: playerSelectedCellHeight)
        let layoutForSelectedPlayerList:UICollectionViewFlowLayout = {
            let layout = UICollectionViewFlowLayout()
            layout.itemSize = playerSelectedItemDimension
            layout.minimumLineSpacing = minimumSpacing
            layout.minimumInteritemSpacing = minimumSpacing
            return layout
        }()
        playerNamesTagCollectionView.collectionViewLayout = layoutForSelectedPlayerList
        let layoutForRoleSelect:UICollectionViewFlowLayout = {
            let layout = UICollectionViewFlowLayout()
            layout.itemSize = CGSize(width: (collectionViewRoleSelect.frame.width)/5, height: collectionViewRoleSelect.frame.height)
            layout.minimumLineSpacing = 0
            layout.minimumInteritemSpacing = 0
            return layout
        }()
        collectionViewRoleSelect.collectionViewLayout = layoutForRoleSelect
        
        collectionViewRoleSelect.backgroundColor = UIColor.white
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? VCTeamFilter {
            destination.tournamentId = selectedTournamentId
            destination.selectedItemsIds = selectedTeams
            destination.delegate = self
        }
    }
    
    func getTeams(selectedItems: [Int]) {
        
        self.selectedTeams = selectedItems
        if selectedTeams.count != 0 {
            
            btnFilterByTeam.setImage(iconFilterGreen, for: .normal)
            filteredByTeamData = tournamentPlayerList.filter{ selectedTeams.contains($0.participationTeamId!) }
        }else{
            btnFilterByTeam.setImage(iconFilter, for: .normal)
            filteredByTeamData = tournamentPlayerList
        }
        
        if collectionViewRoleSelect?.indexPathsForSelectedItems!.count != 0 {
            selectRole(index: (collectionViewRoleSelect?.indexPathsForSelectedItems?[0].row)!)
        }else{
            selectRole(index: 0)
        }
    }
    
    @objc func actionDone(){
        
        var error:Bool = false
        var message:String?
        print("here")
        if selectedPlayerBatsmanList.count<PlayerSelectionRule.BatsmanMIN {
            message = "Please Select atleast \(PlayerSelectionRule.BatsmanMIN) batsman"
            error = true
        }else if selectedPlayerAllrounderList.count<PlayerSelectionRule.AllrounderMIN {
            message = "Please Select atleast \(PlayerSelectionRule.AllrounderMIN) allrounder"
            error = true
        }else if selectedPlayerBowlerList.count<PlayerSelectionRule.BowlerMIN {
            message = "Please Select atleast \(PlayerSelectionRule.BowlerMIN) bowler"
            error = true
        }else if selectedPlayerWktKeeperList.count<PlayerSelectionRule.WktkeeperMIN {
            message = "Please Select atleast \(PlayerSelectionRule.WktkeeperMIN) wicket keeper"
            error = true
        }
        if error {
            let alert = UIAlertController(title: "Team Formation Problem", message: message, preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                return
            }))
            self.present(alert, animated: true, completion: nil)
        }else{
            delegatePlayersSelected?.getList(batsman: selectedPlayerBatsmanList, allrounder: selectedPlayerAllrounderList, bowler: selectedPlayerBowlerList, WktKeeper: selectedPlayerWktKeeperList, budgetRemaining: budgetRemaining!, transferRemaining: transferRemaining!, captainID:captainID, viceCaptainID:viceCaptainID)
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    
    
    func selectRole(index:Int) {
        switch index{
        case 1:
            currentSelectedRole = .batsman
            tournamentPlayerListFilteredByRole = filteredByTeamData.filter{
                $0.speciality == .batsman
            }
        case 2:
            currentSelectedRole = .wicketKeeper
            tournamentPlayerListFilteredByRole = filteredByTeamData.filter{
                $0.speciality == .wicketKeeper
            }
        case 3:
            currentSelectedRole = .allrounder
            tournamentPlayerListFilteredByRole = filteredByTeamData.filter{
                $0.speciality == .allrounder
            }
        case 4:
            currentSelectedRole = .bowler
            tournamentPlayerListFilteredByRole = filteredByTeamData.filter{
                $0.speciality == .bowler
            }
        default:
            currentSelectedRole = nil
            tournamentPlayerListFilteredByRole = filteredByTeamData
        }
        
        if isSearchActive {
            searchedData = searchedActiveWord.isEmpty ? tournamentPlayerListFilteredByRole : tournamentPlayerListFilteredByRole.filter({(item: PlayerInfo) -> Bool in
                return item.name!.range(of: searchedActiveWord, options: .caseInsensitive, range: nil, locale: nil) != nil
            })
        }
        
        self.tablePlayerDetails.reloadData()
    }
    
    @IBAction func openFilter(_ sender: UIButton) {
        
        performSegue(withIdentifier: "teamFilterStoryboardSegue", sender: self)
    }
    
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
//        if collectionView == playerNamesTagCollectionView {
//            let totalHorizontalCellPadding:CGFloat = 15
//            let totalVerticalCellPadding:CGFloat = 4+4
//            var cellSize = allSelectedPlayers[indexPath.row].name!.size(withAttributes: nil)
//            cellSize.width = cellSize.width + 15 + totalHorizontalCellPadding
//            cellSize.height = cellSize.height + totalVerticalCellPadding
//            return cellSize
//        }else{
//            return CGSize(width: UIScreen.main.bounds.size.width/5, height: 40)
//        }
//    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        searchedActiveWord = searchText
        searchedData = self.tournamentPlayerListFilteredByRole.filter({
            $0.name!.localizedCaseInsensitiveContains(searchText)
        })
        
        if(searchedData.count == 0 && searchText == "") {
            isSearchActive = false
        }
        else {
            isSearchActive = true
            print("Search Array = \(searchedData)")
        }
        self.tablePlayerDetails.reloadData()
    }
    
    func updateSearchResults(for searchController: UISearchController) {
        if let searchText = searchController.searchBar.text {
            searchedData = searchText.isEmpty ? tournamentPlayerListFilteredByRole : tournamentPlayerListFilteredByRole.filter({(item: PlayerInfo) -> Bool in
                return item.name!.range(of: searchText, options: .caseInsensitive, range: nil, locale: nil) != nil
            })
            tablePlayerDetails.reloadData()
        }
    }
    
    @objc func removeTaggedPlayer(remove sender: UIButton){
        
        allSelectedPlayers[sender.tag].isPlayerSelected = false
        
        var playerSelected = allSelectedPlayers[sender.tag]
        
        budgetRemaining! += playerSelected.value!
        labelBudgetRemaining.text = String(budgetRemaining!) + "k"
        
        if playerSelected.type != "local" {
            overSeesPlayerCounter = overSeesPlayerCounter - 1
        }
        
        if tournamentStatus == "INPROGRESS" {
            let i = postTransferTeamList.firstIndex(of: playerSelected.id!)!
            print(i)
            postTransferTeamList.remove(at: i)
            
            if isLastCutoffTeamExists{
                let difference = postTransferTeamList.filter{ !lastSavedTeamIDs!.contains($0) }
                transferRemaining! = transferLeftAfterLastCutoff! - difference.count
                labelTransferRemaining.text = "\(transferRemaining!)/\(PlayerSelectionRule.transferAllowedTotal)"
            }else{
                labelTransferRemaining.text = "∞"
            }
        }
        //labelSelectedPlayersCount.text = "(\(totalSelectedPlayersCount))"
        
        playersByCountry[playerSelected.participationTeamId!]!! -= 1
        switch playerSelected.speciality {
        case .batsman :
            let selectedPeopleIDs = selectedPlayerBatsmanList.map { $0.id }
            if selectedPeopleIDs.contains(playerSelected.id){
                selectedPlayerBatsmanList = selectedPlayerBatsmanList.filter { $0.id != playerSelected.id }
                selectedBatsmanCount-=1
            }
        case .allrounder :
            let selectedPeopleIDs = selectedPlayerAllrounderList.map { $0.id }
            if selectedPeopleIDs.contains(playerSelected.id){
                selectedPlayerAllrounderList = selectedPlayerAllrounderList.filter { $0.id != playerSelected.id }
                selectedAllrounderCount-=1
            }
        case .bowler :
            let selectedPeopleIDs = selectedPlayerBowlerList.map { $0.id }
            if selectedPeopleIDs.contains(playerSelected.id){
                selectedPlayerBowlerList = selectedPlayerBowlerList.filter { $0.id != playerSelected.id }
                selectedBowlerCount-=1
            }
        default :
            let selectedPeopleIDs = selectedPlayerWktKeeperList.map { $0.id }
            if selectedPeopleIDs.contains(playerSelected.id){
                selectedPlayerWktKeeperList = selectedPlayerWktKeeperList.filter { $0.id != playerSelected.id }
                selectedWktkeeperCount-=1
            }
        }
        
        if playerSelected.id == captainID {
            captainID = nil
            playerSelected.isCaptain = false
        }else if playerSelected.id == viceCaptainID {
            viceCaptainID = nil
            playerSelected.isCaptain = false
        }
        
        allSelectedPlayers = selectedPlayerBatsmanList + selectedPlayerAllrounderList + selectedPlayerBowlerList + selectedPlayerWktKeeperList
        
        //function to position the adding of deselected player on list
        totalSelectedPlayersCount -= 1
        
//        if (currentSelectedRole == playerSelected.speciality || currentSelectedRole == nil) &&
//            !filteredByTeamData.contains(playerSelected) &&
//            self.selectedTeams.contains(playerSelected.participationTeamId!) {
//            tournamentPlayerListFilteredByRole.append(playerSelected)
//            searchedData.append(playerSelected)
//            filteredByTeamData.append(playerSelected)
//        }
        
        if (currentSelectedRole == playerSelected.speciality || currentSelectedRole == nil)
            && !filteredByTeamData.contains(playerSelected) {
            
            tournamentPlayerListFilteredByRole.append(playerSelected)
            searchedData.append(playerSelected)
            filteredByTeamData.append(playerSelected)
        }
        
        tournamentPlayerList.append(playerSelected)
        
        playerNamesTagCollectionView.reloadData()
        tablePlayerDetails.reloadData()
        
        updateSelectedPlayerslistHeight()
        
    }
    
    func selectPlayer(_ index : Int){           //player addition to selection
    
        var playerSelected = (isSearchActive) ? searchedData[index] : tournamentPlayerListFilteredByRole[index]
        if validateTeamFormation(player: playerSelected) {
            
            budgetRemaining! -= playerSelected.value!
            labelBudgetRemaining.text = String(budgetRemaining!) + "k"
            
            //sender.setImage(UIImage(named: "red-delete"), for: .normal)
            //tournamentPlayerList[sender.tag].isPlayerSelected = true
            
            postTransferTeamList.append(playerSelected.id!)
            
            //labelSelectedPlayersCount.text = "(\(totalSelectedPlayersCount))"
            
            if playerSelected.type != "local" {
                overSeesPlayerCounter += 1
                print("added \(overSeesPlayerCounter)")
            }
            
            if tournamentStatus == "INPROGRESS" && isLastCutoffTeamExists{
                let difference = postTransferTeamList.filter{ !lastSavedTeamIDs!.contains($0) }
                transferRemaining = transferLeftAfterLastCutoff! - difference.count
                labelTransferRemaining.text = "\(transferRemaining!)/\(PlayerSelectionRule.transferAllowedTotal)"
            }else{
                labelTransferRemaining.text = "∞"
            }
            
            
            playerSelected.isPlayerSelected = true
            tournamentPlayerList = tournamentPlayerList.filter {$0 != playerSelected}
            tournamentPlayerListFilteredByRole = tournamentPlayerListFilteredByRole.filter {$0 != playerSelected}
            searchedData = searchedData.filter {$0 != playerSelected}
            
            populateHomeTeamDependancy(player:playerSelected)
            
            switch playerSelected.speciality {
            case .batsman:
                selectedPlayerBatsmanList.append(playerSelected)
                selectedBatsmanCount += 1
            case .allrounder :
                selectedPlayerAllrounderList.append(playerSelected)
                selectedAllrounderCount += 1
            case .bowler :
                selectedPlayerBowlerList.append(playerSelected)
                selectedBowlerCount += 1
            case .wicketKeeper :
                selectedPlayerWktKeeperList.append(playerSelected)
                selectedWktkeeperCount += 1
            }
            totalSelectedPlayersCount += 1
            allSelectedPlayers = selectedPlayerBatsmanList + selectedPlayerAllrounderList + selectedPlayerBowlerList + selectedPlayerWktKeeperList
            if filteredByTeamData.contains(playerSelected){
                filteredByTeamData = filteredByTeamData.filter {$0 != playerSelected}
            }
            playerNamesTagCollectionView.reloadData()
            tablePlayerDetails.reloadData()
            updateSelectedPlayerslistHeight()
        }
    }
    
    @IBAction func collapseSelectedPlayersView(_ sender: UIButton) {
        isSelectedPlayersTagViewCollapsed = !isSelectedPlayersTagViewCollapsed
    }
    
    
    func populateHomeTeamDependancy(player:PlayerInfo){
        let teamIDHash = playersByCountry[player.participationTeamId!]
        if teamIDHash != nil {
            playersByCountry[player.participationTeamId!] = teamIDHash!! + 1
        }else{
            playersByCountry[player.participationTeamId!] = 1
        }
    }
    
    func validateTeamFormation(player : PlayerInfo)-> Bool{
        //max check
        
        var message:String?
        
        print("maxoverseas player \(PlayerSelectionRule.MaxOverseesPlayerCount) and current \(overSeesPlayerCounter)")
        
        if totalSelectedPlayersCount >= 11 {
            message = "11 players already selected"
        }else{
            if player.type! != "local" && overSeesPlayerCounter >= PlayerSelectionRule.MaxOverseesPlayerCount {
                message = "Can't add more overseas player"
            }else{
                if (budgetRemaining! - player.value!)<0 {
                    message = "insufficient budget remaining"
                    
                }else{
                    let countryCount:Int?? = playersByCountry[player.participationTeamId!]
                    if countryCount != nil && countryCount!! >= 6 {
                        message = "maximum 6 players can be selected from a team"
                    }else{
                        switch player.speciality {
                        case .batsman :
                            if selectedBatsmanCount<PlayerSelectionRule.BatsmanMAX {
                                return true
                            }else{
                                message = "maximum batsman allowed already selected, cant add more"
                            }
                        case .allrounder :
                            if selectedAllrounderCount<PlayerSelectionRule.AllrounderMAX {
                                return true
                            }else{
                                message = "maximum allrounder allowed already selected, cant add more"
                            }
                        case .bowler :
                            if selectedBowlerCount<PlayerSelectionRule.BowlerMAX {
                                return true
                            }else{
                                message = "maximum bowler allowed already selected, cant add more"
                            }
                        default :
                            if selectedWktkeeperCount < PlayerSelectionRule.WktkeeperMAX {
                                return true
                            }else{
                                message = "maximum wicketkeeper allowed already selected, cant add more"
                            }
                            
                        }
                    }
                }
            }
        }
        
        let alert = UIAlertController(title: "Team Formation Problem", message: message, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
        }))
        self.present(alert, animated: true, completion: nil)
        
        return false
    }
        
    @IBAction func btnPriceSortingOrder(_ sender: UIButton) {
        
        isPriceOrderIncreasing = !isPriceOrderIncreasing
    }
    @IBAction func btnPointSortingOrder(_ sender: UIButton) {
        isPointsOrderIncreasing = !isPointsOrderIncreasing
    }
    
    func updateSelectedPlayerslistHeight(){
        heightOfTagList = playerNamesTagCollectionView.collectionViewLayout.collectionViewContentSize.height
        playerNamesTagHeightConstraint.constant = heightOfTagList! + 2 * minimumSpacing
        self.playerNamesTagCollectionView.layoutIfNeeded()
        print(playersByCountry)
    }
    
    func joiningSeletedToFirst(){
        let selectedPeopleIDs = allSelectedPlayers.map { $0.id }
        let filteredPeople = tournamentPlayerList.filter { !selectedPeopleIDs.contains($0.id) }
        tournamentPlayerList = filteredPeople
        print("selected list after fetch")
        if selectedTeams.count != 0 {
            filteredByTeamData = tournamentPlayerList.filter{ selectedTeams.contains($0.participationTeamId!) }
            tournamentPlayerListFilteredByRole = filteredByTeamData
        }else{
            filteredByTeamData = tournamentPlayerList
            tournamentPlayerListFilteredByRole = filteredByTeamData
        }
        
        self.tablePlayerDetails.reloadData()
        updateSelectedPlayerslistHeight()
    }
    
    func getAllMatchInfoByTournament(){
        
        self.matchList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        print(["TournamentId" : selectedTournamentId] as [String : Any])
        Alamofire.request(URL_Future_Matches,
                          method: .post,
                          parameters: ["TournamentId" : selectedTournamentId] as [String : Any],
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
                                self.collectionViewMatchlist.reloadData()
                                if self.matchList.count == 0 {
                                    let backgroundLabel:UILabel = UILabel(frame: CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: self.collectionViewMatchlist.frame.width, height: self.collectionViewMatchlist.frame.height)))
                                    backgroundLabel.text = "This tournament has finished"
                                    backgroundLabel.textColor = UIColor.colorAppPrimary()()
                                    backgroundLabel.backgroundColor = UIColor.clear
                                    backgroundLabel.textAlignment = .center
                                    backgroundLabel.font = UIFont.systemFont(ofSize: 12)
                                    self.collectionViewMatchlist.backgroundView?.addSubview(backgroundLabel)
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
    
    func getAllPlayersInfo(){
        
        print("fetching players")
        self.tournamentPlayerList.removeAll()
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_TOURNAMENT_ALLPLAYERS,
                          method: .post,
                          parameters: ["TournamentId" : selectedTournamentId!] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString() { response in
                debugPrint("All Tournament Players: \(response.result.value ?? "")")
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
                    
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            print(dataArray)
                            for item in dataArray{
                                if let element = item as? [String:Any] {
                                    
                                    let PlayerId = element["PlayerId"] as! Int
                                    let PlayerName = element["PlayerName"] as! String
                                    let PlayerShortName = element["PlayerShortName"] as! String
                                    let PlayerSpeciality = element["PlayerSpeciality"] as! String
                                    let PlayerValue = element["PlayerValue"] as! Int
                                    let TotalPoints = element["TotalPoints"] as! Int
                                    let ParticipationTeamName = element["ParticipationTeamName"] as! String
                                    let ParticipationTeamId = element["ParticipationTeamId"] as! Int
                                    let PlayerType = element["PlayerType"] as! String
                                    let TeamImage = element["TeamImage"] as? String
                                    let isPlaying = element["PlayingInd"] as? Bool
                                    //let PlayerImageName = element["PlayerImage"] as? String
                                    let teamShortName = element["TeamShortName"] as! String
                                    let WinnerPrediction = element["WinnerPrediction"] as? String
                                    self.tournamentPlayerList.append(PlayerInfo(name: PlayerName, id: PlayerId, type: PlayerType, speciality: PlayerSpeciality, value: PlayerValue, participationTeamName: ParticipationTeamName, participationTeamId: ParticipationTeamId, totalPoints: TotalPoints, isPlayerSelected: false, shortName: PlayerShortName, playerImageName:TeamImage, teamShortName: teamShortName, isPlaying: isPlaying, WinnerPrediction: WinnerPrediction))
                                    
                                }
                            }
                            DispatchQueue.main.async {
                                self.joiningSeletedToFirst()
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
        
        if imagename != nil {
            
            if let cachedImage = imageCache!.image(withIdentifier: imagename!) {
                playerImageView.image = cachedImage
            }else{
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!+".png").responseImage { response in
                    
                    if let image = response.result.value {
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerImageView.image = image
                    }else{
                        playerImageView.image = playerImagePlaceHolder
                    }
                }
            }
            
        }else{
            playerImageView.image = playerImagePlaceHolder
        }
    }
}

class ImageCatchingSingleTone{
    
    static let shared = ImageCatchingSingleTone()
    var imageCache:ImageCache?
    
    private init() {
    }
    
    func getCacheInstance()-> ImageCache?{
        
        if imageCache == nil {
            imageCache = AutoPurgingImageCache()
        }
        return imageCache
    }
}

extension VCPlayerSelection:UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == collectionViewRoleSelect {
            return playerRoleCategoryCell.count
        }
        else if collectionView == collectionViewMatchlist {
            return matchList.count
        }
        else{
            print("number of cells \(allSelectedPlayers.count)" )
            return allSelectedPlayers.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == collectionViewRoleSelect{
            let cell = collectionViewRoleSelect.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as! PlayerRolesCategoryCollectionViewCell
            switch playerRoleCategoryCell[indexPath.row] {
            case .some(.batsman):
                cell.assignedRole = .batsman
                cell.playerCount = selectedBatsmanCount
            case .some(.allrounder):
                cell.assignedRole = .allrounder
                cell.playerCount = selectedAllrounderCount
            case .some(.bowler):
                cell.assignedRole = .bowler
                cell.playerCount = selectedBowlerCount
            case .some(.wicketKeeper):
                cell.assignedRole = .wicketKeeper
                cell.playerCount = selectedWktkeeperCount
            default:
                cell.assignedRole = nil
                cell.playerCount = totalSelectedPlayersCount
            }
            collectionViewRoleSelect.selectItem(at: IndexPath(row: 0, section: 0), animated: true, scrollPosition: .left)
            return cell
        } else if collectionView == collectionViewMatchlist {
            let cell = collectionViewMatchlist.dequeueReusableCell(withReuseIdentifier: "CollectionViewCellMatch", for: indexPath) as! CollectionViewCellMatch
            cell.dataSource = matchList[indexPath.row]
            return cell
        }
        else{
            let cell = playerNamesTagCollectionView.dequeueReusableCell(withReuseIdentifier: "playerNameTag", for: indexPath) as! SelectedPlayerCollectionViewCell
            cell.collectionView = playerNamesTagCollectionView
            if allSelectedPlayers.count != 0 {
                cell.dataSourcePlayer = allSelectedPlayers[indexPath.row]
                cell.playerRemoveButton.tag = indexPath.row
                cell.removeSelectedPlayer = {sender in
                    self.removeTaggedPlayer(remove: sender)
                }
            }
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        if collectionView == collectionViewRoleSelect {
            return UIEdgeInsets(top: 0, left: 2, bottom: 0, right: 2)
        }else{
            return UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if collectionView == collectionViewRoleSelect {
            print("-----------\(collectionViewRoleSelect.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath).frame.size)")
            print("-----------\((collectionViewRoleSelect.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as! PlayerRolesCategoryCollectionViewCell).dataContainerView.bounds.size)")
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == collectionViewRoleSelect {
            return CGSize(width: (collectionViewRoleSelect.frame.width - 32.0)/5, height: collectionViewRoleSelect.frame.height)
        }else if collectionView == collectionViewMatchlist {
            if GlobalVars.shared.isIpad(){
                return CGSize(width: 180.0, height: collectionViewMatchlist.frame.size.height)
            }else{
                return CGSize(width: 160.0, height: collectionViewMatchlist.frame.size.height)
            }
            
        }else{
            return playerSelectedItemDimension
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        if collectionView == collectionViewMatchlist {
            return 4.0
        }else if collectionView == collectionViewRoleSelect {
            return 0.0
        }
        else{
            return minimumSpacing
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        if collectionView == collectionViewMatchlist {
            return 4.0
        }else if collectionView == collectionViewRoleSelect {
            return 0.0
        }else{
            return minimumSpacing
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("clicked \(indexPath.row)")
        if collectionView == collectionViewRoleSelect {
            selectRole(index: indexPath.row)
        }
    }
}


extension VCPlayerSelection:UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isSearchActive {
            return searchedData.count
        }else{
            return tournamentPlayerListFilteredByRole.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tablePlayerDetails.dequeueReusableCell(withIdentifier: "playerDataCell", for: indexPath) as! PlayerSelectionListTableViewCell
        let cellItem = isSearchActive ? searchedData[indexPath.row] : tournamentPlayerListFilteredByRole[indexPath.row]
        
        if cellItem.type != "local" {
            cell.overseasPlayerIndicatorImageView.isHidden = false
        }else{
            cell.overseasPlayerIndicatorImageView.isHidden = true
        }
        
        cell.labelPlayerName.text = cellItem.shortName
        cell.labelPlayerPrice.text = String(describing: cellItem.value!) + "K"
        cell.labelPlayerPoints.text = String(describing: cellItem.totalPoints!)
        //cell.labelPlayerStats.text = String(describing: cellItem.totalPoints!)
        cell.playerCountryName.text = cellItem.teamShortName
        cell.PlayerRoleImg.image = cellItem.speciality.image
//        cell.PlayerImage.image = playerImagePlaceHolder
        if let isPlayingIndicator = cellItem.isPlayingIndicator {
            cell.PlayerImage.isHidden = false
            cell.PlayerImage.image = isPlayingIndicator
        } else {
            cell.PlayerImage.isHidden = true
        }
        cell.tapMenuAction = { [unowned self] in
            
            let actionSheetController = UIAlertController()
            let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { action -> Void in
                // Just dismiss the action sheet
            }
            actionSheetController.addAction(cancelAction)
            let viewProfileAction = UIAlertAction(title: "View Player Profile", style: .default) { action -> Void in
                guard let playerProfileVC = UIStoryboard(name: StoryboardNames.PlayerProfile.rawValue,
                                                         bundle: Bundle.main)
                        .instantiateInitialViewController() as? VCPlayerProfile
                else {
                    return
                }
                playerProfileVC.ParticipationTeamId = isSearchActive ? searchedData[indexPath.row].participationTeamId : tournamentPlayerListFilteredByRole[indexPath.row].participationTeamId
                playerProfileVC.PlayerId = isSearchActive ? searchedData[indexPath.row].id : tournamentPlayerListFilteredByRole[indexPath.row].id
                playerProfileVC.modalTransitionStyle = .crossDissolve
                playerProfileVC.modalPresentationStyle = .overCurrentContext
                self.present(playerProfileVC, animated: true, completion: nil)
            }
            actionSheetController.addAction(viewProfileAction)
            actionSheetController.popoverPresentationController?.sourceView = cell.btnCellMenu
            self.present(actionSheetController, animated: true, completion: nil)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectPlayer(indexPath.row)
    }
}
