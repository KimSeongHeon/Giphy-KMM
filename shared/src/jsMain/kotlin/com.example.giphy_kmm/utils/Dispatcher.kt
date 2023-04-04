package com.example.giphy_kmm.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun dispatcher(): CoroutineDispatcher = Dispatchers.Main

