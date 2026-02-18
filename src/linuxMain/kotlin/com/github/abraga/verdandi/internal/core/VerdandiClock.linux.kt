@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.CLOCK_REALTIME
import platform.posix.clock_gettime
import platform.posix.timespec

@OptIn(ExperimentalForeignApi::class)
internal actual object VerdandiClock {

    private const val MILLIS_PER_SECOND = 1000L
    private const val NANOS_PER_MILLI = 1_000_000L

    actual fun now(): VerdandiInstant {
        val epochMillis = memScoped {
            val ts = alloc<timespec>()
            clock_gettime(CLOCK_REALTIME, ts.ptr)

            ts.tv_sec * MILLIS_PER_SECOND + ts.tv_nsec / NANOS_PER_MILLI
        }

        return VerdandiInstant.fromUtcEpochMillis(epochMillis)
    }
}
