@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.zone

internal expect class CurrentTimeZone() {

    val id: String

    val offsetMillis: Long

    val displayName: String

    val abbreviation: String

    val isDaylightSavingTime: Boolean

    val daylightSavingTimeOffsetMillis: Long

    val totalOffsetMillis: Long
}
