//
//  ViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/11/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var publicKeyText: UITextField!
    @IBOutlet weak var privateKeyText: UITextField!

    @IBOutlet weak var bucketText: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        publicKeyText.delegate = self;
        privateKeyText.delegate = self;
        bucketText.delegate = self;
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
        // Dispose of any resources that can be recreated.
    @IBAction func onTouchUploadFile(sender: AnyObject) {
        
    }

    @IBAction func onTouchDownloadFile(sender: AnyObject) {
    }
    
    @IBAction func onTouchDeleteFile(sender: AnyObject) {
    }
    
    @IBAction func onTouchHeadFile(sender: AnyObject) {
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        return textField.resignFirstResponder();
    }
    
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let ufileSDK = UFileSDK(fromKeys: publicKeyText.text!, privateKey: privateKeyText.text!, bucket: bucketText.text!)

        if segue.identifier == "head" {
            let headViewCtrl = segue.destinationViewController as! HeadViewController
            headViewCtrl.ufileSDK = ufileSDK;
        } else if segue.identifier == "upload" {
            let headViewCtrl = segue.destinationViewController as! UploadViewController
            headViewCtrl.ufileSDK = ufileSDK;
        } else if segue.identifier == "download" {
            let headViewCtrl = segue.destinationViewController as! DownloadViewController
            headViewCtrl.ufileSDK = ufileSDK
        } else if segue.identifier == "delete"{
            let headViewCtrl = segue.destinationViewController as! DeleteViewController
            headViewCtrl.ufileSDK = ufileSDK
        } else if segue.identifier == "multipart" {
            let headViewCtrl = segue.destinationViewController as! MultipartUploadViewController
            headViewCtrl.ufileSDK = ufileSDK
        }
    }
}

