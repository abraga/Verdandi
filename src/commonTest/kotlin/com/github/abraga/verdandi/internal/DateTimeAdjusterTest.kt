package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.adjust.Monday
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeAdjusterTest {

    @Test
    fun `adjust addYears should add years correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val yearsToAdd = 2
        val expectedYear = 2027

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust {
            atYear(moment.component.year.value + yearsToAdd)
        }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedYear, component.year.value, "Year should be $expectedYear.")
    }

    @Test
    fun `adjust addMonths should add months correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedMonth = 9

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add three months }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedMonth, component.month.value, "Month should be $expectedMonth.")
    }

    @Test
    fun `adjust addWeeks should add weeks correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 29

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add two weeks }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after adding 2 weeks.")
    }

    @Test
    fun `adjust addDays should add days correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 20

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add five days }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after adding 5 days.")
    }

    @Test
    fun `adjust subtractDays should subtract days correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val expectedDay = 10

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { subtract five days }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(expectedDay, component.day.value, "Day should be $expectedDay after subtracting 5 days.")
    }

    @Test
    fun `adjust setYear should set year correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newYear = 2030

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { atYear(newYear) }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(newYear, component.year.value, "Year should be $newYear.")
    }

    @Test
    fun `adjust setMonth should set month correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newMonth = 12

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { atMonth(newMonth) }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(newMonth, component.month.value, "Month should be $newMonth.")
    }

    @Test
    fun `adjust setDay should set day correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newDay = 1

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { atDay(newDay) }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(newDay, component.day.value, "Day should be $newDay.")
    }

    @Test
    fun `adjust setHour should set hour correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newHour = 8

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { atHour(newHour) }
        val component = adjusted.component

        assertEquals(newHour, component.hour.value, "Hour should be $newHour.")
    }

    @Test
    fun `adjust setMinute should set minute correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newMinute = 45

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { atMinute(newMinute) }
        val component = adjusted.component

        assertEquals(newMinute, component.minute.value, "Minute should be $newMinute.")
    }

    @Test
    fun `adjust setSecond should set second correctly`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"
        val newSecond = 30

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { atSecond(newSecond) }
        val component = adjusted.component

        assertEquals(newSecond, component.second.value, "Second should be $newSecond.")
    }

    @Test
    fun `adjust startOfDay should reset time to midnight`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at startOf day }
        val component = adjusted.component

        assertEquals(0, component.hour.value, "Hour should be 0.")
        assertEquals(0, component.minute.value, "Minute should be 0.")
        assertEquals(0, component.second.value, "Second should be 0.")
        assertEquals(0, component.millisecond.value, "Millisecond should be 0.")
    }

    @Test
    fun `adjust endOfDay should set time to end of day`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at endOf day }
        val component = adjusted.component

        assertEquals(23, component.hour.value, "Hour should be 23.")
        assertEquals(59, component.minute.value, "Minute should be 59.")
        assertEquals(59, component.second.value, "Second should be 59.")
        assertEquals(999, component.millisecond.value, "Millisecond should be 999.")
    }

    @Test
    fun `adjust startOfMonth should set to first day of month`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at startOf month }
        val component = adjusted.component

        assertEquals(1, component.day.value, "Day should be 1.")
        assertEquals(0, component.hour.value, "Hour should be 0.")
    }

    @Test
    fun `adjust endOfMonth should set to last day of month`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at endOf month }
        val component = adjusted.component

        assertEquals(30, component.day.value, "Day should be 30 for June.")
        assertEquals(23, component.hour.value, "Hour should be 23.")
    }

    @Test
    fun `adjust startOfYear should set to January 1st`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at startOf year }
        val component = adjusted.component

        assertEquals(1, component.month.value, "Month should be 1.")
        assertEquals(1, component.day.value, "Day should be 1.")
        assertEquals(0, component.hour.value, "Hour should be 0.")
    }

    @Test
    fun `adjust endOfYear should set to December 31st`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust { at endOf year }
        val component = adjusted.component

        assertEquals(12, component.month.value, "Month should be 12.")
        assertEquals(31, component.day.value, "Day should be 31.")
        assertEquals(23, component.hour.value, "Hour should be 23.")
    }

    @Test
    fun `adjust startOfWeek should set to Monday`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust {
            weekStartsOn = Monday
            at startOf week
        }
        val component = adjusted.component

        assertEquals(1, component.dayOfWeek.value, "Day of week should be 1 (Monday).")
        assertEquals(0, component.hour.value, "Hour should be 0.")
    }

    @Test
    fun `adjust endOfWeek should set to Sunday end of day`() {
        val inputTimestamp = "2025-06-15T14:30:45Z"

        val moment = Verdandi.from(inputTimestamp) inTimeZone VerdandiTimeZone.UTC
        val adjusted = moment adjust {
            weekStartsOn = Monday
            at endOf week
        }
        val component = adjusted.component

        assertEquals(7, component.dayOfWeek.value, "Day of week should be 7 (Sunday).")
        assertEquals(23, component.hour.value, "Hour should be 23.")
    }

    @Test
    fun `adjust should handle month boundary correctly`() {
        val inputTimestamp = "2025-06-30T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add one day }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(7, component.month.value, "Month should roll to July.")
        assertEquals(1, component.day.value, "Day should be 1.")
    }

    @Test
    fun `adjust should handle year boundary correctly`() {
        val inputTimestamp = "2025-12-31T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add one day }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(2026, component.year.value, "Year should roll to 2026.")
        assertEquals(1, component.month.value, "Month should be January.")
        assertEquals(1, component.day.value, "Day should be 1.")
    }

    @Test
    fun `adjust should clamp day when month has fewer days`() {
        val inputTimestamp = "2025-01-31T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust { add one month }
        val component = (adjusted inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(2, component.month.value, "Month should be February.")
        assertEquals(28, component.day.value, "Day should be clamped to 28 in February.")
    }

    @Test
    fun `adjust with timezone should switch timezone context`() {
        val inputTimestamp = "2025-06-15T12:00:00Z"

        val moment = Verdandi.from(inputTimestamp)
        val adjusted = moment adjust {
            timeZone = VerdandiTimeZone.of("Asia/Tokyo")
        }

        assertTrue(adjusted.component.offset.toString().contains("+09:00"), "Offset should be +09:00 for Tokyo.")
    }
}

