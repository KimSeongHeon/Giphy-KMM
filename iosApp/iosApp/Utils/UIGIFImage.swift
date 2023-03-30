//
//  UIGIFImage.swift
//  iosApp
//
//  Created by USER on 2023/03/29.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit

class UIGIFImage: UIView {
  private let imageView = UIImageView()
  private var data: Data?
  private var name: String?

  override init(frame: CGRect) {
    super.init(frame: frame)
  }

  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }

  convenience init(name: String) {
    self.init()
    self.name = name
    initView()
  }

  convenience init(data: Data) {
    self.init()
    self.data = data
    initView()
  }

  override func layoutSubviews() {
    super.layoutSubviews()
    imageView.frame = bounds
    self.addSubview(imageView)
  }

  func updateGIF(data: Data) {
    imageView.image = UIImage.gifImage(data: data)
  }

  func updateGIF(name: String) {
    imageView.image = UIImage.gifImage(name: name)
  }

  private func initView() {
    imageView.contentMode = .scaleAspectFit
  }
}
