//
//  DatePopUpViewController.swift
//  HelpDesk
//
//  Created by ICA-IT-IOS-01 on 23/02/18.
//  Copyright © 2018 ICA-IT-IOS-01. All rights reserved.
//

import UIKit

protocol DatePickerDelegate {
    func returnDate(date : Date)
}
class VCDatePickerPopUp: UIViewController {

    @IBOutlet weak var TitleLabel: UILabel!
    @IBOutlet weak var DatePicker: UIDatePicker!
    @IBOutlet weak var BtnSelect: UIButton!
    var showTimePicker:Bool = false
    var isPastDateAllowed:Bool = false
    var minimumDate:Date?
    var maxDate:Date?
    
    let formater = DateFormatter()
    var formatedDate : String{
        return formater.string(from: DatePicker.date)
    }
    
    
    var delegate:DatePickerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let todaysDate = Date()
        DatePicker.setDate(todaysDate, animated: true)
        DatePicker.minimumDate = minimumDate
        DatePicker.maximumDate = maxDate
    }
    
    @IBAction func ButtonPressed(_ sender: Any) {
        delegate?.returnDate(date: DatePicker.date)
        dismiss(animated: true)
    }
    @IBAction func datePickerDismiss(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
}
