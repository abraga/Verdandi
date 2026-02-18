package com.github.abraga.verdandi.api.model.component

import com.github.abraga.verdandi.api.exception.VerdandiValidationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerdandiComponentsTest {

    @Test
    fun `Year should store and return correct value`() {
        val inputValue = 2025
        val year = Year(inputValue)

        assertEquals(inputValue, year.value, "Year value should be $inputValue.")
    }

    @Test
    fun `Year short should return last two digits`() {
        val year = Year(2025)
        val expectedShort = "25"

        assertEquals(expectedShort, year.short, "Short representation should be $expectedShort.")
    }

    @Test
    fun `Year short should handle years less than 10`() {
        val year = Year(2005)
        val expectedShort = "05"

        assertEquals(expectedShort, year.short, "Short representation should be $expectedShort.")
    }

    @Test
    fun `Year isLeapYear should return true for leap year`() {
        val leapYear = Year(2024)

        assertTrue(leapYear.isLeapYear, "2024 should be a leap year.")
    }

    @Test
    fun `Year isLeapYear should return false for non-leap year`() {
        val nonLeapYear = Year(2025)

        assertFalse(nonLeapYear.isLeapYear, "2025 should not be a leap year.")
    }

    @Test
    fun `Year isLeapYear should return true for year divisible by 400`() {
        val leapCenturyYear = Year(2000)

        assertTrue(leapCenturyYear.isLeapYear, "2000 should be a leap year.")
    }

    @Test
    fun `Year isLeapYear should return false for year divisible by 100 but not 400`() {
        val centuryYear = Year(1900)

        assertFalse(centuryYear.isLeapYear, "1900 should not be a leap year.")
    }

    @Test
    fun `Year toString should return zero-padded 4-digit string`() {
        val year = Year(2025)
        val expectedString = "2025"

        assertEquals(expectedString, year.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `Year toString should zero-pad years less than 1000`() {
        val year = Year(99)
        val expectedString = "0099"

        assertEquals(expectedString, year.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `Quarter should store and return correct value`() {
        val quarter = Quarter(2)
        val expectedValue = 2

        assertEquals(expectedValue, quarter.value, "Quarter value should be $expectedValue.")
    }

    @Test
    fun `Quarter should throw exception for value less than 1`() {
        assertFailsWith<VerdandiValidationException> {
            Quarter(0)
        }
    }

    @Test
    fun `Quarter should throw exception for value greater than 4`() {
        assertFailsWith<VerdandiValidationException> {
            Quarter(5)
        }
    }

    @Test
    fun `Quarter name should return Q prefix with number`() {
        val quarter = Quarter(3)
        val expectedName = "Q3"

        assertEquals(expectedName, quarter.name, "Quarter name should be $expectedName.")
    }

    @Test
    fun `Month should store and return correct value`() {
        val month = Month(6)
        val expectedValue = 6

        assertEquals(expectedValue, month.value, "Month value should be $expectedValue.")
    }

    @Test
    fun `Month should throw exception for value less than 1`() {
        assertFailsWith<VerdandiValidationException> {
            Month(0)
        }
    }

    @Test
    fun `Month should throw exception for value greater than 12`() {
        assertFailsWith<VerdandiValidationException> {
            Month(13)
        }
    }

    @Test
    fun `Month quarter should return correct quarter for each month`() {
        assertEquals(1, Month(1).quarter.value, "January should be in Q1.")
        assertEquals(1, Month(3).quarter.value, "March should be in Q1.")
        assertEquals(2, Month(4).quarter.value, "April should be in Q2.")
        assertEquals(2, Month(6).quarter.value, "June should be in Q2.")
        assertEquals(3, Month(7).quarter.value, "July should be in Q3.")
        assertEquals(3, Month(9).quarter.value, "September should be in Q3.")
        assertEquals(4, Month(10).quarter.value, "October should be in Q4.")
        assertEquals(4, Month(12).quarter.value, "December should be in Q4.")
    }

    @Test
    fun `Month toString should return zero-padded 2-digit string`() {
        val month = Month(6)
        val expectedString = "06"

        assertEquals(expectedString, month.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `DayOfMonth should store and return correct value`() {
        val day = DayOfMonth(15)
        val expectedValue = 15

        assertEquals(expectedValue, day.value, "DayOfMonth value should be $expectedValue.")
    }

    @Test
    fun `DayOfMonth should throw exception for value less than 1`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfMonth(0)
        }
    }

    @Test
    fun `DayOfMonth should throw exception for value greater than 31`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfMonth(32)
        }
    }

    @Test
    fun `DayOfMonth toString should return zero-padded 2-digit string`() {
        val day = DayOfMonth(5)
        val expectedString = "05"

        assertEquals(expectedString, day.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `DayOfWeek should store and return correct value`() {
        val dayOfWeek = DayOfWeek(7)
        val expectedValue = 7

        assertEquals(expectedValue, dayOfWeek.value, "DayOfWeek value should be $expectedValue.")
    }

    @Test
    fun `DayOfWeek should throw exception for value less than 1`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfWeek(0)
        }
    }

    @Test
    fun `DayOfWeek should throw exception for value greater than 7`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfWeek(8)
        }
    }

    @Test
    fun `Hour should store and return correct value`() {
        val hour = Hour(14)
        val expectedValue = 14

        assertEquals(expectedValue, hour.value, "Hour value should be $expectedValue.")
    }

    @Test
    fun `Hour should throw exception for value less than 0`() {
        assertFailsWith<VerdandiValidationException> {
            Hour(-1)
        }
    }

    @Test
    fun `Hour should throw exception for value greater than 23`() {
        assertFailsWith<VerdandiValidationException> {
            Hour(24)
        }
    }

    @Test
    fun `Hour valueIn12 should return 12 for midnight`() {
        val hour = Hour(0)
        val expectedValueIn12 = 12

        assertEquals(expectedValueIn12, hour.valueIn12, "Midnight should display as 12 in 12h format.")
    }

    @Test
    fun `Hour valueIn12 should return 12 for noon`() {
        val hour = Hour(12)
        val expectedValueIn12 = 12

        assertEquals(expectedValueIn12, hour.valueIn12, "Noon should display as 12 in 12h format.")
    }

    @Test
    fun `Hour valueIn12 should return correct value for afternoon hours`() {
        val hour = Hour(14)
        val expectedValueIn12 = 2

        assertEquals(expectedValueIn12, hour.valueIn12, "14:00 should display as 2 in 12h format.")
    }

    @Test
    fun `Hour valueIn12 should return same value for morning hours`() {
        val hour = Hour(9)
        val expectedValueIn12 = 9

        assertEquals(expectedValueIn12, hour.valueIn12, "9:00 should display as 9 in 12h format.")
    }

    @Test
    fun `Hour isAM should return true for hours before noon`() {
        val morningHour = Hour(9)

        assertTrue(morningHour.isAM, "9:00 should be AM.")
    }

    @Test
    fun `Hour isAM should return true for midnight`() {
        val midnight = Hour(0)

        assertTrue(midnight.isAM, "Midnight should be AM.")
    }

    @Test
    fun `Hour isPM should return true for noon`() {
        val noon = Hour(12)

        assertTrue(noon.isPM, "Noon should be PM.")
    }

    @Test
    fun `Hour isPM should return true for afternoon hours`() {
        val afternoon = Hour(15)

        assertTrue(afternoon.isPM, "15:00 should be PM.")
    }

    @Test
    fun `Hour toString should return zero-padded 2-digit string`() {
        val hour = Hour(8)
        val expectedString = "08"

        assertEquals(expectedString, hour.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `Minute should store and return correct value`() {
        val minute = Minute(30)
        val expectedValue = 30

        assertEquals(expectedValue, minute.value, "Minute value should be $expectedValue.")
    }

    @Test
    fun `Minute should throw exception for value less than 0`() {
        assertFailsWith<VerdandiValidationException> {
            Minute(-1)
        }
    }

    @Test
    fun `Minute should throw exception for value greater than 59`() {
        assertFailsWith<VerdandiValidationException> {
            Minute(60)
        }
    }

    @Test
    fun `Minute toString should return zero-padded 2-digit string`() {
        val minute = Minute(5)
        val expectedString = "05"

        assertEquals(expectedString, minute.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `Second should store and return correct value`() {
        val second = Second(45)
        val expectedValue = 45

        assertEquals(expectedValue, second.value, "Second value should be $expectedValue.")
    }

    @Test
    fun `Second should throw exception for value less than 0`() {
        assertFailsWith<VerdandiValidationException> {
            Second(-1)
        }
    }

    @Test
    fun `Second should throw exception for value greater than 59`() {
        assertFailsWith<VerdandiValidationException> {
            Second(60)
        }
    }

    @Test
    fun `Second toString should return zero-padded 2-digit string`() {
        val second = Second(9)
        val expectedString = "09"

        assertEquals(expectedString, second.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `Millisecond should store and return correct value`() {
        val millisecond = Millisecond(500)
        val expectedValue = 500

        assertEquals(expectedValue, millisecond.value, "Millisecond value should be $expectedValue.")
    }

    @Test
    fun `Millisecond should throw exception for value less than 0`() {
        assertFailsWith<VerdandiValidationException> {
            Millisecond(-1)
        }
    }

    @Test
    fun `Millisecond should throw exception for value greater than 999`() {
        assertFailsWith<VerdandiValidationException> {
            Millisecond(1000)
        }
    }

    @Test
    fun `Millisecond toString should return unpadded string`() {
        val millisecond = Millisecond(123)
        val expectedString = "123"

        assertEquals(expectedString, millisecond.toString(), "toString should return $expectedString.")
    }

    @Test
    fun `DayOfTheYear should store and return correct value`() {
        val dayOfYear = DayOfTheYear(182)
        val expectedValue = 182

        assertEquals(expectedValue, dayOfYear.value, "DayOfTheYear value should be $expectedValue.")
    }

    @Test
    fun `DayOfTheYear should throw exception for value less than 1`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfTheYear(0)
        }
    }

    @Test
    fun `DayOfTheYear should throw exception for value greater than 366`() {
        assertFailsWith<VerdandiValidationException> {
            DayOfTheYear(367)
        }
    }

    @Test
    fun `VerdandiComponent compareTo should work correctly`() {
        val year2024 = Year(2024)
        val year2025 = Year(2025)

        assertTrue(year2024 < year2025, "2024 should be less than 2025.")
        assertTrue(year2025 > year2024, "2025 should be greater than 2024.")
    }
}

