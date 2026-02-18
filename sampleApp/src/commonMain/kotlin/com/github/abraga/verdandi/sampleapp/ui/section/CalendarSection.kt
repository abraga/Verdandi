package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.abraga.verdandi.sampleapp.state.CalendarState
import com.github.abraga.verdandi.sampleapp.theme.ShowcaseTokens
import com.github.abraga.verdandi.sampleapp.ui.component.CalendarViewer
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSectionTitle

@Composable
fun CalendarSection(
    calendarState: CalendarState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ShowcaseTokens.Spacing.Sm)
    ) {
        ShowcaseSectionTitle(title = "Calendar")
        CalendarViewer(
            state = calendarState,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth,
            onToday = onTodayCalendar,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

