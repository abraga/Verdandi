package com.github.abraga.verdandi.api.model.timezone

import com.github.abraga.verdandi.internal.extension.verdandiRequire
import com.github.abraga.verdandi.internal.timezone.PlatformTimeZone
import com.github.abraga.verdandi.api.model.VerdandiMoment
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Type-safe wrapper around an IANA timezone identifier.
 *
 * Use [VerdandiTimeZone] to project a [VerdandiMoment][VerdandiMoment]
 * into a specific timezone without changing the underlying instant:
 *
 * ```kotlin
 * val utc = Verdandi.from("2025-06-15T12:00:00Z")
 * val tokyo = utc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")
 * val ny = utc inTimeZone VerdandiTimeZone.of("America/New_York")
 * ```
 *
 * @property id the IANA timezone identifier (e.g. `"America/Sao_Paulo"`, `"UTC"`).
 */
@JvmInline
@Serializable
value class VerdandiTimeZone private constructor(val id: String) {

    companion object {

        /** UTC timezone constant. */
        val UTC: VerdandiTimeZone = VerdandiTimeZone("UTC")

        /**
         * Creates a [VerdandiTimeZone] from an IANA timezone identifier.
         *
         * @param id the IANA timezone identifier (e.g. `"America/New_York"`, `"Asia/Tokyo"`).
         * @return a validated [VerdandiTimeZone] instance.
         * @throws com.github.abraga.verdandi.api.exception.VerdandiValidationException
         *   if [id] is not recognized by the platform.
         */
        fun of(id: String): VerdandiTimeZone {
            verdandiRequire(PlatformTimeZone.isTimeZoneIdValid(id)) {
                "Invalid time zone ID: $id"
            }

            return VerdandiTimeZone(id)
        }

        /**
         * Returns all available timezone identifiers as a set.
         */
        fun allTimeZoneIds(): Set<String> {
            return PlatformTimeZone.availableIds()
        }
    }
}
