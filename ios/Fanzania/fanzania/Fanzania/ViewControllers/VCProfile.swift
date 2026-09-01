//
//  VCProfile.swift
//  Fanzania
//
//  Created by Tathagata Dey on 30/11/18.
//  Copyright © 2018 Tathagata Dey. All rights reserved.
//

import UIKit
import Alamofire

class VCProfile: UIViewController, DatePickerDelegate, DelegateReturnCountry, UITextFieldDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    //UI elements
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var errorLabel: UILabel!
    @IBOutlet weak var labelName: UITextField!
    @IBOutlet weak var labelEmail: UITextField!
    @IBOutlet weak var tfSupportedTeam: NoPasteboardTextField!
    @IBOutlet weak var tfReferralCode: NoPasteboardTextField!
    @IBOutlet weak var tfReferralCount: NoPasteboardTextField!
    @IBOutlet weak var countrySelectBox: UIView!
    @IBOutlet weak var labelCountry: UILabel!
    @IBOutlet weak var phone: UITextField!
    @IBOutlet weak var dobSelectBox: UIView!
    @IBOutlet weak var labelDOB: UILabel!
    @IBOutlet weak var imageViewProfilePic: UIImageView!
    @IBOutlet weak var btnSaveProfile: UIButton!
    @IBOutlet weak var importProfilePicButton: UIButton!
    @IBOutlet weak var scroll: UIScrollView!
    @IBOutlet weak var switchCommPref: UISwitch!
    let imagePickerController = UIImagePickerController()
    lazy var distinctTeamPicker = UIPickerView()
    var distinctTeams = [String]()
    //local variables
    var dateSelected:Date?
    var countrySelected:CountryModel?
    
    //usuful formatter
    let dateFormat = DateFormatter()
    let dateGetterFormat = DateFormatter()
    let dateSendingFormat = DateFormatter()

    @IBAction func actionProfilePicUpload(_ sender: UIButton) {
        
        handleProfilePicker()
    }
    
    func handleProfilePicker() {
        
        let alert = UIAlertController(title: "Upload Photo",
                                        message: "",
                                        preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Take Photo",
                                        style: .default) { (action) in
        self.imagePickerController.sourceType = UIImagePickerController.SourceType.camera
                                            self.dismiss(animated: true, completion: nil)
        self.present(self.imagePickerController, animated: true, completion: nil)
                                        
        })
        alert.addAction(UIAlertAction(title: "Select Photo from Library",
                                        style: .default) { (action) in
        self.imagePickerController.sourceType = UIImagePickerController.SourceType.photoLibrary
                                            self.dismiss(animated: true, completion: nil)
        self.present(self.imagePickerController, animated: true, completion: nil)
                                        
        })
        alert.addAction(UIAlertAction(title: "Cancel",
                                        style: .cancel) { (action) in
                                        self.dismiss(animated: true, completion: nil)
        })
        self.present(alert, animated: true, completion: nil)
    }
 
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let original = info[.originalImage] as? UIImage {
            let data = original.jpegData(compressionQuality: 1.0)
            let imageURL = info[.referenceURL] as! NSURL
            let fileName = imageURL.absoluteString
            let selectedImageSize:Int = data!.count
            print("Image Size: %f KB", selectedImageSize/1024)
            
            Alamofire.upload(multipartFormData: { multipartFormData in
                multipartFormData.append(data!, withName: fileName!, fileName: fileName!+".jpg",mimeType: "image/jpeg")},
                             usingThreshold: UInt64.init(),
                             to: URL_ImageUpload_Profile,
                             method: .post,
                             headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                                       "x-api-userid" : UserDefaults.standard.string(forKey: "UserId")!],
                             encodingCompletion: { encodingResult in
                                switch encodingResult {
                                case .success(let upload, _, _):
                                    upload.responseJSON { response in
                                        debugPrint(response)
                                        
//                                        let invalid_login_alert = UIAlertController(title: "Profile Picture Update", message: "Successful", preferredStyle: .alert)
//
//                                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
//                                        }))
//                                        self.present(invalid_login_alert, animated: true, completion: nil)
                                    }
                                case .failure(let encodingError):
                                    print(encodingError)
                                }
            })
            
        }
        dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        print("action cancelled")
    }
    
