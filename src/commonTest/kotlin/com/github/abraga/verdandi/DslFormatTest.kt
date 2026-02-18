package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class DslFormatTest {

    @Test
    fun `yyyy token should format four-digit year`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `yy token should format two-digit year`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "25"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yy }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `MM token should format zero-padded month`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "06"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { MM }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `dd token should format zero-padded day`() {
        val inputTimestamp = "2025-06-05T12:00:00Z"
        val expectedResult = "05"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { dd }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `d token should format unpadded day`() {
        val inputTimestamp = "2025-06-05T12:00:00Z"
        val expectedResult = "5"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { d }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `HH token should format 24-hour time with zero-padding`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedResult = "14"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { HH }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh token should format 12-hour time with zero-padding`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedResult = "02"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { hh }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `mm token should format zero-padded minutes`() {
        val inputTimestamp = "2025-06-15T12:07:00Z"
        val expectedResult = "07"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { mm }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `ss token should format zero-padded seconds`() {
        val inputTimestamp = "2025-06-15T12:00:09Z"
        val expectedResult = "09"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { ss }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `SSS token should format milliseconds with zero-padding`() {
        val inputTimestamp = "2025-06-15T12:00:00.123Z"
        val expectedResult = "12.123"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { HH.SSS }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `Z token should format timezone offset`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "+00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { Z }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `minus operator should format with hyphen separator`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025-06-15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `div operator should format with slash separator`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025/06/15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy/MM/dd }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `rem operator should format with colon separator for time`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { HH.mm.ss }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `T separator should format with space between date and time`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "2025-06-15T14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd T HH.mm.ss }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `times operator should format with space separator`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "2025-06-15T14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd T HH.mm.ss }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `comma separator should insert comma and space`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedResult = "2025-06-15, 14:30"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd comma HH.mm }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `at separator should insert at keyword with spaces`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedResult = "2025-06-15 at 14:30"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd at HH.mm }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `'A' token should format midnight as AM`() {
        val inputTimestamp = "2025-06-15T00:30:00Z"
        val expectedResult = "12:30 AM"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { hh.mm..A }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `'A' token should format noon as PM`() {
        val inputTimestamp = "2025-06-15T12:30:00Z"
        val expectedResult = "12:30 PM"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { hh.mm..A }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `complex format should combine date and time components`() {
        val inputTimestamp = "2025-06-15T14:30:45.500Z"
        val expectedResult = "2025-06-15T14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy-MM-dd T HH.mm.ss }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `format with timezone offset should include Z token`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "2025-06-15T14:30:45 +00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format { yyyy - MM - dd T HH.mm.ss..Z }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `format should adjust hour for Tokyo timezone`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025-06-15T21:00 +09:00"
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val moment = Verdandi.from(inputTimestamp) inTimeZone tokyoTimeZone
        val actualResult = moment format { yyyy - MM - dd T HH.mm..Z }

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }
}
