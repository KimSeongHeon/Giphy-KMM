package com.example.giphy_kmm.di

import com.example.giphy_kmm.api.GiphyApi
import com.example.giphy_kmm.database.Database
import com.example.giphy_kmm.datasoruce.DataSource
import com.example.giphy_kmm.datasoruce.LocalDataSource
import com.example.giphy_kmm.datasoruce.RemoteDataSource
import com.example.giphy_kmm.domain.repository.GiphyRepository
import com.example.giphy_kmm.repository.GiphyRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val giphyModule = module {
    single<GiphyRepository> {
        GiphyRepositoryImpl(
            get(qualifier = named("remote")),
            get(qualifier = named("local"))
        )
    }
    factory<DataSource>(named("remote")) { RemoteDataSource(get()) }
    factory<DataSource>(named("local")) { LocalDataSource(get()) }
    factory { Database(get()) }
    factory { GiphyApi() }
}

expect val dbDriverModule: Module

