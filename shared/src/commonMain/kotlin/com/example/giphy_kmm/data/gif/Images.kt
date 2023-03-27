package com.example.giphy_kmm.data.gif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    @SerialName("preview_gif")
    val previewGif: PreviewGif
)
