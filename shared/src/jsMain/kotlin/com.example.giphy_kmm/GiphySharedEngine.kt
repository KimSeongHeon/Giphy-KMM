package com.example.giphy_kmm

import com.example.giphy_kmm.di.giphyModule
import com.example.giphy_kmm.domain.entity.AutoCompleteEntity
import com.example.giphy_kmm.domain.entity.GifEntity
import com.example.giphy_kmm.domain.repository.GiphyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.koinApplication

actual class GiphySharedEngine {
    private val repository: GiphyRepository

    init {
        val koinApp = koinApplication { modules(giphyModule) }
        repository = koinApp.koin.get()
    }

    actual fun getRandomGif(): Flow<GifEntity> = repository.getRandomGif()

    actual fun getGifFromSearchQuery(
        query: String,
        offset: Int
    ): Flow<List<GifEntity>> = repository.getGifFromSearchQuery(query, offset)

    actual fun getAutoCompleteTerms(query: String): Flow<List<AutoCompleteEntity>> =
        repository.getAutoCompleteTerms(query)
}
