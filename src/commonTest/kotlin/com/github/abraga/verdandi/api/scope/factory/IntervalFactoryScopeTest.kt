@file:OptIn(ExperimentalTime::class)

package com.github.abraga.verdandi.api.scope.factory

import com.github.abraga.verdandi.Verdandi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class IntervalFactoryScopeTest {

    @Test
    fun `last one day should create interval ending at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { last one day }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.end.inMilliseconds >= beforeCreation, "End should not be before creation.")
        assertTrue(interval.end.inMilliseconds <= afterCreation, "End should not be after creation.")
    }

    @Test
    fun `last five days should create interval with correct duration`() {
        val interval = Verdandi.interval { last five days }
        val duration = interval.duration()

        assertEquals(5, duration.days, "Interval should span 5 days.")
        assertTrue(duration.years == 0 && duration.months == 0 && duration.weeks == 0, "No larger units expected.")
    }

    @Test
    fun `last three months should create interval ending at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { last three months }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.end.inMilliseconds >= beforeCreation, "End should not be before creation.")
        assertTrue(interval.end.inMilliseconds <= afterCreation, "End should not be after creation.")
    }

    @Test
    fun `next one week should create interval starting at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { next one week }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.start.inMilliseconds >= beforeCreation, "Start should not be before creation.")
        assertTrue(interval.start.inMilliseconds <= afterCreation, "Start should not be after creation.")
    }

    @Test
    fun `next seven days should create interval with correct duration`() {
        val interval = Verdandi.interval { next seven days }
        val duration = interval.duration()

        assertEquals(1, duration.weeks, "Interval should span 1 week.")
        assertEquals(0, duration.days, "No remaining days expected.")
    }

    @Test
    fun `next two weeks should create interval starting at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { next two weeks }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.start.inMilliseconds >= beforeCreation, "Start should not be before creation.")
        assertTrue(interval.start.inMilliseconds <= afterCreation, "Start should not be after creation.")
    }

    @Test
    fun `last one year should create interval ending at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { last one year }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.end.inMilliseconds >= beforeCreation, "End should not be before creation.")
        assertTrue(interval.end.inMilliseconds <= afterCreation, "End should not be after creation.")
    }

    @Test
    fun `next five years should create interval starting at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { next five years }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(interval.start.inMilliseconds >= beforeCreation, "Start should not be before creation.")
        assertTrue(interval.start.inMilliseconds <= afterCreation, "Start should not be after creation.")
    }

    @Test
    fun `last thirty minutes should create interval with correct duration`() {
        val interval = Verdandi.interval { last thirty minutes }
        val duration = interval.duration()

        assertEquals(30, duration.minutes, "Interval should span 30 minutes.")
    }

    @Test
    fun `next thirty seconds should create interval with correct duration`() {
        val interval = Verdandi.interval { next thirty seconds }
        val duration = interval.duration()

        assertEquals(30, duration.seconds, "Interval should span 30 seconds.")
    }

    @Test
    fun `last twelve hours should create interval with correct duration`() {
        val interval = Verdandi.interval { last twelve hours }
        val duration = interval.duration()

        assertEquals(12, duration.hours, "Interval should span 12 hours.")
    }

    @Test
    fun `last six months should create interval with start before end`() {
        val interval = Verdandi.interval { last six months }

        assertTrue(interval.start.inMilliseconds < interval.end.inMilliseconds, "Start should be before end.")
    }

    @Test
    fun `next six months should create interval with start before end`() {
        val interval = Verdandi.interval { next six months }

        assertTrue(interval.start.inMilliseconds < interval.end.inMilliseconds, "Start should be before end.")
    }
}
