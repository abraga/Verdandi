package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class DstTest {

    @Test
    fun `component should reflect DST offset in summer for New York`() {
        val summerUtc = Verdandi.from("2025-07-15T12:00:00Z")
        val nyView = summerUtc inTimeZone VerdandiTimeZone.of("America/New_York")

        assertEquals("-04:00", nyView.component.offset.toString(), "New York summer offset should be -04:00 (EDT).")
        assertEquals(8, nyView.component.hour.value, "12:00 UTC should be 08:00 EDT.")
    }

    @Test
    fun `component should reflect standard offset in winter for New York`() {
        val winterUtc = Verdandi.from("2025-01-15T12:00:00Z")
        val nyView = winterUtc inTimeZone VerdandiTimeZone.of("America/New_York")

        assertEquals("-05:00", nyView.component.offset.toString(), "New York winter offset should be -05:00 (EST).")
        assertEquals(7, nyView.component.hour.value, "12:00 UTC should be 07:00 EST.")
    }

    @Test
    fun `format should use DST offset in summer`() {
        val summerUtc = Verdandi.from("2025-07-15T12:00:00Z")
        val nyView = summerUtc inTimeZone VerdandiTimeZone.of("America/New_York")
        val formatted = nyView format "HH:mm Z"

        assertEquals("08:00 -04:00", formatted, "Should format with EDT offset.")
    }

    @Test
    fun `format should use standard offset in winter`() {
        val winterUtc = Verdandi.from("2025-01-15T12:00:00Z")
        val nyView = winterUtc inTimeZone VerdandiTimeZone.of("America/New_York")
        val formatted = nyView format "HH:mm Z"

        assertEquals("07:00 -05:00", formatted, "Should format with EST offset.")
    }

    @Test
    fun `adjust one more day across spring forward should preserve wall clock hour`() {
        val beforeSpringForward = Verdandi.from("2025-03-08T12:00:00Z")
        val nyView = beforeSpringForward inTimeZone VerdandiTimeZone.of("America/New_York")
        val nextDay = nyView adjust { add one day }

        assertEquals(7, nyView.component.hour.value, "Before: 12:00 UTC = 07:00 EST.")
        assertEquals(7, nextDay.component.hour.value, "After: wall clock hour should still be 7.")
    }

    @Test
    fun `adjust one more day across fall back should preserve wall clock hour`() {
        val beforeFallBack = Verdandi.from("2025-11-01T12:00:00Z")
        val nyView = beforeFallBack inTimeZone VerdandiTimeZone.of("America/New_York")
        val nextDay = nyView adjust { add one day }

        assertEquals(8, nyView.component.hour.value, "Before: 12:00 UTC = 08:00 EDT.")
        assertEquals(8, nextDay.component.hour.value, "After: wall clock hour should still be 8.")
    }

    @Test
    fun `epoch difference across spring forward should be 23 hours for one day adjust`() {
        val ny = VerdandiTimeZone.of("America/New_York")
        val beforeDst = Verdandi.parse("2025-03-09 01:00", "yyyy-MM-dd HH:mm", ny)
        val afterDst = beforeDst adjust { add one day }

        val diffMs = afterDst.inMilliseconds - beforeDst.inMilliseconds
        val diffHours = diffMs / (1000 * 60 * 60)

        assertEquals(23, diffHours, "One day across spring forward should be 23 real hours.")
    }

    @Test
    fun `epoch difference across fall back should be 25 hours for one day adjust`() {
        val ny = VerdandiTimeZone.of("America/New_York")
        val beforeDst = Verdandi.parse("2025-11-02 01:00", "yyyy-MM-dd HH:mm", ny)
        val afterDst = beforeDst adjust { add one day }

        val diffMs = afterDst.inMilliseconds - beforeDst.inMilliseconds
        val diffHours = diffMs / (1000 * 60 * 60)

        assertEquals(25, diffHours, "One day across fall back should be 25 real hours.")
    }

    @Test
    fun `startOf day across DST should still produce midnight`() {
        val ny = VerdandiTimeZone.of("America/New_York")
        val duringDst = Verdandi.parse("2025-03-09 15:00", "yyyy-MM-dd HH:mm", ny)
        val startOfDay = duringDst adjust { at startOf day }

        assertEquals(0, startOfDay.component.hour.value, "Start of day should be midnight.")
        assertEquals(0, startOfDay.component.minute.value, "Start of day should have zero minutes.")
        assertEquals(9, startOfDay.component.day.value, "Should still be March 9.")
    }

    @Test
    fun `Tokyo should not have DST`() {
        val summerUtc = Verdandi.from("2025-07-15T12:00:00Z")
        val winterUtc = Verdandi.from("2025-01-15T12:00:00Z")

        val summerTokyo = summerUtc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")
        val winterTokyo = winterUtc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

        assertEquals("+09:00", summerTokyo.component.offset.toString(), "Tokyo summer offset should be +09:00.")
        assertEquals("+09:00", winterTokyo.component.offset.toString(), "Tokyo winter offset should be +09:00.")
    }

    @Test
    fun `London should switch between GMT and BST`() {
        val winterUtc = Verdandi.from("2025-01-15T12:00:00Z")
        val summerUtc = Verdandi.from("2025-07-15T12:00:00Z")

        val winterLondon = winterUtc inTimeZone VerdandiTimeZone.of("Europe/London")
        val summerLondon = summerUtc inTimeZone VerdandiTimeZone.of("Europe/London")

        assertEquals("+00:00", winterLondon.component.offset.toString(), "London winter should be GMT (+00:00).")
        assertEquals("+01:00", summerLondon.component.offset.toString(), "London summer should be BST (+01:00).")
    }

    @Test
    fun `same UTC instant should show different hours in different DST states`() {
        val utcMoment = Verdandi.from("2025-07-15T12:00:00Z")
        val ny = VerdandiTimeZone.of("America/New_York")

        val nyView = utcMoment inTimeZone ny
        val utcView = utcMoment inTimeZone VerdandiTimeZone.UTC

        assertNotEquals(
            utcView.component.hour.value,
            nyView.component.hour.value,
            "UTC and NY should show different hours."
        )
        assertEquals(utcMoment.inMilliseconds, nyView.inMilliseconds, "Epoch should be the same.")
    }
}
