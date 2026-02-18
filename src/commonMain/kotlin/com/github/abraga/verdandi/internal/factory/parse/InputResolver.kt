package com.github.abraga.verdandi.internal.factory.parse

import com.github.abraga.verdandi.internal.DateTimeConstants.MAX_NANOSECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.core.VerdandiClock
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.extension.timeZoneContext

internal class InputResolver {

    private val timestampResolver: TimestampResolver = TimestampResolver()

    fun resolveMilliseconds(milliseconds: Long?): Long {
        if (milliseconds == null) {
            return VerdandiClock.now().toUtcEpochMillis()
        }

        return milliseconds
    }

    fun resolveTimestamp(timestamp: String): Long {
        return timestampResolver.resolve(timestamp)
    }

    fun resolveComponents(
        years: Int,
        months: Int,
        days: Int,
        hours: Int,
        minutes: Int,
        seconds: Int,
        milliseconds: Int
    ): Long {
        val nanos = (milliseconds * NANOS_PER_MILLI.toInt()).coerceIn(0, MAX_NANOSECOND)

        val localDateTime = VerdandiLocalDateTime.of(
            year = years,
            monthNumber = months,
            dayOfMonth = days,
            hour = hours,
            minute = minutes,
            second = seconds,
            nanosecond = nanos
        )

        return timeZoneContext.toEpochMillis(localDateTime)
    }
}
