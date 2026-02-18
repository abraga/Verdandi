package com.github.abraga.verdandi.compose

import androidx.compose.runtime.saveable.Saver
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * [Saver] that persists a [VerdandiMoment] across configuration changes
 * by storing its epoch milliseconds.
 *
 * ```kotlin
 * val moment = rememberSaveable(saver = VerdandiMomentSaver) {
 *     Verdandi.now()
 * }
 * ```
 *
 * The saved value is a single [Long] (epoch milliseconds). Timezone context
 * is not preserved because it is presentation-layer metadata — reattach it
 * after restoration with [VerdandiMoment.inTimeZone] if needed.
 */
val VerdandiMomentSaver: Saver<VerdandiMoment, Long> = Saver(
    save = { moment -> moment.inMilliseconds },
    restore = { milliseconds -> Verdandi.from(milliseconds) }
)
