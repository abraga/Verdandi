package com.github.abraga.verdandi.api.model

import com.github.abraga.verdandi.api.scope.adjust.VerdandiIntervalAdjustScope
import com.github.abraga.verdandi.api.scope.adjust.VerdandiIntervalOperationScope
import com.github.abraga.verdandi.api.scope.factory.VerdandiFromMomentFactoryScope
import com.github.abraga.verdandi.internal.adjust.IntervalAdjuster
import com.github.abraga.verdandi.internal.adjust.IntervalOperations
import com.github.abraga.verdandi.internal.factory.query.FromMomentFactory
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * A half-open time interval `[start, end)` between two [VerdandiMoment] instances.
 *
 * Intervals are always normalized so that [start] ≤ [end]. They can be created
 * through the [com.github.abraga.verdandi.Verdandi] entry point:
 *
 * ```kotlin
 * val recent = Verdandi.interval { last thirty days }
 * val custom = Verdandi.interval("2025-01-01T00:00:00Z", "2025-12-31T23:59:59Z")
 * val range = Verdandi.interval(startMs..endMs)
 * ```
 *
 * ### Operations
 *
 * Intervals expose set-like operations and duration computation through
 * [VerdandiIntervalOperationScope]:
 *
 * ```kotlin
 * interval.contains(moment)
 * interval.overlaps(other)
 * interval.intersection(other)
 * interval.union(other)
 * interval.duration()  // DateTimeDuration
 * ```
 *
 * ### Adjustments
 *
 * The [adjust] DSL allows shifting, expanding, shrinking, and aligning
 * interval bounds:
 *
 * ```kotlin
 * val adjusted = interval adjust {
 *     shiftBoth(2.hours)
 *     alignToFullDays()
 * }
 * ```
 *
 * ### Factory access
 *
 * An interval also implements [VerdandiFromMomentFactoryScope], allowing
 * creation of moments relative to it:
 *
 * ```kotlin
 * val midpoint = interval.at(
 *     year = 2025, month = 6, day = 15
 * )
 * ```
 *
 * @property start the inclusive lower bound of the interval.
 * @property end   the exclusive upper bound of the interval.
 */
@ConsistentCopyVisibility
@Serializable
data class VerdandiInterval private constructor(
    val start: VerdandiMoment,
    val end: VerdandiMoment,
    @Transient private val query: QueryData? = null
) : VerdandiFromMomentFactoryScope by FromMomentFactory(query),
    VerdandiIntervalOperationScope by IntervalOperations(start, end) {

    override fun toString(): String {
        return "[$start, $end)"
    }

    /**
     * Adjusts this interval using the interval-specific DSL.
     *
     * The current interval is passed as the lambda parameter for reference
     * (e.g. to read its current bounds).
     *
     * ```kotlin
     * val aligned = interval adjust {
     *     alignToFullDays()
     *     expandBoth(1.hours)
     * }
     * ```
     *
     * @param block configuration applied within a [VerdandiIntervalAdjustScope].
     * @return a new [VerdandiInterval] reflecting the adjustments.
     */
    infix fun adjust(block: VerdandiIntervalAdjustScope.(VerdandiInterval) -> Unit): VerdandiInterval {
        val scope = IntervalAdjuster(start.epoch, end.epoch, start.resolveTimeZoneContext())

        block(scope, this)

        return normalized(
            start = VerdandiMoment(scope.adjustedStartMillis, start.timeZoneId),
            end = VerdandiMoment(scope.adjustedEndMillis, start.timeZoneId)
        )
    }

    companion object {

        internal val Empty: VerdandiInterval = VerdandiInterval(VerdandiMoment(0), VerdandiMoment(0))

        internal fun normalized(
            start: VerdandiMoment,
            end: VerdandiMoment,
            query: QueryData? = null
        ): VerdandiInterval {
            return if (start.epoch <= end.epoch) {
                VerdandiInterval(start, end, query)
            } else {
                VerdandiInterval(end, start, query)
            }
        }
    }
}
