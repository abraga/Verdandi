package com.github.abraga.verdandi.sampleapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages
import com.github.abraga.verdandi.sampleapp.state.CalendarState
import com.github.abraga.verdandi.sampleapp.state.ShowcaseStateFactory
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseBackground
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseHeader
import com.github.abraga.verdandi.sampleapp.ui.section.AdjustmentsSection
import com.github.abraga.verdandi.sampleapp.ui.section.CalendarSection
import com.github.abraga.verdandi.sampleapp.ui.section.ComponentsSection
import com.github.abraga.verdandi.sampleapp.ui.section.FormatExplorerSection
import com.github.abraga.verdandi.sampleapp.ui.section.IntervalsSection
import com.github.abraga.verdandi.sampleapp.ui.section.NowSection
import com.github.abraga.verdandi.sampleapp.ui.section.RecurrenceSection
import com.github.abraga.verdandi.sampleapp.ui.section.RelativeTimeSection
import com.github.abraga.verdandi.sampleapp.ui.section.TimezoneSection

@Composable
fun VerdandiShowcaseScreen(
    modifier: Modifier = Modifier
) {
    val now = remember { VerdandiShowcaseUsages.now() }
    var calendarDisplayedMoment by remember { mutableStateOf(now) }
    var selectedMoment by remember { mutableStateOf(now) }
    var adjustedMoment by remember { mutableStateOf(now) }

    val uiState = remember(calendarDisplayedMoment, selectedMoment) {
        ShowcaseStateFactory.create(now, calendarDisplayedMoment, selectedMoment)
    }

    ShowcaseBackground(modifier = modifier) {
        ShowcaseContent(
            calendarState = uiState.calendar,
            selectedMoment = selectedMoment,
            adjustedMoment = adjustedMoment,
            onPreviousMonth = {
                calendarDisplayedMoment = VerdandiShowcaseUsages.subtractOneMonth(calendarDisplayedMoment)
            },
            onNextMonth = {
                calendarDisplayedMoment = VerdandiShowcaseUsages.addOneMonth(calendarDisplayedMoment)
            },
            onTodayCalendar = {
                calendarDisplayedMoment = now
            },
            onDaySelected = { day ->
                val year = VerdandiShowcaseUsages.getYear(calendarDisplayedMoment)
                val month = VerdandiShowcaseUsages.getMonth(calendarDisplayedMoment).value
                val hour = VerdandiShowcaseUsages.getHour(selectedMoment)
                val minute = VerdandiShowcaseUsages.getMinute(selectedMoment)
                selectedMoment = VerdandiShowcaseUsages.createAt(year, month, day, hour, minute)
            },
            onHourChanged = { hour ->
                val year = VerdandiShowcaseUsages.getYear(selectedMoment)
                val month = VerdandiShowcaseUsages.getMonth(selectedMoment).value
                val day = VerdandiShowcaseUsages.getDayOfMonth(selectedMoment)
                val minute = VerdandiShowcaseUsages.getMinute(selectedMoment)
                selectedMoment = VerdandiShowcaseUsages.createAt(year, month, day, hour, minute)
            },
            onMinuteChanged = { minute ->
                val year = VerdandiShowcaseUsages.getYear(selectedMoment)
                val month = VerdandiShowcaseUsages.getMonth(selectedMoment).value
                val day = VerdandiShowcaseUsages.getDayOfMonth(selectedMoment)
                val hour = VerdandiShowcaseUsages.getHour(selectedMoment)
                selectedMoment = VerdandiShowcaseUsages.createAt(year, month, day, hour, minute)
            },
            onAdjustMoment = { adjustedMoment = it },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ShowcaseContent(
    calendarState: CalendarState,
    selectedMoment: VerdandiMoment,
    adjustedMoment: VerdandiMoment,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayCalendar: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    onAdjustMoment: (VerdandiMoment) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .widthIn(max = 768.dp)
            .verticalScroll(scrollState)
            .padding(ShowcaseTokens.Spacing.Md),
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowcaseHeader()

        CalendarSection(
            calendarState = calendarState,
            selectedHour = VerdandiShowcaseUsages.getHour(selectedMoment),
            selectedMinute = VerdandiShowcaseUsages.getMinute(selectedMoment),
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth,
            onTodayCalendar = onTodayCalendar,
            onDaySelected = onDaySelected,
            onHourChanged = onHourChanged,
            onMinuteChanged = onMinuteChanged
        )

        NowSection(now = selectedMoment)

        FormatExplorerSection(now = selectedMoment)

        AdjustmentsSection(
            now = selectedMoment,
            adjustedMoment = adjustedMoment,
            onAdjustMoment = onAdjustMoment
        )

        ComponentsSection(now = selectedMoment)

        IntervalsSection(now = selectedMoment)

        RecurrenceSection(now = selectedMoment)

        RelativeTimeSection(now = selectedMoment)

        TimezoneSection(now = selectedMoment)

        Spacer(modifier = Modifier.height(ShowcaseTokens.Spacing.Xl))
    }
}
