package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.internal.region.locale.CurrentLocale
import com.github.abraga.verdandi.internal.region.zone.CurrentTimeZone
import com.github.abraga.verdandi.internal.timezone.PlatformTimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlatformVerificationTest {

    // region VerdandiClock

    @Test
    fun `now should return a valid moment`() {
        val moment = Verdandi.now()

        assertTrue(moment.inMilliseconds > 0, "Epoch millis should be positive.")
    }

    @Test
    fun `now should return increasing timestamps`() {
        val first = Verdandi.now().inMilliseconds
        val second = Verdandi.now().inMilliseconds

        assertTrue(second >= first, "Second call should not be earlier than first.")
    }

    // endregion

    // region PlatformTimeZone

    @Test
    fun `defaultId should return a non-empty timezone id`() {
        val id = PlatformTimeZone.defaultId()

        assertTrue(id.isNotEmpty(), "Default timezone ID should not be empty.")
    }

    @Test
    fun `UTC should be a valid timezone`() {
        assertTrue(PlatformTimeZone.isTimeZoneIdValid("UTC"), "UTC should be valid.")
    }

    @Test
    fun `invalid timezone should not be valid`() {
        assertFalse(
            PlatformTimeZone.isTimeZoneIdValid("Invalid/Timezone"),
            "Random string should not be valid timezone."
        )
    }

    @Test
    fun `offset at UTC should be zero`() {
        val offset = PlatformTimeZone.offsetMillisAt("UTC", 0L)

        assertEquals(0L, offset, "UTC offset should be 0.")
    }

    @Test
    fun `offset for known timezone should be correct in winter`() {
        val winterEpoch = 1705334400000L // 2024-01-15T12:00:00Z
        val offset = PlatformTimeZone.offsetMillisAt("America/New_York", winterEpoch)

        assertEquals(-5 * 3600 * 1000L, offset, "New York winter offset should be -5h.")
    }

    @Test
    fun `offset for known timezone should be correct in summer`() {
        val summerEpoch = 1721044800000L // 2024-07-15T12:00:00Z
        val offset = PlatformTimeZone.offsetMillisAt("America/New_York", summerEpoch)

        assertEquals(-4 * 3600 * 1000L, offset, "New York summer offset should be -4h (EDT).")
    }

    @Test
    fun `offset for positive timezone should be positive`() {
        val epoch = 1705334400000L // 2024-01-15T12:00:00Z
        val offset = PlatformTimeZone.offsetMillisAt("Asia/Tokyo", epoch)

        assertEquals(9 * 3600 * 1000L, offset, "Tokyo offset should be +9h.")
    }

    // endregion

    // region CurrentTimeZone

    @Test
    fun `current timezone id should not be empty`() {
        val tz = CurrentTimeZone()

        assertTrue(tz.id.isNotEmpty(), "Current timezone ID should not be empty.")
    }

    @Test
    fun `current timezone abbreviation should not be empty`() {
        val tz = CurrentTimeZone()

        assertTrue(tz.abbreviation.isNotEmpty(), "Abbreviation should not be empty.")
    }

    @Test
    fun `current timezone display name should not be empty`() {
        val tz = CurrentTimeZone()

        assertTrue(tz.displayName.isNotEmpty(), "Display name should not be empty.")
    }

    @Test
    fun `total offset should be consistent`() {
        val tz = CurrentTimeZone()
        val expected = tz.offsetMillis + tz.daylightSavingTimeOffsetMillis

        assertEquals(expected, tz.totalOffsetMillis, "Total offset should equal base + DST.")
    }

    @Test
    fun `offset should be within reasonable range`() {
        val tz = CurrentTimeZone()
        val fourteenHours = 14 * 3600 * 1000L

        assertTrue(
            tz.totalOffsetMillis in -fourteenHours..fourteenHours,
            "Offset ${tz.totalOffsetMillis}ms is outside +-14h range."
        )
    }

    // endregion

    // region CurrentLocale

    @Test
    fun `locale value should not be empty`() {
        val locale = CurrentLocale("")

        assertTrue(locale.value.isNotEmpty(), "Locale value should not be empty.")
    }

    @Test
    fun `decimal separator should not be empty`() {
        val locale = CurrentLocale("")

        assertTrue(locale.decimalSeparator.isNotEmpty(), "Decimal separator should not be empty.")
    }

    // endregion

    // region PlatformComponentName (via Verdandi API)

    @Test
    fun `month name should not be empty`() {
        val moment = Verdandi.at(year = 2024, month = 3, day = 15)

        assertTrue(
            moment.component.month.name.isNotEmpty(),
            "Month name should not be empty."
        )
    }

    @Test
    fun `day of week name should not be empty`() {
        val moment = Verdandi.at(year = 2024, month = 3, day = 15) // Friday

        assertTrue(
            moment.component.dayOfWeek.name.isNotEmpty(),
            "Day of week name should not be empty."
        )
    }

    // endregion

    // region VerdandiTimeZone integration

    @Test
    fun `moment in different timezone should adjust correctly`() {
        val utcMoment = Verdandi.from("2024-06-15T12:00:00Z")
        val tokyoMoment = utcMoment inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

        assertEquals(21, tokyoMoment.component.hour.value, "12:00 UTC should be 21:00 Tokyo.")
    }

    @Test
    fun `moment at known UTC offset should be deterministic`() {
        val moment = Verdandi.from("2024-01-15T12:00:00Z") inTimeZone VerdandiTimeZone.of("UTC")

        assertEquals(2024, moment.component.year.value)
        assertEquals(1, moment.component.month.value)
        assertEquals(15, moment.component.day.value)
        assertEquals(12, moment.component.hour.value)
    }

    // endregion
}
