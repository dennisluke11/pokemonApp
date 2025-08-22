@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pokemonapp.data.model.PokemonListItem
import com.example.pokemonapp.ui.components.ErrorMessage
import com.example.pokemonapp.ui.components.LoadingIndicator
import com.example.pokemonapp.ui.components.PokemonAppBar
import com.example.pokemonapp.ui.components.PokemonCard
import com.example.pokemonapp.ui.theme.Dimens
import com.example.pokemonapp.ui.viewmodel.PokemonListViewModel
import com.example.pokemonapp.ui.viewmodel.PokemonListUiState


@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    onPokemonClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            PokemonAppBar(title = "PokéDex Pro")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.searchPokemon(it)
                }
            )

            when (val currentState = uiState) {
                is PokemonListUiState.Loading -> {
                    LoadingIndicator()
                }

                is PokemonListUiState.Error -> {
                    ErrorMessage(
                        message = currentState.message,
                        onDismiss = { viewModel.clearError() }
                    )
                }

                is PokemonListUiState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Pokémon found",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                is PokemonListUiState.Success -> {
                    val displayList = if (searchQuery.isEmpty()) {
                        currentState.all
                    } else {
                        currentState.filtered
                    }

                    PokemonList(
                        pokemonList = displayList,
                        listState = listState,
                        onPokemonClick = onPokemonClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingMedium, vertical = Dimens.paddingSmall),
        placeholder = { Text("Search Pokémon...") },
        label = { Text("Search") },
        singleLine = true,
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
    )
}

@Composable
private fun PokemonList(
    pokemonList: List<PokemonListItem>,
    listState: LazyListState,
    onPokemonClick: (Int) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
    ) {
        items(
            items = pokemonList,
            key = { pokemon ->
                pokemon.id
            }
        ) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) }
            )
        }
    }
}






