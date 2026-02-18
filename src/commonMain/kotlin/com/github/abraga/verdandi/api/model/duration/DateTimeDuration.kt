package com.github.abraga.verdandi.api.model.duration

import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.duration.DateTimeDurationFormatter
import com.github.abraga.verdandi.internal.extension.toVerdandi
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@JvmInline
value class DateTimeDuration private constructor(
    private val packed: Pair<DateDuration, Duration>
) : Comparable<DateTimeDuration> {

    val date: DateDuration
        get() = packed.first

    val time: Duration
        get() = packed.second

    val years: Int
        get() = date.years

    val months: Int
        get() = date.months

    val weeks: Int
        get() = date.weeks

    val days: Int
        get() = date.days

    val hours: Int
        get() = time.toComponents { hours, _, _, _ -> hours.toInt() }

    val minutes: Int
        get() = time.toComponents { _, minutes, _, _ -> minutes }

    val seconds: Int
        get() = time.toComponents { _, _, seconds, _ -> seconds }

    val inWholeMilliseconds: Long
        get() {
            val dateMillis = date.inWholeMilliseconds
            val timeMillis = time.inWholeMilliseconds

            val result = dateMillis + timeMillis

            if ((dateMillis xor timeMillis) >= 0 && (dateMillis xor result) < 0) {
                throw ArithmeticException(
                    "Overflow computing total milliseconds for DateTimeDuration"
                ).toVerdandi()
            }

            return result
        }

    val isZero: Boolean
        get() = date == DateDuration.Zero && time == Duration.ZERO

    override fun toString(): String {
        return formatted()
    }

    override fun compareTo(other: DateTimeDuration): Int {
        val dateComparison = date.compareTo(other.date)

        if (dateComparison != 0) {
            return dateComparison
        }

        return time.compareTo(other.time)
    }

    operator fun plus(other: DateTimeDuration): DateTimeDuration {
        return from(date + other.date, time + other.time)
    }

    operator fun plus(other: DateDuration): DateTimeDuration {
        return from(date + other, time)
    }

    operator fun plus(other: Duration): DateTimeDuration {
        return from(date, time + other)
    }

    operator fun minus(other: DateTimeDuration): DateTimeDuration {
        return from(date - other.date, time - other.time)
    }

    operator fun minus(other: DateDuration): DateTimeDuration {
        return from(date - other, time)
    }

    operator fun minus(other: Duration): DateTimeDuration {
        return from(date, time - other)
    }

    operator fun times(scalar: Int): DateTimeDuration {
        return from(date * scalar, time * scalar)
    }

    operator fun div(scalar: Int): DateTimeDuration {
        if (scalar == 0) {
            throw ArithmeticException("Division by zero").toVerdandi()
        }

        return from(date / scalar, time / scalar)
    }

    operator fun unaryMinus(): DateTimeDuration {
        return from(date * -1, -time)
    }

    fun toDuration(): Duration {
        return inWholeMilliseconds.milliseconds
    }


    fun formatted(
        includeSubSeconds: Boolean = true,
        unitSeparator: String = "",
        componentSeparator: String = " ",
    ): String {
        if (isZero) {
            return "0${unitSeparator}${if (includeSubSeconds) "ms" else "s"}"
        }

        val timePart = time

        if (timePart.isInfinite()) {
            return if (timePart.isPositive()) "∞" else "-∞"
        }

        return timePart.toComponents { _, hours, minutes, seconds, nanoseconds ->
            val components = buildList {
                add(years to "y")
                add(months to "mo")
                add(weeks to "w")
                add(days to "d")
                add(hours to "h")
                add(minutes to "m")
                add(seconds to "s")

                if (includeSubSeconds) {
                    addSubSecondComponent(nanoseconds)
                }
            }

            DateTimeDurationFormatter.format(
                components = components,
                unitSeparator = unitSeparator,
                componentSeparator = componentSeparator,
            )
        }
    }

    companion object {

        val Zero: DateTimeDuration = DateTimeDuration(DateDuration.Zero to Duration.ZERO)

        fun from(
            date: DateDuration = DateDuration.Zero,
            time: Duration = Duration.ZERO
        ): DateTimeDuration {
            val normalizedTime = normalizeTime(time)
            val dateDuration = date + normalizedTime.first

            return DateTimeDuration(dateDuration to normalizedTime.second)
        }

        fun from(
            years: Int = 0,
            months: Int = 0,
            weeks: Int = 0,
            days: Int = 0,
            time: Duration = Duration.ZERO
        ): DateTimeDuration {
            val datePart = DateDuration.from(
                years = years,
                months = months,
                weeks = weeks,
                days = days
            )

            return from(datePart, time)
        }

        private fun normalizeTime(duration: Duration): Pair<DateDuration, Duration> {
            if (duration == Duration.ZERO) {
                return DateDuration.Zero to Duration.ZERO
            }

            val totalMillis = duration.inWholeMilliseconds
            val days = (totalMillis / MILLIS_PER_DAY).toInt()
            val remainingMillis = totalMillis % MILLIS_PER_DAY

            return DateDuration.from(days = days) to remainingMillis.milliseconds
        }
    }
}
