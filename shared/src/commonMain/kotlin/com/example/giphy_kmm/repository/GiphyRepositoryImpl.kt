package com.example.giphy_kmm.repository

import com.example.giphy_kmm.datasoruce.DataSource
import com.example.giphy_kmm.domain.entity.AutoCompleteEntity
import com.example.giphy_kmm.domain.entity.GifEntity
import com.example.giphy_kmm.domain.repository.GiphyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class GiphyRepositoryImpl(
    private val remoteDataSource: DataSource
): GiphyRepository {
    override fun getRandomGif(): Flow<GifEntity> {
        return remoteDataSource.getRandomGif()
            .flatMapLatest { response ->
                flowOf(GifEntity(response.data.title, response.data.url, response.data.images.downsized.url))
        }
    }

    override fun getGifFromSearchQuery(query: String, offset: Int): Flow<List<GifEntity>> {
        return remoteDataSource.getGifFromSearchQuery(query, offset = offset)
            .flatMapLatest { response ->
                flowOf(response.data.map { GifEntity(it.title, it.url, it.images.downsized.url) })
            }
    }

    override fun getAutoCompleteTerms(query: String): Flow<List<AutoCompleteEntity>> {
        return remoteDataSource.getAutoCompleteTerms(query)
            .flatMapLatest { response ->
                flowOf(response.data.map { AutoCompleteEntity(it.name) })
            }
    }
}
