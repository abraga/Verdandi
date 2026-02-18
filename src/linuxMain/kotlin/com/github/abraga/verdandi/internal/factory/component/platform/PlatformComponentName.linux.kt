@file:OptIn(ExperimentalForeignApi::class)

package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.LC_TIME
import platform.posix.mktime
import platform.posix.setlocale
import platform.posix.strftime
import platform.posix.tm

internal actual fun Month.getMonthName(locale: VerdandiLocale): String {
    return formatWithLocale(locale, "%B") { localTm ->
        localTm.tm_year = 100
        localTm.tm_mon = value - 1
        localTm.tm_mday = 1
    }
}

internal actual fun DayOfWeek.getDayOfWeekName(locale: VerdandiLocale): String {
    return formatWithLocale(locale, "%A") { localTm ->
        localTm.tm_year = 100
        localTm.tm_mon = 0
        localTm.tm_mday = 2 + value
        mktime(localTm.ptr)
    }
}

internal actual fun getAmPmName(locale: VerdandiLocale): Pair<String, String> {
    val am = formatWithLocale(locale, "%p") { localTm ->
        localTm.tm_hour = 6
    }
    val pm = formatWithLocale(locale, "%p") { localTm ->
        localTm.tm_hour = 18
    }

    return (am.ifEmpty { "AM" }) to (pm.ifEmpty { "PM" })
}

private inline fun formatWithLocale(
    locale: VerdandiLocale,
    format: String,
    configure: (tm) -> Unit
): String = memScoped {
    val localeTag = locale.identifier.replace('-', '_')

    val prevLocale = setlocale(LC_TIME, null)?.toKString()
    setlocale(LC_TIME, localeTag)
        ?: setlocale(LC_TIME, "$localeTag.UTF-8")
        ?: setlocale(LC_TIME, "")

    try {
        val localTm = alloc<tm>()
        configure(localTm)

        val buf = allocArray<ByteVar>(256)
        val written = strftime(buf, 256u, format, localTm.ptr)

        if (written > 0u) buf.toKString() else ""
    } finally {
        if (prevLocale != null) {
            setlocale(LC_TIME, prevLocale)
        }
    }
}
