package com.example.giphy_kmm.android.compose

import androidx.annotation.DrawableRes
import com.example.giphy_kmm.android.R

sealed class BottomNavItem(
    @DrawableRes val icon: Int,
    val screenRoute: String
) {
    object Grid : BottomNavItem(R.drawable.baseline_downloading_24, GRID)
    object Scrap : BottomNavItem(R.drawable.baseline_place_24, SCRAP)

    companion object {
        const val GRID = "GRID"
        const val SCRAP = "SCRAP"
    }
}
