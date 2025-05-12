package com.example.newsdbtask.ui.presentation.components.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newsdbtask.R

@Composable
fun RotatingBorderProfileImage(
    modifier: Modifier = Modifier,
    imageRes: Int
) {
    // Infinite rotation animation for the border only
    val infiniteTransition = rememberInfiniteTransition(label = "Border Rotation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val brush = Brush.sweepGradient(
        listOf(
            colorResource(R.color.second_blue),
            colorResource(R.color.dark_purple),
            colorResource(R.color.dark_pink),
            colorResource(R.color.light_yellow),
            colorResource(R.color.dark_yellow),
            colorResource(R.color.dark_blue),
            colorResource(R.color.dark_purple),
            colorResource(R.color.second_blue),
        )
    )

    val imageSize = 130.dp
    val borderThickness = 4.dp

    Box(
        modifier = modifier.size(imageSize + borderThickness * 2),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .rotate(rotation)
                .border(
                    BorderStroke(borderThickness, brush),
                    shape = CircleShape
                )
        )

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
        )
    }
}


