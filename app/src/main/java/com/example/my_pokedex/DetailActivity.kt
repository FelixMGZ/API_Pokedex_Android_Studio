package com.example.my_pokedex

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import com.example.my_pokedex.databinding.ActivityDetailBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var currentPokemonName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarDetail)
        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }
        val pokemonId = intent.getStringExtra("POKEMON_ID")
        if (pokemonId != null) {
            fetchPokemonDetail(pokemonId)
        }
    }

    private fun fetchPokemonDetail(pokemonId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    binding.progressBarDetail.isVisible = true
                }
                val response = PokedexClient.pokedexService.getPokemonDetail(pokemonId)
                withContext(Dispatchers.Main) {
                    binding.progressBarDetail.isVisible = false
                    bindPokemonData(response)
                    fetchEvolutionChain(pokemonId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBarDetail.isVisible = false
                    binding.textViewNameDetail.text = "Error al cargar"
                }
            }
        }
    }

    private fun bindPokemonData(pokemon: PokemonDetailResponse) {
        val imageUrl = pokemon.sprites.officialArtwork?.frontDefault ?: pokemon.sprites.frontDefault
        binding.imageViewPokemonDetail.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.pokeball)
            error(R.drawable.pokeball)
        }
        binding.textViewNameDetail.text = pokemon.name.replaceFirstChar { it.uppercase() }
        currentPokemonName = pokemon.name // Guardamos el nombre
        binding.toolbarDetail.title = "#${pokemon.id.toString().padStart(3, '0')}"
        val heightInMeters = pokemon.height / 10.0
        val weightInKg = pokemon.weight / 10.0
        binding.textViewHeight.text = "Altura: ${heightInMeters}m"
        binding.textViewWeight.text = "Peso: ${weightInKg}kg"
        binding.linearLayoutTypes.removeAllViews()
        pokemon.types.forEach { typeSlot ->
            val chip = Chip(this)
            val typeName = typeSlot.type.name
            chip.text = typeName.replaceFirstChar { it.uppercase() }
            val colorRes = getTypeColor(typeName)
            chip.setChipBackgroundColorResource(colorRes)
            chip.setTextColor(getColor(android.R.color.white))
            binding.linearLayoutTypes.addView(chip)
        }
    }

    private fun fetchEvolutionChain(pokemonId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val speciesResponse = PokedexClient.pokedexService.getPokemonSpecies(pokemonId)
                val evolutionChainUrl = speciesResponse.evolutionChain.url
                val evolutionChainResponse = PokedexClient.pokedexService.getEvolutionChain(evolutionChainUrl)
                val evolutionList = parseEvolutionChain(evolutionChainResponse.chain)
                withContext(Dispatchers.Main) {
                    bindEvolutions(evolutionList)
                    val description = speciesResponse.flavorTextEntries.find { it.language.name == "es" }?.flavorText ?: "Descripción no encontrada."
                    binding.textViewDescription.text = description.replace("\n", " ").replace("\u000c", " ")
                }
            } catch (e: Exception) {
                Log.e("DetailActivity", "Error al cargar la cadena de evolución: ${e.message}")
            }
        }
    }

    private fun parseEvolutionChain(chain: ChainLink): List<PokemonSpecies> {
        val evolutions = mutableListOf<PokemonSpecies>()
        var currentLink: ChainLink? = chain
        while (currentLink != null) {
            evolutions.add(currentLink.species)
            currentLink = currentLink.evolvesTo.firstOrNull()
        }
        return evolutions
    }

    private fun bindEvolutions(evolutions: List<PokemonSpecies>) {
        val filteredEvolutions = evolutions.filter { it.name != currentPokemonName }

        if (filteredEvolutions.isNotEmpty()) {
            binding.textViewEvolutionsTitle.isVisible = true
            binding.linearLayoutEvolutions.isVisible = true
            binding.linearLayoutEvolutions.removeAllViews()
            filteredEvolutions.forEach { species ->
                val imageView = ImageView(this)
                val layoutParams = LinearLayout.LayoutParams(200, 200)
                layoutParams.setMargins(16, 0, 16, 0)
                imageView.layoutParams = layoutParams

                val pokemonId = species.url.split("/").dropLast(1).last()
                val imageUrl =  "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
                imageView.load(imageUrl) {
                    error(R.drawable.pokeball)
                }
                binding.linearLayoutEvolutions.addView(imageView)
            }
        } else {
            binding.textViewEvolutionsTitle.isVisible = false
            binding.linearLayoutEvolutions.isVisible = false
        }
    }

    private fun getTypeColor(type: String): Int {
        return when (type) {
            "normal" -> R.color.type_normal
            "fire" -> R.color.type_fire
            "water" -> R.color.type_water
            "electric" -> R.color.type_electric
            "grass" -> R.color.type_grass
            "ice" -> R.color.type_ice
            "fighting" -> R.color.type_fighting
            "poison" -> R.color.type_poison
            "ground" -> R.color.type_ground
            "flying" -> R.color.type_flying
            "psychic" -> R.color.type_psychic
            "bug" -> R.color.type_bug
            "rock" -> R.color.type_rock
            "ghost" -> R.color.type_ghost
            "dragon" -> R.color.type_dragon
            "dark" -> R.color.type_dark
            "steel" -> R.color.type_steel
            "fairy" -> R.color.type_fairy
            else -> R.color.type_unknown
        }
    }
}