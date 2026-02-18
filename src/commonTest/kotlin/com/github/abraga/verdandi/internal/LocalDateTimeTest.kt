package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.internal.core.VerdandiLocalDate
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.core.VerdandiLocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateTimeTest {

    @Test
    fun `VerdandiLocalDate should create with correct year month and day`() {
        val inputYear = 2025
        val inputMonth = 6
        val inputDay = 15

        val localDate = VerdandiLocalDate.of(inputYear, inputMonth, inputDay)

        assertEquals(inputYear, localDate.year, "Year should be $inputYear.")
        assertEquals(inputMonth, localDate.monthNumber, "Month number should be $inputMonth.")
        assertEquals(inputDay, localDate.dayOfMonth, "Day of month should be $inputDay.")
    }

    @Test
    fun `VerdandiLocalDate isLeapYear should return true for leap year`() {
        val leapYear = 2024

        val localDate = VerdandiLocalDate.of(leapYear, 1, 1)

        assertTrue(localDate.isLeapYear, "$leapYear should be a leap year.")
    }

    @Test
    fun `VerdandiLocalDate isLeapYear should return false for non-leap year`() {
        val nonLeapYear = 2025

        val localDate = VerdandiLocalDate.of(nonLeapYear, 1, 1)

        assertFalse(localDate.isLeapYear, "$nonLeapYear should not be a leap year.")
    }

    @Test
    fun `VerdandiLocalDate lengthOfMonth should return correct days for January`() {
        val localDate = VerdandiLocalDate.of(2025, 1, 15)
        val expectedLength = 31

        assertEquals(expectedLength, localDate.lengthOfMonth, "January should have $expectedLength days.")
    }

    @Test
    fun `VerdandiLocalDate lengthOfMonth should return 28 for February in non-leap year`() {
        val localDate = VerdandiLocalDate.of(2025, 2, 15)
        val expectedLength = 28

        assertEquals(expectedLength, localDate.lengthOfMonth, "February should have $expectedLength days in non-leap year.")
    }

    @Test
    fun `VerdandiLocalDate lengthOfMonth should return 29 for February in leap year`() {
        val localDate = VerdandiLocalDate.of(2024, 2, 15)
        val expectedLength = 29

        assertEquals(expectedLength, localDate.lengthOfMonth, "February should have $expectedLength days in leap year.")
    }

    @Test
    fun `VerdandiLocalDate lengthOfYear should return 365 for non-leap year`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 15)
        val expectedLength = 365

        assertEquals(expectedLength, localDate.lengthOfYear, "Non-leap year should have $expectedLength days.")
    }

    @Test
    fun `VerdandiLocalDate lengthOfYear should return 366 for leap year`() {
        val localDate = VerdandiLocalDate.of(2024, 6, 15)
        val expectedLength = 366

        assertEquals(expectedLength, localDate.lengthOfYear, "Leap year should have $expectedLength days.")
    }

    @Test
    fun `VerdandiLocalDate dayOfYear should return 1 for January 1`() {
        val localDate = VerdandiLocalDate.of(2025, 1, 1)
        val expectedDayOfYear = 1

        assertEquals(expectedDayOfYear, localDate.dayOfYear, "January 1 should be day $expectedDayOfYear of year.")
    }

    @Test
    fun `VerdandiLocalDate dayOfYear should return correct value for mid-year`() {
        val localDate = VerdandiLocalDate.of(2025, 7, 1)
        val expectedDayOfYear = 182

        assertEquals(expectedDayOfYear, localDate.dayOfYear, "July 1 should be day $expectedDayOfYear of year.")
    }

    @Test
    fun `VerdandiLocalDate plusDays should add days correctly`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 15)
        val daysToAdd = 10L
        val expectedDay = 25

        val result = localDate.plusDays(daysToAdd)

        assertEquals(expectedDay, result.dayOfMonth, "Day should be $expectedDay after adding $daysToAdd days.")
    }

    @Test
    fun `VerdandiLocalDate plusDays should handle month overflow`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 25)
        val daysToAdd = 10L
        val expectedMonth = 7
        val expectedDay = 5

        val result = localDate.plusDays(daysToAdd)

        assertEquals(expectedMonth, result.monthNumber, "Month should be $expectedMonth.")
        assertEquals(expectedDay, result.dayOfMonth, "Day should be $expectedDay.")
    }

    @Test
    fun `VerdandiLocalDate minusDays should subtract days correctly`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 15)
        val daysToSubtract = 10L
        val expectedDay = 5

        val result = localDate.minusDays(daysToSubtract)

        assertEquals(expectedDay, result.dayOfMonth, "Day should be $expectedDay after subtracting $daysToSubtract days.")
    }

    @Test
    fun `VerdandiLocalDate plusWeeks should add weeks correctly`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 1)
        val weeksToAdd = 2L
        val expectedDay = 15

        val result = localDate.plusWeeks(weeksToAdd)

        assertEquals(expectedDay, result.dayOfMonth, "Day should be $expectedDay after adding $weeksToAdd weeks.")
    }

    @Test
    fun `VerdandiLocalDate plusMonths should add months correctly`() {
        val localDate = VerdandiLocalDate.of(2025, 6, 15)
        val monthsToAdd = 3L
        val expectedMonth = 9

        val result = localDate.plusMonths(monthsToAdd)

        assertEquals(expectedMonth, result.monthNumber, "Month should be $expectedMonth after adding $monthsToAdd months.")
    }

    @Test
    fun `VerdandiLocalDate plusMonths should handle year overflow`() {
        val localDate = VerdandiLocalDate.of(2025, 11, 15)
        val monthsToAdd = 3L
        val expectedYear = 2026
        val expectedMonth = 2

        val result = localDate.plusMonths(monthsToAdd)

        assertEquals(expectedYear, result.year, "Year should be $expectedYear.")
        assertEquals(expectedMonth, result.monthNumber, "Month should be $expectedMonth.")
    }

    @Test
    fun `VerdandiLocalDate plusMonths should clamp day to valid range`() {
        val localDate = VerdandiLocalDate.of(2025, 1, 31)
        val monthsToAdd = 1L
        val expectedDay = 28

        val result = localDate.plusMonths(monthsToAdd)

        assertEquals(expectedDay, result.dayOfMonth, "Day should be clamped to $expectedDay for February.")
    }

    @Test
    fun `VerdandiLocalDate fromEpochDays should return correct date for epoch day 0`() {
        val epochDays = 0L
        val expectedYear = 1970
        val expectedMonth = 1
        val expectedDay = 1

        val result = VerdandiLocalDate.fromEpochDays(epochDays)

        assertEquals(expectedYear, result.year, "Year should be $expectedYear for epoch day 0.")
        assertEquals(expectedMonth, result.monthNumber, "Month should be $expectedMonth.")
        assertEquals(expectedDay, result.dayOfMonth, "Day should be $expectedDay.")
    }

    @Test
    fun `VerdandiLocalTime should create with correct hour minute and second`() {
        val inputHour = 14
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 0

        val localTime = VerdandiLocalTime.of(inputHour, inputMinute, inputSecond, inputNanosecond)

        assertEquals(inputHour, localTime.hour, "Hour should be $inputHour.")
        assertEquals(inputMinute, localTime.minute, "Minute should be $inputMinute.")
        assertEquals(inputSecond, localTime.second, "Second should be $inputSecond.")
    }

    @Test
    fun `VerdandiLocalTime midnight should have all zero components`() {
        val localTime = VerdandiLocalTime.of(0, 0, 0, 0)

        assertEquals(0, localTime.hour, "Hour should be 0 for midnight.")
        assertEquals(0, localTime.minute, "Minute should be 0 for midnight.")
        assertEquals(0, localTime.second, "Second should be 0 for midnight.")
    }

    @Test
    fun `VerdandiLocalDateTime should combine date and time correctly`() {
        val inputYear = 2025
        val inputMonth = 6
        val inputDay = 15
        val inputHour = 14
        val inputMinute = 30
        val inputSecond = 45
        val inputNanosecond = 0

        val localDateTime = VerdandiLocalDateTime.of(
            year = inputYear,
            monthNumber = inputMonth,
            dayOfMonth = inputDay,
            hour = inputHour,
            minute = inputMinute,
            second = inputSecond,
            nanosecond = inputNanosecond
        )

        assertEquals(inputYear, localDateTime.year, "Year should be $inputYear.")
        assertEquals(inputMonth, localDateTime.monthNumber, "Month should be $inputMonth.")
        assertEquals(inputDay, localDateTime.dayOfMonth, "Day should be $inputDay.")
        assertEquals(inputHour, localDateTime.hour, "Hour should be $inputHour.")
        assertEquals(inputMinute, localDateTime.minute, "Minute should be $inputMinute.")
        assertEquals(inputSecond, localDateTime.second, "Second should be $inputSecond.")
    }

    @Test
    fun `VerdandiLocalDateTime plusDays should add days correctly`() {
        val localDateTime = VerdandiLocalDateTime.of(2025, 6, 15, 14, 30, 45, 0)
        val daysToAdd = 5L

        val result = localDateTime.plusDays(daysToAdd)

        assertEquals(20, result.dayOfMonth, "Day should be 20 after adding 5 days.")
        assertEquals(14, result.hour, "Hour should remain 14.")
    }

    @Test
    fun `VerdandiLocalDateTime plusHours should add hours correctly`() {
        val localDateTime = VerdandiLocalDateTime.of(2025, 6, 15, 14, 30, 45, 0)
        val hoursToAdd = 5L

        val result = localDateTime.plusHours(hoursToAdd)

        assertEquals(19, result.hour, "Hour should be 19 after adding 5 hours.")
        assertEquals(15, result.dayOfMonth, "Day should remain 15.")
    }

    @Test
    fun `VerdandiLocalDateTime plusHours should handle day overflow`() {
        val localDateTime = VerdandiLocalDateTime.of(2025, 6, 15, 22, 0, 0, 0)
        val hoursToAdd = 5L

        val result = localDateTime.plusHours(hoursToAdd)

        assertEquals(3, result.hour, "Hour should be 3 after overflow.")
        assertEquals(16, result.dayOfMonth, "Day should be 16 after overflow.")
    }

    @Test
    fun `VerdandiLocalDateTime plusMinutes should add minutes correctly`() {
        val localDateTime = VerdandiLocalDateTime.of(2025, 6, 15, 14, 30, 0, 0)
        val minutesToAdd = 45L

        val result = localDateTime.plusMinutes(minutesToAdd)

        assertEquals(15, result.minute, "Minute should be 15 after adding 45 minutes.")
        assertEquals(15, result.hour, "Hour should be 15 after overflow.")
    }

    @Test
    fun `VerdandiLocalDateTime compareTo should work correctly`() {
        val earlier = VerdandiLocalDateTime.of(2025, 6, 15, 10, 0, 0, 0)
        val later = VerdandiLocalDateTime.of(2025, 6, 15, 14, 0, 0, 0)

        assertTrue(earlier < later, "Earlier datetime should be less than later.")
        assertTrue(later > earlier, "Later datetime should be greater than earlier.")
    }
}

