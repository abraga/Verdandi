package com.github.abraga.verdandi.api.scope.format.pattern

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerdandiFormatPatternTest {

    @Test
    fun `VerdandiDatePattern should render year correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedYear = "2025"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy }

        assertEquals(expectedYear, result, "Pattern should render year as $expectedYear.")
    }

    @Test
    fun `VerdandiDatePattern minus operator should join with hyphen`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025-06-15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy - MM - dd }

        assertEquals(expectedResult, result, "Pattern should render as $expectedResult.")
    }

    @Test
    fun `VerdandiDatePattern div operator should join with slash`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedResult = "2025/06/15"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy / MM / dd }

        assertEquals(expectedResult, result, "Pattern should render as $expectedResult.")
    }

    @Test
    fun `VerdandiTimePattern should render hour correctly`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedHour = "14"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { HH }

        assertEquals(expectedHour, result, "Pattern should render hour as $expectedHour.")
    }

    @Test
    fun `VerdandiTimePattern dot should join with colon`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { HH.mm.ss }

        assertEquals(expectedResult, result, "Pattern should render as $expectedResult.")
    }

    @Test
    fun `VerdandiFormatPattern T operator should join date and time with T character`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "2025-06-15T14:30:45"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy-MM-dd T HH.mm.ss }

        assertEquals(expectedResult, result, "Pattern should render as $expectedResult.")
    }

    @Test
    fun `VerdandiFormatPattern plus operator should append literal string`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy-MM-dd + " is the date" }

        assertTrue(result.contains("is the date"), "Pattern should append literal string.")
    }

    @Test
    fun `VerdandiDatePattern MMM should render short month name`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { MMM }

        assertTrue(result.isNotEmpty(), "MMM should render short month name.")
        assertEquals(3, result.length, "Short month name should be 3 characters.")
    }

    @Test
    fun `VerdandiDatePattern MMMM should render full month name`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { MMMM }

        assertTrue(result.isNotEmpty(), "MMMM should render full month name.")
        assertTrue(result.length > 3, "Full month name should be more than 3 characters.")
    }

    @Test
    fun `VerdandiDatePattern EEE should render short day of week name`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { EEE }

        assertTrue(result.isNotEmpty(), "EEE should render short day of week name.")
        assertEquals(3, result.length, "Short day name should be 3 characters.")
    }

    @Test
    fun `VerdandiDatePattern EEEE should render full day of week name`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { EEEE }

        assertTrue(result.isNotEmpty(), "EEEE should render full day of week name.")
        assertTrue(result.length > 3, "Full day name should be more than 3 characters.")
    }

    @Test
    fun `VerdandiDatePattern Q should render quarter`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedQuarter = "Q2"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { Q }

        assertEquals(expectedQuarter, result, "Pattern should render quarter as $expectedQuarter.")
    }

    @Test
    fun `VerdandiTimePattern Z should render timezone offset`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedOffset = "+00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { Z }

        assertEquals(expectedOffset, result, "Pattern should render offset as $expectedOffset.")
    }

    @Test
    fun `VerdandiTimePattern SSS should render milliseconds`() {
        val inputTimestamp = "2025-06-15T12:00:00.456Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { SSS }

        assertTrue(result.contains("456"), "Pattern should contain milliseconds 456.")
    }

    @Test
    fun `VerdandiTimePattern hh should render 12-hour format`() {
        val inputTimestamp = "2025-06-15T14:30:00Z"
        val expectedHour = "02"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { hh }

        assertEquals(expectedHour, result, "Pattern should render 12h hour as $expectedHour.")
    }

    @Test
    fun `VerdandiTimePattern AM_PM should render correct period`() {
        val morningTimestamp = "2025-06-15T08:00:00Z"
        val afternoonTimestamp = "2025-06-15T14:00:00Z"

        val morningMoment = Verdandi.from(morningTimestamp) inTimeZone VerdandiTimeZone.UTC
        val afternoonMoment = Verdandi.from(afternoonTimestamp) inTimeZone VerdandiTimeZone.UTC
        val morningResult = morningMoment format { A }
        val afternoonResult = afternoonMoment format { A }

        assertEquals("AM", morningResult, "Morning should render as AM.")
        assertEquals("PM", afternoonResult, "Afternoon should render as PM.")
    }

    @Test
    fun `complex pattern should render all components correctly`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"
        val expectedResult = "2025-06-15T14:30:45 +00:00"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment format { yyyy-MM-dd T HH.mm.ss..Z }

        assertEquals(expectedResult, result, "Complex pattern should render as $expectedResult.")
    }
}
