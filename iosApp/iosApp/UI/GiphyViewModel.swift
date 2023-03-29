//
//  GiphyViewModel.swift
//  iosApp
//
//  Created by USER on 2023/03/28.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Combine
import shared

public class GiphyViewModel: ObservableObject {
    private let GIF_INITIAL_TITLE = "Giphy Random GIFs"
    private let GIF_SEARCH_PREFIX = "Search "
    private let GIF_SEARCH_POSTFIX = " ..."
    
    private var giphySharedEngine = GiphySharedEngine()
    
    @Published
    var giphyTitle: String
    @Published
    var searchMode: SearchMode
    @Published
    var gifList: [GifUiModel] = []
    
    private var disposeBag = CFlowDisposebag()

    init() {
        self.giphyTitle = GIF_INITIAL_TITLE
        self.searchMode = SearchMode.Debouncing
    }
    
    func updateTitle(query: String) {
        if (query.isEmpty) {
            giphyTitle = GIF_INITIAL_TITLE
        } else {
            giphyTitle = GIF_SEARCH_PREFIX + query + GIF_SEARCH_POSTFIX
        }
    }
    
    func updateSearchQuery(query: String) {
        (giphySharedEngine.getGifFromSearchQuery(query: query, offset: 0) as! CFlow<NSArray>)
            .watch { (array: NSArray?) in
                if let gifEntityList = array as? [GifEntity] {
                    self.gifList = gifEntityList.map { entity in
                        NSLog(entity.title + entity.url + entity.downsizedUrl)
                        return GifUiModel(title: entity.title , url: entity.url, downsizedUrl: entity.downsizedUrl)
                    }
                }
            }.store(in: disposeBag)
    }
    
    func loadMoreItem(searchOffset: Int) {
        
    }
    
    func swapSearchMode() {
        if (searchMode == SearchMode.Debouncing) {
            searchMode = SearchMode.Throttling
        } else {
            searchMode = SearchMode.Debouncing
        }
    }
    
    deinit {
        disposeBag.dispose()
    }
}
