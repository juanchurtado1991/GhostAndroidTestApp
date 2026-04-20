package com.ghost.android.test.data

import com.ghost.android.test.domain.RickAndMortyResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import com.hypercubetools.ktor.moshi.moshi // Import from community library

object KtorMoshiClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            moshi(moshi)
        }
    }

    suspend fun getCharacters(page: Int): RickAndMortyResponse {
        return httpClient.get("${BASE_URL}character") {
            parameter("page", page)
        }.body()
    }
}
