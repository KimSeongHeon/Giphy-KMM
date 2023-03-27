package com.example.giphy_kmm.utils

import platform.Foundation.NSLog

actual class Logger {
    actual fun debugLog(tag: String, log: String) {
        NSLog("$tag:: $log")
    }

    actual fun infoLog(tag: String, log: String) {
        NSLog("$tag:: $log")
    }
}
