package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Produces a [State]<[VerdandiMoment]> that refreshes automatically at the
 * given [refreshInterval] (default: 1 second).
 *
 * The returned state emits [Verdandi.now] on every tick, which triggers
 * recomposition for any composable that reads it:
 *
 * ```kotlin
 * val now by rememberCurrentMoment()
 * Text(now format { iso.time })
 * ```
 *
 * @param refreshInterval how often to re-query the current time.
 *   Defaults to `1.seconds`.
 * @return a [State] whose [State.value] is always the latest [VerdandiMoment].
 */
@Composable
fun rememberCurrentMoment(refreshInterval: Duration = 1.seconds): State<VerdandiMoment> {
    return produceState(initialValue = Verdandi.now(), refreshInterval) {
        while (true) {
            delay(refreshInterval)
            value = Verdandi.now()
        }
    }
}
