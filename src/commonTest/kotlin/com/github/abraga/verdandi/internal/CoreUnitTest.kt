package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.internal.core.unit.VerdandiDayOfWeek
import com.github.abraga.verdandi.internal.core.unit.VerdandiMonth
import com.github.abraga.verdandi.api.exception.VerdandiValidationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CoreUnitTest {

    @Test
    fun `VerdandiMonth of should return correct month for valid number`() {
        val inputNumber = 6
        val expectedMonth = VerdandiMonth.JUNE

        val actualMonth = VerdandiMonth.of(inputNumber)

        assertEquals(expectedMonth, actualMonth, "Month should be $expectedMonth for number $inputNumber.")
    }

    @Test
    fun `VerdandiMonth of should return January for number 1`() {
        val inputNumber = 1
        val expectedMonth = VerdandiMonth.JANUARY

        val actualMonth = VerdandiMonth.of(inputNumber)

        assertEquals(expectedMonth, actualMonth, "Month should be $expectedMonth for number $inputNumber.")
    }

    @Test
    fun `VerdandiMonth of should return December for number 12`() {
        val inputNumber = 12
        val expectedMonth = VerdandiMonth.DECEMBER

        val actualMonth = VerdandiMonth.of(inputNumber)

        assertEquals(expectedMonth, actualMonth, "Month should be $expectedMonth for number $inputNumber.")
    }

    @Test
    fun `VerdandiMonth of should throw exception for number less than 1`() {
        val invalidNumber = 0

        assertFailsWith<VerdandiValidationException> {
            VerdandiMonth.of(invalidNumber)
        }
    }

    @Test
    fun `VerdandiMonth of should throw exception for number greater than 12`() {
        val invalidNumber = 13

        assertFailsWith<VerdandiValidationException> {
            VerdandiMonth.of(invalidNumber)
        }
    }

    @Test
    fun `VerdandiMonth lengthInYear should return 31 for January`() {
        val month = VerdandiMonth.JANUARY
        val inputYear = 2025
        val expectedLength = 31

        val actualLength = month.lengthInYear(inputYear)

        assertEquals(expectedLength, actualLength, "January should have $expectedLength days.")
    }

    @Test
    fun `VerdandiMonth lengthInYear should return 28 for February in non-leap year`() {
        val month = VerdandiMonth.FEBRUARY
        val nonLeapYear = 2025
        val expectedLength = 28

        val actualLength = month.lengthInYear(nonLeapYear)

        assertEquals(expectedLength, actualLength, "February should have $expectedLength days in non-leap year.")
    }

    @Test
    fun `VerdandiMonth lengthInYear should return 29 for February in leap year`() {
        val month = VerdandiMonth.FEBRUARY
        val leapYear = 2024
        val expectedLength = 29

        val actualLength = month.lengthInYear(leapYear)

        assertEquals(expectedLength, actualLength, "February should have $expectedLength days in leap year.")
    }

    @Test
    fun `VerdandiMonth lengthInYear should return 30 for April`() {
        val month = VerdandiMonth.APRIL
        val inputYear = 2025
        val expectedLength = 30

        val actualLength = month.lengthInYear(inputYear)

        assertEquals(expectedLength, actualLength, "April should have $expectedLength days.")
    }

    @Test
    fun `VerdandiMonth isLeapYear should return true for year divisible by 4 but not 100`() {
        val leapYear = 2024

        val isLeap = VerdandiMonth.isLeapYear(leapYear)

        assertTrue(isLeap, "$leapYear should be a leap year.")
    }

    @Test
    fun `VerdandiMonth isLeapYear should return false for year divisible by 100 but not 400`() {
        val centuryYear = 1900

        val isLeap = VerdandiMonth.isLeapYear(centuryYear)

        assertFalse(isLeap, "$centuryYear should not be a leap year.")
    }

    @Test
    fun `VerdandiMonth isLeapYear should return true for year divisible by 400`() {
        val leapCenturyYear = 2000

        val isLeap = VerdandiMonth.isLeapYear(leapCenturyYear)

        assertTrue(isLeap, "$leapCenturyYear should be a leap year.")
    }

    @Test
    fun `VerdandiMonth isLeapYear should return false for year not divisible by 4`() {
        val nonLeapYear = 2025

        val isLeap = VerdandiMonth.isLeapYear(nonLeapYear)

        assertFalse(isLeap, "$nonLeapYear should not be a leap year.")
    }

    @Test
    fun `VerdandiDayOfWeek of should return Monday for number 1`() {
        val inputNumber = 1
        val expectedDayOfWeek = VerdandiDayOfWeek.MONDAY

        val actualDayOfWeek = VerdandiDayOfWeek.of(inputNumber)

        assertEquals(expectedDayOfWeek, actualDayOfWeek, "Day of week should be $expectedDayOfWeek for number $inputNumber.")
    }

    @Test
    fun `VerdandiDayOfWeek of should return Sunday for number 7`() {
        val inputNumber = 7
        val expectedDayOfWeek = VerdandiDayOfWeek.SUNDAY

        val actualDayOfWeek = VerdandiDayOfWeek.of(inputNumber)

        assertEquals(expectedDayOfWeek, actualDayOfWeek, "Day of week should be $expectedDayOfWeek for number $inputNumber.")
    }

    @Test
    fun `VerdandiDayOfWeek of should throw exception for number less than 1`() {
        val invalidNumber = 0

        assertFailsWith<VerdandiValidationException> {
            VerdandiDayOfWeek.of(invalidNumber)
        }
    }

    @Test
    fun `VerdandiDayOfWeek of should throw exception for number greater than 7`() {
        val invalidNumber = 8

        assertFailsWith<VerdandiValidationException> {
            VerdandiDayOfWeek.of(invalidNumber)
        }
    }

    @Test
    fun `VerdandiDayOfWeek fromEpochDays should return Thursday for epoch day 0`() {
        val epochDays = 0L
        val expectedDayOfWeek = VerdandiDayOfWeek.THURSDAY

        val actualDayOfWeek = VerdandiDayOfWeek.fromEpochDays(epochDays)

        assertEquals(expectedDayOfWeek, actualDayOfWeek, "Epoch day 0 (1970-01-01) should be Thursday.")
    }

    @Test
    fun `VerdandiDayOfWeek fromEpochDays should return Friday for epoch day 1`() {
        val epochDays = 1L
        val expectedDayOfWeek = VerdandiDayOfWeek.FRIDAY

        val actualDayOfWeek = VerdandiDayOfWeek.fromEpochDays(epochDays)

        assertEquals(expectedDayOfWeek, actualDayOfWeek, "Epoch day 1 (1970-01-02) should be Friday.")
    }

    @Test
    fun `VerdandiDayOfWeek fromEpochDays should handle negative epoch days correctly`() {
        val negativeDays = -1L
        val expectedDayOfWeek = VerdandiDayOfWeek.WEDNESDAY

        val actualDayOfWeek = VerdandiDayOfWeek.fromEpochDays(negativeDays)

        assertEquals(expectedDayOfWeek, actualDayOfWeek, "Epoch day -1 (1969-12-31) should be Wednesday.")
    }

    @Test
    fun `VerdandiDayOfWeek isoDayNumber should match expected values`() {
        assertEquals(1, VerdandiDayOfWeek.MONDAY.isoDayNumber, "Monday should have ISO day number 1.")
        assertEquals(2, VerdandiDayOfWeek.TUESDAY.isoDayNumber, "Tuesday should have ISO day number 2.")
        assertEquals(3, VerdandiDayOfWeek.WEDNESDAY.isoDayNumber, "Wednesday should have ISO day number 3.")
        assertEquals(4, VerdandiDayOfWeek.THURSDAY.isoDayNumber, "Thursday should have ISO day number 4.")
        assertEquals(5, VerdandiDayOfWeek.FRIDAY.isoDayNumber, "Friday should have ISO day number 5.")
        assertEquals(6, VerdandiDayOfWeek.SATURDAY.isoDayNumber, "Saturday should have ISO day number 6.")
        assertEquals(7, VerdandiDayOfWeek.SUNDAY.isoDayNumber, "Sunday should have ISO day number 7.")
    }

    @Test
    fun `VerdandiMonth number should match expected values`() {
        assertEquals(1, VerdandiMonth.JANUARY.number, "January should have number 1.")
        assertEquals(6, VerdandiMonth.JUNE.number, "June should have number 6.")
        assertEquals(12, VerdandiMonth.DECEMBER.number, "December should have number 12.")
    }
}

