package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = ShowcaseTokens.Radius.Lg,
    tintColor: Color = ShowcaseTokens.Palette.glassSurface,
    borderColor: Color = ShowcaseTokens.Palette.glassBorder,
    borderWidth: Dp = 1.dp,
    contentPadding: PaddingValues = PaddingValues(ShowcaseTokens.Spacing.Md),
    content: @Composable BoxScope.() -> Unit
) {
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    val backgroundBrush = remember(tintColor) {
        Brush.verticalGradient(
            colors = listOf(
                tintColor,
                tintColor.copy(alpha = tintColor.alpha * 0.7f)
            )
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = backgroundBrush)
                .blur(16.dp)
        )

        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
