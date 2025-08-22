package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.PokemonConstants
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListItem

interface IPokeRepository {

    suspend fun getPokemonList(
        limit: Int = PokemonConstants.POKEMON_LIMIT,
        offset: Int = PokemonConstants.DEFAULT_OFFSET
    ): List<PokemonListItem>

    suspend fun getPokemonDetail(id: Int): PokemonDetail
    suspend fun getPokemonDetailByName(name: String): PokemonDetail
}
