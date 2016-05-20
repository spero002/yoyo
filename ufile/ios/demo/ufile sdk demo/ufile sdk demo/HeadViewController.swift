//
//  HeadViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/14/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class HeadViewController: UIViewController {
    var ufileSDK: UFileSDK?
    
    @IBOutlet weak var fileNameText: UITextField!
    
    @IBAction func headFile(sender: AnyObject) {
        let key = fileNameText.text!
        let auth = ufileSDK!.calcKey("HEAD", key: key, contentMd5: nil, contentType: nil);
        ufileSDK!.ufileSDK?.headFile(key, authorization: auth,
            success: { (result: [NSObject : AnyObject]) -> Void in
                NSLog("%@", result);
                let s = "etag: " + (result[kUFileRespETag] as! NSString as String) + "\n"
                      + "filetype: " + (result[kUFileRespFileType] as! NSString as String) + "\n"
                    + "length: " + (result[kUFileRespLength] as! NSString as String)
                let alert = UIAlertController(title: "File Info", message: s, preferredStyle: UIAlertControllerStyle.Alert)
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
