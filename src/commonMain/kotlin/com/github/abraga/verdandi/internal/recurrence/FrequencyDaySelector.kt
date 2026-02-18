package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.duration.days
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceDaySelector
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceEndMarker
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceRuleInProgress
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceTimeMarker

internal class FrequencyDaySelector(
    private val startMoment: VerdandiMoment,
    private val frequency: RecurrenceFrequency,
    private val interval: Int,
    private val limit: Int?
) : RecurrenceDaySelector {

    override fun mondays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Monday, end)
    }

    override fun tuesdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Tuesday, end)
    }

    override fun wednesdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Wednesday, end)
    }

    override fun thursdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Thursday, end)
    }

    override fun fridays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Friday, end)
    }

    override fun saturdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Saturday, end)
    }

    override fun sundays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceDayOfWeek.Sunday, end)
    }

    override fun weekdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildMultiDayRecurrence(WEEKDAY_DAYS, end)
    }

    override fun weekends(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildMultiDayRecurrence(WEEKEND_DAYS, end)
    }

    override fun mondays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Monday, time)
    }

    override fun tuesdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Tuesday, time)
    }

    override fun wednesdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Wednesday, time)
    }

    override fun thursdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Thursday, time)
    }

    override fun fridays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Friday, time)
    }

    override fun saturdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Saturday, time)
    }

    override fun sundays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceDayOfWeek.Sunday, time)
    }

    override fun weekdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildMultiDayRuleInProgress(WEEKDAY_DAYS, time)
    }

    override fun weekends(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildMultiDayRuleInProgress(WEEKEND_DAYS, time)
    }

    private fun adjustStartForDayOfWeek(dayOfWeek: RecurrenceDayOfWeek): VerdandiMoment {
        return when (frequency) {
            RecurrenceFrequency.Daily,
            RecurrenceFrequency.Weekly -> adjustForWeekly(dayOfWeek)
            RecurrenceFrequency.Monthly, RecurrenceFrequency.Yearly -> adjustForMonthlyOrYearly(dayOfWeek)
        }
    }

    private fun adjustForWeekly(dayOfWeek: RecurrenceDayOfWeek): VerdandiMoment {
        val currentDayOfWeek = startMoment.component.dayOfWeek.value
        val daysToAdd = (dayOfWeek.isoValue - currentDayOfWeek + 7).mod(7)

        if (daysToAdd > 0) {
            return startMoment + daysToAdd.days
        }

        return startMoment
    }

    private fun adjustForMonthlyOrYearly(dayOfWeek: RecurrenceDayOfWeek): VerdandiMoment {
        val firstOfMonth = startMoment adjust { at startOf month }
        val currentDayOfWeek = firstOfMonth.component.dayOfWeek.value
        val daysToAdd = (dayOfWeek.isoValue - currentDayOfWeek + 7).mod(7)
        var candidate = firstOfMonth + daysToAdd.days

        while (candidate < startMoment) {
            candidate += 7.days
        }

        return candidate
    }

    private fun buildRecurrence(
        dayOfWeek: RecurrenceDayOfWeek,
        end: RecurrenceEndMarker
    ): VerdandiRecurrenceMoments {
        return RecurrenceFactory(
            frequency = frequency,
            interval = interval,
            startMoment = adjustStartForDayOfWeek(dayOfWeek),
            endMoment = end.moment,
            dayOfWeek = dayOfWeek
        ).toMoments(limit)
    }

    private fun buildRuleInProgress(
        dayOfWeek: RecurrenceDayOfWeek,
        time: RecurrenceTimeMarker
    ): RecurrenceRuleInProgress {
        val adjustedStart = adjustStartForDayOfWeek(dayOfWeek) adjust { at startOf day }

        return RecurrenceRuleBuilder(
            frequency = frequency,
            interval = interval,
            startMoment = adjustedStart + time.duration,
            dayOfWeek = dayOfWeek,
            timeOffset = time.duration,
            limit = limit
        )
    }

    private fun buildMultiDayRecurrence(
        days: List<RecurrenceDayOfWeek>,
        end: RecurrenceEndMarker
    ): VerdandiRecurrenceMoments {
        val allMoments = days.flatMap { day ->
            RecurrenceFactory(
                frequency = frequency,
                interval = interval,
                startMoment = adjustStartForDayOfWeek(day),
                endMoment = end.moment,
                dayOfWeek = day
            ).toMoments(limit)
        }.sortedBy { it.inMilliseconds }

        val limitedMoments = if (limit != null) allMoments.take(limit) else allMoments

        val rule = RecurrenceRule(
            frequency = frequency,
            interval = interval,
            daysOfWeek = days.toSet(),
            timeOffset = null,
            endBoundary = end.moment
        )

        return VerdandiRecurrenceMoments(
            moments = limitedMoments,
            startMoment = startMoment,
            endMoment = end.moment ?: limitedMoments.lastOrNull() ?: startMoment,
            rule = rule
        )
    }

    private fun buildMultiDayRuleInProgress(
        days: List<RecurrenceDayOfWeek>,
        time: RecurrenceTimeMarker
    ): RecurrenceRuleInProgress {
        val adjustedStarts = days.map { targetDay ->
            (adjustStartForDayOfWeek(targetDay) adjust { at startOf day }) + time.duration
        }

        return MultiDayRecurrenceRuleBuilder(
            frequency = frequency,
            interval = interval,
            daysOfWeek = days,
            timeOffset = time.duration,
            limit = limit,
            originalStartMoment = startMoment,
            adjustedStarts = adjustedStarts
        )
    }

    companion object {

        private val WEEKDAY_DAYS = listOf(
            RecurrenceDayOfWeek.Monday,
            RecurrenceDayOfWeek.Tuesday,
            RecurrenceDayOfWeek.Wednesday,
            RecurrenceDayOfWeek.Thursday,
            RecurrenceDayOfWeek.Friday
        )

        private val WEEKEND_DAYS = listOf(
            RecurrenceDayOfWeek.Saturday,
            RecurrenceDayOfWeek.Sunday
        )
    }
}
