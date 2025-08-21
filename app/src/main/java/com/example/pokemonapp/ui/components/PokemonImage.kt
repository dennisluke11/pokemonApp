package com.example.pokemonapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.pokemonapp.R
import com.example.pokemonapp.ui.theme.Dimens

@Composable
fun PokemonImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = Dimens.imageSizeLarge,
    contentScale: ContentScale = ContentScale.Fit,
    fallbackUrl: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier.size(size),
                contentScale = contentScale
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        // Loading state with shimmer effect
                        LoadingPlaceholder(size = size)
                    }

                    is AsyncImagePainter.State.Error -> {
                        // Error state - try fallback or show placeholder
                        if (fallbackUrl != null && fallbackUrl != imageUrl) {
                            SubcomposeAsyncImage(
                                model = fallbackUrl,
                                contentDescription = contentDescription,
                                modifier = Modifier.size(size),
                                contentScale = contentScale
                            ) {
                                when (painter.state) {
                                    is AsyncImagePainter.State.Loading -> LoadingPlaceholder(size = size)
                                    is AsyncImagePainter.State.Error -> ErrorPlaceholder(size = size)
                                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                                    is AsyncImagePainter.State.Empty -> ErrorPlaceholder(size = size)
                                }
                            }
                        } else {
                            ErrorPlaceholder(size = size)
                        }
                    }

                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }

                    is AsyncImagePainter.State.Empty -> {
                        ErrorPlaceholder(size = size)
                    }
                }
            }
        } else {
            // No image URL provided
            ErrorPlaceholder(size = size)
        }
    }
}

@Composable
private fun LoadingPlaceholder(size: androidx.compose.ui.unit.Dp) {
    ShimmerPlaceholder(size = size)
}

@Composable
private fun ErrorPlaceholder(size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pokemon_placeholder),
            contentDescription = "Pokemon placeholder",
            modifier = Modifier.size(size * 0.8f),
            contentScale = ContentScale.Fit
        )
    }
}
