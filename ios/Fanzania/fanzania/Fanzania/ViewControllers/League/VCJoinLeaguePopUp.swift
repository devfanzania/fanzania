//
//  VCJoinLeaguePopUp.swift
//  Fanzania
//
//  Created by Tathagata Dey on 08/01/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCJoinLeaguePopUp: UIViewController,UITextFieldDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var textLeagueCode: UITextField!
    @IBOutlet weak var popupView: UIView!
    @IBOutlet weak var buttonJoin: UIButton!
    @IBOutlet weak var buttonCancel: UIButton!
    @IBOutlet weak var labelError: UILabel!
    
    var tournamentId:Int?
    var delegate : VCMyLeague?
    var tap: UITapGestureRecognizer?

    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        popupView.setCurvedCornerBordered()
        textLeagueCode.setCurvedCornerBordered()
        //buttonJoin.createGradientLayer()
        //buttonCancel.createGradientLayer()
        buttonJoin.layer.cornerRadius = 15.0
        buttonCancel.layer.cornerRadius = 15.0
        textLeagueCode.roundedCorner()
        textLeagueCode.setLeftPaddingPoints(10.0)
        textLeagueCode.delegate = self
        tap?.delegate = self
        tap?.cancelsTouchesInView = false
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        guard let location = touch?.location(in: self.view) else { return }
        if !popupView.frame.contains(location) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    @IBAction func actionJoin(_ sender: Any) {
        if let text = textLeagueCode.text, text.isEmpty{
            textLeagueCode.layer.borderColor = UIColor.red.cgColor
            textLeagueCode.shakeAnimation()
            
        }else{
            saveLeague(leagueCode: textLeagueCode.text!)
            //self.delegateTeamNameSend!.getTeam(team: textTeamName.text!, teamId : 38)
            //delegate!.performSegue(withIdentifier: "segueCreateTeam", sender: delegate)
            //dismiss(animated: true, completion: nil)
        }
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        labelError.text = ""
    }
    
    @IBAction func actionCancel(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func saveLeague(leagueCode:String){
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param:[String : Any] = ["LeaguePin" : leagueCode,
                                    "UserId" : UserDefaults.standard.integer(forKey: "UserId"),
                                    "TournamentId" : tournamentId! as Any]
        Alamofire.request(URL_Leagues_JoinLeague,
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
