package com.github.abraga.verdandi.internal.adjust

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.core.VerdandiLocalDate
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.core.VerdandiLocalTime
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext

internal class DateTimeAdjuster(initialMillis: Long, private var timeZoneContext: TimeZoneContext) {

    private var cachedMilliseconds: Long = initialMillis
    private var hasChanged: Boolean = false

    val adjustedMilliseconds: Long
        get() {
            if (hasChanged) {
                cachedMilliseconds = timeZoneContext.toEpochMillis(localDateTime)
                hasChanged = false
            }

            return cachedMilliseconds
        }

    private var localDateTime: VerdandiLocalDateTime = timeZoneContext.toLocalDateTime(initialMillis)

    fun addYears(count: Int) {
        localDateTime = localDateTime.plusYears(count.toLong())
        hasChanged = true
    }

    fun addMonths(count: Int) {
        localDateTime = localDateTime.plusMonths(count.toLong())
        hasChanged = true
    }

    fun addWeeks(count: Int) {
        localDateTime = localDateTime.plusWeeks(count.toLong())
        hasChanged = true
    }

    fun addDays(count: Int) {
        localDateTime = localDateTime.plusDays(count.toLong())
        hasChanged = true
    }

    fun addHours(count: Int) {
        localDateTime = localDateTime.plusHours(count.toLong())
        hasChanged = true
    }

    fun addMinutes(count: Int) {
        localDateTime = localDateTime.plusMinutes(count.toLong())
        hasChanged = true
    }

    fun addSeconds(count: Int) {
        localDateTime = localDateTime.plusSeconds(count.toLong())
        hasChanged = true
    }

    fun addMilliseconds(count: Int) {
        localDateTime = localDateTime.plusMillis(count.toLong())
        hasChanged = true
    }

    fun setYear(value: Int) {
        localDateTime = localDateTime.withComponents(year = value)
        hasChanged = true
    }

    fun setMonth(value: Int) {
        localDateTime = localDateTime.withComponents(monthNumber = value)
        hasChanged = true
    }

    fun setDay(value: Int) {
        localDateTime = localDateTime.withComponents(dayOfMonth = value)
        hasChanged = true
    }

    fun setHour(value: Int) {
        localDateTime = localDateTime.withComponents(hour = value)
        hasChanged = true
    }

    fun setMinute(value: Int) {
        localDateTime = localDateTime.withComponents(minute = value)
        hasChanged = true
    }

    fun setSecond(value: Int) {
        localDateTime = localDateTime.withComponents(second = value)
        hasChanged = true
    }

    fun truncateToYears() {
        localDateTime = VerdandiLocalDateTime.of(
            date = VerdandiLocalDate.of(localDateTime.year, 1, 1),
            time = VerdandiLocalTime.of(0, 0, 0, 0)
        )
        hasChanged = true
    }

    fun truncateToMonths() {
        localDateTime = VerdandiLocalDateTime.of(
            date = VerdandiLocalDate.of(localDateTime.year, localDateTime.monthNumber, 1),
            time = VerdandiLocalTime.of(0, 0, 0, 0)
        )
        hasChanged = true
    }

    fun truncateToDays() {
        localDateTime = localDateTime.date.atTime(VerdandiLocalTime.of(0, 0, 0, 0))
        hasChanged = true
    }

    fun setStartOfDay() {
        truncateToDays()
    }

    fun setStartOfWeek(weekStart: Int) {
        val currentDayOfWeek = localDateTime.dayOfWeek.isoDayNumber
        val daysToSubtract = (currentDayOfWeek - weekStart + DAYS_PER_WEEK) % DAYS_PER_WEEK
        localDateTime = localDateTime.minusDays(daysToSubtract.toLong())
        truncateToDays()
    }

    fun setStartOfMonth() {
        truncateToMonths()
    }

    fun setStartOfYear() {
        truncateToYears()
    }

    fun setEndOfDay() {
        localDateTime = localDateTime.date.atTime(VerdandiLocalTime.Max)
        hasChanged = true
    }

    fun setEndOfWeek(weekStart: Int) {
        val weekEnd = (weekStart + DAYS_PER_WEEK - 2) % DAYS_PER_WEEK + 1
        val currentDayOfWeek = localDateTime.dayOfWeek.isoDayNumber
        val daysToAdd = (weekEnd - currentDayOfWeek + DAYS_PER_WEEK) % DAYS_PER_WEEK
        localDateTime = localDateTime.plusDays(daysToAdd.toLong())
        localDateTime = localDateTime.date.atTime(VerdandiLocalTime.Max)
        hasChanged = true
    }

    fun setEndOfMonth() {
        val lastDayOfMonth = localDateTime.date.lastDayOfMonth
        localDateTime = lastDayOfMonth.atTime(VerdandiLocalTime.Max)
        hasChanged = true
    }

    fun setEndOfYear() {
        val lastDayOfYear = localDateTime.date.lastDayOfYear
        localDateTime = lastDayOfYear.atTime(VerdandiLocalTime.Max)
        hasChanged = true
    }

    fun switchTimeZone(newContext: TimeZoneContext) {
        val millis = adjustedMilliseconds

        timeZoneContext = newContext
        localDateTime = timeZoneContext.toLocalDateTime(millis)
        hasChanged = false
    }
}
