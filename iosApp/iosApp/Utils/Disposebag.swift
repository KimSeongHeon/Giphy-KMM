//
//  Disposebag.swift
//  iosApp
//
//  Created by USER on 2023/03/29.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Combine
import shared

class CombineDisposeBag {
    private var subscriptions: Set<AnyCancellable> = []
    
    func add(subscription: AnyCancellable) {
        self.subscriptions.insert(subscription)
    }
    
    func dispose() {
        self.subscriptions.forEach { $0.cancel() }
        self.subscriptions = []
    }
}

extension AnyCancellable {
    func store(in disposeBag: CombineDisposeBag) {
        disposeBag.add(subscription: self)
    }
}

class CFlowDisposebag {
    private var closeables = [CFlowCloseable]()
    
    func add(closeable: CFlowCloseable) {
        self.closeables.append(closeable)
    }
    
    func dispose() {
        self.closeables.forEach { $0.close() }
        self.closeables = []
    }
}

extension CFlowCloseable {
    func store(in disposeBag: CFlowDisposebag) {
        disposeBag.add(closeable: self)
    }
}
