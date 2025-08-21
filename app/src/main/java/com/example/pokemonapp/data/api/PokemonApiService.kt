package com.example.pokemonapp.data.api

import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    // Fetches paginated Pokemon list from PokeAPI
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse
    
    // Fetches Pokemon details by numeric ID
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): PokemonDetail
    
    // Fetches Pokemon details by name string
    @GET("pokemon/{name}")
    suspend fun getPokemonDetailByName(@Path("name") name: String): PokemonDetail
}
