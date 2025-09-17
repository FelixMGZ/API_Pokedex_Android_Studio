package com.example.my_pokedex

// Esta clase representa un solo Pokémon DENTRO de la lista
// Fíjate que solo tiene 'name' y 'url', como en la respuesta JSON
data class PokemonResult(
    val name: String,
    val url: String
)

// Esta clase representa la respuesta COMPLETA
// Contiene una lista de los objetos que definimos arriba
data class PokemonListResponse(
    val results: List<PokemonResult>
)
