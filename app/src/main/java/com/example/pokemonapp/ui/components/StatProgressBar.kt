package com.example.pokemonapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.pokemonapp.ui.theme.Dimens

@Composable
fun StatProgressBar(
    statName: String,
    statValue: Int,
    maxValue: Int = 255,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            },
            fontSize = Dimens.textSizeMedium,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(Dimens.spacingMedium))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
        ) {
            Text(
                text = statValue.toString(),
                fontSize = Dimens.textSizeMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            LinearProgressIndicator(
                progress = statValue.toFloat() / maxValue,
                modifier = Modifier
                    .width(Dimens.progressBarWidth)
                    .height(Dimens.progressBarHeight),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        }
    }
}

