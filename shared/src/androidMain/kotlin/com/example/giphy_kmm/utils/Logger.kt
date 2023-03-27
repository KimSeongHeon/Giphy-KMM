package com.example.giphy_kmm.utils

import org.slf4j.LoggerFactory

actual class Logger {
    actual fun debugLog(tag: String, log: String) {
        val logger = LoggerFactory.getLogger(tag)
        logger.debug(log)
    }

    actual fun infoLog(tag: String, log: String) {
        val logger = LoggerFactory.getLogger(tag)
        logger.info(log)
    }
}
