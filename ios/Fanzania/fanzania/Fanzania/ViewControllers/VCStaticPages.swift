//
//  VCStaticPages.swift
//  Fanzania
//
//  Created by Tathagata Dey on 12/03/19.
//  Copyright © 2019 Tathagata Dey. All rights reserved.
//

import UIKit

class VCStaticPages: UIViewController, UIWebViewDelegate {

    @IBOutlet var webView: UIWebView!
    var loader:UIView?
    var staticPageLink:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        guard let staticPageLink = staticPageLink else {
            return;
        }
        loader = UIViewController.displaySpinner(onView: self.view)
        let url = URL (string: staticPageLink)
        let request = URLRequest(url: url!)
        
        webView.loadRequest(request)
        navigationController?.navigationBar.barTintColor = UIColor.colorAppPrimary()()
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        DispatchQueue.main.async {
            UIViewController.removeSpinner(spinner: self.loader!)
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
