package com.github.abraga.verdandi.internal.core

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_COMMON_YEAR
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_LEAP_YEAR
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.FIRST_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_PER_YEAR
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.YEARS_RANGE
import com.github.abraga.verdandi.internal.core.executor.EpochCalculator
import com.github.abraga.verdandi.internal.core.executor.TemporalParser
import com.github.abraga.verdandi.internal.core.unit.VerdandiDayOfWeek
import com.github.abraga.verdandi.internal.core.unit.VerdandiMonth
import com.github.abraga.verdandi.internal.extension.capitalized
import com.github.abraga.verdandi.internal.extension.verdandiRequireRangeValidation
import com.github.abraga.verdandi.internal.extension.verdandiRequireValidation
import com.github.abraga.verdandi.internal.format.iso.IsoDateFormatter
import com.github.abraga.verdandi.internal.util.MathUtils

internal class VerdandiLocalDate private constructor(
    val year: Int,
    val monthNumber: Int,
    val dayOfMonth: Int
) : Comparable<VerdandiLocalDate> {

    val month: VerdandiMonth
        get() = VerdandiMonth.of(monthNumber)

    val dayOfWeek: VerdandiDayOfWeek
        get() = VerdandiDayOfWeek.fromEpochDays(toEpochDays())

    val dayOfYear: Int
        get() {
            var result = dayOfMonth

            for (monthIndex in 1 until monthNumber) {
                result += VerdandiMonth.of(monthIndex).lengthInYear(year)
            }

            return result
        }

    val isLeapYear: Boolean
        get() = VerdandiMonth.isLeapYear(year)

    val lengthOfMonth: Int
        get() = month.lengthInYear(year)

    val lengthOfYear: Int
        get() = if (isLeapYear) DAYS_PER_LEAP_YEAR else DAYS_PER_COMMON_YEAR

    fun toEpochDays(): Long {
        return EpochCalculator.daysSinceEpoch(year, monthNumber, dayOfMonth)
    }

    fun plusDays(days: Long): VerdandiLocalDate {
        if (days == 0L) {
            return this
        }

        val newEpochDays = toEpochDays() + days

        return fromEpochDays(newEpochDays)
    }

    fun minusDays(days: Long): VerdandiLocalDate {
        return plusDays(-days)
    }

    fun plusWeeks(weeks: Long): VerdandiLocalDate {
        return plusDays(weeks * DAYS_PER_WEEK)
    }

    fun minusWeeks(weeks: Long): VerdandiLocalDate {
        return plusWeeks(-weeks)
    }

    fun plusMonths(months: Long): VerdandiLocalDate {
        if (months == 0L) {
            return this
        }

        val totalMonths = year * MONTHS_PER_YEAR.toLong() + (monthNumber - 1) + months
        val newYear = MathUtils.floorDiv(totalMonths, MONTHS_PER_YEAR).toInt()
        val newMonth = MathUtils.floorMod(totalMonths, MONTHS_PER_YEAR).toInt() + 1

        validateYearRange(newYear.toLong())

        val newDay = dayOfMonth.coerceAtMost(VerdandiMonth.of(newMonth).lengthInYear(newYear))

        return VerdandiLocalDate(newYear, newMonth, newDay)
    }

    fun minusMonths(months: Long): VerdandiLocalDate {
        return plusMonths(-months)
    }

    fun plusYears(years: Long): VerdandiLocalDate {
        if (years == 0L) {
            return this
        }

        val newYearLong = year.toLong() + years

        validateYearRange(newYearLong)

        val newYear = newYearLong.toInt()
        val newDay = dayOfMonth.coerceAtMost(VerdandiMonth.of(monthNumber).lengthInYear(newYear))

        return VerdandiLocalDate(newYear, monthNumber, newDay)
    }

    fun minusYears(years: Long): VerdandiLocalDate {
        return plusYears(-years)
    }

    fun atTime(time: VerdandiLocalTime): VerdandiLocalDateTime {
        return VerdandiLocalDateTime.of(this, time)
    }

    fun atTime(hour: Int, minute: Int, second: Int = 0, nanosecond: Int = 0): VerdandiLocalDateTime {
        return VerdandiLocalDateTime.of(this, VerdandiLocalTime.of(hour, minute, second, nanosecond))
    }

    fun atStartOfDay(): VerdandiLocalDateTime {
        return VerdandiLocalDateTime.of(this, VerdandiLocalTime.Midnight)
    }

    override fun compareTo(other: VerdandiLocalDate): Int {
        var result = year.compareTo(other.year)

        if (result != 0) {
            return result
        }

        result = monthNumber.compareTo(other.monthNumber)

        if (result != 0) {
            return result
        }

        return dayOfMonth.compareTo(other.dayOfMonth)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is VerdandiLocalDate) {
            return false
        }

        return year == other.year &&
            monthNumber == other.monthNumber &&
            dayOfMonth == other.dayOfMonth
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + monthNumber
        result = 31 * result + dayOfMonth

        return result
    }

    override fun toString(): String {
        return IsoDateFormatter.format(year, monthNumber, dayOfMonth)
    }

    companion object {

        fun of(year: Int, monthNumber: Int, dayOfMonth: Int): VerdandiLocalDate {
            verdandiRequireRangeValidation(monthNumber, MONTHS_RANGE)

            val month = VerdandiMonth.of(monthNumber)
            val maxDays = month.lengthInYear(year)

            verdandiRequireValidation(dayOfMonth in FIRST_DAY..maxDays) {
                "'Day' must be between $FIRST_DAY and $maxDays for" +
                        " $year/${month.name.capitalized()}, but it was $dayOfMonth."
            }

            return VerdandiLocalDate(year, monthNumber, dayOfMonth)
        }

        fun of(year: Int, month: VerdandiMonth, dayOfMonth: Int): VerdandiLocalDate {
            return of(year, month.number, dayOfMonth)
        }

        fun fromEpochDays(epochDays: Long): VerdandiLocalDate {
            val dateFromEpoch = EpochCalculator.fromEpochDays(epochDays)

            return VerdandiLocalDate(dateFromEpoch.year, dateFromEpoch.month, dateFromEpoch.day)
        }

        fun parse(isoString: String): VerdandiLocalDate {
            val dateComponents = TemporalParser.parseDate(isoString.trim())

            return of(dateComponents.year, dateComponents.month, dateComponents.day)
        }

        private fun validateYearRange(year: Long) {
            verdandiRequireRangeValidation(year.toInt(), YEARS_RANGE, "Year")
        }
    }
}
