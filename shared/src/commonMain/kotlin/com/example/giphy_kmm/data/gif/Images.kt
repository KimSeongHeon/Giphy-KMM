package com.example.giphy_kmm.data.gif

import kotlinx.serialization.Serializable

@Serializable
data class Images(
    val downsized: Downsized,
)
