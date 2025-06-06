package com.example.newsdbtask.ui.presentation.components.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Stable
class JumpAnimationState(
    val interactionSource: MutableInteractionSource,
    val scope: CoroutineScope,
) {
    private var animation: Job? = null

    val scale = Animatable(initialValue = 1f)
    val translation = Animatable(initialValue = 0f)

    fun onPress() = launchAnimation {
        scale.snapTo(defaultScale)
        translation.snapTo(defaultTranslation)

        scale.animateTo(pressedScale, defaultSpring)
    }

    fun onRelease(invokeOnCompletion: () -> Unit) = launchAnimation {
        scale.animateTo(pressedScale, defaultSpring)

        launch {
            scale.animateTo(defaultScale, releaseScaleSpring)
        }

        launch {
            translation.animateTo(launchTranslation, launchTranslationSpring)

            var isSquishing = false
            translation.animateTo(defaultTranslation, returnTranslationSpring) {
                val hitTheGround = value >= defaultTranslation
                if (hitTheGround && !isSquishing) {
                    isSquishing = true
                    invokeOnCompletion()
                    launch {
                        scale.animateTo(squishScale, defaultSpring)
                        scale.animateTo(defaultScale, defaultSpring)
                    }
                }
            }
        }
    }

    fun onCancel() = launchAnimation {
        scale.snapTo(defaultScale)
        translation.snapTo(defaultTranslation)
    }

    private fun launchAnimation(block: suspend CoroutineScope.() -> Unit) {
        animation?.cancel()
        animation = scope.launch(block = block)
    }
}

private const val defaultScale = 1f
private const val pressedScale = 0.6f
private const val squishScale = 0.88f
private const val defaultTranslation = 0f
private const val launchTranslation = -0.8f
private val defaultSpring = spring<Float>()
private val releaseScaleSpring = spring<Float>(
    stiffness = Spring.StiffnessMedium,
    dampingRatio = 0.55f
)
private val launchTranslationSpring = spring<Float>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMediumLow,
)
private val returnTranslationSpring = spring<Float>(
    dampingRatio = 0.65f,
    stiffness = 140f,
)