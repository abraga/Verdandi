package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * Remembers a [MutableState]<[VerdandiMoment]> that survives configuration
 * changes. Useful for two-way binding with date pickers or similar UI
 * elements.
 *
 * ```kotlin
 * val (moment, setMoment) = rememberMutableMomentState()
 * Text(moment format { iso.date })
 * Button(onClick = { setMoment(moment adjust { add one day }) }) {
 *     Text("Next day")
 * }
 * ```
 *
 * State is persisted via [MutableVerdandiMomentStateSaver] (epoch milliseconds).
 *
 * @param initialMoment the starting value. Defaults to [Verdandi.now].
 * @return a [MutableState] wrapping the current [VerdandiMoment].
 */
@Composable
fun rememberMutableMomentState(
    initialMoment: VerdandiMoment = Verdandi.now()
): MutableState<VerdandiMoment> {
    return rememberSaveable(saver = MutableVerdandiMomentStateSaver) {
        mutableStateOf(initialMoment)
    }
}

/**
 * Remembers a [MutableState]<[VerdandiMoment]> initialized from calendar
 * components and survives configuration changes.
 *
 * The initial value is rebuilt when any component parameter changes:
 *
 * ```kotlin
 * val state = rememberMutableMomentState(year = 2025, month = 6, day = 15)
 * state.value = state.value adjust { add one day }
 * ```
 *
 * @param year        calendar year (e.g. 2025).
 * @param month       month of year (1–12).
 * @param day         day of month (1–31).
 * @param hour        hour of day (0–23). Defaults to `0`.
 * @param minute      minute of hour (0–59). Defaults to `0`.
 * @param second      second of minute (0–59). Defaults to `0`.
 * @param millisecond millisecond of second (0–999). Defaults to `0`.
 * @return a [MutableState] wrapping the current [VerdandiMoment].
 */
@Composable
fun rememberMutableMomentState(
    year: Int,
    month: Int,
    day: Int,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    millisecond: Int = 0
): MutableState<VerdandiMoment> {
    return rememberSaveable(
        year, month, day, hour, minute, second, millisecond,
        saver = MutableVerdandiMomentStateSaver
    ) {
        mutableStateOf(
            Verdandi.at(
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute,
                second = second,
                millisecond = millisecond
            )
        )
    }
}
