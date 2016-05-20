//
//  MultipartTableController.swift
//  ufile sdk demo
//
//  Created by wu shauk on 12/16/15.
//  Copyright © 2015 ucloud. All rights reserved.
//

import UIKit

class MultipartTableController: UITableViewController {
    
    var session: MultipartSession?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NSLog("%f %f %f %f", self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.width, self.view.frame.height)
    }
    
    func setSession(session: MultipartSession) {
        self.session = session
        self.tableView.reloadData()
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let session = self.session {
            return Int(session.allParts)
        } else {
            return 0
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("multipartcell", forIndexPath: indexPath) as! MultipartTableCell
        cell.setSession(self.session!, partNumber: indexPath.row)
        return cell
    }
    
}
