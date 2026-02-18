package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

@Composable
fun ShowcaseSectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title.uppercase(),
        modifier = modifier,
        color = ShowcaseTokens.Palette.textTertiary,
        fontSize = ShowcaseTokens.Typography.LabelSmall,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp
    )
}

@Composable
fun ShowcaseSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
    ) {
        ShowcaseSectionTitle(title = title)

        GlassCard {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ShowcaseSubCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = remember { RoundedCornerShape(ShowcaseTokens.Radius.Md) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ShowcaseTokens.Palette.glassSurface)
            .border(1.dp, ShowcaseTokens.Palette.glassBorder, shape)
            .padding(ShowcaseTokens.Spacing.Md),
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
    ) {
        Text(
            text = title,
            color = ShowcaseTokens.Palette.textSecondary,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}
