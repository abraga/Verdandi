package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class RecurrenceTest {

    @Test
    fun `every day should generate daily occurrences`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val occurrences = Verdandi.recurrence(start, 5) { every day indefinitely }

        assertEquals(5, occurrences.size)

        val first = (occurrences[0] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(1, first.day.value)

        val second = (occurrences[1] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(2, second.day.value)

        val fifth = (occurrences[4] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(5, fifth.day.value)
    }

    @Test
    fun `every week should generate weekly occurrences`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val occurrences = Verdandi.recurrence(start, 4) { every week indefinitely }

        assertEquals(4, occurrences.size)

        val first = (occurrences[0] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(1, first.day.value)

        val second = (occurrences[1] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(8, second.day.value)
    }

    @Test
    fun `every week on monday should adjust to monday`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val occurrences = Verdandi.recurrence(start, 3) {
            every week on mondays indefinitely
        }

        occurrences.forEach { occurrence ->
            val component = (occurrence inTimeZone VerdandiTimeZone.UTC).component
            assertEquals(1, component.dayOfWeek.value)
        }
    }

    @Test
    fun `every day at 9 hours should set time to 9 AM`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val end = Verdandi.from("2025-01-10T00:00:00Z")

        val occurrences = Verdandi.recurrence(start, 3) {
            every day at { 9.hours } until end
        }

        occurrences.forEach { occurrence ->
            val component = (occurrence inTimeZone VerdandiTimeZone.UTC).component
            assertEquals(9, component.hour.value)
            assertEquals(0, component.minute.value)
        }
    }

    @Test
    fun `every week on monday at 14 hours should combine day and time`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val end = Verdandi.from("2025-03-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 3) {
            every week on mondays at { 14.hours } until end
        }

        recurrence.forEach { occurrence ->
            val component = (occurrence inTimeZone VerdandiTimeZone.UTC).component
            assertEquals(1, component.dayOfWeek.value)
            assertEquals(14, component.hour.value)
        }
    }

    @Test
    fun `every day until end moment should stop at end`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val end = Verdandi.from("2025-01-05T00:00:00Z")
        val occurrences = Verdandi.recurrence(start) { every day until(end) }

        assertTrue(occurrences.size <= 5)
        assertTrue(occurrences.all { it <= end })
    }

    @Test
    fun `every month should generate monthly occurrences`() {
        val start = Verdandi.from("2025-01-15T00:00:00Z")
        val occurrences = Verdandi.recurrence(start, 3) { every month indefinitely }

        val first = (occurrences[0] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(1, first.month.value)

        val second = (occurrences[1] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(2, second.month.value)

        val third = (occurrences[2] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(3, third.month.value)
    }

    @Test
    fun `every year should generate yearly occurrences`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val occurrences = Verdandi.recurrence(start, 3) { every year indefinitely }

        val first = (occurrences[0] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(2025, first.year.value)

        val second = (occurrences[1] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(2026, second.year.value)

        val third = (occurrences[2] inTimeZone VerdandiTimeZone.UTC).component
        assertEquals(2027, third.year.value)
    }

    @Test
    fun `recurrence without start moment should use now`() {
        val occurrences = Verdandi.recurrence(1) { every day indefinitely }

        assertEquals(1, occurrences.size)
    }

    @Test
    fun `matches should return true for moment on daily recurrence`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 10) { every day indefinitely }

        val validMoment = Verdandi.from("2025-01-05T00:00:00Z")

        assertTrue(recurrence.matches(validMoment))
    }

    @Test
    fun `matches should return false for moment with wrong time of day`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 10) { every day indefinitely }

        val wrongTime = Verdandi.from("2025-01-05T12:00:00Z")

        assertFalse(recurrence.matches(wrongTime))
    }

    @Test
    fun `matches should return false for moment before start`() {
        val start = Verdandi.from("2025-01-10T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 5) { every day indefinitely }

        val beforeStart = Verdandi.from("2025-01-05T00:00:00Z")

        assertFalse(recurrence.matches(beforeStart))
    }

    @Test
    fun `matches should return false for moment after end boundary`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val end = Verdandi.from("2025-01-10T00:00:00Z")
        val recurrence = Verdandi.recurrence(start) { every day until(end) }

        val afterEnd = Verdandi.from("2025-01-15T00:00:00Z")

        assertFalse(recurrence.matches(afterEnd))
    }

    @Test
    fun `matches should respect day of week constraint`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val recurrence = Verdandi.recurrence(start, 10) {
            every week on mondays indefinitely
        }

        val monday = Verdandi.from("2025-01-13T00:00:00Z")
        val tuesday = Verdandi.from("2025-01-14T00:00:00Z")

        assertTrue(recurrence.matches(monday))
        assertFalse(recurrence.matches(tuesday))
    }

    @Test
    fun `matches should respect time offset with at`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val end = Verdandi.from("2025-02-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 10) {
            every day at { 9.hours } until end
        }

        val at9am = Verdandi.from("2025-01-05T09:00:00Z")
        val atNoon = Verdandi.from("2025-01-05T12:00:00Z")

        assertTrue(recurrence.matches(at9am))
        assertFalse(recurrence.matches(atNoon))
    }

    @Test
    fun `matches should work for monthly recurrence`() {
        val start = Verdandi.from("2025-01-15T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 6) { every month indefinitely }

        val validMonth = Verdandi.from("2025-03-15T00:00:00Z")
        val wrongDay = Verdandi.from("2025-03-16T00:00:00Z")

        assertTrue(recurrence.matches(validMonth))
        assertFalse(recurrence.matches(wrongDay))
    }

    @Test
    fun `matches should work for yearly recurrence`() {
        val start = Verdandi.from("2025-06-15T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 5) { every year indefinitely }

        val validYear = Verdandi.from("2027-06-15T00:00:00Z")
        val wrongMonth = Verdandi.from("2027-07-15T00:00:00Z")

        assertTrue(recurrence.matches(validYear))
        assertFalse(recurrence.matches(wrongMonth))
    }

    @Test
    fun `matches should verify moment far beyond generated list`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 3) { every day indefinitely }

        val farFuture = Verdandi.from("2025-12-31T00:00:00Z")

        assertTrue(recurrence.matches(farFuture))
    }

    @Test
    fun `every week on weekdays should generate Mon through Fri occurrences`() {
        val start = Verdandi.from("2025-01-06T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val end = Verdandi.from("2025-01-12T23:59:59Z")
        val recurrence = Verdandi.recurrence(start) {
            every week on weekdays until(end)
        }

        assertEquals(5, recurrence.size)

        val days = recurrence.map { (it inTimeZone VerdandiTimeZone.UTC).component.dayOfWeek.value }

        assertEquals(listOf(1, 2, 3, 4, 5), days)
    }

    @Test
    fun `every week on weekends should generate Sat and Sun occurrences`() {
        val start = Verdandi.from("2025-01-06T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val end = Verdandi.from("2025-01-19T23:59:59Z")
        val recurrence = Verdandi.recurrence(start) {
            every week on weekends until(end)
        }

        recurrence.forEach { occurrence ->
            val dayOfWeek = (occurrence inTimeZone VerdandiTimeZone.UTC).component.dayOfWeek.value
            assertTrue(dayOfWeek == 6 || dayOfWeek == 7)
        }
    }

    @Test
    fun `weekdays with time marker should set time correctly`() {
        val start = Verdandi.from("2025-01-06T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val end = Verdandi.from("2025-01-12T23:59:59Z")
        val recurrence = Verdandi.recurrence(start) {
            every week on weekdays at { 9.hours } until end
        }

        recurrence.forEach { occurrence ->
            val component = (occurrence inTimeZone VerdandiTimeZone.UTC).component
            val dayOfWeek = component.dayOfWeek.value

            assertTrue(dayOfWeek in 1..5)
            assertEquals(9, component.hour.value)
        }
    }

    @Test
    fun `matches should work with weekdays`() {
        val start = Verdandi.from("2025-01-06T00:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val recurrence = Verdandi.recurrence(start, 10) {
            every week on weekdays indefinitely
        }

        val wednesday = Verdandi.from("2025-01-08T00:00:00Z")
        val saturday = Verdandi.from("2025-01-11T00:00:00Z")

        assertTrue(recurrence.matches(wednesday))
        assertFalse(recurrence.matches(saturday))
    }

    @Test
    fun `weekdays with limit should respect limit across all days`() {
        val start = Verdandi.from("2025-01-06T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 3) {
            every week on weekdays indefinitely
        }

        assertEquals(3, recurrence.size)
    }

    @Test
    fun `exclude should remove specific moments from list`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 5) { every day indefinitely }

        val excluded = Verdandi.from("2025-01-03T00:00:00Z")
        val filtered = recurrence.exclude(excluded)

        assertEquals(4, filtered.size)
        assertFalse(filtered.contains(excluded))
    }

    @Test
    fun `exclude should make matches return false for excluded moments`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 10) { every day indefinitely }

        val excluded = Verdandi.from("2025-01-05T00:00:00Z")
        val filtered = recurrence.exclude(excluded)

        assertFalse(filtered.matches(excluded))
        assertTrue(filtered.matches(Verdandi.from("2025-01-06T00:00:00Z")))
    }

    @Test
    fun `exclude should accumulate across multiple calls`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 5) { every day indefinitely }

        val first = Verdandi.from("2025-01-02T00:00:00Z")
        val second = Verdandi.from("2025-01-04T00:00:00Z")

        val filtered = recurrence.exclude(first).exclude(second)

        assertEquals(3, filtered.size)
        assertFalse(filtered.matches(first))
        assertFalse(filtered.matches(second))
    }

    @Test
    fun `exclude with collection should remove all specified moments`() {
        val start = Verdandi.from("2025-01-01T00:00:00Z")
        val recurrence = Verdandi.recurrence(start, 5) { every day indefinitely }

        val firstHoliday = Verdandi.from("2025-01-02T00:00:00Z")
        val secondHoliday = Verdandi.from("2025-01-04T00:00:00Z")

        val filtered = recurrence.exclude(firstHoliday, secondHoliday)

        assertEquals(3, filtered.size)
        assertFalse(filtered.matches(firstHoliday))
        assertFalse(filtered.matches(secondHoliday))
    }
}
