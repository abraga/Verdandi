package com.github.abraga.verdandi.sampleapp.showcase

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import com.github.abraga.verdandi.api.model.duration.months
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.time.Duration.Companion.hours

/**
 * Verdandi Showcase - API usage examples
 *
 * This file centralizes all Verdandi API usages in the showcase app.
 * Each function demonstrates a specific feature of the library with
 * clear and readable code.
 */
object VerdandiShowcaseUsages {

    // region Creation - Creating moments

    /**
     * Get the current moment
     */
    fun now(): VerdandiMoment {
        return Verdandi.now()
    }

    /**
     * Create moment from specific date/time components
     */
    fun createAt(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ): VerdandiMoment {
        return Verdandi.at(year, month, day, hour, minute)
    }

    // endregion

    // region Adjustments - Adding/subtracting time

    /**
     * Add one hour
     */
    fun addOneHour(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { add one hour }
    }

    /**
     * Subtract one hour
     */
    fun subtractOneHour(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { subtract one hour }
    }

    /**
     * Add one day to a moment
     */
    fun addOneDay(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { add one day }
    }

    /**
     * Subtract one day from a moment
     */
    fun subtractOneDay(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { subtract one day }
    }

    /**
     * Add one week to a moment
     */
    fun addOneWeek(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { add one week }
    }

    /**
     * Subtract one week from a moment
     */
    fun subtractOneWeek(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { subtract one week }
    }

    /**
     * Add one month to a moment
     */
    fun addOneMonth(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { add one month }
    }

    /**
     * Subtract one month from a moment
     */
    fun subtractOneMonth(moment: VerdandiMoment): VerdandiMoment {
        return moment adjust { subtract one month }
    }

    // endregion

    // region Formatting - How to format dates and times

    /**
     * Format time using DSL syntax: HH.mm.ss
     * Result example: "14:30:45"
     */
    fun formatTime(moment: VerdandiMoment): String {
        return moment format { HH.mm.ss }
    }

    /**
     * Format date and time using DSL syntax: yyyy-MM-dd HH.mm
     * Result example: "2026-02-18 14:30"
     */
    fun formatDateTimeSimple(moment: VerdandiMoment): String {
        return moment format { yyyy-MM-dd..HH.mm }
    }

    /**
     * Format date using pattern string
     * Result example: "Wednesday, 18 February 2026"
     */
    fun formatFullDate(moment: VerdandiMoment): String {
        return moment format "EEEE, dd MMMM yyyy"
    }

    /**
     * Format date and time with DSL
     * Result example: "2026/02/18T14:30"
     */
    fun formatDateTime(moment: VerdandiMoment): String {
        return moment format { yyyy/MM/dd T HH.mm }
    }

    /**
     * Format date for display
     * Result example: "Feb 18, 2026"
     */
    fun formatDateShort(moment: VerdandiMoment): String {
        return moment format "MMM dd, yyyy"
    }

    /**
     * Format date/time with custom pattern
     */
    fun formatWithPattern(moment: VerdandiMoment, pattern: String): String {
        return moment format pattern
    }

    // endregion

    // region Components - Accessing date/time parts

    /**
     * Get year value from moment
     */
    fun getYear(moment: VerdandiMoment): Int {
        return moment.component.year.value
    }

    /**
     * Get month component (1-12)
     */
    fun getMonth(moment: VerdandiMoment): Month {
        return moment.component.month
    }

    /**
     * Get day of month (1-31)
     */
    fun getDayOfMonth(moment: VerdandiMoment): Int {
        return moment.component.day.value
    }

    /**
     * Get day of week (1=Monday, 7=Sunday)
     */
    fun getDayOfWeek(moment: VerdandiMoment): Int {
        return moment.component.dayOfWeek.value
    }

    /**
     * Get number of days in the month
     */
    fun getDaysInMonth(year: Int, month: Month): Int {
        return month.lengthInYear(year)
    }

    /**
     * Get month name (e.g. "February")
     */
    fun getMonthName(moment: VerdandiMoment): String {
        return moment.component.monthName
    }

    /**
     * Get hour (0-23)
     */
    fun getHour(moment: VerdandiMoment): Int {
        return moment.component.hour.value
    }

    /**
     * Get minute (0-59)
     */
    fun getMinute(moment: VerdandiMoment): Int {
        return moment.component.minute.value
    }

    // endregion

    // region Intervals - Working with date ranges

    /**
     * Create an interval for the last 30 days
     */
    fun createLast30DaysInterval(now: VerdandiMoment): VerdandiInterval {
        return Verdandi.interval { last thirty days from now }
    }

    /**
     * Get interval duration in DateTimeDuration
     */
    fun getIntervalDateTimeDuration(interval: VerdandiInterval): DateTimeDuration {
        return interval.duration()
    }

    fun createCustomInterval(start: VerdandiMoment, end: VerdandiMoment): VerdandiInterval {
        return start..end
    }

    // endregion

    // region Recurrence - Creating recurring schedules

    /**
     * Create a recurrence for weekdays at 9:00 AM
     */
    fun createWeekdayRecurrence(
        startMoment: VerdandiMoment,
        endMoment: VerdandiMoment,
        maxResults: Int = 10
    ): VerdandiRecurrenceMoments {
        return Verdandi.recurrence(startMoment, limit = maxResults) {
            every weekdays at { 9.hours } until endMoment
        }
    }

    /**
     * Return how many Fridays there are in the next two months, starting from now
     */
    fun createHowManyFridaysRecurrenceInNextTwoMonths(): Int {
        val now = now()
        val nextTwoMonths = now + 2.months

        return Verdandi.recurrence { every fridays until(nextTwoMonths) }.size
    }

    /**
     * Format all recurrence moments to a simple date format (MM-dd)
     */
    fun formatAllToSimpleDate(recurrence: VerdandiRecurrenceMoments): String {
        val formatted = recurrence format { MM-dd }

        return formatted.joinToString(", ")
    }

    // endregion

    // region Relative Time - Human-readable time differences

    fun getRelativeMoment(): VerdandiMoment {
        return Verdandi.at(
            year = 2021,
            month = 7,
            day = 3,
            hour = 21,
            minute = 24
        )
    }

    /**
     * Format a past moment relative to now with customized strings
     * Result example: "3 hours, 47 minutes ago"
     */
    fun formatPastRelativeCustomized(
        moment: VerdandiMoment,
        units: Int = 5,
        unitSeparator: String = ", ",
        lastUnitSeparator: String = " and ",
        onNow: () -> String = { "just now" },
        onPast: (String) -> String = { "$it ago" },
        onFuture: (String) -> String = { "in $it" }
    ): String {
        return moment.relativeToNow() format {
            maxUnits = units
            separator = unitSeparator
            lastSeparator = lastUnitSeparator

            onNow { onNow() }
            onPast { onPast(it) }
            onFuture { onFuture(it) }
        }
    }

    // endregion

    // region Timezone - Converting between timezones

    /**
     * Convert moment to a specific timezone
     */
    fun convertToTimeZone(moment: VerdandiMoment, timeZoneId: String): VerdandiMoment {
        return moment inTimeZone VerdandiTimeZone.of(timeZoneId)
    }

    // endregion
}
