package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_PER_YEAR
import com.github.abraga.verdandi.internal.core.executor.EpochCalculator

internal object RecurrenceMatcher {

    fun matches(
        candidate: VerdandiMoment,
        startMoment: VerdandiMoment,
        rule: RecurrenceRule
    ): Boolean {
        if (candidate isBefore startMoment) {
            return false
        }

        if (rule.endBoundary != null && candidate isAfter rule.endBoundary) {
            return false
        }

        val normalizedCandidate = normalizeToTimezone(candidate, startMoment)

        val candidateComponent = normalizedCandidate.component
        val startComponent = startMoment.component

        if (timeOfDayMatches(candidateComponent, startComponent).not()) {
            return false
        }

        if (rule.daysOfWeek.isNotEmpty()) {
            val candidateDayOfWeek = candidateComponent.dayOfWeek.value

            if (rule.daysOfWeek.none { it.isoValue == candidateDayOfWeek }) {
                return false
            }
        }

        return frequencyAlignmentMatches(candidateComponent, startComponent, rule)
    }

    private fun normalizeToTimezone(
        moment: VerdandiMoment,
        reference: VerdandiMoment
    ): VerdandiMoment {
        val timeZoneId = reference.timeZoneId ?: return moment

        return moment inTimeZone VerdandiTimeZone.of(timeZoneId)
    }

    private fun timeOfDayMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent
    ): Boolean {
        return candidate.hour.value == start.hour.value &&
            candidate.minute.value == start.minute.value &&
            candidate.second.value == start.second.value &&
            candidate.millisecond.value == start.millisecond.value
    }

    private fun frequencyAlignmentMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent,
        rule: RecurrenceRule
    ): Boolean {
        return when (rule.frequency) {
            RecurrenceFrequency.Daily -> dailyAlignmentMatches(candidate, start, rule.interval)
            RecurrenceFrequency.Weekly -> weeklyAlignmentMatches(candidate, start, rule.interval)
            RecurrenceFrequency.Monthly -> monthlyAlignmentMatches(candidate, start, rule)
            RecurrenceFrequency.Yearly -> yearlyAlignmentMatches(candidate, start, rule)
        }
    }

    private fun dailyAlignmentMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent,
        interval: Int
    ): Boolean {
        val daysDifference = calendarDaysDifference(candidate, start)

        return daysDifference >= 0 && daysDifference % interval == 0L
    }

    private fun weeklyAlignmentMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent,
        interval: Int
    ): Boolean {
        val candidateMondayEpochDays = toMondayEpochDays(candidate)
        val startMondayEpochDays = toMondayEpochDays(start)
        val weeksDifference = (candidateMondayEpochDays - startMondayEpochDays) / DAYS_PER_WEEK

        return weeksDifference >= 0 && weeksDifference % interval == 0L
    }

    private fun monthlyAlignmentMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent,
        rule: RecurrenceRule
    ): Boolean {
        val monthsDifference = totalMonthsDifference(candidate, start)

        if (monthsDifference < 0 || monthsDifference % rule.interval != 0) {
            return false
        }

        if (rule.daysOfWeek.isNotEmpty()) {
            return true
        }

        return candidate.day.value == start.day.value
    }

    private fun yearlyAlignmentMatches(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent,
        rule: RecurrenceRule
    ): Boolean {
        val yearsDifference = candidate.year.value - start.year.value

        if (yearsDifference < 0 || yearsDifference % rule.interval != 0) {
            return false
        }

        if (rule.daysOfWeek.isNotEmpty()) {
            return true
        }

        return candidate.month.value == start.month.value &&
            candidate.day.value == start.day.value
    }

    private fun toMondayEpochDays(component: VerdandiMomentComponent): Long {
        val epochDays = epochDaysFrom(component)
        val isoDayOfWeek = component.dayOfWeek.value

        return epochDays - (isoDayOfWeek - 1)
    }

    private fun calendarDaysDifference(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent
    ): Long {
        return epochDaysFrom(candidate) - epochDaysFrom(start)
    }

    private fun epochDaysFrom(component: VerdandiMomentComponent): Long {
        return EpochCalculator.daysSinceEpoch(
            component.year.value,
            component.month.value,
            component.day.value
        )
    }

    private fun totalMonthsDifference(
        candidate: VerdandiMomentComponent,
        start: VerdandiMomentComponent
    ): Int {
        return (candidate.year.value - start.year.value) * MONTHS_PER_YEAR +
            (candidate.month.value - start.month.value)
    }
}
