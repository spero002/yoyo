
//
//  DownloadViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/14/15.
//  Copyright © 2015 ucloud. All rights reserved.
//


import UIKit

class DownloadViewController: UIViewController {
    var ufileSDK: UFileSDK?
    
    @IBOutlet weak var progress: UIProgressView!
    @IBOutlet weak var rangeText: UITextField!
    @IBOutlet weak var downloadFileName: UITextField!
    @IBAction func downloadFile(sender: AnyObject) {
        progress.setProgress(0.0, animated: true)
        let range = self.rangeText.text!
        let fileName = self.downloadFileName.text!
        var options = [String:String]()
        if !range.isEmpty {
            options[kUFileSDKOptionRange] = range
        }
        let auth = ufileSDK!.calcKey("GET", key: fileName, contentMd5: nil, contentType: nil);
        ufileSDK?.ufileSDK!.getFile(fileName, authorization: auth, option: options as [NSObject : AnyObject], progress: { (p: NSProgress) -> Void in
                dispatch_async(dispatch_get_main_queue()) {
                    self.progress.setProgress(Float(p.fractionCompleted), animated: true)
                }
            },
            success: {(result: [NSObject : AnyObject], responseObj: AnyObject) -> Void in
                let data = responseObj as! NSData
                let alert = UIAlertController(title: "File Delete Done", message: fileName + " File Downloaded, file size: " + UInt(data.length).description, preferredStyle: UIAlertControllerStyle.Alert)
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
    @IBAction func downloadToFile(sender: AnyObject) {
        progress.setProgress(0.0, animated: true)
        let range = self.rangeText.text!
        let fileName = self.downloadFileName.text!
        var options = [String:String]()
        if !range.isEmpty {
            options[kUFileSDKOptionRange] = range
        }
        let dirUrl = NSFileManager.defaultManager().URLsForDirectory(NSSearchPathDirectory.CachesDirectory, inDomains: NSSearchPathDomainMask.UserDomainMask);
        let downloadFile = NSURL(fileURLWithPath: fileName, relativeToURL: dirUrl[0])
        let auth = ufileSDK!.calcKey("GET", key: fileName, contentMd5: nil, contentType: nil);
        ufileSDK!.ufileSDK!.getFile(fileName, toFile: downloadFile.path!, authorization: auth, option: options  as [NSObject : AnyObject],
            progress: { (p: NSProgress) -> Void in
                p.pause()
                let delta: Int64 = 10 * Int64(NSEC_PER_SEC)
                let time = dispatch_time(DISPATCH_TIME_NOW, delta)
                dispatch_after(time, dispatch_get_main_queue(), {
                    p.resume()
                })
                dispatch_async(dispatch_get_main_queue()) {
                    self.progress.setProgress(Float(p.fractionCompleted), animated: true)
                }
            },
            success: {(result: [NSObject : AnyObject], responseObj: AnyObject) -> Void in
                let data = (responseObj as! NSURL).absoluteString
                let alert = UIAlertController(title: "File Delete Done", message: fileName + " File Downloaded to " + data, preferredStyle: UIAlertControllerStyle.Alert)
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
