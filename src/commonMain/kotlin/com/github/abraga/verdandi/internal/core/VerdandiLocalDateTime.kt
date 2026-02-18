package com.github.abraga.verdandi.internal.core

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_SECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_DAY
import com.github.abraga.verdandi.internal.core.executor.TemporalParser
import com.github.abraga.verdandi.internal.core.unit.VerdandiDayOfWeek
import com.github.abraga.verdandi.internal.core.unit.VerdandiMonth
import com.github.abraga.verdandi.internal.extension.verdandiRequireParse
import com.github.abraga.verdandi.internal.util.MathUtils

internal class VerdandiLocalDateTime private constructor(
    val date: VerdandiLocalDate,
    val time: VerdandiLocalTime
) : Comparable<VerdandiLocalDateTime> {

    val year: Int
        get() = date.year

    val monthNumber: Int
        get() = date.monthNumber

    val month: VerdandiMonth
        get() = date.month

    val dayOfMonth: Int
        get() = date.dayOfMonth

    val dayOfWeek: VerdandiDayOfWeek
        get() = date.dayOfWeek

    val dayOfYear: Int
        get() = date.dayOfYear

    val hour: Int
        get() = time.hour

    val minute: Int
        get() = time.minute

    val second: Int
        get() = time.second

    val nanosecond: Int
        get() = time.nanosecond

    internal fun toEpochMillisecondsUtc(): Long {
        val dayMillis = MathUtils.multiplyExact(date.toEpochDays(), MILLIS_PER_DAY)

        return MathUtils.addExact(dayMillis, time.millisecondOfDay)
    }

    fun plusNanos(nanos: Long): VerdandiLocalDateTime {
        if (nanos == 0L) {
            return this
        }

        val currentNanos = time.nanosecondOfDay
        val totalNanos = currentNanos + nanos
        val newNanosOfDay = MathUtils.floorMod(totalNanos, NANOS_PER_DAY)
        val daysToAdd = MathUtils.floorDiv(totalNanos, NANOS_PER_DAY)

        val newTime = VerdandiLocalTime.fromNanosecondOfDay(newNanosOfDay)
        val newDate = date.plusDays(daysToAdd)

        return VerdandiLocalDateTime(newDate, newTime)
    }

    fun plusMillis(millis: Long): VerdandiLocalDateTime {
        if (millis == 0L) {
            return this
        }

        val currentMillis = time.millisecondOfDay
        val totalMillis = currentMillis + millis
        val newMillisOfDay = MathUtils.floorMod(totalMillis, MILLIS_PER_DAY).toInt()
        val daysToAdd = MathUtils.floorDiv(totalMillis, MILLIS_PER_DAY)

        val baseTime = VerdandiLocalTime.fromMillisecondOfDay(newMillisOfDay)
        val newTime = VerdandiLocalTime.of(
            hour = baseTime.hour,
            minute = baseTime.minute,
            second = baseTime.second,
            nanosecond = time.nanosecond
        )
        val newDate = date.plusDays(daysToAdd)

        return VerdandiLocalDateTime(newDate, newTime)
    }

    fun plusSeconds(seconds: Long): VerdandiLocalDateTime {
        return plusMillis(seconds * MILLIS_PER_SECOND)
    }

    fun plusMinutes(minutes: Long): VerdandiLocalDateTime {
        return plusMillis(minutes * MILLIS_PER_MINUTE)
    }

    fun plusHours(hours: Long): VerdandiLocalDateTime {
        return plusMillis(hours * MILLIS_PER_HOUR)
    }

    fun plusDays(days: Long): VerdandiLocalDateTime {
        return VerdandiLocalDateTime(date.plusDays(days), time)
    }

    fun plusWeeks(weeks: Long): VerdandiLocalDateTime {
        return plusDays(weeks * DAYS_PER_WEEK)
    }

    fun plusMonths(months: Long): VerdandiLocalDateTime {
        return VerdandiLocalDateTime(date.plusMonths(months), time)
    }

    fun plusYears(years: Long): VerdandiLocalDateTime {
        return VerdandiLocalDateTime(date.plusYears(years), time)
    }

    fun minusNanos(nanos: Long): VerdandiLocalDateTime {
        return plusNanos(-nanos)
    }

    fun minusMillis(millis: Long): VerdandiLocalDateTime {
        return plusMillis(-millis)
    }

    fun minusSeconds(seconds: Long): VerdandiLocalDateTime {
        return plusSeconds(-seconds)
    }

    fun minusMinutes(minutes: Long): VerdandiLocalDateTime {
        return plusMinutes(-minutes)
    }

    fun minusHours(hours: Long): VerdandiLocalDateTime {
        return plusHours(-hours)
    }

    fun minusDays(days: Long): VerdandiLocalDateTime {
        return plusDays(-days)
    }

    fun minusWeeks(weeks: Long): VerdandiLocalDateTime {
        return plusWeeks(-weeks)
    }

    fun minusMonths(months: Long): VerdandiLocalDateTime {
        return plusMonths(-months)
    }

    fun minusYears(years: Long): VerdandiLocalDateTime {
        return plusYears(-years)
    }

    fun withDate(newDate: VerdandiLocalDate): VerdandiLocalDateTime {
        return VerdandiLocalDateTime(newDate, time)
    }

    fun withTime(newTime: VerdandiLocalTime): VerdandiLocalDateTime {
        return VerdandiLocalDateTime(date, newTime)
    }

    fun withComponents(
        year: Int = this.year,
        monthNumber: Int = this.monthNumber,
        dayOfMonth: Int = this.dayOfMonth,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second,
        nanosecond: Int = this.nanosecond
    ): VerdandiLocalDateTime {
        return of(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond)
    }

    override fun compareTo(other: VerdandiLocalDateTime): Int {
        val dateComparison = date.compareTo(other.date)

        if (dateComparison != 0) {
            return dateComparison
        }

        return time.compareTo(other.time)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is VerdandiLocalDateTime) {
            return false
        }

        return date == other.date && time == other.time
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + time.hashCode()

        return result
    }

    override fun toString(): String {
        return "${date}T${time}"
    }

    companion object {

        fun of(date: VerdandiLocalDate, time: VerdandiLocalTime): VerdandiLocalDateTime {
            return VerdandiLocalDateTime(date, time)
        }

        fun of(
            year: Int,
            monthNumber: Int,
            dayOfMonth: Int,
            hour: Int = 0,
            minute: Int = 0,
            second: Int = 0,
            nanosecond: Int = 0
        ): VerdandiLocalDateTime {
            val date = VerdandiLocalDate.of(year, monthNumber, dayOfMonth)
            val time = VerdandiLocalTime.of(hour, minute, second, nanosecond)

            return VerdandiLocalDateTime(date, time)
        }

        internal fun fromEpochMillisecondsUtc(
            epochMillis: Long,
            nanosecondOfSecond: Int = 0
        ): VerdandiLocalDateTime {
            val epochDays = MathUtils.floorDiv(epochMillis, MILLIS_PER_DAY)
            val millisOfDay = MathUtils.floorMod(epochMillis, MILLIS_PER_DAY).toInt()

            val date = VerdandiLocalDate.fromEpochDays(epochDays)
            val baseTime = VerdandiLocalTime.fromMillisecondOfDay(millisOfDay)

            val time = if (nanosecondOfSecond != 0) {
                VerdandiLocalTime.of(
                    hour = baseTime.hour,
                    minute = baseTime.minute,
                    second = baseTime.second,
                    nanosecond = nanosecondOfSecond
                )
            } else {
                baseTime
            }

            return VerdandiLocalDateTime(date, time)
        }

        fun parse(isoString: String): VerdandiLocalDateTime {
            val text = isoString.trim()
            val separatorIndex = TemporalParser.findDateTimeSeparator(text)

            verdandiRequireParse(separatorIndex > 0) {
                "Invalid ISO date-time: $isoString"
            }

            val datePart = text.take(separatorIndex)
            val timePart = text.substring(separatorIndex + 1)

            val dateComponents = TemporalParser.parseDate(datePart)
            val timeComponents = TemporalParser.parseTime(timePart)

            val date = VerdandiLocalDate.of(
                year = dateComponents.year,
                monthNumber = dateComponents.month,
                dayOfMonth = dateComponents.day
            )

            val time = VerdandiLocalTime.of(
                hour = timeComponents.hour,
                minute = timeComponents.minute,
                second = timeComponents.second,
                nanosecond = timeComponents.nanosecond
            )

            return VerdandiLocalDateTime(date, time)
        }
    }
}
