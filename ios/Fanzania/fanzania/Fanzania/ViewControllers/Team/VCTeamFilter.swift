//
//  VCTeamFilter.swift
//  Fanzania
//
//  Created by Tathagata Dey on 13/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire


struct TeamModel {
    var teamName:String?
    var teamID:Int?
    var teamShortName:String?
    var rank:Int?
    var owner:String?
    var points:Int?
    var isSelected:Bool?
    
    init(teamID:Int, teamName:String, teamShortName:String, isSelected:Bool) {
        self.teamID = teamID
        self.teamName = teamName
        self.teamShortName = teamShortName
        self.isSelected = isSelected
    }
    init(rank:Int?, teamName:String?, owner:String?, points:Int?){
        self.rank = rank
        self.teamName = teamName
        self.owner = owner
        self.points = points
    }
}

protocol DelegateTeamSelection {
    func getTeams(selectedItems:[Int])
}

class VCTeamFilter: UIViewController, UICollectionViewDelegateFlowLayout, UIGestureRecognizerDelegate {

    var distinctTeams:[TeamModel] = []
    var selectedItems: [TeamModel] = []
    var selectedItemsIds:[Int] = []
    var tournamentId:Int?
    var delegate:DelegateTeamSelection?
    var tap: UITapGestureRecognizer?
    
    @IBOutlet weak var popUpDialogView: UIView!
    @IBOutlet weak var collectionViewFilterTeam: UICollectionView!
    @IBOutlet weak var buttonReset: UIButton!
    @IBOutlet weak var buttonApply: UIButton!
    
    @IBAction func actionApply(_ sender: UIButton) {
        selectedItems = distinctTeams.filter { return $0.isSelected! }
        selectedItemsIds = selectedItems.map{ $0.teamID! }
        delegate?.getTeams(selectedItems: selectedItemsIds)
        dismiss(animated: true, completion: nil)
    }
    @IBAction func actionReset(_ sender: UIButton) {
        var i = 0
        while i<distinctTeams.count{
            distinctTeams[i].isSelected = false
            i+=1
        }
        collectionViewFilterTeam.reloadData()
    }
    
    override func viewDidLoad() {
        
        collectionViewFilterTeam.delegate = self
        getDistinctTeams(tournamentID: tournamentId!)
        setUI()
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !popUpDialogView.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func setUI(){
        buttonReset.layer.cornerRadius = 15.0
        buttonReset.backgroundColor = UIColor.colorCrimson()
        buttonApply.layer.cornerRadius = 15.0
        buttonApply.backgroundColor = UIColor.colorCrimson()
        collectionViewFilterTeam.allowsMultipleSelection = true
    }
    
    func getDistinctTeams(tournamentID : Int){
        
        distinctTeams.removeAll()
        let postParams = ["TournamentId":tournamentID]
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_Team_FilterDistinct, method: .post, parameters: postParams, encoding: JSONEncoding.default, headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!, "x-api-devicetype":"ios"])
            .responseString { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                };
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    guard let jsonDictionary = (responseJSON as? [String: Any]) else{
                        print("json format mismatch")
                        return
                    }
                    if let status = (jsonDictionary["status"] as? String), status == "success" {
                        guard let responseArray = jsonDictionary["data"] as? NSArray else {
                            print("No proper json Data format")
                            return
                        }
                        
                        for team in responseArray{
                            
                            print(team)
                            let teamInfo = team as! [String: Any]
                            let id = teamInfo["ParticipationTeamId"] as! Int
                            let name = teamInfo["ParticipationTeamName"] as! String
                            let shortName = teamInfo["TeamShortName"] as! String
                            var bool = false
                            if self.selectedItemsIds.contains(id) {
                                bool = true
                            }
                            self.distinctTeams.append(TeamModel(teamID: id, teamName: name, teamShortName: shortName, isSelected : bool ))
                            
                        }
                        DispatchQueue.main.async { // changing
                            self.collectionViewFilterTeam.reloadData()
                            
//                            for (index, value) in self.distinctTeams.enumerated() {
//                                if value.isSelected! {
//                                    self.collectionViewFilterTeam.selectRow(at: IndexPath(row: index, section: 0), animated: true, scrollPosition: .top)
//                                    self.collectionViewFilterTeam.cellForRow(at: IndexPath(row: index, section: 0))?.accessoryType = .checkmark
//                                }
//                            }
                            
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Login Unsuccessful", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }case .failure(let error):
                        print("Request failed with error: \(error)")
                }
        }
        
    }

}

extension VCTeamFilter : UICollectionViewDelegate, UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return distinctTeams.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "TeamFilterCollectionViewCell", for: indexPath) as! TeamFilterCollectionViewCell
        cell.dataSource = distinctTeams[indexPath.row]
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        distinctTeams[indexPath.row].isSelected = !distinctTeams[indexPath.row].isSelected!
        collectionView.reloadItems(at: [indexPath])
        
    }
    func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
        distinctTeams[indexPath.row].isSelected = false
        collectionView.reloadItems(at: [indexPath])
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (self.collectionViewFilterTeam.frame.width-16)/2, height: 35.0)
    }
    
}
