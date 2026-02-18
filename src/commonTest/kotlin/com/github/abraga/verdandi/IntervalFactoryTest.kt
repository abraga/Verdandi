@file:OptIn(ExperimentalTime::class)

package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class IntervalFactoryTest {

    @Test
    fun `interval from epoch milliseconds should preserve start and end values`() {
        val startMilliseconds = 1735689600000L
        val endMilliseconds = 1735776000000L

        val interval = Verdandi.interval(startMilliseconds, endMilliseconds)

        assertEquals(startMilliseconds, interval.start.inMilliseconds, "Start should be $startMilliseconds.")
        assertEquals(endMilliseconds, interval.end.inMilliseconds, "End should be $endMilliseconds.")
    }

    @Test
    fun `interval should normalize when start is after end`() {
        val laterMilliseconds = 1735776000000L
        val earlierMilliseconds = 1735689600000L

        val interval = Verdandi.interval(laterMilliseconds, earlierMilliseconds)

        assertEquals(earlierMilliseconds, interval.start.inMilliseconds, "Start should be normalized to earlier value.")
        assertEquals(laterMilliseconds, interval.end.inMilliseconds, "End should be normalized to later value.")
    }

    @Test
    fun `interval from ISO timestamps should parse correctly`() {
        val startTimestamp = "2025-01-01T00:00:00Z"
        val endTimestamp = "2025-12-31T23:59:59Z"
        val expectedStartYear = 2025
        val expectedStartMonth = 1
        val expectedStartDay = 1
        val expectedEndYear = 2025
        val expectedEndMonth = 12
        val expectedEndDay = 31

        val interval = Verdandi.interval(startTimestamp, endTimestamp)
        val startComponent = (interval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (interval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartYear, startComponent.year.value, "Start year should be $expectedStartYear.")
        assertEquals(expectedStartMonth, startComponent.month.value, "Start month should be $expectedStartMonth.")
        assertEquals(expectedStartDay, startComponent.day.value, "Start day should be $expectedStartDay.")
        assertEquals(expectedEndYear, endComponent.year.value, "End year should be $expectedEndYear.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End month should be $expectedEndMonth.")
        assertEquals(expectedEndDay, endComponent.day.value, "End day should be $expectedEndDay.")
    }

    @Test
    fun `interval from ISO timestamps should normalize inverted order`() {
        val laterTimestamp = "2025-12-31T23:59:59Z"
        val earlierTimestamp = "2025-01-01T00:00:00Z"
        val expectedStartMonth = 1
        val expectedEndMonth = 12

        val interval = Verdandi.interval(laterTimestamp, earlierTimestamp)
        val startComponent = (interval.start inTimeZone VerdandiTimeZone.UTC).component
        val endComponent = (interval.end inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedStartMonth, startComponent.month.value, "Start should be normalized to earlier month.")
        assertEquals(expectedEndMonth, endComponent.month.value, "End should be normalized to later month.")
    }

    @Test
    fun `interval from LongRange should include range boundaries correctly`() {
        val rangeStart = 1000000L
        val rangeEnd = 2000000L
        val expectedEndExclusive = rangeEnd + 1

        val range = rangeStart..rangeEnd
        val interval = Verdandi.interval(range)

        assertEquals(rangeStart, interval.start.inMilliseconds, "Start should be $rangeStart.")
        assertEquals(expectedEndExclusive, interval.end.inMilliseconds, "End should be exclusive at $expectedEndExclusive.")
    }

    @Test
    fun `interval from degenerate range should create minimal interval`() {
        val singleValue = 5000000L
        val expectedEndExclusive = singleValue + 1

        val degenerateRange = singleValue..singleValue
        val interval = Verdandi.interval(degenerateRange)

        assertEquals(singleValue, interval.start.inMilliseconds, "Start should be $singleValue.")
        assertEquals(expectedEndExclusive, interval.end.inMilliseconds, "End should be $expectedEndExclusive for degenerate range.")
    }

    @Test
    fun `interval DSL with last seven days should end at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { last seven days }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(
            interval.end.inMilliseconds >= beforeCreation,
            "Interval end should not be before creation started."
        )
        assertTrue(
            interval.end.inMilliseconds <= afterCreation,
            "Interval end should not be after creation finished."
        )
    }

    @Test
    fun `interval DSL with last three months should end at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { last three months }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(
            actual = interval.end.inMilliseconds >= beforeCreation,
            message = "Interval end should not be before creation started."
        )
        assertTrue(
            actual = interval.end.inMilliseconds <= afterCreation,
            message = "Interval end should not be after creation finished."
        )
    }

    @Test
    fun `interval DSL with next two weeks should start at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { next two weeks }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(
            interval.start.inMilliseconds >= beforeCreation,
            "Interval start should not be before creation started."
        )
        assertTrue(
            interval.start.inMilliseconds <= afterCreation,
            "Interval start should not be after creation finished."
        )
    }

    @Test
    fun `interval DSL with next five years should start at current time`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()
        val interval = Verdandi.interval { next five years }
        val afterCreation = Clock.System.now().toEpochMilliseconds()

        assertTrue(
            interval.start.inMilliseconds >= beforeCreation,
            "Interval start should not be before creation started."
        )
        assertTrue(
            interval.start.inMilliseconds <= afterCreation,
            "Interval start should not be after creation finished."
        )
    }

    @Test
    fun `rangeTo operator should create interval from two moments`() {
        val startMoment = Verdandi.at(year = 2025, month = 1, day = 1, hour = 0, minute = 0, second = 0)
        val endMoment = Verdandi.at(year = 2025, month = 12, day = 31, hour = 23, minute = 59, second = 59)

        val interval = startMoment..endMoment

        assertEquals(startMoment.inMilliseconds, interval.start.inMilliseconds, "Interval start should match start moment.")
        assertEquals(endMoment.inMilliseconds, interval.end.inMilliseconds, "Interval end should match end moment.")
    }

    @Test
    fun `rangeTo operator should normalize inverted moments`() {
        val laterMoment = Verdandi.at(year = 2025, month = 12, day = 31, hour = 23, minute = 59, second = 59)
        val earlierMoment = Verdandi.at(year = 2025, month = 1, day = 1, hour = 0, minute = 0, second = 0)

        val interval = laterMoment..earlierMoment

        assertEquals(earlierMoment.inMilliseconds, interval.start.inMilliseconds, "Interval start should be normalized to earlier moment.")
        assertEquals(laterMoment.inMilliseconds, interval.end.inMilliseconds, "Interval end should be normalized to later moment.")
    }

    @Test
    fun `interval DSL should work for all supported quantity selectors`() {
        val beforeCreation = Clock.System.now().toEpochMilliseconds()

        val intervals = listOf(
            Verdandi.interval { last zero minutes },
            Verdandi.interval { last one minute },
            Verdandi.interval { last two minutes },
            Verdandi.interval { last three minutes },
            Verdandi.interval { last four minutes },
            Verdandi.interval { last five minutes },
            Verdandi.interval { last six minutes },
            Verdandi.interval { last seven minutes },
            Verdandi.interval { last eight minutes },
            Verdandi.interval { last nine minutes },
            Verdandi.interval { last ten minutes },
            Verdandi.interval { last eleven minutes },
            Verdandi.interval { last twelve minutes },
            Verdandi.interval { last thirteen minutes },
            Verdandi.interval { last fourteen minutes },
            Verdandi.interval { last fifteen minutes },
            Verdandi.interval { last sixteen minutes },
            Verdandi.interval { last seventeen minutes },
            Verdandi.interval { last eighteen minutes },
            Verdandi.interval { last nineteen minutes },
            Verdandi.interval { last twenty minutes },
            Verdandi.interval { last twentyOne minutes },
            Verdandi.interval { last twentyTwo minutes },
            Verdandi.interval { last twentyThree minutes },
            Verdandi.interval { last twentyFour minutes },
            Verdandi.interval { last twentyFive minutes },
            Verdandi.interval { last twentySix minutes },
            Verdandi.interval { last twentySeven minutes },
            Verdandi.interval { last twentyEight minutes },
            Verdandi.interval { last twentyNine minutes },
            Verdandi.interval { last thirty minutes },
            Verdandi.interval { last thirtyOne minutes },
            Verdandi.interval { last thirtyTwo minutes },
            Verdandi.interval { last thirtyThree minutes },
            Verdandi.interval { last thirtyFour minutes },
            Verdandi.interval { last thirtyFive minutes },
            Verdandi.interval { last thirtySix minutes },
            Verdandi.interval { last thirtySeven minutes },
            Verdandi.interval { last thirtyEight minutes },
            Verdandi.interval { last thirtyNine minutes },
            Verdandi.interval { last forty minutes },
            Verdandi.interval { last fortyOne minutes },
            Verdandi.interval { last fortyTwo minutes },
            Verdandi.interval { last fortyThree minutes },
            Verdandi.interval { last fortyFour minutes },
            Verdandi.interval { last fortyFive minutes },
            Verdandi.interval { last fortySix minutes },
            Verdandi.interval { last fortySeven minutes },
            Verdandi.interval { last fortyEight minutes },
            Verdandi.interval { last fortyNine minutes },
            Verdandi.interval { last fifty minutes },
            Verdandi.interval { last fiftyOne minutes },
            Verdandi.interval { last fiftyTwo minutes },
            Verdandi.interval { last fiftyThree minutes },
            Verdandi.interval { last fiftyFour minutes },
            Verdandi.interval { last fiftyFive minutes },
            Verdandi.interval { last fiftySix minutes },
            Verdandi.interval { last fiftySeven minutes },
            Verdandi.interval { last fiftyEight minutes },
            Verdandi.interval { last fiftyNine minutes }
        )

        val afterCreation = Clock.System.now().toEpochMilliseconds()

        intervals.forEach { interval ->
            assertTrue(
                actual = interval.end.inMilliseconds >= beforeCreation,
                message = "Interval end should not be before creation started."
            )
            assertTrue(
                actual = interval.end.inMilliseconds <= afterCreation,
                message = "Interval end should not be after creation finished."
            )
        }
    }

}
