package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseInfoRow
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun NowSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    val fullDate = remember(now) { VerdandiShowcaseUsages.formatFullDate(now) }
    val shortDate = remember(now) { VerdandiShowcaseUsages.formatDateShort(now) }
    val dateTime = remember(now) { VerdandiShowcaseUsages.formatDateTime(now) }
    val simpleDateTime = remember(now) { VerdandiShowcaseUsages.formatDateTimeSimple(now) }
    val time = remember(now) { VerdandiShowcaseUsages.formatTime(now) }

    ShowcaseSection(title = "Now", modifier = modifier) {
        ShowcaseInfoRow(label = "Full date", value = fullDate)
        ShowcaseInfoRow(label = "Short date", value = shortDate)
        ShowcaseInfoRow(label = "Date & time", value = dateTime)
        ShowcaseInfoRow(label = "Simple", value = simpleDateTime)
        ShowcaseInfoRow(label = "Time", value = time)
        ShowcaseInfoRow(label = "Epoch (ms)", value = now.inMilliseconds.toString())
    }
}
