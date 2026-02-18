@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.zone

import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal actual class CurrentTimeZone actual constructor() {

    private val timeZone: TimeZone
        get() = TimeZone.getDefault()

    private val currentDate: Date
        get() = Date()

    actual val id: String
        get() = timeZone.id

    actual val offsetMillis: Long
        get() = timeZone.rawOffset.toLong()

    actual val displayName: String
        get() = timeZone.getDisplayName(false, TimeZone.LONG, Locale.getDefault())

    actual val abbreviation: String
        get() = timeZone.getDisplayName(
            timeZone.inDaylightTime(currentDate),
            TimeZone.SHORT,
            Locale.getDefault()
        )

    actual val isDaylightSavingTime: Boolean
        get() = timeZone.inDaylightTime(currentDate)

    actual val daylightSavingTimeOffsetMillis: Long
        get() {
            return if (isDaylightSavingTime) {
                timeZone.dstSavings.toLong()
            } else {
                0L
            }
        }

    actual val totalOffsetMillis: Long
        get() = offsetMillis + daylightSavingTimeOffsetMillis
}
