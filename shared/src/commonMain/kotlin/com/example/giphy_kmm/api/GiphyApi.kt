package com.example.giphy_kmm.api

import com.example.giphy_kmm.BuildKonfig
import com.example.giphy_kmm.data.gif.GifAutoTermsResponse
import com.example.giphy_kmm.data.gif.GifResponse
import com.example.giphy_kmm.data.gif.GifSearchResponse
import com.example.giphy_kmm.utils.debugLog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GiphyApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getRandomGif(): GifResponse {
        val response = httpClient.get {
            url("${BASE_URL}/random")
            parameter("api_key", BuildKonfig.GIPHY_API_KEY)
        }

        return response.body()
    }

    suspend fun getGifFromSearchQuery(
        query: String,
        offset: Int? = 0,
        limit: Int? = 25,
        rating: String? = "",
        randomId: String? = ""
    ): GifSearchResponse {
        val response = httpClient.get {
            url("${BASE_URL}/search")
            parameter("api_key", BuildKonfig.GIPHY_API_KEY)
            parameter("q", query)
            parameter("limit", limit)
            parameter("offset", offset)

            if (!rating.isNullOrBlank())  {
                parameter("rating", rating)
            }

            if (!randomId.isNullOrBlank()) {
                parameter("random_id", randomId)
            }
        }

        return response.body()
    }

    suspend fun getAutoCompleteTerms(
        query: String,
        limit: Int? = 20,
        offset: Int? = 0,
    ): GifAutoTermsResponse {
        val response = httpClient.get {
            url("${BASE_URL}/search/tags")
            parameter("api_key", BuildKonfig.GIPHY_API_KEY)
            parameter("q", query)
            parameter("limit", limit)
            parameter("offset", offset)
        }

        return response.body()
    }

    companion object {
        private const val BASE_URL = "https://api.giphy.com/v1/gifs"
    }
}
