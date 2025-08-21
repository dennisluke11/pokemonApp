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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    onPokemonClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    val filteredList by remember(uiState.pokemonList, searchQuery) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                uiState.pokemonList
            } else {
                uiState.pokemonList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

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

            when {
                uiState.isLoading && uiState.pokemonList.isEmpty() -> {
                    LoadingIndicator()
                }

                uiState.error != null -> {
                    ErrorMessage(
                        onDismiss = { viewModel.clearError() }
                    )
                }

                else -> {
                    PokemonList(
                        pokemonList = filteredList,
                        listState = listState,
                        onPokemonClick = { pokemon ->
                            val id = pokemon.url.split("/").dropLast(1).last().toIntOrNull()
                            id?.let { onPokemonClick(it) }
                        }
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
    onPokemonClick: (PokemonListItem) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon) }
            )
        }
    }
}






