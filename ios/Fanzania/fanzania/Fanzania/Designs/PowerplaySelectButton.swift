//
//  PowerplaySelectButton.swift
//  Fanzania
//
//  Created by Writayan Das on 05/09/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import UIKit

@IBDesignable
class PowerplaySelectButton: UIButton {
    
    enum ButtonStates {
        case select, selected, used, unavailable
    }
    
    open var buttonState: ButtonStates = .select {
        didSet {
            switch buttonState {
            case .select:
                isEnabled = true
                backgroundColor = .white
                addBorder(withColor: .green)
                setTitle("Select", for: .normal)
                setTitleColor(.black, for: .normal)
            case .selected:
                isEnabled = true
                backgroundColor = .green
                addBorder(withColor: .green)
                setTitle("Selected", for: .normal)
                setTitleColor(.black, for: .normal)
            case .used:
                isEnabled = false
                backgroundColor = UIColor.lightGray
                addBorder(withColor: .black)
                setTitle("Used", for: .disabled)
                setTitleColor(.black, for: .disabled)
            case .unavailable:
                isEnabled = false
                backgroundColor = UIColor.red
                removeBorder()
                setTitle("Unavailable", for: .disabled)
                setTitleColor(.white, for: .disabled)
            }
        }
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    func setup() {
        titleLabel?.lineBreakMode = .byWordWrapping
    }
    
    func addBorder(withColor color: UIColor) {
        self.layer.borderColor = color.cgColor
        self.layer.borderWidth = 1.2
    }
    
    func removeBorder() {
        self.layer.borderColor = nil
        self.layer.borderWidth = 0.0
    }
}
