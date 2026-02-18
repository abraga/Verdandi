package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceDaySelector
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceEndMarker
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceIntervalSelector
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceOnMarker
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceRuleInProgress
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceTimeMarker

internal class FrequencySelector(
    private val startMoment: VerdandiMoment,
    private val interval: Int,
    private val limit: Int?
) : RecurrenceIntervalSelector,
    RecurrenceDaySelector by FrequencyDaySelector(
        startMoment = startMoment,
        frequency = RecurrenceFrequency.Weekly,
        interval = interval,
        limit = limit
    ) {

    override fun day(on: RecurrenceOnMarker): RecurrenceDaySelector {
        return RecurrenceDaySelector(startMoment, RecurrenceFrequency.Daily, interval, limit)
    }

    override fun week(on: RecurrenceOnMarker): RecurrenceDaySelector {
        return RecurrenceDaySelector(startMoment, RecurrenceFrequency.Weekly, interval, limit)
    }

    override fun month(on: RecurrenceOnMarker): RecurrenceDaySelector {
        return RecurrenceDaySelector(startMoment, RecurrenceFrequency.Monthly, interval, limit)
    }

    override fun year(on: RecurrenceOnMarker): RecurrenceDaySelector {
        return RecurrenceDaySelector(startMoment, RecurrenceFrequency.Yearly, interval, limit)
    }

    override fun day(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceFrequency.Daily, end)
    }

    override fun week(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceFrequency.Weekly, end)
    }

    override fun month(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceFrequency.Monthly, end)
    }

    override fun year(end: RecurrenceEndMarker): VerdandiRecurrenceMoments {
        return buildRecurrence(RecurrenceFrequency.Yearly, end)
    }

    override fun day(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceFrequency.Daily, time)
    }

    override fun week(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceFrequency.Weekly, time)
    }

    override fun month(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceFrequency.Monthly, time)
    }

    override fun year(time: RecurrenceTimeMarker): RecurrenceRuleInProgress {
        return buildRuleInProgress(RecurrenceFrequency.Yearly, time)
    }

    private fun buildRecurrence(
        frequency: RecurrenceFrequency,
        end: RecurrenceEndMarker
    ): VerdandiRecurrenceMoments {
        return RecurrenceFactory(
            frequency = frequency,
            interval = interval,
            startMoment = startMoment,
            endMoment = end.moment
        ).toMoments(limit)
    }

    private fun buildRuleInProgress(
        frequency: RecurrenceFrequency,
        time: RecurrenceTimeMarker
    ): RecurrenceRuleInProgress {
        return RecurrenceRuleBuilder(
            frequency = frequency,
            interval = interval,
            startMoment = startMoment + time.duration,
            dayOfWeek = null,
            timeOffset = time.duration,
            limit = limit
        )
    }
}
