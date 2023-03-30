//
//  GIFImage.swift
//  iosApp
//
//  Created by USER on 2023/03/29.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

//
//  UIGifImage.swift
//  iosApp
//
//  Created by USER on 2023/03/28.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import WebKit

struct GIFImage: UIViewRepresentable {
    private let data: Data?
    private let name: String?
    
    init(data: Data) {
        self.data = data
        self.name = nil
    }
    
    public init(name: String) {
        self.data = nil
        self.name = name
    }
    
    func makeUIView(context: Context) -> UIGIFImage {
        if let data = data {
            return UIGIFImage(data: data)
        } else {
            return UIGIFImage(name: name ?? "")
        }
    }
    
    func updateUIView(_ uiView: UIGIFImage, context: Context) {
        if let data = data {
            uiView.updateGIF(data: data)
        } else {
            uiView.updateGIF(name: name ?? "")
        }
    }
}
