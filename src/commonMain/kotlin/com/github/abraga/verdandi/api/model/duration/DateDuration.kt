package com.github.abraga.verdandi.api.model.duration

import com.github.abraga.verdandi.internal.duration.DateTimeDurationFormatter
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.extension.toVerdandi
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@JvmInline
value class DateDuration private constructor(private val packed: Long) : Comparable<DateDuration> {

    val years: Int
        get() = (packed shr 48).toInt()

    val months: Int
        get() = (packed shr 32 and 0xFFFF).toInt()

    val weeks: Int
        get() = (packed shr 16 and 0xFFFF).toInt()

    val days: Int
        get() = (packed and 0xFFFF).toInt()

    val inWholeDays: Int
        get() {
            if (years != 0 || months != 0) {
                throw UnsupportedOperationException(
                    "Cannot convert DateDuration with years ($years)" +
                            " or months ($months) to whole days without a reference date."
                ).toVerdandi()
            }

            return weeks * DAYS_PER_WEEK + days
        }

    val inWholeWeeks: Int
        get() {
            if (years != 0 || months != 0) {
                throw UnsupportedOperationException(
                    "Cannot convert DateDuration with years ($years)" +
                            " or months ($months) to whole weeks without a reference date."
                ).toVerdandi()
            }

            return weeks + (days / DAYS_PER_WEEK)
        }

    val inWholeMilliseconds: Long
        get() {
            if (years != 0 || months != 0) {
                throw UnsupportedOperationException(
                    "Cannot convert DateDuration with years ($years)" +
                            " or months ($months) to milliseconds. Use inWholeDays" +
                            " for week/day-only durations, or apply to a specific date."
                ).toVerdandi()
            }

            val totalDays = inWholeDays.toLong()

            if (totalDays > Long.MAX_VALUE / MILLIS_PER_DAY) {
                throw ArithmeticException(
                    "Overflow computing milliseconds for DateDuration"
                ).toVerdandi()
            }

            return totalDays * MILLIS_PER_DAY
        }

    override fun toString(): String {
        return formatted()
    }

    override fun compareTo(other: DateDuration): Int {
        if (years != other.years || months != other.months) {
            throw UnsupportedOperationException(
                "Cannot compare DateDurations with different year/month" +
                        " components without a reference date."
            ).toVerdandi()
        }

        return inWholeDays.compareTo(other.inWholeDays)
    }

    operator fun plus(other: DateDuration): DateDuration {
        return from(
            years = addExact(years, other.years),
            months = addExact(months, other.months),
            weeks = addExact(weeks, other.weeks),
            days = addExact(days, other.days)
        )
    }

    operator fun plus(other: Duration): DateTimeDuration {
        val normalizedTime = normalizeTime(other)
        val combinedDate = this + normalizedTime.first

        return DateTimeDuration.from(combinedDate, normalizedTime.second)
    }

    operator fun minus(other: DateDuration): DateDuration {
        return from(
            years = subtractExact(years, other.years),
            months = subtractExact(months, other.months),
            weeks = subtractExact(weeks, other.weeks),
            days = subtractExact(days, other.days)
        )
    }

    operator fun minus(other: Duration): DateTimeDuration {
        val normalizedTime = normalizeTime(other)
        val combinedDate = this - normalizedTime.first

        return DateTimeDuration.from(combinedDate, -normalizedTime.second)
    }

    operator fun times(scalar: Int): DateDuration {
        return from(
            years = multiplyExact(years, scalar),
            months = multiplyExact(months, scalar),
            weeks = multiplyExact(weeks, scalar),
            days = multiplyExact(days, scalar)
        )
    }

    operator fun div(scalar: Int): DateDuration {
        if (scalar == 0) {
            throw ArithmeticException("Division by zero").toVerdandi()
        }

        return from(
            years = years / scalar,
            months = months / scalar,
            weeks = weeks / scalar,
            days = days / scalar
        )
    }

    fun formatted(
        unitSeparator: String = "",
        componentSeparator: String = " ",
    ): String {
        if (this == Zero) {
            return "0${unitSeparator}d"
        }

        val components = listOf(
            years to "y",
            months to "mo",
            weeks to "w",
            days to "d"
        )

        return DateTimeDurationFormatter.format(
            components = components,
            unitSeparator = unitSeparator,
            componentSeparator = componentSeparator,
        )
    }

    companion object {

        val Zero: DateDuration = DateDuration(0L)

        fun from(
            years: Int = 0,
            months: Int = 0,
            weeks: Int = 0,
            days: Int = 0
        ): DateDuration {
            val normalizedWeeks = weeks + (days / DAYS_PER_WEEK)
            val normalizedDays = days % DAYS_PER_WEEK

            val packed = (years.toLong() shl 48) or
                    ((months.toLong() and 0xFFFF) shl 32) or
                    ((normalizedWeeks.toLong() and 0xFFFF) shl 16) or
                    (normalizedDays.toLong() and 0xFFFF)

            return DateDuration(packed)
        }

        private fun normalizeTime(duration: Duration): Pair<DateDuration, Duration> {
            if (duration == Duration.ZERO) {
                return Zero to Duration.ZERO
            }

            val totalMillis = duration.inWholeMilliseconds
            val days = (totalMillis / MILLIS_PER_DAY).toInt()
            val remainingMillis = totalMillis % MILLIS_PER_DAY

            return from(days = days) to remainingMillis.milliseconds
        }

        private fun addExact(a: Int, b: Int): Int {
            val result = a.toLong() + b.toLong()

            if (result < Int.MIN_VALUE || result > Int.MAX_VALUE) {
                throw ArithmeticException(
                    "Integer overflow in addition"
                ).toVerdandi()
            }

            return result.toInt()
        }

        private fun subtractExact(a: Int, b: Int): Int {
            val result = a.toLong() - b.toLong()

            if (result < Int.MIN_VALUE || result > Int.MAX_VALUE) {
                throw ArithmeticException(
                    "Integer overflow in subtraction"
                ).toVerdandi()
            }

            return result.toInt()
        }

        private fun multiplyExact(a: Int, b: Int): Int {
            val result = a.toLong() * b.toLong()

            if (result < Int.MIN_VALUE || result > Int.MAX_VALUE) {
                throw ArithmeticException(
                    "Integer overflow in multiplication"
                ).toVerdandi()
            }

            return result.toInt()
        }
    }
}
