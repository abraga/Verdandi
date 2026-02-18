@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.zone

import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.NSTimeZoneNameStyle
import platform.Foundation.abbreviation
import platform.Foundation.currentLocale
import platform.Foundation.daylightSavingTimeOffset
import platform.Foundation.isDaylightSavingTime
import platform.Foundation.localTimeZone
import platform.Foundation.localizedName
import platform.Foundation.secondsFromGMT

internal actual class CurrentTimeZone actual constructor() {

    private val timeZone: NSTimeZone
        get() = NSTimeZone.localTimeZone

    actual val id: String
        get() = timeZone.name

    actual val offsetMillis: Long
        get() = timeZone.secondsFromGMT * 1000L

    actual val displayName: String
        get() {
            return timeZone.localizedName(
                style = NSTimeZoneNameStyle.NSTimeZoneNameStyleStandard,
                locale = NSLocale.currentLocale
            ) ?: timeZone.name
        }

    actual val abbreviation: String
        get() = timeZone.abbreviation ?: ""

    actual val isDaylightSavingTime: Boolean
        get() = timeZone.isDaylightSavingTime()

    actual val daylightSavingTimeOffsetMillis: Long
        get() = (timeZone.daylightSavingTimeOffset * 1000L).toLong()

    actual val totalOffsetMillis: Long
        get() = offsetMillis + daylightSavingTimeOffsetMillis
}
