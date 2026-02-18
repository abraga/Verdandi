@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.core

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual object VerdandiClock {

    actual fun now(): VerdandiInstant {
        val epochSeconds = NSDate().timeIntervalSince1970
        val epochMillis = (epochSeconds * 1000).toLong()

        return VerdandiInstant.fromUtcEpochMillis(epochMillis)
    }
}
