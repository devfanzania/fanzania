//
//  CLPlacemark + Codable.swift
//  Fanzania
//
//  Created by Writayan Das on 19/08/21.
//  Copyright © 2021 Tathagata Dey. All rights reserved.
//

import Foundation
import CoreLocation

extension CLPlacemark {
    
    func jsonValue() -> [String: Any] {
        
        let locationCoords: [String: Any?] = [
            "latitude": location?.coordinate.latitude,
            "longitude": location?.coordinate.longitude
        ]
        
        let json: [String: Any?] = [
            "location": locationCoords.compactMapValues({ $0 }),
            "region": region?.identifier,
            "timeZone": timeZone?.identifier,
            "name": name,
            "thoroughfare": thoroughfare,
            "subThoroughfare": subThoroughfare,
            "locality": locality,
            "subLocality": subLocality,
            "administrativeArea": administrativeArea,
            "subAdministrativeArea": subAdministrativeArea,
            "postalCode": postalCode,
            "isoCountryCode": isoCountryCode,
            "country": country,
            "inlandWater": inlandWater,
            "ocean": ocean
        ]
        return json.compactMapValues({ $0 })
    }
}
