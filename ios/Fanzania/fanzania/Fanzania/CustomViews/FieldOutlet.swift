//
//  MCustomView.swift
//  Fanzania
//
//  Created by Tathagata Dey on 11/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit
import AlamofireImage
import Alamofire

enum ShowableAccountableNoType {
    case points
    case cost
    case none
}

class FieldOutlet: UIView {

    @IBOutlet var containerView: UIView!
    @IBOutlet var playerImages:[UIButton]!
    @IBOutlet var playerNames:[UILabel]!
    @IBOutlet var playerRoles:[UIButton]!
    @IBOutlet var playerCaptaincy:[UIButton]!
    @IBOutlet var playerAccountableNumber:[UILabel]!
    var isShowingAccountableNumber:ShowableAccountableNoType = .points
    
    var allowcations:[PlayerInfo] = []
    var teamCaptId:Int?
    var teamVCaptId:Int?
    
    var dataSource:(list:[PlayerInfo], teamCaptId:Int?, teamVCaptId:Int?)? {
        didSet{
            self.teamCaptId = dataSource?.teamCaptId
            self.teamVCaptId = dataSource?.teamVCaptId
            
            guard let playerList = dataSource?.list else { return }
            var batsmanList:[PlayerInfo] = []
            var bowlerList:[PlayerInfo] = []
            var allrounderList:[PlayerInfo] = []
            var keeperList:[PlayerInfo] = []
            
            for player in playerList {
                switch player.speciality {
                case .batsman :
                    batsmanList.append(player)
                case .bowler :
                    bowlerList.append(player)
                case .allrounder :
                    allrounderList.append(player)
                default :
                    keeperList.append(player)
                }
            }
            
            allowcations.removeAll()
            
            allowcations += keeperList
            allowcations += batsmanList
            allowcations += bowlerList
            allowcations += allrounderList
            
            populateField()
//            reOrderPlayerPositions()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }
    
    func commonInit(){
    
        Bundle.main.loadNibNamed("FieldOutlet", owner: self, options: nil)
        addSubview(containerView)
        containerView.frame = self.bounds
        containerView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
    }
    
//    func reOrderPlayerPositions(){
//
//        let newPositions = [PlayerInfo]? {
//
//        }
//        //allowcations = Array(repeating: nil, count: 11)
//        let bowler_allrounder_merge:[PlayerInfo] = bowlerList + allrounderList
//        var index = 10
//        allowcations[0] = wktKeeper.first
//        for player in bowler_allrounder_merge.reversed() {
//
//            allowcations[index] = player
//            index -= 1
//        }
//        for (index, player) in batsmanList.enumerated() {
//
//            allowcations[index+1] = player
//        }
//        totalPlayers = batsmanList.count + allrounderList.count + bowlerList.count + wktKeeper.count
//
//        //        if totalPlayers == 0 {
//        //            secondaryButton.isEnabled = true
//        //            secondaryButton.createGradientLayer()
//        //        }else{
//        //            secondaryButton.isEnabled = false
//        //            secondaryButton.createDisableButtonGradientLayer()
//        //        }
//    }
    
    func populateField(){
        
        let imageDummy = UIImage(named: "player-dummy")
        let imageDefault = UIImage(named: "create-player")
        
        
        if allowcations.count != 0 {
            var i=0
            while i<11 {
                if allowcations[i] != nil {
                    playerImages[i].setImage(imageDummy, for: .normal)
                    playerNames[i].text = allowcations[i].shortName
                    if allowcations[i].id == teamCaptId {
                        playerCaptaincy[i].setImage(Captaincy.Captain.image, for: .normal)
                    }else if allowcations[i].id == teamVCaptId {
                        playerCaptaincy[i].setImage(Captaincy.ViceCaptain.image, for: .normal)
                    } else{
                        playerCaptaincy[i].setImage(nil, for: .normal)
                    }
                    
                    getPlayerImages(imagename: allowcations[i].playerImageName, playerImageButton: (playerImages?[i])!)
                    
                    switch allowcations[i].speciality {
                    case .batsman :
                        playerRoles?[i].setImage(PlayerSpeciality.batsman.image, for: .normal)
                    case .bowler :
                        playerRoles?[i].setImage(PlayerSpeciality.bowler.image, for: .normal)
                    case .allrounder :
                        playerRoles?[i].setImage(PlayerSpeciality.allrounder.image, for: .normal)
                    default :
                        playerRoles?[i].setImage(PlayerSpeciality.wicketKeeper.image, for: .normal)
                    }
                    switch isShowingAccountableNumber {
                    case .points:
                        playerAccountableNumber[i].text = String(describing: (allowcations[i].totalPoints)!)
                    case .cost:
                        playerAccountableNumber[i].text = String(describing: (allowcations[i].value)!) + "K"
                    case .none:
                        playerAccountableNumber[i].text = ""
                    }
                    playerImages![i].removeTarget(nil, action: nil, for: .touchUpInside)
                }else{
                    playerImages[i].setImage(imageDefault, for: .normal)
                    playerNames[i].text = ""
                    playerCaptaincy[i].setTitle("", for: .normal)
                    playerRoles[i].setImage(nil, for: .normal)
                    playerAccountableNumber[i].text = ""
                }
                i+=1
            }
        }
    }
    
    func getPlayerImages(imagename:String?, playerImageButton : UIButton){
        
        let imageCache = ImageCatchingSingleTone.shared.getCacheInstance()
        if imagename != nil {
            
            if let cachedImage = imageCache!.image(withIdentifier: imagename!) {
                playerImageButton.setImage(cachedImage, for: UIControl.State.normal)
            }else{
                Alamofire.request(URL_SERVER_IMAGE_LOCATION_PlayerImage+imagename!).responseImage { response in
                    
                    if let image = response.result.value {
                        print("image downloaded: \(image)")
                        imageCache!.add(image, withIdentifier: imagename!)
                        playerImageButton.setImage(image, for: .normal)
                    }else{
                        playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
                    }
                }
            }
            
        }else{
            playerImageButton.setImage(playerImagePlaceHolder, for: .normal)
        }
    }
}
