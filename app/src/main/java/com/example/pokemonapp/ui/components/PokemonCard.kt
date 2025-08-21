@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonapp.data.model.PokemonListItem
import com.example.pokemonapp.ui.theme.Dimens

@Composable
fun PokemonCard(
    pokemon: PokemonListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacingSmall),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val pokemonId = pokemon.url.split("/").dropLast(1).last().toIntOrNull() ?: 1
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
            val fallbackUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
            
            PokemonImage(
                imageUrl = imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(Dimens.imageSizeMedium),
                size = Dimens.imageSizeMedium,
                contentScale = ContentScale.Crop,
                fallbackUrl = fallbackUrl
            )
            
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            
            Column {
                Text(
                    text = pokemon.name.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase() else it.toString() 
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "#$pokemonId",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
