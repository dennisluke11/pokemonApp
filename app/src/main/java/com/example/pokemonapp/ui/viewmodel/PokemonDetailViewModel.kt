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

class PokemonDetailViewModel(
    private val repository: IPokeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val pokemonId: Int = savedStateHandle["pokemonId"] ?: 1
    
    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()
    
    private var cachedPokemonDetail: PokemonDetail? = null
    
    init {
        loadPokemonDetail()
    }
    
    // Loads Pokemon detail from repository with caching
    fun loadPokemonDetail() {
        if (cachedPokemonDetail != null) {
            _uiState.value = _uiState.value.copy(
                pokemonDetail = cachedPokemonDetail,
                isLoading = false
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val pokemonDetail = repository.getPokemonDetail(pokemonId)
                cachedPokemonDetail = pokemonDetail
                _uiState.value = _uiState.value.copy(
                    pokemonDetail = pokemonDetail,
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
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun refreshData() {
        cachedPokemonDetail = null
        loadPokemonDetail()
    }
}

data class PokemonDetailUiState(
    val pokemonDetail: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
