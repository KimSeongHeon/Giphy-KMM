package com.example.giphy_kmm

class JsPlatform(override val name: String = "js") : Platform

actual fun getPlatform(): Platform = JsPlatform()


