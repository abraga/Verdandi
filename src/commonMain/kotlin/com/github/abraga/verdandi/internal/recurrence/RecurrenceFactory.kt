package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.exception.VerdandiStateException
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.duration.months
import com.github.abraga.verdandi.api.model.duration.weeks
import com.github.abraga.verdandi.api.model.duration.years
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

internal class RecurrenceFactory(
    private val frequency: RecurrenceFrequency,
    private val interval: Int = 1,
    private val startMoment: VerdandiMoment,
    private val endMoment: VerdandiMoment? = null,
    private val dayOfWeek: RecurrenceDayOfWeek? = null,
    private val timeOffset: Duration? = null
) {

    fun toMoments(limit: Int?): VerdandiRecurrenceMoments {
        val momentsList = generateOccurrences(limit).toList()

        val rule = RecurrenceRule(
            frequency = frequency,
            interval = interval,
            daysOfWeek = if (dayOfWeek != null) setOf(dayOfWeek) else emptySet(),
            timeOffset = timeOffset,
            endBoundary = endMoment
        )

        return VerdandiRecurrenceMoments(
            moments = momentsList,
            startMoment = startMoment,
            endMoment = endMoment ?: momentsList.lastOrNull() ?: startMoment,
            rule = rule
        )
    }

    private fun generateOccurrences(limit: Int?): Sequence<VerdandiMoment> {
        return sequence {
            var count = 0
            var current = startMoment

            while (hasNextOccurrence(current, count, limit)) {
                if (matchesDayOfWeek(current)) {
                    yield(current)
                    count++
                }

                current = nextOccurrence(current)
            }
        }
    }

    private fun matchesDayOfWeek(moment: VerdandiMoment): Boolean {
        if (dayOfWeek == null) return true

        return moment.component.dayOfWeek.value == dayOfWeek.isoValue
    }

    private fun hasNextOccurrence(current: VerdandiMoment, count: Int, limit: Int?): Boolean {
        if (limit != null && count >= limit) {
            return false
        }

        if (endMoment != null && current isAfter endMoment) {
            return false
        }

        if (count >= DEFAULT_LIMIT) {
            throw VerdandiStateException(
                "Recurrence generation exceeded the default limit of $DEFAULT_LIMIT occurrences. " +
                        "Consider specifying a limit or a smaller limit value."
            )
        }

        return true
    }

    private fun nextOccurrence(current: VerdandiMoment): VerdandiMoment {
        return when (frequency) {
            RecurrenceFrequency.Daily -> current + interval.days
            RecurrenceFrequency.Weekly -> current + interval.weeks
            RecurrenceFrequency.Monthly -> nextMonthlyOccurrence(current)
            RecurrenceFrequency.Yearly -> nextYearlyOccurrence(current)
        }
    }

    private fun nextMonthlyOccurrence(current: VerdandiMoment): VerdandiMoment {
        if (dayOfWeek == null) {
            return current + interval.months
        }

        val nextWeek = current + 1.weeks

        if (nextWeek.component.month.value == current.component.month.value) {
            return nextWeek
        }

        val firstOfNextMonth = (current adjust { at startOf month }) + interval.months

        return adjustToTargetDayOfWeek(firstOfNextMonth, dayOfWeek)
    }

    private fun nextYearlyOccurrence(current: VerdandiMoment): VerdandiMoment {
        if (dayOfWeek == null) {
            return current + interval.years
        }

        val nextWeek = current + 1.weeks

        if (nextWeek.component.year.value == current.component.year.value) {
            return nextWeek
        }

        val firstOfNextYear = (current adjust { at startOf year }) + interval.years

        return adjustToTargetDayOfWeek(firstOfNextYear, dayOfWeek)
    }

    private fun adjustToTargetDayOfWeek(
        moment: VerdandiMoment,
        targetDay: RecurrenceDayOfWeek
    ): VerdandiMoment {
        val currentDayOfWeek = moment.component.dayOfWeek.value
        val daysToAdd = (targetDay.isoValue - currentDayOfWeek + 7).mod(7)
        val adjusted = moment + daysToAdd.days

        if (timeOffset != null) {
            return adjusted + timeOffset
        }

        return adjusted
    }

    companion object {

        private const val DEFAULT_LIMIT = 500
    }
}