package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.config.VerdandiConfiguration
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun VerdandiMoment.toInstant(): Instant {
    return Instant.ofEpochMilli(inMilliseconds)
}

fun VerdandiMoment.toZonedDateTime(): ZonedDateTime {
    return toInstant().atZone(resolveZoneId())
}

fun VerdandiMoment.toLocalDateTime(): LocalDateTime {
    return toZonedDateTime().toLocalDateTime()
}

fun VerdandiMoment.toLocalDate(): LocalDate {
    return toZonedDateTime().toLocalDate()
}

fun VerdandiMoment.toLocalTime(): LocalTime {
    return toZonedDateTime().toLocalTime()
}

fun VerdandiMoment.toOffsetDateTime(): OffsetDateTime {
    return toZonedDateTime().toOffsetDateTime()
}

fun Instant.toVerdandiMoment(): VerdandiMoment {
    return Verdandi.from(toEpochMilli())
}

fun ZonedDateTime.toVerdandiMoment(): VerdandiMoment {
    return Verdandi.from(toInstant().toEpochMilli()) inTimeZone zone.toVerdandiTimeZone()
}

fun OffsetDateTime.toVerdandiMoment(): VerdandiMoment {
    return toInstant().toVerdandiMoment()
}

fun LocalDateTime.toVerdandiMoment(zone: ZoneId = ZoneId.systemDefault()): VerdandiMoment {
    return atZone(zone).toVerdandiMoment()
}

fun LocalDate.toVerdandiMoment(zone: ZoneId = ZoneId.systemDefault()): VerdandiMoment {
    return atStartOfDay(zone).toVerdandiMoment()
}

fun VerdandiTimeZone.toZoneId(): ZoneId {
    return ZoneId.of(id)
}

fun ZoneId.toVerdandiTimeZone(): VerdandiTimeZone {
    return VerdandiTimeZone.of(id)
}

private fun VerdandiMoment.resolveZoneId(): ZoneId {
    if (timeZoneId != null) {
        return ZoneId.of(timeZoneId)
    }

    val defaultTimeZone = VerdandiConfiguration.get().defaultTimeZone

    if (defaultTimeZone != null) {
        return ZoneId.of(defaultTimeZone.id)
    }

    return ZoneId.systemDefault()
}
