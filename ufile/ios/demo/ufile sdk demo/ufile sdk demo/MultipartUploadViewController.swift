//
//  MultipartUploadViewController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/16/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class MultipartUploadViewController : UIViewController {
    
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var startUpload: UIButton!
    
    @IBOutlet weak var cancelBtn: UIButton!
    
    @IBOutlet weak var finishUploadBtn: UIButton!
    
    var ufileSDK : UFileSDK?
    var session: MultipartSession?
    var tableController: MultipartTableController?
    
    @IBOutlet weak var upload: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableController = MultipartTableController()
        self.table.delegate = self.tableController
        self.table.dataSource = self.tableController
        self.table.reloadData()
        self.startUpload.enabled = true
        self.finishUploadBtn.enabled = false
        self.cancelBtn.enabled = false
    }
    
    @IBAction func uploadBtnClicked(sender: AnyObject) {
        
        self.cancelBtn.enabled = true
        self.finishUploadBtn.enabled = true
        self.startUpload.enabled = false

        let b = NSBundle.mainBundle().pathForResource("initscreen", ofType: "jpg")
        let key = "test-multipart.jpg"
 //       let data = NSData(contentsOfFile: b!)
        let auth = ufileSDK!.calcKey("POST", key: key, contentMd5: nil, contentType: nil)
        ufileSDK!.ufileSDK!.multipartUploadStart(key, authorization: auth,
            success: { (result: [NSObject : AnyObject]) -> Void in
                let msg = String(format: "Multipart start:\n uploadId=%@\nblockSize=%lu\nbucketName=%@\nKey=%@\n", arguments: [result[kUFileRespUploadId] as! NSString, (result[kUFileRespBlockSize] as! NSNumber).integerValue, result[kUFileRespBucketName] as! NSString, result[kUFileRespKeyName] as! NSString]);
                self.session = MultipartSession(info: self.ufileSDK!, uploadId: result[kUFileRespUploadId] as! NSString as String, blockSize: UInt(result[kUFileRespBlockSize] as! NSNumber), fileURL: b!, key: key)
                self.tableController?.setSession(self.session!)
                self.table.reloadData()
                self.showMsg(msg)
                
            },
            failure: { (err: NSError) -> Void in
                self.showError(err)
        })
    }
    
    private func showMsg(msg: String) {
        let alert = UIAlertController(title: "File Upload Done", message: msg, preferredStyle: UIAlertControllerStyle.Alert)
        let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.Default) {
            UIAlertAction in
        }
        alert.addAction(okAction)
        self.presentViewController(alert, animated: true, completion: nil)
    }
    
    private func showError(err: NSError) {
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
    }
    
    @IBAction func finishUpload(sender: AnyObject) {
        self.cancelBtn.enabled = false
        self.finishUploadBtn.enabled = false
        self.startUpload.enabled = true
        let auth = self.ufileSDK!.calcKey("POST", key: self.session!.key, contentMd5: nil, contentType: "application/jpeg")
        self.ufileSDK!.ufileSDK!.multipartUploadFinish(self.session!.key, uploadId: self.session!.uploadId, newKey: self.session!.key+NSTimeIntervalSince1970.description, etags: self.session!.etags, contentType: "application/jpeg", authorization: auth, success: { (result:[NSObject : AnyObject]) -> Void in
                let s = String(format: "File upload success\nBucket=%@\nKey=%@\nFileSize=%@", arguments: [result[kUFileRespBucketName] as! NSString, result[kUFileRespKeyName] as! NSString, result[kUFileRespLength] as! NSNumber])
                self.showMsg(s)
            }) { (err) -> Void in
                self.showError(err)
        }
        
        self.session = nil
    }
    
    @IBAction func cancelUpload(sender: AnyObject) {
        self.cancelBtn.enabled = false
        self.finishUploadBtn.enabled = false
        self.startUpload.enabled = true
        let auth = self.ufileSDK!.calcKey("DELETE", key: self.session!.key, contentMd5: nil, contentType: nil)
        self.ufileSDK!.ufileSDK!.multipartUploadAbort(self.session!.key, uploadId: self.session!.uploadId, authorization: auth,success: { (result:[NSObject : AnyObject]) -> Void in
            let s = String("File upload aborted")
            self.showMsg(s)
            }) { (err) -> Void in
                self.showError(err)
        }
        self.session = nil

    }
    
}

