package com.example.giphy_kmm.data.gif

import kotlinx.serialization.Serializable

@Serializable
data class GifResponse(
    val data: GifObject,
    val meta: Meta
)

