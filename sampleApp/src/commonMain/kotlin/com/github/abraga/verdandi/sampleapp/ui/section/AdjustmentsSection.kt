package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.AdjustmentButton
import com.github.abraga.verdandi.sampleapp.ui.component.AdjustmentRow
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun AdjustmentsSection(
    now: VerdandiMoment,
    adjustedMoment: VerdandiMoment,
    onAdjustMoment: (VerdandiMoment) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayedDate = remember(adjustedMoment) {
        VerdandiShowcaseUsages.formatFullDate(adjustedMoment)
    }
    val displayedTime = remember(adjustedMoment) {
        VerdandiShowcaseUsages.formatTime(adjustedMoment)
    }

    ShowcaseSection(title = "Adjustments", modifier = modifier) {
        Text(
            text = "$displayedDate  $displayedTime",
            color = ShowcaseTokens.Palette.accent,
            fontSize = ShowcaseTokens.Typography.TitleSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Xs))

        AdjustmentRow(
            label = "Month",
            onDecrement = { onAdjustMoment(VerdandiShowcaseUsages.subtractOneMonth(adjustedMoment)) },
            onIncrement = { onAdjustMoment(VerdandiShowcaseUsages.addOneMonth(adjustedMoment)) },
            modifier = Modifier.fillMaxWidth()
        )
        AdjustmentRow(
            label = "Week",
            onDecrement = { onAdjustMoment(VerdandiShowcaseUsages.subtractOneWeek(adjustedMoment)) },
            onIncrement = { onAdjustMoment(VerdandiShowcaseUsages.addOneWeek(adjustedMoment)) },
            modifier = Modifier.fillMaxWidth()
        )
        AdjustmentRow(
            label = "Day",
            onDecrement = { onAdjustMoment(VerdandiShowcaseUsages.subtractOneDay(adjustedMoment)) },
            onIncrement = { onAdjustMoment(VerdandiShowcaseUsages.addOneDay(adjustedMoment)) },
            modifier = Modifier.fillMaxWidth()
        )
        AdjustmentRow(
            label = "Hour",
            onDecrement = { onAdjustMoment(VerdandiShowcaseUsages.subtractOneHour(adjustedMoment)) },
            onIncrement = { onAdjustMoment(VerdandiShowcaseUsages.addOneHour(adjustedMoment)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Xs))

        AdjustmentButton(
            label = "Reset to now",
            onClick = { onAdjustMoment(now) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
