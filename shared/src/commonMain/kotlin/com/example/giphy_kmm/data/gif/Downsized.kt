package com.example.giphy_kmm.data.gif

import kotlinx.serialization.Serializable

@Serializable
data class Downsized(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)
