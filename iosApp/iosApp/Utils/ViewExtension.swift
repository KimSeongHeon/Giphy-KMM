//
//  ViewExtension.swift
//  iosApp
//
//  Created by USER on 2023/03/29.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

enum ViewVisibility: CaseIterable {
  case visible, // view is fully visible
       invisible, // view is hidden but takes up space
       gone // view is fully removed from the view hierarchy
}

extension View {
    @ViewBuilder func visibility(_ visibility: ViewVisibility) -> some View {
        if visibility != .gone {
            if visibility == .visible {
                self
            } else {
                hidden()
            }
        }
    }
}
