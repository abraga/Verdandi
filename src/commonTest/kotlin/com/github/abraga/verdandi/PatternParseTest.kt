package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.exception.VerdandiParseException
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PatternParseTest {

    @Test
    fun `parse yyyy-MM-dd should extract date components`() {
        val moment = Verdandi.parse("2025-06-15", "yyyy-MM-dd") inTimeZone VerdandiTimeZone.UTC

        assertEquals(2025, moment.component.year.value, "Year should be 2025.")
        assertEquals(6, moment.component.month.value, "Month should be 6.")
        assertEquals(15, moment.component.day.value, "Day should be 15.")
    }

    @Test
    fun `parse dd-MM-yyyy should extract date with reversed order`() {
        val moment = Verdandi.parse("15/06/2025", "dd/MM/yyyy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(2025, moment.component.year.value, "Year should be 2025.")
        assertEquals(6, moment.component.month.value, "Month should be 6.")
        assertEquals(15, moment.component.day.value, "Day should be 15.")
    }

    @Test
    fun `parse HH mm ss should extract time components`() {
        val moment = Verdandi.parse("2025-06-15 14:30:45", "yyyy-MM-dd HH:mm:ss", VerdandiTimeZone.UTC)

        assertEquals(14, moment.component.hour.value, "Hour should be 14.")
        assertEquals(30, moment.component.minute.value, "Minute should be 30.")
        assertEquals(45, moment.component.second.value, "Second should be 45.")
    }

    @Test
    fun `parse SSS should extract milliseconds`() {
        val moment = Verdandi.parse("2025-06-15 12:00:00.456", "yyyy-MM-dd HH:mm:ss.SSS", VerdandiTimeZone.UTC)

        assertEquals(456, moment.component.millisecond.value, "Millisecond should be 456.")
    }

    @Test
    fun `parse Z with UTC offset should convert to UTC epoch`() {
        val moment = Verdandi.parse("2025-06-15 12:00:00 Z", "yyyy-MM-dd HH:mm:ss Z") inTimeZone VerdandiTimeZone.UTC

        assertEquals(12, moment.component.hour.value, "Hour in UTC should be 12.")
    }

    @Test
    fun `parse Z with positive offset should adjust to UTC`() {
        val moment = Verdandi.parse("2025-06-15 14:30 +09:00", "yyyy-MM-dd HH:mm Z") inTimeZone VerdandiTimeZone.UTC

        assertEquals(5, moment.component.hour.value, "Hour in UTC should be 5 (14:30 - 9h).")
        assertEquals(30, moment.component.minute.value, "Minute should be 30.")
    }

    @Test
    fun `parse Z with negative offset should adjust to UTC`() {
        val moment = Verdandi.parse("2025-06-15 10:00 -03:00", "yyyy-MM-dd HH:mm Z") inTimeZone VerdandiTimeZone.UTC

        assertEquals(13, moment.component.hour.value, "Hour in UTC should be 13 (10:00 + 3h).")
    }

    @Test
    fun `parse AM should keep morning hour`() {
        val moment = Verdandi.parse("2025-06-15 09:30 AM", "yyyy-MM-dd hh:mm a", VerdandiTimeZone.UTC)

        assertEquals(9, moment.component.hour.value, "Hour should be 9.")
    }

    @Test
    fun `parse PM should convert to 24-hour`() {
        val moment = Verdandi.parse("2025-06-15 03:30 PM", "yyyy-MM-dd hh:mm a", VerdandiTimeZone.UTC)

        assertEquals(15, moment.component.hour.value, "Hour should be 15 (3 PM).")
    }

    @Test
    fun `parse 12 AM should convert to hour 0`() {
        val moment = Verdandi.parse("2025-06-15 12:00 AM", "yyyy-MM-dd hh:mm a", VerdandiTimeZone.UTC)

        assertEquals(0, moment.component.hour.value, "12 AM should be hour 0.")
    }

    @Test
    fun `parse 12 PM should stay at hour 12`() {
        val moment = Verdandi.parse("2025-06-15 12:00 PM", "yyyy-MM-dd hh:mm a", VerdandiTimeZone.UTC)

        assertEquals(12, moment.component.hour.value, "12 PM should be hour 12.")
    }

    @Test
    fun `parse MMMM should extract full month name`() {
        val moment = Verdandi.parse("june 15, 2025", "MMMM dd, yyyy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(6, moment.component.month.value, "Month should be 6 (June).")
        assertEquals(15, moment.component.day.value, "Day should be 15.")
    }

    @Test
    fun `parse MMMM should be case insensitive`() {
        val moment = Verdandi.parse("JANUARY 01, 2025", "MMMM dd, yyyy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(1, moment.component.month.value, "Month should be 1 (January).")
    }

    @Test
    fun `parse MMM should extract short month name`() {
        val moment = Verdandi.parse("15-Jun-2025", "dd-MMM-yyyy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(6, moment.component.month.value, "Month should be 6 (Jun).")
    }

    @Test
    fun `parse MMM should handle all months`() {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        for ((index, name) in months.withIndex()) {
            val moment = Verdandi.parse("01-$name-2025", "dd-MMM-yyyy") inTimeZone VerdandiTimeZone.UTC

            assertEquals(index + 1, moment.component.month.value, "Month '$name' should map to ${index + 1}.")
        }
    }

    @Test
    fun `parse yy should interpret as 2000s`() {
        val moment = Verdandi.parse("15/06/99", "dd/MM/yy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(2099, moment.component.year.value, "Year '99' should map to 2099.")
    }

    @Test
    fun `parse yy with 00 should map to 2000`() {
        val moment = Verdandi.parse("15/06/00", "dd/MM/yy") inTimeZone VerdandiTimeZone.UTC

        assertEquals(2000, moment.component.year.value, "Year '00' should map to 2000.")
    }

    @Test
    fun `parse d should handle single digit day`() {
        val moment = Verdandi.parse("2025-06-5", "yyyy-MM-d") inTimeZone VerdandiTimeZone.UTC

        assertEquals(5, moment.component.day.value, "Day should be 5.")
    }

    @Test
    fun `parse d should handle double digit day`() {
        val moment = Verdandi.parse("2025-06-25", "yyyy-MM-d") inTimeZone VerdandiTimeZone.UTC

        assertEquals(25, moment.component.day.value, "Day should be 25.")
    }

    @Test
    fun `round trip parse then format should preserve value`() {
        val originalPattern = "yyyy-MM-dd"
        val originalInput = "2025-06-15"

        val moment = Verdandi.parse(originalInput, originalPattern) inTimeZone VerdandiTimeZone.UTC
        val roundTripped = moment format originalPattern

        assertEquals(originalInput, roundTripped, "Round-trip should preserve the original value.")
    }

    @Test
    fun `round trip with full datetime should preserve all components`() {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val input = "2025-06-15 14:30:45"

        val moment = Verdandi.parse(input, pattern, VerdandiTimeZone.UTC)
        val roundTripped = moment format pattern

        assertEquals(input, roundTripped, "Round-trip should preserve all components.")
    }

    @Test
    fun `parse with explicit timezone should interpret in that timezone`() {
        val tokyo = VerdandiTimeZone.of("Asia/Tokyo")
        val moment = Verdandi.parse("2025-06-15 14:30", "yyyy-MM-dd HH:mm", tokyo)
        val utcView = moment inTimeZone VerdandiTimeZone.UTC

        assertEquals(5, utcView.component.hour.value, "14:30 Tokyo should be 05:30 UTC.")
        assertEquals(30, utcView.component.minute.value, "Minutes should be preserved.")
    }

    @Test
    fun `parse with UTC timezone should behave like Z offset`() {
        val withOffset = Verdandi.parse("2025-06-15 12:00:00 Z", "yyyy-MM-dd HH:mm:ss Z")
        val withTimezone = Verdandi.parse("2025-06-15 12:00:00", "yyyy-MM-dd HH:mm:ss", VerdandiTimeZone.UTC)

        assertEquals(withOffset.inMilliseconds, withTimezone.inMilliseconds, "Both should produce the same epoch.")
    }

    @Test
    fun `parse with explicit timezone should set timeZoneId on result`() {
        val tokyo = VerdandiTimeZone.of("Asia/Tokyo")
        val moment = Verdandi.parse("2025-06-15 14:30", "yyyy-MM-dd HH:mm", tokyo)

        assertEquals(14, moment.component.hour.value, "Component should show Tokyo hour.")
    }

    @Test
    fun `parse should fail on empty pattern`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("2025", "")
        }
    }

    @Test
    fun `parse should fail on unrecognized directive`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("2025", "XXXX")
        }
    }

    @Test
    fun `parse should fail when input too short`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("20", "yyyy")
        }
    }

    @Test
    fun `parse should fail on invalid number in input`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("abcd-06-15", "yyyy-MM-dd")
        }
    }

    @Test
    fun `parse should fail on mismatched literal`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("2025/06/15", "yyyy-MM-dd")
        }
    }

    @Test
    fun `parse should fail on unknown month abbreviation`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("15-Xyz-2025", "dd-MMM-yyyy")
        }
    }

    @Test
    fun `parse should fail on unknown full month name`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("Notamonth 15, 2025", "MMMM dd, yyyy")
        }
    }

    @Test
    fun `parse should fail on invalid AM PM marker`() {
        assertFailsWith<VerdandiParseException> {
            Verdandi.parse("03:30 XM", "hh:mm a")
        }
    }
}
