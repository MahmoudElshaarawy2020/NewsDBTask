package com.example.newsdbtask.ui.presentation.shimmer_loader

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.newsdbtask.R

@Composable
fun Modifier.shimmer(
    width: Dp = 200.dp,
    durationMillis: Int = 1000,
    shimmerColors: List<Color> = listOf(
        colorResource(R.color.light_green).copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.8f),
        colorResource(R.color.light_green).copy(alpha = 0.3f),
    ),
): Modifier {
    val density = LocalDensity.current
    val widthInPx = with(density) { width.toPx() }

    val infiniteTransition = rememberInfiniteTransition()
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = widthInPx * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )

    return this.then(
        Modifier.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(offsetX - widthInPx, 0f),
                end = Offset(offsetX, 0f),
                tileMode = TileMode.Mirror,
            )
        )
    )
}