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
    private let GIF_RANDOM_MAX_COUNT = 30
    
    private var giphySharedEngine = GiphySharedEngine()
    
    private var timer: Timer = Timer()
    
    private var searchOffset: Int32 = 0
    
    @Published
    var giphyTitle: String
    @Published
    var searchMode: SearchMode
    @Published
    var gifList: [GifUiModel] = []
    @Published
    var autoCompleteList: [GifAutoCompleteUiModel] = []
    @Published
    var autoCompleteListVisibility: ViewVisibility = .gone
    
    private var disposeBag = CFlowDisposebag()
    
    init() {
        self.giphyTitle = GIF_INITIAL_TITLE
        self.searchMode = SearchMode.Debouncing
    }
    
    func initViewModel() {
        getRandomGifList(maxCount: GIF_RANDOM_MAX_COUNT)
    }
    
    func updateTitle(query: String) {
        if (query.isEmpty) {
            giphyTitle = GIF_INITIAL_TITLE
        } else {
            giphyTitle = GIF_SEARCH_PREFIX + query + GIF_SEARCH_POSTFIX
        }
    }
    
    func updateSearchQuery(query: String) {
        timer.invalidate()
        clearPreviousSearchResult()
        searchOffset = 0

        if (query.isEmpty) {
            getRandomGifList(maxCount: GIF_RANDOM_MAX_COUNT)
        } else {
            getGifObjectsFromQuery(query: query)
            getAutoCompleteTerms(query: query)
        }
    }
    
    
    func loadMoreItem(searchOffset: Int32, query: String) {
        self.searchOffset = searchOffset
        updateSearchQuery(query: query)
    }
    
    func swapSearchMode() {
        if (searchMode == SearchMode.Debouncing) {
            searchMode = SearchMode.Throttling
        } else {
            searchMode = SearchMode.Debouncing
        }
    }
    
    func setAutoCompleteVisibility(visible: Bool) {
        if (visible) {
            autoCompleteListVisibility = .visible
        } else {
            autoCompleteListVisibility = .gone
        }
    }
    
    private func getRandomGifList(maxCount: Int) {
        var count = maxCount
        timer = Timer.scheduledTimer(withTimeInterval: 0.2, repeats: true) { timer in
            count = count - 1
            self.getRandomGif(timer: timer, count: count)
        }
    }
    
    private func getRandomGif(timer: Timer, count: Int) {
        (giphySharedEngine.getRandomGif() as! CFlow<GifEntity>)
            .watch { (gifEntity: GifEntity?) in
                if (count == 0) {
                    timer.invalidate()
                    return
                }
                if let entity = gifEntity {
                    self.gifList.append(GifUiModel(title: entity.title, url: entity.url, downsizedUrl: entity.downsizedUrl))
                }
            }.store(in: disposeBag)
    }
    
    private func getGifObjectsFromQuery(query: String) {
        (giphySharedEngine.getGifFromSearchQuery(query: query, offset: searchOffset) as! CFlow<NSArray>)
            .watch { (array: NSArray?) in
                if let gifEntityList = array as? [GifEntity] {
                    self.gifList.append(contentsOf: gifEntityList.map { entity in
                        return GifUiModel(title: entity.title , url: entity.url, downsizedUrl: entity.downsizedUrl)
                    })
                }
            }.store(in: disposeBag)
    }
    
    
    private func getAutoCompleteTerms(query: String) {
        (giphySharedEngine.getAutoCompleteTerms(query: query) as! CFlow<NSArray>)
            .watch { (array: NSArray?) in
                if let entityList = array as? [AutoCompleteEntity] {
                    self.setAutoCompleteVisibility(visible: true)
                    self.autoCompleteList = entityList.map { entity in
                        return GifAutoCompleteUiModel(name: entity.name)
                    }
                }
            }.store(in: disposeBag)
    }
    
    private func clearPreviousSearchResult() {
        gifList.removeAll()
        autoCompleteList.removeAll()
        setAutoCompleteVisibility(visible: false)
        
    }
    
    
    deinit {
        disposeBag.dispose()
    }
}
