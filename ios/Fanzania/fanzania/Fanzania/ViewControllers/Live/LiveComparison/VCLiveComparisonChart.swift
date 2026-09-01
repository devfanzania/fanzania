//
//  VCLiveComparisonChart.swift
//  Fanzania
//
//  Created by Writayan Das on 01/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCLiveComparisonChart: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var lblMyTeamTotalPts: UILabel!
    @IBOutlet weak var lblOtherTeamTotalPts: UILabel!
    @IBOutlet weak var lblMyTeamName: UILabel!
    @IBOutlet weak var lblOtherTeamName: UILabel!
    @IBOutlet weak var lblHeading: UILabel!
    @IBOutlet weak var lblMyTeamLedger: UILabel!
    @IBOutlet weak var lblOtherTeamLedger: UILabel!
    @IBOutlet weak var viewMyTeamColor: UIView!
    @IBOutlet weak var viewOtherTeamColor: UIView!
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    @IBOutlet weak var chart: Chart!
    @IBOutlet weak var chartWidth: NSLayoutConstraint!
    
    
    var MyTeamId: Int?
    var OtherTeamId: Int?
    var TournamentId: Int?
    
    var MyTeamTournamentTotalPts: Int?
    var OtherTeamTournamentTotalPts: Int?
    
    var MyTeamName: String?
    var OtherTeamName: String?
    
    private var comparisons = [TeamComparisonChartModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupLabels()
        loadValuesFromAPI()
    }
    
    func setupLabels() {
        lblMyTeamTotalPts.text = "\(MyTeamTournamentTotalPts ?? 0)"
        lblOtherTeamTotalPts.text = "\(OtherTeamTournamentTotalPts ?? 0)"
        
        lblMyTeamName.text = MyTeamName
        lblOtherTeamName.text = OtherTeamName
        
        lblMyTeamLedger.text = MyTeamName
        lblOtherTeamLedger.text = OtherTeamName
        
        guard let MyTeamTournamentTotalPts =  MyTeamTournamentTotalPts,
              let OtherTeamTournamentTotalPts = OtherTeamTournamentTotalPts
        else {
            return
        }
        let finalScore = MyTeamTournamentTotalPts - OtherTeamTournamentTotalPts
        
        var compareResult = ""
        var color = UIColor()
        
        if finalScore > 0 {
            color = .green
            compareResult = "AHEAD BY"
        } else {
            color = .red
            compareResult = "TRAILING BY"
        }
        
        let attrsNormal: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 14.0),
            NSAttributedString.Key.foregroundColor: UIColor.black
        ]
        
        let attrsColor: [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 14.0),
            NSAttributedString.Key.foregroundColor: color
        ]
        
        let finalString = NSMutableAttributedString(string: "You are \(compareResult) ", attributes: attrsNormal)
        finalString.append(NSAttributedString(string: "\(abs(finalScore)) ", attributes: attrsColor))
        finalString.append(NSAttributedString(string: "POINTS", attributes: attrsNormal))
        lblHeading.attributedText = finalString
    }
    
    func loadValuesFromAPI() {
        guard let MyTeamId = MyTeamId,
              let OtherTeamId = OtherTeamId,
              let TournamentId = TournamentId
        else {
            return
        }
        spinner.startAnimating()
        let param = [
            "MyTeamId" : MyTeamId,
            "OtherTeamId" : OtherTeamId,
            "TournamentId" : TournamentId
        ]
        
        Alamofire.request(URL_Live_TeamComparison,
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
                                    let modelItem = TeamComparisonChartModel(dictionary: element)
                                    self.comparisons.append(modelItem)
                                }
                            }
                            DispatchQueue.main.async {
                                self.setupChart()
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
    
    func setupChart() {
        var myData = comparisons.map {
            return (x: $0.matchNo, y: Double($0.myMatchTotalPoints))
        }
        myData.insert((x: 0, y: 0.0), at: 0)
        let mySeries = ChartSeries(data: myData)
        mySeries.color = ChartColors.redColor()
        viewMyTeamColor.backgroundColor = ChartColors.redColor()
        
        var otherData = comparisons.map {
            return (x: $0.matchNo, y: Double($0.otherMatchTotalPoints))
        }
        otherData.insert((x: 0, y: 0.0), at: 0)
        let otherSeries = ChartSeries(data: otherData)
        otherSeries.color = ChartColors.purpleColor()
        viewOtherTeamColor.backgroundColor = ChartColors.purpleColor()
        
        chart.xLabelsFormatter = { "\($1<1 ? "" : "M")\(Int($1))" }
        chart.yLabelsFormatter = { "\(Int($1))" }
        
        chart.xLabelsSkipLast = false
        
        chart.isUserInteractionEnabled = false
        let minWidth = UIScreen.main.bounds.width - 40.0 - 28.0
        let chartWidthNeeded = CGFloat(myData.count) * 40.0
        chartWidth.constant = chartWidthNeeded <= minWidth ? minWidth : chartWidthNeeded
        chart.add(mySeries)
        chart.add(otherSeries)
        self.chart.layoutIfNeeded()
    }
}
