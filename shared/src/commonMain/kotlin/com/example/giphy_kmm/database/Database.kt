package com.example.giphy_kmm.database

import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.scrap.ScrapGifModel
import com.example.giphy_kmm.shared.AppDB
import comexamplegiphykmm.shared.AppDBQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Database(private val databaseDriverFactory: DatabaseDriverFactory) {
    private lateinit var database: AppDB
    private val dbQuery: AppDBQueries
        get() = database.appDBQueries

    init {
        CoroutineScope(Dispatchers.Main).launch {
            database = AppDB(databaseDriverFactory.createDriver())
        }
    }

    internal fun loadScrapGifs(): Flow<List<ScrapGifModel>> {
        dbQuery.
    }

    internal fun setScrap(id: String, scrap: Boolean) {

    }

}
