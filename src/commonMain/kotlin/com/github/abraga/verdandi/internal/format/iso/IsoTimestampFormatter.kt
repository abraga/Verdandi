package com.github.abraga.verdandi.internal.format.iso

import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.format.pad

internal object IsoTimestampFormatter {

    private const val DATE_TIME_SEPARATOR = 'T'
    private const val FRACTION_SEPARATOR = '.'
    private const val UTC_DESIGNATOR = 'Z'

    private const val MILLIS_PAD_LENGTH = 3

    fun formatUtc(localDateTime: VerdandiLocalDateTime): String {
        val datePart = IsoDateFormatter.format(
            localDateTime.year,
            localDateTime.monthNumber,
            localDateTime.dayOfMonth
        )
        val timePart = formatTime(localDateTime)

        return buildString {
            append(datePart)
            append(DATE_TIME_SEPARATOR)
            append(timePart)
            append(UTC_DESIGNATOR)
        }
    }

    private fun formatTime(localDateTime: VerdandiLocalDateTime): String {
        val baseTime = IsoTimeFormatter.format(
            hour = localDateTime.hour,
            minute = localDateTime.minute,
            second = localDateTime.second,
            nanosecond = 0
        )

        val millisecond = (localDateTime.nanosecond / NANOS_PER_MILLI).toInt()

        if (millisecond == 0) {
            return baseTime
        }

        val millisStr = millisecond.pad(MILLIS_PAD_LENGTH).trimEnd('0')

        return buildString {
            append(baseTime)
            append(FRACTION_SEPARATOR)
            append(millisStr)
        }
    }
}
