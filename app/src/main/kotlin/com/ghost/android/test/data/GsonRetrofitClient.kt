package com.ghost.android.test.data

import com.ghost.android.test.domain.RickAndMortyResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyGsonService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): RickAndMortyResponse
}

object GsonRetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val gson = GsonBuilder().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service: RickAndMortyGsonService = retrofit
        .create(RickAndMortyGsonService::class.java)
}
