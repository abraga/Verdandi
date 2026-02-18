package com.github.abraga.verdandi.api.scope.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.internal.recurrence.FrequencySelector

interface RecurrenceIntervalSelector : RecurrenceDaySelector {

    infix fun day(on: RecurrenceOnMarker): RecurrenceDaySelector

    infix fun week(on: RecurrenceOnMarker): RecurrenceDaySelector

    infix fun month(on: RecurrenceOnMarker): RecurrenceDaySelector

    infix fun year(on: RecurrenceOnMarker): RecurrenceDaySelector

    infix fun day(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun week(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun month(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun year(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun day(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun week(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun month(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun year(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    companion object {

        internal operator fun invoke(
            startMoment: VerdandiMoment,
            interval: Int = 1,
            limit: Int? = null
        ): RecurrenceIntervalSelector {
            return FrequencySelector(startMoment, interval, limit)
        }
    }
}
