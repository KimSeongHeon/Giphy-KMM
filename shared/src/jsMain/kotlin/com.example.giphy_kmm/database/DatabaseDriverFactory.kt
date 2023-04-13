package com.example.giphy_kmm.database

import com.example.giphy_kmm.shared.AppDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await

//https://cashapp.github.io/sqldelight/1.5.4/multiplatform_sqlite/
actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return initSqlDriver(AppDB.Schema).await()
    }
}
