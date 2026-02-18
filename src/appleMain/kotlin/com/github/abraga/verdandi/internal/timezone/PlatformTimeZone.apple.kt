@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.timezone

import platform.Foundation.NSDate
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.knownTimeZoneNames
import platform.Foundation.localTimeZone
import platform.Foundation.timeZoneWithName

internal actual object PlatformTimeZone {

    private const val MILLIS_PER_SECOND = 1000L

    private val cache = HashMap<String, NSTimeZone>()

    actual fun offsetMillisAt(timeZoneId: String, epochMillis: Long): Long {
        val nsTimeZone =
                cache.getOrPut(timeZoneId) { NSTimeZone.timeZoneWithName(timeZoneId) ?: return 0L }
        val epochSeconds = epochMillis.toDouble() / MILLIS_PER_SECOND
        val nsDate = NSDate.dateWithTimeIntervalSince1970(epochSeconds)

        return nsTimeZone.secondsFromGMTForDate(nsDate) * MILLIS_PER_SECOND
    }

    actual fun isTimeZoneIdValid(timeZoneId: String): Boolean {
        return NSTimeZone.timeZoneWithName(timeZoneId) != null
    }

    actual fun defaultId(): String {
        return NSTimeZone.localTimeZone.name
    }

    actual fun availableIds(): Set<String> {
        return NSTimeZone.knownTimeZoneNames.mapNotNull { it as? String }.toSet()
    }
}
