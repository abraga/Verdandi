package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

private fun DrawScope.softBlob(
    color: Color,
    center: Offset,
    radius: Float
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.35f),
                color.copy(alpha = 0.18f),
                color.copy(alpha = 0f)
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center,
        blendMode = BlendMode.Plus
    )
}

@Composable
fun ShowcaseBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                ShowcaseTokens.Palette.backgroundStart,
                ShowcaseTokens.Palette.backgroundMid,
                ShowcaseTokens.Palette.backgroundEnd
            ),
            start = Offset.Zero,
            end = Offset.Infinite
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            softBlob(
                color = ShowcaseTokens.Palette.blobPrimary,
                center = Offset(size.width * 0.15f, size.height * 0.18f),
                radius = size.width * 0.55f
            )
            softBlob(
                color = ShowcaseTokens.Palette.blobSecondary,
                center = Offset(size.width * 0.88f, size.height * 0.42f),
                radius = size.width * 0.50f
            )
            softBlob(
                color = ShowcaseTokens.Palette.blobTertiary,
                center = Offset(size.width * 0.25f, size.height * 0.82f),
                radius = size.width * 0.48f
            )
            drawRect(
                color = Color.White.copy(alpha = 0.02f),
                blendMode = BlendMode.Overlay
            )
        }

        content()
    }
}
