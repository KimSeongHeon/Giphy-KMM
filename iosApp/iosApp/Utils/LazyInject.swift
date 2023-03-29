//
//  LazyInject.swift
//  iosApp
//
//  Created by USER on 2023/03/29.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit

@propertyWrapper
struct LazyInject<T> {
    private lazy var value: T = {
        let appDelegate = iOSApp().shared.delegate as! AppDelegate
        return appDelegate.container.resolve(T.self)!
    }()
    
    var wrappedValue: T {
        mutating get { return value }
    }
    
}
