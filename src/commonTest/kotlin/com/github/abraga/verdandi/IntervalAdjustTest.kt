package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

class IntervalAdjustTest {

    @Test
    fun `shiftBoth should move start and end by same duration`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shiftDuration = 5.days
        val expectedStartDay = 6
        val expectedEndMonth = 7

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { shiftBoth(shiftDuration) }
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shifted to $expectedStartDay.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End should be shifted into month $expectedEndMonth.")
    }

    @Test
    fun `shiftStart should only move start boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shiftDuration = 3.days
        val expectedStartDay = 4
        val expectedEndDay = 30

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { shiftStart(shiftDuration) }
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shifted to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should remain at $expectedEndDay.")
    }

    @Test
    fun `shiftEnd should only move end boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shiftDuration = 2.days
        val expectedStartDay = 1
        val expectedEndMonth = 7

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { shiftEnd(shiftDuration) }
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should remain at $expectedStartDay.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End should be shifted into month $expectedEndMonth.")
    }

    @Test
    fun `expandBoth should extend interval in both directions`() {
        val startTimestamp = "2025-06-10T00:00:00Z"
        val endTimestamp = "2025-06-20T23:59:59Z"
        val expandDuration = 5.days
        val expectedStartDay = 5
        val expectedEndDay = 25

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { expandBoth(expandDuration) }
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be expanded to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be expanded to $expectedEndDay.")
    }

    @Test
    fun `shrinkBoth should contract interval from both directions`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shrinkDuration = 5.days
        val expectedStartDay = 6
        val expectedEndDay = 25

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { shrinkBoth(shrinkDuration) }
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shrunk to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be shrunk to $expectedEndDay.")
    }

    @Test
    fun `alignToFullDays should set start to midnight and end to end of day`() {
        val startTimestamp = "2025-06-15T14:30:00Z"
        val endTimestamp = "2025-06-20T18:45:00Z"
        val expectedStartDay = 15
        val expectedStartHour = 0
        val expectedStartMinute = 0
        val expectedEndDay = 20
        val expectedEndHour = 23
        val expectedEndMinute = 59

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { alignToFullDays() }
        val startComponent = adjustedInterval.start.component
        val endComponent = adjustedInterval.end.component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be $expectedStartDay.")
        assertEquals(expectedStartHour, startComponent.hour.value, "Start hour should be $expectedStartHour.")
        assertEquals(expectedStartMinute, startComponent.minute.value, "Start minute should be $expectedStartMinute.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be $expectedEndDay.")
        assertEquals(expectedEndHour, endComponent.hour.value, "End hour should be $expectedEndHour.")
        assertEquals(expectedEndMinute, endComponent.minute.value, "End minute should be $expectedEndMinute.")
    }

    @Test
    fun `alignToFullMonths should expand to first and last day of months`() {
        val startTimestamp = "2025-06-15T14:30:00Z"
        val endTimestamp = "2025-08-20T18:45:00Z"
        val expectedStartDay = 1
        val expectedStartMonth = 6
        val expectedEndDay = 31
        val expectedEndMonth = 8

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { alignToFullMonths() }
        val startComponent = adjustedInterval.start.component
        val endComponent = adjustedInterval.end.component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be $expectedStartDay.")
        assertEquals(expectedStartMonth, startComponent.month.value, "Start month should be $expectedStartMonth.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be $expectedEndDay.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End month should be $expectedEndMonth.")
    }

    @Test
    fun `alignToFullYears should expand to January 1st and December 31st`() {
        val startTimestamp = "2025-06-15T14:30:00Z"
        val endTimestamp = "2026-08-20T18:45:00Z"
        val expectedStartDay = 1
        val expectedStartMonth = 1
        val expectedStartYear = 2025
        val expectedEndDay = 31
        val expectedEndMonth = 12
        val expectedEndYear = 2026

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval adjust { alignToFullYears() }
        val startComponent = adjustedInterval.start.component
        val endComponent = adjustedInterval.end.component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be $expectedStartDay.")
        assertEquals(expectedStartMonth, startComponent.month.value, "Start month should be $expectedStartMonth.")
        assertEquals(expectedStartYear, startComponent.year.value, "Start year should be $expectedStartYear.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be $expectedEndDay.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End month should be $expectedEndMonth.")
        assertEquals(expectedEndYear, endComponent.year.value, "End year should be $expectedEndYear.")
    }

    @Test
    fun `plus operator should shift interval forward`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shiftDuration = 10.days
        val expectedStartDay = 11
        val expectedEndMonth = 7

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval + shiftDuration
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shifted to $expectedStartDay.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End should be shifted into month $expectedEndMonth.")
    }

    @Test
    fun `minus operator should shift interval backward`() {
        val startTimestamp = "2025-06-15T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shiftDuration = 5.days
        val expectedStartDay = 10
        val expectedEndDay = 25

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val adjustedInterval = interval - shiftDuration
        val startComponent = (adjustedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (adjustedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shifted to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be shifted to $expectedEndDay.")
    }

    @Test
    fun `expand should extend interval in both directions`() {
        val startTimestamp = "2025-06-10T12:00:00Z"
        val endTimestamp = "2025-06-20T12:00:00Z"
        val expandDuration = 2.days
        val expectedStartDay = 8
        val expectedEndDay = 22

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val expandedInterval = interval.expand(expandDuration)
        val startComponent = (expandedInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (expandedInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be expanded to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be expanded to $expectedEndDay.")
    }

    @Test
    fun `shrink should contract interval from both directions`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val shrinkDuration = 5.days
        val expectedStartDay = 6
        val expectedEndDay = 25

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val shrunkInterval = interval.shrink(shrinkDuration)
        val startComponent = (shrunkInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (shrunkInterval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be shrunk to $expectedStartDay.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be shrunk to $expectedEndDay.")
    }

    @Test
    fun `contains should return true for moment inside interval`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val insideTimestamp = "2025-06-15T12:00:00Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val insideMoment = Verdandi.from(insideTimestamp)

        assertTrue(interval.contains(insideMoment), "Interval should contain moment inside.")
    }

    @Test
    fun `contains should return true for moment at start boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val startMoment = Verdandi.from(startTimestamp)

        assertTrue(interval.contains(startMoment), "Interval should contain moment at start boundary.")
    }

    @Test
    fun `contains should return false for moment at end boundary`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val endMoment = Verdandi.from(endTimestamp)

        assertFalse(interval.contains(endMoment), "Interval should not contain moment at end boundary (half-open).")
    }

    @Test
    fun `contains should return false for moment before interval`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val beforeTimestamp = "2025-05-31T23:59:59Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val beforeMoment = Verdandi.from(beforeTimestamp)

        assertFalse(interval.contains(beforeMoment), "Interval should not contain moment before start.")
    }

    @Test
    fun `contains should return false for moment after interval`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"
        val afterTimestamp = "2025-07-01T00:00:00Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val afterMoment = Verdandi.from(afterTimestamp)

        assertFalse(interval.contains(afterMoment), "Interval should not contain moment after end.")
    }

    @Test
    fun `overlaps should return true for partially overlapping intervals`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-15T23:59:59Z"
        val secondStart = "2025-06-10T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)

        assertTrue(firstInterval.overlaps(secondInterval), "Partially overlapping intervals should overlap.")
    }

    @Test
    fun `overlaps should return true when one interval contains another`() {
        val outerStart = "2025-06-01T00:00:00Z"
        val outerEnd = "2025-06-30T23:59:59Z"
        val innerStart = "2025-06-10T00:00:00Z"
        val innerEnd = "2025-06-20T23:59:59Z"

        val outerInterval = Verdandi.interval(outerStart, outerEnd)
        val innerInterval = Verdandi.interval(innerStart, innerEnd)

        assertTrue(outerInterval.overlaps(innerInterval), "Containing intervals should overlap.")
    }

    @Test
    fun `overlaps should return false for non-overlapping intervals`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-15T23:59:59Z"
        val secondStart = "2025-06-20T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)

        assertFalse(firstInterval.overlaps(secondInterval), "Non-overlapping intervals should not overlap.")
    }

    @Test
    fun `intersection should return overlapping portion`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-20T23:59:59Z"
        val secondStart = "2025-06-10T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"
        val expectedIntersectionStartDay = 10
        val expectedIntersectionEndDay = 20

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)
        val intersectionInterval = firstInterval.intersection(secondInterval)

        assertNotNull(intersectionInterval, "Intersection should not be null for overlapping intervals.")
        val startComponent = (intersectionInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (intersectionInterval.end inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(expectedIntersectionStartDay, startComponent.day.value, "Intersection start day should be $expectedIntersectionStartDay.")
        assertEquals(expectedIntersectionEndDay, endComponent.day.value, "Intersection end day should be $expectedIntersectionEndDay.")
    }

    @Test
    fun `intersection should return null for non-overlapping intervals`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-15T23:59:59Z"
        val secondStart = "2025-06-20T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)
        val intersectionInterval = firstInterval.intersection(secondInterval)

        assertNull(intersectionInterval, "Intersection should be null for non-overlapping intervals.")
    }

    @Test
    fun `intersection should return inner interval when one contains another`() {
        val outerStart = "2025-06-01T00:00:00Z"
        val outerEnd = "2025-06-30T23:59:59Z"
        val innerStart = "2025-06-10T00:00:00Z"
        val innerEnd = "2025-06-20T23:59:59Z"

        val outerInterval = Verdandi.interval(outerStart, outerEnd)
        val innerInterval = Verdandi.interval(innerStart, innerEnd)
        val intersectionInterval = outerInterval.intersection(innerInterval)

        assertNotNull(intersectionInterval, "Intersection should not be null for contained interval.")
        assertEquals(innerInterval.start.inMilliseconds, intersectionInterval.start.inMilliseconds, "Intersection start should match inner interval start.")
        assertEquals(innerInterval.end.inMilliseconds, intersectionInterval.end.inMilliseconds, "Intersection end should match inner interval end.")
    }

    @Test
    fun `union should return spanning interval for overlapping intervals`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-20T23:59:59Z"
        val secondStart = "2025-06-10T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"
        val expectedUnionStartDay = 1
        val expectedUnionEndDay = 30

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)
        val unionInterval = firstInterval.union(secondInterval)

        assertNotNull(unionInterval, "Union should not be null for overlapping intervals.")
        val startComponent = (unionInterval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (unionInterval.end inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(expectedUnionStartDay, startComponent.day.value, "Union start day should be $expectedUnionStartDay.")
        assertEquals(expectedUnionEndDay, endComponent.day.value, "Union end day should be $expectedUnionEndDay.")
    }

    @Test
    fun `union should return null for intervals with gap`() {
        val firstStart = "2025-06-01T00:00:00Z"
        val firstEnd = "2025-06-10T23:59:59Z"
        val secondStart = "2025-06-20T00:00:00Z"
        val secondEnd = "2025-06-30T23:59:59Z"

        val firstInterval = Verdandi.interval(firstStart, firstEnd)
        val secondInterval = Verdandi.interval(secondStart, secondEnd)
        val unionInterval = firstInterval.union(secondInterval)

        assertNull(unionInterval, "Union should be null for intervals with gap.")
    }

    @Test
    fun `union should return outer interval when one contains another`() {
        val outerStart = "2025-06-01T00:00:00Z"
        val outerEnd = "2025-06-30T23:59:59Z"
        val innerStart = "2025-06-10T00:00:00Z"
        val innerEnd = "2025-06-20T23:59:59Z"

        val outerInterval = Verdandi.interval(outerStart, outerEnd)
        val innerInterval = Verdandi.interval(innerStart, innerEnd)
        val unionInterval = outerInterval.union(innerInterval)

        assertNotNull(unionInterval, "Union should not be null for contained interval.")
        assertEquals(outerInterval.start.inMilliseconds, unionInterval.start.inMilliseconds, "Union start should match outer interval start.")
        assertEquals(outerInterval.end.inMilliseconds, unionInterval.end.inMilliseconds, "Union end should match outer interval end.")
    }

    @Test
    fun `duration should calculate interval length correctly`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-11T00:00:00Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val actualDuration = interval.duration()

        assertEquals(1, actualDuration.weeks, "Duration should have 1 week.")
        assertEquals(3, actualDuration.days, "Duration should have 3 remaining days.")
    }

    @Test
    fun `isEmpty should return true for zero-duration interval`() {
        val timestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(timestamp)
        val interval = moment..moment

        assertTrue(interval.isEmpty(), "Interval with same start and end should be empty.")
    }

    @Test
    fun `isEmpty should return false for interval with duration`() {
        val startTimestamp = "2025-06-01T00:00:00Z"
        val endTimestamp = "2025-06-30T23:59:59Z"

        val interval = Verdandi.interval(startTimestamp, endTimestamp)

        assertFalse(interval.isEmpty(), "Interval with duration should not be empty.")
    }
}
