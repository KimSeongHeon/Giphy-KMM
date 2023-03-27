package com.example.giphy_kmm.utils

import android.util.Log
import org.slf4j.LoggerFactory

actual fun debugLog(tag: String, log: String) {
    Log.d(tag, log)
}

actual fun infoLog(tag: String, log: String) {
    Log.i(tag, log)
}
