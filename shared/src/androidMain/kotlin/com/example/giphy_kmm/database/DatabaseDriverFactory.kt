package com.example.giphy_kmm.database

import android.content.Context
import com.example.giphy_kmm.shared.AppDB
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(AppDB.Schema, context, "app.db")
}
