package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * Remembers a [VerdandiInterval] from two [VerdandiMoment] bounds and survives
 * configuration changes.
 *
 * The interval is recomputed when either bound changes (by epoch
 * milliseconds) and automatically saved/restored via [VerdandiIntervalSaver]:
 *
 * ```kotlin
 * val interval = rememberSavableInterval(startMoment, endMoment)
 * ```
 *
 * @param start the inclusive lower bound.
 * @param end   the exclusive upper bound.
 * @return the remembered and saveable [VerdandiInterval].
 */
@Composable
fun rememberSavableInterval(
    start: VerdandiMoment,
    end: VerdandiMoment
): VerdandiInterval {
    return rememberSaveable(start.inMilliseconds, end.inMilliseconds, saver = VerdandiIntervalSaver) {
        start..end
    }
}

/**
 * Remembers a [VerdandiInterval] from raw epoch milliseconds and survives
 * configuration changes.
 *
 * ```kotlin
 * val interval = rememberSavableInterval(
 *     startMilliseconds = 1750000000000L,
 *     endMilliseconds = 1750086400000L
 * )
 * ```
 *
 * @param startMilliseconds epoch milliseconds for the inclusive lower bound.
 * @param endMilliseconds   epoch milliseconds for the exclusive upper bound.
 * @return the remembered and saveable [VerdandiInterval].
 */
@Composable
fun rememberSavableInterval(
    startMilliseconds: Long,
    endMilliseconds: Long
): VerdandiInterval {
    return rememberSaveable(startMilliseconds, endMilliseconds, saver = VerdandiIntervalSaver) {
        Verdandi.interval(startMilliseconds, endMilliseconds)
    }
}