//    private func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : //Any]) {
 //       var selectedImage: UIImage?
 //       if let original = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
//selectedImage = original
//            imageViewProfilePic.image = original
//        }else {
            
//        }
        
//        if let selectedImages = selectedImage {
//            if let data = selectedImages.jpegData(compressionQuality: 1){
//                let parameters: Parameters = [
//                    "access_token" : "YourToken"
//                ]
//                // You can change your image name here, i use NSURL image and convert into string
//                let imageURL = info["UIImagePickerControllerReferenceURL"] as! NSURL
//                let fileName = imageURL.absoluteString
//                print(fileName)
//                // Start Alamofire
//                /*
//                Alamofire.upload(multipartFormData: { multipartFormData in
//                    for (key,value) in parameters {
//                        multipartFormData.append((value as! String).data(using: .utf8)!, withName: key)
//                    }
//                    multipartFormData.append(data, withName: "avatar", fileName: fileName!,mimeType: "image/jpeg")
//                },
//                                 usingTreshold: UInt64.init(),
//                                 to: "YourURL",
//                                 method: .put,
//                                 encodingCompletion: { encodingResult in
//                                    switch encodingResult {
//                                    case .success(let upload, _, _):
//                                        upload.responJSON { response in
//                                            debugPrint(response)
//                                        }
//                                    case .failure(let encodingError):
//                                        print(encodingError)
//                                    }
//                })
//                 */
//            }
//        }
//    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if let title = (self.navigationItem.title) {
            self.setTitle(title)
        }
        self.hideKeyboardWhenTappedAround()
        // Do any additional setup after loading the view.
        imageViewProfilePic.makeCircleWithBorder()
