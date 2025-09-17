package com.example.my_pokedex
data class PokemonSpeciesResponse(
    @com.google.gson.annotations.SerializedName("evolution_chain")
    val evolutionChain: EvolutionChainUrl,
    @com.google.gson.annotations.SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>
)
data class EvolutionChainUrl(
    val url: String
)
data class EvolutionChainResponse(
    val chain: ChainLink
)
data class ChainLink(
    val species: PokemonSpecies,
    @com.google.gson.annotations.SerializedName("evolves_to")
    val evolvesTo: List<ChainLink>
)
data class PokemonSpecies(
    val name: String,
    val url: String
)
data class FlavorTextEntry(
    @com.google.gson.annotations.SerializedName("flavor_text")
    val flavorText: String,
    val language: Language
)
data class Language(
    val name: String
)