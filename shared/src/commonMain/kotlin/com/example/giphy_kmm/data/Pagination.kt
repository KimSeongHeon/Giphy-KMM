package com.example.giphy_kmm.data

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName(value = "total_size")
    val totalSize: Int,
    @SerializedName(value = "count")
    val count: Int,
    @SerializedName(value = "offset")
    val offset: Int
)
