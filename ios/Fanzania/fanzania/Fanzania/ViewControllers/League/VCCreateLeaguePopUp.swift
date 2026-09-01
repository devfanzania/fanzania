//
//  VCCreateLeaguePopUp.swift
//  Fanzania
//
//  Created by Tathagata Dey on 09/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

protocol DelegateNewLeagueCreated {
    func getLeague(league: String, leagueId:Int)
}

class VCCreateLeaguePopUp: UIViewController, UITextFieldDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var viewPopUp: UIView!
    @IBOutlet weak var textLeagueName: UITextField!
    @IBOutlet weak var btnCreateLeague: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var labelError: UILabel!
    
    var tournamentId:Int?
    var delegate : VCMyLeague?
    var delegateLeagueNameSend : DelegateNewLeagueCreated?
    var tap: UITapGestureRecognizer?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        viewPopUp.setCurvedCornerBordered()
        //btnCreateLeague.createGradientLayer()
        btnCreateLeague.layer.cornerRadius = 15.0
        //btnCancel.createGradientLayer()
        btnCancel.layer.cornerRadius = 15.0
        textLeagueName.roundedCorner()
        textLeagueName.setLeftPaddingPoints(10.0)
        textLeagueName.delegate = self
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !viewPopUp.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        labelError.text = ""
    }
    
    @IBAction func actionCreateLeague(_ sender: UIButton) {
        if let text = textLeagueName.text, text.count < 3 && text.count > 10 {
            textLeagueName.layer.borderColor = UIColor.red.cgColor
            labelError.text = "Enter a name with atleast 3 characters"
            textLeagueName.shakeAnimation()
            
        }else{
            varifyTeam(league: textLeagueName.text!)
            //self.delegateTeamNameSend!.getTeam(team: textTeamName.text!, teamId : 38)
            //delegate!.performSegue(withIdentifier: "segueCreateTeam", sender: delegate)
            //dismiss(animated: true, completion: nil)
        }
    }
    
    @IBAction func actionCancel(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func varifyTeam(league:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["LeagueName" : league,
                                    "TournamentId" : tournamentId! as Any]
        Alamofire.request(URL_Leagues_VarifyLeague,
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
                        self.saveLeague(league: league)
                    }else{
                        self.labelError.text = jsonDictionary["statusMessage"] as? String
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    
    func saveLeague(league:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["LeagueName" : league,
                                    "LeagueLeaderId" : UserDefaults.standard.integer(forKey: "UserId"),
                                    "TournamentId" : tournamentId! as Any]
        Alamofire.request(URL_Leagues_CreateLeague,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!])
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
                        
                        let invalid_login_alert = UIAlertController(title: "Joined League", message: nil, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            self.delegate?.refreshLeagues()
                            self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                        
                    }else{
                        self.labelError.text = jsonDictionary["statusMessage"] as? String
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
}
