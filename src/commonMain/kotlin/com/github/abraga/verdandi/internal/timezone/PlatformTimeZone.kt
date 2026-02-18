@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.timezone

internal expect object PlatformTimeZone {

    fun offsetMillisAt(timeZoneId: String, epochMillis: Long): Long

    fun isTimeZoneIdValid(timeZoneId: String): Boolean

    fun defaultId(): String

    fun availableIds(): Set<String>
}
