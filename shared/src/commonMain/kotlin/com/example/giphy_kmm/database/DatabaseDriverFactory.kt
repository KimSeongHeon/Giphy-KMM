package com.example.giphy_kmm.database

import com.squareup.sqldelight.db.SqlDriver

//https://cashapp.github.io/sqldelight/1.5.4/multiplatform_sqlite/
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
