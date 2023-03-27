package com.example.giphy_kmm.di

import com.example.giphy_kmm.api.GiphyApi
import com.example.giphy_kmm.datasoruce.RemoteDataSource
import com.example.giphy_kmm.domain.repository.GiphyRepository
import com.example.giphy_kmm.repository.GiphyRepositoryImpl
import org.koin.dsl.module

val giphyModule = module {
    single<GiphyRepository> { GiphyRepositoryImpl(get()) }
    factory { RemoteDataSource(get()) }
    factory { GiphyApi() }
}
