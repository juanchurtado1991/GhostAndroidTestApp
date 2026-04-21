package com.ghost.android.test.data

import com.ghost.android.test.domain.RickAndMortyResponse
import com.ghost.serialization.benchmark.GhostModuleRegistry_app
import com.ghost.serialization.retrofit.GhostConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyGhostService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): RickAndMortyResponse
}

object GhostRetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    private val registry = GhostModuleRegistry_app.INSTANCE

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GhostConverterFactory.create(registry))
        .build()

    val service: RickAndMortyGhostService = retrofit.create(RickAndMortyGhostService::class.java)
}
