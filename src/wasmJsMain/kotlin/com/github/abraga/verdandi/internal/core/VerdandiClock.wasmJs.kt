@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.core

internal actual object VerdandiClock {

    actual fun now(): VerdandiInstant {
        return VerdandiInstant.fromUtcEpochMillis(jsDateNow().toLong())
    }
}

@JsFun("() => Date.now()")
private external fun jsDateNow(): Double
