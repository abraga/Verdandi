package com.github.abraga.verdandi.internal.timezone

import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocaleFactory

internal class TimeZoneContext private constructor(
    private val lazyLocale: Lazy<VerdandiLocale>,
    val timeZoneId: String
) {

    val locale: VerdandiLocale
        get() = lazyLocale.value

    fun withTimeZoneId(newTimeZoneId: String): TimeZoneContext {
        return TimeZoneContext(lazyLocale, newTimeZoneId)
    }

    fun offsetMillisAt(epochMillis: Long): Long {
        return PlatformTimeZone.offsetMillisAt(timeZoneId, epochMillis)
    }

    fun toLocalDateTime(epochMillis: Long): VerdandiLocalDateTime {
        val offset = offsetMillisAt(epochMillis)
        val adjustedMillis = epochMillis + offset

        return VerdandiLocalDateTime.fromEpochMillisecondsUtc(adjustedMillis)
    }

    fun toEpochMillis(localDateTime: VerdandiLocalDateTime): Long {
        val utcGuess = localDateTime.toEpochMillisecondsUtc()
        val offsetAtGuess = offsetMillisAt(utcGuess)
        val firstEstimate = utcGuess - offsetAtGuess
        val offsetAtEstimate = offsetMillisAt(firstEstimate)

        return utcGuess - offsetAtEstimate
    }

    companion object {

        fun createDefault(): TimeZoneContext {
            return TimeZoneContext(
                lazyLocale = lazy { VerdandiLocaleFactory().current() },
                timeZoneId = PlatformTimeZone.defaultId()
            )
        }

        fun fromTimeZoneId(timeZoneId: String): TimeZoneContext {
            return TimeZoneContext(
                lazyLocale = lazy { VerdandiLocaleFactory().current() },
                timeZoneId = timeZoneId
            )
        }
    }
}
