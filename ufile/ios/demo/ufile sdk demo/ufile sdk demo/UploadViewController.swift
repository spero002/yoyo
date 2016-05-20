//
//  UploadViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/14/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class UploadViewController: UIViewController {
    var ufileSDK: UFileSDK?
    
    @IBOutlet weak var rangeText: UITextField!
    
    @IBOutlet weak var progressBar: UIProgressView!
    
    @IBAction func uploadFile(sender: AnyObject) {
        let b = NSBundle.mainBundle().pathForResource("initscreen", ofType: "jpg")
        let key = "initscreen.jpg"
        let auth = ufileSDK!.calcKey("PUT", key: key, contentMd5: "0d28630ac6120b1cdee408de0492482c", contentType: "application/jpeg")
        let data = NSData(contentsOfFile: b!)
        let itMd5 = UFileAPIUtils.calcMD5ForData(data!);
        let option = [kUFileSDKOptionMD5: itMd5,
            kUFileSDKOptionFileType: "application/jpeg"]
        ufileSDK!.ufileSDK!.putFile(key, authorization: auth, option: option, data: data!,
            progress: { (progress: NSProgress) -> Void in
                dispatch_async(dispatch_get_main_queue(), {() in
                    self.progressBar.setProgress(Float(progress.fractionCompleted), animated: true)
                })
            },
            success: { (result: [NSObject : AnyObject]) -> Void in
                let s = key + " File Uploaded, etag: " + (result[kUFileRespETag] as! String)
                let alert = UIAlertController(title: "File Upload Done", message: s, preferredStyle: UIAlertControllerStyle.Alert)
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
                let alert = UIAlertController(title: "File Upload Fail", message: errMsg, preferredStyle: UIAlertControllerStyle.Alert)
                let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
                    UIAlertAction in
                }
                alert.addAction(okAction)
                self.presentViewController(alert, animated: true, completion: nil)
        })
    }

    @IBAction func uploadHit(sender: AnyObject) {
        let b = NSBundle.mainBundle().pathForResource("initscreen", ofType: "jpg")
        let key = "initscreen.jpg"
        let auth = ufileSDK!.calcKey("POST", key: key, contentMd5: nil, contentType: nil)
        NSLog(auth);
        let data = NSData(contentsOfFile: b!)
        let length = data!.length
        ufileSDK!.ufileSDK!.uploadHit(key, authorization: auth, fileSize: length, fileHash: "AQAAAJQnkf8WXMgGCb2-WYPgLZGI7yz1",
            success: { (result: [NSObject : AnyObject]) -> Void in
                let s = key + " File UploadHit Suceess "
                let alert = UIAlertController(title: "File Upload Hit Done", message: s, preferredStyle: UIAlertControllerStyle.Alert)
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
                let alert = UIAlertController(title: "File Upload Fail", message: errMsg, preferredStyle: UIAlertControllerStyle.Alert)
                let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
                    UIAlertAction in
                }
                alert.addAction(okAction)
                self.presentViewController(alert, animated: true, completion: nil)
        })
    }
    @IBAction func putFileFromFile(sender: AnyObject) {
        let b = NSBundle.mainBundle().pathForResource("initscreen", ofType: "jpg")
        let key = "initscreen-fromfile.jpg"
        let itMd5 = UFileAPIUtils.calcMD5ForPath(b!)
        let auth = ufileSDK!.calcKey("PUT", key: key, contentMd5: itMd5, contentType: "application/jpeg")
        let option = [kUFileSDKOptionMD5: "0d28630ac6120b1cdee408de0492482c",
            kUFileSDKOptionFileType: "application/jpeg"]
        ufileSDK!.ufileSDK!.putFile(key, fromFile: b!, authorization: auth, option: option,
            progress: { (progress: NSProgress) -> Void in
                dispatch_async(dispatch_get_main_queue(), {() in
                    self.progressBar.setProgress(Float(progress.fractionCompleted), animated: true)
                })
            },
            success: { (result: [NSObject : AnyObject]) -> Void in
                let s = key + " File Uploaded, etag: " + (result[kUFileRespETag] as! String)
                let alert = UIAlertController(title: "File Upload Done", message: s, preferredStyle: UIAlertControllerStyle.Alert)
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
                let alert = UIAlertController(title: "File Upload Fail", message: errMsg, preferredStyle: UIAlertControllerStyle.Alert)
                let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
                    UIAlertAction in
                }
                alert.addAction(okAction)
                self.presentViewController(alert, animated: true, completion: nil)
        })
    }
}
