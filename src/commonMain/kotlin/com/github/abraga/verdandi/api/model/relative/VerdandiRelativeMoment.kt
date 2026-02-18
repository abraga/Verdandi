package com.github.abraga.verdandi.api.model.relative

import com.github.abraga.verdandi.api.scope.format.relative.VerdandiRelativeFormatScope
import kotlinx.serialization.Serializable

/**
 * Represents the result of comparing two [VerdandiMoment][com.github.abraga.verdandi.api.model.VerdandiMoment]
 * instances, expressing their temporal distance as a human-readable relative description.
 *
 * A [VerdandiRelativeMoment] is one of three cases:
 * - [Now] — the two moments are identical (zero distance).
 * - [Past] — the reference moment is **before** the target moment.
 * - [Future] — the reference moment is **after** the target moment.
 *
 * Use [format] to produce a localized string from the relative moment:
 *
 * ```kotlin
 * val relative: VerdandiRelativeMoment = momentA.relative(momentB)
 *
 * val text = relative format {
 *     onNow    { "just now" }
 *     onPast   { diff -> "$diff ago" }
 *     onFuture { diff -> "in $diff" }
 * }
 * ```
 *
 * @see VerdandiRelativeComponents
 * @see VerdandiRelativeFormatScope
 */
@Serializable
sealed interface VerdandiRelativeMoment {

    /**
     * Formats this relative moment into a human-readable string using the given DSL [block].
     *
     * The block must provide handlers for all three cases via
     * [onNow][VerdandiRelativeFormatScope.onNow],
     * [onPast][VerdandiRelativeFormatScope.onPast], and
     * [onFuture][VerdandiRelativeFormatScope.onFuture].
     *
     * @param block configuration DSL for resolving each temporal direction.
     * @return the formatted relative string.
     */
    infix fun format(block: VerdandiRelativeFormatScope.() -> Unit): String {
        val scope = VerdandiRelativeFormatScope { it.resolve() }

        scope.block()

        return scope.build(this)
    }
}

/**
 * The two compared moments represent the same instant — zero temporal distance.
 */
data object Now : VerdandiRelativeMoment

/**
 * The reference moment is **before** the target moment.
 *
 * @property components the decomposed temporal distance broken down into
 *   years, months, weeks, days, hours, minutes, and seconds.
 */
data class Past(val components: VerdandiRelativeComponents) : VerdandiRelativeMoment

/**
 * The reference moment is **after** the target moment.
 *
 * @property components the decomposed temporal distance broken down into
 *   years, months, weeks, days, hours, minutes, and seconds.
 */
data class Future(val components: VerdandiRelativeComponents) : VerdandiRelativeMoment
