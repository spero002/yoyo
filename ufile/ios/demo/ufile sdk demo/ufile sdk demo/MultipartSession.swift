//
//  MultipartSession.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/16/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import Foundation

class MultipartSession {
    var uploadId: String
    var blockSize: UInt
    var fileData: NSData
    var key: String
    var fileSize: UInt
    var allParts: UInt
    var ufileSDK: UFileSDK
    var etags: [String]
    
    init(info ufileSDK:UFileSDK, uploadId: String, blockSize: UInt, fileURL: String, key: String) {
        self.uploadId = uploadId
        self.blockSize = blockSize
        self.fileData = NSData(contentsOfFile: fileURL)!
        self.key = key
        let attr = try! NSFileManager.defaultManager().attributesOfItemAtPath(fileURL)
        self.fileSize = UInt((attr[NSFileSize] as! NSNumber).integerValue)
        self.allParts = (self.fileSize+blockSize-1)/(blockSize)
        self.ufileSDK = ufileSDK
        self.etags = [String](count: Int(self.allParts), repeatedValue: "")
    }
    
    func getDataForPart(partNumber: UInt) -> NSData? {
        if partNumber >= self.allParts {
            return nil
        }
        let loc = partNumber * self.blockSize
        var length = self.blockSize
        let end = loc + length
        if end > self.fileSize {
            length = self.fileSize - loc
        }
        return self.fileData.subdataWithRange(NSMakeRange(Int(loc), Int(length)))
    }
    
    func addEtag(partNumber: UInt, etag: String) {
        self.etags[Int(partNumber)] = etag
    }
 
    func outputEtags() -> String {
        return self.etags.joinWithSeparator(",")
    }
}
