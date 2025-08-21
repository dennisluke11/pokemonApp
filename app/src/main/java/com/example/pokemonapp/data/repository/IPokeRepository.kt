package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListItem

interface IPokeRepository {
    // Fetches paginated list of Pokemon from API
    suspend fun getPokemonList(limit: Int = 100, offset: Int = 0): List<PokemonListItem>
    // Fetches detailed Pokemon information by ID
    suspend fun getPokemonDetail(id: Int): PokemonDetail
    // Fetches detailed Pokemon information by name
    suspend fun getPokemonDetailByName(name: String): PokemonDetail
}
