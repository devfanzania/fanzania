//
//  TVCTeamSelection.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 25/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class TVCTeamSelection: UITableViewController {
    
    var distinctTeams:[TeamModel] = []
    var selectedItems: [TeamModel] = []
    var selectedItemsIds:[Int] = []
    var tournamentId:Int?
    var delegate:DelegateTeamSelection?
    
    override func viewDidLoad() {
        
        getDistinctTeams(tournamentID: tournamentId!)
        
    }
    
    @IBAction func actionDone(_ sender: UIBarButtonItem) {
        
        selectedItems = distinctTeams.filter { return $0.isSelected! }
        selectedItemsIds = selectedItems.map{ $0.teamID! }
        delegate?.getTeams(selectedItems: selectedItemsIds)
        navigationController?.popViewController(animated: true)
        
    }
    // MARK: - Table view data source
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return distinctTeams.count
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath) as! TeamSelectionTableViewCell
        cell.labelTeamName.text = distinctTeams[indexPath.row].teamName!
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("indicated")
        distinctTeams[indexPath.row].isSelected = true
    }
    
    override func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        print("indicated")
        distinctTeams[indexPath.row].isSelected = false
    }
    
    /*
             // Override to support conditional editing of the table view.
             override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
                     // Return false if you do not want the specified item to be editable.
                     return true
             }
             */
    
    /*
             // Override to support editing the table view.
             override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
                     if editingStyle == .delete {
                             // Delete the row from the data source
                             tableView.deleteRows(at: [indexPath], with: .fade)
                     } else if editingStyle == .insert {
                             // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
                     }
             }
             */
    
    /*
             // Override to support rearranging the table view.
             override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
     
             }
             */
    
    /*
             // Override to support conditional rearranging of the table view.
             override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
                     // Return false if you do not want the item to be re-orderable.
                     return true
             }
             */
    
    /*
             // MARK: - Navigation
     
             // In a storyboard-based application, you will often want to do a little preparation before navigation
             override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
                     // Get the new view controller using segue.destination.
                     // Pass the selected object to the new view controller.
             }
             */
    
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
                            self.tableView.reloadData()
                            
                            for (index, value) in self.distinctTeams.enumerated() {
                                if value.isSelected! {
                                    self.tableView.selectRow(at: IndexPath(row: index, section: 0), animated: true, scrollPosition: .top)
                                    self.tableView.cellForRow(at: IndexPath(row: index, section: 0))?.accessoryType = .checkmark
                                }
                            }
                            
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
