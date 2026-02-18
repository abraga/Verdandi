package com.github.abraga.verdandi.api.model.recurrence

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.format.VerdandiFormatScope
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiFormatPattern
import com.github.abraga.verdandi.internal.format.VerdandiFormatter
import com.github.abraga.verdandi.internal.format.VerdandiFormatter.Companion.invoke
import com.github.abraga.verdandi.internal.recurrence.RecurrenceMatcher
import com.github.abraga.verdandi.internal.recurrence.RecurrenceRule
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents the result of a recurrence rule evaluation: a finite, ordered collection
 * of [VerdandiMoment] instances generated according to a frequency, interval, and optional
 * day-of-week or time-of-day constraints.
 *
 * This class implements [List]<[VerdandiMoment]> via delegation, so it can be used
 * directly in any context that expects a list of moments (iteration, indexing, etc.).
 *
 * Instances are created exclusively through [Verdandi.recurrence][com.github.abraga.verdandi.Verdandi.recurrence]:
 *
 * ```kotlin
 * val fridays: VerdandiRecurrenceMoments = Verdandi.recurrence {
 *     every week on fridays indefinitely
 * }
 *
 * val dailyAt9: VerdandiRecurrenceMoments = Verdandi.recurrence(start) {
 *     every day at { 9.hours } until end
 * }
 * ```
 *
 * @property startMoment The anchor moment from which the recurrence begins.
 * @property endMoment The moment at or before which the last occurrence falls.
 *   For indefinite recurrences this equals the last generated occurrence.
 */
@ConsistentCopyVisibility
@Serializable
data class VerdandiRecurrenceMoments internal constructor(
    private val moments: List<VerdandiMoment>,
    val startMoment: VerdandiMoment,
    val endMoment: VerdandiMoment,
    @Transient
    internal val rule: RecurrenceRule? = null,
    @Transient
    internal val exclusions: Set<VerdandiMoment> = emptySet()
) : List<VerdandiMoment> by moments {

    override fun toString(): String {
        return "VerdandiRecurrenceMoments(size=${moments.size}, start..end=${toInterval()})"
    }

    /**
     * Converts the first and last moments of the recurrence to a [VerdandiInterval].
     *
     * @return A [VerdandiInterval] spanning from the first to the last moment, or `null` if the recurrence is empty.
     */
    fun toInterval(): VerdandiInterval? {
        return (firstOrNull() ?: return null)..(lastOrNull() ?: return null)
    }

    /**
     * Formats each moment in the recurrence according to the provided [block] pattern.
     *
     * @param block A lambda with receiver that defines the formatting pattern using [VerdandiFormatScope].
     * @return A list of formatted strings, one for each moment in the recurrence.
     */
    infix fun format(block: VerdandiFormatScope.() -> VerdandiFormatPattern): List<String> {
        return map { VerdandiFormatter(it.component, block) }
    }

    /**
     * Filters the recurrence moments based on the provided [predicate].
     *
     * @param predicate A lambda that defines the filtering condition.
     * @return A new [VerdandiRecurrenceMoments] instance with moments that satisfy the predicate.
     */
    infix fun filter(predicate: (VerdandiMoment) -> Boolean): VerdandiRecurrenceMoments {
        return copy(moments = moments.filter(predicate))
    }

    /**
     * Checks if the given [moment] matches the recurrence rule and is not excluded.
     *
     * @param moment The [VerdandiMoment] to check against the recurrence.
     * @return `true` if the moment matches the recurrence and is not excluded, `false` otherwise.
     */
    fun matches(moment: VerdandiMoment): Boolean {
        if (exclusions.contains(moment)) {
            return false
        }

        val activeRule = rule ?: return contains(moment)

        return RecurrenceMatcher.matches(moment, startMoment, activeRule)
    }

    /**
     * Returns a new [VerdandiRecurrenceMoments] instance with the specified [moments] excluded.
     *
     * @param moments The [VerdandiMoment] instances to exclude from the recurrence.
     * @return A new [VerdandiRecurrenceMoments] without the excluded moments.
     */
    fun exclude(vararg moments: VerdandiMoment): VerdandiRecurrenceMoments {
        val updatedExclusions = exclusions + moments.toSet()

        return VerdandiRecurrenceMoments(
            moments = this.moments.filterNot { updatedExclusions.contains(it) },
            startMoment = startMoment,
            endMoment = endMoment,
            rule = rule,
            exclusions = updatedExclusions
        )
    }
}
