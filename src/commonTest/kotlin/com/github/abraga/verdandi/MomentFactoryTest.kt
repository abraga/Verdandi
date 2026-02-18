@file:OptIn(ExperimentalTime::class)

package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.exception.VerdandiStateException
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.internal.factory.query.FromMomentFactory
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import com.github.abraga.verdandi.internal.factory.query.temporal.TemporalDirection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

class MomentFactoryTest {

    @Test
    fun `at should create moment with all datetime components correctly`() {
        val expectedYear = 2025
        val expectedMonth = 6
        val expectedDay = 15
        val expectedHour = 14
        val expectedMinute = 30
        val expectedSecond = 45
        val expectedMillisecond = 123

        val moment = Verdandi.at(
            year = expectedYear,
            month = expectedMonth,
            day = expectedDay,
            hour = expectedHour,
            minute = expectedMinute,
            second = expectedSecond,
            millisecond = expectedMillisecond
        )
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
        assertEquals(expectedMillisecond, component.millisecond.value, "Millisecond should be $expectedMillisecond.")
    }

    @Test
    fun `at should default time components to zero when only date is provided`() {
        val inputYear = 2025
        val inputMonth = 3
        val inputDay = 10

        val moment = Verdandi.at(year = inputYear, month = inputMonth, day = inputDay)
        val component = moment.component

        assertEquals(inputYear, component.year.value, "Year should be $inputYear.")
        assertEquals(inputMonth, component.month.value, "Month should be $inputMonth.")
        assertEquals(inputDay, component.day.value, "Day should be $inputDay.")
        assertEquals(0, component.hour.value, "Hour should default to 0.")
        assertEquals(0, component.minute.value, "Minute should default to 0.")
        assertEquals(0, component.second.value, "Second should default to 0.")
        assertEquals(0, component.millisecond.value, "Millisecond should default to 0.")
    }

    @Test
    fun `at should default minute and second to zero when only hour is provided`() {
        val inputYear = 2025
        val inputMonth = 12
        val inputDay = 25
        val inputHour = 18

        val moment = Verdandi.at(year = inputYear, month = inputMonth, day = inputDay, hour = inputHour)
        val component = moment.component

        assertEquals(inputHour, component.hour.value, "Hour should be $inputHour.")
        assertEquals(0, component.minute.value, "Minute should default to 0.")
        assertEquals(0, component.second.value, "Second should default to 0.")
    }

    @Test
    fun `at should default second to zero when hour and minute are provided`() {
        val inputYear = 2025
        val inputMonth = 1
        val inputDay = 1
        val inputHour = 0
        val inputMinute = 30

        val moment = Verdandi.at(
            year = inputYear,
            month = inputMonth,
            day = inputDay,
            hour = inputHour,
            minute = inputMinute
        )
        val component = moment.component

        assertEquals(inputMinute, component.minute.value, "Minute should be $inputMinute.")
        assertEquals(0, component.second.value, "Second should default to 0.")
    }

    @Test
    fun `at should default millisecond to zero when second is provided`() {
        val inputYear = 2025
        val inputMonth = 7
        val inputDay = 4
        val inputHour = 12
        val inputMinute = 15
        val inputSecond = 30

        val moment = Verdandi.at(
            year = inputYear,
            month = inputMonth,
            day = inputDay,
            hour = inputHour,
            minute = inputMinute,
            second = inputSecond
        )
        val component = moment.component

        assertEquals(inputSecond, component.second.value, "Second should be $inputSecond.")
        assertEquals(0, component.millisecond.value, "Millisecond should default to 0.")
    }

    @Test
    fun `from epoch should create moment with zero milliseconds`() {
        val epochMilliseconds = 0L

        val moment = Verdandi.from(epochMilliseconds)

        assertEquals(epochMilliseconds, moment.inMilliseconds, "Epoch should be 0.")
    }

    @Test
    fun `from epoch should preserve positive milliseconds value`() {
        val epochMilliseconds = 1735689600000L

        val moment = Verdandi.from(epochMilliseconds)

        assertEquals(epochMilliseconds, moment.inMilliseconds, "Epoch should be $epochMilliseconds.")
    }

    @Test
    fun `from epoch should accept negative milliseconds for pre-epoch dates`() {
        val negativeEpochMilliseconds = -86400000L

        val moment = Verdandi.from(negativeEpochMilliseconds)

        assertEquals(negativeEpochMilliseconds, moment.inMilliseconds)
    }

