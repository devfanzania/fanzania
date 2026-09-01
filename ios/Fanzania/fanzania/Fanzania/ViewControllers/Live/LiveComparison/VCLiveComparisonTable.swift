//
//  VCLiveComparisonTable.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCLiveComparisonTable: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var lblMyThisMatchTotal: UILabel!
    @IBOutlet weak var lblThisMatchTotalComparison: UILabel!
    @IBOutlet weak var lblOtherThisMatchTotal: UILabel!
    @IBOutlet weak var lblTotal: UILabel!
    @IBOutlet weak var lblMyTournamentTotal: UILabel!
    @IBOutlet weak var lblOtherTournamentTotal: UILabel!
    
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    
    var MyTeamId: Int?
    var OtherTeamId: Int?
    var TournamentId: Int?
    var MatchId: Int?
    
    var MyTeamTournamentTotalPts: Int?
    var OtherTeamTournamentTotalPts: Int?
    
    private var comparisons = [TeamComparisonTableItemModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        lblHeader.attributedText = nil
        lblHeader.text = nil
        loadValuesFromAPI()
    }
    
    func loadValuesFromAPI() {
        guard let MyTeamId = MyTeamId,
              let OtherTeamId = OtherTeamId,
              let TournamentId = TournamentId,
              let MatchId = MatchId
        else {
            return
        }
        spinner.startAnimating()
        let param = [
            "MyTeamId" : MyTeamId,
            "OtherTeamId" : OtherTeamId,
            "TournamentId" : TournamentId,
            "MatchId" : MatchId
        ]
        
        Alamofire.request(URL_Live_TeamLiveComparison,
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
                    self.spinner.stopAnimating()
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
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray {
                            self.comparisons.removeAll()
                            for item in dataArray {
                                if let element = item as? [String: Any] {
                                    if let modelItem = TeamComparisonTableItemModel(dictionary: element) {
                                        self.comparisons.append(modelItem)
                                    }
                                }
                            }
                        }
                        
                        self.tableView.reloadData()
                        if self.comparisons.count > 0 {
                            self.setTotalValue()
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
    
    func setTotalValue() {
        self.parent?.setTitle(comparisons[0].otherTeamName)
        self.navigationController?.setTitle(comparisons[0].otherTeamName)
        let myMatchTotal = self.comparisons.reduce(0, { $0 + $1.myTotalPoints })
        let otherMatchTotal = self.comparisons.reduce(0, { $0 + $1.otherTotalPoints })
        let finalScore = myMatchTotal - otherMatchTotal
        lblMyThisMatchTotal.text = "\(myMatchTotal)"
        lblOtherThisMatchTotal.text = "\(otherMatchTotal)"
        
        var compareResult = ""
        if finalScore > 0 {
            lblThisMatchTotalComparison.text = "+\(finalScore)"
            lblThisMatchTotalComparison.textColor = .green
            compareResult = "AHEAD OF "
        } else if finalScore < 0 {
            lblThisMatchTotalComparison.text = "\(finalScore)"
            lblThisMatchTotalComparison.textColor = .red
            compareResult = "BEHIND "
        } else {
            lblThisMatchTotalComparison.text = "\(finalScore)"
            lblThisMatchTotalComparison.textColor = .black
        }
        
        lblMyTournamentTotal.text = "\(MyTeamTournamentTotalPts ?? 0)"
        lblOtherTournamentTotal.text = "\(OtherTeamTournamentTotalPts ?? 0)"
        
        let boldText  = "\(compareResult) \(comparisons[0].otherTeamName) By "
        let boldColorText = "\(abs(finalScore)) "
        
        let attrsNormal: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 14.0),
            NSAttributedString.Key.foregroundColor: UIColor.black
        ]
        
        let attrsBold: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 14.0),
            NSAttributedString.Key.foregroundColor: UIColor.black
        ]
        
        let attrsBoldColor: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 14.0),
            NSAttributedString.Key.foregroundColor: lblThisMatchTotalComparison.textColor
        ]
    
        let finalString = NSMutableAttributedString(string: "You are ", attributes: attrsNormal)
        finalString.append(NSAttributedString(string: boldText, attributes: attrsBold))
        finalString.append(NSAttributedString(string: boldColorText, attributes: attrsBoldColor))
        finalString.append(NSAttributedString(string: "POINTS ", attributes: attrsBold))
        finalString.append(NSAttributedString(string: "in this match", attributes: attrsNormal))
        lblHeader.attributedText = finalString
    }
}

// MARK: - UITableViewDataSource
extension VCLiveComparisonTable: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return comparisons.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LiveComparisonTVC", for: indexPath) as! LiveComparisonTVC
        cell.dataSource = comparisons[indexPath.row]
        return cell
    }
}

// MARK: - UITableViewDelegate
extension VCLiveComparisonTable: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LiveComparisonTVCHeader") as! LiveComparisonTVCHeader
        if comparisons.count > 0 {
            cell.lblMyTeamName.text = comparisons[0].myTeamName
            cell.lblOtherTeamName.text = comparisons[0].otherTeamName
        } else {
            cell.lblMyTeamName.text = nil
            cell.lblOtherTeamName.text = nil
        }
        return cell
    }
}
