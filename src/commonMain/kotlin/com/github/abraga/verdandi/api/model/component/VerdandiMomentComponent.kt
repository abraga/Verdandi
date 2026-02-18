package com.github.abraga.verdandi.api.model.component

import com.github.abraga.verdandi.internal.DateTimeConstants.MAX_NANOSECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class VerdandiMomentComponent internal constructor(
    private val years: Int = 0,
    private val months: Int = 0,
    private val days: Int = 0,
    private val hours: Int = 0,
    private val minutes: Int = 0,
    private val seconds: Int = 0,
    private val milliseconds: Int = 0,
    private val offsets: Int = 0
) {

    val year: Year
        get() = Year(years)

    val month: Month
        get() = Month(months)

    val dayOfTheYear: DayOfTheYear
        get() = DayOfTheYear(localDateTime.dayOfYear)

    val dayOfWeek: DayOfWeek
        get() = DayOfWeek(localDateTime.dayOfWeek.isoDayNumber)

    val day: DayOfMonth
        get() = DayOfMonth(days)

    val hour: Hour
        get() = Hour(hours)

    val minute: Minute
        get() = Minute(minutes)

    val second: Second
        get() = Second(seconds)

    val millisecond: Millisecond
        get() = Millisecond(milliseconds)

    val offset: Offset
        get() = Offset(offsets)

    val monthName: String
        get() = month.name

    val monthNameShort: String
        get() = month.shortName

    val dayOfWeekName: String
        get() = dayOfWeek.name

    val dayOfWeekNameShort: String
        get() = dayOfWeek.shortName

    private val localDateTime: VerdandiLocalDateTime by lazy {
        val nanos = (milliseconds * NANOS_PER_MILLI.toInt()).coerceIn(0, MAX_NANOSECOND)

        VerdandiLocalDateTime.of(
            year = years,
            monthNumber = months,
            dayOfMonth = days,
            hour = hours,
            minute = minutes,
            second = seconds,
            nanosecond = nanos
        )
    }
}
