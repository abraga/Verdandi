package com.github.abraga.verdandi.internal.core.executor

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_100_YEAR_CYCLE
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_400_YEAR_CYCLE
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_4_YEAR_CYCLE
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_COMMON_YEAR
import com.github.abraga.verdandi.internal.DateTimeConstants.EPOCH_OFFSET_DAYS
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_SECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.util.MathUtils

internal object EpochCalculator {

    private const val MARCH_BASED_MONTH_ADJUSTMENT = 12
    private const val MARCH_OFFSET = 3
    private const val YEAR_ADJUSTMENT_THRESHOLD = 2
    private const val YEARS_PER_ERA = 400
    private const val MARCH_BASED_MONTH_PIVOT = 10
    private const val CIVIL_MONTH_OFFSET = 9

    private object CivilCalendarCoefficients {

        const val MONTH_TO_DAY_NUMERATOR = 153
        const val MONTH_TO_DAY_OFFSET = 2
        const val MONTH_TO_DAY_DENOMINATOR = 5
        const val DAY_TO_MONTH_NUMERATOR = 5
    }

    fun daysSinceEpoch(year: Int, month: Int, day: Int): Long {
        val adjustedYear: Int
        val adjustedMonth: Int

        if (month <= YEAR_ADJUSTMENT_THRESHOLD) {
            adjustedYear = year - 1
            adjustedMonth = month + MARCH_BASED_MONTH_ADJUSTMENT
        } else {
            adjustedYear = year
            adjustedMonth = month
        }

        val era = computeEra(adjustedYear)
        val yearOfEra = adjustedYear - era * YEARS_PER_ERA
        val dayOfYear = computeDayOfYearFromMarchBasedMonth(adjustedMonth, day)
        val dayOfEra = computeDayOfEra(yearOfEra, dayOfYear)

        return era * DAYS_PER_400_YEAR_CYCLE + dayOfEra - EPOCH_OFFSET_DAYS
    }

    fun fromEpochDays(epochDays: Long): DateFromEpoch {
        val shiftedDays = epochDays + EPOCH_OFFSET_DAYS

        val era = computeEraFromShiftedDays(shiftedDays)
        val dayOfEra = (shiftedDays - era * DAYS_PER_400_YEAR_CYCLE).toInt()
        val yearOfEra = computeYearOfEra(dayOfEra)
        val computedYear = yearOfEra + era * YEARS_PER_ERA
        val dayOfYear = computeDayOfYear(dayOfEra, yearOfEra)
        val marchBasedMonth = computeMarchBasedMonth(dayOfYear)

        val day = computeDayOfMonth(dayOfYear, marchBasedMonth)
        val month = convertToCalendarMonth(marchBasedMonth)
        val year = computeFinalYear(computedYear, month)

        return DateFromEpoch(year, month, day)
    }

    fun toEpochMillis(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nanosecond: Int
    ): Long {
        val days = daysSinceEpoch(year, month, day)

        val timeMillis = hour * MILLIS_PER_HOUR +
                minute * MILLIS_PER_MINUTE +
                second * MILLIS_PER_SECOND +
                nanosecond / NANOS_PER_MILLI

        val dayMillis = MathUtils.multiplyExact(days, MILLIS_PER_DAY)

        return MathUtils.addExact(dayMillis, timeMillis)
    }

    private fun computeEra(adjustedYear: Int): Int {
        return if (adjustedYear >= 0) {
            adjustedYear / YEARS_PER_ERA
        } else {
            (adjustedYear - YEARS_PER_ERA + 1) / YEARS_PER_ERA
        }
    }

    private fun computeEraFromShiftedDays(shiftedDays: Long): Long {
        return if (shiftedDays >= 0) {
            shiftedDays / DAYS_PER_400_YEAR_CYCLE
        } else {
            (shiftedDays - DAYS_PER_400_YEAR_CYCLE + 1) / DAYS_PER_400_YEAR_CYCLE
        }
    }

    private fun computeDayOfYearFromMarchBasedMonth(marchBasedMonth: Int, day: Int): Int {
        val monthDays = with(CivilCalendarCoefficients) {
            (MONTH_TO_DAY_NUMERATOR * (marchBasedMonth - MARCH_OFFSET) + MONTH_TO_DAY_OFFSET) / MONTH_TO_DAY_DENOMINATOR
        }

        return monthDays + day - 1
    }

    private fun computeDayOfEra(yearOfEra: Int, dayOfYear: Int): Int {
        return yearOfEra * DAYS_PER_COMMON_YEAR +
                yearOfEra / 4 -
                yearOfEra / 100 +
                dayOfYear
    }

    private fun computeYearOfEra(dayOfEra: Int): Int {
        val leapYearAdjustment = dayOfEra / DAYS_PER_4_YEAR_CYCLE -
                dayOfEra / DAYS_PER_100_YEAR_CYCLE +
                dayOfEra / (DAYS_PER_400_YEAR_CYCLE.toInt() - 1)

        return (dayOfEra - leapYearAdjustment) / DAYS_PER_COMMON_YEAR
    }

    private fun computeDayOfYear(dayOfEra: Int, yearOfEra: Int): Int {
        return dayOfEra - (DAYS_PER_COMMON_YEAR * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    }

    private fun computeMarchBasedMonth(dayOfYear: Int): Int {
        return with(CivilCalendarCoefficients) {
            (DAY_TO_MONTH_NUMERATOR * dayOfYear + MONTH_TO_DAY_OFFSET) / MONTH_TO_DAY_NUMERATOR
        }
    }

    private fun computeDayOfMonth(dayOfYear: Int, marchBasedMonth: Int): Int {
        val monthStartDay = with(CivilCalendarCoefficients) {
            (MONTH_TO_DAY_NUMERATOR * marchBasedMonth + MONTH_TO_DAY_OFFSET) / MONTH_TO_DAY_DENOMINATOR
        }

        return dayOfYear - monthStartDay + 1
    }

    private fun convertToCalendarMonth(marchBasedMonth: Int): Int {
        return if (marchBasedMonth < MARCH_BASED_MONTH_PIVOT) {
            marchBasedMonth + MARCH_OFFSET
        } else {
            marchBasedMonth - CIVIL_MONTH_OFFSET
        }
    }

    private fun computeFinalYear(computedYear: Long, month: Int): Int {
        val yearAdjustment = if (month <= YEAR_ADJUSTMENT_THRESHOLD) 1 else 0

        return (computedYear + yearAdjustment).toInt()
    }

    internal data class DateFromEpoch(
        val year: Int,
        val month: Int,
        val day: Int
    )
}
