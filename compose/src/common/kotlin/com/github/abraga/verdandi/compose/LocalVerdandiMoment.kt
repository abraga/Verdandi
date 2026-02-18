package com.github.abraga.verdandi.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * [ProvidableCompositionLocal] that exposes a shared [VerdandiMoment] to the
 * composition tree.
 *
 * The default value is [Verdandi.now] captured once at first read. Override it
 * with `CompositionLocalProvider` to inject a fixed moment for previews or
 * testing:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalVerdandiMoment provides fixedMoment) {
 *     val now = LocalVerdandiMoment.current
 * }
 * ```
 *
 * For a self-refreshing clock, combine with [rememberCurrentMoment]:
 *
 * ```kotlin
 * val live = rememberCurrentMoment()
 * CompositionLocalProvider(LocalVerdandiMoment provides live.value) {
 *     Text(LocalVerdandiMoment.current format { iso.time })
 * }
 * ```
 */
val LocalVerdandiMoment: ProvidableCompositionLocal<VerdandiMoment> = staticCompositionLocalOf {
    Verdandi.now()
}
