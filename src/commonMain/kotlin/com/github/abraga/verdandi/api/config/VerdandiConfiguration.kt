package com.github.abraga.verdandi.api.config

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.adjust.WeekStart
import kotlinx.serialization.Serializable
import kotlin.concurrent.Volatile

/**
 * Global configuration for Verdandi library behavior.
 *
 * This configuration allows you to set default values that will be used
 * throughout the library when not explicitly specified.
 *
 * ### Example
 *
 * ```kotlin
 * Verdandi.configure {
 *     defaultTimeZone = VerdandiTimeZone.UTC
 *     defaultWeekStart = Monday
 * }
 * ```
 *
 * ### Thread Safety
 *
 * Configuration changes are thread-safe but should ideally be done once
 * at application startup before any Verdandi operations.
 *
 * @property defaultTimeZone The default timezone to use when not explicitly specified.
 *   If `null`, the system timezone will be used.
 * @property defaultWeekStart The default first day of the week for week-based operations.
 *   If `null`, the system locale's first day of week will be used.
 */
@ConsistentCopyVisibility
@Serializable
data class VerdandiConfiguration internal constructor(
    val defaultTimeZone: VerdandiTimeZone? = null,
    val defaultWeekStart: WeekStart? = null
) {

    companion object {

        /**
         * The current global configuration.
         *
         * This is thread-safe and can be read from any thread.
         */
        @Volatile
        private var current: VerdandiConfiguration = VerdandiConfiguration()

        /**
         * Gets the current global configuration.
         *
         * @return the current [VerdandiConfiguration].
         */
        fun get(): VerdandiConfiguration = current

        /**
         * Updates the global configuration.
         *
         * This operation is thread-safe.
         *
         * @param configuration the new configuration to apply.
         */
        internal fun set(configuration: VerdandiConfiguration) {
            current = configuration
        }

        /**
         * Resets the configuration to default values.
         *
         * This is primarily useful for testing.
         */
        fun reset() {
            current = VerdandiConfiguration()
        }
    }
}
