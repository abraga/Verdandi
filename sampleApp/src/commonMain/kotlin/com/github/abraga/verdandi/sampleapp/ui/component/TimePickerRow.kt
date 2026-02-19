package com.github.abraga.verdandi.sampleapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens

@Composable
fun TimePickerRow(
    hour: Int,
    minute: Int,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdjustmentRow(
                label = hour.toString().padStart(2, '0') + "h",
                onDecrement = { onHourChanged((hour - 1 + 24) % 24) },
                onIncrement = { onHourChanged((hour + 1) % 24) },
                modifier = Modifier.weight(1f)
            )

            Text(
                text = ":",
                color = ShowcaseTokens.Palette.textTertiary,
                fontSize = ShowcaseTokens.Typography.TitleLarge,
                fontWeight = FontWeight.Light
            )

            AdjustmentRow(
                label = minute.toString().padStart(2, '0') + "m",
                onDecrement = { onMinuteChanged((minute - 1 + 60) % 60) },
                onIncrement = { onMinuteChanged((minute + 1) % 60) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
