//
//  VCSelectCountry.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

protocol DelegateReturnCountry:class {
    func getCountry(country:CountryModel)
}

class VCSelectCountry: UIViewController,UITableViewDelegate,UITableViewDataSource {
    
    
    @IBOutlet weak var tableViewCountry: UITableView!
    var countryList = [CountryModel]()
    weak var delegate: DelegateReturnCountry?
    override func viewDidLoad() {
        
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableViewCountry.dataSource = self
        tableViewCountry.delegate = self
        self.tableViewCountry?.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        executePOST()
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return countryList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableViewCountry.dequeueReusableCell(withIdentifier: "cell")
        cell?.textLabel?.text = countryList[indexPath.row].name
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let indexPath = tableView.indexPathForSelectedRow
        //getting the current cell from the index path
        // call this method on whichever class implements our delegate protocol
        delegate?.getCountry(country: countryList[(indexPath?.row)!])
        print(countryList[(indexPath?.row)!].name)
        //self.navigationController?.popViewController(animated: true)
        dismiss(animated: true, completion: nil)
    }
    
    func executePOST(){
        let postParams = [String:String]()
        let requestURL = URL_Country_List
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(requestURL, method: .post, parameters: postParams, encoding: JSONEncoding.default, headers: ["x-api-authtoken":""])
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
                        
                        //print(responseArray.count)
                        for country in responseArray{
                            
                            print(country)
                            let countryInfo = country as! [String: Any]
                            
                            let id = countryInfo["CountryId"] as! Int
                            let name = countryInfo["Country"] as! String
                            let active = countryInfo["Active"] as! Bool
                            self.countryList.append(CountryModel(name:name, id:id, active:active))
                            
                        }
                        
                        DispatchQueue.main.async { // changing
                            self.tableViewCountry.reloadData()
                            //UIViewController.removeSpinner(spinner: loader)
                        }
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Login Unsuccessful", message: jsonDictionary["statusMessage"] as? String, preferredStyle: UIAlertController.Style.alert)
                        
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
