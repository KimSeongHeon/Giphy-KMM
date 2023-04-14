package com.example.giphy_kmm.database

import com.example.giphy_kmm.data.scrap.ScrapGifModel
import com.example.giphy_kmm.shared.AppDB
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
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
        return dbQuery.loadScrapLists { id, url, downsizedUrl ->
            mapGifs(id, url, downsizedUrl)
        }.asFlow().mapToList()
    }

    internal fun setScrap(model: ScrapGifModel, scrap: Boolean) {
        if (scrap) {
            dbQuery.insertScrapTable(model.id, model.url, model.downSizedUrl)
        } else {
            dbQuery.deleteScrapTable(model.id)
        }
    }

    private fun mapGifs(
        id: String,
        url: String,
        downsizedUrl: String
    ): ScrapGifModel = ScrapGifModel(id, url, downsizedUrl)
}
