package com.zetta.tiksid.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp

@SuppressLint("RestrictedApi")
@Composable
fun CompassRefreshIndicator(
    progress: Float,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "compass")
    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulsingScale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsing"
    )

    val activeIndex = remember(animatedRotation) {
        ((animatedRotation / 90f) % 4).toInt()
    }

    val baseScale = 0.6f
    val maxScale = 1.2f

    val initialRotation = when {
        isRefreshing -> animatedRotation
        else -> lerp(180f, 360f, progress)
    }

    Box(
        modifier = modifier
            .layout{ measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val width = placeable.width
                val height = placeable.height
                layout(width, height) {
                    placeable.placeWithLayer(
                        0,
                        0,
                        layerBlock = {
                            rotationZ = initialRotation
                            translationY = progress * 24.dp.roundToPx()
                            alpha = progress
                        }
                    )
                }
            },
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            repeat(4) { index ->
                val scale = if (isRefreshing) {
                    if (index == activeIndex) pulsingScale else baseScale
                } else {
                    lerp(baseScale, maxScale, progress)
                }

                val isHorizontal = index % 2 == 0
                val translation = if (index > 1) 16f else -16f

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            if (isHorizontal) translationX = translation.dp.toPx()
                            else translationY = translation.dp.toPx()
                        }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            }
        }
    }
}
