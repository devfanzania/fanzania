//
//  NoPasteboardTextField.swift
//  Fanzania
//
//  Created by Writayan Das on 01/04/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

class NoPasteboardTextField: UITextField {
    
    open override func canPerformAction(_ action: Selector, withSender sender: Any?) -> Bool {
        if action == #selector(UIResponderStandardEditActions.paste(_:)) {
            return false
        }
        return super.canPerformAction(action, withSender: sender)
    }
}
