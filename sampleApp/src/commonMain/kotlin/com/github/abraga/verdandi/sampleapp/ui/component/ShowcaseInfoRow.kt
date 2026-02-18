package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

@Composable
fun ShowcaseInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = ShowcaseTokens.Palette.accent
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
    }
}
