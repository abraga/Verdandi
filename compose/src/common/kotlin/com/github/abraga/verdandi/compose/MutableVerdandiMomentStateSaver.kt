package com.github.abraga.verdandi.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * [Saver] that persists a [MutableState]<[VerdandiMoment]> across configuration
 * changes by storing the current epoch milliseconds.
 *
 * This is the backing saver for [rememberMutableMomentState]. You can also
 * use it directly:
 *
 * ```kotlin
 * val state = rememberSaveable(saver = MutableVerdandiMomentStateSaver) {
 *     mutableStateOf(Verdandi.now())
 * }
 * state.value = state.value adjust { add one day }
 * ```
 *
 * Timezone context is not preserved — see [VerdandiMomentSaver] for rationale.
 */
val MutableVerdandiMomentStateSaver: Saver<MutableState<VerdandiMoment>, Long> = Saver(
    save = { state -> state.value.inMilliseconds },
    restore = { milliseconds -> mutableStateOf(Verdandi.from(milliseconds)) }
)
