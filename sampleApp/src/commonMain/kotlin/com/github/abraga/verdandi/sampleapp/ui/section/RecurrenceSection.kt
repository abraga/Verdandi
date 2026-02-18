package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseInfoRow
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun RecurrenceSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    val fridayCount = remember(now) {
        VerdandiShowcaseUsages.createHowManyFridaysRecurrenceInNextTwoMonths()
    }
    val weekdayEnd = remember(now) {
        VerdandiShowcaseUsages.addOneMonth(VerdandiShowcaseUsages.addOneMonth(now))
    }
    val weekdayRecurrence = remember(now, weekdayEnd) {
        VerdandiShowcaseUsages.createWeekdayRecurrence(now, weekdayEnd, maxResults = 5)
    }
    val weekdayDates = remember(weekdayRecurrence) {
        VerdandiShowcaseUsages.formatAllToSimpleDate(weekdayRecurrence)
    }

    ShowcaseSection(title = "Recurrence", modifier = modifier) {
        ShowcaseInfoRow(
            label = "Fridays in next 2 months",
            value = "$fridayCount occurrences"
        )
        ShowcaseInfoRow(
            label = "Next 5 weekdays",
            value = weekdayDates
        )
    }
}

