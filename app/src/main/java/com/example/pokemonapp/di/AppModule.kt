package com.example.pokemonapp.di

import com.example.pokemonapp.data.repository.IPokeRepository
import com.example.pokemonapp.data.repository.PokemonRepositoryImpl
import com.example.pokemonapp.ui.viewmodel.PokemonDetailViewModel
import com.example.pokemonapp.ui.viewmodel.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { NetworkModule.pokemonApiService }

    single<IPokeRepository> { PokemonRepositoryImpl(get()) }

    viewModel { PokemonListViewModel(get()) }

    viewModel { PokemonDetailViewModel(get(), get()) }
}