//        importProfilePicButton.layer.cornerRadius = importProfilePicButton.frame.width/2
//        importProfilePicButton.layer.borderColor = UIColor.black.cgColor
//        importProfilePicButton.layer.borderWidth = 1
        labelName.roundedCorner()
        labelName.setLeftPaddingPoints(10.0)
        labelEmail.roundedCorner()
        labelEmail.setLeftPaddingPoints(10.0)
        tfReferralCode.roundedCorner(withBGColor: tfReferralCode.backgroundColor ?? .white)
        tfReferralCode.setLeftPaddingPoints(10.0)
        tfReferralCount.roundedCorner(withBGColor: tfReferralCount.backgroundColor ?? .white)
        tfReferralCount.setLeftPaddingPoints(10.0)
        countrySelectBox.setCurvedCornerBordered()
        dobSelectBox.setCurvedCornerBordered()
        phone.roundedCorner()
        phone.setLeftPaddingPoints(10.0)
        setupDistinctTeamField()
        labelName.delegate = self
        labelEmail.delegate = self
        phone.delegate = self
        self.errorView.alpha = 0.0
        btnSaveProfile.createDisableButtonGradientLayer()
        
        dateFormat.dateFormat = "dd-MMM-yyyy"
        dateFormat.timeZone = NSTimeZone(name: "UTC") as TimeZone?
        
        dateGetterFormat.dateFormat = "MM/dd/yyyy"
        dateGetterFormat.timeZone = NSTimeZone(name: "UTC") as TimeZone?
        
        dateSendingFormat.dateFormat = "yyyy-MM-dd"
        dateGetterFormat.timeZone = NSTimeZone(name: "UTC") as TimeZone?
        self.hideKeyboardWhenTappedAround()
        
        imagePickerController.delegate = self
        imagePickerController.allowsEditing = false
        
        distinctTeamPicker.dataSource = self
        distinctTeamPicker.delegate = self
        
        fetchDistinctTeams(forTournamentWithId: (self.tabBarController as? VCTabBar)?.CurrentTournamentID)
        fetchProfileData()
        
        self.hideKeyboardWhenTappedAround()
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillHideNotification, object: nil)
        notificationCenter.addObserver(self, selector: #selector(adjustForKeyboard), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        
    }
    
    func setupDistinctTeamField() {
        
        tfSupportedTeam.roundedCorner()
        tfSupportedTeam.setLeftPaddingPoints(10.0)
        
        let toolBar = UIToolbar()
        toolBar.barStyle = UIBarStyle.default
        toolBar.isTranslucent = true
        toolBar.sizeToFit()

        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.done, target: self, action: #selector(self.donePicker))
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Clear", style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.clearTeam))

        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        toolBar.isUserInteractionEnabled = true
        
        distinctTeamPicker.dataSource = self
        distinctTeamPicker.delegate = self
        
        tfSupportedTeam.inputView = distinctTeamPicker
        tfSupportedTeam.inputAccessoryView = toolBar
    }
    
    @objc func donePicker() {
        let row = distinctTeamPicker.selectedRow(inComponent: 0)
        distinctTeamPicker.selectRow(row, inComponent: 0, animated: false)
        tfSupportedTeam.text = distinctTeams[row]
        didEnableProfileEditedSaving = true
        tfSupportedTeam.resignFirstResponder()
    }
    
    @objc func clearTeam() {
        tfSupportedTeam.text = nil
        didEnableProfileEditedSaving = true
        tfSupportedTeam.resignFirstResponder()
    }
    
    @objc func adjustForKeyboard(notification : Notification){
        let userInfo = notification.userInfo!
        
        let keyboardScreenEndFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        let keyboardViewEndFrame = view.convert(keyboardScreenEndFrame, from: view.window)
        
        if notification.name == UIResponder.keyboardWillHideNotification {
            scroll.contentInset = UIEdgeInsets.zero
        } else {
            scroll.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: keyboardViewEndFrame.height, right: 0)
        }
        scroll.scrollIndicatorInsets = scroll.contentInset
        
    }
    //delegate methods
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        guard textField == tfReferralCode else {
            return true
        }
        guard let referralCode = textField.text else {
            return false
        }
        let textToShare = ["Hi! Join Fanzania with my referral code: \(referralCode)"]
        let activityViewController = UIActivityViewController(activityItems: textToShare, applicationActivities: nil)
        activityViewController.popoverPresentationController?.sourceView = self.view
        present(activityViewController, animated: true, completion: nil)
        return false
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.darkGray.cgColor
        didEnableProfileEditedSaving = true
        if self.errorView.alpha != 0.0 {
            
            UIView.transition(with: errorView, duration: 0.1, options: .transitionCrossDissolve, animations: {
                self.errorView.alpha = 0.0
                
            })
        }
    }
    
    override func resignFirstResponder() -> Bool {
        if self.errorView.alpha != 0.0{
            self.errorView.alpha = 0.0
        }
        return true
    }
    
    func getCountry(country: CountryModel) {
        print(country.name)
        countrySelected = country
        labelCountry.text = country.name
        didEnableProfileEditedSaving = true
    }
    
    func returnDate(date: Date) {
        print(date)
        dateSelected = date
        self.DOB = dateFormat.string(from: date)
        labelDOB.text = DOB
        didEnableProfileEditedSaving = true
    }
    
    var didEnableProfileEditedSaving:Bool = false {
        didSet {
            if didEnableProfileEditedSaving {
                btnSaveProfile.createGradientLayer()
                btnSaveProfile.isEnabled = true
            }else{
                btnSaveProfile.createDisableButtonGradientLayer()
                btnSaveProfile.isEnabled = false
            }
        }
    }
    
    @IBAction func actionChangeCommPrefStatus(_ sender: UISwitch) {
        didEnableProfileEditedSaving = true
    }
    
    
    @IBAction func ActionBirthdayFetch(_ sender: UIButton) {
        let st = UIStoryboard(name: StoryboardNames.Utility.rawValue, bundle: nil)
        let vc = st.instantiateViewController(withIdentifier: "datePicker") as! VCDatePickerPopUp
        vc.delegate = self
        vc.maxDate = Date()
        self.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func actionCountryFetch(_ sender: UIButton) {
        let st = UIStoryboard(name: StoryboardNames.Utility.rawValue, bundle: nil)
        let vc = st.instantiateViewController(withIdentifier: "countryChooser") as! VCSelectCountry
        vc.delegate = self
        self.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func actionSaveProfile(_ sender: UIButton) {
        guard let firstname = labelName.text, firstname != "" else{
            showInputValidationError(textField: labelName, error: "Enter Name")
            return
        }
        guard let phoneNumber = phone.text, phoneNumber.validatePhone() else{
            showInputValidationError(textField: phone, error: "Enter Phone")
            return
        }
        
        saveProfileData(name: firstname, phone: phoneNumber)
    }
    
    func showInputValidationError(textField: UIView, error: String){
        
        textField.layer.borderColor = UIColor.red.cgColor
        textField.shakeAnimation()
        errorLabel.text = error
        self.errorView.alpha = 1.0
        
        let transition = CATransition()
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromBottom
        errorView.layer.add(transition, forKey: nil)
        self.view.addSubview(self.errorView)
        
    }
    
    var name:String?
    var Email:String?
    var DOB:String?
    var PhoneNumber:String?
    var ProfileImage:String?
    
    func fetchProfileData() {
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_USER_Profile,
                          method: .post,
                          parameters: ["UserId" : UserDefaults.standard.integer(forKey: "UserId")] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                                    "x-api-userid" : UserDefaults.standard.string(forKey: "UserId")!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    print(responseJSON)
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? NSArray{
                            print(dataArray)
                            if let item = dataArray[0] as? [String:Any]{
                                self.name = item["Name"] as? String
                                self.Email = item["Email"] as? String
                                
                                self.tfSupportedTeam.text = item["BackgroundTheme"] as? String
                                
                                if let countryName = item["Country"] as? String, let CountryId = item["CountryId"] as? Int {
                                    self.countrySelected = CountryModel(name: countryName, id: CountryId, active: true)
                                }
                                
                                self.DOB = item["DOB"] as? String
                                self.PhoneNumber = item["PhoneNumber"] as? String
                                self.ProfileImage = item["ProfileImage"] as? String
                                
                                if let ProfileImage = self.ProfileImage {
                                    self.showProfilePicture(imageName: ProfileImage)
                                }else{
                                    self.imageViewProfilePic.image = userProfilePlaceholder
                                }
                                
                                if let name = self.name {
                                    UserDefaults.standard.set(name, forKey: UserDefaultData.Name.rawValue)
                                    self.labelName.text = name
                                } else {
                                    self.labelName.text = "Please enter a name"
                                }
                                
                                if let email = self.Email {
                                    UserDefaults.standard.set(email, forKey: UserDefaultData.Email.rawValue)
                                    self.labelEmail.text = email
                                }
                                
                                
                                if let phoneNumber = self.PhoneNumber {
                                    self.phone.text = phoneNumber
                                    UserDefaults.standard.set(phoneNumber, forKey: UserDefaultData.PhoneNumber.rawValue)
                                }else{
                                    self.phone.text = "Please Enter a valid number"
                                }
                                
                                if let country = self.countrySelected {
                                    self.labelCountry.text = country.name
                                }else{
                                    self.labelCountry.text = "Select Country"
                                }
                                
                                if let dob = self.DOB {
                                    self.dateSelected = self.dateGetterFormat.date(from: dob)
                                    self.labelDOB.text = self.dateFormat.string(from: self.dateSelected!)
                                }else{
                                    self.labelDOB.text = "Select Date of Birth"
                                }
                                
                                var commPref = false
                                if let commPrefInt = item["CommPreference"] as? Int {
                                    commPref = ( commPrefInt == 1 ) ? true : false
                                }
                                
                                self.tfReferralCode.text = item["ReferralCode"] as? String
                                self.tfReferralCount.text = "\(item["ReferralCount"] as? Int ?? 0)"
                                
                                self.switchCommPref.setOn(commPref, animated: false)
                                
                            }
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func showProfilePicture(imageName:String){
        
        Alamofire.request(URLServerProfilePicturePath+imageName).responseImage { response in
            
            if let image = response.result.value {
                print("image downloaded: \(image)")
                self.imageViewProfilePic.image = image
            }else{
                self.imageViewProfilePic.image = userProfilePlaceholder
            }
        }
    }
    
    func saveProfileData(name:String, phone:String) {
        
        var param = ["UserId" : UserDefaults.standard.integer(forKey: "UserId"),
                     "Name" : name,
                     "Email" : labelEmail.text!,
                     "PhoneNumber" : phone,
                     "BackgroundTheme": tfSupportedTeam.text ?? "",
                     "CommPreference" : switchCommPref.isOn] as [String : Any]
        
        if countrySelected != nil {
            param["CountryId"] = countrySelected?.id
        } else {
            param["CountryID"] = 0
        }
        
        if dateSelected != nil {
            param["DOB"] = self.dateSendingFormat.string(from: self.dateSelected!)
        }else {
            param["DOB"] = DOB
        }
        
        print("parameters \(param)")
        
        let loader = UIViewController.displaySpinner(onView: self.view)
        Alamofire.request(URL_USER_ProfileSave,
                          method: .post,
                          parameters: param,
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                                    "x-api-userid" : UserDefaults.standard.string(forKey: "UserId")!, "x-api-devicetype":"ios"])
            .responseJSON { response in
                DispatchQueue.main.async {
                    UIViewController.removeSpinner(spinner: loader)
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    print(responseJSON)
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        let invalid_login_alert = UIAlertController(title: "Profile Updated", message: "Successfully" as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in
                            if let dataArray = jsonDictionary["data"] as? NSArray{
                                print(dataArray)
                                if let item = dataArray[0] as? [String:Any]{
                                    let name = item["Name"] as? String
                                    let Email = item["Email"] as? String
                                    let Country = item["Country"] as? String
                                    let DOB = item["DOB"] as? String
                                    let PhoneNumber = item["PhoneNumber"] as? String
                                    
                                    UserDefaults.standard.set(name, forKey: UserDefaultData.Name.rawValue)
                                    UserDefaults.standard.set(Email, forKey: UserDefaultData.Email.rawValue)
                                    UserDefaults.standard.set(PhoneNumber, forKey: UserDefaultData.PhoneNumber.rawValue)
                                    UserDefaults.standard.set(self.tfSupportedTeam.text, forKey: UserDefaultData.BackgroundTheme.rawValue)
                                    
                                    
                                    self.btnSaveProfile.createDisableButtonGradientLayer()
                                    self.btnSaveProfile.isEnabled = false
                                    
                                }
                            }
                            self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                        
                        
                        
                        
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
    func fetchDistinctTeams(forTournamentWithId tournamentId: Int?) {
        
        guard let tournamentId = tournamentId else {
            return
        }
        
        Alamofire.request(URL_DistinctTeam,
                          method: .post,
                          parameters: ["TournamentId": tournamentId] as [String : Any],
                          encoding: JSONEncoding.default,
                          headers: ["x-api-authtoken" : UserDefaults.standard.string(forKey: UserDefaultData.SessionId.rawValue)!,
                                    "x-api-userid" : UserDefaults.standard.string(forKey: "UserId")!, "x-api-devicetype":"ios"])
            .responseJSON { [weak self] response in
                guard let self = self else {
                    return
                }
                switch response.result {
                case .success:
                    guard let responseJSON = try? JSONSerialization.jsonObject(with: response.data!, options: []) else{
                        print("No data found")
                        return
                    }
                    print(responseJSON)
                    guard let jsonDictionary = responseJSON as? [String: Any] else{
                        print("json format mismatch first")
                        return
                    }
                    print(jsonDictionary)
                    guard let status = jsonDictionary["status"] as? String else{
                        print("json format mismatch second")
                        return
                    }
                    if status == "success" {
                        
                        if let dataArray = jsonDictionary["data"] as? [[String: Any]] {
                            print(dataArray)
                            self.distinctTeams = dataArray.compactMap({ $0["TeamShortName"] as? String })
                            self.distinctTeamPicker.reloadAllComponents()
                        }
                    }else{
                        let invalid_login_alert = UIAlertController(title: "Server Problem", message: jsonDictionary["statusMessage"] as? String, preferredStyle: .alert)
                        
                        invalid_login_alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { action in self.dismiss(animated: true, completion: nil)
                        }))
                        self.present(invalid_login_alert, animated: true, completion: nil)
                    }
                    
                    
                case .failure(let error):
                    print("Request failed with error: \(error)")
                }
        }
    }
    
}

// MARK: - UIPickerViewDataSource, UIPickerViewDelegate
extension VCProfile: UIPickerViewDataSource, UIPickerViewDelegate {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return distinctTeams.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return distinctTeams[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        tfSupportedTeam.text = distinctTeams[row]
        didEnableProfileEditedSaving = true
    }
}
