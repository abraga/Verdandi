package com.github.abraga.verdandi

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationTest {

    @Test
    fun `isValidDate should return true for valid date`() {
        assertTrue(Verdandi.isValidDate(2025, 6, 15), "2025-06-15 should be valid.")
    }

    @Test
    fun `isValidDate should return true for leap year Feb 29`() {
        assertTrue(Verdandi.isValidDate(2024, 2, 29), "2024-02-29 should be valid (leap year).")
    }

    @Test
    fun `isValidDate should return false for non-leap year Feb 29`() {
        assertFalse(Verdandi.isValidDate(2025, 2, 29), "2025-02-29 should be invalid.")
    }

    @Test
    fun `isValidDate should return false for month 0`() {
        assertFalse(Verdandi.isValidDate(2025, 0, 15), "Month 0 should be invalid.")
    }

    @Test
    fun `isValidDate should return false for month 13`() {
        assertFalse(Verdandi.isValidDate(2025, 13, 15), "Month 13 should be invalid.")
    }

    @Test
    fun `isValidDate should return false for day 0`() {
        assertFalse(Verdandi.isValidDate(2025, 6, 0), "Day 0 should be invalid.")
    }

    @Test
    fun `isValidDate should return false for day 32`() {
        assertFalse(Verdandi.isValidDate(2025, 1, 32), "Day 32 should be invalid for January.")
    }

    @Test
    fun `isValidDate should return false for day 31 in April`() {
        assertFalse(Verdandi.isValidDate(2025, 4, 31), "Day 31 should be invalid for April.")
    }

    @Test
    fun `isValidDate should return true for day 31 in March`() {
        assertTrue(Verdandi.isValidDate(2025, 3, 31), "Day 31 should be valid for March.")
    }

    @Test
    fun `isValidDate should return false for negative day`() {
        assertFalse(Verdandi.isValidDate(2025, 6, -1), "Negative day should be invalid.")
    }

    @Test
    fun `isValidTime should return true for valid time`() {
        assertTrue(Verdandi.isValidTime(14, 30, 45), "14:30:45 should be valid.")
    }

    @Test
    fun `isValidTime should return true for midnight`() {
        assertTrue(Verdandi.isValidTime(0, 0, 0), "00:00:00 should be valid.")
    }

    @Test
    fun `isValidTime should return true for end of day`() {
        assertTrue(Verdandi.isValidTime(23, 59, 59), "23:59:59 should be valid.")
    }

    @Test
    fun `isValidTime should return false for hour 24`() {
        assertFalse(Verdandi.isValidTime(24, 0, 0), "Hour 24 should be invalid.")
    }

    @Test
    fun `isValidTime should return false for hour negative`() {
        assertFalse(Verdandi.isValidTime(-1, 0, 0), "Negative hour should be invalid.")
    }

    @Test
    fun `isValidTime should return false for minute 60`() {
        assertFalse(Verdandi.isValidTime(12, 60, 0), "Minute 60 should be invalid.")
    }

    @Test
    fun `isValidTime should return false for second 60`() {
        assertFalse(Verdandi.isValidTime(12, 30, 60), "Second 60 should be invalid.")
    }

    @Test
    fun `isValidTime should return false for millisecond 1000`() {
        assertFalse(Verdandi.isValidTime(12, 30, 0, 1000), "Millisecond 1000 should be invalid.")
    }

    @Test
    fun `isValidTime should return true for millisecond 999`() {
        assertTrue(Verdandi.isValidTime(12, 30, 0, 999), "Millisecond 999 should be valid.")
    }
}
