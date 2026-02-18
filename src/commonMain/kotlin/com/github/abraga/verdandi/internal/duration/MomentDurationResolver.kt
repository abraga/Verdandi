package com.github.abraga.verdandi.internal.duration

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_PER_YEAR
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import kotlin.time.Duration.Companion.milliseconds

internal object MomentDurationResolver {

    fun resolve(from: VerdandiMoment, to: VerdandiMoment): DateTimeDuration {
        val fromLocal = from.resolveTimeZoneContext().toLocalDateTime(from.epoch)
        val toLocal = to.resolveTimeZoneContext().toLocalDateTime(to.epoch)

        val (start, end) = if (from.epoch <= to.epoch) {
            fromLocal to toLocal
        } else {
            toLocal to fromLocal
        }

        val timeDifference = resolveTimeDifference(start, end)
        val totalMonths = resolveTotalMonths(start, end, timeDifference.dayAdjust)
        val remainingDays = resolveRemainingDays(start, end, totalMonths, timeDifference.dayAdjust)

        return DateTimeDuration.from(
            years = (totalMonths / MONTHS_PER_YEAR).toInt(),
            months = (totalMonths % MONTHS_PER_YEAR).toInt(),
            days = remainingDays,
            time = timeDifference.milliseconds.milliseconds
        )
    }

    private fun resolveTimeDifference(
        start: VerdandiLocalDateTime,
        end: VerdandiLocalDateTime
    ): TimeDifference {
        val startTimeMs = start.time.millisecondOfDay
        val endTimeMs = end.time.millisecondOfDay
        val rawDiff = endTimeMs - startTimeMs

        if (rawDiff < 0) {
            return TimeDifference(
                milliseconds = rawDiff + MILLIS_PER_DAY,
                dayAdjust = -1
            )
        }

        return TimeDifference(
            milliseconds = rawDiff,
            dayAdjust = 0
        )
    }

    private fun resolveTotalMonths(
        start: VerdandiLocalDateTime,
        end: VerdandiLocalDateTime,
        dayAdjust: Long
    ): Long {
        var totalMonths = (end.year - start.year).toLong() * MONTHS_PER_YEAR +
                (end.monthNumber - start.monthNumber)

        if (end.dayOfMonth + dayAdjust.toInt() < start.dayOfMonth) {
            totalMonths--
        }

        return totalMonths
    }

    private fun resolveRemainingDays(
        start: VerdandiLocalDateTime,
        end: VerdandiLocalDateTime,
        totalMonths: Long,
        dayAdjust: Long
    ): Int {
        val intermediate = start.date.plusMonths(totalMonths)

        return (end.date.toEpochDays() - intermediate.toEpochDays() + dayAdjust).toInt()
    }

    private data class TimeDifference(val milliseconds: Long, val dayAdjust: Long)
}
