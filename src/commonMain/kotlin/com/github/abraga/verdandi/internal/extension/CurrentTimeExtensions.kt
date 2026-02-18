package com.github.abraga.verdandi.internal.extension

import com.github.abraga.verdandi.internal.core.VerdandiClock
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import com.github.abraga.verdandi.internal.timezone.PlatformTimeZone
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext

private var cachedTimeZoneContext: TimeZoneContext? = null
private var cachedTimeZoneId: String? = null

internal val timeZoneContext: TimeZoneContext
    get() {
        val timeZoneId = PlatformTimeZone.defaultId()
        val cached = cachedTimeZoneContext

        if (cached != null && cachedTimeZoneId == timeZoneId) {
            return cached
        }

        val context = TimeZoneContext.createDefault()

        cachedTimeZoneContext = context
        cachedTimeZoneId = timeZoneId

        return context
    }

internal fun currentTimeMillis(): Long {
    return VerdandiClock.now().toUtcEpochMillis()
}

internal fun currentLocale(): VerdandiLocale {
    return timeZoneContext.locale
}
