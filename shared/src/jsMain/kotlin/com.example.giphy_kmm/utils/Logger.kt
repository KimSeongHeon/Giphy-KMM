package com.example.giphy_kmm.utils

actual fun debugLog(tag: String, log: String) {
    console.log("$tag:: $log")
}

actual fun infoLog(tag: String, log: String) {
    console.log("$tag:: $log")
}
