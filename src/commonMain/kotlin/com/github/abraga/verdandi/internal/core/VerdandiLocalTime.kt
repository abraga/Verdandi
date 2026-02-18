package com.github.abraga.verdandi.internal.core

import com.github.abraga.verdandi.internal.DateTimeConstants.HOURS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MAX_NANOSECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_SECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.MINUTES_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_SECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.SECONDS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.SECONDS_PER_MINUTE
import com.github.abraga.verdandi.internal.core.executor.TemporalParser
import com.github.abraga.verdandi.internal.extension.verdandiRequireValidation
import com.github.abraga.verdandi.internal.format.iso.IsoTimeFormatter

internal class VerdandiLocalTime private constructor(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nanosecond: Int
) : Comparable<VerdandiLocalTime> {

    val millisecondOfDay: Long
        get() = hour * MILLIS_PER_HOUR +
            minute * MILLIS_PER_MINUTE +
            second * MILLIS_PER_SECOND +
            nanosecond / NANOS_PER_MILLI

    val secondOfDay: Int
        get() = hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + second

    val nanosecondOfDay: Long
        get() = hour * NANOS_PER_HOUR +
            minute * NANOS_PER_MINUTE +
            second * NANOS_PER_SECOND +
            nanosecond

    override fun compareTo(other: VerdandiLocalTime): Int {
        var result = hour.compareTo(other.hour)

        if (result != 0) {
            return result
        }

        result = minute.compareTo(other.minute)

        if (result != 0) {
            return result
        }

        result = second.compareTo(other.second)

        if (result != 0) {
            return result
        }

        return nanosecond.compareTo(other.nanosecond)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is VerdandiLocalTime) {
            return false
        }

        return hour == other.hour &&
            minute == other.minute &&
            second == other.second &&
            nanosecond == other.nanosecond
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        result = 31 * result + second
        result = 31 * result + nanosecond

        return result
    }

    override fun toString(): String {
        return IsoTimeFormatter.format(hour, minute, second, nanosecond)
    }

    companion object {

        private const val MIDNIGHT_HOUR = 0
        private const val NOON_HOUR = 12
        private const val MAX_HOUR = 23
        private const val MAX_MINUTE = 59
        private const val MAX_SECOND = 59

        val Midnight: VerdandiLocalTime = of(MIDNIGHT_HOUR, 0, 0, 0)

        val Noon: VerdandiLocalTime = of(NOON_HOUR, 0, 0, 0)

        val Max: VerdandiLocalTime = of(MAX_HOUR, MAX_MINUTE, MAX_SECOND, MAX_NANOSECOND)

        fun of(hour: Int, minute: Int, second: Int = 0, nanosecond: Int = 0): VerdandiLocalTime {
            verdandiRequireValidation(hour in 0 until HOURS_PER_DAY) {
                "Hour must be in 0..23, was $hour"
            }

            verdandiRequireValidation(minute in 0 until MINUTES_PER_HOUR) {
                "Minute must be in 0..59, was $minute"
            }

            verdandiRequireValidation(second in 0 until SECONDS_PER_MINUTE) {
                "Second must be in 0..59, was $second"
            }

            verdandiRequireValidation(nanosecond.toLong() in 0L until NANOS_PER_SECOND) {
                "Nanosecond must be in 0..999999999, was $nanosecond"
            }

            return VerdandiLocalTime(hour, minute, second, nanosecond)
        }

        fun fromMillisecondOfDay(millisecondOfDay: Int): VerdandiLocalTime {
            verdandiRequireValidation(millisecondOfDay in 0..<MILLIS_PER_DAY) {
                "Millisecond of day must be in 0..${MILLIS_PER_DAY - 1}, was $millisecondOfDay"
            }

            var remaining = millisecondOfDay.toLong()

            val hour = (remaining / MILLIS_PER_HOUR).toInt()
            remaining %= MILLIS_PER_HOUR

            val minute = (remaining / MILLIS_PER_MINUTE).toInt()
            remaining %= MILLIS_PER_MINUTE

            val second = (remaining / MILLIS_PER_SECOND).toInt()
            remaining %= MILLIS_PER_SECOND

            val nanosecond = (remaining * NANOS_PER_MILLI).toInt()

            return VerdandiLocalTime(hour, minute, second, nanosecond)
        }

        fun fromNanosecondOfDay(nanosecondOfDay: Long): VerdandiLocalTime {
            verdandiRequireValidation(nanosecondOfDay in 0..<NANOS_PER_DAY) {
                "Nanosecond of day must be in 0..${NANOS_PER_DAY - 1}, was $nanosecondOfDay"
            }

            var remaining = nanosecondOfDay

            val hour = (remaining / NANOS_PER_HOUR).toInt()
            remaining %= NANOS_PER_HOUR

            val minute = (remaining / NANOS_PER_MINUTE).toInt()
            remaining %= NANOS_PER_MINUTE

            val second = (remaining / NANOS_PER_SECOND).toInt()
            val nanosecond = (remaining % NANOS_PER_SECOND).toInt()

            return VerdandiLocalTime(hour, minute, second, nanosecond)
        }

        fun parse(isoString: String): VerdandiLocalTime {
            val timeComponents = TemporalParser.parseTime(isoString.trim())

            return of(
                timeComponents.hour,
                timeComponents.minute,
                timeComponents.second,
                timeComponents.nanosecond
            )
        }
    }
}
