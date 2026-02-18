@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.timezone

import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap

internal actual object PlatformTimeZone {

    private val cache = ConcurrentHashMap<String, TimeZone>()

    actual fun offsetMillisAt(timeZoneId: String, epochMillis: Long): Long {
        val timeZone = cache.getOrPut(timeZoneId) { TimeZone.getTimeZone(timeZoneId) }

        return timeZone.getOffset(epochMillis).toLong()
    }

    actual fun isTimeZoneIdValid(timeZoneId: String): Boolean {
        return availableIds().contains(timeZoneId)
    }

    actual fun defaultId(): String {
        return TimeZone.getDefault().id
    }

    actual fun availableIds(): Set<String> {
        return TimeZone.getAvailableIDs().toSet()
    }
}
