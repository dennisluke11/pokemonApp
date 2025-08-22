package com.example.pokemonapp.data.api

import com.example.pokemonapp.data.PokemonConstants
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = PokemonConstants.POKEMON_LIMIT,
        @Query("offset") offset: Int = PokemonConstants.DEFAULT_OFFSET
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): PokemonDetail

    @GET("pokemon/{name}")
    suspend fun getPokemonDetailByName(@Path("name") name: String): PokemonDetail
}
