package com.example.giphy_kmm.datasoruce

import com.example.giphy_kmm.data.gif.GifAutoTermsResponse
import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.gif.GifSearchResponse
import com.example.giphy_kmm.data.scrap.ScrapGifModel
import com.example.giphy_kmm.database.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LocalDataSource(private val database: Database): DataSource {
    //캐싱은 나중에..
    override fun getRandomGif(): Flow<GifResponse> = flowOf()

    override fun getGifFromSearchQuery(query: String, offset: Int): Flow<GifSearchResponse> = flowOf()

    override fun getAutoCompleteTerms(query: String): Flow<GifAutoTermsResponse> = flowOf()

    override fun loadScrapGifs(): Flow<List<ScrapGifModel>> = database.loadScrapGifs()


    override fun setScrap(id: String, scrap: Boolean) = database.setScrap(id, scrap)
}
