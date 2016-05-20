//
//  DeleteViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/14/15.
//  Copyright © 2015 ucloud. All rights reserved.
//


import UIKit

class DeleteViewController: UIViewController {
    
    var ufileSDK: UFileSDK?
    
    @IBOutlet weak var keyText: UITextField!
    
    @IBAction func deleteFile2(sender: AnyObject) {
        let key = keyText.text!
        let auth = ufileSDK!.calcKey("DELETE", key: key, contentMd5: nil, contentType: nil);
        ufileSDK?.ufileSDK!.deleteFile(key, authorization: auth,
            success: {(result: [NSObject : AnyObject]) -> Void in
                let alert = UIAlertController(title: "File Delete Done", message: key + " File Deleted", preferredStyle: UIAlertControllerStyle.Alert)
                let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
                    UIAlertAction in
                }
                alert.addAction(okAction)
                self.presentViewController(alert, animated: true, completion: nil)
            },
            failure: { (err: NSError) -> Void in
                var errMsg: String? = nil;
                if err.domain == kUFileSDKAPIErrorDomain {
                    errMsg = err.userInfo["ErrMsg"] as? NSString as? String;
                    NSLog("%@", err.userInfo);
                } else {
                    errMsg = err.description
                }
                let alert = UIAlertController(title: "Fail Shame", message: errMsg, preferredStyle: UIAlertControllerStyle.Alert)
                let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
                    UIAlertAction in
                }
                alert.addAction(okAction)
                self.presentViewController(alert, animated: true, completion: nil)
                
        })
    }
    
}
