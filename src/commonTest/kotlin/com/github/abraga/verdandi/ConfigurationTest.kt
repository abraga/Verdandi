package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.config.VerdandiConfiguration
import com.github.abraga.verdandi.api.exception.VerdandiConfigurationException
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.api.scope.adjust.Monday
import com.github.abraga.verdandi.api.scope.adjust.Sunday
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ConfigurationTest {

    @AfterTest
    fun cleanup() {
        VerdandiConfiguration.reset()
    }

    @Test
    fun `default configuration should have null timezone and null week start`() {
        val config = Verdandi.config.current

        assertNull(config.defaultTimeZone, "Default timezone should be null")
        assertNull(config.defaultWeekStart, "Default week start should be null")
    }

    @Test
    fun `configure should set default timezone`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
        }

        val config = Verdandi.config.current

        assertNotNull(config.defaultTimeZone, "Default timezone should be set")
        assertEquals("UTC", config.defaultTimeZone?.id, "Default timezone should be UTC")
    }

    @Test
    fun `configure should set default week start`() {
        Verdandi.config.configure {
            defaultWeekStart = Sunday
        }

        val config = Verdandi.config.current

        assertEquals(Sunday, config.defaultWeekStart, "Default week start should be Sunday")
    }

    @Test
    fun `configure should set both timezone and week start`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
            defaultWeekStart = Sunday
        }

        val config = Verdandi.config.current

        assertNotNull(config.defaultTimeZone, "Default timezone should be set")
        assertEquals("UTC", config.defaultTimeZone?.id, "Default timezone should be UTC")
        assertEquals(Sunday, config.defaultWeekStart, "Default week start should be Sunday")
    }

    @Test
    fun `reset should restore default configuration`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
            defaultWeekStart = Sunday
        }

        VerdandiConfiguration.reset()

        val config = Verdandi.config.current

        assertNull(config.defaultTimeZone, "Default timezone should be null after reset")
        assertNull(config.defaultWeekStart, "Default week start should be null after reset")
    }

    @Test
    fun `invalid temporal unit should throw exception`() {
        val moment = Verdandi.from("2025-01-15T12:00:00Z")

        assertFailsWith<VerdandiConfigurationException> {
            moment adjust { at startOf TemporalUnit.Hour }
        }
    }

    @Test
    fun `default week start should be used in adjust operations`() {
        Verdandi.config.configure {
            defaultWeekStart = Sunday
        }

        val moment = Verdandi.from("2025-01-15T12:00:00Z")
        val startOfWeek = moment adjust { at startOf week }

        val component = (startOfWeek inTimeZone VerdandiTimeZone.UTC).component

        assertEquals(12, component.day.value, "Day should be 12 (Sunday)")
        assertEquals(1, component.month.value, "Month should be January")
        assertEquals(2025, component.year.value, "Year should be 2025")
    }

    @Test
    fun `default timezone should be used when not explicitly set`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
        }

        val moment = Verdandi.at(2025, 6, 15, 12, 0, 0)
        val component = moment.component

        assertEquals("+00:00", component.offset.toString(), "Offset should be UTC")
    }

    @Test
    fun `explicit timezone should override default timezone`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
        }

        val moment = Verdandi.at(2025, 6, 15, 12, 0, 0)
        val tokyoMoment = moment inTimeZone VerdandiTimeZone.of("Asia/Tokyo")
        val component = tokyoMoment.component

        assertEquals("+09:00", component.offset.toString(), "Offset should be Tokyo (+09:00)")
    }

    @Test
    fun `configuration changes should be thread-safe`() {
        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.UTC
        }

        val config1 = Verdandi.config.current

        Verdandi.config.configure {
            defaultTimeZone = VerdandiTimeZone.of("America/New_York")
        }

        val config2 = Verdandi.config.current

        assertEquals("UTC", config1.defaultTimeZone?.id)
        assertEquals("America/New_York", config2.defaultTimeZone?.id)
    }
}