    @Test
    fun `from ISO timestamp with Z suffix should parse correctly in UTC`() {
        val isoTimestamp = "2025-06-15T14:30:45Z"
        val expectedYear = 2025
        val expectedMonth = 6
        val expectedDay = 15
        val expectedHour = 14
        val expectedMinute = 30
        val expectedSecond = 45

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `from ISO timestamp with milliseconds should parse milliseconds correctly`() {
        val isoTimestamp = "2025-03-10T08:15:30.500Z"
        val expectedMillisecond = 500

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedMillisecond, component.millisecond.value, "Millisecond should be $expectedMillisecond.")
    }

    @Test
    fun `from ISO timestamp with positive offset should convert to UTC correctly`() {
        val isoTimestamp = "2025-12-25T18:00:00+05:00"
        val expectedUtcHour = 13

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour after offset conversion.")
    }

    @Test
    fun `from ISO timestamp with negative offset should convert to UTC correctly`() {
        val isoTimestamp = "2025-07-04T12:00:00-04:00"
        val expectedUtcHour = 16

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour after offset conversion.")
    }

    @Test
    fun `from ISO timestamp should parse midnight correctly`() {
        val isoTimestamp = "2025-01-01T00:00:00Z"

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(0, component.hour.value, "Hour should be 0 for midnight.")
        assertEquals(0, component.minute.value, "Minute should be 0 for midnight.")
        assertEquals(0, component.second.value, "Second should be 0 for midnight.")
        assertEquals(0, component.millisecond.value, "Millisecond should be 0 for midnight.")
    }

    @Test
    fun `now should return epoch within current time range`() {
        val beforeCreation = Clock.System.now()
        val moment = Verdandi.now()
        val afterCreation = Clock.System.now()

        assertTrue(
            actual = moment.inMilliseconds >= beforeCreation.toEpochMilliseconds(),
            message = "Moment should not be before the creation started."
        )
        assertTrue(
            actual = moment.inMilliseconds <= afterCreation.toEpochMilliseconds(),
            message = "Moment should not be after the creation finished."
        )
    }

    @Test
    fun `invoke operator should return current time equivalent to now`() {
        val beforeCreation = Clock.System.now()
        val moment = Verdandi()
        val afterCreation = Clock.System.now()

        assertTrue(
            actual = moment.inMilliseconds >= beforeCreation.toEpochMilliseconds(),
            message = "Moment should not be before the creation started."
        )
        assertTrue(
            actual = moment.inMilliseconds <= afterCreation.toEpochMilliseconds(),
            message = "Moment should not be after the creation finished."
        )
    }

