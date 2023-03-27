package com.example.giphy_kmm.data.gif

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val msg: String,
    val response_id: String,
    val status: Int
)
