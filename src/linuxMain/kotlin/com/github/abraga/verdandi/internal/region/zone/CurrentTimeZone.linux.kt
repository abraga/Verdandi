@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalForeignApi::class)

package com.github.abraga.verdandi.internal.region.zone

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.localtime_r
import platform.posix.time
import platform.posix.tm

internal actual class CurrentTimeZone actual constructor() {

    actual val id: String
        get() = com.github.abraga.verdandi.internal.timezone.PlatformTimeZone.defaultId()

    actual val offsetMillis: Long
        get() = withCurrentTm { it.tm_gmtoff * MILLIS_PER_SECOND }

    actual val displayName: String
        get() = withCurrentTm { it.tm_zone?.toKString() ?: id }

    actual val abbreviation: String
        get() = withCurrentTm { it.tm_zone?.toKString() ?: "" }

    actual val isDaylightSavingTime: Boolean
        get() = withCurrentTm { it.tm_isdst > 0 }

    actual val daylightSavingTimeOffsetMillis: Long
        get() {
            if (!isDaylightSavingTime) return 0L

            val nowSeconds = time(null)
            val currentOffset = localTimeGmtOff(nowSeconds)
            val stdOffset = localTimeGmtOff(nowSeconds - SECONDS_PER_HALF_YEAR)

            return (currentOffset - stdOffset) * MILLIS_PER_SECOND
        }

    actual val totalOffsetMillis: Long
        get() = offsetMillis + daylightSavingTimeOffsetMillis

    private fun localTimeGmtOff(epochSeconds: Long): Long = memScoped {
        val localTm = alloc<tm>()

        longArrayOf(epochSeconds).usePinned { pinned ->
            localtime_r(pinned.addressOf(0).reinterpret(), localTm.ptr)
        }

        localTm.tm_gmtoff
    }

    private inline fun <T> withCurrentTm(block: (tm) -> T): T = memScoped {
        val nowSeconds = time(null)
        val localTm = alloc<tm>()

        longArrayOf(nowSeconds).usePinned { pinned ->
            localtime_r(pinned.addressOf(0).reinterpret(), localTm.ptr)
        }

        block(localTm)
    }

    private companion object {

        private const val MILLIS_PER_SECOND = 1000L
        private const val SECONDS_PER_HALF_YEAR = 182L * 24 * 60 * 60
    }
}