    @Test
    fun `component year should return correct value`() {
        val isoTimestamp = "2025-06-15T12:00:00Z"
        val expectedYear = 2025

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedYear, moment.component.year.value, "Year should be $expectedYear.")
    }

    @Test
    fun `component month should return correct value`() {
        val isoTimestamp = "2025-11-03T12:00:00Z"
        val expectedMonth = 11

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedMonth, moment.component.month.value, "Month should be $expectedMonth.")
    }

    @Test
    fun `component day should return correct value`() {
        val isoTimestamp = "2025-06-28T12:00:00Z"
        val expectedDay = 28

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedDay, moment.component.day.value, "Day should be $expectedDay.")
    }

    @Test
    fun `component hour should return correct value`() {
        val isoTimestamp = "2025-06-15T23:00:00Z"
        val expectedHour = 23

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedHour, moment.component.hour.value, "Hour should be $expectedHour.")
    }

    @Test
    fun `component minute should return correct value`() {
        val isoTimestamp = "2025-06-15T12:45:00Z"
        val expectedMinute = 45

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedMinute, moment.component.minute.value, "Minute should be $expectedMinute.")
    }

    @Test
    fun `component second should return correct value`() {
        val isoTimestamp = "2025-06-15T12:00:59Z"
        val expectedSecond = 59

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedSecond, moment.component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `component millisecond should return correct value`() {
        val isoTimestamp = "2025-06-15T12:00:00.999Z"
        val expectedMillisecond = 999

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedMillisecond, moment.component.millisecond.value, "Millisecond should be $expectedMillisecond.")
    }

    @Test
    fun `component dayOfWeek should return correct value for Sunday`() {
        val sundayTimestamp = "2025-06-15T12:00:00Z"
        val expectedDayOfWeek = 7

        val moment = Verdandi.from(sundayTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedDayOfWeek, moment.component.dayOfWeek.value, "Day of week should be $expectedDayOfWeek for Sunday.")
    }

    @Test
    fun `component dayOfTheYear should return 1 for January first`() {
        val januaryFirstTimestamp = "2025-01-01T12:00:00Z"
        val expectedDayOfYear = 1

        val moment = Verdandi.from(januaryFirstTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedDayOfYear, moment.component.dayOfTheYear.value, "Day of year should be $expectedDayOfYear for January 1st.")
    }

    @Test
    fun `component dayOfTheYear should return correct value for mid-year date`() {
        val julyFirstTimestamp = "2025-07-01T12:00:00Z"
        val expectedDayOfYear = 182

        val moment = Verdandi.from(julyFirstTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedDayOfYear, moment.component.dayOfTheYear.value, "Day of year should be $expectedDayOfYear for July 1st in non-leap year.")
    }

    @Test
    fun `component offsetString should return plus zero for UTC timezone`() {
        val isoTimestamp = "2025-06-15T12:00:00Z"
        val expectedOffset = "+00:00"

        val moment = Verdandi.from(isoTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertEquals(expectedOffset, moment.component.offset.toString(), "Offset should be $expectedOffset for UTC.")
    }

    @Test
    fun `inTimeZone should preserve epoch milliseconds when changing timezone`() {
        val moment = Verdandi.at(year = 2025, month = 6, day = 15, hour = 12, minute = 0, second = 0)
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val tokyoMoment = moment inTimeZone tokyoTimeZone

        assertEquals(moment.inMilliseconds, tokyoMoment.inMilliseconds, "Epoch should remain unchanged when converting timezone.")
    }

    @Test
    fun `inTimeZone should adjust hour component for different timezone`() {
        val utcTimestamp = "2025-06-15T12:00:00Z"
        val expectedUtcHour = 12
        val expectedTokyoHour = 21
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val utcMoment = Verdandi.from(utcTimestamp) inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = utcMoment inTimeZone tokyoTimeZone

        assertEquals(expectedUtcHour, utcMoment.component.hour.value, "UTC hour should be $expectedUtcHour.")
        assertEquals(expectedTokyoHour, tokyoMoment.component.hour.value, "Tokyo hour should be $expectedTokyoHour.")
    }

    @Test
    fun `inTimeZone should handle day boundary crossing correctly`() {
        val utcTimestamp = "2025-06-15T22:00:00Z"
        val expectedUtcDay = 15
        val expectedTokyoDay = 16
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val utcMoment = Verdandi.from(utcTimestamp) inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = utcMoment inTimeZone tokyoTimeZone

        assertEquals(expectedUtcDay, utcMoment.component.day.value, "UTC day should be $expectedUtcDay.")
        assertEquals(expectedTokyoDay, tokyoMoment.component.day.value, "Tokyo day should be $expectedTokyoDay due to day boundary crossing.")
    }

    @Test
    fun `inTimeZone should reflect correct offset string for each timezone`() {
        val moment = Verdandi.at(year = 2025, month = 6, day = 15, hour = 12, minute = 0, second = 0)
        val expectedUtcOffset = "+00:00"
        val expectedTokyoOffset = "+09:00"
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val utcMoment = moment inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = moment inTimeZone tokyoTimeZone

        assertEquals(expectedUtcOffset, utcMoment.component.offset.toString(), "UTC offset should be $expectedUtcOffset.")
        assertEquals(expectedTokyoOffset, tokyoMoment.component.offset.toString(), "Tokyo offset should be $expectedTokyoOffset.")
    }

    @Test
    fun `FromMomentFactory should return valid interval`() {
        val moment = Verdandi.at(year = 2025, month = 6, day = 15)
        val toMoment = Verdandi.at(year = 2025, month = 6, day = 16)
        val query = QueryData(TemporalDirection.NEXT, 1, TemporalUnit.Days, moment)
        val fromFactory = FromMomentFactory(query)

        val interval = fromFactory.from(toMoment)

        assertEquals(moment + 1.days, interval.start, "Start moment should match.")
        assertEquals(toMoment, interval.end  - 1.days, "End moment should match.")
    }

    @Test
    fun `invalid query in FromMomentFactory should throw exception`() {
        val moment = Verdandi.at(year = 2025, month = 6, day = 15)
        val fromFactory = FromMomentFactory(null)

        assertFailsWith<VerdandiStateException> {
            fromFactory.from(moment)
        }
    }
}
