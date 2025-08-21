package com.example.pokemonapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.pokemonapp.ui.theme.Dimens

@Composable
fun ErrorMessage(
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops, try again later!",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        
        if (onDismiss != null) {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            
            OutlinedButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    }
}
