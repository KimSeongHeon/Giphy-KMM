package com.example.giphy_kmm

import com.example.giphy_kmm.di.dbDriverModule
import com.example.giphy_kmm.di.giphyModule
import com.example.giphy_kmm.domain.entity.AutoCompleteEntity
import com.example.giphy_kmm.domain.entity.GifEntity
import com.example.giphy_kmm.domain.repository.GiphyRepository
import com.example.giphy_kmm.utils.CFlow
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.koinApplication

actual class GiphySharedEngine {
    private val repository: GiphyRepository

    init {
        val koinApp =
            koinApplication { modules(giphyModule, dbDriverModule) }

        repository = koinApp.koin.get()
    }

    actual fun getRandomGif(): Flow<GifEntity> = CFlow(repository.getRandomGif())

    actual fun getGifFromSearchQuery(
        query: String,
        offset: Int
    ): Flow<List<GifEntity>> = CFlow(repository.getGifFromSearchQuery(query, offset))

    actual fun getAutoCompleteTerms(query: String): Flow<List<AutoCompleteEntity>> =
        CFlow(repository.getAutoCompleteTerms(query))
}
