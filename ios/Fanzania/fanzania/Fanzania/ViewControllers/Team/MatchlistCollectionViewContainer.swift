//
//  MatchlistCollectionViewContainer.swift
//  Fanzania
//
//  Created by Tathagata Dey on 06/05/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class MatchlistCollectionViewContainer: UIView, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var collectionViewMatchList: UICollectionView!
    @IBOutlet weak var contentView: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        //xibSetup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        // Setup view from .xib file
        //xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        // Setup view from .xib file
        //xibSetup()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionViewMatchList.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath)
        cell.layer.backgroundColor = UIColor.white.cgColor
        return cell
    }
    

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}

