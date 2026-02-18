package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.abraga.verdandi.compose.rememberCurrentMoment
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import kotlin.time.Duration.Companion.seconds

@Composable
fun ShowcaseHeader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
        ) {
            Text(
                text = "Verdandi",
                color = ShowcaseTokens.Palette.textPrimary,
                fontSize = ShowcaseTokens.Typography.DisplayLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Date & time DSL for KMP (Kotlin Multiplatform)",
                color = ShowcaseTokens.Palette.textSecondary,
                fontSize = ShowcaseTokens.Typography.BodyMedium,
                textAlign = TextAlign.Center
            )
        }

        LiveClock(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
private fun LiveClock(modifier: Modifier = Modifier) {
    val now by rememberCurrentMoment(refreshInterval = 1.seconds)

    Text(
        text = now format { HH.mm.ss },
        color = ShowcaseTokens.Palette.accent,
        fontSize = ShowcaseTokens.Typography.TitleSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}
