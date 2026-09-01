//
//  InputValidations.swift
//  Fanzania
//
//  Created by ICA-IT-IOS-01 on 29/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation

public extension String {
    
    func validateEmail() -> Bool {
        let emailFormat = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format:"SELF MATCHES %@", emailFormat)
        return emailPredicate.evaluate(with: self)
    }
    func validatePhone() -> Bool {
        //let PHONE_REGEX = "^((\\+)|(00))[0-9]{6,14}$"
        let PHONE_REGEX = "()+^$|^[1-9]{1}[0-9]{9}$"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", PHONE_REGEX)
        let result =  phoneTest.evaluate(with: self)
        return result
    }
    func validationPassword() -> Bool {
        return self.count >= 8
    }
}
