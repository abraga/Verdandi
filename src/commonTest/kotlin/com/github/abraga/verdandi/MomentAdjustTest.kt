package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import com.github.abraga.verdandi.api.model.duration.days
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days as durationDays
import kotlin.time.Duration.Companion.hours as durationHours

class MomentAdjustTest {

    @Test
    fun `adjust one more day should increment day by one`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 16
        val expectedMonth = 6
        val expectedYear = 2025

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { add one day }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be incremented to $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
    }

    @Test
    fun `adjust two more weeks should add fourteen days`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 29
        val expectedMonth = 6

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { add two weeks }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after adding two weeks.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
    }

    @Test
    fun `adjust six more months should add six months correctly`() {
        val inputTimestamp = "2025-01-15T12:00:00Z"
        val expectedDay = 15
        val expectedMonth = 7
        val expectedYear = 2025

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { add six months }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth after adding six months.")
        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
    }

    @Test
    fun `adjust twelve more months should advance year by one`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 15
        val expectedMonth = 6
        val expectedYear = 2026

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { add twelve months }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear after adding twelve months.")
    }

    @Test
    fun `adjust five less days should subtract five days`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 10
        val expectedMonth = 6

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { subtract five days }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after subtracting five days.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
    }

    @Test
    fun `adjust three less months should subtract three months`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 15
        val expectedMonth = 3

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { subtract three months }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth after subtracting three months.")
    }

    @Test
    fun `adjust at startOf day should reset time to midnight`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 15
        val expectedHour = 0
        val expectedMinute = 0
        val expectedSecond = 0
        val expectedMillisecond = 0

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at startOf day }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedHour, component.hour.value, "Hour should be reset to $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be reset to $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be reset to $expectedSecond.")
        assertEquals(expectedMillisecond, component.millisecond.value, "Millisecond should be reset to $expectedMillisecond.")
    }

    @Test
    fun `adjust at endOf day should set time to end of day`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 15
        val expectedHour = 23
        val expectedMinute = 59
        val expectedSecond = 59
        val expectedMillisecond = 999

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at endOf day }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
        assertEquals(expectedMillisecond, component.millisecond.value, "Millisecond should be $expectedMillisecond.")
    }

    @Test
    fun `adjust at startOf month should reset to first day at midnight`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 1
        val expectedMonth = 6
        val expectedHour = 0
        val expectedMinute = 0
        val expectedSecond = 0

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at startOf month }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should be reset to $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedHour, component.hour.value, "Hour should be reset to $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be reset to $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be reset to $expectedSecond.")
    }

    @Test
    fun `adjust at endOf month should set to last day at end of day`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 30
        val expectedMonth = 6
        val expectedHour = 23
        val expectedMinute = 59
        val expectedSecond = 59

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at endOf month }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay for June end.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `adjust at startOf year should reset to January first at midnight`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 1
        val expectedMonth = 1
        val expectedYear = 2025
        val expectedHour = 0
        val expectedMinute = 0

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at startOf year }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should be reset to $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should be reset to $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
        assertEquals(expectedHour, component.hour.value, "Hour should be reset to $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be reset to $expectedMinute.")
    }

    @Test
    fun `adjust at endOf year should set to December 31st at end of day`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedDay = 31
        val expectedMonth = 12
        val expectedYear = 2025
        val expectedHour = 23
        val expectedMinute = 59

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { at endOf year }
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
    }

    @Test
    fun `atYear should change year while preserving other components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val newYear = 2030
        val expectedMonth = 6
        val expectedDay = 15

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { atYear(newYear) }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(newYear, component.year.value, "Year should be changed to $newYear.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
    }

    @Test
    fun `atMonth should change month while preserving other components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val newMonth = 12
        val expectedYear = 2025
        val expectedDay = 15

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { atMonth(newMonth) }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
        assertEquals(newMonth, component.month.value, "Month should be changed to $newMonth.")
        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
    }

    @Test
    fun `atDay should change day while preserving other components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val newDay = 1
        val expectedYear = 2025
        val expectedMonth = 6

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { atDay(newDay) }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(newDay, component.day.value, "Day should be changed to $newDay.")
    }

    @Test
    fun `atHour should change hour while preserving other time components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val newHour = 8
        val expectedMinute = 30
        val expectedSecond = 45

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { atHour(newHour) }
        val component = adjustedMoment.component

        assertEquals(newHour, component.hour.value, "Hour should be changed to $newHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should remain $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should remain $expectedSecond.")
    }

    @Test
    fun `adjust one more month from January 31 should clamp to February 28 in non-leap year`() {
        val inputTimestamp = "2025-01-31T12:00:00Z"
        val expectedDay = 28
        val expectedMonth = 2
        val expectedYear = 2025

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment adjust { add one month }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be clamped to $expectedDay in February.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should remain $expectedYear.")
    }

    @Test
    fun `adjust one more year from leap day should clamp to February 28`() {
        val leapDayTimestamp = "2024-02-29T12:00:00Z"
        val expectedDay = 28
        val expectedMonth = 2
        val expectedYear = 2025

        val moment = Verdandi.from(leapDayTimestamp)
        val adjustedMoment = moment adjust { add one year }
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be clamped to $expectedDay in non-leap year.")
        assertEquals(expectedMonth, component.month.value, "Month should remain $expectedMonth.")
        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
    }

    @Test
    fun `adjust should propagate timezone`() {
        val expectedOffset = "+00:00"

        val moment = Verdandi.at(year = 2025, month = 6, day = 15, hour = 12, minute = 0, second = 0) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { add one day }

        assertEquals(expectedOffset, adjustedMoment.component.offset.toString(), "Offset should remain $expectedOffset after adjustment.")
    }

    @Test
    fun `adjust should preserve time when adding months`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedHour = 12
        val expectedMinute = 0
        val expectedOffset = "+00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjustedMoment = moment adjust { add six months }
        val component = adjustedMoment.component

        assertEquals(expectedHour, component.hour.value, "Hour should remain $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should remain $expectedMinute.")
        assertEquals(expectedOffset, component.offset.toString(), "Offset should remain $expectedOffset.")
    }

    @Test
    fun `plus operator with DateDuration should add days correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val daysToAdd = 5.days
        val expectedDay = 20

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment + daysToAdd
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after adding 5 days.")
    }

    @Test
    fun `minus operator with DateDuration should subtract days correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val daysToSubtract = 3.days
        val expectedDay = 12

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment - daysToSubtract
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after subtracting 3 days.")
    }

    @Test
    fun `plus operator with DateTimeDuration should add months and hours correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 15
        val expectedMonth = 7
        val expectedHour = 14

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val duration = DateTimeDuration.from(months = 1, time = 2.durationHours)
        val adjustedMoment = moment + duration
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should remain $expectedDay.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour after adding 2 hours.")
    }

    @Test
    fun `minus operator with DateTimeDuration should subtract weeks and hours correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 8
        val expectedHour = 9

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val duration = DateTimeDuration.from(weeks = 1, time = 3.durationHours)
        val adjustedMoment = moment - duration
        val component = adjustedMoment.component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after subtracting one week.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour after subtracting 3 hours.")
    }

    @Test
    fun `plus operator with Duration should add days correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 17

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment + 2.durationDays
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after adding 2 days.")
    }

    @Test
    fun `minus operator with Duration should subtract hours correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedHour = 7

        val moment = Verdandi.from(inputTimestamp)
        val adjustedMoment = moment - 5.durationHours
        val component = (adjustedMoment inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour after subtracting 5 hours.")
    }

    @Test
    fun `isBefore should return true when moment is earlier`() {
        val earlierTimestamp = "2025-06-15T12:00:00Z"
        val laterTimestamp = "2025-06-15T13:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertTrue(earlierMoment isBefore laterMoment, "Earlier moment should be before later moment.")
    }

    @Test
    fun `isBefore should return false when moment is later`() {
        val earlierTimestamp = "2025-06-15T12:00:00Z"
        val laterTimestamp = "2025-06-15T13:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertFalse(laterMoment isBefore earlierMoment, "Later moment should not be before earlier moment.")
    }

    @Test
    fun `isAfter should return true when moment is later`() {
        val earlierTimestamp = "2025-06-15T12:00:00Z"
        val laterTimestamp = "2025-06-15T13:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertTrue(laterMoment isAfter earlierMoment, "Later moment should be after earlier moment.")
    }

    @Test
    fun `isAfter should return false when moment is earlier`() {
        val earlierTimestamp = "2025-06-15T12:00:00Z"
        val laterTimestamp = "2025-06-15T13:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertFalse(earlierMoment isAfter laterMoment, "Earlier moment should not be after later moment.")
    }

    @Test
    fun `isSameDayAs should return true for same calendar day`() {
        val morningTimestamp = "2025-06-15T08:00:00Z"
        val eveningTimestamp = "2025-06-15T20:00:00Z"

        val morningMoment = Verdandi.from(morningTimestamp) inTimeZone VerdandiTimeZone.UTC
        val eveningMoment = Verdandi.from(eveningTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(morningMoment isSameDayAs eveningMoment, "Morning and evening of same day should be same day.")
    }

    @Test
    fun `isSameDayAs should return false for different calendar days`() {
        val todayTimestamp = "2025-06-15T23:00:00Z"
        val tomorrowTimestamp = "2025-06-16T01:00:00Z"

        val todayMoment = Verdandi.from(todayTimestamp) inTimeZone VerdandiTimeZone.UTC
        val tomorrowMoment = Verdandi.from(tomorrowTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertFalse(todayMoment isSameDayAs tomorrowMoment, "Different calendar days should not be same day.")
    }

    @Test
    fun `isSameMonthAs should return true for same calendar month`() {
        val startOfMonthTimestamp = "2025-06-01T12:00:00Z"
        val endOfMonthTimestamp = "2025-06-30T12:00:00Z"

        val startOfMonth = Verdandi.from(startOfMonthTimestamp) inTimeZone VerdandiTimeZone.UTC
        val endOfMonth = Verdandi.from(endOfMonthTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(startOfMonth isSameMonthAs endOfMonth, "Start and end of same month should be same month.")
    }

    @Test
    fun `isSameMonthAs should return false for different calendar months`() {
        val juneTimestamp = "2025-06-30T12:00:00Z"
        val julyTimestamp = "2025-07-01T12:00:00Z"

        val juneMoment = Verdandi.from(juneTimestamp) inTimeZone VerdandiTimeZone.UTC
        val julyMoment = Verdandi.from(julyTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertFalse(juneMoment isSameMonthAs julyMoment, "Different months should not be same month.")
    }

    @Test
    fun `isSameYearAs should return true for same calendar year`() {
        val januaryTimestamp = "2025-01-01T12:00:00Z"
        val decemberTimestamp = "2025-12-31T12:00:00Z"

        val januaryMoment = Verdandi.from(januaryTimestamp) inTimeZone VerdandiTimeZone.UTC
        val decemberMoment = Verdandi.from(decemberTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(januaryMoment isSameYearAs decemberMoment, "Start and end of same year should be same year.")
    }

    @Test
    fun `isSameYearAs should return false for different calendar years`() {
        val lastYearTimestamp = "2024-12-31T23:59:59Z"
        val thisYearTimestamp = "2025-01-01T00:00:00Z"

        val lastYearMoment = Verdandi.from(lastYearTimestamp) inTimeZone VerdandiTimeZone.UTC
        val thisYearMoment = Verdandi.from(thisYearTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertFalse(lastYearMoment isSameYearAs thisYearMoment, "Different years should not be same year.")
    }

    @Test
    fun `isBetween should return true for moment inside range`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val middleTimestamp = "2025-06-15T12:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val middleMoment = Verdandi.from(middleTimestamp)
        val endMoment = Verdandi.from(endTimestamp)

        assertTrue(middleMoment.isBetween(startMoment, endMoment), "Middle moment should be between start and end.")
    }

    @Test
    fun `isBetween should include start boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)

        assertTrue(startMoment.isBetween(startMoment, endMoment), "Start boundary should be included in range.")
    }

    @Test
    fun `isBetween should exclude end boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)

        assertFalse(endMoment.isBetween(startMoment, endMoment), "End boundary should be excluded from range.")
    }

    @Test
    fun `isBetween should return false for moment before range`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val beforeTimestamp = "2025-05-31T23:59:59Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val beforeMoment = Verdandi.from(beforeTimestamp)
        val endMoment = Verdandi.from(endTimestamp)

        assertFalse(beforeMoment.isBetween(startMoment, endMoment), "Moment before range should not be in range.")
    }

    @Test
    fun `isBetween should return false for moment after range`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val afterTimestamp = "2025-07-01T00:00:00Z"

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val afterMoment = Verdandi.from(afterTimestamp)

        assertFalse(afterMoment.isBetween(startMoment, endMoment), "Moment after range should not be in range.")
    }

    @Test
    fun `durationUntil should calculate months correctly`() {
        val startTimestamp = "2025-01-01T12:00:00Z"
        val endTimestamp = "2025-07-01T12:00:00Z"
        val expectedYears = 0
        val expectedMonths = 6
        val expectedDays = 0

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val duration = startMoment.durationUntil(endMoment)

        assertEquals(expectedYears, duration.years, "Years should be $expectedYears.")
        assertEquals(expectedMonths, duration.months, "Months should be $expectedMonths.")
        assertEquals(expectedDays, duration.days, "Days should be $expectedDays.")
    }

    @Test
    fun `durationUntil should calculate weeks and days correctly`() {
        val startTimestamp = "2025-06-01T12:00:00Z"
        val endTimestamp = "2025-06-15T12:00:00Z"
        val expectedWeeks = 2
        val expectedDays = 0

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val duration = startMoment.durationUntil(endMoment)

        assertEquals(expectedWeeks, duration.weeks, "Weeks should be $expectedWeeks.")
        assertEquals(expectedDays, duration.days, "Days should be $expectedDays.")
    }
}
