package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceRuleInProgress
import kotlin.time.Duration

internal class MultiDayRecurrenceRuleBuilder(
    private val frequency: RecurrenceFrequency,
    private val interval: Int,
    private val daysOfWeek: List<RecurrenceDayOfWeek>,
    private val timeOffset: Duration,
    private val limit: Int?,
    private val originalStartMoment: VerdandiMoment,
    private val adjustedStarts: List<VerdandiMoment>
) : RecurrenceRuleInProgress {

    override fun until(moment: VerdandiMoment): VerdandiRecurrenceMoments {
        val allMoments = daysOfWeek.zip(adjustedStarts).flatMap { (dayOfWeek, adjustedStart) ->
            RecurrenceFactory(
                frequency = frequency,
                interval = interval,
                startMoment = adjustedStart,
                endMoment = moment,
                dayOfWeek = dayOfWeek,
                timeOffset = timeOffset
            ).toMoments(limit)
        }.sortedBy { it.inMilliseconds }

        val limitedMoments = if (limit != null) allMoments.take(limit) else allMoments

        val rule = RecurrenceRule(
            frequency = frequency,
            interval = interval,
            daysOfWeek = daysOfWeek.toSet(),
            timeOffset = timeOffset,
            endBoundary = moment
        )

        return VerdandiRecurrenceMoments(
            moments = limitedMoments,
            startMoment = originalStartMoment,
            endMoment = moment,
            rule = rule
        )
    }
}
