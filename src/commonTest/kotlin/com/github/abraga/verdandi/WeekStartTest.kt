package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.adjust.Monday
import com.github.abraga.verdandi.api.scope.adjust.Saturday
import com.github.abraga.verdandi.api.scope.adjust.Sunday
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WeekStartTest {

    @Test
    fun `startOf week with Monday should go to Monday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Monday
            at startOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(9, utcResult.component.day.value, "Monday of that week is June 9.")
        assertEquals(0, utcResult.component.hour.value, "Should be start of day.")
    }

    @Test
    fun `startOf week with Sunday should go to Sunday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Sunday
            at startOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(8, utcResult.component.day.value, "Sunday of that week is June 8.")
        assertEquals(0, utcResult.component.hour.value, "Should be start of day.")
    }

    @Test
    fun `startOf week with Saturday should go to Saturday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Saturday
            at startOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(7, utcResult.component.day.value, "Saturday of that week is June 7.")
        assertEquals(0, utcResult.component.hour.value, "Should be start of day.")
    }

    @Test
    fun `endOf week with Monday should go to Sunday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Monday
            at endOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(15, utcResult.component.day.value, "Sunday of that week is June 15.")
        assertEquals(23, utcResult.component.hour.value, "Should be end of day.")
    }

    @Test
    fun `endOf week with Sunday should go to Saturday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Sunday
            at endOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(14, utcResult.component.day.value, "Saturday of that week is June 14.")
        assertEquals(23, utcResult.component.hour.value, "Should be end of day.")
    }

    @Test
    fun `endOf week with Saturday should go to Friday`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            weekStartsOn = Saturday
            at endOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(13, utcResult.component.day.value, "Friday of that week is June 13.")
        assertEquals(23, utcResult.component.hour.value, "Should be end of day.")
    }

    @Test
    fun `startOf week on week start day should stay on same day`() {
        val monday = Verdandi.from("2025-06-09T15:30:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = monday adjust {
            weekStartsOn = Monday
            at startOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(9, utcResult.component.day.value, "Should stay on June 9 (Monday).")
        assertEquals(0, utcResult.component.hour.value, "Should be start of day.")
    }

    @Test
    fun `endOf week on week end day should stay on same day`() {
        val sunday = Verdandi.from("2025-06-15T10:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = sunday adjust {
            weekStartsOn = Monday
            at endOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        assertEquals(15, utcResult.component.day.value, "Should stay on June 15 (Sunday).")
        assertEquals(23, utcResult.component.hour.value, "Should be end of day.")
    }

    @Test
    fun `default weekStartsOn should be based on locale`() {
        val wednesday = Verdandi.from("2025-06-11T12:00:00Z") inTimeZone VerdandiTimeZone.UTC
        val result = wednesday adjust {
            at startOf week
        }
        val utcResult = result inTimeZone VerdandiTimeZone.UTC

        val dayValue = utcResult.component.day.value
        val validDays = listOf(7, 8, 9)

        assertTrue(dayValue in validDays, "Default should use locale-based first day of week (June 7-9).")
    }
}
