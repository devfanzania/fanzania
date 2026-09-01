//
//  TVCNotification.swift
//  Fanzania
//
//  Created by Writayan Das on 21/04/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

class TVCNotification: UITableViewCell {
    // MARK: - IBOutlets
    @IBOutlet weak var lblHeading: UILabel!
    @IBOutlet weak var lblDescription: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    
    var deleteAction: (()->Void)?
    
    // MARK: - IBAction
    @IBAction func tappedDeleteNotification(_ sender: UIButton) {
        deleteAction?()
    }
}
