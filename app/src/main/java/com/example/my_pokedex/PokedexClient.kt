package com.example.my_pokedex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokedexClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val pokedexService: PokedexService by lazy {
        retrofit.create(PokedexService::class.java)
    }
}