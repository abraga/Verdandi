package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * Remembers the current instant, captured once on first composition.
 *
 * The returned [VerdandiMoment] does **not** update over time. For a live
 * clock, use [rememberCurrentMoment] instead.
 *
 * ```kotlin
 * val snapshot = rememberMoment()   // stable across recompositions
 * ```
 *
 * Does **not** survive configuration changes. For saveable persistence
 * use [rememberSavableMoment].
 *
 * @return a [VerdandiMoment] representing [Verdandi.now] at first composition.
 */
@Composable
fun rememberMoment(): VerdandiMoment {
    return remember { Verdandi.now() }
}

/**
 * Remembers a [VerdandiMoment] built from calendar components.
 *
 * The value is recomputed whenever any parameter changes:
 *
 * ```kotlin
 * val birthday = rememberMoment(year = 1990, month = 5, day = 20)
 * ```
 *
 * Does **not** survive configuration changes. For saveable persistence
 * use [rememberSavableMoment].
 *
 * @param year        calendar year (e.g. 2025).
 * @param month       month of year (1–12).
 * @param day         day of month (1–31).
 * @param hour        hour of day (0–23). Defaults to `0`.
 * @param minute      minute of hour (0–59). Defaults to `0`.
 * @param second      second of minute (0–59). Defaults to `0`.
 * @param millisecond millisecond of second (0–999). Defaults to `0`.
 * @return the remembered [VerdandiMoment].
 */
@Composable
fun rememberMoment(
    year: Int,
    month: Int,
    day: Int,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    millisecond: Int = 0
): VerdandiMoment {
    return remember(year, month, day, hour, minute, second, millisecond) {
        Verdandi.at(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            second = second,
            millisecond = millisecond
        )
    }
}

/**
 * Remembers a [VerdandiMoment] from epoch [milliseconds].
 *
 * ```kotlin
 * val moment = rememberMoment(1750000000000L)
 * ```
 *
 * Does **not** survive configuration changes. For saveable persistence
 * use [rememberSavableMoment].
 *
 * @param milliseconds epoch milliseconds since `1970-01-01T00:00:00Z`.
 * @return the remembered [VerdandiMoment].
 */
@Composable
fun rememberMoment(milliseconds: Long): VerdandiMoment {
    return remember(milliseconds) {
        Verdandi.from(milliseconds)
    }
}

/**
 * Remembers a [VerdandiMoment] parsed from an ISO-8601 [timestamp].
 *
 * ```kotlin
 * val moment = rememberMoment("2025-06-15T14:30:00Z")
 * ```
 *
 * Does **not** survive configuration changes. For saveable persistence
 * use [rememberSavableMoment].
 *
 * @param timestamp an ISO-8601 string (e.g. `"2025-06-15T14:30:00Z"`).
 * @return the remembered [VerdandiMoment].
 */
@Composable
fun rememberMoment(timestamp: String): VerdandiMoment {
    return remember(timestamp) {
        Verdandi.from(timestamp)
    }
}
