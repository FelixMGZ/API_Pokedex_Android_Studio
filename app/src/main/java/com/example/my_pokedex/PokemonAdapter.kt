package com.example.my_pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter // CAMBIO 1: Importamos ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.my_pokedex.databinding.ListItemPokemonBinding

// CAMBIO 2: Heredamos de ListAdapter y le pasamos nuestras reglas de comparación
class PokemonAdapter(
    private val onItemClick: (PokemonResult, ImageView) -> Unit
) : ListAdapter<PokemonResult, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback) {

    inner class PokemonViewHolder(val binding: ListItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: PokemonResult) {
            val pokemonId = pokemon.url.split("/").dropLast(1).last()
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
            binding.textViewPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.textViewPokemonId.text = "#${pokemonId.padStart(3, '0')}"
            binding.imageViewPokemon.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.pokeball)
                error(R.drawable.pokeball)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ListItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
        holder.itemView.setOnClickListener { onItemClick(pokemon, holder.binding.imageViewPokemon) }
    }
}

// Las reglas de comparación que ya creaste. Esto está perfecto.
object PokemonDiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
    override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
        return oldItem == newItem
    }
}