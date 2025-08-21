package com.example.pokemonapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.pokemonapp.ui.theme.Dimens

@Composable
fun TypeChip(
    typeName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = typeName.replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase() else it.toString() 
            },
            modifier = Modifier.padding(
                horizontal = Dimens.spacingMedium, 
                vertical = Dimens.spacingSmall
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HiddenAbilityChip(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = "Hidden",
            modifier = Modifier.padding(
                horizontal = Dimens.spacingSmall, 
                vertical = Dimens.spacingSmall
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = Dimens.textSizeSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
