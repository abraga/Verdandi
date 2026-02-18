package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceRuleInProgress
import kotlin.time.Duration

internal class RecurrenceRuleBuilder(
    private val frequency: RecurrenceFrequency,
    private val interval: Int,
    private val startMoment: VerdandiMoment,
    private val dayOfWeek: RecurrenceDayOfWeek?,
    private val timeOffset: Duration,
    private val limit: Int?
) : RecurrenceRuleInProgress {

    override fun until(moment: VerdandiMoment): VerdandiRecurrenceMoments {
        return RecurrenceFactory(
            frequency = frequency,
            interval = interval,
            startMoment = startMoment,
            endMoment = moment,
            dayOfWeek = dayOfWeek,
            timeOffset = timeOffset
        ).toMoments(limit)
    }
}
