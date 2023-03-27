package com.example.giphy_kmm.data

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val total_count: Int?,
    val count: Int?,
    val offset: Int?
)
