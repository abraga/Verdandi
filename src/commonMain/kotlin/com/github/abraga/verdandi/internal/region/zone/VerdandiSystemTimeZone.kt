package com.github.abraga.verdandi.internal.region.zone

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@ConsistentCopyVisibility
internal data class VerdandiSystemTimeZone internal constructor(
    val id: String,
    val displayName: String,
    val abbreviation: String,
    val offset: Offset,
    val daylightSaving: DaylightSaving
) {

    @Serializable
    @ConsistentCopyVisibility
    data class Offset internal constructor(
        val millis: Long,
        val totalMillis: Long
    ) {

        @Transient
        val inHours: Int = (totalMillis / MILLIS_PER_HOUR).toInt()

        private companion object {

            private const val MILLIS_PER_HOUR = 3_600_000L
        }
    }

    @Serializable
    @ConsistentCopyVisibility
    data class DaylightSaving internal constructor(
        val isActive: Boolean,
        val offsetMillis: Long
    )
}
