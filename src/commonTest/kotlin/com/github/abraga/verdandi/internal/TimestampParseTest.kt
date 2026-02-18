package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.exception.VerdandiParseException
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TimestampParseTest {

    @Test
    fun `from should parse ISO 8601 timestamp with Z suffix`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedYear = 2025
        val expectedMonth = 6
        val expectedDay = 15
        val expectedHour = 14
        val expectedMinute = 30
        val expectedSecond = 45

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `from should parse ISO 8601 timestamp with positive offset`() {
        val inputTimestamp = "2025-06-15T14:30:45+05:30"
        val expectedUtcHour = 9

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour after offset.")
    }

    @Test
    fun `from should parse ISO 8601 timestamp with negative offset`() {
        val inputTimestamp = "2025-06-15T14:30:45-04:00"
        val expectedUtcHour = 18

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour after offset.")
    }

    @Test
    fun `from should parse ISO 8601 timestamp with milliseconds`() {
        val inputTimestamp = "2025-06-15T14:30:45.123Z"
        val expectedMillisecond = 123

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedMillisecond, component.millisecond.value, "Millisecond should be $expectedMillisecond.")
    }

    @Test
    fun `from should parse ISO 8601 timestamp with 3-digit milliseconds`() {
        val inputTimestamp = "2025-06-15T14:30:45.123Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(123, component.millisecond.value, "Millisecond should be 123.")
    }

    @Test
    fun `from should parse compact timestamp without separators`() {
        val inputTimestamp = "20250615T143045Z"
        val expectedYear = 2025
        val expectedMonth = 6
        val expectedDay = 15

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
    }

    @Test
    fun `from should parse timestamp with standard offset format`() {
        val inputTimestamp = "2025-06-15T14:30:45+05:30"
        val expectedUtcHour = 9

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour after offset.")
    }

    @Test
    fun `from should parse ISO 8601 local timestamp without offset`() {
        val inputTimestamp = "2025-06-15T14:30:45"

        val moment = Verdandi.from(inputTimestamp)

        assertTrue(moment.inMilliseconds > 0, "Epoch should be positive.")
    }

    @Test
    fun `from should throw exception for invalid timestamp format`() {
        val invalidTimestamp = "not-a-valid-timestamp"

        assertFailsWith<VerdandiParseException> {
            Verdandi.from(invalidTimestamp)
        }
    }

    @Test
    fun `from should throw exception for empty timestamp`() {
        val emptyTimestamp = ""

        assertFailsWith<VerdandiParseException> {
            Verdandi.from(emptyTimestamp)
        }
    }

    @Test
    fun `from should parse midnight timestamp correctly`() {
        val inputTimestamp = "2025-06-15T00:00:00Z"
        val expectedHour = 0
        val expectedMinute = 0
        val expectedSecond = 0

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `from should parse end of day timestamp correctly`() {
        val inputTimestamp = "2025-06-15T23:59:59Z"
        val expectedHour = 23
        val expectedMinute = 59
        val expectedSecond = 59

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour.")
        assertEquals(expectedMinute, component.minute.value, "Minute should be $expectedMinute.")
        assertEquals(expectedSecond, component.second.value, "Second should be $expectedSecond.")
    }

    @Test
    fun `from should handle date boundary crossing with positive offset`() {
        val inputTimestamp = "2025-06-15T02:00:00+05:00"
        val expectedUtcDay = 14
        val expectedUtcHour = 21

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcDay, component.day.value, "UTC day should be $expectedUtcDay.")
        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour.")
    }

    @Test
    fun `from should handle date boundary crossing with negative offset`() {
        val inputTimestamp = "2025-06-15T22:00:00-05:00"
        val expectedUtcDay = 16
        val expectedUtcHour = 3

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedUtcDay, component.day.value, "UTC day should be $expectedUtcDay.")
        assertEquals(expectedUtcHour, component.hour.value, "UTC hour should be $expectedUtcHour.")
    }

    @Test
    fun `from should parse February 28 date correctly`() {
        val inputTimestamp = "2025-02-28T12:00:00Z"
        val expectedYear = 2025
        val expectedMonth = 2
        val expectedDay = 28

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
    }

    @Test
    fun `from should parse year boundary timestamp correctly`() {
        val inputTimestamp = "2025-12-31T23:59:59Z"
        val expectedYear = 2025
        val expectedMonth = 12
        val expectedDay = 31

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
    }

    @Test
    fun `from should parse new year timestamp correctly`() {
        val inputTimestamp = "2025-01-01T00:00:00Z"
        val expectedYear = 2025
        val expectedMonth = 1
        val expectedDay = 1

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val component = moment.component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay.")
    }
}


