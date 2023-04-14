package com.example.giphy_kmm.domain.repository

import com.example.giphy_kmm.domain.entity.AutoCompleteEntity
import com.example.giphy_kmm.domain.entity.GifEntity
import kotlinx.coroutines.flow.Flow

interface GiphyRepository {
    fun getRandomGif(): Flow<GifEntity>

    fun getGifFromSearchQuery(query: String, offset: Int): Flow<List<GifEntity>>

    fun getAutoCompleteTerms(query: String): Flow<List<AutoCompleteEntity>>

    fun getScrapGifs(): Flow<List<GifEntity>>

    fun addScrap(gifEntity: GifEntity)

    fun removeScrap(gifEntity: GifEntity)
}
