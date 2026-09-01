//
//  LiveScoreMatchCollectionViewCell.swift
//  Fanzania
//
//  Created by Tathagata Dey on 29/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class LiveScoreMatchCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var team1Label: UILabel!
    @IBOutlet weak var team2Label: UILabel!
    @IBOutlet weak var team1ImageView: UIImageView!
    @IBOutlet weak var team2ImageView: UIImageView!
    @IBOutlet weak var matchPointLabel: UILabel!
    @IBOutlet weak var totalPointsLabel: UILabel!
    
    var dataSource:LiveMatchModel? {
        didSet{
            if let dataSource = dataSource {
                team1Label.layer.cornerRadius = 15.0
                team2Label.layer.cornerRadius = 15.0
                team1Label.layer.masksToBounds = true;
                team2Label.layer.masksToBounds = true;
                team1Label.backgroundColor = UIColor.black
                team2Label.backgroundColor = UIColor.black
                team1Label.text = dataSource.team1ShortName
                team2Label.text = dataSource.team2ShortName
                
                getPlayerImages(imagename: dataSource.team1ImageName!, playerimageView: team1ImageView)
                getPlayerImages(imagename: dataSource.team2ImageName!, playerimageView: team2ImageView)
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func getPlayerImages(imagename:String?, playerimageView: UIImageView){
        
        let imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        
        if imagename != nil {
            
            if let cachedImage = imageCache!.image(withIdentifier: imagename!) {
                playerimageView.image = cachedImage
            }else{
                print("image path \(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!)")
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!).responseImage { response in
                    
                    if let image = response.result.value {
                        print("image downloaded: \(image)")
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerimageView.image = image
                    }else{
                        playerimageView.image = playerImagePlaceHolder
                    }
                }
            }
            
        }else{
             playerimageView.image = playerImagePlaceHolder
        }
    }
}
