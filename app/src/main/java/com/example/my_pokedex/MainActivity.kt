package com.example.my_pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import com.example.my_pokedex.databinding.ActivityMainBinding
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fullPokemonList = mutableListOf<PokemonResult>()
    private lateinit var pokemonAdapter: PokemonAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    binding.progressBar.isVisible = true
                }
                val response = PokedexClient.pokedexService.getPokemonList(151)
                withContext(Dispatchers.Main) {
                    binding.progressBar.isVisible = false
                    setupRecyclerView(response.results)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun setupRecyclerView(pokemonList: List<PokemonResult>) {
        fullPokemonList.clear()
        fullPokemonList.addAll(pokemonList)
        pokemonAdapter = PokemonAdapter { pokemonClicked, pokemonImage ->
            // --- INICIO DE LA LÓGICA DE ANIMACIÓN ---
            val pokemonId = pokemonClicked.url.split("/").dropLast(1).last()
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("POKEMON_ID", pokemonId)

            // 1. Creamos las opciones de la transición
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                pokemonImage, // La vista que se va a animar
                "pokemon_image_transition" // El nombre de la etiqueta que pusimos en el XML
            )
            startActivity(intent, options.toBundle())
        }
        binding.recyclerViewPokemon.adapter = pokemonAdapter
        binding.recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        pokemonAdapter.submitList(fullPokemonList)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText ?: ""
                val filteredList = fullPokemonList.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true)
                }
                pokemonAdapter.submitList(filteredList)
                return true
            }
        })
        return true
    }
}