package com.example.giphy_kmm

import android.app.Application
import android.content.Context
import com.example.giphy_kmm.di.giphyModule
import com.example.giphy_kmm.domain.entity.AutoCompleteEntity
import com.example.giphy_kmm.domain.entity.GifEntity
import com.example.giphy_kmm.domain.repository.GiphyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.koinApplication
import org.koin.dsl.module

actual class GiphySharedEngine(appContext: Context) {
    private val repository: GiphyRepository

    init {
        val koinApp =
            koinApplication { modules(giphyModule) }.modules(module { single { appContext } })
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