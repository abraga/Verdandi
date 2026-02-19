package com.github.abraga.verdandi.sampleapp.state

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.showcase.VerdandiShowcaseUsages

data class ShowcaseUiState(
    val calendar: CalendarState
)

data class CalendarState(
    val monthYear: String,
    val weekdayHeaders: List<String>,
    val days: List<CalendarDay>,
    val displayedMoment: VerdandiMoment,
    val selectedDayNumber: Int?,
    val isViewingCurrentMonth: Boolean = true
)

data class CalendarDay(
    val dayNumber: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean
)

object ShowcaseStateFactory {

    fun create(
        now: VerdandiMoment,
        displayedMoment: VerdandiMoment,
        selectedMoment: VerdandiMoment
    ): ShowcaseUiState {
        return ShowcaseUiState(
            calendar = buildCalendarState(now, displayedMoment, selectedMoment)
        )
    }

    private fun buildCalendarState(
        now: VerdandiMoment,
        displayedMoment: VerdandiMoment,
        selectedMoment: VerdandiMoment
    ): CalendarState {
        val year = VerdandiShowcaseUsages.getYear(displayedMoment)
        val month = VerdandiShowcaseUsages.getMonth(displayedMoment)

        val nowYear = VerdandiShowcaseUsages.getYear(now)
        val nowMonth = VerdandiShowcaseUsages.getMonth(now)
        val todayDay = VerdandiShowcaseUsages.getDayOfMonth(now)

        val selectedYear = VerdandiShowcaseUsages.getYear(selectedMoment)
        val selectedMonth = VerdandiShowcaseUsages.getMonth(selectedMoment)
        val selectedDay = VerdandiShowcaseUsages.getDayOfMonth(selectedMoment)
        val isSelectedInThisMonth = selectedYear == year && selectedMonth.value == month.value

        val isCurrentMonth = year == nowYear && month.value == nowMonth.value

        val firstOfMonth = VerdandiShowcaseUsages.createAt(year, month.value, 1, 0, 0)
        val firstDayOfWeek = VerdandiShowcaseUsages.getDayOfWeek(firstOfMonth)

        val daysInMonth = VerdandiShowcaseUsages.getDaysInMonth(year, month)
        val daysInPrevMonth = if (month.value == 1) {
            val prevYearDec = VerdandiShowcaseUsages.createAt(year - 1, 12, 1, 0, 0)
            VerdandiShowcaseUsages.getDaysInMonth(year - 1, VerdandiShowcaseUsages.getMonth(prevYearDec))
        } else {
            val prevMonth = VerdandiShowcaseUsages.createAt(year, month.value - 1, 1, 0, 0)
            VerdandiShowcaseUsages.getDaysInMonth(year, VerdandiShowcaseUsages.getMonth(prevMonth))
        }

        val calendarDays = mutableListOf<CalendarDay>()

        val leadingDays = (firstDayOfWeek - 1).let { if (it < 0) 6 else it }

        for (index in leadingDays downTo 1) {
            calendarDays.add(
                CalendarDay(
                    dayNumber = daysInPrevMonth - index + 1,
                    isCurrentMonth = false,
                    isToday = false
                )
            )
        }

        for (day in 1..daysInMonth) {
            calendarDays.add(
                CalendarDay(
                    dayNumber = day,
                    isCurrentMonth = true,
                    isToday = isCurrentMonth && day == todayDay
                )
            )
        }

        val remainingCells = (7 - (calendarDays.size % 7)) % 7

        for (day in 1..remainingCells) {
            calendarDays.add(
                CalendarDay(
                    dayNumber = day,
                    isCurrentMonth = false,
                    isToday = false
                )
            )
        }

        val monthName = VerdandiShowcaseUsages.getMonthName(displayedMoment)

        return CalendarState(
            monthYear = "$monthName $year",
            weekdayHeaders = Verdandi.config.weekdayNames.map { it.take(3) },
            days = calendarDays,
            displayedMoment = displayedMoment,
            selectedDayNumber = if (isSelectedInThisMonth) selectedDay else null,
            isViewingCurrentMonth = isCurrentMonth
        )
    }
}
