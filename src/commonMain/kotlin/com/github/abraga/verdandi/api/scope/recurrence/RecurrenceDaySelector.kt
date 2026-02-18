package com.github.abraga.verdandi.api.scope.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.internal.recurrence.FrequencyDaySelector
import com.github.abraga.verdandi.internal.recurrence.RecurrenceFrequency

interface RecurrenceDaySelector {

    infix fun mondays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun tuesdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun wednesdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun thursdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun fridays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun saturdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun sundays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun weekdays(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun weekends(end: RecurrenceEndMarker): VerdandiRecurrenceMoments

    infix fun mondays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun tuesdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun wednesdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun thursdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun fridays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun saturdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun sundays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun weekdays(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    infix fun weekends(time: RecurrenceTimeMarker): RecurrenceRuleInProgress

    companion object {

        internal operator fun invoke(
            startMoment: VerdandiMoment,
            frequency: RecurrenceFrequency,
            interval: Int,
            limit: Int?
        ): RecurrenceDaySelector {
            return FrequencyDaySelector(startMoment, frequency, interval, limit)
        }
    }
}
