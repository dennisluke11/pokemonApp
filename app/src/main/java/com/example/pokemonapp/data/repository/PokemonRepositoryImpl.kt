package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.api.PokemonApiService
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListItem

class PokemonRepositoryImpl(
    private val apiService: PokemonApiService
) : IPokeRepository {
    
    // Gets paginated Pokemon list with error handling returning empty list on failure
    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        return try {
            val response = apiService.getPokemonList(limit, offset)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Gets Pokemon detail by ID with error handling
    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return try {
            apiService.getPokemonDetail(id)
        } catch (e: Exception) {
            throw e
        }
    }
    
    // Gets Pokemon detail by name with error handling
    override suspend fun getPokemonDetailByName(name: String): PokemonDetail {
        return try {
            apiService.getPokemonDetailByName(name)
        } catch (e: Exception) {
            throw e
        }
    }
}
