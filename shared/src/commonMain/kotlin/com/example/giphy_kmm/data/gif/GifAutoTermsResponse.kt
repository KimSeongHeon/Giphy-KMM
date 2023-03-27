package com.example.giphy_kmm.data.gif

import kotlinx.serialization.Serializable

@Serializable
data class GifAutoTermsResponse(
    val data: List<AutoCompleteData>,
    val meta: Meta
)
