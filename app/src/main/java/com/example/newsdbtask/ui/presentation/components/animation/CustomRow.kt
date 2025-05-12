package com.example.newsdbtask.ui.presentation.components.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsdbtask.R
import kotlinx.coroutines.CoroutineScope

@Composable
fun CustomRow(
    text: String,
    icon: Int,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val jumpState = rememberJumpAnimationState(
        onClick = onClick,
        interactionSource = interactionSource
    )

    val customFontFamily = FontFamily(Font(R.font.my_font))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { /* Shared interaction triggers animation */ }
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(37.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.dark_yellow)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .jumpOnClick(jumpState),
                        painter = painterResource(icon),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { /* Same interaction source triggers animation */ },
                    text = text,
                    fontFamily = customFontFamily,
                    fontSize = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .size(height = 1.dp, width = 300.dp),
            color = colorResource(R.color.dark_yellow)
        )
    }
}


fun Modifier.jumpOnClick(
    state: JumpAnimationState
) = this then Modifier
    .clickable(
        interactionSource = state.interactionSource,
        indication = null,
        onClick = { /* this is handled in the state */ },
    )
    .graphicsLayer {
        transformOrigin = TransformOrigin(
            pivotFractionX = 0.5f,
            pivotFractionY = 1.0f,
        )
        scaleY = state.scale.value
        translationY = state.translation.value * size.height
    }

@Composable
fun rememberJumpAnimationState(
    onClick: () -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): JumpAnimationState {
    val state = remember(scope, interactionSource) {
        JumpAnimationState(
            scope = scope,
            interactionSource = interactionSource,
        )
    }

    val onClickLambda by rememberUpdatedState(onClick)
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> state.onPress()
                is PressInteraction.Release -> state.onRelease(onClickLambda)
                is PressInteraction.Cancel -> state.onCancel()
            }
        }
    }

    return state
}