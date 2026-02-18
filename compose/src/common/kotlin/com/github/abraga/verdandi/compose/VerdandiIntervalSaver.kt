package com.github.abraga.verdandi.compose

import androidx.compose.runtime.saveable.Saver
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiInterval

/**
 * [Saver] that persists a [VerdandiInterval] across configuration changes
 * by storing the start and end epoch milliseconds as a [LongArray].
 *
 * ```kotlin
 * val interval = rememberSaveable(saver = VerdandiIntervalSaver) {
 *     Verdandi.interval { last thirty days }
 * }
 * ```
 *
 * Timezone context is not preserved — see [VerdandiMomentSaver] for rationale.
 */
val VerdandiIntervalSaver: Saver<VerdandiInterval, LongArray> = Saver(
    save = { interval ->
        longArrayOf(interval.start.inMilliseconds, interval.end.inMilliseconds)
    },
    restore = { array ->
        Verdandi.interval(array[0], array[1])
    }
)
