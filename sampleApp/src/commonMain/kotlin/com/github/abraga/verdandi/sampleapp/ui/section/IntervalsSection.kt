package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.AdjustmentStepButton
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseIcons
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSubCard

@Composable
fun IntervalsSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    val last30Days = remember(now) { VerdandiShowcaseUsages.createLast30DaysInterval(now) }
    val last30StartFormatted = remember(last30Days) { VerdandiShowcaseUsages.formatDateShort(last30Days.start) }
    val last30EndFormatted = remember(last30Days) { VerdandiShowcaseUsages.formatDateShort(last30Days.end) }
    val last30Duration = remember(last30Days) { VerdandiShowcaseUsages.getIntervalDateTimeDuration(last30Days) }

    var customStart by remember { mutableStateOf(VerdandiShowcaseUsages.subtractOneMonth(now)) }
    var customEnd by remember { mutableStateOf(VerdandiShowcaseUsages.addOneMonth(now)) }

    val customInterval = remember(customStart, customEnd) {
        try {
            VerdandiShowcaseUsages.createCustomInterval(customStart, customEnd)
        } catch (_: Exception) {
            null
        }
    }

    val customStartFormatted = remember(customStart) { VerdandiShowcaseUsages.formatDateShort(customStart) }
    val customEndFormatted = remember(customEnd) { VerdandiShowcaseUsages.formatDateShort(customEnd) }
    val customDuration = remember(customInterval) {
        customInterval?.let { VerdandiShowcaseUsages.getIntervalDateTimeDuration(it) }
    }

    ShowcaseSection(title = "Intervals", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Md)
        ) {
            ShowcaseSubCard(title = "Last 30 days") {
                IntervalKeyValueRow(label = "Start", value = last30StartFormatted)
                IntervalKeyValueRow(label = "End", value = last30EndFormatted)
                IntervalKeyValueRow(label = "Duration", value = last30Duration.toString())
            }

            ShowcaseSubCard(title = "Custom interval") {
                IntervalStepperRow(
                    label = "Start",
                    formattedValue = customStartFormatted,
                    onDecrement = { customStart = VerdandiShowcaseUsages.subtractOneDay(customStart) },
                    onIncrement = { customStart = VerdandiShowcaseUsages.addOneDay(customStart) }
                )
                IntervalStepperRow(
                    label = "End",
                    formattedValue = customEndFormatted,
                    onDecrement = { customEnd = VerdandiShowcaseUsages.subtractOneDay(customEnd) },
                    onIncrement = { customEnd = VerdandiShowcaseUsages.addOneDay(customEnd) }
                )

                Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Sm))

                IntervalKeyValueRow(
                    label = "Duration",
                    value = customDuration?.toString() ?: "Invalid range",
                    valueColor = if (customInterval == null) ShowcaseTokens.Palette.textMuted else ShowcaseTokens.Palette.accent
                )
            }
        }
    }
}

@Composable
private fun IntervalKeyValueRow(
    label: String,
    value: String,
    valueColor: Color = ShowcaseTokens.Palette.accent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun IntervalStepperRow(
    label: String,
    formattedValue: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = ShowcaseTokens.Palette.textTertiary,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            modifier = Modifier.weight(0.2f)
        )
        AdjustmentStepButton(
            icon = ShowcaseIcons.Remove,
            onClick = onDecrement,
            contentDescription = "Decrease"
        )
        Text(
            text = formattedValue,
            color = ShowcaseTokens.Palette.accent,
            fontSize = ShowcaseTokens.Typography.BodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        AdjustmentStepButton(
            icon = ShowcaseIcons.Add,
            onClick = onIncrement,
            contentDescription = "Increase"
        )
    }
}
