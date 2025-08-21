@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.ui.components.ErrorMessage
import com.example.pokemonapp.ui.components.HiddenAbilityChip
import com.example.pokemonapp.ui.components.InfoCard
import com.example.pokemonapp.ui.components.InfoItem
import com.example.pokemonapp.ui.components.LoadingIndicator
import com.example.pokemonapp.ui.components.PokemonAppBar
import com.example.pokemonapp.ui.components.PokemonImage
import com.example.pokemonapp.ui.components.SectionTitle
import com.example.pokemonapp.ui.components.StatProgressBar
import com.example.pokemonapp.ui.components.TypeChip
import com.example.pokemonapp.ui.theme.Dimens
import com.example.pokemonapp.ui.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PokemonAppBar(
                title = uiState.pokemonDetail?.name?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                } ?: "Pokémon Details",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.error != null -> {
                ErrorMessage(
                    onDismiss = { viewModel.clearError() },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.pokemonDetail != null -> {
                PokemonDetailContent(
                    pokemon = uiState.pokemonDetail!!,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(
    pokemon: PokemonDetail,
    paddingValues: PaddingValues
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(Dimens.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
    ) {
        item {
            PokemonHeader(pokemon = pokemon)
        }

        item {
            val imageUrl = pokemon.sprites.other?.official_artwork?.front_default
                ?: pokemon.sprites.front_default
                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"

            val fallbackUrl = pokemon.sprites.front_default
                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png"

            PokemonImage(
                imageUrl = imageUrl,
                contentDescription = pokemon.name,
                fallbackUrl = fallbackUrl
            )
        }

        item {
            PokemonBasicInfo(pokemon = pokemon)
        }

        item {
            PokemonStats(pokemon = pokemon)
        }

        item {
            PokemonTypesAndAbilities(pokemon = pokemon)
        }
    }
}

@Composable
private fun PokemonHeader(pokemon: PokemonDetail) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "#${pokemon.id}",
            fontSize = Dimens.textSizeMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            },
            fontSize = Dimens.textSizeExtraLarge,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun PokemonBasicInfo(pokemon: PokemonDetail) {
    InfoCard(title = "Basic Information") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem(
                label = "Height",
                value = "${pokemon.height / 10.0}m"
            )
            InfoItem(
                label = "Weight",
                value = "${pokemon.weight / 10.0}kg"
            )
        }
    }
}

@Composable
private fun PokemonStats(pokemon: PokemonDetail) {
    InfoCard(title = "Base Stats") {
        pokemon.stats.forEach { stat ->
            StatProgressBar(
                statName = stat.stat.name,
                statValue = stat.base_stat
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        }
    }
}

@Composable
private fun PokemonTypesAndAbilities(pokemon: PokemonDetail) {
    InfoCard(title = "Types & Abilities") {
        // Types Section
        SectionTitle(text = "Types")

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
            modifier = Modifier.padding(bottom = Dimens.spacingMedium)
        ) {
            pokemon.types.forEach { type ->
                TypeChip(typeName = type.type.name)
            }
        }

        // Abilities Section
        SectionTitle(text = "Abilities")

        pokemon.abilities.forEach { ability ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ability.ability.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    },
                    fontSize = Dimens.textSizeMedium
                )

                if (ability.is_hidden) {
                    HiddenAbilityChip()
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        }
    }
}






