package com.example.pokemonapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.model.PokemonListItem
import com.example.pokemonapp.data.repository.IPokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Success(
        val all: List<PokemonListItem>,
        val filtered: List<PokemonListItem>,
        val query: String
    ) : PokemonListUiState

    data class Error(val message: String) : PokemonListUiState
    data object Empty : PokemonListUiState
}

class PokemonListViewModel(
    private val repository: IPokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private var cachedPokemonList: List<PokemonListItem>? = null

    init {
        loadPokemonList()
    }

    // Loads Pokemon list from repository with caching
    fun loadPokemonList() {
        cachedPokemonList?.let { cachedList ->
            _uiState.value = PokemonListUiState.Success(
                all = cachedList,
                filtered = cachedList,
                query = ""
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Loading
            try {
                val pokemonList = repository.getPokemonList()
                cachedPokemonList = pokemonList
                if (pokemonList.isEmpty()) {
                    _uiState.value = PokemonListUiState.Empty
                } else {
                    _uiState.value = PokemonListUiState.Success(
                        all = pokemonList,
                        filtered = pokemonList,
                        query = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = PokemonListUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun searchPokemon(query: String) {
        val currentState = _uiState.value
        if (currentState is PokemonListUiState.Success) {
            val filteredList = if (query.isEmpty()) {
                currentState.all
            } else {
                currentState.all.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
            _uiState.value = PokemonListUiState.Success(
                all = currentState.all,
                filtered = filteredList,
                query = query
            )
        }
    }

    fun clearError() {
        loadPokemonList()
    }

    // Refreshes Pokemon list by clearing cache and reloading
    fun refreshData() {
        cachedPokemonList = null
        loadPokemonList()
    }
}
