//
//  CountryModel.swift
//  Fanzania
//
//  Created by Tathagata Dey on 31/10/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import Foundation

struct CountryModel:Hashable {
    var name:String
    var id:Int
    var active:Bool
    
    init(name:String, id:Int, active:Bool) {
        self.name = name
        self.id = id
        self.active = active
    }
}
