package com.github.abraga.verdandi.api.model.duration

import com.github.abraga.verdandi.internal.DateTimeConstants.HOURS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MINUTES_RANGE
import com.github.abraga.verdandi.internal.duration.DateTimeDurationFormatter
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MICRO
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.extension.verdandiRequire
import com.github.abraga.verdandi.internal.extension.verdandiRequireRangeValidation
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

val Int.years: DateDuration
    get() = DateDuration.from(years = this)

val Int.months: DateDuration
    get() = DateDuration.from(months = this)

val Int.weeks: DateDuration
    get() = DateDuration.from(weeks = this)

val Int.days: DateDuration
    get() = DateDuration.from(days = this)

val Double.oClock: Duration
    get() {
        val hoursPart = this.toInt()
        var minutesPart = ((this - hoursPart.toDouble()) * 100.0).roundToInt()

        verdandiRequireRangeValidation(hoursPart, HOURS_RANGE, "o'clock")

        verdandiRequireRangeValidation(minutesPart, MINUTES_RANGE, "o'clock")

        if (minutesPart == 60) {
            minutesPart = 0
        }

        return hoursPart.hours + minutesPart.minutes
    }

val Double.am: Duration
    get() {
        val hoursPart = this.toInt()
        var minutesPart = ((this - hoursPart.toDouble()) * 100.0).roundToInt()

        verdandiRequireRangeValidation(hoursPart, HOURS_RANGE, "AM")

        verdandiRequireRangeValidation(minutesPart, MINUTES_RANGE, "AM")

        var normalizedHours = hoursPart % 12
        if (minutesPart == 60) {
            minutesPart = 0
            normalizedHours = (normalizedHours + 1) % 12
        }

        return normalizedHours.hours + minutesPart.minutes
    }

val Double.pm: Duration
    get() {
        val hoursPart = this.toInt()
        var minutesPart = ((this - hoursPart.toDouble()) * 100.0).roundToInt()

        verdandiRequireRangeValidation(hoursPart, HOURS_RANGE, "PM")

        verdandiRequireRangeValidation(minutesPart, MINUTES_RANGE, "PM")

        var hours12 = hoursPart % 12

        if (minutesPart == 60) {
            minutesPart = 0
            hours12 = (hours12 + 1) % 12
        }

        val hours24 = hours12 + 12

        return hours24.hours + minutesPart.minutes
    }

fun Duration.formatted(
    includeSubSeconds: Boolean = true,
    unitSeparator: String = "",
    componentSeparator: String = " ",
): String {
    if (this == Duration.ZERO) {
        return "0${unitSeparator}${if (includeSubSeconds) "ms" else "s"}"
    }

    if (isInfinite()) {
        return if (isPositive()) "∞" else "-∞"
    }

    return toComponents { days, hours, minutes, seconds, nanoseconds ->
        val hasTimeComponents = days > 0 || hours > 0 || minutes > 0 || seconds > 0

        val components = buildList {
            add(days.toInt() to "d")
            add(hours to "h")
            add(minutes to "m")
            add(seconds to "s")

            if (includeSubSeconds || hasTimeComponents.not()) {
                addSubSecondComponent(nanoseconds)
            }
        }

        DateTimeDurationFormatter.format(
            components = components,
            unitSeparator = unitSeparator,
            componentSeparator = componentSeparator
        )
    }
}

internal fun MutableList<Pair<Int, String>>.addSubSecondComponent(nanoseconds: Int) {
    if (nanoseconds == 0) {
        return
    }

    val (value, unit) = when {
        nanoseconds >= NANOS_PER_MILLI -> (nanoseconds / NANOS_PER_MILLI).toInt() to "ms"
        nanoseconds >= NANOS_PER_MICRO -> (nanoseconds / NANOS_PER_MICRO) to "µs"
        else -> nanoseconds to "ns"
    }

    add(value to unit)
}
