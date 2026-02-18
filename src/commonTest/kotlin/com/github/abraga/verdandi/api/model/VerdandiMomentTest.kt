package com.github.abraga.verdandi.api.model

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class VerdandiMomentTest {

    @Test
    fun `toString should return ISO-like representation`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val result = moment.toString()

        assertTrue(result.contains("2025"), "toString should contain year.")
        assertTrue(result.contains("06"), "toString should contain month.")
        assertTrue(result.contains("15"), "toString should contain day.")
    }

    @Test
    fun `compareTo should return negative when moment is before other`() {
        val earlierTimestamp = "2025-06-15T10:00:00Z"
        val laterTimestamp = "2025-06-15T14:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertTrue(earlierMoment < laterMoment, "Earlier moment should be less than later moment.")
    }

    @Test
    fun `compareTo should return positive when moment is after other`() {
        val earlierTimestamp = "2025-06-15T10:00:00Z"
        val laterTimestamp = "2025-06-15T14:00:00Z"

        val earlierMoment = Verdandi.from(earlierTimestamp)
        val laterMoment = Verdandi.from(laterTimestamp)

        assertTrue(laterMoment > earlierMoment, "Later moment should be greater than earlier moment.")
    }

    @Test
    fun `compareTo should return zero for equal epochs`() {
        val timestamp = "2025-06-15T14:00:00Z"

        val moment1 = Verdandi.from(timestamp)
        val moment2 = Verdandi.from(timestamp)

        assertEquals(0, moment1.compareTo(moment2), "Same epochs should compare as equal.")
    }

    @Test
    fun `compareTo with milliseconds should work correctly`() {
        val timestamp = "2025-06-15T14:00:00Z"

        val moment = Verdandi.from(timestamp)
        val epochValue = moment.inMilliseconds

        assertEquals(0, moment.compareTo(epochValue), "Moment should compare equal to its own epoch.")
        assertTrue(moment.compareTo(epochValue - 1000) > 0, "Moment should be greater than earlier epoch.")
        assertTrue(moment.compareTo(epochValue + 1000) < 0, "Moment should be less than later epoch.")
    }

    @Test
    fun `inTimeZone should preserve epoch when changing timezone`() {
        val timestamp = "2025-06-15T12:00:00Z"

        val utcMoment = Verdandi.from(timestamp) inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = utcMoment inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

        assertEquals(utcMoment.inMilliseconds, tokyoMoment.inMilliseconds, "Epoch should remain unchanged.")
    }

    @Test
    fun `inTimeZone should change component values`() {
        val timestamp = "2025-06-15T12:00:00Z"
        val expectedUtcHour = 12
        val expectedTokyoHour = 21

        val utcMoment = Verdandi.from(timestamp) inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = utcMoment inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

        assertEquals(expectedUtcHour, utcMoment.component.hour.value, "UTC hour should be $expectedUtcHour.")
        assertEquals(expectedTokyoHour, tokyoMoment.component.hour.value, "Tokyo hour should be $expectedTokyoHour.")
    }

    @Test
    fun `equals should return true for same epoch regardless of timezone`() {
        val timestamp = "2025-06-15T12:00:00Z"

        val utcMoment = Verdandi.from(timestamp) inTimeZone VerdandiTimeZone.UTC
        val tokyoMoment = utcMoment inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

        assertEquals(utcMoment, tokyoMoment, "Moments with same epoch should be equal regardless of timezone.")
    }

    @Test
    fun `rangeTo operator should create normalized interval`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val interval = startMoment..endMoment

        assertEquals(startMoment.inMilliseconds, interval.start.inMilliseconds, "Interval start should match start moment.")
        assertEquals(endMoment.inMilliseconds, interval.end.inMilliseconds, "Interval end should match end moment.")
    }

    @Test
    fun `plus operator with duration should add time correctly`() {
        val timestamp = "2025-06-15T12:00:00Z"
        val hoursToAdd = 5.hours
        val expectedHour = 17

        val moment = Verdandi.from(timestamp)
        val result = moment + hoursToAdd
        val component = (result inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour after adding 5 hours.")
    }

    @Test
    fun `minus operator with duration should subtract time correctly`() {
        val timestamp = "2025-06-15T12:00:00Z"
        val hoursToSubtract = 3.hours
        val expectedHour = 9

        val moment = Verdandi.from(timestamp)
        val result = moment - hoursToSubtract
        val component = (result inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedHour, component.hour.value, "Hour should be $expectedHour after subtracting 3 hours.")
    }

    @Test
    fun `isToday should return true for current moment`() {
        val moment = Verdandi.now()

        assertTrue(moment.isToday(), "Current moment should be today.")
    }

    @Test
    fun `wasYesterday should return true for moment one day ago`() {
        val yesterday = Verdandi.now() adjust { subtract one day }

        assertTrue(yesterday.wasYesterday(), "Moment one day ago should be yesterday.")
    }

    @Test
    fun `isTomorrow should return true for moment one day ahead`() {
        val tomorrow = Verdandi.now() adjust { add one day }

        assertTrue(tomorrow.isTomorrow(), "Moment one day ahead should be tomorrow.")
    }

    @Test
    fun `isWeekend should return true for Saturday`() {
        val saturdayTimestamp = "2025-06-14T12:00:00Z"

        val saturday = Verdandi.from(saturdayTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(saturday.isWeekend(), "Saturday should be weekend.")
    }

    @Test
    fun `isWeekend should return true for Sunday`() {
        val sundayTimestamp = "2025-06-15T12:00:00Z"

        val sunday = Verdandi.from(sundayTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(sunday.isWeekend(), "Sunday should be weekend.")
    }

    @Test
    fun `isWeekday should return true for Monday`() {
        val mondayTimestamp = "2025-06-16T12:00:00Z"

        val monday = Verdandi.from(mondayTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertTrue(monday.isWeekday(), "Monday should be weekday.")
    }

    @Test
    fun `isWeekday should return false for Saturday`() {
        val saturdayTimestamp = "2025-06-14T12:00:00Z"

        val saturday = Verdandi.from(saturdayTimestamp) inTimeZone VerdandiTimeZone.UTC

        assertFalse(saturday.isWeekday(), "Saturday should not be weekday.")
    }

    @Test
    fun `isBetween with interval should work correctly`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val middleTimestamp = "2025-06-15T12:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val startMoment = Verdandi.from(startTimestamp)
        val middleMoment = Verdandi.from(middleTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val interval = startMoment..endMoment

        assertTrue(middleMoment.isBetween(interval), "Middle moment should be in interval.")
    }

    @Test
    fun `durationUntil should calculate difference correctly`() {
        val startTimestamp = "2025-01-01T12:00:00Z"
        val endTimestamp = "2025-07-01T12:00:00Z"
        val expectedMonths = 6

        val startMoment = Verdandi.from(startTimestamp)
        val endMoment = Verdandi.from(endTimestamp)
        val duration = startMoment.durationUntil(endMoment)

        assertEquals(expectedMonths, duration.months, "Months difference should be $expectedMonths.")
    }
}

