package com.example.my_pokedex

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokedexService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") pokemonId: String
    ): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") pokemonId: String): PokemonSpeciesResponse

    @GET
    suspend fun getEvolutionChain(@Url url: String): EvolutionChainResponse
}