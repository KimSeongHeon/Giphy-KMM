package com.example.giphy_kmm.datasoruce

import com.example.giphy_kmm.api.GiphyApi
import com.example.giphy_kmm.data.gif.GifAutoTermsResponse
import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.gif.GifSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSource(
    private val giphyApi: GiphyApi
) : DataSource {
    override fun getRandomGif(): Flow<GifResponse> {
        return flow {
            val response = giphyApi.getRandomGif()
            emit(response)
        }
    }

    override fun getGifFromSearchQuery(query: String, offset: Int): Flow<GifSearchResponse> {
        return flow {
            val searchResponse = giphyApi.getGifFromSearchQuery(query, offset)
            emit(searchResponse)
        }
    }

    override fun getAutoCompleteTerms(query: String): Flow<GifAutoTermsResponse> {
        return flow {
            val autoTermsResponse = giphyApi.getAutoCompleteTerms(query)
            emit(autoTermsResponse)
        }
    }

    override fun loadScrapGifs(): Flow<GifResponse> = flowOf()

    override fun setScrap(id: String, scrap: Boolean) = Unit
}
