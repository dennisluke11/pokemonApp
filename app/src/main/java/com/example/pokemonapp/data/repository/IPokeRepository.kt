package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListItem

interface IPokeRepository {

    suspend fun getPokemonList(limit: Int = 100, offset: Int = 0): List<PokemonListItem>
    suspend fun getPokemonDetail(id: Int): PokemonDetail
    suspend fun getPokemonDetailByName(name: String): PokemonDetail
}
