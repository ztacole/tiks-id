package com.zetta.tiksid.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerLoading(
    durationMillis: Int = 1000,
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "",
    )

    drawBehind {
        val shimmerRatio = 0.4f

        val start = Offset(
            x = translateAnimation,
            y = translateAnimation
        )
        val end = Offset(
            x = translateAnimation + size.width * shimmerRatio,
            y = translateAnimation + size.width * shimmerRatio
        )
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.1f),
                    Color.LightGray.copy(alpha = 0.35f),
                    Color.LightGray.copy(alpha = 0.1f),
                ),
                start = start,
                end = end,
            )
        )
    }
}