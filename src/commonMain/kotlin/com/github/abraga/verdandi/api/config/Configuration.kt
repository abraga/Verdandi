package com.github.abraga.verdandi.api.config

import com.github.abraga.verdandi.api.scope.adjust.Monday

/**
 * Provides access to global Verdandi configuration settings.
 *
 * This object allows you to read the current configuration and apply new
 * settings that will affect the behavior of Verdandi functions when not
 * overridden by explicit parameters.
 */
object Configuration {

    /**
     * Access the current global Verdandi configuration.
     *
     * This provides read-only access to settings that affect the behavior of
     * Verdandi functions when not overridden by explicit parameters.
     *
     * @return the current [VerdandiConfiguration] instance.
     */
    val current: VerdandiConfiguration
        get() = VerdandiConfiguration.get()

    /**
     * Access the current month names.
     *
     * @return a list of month names.
     */
    val monthNames: List<String>
        get() = VerdandiNames().monthNames

    /**
     * Access the current weekday names.
     *
     * @return a list of weekday names.
     */
    val weekdayNames: List<String>
        get() = VerdandiNames().weekdayNames

    /**
     * Access the current AM/PM names.
     *
     * @return a pair containing the AM and PM names.
     */
    val amPmNames: Pair<String, String>
        get() = VerdandiNames().amPmNames

    /**
     * Configures global Verdandi settings.
     *
     * This allows you to set default values that will be used throughout
     * the library when not explicitly specified. Configuration changes are
     * thread-safe but should ideally be done once at application startup.
     *
     * ```kotlin
     * Verdandi.configure {
     *     defaultTimeZone = VerdandiTimeZone.UTC
     *     defaultWeekStart = WeekStart.Sunday
     * }
     * ```
     *
     * ### Available Settings
     *
     * - **defaultTimeZone**: The default timezone to use when not explicitly specified.
     *   If `null`, the system timezone will be used.
     * - **defaultWeekStart**: The default first day of the week for week-based operations.
     *   Defaults to [Monday][Monday].
     *
     * @param block configuration block applied within a [VerdandiConfigurationScope].
     */
    fun configure(block: VerdandiConfigurationScope.() -> Unit) {
        val scope = VerdandiConfigurationScope()

        scope.block()

        VerdandiConfiguration.set(scope.build())
    }
}
