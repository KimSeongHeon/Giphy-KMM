package com.example.giphy_kmm.data.gif

import com.example.giphy_kmm.data.Pagination
import kotlinx.serialization.Serializable

@Serializable
data class GifSearchResponse(
    val data: List<GifObject>,
    val pagination: Pagination,
    val meta: Meta
)
