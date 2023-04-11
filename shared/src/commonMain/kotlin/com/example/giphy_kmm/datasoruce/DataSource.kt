package com.example.giphy_kmm.datasoruce

import com.example.giphy_kmm.data.gif.GifAutoTermsResponse
import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.gif.GifSearchResponse
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getRandomGif(): Flow<GifResponse>

    fun getGifFromSearchQuery(query: String, offset: Int): Flow<GifSearchResponse>

    fun getAutoCompleteTerms(query: String): Flow<GifAutoTermsResponse>

    fun loadScrapGifs(): Flow<GifResponse>

    fun setScrap(id: String, scrap: Boolean)
}
