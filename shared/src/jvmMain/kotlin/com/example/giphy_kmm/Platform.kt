package com.example.giphy_kmm

class MacOSPlatform(override val name: String = "macOS"): Platform

actual fun getPlatform(): Platform = MacOSPlatform()
