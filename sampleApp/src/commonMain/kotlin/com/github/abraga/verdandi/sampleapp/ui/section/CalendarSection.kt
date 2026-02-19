package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.abraga.verdandi.sampleapp.state.CalendarState
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.CalendarViewer
import com.github.abraga.verdandi.sampleapp.ui.component.GlassCard
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSectionTitle
import com.github.abraga.verdandi.sampleapp.ui.component.TimePickerRow

@Composable
fun CalendarSection(
    calendarState: CalendarState,
    selectedHour: Int,
    selectedMinute: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayCalendar: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
        ) {

            CalendarViewer(
                state = calendarState,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onToday = onTodayCalendar,
                onDayClick = onDaySelected,
                modifier = Modifier.fillMaxWidth()
            )

            TimePickerRow(
                hour = selectedHour,
                minute = selectedMinute,
                onHourChanged = onHourChanged,
                onMinuteChanged = onMinuteChanged
            )
        }
    }
}
