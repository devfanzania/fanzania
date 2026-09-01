//
//  VCPowerplay.swift
//  Fanzania
//
//  Created by Writayan Das on 04/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCPowerplay: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var lblNitroPts: UILabel!
    @IBOutlet weak var lblPainkillerPts: UILabel!
    @IBOutlet weak var lblUltraCaptPts: UILabel!
    @IBOutlet weak var btnNitro: PowerplaySelectButton!
    @IBOutlet weak var btnPainKiller: PowerplaySelectButton!
    @IBOutlet weak var btnUltraCapt: PowerplaySelectButton!
    @IBOutlet weak var btnSave: SaveButton!
    @IBOutlet weak var viewContent: UIView!
    
    var UserTeamId: Int?
    var UserId: Int?
    var TournamentId: Int?
    
    var data: PowerplayLifelineModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "Powerplay Lifeline"
        setTitle("Powerplay Lifeline")
        btnSave.isHidden = true
        btnSave.isEnabled = false
        viewContent.isHidden = true
        loadValuesFromAPI()
    }
    
    // MARK: - IBActions
    @IBAction func tappedNitro(_ sender: PowerplaySelectButton) {
        if sender.buttonState == .select {
            sender.buttonState = .selected
        } else if sender.buttonState == .selected {
            sender.buttonState = .select
        }
        sender.isSelected = !sender.isSelected
        verifySave()
    }
    
    @IBAction func tappedPainkiller(_ sender: PowerplaySelectButton) {
        if sender.buttonState == .select {
            sender.buttonState = .selected
        } else if sender.buttonState == .selected {
            sender.buttonState = .select
        }
        verifySave()
    }
    
    @IBAction func tappedUltraCapt(_ sender: PowerplaySelectButton) {
        if sender.buttonState == .select {
            sender.buttonState = .selected
        } else if sender.buttonState == .selected {
            sender.buttonState = .select
        }
        verifySave()
    }
    
    @IBAction func tappedSave(_ sender: UIButton) {
        save()
    }
    
    func verifySave() {
        btnSave.isEnabled = btnNitro.buttonState == .selected
            || btnPainKiller.buttonState == .selected
            || btnUltraCapt.buttonState == .selected
    }
    
    func setData(data: PowerplayLifelineModel) {
        
        let attrsNormal: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 11.0),
            NSAttributedString.Key.foregroundColor: UIColor.black
        ]
        
        let attrsBold: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 11.0),
            NSAttributedString.Key.foregroundColor: UIColor.black
        ]
    
        
        if data.nitroEnable {
            let finalString = NSMutableAttributedString(string: "Lifeline available. You ", attributes: attrsNormal)
            finalString.append(NSAttributedString(string: "scored \(data.nitroPoints) bonus points ", attributes: attrsBold))
            finalString.append(NSAttributedString(string: "from your first use.", attributes: attrsNormal))
            lblNitroPts.attributedText = finalString
            btnNitro.buttonState = .select
        } else {
            if data.nitroUsed {
                lblNitroPts.text = "Lifeline option already used."
                btnNitro.buttonState = .used
            } else {
                lblNitroPts.text = "Lifeline not available as you have not used the first attempt yet."
                btnNitro.buttonState = .unavailable
            }
        }
        
        if data.painKillerEnable {
            let finalString = NSMutableAttributedString(string: "Lifeline available. You ", attributes: attrsNormal)
            finalString.append(NSAttributedString(string: "scored \(data.painKillerPoints) bonus points ", attributes: attrsBold))
            finalString.append(NSAttributedString(string: "from your first use.", attributes: attrsNormal))
            lblPainkillerPts.attributedText = finalString
            btnPainKiller.buttonState = .select
        } else {
            if data.painKillerUsed {
                lblPainkillerPts.text = "Lifeline option already used."
                btnPainKiller.buttonState = .used
            } else {
                lblPainkillerPts.text = "Lifeline not available as you have not used the first attempt yet."
                btnPainKiller.buttonState = .unavailable
            }
        }
        if data.autoPilotEnable {
            let finalString = NSMutableAttributedString(string: "Lifeline available. You ", attributes: attrsNormal)
            finalString.append(NSAttributedString(string: "scored \(data.autoPilotPoints) bonus points ", attributes: attrsBold))
            finalString.append(NSAttributedString(string: "from your first use.", attributes: attrsNormal))
            lblUltraCaptPts.attributedText = finalString
            btnUltraCapt.buttonState = .select
        } else {
            if data.autoPilotUsed {
                lblUltraCaptPts.text = "Lifeline option already used."
                btnUltraCapt.buttonState = .used
            } else {
                lblUltraCaptPts.text = "Lifeline not available as you have not used the first attempt yet."
                btnUltraCapt.buttonState = .unavailable
            }
        }
        
        btnSave.isHidden = false
        viewContent.isHidden = false
    }
    
    func loadValuesFromAPI() {
        guard let UserTeamId = UserTeamId,
              let UserId = UserId,
              let TournamentId = TournamentId
        else {
            return
        }
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param = [
            "UserTeamId" : UserTeamId,
            "UserId" : UserId,
            "TournamentId" : TournamentId,
        ]
        
        Alamofire.request(URL_League_User_Team_Powerplay,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: [
                            "x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                            "x-api-devicetype":"ios"
                          ])
            .responseString { (response) in
                debugPrint("\(response.result.value ?? "")")
            }
            .responseJSON { [weak self] response in
                guard let self = self else {
                    return
                }
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
                            guard let item = dataArray.first else {
                                return
                            }
                            guard let response = PowerplayLifelineModel(dictionary: item) else {
                                return
                            }
                            self.data = response
                            DispatchQueue.main.async {
                                self.setData(data: response)
                            }
                        }
                        
                    } else{
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
    
    func save() {
        
        guard let UserTeamId = UserTeamId,
              let UserId = UserId,
              let TournamentId = TournamentId,
              let data = data else {
            return
        }
        let loader = UIViewController.displaySpinner(onView: self.view)
        let param: [String: Any] = [
            "UserTeamId": UserTeamId,
            "UserId": UserId,
            "TournamentId": TournamentId,
            "NitroUserTeamMatchPointId": data.nitroUserTeamMatchPointId,
            "PainKillerUserTeamMatchPointId": data.painKillerUserTeamMatchPointId,
            "AutoPilotUserTeamMatchPointId": data.autoPilotUserTeamMatchPointId,
            "NitroSelect": btnNitro.buttonState == .selected,
            "PainKillerSelect": btnPainKiller.buttonState == .selected,
            "AutoPilotSelect": btnUltraCapt.buttonState == .selected,
            "NitroPoints": data.nitroPoints,
            "PainKillerPoints": data.painKillerPoints,
            "AutoPilotPoints": data.autoPilotPoints
        ]
        
        Alamofire.request(URL_League_Update_User_Team_Powerplay,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: [
                            "x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                            "x-api-devicetype":"ios"
                          ])
            .responseString { (response) in
                debugPrint("\(response.result.value ?? "")")
            }
            .responseJSON { [weak self] response in
                guard let self = self else {
                    return
                }
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
                        let successAlert = UIAlertController(title: "Saved", message: "Powerplay saved.", preferredStyle: .alert)
                        
                        successAlert.addAction(UIAlertAction(title: "Ok",
                                                             style: .default,
                                                             handler: { [unowned self] action in
                                                                self.dismiss(animated: true, completion: { self.navigationController?.popViewController(animated: true) })
                        }))
                        self.present(successAlert, animated: true, completion: nil)
                        
                    } else{
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
