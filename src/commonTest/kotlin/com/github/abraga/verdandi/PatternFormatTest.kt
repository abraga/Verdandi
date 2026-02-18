package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class PatternFormatTest {

    @Test
    fun `yyyy pattern should format four-digit year`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val inputPattern = "yyyy"
        val expectedResult = "2025"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `yy pattern should format two-digit year`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val inputPattern = "yy"
        val expectedResult = "25"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `MM pattern should format zero-padded month`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val inputPattern = "MM"
        val expectedResult = "06"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `dd pattern should format zero-padded day`() {
        val inputTimestamp = "2025-06-05T12:00:00Z"
        val inputPattern = "dd"
        val expectedResult = "05"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `d pattern should format unpadded day`() {
        val inputTimestamp = "2025-06-05T12:00:00Z"
        val inputPattern = "d"
        val expectedResult = "5"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `HH pattern should format 24-hour time with zero-padding`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val inputPattern = "HH"
        val expectedResult = "14"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh pattern should format 12-hour time with zero-padding`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val inputPattern = "hh"
        val expectedResult = "02"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `mm pattern should format zero-padded minutes`() {
        val inputTimestamp = "2025-06-15T12:07:00Z"
        val inputPattern = "mm"
        val expectedResult = "07"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `ss pattern should format zero-padded seconds`() {
        val inputTimestamp = "2025-06-15T12:00:09Z"
        val inputPattern = "ss"
        val expectedResult = "09"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `SSS pattern should format milliseconds with zero-padding`() {
        val inputTimestamp = "2025-06-15T12:00:00.123Z"
        val inputPattern = "SSS"
        val expectedResult = "123"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `Z pattern should format timezone offset in UTC`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val inputPattern = "Z"
        val expectedResult = "+00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `Z pattern should format timezone offset for Tokyo`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val inputPattern = "Z"
        val expectedResult = "+09:00"
        val tokyoTimeZone = VerdandiTimeZone.of("Asia/Tokyo")

        val moment = Verdandi.from(inputTimestamp) inTimeZone tokyoTimeZone
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `ISO timestamp pattern should format correctly`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val expectedResult = "2025-06-15T14:30:45+00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `date-only pattern should exclude time components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val inputPattern = "yyyy-MM-dd"
        val expectedResult = "2025-06-15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `time-only pattern should exclude date components`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val inputPattern = "HH:mm:ss"
        val expectedResult = "14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `slash separator pattern should format correctly`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val inputPattern = "yyyy/MM/dd"
        val expectedResult = "2025/06/15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `literal text in pattern should be preserved`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val inputPattern = "yyyy-MM-dd 'at' HH:mm"
        val expectedResult = "2025-06-15 at 14:30"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh pattern at midnight should display 12`() {
        val inputTimestamp = "2025-06-15T00:30:00Z"
        val inputPattern = "hh"
        val expectedResult = "12"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh pattern at noon should display 12`() {
        val inputTimestamp = "2025-06-15T12:30:00Z"
        val inputPattern = "hh"
        val expectedResult = "12"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh pattern at 1 PM should display 01`() {
        val inputTimestamp = "2025-06-15T13:30:00Z"
        val inputPattern = "hh"
        val expectedResult = "01"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `hh pattern at 11 PM should display 11`() {
        val inputTimestamp = "2025-06-15T23:30:00Z"
        val inputPattern = "hh"
        val expectedResult = "11"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `complex pattern with milliseconds should format correctly`() {
        val inputTimestamp = "2025-06-15T14:30:45.500Z"
        val inputPattern = "yyyy-MM-dd HH:mm:ss.SSS"
        val expectedResult = "2025-06-15 14:30:45.500"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val actualResult = moment format inputPattern

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }
}
