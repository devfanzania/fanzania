//
//  VCMyPointsHistory.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 21/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit

class VCMyPointsHistory: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet var pointHistoryTableView: UITableView!
    var pointHistoryList:[MyPointHistoryModel] = [MyPointHistoryModel(tournameName: "IPL-2018", teamName: "Knight-Riders", totalPoints: 350), MyPointHistoryModel(tournameName: "IPL-2018", teamName: "Knight-Riders", totalPoints: 350), MyPointHistoryModel(tournameName: "IPL-2018", teamName: "Knight-Riders", totalPoints: 350), MyPointHistoryModel(tournameName: "IPL-2018", teamName: "Knight-Riders", totalPoints: 350)]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        // Do any additional setup after loading the view.
        pointHistoryTableView.delegate = self
        pointHistoryTableView.dataSource = self
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return pointHistoryList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = pointHistoryTableView.dequeueReusableCell(withIdentifier: "cellPointHistoryData", for: indexPath)  as! MyPointsHistoryTableViewCell
        
        if (indexPath.row % 2) != 0{
            cell.tournamentName.backgroundColor = colorListViewLightGrey
            cell.myTeams.backgroundColor = colorListViewLightGrey
            cell.myPoints.backgroundColor = colorListViewLightGrey
        }else{
            cell.tournamentName.backgroundColor = colorListViewLightYellow
            cell.myTeams.backgroundColor = colorListViewLightYellow
            cell.myPoints.backgroundColor = colorListViewLightYellow
        }
        cell.tournamentName.text = pointHistoryList[indexPath.row].tournameName
        cell.myTeams.text = pointHistoryList[indexPath.row].teamName
        cell.myPoints.text = "\(pointHistoryList[indexPath.row].totalPoints!)"
        return cell
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
