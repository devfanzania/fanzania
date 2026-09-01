//
//  VCVarifyTeamName.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 23/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

protocol DelegateNewTeamNameVarified {
    func getTeam(team : String, teamId:Int)
}

class VCVarifyTeamName: UIViewController, UITextFieldDelegate, UIGestureRecognizerDelegate {

    var delegate : VCTournaments?
    var delegateTeamNameSend : DelegateNewTeamNameVarified?
    var tournamentId:Int?
    var tap: UITapGestureRecognizer?
    @IBOutlet weak var dialogView: UIView!
    @IBOutlet var textTeamName: UITextField!
    @IBOutlet var labelAbailabilityCheck: UILabel!
    @IBOutlet weak var VarifyButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        print(tournamentId)
        VarifyButton.createGradientLayer()
        textTeamName.roundedCorner()
        textTeamName.setLeftPaddingPoints(10.0)
        dialogView.roundedCornerCollectionViewCell()
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !dialogView.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }

    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.darkGray.cgColor
        labelAbailabilityCheck.text = ""
        labelAbailabilityCheck.textColor = UIColor.yellow
    }
    @IBAction func actionVarifyTeamName(_ sender: UITextView) {
        if let text = textTeamName.text, text.count < 3{
            textTeamName.layer.borderColor = UIColor.red.cgColor
            textTeamName.shakeAnimation()
            labelAbailabilityCheck.text = "Please enter a Team Name with atleast 3 characters"
            labelAbailabilityCheck.textColor = UIColor.red
            
        }else{
            varifyTeam(team: textTeamName.text!)
            //self.delegateTeamNameSend!.getTeam(team: textTeamName.text!, teamId : 38)
            //delegate!.performSegue(withIdentifier: "segueCreateTeam", sender: delegate)
            //dismiss(animated: true, completion: nil)
        }
    }
    
    func varifyTeam(team:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["UserTeamName" : team,
                                    "TournamentId" : tournamentId! as Any]
        Alamofire.request(URL_Team_VarifyName,
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
                        print("json format mismatch")
                        return
                    }
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch")
                        return
                    }
                    if status == "success" {
                        self.saveTeam(team: team)
                    }else{
                        self.labelAbailabilityCheck.text = jsonDictionary["statusMessage"] as? String
                        self.labelAbailabilityCheck.textColor = UIColor.yellow
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    
    func saveTeam(team:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["UserTeamName" : team,
                                    "UserId" : UserDefaults.standard.integer(forKey: "UserId"),
                                    "TournamentId" : tournamentId! as Any]
        Alamofire.request(URL_Team_CreateTeam,
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
                                    
                                    let UserTeamId = element["UserTeamId"] as! Int
                                    self.dismiss(animated: true, completion: nil)
                                    self.delegateTeamNameSend!.getTeam(team: team, teamId: UserTeamId)
                                    self.delegate!.performSegue(withIdentifier: "segueCreateTeam", sender: self.delegate)
                                }
                            }
                            
                        }
                        
                    }else{
                        
                        let alert = UIAlertController(title: "Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        
                        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                        }))
                        self.present(alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
