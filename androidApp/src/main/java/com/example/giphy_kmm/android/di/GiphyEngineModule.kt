package com.example.giphy_kmm.android.di

import android.content.Context
import com.example.giphy_kmm.GiphySharedEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GiphyEngineModule {
    @Provides
    @Singleton
    fun provideGiphySharedEngine(@ApplicationContext context: Context): GiphySharedEngine {
        return GiphySharedEngine(context)
    }
}
