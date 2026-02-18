package com.github.abraga.verdandi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.model.VerdandiMoment

/**
 * Remembers the current instant and survives configuration changes.
 *
 * The moment is captured once on first composition (like [rememberMoment])
 * and restored from saved state on recreation:
 *
 * ```kotlin
 * val snapshot = rememberSavableMoment()
 * ```
 *
 * @return a [VerdandiMoment] representing [Verdandi.now] at first composition,
 *   restored from saved state on subsequent recreations.
 */
@Composable
fun rememberSavableMoment(): VerdandiMoment {
    return rememberSaveable(saver = VerdandiMomentSaver) {
        Verdandi.now()
    }
}

/**
 * Remembers a [VerdandiMoment] built from calendar components and survives
 * configuration changes.
 *
 * The value is recomputed when any parameter changes, and automatically
 * saved/restored across configuration changes via [VerdandiMomentSaver]:
 *
 * ```kotlin
 * val birthday = rememberSavableMoment(year = 1990, month = 5, day = 20)
 * ```
 *
 * @param year        calendar year (e.g. 2025).
 * @param month       month of year (1–12).
 * @param day         day of month (1–31).
 * @param hour        hour of day (0–23). Defaults to `0`.
 * @param minute      minute of hour (0–59). Defaults to `0`.
 * @param second      second of minute (0–59). Defaults to `0`.
 * @param millisecond millisecond of second (0–999). Defaults to `0`.
 * @return the remembered and saveable [VerdandiMoment].
 */
@Composable
fun rememberSavableMoment(
    year: Int,
    month: Int,
    day: Int,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    millisecond: Int = 0
): VerdandiMoment {
    return rememberSaveable(
        year,
        month,
        day,
        hour,
        minute,
        second,
        millisecond,
        saver = VerdandiMomentSaver
    ) {
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
 * Remembers a [VerdandiMoment] from epoch [milliseconds] and survives
 * configuration changes.
 *
 * ```kotlin
 * val moment = rememberSavableMoment(1750000000000L)
 * ```
 *
 * @param milliseconds epoch milliseconds since `1970-01-01T00:00:00Z`.
 * @return the remembered and saveable [VerdandiMoment].
 */
@Composable
fun rememberSavableMoment(milliseconds: Long): VerdandiMoment {
    return rememberSaveable(milliseconds, saver = VerdandiMomentSaver) {
        Verdandi.from(milliseconds)
    }
}
