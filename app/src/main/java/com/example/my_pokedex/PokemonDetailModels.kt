package com.example.my_pokedex

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int, // La API lo da en decímetros
    val weight: Int, // La API lo da en hectogramos
    val sprites: PokemonSprites,
    val types: List<PokemonTypeSlot>
)

// --- Planos para las partes anidadas ---

data class PokemonSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?,

    // NUEVA LÍNEA: La imagen clásica (nuestro Plan B)
    @SerializedName("front_default")
    val frontDefault: String?
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?  // Le añadimos '?' para indicar que puede ser nulo
)

data class PokemonTypeSlot(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)