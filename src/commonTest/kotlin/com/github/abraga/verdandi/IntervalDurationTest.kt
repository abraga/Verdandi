package com.github.abraga.verdandi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntervalDurationTest {

    @Test
    fun `duration of 10 day interval should be 1 week 3 days`() {
        val interval = Verdandi.interval("2025-06-01T00:00:00Z", "2025-06-11T00:00:00Z")
        val duration = interval.duration()

        assertEquals(1, duration.weeks, "Should have 1 week.")
        assertEquals(3, duration.days, "Should have 3 remaining days.")
        assertEquals(0, duration.hours, "Should have 0 hours.")
    }

    @Test
    fun `duration of exact month should have zero time components`() {
        val interval = Verdandi.interval("2025-06-01T00:00:00Z", "2025-07-01T00:00:00Z")
        val duration = interval.duration()

        assertEquals(0, duration.hours, "Should have 0 hours.")
        assertEquals(0, duration.minutes, "Should have 0 minutes.")
        assertEquals(0, duration.seconds, "Should have 0 seconds.")
        assertFalse(duration.isZero, "Should not be zero.")
    }

    @Test
    fun `duration of exact year should be 1 year`() {
        val interval = Verdandi.interval("2025-01-01T00:00:00Z", "2026-01-01T00:00:00Z")
        val duration = interval.duration()

        assertEquals(1, duration.years, "Should be 1 year.")
        assertEquals(0, duration.months, "Should have 0 months.")
    }

    @Test
    fun `duration with time difference should include hours`() {
        val interval = Verdandi.interval("2025-06-15T10:00:00Z", "2025-06-15T14:30:00Z")
        val duration = interval.duration()

        assertEquals(0, duration.days, "Should have 0 days.")
        assertEquals(4, duration.hours, "Should have 4 hours.")
        assertEquals(30, duration.minutes, "Should have 30 minutes.")
    }

    @Test
    fun `duration of zero-length interval should be zero`() {
        val interval = Verdandi.interval("2025-06-15T12:00:00Z", "2025-06-15T12:00:00Z")
        val duration = interval.duration()

        assertTrue(duration.isZero, "Zero-length interval should have zero duration.")
    }

    @Test
    fun `duration should be symmetric regardless of argument order`() {
        val interval1 = Verdandi.interval("2025-06-01T00:00:00Z", "2025-06-11T00:00:00Z")
        val interval2 = Verdandi.interval("2025-06-11T00:00:00Z", "2025-06-01T00:00:00Z")

        assertEquals(
            interval1.duration().inWholeMilliseconds,
            interval2.duration().inWholeMilliseconds,
            "Duration should be the same regardless of order."
        )
    }

    @Test
    fun `duration of February in leap year should be 29 days`() {
        val interval = Verdandi.interval("2024-02-01T00:00:00Z", "2024-03-01T00:00:00Z")
        val duration = interval.duration()

        assertEquals(1, duration.months, "Should be exactly 1 month.")
    }

    @Test
    fun `duration of February in non-leap year should be 4 weeks`() {
        val interval = Verdandi.interval("2025-02-01T00:00:00Z", "2025-03-01T00:00:00Z")
        val duration = interval.duration()

        assertEquals(0, duration.hours, "Should have 0 hours.")
        assertEquals(0, duration.minutes, "Should have 0 minutes.")
        assertFalse(duration.isZero, "Should not be zero.")
    }

    @Test
    fun `duration with mixed components should decompose correctly`() {
        val interval = Verdandi.interval("2025-01-15T10:30:00Z", "2025-03-20T14:45:00Z")
        val duration = interval.duration()

        assertEquals(2, duration.months, "Should be 2 months.")
        assertEquals(5, duration.days, "Should have 5 days.")
        assertEquals(4, duration.hours, "Should have 4 hours.")
        assertEquals(15, duration.minutes, "Should have 15 minutes.")
    }
}
