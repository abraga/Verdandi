package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.format.iso.IsoDateFormatter
import com.github.abraga.verdandi.internal.format.iso.IsoTimeFormatter
import com.github.abraga.verdandi.internal.format.iso.IsoTimestampFormatter
import kotlin.test.Test
import kotlin.test.assertEquals

class IsoFormatTest {

    @Test
    fun `IsoDateFormatter should format date with hyphens as separators`() {
        val inputYear = 2025
        val inputMonth = 6
        val inputDay = 15
        val expectedResult = "2025-06-15"

        val actualResult = IsoDateFormatter.format(inputYear, inputMonth, inputDay)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoDateFormatter should zero-pad single digit month`() {
        val inputYear = 2025
        val inputMonth = 1
        val inputDay = 15
        val expectedResult = "2025-01-15"

        val actualResult = IsoDateFormatter.format(inputYear, inputMonth, inputDay)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoDateFormatter should zero-pad single digit day`() {
        val inputYear = 2025
        val inputMonth = 12
        val inputDay = 5
        val expectedResult = "2025-12-05"

        val actualResult = IsoDateFormatter.format(inputYear, inputMonth, inputDay)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoDateFormatter should handle year less than 1000 with zero-padding`() {
        val inputYear = 99
        val inputMonth = 3
        val inputDay = 10
        val expectedResult = "0099-03-10"

        val actualResult = IsoDateFormatter.format(inputYear, inputMonth, inputDay)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimeFormatter should format time with colons as separators`() {
        val inputHour = 14
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 0
        val expectedResult = "14:30:45"

        val actualResult = IsoTimeFormatter.format(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimeFormatter should zero-pad single digit hour`() {
        val inputHour = 8
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 0
        val expectedResult = "08:30:45"

        val actualResult = IsoTimeFormatter.format(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimeFormatter should include nanoseconds when not zero`() {
        val inputHour = 14
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 123000000

        val actualResult = IsoTimeFormatter.format(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(true, actualResult.contains(".123"), "Result should contain nanoseconds fraction.")
    }

    @Test
    fun `IsoTimeFormatter should omit nanoseconds when zero`() {
        val inputHour = 14
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 0
        val expectedResult = "14:30:45"

        val actualResult = IsoTimeFormatter.format(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimeFormatter should handle midnight correctly`() {
        val inputHour = 0
        val inputMinute = 0
        val inputSecond = 0
        val inputNanosecond = 0
        val expectedResult = "00:00:00"

        val actualResult = IsoTimeFormatter.format(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimestampFormatter should format UTC timestamp with Z suffix`() {
        val localDateTime = VerdandiLocalDateTime.of(
            year = 2025,
            monthNumber = 6,
            dayOfMonth = 15,
            hour = 14,
            minute = 30,
            second = 45,
            nanosecond = 0
        )
        val expectedResult = "2025-06-15T14:30:45Z"

        val actualResult = IsoTimestampFormatter.formatUtc(localDateTime)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `IsoTimestampFormatter should include milliseconds when not zero`() {
        val localDateTime = VerdandiLocalDateTime.of(
            year = 2025,
            monthNumber = 6,
            dayOfMonth = 15,
            hour = 14,
            minute = 30,
            second = 45,
            nanosecond = 123000000
        )

        val actualResult = IsoTimestampFormatter.formatUtc(localDateTime)

        assertEquals(true, actualResult.contains(".123"), "Result should contain milliseconds.")
        assertEquals(true, actualResult.endsWith("Z"), "Result should end with Z.")
    }

    @Test
    fun `IsoTimestampFormatter should format midnight correctly`() {
        val localDateTime = VerdandiLocalDateTime.of(
            year = 2025,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )
        val expectedResult = "2025-01-01T00:00:00Z"

        val actualResult = IsoTimestampFormatter.formatUtc(localDateTime)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }
}

