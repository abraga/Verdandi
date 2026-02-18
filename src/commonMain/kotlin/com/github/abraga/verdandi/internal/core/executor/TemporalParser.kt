package com.github.abraga.verdandi.internal.core.executor

import com.github.abraga.verdandi.internal.DateTimeConstants.FIRST_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_RANGE
import com.github.abraga.verdandi.internal.core.unit.VerdandiMonth
import com.github.abraga.verdandi.internal.extension.verdandiRequire
import com.github.abraga.verdandi.internal.extension.verdandiRequireParse

internal object TemporalParser {

    private const val DATE_SEPARATOR = '-'
    private const val TIME_SEPARATOR = ':'
    private const val FRACTION_SEPARATOR = '.'
    private const val ZONE_UTC = 'Z'
    private const val ZONE_PLUS = '+'
    private const val ZONE_MINUS = '-'
    private const val TIMESTAMP_T = 'T'
    private const val TIMESTAMP_SPACE = ' '
    private const val NANO_DIGITS = 9
    private const val MAX_OFFSET_HOURS = 18

    fun parseDate(datePart: String): DateComponents {
        val parts = datePart.split(DATE_SEPARATOR)

        verdandiRequireParse(
            parts.size == 3,
            { "Invalid date format: $datePart" }
        )

        val year = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val day = parts[2].toIntOrNull()

        verdandiRequireParse(
            year != null && month != null && day != null,
            { "Invalid date format: $datePart" }
        )

        verdandiRequireParse(
            month in MONTHS_RANGE,
            { "Month must be $MONTHS_RANGE: $month" }
        )

        verdandiRequireParse(
            day in FIRST_DAY..maxDayOfMonth(year, month),
            { "Day $day is invalid for $year-$month" }
        )

        return DateComponents(year, month, day)
    }

    fun parseTime(timePart: String): TimeComponents {
        var timeString = timePart
        var nanosecond = 0
        val fractionIndex = timeString.indexOf(FRACTION_SEPARATOR)

        if (fractionIndex >= 0) {
            val fractionPart = timeString.substring(fractionIndex + 1)
            nanosecond = parseFraction(fractionPart)
            timeString = timeString.take(fractionIndex)
        }

        val parts = timeString.split(TIME_SEPARATOR)

        verdandiRequireParse(
            parts.size >= 2,
            { "Invalid time format: $timePart" }
        )

        val hour = parts[0].toIntOrNull()
        val minute = parts[1].toIntOrNull()
        val second = if (parts.size > 2) parts[2].toIntOrNull() else 0

        verdandiRequireParse(
            hour != null && minute != null && second != null,
            { "Invalid time format: $timePart" }
        )

        verdandiRequireParse(
            hour in 0..23,
            { "Hour must be 0..23: $hour" }
        )

        verdandiRequireParse(
            minute in 0..59,
            { "Minute must be 0..59: $minute" }
        )

        verdandiRequireParse(
            second in 0..59,
            { "Second must be 0..59: $second" }
        )

        return TimeComponents(hour, minute, second, nanosecond)
    }

    fun parseTimeWithZone(timeAndZonePart: String): ZoneOffsetResult {
        var timePart = timeAndZonePart
        var offsetMillis = 0L
        val zoneStartIndex = findZoneStart(timePart)

        if (zoneStartIndex >= 0) {
            val zonePart = timePart.substring(zoneStartIndex)
            offsetMillis = parseZoneOffset(zonePart)
            timePart = timePart.take(zoneStartIndex)
        }

        val timeComponents = parseTime(timePart)

        return ZoneOffsetResult(timeComponents, offsetMillis)
    }

    fun findDateTimeSeparator(text: String): Int {
        val tIndex = text.indexOf(TIMESTAMP_T)

        if (tIndex > 0) {
            return tIndex
        }

        return text.indexOf(TIMESTAMP_SPACE)
    }

    private fun parseFraction(fraction: String): Int {
        verdandiRequireParse(
            fraction.isNotEmpty(),
            { "Fraction cannot be empty" }
        )

        verdandiRequireParse(
            fraction.length <= NANO_DIGITS,
            { "Fraction exceeds 9 digits: $fraction" }
        )

        verdandiRequireParse(
            fraction.all { it.isDigit() },
            { "Invalid fraction (non-digit characters): $fraction" }
        )

        val normalized = fraction.padEnd(NANO_DIGITS, '0')

        return normalized.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid fraction: $fraction")
    }

    private fun findZoneStart(timePart: String): Int {
        val zIndex = timePart.indexOf(ZONE_UTC)

        if (zIndex >= 0) {
            return zIndex
        }

        for (index in timePart.length - 1 downTo 0) {
            val char = timePart[index]

            if (char == ZONE_PLUS || char == ZONE_MINUS) {
                if (index > 0 && timePart[index - 1].isDigit()) {
                    return index
                }
            }
        }

        return -1
    }

    private fun parseZoneOffset(zonePart: String): Long {
        if (zonePart == ZONE_UTC.toString()) {
            return 0L
        }

        verdandiRequire(
            zonePart.isNotEmpty(),
            { "Empty zone offset" }
        )

        val signChar = zonePart[0]

        verdandiRequire(
            signChar == ZONE_PLUS || signChar == ZONE_MINUS,
            { "Invalid zone offset sign: $zonePart" }
        )

        val sign = if (signChar == ZONE_MINUS) -1 else 1
        val offsetContent = zonePart.substring(1)
        val offsetString = offsetContent.replace(TIME_SEPARATOR.toString(), "")

        verdandiRequire(
            offsetString.all { it.isDigit() },
            { "Invalid zone offset (non-digit characters): $zonePart" }
        )

        val hours: Int
        val minutes: Int

        when (offsetString.length) {
            2 -> {
                hours = parseOffsetComponent(offsetString, zonePart)
                minutes = 0
            }
            4 -> {
                hours = parseOffsetComponent(offsetString.take(2), zonePart)
                minutes = parseOffsetComponent(offsetString.substring(2, 4), zonePart)
            }
            else -> {
                throw IllegalArgumentException("Invalid zone offset format: $zonePart")
            }
        }

        verdandiRequire(
            minutes in 0..59,
            { "Zone offset minutes must be 0..59: $zonePart" }
        )

        verdandiRequire(
            hours in 0..MAX_OFFSET_HOURS,
            { "Zone offset hours must be 0..18: $zonePart" }
        )

        verdandiRequire(
            hours < MAX_OFFSET_HOURS || minutes == 0,
            { "Zone offset exceeds ±18:00: $zonePart" }
        )

        return sign * (hours * MILLIS_PER_HOUR + minutes * MILLIS_PER_MINUTE)
    }

    private fun parseOffsetComponent(value: String, zonePart: String): Int {
        return value.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid zone offset component: $zonePart")
    }

    private fun maxDayOfMonth(year: Int, month: Int): Int {
        return if (month in 1..12) VerdandiMonth.of(month).lengthInYear(year) else 0
    }

    internal data class DateComponents(
        val year: Int,
        val month: Int,
        val day: Int
    )

    internal data class TimeComponents(
        val hour: Int,
        val minute: Int,
        val second: Int,
        val nanosecond: Int
    )

    internal data class ZoneOffsetResult(
        val timeComponents: TimeComponents,
        val offsetMillis: Long
    )
}
