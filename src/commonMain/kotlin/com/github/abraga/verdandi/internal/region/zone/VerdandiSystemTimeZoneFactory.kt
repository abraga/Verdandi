package com.github.abraga.verdandi.internal.region.zone

internal class VerdandiSystemTimeZoneFactory {

    fun current(): VerdandiSystemTimeZone {
        val currentTimeZone = CurrentTimeZone()

        return VerdandiSystemTimeZone(
            id = currentTimeZone.id,
            displayName = currentTimeZone.displayName,
            abbreviation = currentTimeZone.abbreviation,
            offset = VerdandiSystemTimeZone.Offset(
                millis = currentTimeZone.offsetMillis,
                totalMillis = currentTimeZone.totalOffsetMillis
            ),
            daylightSaving = VerdandiSystemTimeZone.DaylightSaving(
                isActive = currentTimeZone.isDaylightSavingTime,
                offsetMillis = currentTimeZone.daylightSavingTimeOffsetMillis
            )
        )
    }
}
