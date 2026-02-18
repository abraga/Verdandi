package com.github.abraga.verdandi.api.config

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.api.scope.adjust.WeekStart

/**
 * DSL scope for configuring Verdandi global settings.
 *
 * This scope provides a fluent API for setting global configuration values.
 *
 * ### Example
 *
 * ```kotlin
 * Verdandi.configure {
 *     defaultTimeZone = VerdandiTimeZone.UTC
 *     defaultWeekStart = WeekStart.Sunday
 * }
 * ```
 */
@VerdandiDslMarker
class VerdandiConfigurationScope internal constructor() {

    /**
     * The default timezone to use when not explicitly specified.
     *
     * If set to `null`, the system timezone will be used.
     *
     * ```kotlin
     * Verdandi.configure {
     *     defaultTimeZone = VerdandiTimeZone.UTC
     * }
     * ```
     */
    var defaultTimeZone: VerdandiTimeZone? = null

    /**
     * The default first day of the week for week-based operations.
     *
     * This affects operations like `at startOf week` and `at endOf week`.
     *
     * ```kotlin
     * Verdandi.configure {
     *     defaultWeekStart = WeekStart.Sunday  // US style
     * }
     * ```
     *
     * If `null`, the system locale's first day of week will be used.
     */
    var defaultWeekStart: WeekStart? = null

    internal fun build(): VerdandiConfiguration {
        return VerdandiConfiguration(
            defaultTimeZone = defaultTimeZone,
            defaultWeekStart = defaultWeekStart
        )
    }
}
