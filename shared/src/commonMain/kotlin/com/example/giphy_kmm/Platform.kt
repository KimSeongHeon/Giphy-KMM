package com.example.giphy_kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform