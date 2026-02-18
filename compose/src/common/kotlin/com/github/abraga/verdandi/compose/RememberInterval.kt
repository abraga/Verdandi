package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * Remembers a [VerdandiInterval] built from [start]..[end].
 *
 * The interval is recomputed only when the epoch milliseconds of either
 * bound change:
 *
 * ```kotlin
 * val interval = rememberInterval(startMoment, endMoment)
 * if (interval.contains(selectedMoment)) { /* ... */ }
 * ```
 *
 * This variant does **not** survive configuration changes. For saveable
 * persistence use [rememberSavableInterval].
 *
 * @param start the inclusive lower bound.
 * @param end   the exclusive upper bound.
 * @return the remembered [VerdandiInterval].
 */
@Composable
fun rememberInterval(
    start: VerdandiMoment,
    end: VerdandiMoment
): VerdandiInterval {
    return remember(start.inMilliseconds, end.inMilliseconds) { start..end }
}
