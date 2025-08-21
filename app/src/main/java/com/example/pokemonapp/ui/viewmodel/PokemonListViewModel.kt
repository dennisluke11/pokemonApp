package com.example.pokemonapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.model.PokemonListItem
import com.example.pokemonapp.data.repository.IPokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: IPokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private var cachedPokemonList: List<PokemonListItem>? = null

    init {
        loadPokemonList()
    }

    // Loads Pokemon list from repository with caching
    fun loadPokemonList() {
        if (cachedPokemonList != null) {
            _uiState.value = _uiState.value.copy(
                pokemonList = cachedPokemonList!!,
                filteredList = cachedPokemonList!!,
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val pokemonList = repository.getPokemonList()
                cachedPokemonList = pokemonList
                _uiState.value = _uiState.value.copy(
                    pokemonList = pokemonList,
                    filteredList = pokemonList,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun searchPokemon(query: String) {
        val filteredList = if (query.isEmpty()) {
            _uiState.value.pokemonList
        } else {
            _uiState.value.pokemonList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredList = filteredList
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Refreshes Pokemon list by clearing cache and reloading
    fun refreshData() {
        cachedPokemonList = null
        loadPokemonList()
    }
}

data class PokemonListUiState(
    val pokemonList: List<PokemonListItem> = emptyList(),
    val filteredList: List<PokemonListItem> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
