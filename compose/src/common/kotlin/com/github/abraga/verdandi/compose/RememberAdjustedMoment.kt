package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.adjust.VerdandiAdjustScope

/**
 * Remembers a [VerdandiMoment] produced by applying an [adjustment] DSL block
 * to the given [moment].
 *
 * The adjustment is re-evaluated only when the source [moment] changes
 * (compared by epoch milliseconds):
 *
 * ```kotlin
 * val startOfDay = rememberAdjustedMoment(selectedDate) {
 *     at startOf day
 * }
 *
 * val tomorrow = rememberAdjustedMoment(Verdandi.now()) {
 *     add one day
 * }
 * ```
 *
 * @param moment     the base moment to adjust.
 * @param adjustment the [VerdandiAdjustScope] configuration block.
 * @return the adjusted and remembered [VerdandiMoment].
 */
@Composable
fun rememberAdjustedMoment(
    moment: VerdandiMoment,
    adjustment: VerdandiAdjustScope.() -> Unit
): VerdandiMoment {
    return remember(moment.inMilliseconds) {
        moment adjust adjustment
    }
}
