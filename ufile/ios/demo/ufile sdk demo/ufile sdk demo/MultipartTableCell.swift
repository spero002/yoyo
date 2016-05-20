//
//  MultipartTableCell.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/16/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class MultipartTableCell: UITableViewCell {
    var session: MultipartSession?
    var partNumber: Int = 0
    
    @IBOutlet weak var progressBar: UIProgressView!
    
    @IBOutlet weak var downloadBtn: UIButton!
    
    func setSession(session: MultipartSession, partNumber: Int) {
        self.session = session
        self.partNumber = partNumber
        self.progressBar.setProgress(0, animated: false)
        self.downloadBtn.enabled = true
    }
    
    @IBAction func startDownload(sender: AnyObject) {
        let contentType = "application/jpeg"
        let auth = self.session!.ufileSDK.calcKey("PUT", key: self.session!.key, contentMd5: nil, contentType: contentType)
        self.session!.ufileSDK.ufileSDK?.multipartUploadPart(self.session!.key, uploadId: self.session!.uploadId, partNumber: self.partNumber, contentType: contentType, data: (self.session!.getDataForPart(UInt(self.partNumber)))!, authorization: auth,
            progress: { (progress:NSProgress) -> Void in
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    self.progressBar.setProgress(Float(progress.fractionCompleted), animated: true)
                })
            },
            success: { (result: [NSObject : AnyObject]) -> Void in
                self.downloadBtn.enabled = false
                self.session!.addEtag(UInt(self.partNumber), etag: result[kUFileRespETag] as! NSString as String)
            },
            failure: { (err: NSError) -> Void in
                self.progressBar.backgroundColor = UIColor.redColor()
        })
    }
}
