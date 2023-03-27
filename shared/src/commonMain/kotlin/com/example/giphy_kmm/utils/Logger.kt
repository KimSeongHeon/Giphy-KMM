package com.example.giphy_kmm.utils

expect class Logger {
    fun debugLog(tag: String, log: String)

    fun infoLog(tag: String, log: String)
}
