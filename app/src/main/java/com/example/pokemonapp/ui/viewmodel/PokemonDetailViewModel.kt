package com.example.pokemonapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.repository.IPokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val detail: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

class PokemonDetailViewModel(
    private val repository: IPokeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = savedStateHandle["pokemonId"] ?: 1

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private var cachedPokemonDetail: PokemonDetail? = null

    init {
        loadPokemonDetail()
    }

    // Loads Pokemon detail from repository with caching
    fun loadPokemonDetail() {
        cachedPokemonDetail?.let { cachedDetail ->
            _uiState.value = PokemonDetailUiState.Success(cachedDetail)
            return
        }

        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val pokemonDetail = repository.getPokemonDetail(pokemonId)
                cachedPokemonDetail = pokemonDetail
                _uiState.value = PokemonDetailUiState.Success(pokemonDetail)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun clearError() {
        loadPokemonDetail()
    }

    fun refreshData() {
        cachedPokemonDetail = null
        loadPokemonDetail()
    }
}
