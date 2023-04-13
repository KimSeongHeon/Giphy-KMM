package com.example.giphy_kmm.di

import com.example.giphy_kmm.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dbDriverModule: Module
    get() = module {
        factory{ DatabaseDriverFactory() }
    }
