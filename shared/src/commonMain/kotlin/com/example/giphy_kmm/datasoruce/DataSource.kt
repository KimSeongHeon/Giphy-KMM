package com.example.giphy_kmm.datasoruce

import com.example.giphy_kmm.data.gif.GifAutoTermsResponse
import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.gif.GifSearchResponse
import com.example.giphy_kmm.data.scrap.ScrapGifModel
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getRandomGif(): Flow<GifResponse>

    fun getGifFromSearchQuery(query: String, offset: Int): Flow<GifSearchResponse>

    fun getAutoCompleteTerms(query: String): Flow<GifAutoTermsResponse>

    fun loadScrapGifs(): Flow<List<ScrapGifModel>>

    fun setScrap(model: ScrapGifModel, scrap: Boolean)
}
