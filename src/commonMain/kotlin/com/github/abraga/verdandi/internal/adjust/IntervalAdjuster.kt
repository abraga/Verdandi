package com.github.abraga.verdandi.internal.adjust

import com.github.abraga.verdandi.api.scope.adjust.VerdandiIntervalAdjustScope
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.core.VerdandiLocalTime
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext
import com.github.abraga.verdandi.internal.util.MathUtils.shrinkInterval
import kotlin.time.Duration

internal class IntervalAdjuster(
    startMillis: Long,
    endMillis: Long,
    private val timeZoneContext: TimeZoneContext
) : VerdandiIntervalAdjustScope {

    var adjustedStartMillis: Long = startMillis
        private set

    var adjustedEndMillis: Long = endMillis
        private set

    override fun shiftBoth(duration: Duration) {
        val delta = duration.inWholeMilliseconds

        adjustedStartMillis += delta
        adjustedEndMillis += delta
    }

    override fun shiftStart(duration: Duration) {
        adjustedStartMillis += duration.inWholeMilliseconds
    }

    override fun shiftEnd(duration: Duration) {
        adjustedEndMillis += duration.inWholeMilliseconds
    }

    override fun expandBoth(duration: Duration) {
        val delta = duration.inWholeMilliseconds

        adjustedStartMillis -= delta
        adjustedEndMillis += delta
    }

    override fun shrinkBoth(duration: Duration) {
        val (newStart, newEnd) = shrinkInterval(
            adjustedStartMillis,
            adjustedEndMillis,
            duration.inWholeMilliseconds
        )

        adjustedStartMillis = newStart
        adjustedEndMillis = newEnd
    }

    override fun alignToFullDays() {
        val startDateTime = toLocalDateTime(adjustedStartMillis)
        val startOfDay = startDateTime.date.atTime(VerdandiLocalTime.Midnight)

        adjustedStartMillis = toEpochMillis(startOfDay)

        val endDateTime = toLocalDateTime(adjustedEndMillis)
        val endOfDay = endDateTime.date.atTime(VerdandiLocalTime.Max)

        adjustedEndMillis = toEpochMillis(endOfDay)
    }

    override fun alignToFullMonths() {
        val startDate = toLocalDateTime(adjustedStartMillis).date
        val firstDayOfMonth = startDate.firstDayOfMonth

        adjustedStartMillis = toEpochMillis(firstDayOfMonth.atTime(VerdandiLocalTime.Midnight))

        val endDate = toLocalDateTime(adjustedEndMillis).date
        val lastDayOfMonth = endDate.lastDayOfMonth

        adjustedEndMillis = toEpochMillis(lastDayOfMonth.atTime(VerdandiLocalTime.Max))
    }

    override fun alignToFullYears() {
        val startDate = toLocalDateTime(adjustedStartMillis).date
        val firstDayOfYear = startDate.firstDayOfYear

        adjustedStartMillis = toEpochMillis(firstDayOfYear.atTime(VerdandiLocalTime.Midnight))

        val endDate = toLocalDateTime(adjustedEndMillis).date
        val lastDayOfYear = endDate.lastDayOfYear

        adjustedEndMillis = toEpochMillis(lastDayOfYear.atTime(VerdandiLocalTime.Max))
    }

    override fun setDurationFromStart(duration: Duration) {
        adjustedEndMillis = adjustedStartMillis + duration.inWholeMilliseconds
    }

    override fun setDurationFromEnd(duration: Duration) {
        adjustedStartMillis = adjustedEndMillis - duration.inWholeMilliseconds
    }

    override fun plusAssign(duration: Duration) {
        shiftBoth(duration)
    }

    override fun minusAssign(duration: Duration) {
        shiftBoth(-duration)
    }

    private fun toLocalDateTime(epochMillis: Long): VerdandiLocalDateTime {
        return timeZoneContext.toLocalDateTime(epochMillis)
    }

    private fun toEpochMillis(localDateTime: VerdandiLocalDateTime): Long {
        return timeZoneContext.toEpochMillis(localDateTime)
    }
}
