package com.example.my_pokedex

import retrofit2.http.Url

data class Pokemon (
    val id: Int,
    val name: String,
    val sprite: Url
)

data class AdviceResponse(
    val pokemon: Pokemon
)
